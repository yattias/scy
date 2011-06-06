package colab.vt.whiteboard.component;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

import colab.vt.whiteboard.utils.XmlUtils;

public class WhiteboardFreehand extends WhiteboardSimpleShape
{
	private static final long serialVersionUID = 490341824508152367L;

	private ArrayList<Point> points = new ArrayList<Point>();
	private int[] xPoints;
	private int[] yPoints;
	private int minX = 0;
	private int minY = 0;
	private int maxX = 0;
	private int maxY = 0;
	private Rectangle bounds = new Rectangle();
	private int pointsSaved = 0;

	public WhiteboardFreehand()
	{
		super(XmlNames.freehand);
	}

	@Override
	public void setBegin(int x, int y)
	{
		//points.add(new Point(x, y));
		addPoint(new Point(x, y));
		minX = x;
		minY = y;
		maxX = x;
		maxY = y;
		createBounds();
		createDrawArrays();
	}

	@Override
	public void setEnd(int x, int y)
	{
		// TODO finds something to prevent this check on null
		if (points != null)
		{
			addPoint(x, y);
			createBounds();
			createDrawArrays();
			// System.out.println("total points " + points.size() + ",saved " + pointsSaved);
		}
	}

	@Override
	public void setIntermediate(int x, int y)
	{
		addPoint(x, y);
		createBounds();
		createDrawArrays();
	}

	private void addPoint(int x, int y)
	{
		addPoint(new Point(x, y));
	}

	private void addPoint(Point point)
	{
		if (points.size()==0)
		{
			minX = point.x;
			maxX = point.x;
			minY = point.y;
			maxY = point.y;
		}
		if (replaceLastPoint(point))
		{
			points.set(points.size() - 1, point);
			++pointsSaved;
		}
		else
			points.add(point);
		minX = Math.min(minX, point.x);
		minY = Math.min(minY, point.y);
		maxX = Math.max(maxX, point.x);
		maxY = Math.max(maxY, point.y);
	}

	private boolean replaceLastPoint(Point point)
	{
		if (points.size() > 1)
		{
			Point lastPoint1 = points.get(points.size() - 1);
			Point lastPoint2 = points.get(points.size() - 2);
			int lastDX = lastPoint1.x - lastPoint2.x;
			int lastDY = lastPoint1.y - lastPoint2.y;
			int newDX = point.x - lastPoint1.x;
			int newDY = point.y - lastPoint1.y;
			if (newDX == 0)
			{
				return signEqual(newDY, lastDY);
			}
			else if (newDY == 0)
			{
				return signEqual(newDX, lastDX);
			}
			else
			{
				if (newDX >= lastDX)
				{
					double dxFactor = 1.0 * lastDX / newDX;
					double dyFactor = 1.0 * lastDY / newDY;
					double diff = dxFactor - dyFactor;
					return Math.abs(diff) < 0.01;
				}
			}
		}
		return false;
	}

	private boolean signEqual(int x, int y)
	{
		return (x > 0 && y > 0) || (x < 0 && y < 0);
	}

	private void createBounds()
	{
		int x = minX;
		int width = maxX - minX;
		if (width < 0)
		{
			x = maxX;
			width = -width;
		}
		int y = minY;
		int height = maxY - minY;
		if (height < 0)
		{
			y = maxY;
			height = -height;
		}
		bounds = new Rectangle(x, y, width, height);
		makeRectangleNotEmpty(bounds);
	}

	@Override
	public Rectangle getBounds()
	{
		return bounds;
	}

	public Point getLastPoint()
	{
		if (xPoints.length > 0)
		{
			return new Point(xPoints[xPoints.length - 1], yPoints[yPoints.length - 1]);
		}
		throw new IllegalStateException("no points in WhiteboardFreehand");
	}

	private void createDrawArrays()
	{
		xPoints = new int[points.size()];
		yPoints = new int[points.size()];
		for (int i = 0; i < xPoints.length; i++)
		{
			Point point = points.get(i);
			xPoints[i] = point.x;
			yPoints[i] = point.y;
		}
	}

	@Override
	public void paint(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		Color fillColor = g2d.getBackground();
		Color lineColor = g2d.getColor();
		g2d.setColor(fillColor);
		g.fillPolygon(xPoints, yPoints, xPoints.length);
		g2d.setColor(lineColor);
		g.drawPolyline(xPoints, yPoints, xPoints.length);
	}

	@Override
	public String getDescription()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Nr of points: ");
		builder.append(xPoints.length);
		builder.append("\n");
		for (int i = 0; i < xPoints.length; i++)
		{
			builder.append(i);
			builder.append(": ");
			builder.append(xPoints[i]);
			builder.append(",");
			builder.append(yPoints[i]);
			builder.append("\n");
		}
		return builder.toString();
	}

	@Override
	public Element getStatus()
	{
		Element status = new Element(getType());
		for (Point point : points)
		{
			XmlUtils.addXmlTag(status, XmlNames.point, point);
		}
		return status;
	}

	@Override
	public void setStatus(Element status)
	{
		points.clear();
		@SuppressWarnings("unchecked")
		List<Element> pointElements = status.getChildren(XmlNames.point);
		for (Element pointElement : pointElements)
		{
			addPoint(XmlUtils.getPointValueFromXmlTag(pointElement));
		}
		createBounds();
		createDrawArrays();
	}

}
