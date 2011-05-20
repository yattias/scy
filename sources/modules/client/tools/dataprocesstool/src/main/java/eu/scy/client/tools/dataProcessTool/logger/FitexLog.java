/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.logger;

import eu.scy.elo.contenttype.dataset.DataSetRow;
import eu.scy.client.tools.dataProcessTool.common.CopyDataset;
import eu.scy.client.tools.dataProcessTool.common.Data;
import eu.scy.client.tools.dataProcessTool.common.DataHeader;
import eu.scy.client.tools.dataProcessTool.common.DataOperation;
import eu.scy.client.tools.dataProcessTool.common.Dataset;
import eu.scy.client.tools.dataProcessTool.common.FunctionParam;
import eu.scy.client.tools.dataProcessTool.common.Graph;
import eu.scy.client.tools.dataProcessTool.common.Visualization;
import eu.scy.client.tools.dataProcessTool.undoRedo.DataUndoRedo;
import eu.scy.client.tools.dataProcessTool.utilities.DataConstants;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.jdom.Element;
import roolo.elo.JDomStringConversion;

/**
 *
 * @author Marjolaine
 */
public class FitexLog {
    public final static String toolName = "fitex";

    private final static String TAG_DATASET = "dataset";
    private final static String TAG_DATASET_NAME = "dataset_name";
    private final static String TAG_DATA = "data";
    private final static String TAG_HEADER= "header";
    private final static String TAG_ROW_VALUE= "row_value";
    private final static String TAG_OLD = "old";
    private final static String TAG_NEW = "new";
    private final static String TAG_FILE_NAME = "file_name";
    private final static String TAG_NB_COL = "nb_col";
    private final static String TAG_NB_ROWS = "nb_rows";
    private final static String TAG_ID_BEFORE = "id_before";
    private final static String TAG_ID = "id";
    private final static String TAG_ID_ROW = "id_row";
    private final static String TAG_ID_COL = "id_col";
    private final static String TAG_OPERATION = "operation";
    private final static String TAG_VISUALIZATION = "visualization";
    private final static String TAG_GRAPH_MODE = "graph_mode";
    private final static String TAG_SORT_TITLE = "sort_title";
    private final static String TAG_SORT_ORDER = "sort_order";
    private final static String TAG_CELL = "cell";
    private final static String TAG_ROW = "row";
    private final static String TAG_COL = "col";
    private final static String TAG_FUNCTION = "function";
    private final static String TAG_FUNCTION_PARAM = "function_param";
    private final static String TAG_COLOR = "color";
    private final static String TAG_COLOR_RED = "red";
    private final static String TAG_COLOR_GREEN = "green";
    private final static String TAG_COLOR_BLUE = "blue";
    private final static String TAG_SUBDATA = "subData";
    private final static String TAG_UNDO_REDO_ACTION = "undo_redo_action";
    public final static String TAG_DATASET_MODEL = "model";

    
    private static String getModel(Dataset ds, Locale locale){
        return new JDomStringConversion().xmlToString(ds.toELO(locale).toXML());
    }


    /* log: edit data*/
    public static List<FitexProperty> logEditData(Dataset ds, Locale locale, Data oldData, Data newData){
        List<FitexProperty> list = new LinkedList();
        list.add(new FitexProperty(TAG_DATASET_NAME, ds.getName(), null));
        if(oldData != null){
            list.add(new FitexProperty(TAG_OLD, oldData.getValue(), oldData.toXMLLog()));
        }
        if(newData == null){
            list.add(new FitexProperty(TAG_NEW, "", null));
        }else{
            list.add(new FitexProperty(TAG_NEW, newData.getValue(), newData.toXMLLog()));
        }
        list.add(new FitexProperty(TAG_DATASET_MODEL, getModel(ds, locale), null));
        return list;
    }

    /* log: edit header*/
    public static List<FitexProperty> logEditHeader(Dataset ds, Locale locale,DataHeader oldHeader, DataHeader newHeader){
        List<FitexProperty> list = new LinkedList();
        list.add(new FitexProperty(TAG_DATASET_NAME, ds.getName(), null));
        if(oldHeader != null){
            list.add(new FitexProperty(TAG_OLD, oldHeader.getValue(), oldHeader.toXMLLog()));
        }
        if(newHeader == null){
            list.add(new FitexProperty(TAG_NEW, "", null));
        }else{
            list.add(new FitexProperty(TAG_NEW, newHeader.getValue(), newHeader.toXMLLog()));
        }
        list.add(new FitexProperty(TAG_DATASET_MODEL, getModel(ds, locale), null));
        return list;
    }

    /* log: open dataset */
    public static List<FitexProperty> logOpenDataset(Dataset ds){
        List<FitexProperty> list = new LinkedList();
        list.add(new FitexProperty(TAG_DATASET_NAME, ds.getName(),ds.toXMLLog()));
        return list;
    }
    /* log: save dataset */
    public static List<FitexProperty> logSaveDataset(Dataset ds){
        List<FitexProperty> list = new LinkedList();
        list.add(new FitexProperty(TAG_DATASET_NAME, ds.getName(),ds.toXMLLog()));
        return list;
    }

    /* log: merge dataset */
    public static List<FitexProperty> logMergeDataset(Dataset ds, Locale locale,String fileName, Dataset finalDataset){
        List<FitexProperty> list = new LinkedList();
        list.add(new FitexProperty(TAG_DATASET_NAME, ds.getName(), null));
        list.add(new FitexProperty(TAG_FILE_NAME, fileName, null));
        list.add(new FitexProperty(TAG_DATASET, finalDataset.getName(), finalDataset.toXMLLog()));
        list.add(new FitexProperty(TAG_DATASET_MODEL, getModel(ds, locale), null));
        return list;
    }

    /* log:import csv file */
    public static List<FitexProperty> logImportCsvFile(String fileName, Dataset ds, Locale locale ){
        List<FitexProperty> list = new LinkedList();
        list.add(new FitexProperty(TAG_FILE_NAME, fileName, null));
        list.add(new FitexProperty(TAG_DATASET, ds.getName(), ds.toXMLLog()));
        list.add(new FitexProperty(TAG_DATASET_MODEL, getModel(ds, locale), null));
        return list;
    }

    /* log: export csv file */
    public static List<FitexProperty> logExportCsv(Dataset ds, String fileName){
        List<FitexProperty> list = new LinkedList();
        list.add(new FitexProperty(TAG_FILE_NAME, fileName, null));
        list.add(new FitexProperty(TAG_DATASET, ds.getName(), ds.toXMLLog()));
        return list;
    }

    /* log: delete dataset */
    public static List<FitexProperty> logDeleteDataset(Dataset ds){
        List<FitexProperty> list = new LinkedList();
        list.add(new FitexProperty(TAG_DATASET_NAME, ds.getName(), null));
        return list;
    }

    /* log: insert columns*/
    public static List<FitexProperty> logInsertColumns(Dataset ds, Locale locale, int nbCol, int idBefore){
        List<FitexProperty> list = new LinkedList();
        list.add(new FitexProperty(TAG_DATASET_NAME, ds.getName(), null));
        list.add(new FitexProperty(TAG_NB_COL, Integer.toString(nbCol), null));
        list.add(new FitexProperty(TAG_ID_BEFORE, Integer.toString(idBefore), null));
        list.add(new FitexProperty(TAG_DATASET_MODEL, getModel(ds, locale), null));
        return list;
    }
    /* log: insert rows*/
    public static List<FitexProperty> logInsertRows(Dataset ds, Locale locale, int nbRows, int idBefore){
        List<FitexProperty> list = new LinkedList();
        list.add(new FitexProperty(TAG_DATASET_NAME, ds.getName(), null));
        list.add(new FitexProperty(TAG_NB_ROWS, Integer.toString(nbRows), null));
        list.add(new FitexProperty(TAG_ID_BEFORE, Integer.toString(idBefore), null));
        list.add(new FitexProperty(TAG_DATASET_MODEL, getModel(ds, locale), null));
        return list;
    }

    /* log: add row */
    public static List<FitexProperty> logAddRow(Dataset ds, Locale locale , DataSetRow row){
        List<FitexProperty> list = new LinkedList();
        list.add(new FitexProperty(TAG_DATASET_NAME, ds.getName(), null));
        for(Iterator<String> values = row.getValues().iterator();values.hasNext();){
            String v = values.next();
            if(v != null && v.length() > 0)
                list.add(new FitexProperty(TAG_ROW_VALUE, v, null));
        }
        list.add(new FitexProperty(TAG_DATASET_MODEL, getModel(ds, locale), null));
        return list;
    }

    /* log: initialize header*/
    public static List<FitexProperty> logInitializeHeader(Dataset ds, Locale locale){
        List<FitexProperty> list = new LinkedList();
        list.add(new FitexProperty(TAG_DATASET_NAME, ds.getName(), null));
        int nbH = ds.getListDataHeader().length;
        for (int j=0; j<nbH; j++){
            list.add(new FitexProperty(TAG_HEADER, ds.getDataHeader(j).getValue(), ds.getDataHeader(j).toXMLLog()));
        }
        list.add(new FitexProperty(TAG_DATASET_MODEL, getModel(ds, locale), null));
        return list;
    }


    /* log: delete rows/columns*/
    public static List<FitexProperty> logDeleteData(Dataset ds,Locale locale, ArrayList<Data> listData,  ArrayList<Integer> listNoDataRow, ArrayList<Integer> listNoDataCol,ArrayList<DataOperation> listOperation){
        List<FitexProperty> list = new LinkedList();
        list.add(new FitexProperty(TAG_DATASET_NAME, ds.getName(), null));
        for(Iterator<Integer> i = listNoDataRow.iterator();i.hasNext();){
            list.add(new FitexProperty(TAG_ID_ROW, Integer.toString(i.next()), null));
        }
        for(Iterator<Integer> i = listNoDataCol.iterator();i.hasNext();){
            list.add(new FitexProperty(TAG_ID_COL, Integer.toString(i.next()), null));
        }
        for(Iterator<DataOperation> o = listOperation.iterator();o.hasNext();){
            DataOperation operation = o.next();
            list.add(new FitexProperty(TAG_OPERATION, operation.getName(), operation.toXMLLog()));
        }
        for(Iterator<Data> d = listData.iterator();d.hasNext();){
            Data data = d.next();
            if(data != null)
                list.add(new FitexProperty(TAG_DATA, data.getValue(), data.toXMLLog()));
        }
        list.add(new FitexProperty(TAG_DATASET_MODEL, getModel(ds, locale), null));
        return list;
    }

    /* log: add operation */
    public static List<FitexProperty> logAddOperation(Dataset ds, DataOperation operation){
        List<FitexProperty> list = new LinkedList();
        list.add(new FitexProperty(TAG_DATASET_NAME, ds.getName(), null));
        list.add(new FitexProperty(TAG_OPERATION, operation.getName(), operation.toXMLLog()));
        return list;
    }

    /* log: ignore data */
    public static List<FitexProperty> logIgnoreData(Dataset ds, Locale locale, boolean isIgnored, ArrayList<Data> listData){
        List<FitexProperty> list = new LinkedList();
        list.add(new FitexProperty(TAG_DATASET_NAME, ds.getName(), null));
        for(Iterator<Data> d = listData.iterator();d.hasNext();){
            Data data = d.next();
            list.add(new FitexProperty(TAG_DATA, data.getValue(), data.toXMLLog()));
        }
        list.add(new FitexProperty(TAG_DATASET_MODEL, getModel(ds, locale), null));
        return list;
    }

    /* log: create Visualization */
    public static List<FitexProperty> logCreateVisualization(Dataset ds, Locale locale, Visualization vis){
        List<FitexProperty> list = new LinkedList();
        list.add(new FitexProperty(TAG_DATASET_NAME, ds.getName(), null));
        list.add(new FitexProperty(TAG_VISUALIZATION, vis.getName(), vis.toXMLLog()));
        list.add(new FitexProperty(TAG_DATASET_MODEL, getModel(ds, locale), null));
        return list;
    }

    /* log: delete visualization */
    public static List<FitexProperty> logDeleteVisualization(Dataset ds, Locale locale, Visualization vis){
        List<FitexProperty> list = new LinkedList();
        list.add(new FitexProperty(TAG_DATASET_NAME, ds.getName(), null));
        list.add(new FitexProperty(TAG_VISUALIZATION, vis.getName(), null));
        list.add(new FitexProperty(TAG_DATASET_MODEL, getModel(ds, locale), null));
        return list;
    }

    /* log: rename dataset */
    public static List<FitexProperty> logRenameDataset(String oldName, String newName){
        List<FitexProperty> list = new LinkedList();
        list.add(new FitexProperty(TAG_OLD, oldName, null));
        list.add(new FitexProperty(TAG_NEW, newName, null));
        return list;
    }

    /* log: graph mode*/
    public static List<FitexProperty> logGraphMode(Dataset ds, Visualization vis, char graphMode){
        List<FitexProperty> list = new LinkedList();
        list.add(new FitexProperty(TAG_DATASET_NAME, ds.getName(), null));
        list.add(new FitexProperty(TAG_VISUALIZATION, vis.getName(), null));
        String mode = graphMode == DataConstants.MODE_ZOOM ? "zoom" : (graphMode == DataConstants.MODE_MOVE ? "move" : "autoscale");
        list.add(new FitexProperty(TAG_GRAPH_MODE, mode, null));
        return list;
    }

    /* log: update visualization param */
    public static List<FitexProperty> logUpdateGraphParam(Dataset ds, String oldName, Visualization newVis){
        List<FitexProperty> list = new LinkedList();
        list.add(new FitexProperty(TAG_DATASET_NAME, ds.getName(), null));
        list.add(new FitexProperty(TAG_OLD, oldName, null));
        list.add(new FitexProperty(TAG_VISUALIZATION, newVis.getName(), newVis.toXMLLog()));
        return list;
    }

    /* log: sort dataset */
    public static List<FitexProperty> logSortDataset(Dataset ds, List<Object[]> elementToSort){
        List<FitexProperty> list = new LinkedList();
        list.add(new FitexProperty(TAG_DATASET_NAME, ds.getName(), null));
        for(Iterator<Object[]> o = elementToSort.iterator();o.hasNext();){
            Object[] tab = o.next();
            String colName = (String)tab[0];
            int order = (Integer)tab[1];
            list.add(new FitexProperty(TAG_SORT_TITLE, colName, null));
            list.add(new FitexProperty(TAG_SORT_ORDER, order==1?"asc":"desc", null));
        }
        return list;
    }

    /* log: copy*/
    public static List<FitexProperty> logCopy(Dataset ds, ArrayList<int[]> listSelCell){
        List<FitexProperty> list = new LinkedList();
        list.add(new FitexProperty(TAG_DATASET_NAME, ds.getName(), null));
        for(Iterator<int[]> c = listSelCell.iterator();c.hasNext();){
            int[] cell = c.next();
            list.add(new FitexProperty(TAG_CELL, "", getCell(ds, cell)));
        }
        return list;
    }

    private static Element getCell(Dataset ds, int[] cell){
        Element e = new Element(TAG_CELL);
        e.addContent(new Element(TAG_ROW).setText(Integer.toString(cell[0])));
        e.addContent(new Element(TAG_COL).setText(Integer.toString(cell[1])));
        if(cell[0] < ds.getNbRows() && cell[1] < ds.getNbCol() && cell[0] >-1 && cell[1] > -1 && ds.getData(cell[0], cell[1]) != null){
            e.addContent(new Element(TAG_DATA).setText(TAG_ID).setText(ds.getData(cell[0], cell[1]).getValue()));
        }
        return e;
    }

    /* log: paste*/
    public static List<FitexProperty> logPaste(Dataset ds, Locale locale, int[] selCell, CopyDataset copyDs){
        List<FitexProperty> list = new LinkedList();
        list.add(new FitexProperty(TAG_DATASET_NAME, ds.getName(), null));
        list.add(new FitexProperty(TAG_CELL, "", getCell(ds, selCell)));
        list.add(new FitexProperty(TAG_SUBDATA, "", copyDs.toXMLLog()));
        list.add(new FitexProperty(TAG_DATASET_MODEL, getModel(ds, locale), null));
        return list;
    }

    

    /* log: function model */
    public static List<FitexProperty> logFunctionModel(Dataset ds, Locale locale, Graph graph, String description,  Color fColor, ArrayList<FunctionParam> listParam){
        List<FitexProperty> list = new LinkedList();
        list.add(new FitexProperty(TAG_DATASET_NAME, ds.getName(), null));
        list.add(new FitexProperty(TAG_VISUALIZATION, graph.getName(), null));
        list.add(new FitexProperty(TAG_FUNCTION, description, null));
        list.add(new FitexProperty(TAG_COLOR, "color", getColor(fColor)));
        if(listParam != null){
            for(Iterator<FunctionParam> p = listParam.iterator();p.hasNext();){
                FunctionParam param = p.next();
                list.add(new FitexProperty(TAG_FUNCTION_PARAM,param.getParam(), param.toXML() ));
            }
        }
        list.add(new FitexProperty(TAG_DATASET_MODEL, getModel(ds, locale), null));
        return list;
    }

    private static Element getColor(Color color){
        Element e= new Element(TAG_COLOR);
        e.addContent(new Element(TAG_COLOR_RED).setText(Integer.toString(color.getRed())));
        e.addContent(new Element(TAG_COLOR_GREEN).setText(Integer.toString(color.getGreen())));
        e.addContent(new Element(TAG_COLOR_BLUE).setText(Integer.toString(color.getBlue())));
        return e;
    }

    public static List<FitexProperty> logUndoRedo(Dataset ds, Locale locale, DataUndoRedo action){
        List<FitexProperty> list = new LinkedList();
        list.add(new FitexProperty(TAG_DATASET_NAME, ds.getName(), null));
        list.add(new FitexProperty(TAG_UNDO_REDO_ACTION, "", action.toXMLLog()));
        list.add(new FitexProperty(TAG_DATASET_MODEL, getModel(ds, locale), null));
        return list;
    }
}
