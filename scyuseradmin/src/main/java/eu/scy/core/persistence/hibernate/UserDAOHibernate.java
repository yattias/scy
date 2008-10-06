package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.Group;
import eu.scy.core.model.Project;
import eu.scy.core.model.User;
import eu.scy.core.persistence.UserDAO;
import org.apache.log4j.Logger;
import org.jfree.data.DefaultKeyedValues;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.jun.2008
 * Time: 15:39:06
 * A hibernate implementation of access to the user database
 */
public class UserDAOHibernate extends BaseDAOHibernate implements UserDAO {

    private static Logger log = Logger.getLogger(UserDAOHibernate.class);

    public User getUserByUsername(String username) {
        return (User) getSession().createQuery("from User where userName like :username")
                .setString("username", username)
                .uniqueResult();
    }

    public User addUser(Project project, Group group, User user) {

        if (project == null) project = getDefaultProject();
        if (group == null) group = getDefaultGroup();

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
        if (project == null) {
            throw new RuntimeException("Project not set - cannot create group");
        }
        Group g = new Group();
        g.setProject(project);
        g.setName(name);
        g.setParentGroup(parent);
        return (Group) save(g);
    }

    public Group getGroup(String id) {
        return (Group) getSession().createQuery("from Group where id = :id")
                .setString("id", id)
                .uniqueResult();
    }

    public Group getRootGroup() {
        return (Group) getSession().createQuery("From Group where parentGroup is null")
                .setMaxResults(1)
                .uniqueResult();
    }

    public List getBuddies(User user) {

        User testOne = new User();
        testOne.setName("Henrik");
        testOne.setLastName("Schlanbusch");
        List returnList = new LinkedList();
        returnList.add(testOne);
        return returnList;
/*
        return getSession().createQuery("select connection.buddy from BuddyConnection connection where myself = :mySelf")
                .setEntity("mySelf", user)
                .list();
                */
    }

    public Boolean loginUser(String username, String password) {
        User user = (User) getSession().createQuery("from User where userName = :username and password = :password")
                .setString("username", username)
                .setString("password", password)
                .setMaxResults(1)
                .uniqueResult();
        return user != null;
    }

    public Project getDefaultProject() {
        log.info("Getting default project!! REALLY HACKY METHOD, but works for now. Need to know more about the future structure to create a good default....");
        return (Project) getSession().createQuery("from Project")
                .setMaxResults(1)
                .uniqueResult();
    }

    private Group getDefaultGroup() {
        return (Group) getSession().createQuery("from Group")
                .setMaxResults(1)
                .uniqueResult();
    }


    public Long getNumberOfGroups(Project project) {
        return (Long) getSession().createQuery("select count(g) from Group g where g.project= :project")
                .setEntity("project", project)
                .uniqueResult();
    }

    public Long getNumberOfUsers(Project project) {
        return (Long) getSession().createQuery("select count(user) from User user where user.project= :project")
                .setEntity("project", project)
                .uniqueResult();
    }

    public Long getNumberOfUsers(Group group) {
        return (Long) getSession().createQuery("select count(user) from User user where user.group= :group")
                .setEntity("group", group)
                .uniqueResult();
    }

    public PieDataset getGroupUserCountPieDataset(Project project) {
        List<Group> groups = project.getGroups();

        DefaultKeyedValues values = new DefaultKeyedValues();
        for (Group group : groups) {
            values.addValue(group.getName(), getNumberOfUsers(group));
        }

        return new DefaultPieDataset(values);
    }
}
