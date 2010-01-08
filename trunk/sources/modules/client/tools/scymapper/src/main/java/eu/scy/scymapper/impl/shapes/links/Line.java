package eu.scy.scymapper.impl.shapes.links;

import eu.scy.scymapper.api.shapes.ILinkShape;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * Created by IntelliJ IDEA.
 *
 * @author bjoerge
 * @created 11.jun.2009 18:18:28
 */
public class Line implements ILinkShape {

	public Line() {
	}

	public static double getAngle(Point2D from, Point2D to) {
		return Math.atan2(from.getY() - to.getY(), from.getX() - to.getX());
	}


	public Point2D getPointInLine(Line2D line, double ratio) {
		double dy = line.getY2() - line.getY1();
		double dx = line.getX2() - line.getX1();
		return new Point2D.Double(line.getX1() + (dx * ratio), line.getY1() + (dy * ratio));
	}
	@Override
	public Point2D getDeCasteljauPoint(Point2D from, Point2D to, double param) {
		return getPointInLine(getShape(from, to), param);
	}

	@Override
	public Line2D getShape(Point2D from, Point2D to) {
		return new Line2D.Double(from.getX(), from.getY(), to.getX(), to.getY());
	}

	@Override
	public void paint(Graphics g, Point from, Point to) {
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.draw(getShape(from, to));
		g2d.dispose();
	}
}
