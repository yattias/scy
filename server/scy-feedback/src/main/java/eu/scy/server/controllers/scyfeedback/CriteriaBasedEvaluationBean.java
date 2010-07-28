package eu.scy.server.controllers.scyfeedback;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.jul.2010
 * Time: 06:37:23
 * To change this template use File | Settings | File Templates.
 */
public class CriteriaBasedEvaluationBean {

    private String criteriaText;
    private String modelId;
    private String eloRefId;
    private String evaluationCriteriaId;
    private String assessmentId;
    private String score;
    private String comment;

    public String getCriteriaText() {
        return criteriaText;
    }

    public void setCriteriaText(String criteriaText) {
        this.criteriaText = criteriaText;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getEloRefId() {
        return eloRefId;
    }

    public void setEloRefId(String eloRefId) {
        this.eloRefId = eloRefId;
    }

    public String getEvaluationCriteriaId() {
        return evaluationCriteriaId;
    }

    public void setEvaluationCriteriaId(String evaluationCriteriaId) {
        this.evaluationCriteriaId = evaluationCriteriaId;
    }

    public String getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(String assessmentId) {
        this.assessmentId = assessmentId;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
