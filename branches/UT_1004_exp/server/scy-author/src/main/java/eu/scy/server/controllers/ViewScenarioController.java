package eu.scy.server.controllers;

import eu.scy.core.LASService;
import eu.scy.core.PedagogicalPlanPersistenceService;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.model.pedagogicalplan.Scenario;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 18.mar.2010
 * Time: 19:52:15
 * To change this template use File | Settings | File Templates.
 */
public class ViewScenarioController extends BaseController {

    private PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService;
    private LASService lasService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        String pedPlanId = request.getParameter("pedagogicalPlanId");
                logger.info("PED PLAN ID: " + pedPlanId);
                PedagogicalPlan plan = getPedagogicalPlanPersistenceService().getPedagogicalPlan(pedPlanId);
                setModel(plan.getScenario());
                String publish = request.getParameter("publish");
                if (publish != null) {
                    if (publish.equals("true")) {
                        plan.setPublished(true);
                    } else {
                        plan.setPublished(false);
                    }
                    getPedagogicalPlanPersistenceService().save(plan);
                }
                logger.info("Setting plan: " + plan.getName());
                modelAndView.addObject("pedagogicalPlan", plan);
                modelAndView.addObject("learningGoals", ((Scenario)getModel()).getLearningGoals());

                modelAndView.addObject("learningActivitySpaces", getLasService().getAllLearningActivitySpacesForScenario(plan.getScenario()));
    }


    public PedagogicalPlanPersistenceService getPedagogicalPlanPersistenceService() {
        return pedagogicalPlanPersistenceService;
    }

    public void setPedagogicalPlanPersistenceService(PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService) {
        this.pedagogicalPlanPersistenceService = pedagogicalPlanPersistenceService;
    }

    public LASService getLasService() {
        return lasService;
    }

    public void setLasService(LASService lasService) {
        this.lasService = lasService;
    }
}
