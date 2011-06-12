package eu.scy.server.controllers.scyauthorruntime;

import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.core.model.transfer.LasActivityInfo;
import eu.scy.core.model.transfer.PedagogicalPlanTransfer;
import eu.scy.core.model.transfer.UserActivityInfo;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.core.runtime.SessionService;
import eu.scy.server.controllers.BaseController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 12.jun.2011
 * Time: 12:35:23
 * To change this template use File | Settings | File Templates.
 */
public class ViewUsersInLasController extends BaseController {

    private MissionELOService missionELOService;
    private SessionService sessionService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        URI uri = getURI(request.getParameter(ELO_URI));
        MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo.loadLastVersionElo(uri, getMissionELOService());

        List<LasActivityInfo> lasActivityInfos = getSessionService().getActiveLasses(missionSpecificationElo);
        Integer highestNumberOfActiveUsers = getHighestNumberOfActiveUsers(lasActivityInfos);
        modelAndView.addObject("lasActivityList", lasActivityInfos);
        modelAndView.addObject("highestNumberOfActiveUsers", highestNumberOfActiveUsers);
    }

    private Integer getHighestNumberOfActiveUsers(List<LasActivityInfo> lasActivityInfos) {
        Integer returnValue = 0;
        for (int i = 0; i < lasActivityInfos.size(); i++) {
            LasActivityInfo lasActivityInfo = lasActivityInfos.get(i);
            Integer activeUsers = lasActivityInfo.getActiveUsers().size();
            if(activeUsers > returnValue) returnValue = activeUsers;
        }
        return returnValue;
    }

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }

    public SessionService getSessionService() {
        return sessionService;
    }

    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }
}
