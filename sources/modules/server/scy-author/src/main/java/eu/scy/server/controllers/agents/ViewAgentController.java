package eu.scy.server.controllers.agents;

import eu.scy.core.AgentService;
import eu.scy.core.model.impl.pedagogicalplan.AgentPropertyImpl;
import eu.scy.core.model.pedagogicalplan.Agent;
import eu.scy.core.model.pedagogicalplan.AgentProperty;
import eu.scy.server.controllers.BaseController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 28.mai.2010
 * Time: 12:37:20
 * To change this template use File | Settings | File Templates.
 */
public class ViewAgentController extends BaseController {

    private AgentService agentService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        Agent agent = null;
        if(request.getParameter("id") != null) agent= getAgentService().getAgent(request.getParameter("id"));
        if(agent != null && request.getParameter("action") != null){
             if(request.getParameter("action").equals("addProperty")) {
                addParameter(agent);
             } else if(request.getParameter("action").equals("addPropertyValue")) {
                 AgentProperty agentProperty = getAgentService().getAgentProperty(request.getParameter("property"));
                 addPropertyValue(agentProperty);
             }
            setModel(agent);
        }



    }

    private void addPropertyValue(AgentProperty agentProperty) {
        getAgentService().addPropertyValue(agentProperty);
    }

    private void addParameter(Agent agent) {
        getAgentService().addAgentProperty(agent);
    }

    public AgentService getAgentService() {
        return agentService;
    }

    public void setAgentService(AgentService agentService) {
        this.agentService = agentService;
    }
}
