package eu.scy.agents.keywords;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.dgc.VMID;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import roolo.api.IRepository;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IMetadataValueContainer;
import eu.scy.agents.Mission;
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

		Mission missionForUser = getMission(user);

		KeywordExtractorFactory factory = new KeywordExtractorFactory();
		KeywordExtractor extractor = factory.getKeywordExtractor(eloType);
		extractor.setMission(missionForUser);
		extractor.setTupleSpace(getCommandSpace());
		List<String> keywords = extractor.getKeywords(elo);
		List<KeyValuePair> keywordsWithBoost = setBoostFactors(keywords, "en", missionForUser); // TODO make language dynamic

		addKeywordsToMetadata(elo, keywordsWithBoost);
	}

	private List<KeyValuePair> setBoostFactors(List<String> keywords, String language, Mission mission) {
		List<KeyValuePair> keywordsWithBoost = new ArrayList<KeyValuePair>();
		HashSet<String> addedKeywords = new HashSet<String>(keywords);
		for (String keyword : keywords) {
			VMID id = new VMID();
			try {
				keywordsWithBoost.add(new KeyValuePair(keyword, "1.0"));
				getCommandSpace().write(
						new Tuple(id.toString(), "onto", "surrounding",
								mission.getNamespace(), keyword, language));
				Tuple respTuple = getCommandSpace()
						.waitToTake(
								new Tuple(id.toString(),
										AgentProtocol.RESPONSE, String.class),
								AgentProtocol.COMMAND_EXPIRATION);
				if (respTuple != null && respTuple.getNumberOfFields() > 2) {
					String surroundedStrings = respTuple.getField(2).getValue()
							.toString();
					for (String surrounding : surroundedStrings.split(",")) {
						if (!addedKeywords.contains(surrounding)) {
							addedKeywords.add(surrounding);
							keywordsWithBoost.add(new KeyValuePair(surrounding,
									"0.5"));
						}
					}
				}
			} catch (TupleSpaceException e) {
				e.printStackTrace();
			}
		}
		return keywordsWithBoost;
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

	private Mission getMission(String user) {
		try {
			Tuple missionTuple = getSessionSpace().read(
					new Tuple(SessionAgent.MISSION, user, String.class,
							String.class));
			if (missionTuple != null) {
				String missionString = (String) missionTuple.getField(3).getValue();
				return Mission.getForName(missionString);
			}
		} catch (TupleSpaceException e) {
			LOGGER.warn(e.getMessage());
		}
		return Mission.UNKNOWN_MISSION;
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
