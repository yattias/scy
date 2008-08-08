package eu.scy.modules.useradmin.components.grouplist;

import eu.scy.modules.useradmin.pages.SCYBasePage;
import eu.scy.core.model.Project;
import eu.scy.core.model.Group;
import eu.scy.core.persistence.hibernate.UserDAOHibernate;

import java.util.List;

import org.apache.tapestry.ioc.annotations.Inject;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 08.aug.2008
 * Time: 14:56:29
 * To change this template use File | Settings | File Templates.
 */
public class GroupList extends SCYBasePage {

    private Group group;

    @Inject
    private UserDAOHibernate userDAOHibernate;

    public UserDAOHibernate getUserDAOHibernate() {
        return userDAOHibernate;
    }

    public void setUserDAOHibernate(UserDAOHibernate userDAOHibernate) {
        this.userDAOHibernate = userDAOHibernate;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public List getGroups() {
        Project p = getCurrentProject();
        if(p == null) return null;

        return p.getGroups();
    }

    public void onActionFromAddGroup() {
        System.out.println("*** *** *** *** * Add group");
        if(getCurrentProject() != null) {
            Project p = getCurrentProject();
            Group g = getUserDAOHibernate().createGroup("New Group", null);
            p.getGroups().add(g);
            getUserDAOHibernate().save(p);
        }
    }

}
