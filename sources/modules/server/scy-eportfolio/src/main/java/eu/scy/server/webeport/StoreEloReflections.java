package eu.scy.server.webeport;

import eu.scy.server.controllers.BaseController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 31.aug.2011
 * Time: 22:25:51
 * To change this template use File | Settings | File Templates.
 */
public class StoreEloReflections extends BaseController {
    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {

        String generalLearningGoals = request.getParameter("generalLearningGoals");
        String specificLearningGoals = request.getParameter("specificLearningGoals");

        logger.info("specificLearningGoals: "+ specificLearningGoals);
        logger.info("generalLearningGoals: " + generalLearningGoals);
    }
}
