package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.Group;
import eu.scy.core.model.User;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.jun.2008
 * Time: 15:39:06
 * A hibernate implementation of access to the user database
 */
public class UserDAOHibernate extends BaseDAOHibernate {

    public void addUser(String preferredUsername) {

    }

    public void addUser (User user) {
        getHibernateTemplate().save(user);
    }

    public List getUsers() {
        return getSession().createQuery("from User order by userName ")
                .list();
    }

    public Group createGroup(String name, Group parent) {
        Group g = new Group();
        g.setName(name);
        g.setParentGroup(parent);
        getHibernateTemplate().save(g);
        return g;
    }

    public Group getRootGroup() {
        return (Group) getSession().createQuery("From Group where parentGroup is null")
                .setMaxResults(1)
                .uniqueResult();
    }
}
