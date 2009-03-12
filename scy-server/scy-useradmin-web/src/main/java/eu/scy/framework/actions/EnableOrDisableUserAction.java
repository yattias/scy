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
        if (model != null) {
            if (model.getEnabled() != null && model.getEnabled().equals("1")) return "Disable";
            return "Enable";
        } else {
            return "Hei";
        }


    }

    protected Object doAction(Object model) {
        User user = (User) model;
        if (user.getEnabled() != null && user.getEnabled().equals("0")) {
            user.setEnabled("1");
        } else {
            user.setEnabled("0");
        }
        user = (User) getActionManager().getUserDAOHibernate().save(user);
        return user;
    }
}
