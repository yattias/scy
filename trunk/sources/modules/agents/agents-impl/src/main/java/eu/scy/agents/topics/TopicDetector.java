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
import eu.scy.agents.impl.PersistentStorage;
import eu.scy.agents.util.Preprocessor;

/**
 * ("topicDetector":String, <QueryID>:String, <ModelName>:String, <Text>:String)
 * -> ("topicDetector":String, <QueryID>:String,
 * <topicModelScore>:byte[](HashMap<Integer,Double>))
 * 
 * @author fschulz_2
 */
public class TopicDetector extends AbstractRequestAgent {

	static final String NAME = TopicDetector.class.getName();

	private PersistentStorage agentDatabase;
	private Tuple activationTuple;
	Preprocessor preprocessor;

	public TopicDetector(Map<String, Object> params) {
		super(NAME, params);
		if (params.containsKey(AgentProtocol.TS_HOST)) {
			host = (String) params.get(AgentProtocol.TS_HOST);
		}
		if (params.containsKey(AgentProtocol.TS_PORT)) {
			port = (Integer) params.get(AgentProtocol.TS_PORT);
		}
		preprocessor = new Preprocessor();
		agentDatabase = new PersistentStorage(host, port);
		activationTuple = new Tuple(TopicAgents.TOPIC_DETECTOR,
				AgentProtocol.QUERY, String.class, String.class, String.class);
	}

	@Override
	protected void doRun() throws TupleSpaceException, AgentLifecycleException {
		while (status == Status.Running) {
			try {
				Tuple tuple = null;
				if (getCommandSpace().isConnected()) {
					tuple = getCommandSpace().waitToTake(activationTuple,
							AgentProtocol.COMMAND_EXPIRATION);
				}
				if (tuple != null) {
					String queryID = (String) tuple.getField(2).getValue();
					String modelName = (String) tuple.getField(3).getValue();
					String text = (String) tuple.getField(4).getValue();
					Map<Integer, Double> topicScores = getTopicScores(
							modelName, text);
					sendTopicsToTS(queryID, topicScores);
				}
				sendAliveUpdate();
			} catch (TupleSpaceException e) {
				// just ignore
			} catch (IOException e) {
				throw new AgentLifecycleException("Could not send tuple", e);
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
				AgentProtocol.RESPONSE, queryId.toString(), bytesOut
						.toByteArray());
		getCommandSpace().write(topicsDetectedTuple);
	}

	@Override
	protected void doStop() {
		status = Status.Stopping;
	}

	@Override
	protected Tuple getIdentifyTuple(String queryId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isStopped() {
		return status == Status.Stopping;
	}

	private Map<Integer, Double> getTopicScores(String modelName, String text) {
		ParallelTopicModel model = (ParallelTopicModel) agentDatabase
				.get(modelName);

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
}
