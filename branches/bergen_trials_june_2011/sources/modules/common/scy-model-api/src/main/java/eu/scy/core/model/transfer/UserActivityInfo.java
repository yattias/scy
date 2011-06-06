package eu.scy.core.model.transfer;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 18.mai.2011
 * Time: 13:33:59
 * To change this template use File | Settings | File Templates.
 */
public class UserActivityInfo {

    private String userName;
    private String language;
    private String missionSpecification;
    private String missionName;
    private String toolName;
    private String lasName;
    private String numberOfElosProduced;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getParsedUserName() {
        String userName = getUserName();
        String returnValue = userName.substring(0, userName.indexOf("@"));
        return returnValue;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getMissionSpecification() {
        return missionSpecification;
    }

    public void setMissionSpecification(String missionSpecification) {
        this.missionSpecification = missionSpecification;
    }

    public String getMissionName() {
        return missionName;
    }

    public void setMissionName(String missionName) {
        this.missionName = missionName;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public String getLasName() {
        return lasName;
    }

    public void setLasName(String lasName) {
        this.lasName = lasName;
    }

    public String getNumberOfElosProduced() {
        return numberOfElosProduced;
    }

    public void setNumberOfElosProduced(String numberOfElosProduced) {
        this.numberOfElosProduced = numberOfElosProduced;
    }
}
