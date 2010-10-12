package colab.vt.whiteboard.component;

import java.awt.Rectangle;
import java.util.logging.Logger;

import org.jdom.Element;

import colab.vt.whiteboard.utils.XmlUtils;

public abstract class WhiteboardSimpleShape extends AbstractWhiteboardObject
{
	private static final long serialVersionUID = -2481144821433102727L;

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(WhiteboardSimpleShape.class.getName());

	protected int xBegin = 0;
	protected int yBegin = 0;
	protected int xEnd = 0;
	protected int yEnd = 0;
	private Rectangle bounds = new Rectangle();
	private Rectangle drawBounds = new Rectangle();
	private String type;

	public WhiteboardSimpleShape(String type)
	{
		super();
		this.type = type;
		calculateBounds();
	}

	@Override
	public Rectangle getBounds()
	{
		return bounds;
	}

	public Rectangle getDrawBounds()
	{
		return drawBounds;
	}

	public void setBegin(int xBegin, int yBegin)
	{
		this.xBegin = xBegin;
		this.yBegin = yBegin;
		setEnd(xBegin, yBegin);
	}

	public void setIntermediate(int x, int y)
	{
		setEnd(x, y);
	}

	public void setEnd(int xEnd, int yEnd)
	{
		this.xEnd = xEnd;
		this.yEnd = yEnd;
		calculateBounds();
	}

	protected void calculateBounds()
	{
		int xTop = xBegin;
		int width = xEnd - xBegin;
		if (width < 0)
		{
			xTop = xEnd;
			width = -width;
		}
		int yTop = yBegin;
		int height = yEnd - yBegin;
		if (height < 0)
		{
			yTop = yEnd;
			height = -height;
		}
		drawBounds = new Rectangle(xTop, yTop, width, height);
		bounds = new Rectangle(drawBounds);
		makeRectangleNotEmpty(bounds);
	}

	public void makeRectangleNotEmpty(Rectangle rectangle)
	{
		if (rectangle.width==0)
			rectangle.width = 1;
		if (rectangle.height==0)
			rectangle.height = 1;
	}

	@Override
	public String toString()
	{
		return "xBegin=" + xBegin + ",yBegin=" + yBegin + ",xEnd=" + xEnd + ",yEnd=" + yEnd
					+ ",width=" + (xEnd - xBegin) + ",height=" + (yEnd - yBegin);
	}

	@Override
	public String getType()
	{
		return type;
	}

	@Override
	public String getDescription()
	{
		return "";
	}

	public Element getStatus()
	{
		Element status = new Element(getType());
		XmlUtils.addXmlTag(status,XmlNames.xBegin,xBegin);
		XmlUtils.addXmlTag(status,XmlNames.yBegin,yBegin);
		XmlUtils.addXmlTag(status,XmlNames.xEnd,xEnd);
		XmlUtils.addXmlTag(status,XmlNames.yEnd,yEnd);
		return status;
	}

	public void setStatus(Element status)
	{
		xBegin = XmlUtils.getIntValueFromXmlTag(status, XmlNames.xBegin);
		yBegin = XmlUtils.getIntValueFromXmlTag(status, XmlNames.yBegin);
		xEnd = XmlUtils.getIntValueFromXmlTag(status, XmlNames.xEnd);
		yEnd = XmlUtils.getIntValueFromXmlTag(status, XmlNames.yEnd);
		calculateBounds();
	}

}
