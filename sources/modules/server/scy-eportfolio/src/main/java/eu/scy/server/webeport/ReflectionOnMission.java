package eu.scy.server.webeport;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.core.XMLTransferObjectService;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.model.transfer.PedagogicalPlanTransfer;
import eu.scy.core.model.transfer.Portfolio;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.core.roolo.PedagogicalPlanELOService;
import eu.scy.core.roolo.PortfolioELOService;
import eu.scy.core.roolo.RuntimeELOService;
import eu.scy.server.controllers.BaseController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;

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

        Portfolio portfolio = getMissionELOService().getPortfolio(MissionRuntimeElo.loadLastVersionElo(missionRuntimeURI, getMissionELOService()));
        PedagogicalPlanTransfer pedagogicalPlanTransfer =  getPedagogicalPlanELOService().getPedagogicalPlanForMissionRuntimeElo(missionRuntimeURI.toString());

        modelAndView.addObject("portfolio", portfolio);
        modelAndView.addObject("pedagogicalPlan", pedagogicalPlanTransfer);
        modelAndView.addObject("missionRuntimeURI", getEncodedUri(missionRuntimeURI.toString()));

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
