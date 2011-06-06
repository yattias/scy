package eu.scy.agents.topics;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.topics.TopicInferencer;
import cc.mallet.types.Instance;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractRequestAgent;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.impl.ModelStorage;
import eu.scy.agents.keywords.workflow.KeywordWorkflowConstants;
import eu.scy.agents.util.Preprocessor;

/**
 * ("topicDetector":String, <QueryID>:String, <Mission>:String,
 * <Language>:String, <Text>:String) -> ("topicDetector":String,
 * <QueryID>:String, <topicModelScore>:byte[](HashMap<Integer,Double>))
 * 
 * @author fschulz_2
 */
public class TopicDetector extends AbstractRequestAgent {

	static final String NAME = TopicDetector.class.getName();

	private ModelStorage modelStorage;
	private Tuple activationTuple;
	private Preprocessor preprocessor;

	private int listenerId;

	public TopicDetector(Map<String, Object> params) {
		super(NAME, params);
		if (params.containsKey(AgentProtocol.TS_HOST)) {
			host = (String) params.get(AgentProtocol.TS_HOST);
		}
		if (params.containsKey(AgentProtocol.TS_PORT)) {
			port = (Integer) params.get(AgentProtocol.TS_PORT);
		}
		preprocessor = new Preprocessor();
		modelStorage = new ModelStorage(getCommandSpace());
		activationTuple = new Tuple(TopicAgents.TOPIC_DETECTOR,
				AgentProtocol.QUERY, String.class, String.class, String.class,
				String.class);
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

	private void sendTopicsToTS(String queryId, Map<Integer, Double> topicScores)
			throws IOException, TupleSpaceException {

		ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(bytesOut);

		out.writeObject(topicScores);
		bytesOut.toByteArray();

		Tuple topicsDetectedTuple = new Tuple(TopicAgents.TOPIC_DETECTOR,
				AgentProtocol.RESPONSE, queryId.toString(),
				bytesOut.toByteArray());
		getCommandSpace().write(topicsDetectedTuple);
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

	private Map<Integer, Double> getTopicScores(String mission,
			String language, String text) {
		ParallelTopicModel model = (ParallelTopicModel) modelStorage.get(
				mission, language, KeywordWorkflowConstants.TOPIC_MODEL);

		String[] tokens = preprocessor.preprocessDocument(text);
		tokens = preprocessor.removeStopwords(tokens, "en");
		Instance instance = preprocessor.createInstanceFromTokens("id", tokens);

		TopicInferencer inferencer = model.getInferencer();
		double[] sampledDistribution = inferencer.getSampledDistribution(
				instance, 2000, 10, 1500);

		Map<Integer, Double> topicScores = new HashMap<Integer, Double>();
		for (int i = 0; i < sampledDistribution.length; i++) {
			topicScores.put(i, sampledDistribution[i]);
		}
		return topicScores;
	}

	@Override
	public void call(Command command, int seq, Tuple tuple, Tuple beforeTuple) {
		if (listenerId != seq) {
			logger.debug("Callback passed to Superclass.");
			super.call(command, seq, tuple, beforeTuple);
			return;
		} else {
			String queryID = (String) tuple.getField(2).getValue();
			String mission = (String) tuple.getField(3).getValue();
			String language = (String) tuple.getField(4).getValue();
			String text = (String) tuple.getField(5).getValue();
			Map<Integer, Double> topicScores = getTopicScores(mission,
					language, text);
			try {
				sendTopicsToTS(queryID, topicScores);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (TupleSpaceException e) {
				e.printStackTrace();
			}
		}
	}
}
