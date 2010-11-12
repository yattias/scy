package eu.scy.tools.math.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTitledPanel;

import eu.scy.tools.math.controller.MathToolController;
import eu.scy.tools.math.shapes.MathRectangle;
import eu.scy.tools.math.ui.actions.ExportToGoogleSketchUpAction;
import eu.scy.tools.math.ui.actions.QuitAction;
import eu.scy.tools.math.ui.actions.ToggleGridAction;
import eu.scy.tools.math.ui.panels.ControlPanel;
import eu.scy.tools.math.ui.panels.ShapeCanvas;

public class MathTool {
	
	private MathToolController mathToolController;
	private JXTitledPanel workAreaPanel;
	private ShapeCanvas shapeCanvas;
	private JXPanel mainPanel;

	public MathTool() {
		this.init();
	}
	
	public MathTool(MathToolController mathToolController) {
		this.mathToolController = mathToolController;
		this.init();
	}
	
	public void init() {
		
		UIUtils.componentLookup = new HashMap<String, Object>();
		UIUtils.componentLookup.put(UIUtils.MATH_TOOL_PANEL, this);
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
	
	public JComponent createMathTool(int width, int height) {
		
		if( width == 0 )
			width = 1280;
		
		if( height == 0)
			height = 800;
			
		UIUtils.frameDimension = new Dimension(width, height);
		
		setMainPanel(new JXPanel(new MigLayout("fill,inset 0 0 0 0"))); //$NON-NLS-1$
//		mainPanel.setBackground(Color.pink);
		
		getMainPanel().add(createToolBar(), "dock north"); //$NON-NLS-1$
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP,JTabbedPane.WRAP_TAB_LAYOUT);
		tabbedPane.addTab(UIUtils._2D,createLayout(UIUtils._2D));
		tabbedPane.addTab(UIUtils._3D,createLayout(UIUtils._3D));
		getMainPanel().add(tabbedPane,"grow"); //$NON-NLS-1$
		// TODO Auto-generated method stub
		return getMainPanel();
	}

	private JToolBar createToolBar() {
		JToolBar toolBar = new JToolBar("Still draggable"); //$NON-NLS-1$
		toolBar.setFloatable(false);
		toolBar.setRollover(false);
		ExportToGoogleSketchUpAction exportToGoogleSketchUpAction = new ExportToGoogleSketchUpAction();
		exportToGoogleSketchUpAction.putValue(Action.NAME, null);
		toolBar.add(new JXButton(exportToGoogleSketchUpAction));
		
		ToggleGridAction toggleGridAction = new ToggleGridAction();
		toggleGridAction.putValue(Action.NAME, null);
		toolBar.add(new JXButton(toggleGridAction));
		toolBar.setOpaque(true);
		return toolBar;
	}
	
	

	private JComponent createLayout(String type) {
		
		String insets ="6 3 6 3";
		
		JXPanel allPanel = new JXPanel(new MigLayout("fill, inset "+insets)); //$NON-NLS-1$
		
		ControlPanel controlPanel = new ControlPanel(type,new MigLayout(" inset "+insets));
		
		//40 of the width
		allPanel.add(createWorkAreaPanel(type, controlPanel), "grow,span"); //$NON-NLS-1$
		
		
		
		allPanel.add(controlPanel,"east");
		return allPanel;
	}
	

	private JXTitledPanel createWorkAreaPanel(String type, ControlPanel controlPanel) {
		workAreaPanel = new JXTitledPanel(type  + " " +"Work Area");
		UIUtils.setModTitlePanel(workAreaPanel);
		
		
		setShapeCanvas(new ShapeCanvas(true));
		getShapeCanvas().setControlPanel(controlPanel);
		getShapeCanvas().setName(UIUtils.SHAPE_CANVAS);
		getShapeCanvas().setBackground(Color.WHITE);
		
		workAreaPanel.add(getShapeCanvas());
		
//		getShapeCanvas().add(new MathToolRectangle(new java.awt.Rectangle(100, 200)));
		UIUtils.componentLookup.put(UIUtils.SHAPE_CANVAS, getShapeCanvas());
		return workAreaPanel;
	}
	
	public JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu(eu.scy.tools.math.ui.Messages.getString("MathTool.1")); //$NON-NLS-1$
		fileMenu.add(new QuitAction());
		
		menuBar.add(fileMenu);
		
		JMenu actionsMenu = new JMenu("Actions");
		
		actionsMenu.add(new ExportToGoogleSketchUpAction());
		actionsMenu.add(new ToggleGridAction());
		
		menuBar.add(actionsMenu);
		
		return menuBar;
	}

	public void setShapeCanvas(ShapeCanvas shapeCanvas) {
		this.shapeCanvas = shapeCanvas;
	}

	public ShapeCanvas getShapeCanvas() {
		return shapeCanvas;
	}

	public void setMainPanel(JXPanel mainPanel) {
		this.mainPanel = mainPanel;
	}

	public JXPanel getMainPanel() {
		return mainPanel;
	}
	
	
}
