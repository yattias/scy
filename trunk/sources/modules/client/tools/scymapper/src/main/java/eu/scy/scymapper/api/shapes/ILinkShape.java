package eu.scy.scymapper.api.shapes;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 17.jun.2009
 * Time: 16:10:07
 * To change this template use File | Settings | File Templates.
 */
public interface ILinkShape {
    /**
     * This method paints the link between the given two points.
     * @param g The graphics object to paint on
	 * @param from The point where the painted link starts
     * @param to The point where the painted link ends
     */
	public void paint(Graphics g, Point from, Point to);
}
