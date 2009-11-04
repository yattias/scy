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
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IMetadataValueContainer;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.api.IRepositoryAgent;
import eu.scy.agents.impl.AbstractProcessingAgent;
import eu.scy.agents.impl.AgentProtocol;

/**
 * Detects topics in text .
 * 
 * ("topicDetector":String, <ELOUri>:String) -> ("topicDetector":String,
 * <ELOUri>:String, <topicModelScore>:byte[](HashMap<Integer,Double>))
 * 
 * @author Florian Schulz
 * 
 */
public class DetectTopicForElos extends AbstractProcessingAgent implements
		IRepositoryAgent {

	static final String NAME = "eu.scy.agents.topics.DetectTopicForElos";
	public static final String MODEL_NAME = "TopicModelName";

	private String modelName;

	private IMetadataTypeManager metadataTypeManager;

	private IRepository repository;

	public DetectTopicForElos(Map<String, Object> params) {
		super(NAME, (String) params.get("id"));
		if (params.containsKey("tsHost")) {
			host = (String) params.get("tsHost");
		}
		if (params.containsKey("tsPort")) {
			port = (Integer) params.get("tsPort");
		}
		modelName = (String) params.get(MODEL_NAME);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doRun() throws TupleSpaceException, AgentLifecycleException {
		while (status == Status.Running) {
			Tuple tuple = getTupleSpace().waitToTake(getTemplateTuple(),
					AgentProtocol.ALIVE_INTERVAL);
			if (tuple != null) {
				String uri = "";
				try {
					uri = (String) tuple.getField(1).getValue();
					IELO elo = repository.retrieveELO(new URI(uri));
					if (isValidType(elo)) {
						String text = new String(elo.getContent().getBytes());
						String queryID = new VMID().toString();
						getTupleSpace().write(
								new Tuple("topicDetector", queryID, modelName,
										text));
						Tuple responseTuple = getTupleSpace().waitToRead(
								new Tuple("topicDetector", queryID, Field
										.createWildCardField()), 10 * 1000);
						if (responseTuple != null) {
							ObjectInputStream bytesIn = new ObjectInputStream(
									new ByteArrayInputStream(
											(byte[]) responseTuple.getField(2)
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
		return new Tuple("topicDetector", String.class);
	}

	private boolean isValidType(IELO elo) {
		IMetadataValueContainer type = elo
				.getMetadata()
				.getMetadataValueContainer(
						metadataTypeManager
								.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT
										.getId()));
		if ("scy/text".equals(type.getValue())) {
			return true;
		}
		return false;
	}

	private void addTopicMetadata(IELO elo, Map<Integer, Double> topicScores) {
		IMetadataValueContainer topicScoresContainer = elo
				.getMetadata()
				.getMetadataValueContainer(
						metadataTypeManager
								.getMetadataKey(TopicDetector.KEY_TOPIC_SCORES));
		for (Integer topicId : topicScores.keySet()) {
			Double topicProbability = topicScores.get(topicId);
			String value = topicId + ":" + topicProbability;
			topicScoresContainer.addValue(value);
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
