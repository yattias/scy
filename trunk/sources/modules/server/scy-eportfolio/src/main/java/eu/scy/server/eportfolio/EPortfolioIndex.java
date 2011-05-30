package eu.scy.server.eportfolio;

import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.UserService;
import eu.scy.core.model.User;
import eu.scy.core.roolo.MissionELOService;
import eu.scy.core.roolo.RuntimeELOService;
import eu.scy.server.controllers.BaseController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 27.sep.2010
 * Time: 07:09:51
 * To change this template use File | Settings | File Templates.
 */
public class EPortfolioIndex extends BaseController {

    private UserService userService;
    private MissionELOService missionELOService;
    private RuntimeELOService runtimeELOService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        logger.info("HANDLING REQUEST!!!!!!");
        modelAndView.addObject("currentUser", getCurrentUser(request));
        modelAndView.addObject("text", "bla bla bla!");
        modelAndView.addObject("author", getCurrentUser(request).getUserDetails().hasGrantedAuthority("ROLE_AUTHOR"));
        modelAndView.addObject("toolURLProvider", "/webapp/app/eportfolio/xml/toolURLProvider.html");
        modelAndView.addObject("la", getCurrentUser(request).getUserDetails().getLocale());
        try {
            if(getScyElo() != null) {
                if(request.getParameter("missionURI") != null) {
                    modelAndView.addObject("missionURI", URLEncoder.encode(request.getParameter("missionURI"), "UTF-8"));
                } else {
                    modelAndView.addObject("missionURI", URLEncoder.encode(getScyElo().getUri().toString(), "UTF-8"));
                }

            } else {
                List runtimeElos = getRuntimeELOService().getRuntimeElosForUser(getCurrentUserName(request));
                if(runtimeElos != null && runtimeElos.size() > 0) {
                    //select default
                    ScyElo scyElo = (ScyElo) runtimeElos.get(0);
                    modelAndView.addObject("missionURI", URLEncoder.encode(scyElo.getUri().toString(), "UTF-8"));

                }

            }

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

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }

    public RuntimeELOService getRuntimeELOService() {
        return runtimeELOService;
    }

    public void setRuntimeELOService(RuntimeELOService runtimeELOService) {
        this.runtimeELOService = runtimeELOService;
    }
}
