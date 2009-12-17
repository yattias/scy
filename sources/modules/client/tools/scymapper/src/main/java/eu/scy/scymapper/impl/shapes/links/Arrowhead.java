package eu.scy.scymapper.impl.shapes.links;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 11.jun.2009
 * Time: 18:22:03
 */
public class Arrowhead {

	private transient double angle = Math.PI / 3;

	private double length = 25;
	private transient double rotation = Double.NaN;

	private boolean fixedSize;

	private Object readResolve() {
		angle = Math.PI / 3;
		return this;
	}

	public Arrowhead() {
	}

	/**
	 * @param length Shaft size
	 */
	public Arrowhead(int length) {
		this.length = (double) length;
	}

	/**
	 * @param length Shaft size
	 * @param angle  The angle between the shafts (i.e. 90: _|)
	 */
	public Arrowhead(int length, double angle) {
		this.length = (double) length;
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

		double x = length * Math.cos(angle / 2);
		double y = length * Math.sin(angle / 2);

		path.moveTo(x, -y);
		path.lineTo(0, 0);
		path.lineTo(x, y);

		AffineTransform at = new AffineTransform();
		if (rotation != Double.NaN) {
			at.rotate(rotation);
		}
		return at.createTransformedShape(path);
	}

	public void setFixedSize(boolean b) {
		this.fixedSize = b;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public boolean isFixedSize() {
		return fixedSize;
	}
}
