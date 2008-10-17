package eu.scy.core.model;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 06:11:34
 * Connects users and roles
 */
public interface UserRole extends ScyBase{
    User getUser();

    void setUser(User user);

    Role getRole();

    void setRole(Role role);
}
