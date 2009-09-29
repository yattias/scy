package eu.scy.scymapper.impl.model;

import eu.scy.scymapper.api.diagram.INodeModel;
import eu.scy.scymapper.api.diagram.INodeModelListener;
import eu.scy.scymapper.api.shapes.INodeShape;
import eu.scy.scymapper.api.styling.INodeStyle;
import eu.scy.scymapper.impl.shapes.concepts.DefaultNodeShape;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 22.jun.2009
 * Time: 19:47:53
 */
public class NodeModel implements INodeModel {
    private INodeShape shape = new DefaultNodeShape();
    private Dimension size = new Dimension(150, 50);
    private Point location = new Point(20, 20);
    private ArrayList<INodeModelListener> listeners;
    private String label = "New concept";
    private INodeStyle style = new DefaultNodeStyle();
    private boolean labelHidden = false;
	private boolean selected = false;

	public NodeModel() {
        listeners = new ArrayList<INodeModelListener>();

    }
    public NodeModel(INodeShape shape) {
        this();
        this.shape = shape;
    }

	@Override
    public int getDistanceToPerimeter(Point p) {
        return (int)p.distance(getShape().getConnectionPoint(p, new Rectangle(getLocation(), getSize())));
    }

    @Override
    public Point getConnectionPoint(Point p) {
        return getShape().getConnectionPoint(p,  new Rectangle(getLocation(), getSize()));
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public void setShape(INodeShape shape) {
        this.shape = shape;
        notifyShapeChanged();
    }

    @Override
    public void setStyle(INodeStyle style) {
        this.style = style;
    }

	@Override
	public void setSelected(boolean b) {
		selected = b;
		notifySelected();
	}

	@Override
    public INodeStyle getStyle() {
        if (style == null) style = new DefaultNodeStyle(); 
        return style;
    }

    @Override
    public boolean isLabelHidden() {
        return labelHidden;
    }

    @Override
    public void setLabelHidden(boolean labelHidden) {
        this.labelHidden = labelHidden;
    }


    @Override
    public INodeShape getShape() {
        return shape;
    }

    @Override
    public Dimension getSize() {
        return this.size;
    }

    @Override
    public void setSize(Dimension size) {
        this.size = size;
        notifyResized();
    }

    @Override
    public void setLocation(Point location) {
        this.location = location;
        notifyMoved();
    }

    @Override
    public Point getLocation() {
        return this.location;
    }

    @Override
    public Point getCenterLocation() {
        return (new Point(getLocation().x + (getSize().width / 2), getLocation().y + (getSize().height / 2)));
    }

    @Override
    public void addListener(INodeModelListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(INodeModelListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void notifyMoved() {
        for (INodeModelListener listener : listeners) {
            listener.moved(this);
        }
    }

    @Override
    public void notifyResized() {
        for (INodeModelListener listener : listeners) {
            listener.resized(this);
        }
    }

    @Override
    public void notifyLabelChanged() {
        for (INodeModelListener listener : listeners) {
            listener.labelChanged(this);
        }
    }
    @Override
    public void notifyShapeChanged() {
        for (INodeModelListener listener : listeners) {
            listener.shapeChanged(this);
        }
    }
	@Override
	public void notifySelected() {
		for (INodeModelListener listener : listeners) {
            listener.nodeSelected(this);
        }
	}

	@Override
	public boolean isSelected() {
		return selected;
	}

	public void setLabel(String label) {
        this.label = label;
        notifyLabelChanged();
    }

    @Override
    public String toString() {
        return "NodeModel{" +
                "label='" + label + '\'' +
                '}';
    }
}
