package eu.scy.scymapper.impl.shapes.links;

import eu.scy.scymapper.api.shapes.ILinkShape;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * Created by IntelliJ IDEA.
 * @author bjoerge
 * @created 11.jun.2009 18:18:28
 */
public class Line implements ILinkShape {

	public Line() {
	}

//	public double getAngleAtEnd(Point from, Point to) {
//		CubicCurve2D curve = getShape(from, to);
//		Line2D line = new Line2D.Float(from, curve.getCtrlP2());
//
//		return Math.atan2(line.getBounds().getCenterY() - to.getY(), line.getBounds().getCenterX() - to.getX());
//	}
//
//	public double getAngleAtStart(Point from, Point to) {
//		CubicCurve2D curve = getShape(from, to);
//		return Math.atan2(from.y - curve.getCtrlY1(), from.x - curve.getCtrlX1());
//	}

	public static double getAngle(Point from, Point to) {
		return Math.atan2(from.getY() - to.getY(), from.getX() - to.getX());
	}

	@Override
	public void paint(Graphics g, Point from, Point to) {
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.draw(getShape(from, to));
		g2d.dispose();
	}

	public Line2D getShape(Point from, Point to) {
		//return new CubicCurve2D.Double(from.x, from.y, to.x, from.y, from.x, to.y, to.x, to.y);
		return new Line2D.Double(from.x, from.y, to.x, to.y);
	}
}
