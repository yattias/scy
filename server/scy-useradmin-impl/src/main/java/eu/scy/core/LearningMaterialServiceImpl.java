package eu.scy.core;

import eu.scy.core.model.pedagogicalplan.LearningMaterial;
import eu.scy.core.persistence.LearningMaterialDAO;
import eu.scy.core.persistence.SCYBaseDAO;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 03.mai.2010
 * Time: 09:42:07
 * To change this template use File | Settings | File Templates.
 */
public class LearningMaterialServiceImpl extends BaseServiceImpl implements LearningMaterialService {

    private LearningMaterialDAO learningMaterialDAO;

    public LearningMaterialDAO getLearningMaterialDAO() {
        return learningMaterialDAO;
    }

    public void setLearningMaterialDAO(LearningMaterialDAO learningMaterialDAO) {
        setScyBaseDAO((SCYBaseDAO) learningMaterialDAO);
        this.learningMaterialDAO = learningMaterialDAO;
    }

    @Override
    @Transactional
    public LearningMaterial getLearningMaterial(String id) {
        return learningMaterialDAO.getLearningMaterial(id);
    }

    @Override
    @Transactional
    public void delete(LearningMaterial learningMaterial) {
        learningMaterialDAO.delete(learningMaterial);
    }
}
