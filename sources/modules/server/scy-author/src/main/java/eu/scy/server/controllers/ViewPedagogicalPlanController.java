package eu.scy.server.controllers;

import eu.scy.core.LASService;
import eu.scy.core.PedagogicalPlanPersistenceService;
import eu.scy.core.model.pedagogicalplan.AnchorELO;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.feb.2010
 * Time: 09:46:42
 * To change this template use File | Settings | File Templates.
 */
public class ViewPedagogicalPlanController extends BaseController {

    private PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService;
    private LASService lasService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        String pedPlanId = request.getParameter("id");
        logger.info("PED PLAN ID: " + pedPlanId);
        PedagogicalPlan plan = getPedagogicalPlanPersistenceService().getPedagogicalPlan(pedPlanId);
        String publish = request.getParameter("publish");
        if(publish != null) {
            if(publish.equals("true")) {
                plan.setPublished(true);
            } else {
                plan.setPublished(false);
            }
            getPedagogicalPlanPersistenceService().save(plan);
        }
        logger.info("Setting plan: " + plan.getName());
        modelAndView.addObject("pedagogicalPlan", plan);
        modelAndView.addObject("anchorElos", getAnchorELOs(plan));

        modelAndView.addObject("learningActivitySpaces", getLasService().getAllLearningActivitySpacesForScenario(plan.getScenario()));
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
}
