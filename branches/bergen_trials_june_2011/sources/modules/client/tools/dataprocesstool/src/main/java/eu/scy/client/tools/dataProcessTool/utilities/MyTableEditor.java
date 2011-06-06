package eu.scy.client.tools.dataProcessTool.utilities;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import eu.scy.client.tools.dataProcessTool.dataTool.DataTable;
import java.awt.Font;

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
        //no resize for the height of a row
        //table.setRowHeight(row, 25);
       ((JTextField)component).setFont(new Font(Font.DIALOG, Font.PLAIN, 11));
        if (table instanceof DataTable && (((DataTable)table).isValueHeader(row, column))){

        }else{
            // keep the value of the cell
            //((JTextField)component).setText("");
           if(table instanceof DataTable && (((DataTable)table).isEditAfterOneClick()) ){
               ((JTextField)component).setText("");
           }else{
               //((JTextField)component).setCaretPosition(((JTextField)component).getSelectionEnd());
           }
        }
        return component;
    }

    @Override
    public void cancelCellEditing() {
        super.cancelCellEditing();
        dataTable.resizeColumn();
    }

    

    

    

}
