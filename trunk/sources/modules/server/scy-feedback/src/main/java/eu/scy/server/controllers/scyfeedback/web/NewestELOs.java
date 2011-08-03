package eu.scy.server.controllers.scyfeedback.web;

import eu.scy.server.controllers.BaseController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Lars
 * Date: 03.jul.2011
 * Time: 10:48:45
 * To change this template use File | Settings | File Templates.
 */
public class NewestELOs extends BaseController {
    private String eloURI;

    public String getEloURI() {
        return eloURI;
    }

    public void setEloURI(String eloURI) {
        this.eloURI = eloURI;
    }

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


}
