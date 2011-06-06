package eu.scy.tools.math.ui;

import java.awt.Point;
import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

import eu.scy.tools.math.doa.ComputationDataObj;

public class ComputationTableModel extends AbstractTableModel {

	private HashMap<Point, ComputationDataObj> lookup;

	private final int rows;

	private final int columns;

	private final String headers[];

	public ComputationTableModel(int rows, String columnHeaders[]) {
		if ((rows < 0) || (columnHeaders == null)) {
			throw new IllegalArgumentException(
					"Invalid row count/columnHeaders");
		}
		this.rows = rows;
		this.columns = columnHeaders.length;
		headers = columnHeaders;
		lookup = new HashMap<Point, ComputationDataObj>();
	}

	public int getColumnCount() {
		return columns;
	}

	public int getRowCount() {
		return rows;
	}

	public String getColumnName(int column) {
		return headers[column];
	}

	public ComputationDataObj getValueAt(int row, int column) {
		return lookup.get(new Point(row, column));
	}

	public void setValueAt(Object value, int row, int column) {
		if ((rows < 0) || (columns < 0)) {
			throw new IllegalArgumentException("Invalid row/column setting");
		}
		if ((row < rows) && (column < columns)) {
			lookup.put(new Point(row, column), (ComputationDataObj) value);
		}
	}
	
	
}
