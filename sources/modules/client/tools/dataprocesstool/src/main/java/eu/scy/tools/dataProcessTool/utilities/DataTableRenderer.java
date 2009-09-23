/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.utilities;

import eu.scy.tools.dataProcessTool.dataTool.DataTable;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * apparence table
 * @author Marjolaine Bodin
 */
public class DataTableRenderer extends DefaultTableCellRenderer{

    /* CONSTANTES */
    /* couleur de la fonte */
    private static final Color fontColor = Color.BLACK; 
    /* font donnees */
    private static final Font dataFont = new Font("Tahoma", Font.PLAIN, 12);
    /* font header + titre operations */
    public static final Font titleFont = new Font("Tahoma", Font.PLAIN, 12);
    /* font header unit */
    public static final Font unitFont = new Font("Tahoma", Font.ITALIC, 11);
    /* font numerotation des lignes */
    private static final Font noRowFont = new Font("Tahoma", Font.PLAIN, 10);
    /* couleur de fond cellules data */
    private static final Color backgroundColor = Color.WHITE;
    /* couleur de fond cellules header */
    public static final Color headerBackgroundColor = new Color(217,217, 217);
     /* couleur de fond cellules numerotation ligne */
    private static final Color noBackgroundColor = new Color(217,217,217);
    /* couleur de la selection */
    private static final Color selectedColor = new Color(167, 225, 255);
    /* couleur de data ignorees */
    private static final Color ignoredColor = new Color(248, 193, 169);
    /* couleur de la grille */
    private static final Color borderColor = Color.gray;

    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell =  super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        // comportement par defaut
        cell.setForeground(fontColor);
        cell.setBackground(backgroundColor);
        cell.setFont(dataFont);
        setAlignmentX(LEFT_ALIGNMENT);
        Border border = null;
        if (table instanceof DataTable){
            // numerotation des lignes
            if (((DataTable)table).isValueNoRow(row, column)){
                cell.setFont(noRowFont);
                cell.setBackground(noBackgroundColor);
            }else if (((DataTable)table).isValueHeader(row, column)){
                // header
                cell.setFont(titleFont);
                cell.setBackground(headerBackgroundColor);
                setAlignmentX(CENTER_ALIGNMENT);
                if(cell instanceof JLabel){
                    String text = ((String[])value)[0];
                    if (((String[])value)[1] != null && ((String[])value)[1].length() > 0)
                        text += " ("+((String[])value)[1]+")";
                    ((JLabel)cell).setText(text);
                }
            }else if (((DataTable)table).isValueTitleOperation(row, column)){
                // titre d'operations
                cell.setFont(titleFont);
                cell.setBackground(((DataTable)table).getOperationColor(row, column));
                setAlignmentX(CENTER_ALIGNMENT);
            }else if (((DataTable)table).isValueOperation(row, column)){
                // operations
                cell.setBackground(((DataTable)table).getOperationColor(row, column));
            }else if (((DataTable)table).isValueDataIgnored(row, column)){
                // data ignorees
                cell.setBackground(ignoredColor);
            }

            // determine les bordures
            int[]borders = ((DataTable)table).getBorders(row, column);

            border = BorderFactory.createMatteBorder(borders[0], borders[1], borders[2], borders[3], borderColor);
        }
        ((JLabel)cell).setBorder(border);
        if (isSelected){
            cell.setBackground(selectedColor);
        }
        return cell;
    }

    /* renvoie la font d'une cellule */
    public Font getFont(DataTable table, int row, int column){
        // numerotation des lignes
            if (table.isValueNoRow(row, column)){
                return noRowFont ;
            }else if (table.isValueHeader(row, column)){
                // header
                return titleFont ;
            }else if (table.isValueTitleOperation(row, column)){
                // titre d'operations
                return titleFont ;
            }
            return this.getFont() ;
    }

}
