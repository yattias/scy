package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.pedagogicalplan.LearningMaterial;
import eu.scy.core.model.pedagogicalplan.LearningMaterialContainer;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 03.mai.2010
 * Time: 09:28:03
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name="learningmaterial")
@Inheritance( strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "materialtype")
public class LearningMaterialImpl extends BaseObjectImpl implements LearningMaterial {

    private LearningMaterialContainer learningMaterialContainer;
    private String icon;

    @Override
    @ManyToOne(targetEntity = MissionImpl.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_primKey")
    public LearningMaterialContainer getLearningMaterialContainer() {
        return learningMaterialContainer;
    }

    @Override
    public void setLearningMaterialContainer(LearningMaterialContainer learningMaterialContainer) {
        this.learningMaterialContainer = learningMaterialContainer;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
