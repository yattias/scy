package eu.scy.server.controllers;

import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.AssignedPedagogicalPlanService;
import eu.scy.core.GroupService;
import eu.scy.core.PedagogicalPlanPersistenceService;
import eu.scy.core.UserService;
import eu.scy.core.model.User;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.scy.core.roolo.MissionELOService;
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
    private MissionELOService missionELOService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {

        try {

            String uriParam = request.getParameter("uri");
            logger.info("URI IS : " + uriParam);
            URI uri = new URI(uriParam);

            MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo.loadElo(uri, getMissionELOService());


            Integer globalScaffoldingLevel = getMissionELOService().getGlobalMissionScaffoldingLevel(missionSpecificationElo);

            modelAndView.addObject("missionSpecificationTransporter", getMissionELOService().getWebSafeTransporter(missionSpecificationElo));
            modelAndView.addObject("missionGlobalScaffoldingLevel", globalScaffoldingLevel);

            String action = request.getParameter("action");
            if (action != null) {
                if (action.equals("increaseScaffoldingLevel")) {
                    increaseScaffoldingLevel(request, response, modelAndView, missionSpecificationElo);

                }

            }

            List agentLevels = new LinkedList();
            agentLevels.add("Low");
            agentLevels.add("Medium");
            agentLevels.add("High");
            modelAndView.addObject("agentLevels", agentLevels);

            modelAndView.addObject("scaffoldingLevel", globalScaffoldingLevel);
            modelAndView.addObject("rooloServices", getMissionELOService());


        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        /*String pedPlanId = request.getParameter("id");
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
             modelAndView.addObject("author", getCurrentUser(request).getUserDetails().hasGrantedAuthority("ROLE_AUTHOR"));
         }
        */
    }

    private void increaseScaffoldingLevel(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView, ScyElo elo) {
        logger.info("INCREASING SCAFFOLDING LEVEL for " + elo.getTitle());
        MissionSpecificationElo missionSpecificationElo = (MissionSpecificationElo) elo;
        logger.info("LEVEL: " + getMissionELOService().getGlobalMissionScaffoldingLevel(missionSpecificationElo));
        getMissionELOService().setGlobalMissionScaffoldingLevel((MissionSpecificationElo) elo, (4 + getMissionELOService().getGlobalMissionScaffoldingLevel(missionSpecificationElo)));
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

    public String getCurrentUserName(HttpServletRequest request) {
        org.springframework.security.userdetails.User user = (org.springframework.security.userdetails.User) request.getSession().getAttribute("CURRENT_USER");
        logger.info("UserName: " + user.getUsername());
        return user.getUsername();
    }

    public User getCurrentUser(HttpServletRequest request) {
        return getUserService().getUser(getCurrentUserName(request));
    }

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }
}