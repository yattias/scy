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
    public User getUser(Long id);


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
     */
    public User addUser(SCYProject project, SCYGroup group, User user);

    public String getSecureUserName(String userName);

    public boolean isExistingUsername(User user);

    public List getUsers();

    public List getBuddies(User user);

    public Boolean loginUser(String username, String password);

    public SCYProject getDefaultProject();

    public SCYGroup getDefaultGroup();

    public List<UserSession> getStartedSessions(long startTime, long endTime, SCYProject project);

    public Long getNumberOfGroups(SCYProject project);

    public Long getNumberOfUsers(SCYProject project);

    public Long getNumberOfUsers(SCYGroup group);

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

    /**
     * Removes the group permanently
     * @param groupId
     */
    public void deleteGroup(String groupId);

    /**
     * retrieves the group with the given ID
     * @param id
     * @return
     */
    public SCYGroup getGroup(String id);


    /**
     * removes the user from the system permanently
     * @param id
     */
    public void deleteUser(Long id);

    public User createUser(String username, String password, String role);

    public List<User> getStudents();

    /**
     * returns all granted authorities
     * @return
     */
    public List<SCYGrantedAuthority> getGrantedAuthorities();

}
