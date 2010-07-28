package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.AssessmentCriteria;
import eu.scy.core.model.User;
import eu.scy.core.model.impl.SCYUserImpl;
import eu.scy.core.model.pedagogicalplan.AssessmentCriteriaExperience;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.jul.2010
 * Time: 07:36:41
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "assessmentcriteriaexperience")
public class AssessmentCriteriaExperienceImpl extends BaseObjectImpl implements AssessmentCriteriaExperience {

    private Integer score;
    private AssessmentCriteria assessmentCriteria;
    private User user;
    private String criteriaText;
    private String comment;

    @Override
    public Integer getScore() {
        return score;
    }

    @Override
    public void setScore(Integer score) {
        this.score = score;
    }

    @Override
    @ManyToOne(targetEntity = AssessmentCriteriaImpl.class)
    @JoinColumn(name = "assessmentCriteria_primKey")
    public AssessmentCriteria getAssessmentCriteria() {
        return assessmentCriteria;
    }

    @Override
    public void setAssessmentCriteria(AssessmentCriteria assessmentCriteria) {
        this.assessmentCriteria = assessmentCriteria;
    }

    @Override
    @ManyToOne(targetEntity = SCYUserImpl.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_primKey")
    public User getUser() {
        return user;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String getCriteriaText() {
        return criteriaText;
    }

    @Override
    public void setCriteriaText(String criteriaText) {
        this.criteriaText = criteriaText;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }
}
