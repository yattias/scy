package eu.scy.server.controllers.agents;

import eu.scy.core.AgentService;
import eu.scy.server.controllers.BaseController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 28.mai.2010
 * Time: 11:42:19
 * To change this template use File | Settings | File Templates.
 */
public class AgentBankController extends BaseController {

    private AgentService  agentService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        if(request.getParameter("action") != null) performAction(request);
        modelAndView.addObject("agents", getAgentService().getAgents());
        modelAndView.addObject("agentPropertyValueLevels", getAgentService().getAgentPropertyValueLevels());
    }

    private void performAction(HttpServletRequest request) {
        if(request.getParameter("action").equals("addAgentPropertyValueLevel")) addAgentPropertyValueLevel();
    }

    private void addAgentPropertyValueLevel() {
        getAgentService().addAgentPropertyValueLevel();

    }

    public AgentService getAgentService() {
        return agentService;
    }

    public void setAgentService(AgentService agentService) {
        this.agentService = agentService;
    }
}
