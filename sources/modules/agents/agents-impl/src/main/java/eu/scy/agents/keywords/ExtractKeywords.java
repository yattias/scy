package eu.scy.agents.keywords;

import java.rmi.dgc.VMID;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractRequestAgent;
import eu.scy.agents.impl.AgentProtocol;

public class ExtractKeywords extends AbstractRequestAgent {

	public static final String NAME = "eu.scy.agents.keywords.ExtractKeywords";
	public static final Object EXTRACT_KEYWORDS = "ExtractKeywords";

	private Tuple activationTuple;

	public ExtractKeywords(Map<String, Object> params) {
		super(NAME, params);
		activationTuple = new Tuple(EXTRACT_KEYWORDS, AgentProtocol.QUERY, String.class, String.class);
	}

	@Override
	protected void doRun() throws TupleSpaceException, AgentLifecycleException, InterruptedException {
		while (status == Status.Running) {
			Tuple tuple = getTupleSpace().waitToTake(activationTuple, AgentProtocol.ALIVE_INTERVAL);
			if (tuple != null) {
				ArrayList<String> tfIdfKeywords = getTfIdfKeywords(tuple);
				ArrayList<String> topicKeywords = getTopicKeywords(tuple);

				ArrayList<String> mergedKeywords = mergeKeywords(tfIdfKeywords, topicKeywords);

				sendNotification(mergedKeywords);
			}
			sendAliveUpdate();
		}
	}

	private void sendNotification(ArrayList<String> mergedKeywords) {
		Tuple notificationTuple = new Tuple();
		notificationTuple.add("notification");
		notificationTuple.add(new VMID().toString());
		notificationTuple.add("obama");
		notificationTuple.add("scymapper");
		notificationTuple.add("Sender");
		notificationTuple.add("Mission");
		notificationTuple.add("Session");
		for (String keyword : mergedKeywords) {
			notificationTuple.add("keyword=" + keyword);
		}
		try {
			getTupleSpace().write(notificationTuple);
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<String> mergeKeywords(List<String> keywords1, List<String> keywords2) {
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
			getTupleSpace().write(
					new Tuple(ExtractTfIdfKeywordsAgent.EXTRACT_TFIDF_KEYWORDS, AgentProtocol.QUERY, queryId, text));
			Tuple response = getTupleSpace().waitToTake(
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
			getTupleSpace().write(
					new Tuple(ExtractTopicModelKeywordsAgent.EXTRACT_TOPIC_MODEL_KEYWORDS, AgentProtocol.QUERY,
							queryId, text));
			Tuple response = getTupleSpace().waitToTake(
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
