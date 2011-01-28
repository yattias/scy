package eu.scy.server.eportfolio.xml;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.server.controllers.xml.transfer.ELOSearchResult;
import eu.scy.server.controllers.xml.transfer.TransferElo;
import eu.scy.server.url.UrlInspector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        MissionSpecificationElo missionSpecificationElo = getMissionELOService().getMissionSpecificationELOForRuntume(missionRuntimeElo);
        List anchorElos = getMissionELOService().getAnchorELOs(missionSpecificationElo);
        ELOSearchResult eloSearchResult = new ELOSearchResult();
        for (int i = 0; i < anchorElos.size(); i++) {
            ScyElo o = (ScyElo) anchorElos.get(i);
            if(o.getObligatoryInPortfolio() != null && o.getObligatoryInPortfolio()) {
                eloSearchResult.getElos().add(new TransferElo(o));    
            }


        }
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
