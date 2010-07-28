package eu.scy.server.controllers.scyfeedback;

import eu.scy.core.model.AssessmentCriteria;
import eu.scy.core.model.ELORef;
import eu.scy.core.model.pedagogicalplan.AssessmentCriteriaExperience;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 15.jun.2010
 * Time: 13:22:37
 * To change this template use File | Settings | File Templates.
 */
public class ELORefDataTransporter {

    private ELORef eloRef;
    private List files;
    private List assessments;
    private Integer totalScore;
    private Integer totalAssessments;
    private List evaluationCriteria;
    private List criteriaAndExperienceHolders;
    private List assessmentScoreDefinitions;
    private Boolean useCriteriaBasedAssessment;
    private String scoreImageId;

    private Map criteriaMap;


    public ELORef getEloRef() {
        return eloRef;
    }

    public void setEloRef(ELORef eloRef) {
        this.eloRef = eloRef;
    }

    public List getFiles() {
        return files;
    }

    public void setFiles(List files) {
        this.files = files;
    }

    public List getAssessments() {
        return assessments;
    }

    public void setAssessments(List assessments) {
        this.assessments = assessments;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public Integer getTotalAssessments() {
        return totalAssessments;
    }

    public void setTotalAssessments(Integer totalAssessments) {
        this.totalAssessments = totalAssessments;
    }

    public List getEvaluationCriteria() {
        return evaluationCriteria;
    }

    public void setEvaluationCriteria(List evaluationCriteria) {
        this.evaluationCriteria = evaluationCriteria;
    }

    public List getAssessmentScoreDefinitions() {
        return assessmentScoreDefinitions;
    }

    public void setAssessmentScoreDefinitions(List assessmentScoreDefinitions) {
        this.assessmentScoreDefinitions = assessmentScoreDefinitions;
    }

    //Helper
    public Integer getColSpan() {
        if(getAssessmentScoreDefinitions() == null) return 1;
        return getAssessmentScoreDefinitions().size() + 1;
    }

    public Boolean getUseCriteriaBasedAssessment() {
        return useCriteriaBasedAssessment;
    }

    public void setUseCriteriaBasedAssessment(Boolean useCriteriaBasedAssessment) {
        this.useCriteriaBasedAssessment = useCriteriaBasedAssessment;
    }

    public AssessmentCriteriaExperience getExperience(AssessmentCriteria assessmentCriteria) {
        return (AssessmentCriteriaExperience) criteriaMap.get(assessmentCriteria);
    }

    public List getCriteriaAndExperienceHolders() {
        return criteriaAndExperienceHolders;
    }

    public void setCriteriaAndExperienceHolders(List criteriaAndExperienceHolders) {
        this.criteriaAndExperienceHolders = criteriaAndExperienceHolders;
    }

    public Map getCriteriaMap() {
        return criteriaMap;
    }

    public void setCriteriaMap(Map criteriaMap) {
        this.criteriaMap = criteriaMap;
    }

    public String getStudentCriteriaText(AssessmentCriteria assessmentCriteria) {
        if(getExperience(assessmentCriteria) != null) {
            AssessmentCriteriaExperience experience = getExperience(assessmentCriteria);
            if(experience.getCriteriaText() != null) return experience.getCriteriaText();
        }

        return assessmentCriteria.getCriteria();
    }

    public String getScoreImageId() {
        return scoreImageId;
    }

    public void setScoreImageId(String scoreImageId) {
        this.scoreImageId = scoreImageId;
    }
}
