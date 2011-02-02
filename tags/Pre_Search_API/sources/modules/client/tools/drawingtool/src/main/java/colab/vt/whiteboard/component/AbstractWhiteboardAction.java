package colab.vt.whiteboard.component;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JToggleButton;

import org.jdom.Element;

public abstract class AbstractWhiteboardAction extends AbstractAction implements WhiteboardAction,
			MouseMotionListener, MouseListener, MouseWheelListener, KeyListener
{
	private static final long serialVersionUID = 1009639686170770370L;

	private WhiteboardPanel whiteboardPanel;
	private boolean printEvents = false;
	private JToggleButton toggleButton = null;

	public AbstractWhiteboardAction(WhiteboardPanel whiteboardPanel, String label)
	{
		super(label);
		this.whiteboardPanel = whiteboardPanel;
	}

	public AbstractWhiteboardAction(WhiteboardPanel whiteboardPanel, String label, Icon icon)
	{
		super(label, icon);
		this.whiteboardPanel = whiteboardPanel;
	}

	public JToggleButton getToggleButton()
	{
		if (toggleButton == null)
			toggleButton = new JToggleButton(this);
		return toggleButton;
	}

	public JComponent getToolbarButton()
	{
		return getToggleButton();
	}

	public Element getStatus()
	{
		Element status = new Element(XmlNames.tool);
		status.setAttribute(XmlNames.type, getType());
		return status;
	}

	public abstract String getType();

	public void setStatus(Element status)
	{
	}

	public Cursor getDefaultCursor()
	{
		return null;
	}

	public void setCursor(Cursor cursor)
	{
		getWhiteboardPanel().setCursor(cursor);
	}

	@Override
	public String getToolTipText(MouseEvent event)
	{
		WhiteboardContainer mouseWhiteboardContainer = getWhiteboardPanel()
					.getWhiteboardContainerUnderMouse(event.getX(), event.getY());
		if (mouseWhiteboardContainer != null)
			return mouseWhiteboardContainer.getToolTipText(event);
		return null;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		getWhiteboardPanel().setCurrentAction(this);
	}

	protected WhiteboardPanel getWhiteboardPanel()
	{
		return whiteboardPanel;
	}

	public boolean isPrintMouseActions()
	{
		return printEvents;
	}

	public void setPrintEvents(boolean printEvents)
	{
		this.printEvents = printEvents;
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		printMouseEvent("mouseDragged", e);
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		// printMouseEvent("mouseMoved",e);
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		printMouseEvent("mouseClicked", e);
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		printMouseEvent("mouseEntered", e);
		setCursor(getDefaultCursor());
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		printMouseEvent("mouseExited", e);
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		printMouseEvent("mousePressed", e);
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		printMouseEvent("mouseReleased", e);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		// if (printEvents)
			// System.out.println("mouseWheelMoved: " + e);
	}

	private void printMouseEvent(String label, MouseEvent mouseEvent)
	{
		// if (printEvents)
			// System.out.println(label + ": " + mouseEvent);

	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		printKeyEvent("keyPressed", e);
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		printKeyEvent("keyReleased", e);
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		printKeyEvent("keyTyped", e);
	}

	private void printKeyEvent(String label, KeyEvent keyEvent)
	{
		// if (printEvents)
			// System.out.println(label + ": " + keyEvent);
	}

}
