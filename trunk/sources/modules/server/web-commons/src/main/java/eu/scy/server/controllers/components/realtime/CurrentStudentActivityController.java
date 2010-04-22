package eu.scy.server.controllers.components.realtime;

import eu.scy.core.UserService;
import eu.scy.core.model.User;
import eu.scy.core.model.runtime.AbstractRuntimeAction;
import eu.scy.core.runtime.RuntimeService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 09.apr.2010
 * Time: 10:33:02
 * To change this template use File | Settings | File Templates.
 */
public class CurrentStudentActivityController extends AbstractController {

    private RuntimeService runtimeService;
    private UserService userService;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        logger.info("LOADING CURRENT STUDENT ACTIVITY!");
        ModelAndView modelAndView = new ModelAndView();
        String status = "<i>Initializing</i>";
        String username = httpServletRequest.getParameter("username");
        if (username != null) {
            User user = getUserService().getUser(username);
            List actions = getRuntimeService().getActions(user);
            Collections.reverse(actions);
            if (actions.size() > 0) {
                AbstractRuntimeAction action = (AbstractRuntimeAction) actions.get(0);
                status = action.getActionType();
            }

        }

        /*String status = "Online";

        List randomStatuses = new LinkedList();
        randomStatuses.add("Online");
        randomStatuses.add("Offline");
        randomStatuses.add("LAS: Start");
        randomStatuses.add("Chatting with Hillary");
        randomStatuses.add("Doin' the Bartman");
        randomStatuses.add("LAS: Presentation");
        randomStatuses.add("Added ELO to portfolio");


        int randomElement = (int)(Math.random() * (randomStatuses.size()- 1));
        */
        //modelAndView.addObject("status", randomStatuses.get(randomElement));
        modelAndView.addObject("status", status);

        return modelAndView;
    }

    public RuntimeService getRuntimeService() {
        return runtimeService;
    }

    public void setRuntimeService(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}