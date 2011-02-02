package eu.scy.server.controllers;

import eu.scy.core.LearningMaterialService;
import eu.scy.core.MissionService;
import eu.scy.core.model.impl.pedagogicalplan.LearningMaterialImageImpl;
import eu.scy.core.model.pedagogicalplan.LearningMaterialImage;
import eu.scy.core.model.pedagogicalplan.Mission;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 03.mai.2010
 * Time: 13:51:14
 * To change this template use File | Settings | File Templates.
 */
public class AddFileToMissionController extends BaseController{

    private MissionService missionService;

    private LearningMaterialService learningMaterialService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        String action = request.getParameter("action");
        String missionId = request.getParameter("model");
        Mission mission = null;
        if(missionId != null && missionId.length() > 0) {
            mission = getMissionService().getMission(missionId);
        }
        if(action != null && action.length() > 0) {
            if(action.equals("addImage")) addImage(mission, request, response, modelAndView);
        }
    }

    private void addImage(Mission mission, HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        LearningMaterialImage learningMaterialImage = new LearningMaterialImageImpl();
        getLearningMaterialService().save(learningMaterialImage);
        mission.addLearningMaterial(learningMaterialImage);
        getMissionService().save(mission);

        modelAndView.addObject("model", mission);
        modelAndView.addObject("learningMaterialImage", learningMaterialImage);
    }

    public MissionService getMissionService() {
        return missionService;
    }

    public void setMissionService(MissionService missionService) {
        this.missionService = missionService;
    }

    public LearningMaterialService getLearningMaterialService() {
        return learningMaterialService;
    }

    public void setLearningMaterialService(LearningMaterialService learningMaterialService) {
        this.learningMaterialService = learningMaterialService;
    }
}
