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
    User getUserByUsername(String username);

    User addUser(Project project, Group group, User user);

    String getSecureUserName(String userName);

    boolean isExistingUsername(User user);

    List getUsers();

    List getBuddies(User user);

    Boolean loginUser(String username, String password);

    Project getDefaultProject();

    Group getDefaultGroup();

    List<UserSession> getStartedSessions(long startTime, long endTime, Project project);

    Long getNumberOfGroups(Project project);

    Long getNumberOfUsers(Project project);

    Long getNumberOfUsers(Group group);

    /*SessionRegistryImpl getSessionRegistry();

    void setSessionRegistry(SessionRegistryImpl sessionRegistry);
    */
}
