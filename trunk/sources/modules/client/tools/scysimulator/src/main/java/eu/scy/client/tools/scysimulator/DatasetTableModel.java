package eu.scy.client.tools.scysimulator;

import eu.scy.elo.contenttype.dataset.DataSetRow;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import sqv.ModelVariable;

/**
 *
 * @author lars
 */
public class DatasetTableModel extends AbstractTableModel {

    private String[] columnNames;
    private List<String[]> data;

    DatasetTableModel(List<ModelVariable> selectedVariables) {
        setColumnNames(selectedVariables);
        data = new ArrayList<String[]>();
    }

    private void setColumnNames(List<ModelVariable> variables) {
        columnNames = new String[variables.size()];
        for (int i = 0; i < variables.size(); i++) {
            if (variables.get(i).getExternalName().isEmpty()) {
            	columnNames[i] = variables.get(i).getName();
            } else {
            	columnNames[i] = variables.get(i).getName()+": "+variables.get(i).getExternalName();
            }
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

    public void setValueAt(String value, int row, int col) {
        data.get(row)[col] = value;
        fireTableCellUpdated(row, col);
    }

    void addRow(DataSetRow newRow) {
        String[] values = new String[newRow.getLength()];
        newRow.getValues().toArray(values);
        data.add(values);
    }
}
