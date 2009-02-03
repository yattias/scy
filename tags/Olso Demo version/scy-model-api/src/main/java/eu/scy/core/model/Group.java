package eu.scy.core.model;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 06:00:03
 * A group in the SCY system. Groups can be arranged hierarchically within projects and can contain users
 */
public interface Group extends ScyBase{

    public List<User> getUsers();

    public void setUsers(List<User> users);

    public List<Group> getChildren();

    public void setChildren(List<Group> children);

    public void addChild(Group group);

    public Group getParentGroup();

    public void setParentGroup(Group parentGroup);

    public Project getProject();

    public void setProject(Project project);

    public void addUser(User user);
}
