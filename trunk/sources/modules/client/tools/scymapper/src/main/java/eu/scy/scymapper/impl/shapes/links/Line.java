package eu.scy.scymapper.impl.shapes.links;

import eu.scy.scymapper.api.shapes.ILinkShape;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 11.jun.2009
 * Time: 18:18:28
 * To change this template use File | Settings | File Templates.
 */
public class Line implements ILinkShape {

    public Line() {
    }
	public static double getAngle(Point from, Point to) {
		return Math.atan2(from.getY() - to.getY(), from.getX() - to.getX());
	}

	@Override
	public void paint(Graphics g, Point from, Point to) {
		Graphics2D g2d = (Graphics2D)g.create();
		g2d.draw(getShape(from, to));
	}

	public Shape getShape(Point from, Point to) {
		return new Line2D.Double(from, to);
	}
}
