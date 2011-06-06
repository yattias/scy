package eu.scy.server.controllers;

import eu.scy.core.LASService;
import eu.scy.core.UserService;
import eu.scy.core.model.User;
import eu.scy.core.model.pedagogicalplan.AssessmentStrategyType;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.feb.2010
 * Time: 12:02:09
 * To change this template use File | Settings | File Templates.
 */
public class ViewLASController extends BaseController {

    private LASService lasService;
    private UserService userService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        String id = request.getParameter("id");
        logger.info("LAS ID: " + id);
        setModel(getLasService().getLearningActivitySpace(id));

        List assessmentStrategies = new LinkedList();
        assessmentStrategies.add(AssessmentStrategyType.PEER_TO_PEER);
        assessmentStrategies.add(AssessmentStrategyType.SINGLE);
        assessmentStrategies.add(AssessmentStrategyType.TEACHER);

        modelAndView.addObject("assessmentStrategies", assessmentStrategies);
        modelAndView.addObject("author", getCurrentUser(request).getUserDetails().hasGrantedAuthority("ROLE_AUTHOR"));
    }


    public LASService getLasService() {
        return lasService;
    }

    public void setLasService(LASService lasService) {
        this.lasService = lasService;
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
