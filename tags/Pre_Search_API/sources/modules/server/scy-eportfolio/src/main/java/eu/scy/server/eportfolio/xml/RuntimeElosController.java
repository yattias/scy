package eu.scy.server.eportfolio.xml;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.core.roolo.RuntimeELOService;
import eu.scy.server.controllers.xml.XMLStreamerController;
import eu.scy.server.eportfolio.xml.utilclasses.RuntimeEloInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
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
    private MissionELOService missionELOService;

    @Override
    protected Object getObjectToStream(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        String userName = request.getParameter("username");
        List missionRuntimeElos = new LinkedList();
        List runtimeElos = getRuntimeELOService().getRuntimeElosForUser(userName);
        for (int i = 0; i < runtimeElos.size(); i++) {
            ScyElo scyElo = (ScyElo) runtimeElos.get(i);
            MissionRuntimeElo.loadElo(scyElo.getUri(), getMissionELOService());
            MissionRuntimeElo missionRuntimeElo = new MissionRuntimeElo(scyElo.getElo(), runtimeELOService);

            RuntimeEloInfo runtimeEloInfo = new RuntimeEloInfo();
            runtimeEloInfo.setMissionName(missionRuntimeElo.getTitle());
            runtimeEloInfo.setUri(missionRuntimeElo.getUri().toString());

            missionRuntimeElos.add(runtimeEloInfo);

            ScyElo.loadMetadata(missionRuntimeElo.getElo().getUri(), getMissionELOService());
            URI missionSpecificationURI = missionRuntimeElo.getTypedContent().getMissionSpecificationEloUri();
            if (missionSpecificationURI != null) {
                MissionSpecificationElo missionSpecificationElo = missionELOService.getMissionSpecificationELO(missionSpecificationURI);
                runtimeEloInfo.setObligatoryElos(missionELOService.getAnchorELOs(missionSpecificationElo));
            } else {
                logger.warn("MISSION SPECIFICATION URI IS NULL!! for " + missionRuntimeElo.getUri().toString());
            }



        }

        return missionRuntimeElos;

    }

    public RuntimeELOService getRuntimeELOService() {
        return runtimeELOService;
    }

    public void setRuntimeELOService(RuntimeELOService runtimeELOService) {
        this.runtimeELOService = runtimeELOService;
    }

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }
}
