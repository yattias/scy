package eu.scy.colemo.client;

import eu.scy.colemo.client.figures.LabelArrow;
import eu.scy.colemo.server.uml.UmlLink;

import java.awt.*;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;

public class ConceptLink extends LabelArrow implements FocusListener {

	private ConceptNode fromNode;
	private ConceptNode toNode;
	private boolean bidirectional = false;

	private Color color;
	private UmlLink model;
	private ConceptLink link;

	public ConceptLink(UmlLink link) {
		setModel(link);

		setOpaque(false);
		setBackground(Color.cyan);
		setLayout(null);
		setFocusable(true);
		addFocusListener(new UpdateLinkListener(this));
	}

	public ConceptNode getToNode() {
		return toNode;
	}

	public void setToNode(ConceptNode node) {
		toNode = node;
		getModel().setTo(toNode.getModel().getId());
		updatePosition();
	}

	public ConceptNode getFromNode() {
		return fromNode;
	}

	public void setFromNode(ConceptNode fromNode) {
		this.fromNode = fromNode;
		getModel().setFrom(fromNode.getModel().getId());
		updatePosition();
	}

	public void updatePosition() {
		// If we don't have both nodes, do nothing
		if (fromNode == null || toNode == null) {
			return;
		}

		int dir = findDirection(fromNode.getCenterPoint(), toNode.getCenterPoint());
		setFrom(fromNode.getLinkConnectionPoint(dir));
		setTo(toNode.getLinkConnectionPoint(-dir));

		updateWidth();
		repaint();
	}
	public UmlLink getModel() {
		return model;
	}

	public void setModel(UmlLink model) {
		this.model = model;
		updatePosition();
	}

	private class UpdateLinkListener implements FocusListener {
		public UpdateLinkListener(ConceptLink conceptLink) {
			link = conceptLink;
		}

		public void focusGained(FocusEvent e) {
		}

		public void focusLost(FocusEvent e) {
			link.getModel().setName(link.getLabel());
			ApplicationController.getDefaultInstance().getConnectionHandler().updateObject(link.getModel());
		}
	}
}