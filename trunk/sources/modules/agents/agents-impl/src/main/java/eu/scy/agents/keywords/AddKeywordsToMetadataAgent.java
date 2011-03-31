package eu.scy.agents.keywords;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import roolo.api.IRepository;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IMetadataValueContainer;
import eu.scy.agents.api.IRepositoryAgent;
import eu.scy.agents.impl.AbstractELOSavedAgent;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.keywords.extractors.KeywordExtractor;
import eu.scy.agents.keywords.extractors.KeywordExtractorFactory;
import eu.scy.agents.session.SessionAgent;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.metadata.keys.KeyValuePair;

public class AddKeywordsToMetadataAgent extends AbstractELOSavedAgent implements
		IRepositoryAgent {

	public final static String NAME = AddKeywordsToMetadataAgent.class
			.getName();

	private static final Logger LOGGER = Logger
			.getLogger(AddKeywordsToMetadataAgent.class.getName());

	private IMetadataTypeManager metadataTypeManager;

	private IRepository repository;

	public AddKeywordsToMetadataAgent(Map<String, Object> params) {
		super(NAME, (String) params.get(AgentProtocol.PARAM_AGENT_ID));
		if (params.containsKey(AgentProtocol.TS_HOST)) {
			this.host = (String) params.get(AgentProtocol.TS_HOST);
		}
		if (params.containsKey(AgentProtocol.TS_PORT)) {
			this.port = (Integer) params.get(AgentProtocol.TS_PORT);
		}
	}

	@Override
	public void processELOSavedAction(String actionId, String user,
			long timeInMillis, String tool, String mission, String session,
			String eloUri, String eloType) {

		IELO elo = getELO(eloUri);
		if (elo == null) {
			return;
		}

		String missionForUser = getMission(user);
		if (missionForUser == null) {
			missionForUser = mission;
		}

		KeywordExtractorFactory factory = new KeywordExtractorFactory();
		KeywordExtractor extractor = factory.getKeywordExtractor(eloType);
		extractor.setMission(missionForUser);
		extractor.setTupleSpace(getCommandSpace());
		List<String> keywords = extractor.getKeywords(elo);
		List<KeyValuePair> keywordsWithBoost = new ArrayList<KeyValuePair>();
		for (String keyword : keywords) {
			// initially using a default boosting factor
			keywordsWithBoost.add(new KeyValuePair(keyword, "1.0"));
		}

		addKeywordsToMetadata(elo, keywordsWithBoost);
	}

	private void addKeywordsToMetadata(IELO elo, List<KeyValuePair> keywords) {
		if (keywords.isEmpty()) {
			return;
		}
		IMetadataKey keywordKey = metadataTypeManager
				.getMetadataKey(CoreRooloMetadataKeyIds.KEYWORDS.getId());
		IMetadataValueContainer agentKeywordsContainer = elo.getMetadata()
				.getMetadataValueContainer(keywordKey);
		agentKeywordsContainer.setValueList(keywords);

		// repository.updateELO(elo); //XXX this is important because an update
		// would change the version number of the elo, which will also change
		// the URI
		repository.addMetadata(elo.getUri(), elo.getMetadata());
	}

	private IELO getELO(String eloUri) {
		try {
			return repository.retrieveELO(new URI(eloUri));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getMission(String user) {
		try {
			Tuple missionTuple = getSessionSpace()
					.read(
							new Tuple(SessionAgent.MISSION, user,
									String.class));
			if (missionTuple != null) {
				return (String) missionTuple.getField(2).getValue();
			}
		} catch (TupleSpaceException e) {
			LOGGER.warn(e.getMessage());
		}
		return null;
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
