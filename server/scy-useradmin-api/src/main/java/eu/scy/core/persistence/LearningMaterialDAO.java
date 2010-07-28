package eu.scy.core.persistence;

import eu.scy.core.model.pedagogicalplan.LearningMaterial;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 03.mai.2010
 * Time: 09:41:31
 * To change this template use File | Settings | File Templates.
 */
public interface LearningMaterialDAO extends BaseDAO{
    LearningMaterial getLearningMaterial(String id);

    void delete(LearningMaterial learningMaterial);
}
