package eu.scy.scymapper.impl.configuration;

import eu.scy.scymapper.api.INodeFactory;
import eu.scy.scymapper.api.diagram.model.INodeModel;
import eu.scy.scymapper.api.shapes.INodeShape;
import eu.scy.scymapper.api.styling.INodeStyle;
import eu.scy.scymapper.impl.model.DefaultNodeStyle;
import eu.scy.scymapper.impl.model.NodeModel;
import eu.scy.scymapper.impl.ui.palette.ConceptShapedIcon;
import eu.scy.scymapper.impl.ui.palette.PalettePane;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA. User: Bjoerge Date: 30.okt.2009 Time: 13:25:49
 */
public class DefaultNodeFactory implements INodeFactory {

    private INodeShape shape;

    private String name;

    private String description;

    private int height = 100;

    private int width = 100;

    private Icon icon;

    private INodeStyle nodeStyle = new DefaultNodeStyle();

    private final static String ICON_LOCATION = "/eu/scy/scymapper/impl/ui/toolbar/";

    public INodeShape getNodeShape() {
        return shape;
    }

    public void setNodeShape(INodeShape shape) {
        this.shape = shape;
    }

    @Override
    public Icon getIcon() {
        if (icon == null) {
            if (name.equals("hexagon")) {
                icon = PalettePane.getImageIcon(DefaultNodeFactory.class.getResource(ICON_LOCATION + "hexagon.png"));
            } else if (name.equals("roundRectangle")) {
                icon = PalettePane.getImageIcon(DefaultNodeFactory.class.getResource(ICON_LOCATION + "roundrectangle.png"));
            } else if (name.equals("ellipse")) {
                icon = PalettePane.getImageIcon(DefaultNodeFactory.class.getResource(ICON_LOCATION + "ellipse.png"));
            } else {
                icon = new ConceptShapedIcon(create(), 22, 22);
            }
        }
        return icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setNodeStyle(INodeStyle nodeFactory) {
        this.nodeStyle = nodeFactory;
    }

    public INodeStyle getNodeStyle() {
        return nodeStyle;
    }

    @Override
    public INodeModel create() {
        NodeModel node = new NodeModel();
        node.setWidth(width);
        node.setHeight(height);
        node.setShape(shape);

        INodeStyle style = new DefaultNodeStyle();
        style.setBackground(nodeStyle.getBackground());
        style.setForeground(nodeStyle.getForeground());
        style.setMinHeight(nodeStyle.getMinHeight());
        style.setMinWidth(nodeStyle.getMinWidth());
        style.setOpaque(nodeStyle.isOpaque());
        style.setStroke(nodeStyle.getStroke());
        style.setBorder(nodeStyle.getBorder());

        node.setStyle(style);

        return node;
    }
}
