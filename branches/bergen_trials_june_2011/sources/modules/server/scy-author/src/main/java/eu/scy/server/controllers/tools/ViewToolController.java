package eu.scy.server.controllers.tools;

import eu.scy.core.ToolService;
import eu.scy.core.model.pedagogicalplan.Tool;
import eu.scy.server.controllers.BaseController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 28.mai.2010
 * Time: 06:37:34
 * To change this template use File | Settings | File Templates.
 */
public class ViewToolController extends BaseController {

    private ToolService toolService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        modelAndView.addObject("toolUsage", getToolService().getUsageOfTool((Tool) getModel()));
    }

    public ToolService getToolService() {
        return toolService;
    }

    public void setToolService(ToolService toolService) {
        this.toolService = toolService;
    }
}
