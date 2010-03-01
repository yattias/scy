package colab.vt.whiteboard.component;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

public abstract class MoveAction extends SimpleSelectionAction
{
	private static final long serialVersionUID = -332685518099671011L;
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(MoveAction.class.getName());

	private Cursor moveCursor = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);

	private int xBegin;
	private int yBegin;
	private double xOffset;
	private double yOffset;

	public MoveAction(WhiteboardPanel whiteboardPanel, String label)
	{
		super(whiteboardPanel, label);
	}

	@Override
	public Cursor getActionCursor()
	{
		return moveCursor;
	}

	@Override
	protected void prepareAction(MouseEvent e, WhiteboardContainer whiteboardContainer)
	{
		xBegin = e.getX();
		yBegin = e.getY();
		xOffset = whiteboardContainer.getXOffset();
		yOffset = whiteboardContainer.getYOffset();
	}

	@Override
	protected boolean applyAction(MouseEvent e, WhiteboardContainer whiteboardContainer)
	{
		whiteboardContainer.setXOffset(xOffset + e.getX() - xBegin);
		whiteboardContainer.setYOffset(yOffset + e.getY() - yBegin);
		return true;
	}

}
