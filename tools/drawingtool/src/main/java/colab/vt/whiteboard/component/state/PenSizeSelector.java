package colab.vt.whiteboard.component.state;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import colab.vt.whiteboard.component.WhiteboardPanel;
import colab.vt.whiteboard.component.WhiteboardContainer;
import colab.vt.whiteboard.component.XmlNames;

public class PenSizeSelector extends AbstractStateSelector implements  ActionListener
{
	private static final long serialVersionUID = -7001408104730434045L;
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(PenSizeSelector.class.getName());

	private double penSize = 1;
	private JPopupMenu sizesMenu;

	public PenSizeSelector(WhiteboardPanel whiteboardPanel, int length)
	{
		super(whiteboardPanel,length);
		createSizesMenu();
	}

	private void createSizesMenu()
	{
		sizesMenu = new JPopupMenu();
		for (int i = 1; i<=10; i++)
		{
			JMenuItem menuItem = sizesMenu.add("" + i);
			menuItem.addActionListener(this);
		}
	}

	@Override
	public String getType()
	{
		return XmlNames.penSize;
	}

	@Override
	void paintState(Graphics g, int left, int top, int right, int bottom)
	{
		int displayPenSize = (int) Math.ceil(penSize);
		int drawTop = (top+bottom-displayPenSize)/2;
		g.setColor(Color.black);
		g.fillRect(left, drawTop, right - left, displayPenSize);
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		sizesMenu.show(this,e.getX(),e.getY());
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
//		logger.info("sizesMenu: " + e);
		double selectedPenSize = Double.parseDouble(e.getActionCommand());
		if (selectedPenSize!=penSize)
		{
			penSize = selectedPenSize;
			getWhiteboardPanel().setCurrentPenSize(penSize);
			repaint();
			for (WhiteboardContainer whiteboardContainer : getWhiteboardPanel()
						.getSelectedWhiteboardContainers())
			{
				whiteboardContainer.repaint();
				whiteboardContainer.setPenSize(penSize);
				whiteboardContainer.repaint();
			}
		}
	}

	public double getPenSize()
	{
		return penSize;
	}

	public void setPenSize(double penSize)
	{
		this.penSize = penSize;
	}
}
