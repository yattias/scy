package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.pedagogicalplan.BaseObject;
import eu.scy.core.model.pedagogicalplan.LearningGoalContainer;
import eu.scy.core.model.pedagogicalplan.LearningGoal;

import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind Meistad
 * Date: 23.sep.2009
 * Time: 15:08:34
 */
public class LearningGoalContainerImpl extends BaseObjectImpl implements LearningGoalContainer {
    private Set<LearningGoal> learningGoals = new HashSet<LearningGoal>();

    public Set<LearningGoal> getLearningGoals() {
        return learningGoals;
    }

    public void setLearningGoals(Set<LearningGoal> learningGoals) {
        this.learningGoals = learningGoals;
    }

    public void addLearningGoal(LearningGoal learningGoal) {
        learningGoals.add(learningGoal);
    }

    public void removeLearningGoal(LearningGoal learningGoal) {
        learningGoals.remove(learningGoal);
    }
}
