package eu.scy.server.controllers.util;

import eu.scy.server.controllers.BaseController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.jun.2011
 * Time: 05:36:36
 * To change this template use File | Settings | File Templates.
 */
public class OpenScyLabInSingleEloModeController extends BaseController {
    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        URI uri = getURI(request.getParameter(ELO_URI));
        modelAndView.addObject("eloURI", String.valueOf(uri));
    }
}
