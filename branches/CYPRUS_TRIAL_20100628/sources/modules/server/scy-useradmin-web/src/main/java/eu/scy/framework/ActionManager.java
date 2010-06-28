package eu.scy.framework;

import eu.scy.framework.BaseAction;
import eu.scy.core.persistence.hibernate.ProjectDAOHibernate;
import eu.scy.core.persistence.hibernate.UserDAOHibernate;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 23.nov.2008
 * Time: 21:22:58
 * To change this template use File | Settings | File Templates.
 */
public interface ActionManager {
    public List<BaseAction> getActions(Object userObject);

    public BaseAction getActionById(String id);

    ProjectDAOHibernate getProjectDAOHibernate();

    UserDAOHibernate getUserDAOHibernate();

    void setProjectDAOHibernate(ProjectDAOHibernate projectDAOHibernate);

    void setUserDAOHibernate(UserDAOHibernate userDAOHibernate);
}
