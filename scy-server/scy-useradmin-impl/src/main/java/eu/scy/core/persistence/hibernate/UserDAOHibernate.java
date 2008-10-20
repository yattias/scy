package eu.scy.core.persistence.hibernate;

import eu.scy.core.persistence.UserDAO;
import eu.scy.core.model.User;
import eu.scy.core.model.Project;
import eu.scy.core.model.Group;
import eu.scy.core.model.UserSession;
import eu.scy.core.model.impl.GroupImpl;
import org.springframework.security.concurrent.SessionRegistryImpl;

import java.util.*;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 06:44:19
 * To change this template use File | Settings | File Templates.
 */
public class UserDAOHibernate extends ScyBaseDAOHibernate implements UserDAO {


    private static Logger log = Logger.getLogger("UserDAOHibernate.class");
    private SessionRegistryImpl sessionRegistry;

    public User getUserByUsername(String username) {
        return (User) getSession().createQuery("from UserImpl where userName like :username")
                .setString("username", username)
                .uniqueResult();
    }


    /*public Boolean getIsUserOnline(String userName) {
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
      */
    public User addUser(Project project, Group group, User user) {
        /*List users = getOnlineUsers();
        for (int i = 0; i < users.size(); i++) {
            User user1 = (User) users.get(i);
            log.info("--------------------------------- " + user1.getUserName() + " " + user1.getLastName());
        }
         */
        if (isExistingUsername(user)) {
            user.setUserName(getSecureUserName(user.getUserName()));
        }

        if (project == null) project = getDefaultProject();
        if (group == null) group = getDefaultGroup();

        user.setProject(project);
        user.setGroup(group);
        return (User) save(user);
    }

    public String getSecureUserName(String userName) {
        int counter = 1;
        Boolean found = false;
        String suggestion = userName;
        while (!found) {
            suggestion = userName + counter;
            User result = (User) getSession().createQuery("from UserImpl where userName = :suggestion")
                    .setString("suggestion", suggestion)
                    .setMaxResults(1)
                    .uniqueResult();
            if (result == null) found = true;
            counter++;

        }

        return suggestion;
    }

    public boolean isExistingUsername(User user) {
        User result = (User) getSession().createQuery("From UserImpl where userName = :username")
                .setString("username", user.getUserName())
                .setMaxResults(1)
                .uniqueResult();
        return result != null;


    }

    public List getUsers() {
        return getSession().createQuery("from UserImpl order by userName ")
                .list();
    }

    /*public List<User> getOnlineUsers() {
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
     */

    public List getBuddies(User user) {
        return getSession().createQuery("select connection.buddy from BuddyConnectionImpl connection where myself = :mySelf")
                .setEntity("mySelf", user)
                .list();
    }

    public Boolean loginUser(String username, String password) {
        User user = (User) getSession().createQuery("from UserImpl where userName = :username and password = :password")
                .setString("username", username)
                .setString("password", password)
                .setMaxResults(1)
                .uniqueResult();

        return user != null;
    }

    public Project getDefaultProject() {
        log.info("Getting default project!! REALLY HACKY METHOD, but works for now. Need to know more about the future structure to create a good default....");
        return (Project) getSession().createQuery("from ProjectImpl")
                .setMaxResults(1)
                .uniqueResult();
    }

    public Group getDefaultGroup() {
        return (Group) getSession().createQuery("from GroupImpl")
                .setMaxResults(1)
                .uniqueResult();
    }

    public List<UserSession> getStartedSessions(long startTime, long endTime, Project project) {
        return (List<UserSession>) getSession().createQuery("from UserSessionImpl where sessionStarted >= :startTime and sessionStarted <= :endTime and user.project = :project")
                .setEntity("project", project)
                .setLong("startTime", startTime)
                .setLong("endTime", endTime)
                .list();
    }

    public Long getNumberOfGroups(Project project) {
        return (Long) getSession().createQuery("select count(g) from GroupImpl g where g.project= :project")
                .setEntity("project", project)
                .uniqueResult();
    }

    public Long getNumberOfUsers(Project project) {
        return (Long) getSession().createQuery("select count(user) from UserImpl user where user.project= :project")
                .setEntity("project", project)
                .uniqueResult();
    }

    public Long getNumberOfUsers(Group group) {
        return (Long) getSession().createQuery("select count(user) from UserImpl user where user.group= :group")
                .setEntity("group", group)
                .uniqueResult();
    }


   /* public SessionRegistryImpl getSessionRegistry() {
        return sessionRegistry;
    }

    public void setSessionRegistry(SessionRegistryImpl sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }
    */

}
