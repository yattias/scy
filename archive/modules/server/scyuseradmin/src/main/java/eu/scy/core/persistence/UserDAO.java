package eu.scy.core.persistence;

import eu.scy.core.model.User;
import eu.scy.core.model.Group;
import eu.scy.core.model.Project;

import java.util.List;

import org.jfree.data.general.PieDataset;
import org.jfree.data.xy.XYDataset;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.sep.2008
 * Time: 06:32:26
 * To change this template use File | Settings | File Templates.
 */
public interface UserDAO extends BaseDAO{


    public User getUserByUsername(String username) ;

    public User addUser(Project project, Group group, User user) ;

    public List getUsers() ;

    public Group createGroup(Project project, String name, Group parent) ;

    public Group getGroup(String id) ;

    public Group getRootGroup() ;

    public List getBuddies(User user);

    public Boolean loginUser(String username, String password);

    public Long getNumberOfGroups(Project project);

    public Long getNumberOfUsers(Project project);

    public Long getNumberOfUsers(Group group);

    public PieDataset getGroupUserCountPieDataset(Project project);

    public XYDataset getStartedSessionsDataset(Project project);

    List <User> getOnlineUsers();
}
