package eu.scy.core.persistence;

import eu.scy.core.model.User;
import eu.scy.core.model.UserSession;

import javax.security.auth.login.LoginException;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 21.okt.2008
 * Time: 07:58:29
 * A DAO to manage all user sessions created by any kind of login (web-client-service etc)
 */
public interface UserSessionDAO extends SCYBaseDAO {

    /**
     * creates a UserSession object for the current session. Will first check whether there is an active ongoing session first.
     * If there exists an ongoing session, a new one will not be created
     * @param user
     */
    public UserSession loginUser(User user) throws LoginException;

    /**
     * logs the user out of the system and cleans up user sessions
     * @param user
     */
    public void logoutUser(User user);

    /**
     * gives a handle to the currently ongoing UserSession. Returns null if no sessions are active
     * @param user
     * @return
     */
    public UserSession getActiveSession(User user);

    UserSession loginUser(String userName, String password)throws LoginException;
}
