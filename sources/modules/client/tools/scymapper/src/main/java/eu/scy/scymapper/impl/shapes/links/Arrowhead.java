package eu.scy.scymapper.impl.shapes.links;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.AffineTransform;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 11.jun.2009
 * Time: 18:22:03
 * To change this template use File | Settings | File Templates.
 */
public class Arrowhead {
    private double angle;
    private double length;
    private double rotation = Double.NaN;

    /**
     * @param length Shaft size
     * @param angle  The angle between the shafts (i.e. 90: _|)
     */
    public Arrowhead(int length, double angle) {
        this.length = (double)length;
        this.angle = angle;
    }

    /**
     * @param rotation The rotation of the arrowhead in radians
     */
    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    /**
     * Creates and returns an arrowhead shape
     *
     * @return The arrow shape
     */
    public Shape getShape() {
        GeneralPath path = new GeneralPath();

        //path.append(new Rectangle2D.Double(0,0, 50d, 50d), false);

        double x = length * Math.cos(angle/2);
        double y = length * Math.sin(angle/2);

        path.moveTo(x, -y);
        path.lineTo(0, 0);
        path.lineTo(x, y);

        AffineTransform at = new AffineTransform();
        if (rotation != Double.NaN) {
           at.rotate(rotation);
        }
        return at.createTransformedShape(path);
    }
}
