package eu.scy.agents.hypothesis;

import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.rmi.dgc.VMID;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractDecisionAgent;
import eu.scy.agents.impl.AgentProtocol;

/**
 * related tool: COPEX
 * 
 * Hypothesis evaluation rules: the histogram that is evaluated contains
 * information about the number of topics per sentence: a histogram like
 * "{0=16, 1=14, 2=7, 3=5, 4=1, 5=1}" indicates that the hypothesis text
 * contains 16 sentences without keyword, 14 sentences containing one keyword,
 * ..., one sentence containing 5 keywords. To evaluate, we sum up the numbers
 * of sentences, called N. Furthermore we score sentences that contain keywords:
 * Sentences without keyword score 0, sentences with one, two, ... keywords
 * score 1, 2, ... So we get a second number M by summing up the scores per
 * keyword frequency. Then we compute the fraction M/N and set a threshold to
 * evaluate it.
 * 
 * A second criterion is the co-ocurrence of keywords in one sentence, which
 * also is checked and should be above a minimum of cases.
 * 
 * @author Jörg Kindermann
 */
public class HypothesisDecisionMakerAgent extends AbstractDecisionAgent
		implements Callback {

	class ContextInformation {

		public String mission;

		public String session;

		public String user;

	}

	private final static Logger logger = Logger
			.getLogger(HypothesisDecisionMakerAgent.class);

	static final String NAME = HypothesisDecisionMakerAgent.class.getName();

	public static final String SCORE = "ScoreHypothesis";

	private static final String TOOL_TYPE = "copex";

	private int listenerId;

	public HypothesisDecisionMakerAgent(Map<String, Object> params) {
		super(HypothesisDecisionMakerAgent.class.getName(), (String) params
				.get(AgentProtocol.PARAM_AGENT_ID));
		try {
			this.listenerId = this.getCommandSpace().eventRegister(
					Command.WRITE,
					new Tuple(HypothesisEvaluationAgent.EVAL, String.class,
							String.class, String.class, String.class,
							String.class, Field.createWildCardField()), this,
					true);
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doRun() throws TupleSpaceException, AgentLifecycleException {
		while (status == Status.Running) {
			sendAliveUpdate();
			try {
				Thread.sleep(AgentProtocol.ALIVE_INTERVAL / 2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void call(Command command, int seq, Tuple hypothesisHistogramTuple,
			Tuple beforeTuple) {
		if (this.listenerId != seq) {
			logger.debug("Callback passed to Superclass.");
			super.call(command, seq, hypothesisHistogramTuple, beforeTuple);
			return;
		}
		if (hypothesisHistogramTuple != null) {
			try {
				String eloUri = (String) hypothesisHistogramTuple.getField(5)
						.getValue();
				HashMap<Integer, Integer> histogramMap = getHistogram(hypothesisHistogramTuple);
				double numberOfSentences = sum(histogramMap);
				double score = 0;
				if (numberOfSentences > 0)
					score = score(histogramMap) / numberOfSentences;

				int maxKey = maxKey(histogramMap);
				ContextInformation contextInformation = getContextInformation(hypothesisHistogramTuple);

				// TODO get information about level of scaffold
				if (score < 0.6) {
					// send notification "too few keywords or text too long"
					sendNotification(contextInformation, eloUri,
							"too few keywords or text too long");
				} else if (maxKey < 2) {
					// send notification
					// " try to express more inter-relation between keywords"
					sendNotification(contextInformation, eloUri,
							"inter-relation between keywords");
				} else {
					// send notification "hypothesis is ok"
					sendNotification(contextInformation, eloUri, "ok");
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	private void sendNotification(ContextInformation contextInformation,
			String eloUri, String message) {
		Tuple notificationTuple = new Tuple();
		notificationTuple.add(AgentProtocol.NOTIFICATION);
		notificationTuple.add(new VMID().toString());
		notificationTuple.add(contextInformation.user);
		notificationTuple.add(eloUri);
		notificationTuple.add(NAME);
		notificationTuple.add(contextInformation.mission);
		notificationTuple.add(contextInformation.session);

		notificationTuple.add("message=" + message);
		logger.info("sent notification: " + notificationTuple.toString());
		try {
			if (this.getCommandSpace().isConnected()) {
				this.getCommandSpace().write(notificationTuple);
			}
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}

	}

	public ContextInformation getContextInformation(Tuple beforeTuple) {
		ContextInformation result = new ContextInformation();
		result.user = (String) beforeTuple.getField(1).getValue();
		result.mission = (String) beforeTuple.getField(2).getValue();
		result.session = (String) beforeTuple.getField(3).getValue();
		return result;
	}

	private int maxKey(HashMap<Integer, Integer> histogramMap) {
		int max = 0;
		Set<Integer> keySet = histogramMap.keySet();
		for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {
			Integer key = (Integer) iterator.next();
			if (max < key)
				max = key;
		}
		return max;
	}

	private double score(HashMap<Integer, Integer> histogramMap) {
		double score = 0.0;
		Set<Integer> keySet = histogramMap.keySet();
		for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {
			Integer key = (Integer) iterator.next();
			int value = histogramMap.get(key);
			score += key * value;
		}
		return score;
	}

	private double sum(HashMap<Integer, Integer> histogramMap) {
		double sum = 0;
		Collection<Integer> values = histogramMap.values();
		for (Iterator iterator = values.iterator(); iterator.hasNext();) {
			Integer nSent = (Integer) iterator.next();
			sum += nSent;
		}
		return sum;
	}

	@SuppressWarnings("unchecked")
	private HashMap<Integer, Integer> getHistogram(Tuple topicTuple)
			throws IOException, ClassNotFoundException {
		ObjectInputStream bytesIn = new ObjectInputStream(
				new ByteArrayInputStream((byte[]) topicTuple.getField(6)
						.getValue()));
		HashMap<Integer, Integer> histogramMap = (HashMap<Integer, Integer>) bytesIn
				.readObject();
		return histogramMap;
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
