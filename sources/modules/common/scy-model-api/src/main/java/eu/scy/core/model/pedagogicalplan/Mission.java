package eu.scy.core.model.pedagogicalplan;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.sep.2009
 * Time: 11:18:09
 * To change this template use File | Settings | File Templates.
 */
public interface Mission extends LearningGoalContainer{

    public Set <? extends LearningMaterial> getLearningMaterials();
    public void setLearningMaterials(Set <? extends LearningMaterial> learningMaterials);

    public void setMissionOutline(String missionOutline);
    public String getMissionOutline();

    public void setTargetGroup(String targetGroup); 
    public String getTargetGroup();
}