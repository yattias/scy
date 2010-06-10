package eu.scy.server.controllers.scyfeedback;

import eu.scy.core.AssignedPedagogicalPlanService;
import eu.scy.core.ELORefService;
import eu.scy.core.MissionService;
import eu.scy.core.UserService;
import eu.scy.core.model.ELORef;
import eu.scy.core.model.User;
import eu.scy.core.model.impl.ELORefImpl;
import eu.scy.core.model.pedagogicalplan.AssignedPedagogicalPlan;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.runtime.RuntimeService;
import eu.scy.server.controllers.BaseController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.jun.2010
 * Time: 12:05:14
 * To change this template use File | Settings | File Templates.
 */
public class UploadELOForFeedbackFormController extends BaseController {

    private RuntimeService runtimeService;
    private UserService userService;
    private AssignedPedagogicalPlanService assignedPedagogicalPlanService;
    private ELORefService eloRefService;
    private MissionService missionService;


    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        String username = request.getParameter("username");
        String action = request.getParameter("action");

        if(action != null) {
            if(action.equals("addNewEloRef")) addNewEloRef(request);
        }

        if(username != null) {
            User user = getUserService().getUser(username);

            String currentElo = getRuntimeService().getCurrentELO(user);
            String tool = getRuntimeService().getCurrentTool(user);
            String las = getRuntimeService().getCurrentLAS(user);

            List assignedPedagogicalPlans = getAssignedPedagogicalPlanService().getAssignedPedagogicalPlans(user);

            List missions = new LinkedList();
            for (int i = 0; i < assignedPedagogicalPlans.size(); i++) {
                AssignedPedagogicalPlan assignedPedagogicalPlan = (AssignedPedagogicalPlan) assignedPedagogicalPlans.get(i);
                missions.add(assignedPedagogicalPlan.getPedagogicalPlan().getMission());
            }

            modelAndView.addObject("missions", missions);



            modelAndView.addObject("currentELO", currentElo);
            modelAndView.addObject("currentTool", tool);
            modelAndView.addObject("currentLas", las);


            modelAndView.addObject("currentUser", user);

        }
    }

    private void addNewEloRef(HttpServletRequest request) {
        logger.info("ADDING NEW ELO REF!!");

        User user = getUserService().getUser(request.getParameter("username"));

        ELORef eloRef = new ELORefImpl();
        eloRef.setAuthor(user);
        eloRef.setTool(request.getParameter("tool"));
        eloRef.setELOURI(request.getParameter("productName"));
        eloRef.setTitle(request.getParameter("productName"));
        eloRef.setMission(getMissionService().getMission(request.getParameter("mission")));
    }

    public RuntimeService getRuntimeService() {
        return runtimeService;
    }

    public void setRuntimeService(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
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

    public ELORefService getEloRefService() {
        return eloRefService;
    }

    public void setEloRefService(ELORefService eloRefService) {
        this.eloRefService = eloRefService;
    }

    public MissionService getMissionService() {
        return missionService;
    }

    public void setMissionService(MissionService missionService) {
        this.missionService = missionService;
    }
}
