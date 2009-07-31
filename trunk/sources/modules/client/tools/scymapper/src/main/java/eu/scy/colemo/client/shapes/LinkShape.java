package eu.scy.colemo.client.shapes;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 17.jun.2009
 * Time: 16:10:07
 * To change this template use File | Settings | File Templates.
 */
public interface LinkShape {
    /**
     * This returns the shape at the size of the given bounds.
     * @param from The returned shape starts at this point
     * @param to The returned shape ends at this point
     * @return The shape defined by the specific implementation of this interface
     */
	public Shape getShape(Point from, Point to);
}
