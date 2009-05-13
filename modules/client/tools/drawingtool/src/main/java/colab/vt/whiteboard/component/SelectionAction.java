package colab.vt.whiteboard.component;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;

import colab.vt.whiteboard.component.selection.DragEditAction;
import colab.vt.whiteboard.component.selection.DragMoveAction;
import colab.vt.whiteboard.component.selection.DragRotateAction;
import colab.vt.whiteboard.component.selection.DragScaleAction;
import colab.vt.whiteboard.component.selection.DragSelectionAction;
import colab.vt.whiteboard.utils.Cursors;

public class SelectionAction extends AbstractWhiteboardAction
{
	private static final long serialVersionUID = -5729463802408740604L;
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(SelectionAction.class.getName());

	private DragEditAction dragEditAction = null;
	private boolean dragEditActionPrepared = false;

	private Cursor defaultCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
	// private Cursor aboveCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
	private Cursor moveCursor = Cursors.getMoveCursor();
	private Cursor rotateCursor = Cursors.getRotateCursor();
	private Cursor aboveScaleRectangleCursor = Cursors.getMoveCursor();
	private JPopupMenu popupMenu;
	private JCheckBoxMenuItem lockedMenu;
	private ChangeLockAction changeLockAction;

	class ChangeLockAction extends AbstractAction
	{
		private static final long serialVersionUID = 6065647888918097052L;

		public ChangeLockAction(String menuName)
		{
			super(menuName);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			boolean newLockedState = lockedMenu.isSelected();
			for (WhiteboardContainer whiteboardContainer : getWhiteboardPanel()
						.getSelectedWhiteboardContainers(true))
			{
				whiteboardContainer.setLocked(newLockedState);
				whiteboardContainer.repaint();
			}
		}
	}

	public SelectionAction(WhiteboardPanel whiteboardPanel, String label)
	{
		super(whiteboardPanel, label, Cursors.getImageIcon("iconMouse.png"));
		setPrintEvents(false);
		createPopupMenu();
	}

	private void createPopupMenu()
	{
		popupMenu = new JPopupMenu();
		changeLockAction = new ChangeLockAction("Locked");
		lockedMenu = new JCheckBoxMenuItem(changeLockAction);
		popupMenu.add(lockedMenu);
	}

	@Override
	public String getType()
	{
		return XmlNames.select;
	}

	@Override
	public Cursor getDefaultCursor()
	{
		return defaultCursor;
	}

	public Cursor getAboveCursor(WhiteboardContainer whiteboardContainer, MouseEvent e)
	{
		if (whiteboardContainer.isSelected())
		{
			ScaleRectangle scaleRectangle = whiteboardContainer.getScaleRectangleUnderMouse(e.getX(),
						e.getY());
			if (scaleRectangle != null)
			{
				Cursor scaleCursor = Cursors.getScaleCursor(scaleRectangle.getAngle()
							+ whiteboardContainer.getRotation());
				if (scaleCursor != null)
					return scaleCursor;
			}
			else if (whiteboardContainer.isRotateRectangleUnderMouse(e.getX(), e.getY()))
				return rotateCursor;
			return aboveScaleRectangleCursor;
		}
		return moveCursor;
	}

	public Cursor getActionCursor()
	{
		return moveCursor;
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		WhiteboardContainer whiteboardContainer = getWhiteboardPanel()
					.getWhiteboardContainerUnderMouse(e.getX(), e.getY());
		if (whiteboardContainer == null || whiteboardContainer.isLocked())
			setCursor(defaultCursor);
		else
			setCursor(getAboveCursor(whiteboardContainer, e));
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		super.mouseClicked(e);
		// WhiteboardContainer whiteboardContainer = getWhiteboardPanel()
		// .getWhiteboardContainerUnderMouse(e.getX(), e.getY());
		// if (whiteboardContainer != null)
		// {
		// whiteboardContainer.repaint();
		// if (e.isShiftDown())
		// whiteboardContainer.setSelected(true);
		// else if (e.isControlDown())
		// whiteboardContainer.setSelected(!whiteboardContainer.isSelected());
		// else
		// {
		// deselectAllWhiteboardContainers();
		// whiteboardContainer.setSelected(true);
		// }
		// whiteboardContainer.repaint();
		// }
		// else
		// deselectAllWhiteboardContainers();
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		WhiteboardContainer mouseWhiteboarContainer = getWhiteboardPanel()
					.getWhiteboardContainerUnderMouse(e.getX(), e.getY());
		// if (e.getButton() == MouseEvent.BUTTON3)
		// {
		// if (mouseWhiteboarContainer!=null)
		// {
		// lockedMenu.setSelected(mouseWhiteboarContainer.isLocked());
		// popupMenu.show(getWhiteboardPanel(),e.getX(),e.getY());
		// }
		// return;
		// }
		super.mousePressed(e);
		if (mouseWhiteboarContainer == null)
		{
			// pressed outside an object, so start a drag selection
			if (!e.isShiftDown())
				deselectAllWhiteboardContainers();
			dragEditAction = new DragSelectionAction();
		}
		else
		{
			// pressed inside an object, start move action
			if (!mouseWhiteboarContainer.isSelected())
			{
				// clicked in a not selected object
				if (!e.isShiftDown() && !e.isControlDown())
					deselectAllWhiteboardContainers();
				mouseWhiteboarContainer.repaint();
				mouseWhiteboarContainer.setSelected(true);
				mouseWhiteboarContainer.repaint();
			}
			else
			{
				if (e.isControlDown())
				{
					mouseWhiteboarContainer.repaint();
					mouseWhiteboarContainer.setSelected(!mouseWhiteboarContainer.isSelected());
					mouseWhiteboarContainer.repaint();
				}
			}
			if (e.getButton() == MouseEvent.BUTTON3)
			{
				lockedMenu.setSelected(mouseWhiteboarContainer.isLocked());
				popupMenu.show(getWhiteboardPanel(), e.getX(), e.getY());
				return;
			}

			ScaleRectangle scaleRectangle = mouseWhiteboarContainer.getScaleRectangleUnderMouse(e
						.getX(), e.getY());
			if (scaleRectangle != null)
			{
				// it's a scale action
				dragEditAction = new DragScaleAction(scaleRectangle);
			}
			else if (mouseWhiteboarContainer.isRotateRectangleUnderMouse(e.getX(), e.getY()))
			{
				// it's a rotate action
				dragEditAction = new DragRotateAction();
			}
			else
			{
				// it's a move action
				dragEditAction = new DragMoveAction();
			}
		}
		dragEditAction.startEvent(getWhiteboardPanel(), mouseWhiteboarContainer, e);
		dragEditActionPrepared = false;
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		super.mouseDragged(e);
		if (dragEditAction != null)
		{
			if (!dragEditActionPrepared)
			{
				dragEditAction.prepareAction();
				dragEditActionPrepared = true;
			}
			dragEditAction.doAction(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		if (e.getButton() == MouseEvent.BUTTON3)
		{
			return;
		}
		super.mouseReleased(e);
		if (dragEditActionPrepared)
		{
			dragEditAction.finishAction(e);
			dragEditAction = null;
		}
		else
		{
			// there was no drag, so it must be just a click
		}
		dragEditAction = null;
	}

	private void deselectAllWhiteboardContainers()
	{
		for (WhiteboardContainer whiteboardContainer : getWhiteboardPanel()
					.getSelectedWhiteboardContainers(true))
			if (whiteboardContainer.isSelected())
			{
				whiteboardContainer.repaint();
				whiteboardContainer.setSelected(false);
				whiteboardContainer.repaint();
			}
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		super.keyPressed(e);
		if (e.getKeyCode() == KeyEvent.VK_UP)
			moveSelection(0, -1);
		else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
			moveSelection(1, 0);
		else if (e.getKeyCode() == KeyEvent.VK_DOWN)
			moveSelection(0, 1);
		else if (e.getKeyCode() == KeyEvent.VK_LEFT)
			moveSelection(-1, 0);
		else if (e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
			deleteSelection();
	}

	private void moveSelection(double xStep, double yStep)
	{
		if (dragEditAction == null)
			for (WhiteboardContainer whiteboardContainer : getWhiteboardPanel()
						.getSelectedWhiteboardContainers())
			{
				whiteboardContainer.repaint();
				if (xStep != 0)
					whiteboardContainer.setXOffset(whiteboardContainer.getXOffset() + xStep);
				if (yStep != 0)
					whiteboardContainer.setYOffset(whiteboardContainer.getYOffset() + yStep);
				whiteboardContainer.repaint();
			}
	}

	private void deleteSelection()
	{
		for (WhiteboardContainer whiteboardContainer : getWhiteboardPanel()
					.getSelectedWhiteboardContainers())
		{
			getWhiteboardPanel().deleteWhiteboardContainer(whiteboardContainer);
			whiteboardContainer.repaint();
		}
	}

}
