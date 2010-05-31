package eu.scy.server.controllers.components.help;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 30.mar.2010
 * Time: 09:05:33
 */
public class HelpController extends AbstractController {
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        String helpId = httpServletRequest.getParameter("id");



        if(helpId.equals("HELP_PAGE_1_AUTO_MAKE_BUDDIES")) {
            helpId = "When users are added to the mission, they will automatically become eachothers buddies.";
        }

        modelAndView.addObject("id", helpId);
        return modelAndView;
    }
}
