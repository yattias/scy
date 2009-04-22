package colab.vt.whiteboard.component.events;

import org.jdom.Element;

import colab.vt.whiteboard.component.WhiteboardContainer;
import colab.vt.whiteboard.component.WhiteboardPanel;

public class WhiteboardContainerListChangedEvent extends WhiteboardContainerChangedEvent
{
	private static final long serialVersionUID = -7389076400870695128L;

	private Element xml;

	public WhiteboardContainerListChangedEvent(Object source, WhiteboardPanel whiteboardPanel,
				WhiteboardContainer whiteboardContainer, Element xml)
	{
		super(source, whiteboardPanel, whiteboardContainer);
		this.xml = xml;
	}

	public Element getXml()
	{
		return xml;
	}

}
