package eu.scy.server.controllers;

import eu.scy.core.AnchorELOService;
import eu.scy.core.AssessmentService;
import eu.scy.core.model.pedagogicalplan.AnchorELO;
import eu.scy.core.model.pedagogicalplan.Assessment;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 18.mar.2010
 * Time: 21:28:53
 * To change this template use File | Settings | File Templates.
 */
public class ViewAnchorELOController extends BaseController {


    private AnchorELOService anchorELOService;
    private AssessmentService assessmentService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        logger.info("ANCHOR ELO: " + getAnchorELOService().getAnchorELO(request.getParameter("anchorELOId")));
        AnchorELO anchorELO = null;
        if(request.getParameter("anchorELOId") != null) {
            anchorELO = getAnchorELOService().getAnchorELO(request.getParameter("anchorELOId"));
        } else {
            anchorELO = (AnchorELO) getModel();
        }

        if(request.getParameter("action") != null) {
            String action = request.getParameter("action");
            if(action.equals("addCriteria")) addCriteria(anchorELO);
            else if(action.equals("addScoreDefinition")) addScoreDefinition(anchorELO);
        }


        modelAndView.addObject("assessment", anchorELO.getAssessment());
        setModel(anchorELO);
    }

    private void addScoreDefinition(AnchorELO anchorELO) {
        Assessment assessment = anchorELO.getAssessment();
        getAssessmentService().addScoreDefinition(assessment);
    }

    private void addCriteria(AnchorELO anchorELO) {
        Assessment assessment = anchorELO.getAssessment();
        if(anchorELO.getAssessment() == null) {
            getAssessmentService().addAssessment(anchorELO);
        }
        if(assessment != null) {
            getAssessmentService().addCriteria(assessment);
        } else {
            logger.info("Assessment is null!!");
        }


    }


    public AnchorELOService getAnchorELOService() {
        return anchorELOService;
    }

    public void setAnchorELOService(AnchorELOService anchorELOService) {
        this.anchorELOService = anchorELOService;
    }

    public AssessmentService getAssessmentService() {
        return assessmentService;
    }

    public void setAssessmentService(AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }
}
