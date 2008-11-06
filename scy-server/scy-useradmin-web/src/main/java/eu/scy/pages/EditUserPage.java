package eu.scy.pages;

import eu.scy.core.model.User;
import eu.scy.core.model.impl.UserImpl;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 06.nov.2008
 * Time: 05:44:23
 * To change this template use File | Settings | File Templates.
 */
public class EditUserPage extends ScyModelPage{

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void loadModel() {
        setModel(getUserDAO().getUser(getModelId()));
        setUser((User) getModel());
    }
}
