package eu.scy.agents.keywords;

import java.rmi.dgc.VMID;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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

	private static final Logger logger = Logger
			.getLogger(ExtractKeywordsAgent.class.getName());

	private Tuple activationTuple;

	private int listenerId = -1;

	public ExtractKeywordsAgent(Map<String, Object> params) {
		super(NAME, params);
		if (params.containsKey(AgentProtocol.TS_HOST)) {
			host = (String) params.get(AgentProtocol.TS_HOST);
		}
		if (params.containsKey(AgentProtocol.TS_PORT)) {
			port = (Integer) params.get(AgentProtocol.TS_PORT);
		}
		activationTuple = new Tuple(EXTRACT_KEYWORDS, AgentProtocol.QUERY,
				String.class, String.class, String.class);
		try {
			listenerId = getCommandSpace().eventRegister(Command.WRITE,
					activationTuple, this, true);
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doRun() throws TupleSpaceException, AgentLifecycleException,
			InterruptedException {
		while (status == Status.Running) {
			sendAliveUpdate();
			Thread.sleep(AgentProtocol.ALIVE_INTERVAL / 3);
		}
	}

	private void sendAnswer(Set<String> mergedKeywords, String queryId) {
		Tuple responseTuple = new Tuple();
		responseTuple.add(EXTRACT_KEYWORDS);
		responseTuple.add(AgentProtocol.RESPONSE);
		responseTuple.add(queryId);
		for (String keyword : mergedKeywords) {
			responseTuple.add(keyword);
		}
		try {
			if (getCommandSpace().isConnected()) {
				getCommandSpace().write(responseTuple);
			}
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}

	private Set<String> mergeKeywords(Set<String> tfIdfKeywords,
			Set<String> keyPhrases, Set<String> topicKeywords,
			Set<String> ontologyKeywords) {
		Set<String> mergedResult = new HashSet<String>();
		mergedResult.addAll(tfIdfKeywords);
		mergedResult.addAll(keyPhrases);
		mergedResult.addAll(topicKeywords);
		mergedResult.addAll(ontologyKeywords);
		return mergedResult;
	}

	private Set<String> callKeywordsAgent(String agent, String text,
			String mission) {
		String queryId = new VMID().toString();
		Set<String> result = new HashSet<String>();
		try {
			getCommandSpace().write(
					new Tuple(agent, AgentProtocol.QUERY, queryId, text,
							mission, "en"));
			Tuple response = getCommandSpace().waitToTake(
					new Tuple(agent, AgentProtocol.RESPONSE, queryId,
							String.class), AgentProtocol.SECOND * 30);
			if (response == null) {
				return result;
			}
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
	public void call(Command command, int seq, Tuple tuple, Tuple beforeTuple) {
		if (listenerId != seq) {
			logger.debug("Callback passed to Superclass.");
			super.call(command, seq, tuple, beforeTuple);
			return;
		} else {
			String queryId = (String) tuple.getField(2).getValue();
			String text = (String) tuple.getField(3).getValue();
			String mission = (String) tuple.getField(4).getValue();

			Set<String> tfIdfKeywords = callKeywordsAgent(
					ExtractTfIdfKeywordsAgent.EXTRACT_TFIDF_KEYWORDS, text,
					mission);
			Set<String> keyPhrases = callKeywordsAgent(
					ExtractKeyphrasesAgent.EXTRACT_KEYPHRASES, text, mission);
			Set<String> topicKeywords = callKeywordsAgent(
					ExtractTopicModelKeywordsAgent.EXTRACT_TOPIC_MODEL_KEYWORDS,
					text, mission);
			Set<String> ontologyKeywords = callKeywordsAgent(
					OntologyKeywordsAgent.EXTRACT_ONTOLOGY_KEYWORDS, text,
					mission);

			Set<String> mergedKeywords = mergeKeywords(tfIdfKeywords,
					keyPhrases, topicKeywords, ontologyKeywords);
			logger.info("found keywords: " + mergedKeywords);

			sendAnswer(mergedKeywords, queryId);
		}
	}
}
