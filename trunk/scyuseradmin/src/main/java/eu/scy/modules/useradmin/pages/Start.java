package eu.scy.modules.useradmin.pages;

import eu.scy.core.persistence.hibernate.UserDAOHibernate;
import eu.scy.core.model.Group;
import eu.scy.core.model.User;
import eu.scy.core.model.UserSession;

import java.util.Date;
import java.util.List;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.log4j.Logger;

/**
 * Start page of application scyuseradmin.
 */
public class Start extends SCYBasePage {
    private static Logger log = Logger.getLogger(SCYBasePage.class);

    private UserSession userSession;

    @ApplicationState(create = false)
    private Group currentGroup;

    @Inject
    private UserDAOHibernate userDAO;

    private User user;

    public UserDAOHibernate getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(UserDAOHibernate userDAO) {
        this.userDAO = userDAO;
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

    public String getUsername() {
        if(getUser() != null) {
            return getUser().getUserName();
        }
        return "NO user sete";
    }

    Object onSuccess() {
        User u = userDAO.addUser(getCurrentProject(), getCurrentGroup(), getUser());
        return UserOverview.class;
    }

    public Object onActivate(String username) {
        System.out.println("ACTIVATING WITH USERNAME:" + username);
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
}