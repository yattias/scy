package eu.scy.server.controllers;

import eu.scy.agents.api.parameter.AgentParameter;
import eu.scy.agents.api.parameter.AgentParameterAPI;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.core.roolo.MissionELOService;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 03.nov.2010
 * Time: 13:13:05
 * To change this template use File | Settings | File Templates.
 */
public class AjaxELOTextFieldController extends AbstractAjaxELOController {
    private AgentParameterAPI agentParameterAPI;
    private MissionELOService missionELOService;


    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        String property = httpServletRequest.getParameter(PROPERTY);
        String uri = httpServletRequest.getParameter(URI);
        String value = httpServletRequest.getParameter("value");
        logger.info("VALUE:" + value);
        logger.info("URI: " + uri);
        logger.info("PROPERTY: " + property);

        if(property.equals("groupingAgentMinimumUsers")) {
            doTheCrazyHackMinGroupSize(uri, value);
        } else if(property.equals("groupingAgentMaximumUsers"))  {
            doTheCrazyHackMaxGroupSize(uri, value);
        } else if(property.equals("groupingAgentPercent")) {
            doTheCrazyHackPercentageAvailable(uri, value);
        }


        executeSetter(uri, property, value);
        ModelAndView modelAndView = new ModelAndView();
        return modelAndView;

    }


    private void doTheCrazyHackPercentageAvailable(String uri, String value) {
        try {
            uri = URLDecoder.decode(uri, "utf-8");
            java.net.URI _uri = new java.net.URI(uri);
            MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo.loadElo(_uri, getMissionELOService());
            AgentParameter agentParameter = new AgentParameter(missionSpecificationElo.getTitle(), "PercentageAvailable");
            agentParameter.setParameterValue(value);
            getAgentParameterAPI().setParameter("GroupFormationAgent", agentParameter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doTheCrazyHackMinGroupSize(String uri, String value) {
        try {
            uri = URLDecoder.decode(uri, "utf-8");
            java.net.URI _uri = new java.net.URI(uri);
            MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo.loadElo(_uri, getMissionELOService());
            AgentParameter agentParameter = new AgentParameter(missionSpecificationElo.getTitle(), "MinGroupSize");
            agentParameter.setParameterValue(value);
            getAgentParameterAPI().setParameter("GroupFormationAgent", agentParameter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doTheCrazyHackMaxGroupSize(String uri, String value) {
        try {
            uri = URLDecoder.decode(uri, "utf-8");
            java.net.URI _uri = new java.net.URI(uri);
            MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo.loadElo(_uri, getMissionELOService());
            AgentParameter agentParameter = new AgentParameter(missionSpecificationElo.getTitle(), "MaxGroupSize");
            agentParameter.setParameterValue(value);
            getAgentParameterAPI().setParameter("GroupFormationAgent", agentParameter);
        } catch (Exception e) {
            e.printStackTrace();
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
