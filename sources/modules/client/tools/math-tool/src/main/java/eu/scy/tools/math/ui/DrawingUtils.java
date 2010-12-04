package eu.scy.tools.math.ui;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class DrawingUtils {

	/** Return the point at the center of a Rectangle.
     */
    public static Point2D getCenterPoint (Rectangle2D r) {
        return new Point2D.Double(r.getCenterX(), r.getCenterY());
    }
    
}
