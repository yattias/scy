package eu.scy.server.controllers;

import eu.scy.common.mission.MissionRuntimeModel;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.core.UserService;
import eu.scy.core.model.StudentUserDetails;
import eu.scy.core.model.User;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.roolo.MissionELOService;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 01.sep.2011
 * Time: 06:46:44
 * To change this template use File | Settings | File Templates.
 */
public class AddNewStudentController extends BaseController {

    private UserService userService;
    private MissionELOService missionELOService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        URI uri = getURI(request.getParameter(ELO_URI));

        dispatchAction(request, modelAndView);



        modelAndView.addObject(ELO_URI, getEncodedUri(uri.toString()));
    }

    private void dispatchAction(HttpServletRequest request, ModelAndView modelAndView) {
        String action = request.getParameter("action");
        if(action != null) {
            if(action.equals("addStudent")) {
                addStudent(request, modelAndView);
            }
        }
    }

    private void addStudent(HttpServletRequest request, ModelAndView modelAndView) {
        logger.info("Adding student:");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        URI missionURI = getURI(request.getParameter(ELO_URI));

        User newUser = getUserService().createUser(userName, password, "ROLE_STUDENT" );
        StudentUserDetails userDetails = (StudentUserDetails) newUser.getUserDetails();
        userDetails.setFirstName(firstName);
        userDetails.setLastName(lastName);
        getUserService().save(userDetails);

        addStudent(missionURI.toString(), newUser.getUserDetails().getUsername());
        modelAndView.setViewName("forward:viewPedagogicalPlan.html");
        request.setAttribute("tab", "studentTab");
    }


    private void addStudent(String missionURIString, String username) {
        try {
            long start = System.currentTimeMillis();
            URI missionURI = new URI(missionURIString);
            MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo.loadElo(missionURI, getMissionELOService());
            MissionRuntimeModel missionRuntimeModel = missionSpecificationElo.getMissionManagement().createMissionRuntimeModelElos(username);
            long totalTime = System.currentTimeMillis() - start;
            logger.info("Used freakin: " + totalTime + " millis to assign mission to funky user: " + username);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }
}
