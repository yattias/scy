package eu.scy.server.controllers;

import eu.scy.core.PedagogicalPlanPersistenceService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 08.feb.2010
 * Time: 10:26:12
 * To change this template use File | Settings | File Templates.
 */
public class ScyIndexController extends BaseController {

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
