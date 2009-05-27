package eu.scy.core.persistence.hibernate;

import eu.scy.core.persistence.UserDAO;
import eu.scy.core.model.impl.RoleImpl;
import eu.scy.core.model.impl.SCYUserImpl;
import eu.scy.core.model.impl.UserGroupConnectionImpl;
import eu.scy.core.model.*;


import java.util.*;
import java.util.logging.Logger;

import net.sf.sail.webapp.domain.authentication.MutableGrantedAuthority;
import net.sf.sail.webapp.dao.authentication.GrantedAuthorityDao;
import org.springframework.beans.factory.annotation.Required;


/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 06:44:19
 */
public class UserDAOHibernate extends ScyBaseDAOHibernate implements UserDAO {


    private static Logger log = Logger.getLogger("UserDAOHibernate.class");

    private GrantedAuthorityDao<MutableGrantedAuthority> grantedAuthorityDao;

    @Required
    public void setGrantedAuthorityDao(
            GrantedAuthorityDao<MutableGrantedAuthority> grantedAuthorityDao) {
        this.grantedAuthorityDao = grantedAuthorityDao;
    }
    

    public User getUser(Long id) {
        log.finest("Getting user with id: " + id);
        return (User) getSession().createQuery("from SCYUserImpl where id = :id")
                .setLong("id", id)
                .setMaxResults(1)
                .uniqueResult();

    }

    public void deleteUser(Long id) {
        getSession().createQuery("delete from SCYUserImpl where id = :id")
                .setLong("id", id)
                .executeUpdate();
    }


    public User getUserByUsername(String username) {
        User user =  (User) getSession().createQuery("from SCYUserImpl user where user.userDetails.username like :username")
                .setString("username", username)
                .uniqueResult();
        log.info("FOUND USER: " + user + " from username: "+ username);
        return user;
    }

    public User addUser(SCYProject project, SCYGroup group, User user) {
        if (isExistingUsername(user)) {
            user.getUserDetails().setUsername(getSecureUserName(user.getUserDetails().getUsername()));
        }

        if (project == null) project = getDefaultProject();
        if (group == null) group = getDefaultGroup();

        UserGroupConnection connection = new UserGroupConnectionImpl(user, group);
        save(connection);

        log.info("SAVING USER: " + user.getUserDetails().getUsername());
        return (User) save(user);
    }

    public String getSecureUserName(String userName) {
        int counter = 1;
        Boolean found = false;
        String suggestion = userName;
        while (!found) {
            suggestion = userName + counter;
            User result = (User) getSession().createQuery("from SCYUserImpl user where user.userDetails.username = :suggestion")
                    .setString("suggestion", suggestion)
                    .setMaxResults(1)
                    .uniqueResult();
            if (result == null) found = true;
            counter++;

        }

        return suggestion;
    }

    public boolean isExistingUsername(User user) {
        User result = (User) getSession().createQuery("from SCYUserImpl where userDetails.username = :username")
                .setString("username", user.getUserDetails().getUsername())
                .setMaxResults(1)
                .uniqueResult();
        return result != null;


    }

    
    public List getUsers() {
        return getSession().createQuery("from SCYUserImpl user order by user.userDetails.username")
                .list();
    }

    public List getBuddies(User user) {
        return getSession().createQuery("select connection.buddy from BuddyConnectionImpl connection where myself = :mySelf")
                .setEntity("mySelf", user)
                .list();
    }

    public Boolean loginUser(String username, String password) {
        User user = (User) getSession().createQuery("from SCYUserImpl where userName = :username and password = :password")
                .setString("username", username)
                .setString("password", password)
                .setMaxResults(1)
                .uniqueResult();

        return user != null;
    }

    public SCYProject getDefaultProject() {
        log.info("Getting default project!! REALLY HACKY METHOD, but works for now. Need to know more about the future structure to create a good default....");
        return (SCYProject) getSession().createQuery("from SCYProjectImpl")
                .setMaxResults(1)
                .uniqueResult();
    }

    public SCYGroup getDefaultGroup() {
        return (SCYGroup) getSession().createQuery("from SCYGroupImpl")
                .setMaxResults(1)
                .uniqueResult();
    }

    public List<UserSession> getStartedSessions(long startTime, long endTime, SCYProject project) {
        return (List<UserSession>) getSession().createQuery("from UserSessionImpl where sessionStarted >= :startTime and sessionStarted <= :endTime and user.project = :project")
                .setEntity("project", project)
                .setLong("startTime", startTime)
                .setLong("endTime", endTime)
                .list();
    }

    public Long getNumberOfGroups(SCYProject project) {
        return (Long) getSession().createQuery("select count(g) from SCYGroupImpl g where g.project= :project")
                .setEntity("project", project)
                .uniqueResult();
    }

    public Long getNumberOfUsers(SCYProject project) {
        return (Long) getSession().createQuery("select count(user) from SCYUserImpl user where user.project= :project")
                .setEntity("project", project)
                .uniqueResult();
    }

    public Long getNumberOfUsers(SCYGroup group) {
        return (Long) getSession().createQuery("select count(user) from SCYUserImpl user where user.group= :group")
                .setEntity("group", group)
                .uniqueResult();
    }

    public Boolean getIsUserInRole(String role, User user) {
        UserRole userRole = (UserRole) getSession().createQuery("from UserRoleImpl where user = :user and role.name = :role")
                .setEntity("user", user)
                .setString("role", role)
                .setMaxResults(1)
                .uniqueResult();
        return userRole != null;
    }

    public void addRole(User user, Role role) {

        Role loadedRole = (Role) getSession().createQuery("from RoleImpl where name like :name")
                .setString("name", role.getName())
                .setMaxResults(1)
                .uniqueResult();
        if(loadedRole == null) {
            loadedRole = (Role) save(role);
        }

        /*UserRole userRole = new UserRoleImpl();
        userRole.setUser(user);
        userRole.setRole(loadedRole);
        save(userRole);
        user.getUserRoles().add(userRole);
        */
        save(user);
    }

    public void addRole(User user, String roleName) {
        Role persistentRole = (Role) getSession().createQuery("from RoleImpl where name like :name")
                .setString("name", roleName)
                .setMaxResults(1)
                .uniqueResult();
        if(persistentRole == null) {
            persistentRole = new RoleImpl();
            persistentRole.setName(roleName);
        }
        addRole(user, persistentRole);
    }

    public void deleteGroup(String groupId) {
        getSession().createQuery("delete from SCYGroupImpl where id like :id")
                .setString("id", groupId)
                .executeUpdate();
    }

    public SCYGroup getGroup(String id) {
        return (SCYGroup) getSession().createQuery("from SCYGroupImpl where id  like :id")
                .setString("id", id)
                .setMaxResults(1)
                .uniqueResult();
    }

}
