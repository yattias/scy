package eu.scy.server.webeport;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.XMLTransferObjectService;
import eu.scy.core.model.transfer.*;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.core.roolo.PedagogicalPlanELOService;
import eu.scy.core.roolo.PortfolioELOService;
import eu.scy.core.roolo.RuntimeELOService;
import eu.scy.server.controllers.BaseController;
import eu.scy.server.util.TransferObjectMapService;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 03.sep.2011
 * Time: 14:11:33
 * To change this template use File | Settings | File Templates.
 */
public class StoreReflectionsOnMission extends BaseController {

    private PortfolioELOService portfolioELOService;
    private RuntimeELOService runtimeELOService;
    private MissionELOService missionELOService;
    private XMLTransferObjectService xmlTransferObjectService;
    private PedagogicalPlanELOService pedagogicalPlanELOService;
    private TransferObjectMapService transferObjectMapService;


    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        Enumeration parameters = request.getParameterNames();
        Portfolio portfolio = null;
        MissionRuntimeElo missionRuntimeElo = MissionRuntimeElo.loadLastVersionElo(getURI(request.getParameter("missionRuntimeURI")), getMissionELOService());
        portfolio = getMissionELOService().getPortfolio(missionRuntimeElo);
        while (parameters.hasMoreElements()) {
            String parameter = (String) parameters.nextElement();
            logger.info("PARAMETER: " + parameter + " VALUE: " + request.getParameter(parameter));
            if (parameter.equals("missionRuntimeURI")) {
                //do nothing
            } else {

                String tabId = parameter;
                Tab tab = findTab(missionRuntimeElo, tabId);
                if (tab != null) {
                    MissionReflectionQuestionAnswer missionReflectionQuestionAnswer = new MissionReflectionQuestionAnswer();
                    missionReflectionQuestionAnswer.setAnswer(request.getParameter(tabId));
                    missionReflectionQuestionAnswer.setTab(tab);
                    portfolio.addMissionReflectionQuestionAnswer(missionReflectionQuestionAnswer);
                } else {
                    logger.info("Did not find tab with id: " + tabId);
                }
            }
        }

        logger.info("NUMBER OF ANSWERS: " + portfolio.getMissionReflectionQuestionAnswers().size());

        ScyElo portfolioElo = ScyElo.loadLastVersionElo(missionRuntimeElo.getTypedContent().getEPortfolioEloUri(), getMissionELOService());
        portfolioElo.getContent().setXmlString(getXmlTransferObjectService().getToObjectXStream().toXML(portfolio));
        portfolioElo.updateElo();

    }

    private Tab findTab(MissionRuntimeElo missionRuntimeElo, String tabId) {
        PedagogicalPlanTransfer pedagogicalPlanTransfer = getPedagogicalPlanELOService().getPedagogicalPlanForMissionRuntimeElo(missionRuntimeElo.getUri().toString());
        List<Tab> tabs = pedagogicalPlanTransfer.getAssessmentSetup().getReflectionTabs();
        for (int i = 0; i < tabs.size(); i++) {
            Tab tab = tabs.get(i);
            if (tab.getId().equals(tabId)) return tab;
        }

        return null;
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

    public PedagogicalPlanELOService getPedagogicalPlanELOService() {
        return pedagogicalPlanELOService;
    }

    public void setPedagogicalPlanELOService(PedagogicalPlanELOService pedagogicalPlanELOService) {
        this.pedagogicalPlanELOService = pedagogicalPlanELOService;
    }

    public TransferObjectMapService getTransferObjectMapService() {
        return transferObjectMapService;
    }

    public void setTransferObjectMapService(TransferObjectMapService transferObjectMapService) {
        this.transferObjectMapService = transferObjectMapService;
    }
}
