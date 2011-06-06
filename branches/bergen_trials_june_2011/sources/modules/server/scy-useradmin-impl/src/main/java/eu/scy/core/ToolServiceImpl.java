package eu.scy.core;

import eu.scy.core.model.pedagogicalplan.Tool;
import eu.scy.core.persistence.ToolDAO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 20.jan.2010
 * Time: 11:08:14
 * To change this template use File | Settings | File Templates.
 */
public class ToolServiceImpl extends BaseServiceImpl implements ToolService{

    private ToolDAO toolDAO;

    public ToolDAO getToolDAO() {
        return (ToolDAO) getScyBaseDAO();
    }

    public void setToolDAO(ToolDAO toolDAO) {
        this.toolDAO = toolDAO;
    }

    @Override
    public Tool getToolByToolId(String toolId) {
        return getToolDAO().getToolByToolId(toolId);
    }

    @Override
    @Transactional
    public void registerTool(String toolId) {
        getToolDAO().registerTool(toolId);
    }

    @Override
    public Tool findToolByName(String name) {
        return getToolDAO().findToolByName(name);
    }

    @Override
    public List getTools() {
        return getToolDAO().getTools();
    }

    @Override
    @Transactional
    public void addTool() {
        getToolDAO().addTool();

    }

    @Override
    public Integer getUsageOfTool(Tool tool) {
        return getToolDAO().getUsageOfTool(tool);
    }
}
