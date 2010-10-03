package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.impl.pedagogicalplan.LearningMaterialImpl;
import eu.scy.core.model.pedagogicalplan.LearningMaterial;
import eu.scy.core.persistence.LearningMaterialDAO;
import eu.scy.core.persistence.hibernate.ScyBaseDAOHibernate;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 03.mai.2010
 * Time: 09:42:49
 * To change this template use File | Settings | File Templates.
 */
public class LearningMaterialDAOHibernate extends ScyBaseDAOHibernate implements LearningMaterialDAO {
    @Override
    public LearningMaterial getLearningMaterial(String id) {
        return (LearningMaterial) getHibernateTemplate().get(LearningMaterialImpl.class, id);
    }

    @Override
    public void delete(LearningMaterial learningMaterial) {
        getHibernateTemplate().delete(learningMaterial);
    }
}
