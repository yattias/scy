package eu.scy.server.eportfolio;

import eu.scy.core.UserService;
import eu.scy.core.model.User;
import eu.scy.server.controllers.BaseController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 27.sep.2010
 * Time: 07:09:51
 * To change this template use File | Settings | File Templates.
 */
public class EPortfolioIndex extends BaseController {

    private UserService userService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        logger.info("HANDLING REQUEST!!!!!!");
        modelAndView.addObject("currentUser", getCurrentUser(request));
        modelAndView.addObject("text", "bla bla bla!");
        modelAndView.addObject("author", getCurrentUser(request).getUserDetails().hasGrantedAuthority("ROLE_AUTHOR"));
        modelAndView.addObject("toolURLProvider", "/webapp/app/eportfolio/xml/toolURLProvider.html");
        modelAndView.addObject("missionURI", getScyElo().getUri());
    }

     public User getCurrentUser(HttpServletRequest request) {
        return getUserService().getUser(getCurrentUserName(request));
    }

    public String getCurrentUserName(HttpServletRequest request) {
       org.springframework.security.userdetails.User user = (org.springframework.security.userdetails.User) request.getSession().getAttribute("CURRENT_USER");
        logger.info("UserName: " + user.getUsername());
       return user.getUsername();
   }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
