package eu.scy.modules.useradmin.pages;

import org.apache.tapestry.ioc.annotations.Inject;
import eu.scy.core.persistence.hibernate.UserDAOHibernate;
import eu.scy.core.model.Group;
import eu.scy.core.model.User;

import java.util.Date;

/**
 * Start page of application scyuseradmin.
 */
public class Start {

    @Inject
    private UserDAOHibernate userDAO;

    private User user;

    public UserDAOHibernate getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(UserDAOHibernate userDAO) {
        this.userDAO = userDAO;
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


    Object onSuccess() {

        userDAO.addUser(getUser());

        return UserOverview.class;
    }
}