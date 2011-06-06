package eu.scy.core;

import eu.scy.core.model.pedagogicalplan.LearningMaterial;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 03.mai.2010
 * Time: 09:41:11
 * To change this template use File | Settings | File Templates.
 */
public interface LearningMaterialService extends BaseService{
    LearningMaterial getLearningMaterial(String parameter);

    void delete(LearningMaterial learningMaterial);
}
