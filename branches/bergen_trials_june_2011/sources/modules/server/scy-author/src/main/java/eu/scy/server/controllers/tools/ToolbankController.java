package eu.scy.server.controllers.tools;

import eu.scy.core.ToolService;
import eu.scy.server.controllers.BaseController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 24.mar.2010
 * Time: 14:23:26
 * To change this template use File | Settings | File Templates.
 */
public class ToolbankController extends BaseController {

    private ToolService toolService;

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {


        final String ADD_TOOL_COMMAND = "addTool";

        if(request.getParameter(ADD_TOOL_COMMAND) != null) {
            addTool(request, response, modelAndView);
        } else {

        }

        modelAndView.addObject("tools", getToolService().getTools());

    }

    private void addTool(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        logger.info("ADDING TOOL!");
        getToolService().addTool();
    }

    public ToolService getToolService() {
        return toolService;
    }

    public void setToolService(ToolService toolService) {
        this.toolService = toolService;
    }
}
