package eu.scy.agents.keywords;

import java.rmi.dgc.VMID;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractRequestAgent;
import eu.scy.agents.impl.AgentProtocol;

public class ExtractKeywordsAgent extends AbstractRequestAgent {

	public static final String NAME = ExtractKeywordsAgent.class.getName();
	public static final Object EXTRACT_KEYWORDS = "ExtractKeywords";

	private static final Logger logger = Logger.getLogger(ExtractKeywordsAgent.class.getName());

	private Tuple activationTuple;

	public ExtractKeywordsAgent(Map<String, Object> params) {
		super(NAME, params);
		if (params.containsKey(AgentProtocol.TS_HOST)) {
			host = (String) params.get(AgentProtocol.TS_HOST);
		}
		if (params.containsKey(AgentProtocol.TS_PORT)) {
			port = (Integer) params.get(AgentProtocol.TS_PORT);
		}
		activationTuple = new Tuple(EXTRACT_KEYWORDS, AgentProtocol.QUERY, String.class, String.class);
	}

	@Override
	protected void doRun() throws TupleSpaceException, AgentLifecycleException, InterruptedException {
		while (status == Status.Running) {
			Tuple tuple = null;
			if (getCommandSpace().isConnected()) {
				tuple = getCommandSpace().waitToTake(activationTuple, AgentProtocol.COMMAND_EXPIRATION);
			}
			if (tuple != null) {
				String queryId = (String) tuple.getField(2).getValue();
				ArrayList<String> tfIdfKeywords = getTfIdfKeywords(tuple);
				ArrayList<String> topicKeywords = getTopicKeywords(tuple);

				ArrayList<String> mergedKeywords = mergeKeywords(tfIdfKeywords, topicKeywords);
				logger.info("found keywords: " + mergedKeywords);

				sendAnswer(mergedKeywords, queryId);
			}
			sendAliveUpdate();
		}
	}

	private void sendAnswer(List<String> mergedKeywords, String queryId) {
		Tuple responseTuple = new Tuple();
		responseTuple.add(EXTRACT_KEYWORDS);
		responseTuple.add(AgentProtocol.RESPONSE);
		responseTuple.add(queryId);
		for (String keyword : mergedKeywords) {
			responseTuple.add(keyword);
		}
		try {
			getCommandSpace().write(responseTuple);
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}

	private ArrayList<String> mergeKeywords(List<String> keywords1, List<String> keywords2) {
		ArrayList<String> mergedResult = new ArrayList<String>();
		mergedResult.addAll(keywords1);
		mergedResult.addAll(keywords2);
		return mergedResult;
	}

	private ArrayList<String> getTfIdfKeywords(Tuple tuple) {
		String text = (String) tuple.getField(3).getValue();
		String queryId = new VMID().toString();
		ArrayList<String> result = new ArrayList<String>();
		try {
			getCommandSpace().write(
					new Tuple(ExtractTfIdfKeywordsAgent.EXTRACT_TFIDF_KEYWORDS, AgentProtocol.QUERY, queryId, text));
			Tuple response = getCommandSpace().waitToTake(
					new Tuple(ExtractTfIdfKeywordsAgent.EXTRACT_TFIDF_KEYWORDS, AgentProtocol.RESPONSE, queryId,
							String.class));
			String keywordString = (String) response.getField(3).getValue();
			StringTokenizer tokenizer = new StringTokenizer(keywordString, ";");
			while (tokenizer.hasMoreTokens()) {
				result.add(tokenizer.nextToken());
			}
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
		return result;
	}

	private ArrayList<String> getTopicKeywords(Tuple tuple) {
		String text = (String) tuple.getField(3).getValue();
		String queryId = new VMID().toString();
		ArrayList<String> result = new ArrayList<String>();
		try {
			getCommandSpace().write(
					new Tuple(ExtractTopicModelKeywordsAgent.EXTRACT_TOPIC_MODEL_KEYWORDS, AgentProtocol.QUERY,
							queryId, text));
			Tuple response = getCommandSpace().waitToTake(
					new Tuple(ExtractTopicModelKeywordsAgent.EXTRACT_TOPIC_MODEL_KEYWORDS, AgentProtocol.RESPONSE,
							queryId, String.class));
			String keywordString = (String) response.getField(3).getValue();
			StringTokenizer tokenizer = new StringTokenizer(keywordString, ";");
			while (tokenizer.hasMoreTokens()) {
				result.add(tokenizer.nextToken());
			}
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected void doStop() {
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

}
