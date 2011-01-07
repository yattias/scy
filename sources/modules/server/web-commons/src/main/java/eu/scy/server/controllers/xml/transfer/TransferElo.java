package eu.scy.server.controllers.xml.transfer;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 07.jan.2011
 * Time: 19:49:17
 * To change this template use File | Settings | File Templates.
 */
public class TransferElo {

    private String uri;
    private String catname;
    private String thumbnail;
    private String fullsize;
    private String myname;
    private String modified;
    private String studentDescription;
    private LearningGoal generalLearningGoal;
    private LearningGoal specificLearningGoal;
    private String studentReflection;
    private String studentInquiry;
    private Boolean assessed = Boolean.FALSE;
    private String grade;
    private String assessmentComment;
    private String reflectionComment;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getCatname() {
        return catname;
    }

    public void setCatname(String catname) {
        this.catname = catname;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getFullsize() {
        return fullsize;
    }

    public void setFullsize(String fullsize) {
        this.fullsize = fullsize;
    }

    public String getMyname() {
        return myname;
    }

    public void setMyname(String myname) {
        this.myname = myname;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getStudentDescription() {
        return studentDescription;
    }

    public void setStudentDescription(String studentDescription) {
        this.studentDescription = studentDescription;
    }

    public LearningGoal getGeneralLearningGoal() {
        return generalLearningGoal;
    }

    public void setGeneralLearningGoal(LearningGoal generalLearningGoal) {
        this.generalLearningGoal = generalLearningGoal;
    }

    public LearningGoal getSpecificLearningGoal() {
        return specificLearningGoal;
    }

    public void setSpecificLearningGoal(LearningGoal specificLearningGoal) {
        this.specificLearningGoal = specificLearningGoal;
    }

    public String getStudentReflection() {
        return studentReflection;
    }

    public void setStudentReflection(String studentReflection) {
        this.studentReflection = studentReflection;
    }

    public String getStudentInquiry() {
        return studentInquiry;
    }

    public void setStudentInquiry(String studentInquiry) {
        this.studentInquiry = studentInquiry;
    }

    public Boolean getAssessed() {
        return assessed;
    }

    public void setAssessed(Boolean assessed) {
        this.assessed = assessed;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getAssessmentComment() {
        return assessmentComment;
    }

    public void setAssessmentComment(String assessmentComment) {
        this.assessmentComment = assessmentComment;
    }

    public String getReflectionComment() {
        return reflectionComment;
    }

    public void setReflectionComment(String reflectionComment) {
        this.reflectionComment = reflectionComment;
    }
}
