package eu.scy.server.controllers;

import eu.scy.core.LASService;
import eu.scy.core.model.pedagogicalplan.AssessmentStrategyType;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.feb.2010
 * Time: 12:02:09
 * To change this template use File | Settings | File Templates.
 */
public class ViewLASController extends BaseController {

    private LASService lasService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        String id = request.getParameter("id");
        logger.info("LAS ID: " + id);
        setModel(getLasService().getLearningActivitySpace(id));

        List assessmentStrategies = new LinkedList();
        assessmentStrategies.add(AssessmentStrategyType.PEER_TO_PEER);
        assessmentStrategies.add(AssessmentStrategyType.SINGLE);
        assessmentStrategies.add(AssessmentStrategyType.TEACHER);

        modelAndView.addObject("assessmentStrategies", assessmentStrategies);
    }


    public LASService getLasService() {
        return lasService;
    }

    public void setLasService(LASService lasService) {
        this.lasService = lasService;
    }
}
