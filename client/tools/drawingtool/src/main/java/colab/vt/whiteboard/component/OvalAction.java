package colab.vt.whiteboard.component;

import java.util.logging.Logger;

import colab.vt.whiteboard.utils.Cursors;

public class OvalAction extends SimpleShapeAction
{
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(OvalAction.class.getName());
	private static final long serialVersionUID = -1337362274775143472L;

	public OvalAction(WhiteboardPanel whiteboardPanel, String label)
	{
		super(whiteboardPanel, label, Cursors.getImageIcon("iconOval.png"));
		setPrintEvents(false);
	}

	@Override
	public String getType()
	{
		return XmlNames.oval;
	}

	@Override
	protected WhiteboardSimpleShape createWhiteboardSimpleShape()
	{
		return new WhiteboardOval();
	}
}
