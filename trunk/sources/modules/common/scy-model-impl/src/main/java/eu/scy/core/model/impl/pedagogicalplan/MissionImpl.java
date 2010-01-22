package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.pedagogicalplan.LearningGoal;
import eu.scy.core.model.pedagogicalplan.Mission;
import eu.scy.core.model.pedagogicalplan.LearningMaterial;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 15.sep.2009
 * Time: 14:21:50
 */

@Entity
@Table(name = "mission")
public class MissionImpl extends LearningGoalContainerImpl implements Mission {



    private List<LearningGoal> learningGoals;

    @Transient
    public Set<? extends LearningMaterial> getLearningMaterials() {
        return null;
    }

    @Override
    public void setLearningMaterials(Set<? extends LearningMaterial> learningMaterials) {

    }

    @Override
    public void setMissionOutline(String missionOutline) {

    }

    @Transient
    public String getMissionOutline() {
        return null;
    }

    @Override
    public void setTargetGroup(String targetGroup) {

    }

    @Transient
    public String getTargetGroup() {
        return null;
    }

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "learningGoalContainer", targetEntity = LearningGoalImpl.class, fetch = FetchType.EAGER)
    public List<LearningGoal> getLearningGoals() {
        return learningGoals;
    }

     public void setLearningGoals(List<LearningGoal> learningGoals) {
        this.learningGoals = learningGoals;
    }

    public void addLearningGoal(LearningGoal learningGoal) {
        learningGoals.add(learningGoal);
    }

    public void removeLearningGoal(LearningGoal learningGoal) {
        learningGoals.remove(learningGoal);
    }
}
