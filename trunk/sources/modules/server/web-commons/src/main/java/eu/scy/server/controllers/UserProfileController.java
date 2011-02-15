package eu.scy.server.controllers;

import eu.scy.core.UserService;
import eu.scy.core.model.User;
import eu.scy.core.model.impl.SCYStudentUserDetails;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 15.feb.2011
 * Time: 06:25:23
 * To change this template use File | Settings | File Templates.
 */
public class UserProfileController extends BaseController{

    private UserService userService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        User user = getUserService().getUser(getCurrentUserName(request));
        modelAndView.addObject("user", user);
        modelAndView.addObject("userDetails", user.getUserDetails());


         if(user.getUserDetails() instanceof SCYStudentUserDetails) {
            SCYStudentUserDetails details = (SCYStudentUserDetails) user.getUserDetails();
            if(details.getProfilePicture() != null) {
                modelAndView.addObject("showProfilePicture", true);
            } else {
                modelAndView.addObject("showProfilePicture", false);
            }
        } else {
            modelAndView.addObject("showProfilePicture", false);
        }

    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
