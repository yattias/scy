package eu.scy.scymapper.impl.configuration;

import eu.scy.scymapper.api.ILinkType;
import eu.scy.scymapper.api.shapes.ILinkShape;

/**
 * Created by IntelliJ IDEA.
 * User: Bjoerge
 * Date: 30.okt.2009
 * Time: 13:25:41
 */
public class LinkPrototype implements ILinkType {
    private ILinkShape shape;
    private String label;
    private String description;

    @Override
    public ILinkShape getLinkShape() {
        return shape;
    }

    @Override
    public void setLinkShape(ILinkShape shape) {
        this.shape = shape;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }
}
