package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.impl.pedagogicalplan.ToolImpl;
import eu.scy.core.model.pedagogicalplan.Tool;
import eu.scy.core.persistence.ToolDAO;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 20.jan.2010
 * Time: 11:08:43
 * To change this template use File | Settings | File Templates.
 */
public class ToolDAOHibernate extends ScyBaseDAOHibernate implements ToolDAO {
    @Override
    public void save(Tool tool) {
        getHibernateTemplate().save(tool);
    }

    @Override
    public Tool findToolByName(String name) {
        return (Tool) getSession().createQuery("from ToolImpl where name like :name")
                .setString("name", name)
                .setMaxResults(1)
                .uniqueResult();
    }

    @Override
    public List getTools() {
        logger.info("GETTING TOOLS!");
        return getSession().createQuery("from ToolImpl order by name")
                .list();
    }

    @Override
    public void addTool() {
        logger.info("CREATING TOOL!!!!");
        Tool tool = new ToolImpl();
        save(tool);
    }

    @Override
    public void registerTool(String toolId) {
        Tool tool = new ToolImpl();
        tool.setName("Unregistered tool (" + toolId + ")");
        tool.setToolId(toolId);
        save(tool);
    }

    @Override
    public Tool getToolByToolId(String toolId) {
        return (Tool) getSession().createQuery("from ToolImpl where toolId like :toolId")
                .setString("toolId", toolId)
                .setMaxResults(1)
                .uniqueResult();
    }

     @Override
    public Integer getUsageOfTool(Tool tool) {
         logger.info("Getting usages of tool: " + tool.getName() + " " + tool.getToolId());
        Long count= (Long) getSession().createQuery("select count(tora) from ToolRuntimeActionImpl tora where tora.tool = :toolId")
                .setString("toolId", tool.getToolId())
                .uniqueResult();
         logger.info("COUNT WAS: " + count.intValue());
        return count.intValue();
    }

}
