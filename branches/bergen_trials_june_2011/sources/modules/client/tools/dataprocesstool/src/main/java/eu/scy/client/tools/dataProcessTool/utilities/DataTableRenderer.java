/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.utilities;

import eu.scy.client.tools.dataProcessTool.dataTool.DataTable;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * fitex table renderer
 * @author Marjolaine Bodin
 */
public class DataTableRenderer extends DefaultTableCellRenderer{

    /* font color */
    private static final Color fontColor = Color.BLACK; 
    /* font data */
    private static final Font dataFont = new Font("Tahoma", Font.PLAIN, 12);
    /* font header + operations titles */
    public static final Font titleFont = new Font("Tahoma", Font.PLAIN, 12);
    /* font header unit */
    public static final Font unitFont = new Font("Tahoma", Font.ITALIC, 11);
    /* font no lines */
    private static final Font noRowFont = new Font("Tahoma", Font.PLAIN, 10);
    /* background color cell data */
    private static final Color backgroundColor = Color.WHITE;
    /*  background color cell  header */
    public static final Color headerBackgroundColor = new Color(217,217, 217);
     /*  background color cell  header with type text */
    public static final Color headerTextBackgroundColor = new Color(218,221, 197);
     /*  background color cell no lines */
    private static final Color noBackgroundColor = new Color(217,217,217);
    /* selection color */
    private static final Color selectedColor = new Color(167, 225, 255);
    /* data ignored color */
    private static final Color ignoredColor = new Color(248, 193, 169);
    /* grid color */
    private static final Color borderColor = Color.gray;

    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell =  super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        //by default
        cell.setForeground(fontColor);
        cell.setBackground(backgroundColor);
        cell.setFont(dataFont);
        //setAlignmentX(LEFT_ALIGNMENT);
        if(cell instanceof JLabel){
            //((JLabel)cell).setAlignmentX(JLabel.LEFT);
            ((JLabel)cell).setHorizontalAlignment(RIGHT);
        }
        Border border = null;
        if (table instanceof DataTable){
            //setHorizontalAlignment(((DataTable)table).getDataAlignment(column));
            if(cell instanceof JLabel){
                ((JLabel)cell).setHorizontalAlignment(((DataTable)table).getDataAlignment(column));
             }
            // no lines
            if (((DataTable)table).isValueNoRow(row, column)){
                cell.setFont(noRowFont);
                cell.setBackground(noBackgroundColor);
            }else if (((DataTable)table).isValueHeader(row, column)){
                // header
                cell.setFont(titleFont);
                cell.setBackground(headerBackgroundColor);
                if (!((DataTable)table).isValueDoubleHeader(row, column)){
                    cell.setBackground(headerTextBackgroundColor);
                }
                setAlignmentX(JLabel.CENTER_ALIGNMENT);
                if(cell instanceof JLabel && value != null && (value instanceof String[]) && (((String[])value).length ==2) ){
                        ((JLabel)cell).setAlignmentX(JLabel.CENTER);
                        ((JLabel)cell).setHorizontalAlignment(CENTER);
                //if(cell instanceof JLabel){
                    String text = ((String[])value)[0];
                    if (((String[])value)[1] != null && ((String[])value)[1].length() > 0 && !((String[])value)[1].equals("null"))
                        text += " ("+((String[])value)[1]+")";
                    ((JLabel)cell).setText(text);
                }
            }else if (((DataTable)table).isValueTitleOperation(row, column)){
                // operations titles
                cell.setFont(titleFont);
                cell.setBackground(((DataTable)table).getOperationColor(row, column));
                ((JLabel)cell).setAlignmentX(JLabel.CENTER);
                ((JLabel)cell).setHorizontalAlignment(CENTER);
            }else if (((DataTable)table).isValueOperation(row, column)){
                // operations
                cell.setBackground(((DataTable)table).getOperationColor(row, column));
            }else if (((DataTable)table).isValueDataIgnored(row, column)){
                // ignored data
                cell.setBackground(ignoredColor);
            }

            // define border
            int[]borders = ((DataTable)table).getBorders(row, column);

            border = BorderFactory.createMatteBorder(borders[0], borders[1], borders[2], borders[3], borderColor);
        }
        ((JLabel)cell).setBorder(border);
        if (isSelected){
            cell.setBackground(selectedColor);
        }
        return cell;
    }

    /* returns the cell font */
    public Font getFont(DataTable table, int row, int column){
        // numerotation des lignes
        if (table.isValueNoRow(row, column)){
            return noRowFont ;
        }else if (table.isValueHeader(row, column)){
            // header
            return titleFont ;
        }else if (table.isValueTitleOperation(row, column)){
            // operations title
            return titleFont ;
        }
        return this.getFont() ;
   }

}
