package eu.scy.tools.math.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ToolTipManager;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.text.DefaultEditorKit;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTitledPanel;

import eu.scy.tools.math.controller.MathToolController;
import eu.scy.tools.math.dnd.JLabelSelection;
import eu.scy.tools.math.doa.json.IMathToolbarShape;
import eu.scy.tools.math.doa.json.ToolbarShape;
import eu.scy.tools.math.ui.actions.ExportToGoogleSketchUpAction;
import eu.scy.tools.math.ui.actions.FormulaHelpAction;
import eu.scy.tools.math.ui.actions.NotationHelpAction;
import eu.scy.tools.math.ui.actions.OpenShapesAction;
import eu.scy.tools.math.ui.actions.QuitAction;
import eu.scy.tools.math.ui.actions.RemoveShapeAction;
import eu.scy.tools.math.ui.actions.SaveShapesAction;
import eu.scy.tools.math.ui.actions.ToggleGridAction;
import eu.scy.tools.math.ui.images.Images;
import eu.scy.tools.math.ui.panels.ControlPanel;
import eu.scy.tools.math.ui.panels.ScratchPanel;
import eu.scy.tools.math.ui.panels.ShapeCanvas;

public class MathTool {
	
	private MathToolController mathToolController;
	private JXPanel mainPanel;

	public MathTool() {
		this.init();
	}
	
	public MathTool(MathToolController mathToolController) {
		this.setMathToolController(mathToolController);
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
		
//		if(UIUtils.isWindows()){
//			System.out.println("This is Windows");
//		}else if(UIUtils.isMac()){
//			System.out.println("This is Mac");
//		}else if(UIUtils.isUnix()){
//			System.out.println("This is Unix or Linux");
//		}else{
//			System.out.println("Your OS is not support!!");
//		}
		

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
		tabbedPane.setName(UIUtils.TABS);
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
		ExportToGoogleSketchUpAction exportToGoogleSketchUpAction = new ExportToGoogleSketchUpAction(mathToolController);
		exportToGoogleSketchUpAction.putValue(Action.NAME, null);
		toolBar.add(new JXButton(exportToGoogleSketchUpAction));
		
		ToggleGridAction toggleGridAction = new ToggleGridAction(mathToolController);
		toggleGridAction.putValue(Action.NAME, null);
		toolBar.add(new JXButton(toggleGridAction));
		toolBar.setOpaque(true);
		
		return toolBar;
	}
	
	

	private JComponent createLayout(String type) {
		
		String insets ="6 3 6 3";
		JXPanel a = new JXPanel(new BorderLayout(0,0));
		a.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
//		JXPanel allPanel = new JXPanel(new MigLayout("fill, inset "+insets)); //$NON-NLS-1$
		JXPanel allPanel = new JXPanel(new BorderLayout(5, 5)); //$NON-NLS-1$

		ControlPanel controlPanel = new ControlPanel(mathToolController,type);
		getMathToolController().addCalculator(type,controlPanel.getCalculator());
		getMathToolController().addComputationTable(type, controlPanel.getComputationTable());
		//40 of the width
		allPanel.add(createShapesPanel(type),BorderLayout.WEST);
		allPanel.add(createWorkAreaPanel(type, controlPanel), BorderLayout.CENTER); //$NON-NLS-1$
		
		JXPanel p = new JXPanel(new BorderLayout(0,0));
		
		p.add(controlPanel,BorderLayout.CENTER);
		
		allPanel.add(p, BorderLayout.EAST);
		a.add(allPanel,BorderLayout.CENTER);
		return a;
	}
	
	private JXTitledPanel createShapesPanel(String type) {
		JXTitledPanel shapePanel = new JXTitledPanel(type);
		UIUtils.setModTitlePanel(shapePanel);
		
//		JXPanel allPanel = new JXPanel(new MigLayout("inset 3 3 3 3"));
//		allPanel.setBackgroundPainter(UIUtils.getSubPanelBackgroundPainter());

//		JXPanel labelPanel = new JXPanel(new GridLayout(3, 1, 1, 3));
		
		JXPanel labelPanel = new JXPanel(new MigLayout("inset 2 2 2 2, center"));
		labelPanel.setOpaque(false);
//		List<Images> shapes = getShapes(type);
//		for (Images image : shapes) {
//			
//			JLabel label = new JLabel(image.getIcon());
//			label.setOpaque(false);
//			label.setName(image.getName());
//			label.setToolTipText(UIUtils.dragAndDropShapeTip);
//			label.setTransferHandler(new JLabelSelection());
//			
//			label.addMouseListener(new MouseAdapter(){
//			      public void mousePressed(MouseEvent e){
//			    	 
//			        JComponent jc = (JComponent)e.getSource();
//			        TransferHandler th = jc.getTransferHandler();
//			        th.exportAsDrag(jc, e, TransferHandler.COPY);
//			      }
//			    });
//			labelPanel.add(label,"wrap, center");
//			
//		}
		
		List<IMathToolbarShape> shapeList = UIUtils.getShapeList();
		for (IMathToolbarShape toolbarShape : shapeList) {
			
			if( toolbarShape.getSurfaceType().equals(type)) {
				JLabel label = new JLabel(Images.getIcon(toolbarShape.getToolbarIcon()));
				label.setOpaque(false);
				label.setToolTipText(UIUtils.dragAndDropShapeTip);
				label.putClientProperty(UIUtils.SHAPE_OBJ, toolbarShape);
				label.setName(toolbarShape.getType());
				
				label.setTransferHandler(new JLabelSelection());
				
				label.addMouseListener(new MouseAdapter(){
				      public void mousePressed(MouseEvent e){
				    	 
				        JComponent jc = (JComponent)e.getSource();
				        TransferHandler th = jc.getTransferHandler();
				        th.exportAsDrag(jc, e, TransferHandler.COPY);
				      }
				    });
				labelPanel.add(label,"wrap");
			}
		}
		
		labelPanel.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));
		
		JXPanel outerPanel = new JXPanel(new BorderLayout(0,3));
		outerPanel.add(labelPanel,BorderLayout.NORTH);
		outerPanel.setOpaque(false);
		
		JXButton trashButton = new JXButton(new RemoveShapeAction(mathToolController));
		trashButton.setText("");
		trashButton.setOpaque(false);
		trashButton.putClientProperty(UIUtils.TYPE, type);
//		trashButton.setPaintBorderInsets(false);
		trashButton.setBackgroundPainter(UIUtils.getSubPanelBackgroundPainter());
		trashButton.setBorderPainted(true);
		outerPanel.add(trashButton, BorderLayout.SOUTH);
		
		shapePanel.getContentContainer().setLayout(new MigLayout("fill, insets 0 0 0 0"));
		shapePanel.getContentContainer().add(outerPanel, "grow, center");
		
		return shapePanel;
	}

	
	private List<Images> getShapes(String type) {
		
		List<Images> shapeImages = new ArrayList<Images>();
		
		if( type.equals(UIUtils._2D)) {
			
			
			shapeImages.add(Images.Circle);
			
			shapeImages.add(Images.Triangle);
			
			shapeImages.add(Images.Rectangle);
		} else if( type.equals(UIUtils._3D)) {
			shapeImages.add(Images.Rectangle3d);
			shapeImages.add(Images.Sphere3d);
			shapeImages.add(Images.Cylinder3d);
		}
		
		return shapeImages;

	}

	private JComponent createWorkAreaPanel(String type, ControlPanel controlPanel) {
		
		
		JXTitledPanel workAreaPanel = new JXTitledPanel("Work Area");
		UIUtils.setModTitlePanel(workAreaPanel);
		
		ShapeCanvas shapeCanvas = new ShapeCanvas(true);
		shapeCanvas.setType(type);
		shapeCanvas.setControlPanel(controlPanel);
		shapeCanvas.setName(UIUtils.SHAPE_CANVAS);
		shapeCanvas.setBackground(Color.WHITE);
		
		
	getMathToolController().addCanvas(shapeCanvas);
		
		workAreaPanel.add(shapeCanvas);
		
		JXTitledPanel sPadPanel = new JXTitledPanel("Scratch Pad");
		UIUtils.setModTitlePanel(sPadPanel);
		ScratchPanel sp = new ScratchPanel(type, mathToolController);
		
		getMathToolController().addScratchPanel(sp);
		sPadPanel.add(sp);
		
		
		
//		
//		JXPanel mPanel = new JXPanel(new BorderLayout(0,2));
//		mPanel.add(shapeCanvas,BorderLayout.CENTER);
//		mPanel.add(sPadPanel,BorderLayout.SOUTH);
//		
		
		

		
		
		
//		shapeCanvas.add(new MathToolRectangle(new java.awt.Rectangle(100, 200)));
//		UIUtils.componentLookup.put(UIUtils.SHAPE_CANVAS, shapeCanvas);
		
		JSplitPane s = new JSplitPane(JSplitPane.VERTICAL_SPLIT, workAreaPanel, sPadPanel);
		s.setResizeWeight(.9);
		
		return s;
	}
	
	public JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File"); //$NON-NLS-1$
		fileMenu.add(new SaveShapesAction(getMathToolController()));
		fileMenu.add(new OpenShapesAction(getMathToolController()));
		fileMenu.add(new QuitAction());
		
		menuBar.add(fileMenu);
		
		JMenu editMenu = new JMenu("Edit"); //$NON-NLS-1$

		
		
		JMenuItem jc = new JMenuItem(new DefaultEditorKit.CopyAction());
		jc.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
		    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		
		JMenuItem jp = new JMenuItem(new DefaultEditorKit.PasteAction());
		jp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
		    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		
		JMenuItem jx = new JMenuItem(new DefaultEditorKit.CutAction());
		jx.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
		    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		
		editMenu.add(jc);
		editMenu.add(jp);
		editMenu.add(jx);
		
		menuBar.add(editMenu);
		JMenu actionsMenu = new JMenu("Actions");
		
		actionsMenu.add(new ExportToGoogleSketchUpAction(mathToolController));
		actionsMenu.add(new ToggleGridAction(mathToolController));
		
		menuBar.add(actionsMenu);
		
		JMenu helpMenu = new JMenu("Help");
		
		helpMenu.add(new NotationHelpAction());
		helpMenu.add(new FormulaHelpAction(UIUtils._2D));
		helpMenu.add(new FormulaHelpAction(UIUtils._3D));
		menuBar.add(helpMenu);
		return menuBar;
	}



	public void setMainPanel(JXPanel mainPanel) {
		this.mainPanel = mainPanel;
	}

	public JXPanel getMainPanel() {
		return mainPanel;
	}

	public void setMathToolController(MathToolController mathToolController) {
		this.mathToolController = mathToolController;
	}

	public MathToolController getMathToolController() {
		return mathToolController;
	}
	
	
}
