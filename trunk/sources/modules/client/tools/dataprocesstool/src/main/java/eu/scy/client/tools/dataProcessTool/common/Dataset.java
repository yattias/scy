/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.common;

import eu.scy.elo.contenttype.dataset.DataSet;
import eu.scy.elo.contenttype.dataset.DataSetColumn;
import eu.scy.elo.contenttype.dataset.DataSetHeader;
import eu.scy.elo.contenttype.dataset.DataSetRow;
import eu.scy.client.tools.dataProcessTool.controller.ScyMath;
import eu.scy.client.tools.dataProcessTool.pdsELO.GraphVisualization;
import java.util.ArrayList;

import eu.scy.client.tools.dataProcessTool.pdsELO.ProcessedData;
import eu.scy.client.tools.dataProcessTool.pdsELO.ProcessedDatasetELO;
import eu.scy.client.tools.dataProcessTool.pdsELO.ProcessedHeader;
import eu.scy.client.tools.dataProcessTool.pdsELO.XYAxis;
import eu.scy.client.tools.dataProcessTool.utilities.DataConstants;
import eu.scy.client.tools.dataProcessTool.utilities.MyUtilities;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * a dataset consists of
 * - an array of data (double ou String).
 * - a list of operations on the columns or rows
 * - a list of visualisations (graph, pie, bar chart...)
 * @author Marjolaine Bodin
 */
public class Dataset implements Cloneable{

    public final static String TAG_DATASET = "dataset";
    private final static String TAG_DATASET_ID = "id_dataset";
    private final static String TAG_DATASET_NAME = "dataset_name";
    private final static String TAG_DATASET_RIGHT = "dataset_right";
    private final static String TAG_DATASET_NBCOL = "dataset_nb_col";
    private final static String TAG_DATASET_NBROW = "dataset_nb_row";
    

    /* db key identifier */
    protected long dbKey;
    /* name */
    protected String name;
    /* column number */
    protected int nbCol;
    /* rows number (without header)*/
    protected int nbRows;
    /* header */
    protected DataHeader[] listDataHeader;
    /* data */
    protected Data[][] data;
    /* operations list */
    protected ArrayList<DataOperation> listOperation;
    /* resulst of operations  */
    protected ArrayList<ArrayList<Double>> listOperationResult;
    /* visualizations list */
    protected ArrayList<Visualization> listVisualization;
    /* is open - in labbook */
    protected boolean isOpen;
    /* labbookk mission */
    private Mission mission;
    private long dbKeyLabDoc;

    private char right;
    
    public Dataset(long dbKey, Mission mission, long dbKeyLabDoc, String name, int nbCol, int nbRows, DataHeader[] listDataHeader, Data[][] data, ArrayList<DataOperation> listOperation,ArrayList<Visualization> listVisualization, char right) {
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
        this.mission = mission;
        this.dbKeyLabDoc = dbKeyLabDoc;
        this.right = right;
        calculateOperation();
    }
    
    public Dataset(long dbKey, String name, char right ) {
        this.dbKey = dbKey;
        this.name = name;
        this.nbCol = 0;
        this.nbRows = 0;
        this.listDataHeader = new DataHeader[0];
        this.data = new Data[0][0];
        this.listOperation = new ArrayList();
        this.listOperationResult = new ArrayList();
        this.listVisualization = new ArrayList() ;
        this.isOpen = false;
        this.mission = null;
        this.right = right;
    }

    public Dataset(Element xmlElem) throws JDOMException {
        if (xmlElem.getName().equals(TAG_DATASET)) {
            dbKey = -1;
            try{
                dbKey = Long.parseLong(xmlElem.getChild(TAG_DATASET_ID).getText());
            }catch(NumberFormatException ex){
            }
            name = xmlElem.getChild(TAG_DATASET_NAME).getText();
            try{
                nbCol = Integer.parseInt(xmlElem.getChild(TAG_DATASET_NBCOL).getText());
                nbRows = Integer.parseInt(xmlElem.getChild(TAG_DATASET_NBROW).getText());
            }catch(NumberFormatException e){
                throw(new JDOMException("Dataset expects nbCol and nbRow as integer"));
            }
            this.listDataHeader = new DataHeader[nbCol];
            int i=0;
            for (Iterator<Element> variableElem = xmlElem.getChildren(DataHeader.TAG_HEADER).iterator(); variableElem.hasNext();) {
                listDataHeader[i++] = new DataHeader(variableElem.next());
            }
            this.data = new Data[nbRows][nbCol];
            for (Iterator<Element> variableElem = xmlElem.getChildren(Data.TAG_DATA).iterator(); variableElem.hasNext();) {
                Data d= new Data(variableElem.next());
                data[d.getNoRow()][d.getNoCol()] = d;
            }
            this.listOperation = new ArrayList();
            i=0;
            for (Iterator<Element> variableElem = xmlElem.getChildren(DataOperation.TAG_OPERATION).iterator(); variableElem.hasNext();) {
                listOperation.add(new DataOperation(variableElem.next()));
            }
            this.listOperationResult = new ArrayList();
            this.listVisualization = new ArrayList() ;
            i=0;
            for (Iterator<Element> variableElem = xmlElem.getChildren(Visualization.TAG_VISUALIZATION).iterator(); variableElem.hasNext();) {
                listVisualization.add(new Visualization(variableElem.next()));
            }
            this.isOpen = false;
            this.mission = null;
            this.right = xmlElem.getChild(TAG_DATASET_RIGHT).getText().charAt(0);
            calculateOperation();
        }else {
            throw(new JDOMException("Dataset expects <"+TAG_DATASET+"> as root element, but found <"+xmlElem.getName()+">."));
	}
    }
   
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

    public char getRight() {
        return right;
    }

    public void setRight(char right) {
        this.right = right;
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

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public long getDbKeyLabDoc() {
        return dbKeyLabDoc;
    }

    public void setDbKeyLabDoc(long dbKeyLabDoc) {
        this.dbKeyLabDoc = dbKeyLabDoc;
    }

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
            Mission mClone = null;
            if(mission != null){
                mClone = (Mission)mission.clone();
            }
            dataset.setDbKeyLabDoc(dbKeyLabDoc);
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
            dataset.setMission(mClone);
            dataset.setRight(new Character(right));
            
            return dataset;
        } catch (CloneNotSupportedException e) { 
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
	}
    }

    /* add an operation in the lsit */
    public void addOperation(DataOperation operation){
        this.listOperation.add(operation);
        operate(operation, this.listOperation.size()-1);
    }

    /* returns the header corresponding to the specified index - can be null */
    public DataHeader getDataHeader(int colIndex){
        return this.listDataHeader[colIndex];
    }

    /* update the header to the specified columnIndex */
    public void setDataHeader(DataHeader header, int columnIndex){
        this.listDataHeader[columnIndex] = header;
    }

    /*returns the data corresponding to the specified row index and column index, can be null  */
    public Data getData(int rowIndex, int colIndex){
        return this.data[rowIndex][colIndex];
    }

    /* update the data with the specified row index and column index */
    public void setData(Data data, int rowIndex, int columnIndex){
        this.data[rowIndex][columnIndex] = data;
    }

    /* returns the index in the list of the visulization, give by its dbkey */
    public int getIdVisualization(long dbKey){
        int nb = listVisualization.size();
        for (int i=0; i<nb; i++){
            if (listVisualization.get(i).getDbKey() == dbKey)
                return i;
        }
        return -1;
    }

    /* removes the specified visulization */
    public void removeVisualization(Visualization vis ){
        int id = getIdVisualization(vis.getDbKey());
        if (id == -1)
            return;
        listVisualization.remove(id);
    }

    /* add a visualization in the list */
    public void addVisualization(Visualization vis){
        this.listVisualization.add(vis);
    }

    /* return a processed dataset ELO -> xml elo */
    public ProcessedDatasetELO toELO(Locale locale){
        int nbR = getNbRows() ;
        int nbC = getNbCol() ;
        // creation of the ELO
        // DATASET
        // headers
        List<DataSetHeader> headers = new LinkedList<DataSetHeader>() ;
        List<DataSetColumn> variables = new LinkedList<DataSetColumn>();
        for (int j=0; j<nbC; j++){
            DataSetColumn dsCol = new DataSetColumn(getDataHeader(j) == null ?"" : getDataHeader(j).getValue(), getDataHeader(j) == null ?"" : getDataHeader(j).getDescription(), getDataHeader(j) == null ?"" : getDataHeader(j).getType(), getDataHeader(j) == null ?"" : getDataHeader(j).getUnit()) ;
            variables.add(dsCol);
        }
        DataSetHeader header = new DataSetHeader(variables, locale);
        headers.add(header);
        // rows
        List<eu.scy.client.tools.dataProcessTool.pdsELO.Data> listIgnoredData = new LinkedList<eu.scy.client.tools.dataProcessTool.pdsELO.Data>();
        List<ProcessedHeader> listProcessedHeader = new LinkedList<ProcessedHeader>();
        // => dataset
        DataSet ds = new DataSet(headers);
        for(int j=0; j<nbC; j++){
            if(getDataHeader(j) != null ){
                listProcessedHeader.add(new ProcessedHeader(""+j, getDataHeader(j).getFormulaValue(), getDataHeader(j).isScientificNotation(), getDataHeader(j).getNbShownDecimals(), getDataHeader(j).getNbSignificantDigits(), getDataHeader(j).getDataAlignment()));
            }
        }
        for (int i=0; i<nbR; i++){
            List<String> values = new LinkedList<String>();
            for (int j=0; j<nbC; j++){
                values.add(new String(getData(i, j) == null ?"":getData(i, j).getValue()));
                if (getData(i, j) != null && getData(i, j).isIgnoredData())
                    listIgnoredData.add(new eu.scy.client.tools.dataProcessTool.pdsELO.Data(Integer.toString(i), Integer.toString(j)));
            }
            DataSetRow row = new DataSetRow(values);
            ds.addRow(row);
        }

        // PROCESSED DATASET
        eu.scy.client.tools.dataProcessTool.pdsELO.IgnoredData iData =new eu.scy.client.tools.dataProcessTool.pdsELO.IgnoredData(listIgnoredData);
        List<eu.scy.client.tools.dataProcessTool.pdsELO.Operation> listOperations = new LinkedList<eu.scy.client.tools.dataProcessTool.pdsELO.Operation>();
       int nbop = getListOperation().size();
        ArrayList<ArrayList<Double>> listResult = getListOperationResult() ;
        for (int i=0; i<nbop; i++){
            DataOperation myOp = getListOperation().get(i);
            List<String> references = new LinkedList<String>();
            List<String> results = new LinkedList<String>();
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
            eu.scy.client.tools.dataProcessTool.pdsELO.Operation operation  = new eu.scy.client.tools.dataProcessTool.pdsELO.Operation(myOp.isOnCol(), myOp.getName(), myOp.getName(), "double", myOp.getTypeOperation().getCodeName(), references, results);
            listOperations.add(operation);
        }
        List<eu.scy.client.tools.dataProcessTool.pdsELO.Visualization> listVisualizations = new LinkedList<eu.scy.client.tools.dataProcessTool.pdsELO.Visualization>();
        int nbVis = getListVisualization().size();
        for (int i=0; i<nbVis; i++){
            Visualization myVis = getListVisualization().get(i);
            TypeVisualization type = myVis.getType();
            eu.scy.client.tools.dataProcessTool.pdsELO.Visualization vis = null;
            if (type.getCode() == DataConstants.VIS_GRAPH){
                Graph g = (Graph)myVis ;
                ParamGraph pg = g.getParamGraph() ;
                List<eu.scy.client.tools.dataProcessTool.pdsELO.FunctionModel> listFunction = getListFunctionModel(g.getListFunctionModel());
                List<XYAxis> axis = getAxis(pg);
                vis = new GraphVisualization(DataConstants.TYPE_VIS_GRAPH, myVis.getName(), axis, pg.getX_min(), pg.getX_max(), pg.getDeltaX(), pg.getY_min(), pg.getY_max(), pg.getDeltaY(),
                        pg.isDeltaFixedAutoscale(), listFunction);
            }else if (type.getCode() == DataConstants.VIS_PIE){
                int idHeaderLabel = -1;
                if(((SimpleVisualization)myVis).getHeaderLabel() != null)
                        idHeaderLabel = ((SimpleVisualization)myVis).getHeaderLabel().getNoCol();
                vis = new eu.scy.client.tools.dataProcessTool.pdsELO.PieVisualization(DataConstants.TYPE_VIS_PIE, myVis.getName(),  ((SimpleVisualization)myVis).getNoCol(), idHeaderLabel);
            }else if (type.getCode() == DataConstants.VIS_BAR){
                int idHeaderLabel = -1;
                if(((SimpleVisualization)myVis).getHeaderLabel() != null)
                        idHeaderLabel = ((SimpleVisualization)myVis).getHeaderLabel().getNoCol();
                vis = new eu.scy.client.tools.dataProcessTool.pdsELO.BarVisualization(DataConstants.TYPE_VIS_BAR, myVis.getName(),  ((SimpleVisualization)myVis).getNoCol(), idHeaderLabel);
            }
            if(vis != null)
                listVisualizations.add(vis);
        }
        ProcessedData pds = new ProcessedData(this.name, iData, listProcessedHeader,listOperations, listVisualizations);
        ProcessedDatasetELO pdsELO = new ProcessedDatasetELO(ds, pds);
        return pdsELO ;
    }

    private List<XYAxis> getAxis(ParamGraph pg){
        List<XYAxis> axis = new LinkedList();
        for (Iterator<PlotXY> p = pg.getPlots().iterator();p.hasNext();){
            PlotXY plot = p.next();
            XYAxis a = new XYAxis(plot.getHeaderX().getNoCol(), plot.getHeaderY().getNoCol(), plot.getHeaderX().getValue(), plot.getHeaderY().getValue(), plot.getPlotColor().getRed(), plot.getPlotColor().getGreen(), plot.getPlotColor().getBlue());
            axis.add(a);
        }
        return axis;
    }

    private List<eu.scy.client.tools.dataProcessTool.pdsELO.FunctionModel> getListFunctionModel(ArrayList<FunctionModel> listFm){
        List<eu.scy.client.tools.dataProcessTool.pdsELO.FunctionModel> list = null;
        if (listFm != null){
            int nb = listFm.size();
            list = new LinkedList();
            for (int i=0; i<nb; i++){
                FunctionModel fm = listFm.get(i);
                List<eu.scy.client.tools.dataProcessTool.pdsELO.FunctionParam> listParam = getListFunctionParam(listFm.get(i));
                eu.scy.client.tools.dataProcessTool.pdsELO.FunctionModel f = new eu.scy.client.tools.dataProcessTool.pdsELO.FunctionModel(fm.getDescription(), fm.getType(), fm.getColor().getRed(), fm.getColor().getGreen(), fm.getColor().getBlue(), listParam, fm.getIdPredefFunction());
                list.add(f);
            }
        }
        return list;
    }

    private List<eu.scy.client.tools.dataProcessTool.pdsELO.FunctionParam> getListFunctionParam(FunctionModel functionModel){
        List<eu.scy.client.tools.dataProcessTool.pdsELO.FunctionParam> listParam = new LinkedList();
        int nb = functionModel.getListParam().size();
        for (int i=0; i<nb; i++){
            eu.scy.client.tools.dataProcessTool.pdsELO.FunctionParam p = new eu.scy.client.tools.dataProcessTool.pdsELO.FunctionParam(functionModel.getListParam().get(i).getParam(), functionModel.getListParam().get(i).getValue());
            listParam.add(p);
        }
        return listParam;
    }


    /* returns the index in the list of the operation, defined by its dbkey */
    public int getIdOperation(long dbKey){
        int nb = listOperation.size();
        for (int i=0; i<nb; i++){
            if (listOperation.get(i).getDbKey() == dbKey)
                return i;
        }
        return -1;
    }

    /* removes the specified operation */
    public void removeOperation(DataOperation operation){
        int id = getIdOperation(operation.getDbKey());
        if (id == -1)
            return;
        listOperation.remove(id);
        listOperationResult.remove(id);
    }

    /* removes the header , defined by its index column, doesn't remove the data of the column */
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

    /* removes a row/column index for the specified operation */
    public void removeNoOperation(long dbKeyOp, int no){
        int id = getIdOperation(dbKeyOp);
        if (id ==-1)
            return;
        listOperation.get(id).removeNo(no);
    }
    
    /* remove the specified data*/
    public void removeData(ArrayList<Data> listData){
        for(Iterator<Data> d = listData.iterator();d.hasNext();){
            Data adata = d.next();
            setData(null,adata.getNoRow(), adata.getNoCol());
        }
    }

    /* remove the rows, defined by their index */
    public ArrayList[] removeRows(ArrayList<Integer> listRow){
        ArrayList[] tabDel = new ArrayList[4];
        ArrayList<DataOperation> listOpToDel = new ArrayList();
        ArrayList<Visualization> listVisToDel = new ArrayList();
        ArrayList<DataOperation> listOpToUpdate = new ArrayList();
        ArrayList<Visualization> listVisToUpdate = new ArrayList();
        int nbRowsSel = listRow.size();
        ArrayList<Integer> list = MyUtilities.getSortList(listRow);
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

    /* remove the columns, defined by their index */
    public ArrayList[] removeCols(ArrayList<Integer> listCol){
        ArrayList[] tabDel = new ArrayList[5];
        ArrayList<DataOperation> listOpToDel = new ArrayList();
        ArrayList<Visualization> listVisToDel = new ArrayList();
        ArrayList<DataOperation> listOpToUpdate = new ArrayList();
        ArrayList<Visualization> listVisToUpdate = new ArrayList();
        ArrayList<Long> listPlotsToRemove = new ArrayList();
        int nbColsSel = listCol.size();
        ArrayList<Integer> list = MyUtilities.getSortList(listCol);
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
            for(Iterator<Long> l = listV[4].iterator();l.hasNext();){
                listPlotsToRemove.add(l.next());
            }
        }
        tabDel[0] = listOpToUpdate;
        tabDel[1] = listOpToDel;
        tabDel[2] = listVisToUpdate;
        tabDel[3] = listVisToDel;
        tabDel[4] = listPlotsToRemove;
        return tabDel;
    }

    /* delete a specified column */
    private ArrayList[] deleteCol(int no){
        ArrayList[] tabDel = new ArrayList[5];
        ArrayList<DataOperation> listOperationToUpdate = new ArrayList();
        ArrayList<DataOperation> listOperationToDel = new ArrayList();
        ArrayList<Visualization> listVisualizationToUpdate = new ArrayList();
        ArrayList<Visualization> listVisualizationToDel = new ArrayList();
        ArrayList<Long> listPlotsToremove = new ArrayList();
        // udpate list operation
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
                    listOperationToUpdate.add(listOperation.get(i));
                if (remove && i<listOperationResult.size()){
                    listOperationResult.get(i).remove(idNo);
                }

            }
        }
        // remove operation, for which the listNo is empty
        for (int i=nbOp-1; i>=0; i--){
            if (listOperation.get(i).isOnCol()){
                ArrayList<Integer> listNo = listOperation.get(i).getListNo() ;
                int size = listNo.size();
                if(size == 0){
                    listOperationToDel.add(listOperation.get(i));
                    removeOperation(listOperation.get(i));
                }
            }
        }
        // clone visualizations
        ArrayList<Visualization> listVisC = new ArrayList();
        for(Iterator<Visualization> o = listVisualization.iterator();o.hasNext();){
            listVisC.add((Visualization)o.next().clone());
        }
        // update list visualization
        int nbVis = listVisualization.size();
        for (int i=nbVis-1; i>=0; i--){
            Visualization v = listVisualization.get(i);
            if (v.isOnNo(no)){
                if(v instanceof SimpleVisualization){
                    listVisualization.remove(i);
                    listVisualizationToDel.add(listVisC.get(i));
                }else if(v instanceof Graph){
                    ArrayList<Long> list = ((Graph)v).removePlotWithNo(no);
                    boolean remove = list.size()>0;
                    int nb = ((Graph)v).getNbPlots();
                    if(nb==0){
                        listVisualizationToDel.add(listVisC.get(i));
                        listVisualization.remove(i);
                    }else if(remove){
                        listVisualizationToUpdate.add(listVisC.get(i));
                        for(Iterator<Long> l = list.iterator();l.hasNext();){
                            listPlotsToremove.add(l.next());
                        }
                    }
                }
            }
        }
        for(int i=0; i<listVisualization.size(); i++){
            Visualization v = listVisualization.get(i);
            if(v instanceof SimpleVisualization){
                int n = ((SimpleVisualization)v).getNoCol();
                if ( n > no){
                    ((SimpleVisualization)v).getHeader().setNoCol(n-1);
                }
                if(((SimpleVisualization)v).getHeaderLabel() != null){
                    n = ((SimpleVisualization)v).getHeaderLabel().getNoCol();
                    ((SimpleVisualization)v).getHeaderLabel().setNoCol(n-1);
                }
            }else if (v instanceof Graph){
                //((Graph)v).updateNoCol(no, -1);
            }
        }
        // update header
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
        
        // update nb Col
        tabDel[0] = listOperationToUpdate;
        tabDel[1] = listOperationToDel;
        tabDel[2] = listVisualizationToUpdate;
        tabDel[3] = listVisualizationToDel;
        tabDel[4] = listPlotsToremove;
        this.nbCol = this.nbCol-1;
        return tabDel;
    }

    /* remove the specified row  */
    private ArrayList[] deleteRow(int no){
        ArrayList<DataOperation> listOperationToUpdate = new ArrayList();
        ArrayList<DataOperation> listOperationToDel = new ArrayList();
        ArrayList<Visualization> listVisualizationToUpdate = new ArrayList();
        ArrayList<Visualization> listVisualizationToDel = new ArrayList();
        // update list operation
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
                    listOperationToUpdate.add(listOperation.get(i));
                if (remove){
                    listOperationResult.get(i).remove(idNo);
                }

            }
        }
        // remove operations without listNo to apply
        for (int i=nbOp-1; i>=0; i--){
            if (!listOperation.get(i).isOnCol()){
                ArrayList<Integer> listNo = listOperation.get(i).getListNo() ;
                int size = listNo.size();
                if(size == 0){
                    //listOperationToDel.add(listOpC.get(i));
                    listOperationToDel.add(listOperation.get(i));
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
        
        // update nb Row
        this.nbRows = this.nbRows-1;
        ArrayList[] tab = new ArrayList[4];
        tab[0] = listOperationToUpdate;
        tab[1] = listOperationToDel;
        tab[2] = listVisualizationToUpdate;
        tab[3] = listVisualizationToDel;
        return tab;
    }

   

    /* insert rows :
     * @param nbRowsToInsert : number of rows to insert
     * @param idBefore: index of insertion, if 0 at the start, if idBefore == nbRows, at the end
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
       // header : no update
       // list operation : update the no
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
       // update nb Row
        this.nbRows = this.nbRows+nbRowsToInsert;
    }

    /* insert columns :
     * @param nbColsToInsert : number of cols to insert
     * @param idBefore: index of insertion, if 0 at the start, if idBefore == nbCols, at the end
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
       // list operation : updates no
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
       // list visualization : update no & plots
       int nbVis = listVisualization.size();
        for (int i=0; i<nbVis; i++){
            Visualization v = listVisualization.get(i);
            if(v instanceof SimpleVisualization){
                int n= ((SimpleVisualization)v).getNoCol();
                if(n >= idBefore){
                    ((SimpleVisualization)v).getHeader().setNoCol(n+nbColsToInsert);
                }
                if(((SimpleVisualization)v).getHeaderLabel() != null ){
                    n = ((SimpleVisualization)v).getHeaderLabel().getNoCol();
                    if(n >= idBefore){
                        ((SimpleVisualization)v).getHeaderLabel().setNoCol(n+nbColsToInsert);
                    }
                }
            }else if (v instanceof Graph){
//                ArrayList<PlotXY> plots = ((Graph)v).getParamGraph().getPlots();
//                for(Iterator<PlotXY> p = plots.iterator();p.hasNext();){
//                    PlotXY plot = p.next();
//                    if(plot.getHeaderX().getNoCol() >= idBefore){
//                        plot.getHeaderX().setNoCol(plot.getHeaderX().getNoCol()+ nbColsToInsert);
//                    }
//                    if(plot.getHeaderY().getNoCol() >= idBefore){
//                        plot.getHeaderY().setNoCol(plot.getHeaderY().getNoCol()+ nbColsToInsert);
//                    }
//                }
            }
        }
       // update nb Col
        this.nbCol = this.nbCol+nbColsToInsert;
    }

    /* returns the list of the operations on the rows  */
    public ArrayList<DataOperation> getListOperationOnRows(){
        ArrayList<DataOperation> list = new ArrayList();
        int nb = this.listOperation.size();
        for (int i=0; i<nb; i++){
            if (!listOperation.get(i).isOnCol())
                list.add(listOperation.get(i));
        }
        return list;
    }

    /* returns the list of operations on the columns  */
    public ArrayList<DataOperation> getListOperationOnCols(){
        ArrayList<DataOperation> list = new ArrayList();
        int nb = this.listOperation.size();
        for (int i=0; i<nb; i++){
            if (listOperation.get(i).isOnCol())
                list.add(listOperation.get(i));
        }
        return list;
    }

    /* calculating  all operations */
    public void calculateOperation(){
        int nbOp = this.listOperation.size();
        for (int i=0; i<nbOp; i++){
            operate(this.listOperation.get(i), i);
        }
    }

    /* calculating the spexified operation, id is the index in the list of operation */
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
            double r = ScyMath.calculate(type, listValue);
            listResult.add(r);
        }
        if (this.listOperationResult.size() > id)
            this.listOperationResult.set(id, listResult) ;
        else
            this.listOperationResult.add(listResult);
    }

    /* returns the list of values that are keep in count for calculating (in the specified colunm)*/
    public ArrayList<Double> getListValueCol(int idCol){
        ArrayList<Double> listValue = new ArrayList();
        if(getDataHeader(idCol).isDouble()){
            for (int i=0; i<nbRows; i++){
                Data d = this.data[i][idCol];
                if (d != null && !d.isIgnoredData() && ! Double.isNaN(d.getDoubleValue()) ){
                    listValue.add(d.getDoubleValue());
                }
            }
        }
        return listValue;
    }

    /* returns the list of values that are keep in count for calculating (in the specified row) */
    public ArrayList<Double> getListValueRow(int idRow){
        ArrayList<Double> listValue = new ArrayList();
        for (int j=0; j<nbCol ; j++){
            if(getDataHeader(j).isDouble()){
                Data d = this.data[idRow][j];
                if (d != null && !d.isIgnoredData()&& !Double.isNaN(d.getDoubleValue()) ){
                    listValue.add(d.getDoubleValue());
                }
            }
        }
        return listValue;
    }

    /* returns the result of an operation */
    public ArrayList<Double> getListOperationResult(DataOperation operation){
        int id = getIdOperation(operation.getDbKey());
        if (id == -1)
            return null;
        return this.listOperationResult.get(id);
    }

    /* to string for console (debug) */
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
     * returns the lenght max of the field of the specified column (given by its index)
     * -1 if error
    */
    public int getValueMaxSizeIn(int col) {
        int maxlenght = 0;
        if(col < getNbCol()){
            for (int i=0; i<nbRows; i++){
                Data d = this.data[i][col];
                int l = 0;
                if (d != null){
                    l = d.getValue().length();
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

    /* returns the max. lenght before the comma & the max. lenght after the comma */
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
                    String s = d.getValue();
                    if (s.startsWith("-"))
                        s = s.substring(1);
                    l1 = s.indexOf(".");
                    if(l1 == -1)
                        l1 = s.length();
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

    /* exchange rows */
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
        // the blank rows at the end if needed
        for (int i=0; i<nbRows; i++){
            boolean nullLine = true;
            for (int j=0; j<nbCol;j++ ){
                if(newData[i][j] != null){
                    nullLine = false;
                    break;
                }
            }
            if(nullLine){
                // searching for the last row not blank
                for (int k=i+1; k<nbRows; k++){
                    boolean nullLineS = true;
                    for (int l=0; l<nbCol; l++){
                        if(newData[k][l] != null){
                            nullLineS = false;
                            break;
                        }
                    }
                    if(!nullLineS){
                        // exchange
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

    public ArrayList<DataOperation> getOperationOnCol(int noCol){
        ArrayList<DataOperation> list = new ArrayList();
        for(Iterator<DataOperation> o = listOperation.iterator(); o.hasNext();){
            DataOperation op = o.next();
            if(op.isOnCol(noCol)){
                list.add(op);
            }
        }
        return list;
    }
    public ArrayList<Visualization> getVisualizationOnCol(int noCol){
        ArrayList<Visualization> list = new ArrayList();
        for(Iterator<Visualization> v = listVisualization.iterator(); v.hasNext();){
            Visualization vis = v.next();
            if(vis.isOnNo(noCol)){
                list.add(vis);
            }
        }
        return list;
    }

    /* removes the operations & visualizations on the specified column */
    public ArrayList[] removeOperationAndVisualizationOn(int colIndex){
        ArrayList[] tabDel = new ArrayList[5];
        ArrayList<DataOperation> listOperationToUpdate = new ArrayList();
        ArrayList<DataOperation> listOperationToDel = new ArrayList();
        ArrayList<Visualization> listVisualizationToUpdate = new ArrayList();
        ArrayList<Visualization> listVisualizationToDel = new ArrayList();
        ArrayList<Long> listPlotsToRemove = new ArrayList();
        // update list operation
        // clone operations
        ArrayList<DataOperation> listOpC = new ArrayList();
        for(Iterator<DataOperation> o = listOperation.iterator();o.hasNext();){
            listOpC.add((DataOperation)o.next().clone());
        }
        int nbOp = listOperation.size();
        for (int i=0; i<nbOp; i++){
            if (listOperation.get(i).isOnCol()){
                ArrayList<Integer> listNo = listOperation.get(i).getListNo() ;
                int idNo = listNo.indexOf(new Integer(colIndex));
                boolean remove = listNo.remove(new Integer(colIndex));
                int size = listNo.size();
                if(size > 0 && remove)
                    listOperationToUpdate.add(listOpC.get(i));
                if (remove && i<listOperationResult.size()){
                    listOperationResult.get(i).remove(idNo);
                }

            }
        }
        // remove operations without listNo
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
        calculateOperation();
        // clone  visualizations
        ArrayList<Visualization> listVisC = new ArrayList();
        for(Iterator<Visualization> o = listVisualization.iterator();o.hasNext();){
            listVisC.add((Visualization)o.next().clone());
        }
        // update list visualization
        int nbVis = listVisualization.size();
        for (int i=nbVis-1; i>=0; i--){
            Visualization v = listVisualization.get(i);
            if (v.isOnNo(colIndex)){
                if(v instanceof SimpleVisualization){
                    listVisualization.remove(i);
                    listVisualizationToDel.add(listVisC.get(i));
                }else if(v instanceof Graph){
                    ArrayList<Long> idPlotToRemove = ((Graph)v).removePlotWithNo(colIndex);
                    int nb = ((Graph)v).getNbPlots();
                    boolean remove  = idPlotToRemove.size() > 0;
                    if(nb==0){
                        listVisualizationToDel.add(listVisC.get(i));
                        listVisualization.remove(i);
                    }else if(remove){
                        listVisualizationToUpdate.add(listVisC.get(i));
                        for(Iterator<Long> l = idPlotToRemove.iterator();l.hasNext();){
                            listPlotsToRemove.add(l.next());
                        }
                    }
                }
            }
        }
        for(int i=0; i<listVisualization.size(); i++){
            Visualization v = listVisualization.get(i);
            if(v instanceof SimpleVisualization){
                int n = ((SimpleVisualization)v).getNoCol();
                if ( n > colIndex){
                    ((SimpleVisualization)v).getHeader().setNoCol(n-1);
                }
            }else if (v instanceof Graph){
                //((Graph)v).updateNoCol(no, -1);
            }
        }
        tabDel[0] = listOperationToUpdate;
        tabDel[1] = listOperationToDel;
        tabDel[2] = listVisualizationToUpdate;
        tabDel[3] = listVisualizationToDel;
        tabDel[4] = listPlotsToRemove;
        return tabDel;
    }


    /* xml logger */
    public Element toXMLLog(){
        Element e = new Element(TAG_DATASET);
        e.addContent(new Element(TAG_DATASET_ID).setText(Long.toString(dbKey)));
        e.addContent(new Element(TAG_DATASET_NAME).setText(this.name));
        e.addContent(new Element(TAG_DATASET_NBROW).setText(Integer.toString(nbRows)));
        e.addContent(new Element(TAG_DATASET_NBCOL).setText(Integer.toString(nbCol)));
        e.addContent(new Element(TAG_DATASET_RIGHT).setText(Character.toString(right)));
        for(int j=0; j<listDataHeader.length; j++){
            if(listDataHeader[j] != null){
                e.addContent(listDataHeader[j].toXMLLog());
            }
        }
        for(int i=0; i<nbRows; i++){
            for(int j=0; j<nbCol; j++){
                if(data[i][j] != null){
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

    public boolean hasAtLeastADoubleColumn(){
        for(int j=0; j<nbCol; j++){
            if(getDataHeader(j).isDouble())
                return true;
        }
        return false;
    }

    public DataHeader[] getListDataHeaderDouble(boolean isDouble) {
       ArrayList<DataHeader> list = new ArrayList();
       int nb = 0;
       for(int j=0; j<nbCol; j++){
           if((getDataHeader(j)!= null && (isDouble && getDataHeader(j).isDouble()) || (!isDouble && !getDataHeader(j).isDouble()))){
               list.add(getDataHeader(j));
               nb++;
           }
       }
       DataHeader[] tab = new DataHeader[nb];
       for( int i=0; i<nb; i++){
           tab[i] = list.get(i);
       }
       return tab;
    }

    public Data[] getRow(int i){
        return data[i];
    }

    public DataHeader getDataHeader(String name){
        for(int j=0; j<listDataHeader.length; j++){
            if(listDataHeader[j] != null && listDataHeader[j].getValue().equals(name))
                return listDataHeader[j];
        }
        return null;
    }

    public boolean isScientificNotation(int columnIndex){
        if(listDataHeader[columnIndex] != null )
            return listDataHeader[columnIndex].isScientificNotation();
        else
            return false;
    }

    public int getNbShownDecimals(int columnIndex){
        if(listDataHeader[columnIndex] != null )
            return listDataHeader[columnIndex].getNbShownDecimals();
        else
            return DataConstants.NB_DECIMAL_UNDEFINED;
    }

    public int getNbSignificantDigits(int columnIndex){
        if(listDataHeader[columnIndex] != null )
            return listDataHeader[columnIndex].getNbSignificantDigits();
        else
            return DataConstants.NB_SIGNIFICANT_DIGITS_UNDEFINED;
    }
    
    public List<String> getWords(int noCol){
        List<String> words = new LinkedList();
        for(int j=0; j<nbCol; j++){
            if(j != noCol && listDataHeader[j] != null && listDataHeader[j].isDouble())
                words.add(listDataHeader[j].getValue());
        }
        Collections.sort(words);
        return words;
    }

    /* return true if all columns are of type double */
    public boolean isAllColumnDouble(){
        for(int j=0; j<listDataHeader.length; j++){
            if(!listDataHeader[j].isDouble())
                return false;
        }
        return true;
    }

    public boolean isFitTypeColumn(Dataset ds){
        if(ds != null && ds.getNbCol() == getNbCol()){
            for(int j=0; j<nbCol; j++){
                if((getDataHeader(j) != null && ds.getDataHeader(j) != null && !getDataHeader(j).getType().equals(ds.getDataHeader(j).getType())) ||
                        (getDataHeader(j) != null && ds.getDataHeader(j) == null && !getDataHeader(j).isDouble()) ||
                        (getDataHeader(j) == null && ds.getDataHeader(j) != null && !ds.getDataHeader(j).isDouble()) ){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /* return the nb rows that contains some data */
    public int getNbRowsData(){
        int nbR = 0;
        for(int i=0; i<nbRows; i++){
            if(isDataInRow(i)){
                nbR++;
            }
        }
        return nbR;
    }

    /* return the nb rows that contains some data */
    public int getNbMaxRowsData(){
        for(int i=nbRows-1; i>=0; i--){
            if(isDataInRow(i)){
                return i+1;
            }
        }
        return 0;
    }

    public int getFirstRowData(){
        for(int i=0; i<nbRows; i++){
            if(isDataInRow(i)){
                return i;
            }
        }
        return -1;
    }
    private boolean isDataInRow(int row){
        for(int j=0; j<nbCol; j++){
            if(getData(row, j) != null && getData(row, j).getValue() != null && !getData(row, j).getValue().equals(""))
                return true;
        }
        return false;
    }

    public int getDataAlignment(int idCol){
        if(getDataHeader(idCol) == null){
            return DataConstants.DEFAULT_DATASET_ALIGNMENT;
        }else{
            return getDataHeader(idCol).getDataAlignment();
        }
    }

}
