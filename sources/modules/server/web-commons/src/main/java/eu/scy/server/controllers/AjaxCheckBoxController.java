package eu.scy.server.controllers;

import eu.scy.core.ScenarioService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 23.mar.2010
 * Time: 13:16:04
 * To change this template use File | Settings | File Templates.
 */
public class AjaxCheckBoxController extends AbstractController {

    private ScenarioService scenarioService;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        final String CHECKED = "ajaxCheckBoxValue";
        Boolean checked = Boolean.FALSE;
        String checkedString = httpServletRequest.getParameter(CHECKED);
        if(checkedString != null) {
            checked = Boolean.TRUE;
        }

        ModelAndView modelAndView = new ModelAndView();

        return modelAndView;
    }

    public ScenarioService getScenarioService() {
        return scenarioService;
    }

    public void setScenarioService(ScenarioService scenarioService) {
        this.scenarioService = scenarioService;
    }
}
