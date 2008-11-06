package eu.scy.core.persistence;

import eu.scy.core.model.*;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 06:44:04
 * To change this template use File | Settings | File Templates.
 */
public interface UserDAO extends SCYBaseDAO{

    /**
     * Fetch by primary key
     * @param id
     * @return
     */
    public User getUser(String id);


    /**
     * Fetch by user name
     * @param username
     * @return
     */
    public User getUserByUsername(String username);

    /**
     * Adds a user to the specified group in the specified project
     * @param project
     * @param group
     * @param user
     * @return
     */
    public User addUser(Project project, Group group, User user);

    public String getSecureUserName(String userName);

    public boolean isExistingUsername(User user);

    public List getUsers();

    public List getBuddies(User user);

    public Boolean loginUser(String username, String password);

    public Project getDefaultProject();

    public Group getDefaultGroup();

    public List<UserSession> getStartedSessions(long startTime, long endTime, Project project);

    public Long getNumberOfGroups(Project project);

    public Long getNumberOfUsers(Project project);

    public Long getNumberOfUsers(Group group);

    public Boolean getIsUserInRole(String role, User user);

    /**
     * Adds the role to the user. If the role does not exist, it is created
     * @param user
     * @param role
     */
    void addRole(User user, Role role);

    /**
     * Adds the role to the user. If no role with the rolename exists, a new role will be created
     * @param user
     * @param roleName
     */
    void addRole(User user, String roleName);
}
