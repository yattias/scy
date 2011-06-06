package eu.scy.core.model.pedagogicalplan;

import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.sep.2009
 * Time: 11:18:09
 */
public interface Mission extends LearningGoalContainer{

    public List getLearningMaterials();
    public void setLearningMaterials(List learningMaterials);

    public void setMissionOutline(String missionOutline);
    public String getMissionOutline();

    public void setTargetGroup(String targetGroup); 
    public String getTargetGroup();

    void addLearningMaterial(LearningMaterial learningMaterial);
    public void removeLearningMaterial(LearningMaterial learningMaterial);
}