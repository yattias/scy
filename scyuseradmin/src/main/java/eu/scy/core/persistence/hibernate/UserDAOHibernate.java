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

    public User getUserByUsername(String username) {
        return (User) getSession().createQuery("from User where userName like :username")
                .setString("username", username)
                .uniqueResult();
    }

    public User addUser(User user) {
        if (getUserByUsername(user.getUserName()) != null) {
            getHibernateTemplate().saveOrUpdate(user);
        } else {
            getHibernateTemplate().save(user);
        }

        return user;
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
