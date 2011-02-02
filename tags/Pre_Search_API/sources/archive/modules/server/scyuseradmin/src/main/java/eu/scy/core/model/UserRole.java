package eu.scy.core.model;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 17.jun.2008
 * Time: 00:09:27
 * The many to many connection between users and roles
 */
@Entity
@Table(name="userrole")
public class UserRole extends SCYBaseObject{

    private User user;
    private Role role;


    @ManyToOne(targetEntity = User.class, cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn (name = "user_primKey")
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
