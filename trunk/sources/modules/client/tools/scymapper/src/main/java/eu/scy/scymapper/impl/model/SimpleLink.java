package eu.scy.scymapper.impl.model;

import eu.scy.scymapper.api.diagram.ILinkModel;
import eu.scy.scymapper.api.diagram.ILinkModelListener;
import eu.scy.scymapper.api.shapes.ILinkShape;
import eu.scy.scymapper.api.styling.ILinkStyle;
import eu.scy.scymapper.api.styling.ILinkStyleObserver;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 23.jun.2009
 * Time: 15:46:06
 */
public class SimpleLink implements ILinkModel, ILinkStyleObserver {
    private Point from;
    private Point to;
    private String label;
    private ILinkShape shape;
    private ArrayList<ILinkModelListener> listeners;
    private ILinkStyle style;
    private boolean labelHidden = false;

	public SimpleLink() {
        listeners = new ArrayList<ILinkModelListener>();
    }

    public SimpleLink(ILinkShape shape) {
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
    public ILinkShape getShape() {
        return shape;
    }

    @Override
    public void setShape(ILinkShape shape) {
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
    public void addListener(ILinkModelListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(ILinkModelListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void notifyUpdated() {
        for (ILinkModelListener listener : listeners) {
            listener.updated(this);
        }
    }

    @Override
    public void styleChanged(ILinkStyle s) {
        for (ILinkModelListener listener : listeners) {
            listener.updated(this);
        }
    }

    @Override
    public boolean isLabelHidden() {
        return labelHidden;
    }

    @Override
    public void setLabelHidden(boolean labelHidden) {
        this.labelHidden = labelHidden;
    }
}
