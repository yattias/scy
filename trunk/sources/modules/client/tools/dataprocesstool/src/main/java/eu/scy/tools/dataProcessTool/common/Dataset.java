/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.common;

import eu.scy.elo.contenttype.dataset.DataSet;
import eu.scy.elo.contenttype.dataset.DataSetColumn;
import eu.scy.elo.contenttype.dataset.DataSetHeader;
import eu.scy.elo.contenttype.dataset.DataSetRow;
import eu.scy.tools.dataProcessTool.controller.ScyMath;
import eu.scy.tools.dataProcessTool.pdsELO.GraphVisualization;
import java.util.ArrayList;

import eu.scy.tools.dataProcessTool.pdsELO.ProcessedData;
import eu.scy.tools.dataProcessTool.pdsELO.ProcessedDatasetELO;
import eu.scy.tools.dataProcessTool.utilities.DataConstants;
import eu.scy.tools.fitex.GUI.DrawPanel;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

/**
 * donnees + operations
 * @author Marjolaine Bodin
 */
public class Dataset implements Cloneable{
    //PROPERTY
    /* identifiant*/
    protected long dbKey;
    /* name */
    protected String name;
    /* nombre de colonnes */
    protected int nbCol;
    /* nombre de lignes  sans l'en tete*/
    protected int nbRows;
    /* header */
    protected DataHeader[] listDataHeader;
    /* donnees */
    protected Data[][] data;
    /* liste des operations */
    protected ArrayList<DataOperation> listOperation;
    /* liste des resultats associes */
    protected ArrayList<ArrayList<Double>> listOperationResult;
    /* liste des visualizations */
    protected ArrayList<Visualization> listVisualization;
    /* est ouvert */
    protected boolean isOpen;

    
    // CONSTRUCTOR
    public Dataset(long dbKey, String name, int nbCol, int nbRows, DataHeader[] listDataHeader, Data[][] data, ArrayList<DataOperation> listOperation,ArrayList<Visualization> listVisualization) {
        this.dbKey = dbKey;
        this.name = name;
        this.nbCol = nbCol;
        this.nbRows = nbRows;
        this.listDataHeader = listDataHeader;
        this.data = data;
        this.listOperation = listOperation;
        this.listOperationResult = new ArrayList();
        this.listVisualization = listVisualization ;
        this.isOpen = true;
        calculateOperation();
    }
    

   
     // GETTER AND SETTER
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

    public Data[][] getData() {
        return data;
    }

    public void setData(Data[][] data) {
        this.data = data;
    }

    

    public DataHeader[] getListDataHeader() {
        return listDataHeader;
    }

    public void setListDataHeader(DataHeader[] listDataHeader) {
        this.listDataHeader = listDataHeader;
    }

    public ArrayList<DataOperation> getListOperation() {
        return listOperation;
    }

    public void setListOperation(ArrayList<DataOperation> listOperation) {
        this.listOperation = listOperation;
    }

    public ArrayList<Visualization> getListVisualization() {
        return listVisualization;
    }

    public void setListVisualization(ArrayList<Visualization> listVisualization) {
        this.listVisualization = listVisualization;
    }

    public int getNbCol() {
        return nbCol;
    }

    public void setNbCol(int nbCol) {
        this.nbCol = nbCol;
    }

    public int getNbRows() {
        return nbRows;
    }

    public void setNbRows(int nbRows) {
        this.nbRows = nbRows;
    }

    public boolean isOpen(){
            return this.isOpen;
    }
    public void setOpen(boolean isOpen){
        this.isOpen = isOpen ;
    }

    public ArrayList<ArrayList<Double>> getListOperationResult() {
        return listOperationResult;
    }

    public void setListOperationResult(ArrayList<ArrayList<Double>> listOperationResult) {
        this.listOperationResult = listOperationResult;
    }
    // CLONE
    @Override
    public Object clone()  {
        try {
            Dataset dataset = (Dataset) super.clone() ;
            long dbKeyC = this.dbKey;
            String nameC = new String(this.name);
            int nbColC = new Integer(this.nbCol);
            int nbRowC = new Integer(this.nbRows);
            boolean isOpenC = isOpen ;
            DataHeader[] listDataHeaderC = null;
            if (this.listDataHeader != null){
                int nb = this.listDataHeader.length;
                listDataHeaderC = new DataHeader[nb];
                for (int i=0; i<nb; i++){
                    if (listDataHeader[i] != null)
                        listDataHeaderC[i] = (DataHeader)listDataHeader[i].clone();
                }
            }
            ArrayList<DataOperation> listOperationC = null;
            if (this.listOperation != null){
                listOperationC = new ArrayList();
                int nb = this.listOperation.size();
                for (int i=0; i<nb; i++){
                    listOperationC.add((DataOperation)listOperation.get(i).clone());
                }
            }
            ArrayList<ArrayList<Double>> listResultC = null ;
            if (this.listOperationResult != null){
                listResultC = new ArrayList();
                int nb = this.listOperationResult.size();
                for (int i=0; i<nb; i++){
                    int nbR = this.listOperationResult.get(i).size() ;
                    ArrayList<Double> l = new ArrayList();
                    for (int j=0; j<nbR; j++){
                        l.add(this.listOperationResult.get(i).get(j));
                    }
                    listResultC.add(l);
                }
            }
            ArrayList<Visualization> listVisualizationC = null;
            if (this.listVisualization != null){
                listVisualizationC = new ArrayList();
                int nb = this.listVisualization.size();
                for (int i=0; i<nb; i++){
                    listVisualizationC.add((Visualization)listVisualization.get(i).clone());
                }
            }
            Data[][] dataC = null;
            if (this.data != null){
                int nbR = this.data.length;
                dataC = new Data[nbR][];
                for (int i=0; i<nbR; i++){
                    int nbC = this.data[i].length;
                    dataC[i] = new Data[nbC];
                    for (int j=0; j<nbC; j++){
                        if (this.data[i][j] != null)
                            dataC[i][j] = (Data)this.data[i][j].clone();
                    }
                }
            }
            
            dataset.setDbKey(dbKeyC);
            dataset.setName(nameC);
            dataset.setNbCol(nbColC);
            dataset.setNbRows(nbRowC);
            dataset.setListDataHeader(listDataHeaderC);
            dataset.setListOperation(listOperationC);
            dataset.setListOperationResult(listResultC);
            dataset.setListVisualization(listVisualizationC);
            dataset.setData(dataC);
            dataset.setOpen(isOpenC);
            
            return dataset;
        } catch (CloneNotSupportedException e) { 
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
	}
    }

    // METHODE
    /* ajout d'une operation */
    public void addOperation(DataOperation operation){
        this.listOperation.add(operation);
        operate(operation, this.listOperation.size()-1);
    }

    /*retourne le header avec l'indice de la colonne - null sinon */
    public DataHeader getDataHeader(int colIndex){
        return this.listDataHeader[colIndex];
    }

    /* met le DataHeader a jour */
    public void setDataHeader(DataHeader header, int columnIndex){
        this.listDataHeader[columnIndex] = header;
    }
    /*retourne le data avec l'indice de la ligne /colonne - null sinon */
    public Data getData(int rowIndex, int colIndex){
        return this.data[rowIndex][colIndex];
    }

    /* met le data a jour */
    public void setData(Data data, int rowIndex, int columnIndex){
        this.data[rowIndex][columnIndex] = data;
    }

    /* indice de la visualization */
    public int getIdVisualization(long dbKey){
        int nb = listVisualization.size();
        for (int i=0; i<nb; i++){
            if (listVisualization.get(i).getDbKey() == dbKey)
                return i;
        }
        return -1;
    }
    /* supprime une visualization */
    public void removeVisualization(Visualization vis ){
        int id = getIdVisualization(vis.getDbKey());
        if (id == -1)
            return;
        listVisualization.remove(id);
    }

    /* ajout d'une visualization */
    public void addVisualization(Visualization vis){
        this.listVisualization.add(vis);
    }

    /* return a processed dataset ELO */
    public ProcessedDatasetELO toELO(Locale locale){
        int nbR = getNbRows() ;
        int nbC = getNbCol() ;
        // creation of the ELO
        // DATASET
        // headers
        List<DataSetHeader> headers = new LinkedList<DataSetHeader>() ;
        List<DataSetColumn> variables = new LinkedList<DataSetColumn>();
        for (int j=0; j<nbC; j++){
            DataSetColumn dsCol = new DataSetColumn(getDataHeader(j) == null ?"" : getDataHeader(j).getValue(), "", "double") ;
            variables.add(dsCol);
        }
        DataSetHeader header = new DataSetHeader(variables, locale);
        headers.add(header);
        // rows
        List<eu.scy.tools.dataProcessTool.pdsELO.Data> listIgnoredData = new LinkedList<eu.scy.tools.dataProcessTool.pdsELO.Data>();

        // => dataset
        DataSet ds = new DataSet(headers);

        for (int i=0; i<nbR; i++){
            List<String> values = new LinkedList<String>();
            for (int j=0; j<nbC; j++){
                values.add(new String(getData(i, j) == null ?"":Double.toString(getData(i, j).getValue())));
                if (getData(i, j) != null && getData(i, j).isIgnoredData())
                    listIgnoredData.add(new eu.scy.tools.dataProcessTool.pdsELO.Data(Integer.toString(i), Integer.toString(j)));
            }
            DataSetRow row = new DataSetRow(values);
            ds.addRow(row);
        }

        // PROCESSED DATASET
        eu.scy.tools.dataProcessTool.pdsELO.IgnoredData iData =new eu.scy.tools.dataProcessTool.pdsELO.IgnoredData(listIgnoredData);
        List<eu.scy.tools.dataProcessTool.pdsELO.Operation> listOperations = new LinkedList<eu.scy.tools.dataProcessTool.pdsELO.Operation>();
        int nbop = getListOperation().size();
        ArrayList<ArrayList<Double>> listResult = getListOperationResult() ;
        for (int i=0; i<nbop; i++){
            DataOperation myOp = getListOperation().get(i);
            List<String> references = new LinkedList<String>();
            List<String> results = new LinkedList<String>();
            List<String> allParam = null;
            if (myOp instanceof DataOperationParam){
                allParam= new LinkedList<String>();
                ParamOperation[] allP  = ((DataOperationParam)myOp).getAllParam() ;
                for (int k=0; k<allP.length; k++){
                    allParam.add(allP[k] == null ? "" :""+allP[k].getValue());
                }
            }
            ArrayList<Double> listResop = new ArrayList();
            if (i< listResult.size()){
                listResop = listResult.get(i);
            }
            for (int k=0; k<myOp.getListNo().size(); k++){
                references.add(Integer.toString(myOp.getListNo().get(k)));
            }
            for (int k=0; k<listResop.size();k++){
                results.add(Double.toString(listResop.get(k)));
            }
            eu.scy.tools.dataProcessTool.pdsELO.Operation operation  = new eu.scy.tools.dataProcessTool.pdsELO.Operation(myOp.isOnCol(), myOp.getName(), myOp.getName(), "double", myOp.getTypeOperation().getCodeName(), references, results, allParam);
            listOperations.add(operation);
        }
        List<eu.scy.tools.dataProcessTool.pdsELO.Visualization> listVisualizations = new LinkedList<eu.scy.tools.dataProcessTool.pdsELO.Visualization>();
        int nbVis = getListVisualization().size();
        for (int i=0; i<nbVis; i++){
            Visualization myVis = getListVisualization().get(i);
            TypeVisualization type = myVis.getType();
            eu.scy.tools.dataProcessTool.pdsELO.Visualization vis = null;
            if (type.getCode() == DataConstants.VIS_GRAPH){
                Graph g = (Graph)myVis ;
                ParamGraph pg = g.getParamGraph() ;
                List<eu.scy.tools.dataProcessTool.pdsELO.FunctionModel> listFunction = getListFunctionModel(g.getListFunctionModel());
                vis = new GraphVisualization(DataConstants.TYPE_VIS_GRAPH, myVis.getName(), myVis.isOnCol,g.getTabNo()[0], g.getTabNo()[1],
                        pg.getX_name(), pg.getY_name(), pg.getX_min(), pg.getX_max(), pg.getDeltaX(), pg.getY_min(), pg.getY_max(), pg.getDeltaY(),
                        DrawPanel.SCATTER_PLOT_COLOR.getRed(), DrawPanel.SCATTER_PLOT_COLOR.getGreen(), DrawPanel.SCATTER_PLOT_COLOR.getBlue(),
                        listFunction);
            }else if (type.getCode() == DataConstants.VIS_PIE){
                vis = new eu.scy.tools.dataProcessTool.pdsELO.PieVisualization(DataConstants.TYPE_VIS_PIE, myVis.getName(), myVis.isOnCol, myVis.getTabNo()[0]);
            }else if (type.getCode() == DataConstants.VIS_BAR){
                vis = new eu.scy.tools.dataProcessTool.pdsELO.BarVisualization(DataConstants.TYPE_VIS_BAR, myVis.getName(), myVis.isOnCol, myVis.getTabNo()[0]);
            }
            if(vis != null)
                listVisualizations.add(vis);
        }
        ProcessedData pds = new ProcessedData(iData, listOperations, listVisualizations);
        ProcessedDatasetELO pdsELO = new ProcessedDatasetELO(ds, pds);
        return pdsELO ;
    }

    private List<eu.scy.tools.dataProcessTool.pdsELO.FunctionModel> getListFunctionModel(ArrayList<FunctionModel> listFm){
        List<eu.scy.tools.dataProcessTool.pdsELO.FunctionModel> list = null;
        if (listFm != null){
            int nb = listFm.size();
            list = new LinkedList();
            for (int i=0; i<nb; i++){
                FunctionModel fm = listFm.get(i);
                eu.scy.tools.dataProcessTool.pdsELO.FunctionModel f = new eu.scy.tools.dataProcessTool.pdsELO.FunctionModel(fm.getDescription(), fm.getColor().getRed(), fm.getColor().getGreen(), fm.getColor().getBlue());
                list.add(f);
            }
        }
        return list;
    }

    /* indice d'une operation */
    public int getIdOperation(long dbKey){
        int nb = listOperation.size();
        for (int i=0; i<nb; i++){
            if (listOperation.get(i).getDbKey() == dbKey)
                return i;
        }
        return -1;
    }

    /* suppression d'une operation*/
    public void removeOperation(DataOperation operation){
        int id = getIdOperation(operation.getDbKey());
        if (id == -1)
            return;
        listOperation.remove(id);
    }

    /* suppression d'un header , attention ne supprime pas les data */
  /*  public void removeHeader(DataHeader header){
        DataHeader[] copyHeader = new DataHeader[listDataHeader.length-1];
        int id=0;
        int noCol = header.getNoCol() ;
        for (int i=0; i<listDataHeader.length; i++){
            if (header.getDbKey() == listDataHeader[i].getDbKey())
                continue;
            copyHeader[id] = listDataHeader[i];
            if (copyHeader[id].getNoCol() > noCol)
                copyHeader[id].setNoCol(copyHeader[id].getNoCol()-1);
            id++;
        }
        setListDataHeader(copyHeader);
    }*/
    public void removeHeader(int no){
        DataHeader[] copyHeader = new DataHeader[listDataHeader.length-1];
        for (int i=0; i<listDataHeader.length; i++){
            if (i<no)
                copyHeader[i] = listDataHeader[i];
            else if (i > no){
                copyHeader[i-1] =listDataHeader[i];
                if (copyHeader[i-1] != null)
                    copyHeader[i-1].setNoCol(copyHeader[i-1].getNoCol()-1);
            }
        }
        setListDataHeader(copyHeader);
    }

    /*suppression d'un numero pour une operation */
    public void removeNoOperation(long dbKeyOp, int no){
        int id = getIdOperation(dbKeyOp);
        if (id ==-1)
            return;
        listOperation.get(id).removeNo(no);
    }
    
    /* supprime des donnees et met a jour le nombre de lignes et de colonnes */
    public void removeData(ArrayList<Integer>[] listRowAndCol){
        int nbRowsSel = listRowAndCol[0].size();
        int nbColsSel = listRowAndCol[1].size();
        // suppression des colonnes, on commence par le numero le plus grand
        ArrayList<Integer> listCol = getSortList(listRowAndCol[1]);
        for (int i=0; i<nbColsSel; i++){
            deleteCol(listCol.get(i));
        }
        // suppression des lignes, on commence par le numero le plus grand
        ArrayList<Integer> listRow = getSortList(listRowAndCol[0]);
        for (int i=0; i<nbRowsSel; i++){
            deleteRow(listRow.get(i));
        }

    }

    /* suppression d'une colonne  */
    private void deleteCol(int no){
        Data[][] newData = new Data[this.nbRows][this.nbCol-1];
        for (int i=0; i<nbRows; i++){
            for (int j=0; j<nbCol; j++){
                if (j<no)
                    newData[i][j] = this.data[i][j];
                else if (j> no){
                    Data d = this.data[i][j] ;
                    if(d != null)
                        d.setNoCol(j-1) ;
                    newData[i][j-1] = d;
                }
            }
        }
        setData(newData);
        // maj list operation
        int nbOp = listOperation.size();
        for (int i=0; i<nbOp; i++){
            if (listOperation.get(i).isOnCol()){
                ArrayList<Integer> listNo = listOperation.get(i).getListNo() ;
                int idNo = listNo.indexOf(new Integer(no));
                boolean remove = listNo.remove(new Integer(no));
                int size = listNo.size();
                for (int k=0; k<size; k++){
                    if (listNo.get(k) > no)
                        listNo.set(k, new Integer(listNo.get(k) -1 ));
                }
                if (remove && i<listOperationResult.size()){
                    listOperationResult.get(i).remove(idNo);
                }

            }
        }
        // maj list visualization
        int nbVis = listVisualization.size();
        for (int i=0; i<nbVis; i++){
            if (listVisualization.get(i).isOnCol ){
                int[] tabNo = listVisualization.get(i).getTabNo() ;
                int size = tabNo.length;
                if (listVisualization.get(i).isOnNo(no))
                    size--;
                int[] newTab = new int[size];
                int id = 0;
                for (int k=0; k<tabNo.length; k++){
                    if (tabNo[k] <no)
                        newTab[id++] = tabNo[k];
                    else if (tabNo[k] > no)
                        newTab[id++] = tabNo[k]-1;
                }
                listVisualization.get(i).setTabNo(newTab);
            }
        }
        // maj nb Col
        this.nbCol = this.nbCol-1;
    }

    /* suppression d'une ligne  */
    private void deleteRow(int no){
        Data[][] newData = new Data[this.nbRows-1][this.nbCol];
        for (int i=0; i<nbRows; i++){
            for (int j=0; j<nbCol; j++){
                if (i<no)
                    newData[i][j] = this.data[i][j];
                else if (i> no){
                    Data d = this.data[i][j] ;
                    if(d != null)
                        d.setNoRow(i-1);
                    newData[i-1][j] = d;
                }
            }
        }
        setData(newData);
        // maj list operation
        int nbOp = listOperation.size();
        for (int i=0; i<nbOp; i++){
            if (!listOperation.get(i).isOnCol()){
                ArrayList<Integer> listNo = listOperation.get(i).getListNo() ;
                int idNo = listNo.indexOf(new Integer(no));
                boolean remove = listNo.remove(new Integer(no));
                int size = listNo.size();
                for (int k=0; k<size; k++){
                    if (listNo.get(k) > no)
                        listNo.set(k, new Integer(listNo.get(k) -1 ));
                }
                if (remove){
                    listOperationResult.get(i).remove(idNo);
                }

            }
        }
        // maj list visualization
        int nbVis = listVisualization.size();
        for (int i=0; i<nbVis; i++){
            if (!listVisualization.get(i).isOnCol ){
                int[] tabNo = listVisualization.get(i).getTabNo() ;
                int size = tabNo.length;
                if (listVisualization.get(i).isOnNo(no))
                    size--;
                int[] newTab = new int[size];
                int id = 0;
                for (int k=0; k<tabNo.length; k++){
                    if (tabNo[k] <no)
                        newTab[id++] = tabNo[k];
                    else if (tabNo[k] > no)
                        newTab[id++] = tabNo[k]-1;
                }
                listVisualization.get(i).setTabNo(newTab);
            }
        }
        // maj nb Row
        this.nbRows = this.nbRows-1;
    }

   /* tri la liste du plus grand au plus petit */
    private ArrayList<Integer> getSortList(ArrayList<Integer> list){
        int nb = list.size();
        for (int i=1; i<nb; i++){
            if (list.get(i)> list.get(i-1)){
                int val = list.get(i);
                for (int j=0; j<i; j++){
                    if (list.get(j) < val ){
                        list.remove(i);
                        list.add(j, val);
                        break;
                    }
                }
            }
        }
        return list;
    }

    /* insertion de lignes : nbRowsToInsert avant le no idBefore
     * si id=0 : au debut, si id =nbRows : en fin
     */
    public void insertRow(int nbRowsToInsert, int idBefore){
      //data
       Data[][] newData = new Data[this.nbRows+nbRowsToInsert][this.nbCol];
       for (int i=0; i<this.nbRows; i++){
           for (int j=0; j<this.nbCol; j++){
               if (i<idBefore){
                   newData[i][j] = this.data[i][j];
               }else {
                   newData[i+nbRowsToInsert][j] = this.data[i][j];
               }
           }
       }
       setData(newData);
       // header : pas de changement
       // list operation : mise a jour des no
       int nbOp = listOperation.size();
        for (int i=0; i<nbOp; i++){
            if (!listOperation.get(i).isOnCol()){
                ArrayList<Integer> listNo = listOperation.get(i).getListNo() ;
                int size = listNo.size();
                for (int k=0; k<size; k++){
                    if (listNo.get(k) >= idBefore)
                        listNo.set(k, new Integer(listNo.get(k) +nbRowsToInsert ));
                }
            }
        }
       // list visualization : mise a jour des no
       int nbVis = listVisualization.size();
        for (int i=0; i<nbVis; i++){
            if (!listVisualization.get(i).isOnCol()){
                int[] listNo = listVisualization.get(i).getTabNo() ;
                int size = listNo.length;
                for (int k=0; k<size; k++){
                    if (listNo[k] >= idBefore)
                        listNo[k] =listNo[k] +nbRowsToInsert ;
                }
            }
        }
       // maj nb Row
        this.nbRows = this.nbRows+nbRowsToInsert;
    }

    /* insertion de colonnes : nbColsToInsert avant le no idBefore
     * si id=0 : au debut, si id =nbCols : en fin
     */
    public void insertCol(int nbColsToInsert, int idBefore){
      //data
       Data[][] newData = new Data[this.nbRows][this.nbCol+nbColsToInsert];
       for (int i=0; i<this.nbRows; i++){
           for (int j=0; j<this.nbCol; j++){
               if (j<idBefore){
                   newData[i][j] = this.data[i][j];
               }else {
                   newData[i][j+nbColsToInsert] = this.data[i][j];
               }
           }
       }
       setData(newData);
       // header
       DataHeader[] headers = new DataHeader[this.listDataHeader.length+nbColsToInsert];
       for (int i=0; i< this.listDataHeader.length; i++){
           if (i< idBefore)
               headers[i] =  this.listDataHeader[i];
           else
               headers[i+nbColsToInsert] = this.listDataHeader[i];
       }
       setListDataHeader(headers);
       // list operation : mise a jour des no
       int nbOp = listOperation.size();
        for (int i=0; i<nbOp; i++){
            if (listOperation.get(i).isOnCol()){
                ArrayList<Integer> listNo = listOperation.get(i).getListNo() ;
                int size = listNo.size();
                for (int k=0; k<size; k++){
                    if (listNo.get(k) >= idBefore)
                        listNo.set(k, new Integer(listNo.get(k) +nbColsToInsert ));
                }
            }
        }
       // list visualization : mise a jour des no
       int nbVis = listVisualization.size();
        for (int i=0; i<nbVis; i++){
            if (listVisualization.get(i).isOnCol()){
                int[] listNo = listVisualization.get(i).getTabNo() ;
                int size = listNo.length;
                for (int k=0; k<size; k++){
                    if (listNo[k] >= idBefore)
                        listNo[k] =listNo[k] +nbColsToInsert ;
                }
            }
        }
       // maj nb Col
        this.nbCol = this.nbCol+nbColsToInsert;
    }

    /* liste des operations sur les lignes */
    public ArrayList<DataOperation> getListOperationOnRows(){
        ArrayList<DataOperation> list = new ArrayList();
        int nb = this.listOperation.size();
        for (int i=0; i<nb; i++){
            if (!listOperation.get(i).isOnCol())
                list.add(listOperation.get(i));
        }
        return list;
    }

    /* liste des operations sur les colonnes */
    public ArrayList<DataOperation> getListOperationOnCols(){
        ArrayList<DataOperation> list = new ArrayList();
        int nb = this.listOperation.size();
        for (int i=0; i<nb; i++){
            if (listOperation.get(i).isOnCol())
                list.add(listOperation.get(i));
        }
        return list;
    }
    /* calcul des operations */
    public void calculateOperation(){
        int nbOp = this.listOperation.size();
        for (int i=0; i<nbOp; i++){
            operate(this.listOperation.get(i), i);
        }
    }

    /* calcul d'une operation */
    private void operate(DataOperation operation, int id){
        ArrayList<Double> listResult = new ArrayList();
        TypeOperation type = operation.getTypeOperation();
        ArrayList<Integer> listNo = operation.getListNo();
        int s = listNo.size();
        for (int i=0; i<s; i++){
            ArrayList<Double> listValue = new ArrayList();
            if (operation.isOnCol())
                listValue  = getListValueCol(listNo.get(i));
            else
                listValue = getListValueRow(listNo.get(i));
            double r;
            if (operation instanceof DataOperationParam)
                r = ScyMath.calculateParam(type, listValue, ((DataOperationParam)operation).getAllParam());
            else
                r = ScyMath.calculate(type, listValue);
            listResult.add(r);
        }
        if (this.listOperationResult.size() > id)
            this.listOperationResult.set(id, listResult) ;
        else
            this.listOperationResult.add(listResult);
    }

    /* retourne la liste des valeurs prises en compte pour le calcul dans la colonne donnee */
    private ArrayList<Double> getListValueCol(int idCol){
        ArrayList<Double> listValue = new ArrayList();
        for (int i=0; i<nbRows; i++){
            Data d = this.data[i][idCol];
            if (d != null && !d.isIgnoredData()){
                listValue.add(d.getValue());
            }
        }
        return listValue;
    }

    /* retourne la liste des valeurs prises en compte pour le calcul dans la ligne donnee */
    private ArrayList<Double> getListValueRow(int idRow){
        ArrayList<Double> listValue = new ArrayList();
        for (int j=0; j<nbCol ; j++){
            Data d = this.data[idRow][j];
            if (d != null && !d.isIgnoredData()){
                listValue.add(d.getValue());
            }
        }
        return listValue;
    }

    /* retourne les resultats d'une operation */
    public ArrayList<Double> getListOperationResult(DataOperation operation){
        int id = getIdOperation(operation.getDbKey());
        if (id == -1)
            return null;
        return this.listOperationResult.get(id);
    }

    /* ecriture dataset */
    @Override
    public String  toString(){
        String toString = "*****************************************\n";
        toString += "dataset : "+this.getName()+"  - "+this.getNbRows()+" / "+this.getNbCol() +"\n";
        for (int i=0; i<listDataHeader.length; i++){
            toString += (listDataHeader[i] == null ? "" : listDataHeader[i].getValue())+" / ";
        }
        toString += "\n" ;
        for (int i=0; i<nbRows; i++){
            for (int j=0; j<nbCol; j++){
                toString += (data[i][j] == null ? " " : data[i][j].getValue())+" / ";
            }
            toString += "\n" ;
        }
        toString += "OPERATION : \n";
        for (int i=0; i<listOperation.size(); i++){
            toString += listOperation.get(i).toString();
            String s= "";
            for (int k=0; k<listOperationResult.get(i).size(); k++){
                s += listOperationResult.get(i).get(k)+" / ";
            }
            toString += "result : "+s +"\n";
        }
        toString += "VISUALIZATION : \n";
        for (int i=0; i<listVisualization.size(); i++){
            toString += listVisualization.get(i).toString()+"\n";
        }
        toString += "*****************************************\n";
        return toString;
    }

    /**
    * Renvoit la longueur max du champ de la colonne passee en parametre par son numero
    * Renvoit -1 si erreur
    * @return int
    * @param col int
    */
    public int getValueMaxSizeIn(int col) {
        int maxlenght = 0;
        if(col < getNbCol()){
            for (int i=0; i<nbRows; i++){
                Data d = this.data[i][col];
                int l = 0;
                if (d != null){
                    l = Double.toString(d.getValue()).length();
                }
                maxlenght = Math.max(maxlenght, l);
            }
        }else{
            ArrayList<Double> list = this.getListOperationResult(getListOperationOnRows().get(col-getNbCol()));;
            int nb = list.size();
            for (int i=0; i<nb; i++){
                int l=0;
                if (list.get(i) != null && !list.get(i).isNaN()){
                    l = Double.toString(list.get(i)).length();
                }
                maxlenght = Math.max(maxlenght, l);
            }
        }
        return maxlenght ;
    }

    /* longueur max avant la virgule et longeur max apres la virgule */
    public int[] getValueMaxDoubleSizeIn(int col) {
        int maxlenght1 = 0;
        int maxlenght2 = 0;
        int[] max = new int[2];
        if(col < getNbCol()){
            for (int i=0; i<nbRows; i++){
                Data d = this.data[i][col];
                int l1 = 0;
                int l2 = 0;
                if (d != null){
                    String s = Double.toString(d.getValue());
                    if (s.startsWith("-"))
                        s = s.substring(1);
                    l1 = s.indexOf(".");
                    l2 = s.length()-l1-1;
                }
                maxlenght1 = Math.max(maxlenght1, l1);
                maxlenght2 = Math.max(maxlenght2, l2);
            }
        }else{
            ArrayList<Double> list = this.getListOperationResult(getListOperationOnRows().get(col-getNbCol()));;
            int nb = list.size();
            for (int i=0; i<nb; i++){
                int l1=0;
                int l2=0;
                if (list.get(i) != null && !list.get(i).isNaN()){
                    String s = Double.toString(list.get(i));
                    if (s.startsWith("-"))
                        s = s.substring(1);
                    l1 = s.indexOf(".");
                    l2 = s.length()-l1-1;
                }
                maxlenght1 = Math.max(maxlenght1, l1);
                maxlenght2 = Math.max(maxlenght2, l2);
            }
        }
        max[0] = maxlenght1;
        max[1] = maxlenght2;
        return max ;
    }

    /* echange de lignes */
    public void exchange(Vector exchange){
        Data[][] newData = new Data[nbRows][nbCol];
        for (int i=0; i<nbRows; i++){
            for (int j=0; j<nbCol;j++ ){
                newData[i][j] = this.data[(Integer)exchange.get(i)][j];
                if (newData[i][j] != null){
                    newData[i][j].setNoRow(i);
                }
            }
        }

        setData(newData);
        // operations
        int nbOp = listOperation.size();
        for (int i=0; i<nbOp; i++){
            if (!listOperation.get(i).isOnCol()){
                ArrayList<Integer> listNo = listOperation.get(i).getListNo() ;
                ArrayList<Integer> newListNo = new ArrayList();
                int size = listNo.size();
                for (int k=0; k<size; k++){
                    newListNo.add((Integer)exchange.get(listNo.get(k)));
                }
                listOperation.get(i).setListNo(newListNo);
            }
        }
        // visualizations
        int nbVis = listVisualization.size();
        for (int i=0; i<nbVis; i++){
            if (!listVisualization.get(i).isOnCol()){
                int[] tabNo = listVisualization.get(i).getTabNo() ;
                int[]newTabNo = new int[tabNo.length];
                for (int k=0; k<tabNo.length; k++){
                    newTabNo[k] = (Integer)exchange.get(tabNo[k]);
                }
                listVisualization.get(i).setTabNo(newTabNo);
            }
        }
        calculateOperation();
    }
}
