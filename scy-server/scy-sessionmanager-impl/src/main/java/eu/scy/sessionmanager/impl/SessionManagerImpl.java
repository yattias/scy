package eu.scy.sessionmanager.impl;

import eu.scy.sessionmanager.SessionManager;

import javax.security.auth.login.CredentialNotFoundException;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.nov.2008
 * Time: 11:56:43
 * To change this template use File | Settings | File Templates.
 */
public class SessionManagerImpl implements SessionManager {

      private static Logger log = Logger.getLogger("SessionManagerImpl.class");

    public String login(String username, String password) {
        log.info("Logging in : " + username);
        return "hhhha";
    }

    public void setFirstName(String s) throws CredentialNotFoundException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getFirstName() throws CredentialNotFoundException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setLastName(String s) throws CredentialNotFoundException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getLastName() throws CredentialNotFoundException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
