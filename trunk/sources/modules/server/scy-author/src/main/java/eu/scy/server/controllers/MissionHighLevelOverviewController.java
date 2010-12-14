package eu.scy.server.controllers;

import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.core.LASService;
import eu.scy.core.PedagogicalPlanPersistenceService;
import eu.scy.core.UserService;
import eu.scy.core.model.User;
import eu.scy.core.roolo.MissionELOService;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URLDecoder;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 05.okt.2010
 * Time: 11:15:34
 * To change this template use File | Settings | File Templates.
 */
public class MissionHighLevelOverviewController extends BaseController{

    private LASService lasService;
    private PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService;
    private UserService userService;
    private MissionELOService missionELOService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        try {
            String missionSpecificationUri = request.getParameter("missionSpecificationUri");
            missionSpecificationUri = URLDecoder.decode(missionSpecificationUri, "UTF-8");
            logger.info("loading missino specification: " + missionSpecificationUri);
            URI uri = new URI(missionSpecificationUri);
            MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo.loadLastVersionElo(uri, getMissionELOService());
            if(missionSpecificationElo != null) {
                modelAndView.addObject("learningActivitySpaces", getMissionELOService().getLasses(missionSpecificationElo));
            } else {
                logger.info("DID NOT FIND THE MISSION SPECIFICATION!!!");
            }

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public LASService getLasService() {
        return lasService;
    }

    public void setLasService(LASService lasService) {
        this.lasService = lasService;
    }

    public PedagogicalPlanPersistenceService getPedagogicalPlanPersistenceService() {
        return pedagogicalPlanPersistenceService;
    }

    public void setPedagogicalPlanPersistenceService(PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService) {
        this.pedagogicalPlanPersistenceService = pedagogicalPlanPersistenceService;
    }

    public User getCurrentUser(HttpServletRequest request) {
        return getUserService().getUser(getCurrentUserName(request));
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
