package eu.scy.core;

import eu.scy.core.model.pedagogicalplan.Tool;
import eu.scy.core.persistence.ToolDAO;

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
        return toolDAO;
    }

    public void setToolDAO(ToolDAO toolDAO) {
        this.toolDAO = toolDAO;
    }

    @Override
    public void save(Tool tool) {
        getToolDAO().save(tool);
    }

    @Override
    public Tool findToolByName(String name) {
        return getToolDAO().findToolByName(name);
    }
}
