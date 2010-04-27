package eu.scy.core.model.pedagogicalplan;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.sep.2009
 * Time: 14:34:08
 */
public interface Assessment extends BaseObject {
    public void setAssessmentStrategy(AssessmentStrategy assessmentStrategy);
    public AssessmentStrategy getAssessmentStrategy();

    public void setAssessmentStrategyType(AssessmentStrategyType assessmentStrategyType);
    public AssessmentStrategyType getAssessmentStrategyType();
}