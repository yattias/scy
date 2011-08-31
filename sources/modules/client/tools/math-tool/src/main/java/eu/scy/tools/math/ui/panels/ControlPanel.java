package eu.scy.tools.math.ui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
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

import eu.scy.tools.math.controller.MathToolController;
import eu.scy.tools.math.doa.ComputationDataObj;
import eu.scy.tools.math.ui.UIUtils;
import eu.scy.tools.math.ui.paint.RoundedBorder;


public class ControlPanel extends JXPanel {

	
	private String type;
	private DefaultTableModel twoDeeTableModel;
	private JXTitledPanel tableAreaPanel;

	private JXButton removeAllButton;
	private JXButton removeSelectedButton;
	private JXTitledPanel tableArea;
	private JXTitledPanel calcPanel;
	private JXTitledPanel shapePanel;
	private JXTable table;
	private JXButton addToTableButton;
	private JXLabel resultLabel;
	private JXButton subtractResultButton;
	private Calculator calculator;
	private MathToolController mathToolController;

	public ControlPanel(MathToolController mathToolController, String type) {
		super(new GridLayout(2,1));
		this.type = type;
		this.mathToolController = mathToolController;
		init();
	}

	private void init() {
		this.add(createCalculatorPanel()); //$NON-NLS-1$
		this.add(createTableArea()); //$NON-NLS-1$
	}
	
	private JXTitledPanel createTableArea() {
		tableAreaPanel = new JXTitledPanel("Computations for " + type + " Shapes");
		UIUtils.setModTitlePanel(tableAreaPanel);
		JXPanel allPanel = new JXPanel(new MigLayout("fill, inset 0 0 0 0"));
		
		allPanel.add(createTable(),"grow, span");
		
		tableAreaPanel.add(allPanel);
		return tableAreaPanel;
	}
	
	private JXPanel createTable() {
	    // boilerplate table-setup; this would be the same for a JTable
//	    ComputationTableModel model = new ComputationTableModel(6, new String[] {"Shape", "Computation"});
		twoDeeTableModel = new DefaultTableModel() {
			public boolean isCellEditable(int row, int column) {
				return false;
			};
		};
		twoDeeTableModel.addColumn("#");
		twoDeeTableModel.addColumn("Shape");
		
	
	    
	    
		
		if( type.equals(UIUtils._3D)) {
			twoDeeTableModel.addColumn("Ratio");
			twoDeeTableModel.addColumn("Surface Area");
			twoDeeTableModel.addColumn("Volume");
			twoDeeTableModel.addColumn("shapeId");
		} else {
			twoDeeTableModel.addColumn("Calculation");
			twoDeeTableModel.addColumn("Sum");
			twoDeeTableModel.addColumn("Operation");
			twoDeeTableModel.addColumn("shapeId");
		}
		

		table = new JXTable(twoDeeTableModel);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		getComputationTable().putClientProperty(UIUtils.TYPE, type);
	    getComputationTable().setAutoCreateColumnsFromModel(true);
	    getComputationTable().addHighlighter(HighlighterFactory.createSimpleStriping()); 
	    getComputationTable().setShowGrid(true, true);
		getComputationTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//	    table.setVisibleRowCount(10); 
	    getComputationTable().setColumnControlVisible(true);
	    
//	    getComputationTable().setModel(twoDeeTableModel);
	    getComputationTable().getTableHeader().setVisible(true);
	    List<TableColumn> columns = getComputationTable().getColumns();
	    
	    JScrollPane scrollpane = new JScrollPane(getComputationTable()); 
        
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
	    
		TableColumn col = table.getColumnModel().getColumn(0);
	    int width = 8;
	    col.setPreferredWidth(width);
	    
	    TableColumn column;
	  
//	   if( type.equals(UIUtils._2D) ) {
//	    column = table.getColumnModel().getColumn(1);
//    	table.getColumnModel().removeColumn(column);
	    	 column = table.getColumnModel().getColumn(table.getColumnModel().getColumnCount()-1);
	    	table.getColumnModel().removeColumn(column);
	    
	    
	    return temp;
	}
	
	Action removeSelectedAction = new AbstractAction("Remove Entry") {

		
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			int selectedRow = ControlPanel.this.getComputationTable().getSelectedRow();
			// System.out.println(selectedRow);
			if (selectedRow != -1) {
				DefaultTableModel model = (DefaultTableModel) getComputationTable()
						.getModel();
				int convertRowIndexToModel = ControlPanel.this.getComputationTable().convertRowIndexToModel(selectedRow);
				model.removeRow(convertRowIndexToModel);

				if (type.equals(UIUtils._2D)) {
					// recalc the sums
					Vector dataVector = (Vector) model.getDataVector();

					ComputationDataObj oldCO = null;
					List<ComputationDataObj> newDataSet = new ArrayList<ComputationDataObj>();

					if (!dataVector.isEmpty()) {
						// go over the rows
						for (int i = 0; i < dataVector.size(); i++) {
							Vector rowVector = (Vector) dataVector.get(i);
							ComputationDataObj co = new ComputationDataObj(
									rowVector, UIUtils._2D);
							newDataSet.add(co);
						}

						mathToolController.recalculateModel(model, newDataSet,
								type);

					}
				}
				
				
				ControlPanel.this.getComputationTable().revalidate();
			} else {
				JOptionPane.showMessageDialog(ControlPanel.this,"There are no more Entries to remove.");
			}
			
		}
		
	};
	
	
	
	Action removeAllAction = new AbstractAction("Remove All Entries") {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			DefaultTableModel model = (DefaultTableModel)ControlPanel.this.getComputationTable().getModel();
			
			if( model.getRowCount() == 0 ) {
				JOptionPane.showMessageDialog(ControlPanel.this,"There are no more Entries to remove.");
			} else {
				while (model.getRowCount() > 0){
					model.removeRow(0);
					ControlPanel.this.getComputationTable().revalidate();
				}
				
			}
		}
		
	};

	
	


	
	private JXTitledPanel createCalculatorPanel() {
		calcPanel = new JXTitledPanel("Calculator");
//		calcPanel.setLayout(new MigLayout("fill, inset 0 0 0 0"));
		UIUtils.setModTitlePanel(calcPanel);
		
		JXPanel outerPanel = new JXPanel(new MigLayout("fill, inset 5 5 5 5"));
		outerPanel.setBackgroundPainter(UIUtils.getSubPanelBackgroundPainter());

		
		setCalculator(new Calculator(type));
		
	
		outerPanel.add(getCalculator(), "grow, wrap");
		
		 
	   
	    
		
		
		outerPanel.add(getCalculator().getCalcButtonPanel(), "grow");

		
		calcPanel.add(outerPanel);
		calcPanel.setPreferredSize(new Dimension((int) (UIUtils.frameDimension.getWidth()*.3), calcPanel.getPreferredSize().height));
		
		
		return calcPanel;
	}
	
	public void setComputationTable(JXTable table) {
		this.table = table;
	}

	public JXTable getComputationTable() {
		return table;
	}

	public void setCalculator(Calculator calculator) {
		this.calculator = calculator;
	}

	public Calculator getCalculator() {
		return calculator;
	}


}
