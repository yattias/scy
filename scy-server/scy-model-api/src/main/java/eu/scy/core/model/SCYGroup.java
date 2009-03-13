package eu.scy.core.model;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 06:00:03
 * A group in the SCY system. Groups can be arranged hierarchically within projects and can contain users
 */
public interface SCYGroup extends ScyBase{

    //public List<User> getUsers();

    //public void setUsers(List<User> users);

    public List<SCYGroup> getChildren();

    public void setChildren(List<SCYGroup> children);

    public void addChild(SCYGroup group);

    public SCYGroup getParentGroup();

    public void setParentGroup(SCYGroup parentGroup);

    public SCYProject getProject();

    public void setProject(SCYProject project);

    public void addUser(User user);
}
