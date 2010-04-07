package eu.scy.agents.topics;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

import cc.mallet.topics.TopicModelAnnotator;
import cc.mallet.topics.TopicModelParameter;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Document;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Features;
import de.fhg.iais.kd.tm.obwious.operator.ObjectIdentifiers;
import de.fhg.iais.kd.tm.obwious.operator.Operator;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractRequestAgent;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.impl.PersistentStorage;

/**
 * ("topicDetector":String, <QueryID>:String, <ModelName>:String, <Text>:String) -> ("topicDetector":String,
 * <QueryID>:String, <topicModelScore>:byte[](HashMap<Integer,Double>))
 * 
 * @author fschulz_2
 */
public class TopicDetector extends AbstractRequestAgent {

	static final String NAME = TopicDetector.class.getName();

	private PersistentStorage agentDatabase;
	private Tuple activationTuple;

	public TopicDetector(Map<String, Object> params) {
		super(NAME, params);
		if (params.containsKey(AgentProtocol.TS_HOST)) {
			host = (String) params.get(AgentProtocol.TS_HOST);
		}
		if (params.containsKey(AgentProtocol.TS_PORT)) {
			port = (Integer) params.get(AgentProtocol.TS_PORT);
		}
		agentDatabase = new PersistentStorage(host, port);
		activationTuple = new Tuple(TopicAgents.TOPIC_DETECTOR, AgentProtocol.QUERY, String.class, String.class,
				String.class);
	}

	@Override
	protected void doRun() throws TupleSpaceException, AgentLifecycleException {
		while (status == Status.Running) {
			try {
				Tuple tuple = null;
				if (getCommandSpace().isConnected()) {
					tuple = getCommandSpace().waitToTake(activationTuple, AgentProtocol.COMMAND_EXPIRATION);
				}
				if (tuple != null) {
					String queryID = (String) tuple.getField(2).getValue();
					String modelName = (String) tuple.getField(3).getValue();
					String text = (String) tuple.getField(4).getValue();
					Map<Integer, Double> topicScores = getTopicScores(modelName, text);
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

	private void sendTopicsToTS(String queryId, Map<Integer, Double> topicScores) throws IOException,
			TupleSpaceException {

		ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(bytesOut);

		out.writeObject(topicScores);
		bytesOut.toByteArray();

		Tuple topicsDetectedTuple = new Tuple(TopicAgents.TOPIC_DETECTOR, AgentProtocol.RESPONSE, queryId.toString(),
				bytesOut.toByteArray());
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
		Document doc = new Document("text");
		doc.setFeature(Features.TEXT, text);
		TopicModelAnnotator model = new TopicModelAnnotator((TopicModelParameter) agentDatabase.get(modelName));
		Operator assignTopicScores = new AssignTopicScores().getOperator("Main");
		assignTopicScores.setInputParameter(ObjectIdentifiers.DOCUMENT, doc);
		assignTopicScores.setInputParameter(ObjectIdentifiers.MODEL, model);
		assignTopicScores.run();

		Map<Integer, Double> topicScores = doc.getFeature(TopicAgents.KEY_TOPIC_SCORES);
		return topicScores;
	}
}
