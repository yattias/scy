package eu.scy.server.controllers;

import eu.scy.core.ServerService;
import eu.scy.core.model.ScyBase;
import eu.scy.core.model.Server;
import eu.scy.core.model.impl.ScyBaseObject;
import eu.scy.server.common.OddEven;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    public ScyBase getModel() {
        return model;
    }

    public void setModel(ScyBase model) {
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
        if(getServer() != null) modelAndView.addObject("css", getServer().getServerCSS());
    }

    @Override
    protected final ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        ModelAndView modelAndView = new ModelAndView();

        instpectRequest(request, httpServletResponse);

        handleRequest(request, httpServletResponse, modelAndView);
        populateView(request, httpServletResponse, modelAndView);
        return modelAndView;
    }

    protected void instpectRequest(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        if(request.getParameter("model") != null) {
            logger.info("FOUND MODEL .... " + request.getParameter("model"));
            logger.info("TRYING TO CONVERT....");
        }
    }

    protected abstract void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView);

    public ServerService getServerService() {
        return serverService;
    }

    public void setServerService(ServerService serverService) {
        this.serverService = serverService;
    }
}
