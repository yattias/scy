package eu.scy.server.controllers;

import eu.scy.core.UserService;
import eu.scy.core.model.User;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 17.feb.2011
 * Time: 07:08:21
 * To change this template use File | Settings | File Templates.
 */
public class ManageAssignedStudentController extends BaseController{

    private UserService userService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        String userName = request.getParameter("username");
        User user = getUserService().getUser(userName);

        modelAndView.addObject("user", user);
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
