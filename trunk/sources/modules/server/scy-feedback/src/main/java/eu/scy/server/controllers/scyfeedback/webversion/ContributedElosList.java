package eu.scy.server.controllers.scyfeedback.webversion;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.core.model.transfer.NewestElos;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.core.roolo.RuntimeELOService;
import eu.scy.server.controllers.BaseController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;

/**
 * Created by IntelliJ IDEA.
 * User: Lars
 * Date: 05.jul.2011
 * Time: 06:08:50
 * To change this template use File | Settings | File Templates.
 */
public class ContributedElosList extends BaseController {
    private MissionELOService missionELOService;
    private RuntimeELOService runtimeELOService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {

        String uri = request.getParameter(ELO_URI);
        URI runtimeURI = getURI(uri);
        MissionRuntimeElo missionRuntimeElo = MissionRuntimeElo.loadLastVersionElo(runtimeURI, getMissionELOService());

        NewestElos elos = getMissionELOService().getFeedbackElosWhereIHaveContributed(missionRuntimeElo, getCurrentUserName(request));
        modelAndView.addObject("anchorElos", getMissionELOService().getAnchorELOs(getMissionELOService().getMissionSpecificationELOForRuntume(missionRuntimeElo)));

        modelAndView.addObject("elos", elos.getElos());

    }

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }

    public RuntimeELOService getRuntimeELOService() {
        return runtimeELOService;
    }

    public void setRuntimeELOService(RuntimeELOService runtimeELOService) {
        this.runtimeELOService = runtimeELOService;
    }
}
