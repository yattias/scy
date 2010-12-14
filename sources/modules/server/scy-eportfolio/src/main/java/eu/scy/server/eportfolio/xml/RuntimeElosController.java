package eu.scy.server.eportfolio.xml;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.core.roolo.RuntimeELOService;
import eu.scy.server.controllers.xml.XMLStreamerController;
import eu.scy.server.eportfolio.xml.utilclasses.RuntimeEloInfo;

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

    private RuntimeELOService runtimeELOService;

    @Override
    protected Object getObjectToStream(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        String userName = request.getParameter("username");
        List missionRuntimeElos = new LinkedList();
        List runtimeElos = getRuntimeELOService().getRuntimeElosForUser(userName);
        for (int i = 0; i < runtimeElos.size(); i++) {
            MissionRuntimeElo missionRuntimeElo = new MissionRuntimeElo (((ScyElo) runtimeElos.get(i)).getElo(), runtimeELOService);
            RuntimeEloInfo runtimeEloInfo = new RuntimeEloInfo();
            runtimeEloInfo.setMissionName(missionRuntimeElo.getTitle());
            runtimeEloInfo.setUri(missionRuntimeElo.getUri().toString());
            missionRuntimeElos.add(runtimeEloInfo);
        }

        return missionRuntimeElos;

    }

    public RuntimeELOService getRuntimeELOService() {
        return runtimeELOService;
    }

    public void setRuntimeELOService(RuntimeELOService runtimeELOService) {
        this.runtimeELOService = runtimeELOService;
    }
}
