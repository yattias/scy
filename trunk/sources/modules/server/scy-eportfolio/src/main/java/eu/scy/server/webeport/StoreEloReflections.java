package eu.scy.server.webeport;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.XMLTransferObjectService;
import eu.scy.core.model.transfer.EloReflectionQuestionAnswers;
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
import java.util.LinkedList;
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

        URI missionRuntimeUri = getURI(request.getParameter("missionRuntimeURI"));
        URI eloURI = getURI(request.getParameter(ELO_URI));

        URI anchorEloURI = getURI(request.getParameter("anchorEloURI"));

        MissionRuntimeElo missionRuntimeElo = MissionRuntimeElo.loadLastVersionElo(missionRuntimeUri, getRuntimeELOService());
        Portfolio portfolio = getMissionELOService().getPortfolio(missionRuntimeElo, getCurrentUserName(request));

        ScyElo eloToBeAdded = ScyElo.loadLastVersionElo(eloURI, getMissionELOService());
        ScyElo ae = ScyElo.loadLastVersionElo(anchorEloURI, getMissionELOService());
        TransferElo anchorElo = new TransferElo(ae);
        TransferElo elotoBeAddedTransfer = new TransferElo(eloToBeAdded);

        TransferElo toBeAddedToPortfolio = new TransferElo(eloToBeAdded);
        toBeAddedToPortfolio.setReflectionComment(eloReflectionDescription);
        portfolio.addElo(elotoBeAddedTransfer, anchorElo);

        addReflectionAnswers(request, toBeAddedToPortfolio, portfolio);

        ScyElo portfolioElo = ScyElo.loadLastVersionElo(missionRuntimeElo.getTypedContent().getEPortfolioEloUri(), getMissionELOService());
        portfolioElo.getContent().setXmlString(getXmlTransferObjectService().getToObjectXStream().toXML(portfolio));
        portfolioElo.updateElo();

        modelAndView.setViewName("forward:webEportIndex.html");

    }

    private void addReflectionAnswers(HttpServletRequest request, TransferElo toBeAddedToPortfolio, Portfolio portfolio) {
        List<String> reflectionParameters = new LinkedList<String>();

        Enumeration enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String parameter = (String) enumeration.nextElement();
            if (parameter.startsWith("reflection-")) {
                reflectionParameters.add(parameter);
            }
        }

        for (int i = 0; i < reflectionParameters.size(); i++) {
            String value = request.getParameter(reflectionParameters.get(i));
            String reflectionQuestionId = null;
            try {
                reflectionQuestionId = reflectionParameters.get(i).substring("reflection-".length(), reflectionParameters.get(i).length());
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            if (reflectionQuestionId != null) {
                String reflectionQuestionAnswer = value;
                logger.info("******** ******** ADDING ANSWER: " + reflectionQuestionAnswer + " TO REFLECTION QUESTION: " + reflectionQuestionId);
                EloReflectionQuestionAnswers eloReflectionQuestionAnswers = new EloReflectionQuestionAnswers();
                eloReflectionQuestionAnswers.setEloURI(toBeAddedToPortfolio.getUri());
                eloReflectionQuestionAnswers.setQuestionAnswer(reflectionQuestionAnswer);
                eloReflectionQuestionAnswers.setReflectionQuestionId(reflectionQuestionId);
                portfolio.addEloReflectionQuestion(eloReflectionQuestionAnswers);
            }

        }


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
