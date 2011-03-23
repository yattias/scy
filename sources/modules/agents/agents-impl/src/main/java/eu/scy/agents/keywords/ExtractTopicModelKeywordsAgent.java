package eu.scy.agents.keywords;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import cc.mallet.topics.ParallelTopicModel;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Document;
import de.fhg.iais.kd.tm.obwious.operator.ObjectIdentifiers;
import de.fhg.iais.kd.tm.obwious.operator.Operator;
import de.fhg.iais.kd.tm.obwious.system.documentfrequency.DocumentFrequencyModel;
import de.fhg.iais.kd.tm.obwious.type.Container;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractRequestAgent;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.impl.ModelStorage;
import eu.scy.agents.impl.PersistentStorage;
import eu.scy.agents.keywords.workflow.ExtractTopicModelKeywordsWorkflow;
import eu.scy.agents.keywords.workflow.KeywordWorkflowConstants;
import eu.scy.agents.util.Utilities;

public class ExtractTopicModelKeywordsAgent extends AbstractRequestAgent {

	private static final String CO2_SCY_ENGLISH = "co2_scy_english";

	public static final String EXTRACT_TOPIC_MODEL_KEYWORDS = "ExtractTopicModelKeywords";

	public static final String NAME = ExtractTopicModelKeywordsAgent.class
			.getName();

	private static final Logger logger = Logger
			.getLogger(ExtractTopicModelKeywordsAgent.class.getName());

	private static final String TOPIC_MODEL = "topicModel";

	private Tuple activationTuple;

	private int listenerId = -1;

	private ModelStorage modelStorage;

	private PersistentStorage storage;

	public ExtractTopicModelKeywordsAgent(Map<String, Object> params) {
		super(NAME, params);
		if (params.containsKey(AgentProtocol.TS_HOST)) {
			host = (String) params.get(AgentProtocol.TS_HOST);
		}
		if (params.containsKey(AgentProtocol.TS_PORT)) {
			port = (Integer) params.get(AgentProtocol.TS_PORT);
		}
		activationTuple = new Tuple(EXTRACT_TOPIC_MODEL_KEYWORDS,
				AgentProtocol.QUERY, String.class, String.class, String.class,
				String.class);
		modelStorage = new ModelStorage(getCommandSpace());
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
		return new Tuple(EXTRACT_TOPIC_MODEL_KEYWORDS, AgentProtocol.RESPONSE,
				queryId, keywordBuffer.toString().trim());
	}

	private Set<String> extractKeywords(String text, String mission,
			String language) {
		DocumentFrequencyModel dfModel = getDocumentFrequencyModel(mission,
				language);
		if (dfModel == null) {
			return Collections.emptySet();
		}
		ParallelTopicModel tm = getTopicModel(mission, language);
		if (tm == null) {
			return Collections.emptySet();
		}

		Document document = Utilities.convertTextToDocument(text);

		try {
			Operator extractKeywordsOperator = new ExtractTopicModelKeywordsWorkflow()
					.getOperator("Main");
			extractKeywordsOperator.setInputParameter(
					ObjectIdentifiers.DOCUMENT, document);
			extractKeywordsOperator.setInputParameter(
					KeywordWorkflowConstants.DOCUMENT_FREQUENCY_MODEL, dfModel);
			extractKeywordsOperator.setInputParameter(
					KeywordWorkflowConstants.TOPIC_MODEL, tm);
			Container result = extractKeywordsOperator.run();
			Document doc = (Document) result.get(ObjectIdentifiers.DOCUMENT);
			return doc.getFeature(KeywordWorkflowConstants.TM_KEYWORDS);
		} catch (RuntimeException e) {
			logger.fatal(e.getMessage(), e);
			e.printStackTrace();
			return new HashSet<String>();
		}
	}

	private ParallelTopicModel getTopicModel(String mission, String language) {
		ParallelTopicModel tm = (ParallelTopicModel) modelStorage.getAsObject(
				mission, language, TOPIC_MODEL);
		if (tm == null) {
			tm = (ParallelTopicModel) storage.get(CO2_SCY_ENGLISH);
		}
		return tm;
	}

	private DocumentFrequencyModel getDocumentFrequencyModel(String mission,
			String language) {
		DocumentFrequencyModel dfModel = (DocumentFrequencyModel) modelStorage
				.getAsObject(mission, language,
						KeywordWorkflowConstants.DOCUMENT_FREQUENCY_MODEL);
		if (dfModel == null) {
			dfModel = storage
					.get(KeywordWorkflowConstants.DOCUMENT_FREQUENCY_MODEL);
		}
		return dfModel;
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

			Set<String> keywords = extractKeywords(text, mission, language);

			try {
				getCommandSpace().write(getResponseTuple(keywords, queryId));
			} catch (TupleSpaceException e) {
				e.printStackTrace();
			}
		}
	}

}
