package eu.scy.core.model.transfer;

import eu.scy.core.model.User;

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
    private List <User> activeUsers;
    private String humanReadableName;

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


    public void addActiveUser(User user) {
        if(getActiveUsers() == null) activeUsers = new LinkedList<User>();
        if(!getActiveUsers().contains(user)) {
            getActiveUsers().add(user);    
        }
    }

    public List<User> getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(List<User> activeUsers) {
        this.activeUsers = activeUsers;
    }

    public String getHumanReadableName() {
        return humanReadableName;
    }

    public void setHumanReadableName(String humanReadableName) {
        this.humanReadableName = humanReadableName;
    }
}
