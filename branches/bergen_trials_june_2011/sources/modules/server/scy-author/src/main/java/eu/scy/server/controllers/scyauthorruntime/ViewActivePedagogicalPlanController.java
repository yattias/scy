package eu.scy.server.controllers.scyauthorruntime;

import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.core.UserService;
import eu.scy.core.model.User;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.server.controllers.BaseController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 30.mar.2010
 * Time: 08:20:16
 */
public class ViewActivePedagogicalPlanController extends BaseController {

    private MissionELOService missionELOService;
    private UserService userService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {

        MissionSpecificationElo missionSpecificationElo = (MissionSpecificationElo) getScyElo();
        List userNames = getMissionELOService().getAssignedUserNamesFor(missionSpecificationElo);

        List users = new LinkedList();
        for (int i = 0; i < userNames.size(); i++) {
            String s = (String) userNames.get(i);
            User user = getUserService().getUser(s);
            if (user != null && user.getUserDetails() != null) {
                users.add(user);
                logger.info("ADDED: " + user.getUserDetails().getUsername());
            }


        }
        modelAndView.addObject("users", users);
        String missionURI = request.getParameter("eloURI");
        try {
            modelAndView.addObject("missionURI", URLEncoder.encode(missionURI, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
