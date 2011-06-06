package eu.scy.scymapper.api.diagram.view;

import eu.scy.scymapper.api.diagram.controller.ILinkController;
import eu.scy.scymapper.api.diagram.model.ILinkModel;
import eu.scy.scymapper.api.styling.ILinkStyle;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;

/**
 * @author bjoerge
 * @created 09.des.2009 17:47:32
 */
public class LinkViewComponent extends JComponent {
	private static final double DISTANCE_TRESHOLD = 5.0;

	private ILinkController controller;
	private ILinkModel model;

	protected int marginLeft = 100;
	protected int marginTop = 100;
	protected int marginRight = 100;
	protected int marginBottom = 100;

	private final static Logger logger = Logger.getLogger(LinkViewComponent.class);

	public LinkViewComponent(ILinkController controller, ILinkModel model) {
		this.controller = controller;
		this.model = model;

		//DEBUG: setBorder(BorderFactory.createLineBorder(Color.black, 1));
		setLayout(null);
		updatePosition();
	}

	@Override
	public boolean contains(int x, int y) {
		if (getModel() == null || getModel().getFrom() == null || getModel().getTo() == null) return false;

		Point p = new Point(x, y);

		p.translate(getX(), getY());

		Shape shape = getModel().getShape().getShape(getModel().getFrom(), getModel().getTo());

		return getDistance(shape, p) < DISTANCE_TRESHOLD;
	}

	private double getDistance(Shape shape, Point point) {
		double flatness = 0.01;
		double[] coords = new double[6];

		double minDist = Double.MAX_VALUE;

		Point2D.Double prev = null;
		PathIterator pit = shape.getPathIterator(null, flatness);

		while (!pit.isDone()) {
			pit.currentSegment(coords);

			if (prev != null) {
				double distance = Line2D.ptSegDist(prev.x, prev.y, coords[0], coords[1], point.x, point.y);
				if (distance < minDist) {
					minDist = distance;
				}
			} else
				prev = new Point2D.Double();

			prev.x = coords[0];
			prev.y = coords[1];

			pit.next();
		}
		return minDist;
	}

	public void paintComponent(Graphics g) {

		Graphics2D g2 = (Graphics2D) g.create();

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

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		ILinkStyle style = model.getStyle();

		g2.setStroke(style.getStroke());
		g2.setColor(style.getBackground());

		model.getShape().paint(g2, relFrom, relTo);

//		Shape shape = model.getShape().getShape(relFrom, relTo);
//		if (shape instanceof CubicCurve2D) {
//			CubicCurve2D curve = (CubicCurve2D)shape;
//			g2.drawString("x", (int)curve.getCtrlP1().getX(), (int)curve.getCtrlP1().getY() );
//			g2.drawString("x", (int)curve.getCtrlP2().getX(), (int)curve.getCtrlP2().getY() );
//		}
//		if (shape instanceof QuadCurve2D) {
//			QuadCurve2D curve = (QuadCurve2D)shape;
//			g2.drawString("x", (int)curve.getCtrlPt().getX(), (int)curve.getCtrlPt().getY() );
//
//			QuadCurvedLine qql = (QuadCurvedLine)model.getShape();
//
//			g2.setColor(Color.red);
//			g2.draw(qql.getP1toC(relFrom, relTo));
//
//			g2.setColor(Color.blue);
//			Line2D cToP2 = qql.getCtoP2(relFrom, relTo);
//			g2.draw(cToP2);
//
//			Point2D debugP = qql.getPointInLine(cToP2, 0.5);
//			System.out.println("cToP2 = " + cToP2);
//			System.out.println("debugP = " + debugP);
//
//			g2.drawString("#", (int)debugP.getX(), (int)debugP.getY());
//
//			g2.setColor(Color.green);
//			g2.draw(qql.getCtrlLine(relFrom, relTo, 0.5));
//
//			Point2D deCastPoint = qql.getDeCasteljauPoint(relFrom, relTo, 0.5);
//			g2.setColor(Color.pink);
//			g2.drawString("O", (int)deCastPoint.getX(), (int)deCastPoint.getY());
//
//		}

		g2.dispose();
	}

	public void setFrom(Point p) {
		controller.setFrom(p);
	}

	public void setTo(Point p) {
		controller.setTo(p);
	}

	public Rectangle getInnerBounds() {
		Point from = model.getFrom();
		Point to = model.getTo();

		int width, x, height, y;
		if (to.x < from.x) {
			x = to.x;
			width = from.x - to.x;
		} else {
			x = from.x;
			width = to.x - from.x;
		}
		if (to.y < from.y) {
			y = to.y;
			height = from.y - to.y;
		} else {
			y = from.y;
			height = to.y - from.y;
		}

		return new Rectangle(x, y, width, height);
	}

	protected void updatePosition() {
		if (model.getFrom() == null || model.getTo() == null) return;

		Rectangle b = getInnerBounds();

		setBounds(b.x - marginLeft, b.y - marginTop, b.width + marginLeft + marginRight, b.height + marginTop + marginBottom);
		//setBorder(BorderFactory.createLineBorder(Color.black, 1));
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

        public void setController(ILinkController controller) {
            this.controller = controller;
        }
}
