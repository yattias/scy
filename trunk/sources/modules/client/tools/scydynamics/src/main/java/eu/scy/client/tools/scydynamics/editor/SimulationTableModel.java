package eu.scy.client.tools.scydynamics.editor;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import sqv.ModelVariable;
import sqv.data.BasicDataAgent;
import sqv.data.DataServer;
import sqv.data.IDataClient;

class SimulationTableModel extends AbstractTableModel implements IDataClient {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9087138748912498161L;
	private static final boolean DEBUG = false;
	private String[] columnNames;
	private ArrayList<Double[]> data;
	private BasicDataAgent dataAgent;
	private ArrayList<ModelVariable> variables;

	public SimulationTableModel(ArrayList<ModelVariable> variables,
			DataServer dataServer) {
		setColumnNames(variables);
		data = new ArrayList<Double[]>();
		this.variables = variables;
		dataAgent = new BasicDataAgent(this, dataServer);
		// subscribe to variables
		ModelVariable.copy(dataServer.getVariables("TODO"), variables);
		dataAgent.add(variables);
		// register
		dataAgent.register();
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
		return data.get(row)[col];
	}

	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	public boolean isCellEditable(int row, int col) {
		return false;
	}

	public void setValueAt(Double value, int row, int col) {
		if (DEBUG) {
			System.out.println("Setting value at " + row + "," + col + " to "
					+ value + " (an instance of " + value.getClass() + ")");
		}

		data.get(row)[col] = value;
		fireTableCellUpdated(row, col);

		if (DEBUG) {
			System.out.println("New value of data:");
			printDebugData();
		}
	}

	private void printDebugData() {
		int numRows = getRowCount();
		int numCols = getColumnCount();

		for (int i = 0; i < numRows; i++) {
			System.out.print("    row " + i + ":");
			for (int j = 0; j < numCols; j++) {
				System.out.print("  " + data.get(i)[j]);
			}
			System.out.println();
		}
		System.out.println("--------------------------");
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
			data.remove(data.size()-1);
		} catch (Exception ex) {
		// catched in case there is no first or last element due to weird simulation settings
		}
	}

}
