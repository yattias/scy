package eu.scy.server.controllers;

import eu.scy.core.PedagogicalPlanPersistenceService;
import eu.scy.core.model.impl.pedagogicalplan.PedagogicalPlanTemplateImpl;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlanTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 18.mar.2010
 * Time: 23:16:06
 * To change this template use File | Settings | File Templates.
 */
public class ViewCreateNewPedagogicalPlanController extends BaseController{

    private PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        logger.info("CREATING NEW PEDAGOGICAL PLAN!");
        PedagogicalPlanTemplate template = new PedagogicalPlanTemplateImpl();
        template.setName("New pedagogical plan");
        template.setDescription("A pedagogical plan");
        getPedagogicalPlanPersistenceService().save(template);
        PedagogicalPlan plan = getPedagogicalPlanPersistenceService().createPedagogicalPlan(template);
        setModel(plan);
    }

    public PedagogicalPlanPersistenceService getPedagogicalPlanPersistenceService() {
        return pedagogicalPlanPersistenceService;
    }

    public void setPedagogicalPlanPersistenceService(PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService) {
        this.pedagogicalPlanPersistenceService = pedagogicalPlanPersistenceService;
    }
}
