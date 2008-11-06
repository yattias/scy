package eu.scy.pages;

import eu.scy.core.persistence.GroupDAO;
import eu.scy.core.model.Group;
import eu.scy.core.model.User;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry.commons.components.SlidingPanel;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 20.okt.2008
 * Time: 06:29:24
 * To change this template use File | Settings | File Templates.
 */
public class GroupOverview extends ScyModelPage {

    private static Logger log = Logger.getLogger("GroupOverview.class");

    private User currentUser;

    @InjectPage
    private EditUserPage editUserPage;

    @Inject
    private GroupDAO groupDAO;

    @Inject
    private User newUser;

    public User getNewUser() {
        return newUser;
    }

    public void setNewUser(User newUser) {
        this.newUser = newUser;
    }

    public GroupDAO getGroupDAO() {
        return groupDAO;
    }

    public void setGroupDAO(GroupDAO groupDAO) {
        this.groupDAO = groupDAO;
    }

    public void loadModel() {
        setModel(groupDAO.getGroup(getModelId()));
    }

    public Group getRootGroup() {
        return getGroupDAO().getRootGroup(getCurrentProject());
    }

    public String getSomeTitle() {
        return getRootGroup().getClass().getName() + " :.... " + "HENRIK" + getRootGroup().getName();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public List <User> getGroupMembers() {
        Group group = (Group) getModel();
        return group.getUsers();
    }

    public Object onActionFromAddUser(String groupId) {
        log.info("Adding new user...");
        Group group = getGroupDAO().getGroup(groupId);

        User user = getNewUser();

        user.setUserName(getUserDAOHibernate().getSecureUserName("U"));
        user = (User) getUserDAOHibernate().save(user);

        getUserDAOHibernate().addUser(getCurrentProject(), group, user);
        log.info("Added new user to the project - " + user.getId() + " " + user.getUserName());
        editUserPage.setModelId(user.getId());

        return editUserPage;

    }

}
