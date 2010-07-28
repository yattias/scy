package eu.scy.core.model.pedagogicalplan;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 03.mai.2010
 * Time: 09:58:38
 * To change this template use File | Settings | File Templates.
 */
public interface LearningMaterialContainer extends BaseObject {

    public List getLearningMaterials();

    public void setLearningMaterials(List learningMaterials);

    public void addLearningMaterial(LearningMaterial learningMaterial);


}
