package eu.scy.server.assessment;

import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.server.controllers.BaseController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 25.mai.2011
 * Time: 06:45:08
 * To change this template use File | Settings | File Templates.
 */
public class TeacherAssessmentOverview extends BaseController {

    private MissionELOService missionELOService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        String u = request.getParameter("eloURI");
        MissionSpecificationElo missionSpecificationElo = getMissionSpecificationElo(u);
        modelAndView.addObject("missionSpecificationTransporter", getMissionELOService().getWebSafeTransporter(missionSpecificationElo));
    }

    protected MissionSpecificationElo getMissionSpecificationElo(String u) {
        try {
            URI uri = new URI(u);
            MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo.loadElo(uri, getMissionELOService());
            return missionSpecificationElo;
        } catch (URISyntaxException e) {
            logger.error(e.getMessage(), e);
        }

        throw new RuntimeException("COULD NOT LOAD MISSION SPECIFICATION : " + u);

    }

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }
}
