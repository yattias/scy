package eu.scy.sessionmanager;

import javax.security.auth.login.CredentialNotFoundException;
import javax.security.auth.login.LoginException;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.nov.2008
 * Time: 11:32:47
 * Interface for user sessions.
 */
public interface SessionManager {

    public String login(String username, String password) throws LoginException;

    public void setFirstName(String firstName) throws CredentialNotFoundException;

    public String getFirstName()  throws CredentialNotFoundException;

    public void setLastName(String lastName)  throws CredentialNotFoundException;

    public String getLastName() throws CredentialNotFoundException;


}
