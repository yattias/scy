package colab.vt.whiteboard.component.selection;

import java.awt.event.MouseEvent;
import java.io.Serializable;

import colab.vt.whiteboard.component.WhiteboardPanel;
import colab.vt.whiteboard.component.WhiteboardContainer;

public abstract class AbstractDragEditAction implements DragEditAction, Serializable
{
	private WhiteboardPanel whiteboardPanel;
	private WhiteboardContainer mainWhiteboardContainer;
	private MouseEvent startMouseEvent;

	@Override
	public void startEvent(WhiteboardPanel theWhiteboardPanel, WhiteboardContainer theMainWhiteboardContainer, MouseEvent theStartMouseEvent)
	{
		this.whiteboardPanel = theWhiteboardPanel;
		this.mainWhiteboardContainer = theMainWhiteboardContainer;
		this.startMouseEvent = theStartMouseEvent;
	}

	protected WhiteboardPanel getWhiteboardPanel()
	{
		return whiteboardPanel;
	}

	protected WhiteboardContainer getMainWhiteboardContainer()
	{
		return mainWhiteboardContainer;
	}

	protected MouseEvent getStartMouseEvent()
	{
		return startMouseEvent;
	}

}
