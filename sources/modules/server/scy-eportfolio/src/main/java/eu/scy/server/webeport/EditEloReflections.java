package eu.scy.server.webeport;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.XMLTransferObjectService;
import eu.scy.core.model.transfer.*;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.core.roolo.PedagogicalPlanELOService;
import eu.scy.core.roolo.RuntimeELOService;
import eu.scy.server.controllers.BaseController;
import org.springframework.web.servlet.ModelAndView;
import roolo.search.ISearchResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 31.aug.2011
 * Time: 21:25:38
 * To change this template use File | Settings | File Templates.
 */
public class EditEloReflections extends BaseController {

    private MissionELOService missionELOService;
    private XMLTransferObjectService xmlTransferObjectService;
    private RuntimeELOService runtimeELOService;
    private PedagogicalPlanELOService pedagogicalPlanELOService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        logger.info("ELO URI: : " + request.getParameter(ELO_URI));

        URI eloURI = getURI(request.getParameter(ELO_URI));
        ScyElo elo = ScyElo.loadLastVersionElo(eloURI, getMissionELOService());
        TransferElo transferElo = new TransferElo(elo);

        logger.info("QUERY: " + request.getQueryString());
        logger.info("MISSIONURI: " + request.getParameter("missionRuntimeURI"));


        URI missionRuntimeURI = getURI(request.getParameter("missionRuntimeURI"));
        MissionRuntimeElo missionRuntimeElo = MissionRuntimeElo.loadLastVersionElo(missionRuntimeURI, getMissionELOService());


        URI anchorEloURI = getURI(request.getParameter("anchorEloURI"));
        ScyElo anchorElo = ScyElo.loadLastVersionElo(anchorEloURI, getMissionELOService());
        TransferElo anchorEloTransfer = new TransferElo(anchorElo);

        modelAndView.addObject("elo", transferElo);
        modelAndView.addObject("anchorElo", anchorEloTransfer);

        List<ISearchResult> runtimeElos = getRuntimeELOService().getRuntimeElosForUser(getCurrentUserName(request));
        PedagogicalPlanTransfer pedagogicalPlanTransfer = null;
        if (runtimeElos.size() > 0) {
            ISearchResult searchResult = runtimeElos.get(0);
            pedagogicalPlanTransfer = getPedagogicalPlanELOService().getPedagogicalPlanForMissionRuntimeElo(searchResult.getUri().toString());
        }

        List<ReflectionQuestion> reflectionQuestions = pedagogicalPlanTransfer.getAssessmentSetup().getReflectionQuestionsForAnchorElo(anchorEloTransfer.getUri());

        String action = request.getParameter("action");
        if (action != null) {
            dispatchAction(request, missionRuntimeElo);
        }

        Portfolio portfolio = getMissionELOService().getPortfolio(missionRuntimeElo, getCurrentUserName(request));
        List<SelectedLearningGoalWithScore> selectedGeneralLearningGoalWithScores = portfolio.getSelectedGeneralLearningGoalWithScores();
        if(selectedGeneralLearningGoalWithScores != null) {
            for (int i = 0; i < selectedGeneralLearningGoalWithScores.size(); i++) {
                SelectedLearningGoalWithScore selectedLearningGoalWithScore = selectedGeneralLearningGoalWithScores.get(i);
                populateWithCorrectLearningGoalText(selectedLearningGoalWithScore, pedagogicalPlanTransfer);
            }
            
        }

        List<SelectedLearningGoalWithScore> selectedSpecificLearningGoalWithScores = portfolio.getSelectedSpecificLearningGoalWithScores();
        if (selectedSpecificLearningGoalWithScores != null) {
            for (int i = 0; i < selectedSpecificLearningGoalWithScores.size(); i++) {
                SelectedLearningGoalWithScore selectedLearningGoalWithScore = selectedSpecificLearningGoalWithScores.get(i);
                populateWithCorrectLearningGoalText(selectedLearningGoalWithScore, pedagogicalPlanTransfer);
            }
        }


        modelAndView.addObject("missionRuntimeURI", getEncodedUri(missionRuntimeURI.toString()));
        modelAndView.addObject(ELO_URI, getEncodedUri(eloURI.toString()));
        modelAndView.addObject("anchorEloURI", getEncodedUri(anchorEloURI.toString()));
        modelAndView.addObject("reflectionQuestions", reflectionQuestions);
        modelAndView.addObject("selectedGeneralLearningGoalWithScores", selectedGeneralLearningGoalWithScores);
        modelAndView.addObject("selectedSpecificLearningGoalWithScores", selectedSpecificLearningGoalWithScores);

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

    private void dispatchAction(HttpServletRequest request, MissionRuntimeElo missionRuntimeElo) {
        String action = request.getParameter("action");
        String type = request.getParameter("lgType");
        if (action.equals("addLearningGoal")) {
            if (type.equals("specific")) {
                addSpecificLearningGoal(request, missionRuntimeElo);
            } else {
                addGeneralLearningGoal(request, missionRuntimeElo);
            }
        }
    }

    private void addGeneralLearningGoal(HttpServletRequest request, MissionRuntimeElo missionRuntimeElo) {
        SelectedLearningGoalWithScore selectedLearningGoalWithScore = new SelectedLearningGoalWithScore();
        String learningGoalId = request.getParameter("learningGoalId");
        selectedLearningGoalWithScore.setLearningGoalId(learningGoalId);

        Portfolio portfolio = getMissionELOService().getPortfolio(missionRuntimeElo, getCurrentUserName(request));
        portfolio.addSelectedGeneralLearningGoalWithScore(selectedLearningGoalWithScore);

        ScyElo portfolioElo = ScyElo.loadLastVersionElo(missionRuntimeElo.getTypedContent().getEPortfolioEloUri(), getMissionELOService());
        portfolioElo.getContent().setXmlString(getXmlTransferObjectService().getToObjectXStream().toXML(portfolio));
        portfolioElo.updateElo();
    }

    private void addSpecificLearningGoal(HttpServletRequest request, MissionRuntimeElo missionRuntimeElo) {
        SelectedLearningGoalWithScore selectedLearningGoalWithScore = new SelectedLearningGoalWithScore();
        String learningGoalId = request.getParameter("learningGoalId");
        selectedLearningGoalWithScore.setLearningGoalId(learningGoalId);

        Portfolio portfolio = getMissionELOService().getPortfolio(missionRuntimeElo, getCurrentUserName(request));
        portfolio.addSelectedSpecificLearningGoalWithScore(selectedLearningGoalWithScore);

        ScyElo portfolioElo = ScyElo.loadLastVersionElo(missionRuntimeElo.getTypedContent().getEPortfolioEloUri(), getMissionELOService());
        portfolioElo.getContent().setXmlString(getXmlTransferObjectService().getToObjectXStream().toXML(portfolio));
        portfolioElo.updateElo();
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

    public RuntimeELOService getRuntimeELOService() {
        return runtimeELOService;
    }

    public void setRuntimeELOService(RuntimeELOService runtimeELOService) {
        this.runtimeELOService = runtimeELOService;
    }

    public PedagogicalPlanELOService getPedagogicalPlanELOService() {
        return pedagogicalPlanELOService;
    }

    public void setPedagogicalPlanELOService(PedagogicalPlanELOService pedagogicalPlanELOService) {
        this.pedagogicalPlanELOService = pedagogicalPlanELOService;
    }
}
