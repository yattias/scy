package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.AssessmentCriteria;
import eu.scy.core.model.pedagogicalplan.Assessment;
import eu.scy.core.model.pedagogicalplan.AssessmentScoreDefinition;
import eu.scy.core.model.pedagogicalplan.AssessmentStrategy;
import eu.scy.core.model.pedagogicalplan.AssessmentStrategyType;

import javax.persistence.*;
import java.util.List;

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
    private List<AssessmentCriteria> assessmentCriterias;
    private List<AssessmentScoreDefinition> assessmentScoreDefinitions;


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

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "assessment", targetEntity = AssessmentCriteriaImpl.class, fetch = FetchType.LAZY)
    public List<AssessmentCriteria> getAssessmentCriterias() {
        return assessmentCriterias;
    }

    public void setAssessmentCriterias(List<AssessmentCriteria> assessmentCriterias) {
        this.assessmentCriterias = assessmentCriterias;
    }

    @Override
    public void addCriteria(AssessmentCriteria assessmentCriteria) {
        assessmentCriterias.add(assessmentCriteria);
        assessmentCriteria.setAssessment(this);
    }

    @Override
    @OneToMany(mappedBy = "assessment", targetEntity = AssessmentScoreDefinitionImpl.class)
    public List<AssessmentScoreDefinition> getAssessmentScoreDefinitions() {
        return assessmentScoreDefinitions;
    }

    @Override
    public void setAssessmentScoreDefinitions(List<AssessmentScoreDefinition> assessmentScoreDefinitions) {
        this.assessmentScoreDefinitions = assessmentScoreDefinitions;
    }

    @Override
    public void addAssessmentScoreDefinition(AssessmentScoreDefinition assessmentScoreDefinition) {
        assessmentScoreDefinition.setAssessment(this);
        assessmentScoreDefinitions.add(assessmentScoreDefinition);
    }
}
