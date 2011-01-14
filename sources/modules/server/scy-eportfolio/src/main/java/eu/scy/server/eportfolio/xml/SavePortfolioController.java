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
 * Time: 14:40:46
 * To change this template use File | Settings | File Templates.
 */
public class SavePortfolioController extends BaseController {

    private MissionELOService missionELOService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        String missionURI = request.getParameter("missionURI");
        String xmlContent = request.getParameter("xmlContent");

        logger.info("THE MISSION :" + missionURI + " will be updated with: " + xmlContent);
    }

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }
}
