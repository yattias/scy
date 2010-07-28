package eu.scy.core.model.impl.pedagogicalplan;

import eu.scy.core.model.FileRef;
import eu.scy.core.model.ImageRef;
import eu.scy.core.model.impl.ImageRefImpl;
import eu.scy.core.model.pedagogicalplan.LearningMaterial;
import eu.scy.core.model.pedagogicalplan.LearningMaterialImage;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 03.mai.2010
 * Time: 14:11:13
 */
@Entity
@DiscriminatorValue("learningmaterialimage")
public class LearningMaterialImageImpl extends LearningMaterialImpl implements LearningMaterialImage {

    private ImageRef image;

    @Override
    @OneToOne(targetEntity = ImageRefImpl.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "image_primKey")
    public ImageRef getImage() {
        return image;
    }

    @Override
    public void setImage(ImageRef image) {
        this.image = image;
    }

    @Transient
    public String getUrl(){
        return "";
    }

}
