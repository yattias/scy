package eu.scy.framework.actions;

import eu.scy.framework.BaseAction;
import eu.scy.core.model.impl.SCYUserImpl;
import eu.scy.core.model.User;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 25.nov.2008
 * Time: 10:02:58
 * To change this template use File | Settings | File Templates.
 */
public class EnableOrDisableUserAction extends BaseAction {
    public Class getOperatesOn() {
        return SCYUserImpl.class;
    }

    @Override
    public String getName() {
        User model = (User) getUserObject();
        /*if (model != null) {
            //if (model.getUserDetails().isEnabled() ){
            //return "Enable";
        } else {
            return "Hei";
        }
        }
        return "";
        */
        return "hi";

    }

    protected Object doAction(Object model) {
        /*User user = (User) model;
        if (user.getUserDetails().isEnabled()) {
            user.getUserDetails().
        } else {
            user.setEnabled("0");
        }
        user = (User) getActionManager().getUserDAOHibernate().save(user);
        return user;
        */
        throw new RuntimeException("NOT IMPLEMENTED YET!");
    }
}
