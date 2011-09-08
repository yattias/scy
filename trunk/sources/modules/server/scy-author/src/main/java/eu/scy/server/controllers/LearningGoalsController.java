package eu.scy.server.controllers;

import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.XMLTransferObjectService;
import eu.scy.core.model.transfer.LearningGoal;
import eu.scy.core.model.transfer.LearningGoalCriterium;
import eu.scy.core.model.transfer.PedagogicalPlanTransfer;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.core.roolo.PedagogicalPlanELOService;
import eu.scy.server.util.TransferObjectServiceCollection;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 13.jun.2011
 * Time: 05:52:53
 * To change this template use File | Settings | File Templates.
 */
public class LearningGoalsController extends BaseController{

    private MissionELOService missionELOService;
    private PedagogicalPlanELOService pedagogicalPlanELOService;
    private XMLTransferObjectService xmlTransferObjectService;
    private TransferObjectServiceCollection transferObjectServiceCollection;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        String action = request.getParameter("action");

        URI uri = getURI(request.getParameter(ELO_URI));
        MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo.loadElo(uri, getMissionELOService());

        PedagogicalPlanTransfer pedagogicalPlanTransfer = getPedagogicalPlanELOService().getPedagogicalPlanForMission(missionSpecificationElo);

        if (action != null) {
            if (action.equals("addGeneralLearningGoal")){
                addGeneralLearningGoal(missionSpecificationElo, pedagogicalPlanTransfer);
            } else if (action.equals("addSpecificLearningGoal")){
                addSpecificLearningGoal(missionSpecificationElo, pedagogicalPlanTransfer);
            } else if (action.equals("addCriteriaToGeneralLearningGoal")) {
                LearningGoal learningGoal = getLearningGoal(pedagogicalPlanTransfer, request);
                addCriteriaToLearningGoals(missionSpecificationElo, pedagogicalPlanTransfer, learningGoal);
            } else if (action.equals("addCriteriaToSpecificLearningGoal")) {
                LearningGoal learningGoal = getLearningGoal(pedagogicalPlanTransfer, request);
                addCriteriaToLearningGoals(missionSpecificationElo, pedagogicalPlanTransfer, learningGoal);
            } else if (action.equals("setLevelOnCriteria")) {
                LearningGoal learningGoal = getLearningGoal(pedagogicalPlanTransfer, request);
                setLevelOnCriteria(missionSpecificationElo, pedagogicalPlanTransfer, learningGoal, request);
            }

        }

        modelAndView.addObject("pedagogicalPlan", pedagogicalPlanTransfer);
        modelAndView.addObject("transferObjectServiceCollection", getTransferObjectServiceCollection());
        modelAndView.addObject("missionSpecificationEloURI", getEncodedUri(request.getParameter(ELO_URI)));
    }

    private void setLevelOnCriteria(MissionSpecificationElo missionSpecificationElo, PedagogicalPlanTransfer pedagogicalPlanTransfer, LearningGoal learningGoal, HttpServletRequest request) {
        String criteriaId = request.getParameter("criteriaId");
        for (int i = 0; i < learningGoal.getLearningGoalCriterias().size(); i++) {
            LearningGoalCriterium criterium = learningGoal.getLearningGoalCriterias().get(i);
            if(criterium.getId().equals(criteriaId)) criterium.setLevel(request.getParameter("level"));
        }
        ScyElo pedagogicalPlanElo = getPedagogicalPlanEloForMission(missionSpecificationElo);
        pedagogicalPlanElo.getContent().setXmlString(getXmlTransferObjectService().getXStreamInstance().toXML(pedagogicalPlanTransfer));
        pedagogicalPlanElo.updateElo();
        
    }

    private void addCriteriaToLearningGoals(MissionSpecificationElo missionSpecificationElo, PedagogicalPlanTransfer pedagogicalPlanTransfer, LearningGoal learningGoal) {
        LearningGoalCriterium criterium = new LearningGoalCriterium();
        criterium.setLevel(LearningGoalCriterium.LOW);
        learningGoal.addLearningGoalCriterium(criterium);
        ScyElo pedagogicalPlanElo = getPedagogicalPlanEloForMission(missionSpecificationElo);
        pedagogicalPlanElo.getContent().setXmlString(getXmlTransferObjectService().getXStreamInstance().toXML(pedagogicalPlanTransfer));
        pedagogicalPlanElo.updateElo();
        
    }

    private LearningGoal getLearningGoal(PedagogicalPlanTransfer pedagogicalPlanTransfer, HttpServletRequest request) {
        String learningGoalId = request.getParameter("learningGoalId");
        List generalLearningGoals = pedagogicalPlanTransfer.getAssessmentSetup().getGeneralLearningGoals();
        List specificLearningGoals = pedagogicalPlanTransfer.getAssessmentSetup().getSpecificLearningGoals();

        List<LearningGoal> allGoals = new LinkedList<LearningGoal>();
        allGoals.addAll(generalLearningGoals);
        allGoals.addAll(specificLearningGoals);
        for (int i = 0; i < allGoals.size(); i++) {
            LearningGoal learningGoal = allGoals.get(i);
            if(learningGoal.getId().equals(learningGoalId)) return learningGoal;
        }

        logger.warn("DID NOT FIND LEARNING GOAL : " + learningGoalId);
        return null;
    }


    private void addGeneralLearningGoal(MissionSpecificationElo missionSpecificationElo, PedagogicalPlanTransfer pedagogicalPlanTransfer) {
        LearningGoal learningGoal = new LearningGoal();
        if (pedagogicalPlanTransfer != null) {
            pedagogicalPlanTransfer.getAssessmentSetup().addGeneralLearningGoal(learningGoal);
            ScyElo pedagogicalPlanElo = getPedagogicalPlanEloForMission(missionSpecificationElo);
            pedagogicalPlanElo.getContent().setXmlString(getXmlTransferObjectService().getXStreamInstance().toXML(pedagogicalPlanTransfer));
            pedagogicalPlanElo.updateElo();
        } else {
            logger.info("PEDAGOGICAL PLAN TRANSFER IS NULL!!");
        }

    }


    private void addSpecificLearningGoal(MissionSpecificationElo missionSpecificationElo, PedagogicalPlanTransfer pedagogicalPlanTransfer) {
        LearningGoal learningGoal = new LearningGoal();
        if (pedagogicalPlanTransfer != null) {
            pedagogicalPlanTransfer.getAssessmentSetup().addSpecificLearningGoal(learningGoal);
            ScyElo pedagogicalPlanElo = getPedagogicalPlanEloForMission(missionSpecificationElo);
            pedagogicalPlanElo.getContent().setXmlString(getXmlTransferObjectService().getXStreamInstance().toXML(pedagogicalPlanTransfer));
            pedagogicalPlanElo.updateElo();
        } else {
            logger.info("PEDAGOGICAL PLAN TRANSFER IS NULL!!");
        }
    }



    private ScyElo getPedagogicalPlanEloForMission(MissionSpecificationElo missionSpecificationElo) {
        URI pedagogicalPlanUri = missionSpecificationElo.getTypedContent().getPedagogicalPlanSettingsEloUri();
        ScyElo pedagogicalPlanELO = ScyElo.loadLastVersionElo(pedagogicalPlanUri, getMissionELOService());
        return pedagogicalPlanELO;

    }


    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }

    public PedagogicalPlanELOService getPedagogicalPlanELOService() {
        return pedagogicalPlanELOService;
    }

    public void setPedagogicalPlanELOService(PedagogicalPlanELOService pedagogicalPlanELOService) {
        this.pedagogicalPlanELOService = pedagogicalPlanELOService;
    }

    public XMLTransferObjectService getXmlTransferObjectService() {
        return xmlTransferObjectService;
    }

    public void setXmlTransferObjectService(XMLTransferObjectService xmlTransferObjectService) {
        this.xmlTransferObjectService = xmlTransferObjectService;
    }

    public TransferObjectServiceCollection getTransferObjectServiceCollection() {
        return transferObjectServiceCollection;
    }

    public void setTransferObjectServiceCollection(TransferObjectServiceCollection transferObjectServiceCollection) {
        this.transferObjectServiceCollection = transferObjectServiceCollection;
    }
}
