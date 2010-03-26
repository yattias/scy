package eu.scy.server.controllers;

import eu.scy.core.ActivityService;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

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

        List teacherRoles = new LinkedList();
        teacherRoles.add("Activator");
        teacherRoles.add("Facilitator");
        teacherRoles.add("Observer");

        modelAndView.addObject("teacherRoles", teacherRoles);

        List workArrangement = new LinkedList();
        workArrangement.add("Individual");
        workArrangement.add("Group");
        workArrangement.add("Peer to peer");

        modelAndView.addObject("workArrangement", workArrangement);
    }


    public ActivityService getActivityService() {
        return activityService;
    }

    public void setActivityService(ActivityService activityService) {
        this.activityService = activityService;
    }
}
