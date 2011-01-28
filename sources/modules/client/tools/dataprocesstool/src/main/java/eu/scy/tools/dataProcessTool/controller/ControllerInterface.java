/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.controller;

import eu.scy.tools.dataProcessTool.common.CopyDataset;
import eu.scy.tools.dataProcessTool.common.Data;
import eu.scy.tools.dataProcessTool.common.DataOperation;
import eu.scy.tools.dataProcessTool.common.Dataset;
import eu.scy.tools.dataProcessTool.common.FunctionParam;
import eu.scy.tools.dataProcessTool.common.Mission;
import eu.scy.tools.dataProcessTool.common.ParamGraph;
import eu.scy.tools.dataProcessTool.common.Visualization;
import eu.scy.tools.dataProcessTool.dataTool.DataTableModel;
import eu.scy.tools.dataProcessTool.logger.FitexProperty;
import java.util.ArrayList;
import eu.scy.tools.dataProcessTool.utilities.CopexReturn;
import eu.scy.tools.fitex.analyseFn.Function;
import java.awt.Color;
import java.io.File;
import java.util.List;
import java.util.Vector;
import org.jdom.Element;

/**
 * Interface Controller
 * @author Marjolaine Bodin
 */
public interface ControllerInterface {
    /** load data */
    public CopexReturn load();
    /** load an elo */
    public CopexReturn loadELO(String xmlContent, String dsName);
    /*save an elo*/
    public Element getPDS(Dataset ds);
    /** merge an elo with the current elo */
    public CopexReturn mergeELO(Dataset ds, Element elo);
    /** merge a dataset with the current dataset*/
    public CopexReturn mergeDataset(Dataset currentDs, Mission m, Dataset dsToMerge, ArrayList v);
    /** update the statut of a data: ignored or not */
    public CopexReturn setDataIgnored(Dataset ds, boolean isIgnored, ArrayList<Data> listData, ArrayList v);
    /** creation of a new operation - v[0]= new Dataset and v[1]= new DataOperation */
    public CopexReturn createOperation(Dataset ds, int typeOperation, boolean isOnCol, ArrayList<Integer> listNo, ArrayList v);
    /** update a header */
    public CopexReturn updateDataHeader(Dataset ds, boolean confirm, int colIndex, String title, String unit, String description, String type,String formulaValue,  Function function, boolean scientifcNotation, int nbShownDecimals, int nbSignificantDigits, ArrayList v);
    /** update the title of an operation */
    public CopexReturn updateDataOperation(Dataset ds, DataOperation operation, String title, ArrayList v);
    /** update a data */
    public CopexReturn updateData(Dataset ds, int rowIndex, int colIndex, String value, ArrayList v);
    /** close a visualization*/
    public CopexReturn closeVisualization(Dataset ds, Visualization vis);
    /** delete a visualization*/
    public CopexReturn deleteVisualization(Dataset ds, Visualization vis);
    /* create a visualization - v[0]=new Dataset and  v[1] = new Visualization */
    public CopexReturn createVisualization(Dataset ds, Visualization vis, boolean findAxisParam, ArrayList v) ;
    /* update the graph name */
    public CopexReturn updateVisualizationName(Dataset ds, Visualization vis, String newName);
   /** update the dataset name */
    public CopexReturn renameDataset(Dataset ds, String name);
    /** delete data or operations in a dataset*/
    public CopexReturn deleteData(boolean confirm, Dataset ds, ArrayList<Data> listData, ArrayList<Integer> listNoDataRow, ArrayList<Integer> listNoDataCol, ArrayList<DataOperation> listOperation,  ArrayList v);
    /** add or update a function model */
    public CopexReturn setFunctionModel(Dataset ds, Visualization vis, String description, char type, Color fColor, ArrayList<FunctionParam> listParam, String idPredefFunction, ArrayList v);
    /** insert row or columns*/
    public CopexReturn insertData(Dataset ds,  boolean isOnCol, int nb, int idBefore, ArrayList v) ;
    /** PDF export*/
    public CopexReturn printDataset(Dataset dataset, boolean printDataset, DataTableModel model, ArrayList<Visualization> listVis, ArrayList<Object> listGraph);
    /** get the list of missions and list of dataset per mission that can be opened */
    public CopexReturn getListDatasetToOpenOrMerge(ArrayList v);
    /** update a dataset after a sort*/
    public CopexReturn updateDatasetRow(Dataset ds, Vector exchange, ArrayList v);
    /** create a new dataset */
    public CopexReturn createDataset(String name, String[] headers, String[] units, String[] types, String[] descriptions, ArrayList v);
    /** add a data row */
    public CopexReturn addData(long dbKeyDs, String[] values, ArrayList v);
    /** update parameters of the graph */
    public CopexReturn setParamGraph(long dbKeyDs, long dbKeyVis, ParamGraph pg, ArrayList v);
    /** update the autoscale */
    public CopexReturn setAutoScale(long dbKeyDs, long dbKeyVis, boolean autoScale, ArrayList v);
    /** copy/paste*/
    public CopexReturn paste(long dbKeyDs, CopyDataset copyDs, int[] selCell, ArrayList v);
    /** import a CSV file */
    public CopexReturn importCSVFile(File file,String sepField, String sepText, String charEncoding, ArrayList v);
    /** import a GMBL file */
    public CopexReturn importGMBLFile(File file,ArrayList v);
    /** delete a dataset*/
    public CopexReturn deleteDataset(Dataset ds);
    /** create a dataset by default*/
    public CopexReturn createDefaultDataset(String name, ArrayList v);
    /** close a dataste*/
    public CopexReturn closeDataset(Dataset ds);
    /** update the dataset*/
    public CopexReturn updateDataset(Dataset ds, ArrayList v);
    /** update an operation*/
    public CopexReturn updateOperation(Dataset ds, DataOperation operation , ArrayList v);
    /** open a dataset*/
    public CopexReturn openDataset(Mission mission, Dataset ds);
    /** close the editor */
    public CopexReturn stopFitex();
    /** return true in v[0] if the specified dataset is the dataset from the labdoc */
    public CopexReturn isLabDocDataset(Dataset ds, ArrayList v);
    /** log a user action in the db*/
    public CopexReturn logUserActionInDB(String type, List<FitexProperty> attribute);
}
