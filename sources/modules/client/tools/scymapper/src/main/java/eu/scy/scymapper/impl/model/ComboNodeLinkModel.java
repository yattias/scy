package eu.scy.scymapper.impl.model;

import java.awt.Point;
import java.util.List;

import eu.scy.scymapper.api.diagram.model.INodeLinkModel;
import eu.scy.scymapper.api.diagram.model.INodeModel;

public class ComboNodeLinkModel extends SimpleLink implements INodeLinkModel {
	private INodeModel fromNode;
	private INodeModel toNode;
	private List<String> options;

	public ComboNodeLinkModel(Point from, Point to) {
		this.from = from;
		this.to = to;
	}

	public ComboNodeLinkModel(INodeModel fromNode, INodeModel toNode, List<String> options) {
		this.fromNode = fromNode;
		this.toNode = toNode;
		this.options = options;
	}

	@Override
	public Point getFrom() {

		if (!isConnected())
			return from;

		Point to = toNode.getConnectionPoint(fromNode.getCenterLocation());
		return fromNode.getConnectionPoint(to);

	}

	@Override
	public Point getTo() {

		if (!isConnected())
			return to;

		Point from = fromNode.getConnectionPoint(toNode.getCenterLocation());
		return toNode.getConnectionPoint(from);
	}

	@Override
	public boolean isConnected() {
		return this.fromNode != null && this.toNode != null;
	}

	@Override
	public INodeModel getFromNode() {
		return fromNode;
	}

	@Override
	public void setFromNode(INodeModel node) {
		fromNode = node;
	}

	@Override
	public INodeModel getToNode() {
		return toNode;
	}

	@Override
	public void setToNode(INodeModel node) {
		toNode = node;
	}

	public List<String> getOptions() {
		return options;
	}

	public void setOptions(List<String> options) {
		this.options = options;
		notifyLabelChanged();
	}

    public void edgeFlipped() {
        notifyEdgeFlipped();

    }
}
