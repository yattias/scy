package eu.scy.server.assessment;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.mission.MissionSpecificationElo;
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
import java.net.URI;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 05.sep.2011
 * Time: 02:20:18
 * To change this template use File | Settings | File Templates.
 */
public class AssessMission extends BaseController {

    private PortfolioELOService portfolioELOService;
    private RuntimeELOService runtimeELOService;
    private MissionELOService missionELOService;
    private XMLTransferObjectService xmlTransferObjectService;
    private PedagogicalPlanELOService pedagogicalPlanELOService;
    private TransferObjectMapService transferObjectMapService;


    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        URI missionRuntimeURI = getURI(request.getParameter("missionRuntimeURI"));
        MissionRuntimeElo missionRuntimeElo = MissionRuntimeElo.loadLastVersionElo(missionRuntimeURI, getMissionELOService());
        MissionSpecificationElo missionSpecificationELo = getMissionELOService().getMissionSpecificationELOForRuntume(missionRuntimeElo);
        PedagogicalPlanTransfer pedagogicalPlan = getPedagogicalPlanELOService().getPedagogicalPlanForMission(missionSpecificationELo);
        Portfolio portfolio = getMissionELOService().getPortfolio(missionRuntimeElo, getCurrentUserName(request));

        initializePortfolio(pedagogicalPlan, portfolio, missionRuntimeElo);

        

        List<MissionReflectionQuestionAnswer> missionReflectionQuestionAnswers= portfolio.getMissionReflectionQuestionAnswers();
        modelAndView.addObject("missionReflectionQuestionAnswers", missionReflectionQuestionAnswers);
        modelAndView.addObject("missionRuntimeURI", getEncodedUri(missionRuntimeElo.getUri().toString()));
        modelAndView.addObject("teacherReflectionOnMisionAnswers", portfolio.getTeachersReflectionsOnMissionAnswers());
    }

    private void initializePortfolio(PedagogicalPlanTransfer pedagogicalPlan, Portfolio portfolio, MissionRuntimeElo missionRuntimeElo) {
        for (int i = 0; i < pedagogicalPlan.getAssessmentSetup().getTeacherQuestionsToMission().size(); i++) {
            TeacherQuestionToMission teacherQuestionToMission = pedagogicalPlan.getAssessmentSetup().getTeacherQuestionsToMission().get(i);
            TeachersReflectionsOnMissionAnswer answer = new TeachersReflectionsOnMissionAnswer();
            answer.setTeacherQuestionToMissionId(teacherQuestionToMission.getId());
            answer.setTeacherQuestionToMission(teacherQuestionToMission.getQuestion());
            portfolio.addTeacherReflectionOnMissionAnswer(answer);
        }

        ScyElo portfolioElo = ScyElo.loadLastVersionElo(missionRuntimeElo.getTypedContent().getEPortfolioEloUri(), getMissionELOService());
        portfolioElo.getContent().setXmlString(getXmlTransferObjectService().getToObjectXStream().toXML(portfolio));
        portfolioElo.updateElo();

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
