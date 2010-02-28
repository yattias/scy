package colab.vt.whiteboard.component;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

import colab.vt.whiteboard.component.state.WhiteboardContainerInfoDisplay;
import colab.vt.whiteboard.utils.Cursors;

public class InfoAction extends SimpleSelectionAction
{
	private static final long serialVersionUID = 4455526181731754619L;

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(InfoAction.class.getName());

	public InfoAction(WhiteboardPanel whiteboardPanel, String label)
	{
		super(whiteboardPanel, label, Cursors.getImageIcon("iconInfo.png"));
	}

	@Override
	public String getType()
	{
		return XmlNames.info;
	}

	@Override
	public Cursor getAboveCursor()
	{
		return Cursors.getInfoCursor();
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		WhiteboardContainer selectedWhiteboardContainer = getWhiteboardPanel()
					.getWhiteboardContainerUnderMouse(e.getX(), e.getY());
		if (selectedWhiteboardContainer != null)
		{
			WhiteboardContainerInfoDisplay whiteboardObjectContainerInfoDisplay = selectedWhiteboardContainer
						.getWhiteboardContainerInfoDisplay();
			selectedWhiteboardContainer
						.addWhiteboardContainerChangedListener(whiteboardObjectContainerInfoDisplay);
			whiteboardObjectContainerInfoDisplay.setVisible(true);
			whiteboardObjectContainerInfoDisplay.requestFocus();
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
