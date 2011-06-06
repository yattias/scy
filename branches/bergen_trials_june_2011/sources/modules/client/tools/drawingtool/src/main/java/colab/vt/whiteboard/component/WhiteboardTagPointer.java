package colab.vt.whiteboard.component;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import org.jdom.Element;

import colab.vt.whiteboard.utils.XmlUtils;

public class WhiteboardTagPointer extends WhiteboardSimpleShape
{
	private static final long serialVersionUID = -3512722897699578147L;
	private Color color;
	private String tag;
	private String tooltip;
	private int circleRadius = 5;
	private int lineWidth = 2;
	private String fontName = Font.SANS_SERIF;
	private int fontSize = 18;
	private int fontStyle = Font.BOLD;
	private Font font = null;
	private FontMetrics fontMetrics = null;
	private transient Graphics graphics;
	private Rectangle tagBounds;
	private int xTextOffset = 0;
	private int yTextOffset = 0;

	public WhiteboardTagPointer(String tag, Color color, String tooltip, Graphics graphics)
	{
		super(XmlNames.tagPointer);
		this.tag = tag;
		this.color = color;
		this.tooltip = tooltip;
		this.graphics = graphics;
	}

	public WhiteboardTagPointer(Graphics graphics)
	{
		super(XmlNames.tagPointer);
		this.graphics = graphics;
	}

	@Override
	public String getToolTipText(MouseEvent event, Point mouseLocation)
	{
		if (isLocationInTagBounds(mouseLocation))
			return tooltip;
		return null;
	}
	
	boolean isLocationInTagBounds(Point location)
	{
		return getTagBounds().contains(location.x, location.y);
	}

	Rectangle getTagBounds()
	{
		if (tagBounds == null)
		{
			if (font == null)
				font = new Font(fontName, fontStyle, fontSize);
			if (fontMetrics == null)
				fontMetrics = graphics.getFontMetrics(font);
			int width = fontMetrics.stringWidth(tag);
			width = Math.max(width, fontSize);
			int height = fontMetrics.getDescent() + fontMetrics.getAscent();
			xTextOffset = 3;
			yTextOffset = fontMetrics.getAscent() - 1;
			tagBounds = new Rectangle(0, -height + fontMetrics.getDescent(), width + xTextOffset + 1,
						height);
		}
		return tagBounds;
	}

	@Override
	public Rectangle getBounds()
	{
		Rectangle baseRect = super.getBounds();
		Rectangle tagRect = getTagBounds();
		tagRect.setLocation(xEnd, yEnd);
		Rectangle bounds = baseRect.union(tagRect);
		int correction = Math.max(circleRadius - 1, lineWidth - 1);
		return new Rectangle(bounds.x - correction, bounds.y - correction, bounds.width + 2
					* correction, bounds.height + 2 * correction);
	}

	@Override
	public void paint(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		g.setColor(color);
		g2d.setStroke(new BasicStroke(lineWidth));
		g.fillOval(xBegin - circleRadius, yBegin - circleRadius, 2 * circleRadius, 2 * circleRadius);
		Point lineEnd = getLineEnd();
		g.drawLine(xBegin, yBegin, lineEnd.x, lineEnd.y);
		g.drawRect(xEnd, yEnd, getTagBounds().width, getTagBounds().height);
		g.setFont(font);
		g.drawString(tag, xEnd + xTextOffset, yEnd + yTextOffset);
	}

	private Point getLineEnd()
	{
		int xLineEnd = getTagBounds().x;
		int yLineEnd = getTagBounds().y;
		double topLeftDistance = calculateDistance(xBegin, yBegin, getTagBounds().x, getTagBounds().y);
		double minimumDistance = topLeftDistance;
		double topRightdistance = calculateDistance(xBegin, yBegin, getTagBounds().x + getTagBounds().width,
					getTagBounds().y);
		if (topRightdistance < minimumDistance)
		{
			xLineEnd = getTagBounds().x + getTagBounds().width;
			yLineEnd = getTagBounds().y;
			minimumDistance = topRightdistance;
		}
		double bottomRightdistance = calculateDistance(xBegin, yBegin, getTagBounds().x + getTagBounds().width,
					getTagBounds().y + getTagBounds().height);
		if (bottomRightdistance < minimumDistance)
		{
			xLineEnd = getTagBounds().x + getTagBounds().width;
			yLineEnd = getTagBounds().y + getTagBounds().height;
			minimumDistance = bottomRightdistance;
		}
		double bottomLeftdistance = calculateDistance(xBegin, yBegin, getTagBounds().x,
					getTagBounds().y + getTagBounds().height);
		if (bottomLeftdistance < minimumDistance)
		{
			xLineEnd = getTagBounds().x;
			yLineEnd = getTagBounds().y + getTagBounds().height;
			minimumDistance = bottomLeftdistance;
		}
		return new Point(xLineEnd, yLineEnd);
	}

	private double calculateDistance(int x1, int y1, int x2, int y2)
	{
		return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
	}
	
	@Override
	public Element getStatus()
	{
		Element status = super.getStatus();
//		XmlUtils.addXmlTag(status,XmlNames.fontName,fontName);
//		XmlUtils.addXmlTag(status,XmlNames.fontStyle,fontStyle);
//		XmlUtils.addXmlTag(status,XmlNames.fontSize,fontSize);
		XmlUtils.addXmlTag(status,XmlNames.tag,tag);
		XmlUtils.addXmlTag(status,XmlNames.color,color);
		XmlUtils.addXmlTag(status,XmlNames.tooltip,tooltip);
		return status;
	}

	@Override
	public void setStatus(Element status)
	{
		super.setStatus(status);
//		fontName = status.getChildText(XmlNames.fontName);
//		fontStyle = XmlUtils.getIntValueFromXmlTag(status, XmlNames.fontStyle);
//		fontSize = XmlUtils.getIntValueFromXmlTag(status, XmlNames.fontSize);
//		font = null;
//		fontMetrics = null;
		tag = status.getChildText(XmlNames.tag);
		color = XmlUtils.getColorValueFromXmlTag(status, XmlNames.color);
		tooltip = status.getChildText(XmlNames.tooltip);
		tagBounds = null;
//		calculateBounds();
	}
	
	public void setTagDefinition(TagDefinition tagDefinition)
	{
		tag = tagDefinition.getTag();
		color = tagDefinition.getColor();
		tooltip = tagDefinition.getTooltip();
		tagBounds = null;
	}

}
