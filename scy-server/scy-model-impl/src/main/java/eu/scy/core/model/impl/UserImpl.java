package eu.scy.core.model.impl;

import eu.scy.core.model.*;

import javax.persistence.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 05:49:47
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table (name = "user" )
@org.hibernate.annotations.Proxy (proxyClass = User.class )
public class UserImpl extends ScyBaseObject implements User {

    private String userName;
    private String password;
    private String enabled;

    private String firstName;
    private String lastName;

    private Group group;

    private List<UserRole> userRoles;
    private List<UserSession> userSessions;

    private Project project;


    @Column(name = "userName", nullable = false, unique = true)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @ManyToOne(targetEntity = ProjectImpl.class, cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "project_primKey")
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @ManyToOne(targetEntity = GroupImpl.class, cascade = {CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "group_primKey")
    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @OneToMany(targetEntity = UserRoleImpl.class, mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public List<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    public void addRole(String rolename) {
        UserRole role = new UserRoleImpl();
        role.setName(rolename);
        role.setUser(this);

        getUserRoles().add(role);

    }

    @OneToMany(targetEntity = UserSessionImpl.class, mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public List<UserSession> getUserSessions() {
        return userSessions;
    }

    public void setUserSessions(List<UserSession> userSessions) {
        this.userSessions = userSessions;
    }

    public void addUserSession(UserSession userSession) {
        getUserSessions().add(userSession);
        userSession.setUser(this);
    }

}
