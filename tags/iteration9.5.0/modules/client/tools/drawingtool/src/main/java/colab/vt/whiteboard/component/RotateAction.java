package colab.vt.whiteboard.component;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.logging.Logger;

public abstract class RotateAction extends SimpleSelectionAction
{
	private static final long serialVersionUID = 6419795819625512966L;
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(RotateAction.class.getName());

	private Cursor rotateCursor = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
	private double xRotateCenter = 0;
	private double yRotateCenter = 0;
	private double initialObjectRotation = 0;
	private double initialMouseRotation = 0;

	public RotateAction(WhiteboardPanel whiteboardPanel, String label)
	{
		super(whiteboardPanel, label);
	}

	@Override
	public Cursor getActionCursor()
	{
		return rotateCursor;
	}

	@Override
	protected void prepareAction(MouseEvent e, WhiteboardContainer whiteboardContainer)
	{
		Rectangle2D contentRectangle = whiteboardContainer.getContentBounds();
		xRotateCenter = contentRectangle.getCenterX()+whiteboardContainer.getXOffset();
		yRotateCenter = contentRectangle.getCenterY()+whiteboardContainer.getYOffset();
		initialObjectRotation = whiteboardContainer.getRotation();
		initialMouseRotation = calculateRotation(e.getX(),e.getY());
	}

	@Override
	protected boolean applyAction(MouseEvent e, WhiteboardContainer whiteboardContainer)
	{
		double newMouseRotation = calculateRotation(e.getX(),e.getY());
		whiteboardContainer.setRotation(initialObjectRotation+newMouseRotation-initialMouseRotation);
		return true;
	}

	private double calculateRotation(int x,int y)
	{
		double deltaX = x-xRotateCenter;
		double deltaY = y-yRotateCenter;
		return -Math.atan(deltaX/deltaY);
	}
}
