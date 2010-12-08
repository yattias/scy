package eu.scy.server.eportfolio.xml;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.server.controllers.xml.XMLStreamerController;
import eu.scy.server.eportfolio.xml.utilclasses.RuntimeEloInfo;
import eu.scy.server.roolo.MissionELOService;
import roolo.elo.api.IELO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 08.des.2010
 * Time: 14:53:56
 * To change this template use File | Settings | File Templates.
 */
public class RuntimeElosController extends XMLStreamerController {

    private MissionELOService missionELOService;

    @Override
    protected Object getObjectToStream(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        String userName = request.getParameter("username");
        List missionRuntimeElos = new LinkedList();
        List runtimeElos = getMissionELOService().getRuntimeElosForUser(userName);
        for (int i = 0; i < runtimeElos.size(); i++) {
            MissionRuntimeElo missionRuntimeElo = new MissionRuntimeElo (((ScyElo) runtimeElos.get(i)).getElo(), missionELOService);
            RuntimeEloInfo runtimeEloInfo = new RuntimeEloInfo();
            runtimeEloInfo.setMissionName(missionRuntimeElo.getTitle());
            runtimeEloInfo.setUri(missionRuntimeElo.getUri().toString());
            missionRuntimeElos.add(runtimeEloInfo);
        }

        return missionRuntimeElos;

    }

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }
}
