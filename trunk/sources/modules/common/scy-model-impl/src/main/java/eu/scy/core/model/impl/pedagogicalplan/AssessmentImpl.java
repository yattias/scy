package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.pedagogicalplan.Assessment;
import eu.scy.core.model.pedagogicalplan.AssessmentStrategy;
import eu.scy.core.model.pedagogicalplan.BaseObject;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind Meistad
 * Date: 28.sep.2009
 * Time: 16:08:14
 */
public class AssessmentImpl extends BaseObjectImpl implements Assessment {
    private AssessmentStrategy assessmentStrategy = null;

    public AssessmentStrategy getAssessmentStrategy() {
        return assessmentStrategy;
    }

    public void setAssessmentStrategy(AssessmentStrategy assessmentStrategy) {
        this.assessmentStrategy = assessmentStrategy;
    }
}
