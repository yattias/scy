package eu.scy.server.controllers.scyfeedback;

import eu.scy.core.AnchorELOService;
import eu.scy.core.AssessmentService;
import eu.scy.core.ELORefService;
import eu.scy.core.model.AssessmentCriteria;
import eu.scy.core.model.ELORef;
import eu.scy.core.model.impl.pedagogicalplan.AssessmentCriteriaImpl;
import eu.scy.core.model.pedagogicalplan.AnchorELO;
import eu.scy.server.controllers.BaseController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.jul.2010
 * Time: 05:37:16
 * To change this template use File | Settings | File Templates.
 */
public class CriteriaBasedEvaluationController extends BaseController {

    private AssessmentService assessmentService;
    private AnchorELOService anchorELOService;
    private ELORefService eloRefService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        AssessmentCriteria assessmentCriteria = null;
        AnchorELO anchorELO = null;
        ELORef eloRef = null;

        if(request.getParameter("evaluationCriteriaId") != null) assessmentCriteria = getAssessmentService().getAssessmentCriteria(request.getParameter("evaluationCriteriaId"));
        if(request.getParameter("anchorEloId") != null) anchorELO = anchorELOService.getAnchorELO(request.getParameter("anchorEloId"));
        if(request.getParameter("eloRefId") != null) eloRef = getEloRefService().getELORefById(request.getParameter("eloRefId"));

        logger.info("assessmentCriteria: " + assessmentCriteria);
        logger.info("anchorELO:" + anchorELO);
        logger.info("EloRef: " + eloRef);

        assessmentCriteria.getAssessment().getAssessmentScoreDefinitions();

        modelAndView.addObject("assessmentCriteria", assessmentCriteria);
        modelAndView.addObject("anchorELO", anchorELO);
        modelAndView.addObject("eloRef", eloRef);
        modelAndView.addObject("assessment", assessmentCriteria.getAssessment());
    }

    public AssessmentService getAssessmentService() {
        return assessmentService;
    }

    public void setAssessmentService(AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }

    public AnchorELOService getAnchorELOService() {
        return anchorELOService;
    }

    public void setAnchorELOService(AnchorELOService anchorELOService) {
        this.anchorELOService = anchorELOService;
    }

    public ELORefService getEloRefService() {
        return eloRefService;
    }

    public void setEloRefService(ELORefService eloRefService) {
        this.eloRefService = eloRefService;
    }
}
