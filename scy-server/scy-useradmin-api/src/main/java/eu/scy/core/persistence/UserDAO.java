package eu.scy.core.persistence;

import eu.scy.core.model.User;
import eu.scy.core.model.Group;
import eu.scy.core.model.Project;
import eu.scy.core.model.UserSession;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 06:44:04
 * To change this template use File | Settings | File Templates.
 */
public interface UserDAO extends SCYBaseDAO{

    public User getUserByUsername(String username);

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

    /*SessionRegistryImpl getSessionRegistry();

    void setSessionRegistry(SessionRegistryImpl sessionRegistry);
    */

    Boolean getUserInRole(String role, User user);
}
