package eu.scy.modules.useradmin.pages;

import eu.scy.core.persistence.hibernate.UserDAOHibernate;
import eu.scy.core.persistence.hibernate.RoleDAOHibernate;
import eu.scy.core.persistence.UserDAO;
import eu.scy.core.model.*;
import eu.scy.core.Constants;

import java.util.Date;
import java.util.List;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.util.TextStreamResponse;
import org.apache.log4j.Logger;
import org.apache.tapestry.commons.components.InPlaceCheckbox;
import org.jfree.data.general.PieDataset;
import org.jfree.data.xy.XYDataset;

/**
 * Start page of application scyuseradmin.
 */
public class Start extends SCYBasePage {
    private static Logger log = Logger.getLogger(SCYBasePage.class);

    private UserSession userSession;
    private UserRole userRole;
    private Role role;

    @ApplicationState(create = false)
    private Group currentGroup;

    @Inject
    private UserDAOHibernate userDAO;

    @Inject
    private RoleDAOHibernate roleDAO;

    private User user;

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(UserDAOHibernate userDAO) {
        this.userDAO = userDAO;
    }

    public RoleDAOHibernate getRoleDAO() {
        return roleDAO;
    }

    public void setRoleDAO(RoleDAOHibernate roleDAO) {
        this.roleDAO = roleDAO;
    }

    public Group getCurrentGroup() {
        return currentGroup;
    }

    public void setCurrentGroup(Group currentGroup) {
        this.currentGroup = currentGroup;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCurrentTime() {
        return new Date();
    }

    public Group getRootGroup() {
        return userDAO.getRootGroup();
    }

    public Long getNumberOfGroups() {
        return userDAO.getNumberOfGroups(getCurrentProject());
    }

    public Long getNumberOfUsers() {
        return userDAO.getNumberOfUsers(getCurrentProject());
    }

    public List  getOnlineUsers() {
        return userDAO.getOnlineUsers();
    }

    public Long getNumberOfUsers(Group group) {
        return userDAO.getNumberOfUsers(group);
    }

    public PieDataset getGroupUserCountPieDataset(Project project) {
        return userDAO.getGroupUserCountPieDataset(project);
    }

    public XYDataset getStartedSessionsDataset(Project project) {
        return userDAO.getStartedSessionsDataset(project);
    }

    public String getUsername() {
        if(getUser() != null) {
            return getUser().getUserName();
        }
        return "NO user sete";
    }

    Object onSuccess() {
        userDAO.addUser(getCurrentProject(), getCurrentGroup(), getUser());
        return UserOverview.class;
    }

    public Object onActivate(String username) {
        System.out.println("****************************** ******************************** ACTIVATING WITH USERNAME:" + username);
        if(getUserDAO().getUserByUsername(username) == null) System.out.println("USER IS NULL!!!");
        setUser(getUserDAO().getUserByUsername(username));
        return null;
    }

    public List getUserSessions() {
        log.info("Getting user sessions!");
        return getUser().getUserSessions();
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public String getUserSessionStarted() {
        Date d = new Date(getUserSession().getSessionStarted());
        return String.valueOf(d);
   }

    @OnEvent(component = "inplacecheckbox", value = InPlaceCheckbox.EVENT_NAME)
    public void inPlaceCheckbox(boolean checked) {
        log.info("CHECKING ME!! " + checked);
        //log.info("ROLE IS: " + getRole().getName());
        //return new TextStreamResponse("text/html", checked ? "checked" : "un-checked");
    }

    public void onAction(String roleId) {
        log.info("ROLE ID IS: " + roleId);
    }

    public Boolean getChecked() {
        return true;
    }

    public List getUserRoles() {
        return getUser().getUserRoles();    
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public List getRoles() {
        return getRoleDAO().getRoles();
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}