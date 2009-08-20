/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.utilities;

import eu.scy.tools.copex.edp.DataSheetPanel;
import eu.scy.tools.copex.edp.EdPPanel;
import javax.swing.table.*;
/**
 * modele pour la table de la feuille de donneees
 * @author MBO
 */
public class MyTableModel extends AbstractTableModel {
    // ATTRIBUTS
    /* fenetre mere */
    private EdPPanel edP;
    private DataSheetPanel owner;
    /* donnees */
    private String[][] data;
    /* nombre de lignes */
    private int nbL;
    /* nombre de colonnes */
    private int nbC;
    private MyTable table;

    public MyTableModel(EdPPanel edP, DataSheetPanel owner,int nbL, int nbC, String[][] data) {
        super();
        this.edP = edP;
        this.owner = owner;
        this.nbL = nbL;
        this.nbC = nbC;
        this.data = data;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (((String)aValue).length() > MyConstants.MAX_LENGHT_DATA){
            String msg = edP.getBundleString("MSG_LENGHT_MAX");
            msg  = CopexUtilities.replace(msg, 0, edP.getBundleString("LABEL_DATA"));
            msg = CopexUtilities.replace(msg, 1, ""+MyConstants.MAX_LENGHT_DATA);
            edP.displayError(new CopexReturn(msg, false), edP.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        this.data[rowIndex][columnIndex] = (String)aValue;
        super.setValueAt(aValue, rowIndex, columnIndex);
        edP.updateDataSheet((String)aValue, rowIndex, columnIndex);
        table.resizeColumn();
        owner.scrollToColumn(columnIndex);
    }

    public void setTable(MyTable table) {
        this.table = table;
    }
    
    
    
    
    
    
    public int getRowCount() {
        return this.data.length;
    }

    public int getColumnCount() {
        if (data.length > 1)
            return this.data[0].length;
        else
            return 0;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return (rowIndex == 0 || columnIndex == 0);
               
    }
    

}
