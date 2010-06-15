package eu.scy.server.controllers.components.realtime;

import eu.scy.core.UserService;
import eu.scy.core.model.User;
import eu.scy.core.runtime.RuntimeService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.jun.2010
 * Time: 08:38:57
 * To change this template use File | Settings | File Templates.
 */
public class MyCurrentActivity extends AbstractController {

    private RuntimeService runtimeService;
    private UserService userService;



    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        String username = httpServletRequest.getParameter("username");
        if(username != null) {
            logger.debug("LOADING FOR USER " + username);
            User user = userService.getUser(username);
            String currentElo = getRuntimeService().getCurrentELO(user);
            String tool = getRuntimeService().getCurrentTool(user);
            String las = getRuntimeService().getCurrentLAS(user);

            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("user", user);
            modelAndView.addObject("currentELO", currentElo);
            modelAndView.addObject("currentTool", tool);
            modelAndView.addObject("currentLas", las);

            return modelAndView;
        } else {
            logger.warn("DID NOT FIND USERNAME IN URL");
        }

        return null;

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
