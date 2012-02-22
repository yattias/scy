package eu.scy.server.webeport;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.core.XMLTransferObjectService;
import eu.scy.core.model.transfer.PedagogicalPlanTransfer;
import eu.scy.core.model.transfer.Portfolio;
import eu.scy.core.model.transfer.TransferElo;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.core.roolo.PedagogicalPlanELOService;
import eu.scy.core.roolo.PortfolioELOService;
import eu.scy.core.roolo.RuntimeELOService;
import eu.scy.server.controllers.BaseController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 03.sep.2011
 * Time: 13:31:24
 * To change this template use File | Settings | File Templates.
 */
public class ReflectionOnMission extends BaseController {

    private PortfolioELOService portfolioELOService;
    private RuntimeELOService runtimeELOService;
    private MissionELOService missionELOService;
    private XMLTransferObjectService xmlTransferObjectService;
    private PedagogicalPlanELOService pedagogicalPlanELOService;


    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        URI missionRuntimeURI = getURI(request.getParameter("missionRuntimeURI"));

        Portfolio portfolio = getMissionELOService().getPortfolio(MissionRuntimeElo.loadLastVersionElo(missionRuntimeURI, getMissionELOService()), getCurrentUserName(request));
        PedagogicalPlanTransfer pedagogicalPlanTransfer =  getPedagogicalPlanELOService().getPedagogicalPlanForMissionRuntimeElo(missionRuntimeURI.toString());
        MissionRuntimeElo missionRuntimeElo = MissionRuntimeElo.loadLastVersionElo(missionRuntimeURI, getMissionELOService());
        URI missionSpecificationEloURI = missionRuntimeElo.getMissionSpecificationElo();
        MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo.loadLastVersionElo(missionSpecificationEloURI, getMissionELOService());

        modelAndView.addObject("portfolio", portfolio);
        modelAndView.addObject("pedagogicalPlan", pedagogicalPlanTransfer);
        modelAndView.addObject("missionRuntimeURI", getEncodedUri(missionRuntimeURI.toString()));
        modelAndView.addObject("showSubmitButton", getShowSubmitButton(request, missionSpecificationElo, portfolio, pedagogicalPlanTransfer));

    }

    private boolean getShowSubmitButton(HttpServletRequest httpServletRequest, MissionSpecificationElo missionSpecificationElo, Portfolio portfolio, PedagogicalPlanTransfer pedagogicalPlanTransfer) {

        List obligatoryAnchorElos = getObligatoryAnchorElos(httpServletRequest, missionSpecificationElo, pedagogicalPlanTransfer);
        List <AnchoELOWithStatus> anchoELOWithStatuses = new LinkedList<AnchoELOWithStatus>();
        for (int i = 0; i < obligatoryAnchorElos.size(); i++) {
            TransferElo anchorElo = (TransferElo) obligatoryAnchorElos.get(i);
            AnchoELOWithStatus anchoELOWithStatus = new AnchoELOWithStatus();
            anchoELOWithStatus.setAnchorElo(anchorElo);
            if(portfolio != null) {
                anchoELOWithStatus.setAddedElo(portfolio.getEloForAnchroElo(anchorElo));
                if(portfolio.getHasEloBeenAddedForAnchorElo(anchorElo)) anchoELOWithStatus.setEloHasBeenAdded(true);
            }
            anchoELOWithStatuses.add(anchoELOWithStatus);
        }

        if(anchoELOWithStatuses.size() > 0 ) {
            return true;
        }


        return false;
    }

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }

    public PortfolioELOService getPortfolioELOService() {
        return portfolioELOService;
    }

    public void setPortfolioELOService(PortfolioELOService portfolioELOService) {
        this.portfolioELOService = portfolioELOService;
    }

    public RuntimeELOService getRuntimeELOService() {
        return runtimeELOService;
    }

    public void setRuntimeELOService(RuntimeELOService runtimeELOService) {
        this.runtimeELOService = runtimeELOService;
    }

    public XMLTransferObjectService getXmlTransferObjectService() {
        return xmlTransferObjectService;
    }

    public void setXmlTransferObjectService(XMLTransferObjectService xmlTransferObjectService) {
        this.xmlTransferObjectService = xmlTransferObjectService;
    }

    public PedagogicalPlanELOService getPedagogicalPlanELOService() {
        return pedagogicalPlanELOService;
    }

    public void setPedagogicalPlanELOService(PedagogicalPlanELOService pedagogicalPlanELOService) {
        this.pedagogicalPlanELOService = pedagogicalPlanELOService;
    }
}
