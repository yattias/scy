package colab.vt.whiteboard.component;

import java.util.logging.Logger;

import colab.vt.whiteboard.utils.Cursors;

public class RectangleAction extends SimpleShapeAction
{
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(RectangleAction.class.getName());
	private static final long serialVersionUID = -8016515311739102954L;

	public RectangleAction(WhiteboardPanel whiteboardPanel, String label)
	{
		super(whiteboardPanel, label, Cursors.getImageIcon("iconRectangle.png"));
	}

	@Override
	public String getType()
	{
		return XmlNames.rectangle;
	}

	@Override
	protected WhiteboardSimpleShape createWhiteboardSimpleShape()
	{
		return new WhiteboardRectangle();
	}

}
