package eu.scy.scymapper.api.diagram.view;

import eu.scy.scymapper.api.diagram.controller.ILinkController;
import eu.scy.scymapper.api.diagram.model.ILinkModel;
import eu.scy.scymapper.api.styling.ILinkStyle;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;

/**
 * @author bjoerge
 * @created 09.des.2009 17:47:32
 */
public class LinkViewComponent extends JComponent {

	private ILinkController controller;
	private ILinkModel model;

	protected int minWidth = 100;
	protected int minHeight = 200;

	private final static Logger logger = Logger.getLogger(LinkViewComponent.class);

	public LinkViewComponent(ILinkController controller, ILinkModel model) {
		this.controller = controller;
		this.model = model;

		setLayout(null);
		updatePosition();
	}

	public void paintComponent(Graphics g) {

		Point from = model.getFrom();
		Point to = model.getTo();

		if (from == null || to == null) {
			logger.warn("From or to is null");
			return;
		}
		Point relFrom = new Point(from);
		relFrom.translate(-getX(), -getY());

		Point relTo = new Point(to);
		relTo.translate(-getX(), -getY());

		Graphics2D g2 = (Graphics2D) g.create();

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		ILinkStyle style = model.getStyle();

		g2.setStroke(style.getStroke());

		model.getShape().paint(g2, relFrom, relTo);

		g2.dispose();

		// Continue painting other component on top
		super.paintComponent(g);

	}

	public void setFrom(Point p) {
		controller.setFrom(p);
	}

	public void setTo(Point p) {
		controller.setTo(p);
	}

	protected void updatePosition() {
		Point from = model.getFrom();
		Point to = model.getTo();
		if (from == null || to == null) return;

		int w, x, h, y;
		if (to.x < from.x) {
			x = to.x;
			w = from.x - to.x;
		} else {
			x = from.x;
			w = to.x - from.x;
		}
		if (to.y < from.y) {
			y = to.y;
			h = from.y - to.y;
		} else {
			y = from.y;
			h = to.y - from.y;
		}

		setBounds(x - minWidth / 2, y - minHeight / 2, w + minWidth, h + minHeight);
	}

	public ILinkModel getModel() {
		return model;
	}

	public void setModel(ILinkModel model) {
		this.model = model;
	}

	public ILinkController getController() {
		return controller;
	}
}
