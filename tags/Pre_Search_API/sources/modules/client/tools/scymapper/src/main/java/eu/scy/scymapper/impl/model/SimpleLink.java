package eu.scy.scymapper.impl.model;

import eu.scy.scymapper.api.diagram.model.ILinkModel;
import eu.scy.scymapper.api.diagram.model.ILinkModelListener;
import eu.scy.scymapper.api.shapes.ILinkShape;
import eu.scy.scymapper.api.styling.ILinkStyle;
import eu.scy.scymapper.api.styling.ILinkStyleListener;

import java.awt.*;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 23.jun.2009
 * Time: 15:46:06
 */
public class SimpleLink implements ILinkModel, ILinkStyleListener {
	protected Point from;
	protected Point to;
	protected String label = "";
	private ILinkShape shape;

	private transient java.util.List<ILinkModelListener> listeners;

	private ILinkStyle style;
	private boolean labelHidden = false;
	private transient boolean selected;
	private String id = UUID.randomUUID().toString();

	private Object readResolve() {
		listeners = new ArrayList<ILinkModelListener>();
		return this;
	}

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
		notifyLabelChanged();
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
		notifyUpdated();
	}

	@Override
	public void setStyle(ILinkStyle style) {
		this.style = style;
		style.addStyleListener(this);
		notifyUpdated();
	}

	@Override
	public ILinkStyle getStyle() {
		if (this.style == null) setStyle(new DefaultLinkStyle());
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
	public void notifyLabelChanged() {
		for (ILinkModelListener listener : listeners) {
			listener.labelChanged(this);
		}
	}
	@Override
	public void notifyEdgeFlipped() {
	    for (ILinkModelListener listener : listeners) {
	        listener.linkFlipped(this);
	    }
	}

	@Override
	public boolean isSelected() {
		return selected;
	}

	@Override
	public void setSelected(boolean b) {
		selected = b;
		notifySelectionChanged();
	}

	private void notifySelectionChanged() {
		for (ILinkModelListener listener : listeners) {
			listener.selectionChanged(this);
		}
	}

	@Override
	public void styleChanged(ILinkStyle s) {
		notifyUpdated();
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
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}
}
