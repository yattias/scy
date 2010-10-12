package eu.scy.core.model.pedagogicalplan;

import eu.scy.core.model.AssessmentCriteria;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.sep.2009
 * Time: 14:34:08
 */
public interface Assessment extends BaseObject {

    public void setAssessmentCriterias(List <AssessmentCriteria> assessmentCriterias);
    public List <AssessmentCriteria> getAssessmentCriterias();

    public void setAssessmentStrategy(AssessmentStrategy assessmentStrategy);
    public AssessmentStrategy getAssessmentStrategy();

    public void setAssessmentStrategyType(AssessmentStrategyType assessmentStrategyType);
    public AssessmentStrategyType getAssessmentStrategyType();

    void addCriteria(AssessmentCriteria assessmentCriteria);

    List<AssessmentScoreDefinition> getAssessmentScoreDefinitions();
    void setAssessmentScoreDefinitions(List<AssessmentScoreDefinition> assessmentScoreDefinitions);

    void addAssessmentScoreDefinition(AssessmentScoreDefinition assessmentScoreDefinition);
}