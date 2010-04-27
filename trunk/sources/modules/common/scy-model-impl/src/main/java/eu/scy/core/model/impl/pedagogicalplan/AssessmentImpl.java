package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.pedagogicalplan.Assessment;
import eu.scy.core.model.pedagogicalplan.AssessmentStrategy;
import eu.scy.core.model.pedagogicalplan.AssessmentStrategyType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind Meistad
 * Date: 28.sep.2009
 * Time: 16:08:14
 */

@Entity
@Table(name="assessment")
public class AssessmentImpl extends BaseObjectImpl implements Assessment {
    private AssessmentStrategy assessmentStrategy = null;
    private AssessmentStrategyType assessmentStrategyType = AssessmentStrategyType.SINGLE;

    @OneToOne(targetEntity = AssessmentStrategyImpl.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="assessmentStrategy_primKey")
    public AssessmentStrategy getAssessmentStrategy() {
        return assessmentStrategy;
    }

    public void setAssessmentStrategy(AssessmentStrategy assessmentStrategy) {
        this.assessmentStrategy = assessmentStrategy;
    }    

    @Enumerated(EnumType.STRING)
    public AssessmentStrategyType getAssessmentStrategyType() {
        return assessmentStrategyType;
}

    public void setAssessmentStrategyType(AssessmentStrategyType assessmentStrategyType) {
        this.assessmentStrategyType = assessmentStrategyType;
    }
}
