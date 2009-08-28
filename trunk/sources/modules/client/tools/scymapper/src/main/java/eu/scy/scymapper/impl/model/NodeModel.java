package eu.scy.scymapper.impl.model;

import eu.scy.scymapper.impl.shapes.INodeShape;
import eu.scy.scymapper.api.diagram.INodeModelObserver;
import eu.scy.scymapper.api.diagram.INodeModel;
import eu.scy.scymapper.api.styling.INodeStyle;
import eu.scy.scymapper.api.styling.INodeStyleObserver;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 22.jun.2009
 * Time: 19:47:53
 */
public class NodeModel implements INodeModel, INodeStyleObserver {
    private INodeShape shape;
    private Dimension size;
    private Point location;
    private ArrayList<INodeModelObserver> observers;
    private String label;
    private INodeStyle style;
    private boolean selected = false;

    public NodeModel() {
        observers = new ArrayList<INodeModelObserver>();

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
        if (!this.style.equals(style)) notifyStyleChanged();
        else if (!style.hasObserver((this))) style.addObserver(this);
    }

    @Override
    public INodeStyle getStyle() {
        if (style == null) style = new DefaultNodeStyle(); 
        return style;
    }

    @Override
    public void setSelected(boolean b) {
        this.selected = true;
        notifySelected();
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
    public void addObserver(INodeModelObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(INodeModelObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyMoved() {
        for (INodeModelObserver observer : observers) {
            observer.moved(this);
        }
    }

    @Override
    public void notifyResized() {
        for (INodeModelObserver observer : observers) {
            observer.resized(this);
        }
    }

    @Override
    public void notifyLabelChanged() {
        for (INodeModelObserver observer : observers) {
            observer.labelChanged(this);
        }
    }
    @Override
    public void notifyStyleChanged() {
        for (INodeModelObserver observer : observers) {
            observer.styleChanged(this);
        }
    }

    @Override
    public void notifyShapeChanged() {
        for (INodeModelObserver observer : observers) {
            observer.shapeChanged(this);
        }
    }
    @Override
    public void notifySelected() {
        for (INodeModelObserver observer : observers) {
            observer.nodeSelected(this);
        }
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

    @Override
    public void styleChanged(INodeStyle s) {
        notifyStyleChanged();
    }
}
