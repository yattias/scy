package eu.scy.framework.actions;

import eu.scy.core.model.User;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 05.jan.2009
 * Time: 12:33:48
 * To change this template use File | Settings | File Templates.
 */
public class DeleteUser extends DeleteAction{

    public Class getOperatesOn() {
        return User.class;
    }

    protected Object doAction(Object model) {
        User user = (User) model;
        getActionManager().getUserDAOHibernate().deleteUser(null);
        return null;
    }
}
