package eu.scy.server.controllers;

import eu.scy.core.AnchorELOService;
import eu.scy.core.PedagogicalPlanPersistenceService;
import eu.scy.core.model.pedagogicalplan.AnchorELO;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.server.controllers.BaseController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 12.jul.2010
 * Time: 09:20:06
 * To change this template use File | Settings | File Templates.
 */
public class SelectAnchorELOToBePeerAssessedController extends BaseController {

    private AnchorELOService anchorELOService;
    private PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        String pedPlanId = request.getParameter("id");
        String action = request.getParameter("action");
        if (action != null) {
            if(action.equals("addToPedagogicalPlan")) addToPedagogicalPlan(request, modelAndView);

        } else {
            List elosToBeAssessed = new LinkedList();

            PedagogicalPlan pedagogicalPlan = getPedagogicalPlanPersistenceService().getPedagogicalPlan(pedPlanId);

            if (pedagogicalPlan != null && pedagogicalPlan.getLimitNumberOfELOsAvailableForPeerAssessment()) {
                List anchorElos = getAnchorELOService().getAllAnchorELOsForScenario(pedagogicalPlan.getScenario());
                for (int j = 0; j < anchorElos.size(); j++) {
                    AnchorELO anchorELO = (AnchorELO) anchorElos.get(j);
                    elosToBeAssessed.add(anchorELO);
                }
            }

            modelAndView.addObject("elosToBeAssessed", elosToBeAssessed);
            modelAndView.addObject("pedagogicalPlan", pedagogicalPlan);
        }


    }

    private void addToPedagogicalPlan(HttpServletRequest request, ModelAndView modelAndView) {
        String ppId = request.getParameter("id");
        String aeId = request.getParameter("anchorEloId");

        PedagogicalPlan pedagogicalPlan = getPedagogicalPlanPersistenceService().getPedagogicalPlan(ppId);
        AnchorELO anchorELO = getAnchorELOService().getAnchorELO(aeId);


        getPedagogicalPlanPersistenceService().addAnchorEloToPedagogicalPlan(pedagogicalPlan, anchorELO);

        modelAndView.setViewName("forward:viewPedagogicalPlan.html");
    }

    public AnchorELOService getAnchorELOService() {
        return anchorELOService;
    }

    public void setAnchorELOService(AnchorELOService anchorELOService) {
        this.anchorELOService = anchorELOService;
    }

    public PedagogicalPlanPersistenceService getPedagogicalPlanPersistenceService() {
        return pedagogicalPlanPersistenceService;
    }

    public void setPedagogicalPlanPersistenceService(PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService) {
        this.pedagogicalPlanPersistenceService = pedagogicalPlanPersistenceService;
    }
}
