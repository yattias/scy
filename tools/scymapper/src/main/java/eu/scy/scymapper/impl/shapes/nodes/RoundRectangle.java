package eu.scy.scymapper.impl.shapes.nodes;

import eu.scy.scymapper.api.shapes.INodeShape;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 03.jun.2009
 * Time: 13:25:19
 */
public class RoundRectangle extends INodeShape {
	public RoundRectangle() {
	}

	@Override
	public Point getConnectionPoint(Point point, java.awt.Rectangle bounds) {

		double x = point.x;
		double y = point.y;
		if (point.x > bounds.getMaxX()) x = bounds.getMaxX();
		else if (point.x < bounds.getMinX()) x = bounds.getMinX();

		if (point.y > bounds.getMaxY()) y = bounds.getMaxY();
		else if (point.y < bounds.getMinY()) y = bounds.getMinY();

		return new Point((int) x, (int) y);
	}

	@Override
	public void paint(Graphics g, java.awt.Rectangle bounds) {
		Graphics2D g2 = (Graphics2D) g.create();

		g2.translate(bounds.x, bounds.y);

		switch (getMode()) {
			case DRAW:
				g2.draw(new RoundRectangle2D.Double(0, 0, bounds.width - 1, bounds.height - 1, 10, 10));
				break;
			case FILL:
				g2.fill(new RoundRectangle2D.Double(0, 0, bounds.width - 1, bounds.height - 1, 10, 10));
		}
	}
}
