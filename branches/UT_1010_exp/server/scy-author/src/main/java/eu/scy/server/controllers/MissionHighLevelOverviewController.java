package eu.scy.server.controllers;

import eu.scy.core.LASService;
import eu.scy.core.PedagogicalPlanPersistenceService;
import eu.scy.core.UserService;
import eu.scy.core.model.User;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 05.okt.2010
 * Time: 11:15:34
 * To change this template use File | Settings | File Templates.
 */
public class MissionHighLevelOverviewController extends BaseController{

    private LASService lasService;
    private PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService;
    private UserService userService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        String pedPlanId = request.getParameter("pedagogicalPlanId");
        PedagogicalPlan plan = getPedagogicalPlanPersistenceService().getPedagogicalPlan(pedPlanId);
        modelAndView.addObject("learningActivitySpaces", getLasService().getAllLearningActivitySpacesForScenario(plan.getScenario()));
        modelAndView.addObject("author", getCurrentUser(request).getUserDetails().hasGrantedAuthority("ROLE_AUTHOR"));
        
    }

    public LASService getLasService() {
        return lasService;
    }

    public void setLasService(LASService lasService) {
        this.lasService = lasService;
    }

    public PedagogicalPlanPersistenceService getPedagogicalPlanPersistenceService() {
        return pedagogicalPlanPersistenceService;
    }

    public void setPedagogicalPlanPersistenceService(PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService) {
        this.pedagogicalPlanPersistenceService = pedagogicalPlanPersistenceService;
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
