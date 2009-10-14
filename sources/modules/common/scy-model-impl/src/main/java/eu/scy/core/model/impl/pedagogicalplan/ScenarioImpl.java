package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.pedagogicalplan.Scenario;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;
import eu.scy.core.model.pedagogicalplan.LearningGoal;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind Meistad
 * Date: 23.sep.2009
 * Time: 15:07:38
 */

@Entity
@Table(name="scenario")
public class ScenarioImpl extends BaseObjectImpl implements Scenario {

    private LearningActivitySpace learningActivitySpace = null;

    private Set<LearningGoal> learningGoals;

    @Transient
    public LearningActivitySpace getLearningActivitySpace() {
        return learningActivitySpace;
    }

    public void setLearningActivitySpace(LearningActivitySpace learningActivitySpace) {
        this.learningActivitySpace = learningActivitySpace;
    }

    @Transient
    public Set<LearningGoal> getLearningGoals() {
        return this.learningGoals;
    }


    public void setLearningGoals(Set<LearningGoal> learningGoals) {
        this.learningGoals = learningGoals;

    }

    public void addLearningGoal(LearningGoal learningGoal) {
        getLearningGoals().add(learningGoal);

    }

    public void removeLearningGoal(LearningGoal learningGoal) {
        getLearningGoals().remove(learningGoal);

    }
}
