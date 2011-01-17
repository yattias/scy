package eu.scy.server.eportfolio.xml;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.server.controllers.xml.transfer.TransferElo;
import eu.scy.server.eportfolio.xml.utilclasses.ELOSearchResult;
import eu.scy.server.url.UrlInspector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 17.jan.2011
 * Time: 06:21:45
 * To change this template use File | Settings | File Templates.
 */
public class EloSearchService extends MissionRuntimeEnabledXMLService{

    private MissionELOService missionELOService;
    private UrlInspector urlInspector;

    @Override
    protected Object getObject(MissionRuntimeElo missionRuntimeElo, HttpServletRequest request, HttpServletResponse response) {

        String searchParameters = request.getParameter("searchParameters");

        List elos = getMissionELOService().getELOSWithTechnicalFormat("scy/mapping");
        ELOSearchResult eloSearchResult = new ELOSearchResult();
        for (int i = 0; i < elos.size(); i++) {
            ScyElo scyElo = (ScyElo) elos.get(i);
            eloSearchResult.getElos().add(createELOModel(scyElo));
        }

        return eloSearchResult;

    }

    private Object createELOModel(ScyElo scyElo) {
        TransferElo eloModel = new TransferElo();
        eloModel.setUri(scyElo.getUri().toString());
        Date createdDate = new Date(scyElo.getDateCreated());
        Date lastModified = new Date(scyElo.getDateLastModified());
        eloModel.setCreatedDate(createdDate.toString());
        eloModel.setLastModified(lastModified.toString());
        eloModel.setMyname(scyElo.getTitle());
        if(scyElo.getThumbnail() != null) {
            System.out.println("THIS ELO HAS A THUMBNAIL!");
        } else {
            System.out.println("THIS ELO DOES NOT HAVE A THUMBNAIL!!!");
        }

        return eloModel;
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
