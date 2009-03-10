package colab.vt.whiteboard.component.state;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import colab.vt.whiteboard.component.WhiteboardContainer;
import colab.vt.whiteboard.component.WhiteboardPanel;
import colab.vt.whiteboard.component.XmlNames;

public class AlphaCompositeSelector extends AbstractStateSelector implements  ActionListener
{
	private static final long serialVersionUID = -7001408104730434045L;
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(PenSizeSelector.class.getName());
	
	private AlphaComposites alphaComposites = new AlphaComposites();
	private AlphaComposite alphaComposite = AlphaComposite.SrcOver;
	private JPopupMenu sizesMenu;

	public AlphaCompositeSelector(WhiteboardPanel whiteboardPanel, int length)
	{
		super(whiteboardPanel,length);
		createSizesMenu();
	}

	private void createSizesMenu()
	{
		sizesMenu = new JPopupMenu();
//		List<String> names = alphaComposites.getNames();
		for (String name : alphaComposites.getNames())
		{
			JMenuItem menuItem = sizesMenu.add(name);
			menuItem.addActionListener(this);
		}
	}

	@Override
	public String getType()
	{
		return XmlNames.alphaComposite;
	}

	@Override
	void paintState(Graphics g, int left, int top, int right, int bottom)
	{
//		int displayPenSize = (int) Math.ceil(penSize);
//		int drawTop = (top+bottom-displayPenSize)/2;
//		g.setColor(Color.black);
//		g.fillRect(left, drawTop, right - left, displayPenSize);
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
		AlphaComposite selectedAlphaComposite = alphaComposites.fromName(e.getActionCommand());
		if (selectedAlphaComposite!=alphaComposite)
		{
			alphaComposite = selectedAlphaComposite;
//			getWhiteboardPanel().setCurrentPenSize(penSize);
//			repaint();
			for (WhiteboardContainer whiteboardContainer : getWhiteboardPanel()
						.getSelectedWhiteboardContainers())
			{
				whiteboardContainer.setAlphaComposite(alphaComposite);
				whiteboardContainer.repaint();
			}
		}
	}

	public AlphaComposite getAlphaComposite()
	{
		return alphaComposite;
	}

	public void setAlphaComposite(AlphaComposite alphaComposite)
	{
		this.alphaComposite = alphaComposite;
	}
}
