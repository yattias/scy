/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.scy.scyDataTool.dataTool;

import com.scy.scyDataTool.common.*;
import com.scy.scyDataTool.dnd.SubData;
import com.scy.scyDataTool.utilities.CopexReturn;
import com.scy.scyDataTool.utilities.DataConstants;
import com.scy.scyDataTool.utilities.ScyUtilities;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;


/**
 * modele pour la table
 * @author Marjolaine bodin
 */
public class DataTableModel extends AbstractTableModel {
    // PROPERTY
    /* owner */
    private MainDataToolPanel owner ;
    /* table */
    private DataTable table;
    /* données correspondantes */
    private Dataset dataset;
    /* tableau total des données */
    private Object[][] tabData;
    /* nombre de lignes */
    private int nbRows;
    /* nombre de colonnes */
    private int nbCols;
    /*nombre de lignes dataset */
    private int nbRowDs;
    /* nombre de colonnes dataset*/
    private int nbColDs;
    
    /* tableau des données */
    private Data[][] datas;
    /* en tetes */
    private DataHeader[] tabHeader;
    /* liste des operations sur les lignes => operations colonnes */
    private ArrayList<DataOperation> listOperationsOnRows;
    /* liste des operations sur les colonnes => operations lignes */
    private ArrayList<DataOperation> listOperationsOnCols;
    /* liste des numeros des lignes */
    private Integer[]tabNoRow;

    // CONSTRUCTOR
    public DataTableModel(MainDataToolPanel owner, DataTable table, Dataset dataset) {
        super();
        this.owner = owner;
        this.table = table;
        this.dataset = dataset;
        loadData();
    }

    /* construction de la table */
    // la premiere ligne est le header
    // la premiere colonne est vide : destinée à mettre les titres des operations sur colonnes
    // tableau des données
    // operations
    // derniere colonne vide pour inserer des operations
    // derniere ligne vide pour inserer des operations
    private void loadData(){
         datas = this.dataset.getData();
        tabHeader = this.dataset.getListDataHeader() ;
        listOperationsOnCols = new ArrayList();
        listOperationsOnRows = new ArrayList();
        ArrayList<DataOperation> listOperation = this.dataset.getListOperation();
        if (listOperation != null){
            int nbOp = listOperation.size();
            for (int o=0; o<nbOp; o++){
                DataOperation op = listOperation.get(o);
                if (op.isOnCol()){
                    listOperationsOnCols.add(op);
                }else
                    listOperationsOnRows.add(op);
            }
        }
        nbRows = 2 + listOperationsOnCols.size();
        nbCols = 2 + listOperationsOnRows.size();
        if (datas != null){
            nbRows += datas.length;
            if (datas.length > 0)
                nbCols += datas[0].length;
        }
        // construction de la table :
        this.tabData = new Object[nbRows][nbCols];
        // => header
        for (int t=0; t<tabHeader.length; t++){
            this.tabData[0][t+1] = tabHeader[t] == null ? "" : tabHeader[t].getValue();
        }
        // => données
         // => numérotation des lignes
        nbRowDs = this.dataset.getNbRows() ;
        nbColDs = this.dataset.getNbCol() ;
        tabNoRow = new Integer[nbRowDs];
        for (int i=0; i<nbRowDs; i++){
            tabNoRow[i] = i+1;
            this.tabData[i+1][0] = i+1;
            for (int j=0; j<nbColDs; j++){
                this.tabData[i+1][j+1] = this.datas[i][j] == null ? "" :this.datas[i][j].getValue();
            }
        }
        // operations
        // => operations sur les colonnes (donc en ligne)
        int nbO = this.listOperationsOnCols.size();
        for (int i=0; i<nbO; i++){
            operateOnCol(listOperationsOnCols.get(i), nbRowDs+i+1);
        }
        // => operations sur les lignes (donc en colonne)
        nbO = this.listOperationsOnRows.size();
        for (int j=0; j<nbO; j++){
            operateOnRow(listOperationsOnRows.get(j), nbColDs+j+1);
        }
    }
    
    @Override
    public int getRowCount() {
        return this.nbRows;
    }

    @Override
    public int getColumnCount() {
        return this.nbCols;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return this.tabData[rowIndex][columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return isValueHeader(rowIndex, columnIndex) || isValueData(rowIndex, columnIndex) ||isValueTitleOperation(rowIndex, columnIndex);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Object oldValue = getValueAt(rowIndex, columnIndex);
        if (((String)aValue).length() == 0)
            return;
        if ((rowIndex == 0 || columnIndex == 0) && ((String)aValue).length() > DataConstants.MAX_LENGHT_DATASET_NAME){
            String msg = owner.getBundleString("MSG_LENGHT_MAX");
            msg  = ScyUtilities.replace(msg, 0, owner.getBundleString("LABEL_DATA"));
            msg = ScyUtilities.replace(msg, 1, ""+DataConstants.MAX_LENGHT_DATASET_NAME);
            owner.displayError(new CopexReturn(msg, false), owner.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        this.tabData[rowIndex][columnIndex] = (String)aValue;
        super.setValueAt(aValue, rowIndex, columnIndex);
        if (isValueHeader(rowIndex, columnIndex)){
            owner.updateDataHeader(dataset, (String)aValue, columnIndex-1);
        }else if (isValueTitleOperation(rowIndex, columnIndex)){
            DataOperation operation = null;
            if(rowIndex == 0){
                int id = columnIndex - 1 - nbColDs;
                if (id >= 0 && id < listOperationsOnRows.size())
                    operation = listOperationsOnRows.get(id);
            }else if (columnIndex == 0){
                int id = rowIndex - 1 - nbRowDs;
                if (id >= 0 && id < listOperationsOnCols.size())
                    operation = listOperationsOnCols.get(id);
            }
            if (operation == null){
                owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_UPDATE_DATA"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
                super.setValueAt(oldValue, rowIndex, columnIndex);
                return;
            }
            owner.updateDataOperation(dataset, (String)aValue, operation);
        }else if (isValueData(rowIndex, columnIndex)){
            double val = 0;
            try{
                val = Double.parseDouble((String)aValue);
            }catch(NumberFormatException e){
                owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_DOUBLE_VALUE"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
                super.setValueAt(oldValue, rowIndex, columnIndex);
                return;
            }
            owner.updateData(dataset,val, rowIndex-1, columnIndex-1);
        }
        table.resizeColumn();
    }
    
    /* applique une operation sur les colonnes */
    private void operateOnCol(DataOperation operation, int noR){
        String name = operation.getName();
        ArrayList<Integer> listNo = operation.getListNo();
        for (int i=0; i<nbColDs; i++){
            int id = listNo.indexOf(i);
            if (id !=-1 && !isIgnoredCol(i)){
                this.tabData[noR][1+i] = this.dataset.getListOperationResult(operation).get(id);
            }else
                this.tabData[noR][1+i] = "-" ;
        }
       /* for (int i=0; i<s; i++){
            ArrayList<Double> listValue = getListValueCol(listNo.get(i));
            if (listValue.size() == 0)
                this.tabData[noR][1+listNo.get(i)] = "-";
            else
                this.tabData[noR][1+listNo.get(i)] = calculate(type, listValue);
        }*/
        this.tabData[noR][0] = name;
    }

    /* retourne vrai si tous les elements d'une colonne sont à ignorer */
    private boolean isIgnoredCol(int id){
        boolean allIgnored = true;
        for (int i=0; i<nbRowDs; i++){
            if (datas[i][id] != null && !datas[i][id].isIgnoredData()){
                allIgnored = false ;
                break;
            }
        }
        return allIgnored ;
    }

    /* retourne vrai si tous les elements d'une ligne sont à ignorer */
    private boolean isIgnoredRow(int id){
        boolean allIgnored = true;
        for (int i=0; i<nbColDs; i++){
            if (datas[id][i] != null &&  !datas[id][i].isIgnoredData()){
                allIgnored = false ;
                break;
            }
        }
        return allIgnored ;
    }


    /* applique une operation sur les lignes */
    private void operateOnRow(DataOperation operation, int noC){
        String name = operation.getName();
        ArrayList<Integer> listNo = operation.getListNo();
        for (int i=0; i<nbRowDs; i++){
            int id = listNo.indexOf(i);
            if (id != -1 && !isIgnoredRow(i)){
                this.tabData[1+i][noC] = this.dataset.getListOperationResult(operation).get(id);
            }else
                this.tabData[1+i][noC] = "-" ;
        }
        /*for (int i=0; i<s; i++){
            ArrayList<Double> listValue = getListValueRow(listNo.get(i));
            if (listValue.size() == 0)
                this.tabData[1+listNo.get(i)][noC] = "-";
            else
                this.tabData[1+listNo.get(i)][noC] = calculate(type, listValue);
        }
         * */
        this.tabData[0][noC] = name;
    }
    
    
    
    
    /* retourne vrai s'il s'agit d'une cellule contenant la numerotation des lignes*/
    public boolean isValueNoRow(int noRow, int noCol){
        return  noCol == 0 && noRow > 0 && noRow <= nbRowDs;
    }
    
     /* retourne vrai s'il s'agit d'une cellule contenant un header (hors titre) */
    public boolean isValueHeader(int noRow, int noCol){
        return  noRow == 0 && noCol > 0 && noCol <= nbColDs;
    }
    
    /* retourne vrai s'il s'agit d'une cellule operations */
    public boolean isValueOperation(int noRow, int noCol){
        return (noRow > 0 && noRow <= nbRowDs && noCol > nbColDs && noCol < this.nbCols -1  ) || (noCol > 0 && noCol <= nbColDs && noRow > nbRowDs && noRow < this.nbRows -1 );
    }


    
    /* retourne vrai s'il s'agit d'une cellule titre operations */
    public boolean isValueTitleOperation(int noRow, int noCol){
        //return  (noRow == 0 && noCol > nbColDs && noCol < this.nbCols -1  ) || (noCol == 0 && noRow > nbRowDs && noRow < this.nbRows - 1);
        return isValueTitleOperationCol(noRow, noCol) || isValueTitleOperationRow(noRow, noCol);
    }

     /* retourne vrai s'il s'agit d'une cellule titre operations en colonne*/
    public boolean isValueTitleOperationCol (int noRow, int noCol){
        return  (noRow == 0 && noCol > nbColDs && noCol < this.nbCols -1  ) ;
    }

     /* retourne vrai s'il s'agit d'une cellule titre operations en ligne */
    public boolean isValueTitleOperationRow (int noRow, int noCol){
        return  (noCol == 0 && noRow > nbRowDs && noRow < this.nbRows - 1);
    }


    /* retourne vrai s'il s'agit d'une cellule de donnée */
    public boolean isValueData(int noRow, int noCol){
        return  noCol > 0 && noCol <= nbColDs && noRow > 0 && noRow <= nbRowDs;
    }
    /* retourne vrai s'il s'agit d'une cellule de donnée ignorée */
    public boolean isValueDataIgnored(int noRow, int noCol){
        return isValueData(noRow, noCol) && this.datas[noRow-1][noCol-1] != null && this.datas[noRow-1][noCol-1].isIgnoredData() ;
    }
    /* retourne vrai s'il s'agit d'une celllue de la dernière ligne (entre 1 et nbColsDs) */
    public boolean isValueLastRow(int noRow, int noCol){
        return (noRow == (nbRows-1)) && (noCol > 0 && noCol <= nbColDs);
    }
    /* retourne vrai s'il s'agit d'une celllue de la dernière colonne (entre 1 et nbRowsDs) */
    public boolean isValueLastCol(int noRow, int noCol){
        return (noCol == (nbCols-1)) && (noRow > 0 && noRow <= nbRowDs);
    }
    
    /* retourne la couleur selon le type d'operations */
    public Color getOperationColor(int noRow, int noCol){
        Color opColor = Color.WHITE ;
        int id=0;
        if (noRow <= nbRowDs && noCol > nbColDs && noCol < this.nbCols -1  ){
            id = noCol - nbColDs - 1;
            opColor = listOperationsOnRows.get(id).getTypeOperation().getColor();
        }else if (noCol <= nbColDs && noRow > nbRowDs && noRow < this.nbRows - 1){
            id = noRow - nbRowDs - 1;
            opColor = listOperationsOnCols.get(id).getTypeOperation().getColor();
        }
        return opColor;
    }


    /* retourne la liste des lignes/colonnes selectionnées */
    public ArrayList<Integer>[] getSelectedRowAndCol(ArrayList<int[]> listSelected){
        ArrayList<Integer>[] tabSel  = new ArrayList[2];
        ArrayList<Integer> listSelectedCol = new ArrayList();
        ArrayList<Integer> listSelectedRow = new ArrayList();
        int nb = listSelected.size();
        for (int i=0; i<nb; i++){
            int[] selCell = listSelected.get(i);
            if(isValueHeader(selCell[0], selCell[1])){
                int noCol = selCell[1]-1 ;
                listSelectedCol.add(noCol);
            }else if (isValueNoRow(selCell[0], selCell[1])){
                int noRow = selCell[0]-1 ;
                listSelectedRow.add(noRow);
            }
        }
        tabSel[0] = listSelectedRow ;
        tabSel[1] = listSelectedCol;
        return tabSel;
    }

    /* retourne les data selectionnées */
    public ArrayList<Data> getSelectedData(ArrayList<int[]> listSelected){
        ArrayList<Data> listSelectedData = new ArrayList();
        int nb = listSelected.size();
        for (int i=0; i<nb; i++){
            int[] selCell = listSelected.get(i);
            Data data = null;
            if (isValueData(selCell[0], selCell[1])){
                data = datas[selCell[0]-1][selCell[1]-1];
            }
            if (data != null)
                listSelectedData.add(data);
        }
        return listSelectedData ;
    }

    /* retourne les operations selectionnées */
    public ArrayList<DataOperation> getSelectedOperation(ArrayList<int[]> listSelected){
        ArrayList<DataOperation> listSelectedOperation = new ArrayList();
        int nb = listSelected.size();
        for (int i=0; i<nb; i++){
            int[] selCell = listSelected.get(i);
            DataOperation op = null;
            if (isValueTitleOperation(selCell[0], selCell[1])){
                if(selCell[0] == 0){// ligne 0 => operation sur les lignes
                    op = listOperationsOnRows.get(selCell[1]-(nbColDs+1));
                }else if (selCell[1] ==0){// colonne 0 => operation sur les colonnes
                    op = listOperationsOnCols.get(selCell[0]-(nbRowDs+1));
                }
            }
            if (op != null)
                listSelectedOperation.add(op);
        }
        return listSelectedOperation ;
    }

    /* retourne un vecteur avec en 0 : boolean col ou ligne, en 1 : nb de col sel, en 2 : id Insert*/
    public ArrayList getSelectedIdForInsert(ArrayList<int[]> listSelected){
        ArrayList v = new ArrayList();
        boolean isOnCol = true;
        int nbInsert = 0;
        int idBefore = -1;
        ArrayList<Integer> listSelectedCol = new ArrayList();
        ArrayList<Integer> listSelectedRow = new ArrayList();
        int nbOpColSel = 0;
        int nbOpRowSel = 0;
        int nb = listSelected.size();
        for (int i=0; i<nb; i++){
            int[] selCell = listSelected.get(i);
            if(isValueHeader(selCell[0], selCell[1])){
                int noCol = selCell[1]-1 ;
                // on inserre en triant du plus petit au plus grand
                int id = -1;
                for (int j=0; j<listSelectedCol.size(); j++){
                    if (listSelectedCol.get(j) > noCol){
                        id = j;
                        break;
                    }
                }
                if (id == -1)
                    listSelectedCol.add(noCol);
                else
                    listSelectedCol.add(id, noCol);
            }else if (isValueNoRow(selCell[0], selCell[1])){
                int noRow = selCell[0]-1 ;
                // on inserre en triant du plus petit au plus grand
                int id = -1;
                for (int j=0; j<listSelectedRow.size(); j++){
                    if (listSelectedRow.get(j) > noRow){
                        id = j;
                        break;
                    }
                }
                if (id == -1)
                    listSelectedRow.add(noRow);
                else
                    listSelectedRow.add(id, noRow);
            }else if (isValueTitleOperationCol(selCell[0], selCell[1])){
                nbOpColSel++;
            }else if (isValueTitleOperationRow(selCell[0], selCell[1])){
                nbOpRowSel++;
            }
        }
        isOnCol = listSelectedCol.size() > 0 || nbOpColSel > 0;
        if (isOnCol){
            nbInsert = listSelectedCol.size()+nbOpColSel ;
            if (listSelectedCol.size() == 0)
                idBefore = this.nbColDs ;
            else{
                idBefore = listSelectedCol.get(0);
            }
        }else{
            nbInsert = listSelectedRow.size()+nbOpRowSel ;
            if (listSelectedRow.size() == 0)
                idBefore = this.nbRowDs ;
            else{
                idBefore = listSelectedRow.get(0);
            }
        }
        v.add(isOnCol);
        v.add(nbInsert);
        v.add(idBefore);
        return v;
    }



    /* retourne le dataset selectionné */
    public Dataset getSelectedDataset(ArrayList<int[]> listSelected){
        int nbC = 0;
        int nbR = this.nbRowDs;
        int nb = listSelected.size();
        ArrayList<Integer> listNoH = new ArrayList();
        for (int i=0; i<nb; i++){
            int[] selCell = listSelected.get(i);
            if (isValueHeader(selCell[0], selCell[1])){
                nbC++;
                listNoH.add(selCell[1]-1);
            }
        }
        DataHeader[] headers = new DataHeader[nbC];
        Data[][] data = new Data[nbR][nbC];
        for (int i=0; i<nbC; i++){
            headers[i] = this.dataset.getDataHeader(listNoH.get(i));
        }
        for (int i=0; i<nbR; i++){
            for (int j=0; j<nbC; j++){
                data[i][j] = this.datas[i][listNoH.get(j)];
            }
        }

        Dataset ds = new Dataset(-1, "", nbC, nbR, headers, data,new ArrayList(), new ArrayList() );
        return ds;
    }



    /* mise à jour du dataset */
    public void updateDataset(Dataset ds, boolean reload){
        this.dataset = ds;
        if (reload)
            loadData();
        this.fireTableStructureChanged();
    }

    /* creation d'une nouvelle operation */
    public void createOperation(Dataset ds, DataOperation operation){
        this.dataset = ds;
        if (operation.isOnCol()){
            int oldnbR = nbRows;
            nbRows += 1;
            listOperationsOnCols.add(operation);
            Object[][] tabDataC = new Object[nbRows][nbCols];
            for (int i=0; i<oldnbR-1; i++){
                for (int j=0; j<nbCols; j++)
                    tabDataC[i][j] = tabData[i][j];
            }
            for (int j=0; j<nbCols; j++)
                tabDataC[oldnbR-1][j] = tabData[oldnbR-1][j];
            tabData = tabDataC ;
            operateOnCol(operation, oldnbR-1);
        }else{// operation en ligne
            int oldnbC = nbCols;
            nbCols += 1;
            listOperationsOnRows.add(operation);
            Object[][] tabDataC = new Object[nbRows][nbCols];
            for (int i=0; i<nbRows; i++){
                for (int j=0; j<oldnbC-1; j++)
                    tabDataC[i][j] = tabData[i][j];
            }
            for (int i=0; i<nbRows; i++){
                tabDataC[i][oldnbC-1] = tabData[i][oldnbC-1];
            }
            tabData = tabDataC ;
            operateOnRow(operation, oldnbC-1);
        }
        this.fireTableStructureChanged();
    }

    /* retourne la liste des no des headers selectionnés */
    public int[] getSelectedNoHeaders(ArrayList<int[]> listSelected){
        int nbH = 0;
        int nb = listSelected.size();
        ArrayList<Integer> listNoH = new ArrayList();
        for (int i=0; i<nb; i++){
            int[] selCell = listSelected.get(i);
            if (isValueHeader(selCell[0], selCell[1])){
                nbH++;
                listNoH.add(selCell[1]-1);
            }
        }
        int[] headers = new int[nbH];
        for (int i=0; i<nbH; i++){
            headers[i] = listNoH.get(i);
        }
        return headers ;
    }

    /* move subData */
    public void moveSubData(SubData subDataToMove, int noColInsertBefore) {
        int[] noHeaders = subDataToMove.getNoHeaders() ;
        int nbC = noHeaders.length;
    }

   

    
}
