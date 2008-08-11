package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.Group;
import eu.scy.core.model.User;
import eu.scy.core.model.Project;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.jun.2008
 * Time: 15:39:06
 * A hibernate implementation of access to the user database
 */
public class UserDAOHibernate extends BaseDAOHibernate {


    public User getUserByUsername(String username) {
        return (User) getSession().createQuery("from User where userName like :username")
                .setString("username", username)
                .uniqueResult();
    }

    public User addUser(Project project, Group group, User user) {
        user.setProject(project);
        user.setGroup(group);
        save(user);

        return user;
    }

    public List getUsers() {
        return getSession().createQuery("from User order by userName ")
                .list();
    }

    public Group createGroup(Project project, String name, Group parent) {
        if(project== null) {
            throw new RuntimeException("Project not set - cannot create group");
        }
        Group g = new Group();
        g.setProject(project);
        g.setName(name);
        g.setParentGroup(parent);
        save(g);
        return g;
    }

    public Group getGroup(String id) {
        Group g = (Group) getSession().createQuery("from Group where id = :id")
                .setString("id", id)
                .uniqueResult();
        return g;
    }

    public Group getRootGroup() {
        return (Group) getSession().createQuery("From Group where parentGroup is null")
                .setMaxResults(1)
                .uniqueResult();
    }
}
