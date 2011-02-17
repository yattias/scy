package eu.scy.server.controllers;

import eu.scy.common.scyelo.ScyElo;
import eu.scy.core.ServerService;
import eu.scy.core.model.ScyBase;
import eu.scy.core.model.Server;
import eu.scy.server.controllers.ui.OddEven;
import eu.scy.server.url.UrlInspector;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.feb.2010
 * Time: 09:47:07
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseController extends AbstractController {

    private ServerService serverService;
    private ScyBase model;
    private UrlInspector urlInspector;
    private ScyElo scyElo;

    public ScyElo getScyElo() {
        return scyElo;
    }

    public void setScyElo(ScyElo scyElo) {
        this.scyElo = scyElo;
    }

    public ScyBase getModel() {
        return model;
    }

    public void setModel(ScyBase model) {
        logger.info("SETTING MODEL : " + model);
        this.model = model;
    }

    public Server getServer() {
        if (getServerService() != null) {
            return getServerService().getServer();
        }
        return null;

    }

    private void populateView(HttpServletRequest request, HttpServletResponse httpServletResponse, ModelAndView modelAndView) {
        modelAndView.addObject("model", getModel());
        modelAndView.addObject("oddEven", new OddEven());
        modelAndView.addObject("baseUrl", request.getScheme() + "://" + request.getServerName()+ ":" + request.getServerPort() +  request.getContextPath());
        Locale locale = request.getLocale();
        modelAndView.addObject("language", locale.getLanguage());
        if(getServer() != null) modelAndView.addObject("css", getServer().getServerCSS());
    }

    @Override
    protected final ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        logger.info("NEW REQUEST ARRIVED:");

        Enumeration parameEnumeration = request.getParameterNames();
        while(parameEnumeration.hasMoreElements()) {
            String param = (String) parameEnumeration.nextElement();
            logger.info("** ** ** RECEIVED PARAM: " + param + " " + request.getParameter(param));
            
        }

        ModelAndView modelAndView = new ModelAndView();

        instpectRequest(request, httpServletResponse);

        handleRequest(request, httpServletResponse, modelAndView);
        populateView(request, httpServletResponse, modelAndView);
        return modelAndView;
    }

    protected void instpectRequest(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        logger.info("----------------------------------------------------- " + getClass().getName());
        if(getUrlInspector() != null) {
            Object model = getUrlInspector().instpectRequest(request, httpServletResponse);
            if(model instanceof ScyElo) {
                logger.info("Setting ELO: " + ((ScyElo)model). getTechnicalFormat());
                setScyElo((ScyElo) model);
            } else {
                setModel((ScyBase) model);
            }

        }
        logger.info("*******************************************************" + getClass().getName());
    }

    protected abstract void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView);

    public ServerService getServerService() {
        return serverService;
    }

    public void setServerService(ServerService serverService) {
        this.serverService = serverService;
    }

    public UrlInspector getUrlInspector() {
        return urlInspector;
    }

    public void setUrlInspector(UrlInspector urlInspector) {
        this.urlInspector = urlInspector;
    }

    public String getCurrentUserName(HttpServletRequest request) {
       org.springframework.security.userdetails.User user = (org.springframework.security.userdetails.User) request.getSession().getAttribute("CURRENT_USER");
       return user.getUsername();
   }
}
