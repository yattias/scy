package eu.scy.agents.topics;

import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.dgc.VMID;
import java.util.HashMap;
import java.util.Map;

import roolo.api.IRepository;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IMetadataValueContainer;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.metadata.keys.KeyValuePair;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.api.IRepositoryAgent;
import eu.scy.agents.impl.AbstractRequestAgent;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.impl.EloTypes;

/**
 * Detects topics in text . ("topicDetector":String, <ELOUri>:String) ->
 * ("topicDetector":String, <ELOUri>:String)
 * 
 * @author Florian Schulz
 */
public class DetectTopicForElos extends AbstractRequestAgent implements
		IRepositoryAgent {

	public static final String NAME = DetectTopicForElos.class.getName();

	private String modelName;

	private IMetadataTypeManager metadataTypeManager;

	private IRepository repository;

	public DetectTopicForElos(Map<String, Object> params) {
		super(NAME, (String) params.get(AgentProtocol.PARAM_AGENT_ID));
		if (params.containsKey(AgentProtocol.TS_HOST)) {
			host = (String) params.get(AgentProtocol.TS_HOST);
		}
		if (params.containsKey(AgentProtocol.TS_PORT)) {
			port = (Integer) params.get(AgentProtocol.TS_PORT);
		}
		modelName = (String) params.get(TopicAgents.MODEL_NAME);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doRun() throws TupleSpaceException, AgentLifecycleException {
		while (status == Status.Running) {
			Tuple tuple = null;
			if (getCommandSpace().isConnected()) {
				tuple = getCommandSpace().waitToTake(getTemplateTuple(),
						AgentProtocol.COMMAND_EXPIRATION);
			}
			if (tuple != null) {
				String uri = "";
				try {
					uri = (String) tuple.getField(1).getValue();
					IELO elo = repository.retrieveELO(new URI(uri));
					if (isValidType(elo)) {
						String text = new String(elo.getContent().getBytes());
						String queryID = new VMID().toString();
						getCommandSpace().write(
								new Tuple(TopicAgents.TOPIC_DETECTOR,
										AgentProtocol.QUERY, queryID,
										modelName, text));
						Tuple responseTuple = getCommandSpace().waitToRead(
								new Tuple(TopicAgents.TOPIC_DETECTOR,
										AgentProtocol.RESPONSE, queryID, Field
												.createWildCardField()),
								10 * 1000);
						if (responseTuple != null) {
							ObjectInputStream bytesIn = new ObjectInputStream(
									new ByteArrayInputStream(
											(byte[]) responseTuple.getField(3)
													.getValue()));
							HashMap<Integer, Double> topicScoresMap = (HashMap<Integer, Double>) bytesIn
									.readObject();
							addTopicMetadata(elo, topicScoresMap);
							repository.addMetadata(elo.getUri(), elo
									.getMetadata());
						}
					}
				} catch (URISyntaxException e) {
					throw new AgentLifecycleException("malformed uri: " + uri,
							e);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			} else {
				sendAliveUpdate();
			}
		}
	}

	/**
	 * Sends ("topicDetector":String, <ELOUri>:String,
	 * topicModelScores:byte[](HashMap<Integer,Double>))
	 * 
	 * @throws IOException
	 * @throws TupleSpaceException
	 */

	private Tuple getTemplateTuple() {
		return new Tuple(TopicAgents.TOPIC_DETECTOR, String.class);
	}

	private boolean isValidType(IELO elo) {
		IMetadataValueContainer type = elo
				.getMetadata()
				.getMetadataValueContainer(
						metadataTypeManager
								.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT
										.getId()));
		if (EloTypes.SCY_TEXT.equals(type.getValue())) {
			return true;
		}
		return false;
	}

	private void addTopicMetadata(IELO elo, Map<Integer, Double> topicScores) {
		IMetadataKey key = metadataTypeManager
				.getMetadataKey(TopicAgents.KEY_TOPIC_SCORES);
		IMetadataValueContainer topicScoresContainer = elo.getMetadata()
				.getMetadataValueContainer(key);
		for (Integer topicId : topicScores.keySet()) {
			KeyValuePair entry = new KeyValuePair();
			entry.setKey("" + topicId);
			entry.setValue("" + topicScores.get(topicId));
			// Double topicProbability =
			// String value = +":" + topicProbability;
			topicScoresContainer.addValue(entry);
		}
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

	@Override
	public void setMetadataTypeManager(IMetadataTypeManager manager) {
		metadataTypeManager = manager;
	}

	@Override
	public void setRepository(IRepository rep) {
		repository = rep;
	}
}
