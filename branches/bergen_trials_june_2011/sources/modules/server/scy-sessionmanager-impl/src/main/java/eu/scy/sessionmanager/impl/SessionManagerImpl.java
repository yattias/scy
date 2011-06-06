package eu.scy.sessionmanager.impl;

import eu.scy.sessionmanager.SessionManager;
import eu.scy.core.persistence.hibernate.UserSessionDAOHibernate;
import eu.scy.core.model.UserSession;

import javax.security.auth.login.CredentialNotFoundException;
import javax.security.auth.login.LoginException;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.nov.2008
 * Time: 11:56:43
 * This class provides an in-memory implementation of the UserSessionManager. Objects of this class should only
 * be used when instantiated within the same vm as the scy-server
 */
public class SessionManagerImpl implements SessionManager {

      private static Logger log = Logger.getLogger("SessionManagerImpl.class");
    private UserSessionDAOHibernate userSessionDAOHibernate;


    public UserSessionDAOHibernate getUserSessionDAOHibernate() {
        return userSessionDAOHibernate;
    }

    public void setUserSessionDAOHibernate(UserSessionDAOHibernate userSessionDAOHibernate) {
        this.userSessionDAOHibernate = userSessionDAOHibernate;
    }

    public String login(String username, String password) throws LoginException {
        log.info("Logging in user: "+ username);
        UserSession session = getUserSessionDAOHibernate().loginUser(username, password);
        if(session == null) throw new LoginException("User not found!");
        return session.getSessionId();
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
