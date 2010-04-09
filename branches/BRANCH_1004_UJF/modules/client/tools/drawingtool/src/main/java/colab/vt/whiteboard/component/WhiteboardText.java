package colab.vt.whiteboard.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import org.jdom.Element;

import colab.vt.whiteboard.utils.XmlUtils;

public class WhiteboardText extends AbstractWhiteboardObject
{
	private static final long serialVersionUID = 1226002985835457267L;

	private String text;
	private int x = 0;
	private int y = 0;
	private String fontName = Font.SANS_SERIF;
	private int fontSize = 12;
	private int fontStyle = Font.PLAIN;
	private Font font = null;
	private FontMetrics fontMetrics = null;
	private Rectangle bounds = new Rectangle();
	private transient Graphics graphics;

//	public WhiteboardText(String text, int x, int y, Font font, FontMetrics fontMetrics)
//	{
//		super();
//		this.text = text;
//		this.x = x;
//		this.y = y;
//		this.font = font;
//		this.fontMetrics = fontMetrics;
//		calculateBounds();
//	}

	public WhiteboardText(String text, int x, int y, Graphics graphics)
	{
		super();
		this.graphics = graphics;
		this.text = text;
		this.x = x;
		this.y = y;
		calculateBounds();
	}

	public WhiteboardText(Graphics graphics)
	{
		super();
		this.graphics = graphics;
	}

	private void calculateBounds()
	{
		if (font==null)
			font = new Font(fontName,fontStyle,fontSize);
		if (fontMetrics==null)
			fontMetrics = graphics.getFontMetrics(font);
		int width = fontMetrics.stringWidth(text);
		int height = fontMetrics.getDescent()+fontMetrics.getAscent();
		bounds = new Rectangle(x,y-height+fontMetrics.getDescent(),width,height);
	}

	@Override
	public Rectangle getBounds()
	{
		return bounds;
	}

	@Override
	public String getDescription()
	{
		return text;
	}

	@Override
	public String getType()
	{
		return XmlNames.text;
	}

	@Override
	public void paint(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		Color fillColor = g2d.getBackground();
		Color lineColor = g2d.getColor();
		g2d.setColor(fillColor);
		g2d.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
		g2d.setColor(lineColor);
		g.setFont(font);
		g.drawString(text, x, y);
//		g.drawLine(x, y, x+bounds.width, y);
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
		calculateBounds();
	}

	public Element getStatus()
	{
		Element status = new Element(getType());
		XmlUtils.addXmlTag(status,XmlNames.xBegin,x);
		XmlUtils.addXmlTag(status,XmlNames.yBegin,y);
		XmlUtils.addXmlTag(status,XmlNames.fontName,fontName);
		XmlUtils.addXmlTag(status,XmlNames.fontStyle,fontStyle);
		XmlUtils.addXmlTag(status,XmlNames.fontSize,fontSize);
		XmlUtils.addXmlTag(status,XmlNames.text,text);
		return status;
	}

	public void setStatus(Element status)
	{
		x = XmlUtils.getIntValueFromXmlTag(status, XmlNames.xBegin);
		y = XmlUtils.getIntValueFromXmlTag(status, XmlNames.yBegin);
		fontName = status.getChildText(XmlNames.fontName);
		fontStyle = XmlUtils.getIntValueFromXmlTag(status, XmlNames.fontStyle);
		fontSize = XmlUtils.getIntValueFromXmlTag(status, XmlNames.fontSize);
		text = status.getChildText(XmlNames.text);
		font = null;
		fontMetrics = null;
		calculateBounds();
	}

}
