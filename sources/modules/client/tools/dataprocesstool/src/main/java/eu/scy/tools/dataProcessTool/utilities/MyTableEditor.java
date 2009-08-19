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

    public MyTableEditor(JTextField textField) {
        super(textField);
        component = new JTextField();

    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        component =  super.getTableCellEditorComponent(table, value, isSelected, row, column);
        if (table instanceof DataTable && ((DataTable)table).isValueHeader(row, column)){
            // header
            
        }else{
            ((JTextField)component).setText("");
        }
        return component;
    }

    

    

    

}
