package eu.scy.colemo.client.ui.impl.model;

import eu.scy.colemo.client.ui.api.links.ILink;
import eu.scy.colemo.client.ui.api.links.ILinkObservable;
import eu.scy.colemo.client.ui.api.links.ILinkObserver;
import eu.scy.colemo.client.ui.api.styling.ILinkStyle;
import eu.scy.colemo.client.ui.api.styling.ILinkStyleObserver;
import eu.scy.colemo.client.shapes.LinkShape;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Bjørge Næss
 * Date: 23.jun.2009
 * Time: 15:46:06
 */
public class SimpleLink implements ILink, ILinkObservable, ILinkStyleObserver {
    private Point from;
    private Point to;
    private String label;
    private LinkShape shape;
    private ArrayList<ILinkObserver> observers;
    private ILinkStyle style;

    public SimpleLink() {
        observers = new ArrayList<ILinkObserver>(); 
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
    public void addObserver(ILinkObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(ILinkObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyUpdated() {
        for (ILinkObserver observer : observers) {
            observer.updated(this);
        }
    }

    @Override
    public void styleChanged(ILinkStyle s) {
        for (ILinkObserver observer : observers) {
            observer.updated(this);
        }
    }
}
