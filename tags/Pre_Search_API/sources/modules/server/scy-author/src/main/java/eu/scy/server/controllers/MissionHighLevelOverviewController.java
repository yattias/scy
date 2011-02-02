package eu.scy.server.controllers;

import eu.scy.common.mission.Las;
import eu.scy.common.mission.MissionModelElo;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.LASService;
import eu.scy.core.PedagogicalPlanPersistenceService;
import eu.scy.core.UserService;
import eu.scy.core.model.User;
import eu.scy.core.roolo.MissionELOService;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 05.okt.2010
 * Time: 11:15:34
 * To change this template use File | Settings | File Templates.
 */
public class MissionHighLevelOverviewController extends BaseController {

    private LASService lasService;
    private PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService;
    private UserService userService;
    private MissionELOService missionELOService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {

        String action= request.getParameter("action");
        if(action != null) {
            if(action.equals("flipObligatoryInPortfolio")) flipObligatoryInPortfolio(request, modelAndView);
        }

        try {
            MissionSpecificationElo missionSpecificationElo = (MissionSpecificationElo) getScyElo();
            if (missionSpecificationElo != null) {
                modelAndView.addObject("anchorElos", getMissionELOService().getWebSafeTransporters(getMissionELOService().getAnchorELOs(missionSpecificationElo)));
                modelAndView.addObject("eloWrapper", getMissionELOService().getWebSafeTransporter(getScyElo()));
                modelAndView.addObject("rooloServices", getMissionELOService());
            } else {
                logger.info("DID NOT FIND THE MISSION SPECIFICATION!!!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void flipObligatoryInPortfolio(HttpServletRequest request, ModelAndView modelAndView) {
        logger.info("Flipping anchor elo:" + request.getParameter("anchorElo"));
        try {
            URI uri = new URI(request.getParameter("anchorElo"));
            ScyElo scyElo = ScyElo.loadLastVersionElo(uri, getMissionELOService());

            Boolean currentStatus = scyElo.getObligatoryInPortfolio();
            if(currentStatus == null) currentStatus = false;

            logger.info("MY CURRENT STATUS IS: " + currentStatus);

            scyElo.setObligatoryInPortfolio(!currentStatus);
            scyElo.updateElo();

            logger.info("UPDATED MY STATUS TO : " + scyElo.getObligatoryInPortfolio());


        } catch (URISyntaxException e) {
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
