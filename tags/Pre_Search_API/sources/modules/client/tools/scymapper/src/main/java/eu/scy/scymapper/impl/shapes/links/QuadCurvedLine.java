package eu.scy.scymapper.impl.shapes.links;

import eu.scy.scymapper.api.shapes.ILinkShape;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;

/**
 * @author bjoerge
 * @created 16.des.2009 18:13:57
 */
public class QuadCurvedLine implements ILinkShape {

	private double curving = 0.75;

	public QuadCurvedLine() {
	}

	/**
	 * See http://www.caffeineowl.com/graphics/2d/vectorial/bezierintro.html for an explanation of this algorithm.
	 *
	 * @param from
	 * @param to
	 * @param ratio
	 * @return
	 */
	@Override
	public Point2D getDeCasteljauPoint(Point2D from, Point2D to, double ratio) {
		return getPointInLine(getCtrlLine(from, to, ratio), ratio);
	}

	public Line2D getCtrlLine(Point2D from, Point2D to, double param) {
		Line2D p1ToC = getP1toC(from, to);
		Line2D cToP2 = getCtoP2(from, to);

		Point2D A = getPointInLine(p1ToC, param);
		Point2D B = getPointInLine(cToP2, param);

		return new Line2D.Double(A, B);
	}

	public double getCurving() {
		return curving;
	}

	public void setCurving(double curving) {
		this.curving = curving;
	}

	public Point2D getPointInLine(Line2D line, double ratio) {
		double dy = line.getY2() - line.getY1();
		double dx = line.getX2() - line.getX1();
		return new Point2D.Double(line.getX1() + (dx * ratio), line.getY1() + (dy * ratio));
	}

	public Line2D getP1toC(Point2D from, Point2D to) {
		QuadCurve2D shape = getShape(from, to);
		Point2D C = shape.getCtrlPt();
		Point2D P1 = shape.getP1();
		return new Line2D.Double(P1, C);
	}

	public Line2D getCtoP2(Point2D from, Point2D to) {
		QuadCurve2D shape = getShape(from, to);
		Point2D C = shape.getCtrlPt();
		Point2D P2 = shape.getP2();
		return new Line2D.Double(C, P2);
	}

	@Override
	public QuadCurve2D getShape(Point2D from, Point2D to) {
		// return new QuadCurve2D.Double(from.x, from.y, from.x, to.y, to.x, to.y);

		Rectangle2D rect = new Line2D.Double(from, to).getBounds2D();

		double ctrlX, ctrlY;

		double w = rect.getWidth();
		double h = rect.getHeight();

		if (from.getX() < to.getX()) {
			if (from.getY() < to.getY()) {
				ctrlX = from.getX() + (w * curving);
				ctrlY = from.getY() + (h * (1 - curving));
			} else {
				ctrlX = from.getX() + (w * curving);
				ctrlY = from.getY() + (h * -(1 - curving));
			}
		} else {
			if (from.getY() < to.getY()) {
				ctrlX = from.getX() + (w * -curving);
				ctrlY = from.getY() + (h * (1 - curving));
			} else {
				ctrlX = from.getX() + (w * -curving);
				ctrlY = from.getY() + (h * -(1 - curving));
			}
		}
		return new QuadCurve2D.Double(from.getX(), from.getY(), ctrlX, ctrlY, to.getX(), to.getY());
	}

	@Override
	public void paint(Graphics g, Point from, Point to) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.draw(getShape(from, to));
		g2d.dispose();
	}
}