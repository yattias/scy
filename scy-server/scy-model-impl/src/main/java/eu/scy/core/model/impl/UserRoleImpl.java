package eu.scy.core.model.impl;

import eu.scy.core.model.UserRole;
import eu.scy.core.model.User;
import eu.scy.core.model.Role;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 06:12:30
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "userrole")
public class UserRoleImpl extends ScyBaseObject implements UserRole {

    private User user;
    private Role role;


    @ManyToOne(targetEntity = SCYUserImpl.class, cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_primKey")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    @ManyToOne(targetEntity = RoleImpl.class, cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "role_primKey")
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}
