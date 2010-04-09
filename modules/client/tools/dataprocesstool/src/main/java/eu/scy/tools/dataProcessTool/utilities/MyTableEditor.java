package eu.scy.tools.dataProcessTool.utilities;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import eu.scy.tools.dataProcessTool.dataTool.DataTable;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Marjolaine
 */
public class MyTableEditor extends DefaultCellEditor{

    private Component component;
    private DataTable dataTable;
    public MyTableEditor(DataTable dataTable,JTextField textField) {
        super(textField);
        this.dataTable = dataTable;
        component = new JTextField();

    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        component =  super.getTableCellEditorComponent(table, value, isSelected, row, column);
        table.setRowHeight(row, 25);
        if (table instanceof DataTable && (((DataTable)table).isValueHeader(row, column))){

        }else{
            ((JTextField)component).setText("");
        }
        return component;
    }

    @Override
    public void cancelCellEditing() {
        super.cancelCellEditing();
        dataTable.resizeColumn();
    }

    

    

    

}
