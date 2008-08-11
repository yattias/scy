package eu.scy.modules.useradmin.pages;

import org.apache.tapestry.ioc.annotations.Inject;
import org.apache.tapestry.annotations.ApplicationState;
import eu.scy.core.persistence.hibernate.UserDAOHibernate;
import eu.scy.core.model.Group;
import eu.scy.core.model.User;

import java.util.Date;

/**
 * Start page of application scyuseradmin.
 */
public class Start extends SCYBasePage {

    @ApplicationState (create = false)
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
}