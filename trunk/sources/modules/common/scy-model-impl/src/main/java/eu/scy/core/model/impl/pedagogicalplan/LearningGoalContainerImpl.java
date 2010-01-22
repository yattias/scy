package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.pedagogicalplan.BaseObject;
import eu.scy.core.model.pedagogicalplan.LearningGoalContainer;
import eu.scy.core.model.pedagogicalplan.LearningGoal;

import javax.persistence.*;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind Meistad
 * Date: 23.sep.2009
 * Time: 15:08:34
 */
@Entity
public abstract class LearningGoalContainerImpl extends BaseObjectImpl implements LearningGoalContainer {

    private Set<LearningGoal> learningGoals = new HashSet<LearningGoal>();

    //OneToMany(cascade = {CascadeType.ALL}, mappedBy = "learningGoalContainer", targetEntity = LearningGoalImpl.class, fetch = FetchType.EAGER)
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
