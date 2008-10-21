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

    String getUserName();

    void setUserName(String userName);

    String getPassword();

    void setPassword(String password);

    String getEnabled();

    void setEnabled(String enabled);

    String getFirstName();

    void setFirstName(String firstName);

    String getLastName();

    void setLastName(String lastName);

    Project getProject();

    void setProject(Project project);

    Group getGroup();

    void setGroup(Group group);

    List<UserRole> getUserRoles();

    void setUserRoles(List<UserRole> userRoles);

    void addRole(String rolename);

    List<UserSession> getUserSessions();

    void setUserSessions(List<UserSession> userSessions);
}
