package eu.scy.modules.useradmin.pages;


import eu.scy.core.model.Group;
import eu.scy.core.model.User;
import eu.scy.core.persistence.hibernate.UserDAOHibernate;
import eu.scy.modules.useradmin.pages.projectmanagement.ProjectManagement;

import java.util.Collections;
import java.util.List;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 22.jul.2008
 * Time: 14:41:57
 * Just a quick hack to list out users from the root group
 */
public class UserOverview extends SCYBasePage {

    @ApplicationState(create = false)
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

    public Object onSuccess() {
        System.out.println("SAVING Group!!!!!");
        userDAO.save(getCurrentGroup());
        return UserOverview.class;
    }
}
