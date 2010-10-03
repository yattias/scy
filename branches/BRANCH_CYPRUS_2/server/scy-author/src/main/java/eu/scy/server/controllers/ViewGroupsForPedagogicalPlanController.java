package eu.scy.server.controllers;

import eu.scy.core.GroupService;
import eu.scy.core.PedagogicalPlanPersistenceService;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.feb.2010
 * Time: 09:46:42
 */
public class ViewGroupsForPedagogicalPlanController extends BaseController {

    private PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService;
    private GroupService groupService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        String pedPlanId = request.getParameter("id");
        PedagogicalPlan plan = null;
        if(pedPlanId != null) {
            logger.info("PED PLAN ID: " + pedPlanId);
            plan = getPedagogicalPlanPersistenceService().getPedagogicalPlan(pedPlanId);
        }
        if(getModel() != null) {
            plan = (PedagogicalPlan) getModel();
        }

        if (plan!= null)  {
            /*String action = request.getParameter("action");
            if (action != null) {
                if (action.equals("addStudent")) {
                    addStudent(request.getParameter("username"), modelAndView, plan);
                } else if (action.equals("removeStudent")) {
                    removeStudent(request.getParameter("username"), modelAndView, plan);
                }
            } */

            modelAndView.addObject("pedagogicalPlan", plan);
        }
    }

    /*private void removeStudent(String username, ModelAndView modelAndView, PedagogicalPlan plan) {
        User user = getUserService().getUser(username);
        getAssignedPedagogicalPlanService().removeAssignedAssessment(user, plan);
    }

    private void addStudent(String username, ModelAndView modelAndView, PedagogicalPlan pedagogicalPlan) {
        User user = getUserService().getUser(username);
        StudentUserDetails details = (StudentUserDetails) user.getUserDetails();

        logger.info("Adding " + details.getUsername() + " " + details.getFirstname() + " " + details.getLastname() + " to ped plan " + pedagogicalPlan.getName() + " " + pedagogicalPlan.getId());

        getAssignedPedagogicalPlanService().assignPedagogicalPlanToUser(pedagogicalPlan, user);

    } */

    public PedagogicalPlanPersistenceService getPedagogicalPlanPersistenceService() {
        return pedagogicalPlanPersistenceService;
    }

    public void setPedagogicalPlanPersistenceService(PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService) {
        this.pedagogicalPlanPersistenceService = pedagogicalPlanPersistenceService;
    }    

    public GroupService getGroupService() {
        return groupService;
    }

    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
    }
}