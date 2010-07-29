package eu.scy.agents.keywords;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Document;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Features;
import de.fhg.iais.kd.tm.obwious.operator.ObjectIdentifiers;
import de.fhg.iais.kd.tm.obwious.operator.Operator;
import de.fhg.iais.kd.tm.obwious.system.documentfrequency.DocumentFrequencyModel;
import de.fhg.iais.kd.tm.obwious.type.Container;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractRequestAgent;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.impl.PersistentStorage;
import eu.scy.agents.keywords.workflow.ExtractTfIdfKeywordsWorkflow;
import eu.scy.agents.keywords.workflow.KeywordConstants;

/**
 * (ExtractTfIdfKeywords:String, query:String, <QueryId>:String,
 * <Text>:String)-> (ExtractTfIdfKeywords:String, response:String,
 * <QueryId>:String, <Keywords>:String)->
 * 
 * @author fschulz
 */
public class ExtractTfIdfKeywordsAgent extends AbstractRequestAgent {

	public static final String EXTRACT_TFIDF_KEYWORDS = "ExtractTfIdfKeywords";

	public static final String NAME = ExtractTfIdfKeywordsAgent.class.getName();

	private Tuple activationTuple;

	private int listenerId;

	private static final Logger logger = Logger
			.getLogger(ExtractTfIdfKeywordsAgent.class.getName());

	private PersistentStorage storage = null;

	public ExtractTfIdfKeywordsAgent(Map<String, Object> params) {
		super(NAME, params);
		if (params.containsKey(AgentProtocol.TS_HOST)) {
			host = (String) params.get(AgentProtocol.TS_HOST);
		}
		if (params.containsKey(AgentProtocol.TS_PORT)) {
			port = (Integer) params.get(AgentProtocol.TS_PORT);
		}
		activationTuple = new Tuple(EXTRACT_TFIDF_KEYWORDS,
				AgentProtocol.QUERY, String.class, String.class);
		storage = new PersistentStorage(host, port);
		try {
			listenerId = getCommandSpace().eventRegister(Command.WRITE,
					activationTuple, this, true);
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doRun() throws TupleSpaceException, AgentLifecycleException {

		while (status == Status.Running) {
			sendAliveUpdate();
			try {
				Thread.sleep(AgentProtocol.ALIVE_INTERVAL / 3);
			} catch (InterruptedException e) {
				throw new AgentLifecycleException(e.getMessage());
			}
		}
	}

	private Tuple getResponseTuple(Set<String> keywords, String queryId) {
		StringBuffer keywordBuffer = new StringBuffer();
		for (String keyword : keywords) {
			keywordBuffer.append(keyword);
			keywordBuffer.append(";");
		}
		return new Tuple(EXTRACT_TFIDF_KEYWORDS, AgentProtocol.RESPONSE,
				queryId, keywordBuffer.toString().trim());
	}

	private Set<String> extractKeywords(String text) {
		DocumentFrequencyModel dfModel = storage
				.get(KeywordConstants.DOCUMENT_FREQUENCY_MODEL);

		Document document = convertTextToDocument(text);
		if (dfModel == null) {
			logger.fatal("TfIdfModel is not present");
			return new HashSet<String>();
		}

		Operator extractKeywordsOperator = new ExtractTfIdfKeywordsWorkflow()
				.getOperator("Main");
		extractKeywordsOperator.setInputParameter(ObjectIdentifiers.DOCUMENT,
				document);
		extractKeywordsOperator.setInputParameter(
				KeywordConstants.DOCUMENT_FREQUENCY_MODEL, dfModel);
		Container result = extractKeywordsOperator.run();
		Document doc = (Document) result.get(ObjectIdentifiers.DOCUMENT);

		return doc.getFeature(KeywordConstants.TFIDF_KEYWORDS);
	}

	private Document convertTextToDocument(String text) {
		Document doc = new Document("id");
		doc.setFeature(Features.TEXT, text);
		return doc;
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

			Set<String> keywords = extractKeywords(text);

			try {
				getCommandSpace().write(getResponseTuple(keywords, queryId));
			} catch (TupleSpaceException e) {
				e.printStackTrace();
			}
		}
	}

}
