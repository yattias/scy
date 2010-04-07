package eu.scy.server.controllers;

import eu.scy.core.AssignedPedagogicalPlanService;
import eu.scy.core.PedagogicalPlanPersistenceService;
import eu.scy.core.UserService;
import eu.scy.core.model.StudentUserDetails;
import eu.scy.core.model.User;
import eu.scy.core.model.impl.pedagogicalplan.AssignedPedagogicalPlanImpl;
import eu.scy.core.model.pedagogicalplan.AssignedPedagogicalPlan;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;

import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.scy.core.openfire.BuddyService;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.feb.2010
 * Time: 09:46:42
 */
public class ViewPedagogicalPlanController extends BaseController {

    private PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService;
    private AssignedPedagogicalPlanService assignedPedagogicalPlanService;
    private UserService userService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        String pedPlanId = request.getParameter("id");


        logger.info("PED PLAN ID: " + pedPlanId);
        PedagogicalPlan plan = getPedagogicalPlanPersistenceService().getPedagogicalPlan(pedPlanId);

        String action = request.getParameter("action");
        if (action != null) {
            if (action.equals("addStudent")) {
                addStudent(request.getParameter("username"), modelAndView, plan);
            } else if(action.equals("removeStudent")) {
                removeStudent(request.getParameter("username"), modelAndView, plan);
            }
        }


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

        modelAndView.addObject("pedagogicalPlan", plan);
        modelAndView.addObject("assignedPedagogicalPlans", getAssignedPedagogicalPlanService().getAssignedPedagogicalPlans(plan));
    }

    private void removeStudent(String username, ModelAndView modelAndView, PedagogicalPlan plan) {
        User user = getUserService().getUser(username);
        getAssignedPedagogicalPlanService().removeAssignedAssessment(user, plan);
    }

    private void addStudent(String username, ModelAndView modelAndView, PedagogicalPlan pedagogicalPlan) {
        User user = getUserService().getUser(username);
        StudentUserDetails details = (StudentUserDetails) user.getUserDetails();

        logger.info("Adding " + details.getUsername() + " " + details.getFirstname() + " " + details.getLastname() + " to ped plan " + pedagogicalPlan.getName() + " " + pedagogicalPlan.getId());

        getAssignedPedagogicalPlanService().assignPedagogicalPlanToUser(pedagogicalPlan, user);

    }


    public PedagogicalPlanPersistenceService getPedagogicalPlanPersistenceService() {
        return pedagogicalPlanPersistenceService;
    }

    public void setPedagogicalPlanPersistenceService(PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService) {
        this.pedagogicalPlanPersistenceService = pedagogicalPlanPersistenceService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public AssignedPedagogicalPlanService getAssignedPedagogicalPlanService() {
        return assignedPedagogicalPlanService;
    }

    public void setAssignedPedagogicalPlanService(AssignedPedagogicalPlanService assignedPedagogicalPlanService) {
        this.assignedPedagogicalPlanService = assignedPedagogicalPlanService;
    }


}
