package eu.scy.scymapper.impl.shapes.links;

import eu.scy.scymapper.impl.shapes.LinkShape;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 11.jun.2009
 * Time: 18:18:28
 * To change this template use File | Settings | File Templates.
 */
public class Line implements LinkShape {
    private Point2D from;
    private Point2D to;

    public Line() {
    }
    public Line(Point from, Point to) {
        this.from = from;
        this.to = to;
    }
	public static double getAngle(Point2D from, Point2D to) {
		return Math.atan2(from.getY() - to.getY(), from.getX() - to.getX());
	}

	public double getAngle() {
		return getAngle(from, to);
	}
    public double getLength() {
        return from.distance(to);
    }

    public Shape getShape() {
        return new Line2D.Double(from, to);
    }


    @Override
    public Shape getShape(Point from, Point to) {
        return new Line(from, to).getShape();
    }
}
