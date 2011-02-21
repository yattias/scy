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
import eu.scy.tools.dataProcessTool.pdsELO.XYAxis;
import eu.scy.tools.dataProcessTool.utilities.DataConstants;
import eu.scy.tools.fitex.GUI.DrawPanel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import org.jdom.Element;

/**
 * donnees + operations
 * @author Marjolaine Bodin
 */
public class Dataset implements Cloneable{
    private final static String TAG_DATASET = "dataset";
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
            DataSetColumn dsCol = new DataSetColumn(getDataHeader(j) == null ?"" : getDataHeader(j).getValue(), "", getDataHeader(j) == null ?"" : getDataHeader(j).getUnit()) ;
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
                values.add(new String(getData(i, j) == null ?Double.toString(Double.NaN):Double.toString(getData(i, j).getValue())));
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
                List<XYAxis> axis = getAxis(pg);
                vis = new GraphVisualization(DataConstants.TYPE_VIS_GRAPH, myVis.getName(), axis, pg.getX_min(), pg.getX_max(), pg.getDeltaX(), pg.getY_min(), pg.getY_max(), pg.getDeltaY(),
                        DrawPanel.SCATTER_PLOT_COLOR_1.getRed(), DrawPanel.SCATTER_PLOT_COLOR_1.getGreen(), DrawPanel.SCATTER_PLOT_COLOR_1.getBlue(),
                        listFunction);
            }else if (type.getCode() == DataConstants.VIS_PIE){
                vis = new eu.scy.tools.dataProcessTool.pdsELO.PieVisualization(DataConstants.TYPE_VIS_PIE, myVis.getName(),  ((SimpleVisualization)myVis).getNoCol());
            }else if (type.getCode() == DataConstants.VIS_BAR){
                vis = new eu.scy.tools.dataProcessTool.pdsELO.BarVisualization(DataConstants.TYPE_VIS_BAR, myVis.getName(),  ((SimpleVisualization)myVis).getNoCol());
            }
            if(vis != null)
                listVisualizations.add(vis);
        }
        ProcessedData pds = new ProcessedData(this.name, iData, listOperations, listVisualizations);
        ProcessedDatasetELO pdsELO = new ProcessedDatasetELO(ds, pds);
        return pdsELO ;
    }

    private List<XYAxis> getAxis(ParamGraph pg){
        List<XYAxis> axis = new LinkedList();
        for (Iterator<PlotXY> p = pg.getPlots().iterator();p.hasNext();){
            PlotXY plot = p.next();
            XYAxis a = new XYAxis(plot.getHeaderX().getNoCol(), plot.getHeaderY().getNoCol(), plot.getHeaderX().getValue(), plot.getHeaderY().getValue());
            axis.add(a);
        }
        return axis;
    }

    private List<eu.scy.tools.dataProcessTool.pdsELO.FunctionModel> getListFunctionModel(ArrayList<FunctionModel> listFm){
        List<eu.scy.tools.dataProcessTool.pdsELO.FunctionModel> list = null;
        if (listFm != null){
            int nb = listFm.size();
            list = new LinkedList();
            for (int i=0; i<nb; i++){
                FunctionModel fm = listFm.get(i);
                List<eu.scy.tools.dataProcessTool.pdsELO.FunctionParam> listParam = getListFunctionParam(listFm.get(i));
                eu.scy.tools.dataProcessTool.pdsELO.FunctionModel f = new eu.scy.tools.dataProcessTool.pdsELO.FunctionModel(fm.getDescription(), fm.getColor().getRed(), fm.getColor().getGreen(), fm.getColor().getBlue(), listParam);
                list.add(f);
            }
        }
        return list;
    }

    private List<eu.scy.tools.dataProcessTool.pdsELO.FunctionParam> getListFunctionParam(FunctionModel functionModel){
        List<eu.scy.tools.dataProcessTool.pdsELO.FunctionParam> listParam = new LinkedList();
        int nb = functionModel.getListParam().size();
        for (int i=0; i<nb; i++){
            eu.scy.tools.dataProcessTool.pdsELO.FunctionParam p = new eu.scy.tools.dataProcessTool.pdsELO.FunctionParam(functionModel.getListParam().get(i).getParam(), functionModel.getListParam().get(i).getValue());
            listParam.add(p);
        }
        return listParam;
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
        listOperationResult.remove(id);
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
//    public void removeData(ArrayList<Integer>[] listRowAndCol){
//        int nbRowsSel = listRowAndCol[0].size();
//        int nbColsSel = listRowAndCol[1].size();
//        // suppression des colonnes, on commence par le numero le plus grand
//        ArrayList<Integer> listCol = getSortList(listRowAndCol[1]);
//        for (int i=0; i<nbColsSel; i++){
//            deleteCol(listCol.get(i));
//        }
//        // suppression des lignes, on commence par le numero le plus grand
//        ArrayList<Integer> listRow = getSortList(listRowAndCol[0]);
//        for (int i=0; i<nbRowsSel; i++){
//            deleteRow(listRow.get(i));
//        }
//
//    }

    /* supprime des donnees*/
    public void removeData(ArrayList<Data> listData){
        for(Iterator<Data> d = listData.iterator();d.hasNext();){
            Data adata = d.next();
            setData(null,adata.getNoRow(), adata.getNoCol());
        }
    }

    /* suppression de lignes */
    public ArrayList[] removeRows(ArrayList<Integer> listRow){
        ArrayList[] tabDel = new ArrayList[4];
        ArrayList<DataOperation> listOpToDel = new ArrayList();
        ArrayList<Visualization> listVisToDel = new ArrayList();
        ArrayList<DataOperation> listOpToUpdate = new ArrayList();
        ArrayList<Visualization> listVisToUpdate = new ArrayList();
        int nbRowsSel = listRow.size();
        ArrayList<Integer> list = getSortList(listRow);
        for (int i=0; i<nbRowsSel; i++){
            ArrayList[] listV = deleteRow(list.get(i));
            for(Iterator<DataOperation> l = listV[0].iterator();l.hasNext();){
                listOpToUpdate.add(l.next());
            }
            for(Iterator<DataOperation> l = listV[1].iterator();l.hasNext();){
                listOpToDel.add(l.next());
            }
            for(Iterator<Visualization> l = listV[2].iterator();l.hasNext();){
                listVisToUpdate.add(l.next());
            }
            for(Iterator<Visualization> l = listV[3].iterator();l.hasNext();){
                listVisToDel.add(l.next());
            }
        }
        tabDel[0] = listOpToUpdate;
        tabDel[1] = listOpToDel;
        tabDel[2] = listVisToUpdate;
        tabDel[3] = listVisToDel;
        return tabDel;
    }

    /* suppression de colonnes */
    public ArrayList[] removeCols(ArrayList<Integer> listCol){
        ArrayList[] tabDel = new ArrayList[4];
        ArrayList<DataOperation> listOpToDel = new ArrayList();
        ArrayList<Visualization> listVisToDel = new ArrayList();
        ArrayList<DataOperation> listOpToUpdate = new ArrayList();
        ArrayList<Visualization> listVisToUpdate = new ArrayList();
        int nbColsSel = listCol.size();
        ArrayList<Integer> list = getSortList(listCol);
        for (int i=0; i<nbColsSel; i++){
            ArrayList[] listV = deleteCol(list.get(i));
            for(Iterator<DataOperation> l = listV[0].iterator();l.hasNext();){
                listOpToUpdate.add(l.next());
            }
            for(Iterator<DataOperation> l = listV[1].iterator();l.hasNext();){
                listOpToDel.add(l.next());
            }
            for(Iterator<Visualization> l = listV[2].iterator();l.hasNext();){
                listVisToUpdate.add(l.next());
            }
            for(Iterator<Visualization> l = listV[3].iterator();l.hasNext();){
                listVisToDel.add(l.next());
            }
        }
        tabDel[0] = listOpToUpdate;
        tabDel[1] = listOpToDel;
        tabDel[2] = listVisToUpdate;
        tabDel[3] = listVisToDel;
        return tabDel;
    }

    /* suppression d'une colonne  */
    private ArrayList[] deleteCol(int no){
        ArrayList[] tabDel = new ArrayList[4];
        ArrayList<DataOperation> listOperationToUpdate = new ArrayList();
        ArrayList<DataOperation> listOperationToDel = new ArrayList();
        ArrayList<Visualization> listVisualizationToUpdate = new ArrayList();
        ArrayList<Visualization> listVisualizationToDel = new ArrayList();
        // maj list operation
        // clone les operations
        ArrayList<DataOperation> listOpC = new ArrayList();
        for(Iterator<DataOperation> o = listOperation.iterator();o.hasNext();){
            listOpC.add((DataOperation)o.next().clone());
        }
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
                if(size > 0 && remove)
                    listOperationToUpdate.add(listOpC.get(i));
                if (remove && i<listOperationResult.size()){
                    listOperationResult.get(i).remove(idNo);
                }

            }
        }
        // suppresssion des operations qui n'ont pas lieu d'etre
        for (int i=nbOp-1; i>=0; i--){
            if (listOperation.get(i).isOnCol()){
                ArrayList<Integer> listNo = listOperation.get(i).getListNo() ;
                int size = listNo.size();
                if(size == 0){
                    listOperationToDel.add(listOpC.get(i));
                    removeOperation(listOperation.get(i));
                }
            }
        }
        // clone les vis
        ArrayList<Visualization> listVisC = new ArrayList();
        for(Iterator<Visualization> o = listVisualization.iterator();o.hasNext();){
            listVisC.add((Visualization)o.next().clone());
        }
        // maj list visualization
        int nbVis = listVisualization.size();
        for (int i=nbVis-1; i>=0; i--){
            Visualization v = listVisualization.get(i);
            if (v.isOnNo(no)){
                if(v instanceof SimpleVisualization){
                    listVisualization.remove(i);
                    listVisualizationToDel.add(listVisC.get(i));
                }else if(v instanceof Graph){
                    boolean remove = ((Graph)v).removePlotWithNo(no);
                    int nb = ((Graph)v).getNbPlots();
                    if(nb==0){
                        listVisualizationToDel.add(listVisC.get(i));
                        listVisualization.remove(i);
                    }else if(remove)
                        listVisualizationToUpdate.add(listVisC.get(i));
                }
            }
        }
        for(int i=0; i<listVisualization.size(); i++){
            Visualization v = listVisualization.get(i);
            if(v instanceof SimpleVisualization){
                int n = ((SimpleVisualization)v).getNoCol();
                if ( n > no){
                    ((SimpleVisualization)v).setNoCol(n-1);
                }
            }else if (v instanceof Graph){
                //((Graph)v).updateNoCol(no, -1);
            }
        }
        // maj header
        removeHeader(no);
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
        
        // maj nb Col
        this.nbCol = this.nbCol-1;
        tabDel[0] = listOperationToUpdate;
        tabDel[1] = listOperationToDel;
        tabDel[2] = listVisualizationToUpdate;
        tabDel[3] = listVisualizationToDel;
        return tabDel;
    }

    /* suppression d'une ligne  */
    private ArrayList[] deleteRow(int no){
        ArrayList<DataOperation> listOperationToUpdate = new ArrayList();
        ArrayList<DataOperation> listOperationToDel = new ArrayList();
        ArrayList<Visualization> listVisualizationToUpdate = new ArrayList();
        ArrayList<Visualization> listVisualizationToDel = new ArrayList();
        // maj list operation
        // clone les operations
        ArrayList<DataOperation> listOpC = new ArrayList();
        for(Iterator<DataOperation> o = listOperation.iterator();o.hasNext();){
            listOpC.add((DataOperation)o.next().clone());
        }
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
                if(size > 0 && remove)
                    listOperationToUpdate.add(listOpC.get(i));
                if (remove){
                    listOperationResult.get(i).remove(idNo);
                }

            }
        }
        // suppresssion des operations qui n'ont pas lieu d'etre
        for (int i=nbOp-1; i>=0; i--){
            if (!listOperation.get(i).isOnCol()){
                ArrayList<Integer> listNo = listOperation.get(i).getListNo() ;
                int size = listNo.size();
                if(size == 0){
                    listOperationToDel.add(listOpC.get(i));
                    removeOperation(listOperation.get(i));
                }
            }
        }
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
        
        // maj nb Row
        this.nbRows = this.nbRows-1;
        ArrayList[] tab = new ArrayList[4];
        tab[0] = listOperationToUpdate;
        tab[1] = listOperationToDel;
        tab[2] = listVisualizationToUpdate;
        tab[3] = listVisualizationToDel;
        return tab;
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
                   if(newData[i+nbRowsToInsert][j] != null){
                       newData[i+nbRowsToInsert][j].setNoRow(i+nbRowsToInsert);
                   }
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
                   if(newData[i][j+nbColsToInsert] != null){
                       newData[i][j+nbColsToInsert].setNoCol(j+nbColsToInsert);
                   }
               }
           }
       }
       setData(newData);
       // header
       DataHeader[] headers = new DataHeader[this.listDataHeader.length+nbColsToInsert];
       for (int i=0; i< this.listDataHeader.length; i++){
           if (i< idBefore)
               headers[i] =  this.listDataHeader[i];
           else{
               headers[i+nbColsToInsert] = this.listDataHeader[i];
               if(headers[i+nbColsToInsert] != null){
                       headers[i+nbColsToInsert].setNoCol(i+nbColsToInsert);
                   }
           }
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
            Visualization v = listVisualization.get(i);
            if(v instanceof SimpleVisualization){
                int n= ((SimpleVisualization)v).getNoCol();
                if(n >= idBefore){
                    ((SimpleVisualization)v).setNoCol(n+nbColsToInsert);
                }
            }else if (v instanceof Graph){
                ArrayList<PlotXY> plots = ((Graph)v).getParamGraph().getPlots();
                for(Iterator<PlotXY> p = plots.iterator();p.hasNext();){
                    PlotXY plot = p.next();
                    if(plot.getHeaderX().getNoCol() >= idBefore){
                        plot.getHeaderX().setNoCol(plot.getHeaderX().getNoCol()+ nbColsToInsert);
                    }
                    if(plot.getHeaderY().getNoCol() >= idBefore){
                        plot.getHeaderY().setNoCol(plot.getHeaderY().getNoCol()+ nbColsToInsert);
                    }
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
                toString += (data[i][j] == null ? " " : data[i][j].getValue()+" ("+data[i][j].getNoRow()+", "+data[i][j].getNoCol()+") / ");
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
            ArrayList<Double> list = this.getListOperationResult(getListOperationOnRows().get(col-getNbCol()));
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
            ArrayList<Double> list = this.getListOperationResult(getListOperationOnRows().get(col-getNbCol()));
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
        // on met les lignes blanches à la fin si besoin
        for (int i=0; i<nbRows; i++){
            boolean nullLine = true;
            for (int j=0; j<nbCol;j++ ){
                if(newData[i][j] != null){
                    nullLine = false;
                    break;
                }
            }
            if(nullLine){
                // on cherche la derniere ligne non null derriere
                for (int k=i+1; k<nbRows; k++){
                    boolean nullLineS = true;
                    for (int l=0; l<nbCol; l++){
                        if(newData[k][l] != null){
                            nullLineS = false;
                            break;
                        }
                    }
                    if(!nullLineS){
                        // on intervertit
                        for (int j=0; j<nbCol; j++){
                            newData[i][j] = newData[k][j];
                            if (newData[i][j] != null){
                                newData[i][j].setNoRow(i);
                            }
                            newData[k][j] = null;
                        }
                    }
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
        calculateOperation();
    }

    public Visualization getVisualization(long dbKeyVis){
        int nb = this.listVisualization.size();
        for (int i=0; i<nb; i++){
            if(listVisualization.get(i).getDbKey() == dbKeyVis)
                return listVisualization.get(i);
        }
        return null;
    }

    /* xml logger */
    public Element toXMLLog(){
        Element e = new Element(TAG_DATASET);
        for(int j=0; j<listDataHeader.length; j++){
            if(listDataHeader[j] == null)
                e.addContent(new Element(DataHeader.TAG_HEADER).setText(""));
            else{
                e.addContent(listDataHeader[j].toXMLLog());
            }
        }
        for(int i=0; i<nbRows; i++){
            for(int j=0; j<nbCol; j++){
                if(data[i][j] == null){
                    e.addContent(new Element(Data.TAG_DATA).setText(""));
                }else{
                    e.addContent(data[i][j].toXMLLog());
                }
            }
        }
        if(listOperation != null){
            for(Iterator<DataOperation> o = listOperation.iterator();o.hasNext();){
                e.addContent(o.next().toXMLLog());
            }
        }
        if(listVisualization != null){
            for(Iterator<Visualization> v = listVisualization.iterator();v.hasNext();){
                e.addContent(v.next().toXMLLog());
            }
        }
        return e;
    }
}