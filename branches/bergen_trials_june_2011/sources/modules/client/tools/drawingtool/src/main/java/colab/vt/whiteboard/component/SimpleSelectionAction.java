package colab.vt.whiteboard.component;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

import javax.swing.Icon;

public abstract class SimpleSelectionAction extends AbstractWhiteboardAction
{
	private static final long serialVersionUID = -5729463802408740604L;
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(WhiteboardPanel.class.getName());

	private Cursor defaultCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
	private Cursor aboveCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);

	private WhiteboardContainer selectedWhiteboardContainer = null;

	public SimpleSelectionAction(WhiteboardPanel whiteboardPanel, String label)
	{
		super(whiteboardPanel, label);
	}

	public SimpleSelectionAction(WhiteboardPanel whiteboardPanel, String label, Icon icon)
	{
		super(whiteboardPanel, label, icon);
	}

	@Override
	public Cursor getDefaultCursor()
	{
		return defaultCursor;
	}

	public Cursor getAboveCursor()
	{
		return aboveCursor;
	}

	public abstract Cursor getActionCursor();

	@Override
	public void mouseMoved(MouseEvent e)
	{
		WhiteboardContainer whiteboardObjectContainer = getWhiteboardPanel()
					.getWhiteboardContainerUnderMouse(e.getX(), e.getY());
		if (whiteboardObjectContainer == null)
			setCursor(getDefaultCursor());
		else
			setCursor(getAboveCursor());
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		super.mousePressed(e);
		selectedWhiteboardContainer = getWhiteboardPanel().getWhiteboardContainerUnderMouse(e.getX(),
					e.getY());
		if (selectedWhiteboardContainer != null && !selectedWhiteboardContainer.isLocked())
		{
			selectedWhiteboardContainer.setSelected(true);
			selectedWhiteboardContainer.repaint();
			prepareAction(e, selectedWhiteboardContainer);
			setCursor(getActionCursor());
		}
	}

	protected abstract void prepareAction(MouseEvent e, WhiteboardContainer whiteboardContainer);

	@Override
	public void mouseDragged(MouseEvent e)
	{
		super.mouseDragged(e);
		if (selectedWhiteboardContainer != null && !selectedWhiteboardContainer.isLocked())
		{
			// selectedWhiteboardContainer.correctMouseEventLocation(e);
			selectedWhiteboardContainer.repaint();
			if (applyAction(e, selectedWhiteboardContainer))
				selectedWhiteboardContainer.repaint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		super.mouseReleased(e);
		if (selectedWhiteboardContainer != null && !selectedWhiteboardContainer.isLocked())
		{
			// selectedWhiteboardContainer.correctMouseEventLocation(e);
			selectedWhiteboardContainer.repaint();
			if (applyAction(e, selectedWhiteboardContainer))
				selectedWhiteboardContainer.repaint();
			selectedWhiteboardContainer.setSelected(false);
			setCursor(getAboveCursor());
			selectedWhiteboardContainer = null;
		}
	}

	protected abstract boolean applyAction(MouseEvent e, WhiteboardContainer whiteboardContainer);

}
