package eu.scy.server.controllers;

import eu.scy.agents.api.parameter.AgentParameter;
import eu.scy.agents.api.parameter.AgentParameterAPI;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.core.roolo.MissionELOService;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 09.feb.2011
 * Time: 22:42:25
 * To change this template use File | Settings | File Templates.
 */
public class AgentConfigurationController extends BaseController{

    private AgentParameterAPI agentParameterAPI;
    private MissionELOService missionELOService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        try {
            String uriParam = request.getParameter("eloURI");
            logger.info("*** **** MISSION ELO URI IS : " + uriParam);
            URI uri = new URI(uriParam);

            MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo.loadElo(uri, getMissionELOService());
            modelAndView.addObject("missionSpecificationTransporter", getMissionELOService().getWebSafeTransporter(missionSpecificationElo));
            modelAndView.addObject("agentParameterAPI", getAgentParameterAPI());
            modelAndView.addObject("anchorElos", getMissionELOService().getWebSafeTransporters(getMissionELOService().getAnchorELOs(missionSpecificationElo)));

            missionSpecificationElo.getTypedContent().getMissionMapModelEloUri();

            logger.info("SETTING PARAMETERS FOR VOTAT!");
            AgentParameter agentParameter = new AgentParameter();
            agentParameter.setParameterName("funkyParam");
            agentParameter.setParameterValue("42");
            getAgentParameterAPI().setParameter("votat", agentParameter);
            logger.info("DONE SETTING PARAMETERS FOR VOTAT!");

            //AgentParameter aParm = getAgentParameterAPI().getParameter("votat", agentParameter);
            //logger.info(aParm.getParameterName() + " " + aParm.getParameterValue());
        } catch (URISyntaxException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public AgentParameterAPI getAgentParameterAPI() {
        return agentParameterAPI;
    }

    public void setAgentParameterAPI(AgentParameterAPI agentParameterAPI) {
        this.agentParameterAPI = agentParameterAPI;
    }

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }
}
