package eu.scy.core.model.impl;

import eu.scy.core.model.UserRole;
import eu.scy.core.model.User;
import eu.scy.core.model.Role;

import javax.persistence.ManyToOne;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 06:12:30
 * To change this template use File | Settings | File Templates.
 */
public class UserRoleImpl extends ScyBaseObject implements UserRole {

     private User user;
    private Role role;


    @ManyToOne(targetEntity = User.class, cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_primKey")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    @ManyToOne(targetEntity = Role.class, cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn (name = "role_primKey")
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}
