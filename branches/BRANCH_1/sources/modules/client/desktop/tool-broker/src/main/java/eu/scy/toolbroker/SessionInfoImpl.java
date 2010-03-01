package eu.scy.toolbroker;

import eu.scy.core.model.auth.SessionInfo;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 13.jan.2010
 * Time: 11:02:08
 * To change this template use File | Settings | File Templates.
 */
public class SessionInfoImpl implements SessionInfo {

    private String userName;
    private String password;

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }
}
