package colab.vt.whiteboard.component.selection;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.logging.Logger;

import colab.vt.whiteboard.component.WhiteboardContainer;
import colab.vt.whiteboard.utils.ShiftLimitations;

public class DragMoveAction extends AbstractDragEditAction
{
	private static final long serialVersionUID = 2445930855423336577L;
	private static final Logger logger = Logger.getLogger(DragMoveAction.class.getName());

	private class MoveState
	{
		double xOffset;
		double yOffset;
	}

	private int xBegin;
	private int yBegin;
	private HashMap<WhiteboardContainer, MoveState> moveStates = new HashMap<WhiteboardContainer, MoveState>();

	@Override
	public void prepareAction()
	{
		xBegin = getStartMouseEvent().getX();
		yBegin = getStartMouseEvent().getY();
		moveStates.clear();
		for (WhiteboardContainer whiteboardContainer : getWhiteboardPanel()
					.getSelectedWhiteboardContainers())
		{
			MoveState moveState = new MoveState();
			moveState.xOffset = whiteboardContainer.getXOffset();
			moveState.yOffset = whiteboardContainer.getYOffset();
			moveStates.put(whiteboardContainer, moveState);
		}
	}

	@Override
	public void doAction(MouseEvent mouseEvent)
	{
		int xMove = mouseEvent.getX() - xBegin;
		int yMove = mouseEvent.getY() - yBegin;
		if (mouseEvent.isShiftDown())
		{
			Point shiftedMouseLocation = ShiftLimitations.getShiftLocation(xBegin, yBegin, mouseEvent
						.getX(), mouseEvent.getY());
			xMove = shiftedMouseLocation.x - xBegin;
			yMove = shiftedMouseLocation.y - yBegin;
		}
		for (WhiteboardContainer whiteboardContainer : getWhiteboardPanel()
					.getSelectedWhiteboardContainers())
		{
			MoveState moveState = moveStates.get(whiteboardContainer);
			if (moveState != null)
			{
				whiteboardContainer.repaint();
				whiteboardContainer.setXOffset(moveState.xOffset + xMove);
				whiteboardContainer.setYOffset(moveState.yOffset + yMove);
				whiteboardContainer.repaint();
			}
			else
			{
				logger.severe("could not find move state " + moveStates.size());
			}
		}
	}

	@Override
	public void finishAction(MouseEvent mouseEvent)
	{
		doAction(mouseEvent);
		moveStates.clear();
	}

}
