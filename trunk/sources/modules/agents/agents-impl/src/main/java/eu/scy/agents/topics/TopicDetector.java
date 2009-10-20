package eu.scy.agents.topics;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import roolo.api.IRepository;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IMetadataValueContainer;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import cc.mallet.topics.TopicModelAnnotator;
import cc.mallet.topics.TopicModelParameter;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Document;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Features;
import de.fhg.iais.kd.tm.obwious.operator.ObjectIdentifiers;
import de.fhg.iais.kd.tm.obwious.operator.Operator;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.api.IRepositoryAgent;
import eu.scy.agents.impl.AbstractProcessingAgent;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.impl.PersistentStorage;

/**
 * Detects topics in a text ELOs. Intended to be a before agent.
 * 
 * ("topicDetector":String, <ELOUri>:String) -> ("topicDetector":String,
 * <ELOUri>:String, topicModelScores:Map<Integer,Double>)
 * 
 * @author Florian Schulz
 * 
 */
public class TopicDetector extends AbstractProcessingAgent implements
		IRepositoryAgent {

	static final String KEY_TOPIC_SCORES = "topicScores";

	private static final String NAME = "eu.scy.agents.topics.TopicDetector";

	public static final String MODEL_NAME = "TopicModelName";

	private TopicModelAnnotator model;
	private PersistentStorage agentDatabase;
	private String modelName;

	private IMetadataTypeManager metadataTypeManager;

	private IRepository repository;

	public TopicDetector(Map<String, Object> params) {
		super(NAME, (String) params.get("id"));
		agentDatabase = new PersistentStorage();
		modelName = (String) params.get(MODEL_NAME);
	}

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
						Document doc = convertEloToDocument(elo);
						Map<Integer, Double> topicScores = getTopicScores(doc);
						addTopicMetadata(elo, topicScores);
						repository.addMetadata(new URI(uri), elo.getMetadata());
					}
				} catch (URISyntaxException e) {
					throw new AgentLifecycleException("malformed uri: " + uri,
							e);
				}
			} else {
				sendAliveUpdate();
			}
		}
	}

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

	private Map<Integer, Double> getTopicScores(Document doc) {
		model = new TopicModelAnnotator((TopicModelParameter) agentDatabase
				.get(modelName));
		Operator assignTopicScores = new AssignTopicScores()
				.getOperator("Main");
		assignTopicScores.setInputParameter(ObjectIdentifiers.DOCUMENT, doc);
		assignTopicScores.setInputParameter(ObjectIdentifiers.MODEL, model);
		assignTopicScores.run();

		Map<Integer, Double> topicScores = doc.getFeature(KEY_TOPIC_SCORES);
		return topicScores;
	}

	private void addTopicMetadata(IELO elo, Map<Integer, Double> topicScores) {
		IMetadataValueContainer topicScoresContainer = elo.getMetadata()
				.getMetadataValueContainer(
						metadataTypeManager.getMetadataKey(KEY_TOPIC_SCORES));
		for (Integer topicId : topicScores.keySet()) {
			Double topicProbability = topicScores.get(topicId);
			String value = topicId + ":" + topicProbability;
			topicScoresContainer.addValue(value);
		}
	}

	private Document convertEloToDocument(IELO elo) {
		Document document = new Document("tmp");
		String text = new String(elo.getContent().getBytes());
		document.setFeature(Features.TEXT, text);
		return document;
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
