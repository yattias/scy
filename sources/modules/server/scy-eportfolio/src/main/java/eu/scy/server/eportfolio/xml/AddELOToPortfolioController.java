package eu.scy.server.eportfolio.xml;

import eu.scy.core.roolo.MissionELOService;
import eu.scy.server.controllers.BaseController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.jan.2011
 * Time: 14:31:38
 * To change this template use File | Settings | File Templates.
 */
public class AddELOToPortfolioController extends BaseController {

    private MissionELOService missionELOService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        String missionURI = request.getParameter("missionURI");
        String eloURI = request.getParameter("eloURI");

        logger.info("ADDING ELO: " + eloURI + " TO PORTFOLIO OF : " + missionURI);
    }

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }
}
