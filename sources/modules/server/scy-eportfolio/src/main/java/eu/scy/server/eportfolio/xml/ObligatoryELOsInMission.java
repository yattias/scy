package eu.scy.server.eportfolio.xml;

import eu.scy.core.AssignedPedagogicalPlanService;
import eu.scy.core.PedagogicalPlanPersistenceService;
import eu.scy.core.model.pedagogicalplan.AnchorELO;
import eu.scy.core.model.pedagogicalplan.AssignedPedagogicalPlan;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.server.controllers.xml.XMLStreamerController;
import eu.scy.server.eportfolio.xml.utilclasses.ELOModel;
import eu.scy.server.eportfolio.xml.utilclasses.ELOSearchResult;
import eu.scy.server.eportfolio.xml.utilclasses.EPortfolioSearchResult;

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

    private PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService;
    private AssignedPedagogicalPlanService assignedPedagogicalPlanService;

    @Override
    protected Object getObjectToStream(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        EPortfolioSearchResult searchResult = new EPortfolioSearchResult();

        AssignedPedagogicalPlan assignedPedagogicalPlan = getAssignedPedagogicalPlanService().getCurrentAssignedPedagogicalPlan(getCurrentUser(request));
        PedagogicalPlan pedagogicalPlan = assignedPedagogicalPlan.getPedagogicalPlan();
        if(pedagogicalPlan != null) {
            logger.info("Current assigned plan is : " + pedagogicalPlan.getName());
        } else {
            logger.info("No plan set as current pedagogical plan");
        }
        List anchorELOS = getPedagogicalPlanPersistenceService().getAnchorELOs(pedagogicalPlan);

        for (int i = 0; i < anchorELOS.size(); i++) {
            AnchorELO anchorELO = (AnchorELO) anchorELOS.get(i);
            if(anchorELO.getObligatoryInPortfolio()) {
                Date d = new Date();
                searchResult.addSearchResult(createEloModel(anchorELO.getHumanReadableName(), anchorELO.getMissionMapId(), "imageUrl", d.toString()));
            }

        }







        return searchResult;
    }

    private ELOModel createEloModel(String name, String uri, String thumbnailId, String createdDate) {
        ELOModel eloModel = new ELOModel();

        eloModel.setEloName(name);
        eloModel.setEloURI(uri);
        eloModel.setThumbnailId(thumbnailId);
        eloModel.setCratedDate(createdDate);

        return eloModel;
    }

    public PedagogicalPlanPersistenceService getPedagogicalPlanPersistenceService() {
        return pedagogicalPlanPersistenceService;
    }

    public void setPedagogicalPlanPersistenceService(PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService) {
        this.pedagogicalPlanPersistenceService = pedagogicalPlanPersistenceService;
    }

    public AssignedPedagogicalPlanService getAssignedPedagogicalPlanService() {
        return assignedPedagogicalPlanService;
    }

    public void setAssignedPedagogicalPlanService(AssignedPedagogicalPlanService assignedPedagogicalPlanService) {
        this.assignedPedagogicalPlanService = assignedPedagogicalPlanService;
    }
}
