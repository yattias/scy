package eu.scy.server.controllers.xml.transfer;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 07.jan.2011
 * Time: 19:46:35
 * To change this template use File | Settings | File Templates.
 */
public class Portfolio {

    private String owner;
    private String portfolioStatus;
    private String missionName;
    private String reflectionMission;
    private String reflectionCollaboration;
    private String reflectionInquiry;
    private String reflectionEffort;
    private String assessmentPortfolioComment;
    private String assessmentPortfolioRating;
    private Boolean assessed = Boolean.FALSE;

    private static final String CDATA_START = "<![CDATA[";
    private static final String CDATA_END = "]]>";

    private List<TransferElo> elos = new LinkedList();


    private String getCdata(String content) {
        return CDATA_START + content + CDATA_END;
    }

    public String getPortfolioStatus() {
        return getCdata(portfolioStatus);
    }

    public void setPortfolioStatus(String portfolioStatus) {
        this.portfolioStatus = portfolioStatus;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getMissionName() {
        return getCdata(missionName);
    }

    public void setMissionName(String missionName) {
        this.missionName = missionName;
    }

    public String getReflectionMission() {
        return getCdata(reflectionMission);
    }

    public void setReflectionMission(String reflectionMission) {
        this.reflectionMission = reflectionMission;
    }

    public String getReflectionCollaboration() {
        return getCdata(reflectionCollaboration);
    }

    public void setReflectionCollaboration(String reflectionCollaboration) {
        this.reflectionCollaboration = reflectionCollaboration;
    }

    public String getReflectionInquiry() {
        return getCdata(reflectionInquiry);
    }

    public void setReflectionInquiry(String reflectionInquiry) {
        this.reflectionInquiry = reflectionInquiry;
    }

    public String getReflectionEffort() {
        return reflectionEffort;
    }

    public void setReflectionEffort(String reflectionEffort) {
        this.reflectionEffort = reflectionEffort;
    }

    public Boolean getAssessed() {
        return assessed;
    }

    public void setAssessed(Boolean assessed) {
        this.assessed = assessed;
    }

    public List<TransferElo> getElos() {
        return elos;
    }

    public void setElos(List<TransferElo> elos) {
        this.elos = elos;
    }

    public void addElo(TransferElo transferElo) {
        if(getElos() == null) setElos(new LinkedList());
        getElos().add(transferElo);
    }

    public String getAssessmentPortfolioComment() {
        return assessmentPortfolioComment;
    }

    public void setAssessmentPortfolioComment(String assessmentPortfolioComment) {
        this.assessmentPortfolioComment = assessmentPortfolioComment;
    }

    public String getAssessmentPortfolioRating() {
        return assessmentPortfolioRating;
    }

    public void setAssessmentPortfolioRating(String assessmentPortfolioRating) {
        this.assessmentPortfolioRating = assessmentPortfolioRating;
    }
}
