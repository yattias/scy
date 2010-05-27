package eu.scy.server.controllers.components.realtime;

import eu.scy.core.UserService;
import eu.scy.core.model.User;
import eu.scy.core.model.runtime.AbstractRuntimeAction;
import eu.scy.core.model.runtime.EloRuntimeAction;
import eu.scy.core.model.runtime.LASRuntimeAction;
import eu.scy.core.model.runtime.ToolRuntimeAction;
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
        long start = System.currentTimeMillis();
        logger.debug("LOADING CURRENT STUDENT ACTIVITY!");
        ModelAndView modelAndView = new ModelAndView();
        String status = "<i>Initializing</i>";
        String username = httpServletRequest.getParameter("username");
        if (username != null) {
            User user = getUserService().getUser(username);
            AbstractRuntimeAction latestInterestingAction = getRuntimeService().getLatestInterestingAction(user);
            String las = getRuntimeService().getCurrentLAS(user);
            status = las + ", " ;
            String currentTool = getRuntimeService().getCurrentTool(user);
            String currentELO = getRuntimeService().getCurrentELO(user);
            if(currentTool.length() > 0) currentTool += ", ";

            status += currentTool;

            if(currentELO != null) status += currentELO + ", ";

            if(latestInterestingAction  instanceof ToolRuntimeAction) {
                status += latestInterestingAction.getActionType();
            } else if (latestInterestingAction instanceof EloRuntimeAction) {
                status += ((EloRuntimeAction)latestInterestingAction).getEloUri();
            }

        }

        modelAndView.addObject("status", status);

        logger.info("used " + (System.currentTimeMillis() - start) + " millis to prepare runtime activity for user " + username);
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