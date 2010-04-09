package eu.scy.scymapper.impl.shapes.nodes;

import eu.scy.scymapper.api.shapes.INodeShape;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

/**
 * @author bjoerge
 * @created 17.des.2009 13:44:44
 */
public class Triangle extends INodeShape {
	private int xPoints[] = {100, 200, 100, 0};
	private int yPoints[] = {0, 100, 100, 100};
	private GeneralPath star = null;

	@Override
	public Point getConnectionPoint(Point p, Rectangle bounds) {

		// For a star, this method returns the nearest point at which two vertices connect

		Point nearestPoint = null;
		for (int i = 0; i < yPoints.length; i++) {
			double yPoint = yPoints[i] * getSY(bounds);
			double xPoint = xPoints[i] * getSX(bounds);
			double xPointPos = bounds.x + xPoint;
			double yPointPos = bounds.y + yPoint;

			Point thisPoint = new Point((int) xPointPos, (int) yPointPos);

			if (nearestPoint == null || thisPoint.distance(p) < nearestPoint.distance(p))
				nearestPoint = thisPoint;
		}
		return nearestPoint;

	}

	/**
	 * @param bounds The bounds of the resulting shape
	 * @return The factor by which the shape are scaled along the X axis direction
	 */
	private double getSX(Rectangle bounds) {
		return bounds.width / (double) createStar().getBounds().width;
	}

	/**
	 * @param bounds The bounds of the resulting shape
	 * @return The factor by which the shape are scaled along the Y axis direction
	 */
	private double getSY(Rectangle bounds) {
		return bounds.height / (double) createStar().getBounds().height;
	}

	/**
	 * Creates a new star shape
	 *
	 * @return A star-shaped shape
	 */
	private Shape createStar() {
		if (star == null) {

			star = new GeneralPath();

			star.moveTo(xPoints[0], yPoints[0]);

			for (int k = 1; k < xPoints.length; k++)
				star.lineTo(xPoints[k], yPoints[k]);

			star.closePath();
		}
		return star;
	}

	@Override
	public void paint(Graphics g, Rectangle bounds) {
		Graphics2D g2 = (Graphics2D) g.create();

		Shape star = createStar();

		// Scale it according to bounds
		AffineTransform at = AffineTransform.getScaleInstance(getSX(bounds), getSY(bounds));

		g2.translate(bounds.x, bounds.y);

		switch (getMode()) {
			case DRAW:
				g2.draw(at.createTransformedShape(star));
				break;
			case FILL:
				g2.fill(at.createTransformedShape(star));
				break;
		}
	}
}
