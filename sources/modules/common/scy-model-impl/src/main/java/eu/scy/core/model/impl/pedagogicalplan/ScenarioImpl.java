package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.pedagogicalplan.Scenario;
import eu.scy.core.model.pedagogicalplan.LearningActivitySpace;
import eu.scy.core.model.pedagogicalplan.LearningGoal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
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

    @OneToOne(targetEntity = LearningActivitySpaceImpl.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="learningActivitySpace_primKey")
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
