package eu.scy.webapp.pages;

import eu.scy.core.model.User;
import eu.scy.core.model.SCYGroup;
import eu.scy.core.model.impl.SCYUserDetails;

import java.util.List;
import java.util.Collections;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.annotations.Property;
//import org.telscenter.sail.webapp.service.authentication.UserDetailsService;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 06.nov.2008
 * Time: 05:44:23
 * To change this template use File | Settings | File Templates.
 */
public class EditUserPage extends ScyModelPage {

    @Inject
    //private UserDetailsService userDetailsService;



    @Property
    private User user;

    /*public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    */

    public void setUserObject(User user) {
        this.user = user;
    }

    public User getUserObject() {
        return this.user;
    }



    public Object getUserDetails() {
        return null;//(SCYUserDetails) user.getUserDetails();
    }

    public Object onSuccess() {
        getUserDAO().save(user);
        setUserObject(user);
        return EditUserPage.class;
    }
/*
    public void loadModel() {
        setUser(getUserDAO().getUser(new Long(getModelId())));
    }
  */
    public Boolean getDisabled() {
        return true;//getUserDetails().isEnabled();
    }

    public List getRoles() {
         //getUser().
        return Collections.EMPTY_LIST;
    }
   /*
   public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    */
}


