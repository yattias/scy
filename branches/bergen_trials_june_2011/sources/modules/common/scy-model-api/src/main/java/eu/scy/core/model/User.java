package eu.scy.core.model;

import java.util.List;

//import net.sf.sail.webapp.domain.User;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 05:49:08
 * A user in the SCY system.
 */

public interface User   {
    /*
    public String getUserName();

    public void setUserName(String userName);

    public String getPassword();

    public void setPassword(String password);

    public String getEnabled();

    public void setEnabled(String enabled);

    public String getFirstName();

    public void setFirstName(String firstName);

    public String getLastName();

    public void setLastName(String lastName);

    public SCYProject getProject();

    public void setProject(SCYProject project);

    public SCYGroup getGroup();

    public void setGroup(SCYGroup group);

    public List<UserRole> getUserRoles();

    public void setUserRoles(List<UserRole> userRoles);

    public void addRole(String rolename);

    public List<UserSession> getUserSessions();

    public void setUserSessions(List<UserSession> userSessions);

    public void addUserSession(UserSession userSession);
    */
    long getId();

    UserDetails getUserDetails();
}
