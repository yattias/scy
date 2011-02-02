package eu.scy.server.assessment;

import eu.scy.core.UserService;
import eu.scy.core.model.User;
import eu.scy.server.controllers.BaseController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.jan.2011
 * Time: 13:45:19
 * To change this template use File | Settings | File Templates.
 */
public class AssessmentIndex extends BaseController {

    private UserService userService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        logger.info("HANDLING REQUEST!!!!!!");
        modelAndView.addObject("currentUser", getCurrentUser(request));
        modelAndView.addObject("text", "bla bla bla!");
        modelAndView.addObject("author", getCurrentUser(request).getUserDetails().hasGrantedAuthority("ROLE_AUTHOR"));
        modelAndView.addObject("toolURLProvider", "/webapp/app/eportfolio/xml/toolURLProvider.html");
        try {
            modelAndView.addObject("missionURI", URLEncoder.encode(getScyElo().getUri().toString(), "UTF-8"));
            
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

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
