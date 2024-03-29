package eu.scy.client.tools.scydynamics.editor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import sqv.Interface;
import sqv.Model;
import sqv.ModelVariable;
import sqv.data.DataServer;
import sqv.widgets.Curve;
import sqv.widgets.GraphWidget;
import sqv.widgets.VariableRef;
import eu.scy.client.common.scyi18n.ResourceBundleWrapper;
import eu.scy.client.tools.scydynamics.editor.ModelEditor.Mode;
import eu.scy.client.tools.scydynamics.logging.ModellingLogger;
import eu.scy.client.tools.scydynamics.model.SimquestModelQualitative;
import eu.scy.client.tools.scydynamics.model.SimquestModelQuantitative;
import eu.scy.client.tools.scydynamics.store.SCYDynamicsStore.StoreType;

@SuppressWarnings("serial")
public class GraphTab extends SimulationPanel implements ChangeListener {

	private GraphWidget graphWidget;
	private JComboBox xAxisSelector;
	private Interface iface;
	private LinkedList<Curve> curves;
	private JPanel graphPanel;
	private JCheckBox multiPlotsCheckBox;
	private Model sqvModel;

	public GraphTab(ModelEditor editor, ResourceBundleWrapper bundle) {
		super(editor, bundle);
		initComponents(false);
		splitPane.setRightComponent(createGraphPanel());
		clearGraph();
	}

	public void updateXAxisSelector() {
		xAxisSelector.removeAllItems();
		xAxisSelector.addItem("time");
		for (String varName : variablePanel.getVariables().keySet()) {
			xAxisSelector.addItem(varName);
		}
	}

	private void prepareGraph() {
		dataServer = new DataServer();
		iface = new Interface(dataServer);
		iface.addWidget(graphWidget);
		graphWidget.setInterface(iface);
	}

	void clearGraph() {
		curves = new LinkedList<Curve>();
		graphPanel.remove(graphWidget.getComponent());
		graphWidget = new GraphWidget();
		if (editor.getMode().equals(Mode.QUALITATIVE_MODELLING)) {
			graphWidget.getGraph().setDrawLabels(false);
		}
		graphWidget.setBasics(true, true);
		graphPanel.add(graphWidget.getComponent(), BorderLayout.CENTER);
		graphPanel.updateUI();
	}

	private JPanel createGraphPanel() {
		graphPanel = new JPanel();
		graphPanel.setLayout(new BorderLayout());
		//graphPanel.setBorder(BorderFactory.createTitledBorder(bundle.getString("PANEL_GRAPH")));
		curves = new LinkedList<Curve>();
		graphWidget = new GraphWidget();
		if (editor.getMode().equals(Mode.QUALITATIVE_MODELLING)) {
			graphWidget.getGraph().setDrawLabels(false);
		}
		graphWidget.setBasics(true, true);
		graphPanel.add(graphWidget.getComponent(), BorderLayout.CENTER);
		JPanel axisPanel = new JPanel();
		FlowLayout flow = new FlowLayout();
		flow.setAlignment(FlowLayout.RIGHT);
		axisPanel.setLayout(flow);
		multiPlotsCheckBox = new JCheckBox();
		multiPlotsCheckBox.setSelected(true);
		if (Boolean.parseBoolean(editor.getProperties().getProperty("multiPlotCheckbox", "true"))) {
			axisPanel.add(new JLabel(editor.getBundle().getString("SIMULATION_MULTIPLE")));
			axisPanel.add(multiPlotsCheckBox);
		}
		JButton button = new JButton(editor.getBundle().getString("SIMULATION_CLEAR"));
		button.setActionCommand("clear");
		button.addActionListener(this);
		axisPanel.add(button);
		xAxisSelector = new JComboBox();
		updateXAxisSelector();
		axisPanel.add(xAxisSelector);
		graphPanel.add(axisPanel, BorderLayout.SOUTH);
		return graphPanel;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		variablePanel.updateVariables();
		simulationSettingsPanel.updateSettings();
		this.updateXAxisSelector();
		if (editor.getMode().equals(Mode.QUALITATIVE_MODELLING)) {
			graphWidget.getGraph().setDrawLabels(false);
		}
		graphPanel.updateUI();
		editor.getActionLogger().logActivateWindow("graph", null, this);
	}
	
	@Override
	public void stop() {
		super.stop();
		SwingUtilities.invokeLater( new Runnable() 
		{ 
		  public void run() { 
				graphWidget.update();
				graphPanel.updateUI();
				graphPanel.repaint();
		  } 
		} );
	}
	
	@Override
	public void runSimulation() {
		editor.checkModel();
		// can the model be parsed?
		if (editor.getModelCheckMessages().size() > 0) {
			String messages = new String(
					bundle.getString("PANEL_CANNOTEXECUTE") + "\n");
			for (String msg : editor.getModelCheckMessages()) {
				messages = messages + msg + "\n";
			}
			JOptionPane.showMessageDialog(null, messages);
			editor.getActionLogger().logModelRanError(editor.getModelXML(), "");
			return;
		}
		// can the variable values be parsed?
		try {
			variablePanel.getValues();
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(null, "The variable values cannot be parsed correctly.\n Please check and try again.");
			return;
		}
		// can the simulation settings be parsed?
		try {
			injectSimulationSettings();
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(null,
					bundle.getString("PANEL_CANNOTPARSE"));
			return;
		}

		if (!multiPlotsCheckBox.isSelected()) {
			clearGraph();
		}
		prepareGraph();

		// create the SimQuest model from the CoLab model
		if (editor.getMode().equals(ModelEditor.Mode.QUALITATIVE_MODELLING)) {
			sqModel = new SimquestModelQualitative(editor, variablePanel.getValues());
		} else {
			sqModel = new SimquestModelQuantitative(editor, variablePanel.getValues());
		}
		
		sqvModel = new sqv.Model(sqModel, dataServer);
		
		//fade out existing curves
		for (Curve curve : curves) {
			if (Util.getBrightness(curve.getColor()) < 175) {
				curve.setColor(curve.getColor().brighter());
			}
			curve.setLineWidth(1);
		}

		// adding the new curves
		// get the x-axis-variable
		VariableRef xAxisVariable = null;
		for (ModelVariable var : sqvModel.getVariables()) {
			if (var.getName().equals(
					xAxisSelector.getSelectedItem().toString())) {
				xAxisVariable = new VariableRef(var);
				break;
			}
		}
		for (ModelVariable var : sqvModel.getVariables()) {
			// if the variable is one of the selected ones
			if (variablePanel.getSelectedVariables().contains(var.getName())) {
				curves.add(new Curve(curves.size(), xAxisVariable, new VariableRef(var),
						editor.getModel().getNodes().get(var.getName()).getLabelColor(),
						(float) 3, null));
			}
		}

		graphWidget.setCurvesList(curves);
		// need the variableIdList for logging
		String variableIdList = new String();
		for (String varname : variablePanel.getSelectedVariables()) {
			variableIdList = variableIdList.concat(editor.getModel().getObjectOfName(varname).getID()+ "; ");
		}

		if (variablePanel.getSelectedVariables().size() > 0) {
			// simulate
			//LOGGER.info("starting simulation");
			simulationThread = new Thread(this);
			simulationThread.start();
			// log
			String injectedVariables = "";
			for (String varName: variablePanel.getValues().keySet()) {
				injectedVariables = injectedVariables + editor.getModel().getObjectOfName(varName).getID() + "=" + variablePanel.getValues().get(varName)+"; ";
			}
			String injection = "";
			if (injectedVariables.length()>=2) {
				injection = injectedVariables.substring(0, injectedVariables.length()-2);
			}
			editor.getActionLogger().logModelRan(editor.getModelXML(), injection);
			editor.getActionLogger().logInspectVariablesAction(ModellingLogger.GRAPH_VIEWED, variableIdList.substring(0, variableIdList.length()-2));
			editor.doAutosave(StoreType.ON_SIMULATE_GRAPH);
		} else {
			JOptionPane.showMessageDialog(null,
					bundle.getString("PANEL_SELECTVARIABLE"));
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		if (e.getActionCommand().equals("clear")) {
			this.clearGraph();
		}
	}

	@Override
	public void run() {
		simulationSettingsPanel.setRunning(true);
		sqvModel.getSimulation().Simulate();
		SwingUtilities.invokeLater( new Runnable() 
		{ 
		  public void run() { 
				graphWidget.update();
				graphPanel.updateUI();
				graphPanel.repaint();
				simulationSettingsPanel.setRunning(false);
		  } 
		} );

	}

}
