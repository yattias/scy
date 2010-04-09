package eu.scy.core.model.impl.auth;

import eu.scy.core.model.auth.SessionInfo;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.jan.2010
 * Time: 13:10:24
 * To change this template use File | Settings | File Templates.
 */
public class SessionInfoImpl implements SessionInfo,  Serializable {

    private String userName;
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
