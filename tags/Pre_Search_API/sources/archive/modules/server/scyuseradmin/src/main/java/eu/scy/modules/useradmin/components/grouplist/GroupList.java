package eu.scy.modules.useradmin.components.grouplist;

import eu.scy.modules.useradmin.pages.SCYBasePage;
import eu.scy.core.model.Project;
import eu.scy.core.model.Group;
import eu.scy.core.persistence.hibernate.UserDAOHibernate;
import eu.scy.core.persistence.UserDAO;

import java.util.List;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 08.aug.2008
 * Time: 14:56:29
 * To change this template use File | Settings | File Templates.
 */
public class GroupList extends SCYBasePage {

    @ApplicationState(create=false)
    private Group currentGroup;

    private Group group;

    @Inject
    private UserDAOHibernate userDAOHibernate;

    public UserDAO getUserDAO() {
        return userDAOHibernate;
    }

    public void setUserDAOHibernate(UserDAOHibernate userDAOHibernate) {
        this.userDAOHibernate = userDAOHibernate;
    }

    //used for iterating over the list the list
    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    /**
     * the currently selected group - application state
     * @return
     */

    public Group getCurrentGroup() {
        return currentGroup;
    }

    public void setCurrentGroup(Group currentGroup) {
        this.currentGroup = currentGroup;
    }

    public List getGroups() {
        Project p = getCurrentProject();
        if(p == null) {
            return null;
        }

        return p.getGroups();
    }

    public void onActionFromAddGroup() {
        if(getCurrentProject() != null) {
            Group g = getUserDAO().createGroup(getCurrentProject(), "New Group", null);
            getUserDAO().save(g);
        }
    }

    public void onActionFromSelectGroup(String id) {
        System.out.println("SELECTED GROUP!!! "+ id);
        Group g = getUserDAO().getGroup(id);
        setCurrentGroup(g);
    }

    public long getUserCount() {
        return getUserDAO().getNumberOfUsers(getGroup());
    }
}
