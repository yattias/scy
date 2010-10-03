package eu.scy.core.model.pedagogicalplan;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.sep.2009
 * Time: 12:13:27
 * Superinterface for all types of learning materials
 */
public interface LearningMaterial extends BaseObject {
    LearningMaterialContainer getLearningMaterialContainer();

    void setLearningMaterialContainer(LearningMaterialContainer learningMaterialContainer);

    String getIcon();

    void setIcon(String favico);
}
