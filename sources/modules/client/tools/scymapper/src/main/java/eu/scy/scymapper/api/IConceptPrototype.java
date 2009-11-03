package eu.scy.scymapper.api;

import eu.scy.scymapper.api.shapes.INodeShape;
import eu.scy.scymapper.api.styling.INodeStyle;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Bjoerge
 * Date: 30.okt.2009
 * Time: 13:07:12
 */
public interface IConceptPrototype {
    INodeShape getNodeShape();
    void setNodeShape(INodeShape shape);

    void setNodeStyle(INodeStyle nodeStyle);

    INodeStyle getNodeStyle();

    String getName();
    void setName(String name);

    String getDescription();
    void setDescription(String description);

    int getHeight();

    void setHeight(int height);

    int getWidth();

    void setWidth(int width);

}
