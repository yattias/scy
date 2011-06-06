package eu.scy.core;

import eu.scy.core.model.pedagogicalplan.Tool;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 20.jan.2010
 * Time: 11:07:39
 * To change this template use File | Settings | File Templates.
 */
public interface ToolService extends BaseService{

    public Tool getToolByToolId(String toolId);

    public void registerTool(String toolId);

    public Tool findToolByName(String name);

    List getTools();

    void addTool();

    Integer getUsageOfTool(Tool tool);
}
