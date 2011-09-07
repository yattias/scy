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
        URI eloURI = getURI(request.getParameter(ELO_URI));
        ScyElo elo = ScyElo.loadLastVersionElo(eloURI, getMissionELOService());
        TransferElo transferElo = new TransferElo(elo);

        URI missionRuntimeURI = getURI(request.getParameter("missionRuntimeURI"));
        MissionRuntimeElo missionRuntimeElo = null;
        if (missionRuntimeURI == null) {
            //this will happen if elo has been added from scylab
            List<ISearchResult> missionRuntimes = (List<ISearchResult>) getRuntimeELOService().getRuntimeElosForUser(getCurrentUserName(request));
            if (missionRuntimes.size() > 0) {
                ISearchResult searchResult = missionRuntimes.get(0);
                missionRuntimeElo = MissionRuntimeElo.loadLastVersionElo(searchResult.getUri(), getMissionELOService());
            }
        } else {
            missionRuntimeElo = MissionRuntimeElo.loadLastVersionElo(missionRuntimeURI, getMissionELOService());
        }


        URI anchorEloURI = getURI(request.getParameter("anchorEloURI"));
        ScyElo anchorElo = null;
        if(anchorEloURI == null) {
            anchorEloURI = missionRuntimeElo.getMissionRuntimeModel().getAnchorEloUriForElo(elo.getUri());
            logger.info("ANCHOR ELO URI: " + anchorEloURI);
        }
        if (anchorEloURI != null) {
            anchorElo = ScyElo.loadLastVersionElo(anchorEloURI, getMissionELOService());
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
            logger.info("ACTION IS : " + action);
            if (action != null) {
                dispatchAction(request, missionRuntimeElo, transferElo);
            }

            Portfolio portfolio = getMissionELOService().getPortfolio(missionRuntimeElo, getCurrentUserName(request));
            List<SelectedLearningGoalWithScore> selectedGeneralLearningGoalWithScores = portfolio.getGeneralLearningGoalsForElo(transferElo.getUri());
            if (selectedGeneralLearningGoalWithScores != null) {
                for (int i = 0; i < selectedGeneralLearningGoalWithScores.size(); i++) {
                    SelectedLearningGoalWithScore selectedLearningGoalWithScore = selectedGeneralLearningGoalWithScores.get(i);
                    populateWithCorrectLearningGoalText(selectedLearningGoalWithScore, pedagogicalPlanTransfer);
                }

            }

            List<SelectedLearningGoalWithScore> selectedSpecificLearningGoalWithScores = portfolio.getSpecificLearningGoalsForElo(transferElo.getUri());
            if (selectedSpecificLearningGoalWithScores != null) {
                for (int i = 0; i < selectedSpecificLearningGoalWithScores.size(); i++) {
                    SelectedLearningGoalWithScore selectedLearningGoalWithScore = selectedSpecificLearningGoalWithScores.get(i);
                    populateWithCorrectLearningGoalText(selectedLearningGoalWithScore, pedagogicalPlanTransfer);
                }
            }

            boolean showWarningNoGeneralLearningGoalsAdded = false;
            boolean showWarningNoSpecificLearningGoalsAdded = false;

            if (selectedGeneralLearningGoalWithScores.size() == 0) showWarningNoGeneralLearningGoalsAdded = true;
            if (selectedSpecificLearningGoalWithScores.size() == 0) showWarningNoSpecificLearningGoalsAdded = true;

            boolean portfolioLocked = false;
            if (portfolio.getPortfolioStatus().equals(Portfolio.PORTFOLIO_STATUS_ASSESSED))
                logger.info("-------------------------------> THE MISION RUNTIME URI IS: !" + missionRuntimeURI.toString());
            modelAndView.addObject("missionRuntimeURI", getEncodedUri(missionRuntimeElo.getUri().toString()));
            modelAndView.addObject(ELO_URI, getEncodedUri(eloURI.toString()));
            modelAndView.addObject("anchorEloURI", getEncodedUri(anchorEloURI.toString()));
            modelAndView.addObject("reflectionQuestions", reflectionQuestions);
            modelAndView.addObject("selectedGeneralLearningGoalWithScores", selectedGeneralLearningGoalWithScores);
            modelAndView.addObject("selectedSpecificLearningGoalWithScores", selectedSpecificLearningGoalWithScores);
            if (portfolio.getPortfolioStatus().equals(Portfolio.PORTFOLIO_STATUS_ASSESSED)) {
                modelAndView.addObject("portfolioLocked", true);
            } else {
                modelAndView.addObject("portfolioLocked", false);
            }
            modelAndView.addObject("showWarningNoGeneralLearningGoalsAdded", showWarningNoGeneralLearningGoalsAdded);
            modelAndView.addObject("showWarningNoSpecificLearningGoalsAdded", showWarningNoSpecificLearningGoalsAdded);

        } else {
            //this is a silly hack
            modelAndView.setViewName("forward:selectAnchorEloForEloThatHasBeenAddedFromScyLab.html");
        }

    }

    private void populateWithCorrectLearningGoalText(SelectedLearningGoalWithScore selectedLearningGoalWithScore, PedagogicalPlanTransfer pedagogicalPlanTransfer) {
        String learningGoalId = selectedLearningGoalWithScore.getLearningGoalId();
        logger.info("LEARNING GOAL ID: " + learningGoalId);
        learningGoalId = learningGoalId.replaceAll("\"", "");//haque
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

    private void dispatchAction(HttpServletRequest request, MissionRuntimeElo missionRuntimeElo, TransferElo transferElo) {
        String action = request.getParameter("action");
        String type = request.getParameter("lgType");
        if (action.equals("addLearningGoal")) {
            if (type.equals("specific")) {
                addSpecificLearningGoal(request, missionRuntimeElo, transferElo);
            } else {
                addGeneralLearningGoal(request, missionRuntimeElo, transferElo);
            }
        } else if (action.equals("setLearningGoalScore")) {
            setLearningGoalScore(request, missionRuntimeElo, transferElo);
        } else if (action.equals("delete")) {
            deleteLearningGoal(request, missionRuntimeElo, transferElo);
        }
    }

    private void deleteLearningGoal(HttpServletRequest request, MissionRuntimeElo missionRuntimeElo, TransferElo transferElo) {
        String generalLearningGoalWithScoreId = request.getParameter("generalLearningGoalWithScoreId");

        logger.info("DELETING WITH ID: " + generalLearningGoalWithScoreId);

        Portfolio portfolio = getMissionELOService().getPortfolio(missionRuntimeElo, getCurrentUserName(request));
        SelectedLearningGoalWithScore toBeDeleted = null;
        List<SelectedLearningGoalWithScore> selectedLearningGoalWithScores = portfolio.getSelectedGeneralLearningGoalWithScores();
        for (int i = 0; i < selectedLearningGoalWithScores.size(); i++) {
            SelectedLearningGoalWithScore selectedLearningGoalWithScore = selectedLearningGoalWithScores.get(i);
            if (selectedLearningGoalWithScore.getId().equals(generalLearningGoalWithScoreId))
                toBeDeleted = selectedLearningGoalWithScore;
        }
        if (toBeDeleted != null) {
            logger.info("DELETING: " + toBeDeleted.getLearningGoalText());
            selectedLearningGoalWithScores.remove(toBeDeleted);
        }
        if (toBeDeleted == null) {
            selectedLearningGoalWithScores = portfolio.getSelectedSpecificLearningGoalWithScores();
            for (int i = 0; i < selectedLearningGoalWithScores.size(); i++) {
                SelectedLearningGoalWithScore selectedLearningGoalWithScore = selectedLearningGoalWithScores.get(i);
                if (selectedLearningGoalWithScore.getId().equals(generalLearningGoalWithScoreId))
                    toBeDeleted = selectedLearningGoalWithScore;
            }
            if (toBeDeleted != null) {
                selectedLearningGoalWithScores.remove(toBeDeleted);
            }

        }

        ScyElo portfolioElo = ScyElo.loadLastVersionElo(missionRuntimeElo.getTypedContent().getEPortfolioEloUri(), getMissionELOService());
        portfolioElo.getContent().setXmlString(getXmlTransferObjectService().getToObjectXStream().toXML(portfolio));
        portfolioElo.updateElo();


    }

    private void setLearningGoalScore(HttpServletRequest request, MissionRuntimeElo missionRuntimeElo, TransferElo transferElo) {
        String generalLearningGoalWithScoreId = request.getParameter("generalLearningGoalWithScoreId");
        String score = request.getParameter("score-" + generalLearningGoalWithScoreId);
        String value=request.getParameter("value");


        logger.info("SCORE IS: " + score);
        logger.info("VALUE IS: " + value);
        logger.info("ID IS: " + generalLearningGoalWithScoreId);

        Portfolio portfolio = getMissionELOService().getPortfolio(missionRuntimeElo, getCurrentUserName(request));
        List<SelectedLearningGoalWithScore> selectedLearningGoalWithScores = portfolio.getSelectedGeneralLearningGoalWithScores();
        for (int i = 0; i < selectedLearningGoalWithScores.size(); i++) {
            SelectedLearningGoalWithScore selectedLearningGoalWithScore = selectedLearningGoalWithScores.get(i);
            logger.info("SELECTED: " + selectedLearningGoalWithScore.getId() + " COMPARE TO: " + generalLearningGoalWithScoreId);
            if (selectedLearningGoalWithScore.getId().equals(generalLearningGoalWithScoreId)) {
                logger.info("SET SCORE ON : " + selectedLearningGoalWithScore.getLearningGoalText());
                selectedLearningGoalWithScore.setScore(value);
            }

        }
        selectedLearningGoalWithScores = portfolio.getSelectedSpecificLearningGoalWithScores();
        for (int i = 0; i < selectedLearningGoalWithScores.size(); i++) {
            SelectedLearningGoalWithScore selectedLearningGoalWithScore = selectedLearningGoalWithScores.get(i);
            logger.info("SELECTED: " + selectedLearningGoalWithScore.getId() + " COMPARE TO: " + generalLearningGoalWithScoreId);
            if (selectedLearningGoalWithScore.getId().equals(generalLearningGoalWithScoreId)) {
                logger.info("SET SCORE ON : " + selectedLearningGoalWithScore.getLearningGoalText());
                selectedLearningGoalWithScore.setScore(value);
            }

        }

        ScyElo portfolioElo = ScyElo.loadLastVersionElo(missionRuntimeElo.getTypedContent().getEPortfolioEloUri(), getMissionELOService());
        portfolioElo.getContent().setXmlString(getXmlTransferObjectService().getToObjectXStream().toXML(portfolio));
        portfolioElo.updateElo();


    }

    private void addGeneralLearningGoal(HttpServletRequest request, MissionRuntimeElo missionRuntimeElo, TransferElo transferElo) {
        SelectedLearningGoalWithScore selectedLearningGoalWithScore = new SelectedLearningGoalWithScore();
        String learningGoalId = request.getParameter("learningGoalId");
        String level = request.getParameter("value");

        selectedLearningGoalWithScore.setLearningGoalId(learningGoalId);
        selectedLearningGoalWithScore.setEloURI(transferElo.getUri());
        selectedLearningGoalWithScore.setScore(level);

        Portfolio portfolio = getMissionELOService().getPortfolio(missionRuntimeElo, getCurrentUserName(request));
        portfolio.addSelectedGeneralLearningGoalWithScore(selectedLearningGoalWithScore);

        ScyElo portfolioElo = ScyElo.loadLastVersionElo(missionRuntimeElo.getTypedContent().getEPortfolioEloUri(), getMissionELOService());
        portfolioElo.getContent().setXmlString(getXmlTransferObjectService().getToObjectXStream().toXML(portfolio));
        portfolioElo.updateElo();
    }

    private void addSpecificLearningGoal(HttpServletRequest request, MissionRuntimeElo missionRuntimeElo, TransferElo transferElo) {
        SelectedLearningGoalWithScore selectedLearningGoalWithScore = new SelectedLearningGoalWithScore();
        String learningGoalId = request.getParameter("learningGoalId");
        String level = request.getParameter("value");

        selectedLearningGoalWithScore.setLearningGoalId(learningGoalId);
        selectedLearningGoalWithScore.setEloURI(transferElo.getUri());
        selectedLearningGoalWithScore.setScore(level);

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
