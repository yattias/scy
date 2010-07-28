package colab.vt.whiteboard.component;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class WhiteboardOval extends WhiteboardSimpleShape
{
	private static final long serialVersionUID = -4042922612476871872L;

	public WhiteboardOval()
	{
		super(XmlNames.oval);
	}

	@Override
	public void paint(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		Rectangle bounds = getDrawBounds();
		Color fillColor = g2d.getBackground();
		Color lineColor = g2d.getColor();
		g2d.setColor(fillColor);
		g2d.fillOval(bounds.x, bounds.y, bounds.width, bounds.height);
		g2d.setColor(lineColor);
		g2d.drawOval(bounds.x, bounds.y, bounds.width, bounds.height);
	}
}
