package eu.scy.colemo.client.shapes;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 03.jun.2009
 * Time: 13:24:30
 * To change this template use File | Settings | File Templates.
 */
public abstract class INodeShape {

    public static final int DRAW = 0;
    public static final int FILL = 1;
    private int mode = DRAW;

    public void setMode(int mode) {
        if (mode != DRAW && mode != FILL) throw new IllegalArgumentException("Unsupported mode");
        this.mode = mode;
    }
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
     * This returns the shape at the size of the given bounds.
     * @param bounds The returned shape conforms to these bounds. Typically, a shape should fit the size of these bounds
     * @param g The graphics object to paint on
     */
	public abstract void paint(Graphics g, Rectangle bounds);
}
