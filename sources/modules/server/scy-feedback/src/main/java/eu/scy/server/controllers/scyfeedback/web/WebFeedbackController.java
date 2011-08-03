package eu.scy.server.controllers.scyfeedback.web;

import eu.scy.core.roolo.MissionELOService;
import eu.scy.server.controllers.BaseController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Lars
 * Date: 02.jul.2011
 * Time: 20:18:54
 * To change this template use File | Settings | File Templates.
 */
public class WebFeedbackController extends BaseController {
    private MissionELOService missionELOService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        modelAndView.addObject("eloURI", getEncodedUri(request.getParameter(ELO_URI)));
    }

    

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }
}
