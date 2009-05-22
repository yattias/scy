package eu.scy.webapp.pages;

import eu.scy.core.model.User;
import eu.scy.core.model.SCYGroup;

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
        log.info("SUCCESS!! -SAVING USER!!!!");
        getUserDAO().save(user);
        setUser(user);
        return EditUserPage.class;
        //return getUserDAO().getUserByUsername(user.getUserDetails().getUsername());
        //throw new RuntimeException("NOT IMPLEMENTED YET");
        //return null;
    }

    public void loadModel() {
        //throw new RuntimeException("NOT IMPLEMENTED YET");
        setUser(getUserDAO().getUser(new Long(getModelId())));
        //log.info("Got model in EDIT USER PAGE: " + getModel());
        //setUser((User) getModel());

    }


    public Boolean getDisabled() {
        return getUser().getUserDetails().isEnabled();
    }
}


