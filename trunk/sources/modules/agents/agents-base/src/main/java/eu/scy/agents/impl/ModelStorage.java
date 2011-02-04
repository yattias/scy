package eu.scy.agents.impl;

import java.net.URI;
import java.net.URISyntaxException;

import roolo.elo.api.IELO;
import eu.scy.agents.api.IPersistentStorage;
import eu.scy.common.mission.AgentModelElo;
import eu.scy.common.mission.AgentModelEloContent;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.common.mission.MissionSpecificationEloContent;
import eu.scy.common.scyelo.RooloServices;

/**
 * The implementation of {@link IPersistentStorage}. Saves things as ELOs
 * 
 * @author Florian Schulz
 */
public class ModelStorage {

	private RooloServices rooloServices;

	/**
	 * Create a new {@link ModelStorage} instance.
	 */
	public ModelStorage(RooloServices services) {
		rooloServices = services;
	}

	public byte[] get(String mission, String language, String key) {
		MissionSpecificationElo missionSpecification = getMissionSpecification(mission);
		MissionSpecificationEloContent missionSpecificationEloContent = missionSpecification
				.getTypedContent();
		URI agentModelsEloUri = missionSpecificationEloContent
				.getAgentModelsEloUri();
		AgentModelElo agentModelElo = AgentModelElo.loadElo(agentModelsEloUri,
				rooloServices);
		AgentModelEloContent agentModelEloContent = agentModelElo
				.getTypedContent();
		URI modelEloUri = agentModelEloContent.getModelEloUri(key, language);
		IELO modelElo = rooloServices.getRepository().retrieveELO(modelEloUri);
		return modelElo.getContent().getBytes();
	}

	private MissionSpecificationElo getMissionSpecification(String mission) {
		try {
			return MissionSpecificationElo.loadElo(new URI(mission),
					rooloServices);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

}
