package eu.scy.server.controllers;

import eu.scy.core.UserService;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 29.mar.2010
 * Time: 08:14:34
 * To change this template use File | Settings | File Templates.
 */
public class SelectStudentsForPedagogicalPlan extends BaseController{

    private UserService userService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        String eloURI = request.getParameter("eloURI");
        String version = request.getParameter("eloVersion");
        modelAndView.addObject("eloURI", eloURI);
        modelAndView.addObject("eloVersion", eloURI);
        List students = getUserService().getStudents();
        modelAndView.addObject("students", students);
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
