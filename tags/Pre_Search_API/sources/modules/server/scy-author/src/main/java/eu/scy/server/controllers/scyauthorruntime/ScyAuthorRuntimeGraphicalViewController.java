package eu.scy.server.controllers.scyauthorruntime;

import eu.scy.core.PedagogicalPlanPersistenceService;
import eu.scy.core.ScenarioService;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.server.controllers.BaseController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 18.mar.2010
 * Time: 22:51:19
 * To change this template use File | Settings | File Templates.
 */
public class ScyAuthorRuntimeGraphicalViewController extends BaseController {

    private PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService;

    public List<LearningActivitySpace> getLearningActivitySpaces() {
        return pedagogicalPlanPersistenceService.getLearningActivitySpaces((PedagogicalPlan) getModel());
    }

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {

        modelAndView.addObject("pedagogicalPlans", getPedagogicalPlanPersistenceService().getPedagogicalPlans());
        modelAndView.addObject("myLasLink", getMyLasLink());
    }

    public String getMyLasLink() {
        return "viewLASRuntimeInfo.html";
    }


    public PedagogicalPlanPersistenceService getPedagogicalPlanPersistenceService() {
        return pedagogicalPlanPersistenceService;
    }

    public void setPedagogicalPlanPersistenceService(PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService) {
        this.pedagogicalPlanPersistenceService = pedagogicalPlanPersistenceService;
    }

}