package eu.scy.scymapper.api;

import eu.scy.scymapper.api.shapes.ILinkShape;

/**
 * Created by IntelliJ IDEA.
 * @author Bjoerge Naess
 * Date: 30.okt.2009
 * Time: 13:07:28
 */
public interface ILinkType {

    ILinkShape getLinkShape();
    void setLinkShape(ILinkShape shape);

    String getLabel();
    void setLabel(String label);

    String getDescription();
    void setDescription(String description);
}
