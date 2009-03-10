package eu.scy.webapp.pages;

import eu.scy.core.persistence.GroupDAO;
import eu.scy.core.model.Group;

import eu.scy.webapp.pages.projectmanagement.ProjectEditor;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.annotations.InjectPage;

import java.util.List;
import java.util.logging.Logger;

import net.sf.sail.webapp.domain.User;

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

    public List<User> getGroupMembers() {
        Group group = (Group) getModel();
        //return group.getUsers();
        throw new RuntimeException("NOT IMPLEMENTED!");
    }

    @InjectPage
    private EditGroup editGroup;

    Object onActionFromEditGroup(String groupId) {
        log.info("EDIT GROUP!");
        editGroup.setModelId(groupId);
        editGroup.loadModel();
        return editGroup;
    }

    Object onActionFromDeleteGroup(String groupId) {
        getUserDAO().deleteGroup(groupId);
        return null;
    }


}
