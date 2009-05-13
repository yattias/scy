package eu.scy.core.persistence.hibernate;

import eu.scy.core.Constants;
import eu.scy.core.startup.UserCounterListener;
import eu.scy.core.model.Group;
import eu.scy.core.model.Project;
import eu.scy.core.model.User;
import eu.scy.core.model.UserSession;
import eu.scy.core.persistence.UserDAO;
import org.apache.log4j.Logger;
import org.jfree.data.DefaultKeyedValues;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;
import org.jfree.data.xy.XYDataset;
import org.springframework.security.concurrent.SessionRegistryImpl;
import org.springframework.security.context.SecurityContext;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.jun.2008
 * Time: 15:39:06
 * A hibernate implementation of access to the user database
 */
public class UserDAOHibernate extends BaseDAOHibernate implements UserDAO {

    private static Logger log = Logger.getLogger(UserDAOHibernate.class);
    private SessionRegistryImpl sessionRegistry;

    public User getUserByUsername(String username) {
        return (User) getSession().createQuery("from User where userName like :username")
                .setString("username", username)
                .uniqueResult();
    }


    public Boolean getIsUserOnline(String userName) {
        Set users = UserCounterListener.getUsers();
        if (users != null) {
            Iterator it = users.iterator();
            while (it.hasNext()) {
                org.springframework.security.userdetails.User o = (org.springframework.security.userdetails.User) it.next();
                User online = getUserByUsername(o.getUsername());
                if (online != null) return true;

            }
        }

        return false;
    }

    public User addUser(Project project, Group group, User user) {
        List users = getOnlineUsers();
        for (int i = 0; i < users.size(); i++) {
            User user1 = (User) users.get(i);
            log.info("--------------------------------- " + user1.getUserName() + " " + user1.getLastName());
        }

        if (isExistingUsername(user)) {
            user.setUserName(getSecureUserName(user.getUserName()));
        }

        if (project == null) project = getDefaultProject();
        if (group == null) group = getDefaultGroup();

        user.setProject(project);
        user.setGroup(group);
        return (User) save(user);
    }

    private String getSecureUserName(String userName) {
        int counter = 1;
        Boolean found = false;
        String suggestion = userName;
        while (!found) {
            suggestion = userName + counter;
            User result = (User) getSession().createQuery("from User where userName = :suggestion")
                    .setString("suggestion", suggestion)
                    .setMaxResults(1)
                    .uniqueResult();
            if (result == null) found = true;
            counter++;

        }

        return suggestion;
    }

    private boolean isExistingUsername(User user) {
        User result = (User) getSession().createQuery("From User where userName = :username")
                .setString("username", user.getUserName())
                .setMaxResults(1)
                .uniqueResult();
        return result != null;


    }

    public List getUsers() {
        return getSession().createQuery("from User order by userName ")
                .list();
    }

    public List<User> getOnlineUsers() {
        Set users = UserCounterListener.getUsers();

        if (users != null) {
            Iterator it = users.iterator();
            List onlineUsers = new LinkedList<User>();
            while (it.hasNext()) {
                org.springframework.security.userdetails.User o = (org.springframework.security.userdetails.User) it.next();
                User user = getUserByUsername(o.getUsername());
                if (user != null) onlineUsers.add(user);
            }

            return onlineUsers;
        }
        return Collections.EMPTY_LIST;
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

    private Project getDefaultProject() {
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

    public List<UserSession> getStartedSessions(long startTime, long endTime, Project project) {
        return (List<UserSession>) getSession().createQuery("from UserSession where sessionStarted >= :startTime and sessionStarted <= :endTime and user.project = :project")
                .setEntity("project", project)
                .setLong("startTime", startTime)
                .setLong("endTime", endTime)
                .list();
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

    public XYDataset getStartedSessionsDataset(Project project) {
        long startTime = System.currentTimeMillis() - Constants.ONE_MONTH_MILLISECONDS;
        long endTime = startTime + Constants.ONE_MONTH_MILLISECONDS;

        List<UserSession> sessions = getStartedSessions(startTime, endTime, project);
        TimeSeries s1 = new TimeSeries("User sessions", Day.class);
        TimeSeries s2 = new TimeSeries("Log in failures", Day.class);

        if (sessions.size() == 0) {
            for (int count = 1; count < 26; count++) {
                log.warn("hack hack hack - dummy data dummy data dummy data");
                s1.add(new TimeSeriesDataItem(new Day(new Date(startTime + (Constants.ONE_DAY_MILLISECONDS * count))), Math.random() * 1000));
                s2.add(new TimeSeriesDataItem(new Day(new Date(startTime + (Constants.ONE_DAY_MILLISECONDS * count))), Math.random() * 100));
            }
        } else {
            s1.add(new TimeSeriesDataItem(new Day(new Date(startTime)), 0));
            s1.add(new TimeSeriesDataItem(new Day(new Date(endTime)), 0));

            for (UserSession session : sessions) {
                Day day = new Day(new Date(session.getSessionStarted()));
                TimeSeriesDataItem item = s1.getDataItem(day);
                if (item != null) {
                    s1.addOrUpdate(day, item.getValue().intValue() + 1);
                } else {
                    s1.add(day, 1);
                }
            }
        }

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(s1);
        dataset.addSeries(s2);
        return dataset;
    }


    public SessionRegistryImpl getSessionRegistry() {
        return sessionRegistry;
    }

    public void setSessionRegistry(SessionRegistryImpl sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }
}