package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.pedagogicalplan.Mission;
import eu.scy.core.model.pedagogicalplan.LearningMaterial;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
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
}
