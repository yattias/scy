package eu.scy.server.controllers;

import eu.scy.core.AnchorELOService;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 18.mar.2010
 * Time: 21:28:53
 * To change this template use File | Settings | File Templates.
 */
public class ViewAnchorELOController extends BaseController {


    private AnchorELOService anchorELOService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        logger.info("ANCHOR ELO: " + getAnchorELOService().getAnchorELO(request.getParameter("anchorELOId")));
        setModel(getAnchorELOService().getAnchorELO(request.getParameter("anchorELOId")));

    }


    public AnchorELOService getAnchorELOService() {
        return anchorELOService;
    }

    public void setAnchorELOService(AnchorELOService anchorELOService) {
        this.anchorELOService = anchorELOService;
    }
}
