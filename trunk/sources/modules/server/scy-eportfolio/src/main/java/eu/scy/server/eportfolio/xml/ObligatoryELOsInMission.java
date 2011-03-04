package eu.scy.server.eportfolio.xml;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.core.model.transfer.ELOSearchResult;
import eu.scy.core.model.transfer.TransferElo;
import eu.scy.server.controllers.xml.MissionRuntimeEnabledXMLService;
import eu.scy.server.url.UrlInspector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 11.okt.2010
 * Time: 11:38:20
 * To change this template use File | Settings | File Templates.
 */

public class ObligatoryELOsInMission extends MissionRuntimeEnabledXMLService {

    private MissionELOService missionELOService;
    private UrlInspector urlInspector;


    @Override
    protected Object getObject(MissionRuntimeElo missionRuntimeElo, HttpServletRequest request, HttpServletResponse response) {
        MissionSpecificationElo missionSpecificationElo = null;
        logger.info("Loading anchor elos! missionRuntimeElo: " + missionRuntimeElo);
        logger.info("MISSION URI PARAMETER: " + request.getParameter("missionURI"));

        ScyElo scyElo = null;
        if (missionRuntimeElo == null) {
            String missionURI = request.getParameter("missionURI");
            try {
                scyElo = ScyElo.loadLastVersionElo(new URI(missionURI), getMissionELOService());
                if(scyElo.getTechnicalFormat().equals("scy/missionspecification")) {
                    missionSpecificationElo = MissionSpecificationElo.loadLastVersionElo(new URI(missionURI), getMissionELOService());
                } else {
                    missionRuntimeElo = MissionRuntimeElo.loadLastVersionElo(new URI(missionURI),  getMissionELOService());
                    missionSpecificationElo = getMissionELOService().getMissionSpecificationELOForRuntume(missionRuntimeElo);
                }

            } catch (URISyntaxException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        } else {
            scyElo = ScyElo.loadLastVersionElo(missionRuntimeElo.getTypedContent().getEPortfolioEloUri(), getMissionELOService());
        }

        if (missionSpecificationElo == null) {
            if (scyElo.getTechnicalFormat().equals("scy/missionruntime")) {
                missionSpecificationElo = getMissionELOService().getMissionSpecificationELOForRuntume(missionRuntimeElo);
            } else {
                missionSpecificationElo = MissionSpecificationElo.loadLastVersionElo(missionRuntimeElo.getUri(), getMissionELOService());
            }
        }


        List anchorElos = getMissionELOService().getAnchorELOs(missionSpecificationElo);
        ELOSearchResult eloSearchResult = new ELOSearchResult();
        for (int i = 0; i < anchorElos.size(); i++) {
            ScyElo o = (ScyElo) anchorElos.get(i);
            logger.info("IS THIS OBLIGATORY IN PORTFOLIO: " + o.getTitle() + " " + o.getObligatoryInPortfolio());
            if (o.getObligatoryInPortfolio() != null && o.getObligatoryInPortfolio()) {
                eloSearchResult.getElos().add(new TransferElo(o));
            }
        }
        logger.info("ANCHOR ELOS: " + eloSearchResult.getElos().size());
        return eloSearchResult;
    }

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }

    public UrlInspector getUrlInspector() {
        return urlInspector;
    }

    public void setUrlInspector(UrlInspector urlInspector) {
        this.urlInspector = urlInspector;
    }
}
