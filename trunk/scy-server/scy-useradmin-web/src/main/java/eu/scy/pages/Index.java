package eu.scy.pages;

import eu.scy.pages.TapestryContextAware;
import eu.scy.core.persistence.hibernate.UserDAOHibernate;

import java.util.Date;
import java.util.List;

import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * Start page of application scy-useradmin-web.
 */
public class Index  extends TapestryContextAware {

     @Inject
    private UserDAOHibernate userDAO;


    public UserDAOHibernate getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(UserDAOHibernate userDAO) {
        this.userDAO = userDAO;
    }

    public List getUsers() {
        return getUserDAO().getUsers();
    }
}
