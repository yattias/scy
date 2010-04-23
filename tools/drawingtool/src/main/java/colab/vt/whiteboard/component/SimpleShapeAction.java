package colab.vt.whiteboard.component;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

import javax.swing.Icon;

import colab.vt.whiteboard.utils.ShiftLimitations;

public abstract class SimpleShapeAction extends AbstractWhiteboardAction
{
	//@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(SimpleShapeAction.class.getName());
	private static final long serialVersionUID = -2147504761781348667L;

	private WhiteboardSimpleShape simpleShape = null;
	private WhiteboardObjectContainer whiteboardObjectContainer = null;

	public SimpleShapeAction(WhiteboardPanel whiteboardPanel, String label)
	{
		super(whiteboardPanel, label);
		setPrintEvents(false);
	}

	public SimpleShapeAction(WhiteboardPanel whiteboardPanel, String label, Icon icon)
	{
		super(whiteboardPanel, label, icon);
		setPrintEvents(false);
	}

	@Override
	public Cursor getDefaultCursor()
	{
		return Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		super.mousePressed(e);
		getWhiteboardPanel().deselectAllWhiteboardContainers();
		simpleShape = createWhiteboardSimpleShape();
		whiteboardObjectContainer = new WhiteboardObjectContainer(getWhiteboardPanel(), simpleShape);
		getWhiteboardPanel().setTemporaryWhiteboardContainer(whiteboardObjectContainer);
		simpleShape.setBegin(e.getX(), e.getY());
	}

	protected abstract WhiteboardSimpleShape createWhiteboardSimpleShape();

	@Override
	public void mouseDragged(MouseEvent e)
	{
		super.mousePressed(e);
		if (simpleShape != null)
		{
			mouseDragAction(e);
		}
		else
			logger.warning("unexpected mouseDragged");
	}

	protected void mouseDragAction(MouseEvent e)
	{
		whiteboardObjectContainer.repaint();
		Point processedMouseLocation = getProcessedMouseLocation(e);
		simpleShape.setIntermediate(processedMouseLocation.x, processedMouseLocation.y);
		// whiteboardObjectContainer.sendWhiteboardContainerChangedEvent();
		whiteboardObjectContainer.repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		super.mousePressed(e);
		if (simpleShape != null)
		{
			// whiteboardObjectContainer.correctMouseEventLocation(e);
			whiteboardObjectContainer.repaint();
			Point processedMouseLocation = getProcessedMouseLocation(e);
			simpleShape.setEnd(processedMouseLocation.x, processedMouseLocation.y);
			getWhiteboardPanel()
						.makeTemporaryWhiteboardContainerFinal(whiteboardObjectContainer, true);
			whiteboardObjectContainer.repaint();
			simpleShape = null;
			whiteboardObjectContainer = null;
		}
		else
			logger.warning("unexpected mouseReleased");
	}

	protected Point getProcessedMouseLocation(MouseEvent e)
	{
		int x = e.getX();
		int y = e.getY();
		if (e.isShiftDown())
		{
			Point shiftMouseLocation = getShiftMouseLocation(e);
			x = shiftMouseLocation.x;
			y = shiftMouseLocation.y;
		}
		return new Point(x, y);
	}

	protected Point getShiftMouseLocation(MouseEvent e)
	{
		return ShiftLimitations.getShiftLocationCross(simpleShape.xBegin,simpleShape.yBegin, e.getX(),e.getY());
//		int x = e.getX();
//		int y = e.getY();
//		int deltaX = x - simpleShape.xBegin;
//		int deltaY = y - simpleShape.yBegin;
//		int delta = (Math.abs(deltaX) + Math.abs(deltaY)) / 2;
//		if (deltaX < 0)
//			deltaX = -delta;
//		else
//			deltaX = delta;
//		if (deltaY < 0)
//			deltaY = -delta;
//		else
//			deltaY = delta;
//		x = simpleShape.xBegin + deltaX;
//		y = simpleShape.yBegin + deltaY;
//		return new Point(x, y);
	}

	protected WhiteboardSimpleShape getSimpleShape()
	{
		return simpleShape;
	}

	protected WhiteboardObjectContainer getWhiteboardObjectContainer()
	{
		return whiteboardObjectContainer;
	}
}
