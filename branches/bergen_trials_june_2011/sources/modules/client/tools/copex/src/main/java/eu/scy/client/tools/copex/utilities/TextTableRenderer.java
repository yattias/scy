package eu.scy.client.tools.copex.utilities;

import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Marjolaine
 */
public class TextTableRenderer  extends DefaultTableCellRenderer{
    /* font donnees */
    public static final Font dataFont = new Font("Tahoma", Font.PLAIN, 11);
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell =  super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        cell.setFont(dataFont);
        setAlignmentX(LEFT_ALIGNMENT);
        Border border =null;
        ((JLabel)cell).setBorder(border);
        return cell;
    }
}
