package eu.scy.server.controllers.components.realtime;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.core.UserService;
import eu.scy.core.model.User;
import eu.scy.core.model.runtime.AbstractRuntimeAction;
import eu.scy.core.model.runtime.EloRuntimeAction;
import eu.scy.core.model.runtime.LASRuntimeAction;
import eu.scy.core.model.runtime.ToolRuntimeAction;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.core.roolo.RuntimeELOService;
import eu.scy.core.runtime.RuntimeService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 09.apr.2010
 * Time: 10:33:02
 * To change this template use File | Settings | File Templates.
 */
public class CurrentStudentActivityController extends AbstractController {

    private RuntimeService runtimeService;
    private UserService userService;
    private RuntimeELOService runtimeELOService;
    private MissionELOService missionELOService;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        long start = System.currentTimeMillis();
        logger.debug("LOADING CURRENT STUDENT ACTIVITY!");
        ModelAndView modelAndView = new ModelAndView();
        String status = "<i>Initializing</i>";
        String username = httpServletRequest.getParameter("username");
        String missionURI = httpServletRequest.getParameter("missionURI");
        MissionSpecificationElo missionSpecificationElo = null;

        MissionRuntimeElo missionRuntimeElo = null;

        try {
            missionSpecificationElo = MissionSpecificationElo.loadLastVersionElo(new URI(missionURI), getMissionELOService());
        } catch (URISyntaxException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        logger.info("MISSION : " + missionURI);
        if (username != null) {
            User user = getUserService().getUser(username);
            List missionRuntimeElos =  getRuntimeELOService().getRuntimeElosForUser(username);
            for (int i = 0; i < missionRuntimeElos.size(); i++) {
                MissionRuntimeElo mre = (MissionRuntimeElo) missionRuntimeElos.get(i);
                if(mre.getTitle().equals(missionSpecificationElo.getTitle()));
                status = mre.getTitle();
                missionRuntimeElo  = mre;
            }
        }

        modelAndView.addObject("status", status);

        logger.debug("used " + (System.currentTimeMillis() - start) + " millis to prepare runtime activity for user " + username);
        return modelAndView;
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

    public RuntimeELOService getRuntimeELOService() {
        return runtimeELOService;
    }

    public void setRuntimeELOService(RuntimeELOService runtimeELOService) {
        this.runtimeELOService = runtimeELOService;
    }

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }
}
