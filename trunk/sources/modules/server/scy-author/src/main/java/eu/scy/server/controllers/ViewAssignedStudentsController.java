package eu.scy.server.controllers;

import eu.scy.core.UserService;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 18.mar.2010
 * Time: 22:29:04
 * To change this template use File | Settings | File Templates.
 */
public class ViewAssignedStudentsController extends BaseController{

    private UserService userService;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        ModelAndView modelAndView = new ModelAndView();

        populateView(request, httpServletResponse, modelAndView);

        modelAndView.addObject("students", getUserService().getStudents());


        return modelAndView;

    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
