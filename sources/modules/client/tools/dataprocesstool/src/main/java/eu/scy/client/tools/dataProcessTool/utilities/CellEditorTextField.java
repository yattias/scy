
package eu.scy.client.tools.dataProcessTool.utilities;

import eu.scy.client.tools.dataProcessTool.dataTool.DataTable;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Marjolaine
 */
public class CellEditorTextField extends JTextField implements FocusListener
{
    /* table */
    private DataTable table;

    public CellEditorTextField(DataTable table) {
        super();
        this.table = table;
        this.addFocusListener(this);
    }

    @Override
    public void focusGained(FocusEvent e) {
        if(table.isCellSelectedData())
            ((JTextComponent)e.getComponent()).selectAll();
    }

    @Override
    public void focusLost(FocusEvent e) {
        //
    }

   

}
