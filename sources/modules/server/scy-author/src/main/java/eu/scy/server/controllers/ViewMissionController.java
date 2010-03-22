package eu.scy.server.controllers;

import eu.scy.core.MissionService;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 19.mar.2010
 * Time: 22:29:57
 * To change this template use File | Settings | File Templates.
 */
public class ViewMissionController extends BaseController{

    private MissionService missionService;


    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {

        setModel(getMissionService().getMission(request.getParameter("missionId")));

    }

    public MissionService getMissionService() {
        return missionService;
    }

    public void setMissionService(MissionService missionService) {
        this.missionService = missionService;
    }
}
