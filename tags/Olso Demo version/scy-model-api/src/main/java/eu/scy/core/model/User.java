package eu.scy.core.model;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 05:49:08
 * A user in the SCY system.
 */
public interface User extends ScyBase{

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

    public Project getProject();

    public void setProject(Project project);

    public Group getGroup();

    public void setGroup(Group group);

    public List<UserRole> getUserRoles();

    public void setUserRoles(List<UserRole> userRoles);

    public void addRole(String rolename);

    public List<UserSession> getUserSessions();

    public void setUserSessions(List<UserSession> userSessions);

    public void addUserSession(UserSession userSession);
}