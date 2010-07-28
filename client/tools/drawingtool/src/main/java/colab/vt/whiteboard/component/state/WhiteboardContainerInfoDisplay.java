package colab.vt.whiteboard.component.state;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.LayoutStyle;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import colab.vt.whiteboard.component.WhiteboardContainer;
import colab.vt.whiteboard.component.WhiteboardObjectContainer;
import colab.vt.whiteboard.component.events.WhiteboardContainerChangedEvent;
import colab.vt.whiteboard.component.events.WhiteboardContainerChangedListener;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI Builder, which is free
 * for non-commercial use. If Jigloo is being used commercially (ie, by a corporation, company or
 * business for any purpose whatever) then you should purchase a license for each developer using
 * Jigloo. Please visit www.cloudgarden.com for details. Use of Jigloo implies acceptance of these
 * licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS
 * CODE CANNOT BE USED LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class WhiteboardContainerInfoDisplay extends javax.swing.JFrame implements
			WhiteboardContainerChangedListener, WindowListener
{
	private static final long serialVersionUID = -8491814223201356848L;
	private WhiteboardContainer whiteboardContainer;
	private ContainerInfoPanel2 containerInfoPanel;
	private JLabel redrawLabel;
	private JButton redrawPanelButton;
	private ObjectInfoPanel objectInfoPanel;
	private AbstractAction redrawPanelAction;
	private AbstractAction redrawObjectAction;
	private JButton redrawObjectButton;

	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				WhiteboardContainerInfoDisplay inst = new WhiteboardContainerInfoDisplay();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}

	public WhiteboardContainerInfoDisplay()
	{
		super();
		initGUI();
	}

	private void initGUI()
	{
		try
		{
			GroupLayout thisLayout = new GroupLayout(getContentPane());
			getContentPane().setLayout(thisLayout);
			this.setTitle("Object info");
			this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
			// this.addWindowListener(this);
			{
				containerInfoPanel = new ContainerInfoPanel2();
				containerInfoPanel.setBorder(BorderFactory.createTitledBorder("Container info"));
			}
			{
				redrawLabel = new JLabel();
				redrawLabel.setText("Redraw");
			}
			{
				redrawObjectButton = new JButton();
				redrawObjectButton.setText("Object");
				redrawObjectButton.setAction(getRedrawObjectAction());
			}
			{
				redrawPanelButton = new JButton();
				redrawPanelButton.setText("Panel");
				redrawPanelButton.setAction(getRedrawPanelAction());
			}
			thisLayout.setVerticalGroup(thisLayout.createSequentialGroup()
				.addContainerGap()
				.addComponent(containerInfoPanel, GroupLayout.PREFERRED_SIZE, 303, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
				.addComponent(getObjectInfoPanel(), 0, 159, Short.MAX_VALUE)
				.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
				.addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				    .addComponent(redrawObjectButton, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				    .addComponent(redrawLabel, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				    .addComponent(redrawPanelButton, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addContainerGap());
			thisLayout.setHorizontalGroup(thisLayout.createSequentialGroup()
				.addContainerGap()
				.addGroup(thisLayout.createParallelGroup()
				    .addComponent(containerInfoPanel, GroupLayout.Alignment.LEADING, 0, 303, Short.MAX_VALUE)
				    .addComponent(getObjectInfoPanel(), GroupLayout.Alignment.LEADING, 0, 303, Short.MAX_VALUE)
				    .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
				        .addPreferredGap(containerInfoPanel, redrawLabel, LayoutStyle.ComponentPlacement.INDENT)
				        .addComponent(redrawLabel, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
				        .addGap(0, 50, Short.MAX_VALUE)
				        .addComponent(redrawObjectButton, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
				        .addGap(39)
				        .addComponent(redrawPanelButton, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)))
				.addContainerGap());
			pack();
			this.setSize(335, 557);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void whiteboardContainerChanged(
				WhiteboardContainerChangedEvent whiteboardContainerChangedEvent)
	{
		if (this.whiteboardContainer == whiteboardContainer)
		{
			containerInfoPanel.valuesChanged();
			objectInfoPanel.valuesChanged();
		}
	}

	public void setWhiteboardContainer(WhiteboardContainer whiteboardContainer)
	{
		this.whiteboardContainer = whiteboardContainer;
		containerInfoPanel.setWhiteboardContainer(whiteboardContainer);
		if (whiteboardContainer instanceof WhiteboardObjectContainer)
			objectInfoPanel
						.setWhiteboardObjectContainer((WhiteboardObjectContainer) whiteboardContainer);
	}

	@Override
	public void windowActivated(WindowEvent e)
	{
	}

	@Override
	public void windowClosed(WindowEvent e)
	{
	}

	@Override
	public void windowClosing(WindowEvent e)
	{
		whiteboardContainer.removeWhiteboardContainerChangedListener(this);
	}

	@Override
	public void windowDeactivated(WindowEvent e)
	{
	}

	@Override
	public void windowDeiconified(WindowEvent e)
	{
	}

	@Override
	public void windowIconified(WindowEvent e)
	{
	}

	@Override
	public void windowOpened(WindowEvent e)
	{
	}

	private AbstractAction getRedrawObjectAction()
	{
		if (redrawObjectAction == null)
		{
			redrawObjectAction = new AbstractAction("Object", null)
			{
				private static final long serialVersionUID = 2438244220852633780L;

				public void actionPerformed(ActionEvent evt)
				{
					whiteboardContainer.repaint(false);
				}
			};
		}
		return redrawObjectAction;
	}

	private AbstractAction getRedrawPanelAction()
	{
		if (redrawPanelAction == null)
		{
			redrawPanelAction = new AbstractAction("Panel", null)
			{
				private static final long serialVersionUID = -5056892337276206627L;

				public void actionPerformed(ActionEvent evt)
				{
					whiteboardContainer.repaint(true);
				}
			};
		}
		return redrawPanelAction;
	}

	private ObjectInfoPanel getObjectInfoPanel()
	{
		if (objectInfoPanel == null)
		{
			objectInfoPanel = new ObjectInfoPanel();
		}
		return objectInfoPanel;
	}

}
