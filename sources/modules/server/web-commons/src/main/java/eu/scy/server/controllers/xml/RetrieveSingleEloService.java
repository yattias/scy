package eu.scy.server.controllers.xml;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.model.transfer.NewestElos;
import eu.scy.core.model.transfer.ServiceMessage;
import eu.scy.core.model.transfer.TransferElo;
import eu.scy.core.roolo.MissionELOService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 11.feb.2011
 * Time: 11:53:35
 * To change this template use File | Settings | File Templates.
 */
public class RetrieveSingleEloService extends XMLStreamerController{

    private MissionELOService missionELOService;

    @Override
    protected Object getObjectToStream(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        try {
            String eloURI = request.getParameter("singleELO");
            String missionURI = request.getParameter("missionURI");


            URI missionRuntimeURI = new URI(missionURI);
            MissionRuntimeElo missionRuntimeElo = MissionRuntimeElo.loadLastVersionElo(missionRuntimeURI, getMissionELOService());

            String decodedEloUri = URLDecoder.decode(eloURI, "UTF-8");

            URI eloUri = new URI(decodedEloUri);

            ScyElo scyElo = ScyElo.loadLastVersionElo(eloUri, getMissionELOService());
            TransferElo returnElo = getMissionELOService().getTransferElo(scyElo);
            if(returnElo != null) return returnElo;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        ServiceMessage sm = new ServiceMessage();
        sm.setMessage("This wasn't exactly how we envisioned it - unclearException occurred.");
        return sm;


    }


    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }
}
