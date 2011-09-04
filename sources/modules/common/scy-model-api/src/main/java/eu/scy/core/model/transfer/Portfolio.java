package eu.scy.core.model.transfer;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 07.jan.2011
 * Time: 19:46:35
 * To change this template use File | Settings | File Templates.
 */
public class Portfolio extends BaseXMLTransfer {

    private String owner;
    private String portfolioStatus;
    private String missionRuntimeURI;
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
    private List <EloAnchorEloPair> eloAnchorEloPairs = new LinkedList<EloAnchorEloPair>();
    private List <MissionReflectionQuestionAnswer> missionReflectionQuestionAnswers = new LinkedList<MissionReflectionQuestionAnswer>();
    private List <SelectedLearningGoalWithScore> selectedGeneralLearningGoalWithScores = new LinkedList<SelectedLearningGoalWithScore>();
    private List <SelectedLearningGoalWithScore> selectedSpecificLearningGoalWithScores = new LinkedList<SelectedLearningGoalWithScore>();

    public final static String PORTFOLIO_STATUS_NOT_SUBMITTED = "PORTFOLIO_STATUS_NOT_SUBMITTED";
    public final static String PORTFOLIO_STATUS_SUBMITTED_WAITING_FOR_ASSESSMENT = "PORTFOLIO_STATUS_SUBMITTED_WAITING_FOR_ASSESSMENT";
    public final static String PORTFOLIO_STATUS_ASSESSED = "PORTFOLIO_ASSESSED";

    public Portfolio() {
        super();
        setPortfolioStatus(PORTFOLIO_STATUS_NOT_SUBMITTED);
    }

    public void unCdatify() {
        //setPortfolioStatus(replaceCdata(getPortfolioStatus()));

    }

    private String replaceCdata(String inputString) {
        if(inputString == null) return null;
        String thing = "\\[";
        String cdata = "<!CDATA";
        String end = "\\]";

        inputString = inputString.replaceAll(thing, "");
        inputString = inputString.replaceAll(cdata, "");
        inputString = inputString.replaceAll(end, "");
        return inputString;
    }

    private String getCdata(String content) {
        return CDATA_START + content + CDATA_END;
    }

    public String getPortfolioStatus() {
        return portfolioStatus;
    }

    public Boolean getIsPortfolioSubmitted() {
        if(getPortfolioStatus() != null && getPortfolioStatus().equals(PORTFOLIO_STATUS_SUBMITTED_WAITING_FOR_ASSESSMENT)) return true;
        return false;
    }

    public Boolean getIsPortfolioAssessed() {
        if(getPortfolioStatus() != null && getPortfolioStatus().equals(PORTFOLIO_STATUS_ASSESSED)) return true;
        return false;
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

    public void addElo(TransferElo transferElo, TransferElo anchorElo) {
        if(getEloAnchorEloPairs() == null) setEloAnchorEloPairs(new LinkedList<EloAnchorEloPair>());
        EloAnchorEloPair eloAnchorEloPair = new EloAnchorEloPair();
        eloAnchorEloPair.setElo(transferElo);
        eloAnchorEloPair.setAnchorElo(anchorElo);
        getEloAnchorEloPairs().add(eloAnchorEloPair);
    }

    public List<EloAnchorEloPair> getEloAnchorEloPairs() {
        return eloAnchorEloPairs;
    }

    public void setEloAnchorEloPairs(List<EloAnchorEloPair> eloAnchorEloPairs) {
        this.eloAnchorEloPairs = eloAnchorEloPairs;
    }

    public TransferElo getAnchorEloFor(TransferElo elo) {
        for (int i = 0; i < eloAnchorEloPairs.size(); i++) {
            EloAnchorEloPair eloAnchorEloPair = eloAnchorEloPairs.get(i);
            if(eloAnchorEloPair.getElo().getUri().equals(elo.getUri())) return eloAnchorEloPair.getElo();
        }

        return null;
    }

    public boolean getHasEloBeenAdded(TransferElo elo) {
        for (int i = 0; i < eloAnchorEloPairs.size(); i++) {
            EloAnchorEloPair eloAnchorEloPair = eloAnchorEloPairs.get(i);
            if(eloAnchorEloPair.getElo().getUri().equals(elo.getUri())) return true;
        }

        return false;
    }

    public boolean getHasEloBeenAddedForAnchorElo(TransferElo anchorElo) {
        for (int i = 0; i < eloAnchorEloPairs.size(); i++) {
            EloAnchorEloPair eloAnchorEloPair = eloAnchorEloPairs.get(i);
            if(eloAnchorEloPair.getAnchorElo().getUri().equals(anchorElo.getUri())) return true;
        }
        return false;
    }


    public List<MissionReflectionQuestionAnswer> getMissionReflectionQuestionAnswers() {
        return missionReflectionQuestionAnswers;
    }

    public void setMissionReflectionQuestionAnswers(List<MissionReflectionQuestionAnswer> missionReflectionQuestionAnswers) {
        this.missionReflectionQuestionAnswers = missionReflectionQuestionAnswers;
    }

    public void addMissionReflectionQuestionAnswer(MissionReflectionQuestionAnswer missionReflectionQuestionAnswer) {
        if (getMissionReflectionQuestionAnswers() == null) setMissionReflectionQuestionAnswers(new LinkedList<MissionReflectionQuestionAnswer>());
        getMissionReflectionQuestionAnswers().add(missionReflectionQuestionAnswer);
    }

    public List<SelectedLearningGoalWithScore> getSelectedGeneralLearningGoalWithScores() {
        return selectedGeneralLearningGoalWithScores;
    }


    public List<SelectedLearningGoalWithScore> getGeneralLearningGoalsForElo(String eloURI) {
        List <SelectedLearningGoalWithScore> returnList = new LinkedList<SelectedLearningGoalWithScore>();
        for (int i = 0; i < getSelectedGeneralLearningGoalWithScores().size(); i++) {
            SelectedLearningGoalWithScore selectedLearningGoalWithScore = getSelectedGeneralLearningGoalWithScores().get(i);
            if(selectedLearningGoalWithScore.getEloURI() != null && selectedLearningGoalWithScore.getEloURI().equals(eloURI)) {
                returnList.add(selectedLearningGoalWithScore);
            }
        }
        return returnList;
    }


    public void setSelectedGeneralLearningGoalWithScores(List<SelectedLearningGoalWithScore> selectedGeneralLearningGoalWithScores) {
        this.selectedGeneralLearningGoalWithScores = selectedGeneralLearningGoalWithScores;
    }

    public void addSelectedGeneralLearningGoalWithScore(SelectedLearningGoalWithScore selectedLearningGoalWithScore) {
        if(getSelectedGeneralLearningGoalWithScores() == null) setSelectedGeneralLearningGoalWithScores(new LinkedList<SelectedLearningGoalWithScore>());
        getSelectedGeneralLearningGoalWithScores().add(selectedLearningGoalWithScore);
    }

    public List<SelectedLearningGoalWithScore> getSelectedSpecificLearningGoalWithScores() {
        return selectedSpecificLearningGoalWithScores;
    }

    public List<SelectedLearningGoalWithScore> getSpecificLearningGoalsForElo(String eloURI) {
        List <SelectedLearningGoalWithScore> returnList = new LinkedList<SelectedLearningGoalWithScore>();
        for (int i = 0; i < getSelectedSpecificLearningGoalWithScores().size(); i++) {
            SelectedLearningGoalWithScore selectedLearningGoalWithScore = getSelectedSpecificLearningGoalWithScores().get(i);
            if(selectedLearningGoalWithScore.getEloURI() != null && selectedLearningGoalWithScore.getEloURI().equals(eloURI)) {
                returnList.add(selectedLearningGoalWithScore);
            }
        }
        return returnList;
    }

    public void setSelectedSpecificLearningGoalWithScores(List<SelectedLearningGoalWithScore> selectedSpecificLearningGoalWithScores) {
        this.selectedSpecificLearningGoalWithScores = selectedSpecificLearningGoalWithScores;
    }

    public void addSelectedSpecificLearningGoalWithScore(SelectedLearningGoalWithScore selectedLearningGoalWithScore) {
        if(getSelectedSpecificLearningGoalWithScores() == null) setSelectedSpecificLearningGoalWithScores(new LinkedList<SelectedLearningGoalWithScore>());
        getSelectedSpecificLearningGoalWithScores().add(selectedLearningGoalWithScore);
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

    public String getMissionRuntimeURI() {
        return missionRuntimeURI;
    }

    public void setMissionRuntimeURI(String missionRuntimeURI) {
        this.missionRuntimeURI = missionRuntimeURI;
    }

}
