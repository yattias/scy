package eu.scy.framework.actions;

import eu.scy.framework.BaseAction;
import eu.scy.core.model.Group;
import eu.scy.core.model.impl.UserImpl;
import eu.scy.core.model.impl.UserDetails;

import net.sf.sail.webapp.domain.User;
import org.telscenter.sail.webapp.domain.authentication.impl.StudentUserDetails;


/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 23.nov.2008
 * Time: 22:26:19
 * To change this template use File | Settings | File Templates.
 */
public class AddMemberToGroupAction extends BaseAction {

    public AddMemberToGroupAction() {
        super.setName("Add group member");
    }

    public Class getOperatesOn() {
        return Group.class;
    }

    protected Object doAction(Object model) {
       log.info("Adding member to group:" + model);
        User user = new UserImpl();
        UserDetails userDetails = new UserDetails();
        userDetails.setFirstname("New User");
        userDetails.setLastname("New User");
        userDetails.setUsername(getActionManager().getUserDAOHibernate().getSecureUserName("NewUser"));
        userDetails.setPassword("default");
        //userDetails.setAccountNonLocked(true);
        user.setUserDetails(userDetails);
        user = (User) getActionManager().getUserDAOHibernate().save(user);

        //getActionManager().getUserDAOHibernate().addUser(getProject(), (Group) model, user);

        return user;
    }
}
