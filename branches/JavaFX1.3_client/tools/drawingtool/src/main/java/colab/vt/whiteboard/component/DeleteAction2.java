package colab.vt.whiteboard.component;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import colab.vt.whiteboard.utils.Cursors;

public class DeleteAction2 extends AbstractWhiteboardAction
{
	private static final long serialVersionUID = -1773769779420093354L;
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(SelectionAction.class.getName());

	private Cursor defaultCursor = Cursors.getDeleteCursor();
	private Cursor aboveCursor = Cursors.getDeleteActionCursor();
	private BlinkWhiteboardContainersTask blinkWhiteboardContainersTask = null;
	private WhiteboardContainer blinkWhiteboardContainer = null;

	public DeleteAction2(WhiteboardPanel whiteboardPanel, String label)
	{
		super(whiteboardPanel, label, Cursors.getImageIcon("iconDelete.png"));
	}

	@Override
	public String getType()
	{
		return XmlNames.delete;
	}

	@Override
	public Cursor getDefaultCursor()
	{
		return defaultCursor;
	}

	public void setCursor(MouseEvent e)
	{
		WhiteboardContainer whiteboardContainer = getWhiteboardPanel()
					.getWhiteboardContainerUnderMouse(e.getX(), e.getY());
		if (whiteboardContainer == null || whiteboardContainer.isLocked())
			setCursor(defaultCursor);
		else
			setCursor(aboveCursor);
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		setCursor(e);
		WhiteboardContainer mouseWhiteboardContainer = getWhiteboardPanel()
					.getWhiteboardContainerUnderMouse(e.getX(), e.getY());
		if (mouseWhiteboardContainer == null || mouseWhiteboardContainer.isLocked())
		{
			stopBlinking();
		}
		else
		{
			startBlinking(mouseWhiteboardContainer);
		}
	}

	private void startBlinking(WhiteboardContainer mouseWhiteboardContainer)
	{
		if (blinkWhiteboardContainersTask == null
					|| mouseWhiteboardContainer != blinkWhiteboardContainer)
		{
			stopBlinking();
			List<WhiteboardContainer> blinkWhiteboardContainers;
			if (!mouseWhiteboardContainer.isSelected())
			{
				blinkWhiteboardContainers = new ArrayList<WhiteboardContainer>();
				blinkWhiteboardContainers.add(mouseWhiteboardContainer);
			}
			else
			{
				blinkWhiteboardContainers = getWhiteboardPanel().getSelectedWhiteboardContainers();
			}
			blinkWhiteboardContainersTask = new BlinkWhiteboardContainersTask(
						blinkWhiteboardContainers);
			blinkWhiteboardContainer = mouseWhiteboardContainer;
		}
	}

	private void stopBlinking()
	{
		if (blinkWhiteboardContainersTask != null)
		{
			blinkWhiteboardContainersTask.stop();
			blinkWhiteboardContainersTask = null;
		}
		blinkWhiteboardContainer = null;
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		super.mouseClicked(e);
		WhiteboardContainer mouseWhiteboardContainer = getWhiteboardPanel()
					.getWhiteboardContainerUnderMouse(e.getX(), e.getY());
		if (mouseWhiteboardContainer != null && !mouseWhiteboardContainer.isLocked())
		{
			deleteWhiteboardContainers(mouseWhiteboardContainer);
		}
		getWhiteboardPanel().deselectAllWhiteboardContainers();
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		super.mousePressed(e);
		WhiteboardContainer mouseWhiteboardContainer = getWhiteboardPanel()
					.getWhiteboardContainerUnderMouse(e.getX(), e.getY());
		if (mouseWhiteboardContainer != null && !mouseWhiteboardContainer.isLocked())
		{
			startBlinking(mouseWhiteboardContainer);
			// if (!mouseWhiteboardContainer.isSelected())
			// {
			// getWhiteboardPanel().deselectAllWhiteboardContainers();
			// mouseWhiteboardContainer.setSelected(true);
			// mouseWhiteboardContainer.repaint();
			// }
			// blinkWhiteboardContainersTask = new BlinkWhiteboardContainersTask(getWhiteboardPanel()
			// .getSelectedWhiteboardContainers());
		}
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		super.mouseDragged(e);
		mouseMoved(e);
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		super.mouseReleased(e);
		WhiteboardContainer mouseWhiteboardContainer = getWhiteboardPanel()
					.getWhiteboardContainerUnderMouse(e.getX(), e.getY());
		stopBlinking();
		// if (blinkWhiteboardContainersTask != null)
		// {
		// blinkWhiteboardContainersTask.stop();
		// blinkWhiteboardContainersTask = null;
		// }
		if (mouseWhiteboardContainer != null && !mouseWhiteboardContainer.isLocked())
		{
			deleteWhiteboardContainers(mouseWhiteboardContainer);
		}
		getWhiteboardPanel().deselectAllWhiteboardContainers();
	}

	private void deleteWhiteboardContainers(WhiteboardContainer mouseWhiteboardContainer)
	{
		if (mouseWhiteboardContainer.isSelected())
		{
			for (WhiteboardContainer whiteboardContainer : getWhiteboardPanel()
						.getSelectedWhiteboardContainers())
			{
				getWhiteboardPanel().deleteWhiteboardContainer(whiteboardContainer);
				whiteboardContainer.repaint();
			}
		}
		else
		{
			if (!mouseWhiteboardContainer.isLocked())
			{
				getWhiteboardPanel().deleteWhiteboardContainer(mouseWhiteboardContainer);
				mouseWhiteboardContainer.repaint();
			}
		}
	}

}
