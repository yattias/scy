package eu.scy.server.controllers;

import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.core.model.transfer.PedagogicalPlanTransfer;
import eu.scy.core.roolo.PedagogicalPlanELOService;
import eu.scy.server.util.TransferObjectServiceCollection;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 20.okt.2011
 * Time: 13:29:15
 * To change this template use File | Settings | File Templates.
 */
public class EditGroupingAgentController extends BaseController{

    private PedagogicalPlanELOService pedagogicalPlanELOService;
    private TransferObjectServiceCollection transferObjectServiceCollection;


    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        String uriParam = request.getParameter("eloURI");

        logger.info("ELO URI: " + uriParam);

        URI uri = getURI(uriParam);
        MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo.loadElo(uri, getMissionELOService());
        PedagogicalPlanTransfer pedagogicalPlanTransfer = getPedagogicalPlanTransfer(missionSpecificationElo);
        modelAndView.addObject("pedagogicalPlanTransfer", pedagogicalPlanTransfer);
        modelAndView.addObject("transferObjectServiceCollection", getTransferObjectServiceCollection());

    }

    private PedagogicalPlanTransfer getPedagogicalPlanTransfer(MissionSpecificationElo missionSpecificationElo) {
        return getPedagogicalPlanELOService().getPedagogicalPlanForMission(missionSpecificationElo);
    }
    

    public PedagogicalPlanELOService getPedagogicalPlanELOService() {
        return pedagogicalPlanELOService;
    }

    public void setPedagogicalPlanELOService(PedagogicalPlanELOService pedagogicalPlanELOService) {
        this.pedagogicalPlanELOService = pedagogicalPlanELOService;
    }

    public TransferObjectServiceCollection getTransferObjectServiceCollection() {
        return transferObjectServiceCollection;
    }

    public void setTransferObjectServiceCollection(TransferObjectServiceCollection transferObjectServiceCollection) {
        this.transferObjectServiceCollection = transferObjectServiceCollection;
    }
}
