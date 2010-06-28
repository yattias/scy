package colab.vt.whiteboard.utils;

import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TransformUtils
{
	private static final Logger logger = Logger.getLogger(TransformUtils.class.getName());

	private TransformUtils()
	{
	}

	public static Point2D getTransformedPoint(AffineTransform affineTransform, double x, double y)
	{
		Point2D point = new Point2D.Double(x, y);
		Point2D transformedPoint = new Point2D.Double();
		affineTransform.transform(point, transformedPoint);
		return transformedPoint;
	}

	public static double getMinimum(double... values)
	{
		double minimum = values[0];
		for (int i = 1; i < values.length; i++)
			minimum = Math.min(minimum, values[i]);
		return minimum;
	}

	public static double getMaximum(double... values)
	{
		double maximum = values[0];
		for (int i = 1; i < values.length; i++)
			maximum = Math.max(maximum, values[i]);
		return maximum;
	}

	public static Point getInverseTransformedPoint(AffineTransform affineTransform, int x, int y)
	{
		Point2D point2D = getInverseTransformedPoint2D(affineTransform, x, y);
		Point point = new Point();
		point.x = (int) Math.round(point2D.getX());
		point.y = (int) Math.round(point2D.getY());
		return point;
	}

	public static Point2D getInverseTransformedPoint2D(AffineTransform affineTransform, double x,
				double y)
	{
		Point2D point = new Point2D.Double(x, y);
		Point2D transformedPoint = new Point2D.Double();
		try
		{
			affineTransform.inverseTransform(point, transformedPoint);
		}
		catch (NoninvertibleTransformException e)
		{
			logger.log(Level.SEVERE, "problems with inverse transform", e);
		}
		return transformedPoint;
	}

	public static Rectangle2D getTranformedRectangle(AffineTransform affineTransform,
				Rectangle2D rectangle)
	{
		Point2D topLeft = getTransformedPoint(affineTransform, rectangle.getX(), rectangle.getY());
		Point2D topRight = getTransformedPoint(affineTransform, rectangle.getX()
					+ rectangle.getWidth(), rectangle.getY());
		Point2D bottomLeft = getTransformedPoint(affineTransform, rectangle.getX(), rectangle.getY()
					+ rectangle.getHeight());
		Point2D bottomRight = getTransformedPoint(affineTransform, rectangle.getX()
					+ rectangle.getWidth(), rectangle.getY() + rectangle.getHeight());
		double xMin = getMinimum(topLeft.getX(), topRight.getX(), bottomLeft.getX(), bottomRight
					.getX());
		double xMax = getMaximum(topLeft.getX(), topRight.getX(), bottomLeft.getX(), bottomRight
					.getX());
		double yMin = getMinimum(topLeft.getY(), topRight.getY(), bottomLeft.getY(), bottomRight
					.getY());
		double yMax = getMaximum(topLeft.getY(), topRight.getY(), bottomLeft.getY(), bottomRight
					.getY());
		double x = xMin;
		double width = xMax - xMin;
		if (width < 0)
		{
			x = xMax;
			width = -width;
		}
		double y = yMin;
		double height = yMax - yMin;
		if (height < 0)
		{
			y = yMax;
			height = -height;
		}
		Rectangle2D rectangle2D = new Rectangle2D.Double(x, y, width, height);
		// System.out.println("getTranformedRectangle(" + rectangle + "): " + rectangle2D);
		return rectangle2D;
	}
}
