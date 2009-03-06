package colab.vt.whiteboard.component.events;

import java.util.EventObject;

import colab.vt.whiteboard.component.WhiteboardContainer;
import colab.vt.whiteboard.component.WhiteboardPanel;

public class WhiteboardContainerChangedEvent extends EventObject
{
	private static final long serialVersionUID = 1691003638184549273L;
	
	private WhiteboardPanel whiteboardPanel;
	private WhiteboardContainer whiteboardContainer;

	public WhiteboardContainerChangedEvent(Object source,WhiteboardPanel whiteboardPanel,
				WhiteboardContainer whiteboardContainer)
	{
		super(source);
		this.whiteboardPanel = whiteboardPanel;
		this.whiteboardContainer = whiteboardContainer;
	}

	public WhiteboardPanel getWhiteboardPanel()
	{
		return whiteboardPanel;
	}

	public WhiteboardContainer getWhiteboardContainer()
	{
		return whiteboardContainer;
	}

}
