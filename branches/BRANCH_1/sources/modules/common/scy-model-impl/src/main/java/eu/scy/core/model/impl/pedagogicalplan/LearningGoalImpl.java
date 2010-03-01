package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.pedagogicalplan.LearningGoal;
import eu.scy.core.model.pedagogicalplan.LearningGoalContainer;
import eu.scy.core.model.pedagogicalplan.Mission;

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

    private Mission learningGoalContainer;

    @Override
    @ManyToOne(targetEntity = MissionImpl.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "mission_primKey")
    public Mission getLearningGoalContainer() {
        return learningGoalContainer;
    }

    @Override
    public void setLearningGoalContainer(Mission learningGoalContainer) {
        this.learningGoalContainer = learningGoalContainer;
    }
}