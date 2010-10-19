package eu.scy.tools.math.ui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;

import eu.scy.tools.math.controller.MathToolController;
import eu.scy.tools.math.ui.images.Images;

public class MathTool {

	private static final String _3D = "3D";
	private static final String _2D = "2D";
	private MathToolController mathToolController;
	private boolean hasMenu;
	private boolean hasToolbar;


	public MathTool() {
		this.init();
	}
	
	public MathTool(MathToolController mathToolController) {
		this.mathToolController = mathToolController;
		this.init();
	}
	
	public void init() {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) { //$NON-NLS-1$
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			// If Nimbus is not available, you can set the GUI to another look
			// and feel.
		}

		// Get current delay
		int initialDelay = ToolTipManager.sharedInstance().getInitialDelay();

		// Show tool tips immediately
		ToolTipManager.sharedInstance().setInitialDelay(0);

		// Show tool tips after a second
		initialDelay = 1000;
		ToolTipManager.sharedInstance().setInitialDelay(initialDelay);
		ToolTipManager.sharedInstance().setDismissDelay(initialDelay * 4);
	}
	
	public JComponent createMathTool() {
		
		JXPanel mainPanel = new JXPanel(new MigLayout("fill,inset 0 0 0 0"));
		mainPanel.setBackground(Color.pink);
		
		mainPanel.add(createToolBar(), "dock north");
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP,JTabbedPane.WRAP_TAB_LAYOUT);
		tabbedPane.addTab(_2D, createLayout(_2D));
		tabbedPane.addTab(_3D,createLayout(_3D));
		mainPanel.add(tabbedPane,"grow");
		// TODO Auto-generated method stub
		return mainPanel;
	}

	private JToolBar createToolBar() {
		JToolBar toolBar = new JToolBar("Still draggable");
		toolBar.add(new JXButton(Images.SketchUp.getIcon()));
		toolBar.setOpaque(true);
		return toolBar;
	}

	private JComponent createLayout(String string) {
		
		JXPanel allPanel = new JXPanel(new MigLayout("inset 0 0 0 0"));
		
		JXPanel canvasPanel = new JXPanel();
		JXPanel mathPanel = new JXPanel();
		mathPanel.add(new JXLabel("bob"));
		
		allPanel.add(canvasPanel, "growx 100");
		allPanel.add(mathPanel,"growx 100");
		return allPanel;
	}

	public void hasMenu(boolean hasMenu) {
		this.hasMenu = hasMenu;
	}

	public void hasToolbar(boolean hasToolbar) {
		this.hasToolbar = hasToolbar;
	}
}
