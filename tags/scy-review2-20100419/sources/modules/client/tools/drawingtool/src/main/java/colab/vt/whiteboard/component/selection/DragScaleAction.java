package colab.vt.whiteboard.component.selection;

import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import colab.vt.whiteboard.component.ScaleRectangle;
import colab.vt.whiteboard.component.WhiteboardContainer;

public class DragScaleAction extends AbstractDragEditAction
{
	private static final long serialVersionUID = -5619656756065989132L;
	private static final Logger logger = Logger.getLogger(DragScaleAction.class.getName());

	private class ScaleState
	{
		double initialXScale;
		double initialYScale;
		Rectangle2D objectRectangle;
	}

	private boolean scaleOnX = false;
	private boolean scaleOnY = false;

	private AffineTransform ratotionTransform;
	private double centerX = 0;
	private double centerY = 0;
	private Point2D initialRotatedInputLength;
	private HashMap<WhiteboardContainer, ScaleState> scaleStates = new HashMap<WhiteboardContainer, ScaleState>();

	public DragScaleAction(ScaleRectangle scaleRectangle)
	{
		super();
		scaleOnX = scaleRectangle.isScaleOnX();
		scaleOnY = scaleRectangle.isScaleOnY();
	}

	@Override
	public void prepareAction()
	{
		ratotionTransform = new AffineTransform();
		ratotionTransform.rotate(getMainWhiteboardContainer().getRotation());
		Rectangle2D objectRectangle = getMainWhiteboardContainer().getContentBounds();
		centerX = objectRectangle.getCenterX() + getMainWhiteboardContainer().getXOffset();
		centerY = objectRectangle.getCenterY() + getMainWhiteboardContainer().getYOffset();
		initialRotatedInputLength = getInputLength(getStartMouseEvent());
		scaleStates.clear();
		for (WhiteboardContainer whiteboardContainer : getWhiteboardPanel()
					.getSelectedWhiteboardContainers())
		{
			ScaleState scaleState = new ScaleState();
			scaleState.initialXScale = whiteboardContainer.getXScale();
			scaleState.initialYScale = whiteboardContainer.getYScale();
			scaleState.objectRectangle = whiteboardContainer.getContentBounds();
			scaleStates.put(whiteboardContainer, scaleState);
		}
	}

	@Override
	public void doAction(MouseEvent mouseEvent)
	{
		Point2D rotatedInputLength = getInputLength(mouseEvent);
		double xScaleFactor = rotatedInputLength.getX() / initialRotatedInputLength.getX();
		double yScaleFactor = rotatedInputLength.getY() / initialRotatedInputLength.getY();
		boolean shiftScale = mouseEvent.isShiftDown();
		if (shiftScale)
		{
			if (scaleOnX && !scaleOnY)
				yScaleFactor = xScaleFactor;
			else if (!scaleOnX && scaleOnY)
				xScaleFactor = yScaleFactor;
			else
			{
				// scale on x and y
				xScaleFactor = (xScaleFactor + yScaleFactor) / 2;
				yScaleFactor = xScaleFactor;
			}
		}
		// xScaleFactor = checkScaleFactor(xScaleFactor);
		// yScaleFactor = checkScaleFactor(yScaleFactor);
		// System.out.println("xScaleFactor: " + xScaleFactor + ", yScaleFactor: " + yScaleFactor);

		for (WhiteboardContainer whiteboardContainer : getWhiteboardPanel()
					.getSelectedWhiteboardContainers())
		{
			ScaleState scaleState = scaleStates.get(whiteboardContainer);
			if (scaleState != null)
			{
				whiteboardContainer.repaint();
				if (scaleOnX || shiftScale)
				{
					double xScale = xScaleFactor * scaleState.initialXScale;
					whiteboardContainer.setXScale(xScale);
				}
				else if (!scaleOnX)
					whiteboardContainer.setXScale(scaleState.initialXScale);
				if (scaleOnY || shiftScale)
				{
					double yScale = yScaleFactor * scaleState.initialYScale;
					whiteboardContainer.setYScale(yScale);
				}
				else if (!scaleOnY)
					whiteboardContainer.setYScale(scaleState.initialYScale);
				whiteboardContainer.repaint();
			}
			else
			{
				logger.severe("could not find move state " + scaleStates.size());
			}
		}
	}

	// private double checkScaleFactor(double scaleFactor)
	// {
	// if (Math.abs(scaleFactor)<0.5)
	// return 0;
	// else
	// return scaleFactor;
	// }

	@Override
	public void finishAction(MouseEvent mouseEvent)
	{
		doAction(mouseEvent);
		scaleStates.clear();
	}

	private Point2D getInputLength(MouseEvent mouseEvent)
	{
		double xLength = mouseEvent.getX() - centerX;
		double yLength = mouseEvent.getY() - centerY;
		Point2D initialPoint = new Point2D.Double(xLength, yLength);
		Point2D rotatedPoint = new Point2D.Double(xLength, yLength);
		try
		{
			ratotionTransform.inverseTransform(initialPoint, rotatedPoint);
			// transform.transform(initialPoint, rotatedPoint);
		}
		catch (NoninvertibleTransformException e)
		{
			logger.log(Level.SEVERE, "problems with inverse transform of rotate compensation", e);
		}
		return rotatedPoint;
	}
}
