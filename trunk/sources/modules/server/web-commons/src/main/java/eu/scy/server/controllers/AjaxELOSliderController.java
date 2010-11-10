package eu.scy.server.controllers;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.nov.2010
 * Time: 12:47:13
 * To change this template use File | Settings | File Templates.
 */
public class AjaxELOSliderController extends AbstractAjaxELOController {
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        String property = request.getParameter(PROPERTY);
        String uri = request.getParameter(URI);
        String value = request.getParameter("value");
        logger.info("VALUE:" + value);
        logger.info("URI: " + uri);
        logger.info("PROPERTY: " + property);

        executeSetter(uri, property, value);

        ModelAndView modelAndView = new ModelAndView();
        return modelAndView;
    }
}
