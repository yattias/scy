package colab.vt.whiteboard.component;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class WhiteboardRectangle extends WhiteboardSimpleShape
{
	private static final long serialVersionUID = 926282463277544321L;

	public WhiteboardRectangle()
	{
		super(XmlNames.rectangle);
	}

	@Override
	public void paint(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		Rectangle bounds = getDrawBounds();
		Color fillColor = g2d.getBackground();
		Color lineColor = g2d.getColor();
		g2d.setColor(fillColor);
		g2d.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
		g2d.setColor(lineColor);
		g2d.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
//		g2d.setColor(Color.red);
//		g2d.setStroke(new BasicStroke(1));
//		g2d.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
//		int centerX = (int) Math.round(bounds.getCenterX());
//		int centerY = (int) Math.round(bounds.getCenterY());
//		g2d.drawLine(bounds.x, centerY, bounds.x+bounds.width,centerY);
//		g2d.drawLine(centerX, bounds.y, centerX,bounds.y+bounds.height);
	}
}
