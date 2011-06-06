package eu.scy.client.tools.copex.utilities;

import java.util.List;
import javax.swing.table.AbstractTableModel;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Marjolaine
 */
public class TextTableModel extends AbstractTableModel{
    private List<String> textList;
    private int nbDataCol;
    private int nbCol;
    private int nbRow;
    private String[][] textData;

    public TextTableModel(List<String> textList, int nbDataCol) {
        super();
        this.textList = textList;
        this.nbDataCol = nbDataCol;
        loadData();
    }

    private void loadData(){
        // nbCol
//        nbCol = 2*nbDataCol-1;
//        // nbrows ?
//        int nbD = textList.size();
//        if(nbD % nbDataCol == 0){
//            nbRow = (nbD / nbDataCol);
//        }else{
//            nbRow = (nbD / nbDataCol)+1;
//        }
//        textData = new String[nbRow][nbCol];
//        // initialization
//        for(int j=0; j<nbDataCol; j++){
//            for (int i=0; i<nbRow; i++){
//                int k = i+(j*nbRow);
//                if((2*j+1) < nbCol)
//                    textData[i][2*j+1] = "";
//                if(k < nbD)
//                    textData[i][2*j] = textList.get(i+(j*nbRow));
//                else
//                    textData[i][2*j] = "";
//            }
//        }
        int nbD = textList.size();
        // nbCol
        nbCol = nbDataCol;
        // nbrows ?
        if(nbD % nbDataCol == 0){
            nbRow = (nbD / nbDataCol);
        }else{
            nbRow = (nbD / nbDataCol)+1;
        }
        textData = new String[nbRow][nbCol];
        // initialization
        for(int i=0; i<nbRow; i++){
            for(int j=0; j<nbCol; j++){
                int k = j+(i*nbCol);
                if(k<nbD)
                    textData[i][j] = textList.get(k);
                else
                    textData[i][j] = "";
            }
        }
    }


    @Override
    public int getRowCount() {
        return this.nbRow;
    }

    @Override
    public int getColumnCount() {
        return this.nbCol;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return textData[rowIndex][columnIndex];
    }
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public void setNbCol(int nbCol){
        this.nbCol = nbCol;
        loadData();
        this.fireTableStructureChanged();
        this.fireTableDataChanged();
    }

    public void setTextList(List<String> textList){
        this.textList = textList;
        loadData();
        this.fireTableDataChanged();
    }
}
