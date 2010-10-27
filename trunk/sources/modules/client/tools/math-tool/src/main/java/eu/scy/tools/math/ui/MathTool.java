package eu.scy.tools.math.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LinearGradientPaint;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.LineBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTextField;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.painter.GlossPainter;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.painter.Painter;

import eu.scy.tools.math.controller.MathToolController;
import eu.scy.tools.math.ui.actions.ExportToGoogleSketchUp;
import eu.scy.tools.math.ui.actions.QuitAction;

public class MathTool {

	private static final String _3D = "3D"; //$NON-NLS-1$
	private static final String _2D = "2D"; //$NON-NLS-1$
	private MathToolController mathToolController;
	private Dimension frameDimension;
	private JXTitledPanel tableArea;
	private JXTitledPanel calcPanel;
	private JXTitledPanel shapePanel;
	private JXTitledPanel workAreaPanel;
	private ArrayList<JXButton> symbolicButtons;
	private ArrayList<JXButton> adderButtons;
	private ArrayList<JXButton> numberButtons;

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
	
	public JComponent createMathTool(int width, int height) {
		
		if( width == 0 )
			width = 1280;
		
		if( height == 0)
			height = 800;
			
		frameDimension = new Dimension(width, height);
		
		JXPanel mainPanel = new JXPanel(new MigLayout("fill,inset 0 0 0 0")); //$NON-NLS-1$
//		mainPanel.setBackground(Color.pink);
		
		mainPanel.add(createToolBar(), "dock north"); //$NON-NLS-1$
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP,JTabbedPane.WRAP_TAB_LAYOUT);
		tabbedPane.addTab(_2D, createLayout(_2D));
		tabbedPane.addTab(_3D,createLayout(_3D));
		mainPanel.add(tabbedPane,"grow"); //$NON-NLS-1$
		// TODO Auto-generated method stub
		return mainPanel;
	}

	private JToolBar createToolBar() {
		JToolBar toolBar = new JToolBar("Still draggable"); //$NON-NLS-1$
		toolBar.add(new JXButton(new ExportToGoogleSketchUp()));
		toolBar.setOpaque(true);
		return toolBar;
	}
	
	

	private JComponent createLayout(String string) {
		
		String insets ="6 3 6 3";
		
		JXPanel allPanel = new JXPanel(new MigLayout("fill, inset "+insets)); //$NON-NLS-1$
		
//		allPanel.setBackground(Color.blue);
		
		//40 of the width
		allPanel.add(createWorkAreaPanel(string + " " +"Work Area"), "grow,span"); //$NON-NLS-1$
		
		
		JXPanel subPanel = new JXPanel(new MigLayout(" inset "+insets));
		
		
		subPanel.add(createShapesPanel(string + " " +"Shapes"), "growx, wrap"); //$NON-NLS-1$
		subPanel.add(createCalculatorPanel(),"wrap"); //$NON-NLS-1$
		subPanel.add(createTableArea(),"grow, span"); //$NON-NLS-1$
		allPanel.add(subPanel,"east");
		
		//30
		
		return allPanel;
	}
	


	private JXTitledPanel createTableArea() {
		tableArea = new JXTitledPanel("Computations");
		this.setModTitlePanel(tableArea);
		JXPanel allPanel = new JXPanel(new MigLayout("fill, inset 0 0 0 0"));
		
		allPanel.add(createTable(), "grow");
		
		tableArea.add(allPanel);
		return tableArea;
	}
	
	private JXPanel createTable() {
	    // boilerplate table-setup; this would be the same for a JTable
//	    ComputationTableModel model = new ComputationTableModel(6, new String[] {"Shape", "Computation"});
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("Name");
		model.addColumn("Value");
		String[] socrates = { "Socrates",  "469-399 B.C." };
	    model.addRow(socrates);
	    model.addRow(socrates);
	    model.addRow(socrates);
	    model.addRow(socrates);


	    JXTable table = new JXTable();
//	    model.loadData();
//	    table.setPreferredScrollableViewportSize(new Dimension(500, 70));

	    table.setAutoCreateColumnsFromModel(true);
        table.addHighlighter(HighlighterFactory.createSimpleStriping()); 
	    table.setShowGrid(true, true);
	    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//	    table.setVisibleRowCount(10); 
	    table.setColumnControlVisible(true);
	    
	    table.setModel(model);
	    table.getTableHeader().setVisible(true);
	    List<TableColumn> columns = table.getColumns();
//	    table.setColumnSequence(new Object[] {"Computation", "categoryColumn"}); 
	    
	    JScrollPane scrollpane = new JScrollPane(table); 
        
	    JXPanel temp = new JXPanel(new BorderLayout(0,0));
	    temp.add(scrollpane,BorderLayout.CENTER);
	    return temp;
	}

	private Font getTitleFont() {
		Font baseFont = UIManager.getFont("JXTitledPanel.titleFont"); 
        Font bigFont = new FontUIResource(baseFont.deriveFont(baseFont.getSize2D() * 1.3f)); 
        return bigFont;
        
	}
	private JXTitledPanel createCalculatorPanel() {
		calcPanel = new JXTitledPanel("Calculator");
//		calcPanel.setLayout(new MigLayout("fill, inset 0 0 0 0"));
		this.setModTitlePanel(calcPanel);
		
		JXPanel calculator = new JXPanel(new MigLayout("fill, inset 5 5 5 5"));
		calculator.setBorder(new RoundedBorder(5));
		
		JXTextField sumTextField = new JXTextField("sum");
		
		calculator.add(sumTextField,"growx, wrap");
		
		JXPanel buttonPanel = new JXPanel(new GridLayout(5,5,6,6));
		
		//symbolic
		symbolicButtons = new ArrayList<JXButton>();
		adderButtons = new ArrayList<JXButton>();
		numberButtons = new ArrayList<JXButton>();
		
		symbolicButtons.add(new JXButton("PI"));
		symbolicButtons.add(new JXButton("x2"));
		symbolicButtons.add(new JXButton("x3"));
		symbolicButtons.add(new JXButton("."));
		symbolicButtons.add(new JXButton("C"));
		symbolicButtons.add(new JXButton("("));
		symbolicButtons.add(new JXButton(")"));
		symbolicButtons.add(new JXButton("R"));
		symbolicButtons.add(new JXButton("W"));
		symbolicButtons.add(new JXButton("H"));
		
		adderButtons.add(new JXButton("*"));
		adderButtons.add(new JXButton("-"));
		adderButtons.add(new JXButton("/"));
		adderButtons.add(new JXButton("+"));
		adderButtons.add(new JXButton("="));
		

		numberButtons.add(new JXButton("4"));
		numberButtons.add(new JXButton("5"));
		numberButtons.add(new JXButton("3"));
		numberButtons.add(new JXButton("9"));
		numberButtons.add(new JXButton("1"));
		numberButtons.add(new JXButton("2"));
		numberButtons.add(new JXButton("8"));
		numberButtons.add(new JXButton("6"));
		numberButtons.add(new JXButton("0"));
		numberButtons.add(new JXButton("7"));
		
		
		for (JXButton addButton : adderButtons) {
			addButton.setOpaque(true);
			if( addButton.getText().equals("=")) {
				addButton.setBackgroundPainter(getEqualButtonPainter());
			} else {
				addButton.setBackgroundPainter(getAdderButtonPainter());
			}
			
			addButton.setForeground(Color.BLACK);
			addButton.setBorderPainted(true);
			addButton.setBorder(new LineBorder(Color.WHITE, 1));
		}
		
		for (JXButton symButton : symbolicButtons) {
			symButton.setOpaque(true);
			symButton.setBackgroundPainter(getSymbolButtonPainter());
			symButton.setForeground(Color.WHITE);
			symButton.setBorderPainted(true);
			symButton.setBorder(new LineBorder(Color.WHITE, 1));
			buttonPanel.add(symButton);
		}

		for (JXButton numButton : numberButtons) {
			numButton.setBackgroundPainter(getNumButtonPainter());
			numButton.setForeground(Color.WHITE);
			numButton.setBorderPainted(true);
			numButton.setBorder(new LineBorder(Color.WHITE, 1));
			buttonPanel.add(numButton);
			if( numButton.getText().equals("9"))
				buttonPanel.add(adderButtons.get(0));
			
			if( numButton.getText().equals("6"))
				buttonPanel.add(adderButtons.get(1));
			
			if( numButton.getText().equals("7")) {
				buttonPanel.add(adderButtons.get(2));
				buttonPanel.add(adderButtons.get(3));
				buttonPanel.add(adderButtons.get(4));
			}
		}
		
		
		
		
		
		buttonPanel.setOpaque(false);
		calculator.add(buttonPanel,"grow");
		calculator.setBackgroundPainter(getCalcBackgroundPainter());
		calcPanel.add(calculator);
		calcPanel.setPreferredSize(new Dimension((int) (frameDimension.getWidth()*.3), calcPanel.getPreferredSize().height));
		
		return calcPanel;
	}

	private JXTitledPanel createShapesPanel(String title) {
		shapePanel = new JXTitledPanel(title);
		this.setModTitlePanel(shapePanel);
		//shapePanel.setPreferredSize(new Dimension((int) (frameDimension.getWidth()*.3), 100));
		return shapePanel;
	}

	private JXTitledPanel createWorkAreaPanel(String title) {
		workAreaPanel = new JXTitledPanel(title);
		this.setModTitlePanel(workAreaPanel);
		return workAreaPanel;
	}
	
	protected void setModTitlePanel(JXTitledPanel panel) {
		panel.setBorder(new RoundedBorder(3));
		panel.setTitleFont(getTitleFont());
		panel.setTitleForeground(Colors.White.color());
		panel.setTitlePainter(getTitlePainter());
		panel.setBackgroundPainter(getSubPanelBackgroundPainter());
		panel.revalidate();
	}

	public JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu(eu.scy.tools.math.ui.Messages.getString("MathTool.1")); //$NON-NLS-1$
		fileMenu.add(new QuitAction());
		menuBar.add(fileMenu);
		return menuBar;
	}
	
	public Painter getTitlePainter() {
		int width = 100;
		int height = 100;
		Color color1 = Colors.White.color(0.7f);
		Color color2 = Colors.Red.color(0.7f);

		LinearGradientPaint gradientPaint = new LinearGradientPaint(0.0f, 0.0f,
				width, height, new float[] { 0.0f, 1f }, new Color[] {
						color1, color2 });
		MattePainter mattePainter = new MattePainter(gradientPaint);
		return mattePainter;
	}
	
	public Painter getSubPanelBackgroundPainter() {
		int width = 100;
		int height = 100;
		Color color1 = Colors.White.color(1f);
		Color color2 = Colors.Black.color(0.5f);

		LinearGradientPaint gradientPaint = new LinearGradientPaint(0.0f, 0.0f,
				width, height, new float[] { 0.0f, 1f }, new Color[] {
						color1, color1 });
		MattePainter mattePainter = new MattePainter(gradientPaint);
		return mattePainter;
	}
	
	public Painter getCalcBackgroundPainter() {
		int width = 100;
		int height = 100;
		Color color1 = Colors.White.color(1f);
		Color color2 = Colors.Black.color(0.8f);

		LinearGradientPaint gradientPaint = new LinearGradientPaint(0.0f, 0.0f,
				width, height, new float[] { 0.0f, 1f }, new Color[] {
						color1, color2 });
		MattePainter mattePainter = new MattePainter(gradientPaint);
		return mattePainter;
	}
	
	
	
    private Painter getSymbolButtonPainter() { 
 
    	 MattePainter mp = new MattePainter(Colors.Black.alpha(0.5f));
      GlossPainter gp = new GlossPainter(Colors.White.alpha(0.3f),
                                          GlossPainter.GlossPosition.TOP);
//      return (new CompoundPainter(mp, gp));
 
      return mp;
      
    } 
    
    private Painter getNumButtonPainter() { 
    	 
   	 MattePainter mp = new MattePainter(Colors.Blue.alpha(0.5f));
     GlossPainter gp = new GlossPainter(Colors.White.alpha(0.3f),
                                         GlossPainter.GlossPosition.TOP);
//     return (new CompoundPainter(mp, gp));
     
     return mp;
     
   } 
    
    private Painter getAdderButtonPainter() { 
   	 
      	 MattePainter mp = new MattePainter(Colors.White.alpha(1f));
        GlossPainter gp = new GlossPainter(Colors.Black.alpha(0.1f),
                                            GlossPainter.GlossPosition.TOP);
//        return (new CompoundPainter(mp, gp));
        
        return mp;
      } 
    
    private Painter getEqualButtonPainter() { 
      	 
     	 MattePainter mp = new MattePainter(Colors.Orange.alpha(0.5f));
       GlossPainter gp = new GlossPainter(Colors.Black.alpha(0.1f),
                                           GlossPainter.GlossPosition.TOP);
//       return (new CompoundPainter(mp, gp));
       return mp;
     }
}
