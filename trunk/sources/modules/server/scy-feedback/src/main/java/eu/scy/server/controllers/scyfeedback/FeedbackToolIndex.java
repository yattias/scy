package eu.scy.server.controllers.scyfeedback;

import eu.scy.core.roolo.MissionELOService;
import eu.scy.server.controllers.BaseController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 21.jan.2011
 * Time: 13:41:27
 * To change this template use File | Settings | File Templates.
 */
public class FeedbackToolIndex extends BaseController {

    private MissionELOService missionELOService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        //List feedback = getMissionELOService().getFeedback();
        //logger.info("FOUND " + feedback.size() + " FEEDBACKS!");
        modelAndView.setViewName("forward:feedbackindex.html");

    }

    public MissionELOService getMissionELOService() {
        return missionELOService;
    }

    public void setMissionELOService(MissionELOService missionELOService) {
        this.missionELOService = missionELOService;
    }
}
