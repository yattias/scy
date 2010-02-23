package eu.scy.server.controllers;

import eu.scy.core.ServerService;
import eu.scy.core.model.ScyBase;
import eu.scy.core.model.Server;
import eu.scy.core.model.impl.ScyBaseObject;
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
public class BaseController extends AbstractController {

    private ServerService serverService;

    private ScyBase model;

    public ScyBase getModel(){
        return model;
    }
    public void setModel(ScyBase model) {
        this.model = model;
    }

    public Server getServer() {
        return getServerService().getServer();    
    }

    protected void populateView(HttpServletRequest request, HttpServletResponse httpServletResponse, ModelAndView modelAndView) {
        modelAndView.addObject("model", getModel());
        modelAndView.addObject("css", getServer().getServerCSS());

    }

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        ModelAndView modelAndView = new ModelAndView();

        

        return modelAndView;
    }

    public ServerService getServerService() {
        return serverService;
    }

    public void setServerService(ServerService serverService) {
        this.serverService = serverService;
    }
}
