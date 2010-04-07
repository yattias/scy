package eu.scy.server.controllers.components.help;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 30.mar.2010
 * Time: 09:05:33
 * To change this template use File | Settings | File Templates.
 */
public class HelpController extends AbstractController {
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        String helpId =httpServletRequest.getParameter("id");
        modelAndView.addObject("id", helpId);
        return modelAndView;
    }
}
