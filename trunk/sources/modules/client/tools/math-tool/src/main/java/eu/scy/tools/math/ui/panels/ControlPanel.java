package eu.scy.tools.math.ui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTextField;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.error.ErrorInfo;

import eu.scy.tools.math.ui.ComputationDataObject;
import eu.scy.tools.math.ui.UIUtils;
import eu.scy.tools.math.ui.images.Images;
import eu.scy.tools.math.ui.paint.RoundedBorder;


public class ControlPanel extends JXPanel {

	
	private String type;
	private DefaultTableModel twoDeeTableModel;
	private JXTitledPanel tableAreaPanel;
	private ArrayList<JXButton> symbolicButtons;
	private ArrayList<JXButton> adderButtons;
	private ArrayList<JXButton> numberButtons;
	private JXButton removeAllButton;
	private JXButton removeSelectedButton;
	private JXTitledPanel tableArea;
	private JXTitledPanel calcPanel;
	private JXTitledPanel shapePanel;
	private JXTable table;

	public ControlPanel(String type, LayoutManager layout) {
		super(layout);
		this.type = type;
		init();
	}

	private void init() {
		this.add(createShapesPanel(type), "grow, wrap"); //$NON-NLS-1$
		this.add(createCalculatorPanel(),"wrap"); //$NON-NLS-1$
		this.add(createTableArea(type),"grow, span"); //$NON-NLS-1$
	}
	
	private JXTitledPanel createTableArea(String type) {
		tableAreaPanel = new JXTitledPanel("Computations for " + type + " Shapes");
		UIUtils.setModTitlePanel(tableAreaPanel);
		JXPanel allPanel = new JXPanel(new MigLayout("fill, inset 0 0 0 0"));
		
		allPanel.add(createTable(), "grow");
		
		tableAreaPanel.add(allPanel);
		return tableAreaPanel;
	}
	
	private JXPanel createTable() {
	    // boilerplate table-setup; this would be the same for a JTable
//	    ComputationTableModel model = new ComputationTableModel(6, new String[] {"Shape", "Computation"});
		twoDeeTableModel = new DefaultTableModel();
		twoDeeTableModel.addColumn(" ");
		twoDeeTableModel.addColumn("Shape");
		twoDeeTableModel.addColumn("Calculation");
		twoDeeTableModel.addColumn("Sum");
//		String[] socrates = { "1", "circle 1", "-100", "0" };
//	    twoDeeTableModel.addRow(socrates);
//	    twoDeeTableModel.addRow(socrates);
//	    twoDeeTableModel.addRow(socrates);
	    
//	    ComputationDataObject c = new ComputationDataObject(new Integer(twoDeeTableModel.getRowCount()+1),"test", 4f, 3.00f);
//	    twoDeeTableModel.addRow(c.toArray());


	    table = new JXTable();
//	    model.loadData();
//	    table.setPreferredScrollableViewportSize(new Dimension(500, 70));

	    table.setAutoCreateColumnsFromModel(true);
	    table.addHighlighter(HighlighterFactory.createSimpleStriping()); 
	    table.setShowGrid(true, true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//	    table.setVisibleRowCount(10); 
	    table.setColumnControlVisible(true);
	    
	    table.setModel(twoDeeTableModel);
	    table.getTableHeader().setVisible(true);
	    List<TableColumn> columns = table.getColumns();
//	    table.setColumnSequence(new Object[] {"Computation", "categoryColumn"}); 
	    
	    JScrollPane scrollpane = new JScrollPane(table); 
        
	    JXPanel temp = new JXPanel(new MigLayout("fill,insets 5 5 5 5"));
	    temp.setBackgroundPainter(UIUtils.getSubPanelBackgroundPainter());
	    temp.add(scrollpane,"grow, wrap");
	    
	    JXPanel buttonPanel = new JXPanel(new FlowLayout(FlowLayout.RIGHT));
	    buttonPanel.setBackgroundPainter(UIUtils.getSubPanelBackgroundPainter());
	    
	    removeSelectedButton = new JXButton(removeSelectedAction);
	    removeSelectedAction.putValue(Action.SHORT_DESCRIPTION, "Removes a selected Entry from the table");
	    removeAllButton = new JXButton(removeAllAction);
	    removeAllAction.putValue(Action.SHORT_DESCRIPTION, "Removes all the Entries from the table.");
	    buttonPanel.add(removeSelectedButton);
	    buttonPanel.add(removeAllButton);
	    
	    
	    temp.add(buttonPanel,"grow");
	    return temp;
	}
	
	Action removeSelectedAction = new AbstractAction("Remove Entry") {

		
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			int selectedRow = ControlPanel.this.table.getSelectedRow();
			System.out.println(selectedRow);
			if( selectedRow != -1) {
				DefaultTableModel model = (DefaultTableModel)table.getModel();
				model.removeRow(selectedRow);
				ControlPanel.this.table.revalidate();
			} else {
				JOptionPane.showMessageDialog(ControlPanel.this,"Please Select a Row to Remove");
			}
			
		}
		
	};
	
	
	
	Action removeAllAction = new AbstractAction("Remove All Entries") {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			DefaultTableModel model = (DefaultTableModel)ControlPanel.this.table.getModel();
			
			if( model.getRowCount() == 0 ) {
				JOptionPane.showMessageDialog(ControlPanel.this,"There are no more Entries to remove.");
			} else {
				while (model.getRowCount() > 0){
					model.removeRow(0);
					ControlPanel.this.table.revalidate();
				}
				
			}
		}
		
	};
	private JXButton addToTableButton;
	private JXLabel resultLabel;
	private JXButton subtractResultButton;
	
	private JXTitledPanel createShapesPanel(String type) {
		shapePanel = new JXTitledPanel(type + " " +"Shapes");
		UIUtils.setModTitlePanel(shapePanel);
		
		JXPanel allPanel = new JXPanel(new MigLayout("fill, inset 3 3 3 3"));
		allPanel.setBackgroundPainter(UIUtils.getSubPanelBackgroundPainter());

		List<JXLabel> shapes = getShapes(type);
		for (JXLabel jxLabel : shapes) {
			allPanel.add(jxLabel, "grow");
		}
		
		shapePanel.add(allPanel);
		return shapePanel;
	}
	
	private List<JXLabel> getShapes(String type) {
		
		List<JXLabel> shapes = new ArrayList<JXLabel>();
		
		if( type.equals(UIUtils._2D)) {
			shapes.add(new JXLabel(Images.Circle.getIcon()));
			shapes.add(new JXLabel(Images.Triangle.getIcon()));
			shapes.add(new JXLabel(Images.Rectangle.getIcon()));
		} else if( type.equals(UIUtils._3D)) {
			shapes.add(new JXLabel(Images.Cube.getIcon()));
			shapes.add(new JXLabel(Images.Sphere.getIcon()));
			shapes.add(new JXLabel(Images.Prism.getIcon()));
		}
		
		return shapes;

	}


	
	private JXTitledPanel createCalculatorPanel() {
		calcPanel = new JXTitledPanel("Calculator");
//		calcPanel.setLayout(new MigLayout("fill, inset 0 0 0 0"));
		UIUtils.setModTitlePanel(calcPanel);
		
		JXPanel outerPanel = new JXPanel(new MigLayout("fill, inset 5 5 5 5"));
		outerPanel.setBackgroundPainter(UIUtils.getSubPanelBackgroundPainter());
		JXPanel calculator = new JXPanel(new MigLayout("fill, inset 5 5 5 5"));
		calculator.setBorder(new RoundedBorder(5));
		
		JXTextField sumTextField = new JXTextField("Formula");
		sumTextField.addActionListener(calcTextFieldListner);
		sumTextField.setBackground(Color.WHITE);
		sumTextField.setOpaque(true);
		calculator.add(sumTextField,"growx, wrap");
		
		JXPanel buttonPanel = new JXPanel(new GridLayout(5,5,6,6));
		
		//symbolic
		symbolicButtons = new ArrayList<JXButton>();
		adderButtons = new ArrayList<JXButton>();
		numberButtons = new ArrayList<JXButton>();
		
		symbolicButtons.add(new JXButton("¹"));
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
				addButton.setBackgroundPainter(UIUtils.getEqualButtonPainter());
			} else {
				addButton.setBackgroundPainter(UIUtils.getAdderButtonPainter());
			}
			
			addButton.setForeground(Color.BLACK);
			addButton.setBorderPainted(true);
			addButton.setBorder(new LineBorder(Color.WHITE, 1));
		}
		
		for (JXButton symButton : symbolicButtons) {
			symButton.setOpaque(true);
			symButton.setBackgroundPainter(UIUtils.getSymbolButtonPainter());
			symButton.setForeground(Color.WHITE);
			symButton.setBorderPainted(true);
			symButton.setBorder(new LineBorder(Color.WHITE, 1));
			buttonPanel.add(symButton);
		}

		for (JXButton numButton : numberButtons) {
			numButton.setBackgroundPainter(UIUtils.getNumButtonPainter());
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
		calculator.setBackgroundPainter(UIUtils.getCalcBackgroundPainter());
		
		outerPanel.add(calculator, "grow, wrap");
		
		 
	    JXPanel calcButtonPanel = new JXPanel(new MigLayout("fill,insets 5 5 5 5"));
	    calcButtonPanel.setBackgroundPainter(UIUtils.getSubPanelBackgroundPainter());
	    
	    resultLabel = new JXLabel("0.00");
	    this.modResultLabel(resultLabel);
	    calcButtonPanel.add(resultLabel,"west");
	    
	    
	    
	    addToTableButton = new JXButton(addResultAction);
	    addResultAction.putValue(Action.SHORT_DESCRIPTION, "Adds the calculation to the table.");
	    subtractResultButton = new JXButton(subtractResultAction);
	    subtractResultAction.putValue(Action.SHORT_DESCRIPTION, "Subtracts the calculation to the table.");
	    calcButtonPanel.add(subtractResultButton, "east");
	    calcButtonPanel.add(addToTableButton, "east");
	    
		
		
		outerPanel.add(calcButtonPanel, "grow");

		
		calcPanel.add(outerPanel);
		calcPanel.setPreferredSize(new Dimension((int) (UIUtils.frameDimension.getWidth()*.3), calcPanel.getPreferredSize().height));
		
		
		return calcPanel;
	}
	
	private void modResultLabel(JXLabel label) {
		Font font = label.getFont();
		Font bigFont = new FontUIResource(font.deriveFont(Font.BOLD, font.getSize2D() * 1.3f)); 
		label.setFont(bigFont);
		label.setForeground(Color.blue);
	}
	
	Action addResultAction = new AbstractAction("Add") {

		
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			String text = ControlPanel.this.resultLabel.getText();
			float parseFloat = Float.parseFloat(text);
			
			DefaultTableModel model = (DefaultTableModel) ControlPanel.this.table.getModel();
			
			if( model.getRowCount() == 0 ) {
				model.addRow(new Object[]{new Integer(1), "test shape",new Float(text),new Float(text)});
			} else {
				
				Vector data = model.getDataVector();
				Vector lastElement = (Vector) data.lastElement();
				ComputationDataObject c = new ComputationDataObject(lastElement);
//				
				float sum = c.getSum() + parseFloat;
				
				model.addRow(new Object[]{new Integer(model.getRowCount() + 1), "test shape",new Float(text),new Float(sum)});
//				
			}
			
			
			
//			model.
			
		}
		
	};
	
	Action subtractResultAction = new AbstractAction("Subtract") {

		
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			float parseFloat = Float.parseFloat(resultLabel.getText());
			
		}
		
	};
	
	ActionListener calcTextFieldListner = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			
			JXTextField tf = (JXTextField) e.getSource();
			
			try {
				Float.parseFloat(tf.getText());
			} catch (NumberFormatException nfe) {
				ErrorInfo info = new ErrorInfo("This is not a number!", "Numbers Only!", null,  "category", 
						 nfe, Level.ALL, null);
		         JXErrorPane.showDialog(ControlPanel.this, info);
		         resultLabel.setText("0.00");
		         return;
			}
			
			
			resultLabel.setText(tf.getText());
			
			System.out.println("calc fired");
		}
	};

}
