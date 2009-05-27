package eu.scy.core.model;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 27.mai.2009
 * Time: 20:07:03
 * Connects users and groups.
 */
public interface UserGroupConnection extends ScyBase{
    
    public void setUser(User user);
    public User getUser();

    public void setGroup(SCYGroup group);
    public SCYGroup getGroup();

}
