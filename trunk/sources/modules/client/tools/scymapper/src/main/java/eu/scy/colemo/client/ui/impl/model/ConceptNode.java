package eu.scy.colemo.client.ui.impl.model;

import eu.scy.colemo.client.shapes.ConceptShape;
import eu.scy.colemo.client.ui.api.nodes.INodeObserver;
import eu.scy.colemo.client.ui.api.nodes.IConceptNode;
import eu.scy.colemo.client.ui.api.styling.INodeStyle;
import eu.scy.colemo.client.ui.api.styling.INodeStyleObserver;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 22.jun.2009
 * Time: 19:47:53
 */
public class ConceptNode implements IConceptNode, INodeStyleObserver {
    private ConceptShape shape;
    private Dimension size;
    private Point location;
    private ArrayList<INodeObserver> observers;
    private String label;
    private INodeStyle style;
    private boolean selected = false;

    public ConceptNode() {
        observers = new ArrayList<INodeObserver>();

    }
    public ConceptNode(ConceptShape shape) {
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
    public void setShape(ConceptShape shape) {
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
    public ConceptShape getShape() {
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
    public void addObserver(INodeObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(INodeObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyMoved() {
        for (INodeObserver observer : observers) {
            observer.moved(this);
        }
    }

    @Override
    public void notifyResized() {
        for (INodeObserver observer : observers) {
            observer.resized(this);
        }
    }

    @Override
    public void notifyLabelChanged() {
        for (INodeObserver observer : observers) {
            observer.labelChanged(this);
        }
    }
    @Override
    public void notifyStyleChanged() {
        for (INodeObserver observer : observers) {
            observer.styleChanged(this);
        }
    }

    @Override
    public void notifyShapeChanged() {
        for (INodeObserver observer : observers) {
            observer.shapeChanged(this);
        }
    }
    @Override
    public void notifySelected() {
        for (INodeObserver observer : observers) {
            observer.nodeSelected(this);
        }
    }
    public void setLabel(String label) {
        this.label = label;
        notifyLabelChanged();
    }

    @Override
    public String toString() {
        return "ConceptNode{" +
                "label='" + label + '\'' +
                '}';
    }

    @Override
    public void styleChanged(INodeStyle s) {
        notifyStyleChanged();
    }
}
