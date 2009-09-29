package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.pedagogicalplan.Scenario;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;
import eu.scy.core.model.pedagogicalplan.LearningGoal;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind Meistad
 * Date: 23.sep.2009
 * Time: 15:07:38
 */
public class ScenarioImpl extends BaseObjectImpl implements Scenario {
    private LearningActivitySpace learningActivitySpace = null;

    public LearningActivitySpace getLearningActivitySpace() {
        return learningActivitySpace;
    }

    public void setLearningActivitySpace(LearningActivitySpace learningActivitySpace) {
        this.learningActivitySpace = learningActivitySpace;
    }

    @Override
    public Set<LearningGoal> getLearningGoals() {
        return null;
    }

    @Override
    public void setLearningGoals(Set<LearningGoal> learningGoals) {

    }
}
