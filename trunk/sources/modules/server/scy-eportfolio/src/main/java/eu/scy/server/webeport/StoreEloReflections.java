package eu.scy.server.webeport;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.XMLTransferObjectService;
import eu.scy.core.model.transfer.Portfolio;
import eu.scy.core.model.transfer.TransferElo;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.core.roolo.PortfolioELOService;
import eu.scy.core.roolo.RuntimeELOService;
import eu.scy.server.controllers.BaseController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 31.aug.2011
 * Time: 22:25:51
 * To change this template use File | Settings | File Templates.
 */
public class StoreEloReflections extends BaseController {

    private PortfolioELOService portfolioELOService;
    private RuntimeELOService runtimeELOService;
    private MissionELOService missionELOService;
    private XMLTransferObjectService xmlTransferObjectService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {



        String generalLearningGoals = request.getParameter("generalLearningGoals");
        String specificLearningGoals = request.getParameter("specificLearningGoals");

        String eloReflectionDescription = request.getParameter("eloReflectionDescription");
        String reflectionOnElo = request.getParameter("reflectionOnElo");
        String reflectionOnInquiry = request.getParameter("reflectionOnInquiry");

        URI missionRuntimeUri = getURI(request.getParameter("missionRuntimeURI"));
        URI eloURI = getURI(request.getParameter(ELO_URI));

        logger.info("specificLearningGoals: "+ specificLearningGoals);
        logger.info("generalLearningGoals: " + generalLearningGoals);
        logger.info("missionRuntimeURI: " + missionRuntimeUri.toString());

        MissionRuntimeElo missionRuntimeElo = MissionRuntimeElo.loadLastVersionElo(missionRuntimeUri, getRuntimeELOService());
        Portfolio portfolio = getMissionELOService().getPortfolio(missionRuntimeElo);

        ScyElo eloToBeAdded = ScyElo.loadLastVersionElo(eloURI, getMissionELOService());

        TransferElo toBeAddedToPortfolio = new TransferElo(eloToBeAdded);
        toBeAddedToPortfolio.setReflectionComment(eloReflectionDescription);
        portfolio.addElo(toBeAddedToPortfolio);

        ScyElo portfolioElo = ScyElo.loadLastVersionElo(missionRuntimeElo.getTypedContent().getEPortfolioEloUri(), getMissionELOService());
        portfolioElo.getContent().setXmlString(getXmlTransferObjectService().getToObjectXStream().toXML(portfolio));
        portfolioElo.updateElo();
        logger.info("PORTFOLIO LOADED: " + portfolio.getPortfolioStatus() + " :: " + portfolio.getAssessed());

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
}
