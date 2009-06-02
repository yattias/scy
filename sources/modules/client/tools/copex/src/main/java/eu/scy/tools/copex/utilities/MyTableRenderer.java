/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.utilities;

import eu.scy.tools.copex.edp.EdPPanel;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * affichage des cellules de la tabel pour datasheet
 * @author MBO
 */
public class MyTableRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell =  super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        cell.setForeground(Color.BLACK);
        if (row == 0){
            cell.setBackground(new Color(247,204,102));
        }else
            cell.setBackground(EdPPanel.backgroundColor);
        return cell;
    }
    

}
