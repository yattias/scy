package eu.scy.core.model;


import org.apache.tapestry5.beaneditor.Validate;

import javax.persistence.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.jun.2008
 * Time: 22:55:58
 * A user that can log in to the system
 */

@Entity
@Table(name = "user")
public class User extends SCYBaseObject {

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

    @Validate("required")
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

    @ManyToOne(targetEntity = Project.class, cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "project_primKey")
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @ManyToOne(targetEntity = Group.class, cascade = {CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "group_primKey")
    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @OneToMany(targetEntity = UserRole.class, mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public List<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    public void addRole(String rolename) {
        UserRole role = new UserRole();
        role.setName(rolename);
        role.setUser(this);

        getUserRoles().add(role);

    }

    @OneToMany(targetEntity = UserSession.class, mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public List<UserSession> getUserSessions() {
        return userSessions;
    }

    public void setUserSessions(List<UserSession> userSessions) {
        this.userSessions = userSessions;
    }
}
