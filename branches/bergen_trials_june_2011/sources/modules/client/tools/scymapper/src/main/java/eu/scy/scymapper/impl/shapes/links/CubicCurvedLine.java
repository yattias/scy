package eu.scy.scymapper.impl.shapes.links;

import java.awt.*;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by IntelliJ IDEA.
 *
 * @author bjoerge
 * @created 11.jun.2009 18:18:28
 */
public class CubicCurvedLine implements ICurvedLine {

	private double curving = 0.75;
	public CubicCurvedLine() {
	}

	public static double getAngle(Point2D from, Point2D to) {
		return Math.atan2(from.getY() - to.getY(), from.getX() - to.getX());
	}

	/**
	 * See http://www.caffeineowl.com/graphics/2d/vectorial/bezierintro.html for an explanation of this algorithm.
	 * @param from
	 * @param to
	 * @param ratio
	 * @return
	 */
	@Override
	public Point2D getDeCasteljauPoint(Point2D from, Point2D to, double ratio) {

		// A: P1 		-> CtrlP1
		// B: CtrlP1 	-> CtrlP2
		// C: CtrlP2 	-> P2
		CubicCurve2D curve = getShape(from, to);
		Point2D A = getPointInLine(new Line2D.Double(curve.getP1(), curve.getCtrlP1()), ratio);
		Point2D B = getPointInLine(new Line2D.Double(curve.getCtrlP1(), curve.getCtrlP2()), ratio);
		Point2D C = getPointInLine(new Line2D.Double(curve.getCtrlP2(), curve.getP2()), ratio);

		// M: A		-> B
		// N: B		-> C
		Point2D M = getPointInLine(new Line2D.Double(A, B), ratio);
		Point2D N = getPointInLine(new Line2D.Double(B, C), ratio);

		return getPointInLine(new Line2D.Double(M, N), ratio);
	}

	public Point2D getPointInLine(Line2D line, double ratio) {
		double dy = line.getY2() - line.getY1();
		double dx = line.getX2() - line.getX1();
		return new Point2D.Double(line.getX1() + (dx * ratio), line.getY1() + (dy * ratio));
	}

	@Override
	public CubicCurve2D getShape(Point2D from, Point2D to) {
		double ctrlX1, ctrlY1, ctrlX2, ctrlY2;
		Rectangle2D rect = new Line2D.Double(from, to).getBounds2D();

		double w = rect.getWidth();
		double h = rect.getHeight();

		if (from.getX() < to.getX()) {
			if (from.getY() < to.getY()) {
				ctrlX1 = from.getX() + (w * curving);
				ctrlY1 = from.getY() + (h * (1-curving));
				ctrlX2 = from.getX() + (w * (1-curving));
				ctrlY2 = from.getY() + (h * curving);
			} else {
				ctrlX1 = from.getX() + (w * (1-curving));
				ctrlY1 = from.getY() + (h * -curving);
				ctrlX2 = from.getX() + (w * curving);
				ctrlY2 = from.getY() + (h * -(1-curving));
			}
		} else {
			if (from.getY() < to.getY()) {
				ctrlX1 = from.getX() + (w * -curving);
				ctrlY1 = from.getY() + (h * (1-curving));
				ctrlX2 = from.getX() + (w * -(1-curving));
				ctrlY2 = from.getY() + (h * curving);
			} else {
				ctrlX1 = from.getX() + (w * -curving);
				ctrlY1 = from.getY() + (h * -(1-curving));
				ctrlX2 = from.getX() + (w * -(1-curving));
				ctrlY2 = from.getY() + (h * -curving);
			}
		}
		return new CubicCurve2D.Double(from.getX(), from.getY(), ctrlX1, ctrlY1, ctrlX2, ctrlY2, to.getX(), to.getY());
	}

	@Override
	public void paint(Graphics g, Point from, Point to) {
		Graphics2D g2d = (Graphics2D) g.create();
		CubicCurve2D c = getShape(from, to);
		//DEBUG: Draw control points
//		GeneralPath gp = new GeneralPath();
//		gp.append(c, false);
//		gp.append(new Ellipse2D.Double(c.getCtrlX1(), c.getCtrlY1(), 4, 4), false);
//		gp.append(new Rectangle2D.Double(c.getCtrlX2(), c.getCtrlY2(), 4, 4), false);
		g2d.draw(c);
		g2d.dispose();
	}
}