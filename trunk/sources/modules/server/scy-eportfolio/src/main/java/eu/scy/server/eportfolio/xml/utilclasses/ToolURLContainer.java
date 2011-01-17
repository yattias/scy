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
    private String currentMissionRuntimeInfo = "/webapp/app/eportfolio/xml/currentMissionRuntimeInfo.html";
    private String assessmentService = "/webapp/components/xml/AssessmentService.html";
    private String portfolioLoader = "/webapp/app/eportfolio/xml/loadPortfolio.html";
    private String learningGoalsLoader = "/webapp/app/eportfolio/xml/loadLearningGoals.html";
    private String savePortfolio = "/webapp/app/eportfolio/xml/savePortfolio.html";
    private String eloSearchService = "/webapp/app/eportfolio/xml/eloSearchService.html";


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

    public String getCurrentMissionRuntimeInfo() {
        return currentMissionRuntimeInfo;
    }

    public void setCurrentMissionRuntimeInfo(String currentMissionRuntimeInfo) {
        this.currentMissionRuntimeInfo = currentMissionRuntimeInfo;
    }

    public String getAssessmentService() {
        return assessmentService;
    }

    public void setAssessmentService(String assessmentService) {
        this.assessmentService = assessmentService;
    }

    public String getPortfolioLoader() {
        return portfolioLoader;
    }

    public void setPortfolioLoader(String portfolioLoader) {
        this.portfolioLoader = portfolioLoader;
    }

    public String getSavePortfolio() {
        return savePortfolio;
    }

    public void setSavePortfolio(String savePortfolio) {
        this.savePortfolio = savePortfolio;
    }

    public String getLearningGoalsLoader() {
        return learningGoalsLoader;
    }

    public void setLearningGoalsLoader(String learningGoalsLoader) {
        this.learningGoalsLoader = learningGoalsLoader;
    }

    public String getEloSearchService() {
        return eloSearchService;
    }

    public void setEloSearchService(String eloSearchService) {
        this.eloSearchService = eloSearchService;
    }
}
