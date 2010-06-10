package eu.scy.server.controllers.scyfeedback;

import eu.scy.core.ELORefService;
import eu.scy.server.controllers.BaseController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.jun.2010
 * Time: 07:07:25
 * To change this template use File | Settings | File Templates.
 */
public class ScyFeedbackIndexController extends BaseController {

    private ELORefService eloRefService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        modelAndView.addObject("eloRefs", getEloRefService().getELORefs());

    }


    public ELORefService getEloRefService() {
        return eloRefService;
    }

    public void setEloRefService(ELORefService eloRefService) {
        this.eloRefService = eloRefService;
    }
}
