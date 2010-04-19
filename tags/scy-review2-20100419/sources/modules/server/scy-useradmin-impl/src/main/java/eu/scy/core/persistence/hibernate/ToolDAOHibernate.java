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
        return getSession().createQuery("from ToolImpl order by name")
                .list();
    }

    @Override
    public void addTool() {
        Tool tool = new ToolImpl();
        save(tool);
    }
}
