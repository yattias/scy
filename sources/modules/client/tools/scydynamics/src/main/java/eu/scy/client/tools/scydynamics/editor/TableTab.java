package eu.scy.client.tools.scydynamics.editor;

import eu.scy.client.common.scyi18n.ResourceBundleWrapper;
import eu.scy.client.tools.scydynamics.logging.ModellingLogger;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;

import eu.scy.client.tools.scydynamics.model.SimquestModelQualitative;
import eu.scy.client.tools.scydynamics.model.SimquestModelQuantitative;
import eu.scy.elo.contenttype.dataset.DataSet;
import eu.scy.elo.contenttype.dataset.DataSetColumn;
import eu.scy.elo.contenttype.dataset.DataSetHeader;
import eu.scy.elo.contenttype.dataset.DataSetRow;

import sqv.Model;
import sqv.ModelVariable;
import sqv.data.DataServer;

public class TableTab extends SimulationPanel implements Runnable, ChangeListener {

    private static final long serialVersionUID = 2337114562359454914L;
    private JPanel tablePanel;
    private JTable table;
    private SimulationTableModel tableModel;
    private JScrollPane scrollPane;
	private Model sqvModel;
	private Thread simulationThread;

    public TableTab(ModelEditor editor, ResourceBundleWrapper bundle) {
        super(editor, bundle);
        initComponents(true);
        splitPane.setRightComponent(createTable());
    }

    private JPanel createTable() {
        tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(bundle.getString("PANEL_TABLE")));

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
    public void stop() {
    	super.stop();
        tableModel.deleteFirstAndLast();
        tablePanel.updateUI();
    }

	public DataSet getDataSet() {
		ArrayList<DataSetColumn> columns = new ArrayList<DataSetColumn>();
		for (String varName: variablePanel.getSelectedVariables()) {
			columns.add(new DataSetColumn(varName, "", "double"));
		}
		ArrayList<DataSetHeader> headers = new ArrayList<DataSetHeader>();
		headers.add(new DataSetHeader(columns, Locale.ENGLISH));
		DataSet dataset = new DataSet(headers);
		DataSetRow datarow;
		if (tableModel != null) {
			for (int row=0; row<tableModel.getRowCount(); row++) {
				ArrayList<String> values = new ArrayList<String>();
				for (int col=0; col<tableModel.getColumnCount(); col++) {
					values.add(tableModel.getValueAt(row, col).toString());
				}
				datarow = new DataSetRow(values);
				dataset.addRow(datarow);
			}
		}
		return dataset;
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
			return;
		}
		// can the variable values be parsed?
		try {
			variablePanel.getValues();
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(null,"The variable values cannot be parsed correctly.\n Please check and try again.");
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
        // create the SimQuest model from the CoLab model
		if (editor.getMode().equals(ModelEditor.Mode.QUALITATIVE_MODELLING)) {
			sqModel = new SimquestModelQualitative(editor, variablePanel.getValues());
		} else {
			sqModel = new SimquestModelQuantitative(editor, variablePanel.getValues());
		}
        sqvModel = new sqv.Model(sqModel, dataServer);

        // building the tablemodel
        ArrayList<ModelVariable> selectedVariables = new ArrayList<ModelVariable>();
        for (ModelVariable var : sqvModel.getVariables()) {
            // getting a reference to the time variable
            if (variablePanel.getSelectedVariables().contains(var.getName())) {
                selectedVariables.add(var);
            }
        }

        tableModel = null;
        tablePanel.remove(scrollPane);
        tableModel = new SimulationTableModel(selectedVariables, dataServer, simulationPanel.getDigits());
        table = new JTable(tableModel);
        DefaultTableCellRenderer defaultRenderer = new DefaultTableCellRenderer();
        defaultRenderer.setHorizontalAlignment(JLabel.RIGHT);
        table.setDefaultRenderer(String.class, defaultRenderer);
        table.setFillsViewportHeight(true);
        scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        String variableIdList = new String();
        for (String varname : variablePanel.getSelectedVariables()) {
        	if (varname.equals("time")) {
        		variableIdList = variableIdList.concat("time, ");
        	} else {
        		variableIdList = variableIdList.concat(editor.getModel().getObjectOfName(varname).getID() + ", ");
        	}
        }

        if (variableIdList.length() > 0) {
            // simulate
			simulationThread = new Thread(this);
			simulationThread.start();
			
            // log
	String injectedVariables = "";
	for (String varName: variablePanel.getValues().keySet()) {
	    injectedVariables = injectedVariables + editor.getModel().getObjectOfName(varName).getID() + "=" + variablePanel.getValues().get(varName)+"; ";
	}
	editor.getActionLogger().logModelRan(editor.getModelXML(), injectedVariables.substring(0, injectedVariables.length()-2));
            editor.getActionLogger().logInspectVariablesAction(ModellingLogger.TABLE_VIEWED, variableIdList.substring(0, variableIdList.length()-2));
        } else {
            JOptionPane.showMessageDialog(null, bundle.getString("PANEL_SELECTVARIABLE"));
        }
	}
	
	@Override
	public void run() {
		simulationPanel.setRunning(true);
		sqvModel.getSimulation().Simulate();
		simulationPanel.setRunning(false);
		// tune table
        tableModel.deleteFirstAndLast();
        tablePanel.updateUI();
	}

}
