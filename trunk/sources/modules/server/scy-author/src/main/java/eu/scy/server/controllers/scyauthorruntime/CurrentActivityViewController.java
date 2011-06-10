package eu.scy.server.controllers.scyauthorruntime;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.core.model.transfer.LasActivityInfo;
import eu.scy.core.model.transfer.PedagogicalPlanTransfer;
import eu.scy.core.model.transfer.UserActivityInfo;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.core.roolo.PedagogicalPlanELOService;
import eu.scy.core.runtime.SessionService;
import eu.scy.server.controllers.BaseController;
import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 18.mai.2011
 * Time: 13:00:33
 * To change this template use File | Settings | File Templates.
 */
public class CurrentActivityViewController extends BaseController {

    private MissionELOService missionELOService;
    private PedagogicalPlanELOService pedagogicalPlanELOService;

    private TupleSpace tupleSpace;
    private TupleSpace commandSpace;
    private SessionService sessionService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {

        MissionSpecificationElo missionSpecificationElo = null;
        try {
            URI uri = new URI(request.getParameter("eloURI"));
            missionSpecificationElo = MissionSpecificationElo.loadLastVersionElo(uri, getMissionELOService());
        } catch (URISyntaxException e) {
            logger.error(e.getMessage(), e);
        }

        PedagogicalPlanTransfer pedagogicalPlanTransfer = getPedagogicalPlanELOService().getPedagogicalPlanForMission(missionSpecificationElo);

        List<UserActivityInfo> userActivityInfo = getSessionService().getCurrentStudentActivity(missionSpecificationElo);
        List<LasActivityInfo> lasActivityInfos = getSessionService().getActiveLasses(missionSpecificationElo);



        modelAndView.addObject("userActivityList", userActivityInfo);
        //modelAndView.addObject("lasActivityList", lasActivityInfos);
    }

    public TupleSpace getTupleSpace() {
        return tupleSpace;
    }

    public void setTupleSpace(TupleSpace tupleSpace) {
        this.tupleSpace = tupleSpace;
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

    public TupleSpace getCommandSpace() {
        return commandSpace;
    }

    public void setCommandSpace(TupleSpace commandSpace) {
        this.commandSpace = commandSpace;
    }

    public PedagogicalPlanELOService getPedagogicalPlanELOService() {
        return pedagogicalPlanELOService;
    }

    public void setPedagogicalPlanELOService(PedagogicalPlanELOService pedagogicalPlanELOService) {
        this.pedagogicalPlanELOService = pedagogicalPlanELOService;
    }
}
