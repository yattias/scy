package colab.vt.whiteboard.component.state;

import java.awt.Color;

import colab.vt.whiteboard.component.WhiteboardPanel;
import colab.vt.whiteboard.component.WhiteboardContainer;
import colab.vt.whiteboard.component.XmlNames;

public class FillColorSelector extends AbstractColorSelector
{
	private static final long serialVersionUID = -2634729394571362926L;

	public FillColorSelector(WhiteboardPanel whiteboardPanel, int size)
	{
		super(whiteboardPanel, size);
		setColor(new Color(255,255,255,255));
		setTransparent(true);
	}

	@Override
	public String getType()
	{
		return XmlNames.fillColor;
	}

	@Override
	void applyColor(WhiteboardContainer whiteboardContainer)
	{
		whiteboardContainer.setFillColor(getColor());
	}

	@Override
	String getDialogTitle()
	{
		return "Select fill color";
	}

	@Override
	void setWhiteboardPanelColor()
	{
		getWhiteboardPanel().setCurrentFillColor(getColor());
	}
}
