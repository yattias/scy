/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.dataTool;

import eu.scy.client.tools.dataProcessTool.controller.FitexNumber;
import eu.scy.client.tools.dataProcessTool.common.*;
import eu.scy.client.tools.dataProcessTool.controller.ScyMath;
import eu.scy.client.tools.dataProcessTool.utilities.CopexReturn;
import eu.scy.client.tools.dataProcessTool.utilities.DataConstants;
import eu.scy.client.tools.dataProcessTool.utilities.MyUtilities;
import java.awt.Color;
import java.text.NumberFormat;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;


/**
 * table model
 * @author Marjolaine bodin
 */
public class DataTableModel extends AbstractTableModel {
    /* owner */
    private FitexToolPanel owner ;
    /* table */
    private DataTable table;
    /* data */
    private Dataset dataset;
    /* final data array */
    private Object[][] tabData;
    /* number of rows */
    private int nbRows;
    /* number of columns */
    private int nbCols;
    /*number of rows dataset */
    private int nbRowDs;
    /* number of columns dataset*/
    private int nbColDs;
    
    /* datas */
    private Data[][] datas;
    /* headers */
    private DataHeader[] tabHeader;
    /* list of operations on rows => column operation  */
    private ArrayList<DataOperation> listOperationsOnRows;
    /* list of operations on columns => row operation */
    private ArrayList<DataOperation> listOperationsOnCols;
    /* list rows number */
    private Integer[]tabNoRow;

    private NumberFormat numberFormat;

    
    public DataTableModel(FitexToolPanel owner, DataTable table, Dataset dataset) {
        super();
        this.owner = owner;
        this.table = table;
        this.dataset = dataset;
        this.numberFormat = owner.getNumberFormat();
        loadData();
    }

    /* build the table */
    // lfrist line is the header
    // first column contains rows number and operations title
    // data table
    // operations
    // last column empty (to insert operations, or columns)
    // last row empty, to insert rows or operations
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
        // build the table
        this.tabData = new Object[nbRows][nbCols];
        // => header
        for (int t=0; t<tabHeader.length; t++){
            String[] h = new String[2];
            h[0] = tabHeader[t] == null ? "" : tabHeader[t].getValue() ;
            h[1] = tabHeader[t] == null ? "" : tabHeader[t].getUnit();
            //this.tabData[0][t+1] = tabHeader[t] == null ? "" : tabHeader[t].getValue();
            this.tabData[0][t+1] = h;
        }
        // => data
         // => line numbering
        nbRowDs = this.dataset.getNbRows() ;
        nbColDs = this.dataset.getNbCol() ;
        tabNoRow = new Integer[nbRowDs];
        for (int i=0; i<nbRowDs; i++){
            tabNoRow[i] = i+1;
            this.tabData[i+1][0] = (i+1)+" ";
            for (int j=0; j<nbColDs; j++){
                String s = "";
                if(this.datas[i][j] != null && this.datas[i][j].isDoubleValue()){
                    //s = numberFormat.format(this.datas[i][j].getDoubleValue());
                    s = FitexNumber.getFormat(this.datas[i][j].getValue(), this.dataset.isScientificNotation(j), this.dataset.getNbShownDecimals(j), this.dataset.getNbSignificantDigits(j), owner.getLocale());
                    if(Double.isNaN(this.datas[i][j].getDoubleValue()))
                        s = "";
                }else if (this.datas[i][j] != null){
                    s = this.datas[i][j].getValue();
                }
                this.tabData[i+1][j+1] =s;
            }
        }
        
        // operations
        // => operations on columns (so on row...)
        int nbO = this.listOperationsOnCols.size();
        for (int i=0; i<nbO; i++){
            operateOnCol(listOperationsOnCols.get(i), nbRowDs+i+1);
        }
        // => operations on rows(so on columns...)
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
        if(dataset.getRight() ==  DataConstants.NONE_RIGHT)
            return false;
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
        if (controlLenght() &&  (rowIndex == 0 || columnIndex == 0) && (v1.length() > DataConstants.MAX_LENGHT_DATASET_NAME || v2.length() > DataConstants.MAX_LENGHT_DATASET_NAME)){
            String msg = owner.getBundleString("MSG_LENGHT_MAX");
            msg  = MyUtilities.replace(msg, 0, owner.getBundleString("LABEL_DATA"));
            msg = MyUtilities.replace(msg, 1, ""+DataConstants.MAX_LENGHT_DATASET_NAME);
            owner.displayError(new CopexReturn(msg, false), owner.getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        this.tabData[rowIndex][columnIndex] = aValue;
        super.setValueAt(aValue, rowIndex, columnIndex);
        if (isValueHeader(rowIndex, columnIndex)){
            owner.updateDataHeader(dataset, v1, v2,columnIndex-1, dataset.getDataHeader(columnIndex-1).getDescription(), dataset.getDataHeader(columnIndex-1).getType(), dataset.getDataHeader(columnIndex-1).getFormulaValue(),  dataset.getDataHeader(columnIndex-1).isScientificNotation(),  dataset.getDataHeader(columnIndex-1).getNbShownDecimals(),  dataset.getDataHeader(columnIndex-1).getNbSignificantDigits(), true);
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
                        if(controlLenght() && Double.toString(val).length() > DataConstants.MAX_LENGHT_DATA){
                            String msg = owner.getBundleString("MSG_LENGHT_MAX");
                            msg  = MyUtilities.replace(msg, 0, owner.getBundleString("LABEL_DATA"));
                            msg = MyUtilities.replace(msg, 1, ""+DataConstants.MAX_LENGHT_DATA);
                            owner.displayError(new CopexReturn(msg, false), owner.getBundleString("TITLE_DIALOG_ERROR"));
                            setValueAt(oldValue, rowIndex, columnIndex);
                            table.setValueAt(oldValue, rowIndex, columnIndex);
                            table.setCellSelected(rowIndex, columnIndex);
                            return;
                        }
                        owner.updateData(dataset,(String)aValue, rowIndex-1, columnIndex-1, true);
                    }else{
                        owner.updateData(dataset, null, rowIndex-1, columnIndex-1, true);
                    }
                }catch(NumberFormatException e){
                    owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_DOUBLE_VALUE"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
                    setValueAt(oldValue, rowIndex, columnIndex);
                    table.setValueAt(oldValue, rowIndex, columnIndex);
                    table.setCellSelected(rowIndex, columnIndex);
                    return;
                }
            }else{
                if(controlLenght() && v1.length() > DataConstants.MAX_LENGHT_DATA){
                    String msg = owner.getBundleString("MSG_LENGHT_MAX");
                    msg  = MyUtilities.replace(msg, 0, owner.getBundleString("LABEL_DATA"));
                    msg = MyUtilities.replace(msg, 1, ""+DataConstants.MAX_LENGHT_DATA);
                    owner.displayError(new CopexReturn(msg, false), owner.getBundleString("TITLE_DIALOG_ERROR"));
                    setValueAt(oldValue, rowIndex, columnIndex);
                    table.setValueAt(oldValue, rowIndex, columnIndex);
                    table.setCellSelected(rowIndex, columnIndex);
                    return;
                }
                owner.updateData(dataset,v1, rowIndex-1, columnIndex-1, true);
            }
        }
        table.resizeColumn();
    }
    
    /* returns the list of values taken into account in calculating the column operation*/
    public ArrayList<String> getStringListValueCol(int idCol){
        ArrayList<String> listValue = new ArrayList();
        if(dataset.getDataHeader(idCol).isDouble()){
            for (int i=0; i<nbRowDs; i++){
                Data d = dataset.getData(i,idCol);
                if (d != null && !d.isIgnoredData() && ! Double.isNaN(d.getDoubleValue()) ){
                    listValue.add(""+this.tabData[i+1][idCol+1]);
                }
            }
        }
        return listValue;
    }

    /* returns the list of values taken into account in calculating the row operation */
    public ArrayList<String> getStringListValueRow(int idRow){
        ArrayList<String> listValue = new ArrayList();
        for (int j=0; j<nbColDs ; j++){
            if(dataset.getDataHeader(j).isDouble()){
                Data d = dataset.getData(idRow, j);
                if (d != null && !d.isIgnoredData()&& !Double.isNaN(d.getDoubleValue()) ){
                    listValue.add((String)this.tabData[idRow+1][j+1]);
                }
            }
        }
        return listValue;
    }

    
    /* operation on column */
    private void operateOnCol(DataOperation operation, int noR){
        String name = operation.getName();
        ArrayList<Integer> listNo = operation.getListNo();
        for (int i=0; i<nbColDs; i++){
            int id = listNo.indexOf(i);
            if (id !=-1 && !isIgnoredCol(i)){
                //this.tabData[noR][1+i] = numberFormat.format(this.dataset.getListOperationResult(operation).get(id));
                this.tabData[noR][1+i] = getOperationValue(operation, i, getStringListValueCol(i), ""+this.dataset.getListOperationResult(operation).get(id));
            }else
                this.tabData[noR][1+i] = "-" ;
        }
       /* for (int i=0; i<s; i++){getOperationValue
            ArrayList<Double> listValue = getListValueCol(listNo.get(i));
            if (listValue.size() == 0)
                this.tabData[noR][1+listNo.get(i)] = "-";
            else
                this.tabData[noR][1+listNo.get(i)] = calculate(type, listValue);
        }*/
        this.tabData[noR][0] = name;
    }

    /* returns true if all elements of a column are  ignored */
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

    /* returns true if all elements of a row are ignored  */
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

    /* operation on row */
    private void operateOnRow(DataOperation operation, int noC){
        String name = operation.getName();
        ArrayList<Integer> listNo = operation.getListNo();
        for (int i=0; i<nbRowDs; i++){
            int id = listNo.indexOf(i);
            if (id != -1 && !isIgnoredRow(i)){
                //this.tabData[1+i][noC] = numberFormat.format(this.dataset.getListOperationResult(operation).get(id));
                this.tabData[1+i][noC] = getOperationValue(operation, i, getStringListValueRow(i), ""+this.dataset.getListOperationResult(operation).get(id));
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
    
    
    public String getOperationValue(DataOperation operation, int id,  ArrayList<String> numberList, String result){
        if(operation.getTypeOperation().getCodeName().equals("SUM")){
            return FitexNumber.getSumValue(numberList, result, owner.getLocale());
        }else if (operation.getTypeOperation().getCodeName().equals("AVG")){
            ArrayList<Double> listValue = new ArrayList();
            if (operation.isOnCol())
                listValue  = dataset.getListValueCol(id);
            else
                listValue = dataset.getListValueRow(id);
            double sum = ScyMath.calculate(getSumOperation(), listValue);
            return FitexNumber.getAvgValue(numberList,FitexNumber.getSumValue(numberList, ""+sum, owner.getLocale()), result, owner.getLocale());
        }else{
            return result;
        }
    }

    private TypeOperation getSumOperation(){
        return owner.getOperation(DataConstants.OP_SUM);
    }
    
    /* returns true if the cell contains the number of a row */
    public boolean isValueNoRow(int noRow, int noCol){
        return  noCol == 0 && noRow > 0 && noRow <= nbRowDs;
    }
    
     /* returns true if the cell contains a header (no operation title)*/
    public boolean isValueHeader(int noRow, int noCol){
        return  noRow == 0 && noCol > 0 && noCol <= nbColDs;
    }
    /* returns true if the cell contains a header with double type */
    public boolean isValueDoubleHeader(int noRow, int noCol){
        return  noRow == 0 && noCol > 0 && noCol <= nbColDs && (dataset.getDataHeader(noCol-1) == null ||dataset.getDataHeader(noCol-1).isDouble() );
    }
    
    /* returns true if the cell contains an operation */
    public boolean isValueOperation(int noRow, int noCol){
        return (noRow > 0 && noRow <= nbRowDs && noCol > nbColDs && noCol < this.nbCols -1  ) || (noCol > 0 && noCol <= nbColDs && noRow > nbRowDs && noRow < this.nbRows -1 );
    }


    
    /* returns true if the cell contains the title of an operation  */
    public boolean isValueTitleOperation(int noRow, int noCol){
        //return  (noRow == 0 && noCol > nbColDs && noCol < this.nbCols -1  ) || (noCol == 0 && noRow > nbRowDs && noRow < this.nbRows - 1);
        return isValueTitleOperationCol(noRow, noCol) || isValueTitleOperationRow(noRow, noCol);
    }

     /*returns true if the cell contains operations title on column */
    public boolean isValueTitleOperationCol (int noRow, int noCol){
        return  (noRow == 0 && noCol > nbColDs && noCol < this.nbCols -1  ) ;
    }

     /* returns true if the cell contains operations title on row*/
    public boolean isValueTitleOperationRow (int noRow, int noCol){
        return  (noCol == 0 && noRow > nbRowDs && noRow < this.nbRows - 1);
    }


    /* return true if the cell contains data */
    public boolean isValueData(int noRow, int noCol){
        return  noCol > 0 && noCol <= nbColDs && noRow > 0 && noRow <= nbRowDs;
    }
    /* returns true if the cell contains compute data */
    public boolean isValueDataCompute(int noRow, int noCol){
        return isValueData(noRow, noCol) && this.dataset.getDataHeader(noCol-1) != null && this.dataset.getDataHeader(noCol-1).isFormula();
    }
    /*returns true if the cells contains ignored data  */
    public boolean isValueDataIgnored(int noRow, int noCol){
        return isValueData(noRow, noCol) && this.datas[noRow-1][noCol-1] != null && this.datas[noRow-1][noCol-1].isIgnoredData() ;
    }
    /* returns true if the cell is in the last row  (between 1 & nbColsDs) */
    public boolean isValueLastRow(int noRow, int noCol){
        return (noRow == (nbRows-1)) && (noCol > 0 && noCol <= nbColDs);
    }
    /* returns true if the cell is in the last column  (between 1 and nbRowsDs) */
    public boolean isValueLastCol(int noRow, int noCol){
        return (noCol == (nbCols-1)) && (noRow > 0 && noRow <= nbRowDs);
    }
    public boolean isValueLastCol2(int noRow, int noCol){
        return (noCol == (nbCols-1)) && (noRow==0);
    }
    public boolean isValueLastRow2(int noRow, int noCol){
        return (noRow == (nbRows-1)) && (noCol==0);
    }
    /* returns the color, depending the operation type */
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


    /* returns the list of row/column selected  */
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

    /* returns data selected  */
    public ArrayList<Data> getSelectedData(ArrayList<int[]> listSelected, boolean computeNull){
        ArrayList<Data> listSelectedData = new ArrayList();
        int nb = listSelected.size();
        for (int i=0; i<nb; i++){
            int[] selCell = listSelected.get(i);
            Data data = null;
            if (isValueData(selCell[0], selCell[1])){
                data = datas[selCell[0]-1][selCell[1]-1];
                if(data == null && computeNull){
                    data = new Data(-1, null, selCell[0]-1, selCell[1]-1, true);
                }
            }
            if (data != null)
                listSelectedData.add(data);
        }
        return listSelectedData ;
    }

    /* returns data selected, exclue entire rows or columns  */
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

    /* returns header selected */
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


    /* returns index  (i, j) of the selected cell */
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
    /* return operations selected */
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

    /* returns in v[0] = boolean col or row, v[1] =nb col selected, v[2] : id Insert*/
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
                // insert, by sorting from smallest to largest
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
                // insert, by sorting from smallest to largest
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
            }else if(listSelectedCol.isEmpty()){
                idBefore = this.nbColDs;
            }else{
                idBefore = listSelectedCol.get(0);
            }
        }else{
            nbInsert = listSelectedRow.size()+nbOpRowSel ;
            if (lastRow){
                idBefore = this.nbRowDs ;
                nbInsert++;
            }else if(listSelectedRow.isEmpty()){
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

    /* get the selected data for copy */
    public CopyDataset getCopyDataset(ArrayList<int[]> listSelected){
        ArrayList<DataOperation> listOp = getSelectedOperation(listSelected);
        ArrayList<DataHeader> listHeader = getSelectedHeader(listSelected);
        ArrayList<Integer> listRow = getSelectedRowAndCol(listSelected)[0];
        ArrayList<Data> listData = getSelectedData(listSelected, true);
        CopyDataset copyDs = new CopyDataset(listData, listHeader, listRow, listOp);
        return copyDs;
    }

    /* update  dataset */
    public void updateDataset(Dataset ds, boolean reload){
        this.dataset = ds;
        if (reload)
            loadData();
        this.fireTableStructureChanged();
    }

    /* creation of a new operation */
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
        }else{// operation on row
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

    /* returns the list of no headers selected */
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
       // top : first cell or last col (-except the first row)
       if((row==0&& col==0) || (col == nbCols-1 && row >0)){
           top = 0;
       }
       //bottom : last row
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

   /*returns the nb of rows of data, ignores the last lines that are empty*/
   public int getNbRowData(){
       return dataset.getNbMaxRowsData();
   }

   private boolean controlLenght(){
        return owner.controlLenght();
    }

    
}
