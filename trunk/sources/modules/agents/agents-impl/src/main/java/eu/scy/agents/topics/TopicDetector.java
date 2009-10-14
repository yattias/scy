package eu.scy.agents.topics;

import java.util.Map;

import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataValueContainer;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import cc.mallet.topics.TopicModelAnnotator;
import cc.mallet.topics.TopicModelParameter;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Document;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Features;
import de.fhg.iais.kd.tm.obwious.operator.ObjectIdentifiers;
import de.fhg.iais.kd.tm.obwious.operator.Operator;
import eu.scy.agents.impl.PersistentStorage;
import eu.scy.agents.impl.elo.AbstractELOAgent;

/**
 * Detects topics in a text ELOs. Intended to be a before agent.
 * 
 * ELO Saved -> ("topicDetector":String, <ELOUri>:String,
 * topicModelScores:Map<Integer,Double>)
 * 
 * @author Florian Schulz
 * 
 */
public class TopicDetector extends AbstractELOAgent {

	static final String KEY_TOPIC_SCORES = "topicScores";

	private static final String NAME = "eu.scy.agents.topics.TopicDetector";

	public static final String MODEL_NAME = "TopicModelName";

	private TopicModelAnnotator model;
	private PersistentStorage agentDatabase;
	private String modelName;

	public TopicDetector(String modelName) {
		super(NAME, modelName);
		agentDatabase = new PersistentStorage();
		this.modelName = modelName;
	}

	@Override
	public void processElo(IELO elo) {
		if (elo != null && isValidType(elo)) {
			Document doc = convertEloToDocument(elo);
			Map<Integer, Double> topicScores = getTopicScores(doc);
			addTopicMetadata(elo, topicScores);
		}
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
}
