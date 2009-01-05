package eu.scy.framework.actions;

import eu.scy.framework.BaseAction;
import eu.scy.core.model.Group;
import eu.scy.core.model.User;
import eu.scy.core.model.impl.UserImpl;

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
        user.setFirstName("New User");
        user.setLastName("New User");
        user.setUserName(getActionManager().getUserDAOHibernate().getSecureUserName("NewUser"));
        user.setPassword("default");
        user.setEnabled("1");
        user = (User) getActionManager().getUserDAOHibernate().save(user);

        getActionManager().getUserDAOHibernate().addUser(getProject(), (Group) model, user);

        return user;
    }
}
