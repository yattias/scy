package eu.scy.agents.impl.parameter;

import eu.scy.agents.api.parameter.AgentParameter;
import eu.scy.agents.api.parameter.AgentParameterAPI;
import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.mission.RuntimeSetting;
import eu.scy.common.mission.RuntimeSettingKey;
import eu.scy.common.mission.RuntimeSettingsElo;
import eu.scy.common.mission.RuntimeSettingsEloContent;
import eu.scy.common.scyelo.RooloServices;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

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
        agentParameterAPI.getParameter(agentId, parameter);
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
        } catch ( URISyntaxException e ) {
            throw new RuntimeException(
                    "the uri of the mission settings is wrong ", e);
        }

    }

    private RuntimeSettingsElo getRuntimeSettingsElo(String missionURI)
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
        agentParameterAPI.setParameter(agentId, parameter);
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
        } catch ( URISyntaxException e ) {
            throw new RuntimeException(
                    "the uri of the mission settings is wrong ", e);
        }
    }

}
