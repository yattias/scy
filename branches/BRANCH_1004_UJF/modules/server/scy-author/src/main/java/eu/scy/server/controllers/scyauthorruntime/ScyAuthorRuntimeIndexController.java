package eu.scy.server.controllers.scyauthorruntime;

import eu.scy.core.PedagogicalPlanPersistenceService;
import eu.scy.server.controllers.BaseController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 18.mar.2010
 * Time: 22:51:19
 * To change this template use File | Settings | File Templates.
 */
public class ScyAuthorRuntimeIndexController extends BaseController {

    private PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        modelAndView.addObject("pedagogicalPlans", getPedagogicalPlanPersistenceService().getPedagogicalPlans());
    }


    public PedagogicalPlanPersistenceService getPedagogicalPlanPersistenceService() {
        return pedagogicalPlanPersistenceService;
    }

    public void setPedagogicalPlanPersistenceService(PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService) {
        this.pedagogicalPlanPersistenceService = pedagogicalPlanPersistenceService;
    }
}
