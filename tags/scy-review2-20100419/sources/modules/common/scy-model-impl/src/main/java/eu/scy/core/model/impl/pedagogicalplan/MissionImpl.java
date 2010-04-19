package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.pedagogicalplan.LearningGoal;
import eu.scy.core.model.pedagogicalplan.Mission;
import eu.scy.core.model.pedagogicalplan.LearningMaterial;

import javax.persistence.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 15.sep.2009
 * Time: 14:21:50
 */

@Entity
@Table(name = "mission")
public class MissionImpl extends LearningGoalContainerImpl implements Mission {

    private String missionOutline = null;
    private String targetGroup = null;

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
        this.missionOutline = missionOutline;
    }

    public String getMissionOutline() {
        return missionOutline;
    }

    @Override
    public void setTargetGroup(String targetGroup) {
        this.targetGroup = targetGroup;
    }

    public String getTargetGroup() {
        return targetGroup;
    }

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "learningGoalContainer", targetEntity = LearningGoalImpl.class, fetch = FetchType.LAZY)
    public List<LearningGoal> getLearningGoals() {
        if(learningGoals == null) learningGoals = new LinkedList();
        return learningGoals;
    }

     public void setLearningGoals(List<LearningGoal> learningGoals) {
        this.learningGoals = learningGoals;
    }

    public void addLearningGoal(LearningGoal learningGoal) {
        getLearningGoals().add(learningGoal);
        learningGoal.setLearningGoalContainer(this);
    }

    public void removeLearningGoal(LearningGoal learningGoal) {
        getLearningGoals().remove(learningGoal);
    }
}
