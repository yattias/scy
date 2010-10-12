package eu.scy.core.model.pedagogicalplan;

import eu.scy.core.model.ImageRef;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.sep.2009
 * Time: 12:25:53
 * represents an image/picture/illustration
 */
public interface LearningMaterialImage extends LearningMaterial{
    ImageRef getImage();

    void setImage(ImageRef image);
}