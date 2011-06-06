package colab.vt.whiteboard.component;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.logging.Logger;

import org.jdom.Element;

public class WhiteboardObjectContainer extends AbstractWhiteboardContainer
{
	private static final long serialVersionUID = 4492012460623242364L;
	// @SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(WhiteboardObjectContainer.class.getName());

	private WhiteboardObject whiteboardObject = null;

	public WhiteboardObjectContainer(WhiteboardPanel whiteboardPanel,
				WhiteboardObject whiteboardObject)
	{
		super(whiteboardPanel);
		this.whiteboardObject = whiteboardObject;
		createSelectionRectangles();
	}

	public WhiteboardObjectContainer(WhiteboardPanel whiteboardPanel, Element status)
	{
		super(whiteboardPanel, status);
	}

	@Override
	public String toString()
	{
		return "type=" + whiteboardObject.getType() + ",object=" + whiteboardObject.toString();
	}

	@Override
	public String getToolTipText(MouseEvent event)
	{
		String tooltipText = whiteboardObject.getToolTipText(event, getObjectMousePoint(event.getX(),
					event.getY()));
		if (tooltipText != null)
			return tooltipText;
		return super.getToolTipText(event);
	}

	@Override
	boolean contentUnderMouse(int x, int y, double withPenSize)
	{
		return whiteboardObject.objectUnderMouse(x, y, withPenSize);
	}

	@Override
	public Rectangle2D getContentBounds()
	{
		return whiteboardObject.getBounds();
	}

	@Override
	void paintContent(Graphics2D g2d)
	{
		whiteboardObject.paint(g2d);
	}

	public WhiteboardObject getWhiteboardObject()
	{
		return whiteboardObject;
	}

	@Override
	public Element getContentStatus()
	{
		return whiteboardObject.getStatus();
	}

	@Override
	public void setContentStatus(Element contentStatus)
	{
		if (whiteboardObject == null)
		{
			createWhiteboardObject(contentStatus);
		}
		if (whiteboardObject != null)
			whiteboardObject.setStatus(contentStatus);
	}

	private void createWhiteboardObject(Element contentStatus)
	{
		String objectType = contentStatus.getName();
		if (XmlNames.line.equals(objectType))
			whiteboardObject = new WhiteboardLine();
		else if (XmlNames.rectangle.equals(objectType))
			whiteboardObject = new WhiteboardRectangle();
		else if (XmlNames.oval.equals(objectType))
			whiteboardObject = new WhiteboardOval();
		else if (XmlNames.freehand.equals(objectType))
			whiteboardObject = new WhiteboardFreehand();
		else if (XmlNames.text.equals(objectType))
			whiteboardObject = new WhiteboardText(getWhiteboardPanel().getGraphics());
		else if (XmlNames.image.equals(objectType))
			whiteboardObject = new WhiteboardImage();
		else if (XmlNames.tagPointer.equals(objectType))
			whiteboardObject = new WhiteboardTagPointer(getWhiteboardPanel().getGraphics());
		else
			logger.warning("Unknown object type " + objectType);
	}

	@Override
	String getType()
	{
		return XmlNames.objectContainer;
	}

}
