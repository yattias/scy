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
        //SCYGroup g = getUserDAO().getUserByUsername(user.getUserName()).getGroup();
        throw new RuntimeException("NOT IMPLEMENTED YET");
        //return null;
    }

    public void loadModel() {
        throw new RuntimeException("NOT IMPLEMENTED YET");
        /*setModel(getUserDAO().getUser(getModelId()));
        log.info("Got model in EDIT USER PAGE: " + getModel());
        setUser((User) getModel());
        */
    }


    public Boolean getDisabled() {
        if(getUser().getEnabled() == null || getUser().getEnabled().equals("0") ) return true;
        return false;
    }
}


