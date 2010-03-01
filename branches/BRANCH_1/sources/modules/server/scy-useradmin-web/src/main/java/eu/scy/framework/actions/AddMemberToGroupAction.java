package eu.scy.framework.actions;

import eu.scy.framework.BaseAction;
import eu.scy.core.model.SCYGroup;
import eu.scy.core.model.User;
import eu.scy.core.model.impl.SCYUserImpl;
import eu.scy.core.model.impl.SCYUserDetails;

import java.util.Date;



/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 23.nov.2008
 * Time: 22:26:19
 * To change this template use File | Settings | File Templates.
 */
public class    AddMemberToGroupAction extends BaseAction {

    public AddMemberToGroupAction() {
        super.setName("Add group member");
    }

    public Class getOperatesOn() {
        return SCYGroup.class;
    }

    protected Object doAction(Object model) {
       log.info("Adding member to group:" + model);
        /*User user = new SCYUserImpl();
        SCYUserDetails userDetails = new SCYUserDetails();
        userDetails.setFirstname("New User");
        userDetails.setLastname("New User");
        userDetails.setUsername(getActionManager().getUserDAOHibernate().getSecureUserName("NewUser"));
        userDetails.setPassword("default");
        userDetails.setSignupdate(new Date());
        userDetails.setAccountQuestion("What is your name");
        userDetails.setAccountAnswer("New User");
        userDetails.setBirthday(new Date());
        userDetails.setGender(Gender.MALE);
        userDetails.setNumberOfLogins(0);
        user.setUserDetails(userDetails);
        user = (User) getActionManager().getUserDAOHibernate().save(user);
         */
        //getActionManager().getUserDAOHibernate().addUser(getProject(), (SCYGroup) model, user);

        return null;
    }
}
