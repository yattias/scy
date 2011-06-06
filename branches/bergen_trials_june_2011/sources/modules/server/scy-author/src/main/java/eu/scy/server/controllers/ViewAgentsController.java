package eu.scy.server.controllers;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 18.mar.2010
 * Time: 19:18:27
 * To change this template use File | Settings | File Templates.
 */
public class ViewAgentsController extends BaseController {

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        List agentLevels = new LinkedList();
        agentLevels.add("Low");
        agentLevels.add("Medium");
        agentLevels.add("High");

        List contentLevels = new LinkedList();
        contentLevels.add("Low");
        contentLevels.add("Medium");
        contentLevels.add("High");
        modelAndView.addObject("agentLevels", agentLevels);
        modelAndView.addObject("contentLevels", contentLevels);
    }
}
