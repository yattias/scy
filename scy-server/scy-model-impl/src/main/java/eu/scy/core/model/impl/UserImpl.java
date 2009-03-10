package eu.scy.core.model.impl;

import eu.scy.core.model.*;

import javax.persistence.*;
import java.util.List;
import java.util.LinkedList;

import net.sf.sail.webapp.domain.authentication.MutableUserDetails;
import net.sf.sail.webapp.domain.authentication.impl.PersistentUserDetails;
import net.sf.sail.webapp.domain.sds.SdsUser;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 05:49:47
 * To change this template use File | Settings | File Templates.
 */

@Entity(name = "eu.scy.core.model.impl.UserImpl")
@Table(name = "user")
@org.hibernate.annotations.Proxy(proxyClass = User.class)
public class UserImpl extends net.sf.sail.webapp.domain.impl.UserImpl implements User {

    private String userName;
    private String password;
    private String enabled;

    private String firstName;
    private String lastName;

    private Group group;

    private List<UserRole> userRoles;
    private List<UserSession> userSessions;

    private Project project;

    @OneToOne(cascade = CascadeType.ALL, targetEntity = UserDetails.class)
    @JoinColumn(name = COLUMN_NAME_USER_DETAILS_FK, nullable = false, unique = true)
    private MutableUserDetails userDetails;



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

    @ManyToOne(targetEntity = GroupImpl.class, cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "group_primKey")
    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @OneToMany(targetEntity = UserRoleImpl.class, mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public List<UserRole> getUserRoles() {
        if (userRoles == null) {
            userRoles = new LinkedList<UserRole>();
        }
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

    public MutableUserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(MutableUserDetails mutableUserDetails) {
        this.userDetails = mutableUserDetails;
    }


    public void setSdsUser(SdsUser sdsUser) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Transient
    public SdsUser getSdsUser() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
