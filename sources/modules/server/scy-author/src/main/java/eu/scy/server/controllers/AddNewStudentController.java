package eu.scy.server.controllers;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 01.sep.2011
 * Time: 06:46:44
 * To change this template use File | Settings | File Templates.
 */
public class AddNewStudentController extends BaseController {
    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        URI uri = getURI(request.getParameter(ELO_URI));

        modelAndView.addObject(ELO_URI, getEncodedUri(uri.toString()));
    }
}
