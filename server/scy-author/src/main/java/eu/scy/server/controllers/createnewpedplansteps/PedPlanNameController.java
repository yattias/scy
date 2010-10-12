package eu.scy.server.controllers.createnewpedplansteps;

import eu.scy.core.PedagogicalPlanPersistenceService;
import eu.scy.core.model.impl.pedagogicalplan.PedagogicalPlanTemplateImpl;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlanTemplate;
import eu.scy.core.model.pedagogicalplan.Scenario;
import eu.scy.server.controllers.BaseController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 09.okt.2010
 * Time: 05:57:49
 * To change this template use File | Settings | File Templates.
 */
public class PedPlanNameController extends BaseController {

    private PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
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
