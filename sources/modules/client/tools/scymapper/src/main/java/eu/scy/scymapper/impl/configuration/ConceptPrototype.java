package eu.scy.scymapper.impl.configuration;

import eu.scy.scymapper.api.IConceptPrototype;
import eu.scy.scymapper.api.shapes.INodeShape;
import eu.scy.scymapper.api.styling.INodeStyle;
import eu.scy.scymapper.impl.model.DefaultNodeStyle;

/**
 * Created by IntelliJ IDEA.
 * User: Bjoerge
 * Date: 30.okt.2009
 * Time: 13:25:49
 */
public class ConceptPrototype implements IConceptPrototype {
    private INodeShape shape;
    private String name;
    private String description;
    private int height = 100;
    private int width = 100;
    private INodeStyle nodeStyle = new DefaultNodeStyle();

    @Override
    public INodeShape getNodeShape() {
        return shape;
    }

    @Override
    public void setNodeShape(INodeShape shape) {
        this.shape = shape;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public void setNodeStyle(INodeStyle nodeStyle) {
        this.nodeStyle = nodeStyle;
    }

    @Override
    public INodeStyle getNodeStyle() {
        return nodeStyle;
    }
}
