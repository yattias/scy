package colab.vt.whiteboard.component;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

import colab.vt.whiteboard.utils.Cursors;
import colab.vt.whiteboard.utils.ShiftLimitations;

public class LineAction extends SimpleShapeAction
{
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(LineAction.class.getName());
	private static final long serialVersionUID = -5439750977364612691L;

	public LineAction(WhiteboardPanel whiteboardPanel, String label)
	{
		super(whiteboardPanel, label, Cursors.getImageIcon("iconLine.png"));
		setPrintEvents(false);
	}

	@Override
	public String getType()
	{
		return XmlNames.line;
	}

	@Override
	protected WhiteboardSimpleShape createWhiteboardSimpleShape()
	{
		return new WhiteboardLine();
	}

	@Override
	protected Point getShiftMouseLocation(MouseEvent e)
	{
		return ShiftLimitations.getShiftLocation(getSimpleShape().xBegin,getSimpleShape().yBegin, e.getX(),e.getY());
//		int x = e.getX();
//		int y = e.getY();
//		int deltaX = x - getSimpleShape().xBegin;
//		int deltaY = y - getSimpleShape().yBegin;
//		if (Math.abs(deltaX) < Math.abs(deltaY) / 2)
//		{
//			deltaX = 0;
//		}
//		else if (Math.abs(deltaY) < Math.abs(deltaX) / 2)
//		{
//			deltaY = 0;
//		}
//		else
//		{
//			int delta = (Math.abs(deltaX) + Math.abs(deltaY)) / 2;
//			if (deltaX < 0)
//				deltaX = -delta;
//			else
//				deltaX = delta;
//			if (deltaY < 0)
//				deltaY = -delta;
//			else
//				deltaY = delta;
//		}
//		x = getSimpleShape().xBegin + deltaX;
//		y = getSimpleShape().yBegin + deltaY;
//		return new Point(x, y);
	}

}
