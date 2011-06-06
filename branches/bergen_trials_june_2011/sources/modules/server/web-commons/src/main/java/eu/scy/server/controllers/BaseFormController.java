package eu.scy.server.controllers;

import eu.scy.core.ServerService;
import eu.scy.core.model.ScyBase;
import eu.scy.core.model.Server;
import eu.scy.server.controllers.ui.OddEven;
import eu.scy.server.url.UrlInspector;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 01.jul.2010
 * Time: 12:39:49
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseFormController extends SimpleFormController {


    protected abstract ModelAndView onSubmit(
            HttpServletRequest request,
            HttpServletResponse response,
            Object command,
            BindException errors) throws Exception ;


    private ServerService serverService;
    private ScyBase model;
    private UrlInspector urlInspector;

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

    protected void populateView(HttpServletRequest request, HttpServletResponse httpServletResponse, ModelAndView modelAndView) {
        modelAndView.addObject("model", getModel());
        modelAndView.addObject("oddEven", new OddEven());
        modelAndView.addObject("baseUrl", request.getScheme() + "://" + request.getServerName()+ ":" + request.getServerPort() +  request.getContextPath());
        if(getServer() != null) modelAndView.addObject("css", getServer().getServerCSS());
    }

    //Override
    /*protected final ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        ModelAndView modelAndView = new ModelAndView();

        instpectRequest(request, httpServletResponse);

        handleRequest(request, httpServletResponse, modelAndView);
        populateView(request, httpServletResponse, modelAndView);
        return modelAndView;
    } */

    protected void instpectRequest(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        logger.info("-----------------------------------------------------");
        if(getUrlInspector() != null) {
            Object model = getUrlInspector().instpectRequest(request, httpServletResponse);
            setModel((ScyBase) model);
        }
        logger.info("*******************************************************");
    }

    //protected abstract void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView);

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


}
