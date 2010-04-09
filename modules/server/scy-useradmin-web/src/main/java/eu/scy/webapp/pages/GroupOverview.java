package eu.scy.webapp.pages;

import eu.scy.core.persistence.GroupDAO;
import eu.scy.core.model.SCYGroup;
import eu.scy.core.model.impl.SCYUserDetails;
import eu.scy.core.model.impl.SCYGroupImpl;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.annotations.InjectPage;

import java.util.List;
import java.util.Collections;
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

    //private User currentUser;

    @InjectPage
    private EditUserPage editUserPage;

    @Inject
    private GroupDAO groupDAO;

    /*Inject
    private User newUser;

    public User getNewUser() {
        return newUser;
    }

    public void setNewUser(User newUser) {
        this.newUser = newUser;
    }
    */
    public GroupDAO getGroupDAO() {
        return groupDAO;
    }

    public void setGroupDAO(GroupDAO groupDAO) {
        this.groupDAO = groupDAO;
    }

    /*public SCYGroup getRootGroup() {
        return getGroupDAO().getRootGroup(getCurrentProject());
    } */
    /*
    public String getSomeTitle() {
        return getRootGroup().getClass().getName() + " :.... " + "HENRIK" + getRootGroup().getName();
    }
      */

    /*
    public SCYUserDetails getUserDetails() {
        return (SCYUserDetails) getCurrentUser().getUserDetails();
    }

    public List<User> getGroupMembers() {
        SCYGroupImpl group = (SCYGroupImpl) getModel();
        return getGroupDAO().getUsers(group);
    }
      */
    @InjectPage
    private EditGroup editGroup;

    Object onActionFromEditGroup(String groupId) {
        log.info("EDIT GROUP!");
/*        editGroup.setModelId(groupId);
        editGroup.loadModel();
        */
        return editGroup;
    }

    Object onActionFromDeleteGroup(String groupId) {
        getUserDAO().deleteGroup(groupId);
        return null;
    }


    Object onActionFromOpenUser(String userId) {
        log.info("USER ID: " + userId);
        //editUserPage.setModelId(userId);
        //editUserPage.loadModel();
        return editUserPage;
    }


}
