package eu.scy.core.persistence;

import eu.scy.core.model.pedagogicalplan.Tool;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 20.jan.2010
 * Time: 11:07:56
 * To change this template use File | Settings | File Templates.
 */
public interface ToolDAO {

    void save(Tool tool);

    Tool findToolByName(String name);

    List getTools();

    void addTool();

    void registerTool(String toolId);

    Tool getToolByToolId(String toolId);

    Integer getUsageOfTool(Tool tool);
}
