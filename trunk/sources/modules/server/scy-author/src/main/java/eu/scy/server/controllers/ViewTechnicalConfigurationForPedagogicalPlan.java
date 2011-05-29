package eu.scy.server.controllers;

import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.core.model.transfer.PedagogicalPlanTransfer;
import eu.scy.core.model.transfer.PropertyTransfer;
import eu.scy.core.model.transfer.TechnicalInfo;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.core.roolo.PedagogicalPlanELOService;
import eu.scy.server.util.TransferObjectServiceCollection;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 27.mai.2011
 * Time: 06:00:08
 * To change this template use File | Settings | File Templates.
 */
public class ViewTechnicalConfigurationForPedagogicalPlan extends BaseController{

    private PedagogicalPlanELOService pedagogicalPlanELOService;
    private MissionELOService missionELOService;
    private TransferObjectServiceCollection transferObjectServiceCollection;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        MissionSpecificationElo missionSpecificationElo = getMissionSpecificationElo(request);
        PedagogicalPlanTransfer pedagogicalPlanTransfer = getPedagogicalPlanELOService().getPedagogicalPlanForMission(missionSpecificationElo);

        dispatchActions(request, missionSpecificationElo, pedagogicalPlanTransfer);

        modelAndView.addObject("pedagogicalPlan", pedagogicalPlanTransfer);
        modelAndView.addObject("transferObjectServiceCollection", getTransferObjectServiceCollection());
        modelAndView.addObject("missionSpecificationEloURI", request.getParameter("eloURI"));
    }

    private void dispatchActions(HttpServletRequest request, MissionSpecificationElo missionSpecificationElo, PedagogicalPlanTransfer pedagogicalPlanTransfer) {
        String action = request.getParameter("action");
        if(action != null) {
            if(action.equals("addProperty")) addProperty(missionSpecificationElo, pedagogicalPlanTransfer);
            if(action.equals("clearProperties")) clearProperties(missionSpecificationElo, pedagogicalPlanTransfer);
        }
    }

    private void clearProperties(MissionSpecificationElo missionSpecificationElo, PedagogicalPlanTransfer pedagogicalPlanTransfer) {
        TechnicalInfo technicalInfo= pedagogicalPlanTransfer.getTechnicalInfo();
        technicalInfo.setJnlpProperties(new LinkedList());

        getPedagogicalPlanELOService().savePedagogicalPlan(pedagogicalPlanTransfer, missionSpecificationElo);
    }

    private void addProperty(MissionSpecificationElo missionSpecificationElo, PedagogicalPlanTransfer pedagogicalPlanTransfer) {
        PropertyTransfer propertyTransfer = new PropertyTransfer();
        pedagogicalPlanTransfer.getTechnicalInfo().getJnlpProperties().add(propertyTransfer);
        getPedagogicalPlanELOService().savePedagogicalPlan(pedagogicalPlanTransfer, missionSpecificationElo);
    }

    private MissionSpecificationElo getMissionSpecificationElo(HttpServletRequest request) {
        String uriString = request.getParameter(ELO_URI);
        if(uriString != null) {
            try {
                URI uri = new URI(uriString);
                MissionSpecificationElo missionSpecificationElo= MissionSpecificationElo.loadLastVersionElo(uri, getMissionELOService());
                return missionSpecificationElo;
            } catch (URISyntaxException e) {
                logger.error(e.getMessage(), e);
            }
        }

        throw new RuntimeException("DId not manage to load mission specification! Maybe request does not contain the eloURI?");
    }


    public PedagogicalPlanELOService getPedagogicalPlanELOService() {
        return pedagogicalPlanELOService;
    }

    public void setPedagogicalPlanELOService(PedagogicalPlanELOService pedagogicalPlanELOService) {
        this.pedagogicalPlanELOService = pedagogicalPlanELOService;
    }

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }

    public TransferObjectServiceCollection getTransferObjectServiceCollection() {
        return transferObjectServiceCollection;
    }

    public void setTransferObjectServiceCollection(TransferObjectServiceCollection transferObjectServiceCollection) {
        this.transferObjectServiceCollection = transferObjectServiceCollection;
    }
}
