package eu.scy.server.controllers;

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
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        String checked = httpServletRequest.getParameter("ajaxCheckBox");
        logger.info("CHECKBOX: " + checked);


        Iterator it = httpServletRequest.getParameterMap().keySet().iterator();
        while (it.hasNext()) {
            String s = (String) it.next();
            logger.info(s + " " + httpServletRequest.getParameterMap().get(s));
        }


        ModelAndView modelAndView = new ModelAndView();

        return modelAndView;
    }
}
