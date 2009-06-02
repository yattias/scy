
package eu.scy.tools.copex.utilities;
import javax.swing.JTable;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * table for parameters (iterative task)
 * @author Marjolaine
 */
public class ParamTable extends JTable{
    /* model */
    private ParamTableModel tableModel;


    @Override
    public boolean isCellEditable(int row, int column) {
        return true;
    }

    public ParamTable(ParamTableModel tableModel) {
        super(tableModel);
        this.tableModel = tableModel;
        setTableHeader(null);
    }

    public ParamTableModel getTableModel() {
        return tableModel;
    }

    
}
