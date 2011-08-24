package eu.scy.server.controllers;

import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
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
 * Date: 10.nov.2010
 * Time: 12:47:13
 * To change this template use File | Settings | File Templates.
 */
public class AjaxELOSliderController extends AbstractAjaxELOController {

    private AgentParameterAPI agentParameterAPI;
    private MissionELOService missionELOService;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        String property = request.getParameter(PROPERTY);
        String uri = request.getParameter(URI);
        String value = request.getParameter("value");
        logger.info("VALUE:" + value);
        logger.info("URI: " + uri);
        logger.info("PROPERTY: " + property);

        if(property.equals("globalMissionScaffoldingLevel")) {
            doTheCrazyHack(uri, value);
        }

        executeSetter(uri, property, value);

        IAction action = new Action();
        action.setType("value_set_from_slider");
        action.setTimeInMillis(System.currentTimeMillis());
        action.setUser(getCurrentUserName(request));
        action.addContext(ContextConstants.tool, "portal");
        action.addContext(ContextConstants.eloURI, uri);
        action.addAttribute("property", property);
        action.addAttribute("value", value);

        getActionLoggingService().logAction(action);

        ModelAndView modelAndView = new ModelAndView();
        return modelAndView;
    }

    private void doTheCrazyHack(String uri, String value) {
        try {
            uri = URLDecoder.decode(uri, "utf-8");
            java.net.URI _uri = new java.net.URI(uri);
            MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo.loadElo(_uri, getMissionELOService());
            AgentParameter agentParameter = new AgentParameter(missionSpecificationElo.getTitle(), "globalScaffoldingLevel");
            agentParameter.setParameterValue(value);
            getAgentParameterAPI().setParameter("global", agentParameter);
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
