package eu.scy.core.model;

import javax.persistence.*;
import java.util.List;

/**
 * User: Henrik
 * Date: 17.jun.2008
 * Time: 00:06:52
 * Roles are used by the permission scheme of SCY to identify type of user and his/her authorities
 */
@Entity
@Table(name = "role")
public class Role extends SCYBaseObject{

    private List<UserRole> userRoles;

    @OneToMany(targetEntity = UserRole.class, mappedBy = "role", fetch = FetchType.LAZY)
    public List<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<UserRole> userRoles) {
        this.userRoles = userRoles;
    }
}
