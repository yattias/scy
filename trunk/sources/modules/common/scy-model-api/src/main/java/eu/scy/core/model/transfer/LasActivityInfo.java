package eu.scy.core.model.transfer;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 07.jun.2011
 * Time: 22:38:14
 * To change this template use File | Settings | File Templates.
 */
public class LasActivityInfo {

    private String lasName;
    private List <String> activeUsers;

    public String getLasName() {
        return lasName;
    }

    public void setLasName(String lasName) {
        this.lasName = lasName;
    }


    public String getParsedUserName(String smackUserName) {
        String userName = smackUserName;
        String returnValue = userName.substring(0, userName.indexOf("@"));
        return returnValue;
    }


    public void addActiveUser(String user) {
        user = getParsedUserName(user);
        if(getActiveUsers() == null) activeUsers = new LinkedList<String>();
        if(!getActiveUsers().contains(user)) {
            getActiveUsers().add(user);    
        }
    }

    public List<String> getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(List<String> activeUsers) {
        this.activeUsers = activeUsers;
    }
}
