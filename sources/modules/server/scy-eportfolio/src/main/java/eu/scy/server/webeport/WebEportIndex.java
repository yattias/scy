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
 * Date: 04.aug.2011
 * Time: 06:23:06
 * To change this template use File | Settings | File Templates.
 */
public class WebEportIndex extends BaseController {

    private MissionELOService missionELOService;
    private XMLTransferObjectService xmlTransferObjectService;
    private PedagogicalPlanELOService pedagogicalPlanELOService;
    private PortfolioELOService portfolioELOService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        URI missionURI = null;
        if(request.getParameter("missionRuntimeURI") != null) {
            missionURI = getURI(request.getParameter("missionRuntimeURI"));
        } else {
            missionURI = getURI(request.getParameter(ELO_URI));
        }

        MissionRuntimeElo missionRuntimeElo = MissionRuntimeElo.loadLastVersionElo(missionURI, getMissionELOService());
        MissionSpecificationElo missionSpecificationElo = getMissionELOService().getMissionSpecificationELOForRuntume(missionRuntimeElo);
        PedagogicalPlanTransfer pedagogicalPlanTransfer = getPedagogicalPlanELOService().getPedagogicalPlanForMission(missionSpecificationElo);
        Portfolio portfolio = getMissionELOService().getPortfolio(missionRuntimeElo, getCurrentUserName(request));

        List obligatoryAnchorElos = getObligatoryAnchorElos(missionSpecificationElo, pedagogicalPlanTransfer);

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


        modelAndView.addObject("obligatoryAnchorElos", obligatoryAnchorElos);
        modelAndView.addObject("portfolio", portfolio);
        modelAndView.addObject("missionRuntimeURI", getEncodedUri(missionURI.toString()));
        modelAndView.addObject("anchorElosWithStatuses", anchoELOWithStatuses);



        



    }

    private List getObligatoryAnchorElos(MissionSpecificationElo missionSpecificationElo, PedagogicalPlanTransfer pedagogicalPlanTransfer) {
        return getMissionELOService().getObligatoryAnchorELOs(missionSpecificationElo, pedagogicalPlanTransfer);
    }

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
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

    public PortfolioELOService getPortfolioELOService() {
        return portfolioELOService;
    }

    public void setPortfolioELOService(PortfolioELOService portfolioELOService) {
        this.portfolioELOService = portfolioELOService;
    }
}
