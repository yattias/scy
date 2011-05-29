package eu.scy.server.controllers;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 29.mai.2011
 * Time: 21:19:16
 * To change this template use File | Settings | File Templates.
 */
public class AssessmentToolViewer extends BaseController {
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
}
