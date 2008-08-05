package eu.scy.modules.useradmin.pages;

import org.apache.tapestry.ioc.annotations.Inject;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UserDetails;

import java.util.List;

import eu.scy.core.persistence.hibernate.UserDAOHibernate;
import eu.scy.core.model.User;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 22.jul.2008
 * Time: 14:41:57
 * Just a quick hack to list out users from the root group
 */
public class UserOverview extends SCYBasePage{

    public String getTull() {
        return "tullballsdf" +getCurrentUsersUserName();
    }

    @Inject
    private UserDAOHibernate userDAO;
    private User user;


    public UserDAOHibernate getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(UserDAOHibernate userDAO) {
        this.userDAO = userDAO;
    }

    public List getUsers() {
        return getUserDAO().getUsers();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
