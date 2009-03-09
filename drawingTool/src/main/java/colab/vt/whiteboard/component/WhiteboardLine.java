package colab.vt.whiteboard.component;

import java.awt.Graphics;

public class WhiteboardLine extends WhiteboardSimpleShape
{
	private static final long serialVersionUID = 3171481954693337550L;

	public WhiteboardLine()
	{
		super(XmlNames.line);
	}

	@Override
	public void paint(Graphics g)
	{
		g.drawLine(xBegin, yBegin, xEnd, yEnd);
	}
}
