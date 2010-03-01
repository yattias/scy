package eu.scy.core.model;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 06:01:23
 * The role defines which access authorisations a User has within the system
 */
public interface Role extends ScyBase{

    public List<UserRole> getUserRoles();

    public void setUserRoles(List<UserRole> userRoles);
}
