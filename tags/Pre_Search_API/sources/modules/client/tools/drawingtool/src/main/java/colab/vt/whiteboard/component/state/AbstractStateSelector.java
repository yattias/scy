package colab.vt.whiteboard.component.state;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;

import org.jdom.Element;

import colab.vt.whiteboard.component.WhiteboardPanel;
import colab.vt.whiteboard.component.WhiteboardAction;
import colab.vt.whiteboard.component.XmlNames;

public abstract class AbstractStateSelector extends JComponent implements MouseListener,
			WhiteboardAction
{
	private static final long serialVersionUID = 5378163467976146492L;

	private WhiteboardPanel whiteboardPanel;
	private boolean printMouseActions = false;

	public AbstractStateSelector(WhiteboardPanel whiteboardPanel, int length)
	{
		this.whiteboardPanel = whiteboardPanel;
		Dimension dimension = new Dimension(length, length);
		setMinimumSize(dimension);
		setPreferredSize(dimension);
		setMaximumSize(dimension);
		setBorder(new EmptyBorder(0, 1, 1, 2));
		addMouseListener(this);
	}

	public JToggleButton getToggleButton()
	{
		return null;
	}

	public JComponent getToolbarButton()
	{
		return this;
	}

	public Cursor getDefaultCursor()
	{
		return null;
	}

	public Element getStatus()
	{
		Element status = new Element(XmlNames.tool);
		status.setAttribute(XmlNames.type, getType());
		return status;
	}

	abstract String getType();

	public void setStatus(Element status)
	{
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		Insets insets = getInsets();
		int left = insets.left;
		int top = insets.top;
		int right = getWidth() - insets.right;
		int bottom = getWidth() - insets.bottom - 1;

		// g.setColor(Color.white);
		// g.fillRect(left, top, right - left, bottom - top);

		paintState(g, left, top, right, bottom);

		g.setColor(Color.black);
		g.drawRect(left, top, right - left, bottom - top);
	}

	abstract void paintState(Graphics g, int left, int top, int right, int bottom);

	@Override
	public void mouseClicked(MouseEvent e)
	{
		printMouseEvent("mouseClicked", e);
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		printMouseEvent("mouseEntered", e);
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		printMouseEvent("mouseExited", e);
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

	private void printMouseEvent(String label, MouseEvent mouseEvent)
	{
		// if (printMouseActions)
			// System.out.println(label + ": " + mouseEvent);

	}

	public WhiteboardPanel getWhiteboardPanel()
	{
		return whiteboardPanel;
	}

	public boolean isPrintMouseActions()
	{
		return printMouseActions;
	}

	public void setPrintMouseActions(boolean printMouseActions)
	{
		this.printMouseActions = printMouseActions;
	}

}
