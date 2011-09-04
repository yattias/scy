package eu.scy.server.assessment;

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
import java.net.URI;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 05.sep.2011
 * Time: 00:06:48
 * To change this template use File | Settings | File Templates.
 */
public class AssessElo extends BaseController {

    private PortfolioELOService portfolioELOService;
    private RuntimeELOService runtimeELOService;
    private MissionELOService missionELOService;
    private XMLTransferObjectService xmlTransferObjectService;
    private PedagogicalPlanELOService pedagogicalPlanELOService;
    private TransferObjectMapService transferObjectMapService;


    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        URI eloURI = getURI(request.getParameter(ELO_URI));
        URI missionRuntimeURI = getURI(request.getParameter("missionRuntimeURI"));

        ScyElo scyElo = ScyElo.loadLastVersionElo(eloURI, getMissionELOService());
        TransferElo elo = new TransferElo(scyElo);

        MissionRuntimeElo missionRuntimeElo = MissionRuntimeElo.loadLastVersionElo(missionRuntimeURI, getMissionELOService());

        Portfolio portfolio = getMissionELOService().getPortfolio(missionRuntimeElo, getCurrentUserName(request));
        PedagogicalPlanTransfer pedagogicalPlanTransfer = getPedagogicalPlanELOService().getPedagogicalPlanForMission(getMissionELOService().getMissionSpecificationELOForRuntume(missionRuntimeElo));

        List specificLearningGoals =  portfolio.getSpecificLearningGoalsForElo(elo.getUri());
        if(specificLearningGoals != null) {
            for (int i = 0; i < specificLearningGoals.size(); i++) {
                SelectedLearningGoalWithScore selectedLearningGoalWithScore = (SelectedLearningGoalWithScore) specificLearningGoals.get(i);
                populateWithCorrectLearningGoalText(selectedLearningGoalWithScore, pedagogicalPlanTransfer);
            }

        }

        List generalLearningGoals = portfolio.getGeneralLearningGoalsForElo(elo.getUri());
        if (generalLearningGoals != null) {
            for (int i = 0; i < generalLearningGoals.size(); i++) {
                SelectedLearningGoalWithScore selectedLearningGoalWithScore = (SelectedLearningGoalWithScore) generalLearningGoals.get(i);
                populateWithCorrectLearningGoalText(selectedLearningGoalWithScore, pedagogicalPlanTransfer);
            }
        }


        modelAndView.addObject("elo", elo);
        modelAndView.addObject("missionRuntimeURI", getEncodedUri(missionRuntimeURI.toString()));
        modelAndView.addObject("generalLearningGoals", generalLearningGoals);
        modelAndView.addObject("specificLearningGoals", specificLearningGoals);
    }

    private void populateWithCorrectLearningGoalText(SelectedLearningGoalWithScore selectedLearningGoalWithScore, PedagogicalPlanTransfer pedagogicalPlanTransfer) {
        String learningGoalId = selectedLearningGoalWithScore.getLearningGoalId();
        if (learningGoalId != null) {
            List learningGoals = pedagogicalPlanTransfer.getAssessmentSetup().getGeneralLearningGoals();
            for (int i = 0; i < learningGoals.size(); i++) {
                LearningGoal learningGoal = (LearningGoal) learningGoals.get(i);
                if (learningGoal.getId().equals(learningGoalId)) {
                    selectedLearningGoalWithScore.setLearningGoalText(learningGoal.getGoal());
                    return;
                }
            }
            learningGoals = pedagogicalPlanTransfer.getAssessmentSetup().getSpecificLearningGoals();
            for (int i = 0; i < learningGoals.size(); i++) {
                LearningGoal learningGoal = (LearningGoal) learningGoals.get(i);
                if (learningGoal.getId().equals(learningGoalId)) {
                    selectedLearningGoalWithScore.setLearningGoalText(learningGoal.getGoal());
                    return;
                }
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
