package eu.scy.common.mission;

import java.net.URI;
import java.net.URISyntaxException;

import roolo.elo.api.IELO;
import eu.scy.common.mission.impl.BasicAgentModelEloContent;
import eu.scy.common.mission.impl.jdom.AgentModelEloContentXmlUtils;
import eu.scy.common.scyelo.ContentTypedScyElo;
import eu.scy.common.scyelo.RooloServices;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.common.scyelo.ScyEloContentCreator;

public class AgentModelElo extends ContentTypedScyElo<AgentModelEloContent> {

	private static class AgentModelEloContentCreator implements
			ScyEloContentCreator<AgentModelEloContent> {

		@Override
		public AgentModelEloContent createScyEloContent(ScyElo scyElo) {
			String xml = scyElo.getElo().getContent().getXmlString();
			if (xml == null || xml.length() == 0) {
				return new BasicAgentModelEloContent();
			}
			try {
				return AgentModelEloContentXmlUtils.agentModelFromXml(xml);
			} catch (URISyntaxException ex) {
				throw new IllegalArgumentException(
						"problems with the xml of the elo, uri: "
								+ scyElo.getUri(), ex);
			}
		}

		@Override
		public void updateEloContent(
				ContentTypedScyElo<AgentModelEloContent> scyElo) {
			scyElo.getElo()
					.getContent()
					.setXmlString(
							AgentModelEloContentXmlUtils.agentModelToXml(scyElo
									.getTypedContent()));
		}
	}

	private final static AgentModelEloContentCreator agentEloContentCreator = new AgentModelEloContentCreator();

	public AgentModelElo(IELO elo, RooloServices rooloServices) {
		super(elo, rooloServices, agentEloContentCreator,
				MissionEloType.AGENT_MODELS.getType());
	}

	public static AgentModelElo loadElo(URI uri, RooloServices rooloServices) {
		IELO elo = rooloServices.getRepository().retrieveELO(uri);
		if (elo == null) {
			return null;
		}
		return new AgentModelElo(elo, rooloServices);
	}

	public static AgentModelElo loadLastVersionElo(URI uri,
			RooloServices rooloServices) {
		IELO elo = rooloServices.getRepository().retrieveELOLastVersion(uri);
		if (elo == null) {
			return null;
		}
		return new AgentModelElo(elo, rooloServices);
	}

	public static AgentModelElo createElo(RooloServices rooloServices) {
		IELO elo = rooloServices.getELOFactory().createELO();
		elo.getMetadata()
				.getMetadataValueContainer(
						ScyElo.getTechnicalFormatKey(rooloServices))
				.setValue(MissionEloType.AGENT_MODELS.getType());
		AgentModelElo scyElo = new AgentModelElo(elo, rooloServices);
		return scyElo;
	}

}
