package eu.scy.core.model;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 06:00:03
 * To change this template use File | Settings | File Templates.
 */
public interface Group extends ScyBase{
    List<User> getUsers();

    void setUsers(List<User> users);

    List<Group> getChildren();

    void setChildren(List<Group> children);

    void addChild(Group group);

    Group getParentGroup();

    void setParentGroup(Group parentGroup);

    Project getProject();

    void setProject(Project project);

    void addUser(User user);
}
