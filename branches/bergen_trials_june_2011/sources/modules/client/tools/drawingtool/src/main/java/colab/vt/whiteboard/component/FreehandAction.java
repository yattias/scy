package colab.vt.whiteboard.component;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;

import colab.vt.whiteboard.utils.Cursors;

public class FreehandAction extends SimpleShapeAction
{
	private static final long serialVersionUID = -723840450152345699L;

	public FreehandAction(WhiteboardPanel whiteboardPanel, String label)
	{
		super(whiteboardPanel, label, Cursors.getImageIcon("iconPen.png"));
	}

	@Override
	public String getType()
	{
		return XmlNames.freehand;
	}

	@Override
	public Cursor getDefaultCursor()
	{
		return Cursors.getPenCursor();
	}

	@Override
	protected void mouseDragAction(MouseEvent e)
	{
		if (getWhiteboardObjectContainer().getFillColor().getAlpha() == 0)
		{
			// nothing to fill, so we can do a smart draw
			WhiteboardFreehand whiteboardFreehand = (WhiteboardFreehand) getSimpleShape();
			Point lastPoint = whiteboardFreehand.getLastPoint();
			whiteboardFreehand.setIntermediate(e.getX(), e.getY());
			int left = Math.min(lastPoint.x, e.getX());
			int top = Math.min(lastPoint.y, e.getY());
			int right = Math.max(lastPoint.x, e.getX());
			int bottom = Math.max(lastPoint.y, e.getY());
			int width = right - left;
			int height = bottom - top;
			// System.out.println("left:" + left + ", top:" + top + ", width:" + width + ", height:" +
			// height);
			getWhiteboardObjectContainer().repaint(left, top, width, height);
		}
		else
		{
			// the freehand is filled, so don't do a smart redraw
			super.mouseDragAction(e);
		}
//		getWhiteboardObjectContainer().sendWhiteboardContainerChangedEvent();
	}

	@Override
	protected Point getShiftMouseLocation(MouseEvent e)
	{
		return new Point(e.getX(),e.getY());
	}

	@Override
	protected WhiteboardSimpleShape createWhiteboardSimpleShape()
	{
		return new WhiteboardFreehand();
	}

}
