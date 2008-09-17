package eu.scy.core.persistence;

import eu.scy.core.model.User;
import eu.scy.core.model.Group;
import eu.scy.core.model.Project;

import java.util.List;

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

}
