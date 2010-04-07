package eu.scy.core.model;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 06:11:34
 * Connects users and roles
 */
public interface UserRole extends ScyBase{

    public User getUser();

    public void setUser(User user);

    public Role getRole();

    public void setRole(Role role);
}
