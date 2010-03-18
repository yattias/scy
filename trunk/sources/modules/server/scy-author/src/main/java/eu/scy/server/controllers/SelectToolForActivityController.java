package eu.scy.server.controllers;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 18.mar.2010
 * Time: 19:02:05
 */
public class SelectToolForActivityController extends BaseController{


    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {

        String id = request.getParameter("id");
        String clazz = request.getParameter("clazz");

        ModelAndView modelAndView = new ModelAndView();
        populateView(request, httpServletResponse, modelAndView);

        modelAndView.addObject("id", id);
        modelAndView.addObject("class", clazz);

        return modelAndView;
    }
}
