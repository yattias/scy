package eu.scy.server.eportfolio.xml;

import eu.scy.core.AssignedPedagogicalPlanService;
import eu.scy.core.PedagogicalPlanPersistenceService;
import eu.scy.core.model.pedagogicalplan.AnchorELO;
import eu.scy.core.model.pedagogicalplan.AssignedPedagogicalPlan;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.server.controllers.xml.XMLStreamerController;
import eu.scy.server.eportfolio.xml.utilclasses.ELOModel;
import eu.scy.server.eportfolio.xml.utilclasses.ELOSearchResult;
import eu.scy.server.eportfolio.xml.utilclasses.EPortfolioSearchResult;
import eu.scy.server.url.UrlInspector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 11.okt.2010
 * Time: 11:38:20
 * To change this template use File | Settings | File Templates.
 */
public class ObligatoryELOsInMission extends XMLStreamerController {

    private MissionELOService missionELOService;
    private UrlInspector urlInspector;

    @Override
    protected Object getObjectToStream(HttpServletRequest request, HttpServletResponse httpServletResponse) {

        return getUrlInspector().instpectRequest(request, httpServletResponse);    


    }

    private ELOModel createEloModel(String name, String uri, String thumbnailId, String createdDate) {
        ELOModel eloModel = new ELOModel();

        eloModel.setEloName(name);
        eloModel.setEloURI(uri);
        eloModel.setThumbnailId(thumbnailId);
        eloModel.setCratedDate(createdDate);

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
