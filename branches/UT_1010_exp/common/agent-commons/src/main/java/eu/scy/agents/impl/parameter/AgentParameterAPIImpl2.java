package eu.scy.agents.impl.parameter;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import eu.scy.agents.api.parameter.AgentParameter;
import eu.scy.agents.api.parameter.AgentParameterAPI;
import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.mission.RuntimeSetting;
import eu.scy.common.mission.RuntimeSettingKey;
import eu.scy.common.mission.RuntimeSettingsElo;
import eu.scy.common.mission.RuntimeSettingsEloContent;
import eu.scy.common.scyelo.RooloServices;

public class AgentParameterAPIImpl2 implements AgentParameterAPI {

	private RooloServices rooloService;
	private AgentParameterAPI agentParameterAPI;

	public AgentParameterAPIImpl2(RooloServices rooloService,
			AgentParameterAPI agentParameterAPI) {
		this.rooloService = rooloService;
		this.agentParameterAPI = agentParameterAPI;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getParameter(String agentId, AgentParameter parameter) {
		String missionURI = parameter.getMission();
		try {
			RuntimeSettingsElo runtimeSettingsElo = getRuntimeSettingsElo(missionURI);
			RuntimeSettingsEloContent runtimeSettingsEloContent = runtimeSettingsElo
					.getTypedContent();

			RuntimeSettingKey runtimeSettingKey = new RuntimeSettingKey(agentId
					+ parameter.getParameterName(), parameter.getLas(),
					parameter.getELOUri());

			String setting = runtimeSettingsEloContent
					.getSetting(runtimeSettingKey);
			return (T) setting;
		} catch (URISyntaxException e) {
			throw new RuntimeException(
					"the uri of the mission settings is wrong ", e);
		}

	}

	public RuntimeSettingsElo getRuntimeSettingsElo(String missionURI)
			throws URISyntaxException {
		URI runtimeSettingsEloUri = getMissionSettingsELO(missionURI);
		RuntimeSettingsElo runtimeSettingsElo = RuntimeSettingsElo
				.loadLastVersionElo(runtimeSettingsEloUri, rooloService);
		return runtimeSettingsElo;
	}

	private URI getMissionSettingsELO(String missionURI)
			throws URISyntaxException {
		MissionRuntimeElo loadElo = MissionRuntimeElo.loadElo(new URI(
				missionURI), rooloService);
		URI runtimeSettingsEloUri = loadElo.getTypedContent()
				.getRuntimeSettingsEloUri();
		return runtimeSettingsEloUri;
	}

	@Override
	public List<String> listAgentParameter(String agentId) {
		List<String> listAgentParameter = agentParameterAPI
				.listAgentParameter(agentId);
		return listAgentParameter;
	}

	@Override
	public <T> void setParameter(String agentId, AgentParameter parameter) {
		String missionURI = parameter.getMission();
		RuntimeSettingsElo runtimeSettingsElo;
		try {
			runtimeSettingsElo = getRuntimeSettingsElo(missionURI);

			RuntimeSettingsEloContent runtimeSettingsEloContent = runtimeSettingsElo
					.getTypedContent();

			RuntimeSetting runtimeSetting = new RuntimeSetting();
			runtimeSetting.setKey(new RuntimeSettingKey(agentId
					+ parameter.getParameterName(), parameter.getLas(),
					parameter.getELOUri()));
			runtimeSetting.setValue((String) parameter.getParameterValue());

			runtimeSettingsEloContent.addSetting(runtimeSetting);
			runtimeSettingsElo.updateElo();
		} catch (URISyntaxException e) {
			throw new RuntimeException(
					"the uri of the mission settings is wrong ", e);
		}
	}

	// final IMetadataKey missionRunningKey = tbi.getMetaDataTypeManager()
	// .getMetadataKey(ScyRooloMetadataKeyIds.MISSION_RUNNING);
	// final IMetadataKey technicalFormatKey = tbi.getMetaDataTypeManager()
	// .getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
	// IQuery missionSpecificationQuery = new BasicMetadataQuery(
	// technicalFormatKey, BasicSearchOperations.EQUALS,
	// MissionEloType.MISSION_SPECIFICATIOM.getType(), null);
	// List<ISearchResult> missionSpecificationResults = tbi.getRepository()
	// .search(missionSpecificationQuery);
	// IQuery missionRuntimeQuery = new BasicMetadataQuery(technicalFormatKey,
	// BasicSearchOperations.EQUALS, MissionEloType.MISSION_RUNTIME
	// .getType(), null);
	// IQuery titleQuery = new BasicMetadataQuery(missionRunningKey,
	// BasicSearchOperations.EQUALS, userName, null);
	// IQuery myMissionRuntimeQuery = new AndQuery(missionRuntimeQuery,
	// titleQuery);
	// List<ISearchResult> missionRuntimeResults = tbi.getRepository().search(
	// myMissionRuntimeQuery);
}
