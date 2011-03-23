package eu.scy.agents.keywords;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Document;
import de.fhg.iais.kd.tm.obwious.identifiers.WikiFeatures;
import de.fhg.iais.kd.tm.obwious.identifiers.WikiParameters;
import de.fhg.iais.kd.tm.obwious.identifiers.WikiValues;
import de.fhg.iais.kd.tm.obwious.operator.ObjectIdentifiers;
import de.fhg.iais.kd.tm.obwious.operator.Operator;
import de.fhg.iais.kd.tm.obwious.type.Container;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractRequestAgent;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.keywords.workflow.ExtractKeyphrasesWorkflow;
import eu.scy.agents.util.Utilities;

/**
 * (ExtractKeyphrases:String, query:String, <QueryId>:String, <Text>:String)->
 * (ExtractKeyphrases:String, response:String, <QueryId>:String,
 * <Keywords>:String)->
 * 
 * @author Jörg Kindermann
 */
public class ExtractKeyphrasesAgent extends AbstractRequestAgent {

	public static final String EXTRACT_KEYPHRASES = "ExtractKeyphrases";

	public static final String NAME = ExtractKeyphrasesAgent.class.getName();

	private Tuple activationTuple;

	private int listenerId;

	private static final Logger logger = Logger
			.getLogger(ExtractKeyphrasesAgent.class.getName());

	// private PersistentStorage storage = null;
	public ExtractKeyphrasesAgent(Map<String, Object> params) {
		super(NAME, params);
		if (params.containsKey(AgentProtocol.TS_HOST)) {
			host = (String) params.get(AgentProtocol.TS_HOST);
		}
		if (params.containsKey(AgentProtocol.TS_PORT)) {
			port = (Integer) params.get(AgentProtocol.TS_PORT);
		}
		activationTuple = new Tuple(EXTRACT_KEYPHRASES, AgentProtocol.QUERY,
				String.class, String.class, String.class, String.class);
		// storage = new PersistentStorage(host, port);
		try {
			listenerId = getCommandSpace().eventRegister(Command.WRITE,
					activationTuple, this, true);
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}

	private Properties initWorkflowProperties() {
		WikiFeatures.addFeatures();

		Properties workflowProperties = new Properties();
		workflowProperties.setProperty(WikiParameters.DIRECTORY,
				"mission1_texts/English");

		/* PROPERTIES ------------------------ */

		/**
		 * file type for documents (WikiValues.FILE_TYPE_CAS or
		 * WikiValues.FILE_TYPE_TXT)
		 */
		workflowProperties.setProperty(WikiParameters.FILE_TYPE,
				WikiValues.FILE_TYPE_TXT);

		/** minimum length that a token must have */
		workflowProperties.setProperty(WikiParameters.MIN_TOKEN_LENGTH,
				new Integer(1).toString());
		/** indicator of whether the tokens should be transformed to lower case */
		workflowProperties.setProperty(WikiParameters.TO_LOWER,
				Boolean.FALSE.toString());

		/** minimum number of tokens that a n-gram must have */
		workflowProperties.setProperty(WikiParameters.MIN_NGRAM_TOKENS,
				new Integer(2).toString());
		/** maximum number of tokens that a n-gram can have */
		workflowProperties.setProperty(WikiParameters.MAX_NGRAM_TOKENS,
				new Integer(5).toString());
		/** minimum number of appearances in the document a n-gram must have */
		workflowProperties.setProperty(WikiParameters.MIN_FREQUENCY,
				new Integer(2).toString());
		/**
		 * indicator of whether the tokens of a n-gram might be separated by a
		 * delimiter (like . or ,)
		 */
		workflowProperties.setProperty(WikiParameters.IGNORE_DELIMITERS,
				Boolean.FALSE.toString());
		/**
		 * indicator of whether the n-gram might begin or end with a stopword
		 * token
		 */
		workflowProperties.setProperty(WikiParameters.IGNORE_STOPWORDS,
				Boolean.FALSE.toString());

		/** use corpus model instead of document model */
		workflowProperties.setProperty(WikiParameters.USE_BACKGROUND_CORPUS,
				Boolean.FALSE.toString());
		/** normalize all phraseness values */
		workflowProperties.setProperty(WikiParameters.NORMALIZE_PHRASENESS,
				Boolean.FALSE.toString());
		/** square phraseness values to avoid negative values */
		workflowProperties.setProperty(WikiParameters.SQUARE_PHRASENESS,
				Boolean.FALSE.toString());

		/**
		 * default value for informativeness, if the n-gram does not appear in
		 * the corpus
		 */
		workflowProperties.setProperty(WikiParameters.DEFAULT_VALUE, new Float(
				0.0f).toString());
		/** normalize all informativeness values */
		workflowProperties.setProperty(
				WikiParameters.NORMALIZE_INFORMATIVENESS,
				Boolean.FALSE.toString());
		/** square informativeness values to avoid negative values */
		workflowProperties.setProperty(WikiParameters.SQUARE_INFORMATIVENESS,
				Boolean.FALSE.toString());

		/** phraseness weight in the final score */
		workflowProperties.setProperty(WikiParameters.PHRASENESS_WEIGHT,
				new Float(0.5f).toString());
		/** informativeness weight in the final score */
		workflowProperties.setProperty(WikiParameters.INFORMATIVENESS_WEIGHT,
				new Float(0.5f).toString());
		return workflowProperties;

	}

	@Override
	protected void doRun() throws TupleSpaceException, AgentLifecycleException {

		while (status == Status.Running) {
			sendAliveUpdate();
			try {
				Thread.sleep(AgentProtocol.ALIVE_INTERVAL / 3);
			} catch (InterruptedException e) {
				throw new AgentLifecycleException(e.getMessage(), e);
			}
		}
	}

	private Tuple getResponseTuple(Set<String> keywords, String queryId) {
		StringBuffer keywordBuffer = new StringBuffer();
		for (String keyword : keywords) {
			keywordBuffer.append(keyword);
			keywordBuffer.append(";");
		}
		return new Tuple(EXTRACT_KEYPHRASES, AgentProtocol.RESPONSE, queryId,
				keywordBuffer.toString().trim());
	}

	@SuppressWarnings("unchecked")
	private Set<String> extractKeyphrases(String text, String mission,
			String language) {
		Document document = Utilities.convertTextToDocument(text);
		Operator extractKeyphrasesOperator = new ExtractKeyphrasesWorkflow(
				initWorkflowProperties()).getOperator("Main");
		extractKeyphrasesOperator.setInputParameter(ObjectIdentifiers.DOCUMENT,
				document);

		Container output = extractKeyphrasesOperator.run();

		Document doc = (Document) output.getObject(ObjectIdentifiers.DOCUMENT);

		Map<String, Float> keyphraseMap = (HashMap<String, Float>) doc
				.getFeature(WikiFeatures.NGRAM_FREQUENCIES);
		return keyphraseMap.keySet();
	}

	@Override
	protected void doStop() throws AgentLifecycleException {
		try {
			getCommandSpace().eventDeRegister(listenerId);
		} catch (TupleSpaceException e) {
			throw new AgentLifecycleException("Could not deregister listener",
					e);
		}
		status = Status.Stopping;
	}

	@Override
	protected Tuple getIdentifyTuple(String queryId) {
		return null;
	}

	@Override
	public boolean isStopped() {
		return status == Status.Stopping;
	}

	@Override
	public void call(Command command, int seq, Tuple afterTuple,
			Tuple beforeTuple) {
		if (listenerId != seq) {
			logger.debug("Callback passed to Superclass.");
			super.call(command, seq, afterTuple, beforeTuple);
			return;
		} else {
			String queryId = (String) afterTuple.getField(2).getValue();
			String text = (String) afterTuple.getField(3).getValue();
			String mission = (String) afterTuple.getField(4).getValue();
			String language = (String) afterTuple.getField(5).getValue();

			Set<String> keywords = extractKeyphrases(text, mission, language);

			try {
				getCommandSpace().write(getResponseTuple(keywords, queryId));
			} catch (TupleSpaceException e) {
				e.printStackTrace();
			}
		}
	}

}
