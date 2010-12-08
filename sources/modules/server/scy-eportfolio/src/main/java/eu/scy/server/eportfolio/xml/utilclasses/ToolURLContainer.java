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
    private String eportfolioELOSearch = "/webapp/app/eportfolio/xml/eportfolioELOSearch.html";
    private String obligatoryELOsInMission = "/webapp/app/eportfolio/xml/obligatoryELOsInMission.html";
    private String runtimeElosList = "/webapp/app/eportfolio/xml/runtimeElosList.html";
    private String metaData = "We give a fuck in metadata!";

    private String userName;

    public ToolURLContainer(String userName) {
        this.userName = userName;
        setRuntimeElosList(getRuntimeElosList()+ "?username=" + userName);
    }

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

    public String getEportfolioELOSearch() {
        return eportfolioELOSearch;
    }

    public void setEportfolioELOSearch(String eportfolioELOSearch) {
        this.eportfolioELOSearch = eportfolioELOSearch;
    }

    public String getObligatoryELOsInMission() {
        return obligatoryELOsInMission;
    }

    public void setObligatoryELOsInMission(String obligatoryELOsInMission) {
        this.obligatoryELOsInMission = obligatoryELOsInMission;
    }

    public String getRuntimeElosList() {
        return runtimeElosList;
    }

    public void setRuntimeElosList(String runtimeElosList) {
        this.runtimeElosList = runtimeElosList;
    }
}
