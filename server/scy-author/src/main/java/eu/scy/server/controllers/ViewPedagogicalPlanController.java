package eu.scy.server.controllers;

import eu.scy.core.AssignedPedagogicalPlanService;
import eu.scy.core.GroupService;
import eu.scy.core.PedagogicalPlanPersistenceService;
import eu.scy.core.UserService;
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
    private AssignedPedagogicalPlanService assignedPedagogicalPlanService;
    private UserService userService;
    private GroupService groupService = null;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        String pedPlanId = request.getParameter("id");
        PedagogicalPlan plan = null;
        if(pedPlanId != null) {
            logger.info("PED PLAN ID: " + pedPlanId);
            plan = getPedagogicalPlanPersistenceService().getPedagogicalPlan(pedPlanId);
        } else {
            logger.info("PEDAGOGICAL PLAN IS NULL!!");
        }
        if(getModel() != null) {
            plan = (PedagogicalPlan) getModel();
        }

        if (plan!= null)  {
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
            modelAndView.addObject("assignedPedagogicalPlansCount", getAssignedPedagogicalPlanService().getAssignedPedagogicalPlansCount(plan));
            modelAndView.addObject("pedagogicalPlanGroupsCount", getGroupService().getPedagogicalPlanGroupsCount(plan));
            modelAndView.addObject("anchorElos", getPedagogicalPlanPersistenceService().getAnchorELOs(plan));
        }
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

    public GroupService getGroupService() {
        return groupService;
    }

    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
    }
}