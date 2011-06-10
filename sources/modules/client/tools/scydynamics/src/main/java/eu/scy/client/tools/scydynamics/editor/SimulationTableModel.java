package eu.scy.client.tools.scydynamics.editor;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Logger;

import javax.swing.table.AbstractTableModel;

import sqv.ModelVariable;
import sqv.data.BasicDataAgent;
import sqv.data.DataServer;
import sqv.data.IDataClient;

@SuppressWarnings("serial")
class SimulationTableModel extends AbstractTableModel implements IDataClient {

    private final static Logger LOGGER = Logger.getLogger(SimulationTableModel.class.getName());
    private String[] columnNames;
    private ArrayList<Double[]> data;
    private BasicDataAgent dataAgent;
    private ArrayList<ModelVariable> variables;
    private DecimalFormat decimalFormatter;

    public SimulationTableModel(ArrayList<ModelVariable> variables, DataServer dataServer) {
	this(variables, dataServer, 2);
    }

    public SimulationTableModel(ArrayList<ModelVariable> variables, DataServer dataServer, int digits) {
	setDecimalFormat(digits);
	setColumnNames(variables);
	data = new ArrayList<Double[]>();
	this.variables = variables;
	dataAgent = new BasicDataAgent(this, dataServer);
	// subscribe to variables
	ModelVariable.copy(dataServer.getVariables("dummy"), variables);
	dataAgent.add(variables);
	// register
	dataAgent.register();
    }

    private void setDecimalFormat(int digits) {
	String formatString = "#0";
	if (digits > 0) {
	    formatString = formatString.concat(".");
	}
	for (int i=0; i<digits; i++) {
	    formatString = formatString.concat("0");
	}
	decimalFormatter = new DecimalFormat(formatString, new DecimalFormatSymbols(Locale.US));
    }

    public ArrayList<ModelVariable> getVariables() {
	return variables;
    }

    private void setColumnNames(ArrayList<ModelVariable> variables) {
	columnNames = new String[variables.size()];
	for (int i = 0; i < variables.size(); i++) {
	    columnNames[i] = variables.get(i).getName();
	}
    }

    public int getColumnCount() {
	return columnNames.length;
    }

    public int getRowCount() {
	return data.size();
    }

    public String getColumnName(int col) {
	return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
	Double value = data.get(row)[col];
	if (value.isNaN()) {
	    return "-";
	} else {
	    return decimalFormatter.format(value);
	}
    }

    public Class<? extends Object> getColumnClass(int c) {
	return getValueAt(0, c).getClass();
    }

    public boolean isCellEditable(int row, int col) {
	return false;
    }

    public void setValueAt(Double value, int row, int col) {
	data.get(row)[col] = value;
	fireTableCellUpdated(row, col);
    }

    @Override
    public void updateClient() {
	Double[] newRow = new Double[variables.size()];
	for (int i = 0; i < variables.size(); i++) {
	    newRow[i] = variables.get(i).getValue();
	}
	data.add(newRow);
    }

    public void deleteFirstAndLast() {
	try {
	    data.remove(0);
	    data.remove(data.size() - 1);
	} catch (Exception ex) {
	    // caught in case there is no first or last element due to weird simulation settings
	}
    }
}
