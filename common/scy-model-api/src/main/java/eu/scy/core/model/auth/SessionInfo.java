package eu.scy.core.model.auth;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 13.jan.2010
 * Time: 11:01:30
 * To change this template use File | Settings | File Templates.
 */
public interface SessionInfo {
    String getUserName();

    void setUserName(String userName);

    String getPassword();

    void setPassword(String password);
}
