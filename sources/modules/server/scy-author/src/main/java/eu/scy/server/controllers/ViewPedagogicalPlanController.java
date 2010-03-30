package eu.scy.server.controllers;

import eu.scy.core.PedagogicalPlanPersistenceService;
import eu.scy.core.UserService;
import eu.scy.core.model.StudentUserDetails;
import eu.scy.core.model.User;
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
    private UserService userService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        String pedPlanId = request.getParameter("id");

        String action = request.getParameter("action");
        if(action != null) {
            if(action.equals("addStudent")) {
                addStudent(request.getParameter("username"), modelAndView);
            }
        }

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

    private void addStudent(String username, ModelAndView modelAndView) {
        User user = getUserService().getUser(username);
        StudentUserDetails details = (StudentUserDetails) user.getUserDetails();

        logger.info("Adding " + details.getUsername() + " " + details.getFirstname() + " " + details.getLastname() + " to ped plan");

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
}
