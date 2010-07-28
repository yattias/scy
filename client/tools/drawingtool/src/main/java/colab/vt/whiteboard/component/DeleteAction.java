package colab.vt.whiteboard.component;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

public abstract class DeleteAction extends SimpleSelectionAction
{
	private static final long serialVersionUID = -8054064844858415977L;
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(DeleteAction.class.getName());

	private Cursor aboveCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);

	public DeleteAction(WhiteboardPanel whiteboardPanel, String label)
	{
		super(whiteboardPanel, label);
	}

	@Override
	public Cursor getAboveCursor()
	{
		return aboveCursor;
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		WhiteboardContainer selectedWhiteboardContainer = getWhiteboardPanel()
					.getWhiteboardContainerUnderMouse(e.getX(), e.getY());
		if (selectedWhiteboardContainer != null)
		{
			getWhiteboardPanel().deleteWhiteboardContainer(selectedWhiteboardContainer);
		}
	}

	@Override
	protected boolean applyAction(MouseEvent e, WhiteboardContainer whiteboardContainer)
	{
		return false;
	}

	@Override
	public Cursor getActionCursor()
	{
		return null;
	}

	@Override
	protected void prepareAction(MouseEvent e, WhiteboardContainer whiteboardContainer)
	{
	}

}
