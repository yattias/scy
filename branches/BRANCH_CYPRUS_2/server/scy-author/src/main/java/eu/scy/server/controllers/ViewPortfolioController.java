package eu.scy.server.controllers;

import eu.scy.core.AnchorELOService;
import eu.scy.core.LASService;
import eu.scy.core.PedagogicalPlanPersistenceService;
import eu.scy.core.model.impl.pedagogicalplan.AssessmentImpl;
import eu.scy.core.model.pedagogicalplan.AnchorELO;
import eu.scy.core.model.pedagogicalplan.Assessment;
import eu.scy.core.model.pedagogicalplan.AssessmentStrategyType;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind
 * Date: 26.mar.2010
 * Time: 09:46:42
 */
public class ViewPortfolioController extends BaseController {

    private PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService = null;
    private AnchorELOService anchorELOService = null;
    private LASService lasService = null;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        String pedPlanId = request.getParameter("id");
        logger.info("PED PLAN ID: " + pedPlanId);
        PedagogicalPlan plan = getPedagogicalPlanPersistenceService().getPedagogicalPlan(pedPlanId);

        Object rq = request.getParameter("newAssessment");
        if (rq != null) {
            AnchorELO ae = getAnchorELOService().getAnchorELO((String) rq);
            Assessment assessment = new AssessmentImpl();
            ae.setAssessment(assessment);
            getAnchorELOService().save(ae);
        }

        List assessmentStrategies = new LinkedList();
        assessmentStrategies.add(AssessmentStrategyType.PEER_TO_PEER);
        assessmentStrategies.add(AssessmentStrategyType.SINGLE);
        assessmentStrategies.add(AssessmentStrategyType.TEACHER);

        modelAndView.addObject("pedagogicalPlan", plan);
        modelAndView.addObject("anchorElos", getAnchorELOs(plan));
        modelAndView.addObject("assessmentStrategies", assessmentStrategies);
    }


    private List getAnchorELOs(PedagogicalPlan plan) {
        List returnList = new LinkedList();
        List <LearningActivitySpace> lases = getLasService().getAllLearningActivitySpacesForScenario(plan.getScenario());
        for (int i = 0; i < lases.size(); i++) {
            LearningActivitySpace learningActivitySpace = lases.get(i);
            List <AnchorELO> anchorELOs = getLasService().getAnchorELOsProducedByLAS(learningActivitySpace);
            logger.info("LearningActivitySpace: " + learningActivitySpace + " " + anchorELOs.size());

            for (int j = 0; j < anchorELOs.size(); j++) {
                AnchorELO anchorELO = anchorELOs.get(j);
                logger.info("ADDING ANCHOR ELO: " + anchorELO);
                returnList.add(anchorELO);
            }
        }

        return returnList;
    }

    public PedagogicalPlanPersistenceService getPedagogicalPlanPersistenceService() {
        return pedagogicalPlanPersistenceService;
    }

    public void setPedagogicalPlanPersistenceService(PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService) {
        this.pedagogicalPlanPersistenceService = pedagogicalPlanPersistenceService;
    }

    public LASService getLasService() {
        return lasService;
    }

    public void setLasService(LASService lasService) {
        this.lasService = lasService;
    }

    public AnchorELOService getAnchorELOService() {
        return anchorELOService;
    }

    public void setAnchorELOService(AnchorELOService anchorELOService) {
        this.anchorELOService = anchorELOService;
    }

    private class DataClass {
        private AnchorELO anchorElo = null;
        private Assessment assessment = null;

        private DataClass(AnchorELO anchorElo, Assessment assessment) {
            this.anchorElo = anchorElo;
            this.assessment = assessment;
        }

        public AnchorELO getAnchorElo() {
            return anchorElo;
        }

        public Assessment getAssessment() {
            return assessment;
        }
    }
}