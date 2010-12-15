package eu.scy.server.controllers.components.runtimemissionselector;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.des.2010
 * Time: 10:15:37
 * To change this template use File | Settings | File Templates.
 */
public class RuntimeMissionSelector extends AbstractController {
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        String eloURI = request.getParameter("eloURI");
        ModelAndView modelAndView = new ModelAndView();
        if (eloURI != null) {
            eloURI = URLDecoder.decode(eloURI, "UTF-8");
            request.getSession().setAttribute("currentMissionURI", eloURI);
            //modelAndView.setViewName("forward:" + request.getParameter("viewName"));

        }
        return modelAndView;

    }
}
