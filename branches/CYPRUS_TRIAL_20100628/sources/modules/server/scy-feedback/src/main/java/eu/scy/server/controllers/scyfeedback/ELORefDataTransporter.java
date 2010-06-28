package eu.scy.server.controllers.scyfeedback;

import eu.scy.core.model.ELORef;

import java.util.List;

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
}
