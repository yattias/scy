package eu.scy.server.assessment;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.XMLTransferObjectService;
import eu.scy.core.model.transfer.Portfolio;
import eu.scy.core.model.transfer.TeachersReflectionsOnMissionAnswer;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.core.roolo.PedagogicalPlanELOService;
import eu.scy.core.roolo.PortfolioELOService;
import eu.scy.core.roolo.RuntimeELOService;
import eu.scy.server.actionlogging.ActionLoggerService;
import eu.scy.server.controllers.BaseController;
import eu.scy.server.util.TransferObjectMapService;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 05.sep.2011
 * Time: 02:47:45
 * To change this template use File | Settings | File Templates.
 */
public class StoreMissionReflection extends BaseController {

    private PortfolioELOService portfolioELOService;
    private RuntimeELOService runtimeELOService;
    private MissionELOService missionELOService;
    private XMLTransferObjectService xmlTransferObjectService;
    private PedagogicalPlanELOService pedagogicalPlanELOService;
    private TransferObjectMapService transferObjectMapService;
    private ActionLoggerService actionLoggerService;


    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        String rating = request.getParameter("rating");
        String commentsOnPortfolio = request.getParameter("commentsOnPortfolio");
        URI missionRuntimeURI = getURI(request.getParameter("missionRuntimeURI"));
        MissionRuntimeElo missionRuntimeElo = MissionRuntimeElo.loadLastVersionElo(missionRuntimeURI, getMissionELOService());

        Portfolio portfolio = getMissionELOService().getPortfolio(missionRuntimeElo, getCurrentUserName(request));

        storeTeacherQuestions(request, portfolio);


        portfolio.setAssessmentPortfolioComment(commentsOnPortfolio);
        portfolio.setAssessmentPortfolioRating(rating);
        portfolio.setPortfolioStatus(Portfolio.PORTFOLIO_STATUS_ASSESSED);
        portfolio.setAssessed(true);

        ScyElo portfolioElo = ScyElo.loadLastVersionElo(missionRuntimeElo.getTypedContent().getEPortfolioEloUri(), getMissionELOService());
        portfolioElo.getContent().setXmlString(getXmlTransferObjectService().getToObjectXStream().toXML(portfolio));
        portfolioElo.updateElo();

        String host = request.getServerName();
        if(host.equals("localhost") || host.equals("127.0.0.1")) {
            host = "scy.collide.info";
        }

        String username = portfolio.getOwner() + "@" + host;
        logger.info("USERNAME: " + username);


        getActionLoggerService().logActionForRuntime("portfolio_assessed", username, "scy_author", missionRuntimeURI.toString());

        URI missionSpecURI = missionRuntimeElo.getMissionSpecificationEloUri();
        String encodedURI = getEncodedUri(missionSpecURI.toString());
        request.setAttribute(ELO_URI, encodedURI);

        modelAndView.setViewName("forward:/app/scyauthor/viewPedagogicalPlan.html");


    }

    private void storeTeacherQuestions(HttpServletRequest request, Portfolio portfolio) {
        Enumeration enumeration = request.getParameterNames();
        while(enumeration.hasMoreElements()) {
            String parameter = (String) enumeration.nextElement();
            if(parameter.startsWith("teacherReflection-")) {
                String id = parameter.substring("teacherReflection-".length(), parameter.length());
                String value=request.getParameter(parameter);
                setTeacherQuestionAnswer(portfolio, id, value);
            }
        }
    }

    private void setTeacherQuestionAnswer(Portfolio portfolio, String id, String value) {
        List teachersReflections = portfolio.getTeachersReflectionsOnMissionAnswers();
        for (int i = 0; i < teachersReflections.size(); i++) {
            TeachersReflectionsOnMissionAnswer teacherReflectionsOnMission = (TeachersReflectionsOnMissionAnswer) teachersReflections.get(i);
            if(teacherReflectionsOnMission.getId().equals(id)) teacherReflectionsOnMission.setAnswer(value);
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

    public ActionLoggerService getActionLoggerService() {
        return actionLoggerService;
    }

    public void setActionLoggerService(ActionLoggerService actionLoggerService) {
        this.actionLoggerService = actionLoggerService;
    }
}
