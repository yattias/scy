package eu.scy.server.controllers;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.mission.MissionRuntimeModel;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.common.mission.RuntimeSettingKey;
import eu.scy.common.scyelo.ScyElo;
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
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
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

        logger.info("VIEW STUDENTS!");

        PedagogicalPlan pedagogicalPlan = null;
        String missionURI = request.getParameter("eloURI");
        try {

            if (missionURI != null) missionURI = URLDecoder.decode(missionURI, "UTF-8");


            URI uri = null;

            try {
                uri = new URI(missionURI);
            } catch (URISyntaxException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo.loadElo(uri, getMissionELOService());

            List userNames = getMissionELOService().getAssignedUserNamesFor(missionSpecificationElo);


            List users = new LinkedList();
            for (int i = 0; i < userNames.size(); i++) {
                String s = (String) userNames.get(i);
                User user = getUserService().getUser(s);
                users.add(user);

            }
            modelAndView.addObject("users", users);
            logger.info("ADDED " + users.size() + " USERS!");


            if (!missionURI.contains("#")) {
                logger.warn("HACK WARNING HACK WARNING HACK WARNING!");
                logger.warn("HACK WARNING HACK WARNING HACK WARNING!");
                logger.warn("HACK WARNING HACK WARNING HACK WARNING!");
                logger.warn("HACK WARNING HACK WARNING HACK WARNING!");
                logger.warn("HACK WARNING HACK WARNING HACK WARNING!");
                logger.warn("HACK WARNING HACK WARNING HACK WARNING!");
                missionURI += "#2";
            }

            logger.info("MissionURL: " + missionURI);
            modelAndView.addObject("eloURI", URLEncoder.encode(missionURI, "UTF-8"));

            logger.info("DECODED URI: " + missionURI);
            if (missionURI != null && missionURI.length() > 0) {
                pedagogicalPlan = pedagogicalPlanPersistenceService.getOrCreatePedagogicalPlanFromURI(missionURI);
            } else {
                logger.warn("MISSION URI IS NULL!!");
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        String action = request.getParameter("action");
        if (action != null) {
            if (action.equals("addStudent")) {
                String username = request.getParameter("username");
                addStudent(missionURI, username, modelAndView, pedagogicalPlan);
            } else if (action.equals("removeStudent")) {
                removeStudent(request.getParameter("username"), modelAndView, pedagogicalPlan);
            }
        }

        modelAndView.addObject("pedagogicalPlan", pedagogicalPlan);
    }

    private void removeStudent(String username, ModelAndView modelAndView, PedagogicalPlan plan) {
        User user = getUserService().getUser(username);
        getAssignedPedagogicalPlanService().removeAssignedAssessment(user, plan);
    }

    private void addStudent(String missionURIString, String username, ModelAndView modelAndView, PedagogicalPlan pedagogicalPlan) {
        try {
            logger.info("LOADING URI: " + missionURIString + " ?????????? ADDING STUDENT!");
            URI missionURI = new URI(missionURIString);
            MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo.loadElo(missionURI, getMissionELOService());

            // /String username = request.getParameter("username");

            User user = getUserService().getUser(username);
            StudentUserDetails details = (StudentUserDetails) user.getUserDetails();
            MissionRuntimeModel missionRuntimeModel = missionSpecificationElo.getMissionManagement().createMissionRuntimeModelElos(username);
            logger.info("MISSION RUNTIME: " + missionRuntimeModel.getRuntimeSettingsElo().getDescription());
            RuntimeSettingKey globalMissionScaffoldingLevelKey = new RuntimeSettingKey("globalMissionScaffoldingLevel", null, null);
            String scaffolding = missionRuntimeModel.getRuntimeSettingsElo().getTypedContent().getSetting(globalMissionScaffoldingLevelKey);
            logger.info("SCAFFOLDING: " + scaffolding);

            logger.info("Adding " + details.getUsername() + " " + details.getFirstName() + " " + details.getLastName() + " to ped plan " + pedagogicalPlan.getName() + " " + pedagogicalPlan.getId());

            //getAssignedPedagogicalPlanService().assignPedagogicalPlanToUser(pedagogicalPlan, user);


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