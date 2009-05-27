package eu.scy.client.tools.scydynamics.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import eu.scy.client.tools.scydynamics.model.SimquestModel;


import sqv.Interface;
import sqv.ModelVariable;
import sqv.data.DataServer;
import sqv.data.ScreenOutputDataClient;
import sqv.widgets.Curve;
import sqv.widgets.GraphWidget;
import sqv.widgets.VariableRef;

public class TableTab extends JPanel implements ChangeListener, ActionListener  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2337114562359454914L;
	private ModelEditor editor;
	private VariableSelectionPanel variablePanel;
	private JPanel tablePanel;
	private DataServer dataServer;
	private SimulationSettingsPanel simulationPanel;
	private SimquestModel sqModel;
	private JTable table;
	private SimulationTableModel tableModel;
	private JScrollPane scrollPane;
	
	public TableTab(ModelEditor editor) {
		super();
		this.editor = editor;
		initComponents();
	}

	private void initComponents() {
		this.setLayout(new BorderLayout());
		JPanel westPanel = new JPanel();
		westPanel.setLayout(new BorderLayout());
		westPanel.add(variablePanel = new VariableSelectionPanel(editor), BorderLayout.NORTH);
		westPanel.add(simulationPanel = new SimulationSettingsPanel(editor, this), BorderLayout.CENTER);
		this.add(westPanel, BorderLayout.WEST);
		this.add(createTable(), BorderLayout.CENTER);
	}
	
	private JPanel createTable() {
		tablePanel = new JPanel();
		tablePanel.setLayout(new BorderLayout());
		tablePanel.setBorder(BorderFactory.createTitledBorder("table"));
		
		table = new JTable();
		scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		
		dataServer = new DataServer();
		tablePanel.add(scrollPane, BorderLayout.CENTER);
		return tablePanel;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		variablePanel.updateVariables();
		simulationPanel.updateSettings();
		editor.getActionLogger().logActivateWindow("table", null, this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("run")) {
			try {
				injectSimulationSettings();
			} catch(NumberFormatException ex) {
				JOptionPane.showMessageDialog( null, "Couldn't parse the simulation settings.\nPlease check."); 
				return;
			}
			
			
			
			// create the SimQuest model from the CoLab model
			sqModel = new SimquestModel(editor.getModel());
			sqv.Model model = new sqv.Model(sqModel, dataServer);
			
			// building the tablemodel
			ArrayList<ModelVariable> selectedVariables = new ArrayList<ModelVariable>();
			ModelVariable time = new ModelVariable();
			for (ModelVariable var : model.getVariables()){
				// getting a reference to the time variable
				if (var.getKind()==ModelVariable.VK_TIME) {
					selectedVariables.add(var);
				}
			}
			for (ModelVariable var : model.getVariables()){
				// getting a reference to the time variable
				if (variablePanel.getSelectedVariables().contains(var.getName())) {
					selectedVariables.add(var);
				}
			}
			
			tablePanel.remove(scrollPane);
			tableModel = new SimulationTableModel(selectedVariables, dataServer);
			table = new JTable(tableModel);
			table.setFillsViewportHeight(true);
			scrollPane = new JScrollPane(table);
			tablePanel.add(scrollPane, BorderLayout.CENTER);
					
			// simulate
			model.getSimulation().Simulate();			
			// tune table
			tableModel.deleteFirstAndLast();
			tablePanel.updateUI();
			// log
			editor.getActionLogger().logSimpleAction("run_model");
			String variableIdList = new String();
			for (String varname : variablePanel.getSelectedVariables()) {
				variableIdList = variableIdList.concat(editor.getModel().getObjectOfName(varname).getID()+", ");
			}
			editor.getActionLogger().logInspectVariablesAction("inspect_table", variableIdList.substring(0, variableIdList.length()-2));
			
		}
	}

	private void injectSimulationSettings() throws NumberFormatException {
			editor.getModel().setStart(Double.parseDouble(simulationPanel.getStart()));
			editor.getModel().setStop(Double.parseDouble(simulationPanel.getStop()));
			editor.getModel().setStep(Double.parseDouble(simulationPanel.getStep()));
			editor.getModel().setMethod(simulationPanel.getMethod());
	}
	
}
