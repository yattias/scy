package colab.vt.whiteboard.component;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import colab.vt.whiteboard.utils.Cursors;

public class SimpleTextAction extends SimpleSelectionAction
{
	private static final long serialVersionUID = 3752188223736154889L;

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(SimpleTextAction.class.getName());

	public SimpleTextAction(WhiteboardPanel whiteboardPanel, String label)
	{
		super(whiteboardPanel, label, Cursors.getImageIcon("iconText.png"));
	}

	@Override
	public String getType()
	{
		return XmlNames.text;
	}

	@Override
	public Cursor getDefaultCursor()
	{
		return Cursors.getTextCursor();
	}

	@Override
	public Cursor getAboveCursor()
	{
		return Cursors.getTextEditCursor();
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		getWhiteboardPanel().deselectAllWhiteboardContainers();
		WhiteboardContainer mouseWhiteboardContainer = getWhiteboardPanel()
					.getWhiteboardContainerUnderMouse(e.getX(), e.getY());
		if (mouseWhiteboardContainer != null)
		{
			if (mouseWhiteboardContainer instanceof WhiteboardObjectContainer)
			{
				WhiteboardObjectContainer mouseWhiteboardObjectContainer = (WhiteboardObjectContainer) mouseWhiteboardContainer;
				if (mouseWhiteboardObjectContainer.getWhiteboardObject() instanceof WhiteboardText)
				{
					WhiteboardText whiteboardText = (WhiteboardText) mouseWhiteboardObjectContainer
								.getWhiteboardObject();
					String newText = JOptionPane.showInputDialog(getWhiteboardPanel(), "Edit text",
								whiteboardText.getText());
					if (newText != null && newText.length() > 0)
					{
						mouseWhiteboardObjectContainer.repaint();
						whiteboardText.setText(newText);
						mouseWhiteboardObjectContainer.repaint();
					}
					return;
				}
			}
		}
		String text = JOptionPane.showInputDialog(getWhiteboardPanel(), "Enter text");
		if (text != null && text.length() > 0)
		{
			// Font font = getWhiteboardPanel().getGraphics().getFont();
			// FontMetrics fontMetrics = getWhiteboardPanel().getGraphics().getFontMetrics();
			// WhiteboardText whiteboardText = new WhiteboardText(text, e.getX(), e.getY(), font,
			// fontMetrics);
			WhiteboardText whiteboardText = new WhiteboardText(text, e.getX(), e.getY(),
						getWhiteboardPanel().getGraphics());
			WhiteboardObjectContainer whiteboardObjectContainer = new WhiteboardObjectContainer(
						getWhiteboardPanel(), whiteboardText);
			getWhiteboardPanel().setTemporaryWhiteboardContainer(whiteboardObjectContainer);
			getWhiteboardPanel()
						.makeTemporaryWhiteboardContainerFinal(whiteboardObjectContainer, true);
			whiteboardObjectContainer.repaint();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		super.mouseClicked(e);
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
