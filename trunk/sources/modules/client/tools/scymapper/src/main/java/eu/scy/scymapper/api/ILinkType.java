package eu.scy.scymapper.api;

import eu.scy.scymapper.api.shapes.ILinkShape;
import eu.scy.scymapper.api.shapes.INodeShape;

/**
 * Created by IntelliJ IDEA.
 * User: Bjørge
 * Date: 30.okt.2009
 * Time: 13:07:28
 * To change this template use File | Settings | File Templates.
 */
public interface ILinkType {

    ILinkShape getLinkShape();
    void setLinkShape(ILinkShape shape);

    String getLabel();
    void setLabel(String label);

    String getDescription();
    void setDescription(String description);
}
