package eu.scy.server.controllers;

import eu.scy.core.ActivityService;
import eu.scy.core.model.pedagogicalplan.Activity;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 18.mar.2010
 * Time: 18:30:21
 * To change this template use File | Settings | File Templates.
 */
public class ViewActivityController extends BaseController {

    private ActivityService activityService;


    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        String activityId = request.getParameter("activityId");
        setModel(getActivityService().getActivity(activityId));
    }


    public ActivityService getActivityService() {
        return activityService;
    }

    public void setActivityService(ActivityService activityService) {
        this.activityService = activityService;
    }
}
