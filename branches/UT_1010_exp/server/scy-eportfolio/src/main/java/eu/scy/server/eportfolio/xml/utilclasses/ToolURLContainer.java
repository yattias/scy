package eu.scy.server.eportfolio.xml.utilclasses;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 06.okt.2010
 * Time: 05:30:37
 * To change this template use File | Settings | File Templates.
 */
public class ToolURLContainer {

    private String userInfoURL = "/webapp/components/xml/UserInfoController.html";
    private String curentMissionProgressOverview = "/webapp/app/eportfolio/xml/currentMissionProgressOverview.html";
    private String metaData = "We give a fuck in metadata!";

    public String getUserInfoURL() {
        return userInfoURL;
    }

    public void setUserInfoURL(String userInfoURL) {
        this.userInfoURL = userInfoURL;
    }

    public String getCurentMissionProgressOverview() {
        return curentMissionProgressOverview;
    }

    public void setCurentMissionProgressOverview(String curentMissionProgressOverview) {
        this.curentMissionProgressOverview = curentMissionProgressOverview;
    }
}
