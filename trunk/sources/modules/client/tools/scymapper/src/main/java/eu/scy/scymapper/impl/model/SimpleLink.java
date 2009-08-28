package eu.scy.scymapper.impl.model;

import eu.scy.scymapper.api.diagram.ILinkModel;
import eu.scy.scymapper.api.diagram.ILinkModelObservable;
import eu.scy.scymapper.api.diagram.ILinkModelObserver;
import eu.scy.scymapper.api.styling.ILinkStyle;
import eu.scy.scymapper.api.styling.ILinkStyleObserver;
import eu.scy.scymapper.impl.shapes.LinkShape;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 23.jun.2009
 * Time: 15:46:06
 */
public class SimpleLink implements ILinkModel, ILinkModelObservable, ILinkStyleObserver {
    private Point from;
    private Point to;
    private String label;
    private LinkShape shape;
    private ArrayList<ILinkModelObserver> observers;
    private ILinkStyle style;

    public SimpleLink() {
        observers = new ArrayList<ILinkModelObserver>();
    }

    public SimpleLink(LinkShape shape) {
        this();
        this.shape = shape;
    }

	@Override
    public String getLabel() {
        return label;
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
        notifyUpdated();
    }

    @Override
    public Point getFrom() {
        return from;
    }

    @Override
    public void setFrom(Point p) {
        this.from = p;
        notifyUpdated();
    }

    @Override
    public Point getTo() {
        return to;
    }

    @Override
    public void setTo(Point p) {
        this.to = p;
        notifyUpdated();
    }

    @Override
    public LinkShape getShape() {
        return shape;
    }

    @Override
    public void setShape(LinkShape shape) {
        this.shape = shape;
    }

    @Override
    public void setStyle(ILinkStyle style) {
        this.style = style;
    }

    @Override
    public ILinkStyle getStyle() {
        if (this.style == null) this.style = new DefaultLinkStyle();
        return this.style;
    }

    @Override
    public void addObserver(ILinkModelObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(ILinkModelObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyUpdated() {
        for (ILinkModelObserver observer : observers) {
            observer.updated(this);
        }
    }

    @Override
    public void styleChanged(ILinkStyle s) {
        for (ILinkModelObserver observer : observers) {
            observer.updated(this);
        }
    }
}
