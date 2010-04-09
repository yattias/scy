package eu.scy.scymapper.api.shapes;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 * @author bjoerge
 * Date: 17.jun.2009
 * Time: 16:10:07
 */
public interface ILinkShape extends Serializable {

	Point2D getDeCasteljauPoint(Point2D from, Point2D to, double param);

	/**
	 *
	 * @param from
	 * @param to
	 * @return The shape corresponding to the bounds of the link
	 */
	Shape getShape(Point2D from, Point2D to);

    /**
     * This method paints the link between the given two points.
     * @param g The graphics object to paint on
	 * @param from The point where the painted link starts
     * @param to The point where the painted link ends
     */
	void paint(Graphics g, Point from, Point to);
}
