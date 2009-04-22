package colab.vt.whiteboard.component.selection;

import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.logging.Logger;

import colab.vt.whiteboard.component.WhiteboardContainer;
import colab.vt.whiteboard.utils.ShiftLimitations;

public class DragRotateAction extends AbstractDragEditAction
{
	private static final long serialVersionUID = 7591835346947549050L;
	private static final Logger logger = Logger.getLogger(DragRotateAction.class.getName());

	private double xRotateCenter = 0;
	private double yRotateCenter = 0;
	private double initialMouseRotation = 0;
	private HashMap<WhiteboardContainer, Double> initialRotations = new HashMap<WhiteboardContainer, Double>();

	@Override
	public void prepareAction()
	{
		Rectangle2D objectRectangle = getMainWhiteboardContainer().getContentBounds();
		xRotateCenter = objectRectangle.getCenterX() + getMainWhiteboardContainer().getXOffset();
		yRotateCenter = objectRectangle.getCenterY() + getMainWhiteboardContainer().getYOffset();
		initialMouseRotation = calculateRotation(getStartMouseEvent().getX(), getStartMouseEvent()
					.getY());
		for (WhiteboardContainer whiteboardContainer : getWhiteboardPanel()
					.getSelectedWhiteboardContainers())
		{
			initialRotations.put(whiteboardContainer, whiteboardContainer.getRotation());
		}
	}

	@Override
	public void doAction(MouseEvent mouseEvent)
	{
		double newMouseRotation = calculateRotation(mouseEvent.getX(), mouseEvent.getY());
		double deltaRotation = newMouseRotation - initialMouseRotation;
		if (mouseEvent.isShiftDown())
		{
			deltaRotation = ShiftLimitations.getShiftRotation(deltaRotation);
		}

		for (WhiteboardContainer whiteboardContainer : getWhiteboardPanel()
					.getSelectedWhiteboardContainers())
		{
			Double initialWhiteboardRotation = initialRotations.get(whiteboardContainer);
			if (initialWhiteboardRotation != null)
			{
				whiteboardContainer.repaint();
				whiteboardContainer.setRotation(initialWhiteboardRotation + deltaRotation);
				whiteboardContainer.repaint();
			}
			else
			{
				logger.severe("could not find initial rotation");
			}
		}
	}

	@Override
	public void finishAction(MouseEvent mouseEvent)
	{
		doAction(mouseEvent);
		initialRotations.clear();
	}

	private double calculateRotation(int x, int y)
	{
		double deltaX = x - xRotateCenter;
		double deltaY = y - yRotateCenter;
		double rotation = -Math.atan(deltaX / deltaY);
		if (deltaY >= 0)
			rotation += Math.PI;
		// System.out.println("deltaX " + deltaX + ", deltaY " + deltaY + ", rotation " + rotation);
		return rotation;
	}

}
