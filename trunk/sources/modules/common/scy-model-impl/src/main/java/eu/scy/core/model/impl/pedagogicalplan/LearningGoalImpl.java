package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.pedagogicalplan.LearningGoal;
import eu.scy.core.model.pedagogicalplan.LearningGoalContainer;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind Meistad
 * Date: 23.sep.2009
 * Time: 15:09:52
 */

@Entity
@Table(name="learninggoal")
public class LearningGoalImpl extends BaseObjectImpl implements LearningGoal {

    private LearningGoalContainer learningGoalContainer;

    //ManyToOne(targetEntity = LearningGoalContainer.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    //JoinColumn(name = "learningGoalContainer_primKey")
    public LearningGoalContainer getLearningGoalContainer() {
        return learningGoalContainer;
    }

    public void setLearningGoalContainer(LearningGoalContainer learningGoalContainer) {
        this.learningGoalContainer = learningGoalContainer;
    }
}