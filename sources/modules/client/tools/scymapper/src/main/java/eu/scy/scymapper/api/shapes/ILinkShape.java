package eu.scy.scymapper.api.shapes;

import java.awt.*;
import java.io.Serializable;

/**
 * @author bjoerge
 * Date: 17.jun.2009
 * Time: 16:10:07
 */
public interface ILinkShape extends Serializable {
    /**
     * This method paints the link between the given two points.
     * @param g The graphics object to paint on
	 * @param from The point where the painted link starts
     * @param to The point where the painted link ends
     */
	public void paint(Graphics g, Point from, Point to);
}
