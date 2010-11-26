package eu.scy.server.controllers;

import eu.scy.common.mission.MissionRuntimeModel;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.core.AssignedPedagogicalPlanService;
import eu.scy.core.PedagogicalPlanPersistenceService;
import eu.scy.core.UserService;
import eu.scy.core.model.StudentUserDetails;
import eu.scy.core.model.User;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.scy.server.roolo.MissionELOService;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.feb.2010
 * Time: 09:46:42
 */
public class ViewStudentsForPedagogicalPlanController extends BaseController {

    private PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService;
    private AssignedPedagogicalPlanService assignedPedagogicalPlanService;
    private UserService userService;
    private MissionELOService missionELOService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {

        logger.info("VIEW STUDENTS!");

        PedagogicalPlan pedagogicalPlan = null;
        try {
            String missionURI = request.getParameter("eloURI");
            logger.info("MissionURL: " + missionURI);
            if (missionURI != null) missionURI = URLDecoder.decode(missionURI, "UTF-8");

            if (missionURI != null && missionURI.length() > 0) {
                pedagogicalPlan = pedagogicalPlanPersistenceService.getOrCreatePedagogicalPlanFromURI(missionURI);
            } else {
                logger.warn("MISSION URI IS NULL!!");
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        if (pedagogicalPlan != null) {
            String action = request.getParameter("action");
            if (action != null) {
                if (action.equals("addStudent")) {
                    addStudent(request, modelAndView, pedagogicalPlan);
                } else if (action.equals("removeStudent")) {
                    removeStudent(request.getParameter("username"), modelAndView, pedagogicalPlan);
                }
            }

            modelAndView.addObject("pedagogicalPlan", pedagogicalPlan);
        }
    }

    private void removeStudent(String username, ModelAndView modelAndView, PedagogicalPlan plan) {
        User user = getUserService().getUser(username);
        getAssignedPedagogicalPlanService().removeAssignedAssessment(user, plan);
    }

    private void addStudent(HttpServletRequest request, ModelAndView modelAndView, PedagogicalPlan pedagogicalPlan) {
        try {
            String muri = request.getParameter("eloURI");
            if (muri != null) {
                muri = URLDecoder.decode(muri, "UTF-8");
                URI missionURI = new URI(muri);
                MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo.loadElo(missionURI, getMissionELOService());
                String username = request.getParameter("username");

                User user = getUserService().getUser(username);
                StudentUserDetails details = (StudentUserDetails) user.getUserDetails();
                MissionRuntimeModel missionRuntimeModel = missionSpecificationElo.getMissionManagement().createMissionRuntimeModelElos(username);

                logger.info("Adding " + details.getUsername() + " " + details.getFirstName() + " " + details.getLastName() + " to ped plan " + pedagogicalPlan.getName() + " " + pedagogicalPlan.getId());

                //getAssignedPedagogicalPlanService().assignPedagogicalPlanToUser(pedagogicalPlan, user);
            }


        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
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

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }
}