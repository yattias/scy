package eu.scy.server.controllers;

import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.agents.api.parameter.AgentParameter;
import eu.scy.agents.api.parameter.AgentParameterAPI;
import eu.scy.server.controllers.xml.ActionLoggingService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.feb.2011
 * Time: 00:14:06
 * To change this template use File | Settings | File Templates.
 */
public class AgentParameterTextFieldController extends AbstractController {

    private AgentParameterAPI agentParameterAPI;
    private ActionLoggingService actionLoggingService;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String missionURI = request.getParameter("missionURI");
        if(missionURI != null) missionURI = URLDecoder.decode(missionURI, "UTF-8");
        String agentId = request.getParameter("agentId");
        String parameterName = request.getParameter("parameterName");
        String value = request.getParameter("value");
        String las = request.getParameter("las");
        if(las == "" ) las = null;

        AgentParameter agentParameter = new AgentParameter();
        agentParameter.setMission(missionURI);
        agentParameter.setParameterValue(value);
        agentParameter.setParameterName(parameterName);
        if(las != null) agentParameter.setLas(las);
        getAgentParameterAPI().setParameter(agentId, agentParameter);

        logger.info("VALUE: " +value + " parmeter: " + parameterName + " Agent: " + agentId + " MISSION: " + missionURI + " LAS: " + las);

        IAction action = new Action();
        action.setType("set_value_on_agent");
        action.setTimeInMillis(System.currentTimeMillis());
        action.setUser(getCurrentUserName(request));
        action.addContext(ContextConstants.tool, "portal");
        action.addContext(ContextConstants.eloURI, missionURI);
        action.addAttribute("property", parameterName);
        action.addAttribute("value", value);
        action.addAttribute("agent" , agentId);
        getActionLoggingService().logAction(action);

        ModelAndView modelAndView = new ModelAndView();
        return modelAndView;

    }

    public AgentParameterAPI getAgentParameterAPI() {
        return agentParameterAPI;
    }

    public void setAgentParameterAPI(AgentParameterAPI agentParameterAPI) {
        this.agentParameterAPI = agentParameterAPI;
    }

    public ActionLoggingService getActionLoggingService() {
        return actionLoggingService;
    }

    public void setActionLoggingService(ActionLoggingService actionLoggingService) {
        this.actionLoggingService = actionLoggingService;
    }

    public String getCurrentUserName(HttpServletRequest request) {
       org.springframework.security.userdetails.User user = (org.springframework.security.userdetails.User) request.getSession().getAttribute("CURRENT_USER");
       return user.getUsername();
   }

}
