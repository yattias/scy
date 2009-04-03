package eu.scy.colemo.client;

import eu.scy.colemo.server.uml.UmlLink;

import java.awt.*;

public class ConceptLink extends eu.scy.colemo.client.figures.LabelArrow {
	public static final String DEFAULT_LABEL = "Link";

	private ConceptNode fromNode;
	private ConceptNode toNode;
	private boolean bidirectional = false;

	private Color color;
	private UmlLink model;

	public ConceptLink(UmlLink link) {
		setModel(link);

		setOpaque(false);
		setBackground(Color.cyan);
		setLayout(null);
		setFocusable(true);
	}

	public ConceptNode getToNode() {
		return toNode;
	}

	public void setToNode(ConceptNode node) {
		toNode = node;
		update();
	}

	public ConceptNode getFromNode() {
		return fromNode;
	}

	public void setFromNode(ConceptNode fromNode) {
		this.fromNode = fromNode;
		update();
	}

	public void update() {
		// If we don't have both nodes, do nothing
		if (fromNode == null || toNode == null) {
			return;
		}

		int dir = findDirection(fromNode.getCenterPoint(), toNode.getCenterPoint());
		setFrom(fromNode.getLinkConnectionPoint(dir));
		setTo(toNode.getLinkConnectionPoint(-dir));
		setLabel(getModel().getName());
		repaint();
	}
	public UmlLink getModel() {
		return model;
	}

	public void setModel(UmlLink model) {
		this.model = model;
		update();
	}
}