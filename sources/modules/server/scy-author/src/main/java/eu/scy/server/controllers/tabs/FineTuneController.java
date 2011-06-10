package eu.scy.server.controllers.tabs;

import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.server.controllers.BaseController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.jun.2011
 * Time: 09:56:35
 * To change this template use File | Settings | File Templates.
 */
public class FineTuneController extends BaseController {

    private MissionELOService missionELOService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {

        URI missionUri = getURI(request.getParameter(ELO_URI));
        MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo.loadLastVersionElo(missionUri,getMissionELOService());
        modelAndView.addObject("missionSpecificationTransporter", getMissionELOService().getWebSafeTransporter(missionSpecificationElo));
    }

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }
}
