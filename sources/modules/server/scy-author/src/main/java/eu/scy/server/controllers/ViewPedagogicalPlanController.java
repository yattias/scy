package eu.scy.server.controllers;

import eu.scy.core.PedagogicalPlanPersistenceService;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.feb.2010
 * Time: 09:46:42
 */
public class ViewPedagogicalPlanController extends BaseController {

    private PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        String pedPlanId = request.getParameter("id");
        logger.info("PED PLAN ID: " + pedPlanId);
        PedagogicalPlan plan = getPedagogicalPlanPersistenceService().getPedagogicalPlan(pedPlanId);

        List agentLevels = new LinkedList();
        agentLevels.add("Low");
        agentLevels.add("Medium");
        agentLevels.add("High");

        List contentLevels = new LinkedList();
        contentLevels.add("Low");
        contentLevels.add("Medium");
        contentLevels.add("High");
        modelAndView.addObject("agentLevels", agentLevels);
        modelAndView.addObject("contentLevels", contentLevels);

        logger.info("Setting plan: " + plan.getName());
        modelAndView.addObject("pedagogicalPlan", plan);
    }


    public PedagogicalPlanPersistenceService getPedagogicalPlanPersistenceService() {
        return pedagogicalPlanPersistenceService;
    }

    public void setPedagogicalPlanPersistenceService(PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService) {
        this.pedagogicalPlanPersistenceService = pedagogicalPlanPersistenceService;
    }    
}
