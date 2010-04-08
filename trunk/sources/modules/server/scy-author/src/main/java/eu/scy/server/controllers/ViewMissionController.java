package eu.scy.server.controllers;

import eu.scy.core.MissionService;
import eu.scy.core.model.impl.pedagogicalplan.LearningGoalImpl;
import eu.scy.core.model.pedagogicalplan.Mission;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 19.mar.2010
 * Time: 22:29:57
 */
public class ViewMissionController extends BaseController {

    private MissionService missionService = null;
    private final static String NEW_LEARNING_GOAL = "NEW_LEARNING_GOAL";
    private final static String NEW_LEARNING_MATERIAL = "NEW_LEARNING_MATERIAL";


    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        Mission mission = getMissionService().getMission(request.getParameter("missionId"));
        Object rq = request.getParameter("newObject");
        if (rq != null && rq.equals(NEW_LEARNING_GOAL)) {
            LearningGoalImpl lg = new LearningGoalImpl();
            lg.setName("Name " + mission.getLearningGoals().size());
            mission.addLearningGoal(lg);
            getMissionService().save(lg);
        }

        modelAndView.addObject("learningMaterials", mission.getLearningMaterials());
        modelAndView.addObject("learningGoals", mission.getLearningGoals());

        setModel(mission);
    }

    public MissionService getMissionService() {
        return missionService;
    }

    public void setMissionService(MissionService missionService) {
        this.missionService = missionService;
    }
}