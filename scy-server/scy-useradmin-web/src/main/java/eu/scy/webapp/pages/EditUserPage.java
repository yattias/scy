package eu.scy.webapp.pages;

import eu.scy.core.model.User;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 06.nov.2008
 * Time: 05:44:23
 * To change this template use File | Settings | File Templates.
 */
public class EditUserPage extends ScyModelPage {

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Object onSuccess() {
        getUserDAO().save(user);
        return getNextPage(user);
    }

    public void loadModel() {
        setModel(getUserDAO().getUser(getModelId()));
        log.info("Got model in EDIT USER AGE: " + getModel());
        setUser((User) getModel());
    }
}
