package eu.scy.server.controllers.scyauthorruntime;

import eu.scy.core.LASService;
import eu.scy.core.PedagogicalPlanPersistenceService;
import eu.scy.core.UserService;
import eu.scy.core.model.User;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;
import eu.scy.core.runtime.RuntimeService;
import eu.scy.server.controllers.BaseController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 30.mai.2010
 * Time: 21:30:08
 * To change this template use File | Settings | File Templates.
 */
public class ViewLASRuntimeInfo extends BaseController {

    private LASService lasService;
    private PedagogicalPlanPersistenceService pedagogicalPlanPersistenceService;
    private UserService userService;
    private RuntimeService runtimeService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        if(request.getParameter("username") != null) displayUser(request, response, modelAndView);
        else displayLAS(request, response, modelAndView);
    }

    private void displayLAS(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        LearningActivitySpace learningActivitySpace = getLasService().getLearningActivitySpace(request.getParameter("id"));
        logger.info("LOADING LAS: " + request.getParameter("id"));
        modelAndView.addObject("MODE", "LASVIEW");
        modelAndView.addObject("showUserInfo", false);
        modelAndView.addObject("model", learningActivitySpace);
        modelAndView.addObject("currentUsers", getRuntimeService().getUsersCurrentlyInLAS(learningActivitySpace.getName()));

    }

    private void displayUser(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        User user = getUserService().getUser(request.getParameter("username"));
        modelAndView.addObject("MODE", "USERVIEW");
        modelAndView.addObject("showUserInfo", true);
        modelAndView.addObject("model", user);
        modelAndView.addObject("userdetails", user.getUserDetails());

        modelAndView.addObject("currentTool", getRuntimeService().getCurrentTool(user));
        modelAndView.addObject("currentELO", getRuntimeService().getCurrentELO(user));
        modelAndView.addObject("currentLAS", getRuntimeService().getCurrentLAS(user));
        modelAndView.addObject("lastElos", getRuntimeService().getLastELOs(user));



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

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public RuntimeService getRuntimeService() {
        return runtimeService;
    }

    public void setRuntimeService(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }
}
