package colab.vt.whiteboard.component.state;

import java.awt.Color;
import java.awt.Graphics;

import colab.vt.whiteboard.component.WhiteboardPanel;
import colab.vt.whiteboard.component.WhiteboardContainer;
import colab.vt.whiteboard.component.XmlNames;

public class LineColorSelector extends AbstractColorSelector
{
	private static final long serialVersionUID = -2634729394571362926L;

	public LineColorSelector(WhiteboardPanel whiteboardPanel, int size)
	{
		super(whiteboardPanel, size);
		setColor(Color.black);
		setTransparent(false);
	}

	@Override
	public String getType()
	{
		return XmlNames.lineColor;
	}

	@Override
	void paintState(Graphics g, int left, int top, int right, int bottom)
	{
		super.paintState(g, left, top, right, bottom);
		g.setColor(Color.white);
		int length3 = (right - left) / 3;
		int width3 = 1+(right - left) - 2 * length3;
		g.fillRect(left + length3, top + length3, width3, width3);
	}

//	@Override
//	protected void paintComponent(Graphics g)
//	{
//		super.paintComponent(g);
//		g.setColor(Color.white);
//		int length3 = getLength() / 3;
//		int width3 = getWidth() - 2 * length3;
//		g.fillRect(length3, length3, width3, width3);
//	}
//
	@Override
	void applyColor(WhiteboardContainer whiteboardContainer)
	{
		whiteboardContainer.setLineColor(getColor());
	}

	@Override
	String getDialogTitle()
	{
		return "Select line color";
	}

	@Override
	void setWhiteboardPanelColor()
	{
		getWhiteboardPanel().setCurrentLineColor(getColor());
	}

}
