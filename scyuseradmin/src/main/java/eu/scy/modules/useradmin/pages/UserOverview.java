package eu.scy.modules.useradmin.pages;

import org.apache.tapestry.ioc.annotations.Inject;
import org.apache.tapestry.annotations.ApplicationState;
import org.apache.tapestry.annotations.Persist;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UserDetails;

import java.util.List;
import java.util.Collections;

import eu.scy.core.persistence.hibernate.UserDAOHibernate;
import eu.scy.core.model.User;
import eu.scy.core.model.Group;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 22.jul.2008
 * Time: 14:41:57
 * Just a quick hack to list out users from the root group
 */
public class UserOverview extends SCYBasePage{

    @ApplicationState (create = false)
    private Group currentGroup;

    public Group getCurrentGroup() {
        return currentGroup;
    }

    public void setCurrentGroup(Group currentGroup) {
        this.currentGroup = currentGroup;
    }

    public String getCurrentlySelectedGroupName() {
        if(getCurrentGroup() != null) {
            return getCurrentGroup().getName();
        }

        return "No group selected";
    }


    @Inject
    private UserDAOHibernate userDAO;
    @Persist
    private User user;


    public UserDAOHibernate getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(UserDAOHibernate userDAO) {
        this.userDAO = userDAO;
    }

    public List getUsers() {
        if(getCurrentGroup() != null) {
        return getCurrentGroup().getUsers();
        }
        return Collections.EMPTY_LIST;

    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getCurrentGroupExists() {
        return false;//if(getCurrentGroup() != null) return true;
        //return false;
    }
}
