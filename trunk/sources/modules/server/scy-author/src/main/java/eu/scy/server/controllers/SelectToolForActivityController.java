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
public class SelectToolForActivityController extends BaseController {


    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        String id = request.getParameter("id");
        String clazz = request.getParameter("clazz");

        modelAndView.addObject("id", id);
        modelAndView.addObject("class", clazz);

    }

}
