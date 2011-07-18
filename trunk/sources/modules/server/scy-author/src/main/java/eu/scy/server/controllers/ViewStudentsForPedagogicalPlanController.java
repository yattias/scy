package eu.scy.server.controllers;

import eu.scy.common.mission.MissionRuntimeModel;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.common.mission.RuntimeSettingKey;
import eu.scy.core.AssignedPedagogicalPlanService;
import eu.scy.core.PedagogicalPlanPersistenceService;
import eu.scy.core.UserService;
import eu.scy.core.model.StudentUserDetails;
import eu.scy.core.model.User;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.scy.core.roolo.MissionELOService;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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
        String message = (String) request.getSession().getAttribute("message");

        PedagogicalPlan pedagogicalPlan = null;
        String missionURI = request.getParameter(ELO_URI);
        try {

            if (missionURI != null) missionURI = URLDecoder.decode(missionURI, "UTF-8");


            URI uri = null;

            try {
                uri = new URI(missionURI);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo.loadElo(uri, getMissionELOService());

            String action = request.getParameter("action");
            if (action != null) {
                if (action.equals("addStudent")) {
                    String username = request.getParameter("username");
                    addStudent(missionURI, username, modelAndView, pedagogicalPlan);
                    modelAndView.setViewName("forward:viewPedagogicalPlan.html");
                    return;
                } else if (action.equals("removeStudent")) {
                    removeStudent(request.getParameter("username"), modelAndView, pedagogicalPlan);
                } else if(action.equals("addMultipleUsers")) {
                    addMultipleStudents(missionURI);
                }
            }
            


            List userNames = getMissionELOService().getAssignedUserNamesFor(missionSpecificationElo);

            // sort usernames
            Collections.sort(userNames);

            List users = new LinkedList();
            for (int i = 0; i < userNames.size(); i++) {
                String s = (String) userNames.get(i);
                User user = getUserService().getUser(s);
                users.add(user);

            }
            String encodedURI = getEncodedUri(request.getParameter(ELO_URI));

            modelAndView.addObject("users", users);
            modelAndView.addObject("eloURI", encodedURI);

            logger.info("DECODED URI: " + missionURI);
            if (missionURI != null && missionURI.length() > 0) {
                pedagogicalPlan = pedagogicalPlanPersistenceService.getOrCreatePedagogicalPlanFromURI(missionURI);
            } else {
                logger.warn("MISSION URI IS NULL!!");
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }



        modelAndView.addObject("pedagogicalPlan", pedagogicalPlan);
    }

    private void addMultipleStudents(String missionURI) {
        logger.info("ADDING MULTIPLE STUDENTS!");
        for(int counter = 0; counter < 30; counter++) {
            String userName = "uib" + counter;
            User user = getUserService().createUser(userName, userName, "ROLE_STUDENT");
            if(user != null) {
                addStudent(missionURI, user.getUserDetails().getUsername(), null, null);
            }
            logger.info("ADDED STUDENT: " + user.getUserDetails().getUsername());
        }

    }

    private void removeStudent(String username, ModelAndView modelAndView, PedagogicalPlan plan) {
        User user = getUserService().getUser(username);
        getAssignedPedagogicalPlanService().removeAssignedAssessment(user, plan);
    }

    private void addStudent(String missionURIString, String username, ModelAndView modelAndView, PedagogicalPlan pedagogicalPlan) {
        try {
            long start = System.currentTimeMillis();
            logger.info("LOADING URI: " + missionURIString + " ?????????? ADDING STUDENT!");
            URI missionURI = new URI(missionURIString);
            MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo.loadElo(missionURI, getMissionELOService());
            MissionRuntimeModel missionRuntimeModel = missionSpecificationElo.getMissionManagement().createMissionRuntimeModelElos(username);
            long totalTime = System.currentTimeMillis() - start;
            logger.info("ASSIGNING STUDENT: " + username + " TO MISSION " + missionSpecificationElo.getTitle() + " TOOK " + totalTime + " MILLIS!");
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