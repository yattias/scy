package eu.scy.server.controllers;

import eu.scy.core.LASService;
import eu.scy.core.PedagogicalPlanPersistenceService;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.model.pedagogicalplan.Scenario;
import eu.scy.core.model.pedagogicalplan.TeacherRoleType;
import eu.scy.core.model.pedagogicalplan.WorkArrangementType;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind
 * Date: 8.apr.2010
 * Time: 12:02:09
 */
public class ViewAllScenarioActivitiesController extends BaseController {
    private PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService;
    private LASService lasService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        String pedPlanId = request.getParameter("pedagogicalPlanId");
        logger.info("PED PLAN ID: " + pedPlanId);
        PedagogicalPlan plan = getPedagogicalPlanPersistenceService().getPedagogicalPlan(pedPlanId);
        setModel(plan.getScenario());

        logger.info("Setting plan: " + plan.getName());
        modelAndView.addObject("pedagogicalPlan", plan);
        modelAndView.addObject("learningGoals", ((Scenario) getModel()).getLearningGoals());

        modelAndView.addObject("learningActivitySpaces", getLasService().getAllLearningActivitySpacesForScenario(plan.getScenario()));

        List teacherRoles = new LinkedList();
        teacherRoles.add(TeacherRoleType.ACTIVATOR);
        teacherRoles.add(TeacherRoleType.FACILITATOR);
        teacherRoles.add(TeacherRoleType.OBSERVER);

        modelAndView.addObject("teacherRoles", teacherRoles);

        List workArrangement = new LinkedList();
        workArrangement.add(WorkArrangementType.INDIVIDUAL);
        workArrangement.add(WorkArrangementType.GROUP);
        workArrangement.add(WorkArrangementType.PEER_TO_PEER);

        modelAndView.addObject("workArrangement", workArrangement);
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