package eu.scy.core.model;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 06:01:23
 * To change this template use File | Settings | File Templates.
 */
public interface Role extends ScyBase{

    List<UserRole> getUserRoles();

    void setUserRoles(List<UserRole> userRoles);
}
