/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.utilities;

import java.awt.Component;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

/**
 * Editeur pour la table de feuilles de données
 * @author MBO
 */
public class MyTableEditor extends AbstractCellEditor implements TableCellEditor{

    // ATTRIBUTS
    /* text field */
    private JTextField field;
    
    // CONTRUCTEURS
    public MyTableEditor() {
        super();
        field = new JTextField();
    } 

    // INTERFACE
    public Object getCellEditorValue() {
        return field.getText();
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        field.setText((String)value);
        return field;
    }

    

}
