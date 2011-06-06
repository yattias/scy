package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.AssessmentCriteria;
import eu.scy.core.model.pedagogicalplan.Assessment;
import eu.scy.core.model.pedagogicalplan.BaseObject;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 12.jul.2010
 * Time: 05:51:25
 */
@Entity
@Table(name = "assessmentcriteria")
public class AssessmentCriteriaImpl extends BaseObjectImpl implements AssessmentCriteria{

    private Assessment assessment;

    private String criteria;


    @OneToOne(targetEntity = AssessmentImpl.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="assessment_primKey")
    @Override
    public Assessment getAssessment() {
        return assessment;
    }

    @Override
    public void setAssessment(Assessment assessment) {
        this.assessment = assessment;
    }

    @Override
    public String getCriteria() {
        return criteria;
    }

    @Override
    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }
}
