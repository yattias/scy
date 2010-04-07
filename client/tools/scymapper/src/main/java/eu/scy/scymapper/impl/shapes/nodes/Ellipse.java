package eu.scy.scymapper.impl.shapes.nodes;

import eu.scy.scymapper.api.shapes.INodeShape;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 15.jun.2009
 * Time: 15:35:10
 */
public class Ellipse extends INodeShape {
	public Ellipse() {
	}

	/**
	 * @param point  The location of the point located outside of the bounding box
	 * @param bounds The bounding box of this shape
	 * @return The point where the line going from the outside location hits the perimeter of this shape.
	 */
	@Override
	public Point getConnectionPoint(Point point, java.awt.Rectangle bounds) {

		Point center = new Point((int) bounds.getCenterX(), (int) bounds.getCenterY());

		double angle = Math.atan2(point.y - center.y, point.x - center.x);

		double x = center.x + (bounds.width - 2) / 2 * Math.cos(angle);
		double y = center.y + (bounds.height - 2) / 2 * Math.sin(angle);
		return new Point((int) x, (int) y);

	}

	@Override
	public void paint(Graphics g, java.awt.Rectangle bounds) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.translate(bounds.x, bounds.y);
		switch (getMode()) {
			case DRAW:
				g2.draw(new Ellipse2D.Float(0, 0, bounds.width - 1, bounds.height - 1));
				break;
			case FILL:
				g2.fill(new Ellipse2D.Float(0, 0, bounds.width - 1, bounds.height - 1));
		}
	}

}
