package eu.scy.core.model;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 06:11:09
 * The project is the workspace for users. Information in a project will only be available for that project's users 
 */
public interface Project extends ScyBase{


    List<Group> getGroups();

    void setGroups(List<Group> groups);

    List<User> getUsers();

    void setUsers(List<User> users);

    void addGroup(Group group);
}
