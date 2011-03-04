package eu.scy.server.eportfolio.xml;

import com.thoughtworks.xstream.XStream;
import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.core.model.transfer.ELOSearchResult;
import eu.scy.core.model.transfer.TransferElo;
import eu.scy.server.controllers.xml.MissionRuntimeEnabledXMLService;
import eu.scy.server.url.UrlInspector;
import roolo.elo.BasicELO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 17.jan.2011
 * Time: 06:21:45
 * To change this template use File | Settings | File Templates.
 */
public class EloSearchService extends MissionRuntimeEnabledXMLService {

    private MissionELOService missionELOService;
    private UrlInspector urlInspector;

    @Override
    protected Object getObject(MissionRuntimeElo missionRuntimeElo, HttpServletRequest request, HttpServletResponse response) {


        String searchParameters = request.getParameter("searchParameters");

        List elos = getMissionELOService().findElosFor("ecomission", getCurrentUserName(request));
        ELOSearchResult eloSearchResult = new ELOSearchResult();
        for (int i = 0; i < elos.size(); i++) {
            BasicELO basicELO = (BasicELO) elos.get(i);
            ScyElo scyElo = ScyElo.loadLastVersionElo(basicELO.getUri(), getMissionELOService());
            eloSearchResult.getElos().add(createELOModel(scyElo));
            logger.info("PART OF SEARCH RESULT: "+  scyElo.getTitle());
        }

        return eloSearchResult;

    }

    @Override
    protected void addAliases(XStream xStream) {
        super.addAliases(xStream);    //To change body of overridden methods use File | Settings | File Templates.
        xstream.addImplicitCollection(ELOSearchResult.class, "elos");
    }

    private Object createELOModel(ScyElo scyElo) {
        TransferElo eloModel = new TransferElo(scyElo);
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
