package eu.scy.scymapper.impl.model;

import eu.scy.scymapper.api.diagram.model.INodeModel;
import eu.scy.scymapper.api.diagram.model.INodeModelConstraints;
import eu.scy.scymapper.api.diagram.model.INodeModelListener;
import eu.scy.scymapper.api.shapes.INodeShape;
import eu.scy.scymapper.api.styling.INodeStyle;
import eu.scy.scymapper.api.styling.INodeStyleListener;
import eu.scy.scymapper.impl.shapes.nodes.DefaultNodeShape;
import eu.scy.scymapper.impl.ui.Localization;

import java.awt.*;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 22.jun.2009
 * Time: 19:47:53
 */
public class NodeModel implements INodeModel, INodeStyleListener {

	private transient java.util.List<INodeModelListener> listeners;
	private transient boolean selected = false;

	private String label = Localization.getString("Mainframe.ConceptMap.DefaultConceptName");

	private INodeShape shape = new DefaultNodeShape();
	private INodeStyle style = new DefaultNodeStyle();

	private boolean labelHidden = false;
	private boolean deleted;

	private INodeModelConstraints constraints = new NodeModelConstraints();
	private int x = 20;
	private int y = 20;

	private int height = 100;
	private int width = 100;
	private String id = UUID.randomUUID().toString();

	private Object readResolve() {
		listeners = new ArrayList<INodeModelListener>();
		return this;
	}

	public NodeModel() {
		listeners = new ArrayList<INodeModelListener>();
	}

	public NodeModel(INodeShape shape) {
		this();
		this.shape = shape;
	}

	@Override
	public int getDistanceToPerimeter(Point p) {
		return (int) p.distance(getShape().getConnectionPoint(p, new Rectangle(getLocation(), getSize())));
	}

	@Override
	public Point getConnectionPoint(Point p) {
		return getShape().getConnectionPoint(p, new Rectangle(getLocation(), getSize()));
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
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
		style.addStyleListener(this);
		notifyStyleChanged();
	}

	@Override
	public void setSelected(boolean b) {
		selected = b;
		notifySelected();
	}

	@Override
	public INodeStyle getStyle() {
		if (style == null) {
			style = new DefaultNodeStyle();
			style.addStyleListener(this);
		}
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
		if (shape == null) {
			setShape(new DefaultNodeShape());
		}
		return shape;
	}

	@Override
	public Dimension getSize() {
		return new Dimension(this.width, this.height);
	}

	@Override
	public void setSize(int height, int width) {
		this.height = height;
		this.width = width;
		notifyResized();
	}

	@Override
	public void setHeight(int height) {
		setSize(height, width);
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public void setWidth(int width) {
		setSize(height, width);
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public void setSize(Dimension size) {
		setSize(size.height, size.width);
	}

	@Override
	public void setLocation(Point location) {
		setLocation(location.x, location.y);
	}

	@Override
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
		notifyMoved();
	}

	@Override
	public void setX(int x) {
		setLocation(x, y);
	}

	@Override
	public void setY(int y) {
		setLocation(x, y);
	}

	@Override
	public Point getLocation() {
		return new Point(x, y);
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
	public void notifyStyleChanged() {
		for (INodeModelListener listener : listeners) {
			listener.styleChanged(this);
		}
	}

	@Override
	public void notifySelected() {
		for (INodeModelListener listener : listeners) {
			listener.selectionChanged(this);
		}
	}

	@Override
	public void notifyDeleted() {
		for (INodeModelListener listener : listeners) {
			listener.deleted(this);
		}
	}

	@Override
	public void setConstraints(INodeModelConstraints constraints) {
		this.constraints = constraints;
	}

	@Override
	public INodeModelConstraints getConstraints() {
		if (constraints == null) constraints = new NodeModelConstraints();
		return constraints;
	}

	@Override
	public boolean isSelected() {
		return selected;
	}

	@Override
	public void setDeleted(boolean b) {
		deleted = b;
		notifyDeleted();
	}

	@Override
	public boolean isDeleted() {
		return deleted;
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
