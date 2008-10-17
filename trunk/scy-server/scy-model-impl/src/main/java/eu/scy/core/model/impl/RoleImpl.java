package eu.scy.core.model.impl;

import eu.scy.core.model.Role;
import eu.scy.core.model.UserRole;

import javax.persistence.OneToMany;
import javax.persistence.FetchType;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 06:01:35
 * To change this template use File | Settings | File Templates.
 */
public class RoleImpl extends ScyBaseObject implements Role {
    private List<UserRole> userRoles;

    @OneToMany(targetEntity = UserRole.class, mappedBy = "role", fetch = FetchType.LAZY)
    public List<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    

}
