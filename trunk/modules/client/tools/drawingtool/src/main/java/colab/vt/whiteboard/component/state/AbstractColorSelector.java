package colab.vt.whiteboard.component.state;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

import javax.swing.JColorChooser;

import org.jdom.Element;

import colab.vt.whiteboard.component.WhiteboardPanel;
import colab.vt.whiteboard.component.WhiteboardContainer;
import colab.vt.whiteboard.component.XmlNames;
import colab.vt.whiteboard.utils.XmlUtils;

public abstract class AbstractColorSelector extends AbstractStateSelector
{
	private static final long serialVersionUID = 8486667661463191360L;
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(AbstractColorSelector.class.getName());

	private Color color = Color.black;
	private boolean transparent = false;
	private boolean allowTransparent = true;
	private int alphaLevel = 255;
	private boolean mouseAbove = false;

	public AbstractColorSelector(WhiteboardPanel whiteboardPanel, int length)
	{
		super(whiteboardPanel, length);
	}

	@Override
	public Element getStatus()
	{
		Element status = super.getStatus();
		XmlUtils.addXmlTag(status, XmlNames.allowTransparent, allowTransparent);
		return status;
	}

	@Override
	public void setStatus(Element status)
	{
		super.setStatus(status);
		setAllowTransparent(XmlUtils.getBooleanValueFromXmlTag(status, XmlNames.allowTransparent));
	}

	@Override
	void paintState(Graphics g, int left, int top, int right, int bottom)
	{
		if (transparent)
		{
			g.setColor(Color.black);
			g.drawLine(left, top, right, bottom);
			g.drawLine(left, bottom, right, top);
		}
		else
		{
			g.setColor(color);
			g.fillRect(left, top, right - left, bottom - top);
		}
	}

	public Color getColor()
	{
		return color;
	}

	public void setColor(Color color)
	{
		this.alphaLevel = color.getAlpha();
		this.color = getRealColor(color);
	}

	private Color getRealColor(Color theColor)
	{
		int alpha = alphaLevel;
		if (transparent)
			alpha = 0;
		return new Color(theColor.getRed(), theColor.getGreen(), theColor.getBlue(), alpha);
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		super.mouseEntered(e);
		mouseAbove = true;
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		super.mouseExited(e);
		mouseAbove = false;
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		super.mouseClicked(e);
		if (!mouseAbove)
			return;
		Color selectedColor = null;
		if (e.isControlDown())
		{
			if (allowTransparent)
			{
				transparent = !transparent;
				selectedColor = getRealColor(color);
			}
		}
		else
		{
			selectedColor = JColorChooser.showDialog(this, getDialogTitle(), color);
			if (selectedColor != null)
				transparent = false;
		}
		if (selectedColor != null)
		{
			color = selectedColor;
			repaint();
			setWhiteboardPanelColor();
			for (WhiteboardContainer whiteboardContainer : getWhiteboardPanel()
						.getSelectedWhiteboardContainers())
			{
				applyColor(whiteboardContainer);
				whiteboardContainer.repaint();
			}
		}
	}

	abstract String getDialogTitle();

	abstract void setWhiteboardPanelColor();

	abstract void applyColor(WhiteboardContainer whiteboardContainer);

	public boolean isTransparent()
	{
		return transparent;
	}

	public void setTransparent(boolean transparent)
	{
		if (this.transparent != transparent)
		{
			this.transparent = transparent;
			color = getRealColor(color);
		}
	}

	public boolean isAllowTransparent()
	{
		return allowTransparent;
	}

	public void setAllowTransparent(boolean allowTransparent)
	{
		this.allowTransparent = allowTransparent;
		if (!allowTransparent)
		{
			if (transparent)
				setTransparent(false);

		}
	}

}
