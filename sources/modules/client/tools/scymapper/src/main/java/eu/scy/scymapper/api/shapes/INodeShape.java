package eu.scy.scymapper.api.shapes;

import java.awt.*;
import java.io.Serializable;

/**
 * @author bjoerge
 * @created 03.jun.2009 13:24:30
 * Implementations of this abstract class is responsible for painting a node on a graphics object
 * In addition, it is responsible for returning a connection point for links at certain points in the diagram
 * I.e. a connected link may ask where the node want it to connect in regards of a nearby point.
 * Lets say the link L starts at [10, 10] and is supposed to connect to the node N which resides at [30,30]
 * In order to figure out where the link should connect to the node, it asks the node to decide by calling
 * N.getConnectionPoint([10, 10]). The node would then return an appropriate location, i.e. [30, 30] which is
 * the upper left corner. If another link is already connected to [30, 30], the node may return another point, like
 * [30,40]
 */
public abstract class INodeShape implements Serializable {

    public static final int DRAW = 0;
    public static final int FILL = 1;
    private int mode = DRAW;

    /**
     * Sets the paint mode. Can be either INodeShape.DRAW or INodeShape.FILL
     * @param mode paint mode
     */
    public void setMode(int mode) {
        if (mode != DRAW && mode != FILL) throw new IllegalArgumentException("Unsupported mode");
        this.mode = mode;
    }

    /**
     * Returns paint mode. Either INodeShape.DRAW or INodeShape.FILL
     * @return paint mode
     */
    public int getMode() {
        return mode;
    }
    /**
     * This method returns the point where links coming from an outside point in the x,y space should connect
     * @param p The point where the connection is coming from
     * @param bounds The bounding box of the shape container
     * @return The point at which the link should connect. This point is *NOT* relative to the shape itself, but rather
     * an absolute position in the given X,Y space
     */
    public abstract Point getConnectionPoint(Point p, Rectangle bounds);

    /**
     * Paints the shape on the given graphics object.
     * @param bounds The returned shape conforms to these bounds. Typically, a shape should fit the size of these bounds
     * @param g The graphics object to paint on
     */
	public abstract void paint(Graphics g, Rectangle bounds);
}
