/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.dataTool;

import eu.scy.tools.dataProcessTool.common.*;
import eu.scy.tools.dataProcessTool.utilities.CopexReturn;
import eu.scy.tools.dataProcessTool.utilities.DataConstants;
import eu.scy.tools.dataProcessTool.utilities.MyUtilities;
import java.awt.Color;
import java.text.NumberFormat;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;


/**
 * modele pour la table
 * @author Marjolaine bodin
 */
public class DataTableModel extends AbstractTableModel {
    // PROPERTY
    /* owner */
    private FitexToolPanel owner ;
    /* table */
    private DataTable table;
    /* donnees correspondantes */
    private Dataset dataset;
    /* tableau total des donnees */
    private Object[][] tabData;
    /* nombre de lignes */
    private int nbRows;
    /* nombre de colonnes */
    private int nbCols;
    /*nombre de lignes dataset */
    private int nbRowDs;
    /* nombre de colonnes dataset*/
    private int nbColDs;
    
    /* tableau des donnees */
    private Data[][] datas;
    /* en tetes */
    private DataHeader[] tabHeader;
    /* liste des operations sur les lignes => operations colonnes */
    private ArrayList<DataOperation> listOperationsOnRows;
    /* liste des operations sur les colonnes => operations lignes */
    private ArrayList<DataOperation> listOperationsOnCols;
    /* liste des numeros des lignes */
    private Integer[]tabNoRow;

    private NumberFormat numberFormat;

    
    // CONSTRUCTOR
    public DataTableModel(FitexToolPanel owner, DataTable table, Dataset dataset) {
        super();
        this.owner = owner;
        this.table = table;
        this.dataset = dataset;
        this.numberFormat = owner.getNumberFormat();
        loadData();
    }

    /* construction de la table */
    // la premiere ligne est le header
    // la premiere colonne est vide : destinee a mettre les titres des operations sur colonnes
    // tableau des donnees
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
            else{
                nbCols += tabHeader.length;
                nbRows += 1;
            }
        }else{
            nbCols += tabHeader.length;
            nbRows += 1;
        }
        // construction de la table :
        this.tabData = new Object[nbRows][nbCols];
        // => header
        for (int t=0; t<tabHeader.length; t++){
            String[] h = new String[2];
            h[0] = tabHeader[t] == null ? "" : tabHeader[t].getValue() ;
            h[1] = tabHeader[t] == null ? "" : tabHeader[t].getUnit();
            //this.tabData[0][t+1] = tabHeader[t] == null ? "" : tabHeader[t].getValue();
            this.tabData[0][t+1] = h;
        }
        // => donnees
         // => numerotation des lignes
        nbRowDs = this.dataset.getNbRows() ;
        nbColDs = this.dataset.getNbCol() ;
        tabNoRow = new Integer[nbRowDs];
        for (int i=0; i<nbRowDs; i++){
            tabNoRow[i] = i+1;
            this.tabData[i+1][0] = i+1;
            for (int j=0; j<nbColDs; j++){
                String s = "";
                if(this.datas[i][j] != null && this.datas[i][j].isDoubleValue()){
                    s = numberFormat.format(this.datas[i][j].getDoubleValue());
                    if(Double.isNaN(this.datas[i][j].getDoubleValue()))
                        s = "";
                }else if (this.datas[i][j] != null){
                    s = this.datas[i][j].getValue();
                }
                this.tabData[i+1][j+1] =s;
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

    public Dataset getDataset() {
        return dataset;
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
        return  (isValueData(rowIndex, columnIndex) && !isValueDataCompute(rowIndex, columnIndex)) ||isValueTitleOperation(rowIndex, columnIndex);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Object oldValue = getValueAt(rowIndex, columnIndex);
//        if (((String)aValue).length() == 0)
//            return;
        String v1 = "";
        String v2 = "";
        if( aValue instanceof String){
            v1 = (String)aValue ;
        }else if (aValue instanceof String[]){
            v1 = ((String[])aValue)[0];
            v2 = ((String[])aValue)[1];
        }else
            return;
        if ((rowIndex == 0 || columnIndex == 0) && (v1.length() > DataConstants.MAX_LENGHT_DATASET_NAME || v2.length() > DataConstants.MAX_LENGHT_DATASET_NAME)){
            String msg = owner.getBundleString("MSG_LENGHT_MAX");
            msg  = MyUtilities.replace(msg, 0, owner.getBundleString("LABEL_DATA"));
            msg = MyUtilities.replace(msg, 1, ""+DataConstants.MAX_LENGHT_DATASET_NAME);
            owner.displayError(new CopexReturn(msg, false), owner.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        this.tabData[rowIndex][columnIndex] = aValue;
        super.setValueAt(aValue, rowIndex, columnIndex);
        if (isValueHeader(rowIndex, columnIndex)){
            owner.updateDataHeader(dataset, v1, v2,columnIndex-1, dataset.getDataHeader(columnIndex-1).getDescription(), dataset.getDataHeader(columnIndex-1).getType(), dataset.getDataHeader(columnIndex-1).getFormulaValue());
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
                table.setValueAt(oldValue, rowIndex, columnIndex);
                return;
            }
            owner.updateDataOperation(dataset, v1, operation);
        }else if (isValueData(rowIndex, columnIndex)){
            if(dataset.getDataHeader(columnIndex-1).getType().equals(DataConstants.TYPE_DOUBLE)){
                Double val = null;
                v1 = v1.replace(",", ".");
                try{
                    if(v1 != null && !v1.equals("")){
                        val = Double.parseDouble(v1);
                        if(Double.toString(val).length() > DataConstants.MAX_LENGHT_DATA){
                            String msg = owner.getBundleString("MSG_LENGHT_MAX");
                            msg  = MyUtilities.replace(msg, 0, owner.getBundleString("LABEL_DATA"));
                            msg = MyUtilities.replace(msg, 1, ""+DataConstants.MAX_LENGHT_DATA);
                            owner.displayError(new CopexReturn(msg, false), owner.getBundleString("TITLE_DIALOG_ERROR"));
                            setValueAt(oldValue, rowIndex, columnIndex);
                            table.setValueAt(oldValue, rowIndex, columnIndex);
                            table.setCellSelected(rowIndex, columnIndex);
                            return;
                        }
                        owner.updateData(dataset,Double.toString(val), rowIndex-1, columnIndex-1);
                    }else{
                        owner.updateData(dataset, null, rowIndex-1, columnIndex-1);
                    }
                }catch(NumberFormatException e){
                    System.out.println("excep : "+e);
                    owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_DOUBLE_VALUE"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
                    setValueAt(oldValue, rowIndex, columnIndex);
                    table.setValueAt(oldValue, rowIndex, columnIndex);
                    table.setCellSelected(rowIndex, columnIndex);
                    return;
                }
            }else{
                if(v1.length() > DataConstants.MAX_LENGHT_DATA){
                    String msg = owner.getBundleString("MSG_LENGHT_MAX");
                    msg  = MyUtilities.replace(msg, 0, owner.getBundleString("LABEL_DATA"));
                    msg = MyUtilities.replace(msg, 1, ""+DataConstants.MAX_LENGHT_DATA);
                    owner.displayError(new CopexReturn(msg, false), owner.getBundleString("TITLE_DIALOG_ERROR"));
                    setValueAt(oldValue, rowIndex, columnIndex);
                    table.setValueAt(oldValue, rowIndex, columnIndex);
                    table.setCellSelected(rowIndex, columnIndex);
                    return;
                }
                owner.updateData(dataset,v1, rowIndex-1, columnIndex-1);
            }
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
                this.tabData[noR][1+i] = numberFormat.format(this.dataset.getListOperationResult(operation).get(id));
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

    /* retourne vrai si tous les elements d'une colonne sont a ignorer */
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

    /* retourne vrai si tous les elements d'une ligne sont a ignorer */
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
                System.out.println("operateOnRow : "+i+", "+id+", "+noC);
                this.tabData[1+i][noC] = numberFormat.format(this.dataset.getListOperationResult(operation).get(id));
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
    /* retourne vrai s'il s'agit d'une cellule contenant un header (hors titre) de type double */
    public boolean isValueDoubleHeader(int noRow, int noCol){
        return  noRow == 0 && noCol > 0 && noCol <= nbColDs && dataset.getDataHeader(noCol-1).isDouble();
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


    /* retourne vrai s'il s'agit d'une cellule de donnee */
    public boolean isValueData(int noRow, int noCol){
        return  noCol > 0 && noCol <= nbColDs && noRow > 0 && noRow <= nbRowDs;
    }
    /* retourne vrai s'il s'agit d'une cellule de donnee compute*/
    public boolean isValueDataCompute(int noRow, int noCol){
        return isValueData(noRow, noCol) && this.dataset.getDataHeader(noCol-1) != null && this.dataset.getDataHeader(noCol-1).isFormula();
    }
    /* retourne vrai s'il s'agit d'une cellule de donnee ignoree */
    public boolean isValueDataIgnored(int noRow, int noCol){
        return isValueData(noRow, noCol) && this.datas[noRow-1][noCol-1] != null && this.datas[noRow-1][noCol-1].isIgnoredData() ;
    }
    /* retourne vrai s'il s'agit d'une celllue de la derniere ligne (entre 1 et nbColsDs) */
    public boolean isValueLastRow(int noRow, int noCol){
        return (noRow == (nbRows-1)) && (noCol > 0 && noCol <= nbColDs);
    }
    /* retourne vrai s'il s'agit d'une celllue de la derniere colonne (entre 1 et nbRowsDs) */
    public boolean isValueLastCol(int noRow, int noCol){
        return (noCol == (nbCols-1)) && (noRow > 0 && noRow <= nbRowDs);
    }
    public boolean isValueLastCol2(int noRow, int noCol){
        return (noCol == (nbCols-1)) && (noRow==0);
    }
    public boolean isValueLastRow2(int noRow, int noCol){
        return (noRow == (nbRows-1)) && (noCol==0);
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


    /* retourne la liste des lignes/colonnes selectionnees */
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

    /* retourne les data selectionnees */
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

    /* retourne les data selectionnees, hors lignes completes ou colonnes complete */
    public ArrayList<Data> getSelectedDataAlone(ArrayList<int[]> listSelected, ArrayList<Integer>[] listRowAndCol){
        ArrayList<Data> listSelectedData = new ArrayList();
        int nb = listSelected.size();
        for (int i=0; i<nb; i++){
            int[] selCell = listSelected.get(i);
            Data data = null;
            if (isValueData(selCell[0], selCell[1])){
                data = datas[selCell[0]-1][selCell[1]-1];
            }
            if (data != null && listRowAndCol[0].indexOf(selCell[0]-1) == -1 && listRowAndCol[1].indexOf(selCell[1]-1) == -1)
                listSelectedData.add(data);
        }
        return listSelectedData ;
    }

    /* retourne les en tetes selectionnes */
    public ArrayList<DataHeader> getSelectedHeader(ArrayList<int[]> listSelected){
        ArrayList<DataHeader> listSelectedHeader = new ArrayList();
        int nb = listSelected.size();
        for (int i=0; i<nb; i++){
            int[] selCell = listSelected.get(i);
            DataHeader header = null;
            if (isValueHeader(selCell[0], selCell[1])){
                header = this.tabHeader[selCell[1]-1];
            }
            if (header != null)
                listSelectedHeader.add(header);
        }
        return listSelectedHeader ;
    }

    public DataHeader getHeader(int row, int col){
        return this.tabHeader[col-1];
    }


    /* retourne l'indice (i, j) de la cellule selectionnee */
    public int[] getSelectedCell(ArrayList<int[]> listSelected){
        int[] id = new int[2];
        int nb = listSelected.size();
        if(nb > 0){
            int i= listSelected.get(0)[0];
            int j = listSelected.get(0)[1];
            int id1 = -1;
            int id2 = -1;
            if(i == 0 && j == 0){
                id1 = -1;
                id2 = -1;
            }else if (isValueNoRow(i, j)){
                id1 = i-1;
                id2 = -1;
            }else if (j == 0 && i>nbRowDs){
                id1 = nbRowDs;
                id2 = -1;
            }else if (isValueHeader(i, j)){
                id1 = -1;
                id2 = j-1;
            }else if (i ==0 && j> nbColDs){
                id1 = -1;
                id2 = nbColDs;
            }else if (isValueData(i, j)){
                id1 = i-1;
                id2 = j-1;
            }else {
                id1 = i-1;
                if(i> nbRowDs)
                    i = nbRowDs;
                id2 = j-1;
                if (j> nbColDs)
                    j = nbColDs ;
            }
            id[0] = id1;
            id[1] = id2;
            return id;
        }else
            return null;
    }
    /* retourne les operations selectionnees */
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
        boolean lastCol = false;
        boolean lastRow = false;
        int nb = listSelected.size();
        for (int i=0; i<nb; i++){
            int[] selCell = listSelected.get(i);
            if(isValueHeader(selCell[0], selCell[1])){
                int noCol = selCell[1]-1 ;
                // on insere en triant du plus petit au plus grand
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
                // on insere en triant du plus petit au plus grand
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
            }else if (isValueLastCol2(selCell[0], selCell[1])){
                lastCol = true;
            }else if(isValueLastRow2(selCell[0], selCell[1])){
                lastRow = true;
            }
        }
        //System.out.println("liste : "+(listSelectedCol.size()>0));
        //System.out.println(" nbop : "+nbOpColSel);
        //System.out.println(" lastCOl : "+lastCol);
        isOnCol = listSelectedCol.size() > 0 || nbOpColSel > 0 || lastCol;
        if (isOnCol){
            nbInsert = listSelectedCol.size()+nbOpColSel ;
            if (lastCol){
                idBefore = this.nbColDs ;
                nbInsert++;
            }else if(listSelectedCol.size() == 0){
                idBefore = this.nbColDs;
            }else{
                idBefore = listSelectedCol.get(0);
            }
        }else{
            nbInsert = listSelectedRow.size()+nbOpRowSel ;
            if (lastRow){
                idBefore = this.nbRowDs ;
                nbInsert++;
            }else if(listSelectedRow.size() == 0){
                idBefore = this.nbRowDs;
            }else{
                idBefore = listSelectedRow.get(0);
            }
        }
        //System.out.println("=> "+isOnCol);
        //System.out.println(" "+nbInsert);
        //System.out.println(" "+idBefore);
        v.add(isOnCol);
        v.add(nbInsert);
        v.add(idBefore);
        return v;
    }



    /* retourne le dataset selectionne */
    public Dataset getSelectedDataset(ArrayList<int[]> listSelected){
        int nb = listSelected.size();
        // col et ligne sel
        boolean[] colSel = new boolean[nbColDs];
        boolean[] rowSel = new boolean[nbRowDs];
        for (int i=0; i<nbRowDs; i++){
            rowSel[i] = false;
        }
        for (int j=0; j<nbColDs; j++){
            colSel[j] = false;
        }
        for (int i=0; i<nb; i++){
            int[] selCell = listSelected.get(i);
            if (isValueData(selCell[0], selCell[1]) ){
                rowSel[selCell[0]-1] = true;
                colSel[selCell[1]-1] = true;
            }
        }
        int nbR = 0;
        for (int i=0; i<nbRowDs; i++){
            if(rowSel[i])
                nbR++;
        }
        int nbC = 0;
        for (int j=0; j<nbColDs; j++){
            if(colSel[j])
                nbC++;
        }
        // correspondance
        int[] corrRow = new int[nbR];
        int id=0;
        for (int i=0; i<nbRowDs; i++){
            if(rowSel[i]){
                corrRow[id] = i;
                id++;
            }
        }
        int[] corrCol = new int[nbC];
        id=0;
        for (int j=0; j<nbColDs; j++){
            if(colSel[j]){
                corrCol[id] = j;
                id++;
            }
        }
        // creation d'un dataset nbR / nbC
        DataHeader[] headers = new DataHeader[nbC];
        Data[][] data = new Data[nbR][nbC];
        for (int i=0; i<nb; i++){
            int[] selCell = listSelected.get(i);
            if (isValueHeader(selCell[0], selCell[1])){
                int idC=-1;
                for (int k=0; k<nbC; k++){
                    if (corrCol[k] == (selCell[1]-1)){
                        idC = k;
                        break;
                    }
                }
                if(idC != -1)
                    headers[idC] = this.dataset.getDataHeader(selCell[1]-1);
            }else if (isValueData(selCell[0], selCell[1])){
                int idC=-1;
                for (int k=0; k<nbC; k++){
                    if (corrCol[k] == (selCell[1]-1)){
                        idC = k;
                        break;
                    }
                }
                int idR = -1;
                for (int k=0; k<nbR; k++){
                    if (corrRow[k] == (selCell[0]-1)){
                        idR = k;
                        break;
                    }
                }
                if(idR != -1 && idC != -1){
                    data[idR][idC] = this.dataset.getData(selCell[0]-1, selCell[1]-1);
                }
            }
        }

        Dataset ds = new Dataset(-1,null, "subData", nbC, nbR, headers, data,getSelectedOperation(listSelected), new ArrayList() );
        return ds;
    }



    /* mise a jour du dataset */
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

    /* retourne la liste des no des headers selectionnes */
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


   public int[] getBorders(int row, int col){
       int top = 1;
       int bottom = 0;
       int left = 1;
       int right = 0;
       int[] borders = new int[4];
       // top : premiere cell ou derniere col(-sauf premiere ligne)
       if((row==0&& col==0) || (col == nbCols-1 && row >0)){
           top = 0;
       }
       //bottom : derniere ligne
       if(row==nbRows-1){
           bottom = 1;
       }
       //left :
       if( (row==0 && col==0) || (row== nbRows-1 && col>0) ){
           left = 0;
       }
       //right
       if(col==nbCols-1){
           right = 1;
       }

       borders[0] = top;
       borders[1] = left;
       borders[2] = bottom;
       borders[3] = right;
       return borders;
   }

   public boolean isAllSelectionIgnore(ArrayList<int[]> listSelected){
        int nb = listSelected.size();
        for (int i=0; i<nb; i++){
            int[] selCell = listSelected.get(i);
            if (isValueData(selCell[0], selCell[1])){
                if(datas[selCell[0]-1][selCell[1]-1] == null)
                    return false;
                if(!datas[selCell[0]-1][selCell[1]-1].isIgnoredData())
                    return false;
            }else
                return false;
        }
        return true ;
   }
    
}
