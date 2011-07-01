/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.collaboration;

import eu.scy.client.tools.dataProcessTool.common.CopyDataset;
import eu.scy.common.datasync.ISyncObject;
import eu.scy.common.datasync.SyncObject;
import eu.scy.client.tools.dataProcessTool.common.Data;
import eu.scy.client.tools.dataProcessTool.common.DataHeader;
import eu.scy.client.tools.dataProcessTool.common.DataOperation;
import eu.scy.client.tools.dataProcessTool.common.FunctionModel;
import eu.scy.client.tools.dataProcessTool.common.Graph;
import eu.scy.client.tools.dataProcessTool.common.Visualization;
import eu.scy.client.tools.fitex.main.DataProcessToolPanel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.Element;
import org.jdom.JDOMException;
import roolo.elo.JDomStringConversion;

/**
 * collaboration manager.
 * @author Marjolaine
 */
public class FitexSyncManager {
    public final static String TOOLNAME = "fitex";
    // sync. name action
    private final static String sync_name = "sync_name";
    private final static String sync_data_value = "value";
    private final static String sync_id_row = "id_row";
    private final static String sync_id_col = "id_col";
    private final static String sync_header_xml = "header_xml";
    private final static String sync_is_ignored = "is_ignored";
    private final static String sync_listData = "list_data";
    private final static String sync_operation = "operation";
    private final static String sync_visualization = "visualization";
    private final static String sync_function = "functionModel";
    private final static String sync_listHeader = "list_header";
    private final static String sync_listOperations = "list_operations";
    private final static String sync_listSelectedRows = "list_selectedRows";
    private final static String sync_listSelectedCols = "list_selectedCols";
    private final static String sync_is_on_col = "is_on_col";
    private final static String sync_nb = "nb";
    private final static String sync_id_before = "id_before";
    private final static String sync_dataset = "dataset";

    private DataProcessToolPanel fitex;
    private final Logger logger;

    public FitexSyncManager(DataProcessToolPanel fitex) {
        this.fitex = fitex;
        logger = Logger.getLogger(FitexSyncManager.class.getName());
    }

    /*sync. node added */
    public void syncNodeAdded(ISyncObject syncObject){
        String syncName = syncObject.getProperty(sync_name);
        if(syncName != null){
            if(syncName.equals(FitexSyncEnum.OPERATION_CREATED.name())){ // create operation
                createOperation(syncObject);
            }else if(syncName.equals(FitexSyncEnum.GRAPH_CREATED.name())){ // create vis
                createGraph(syncObject);
            }else if(syncName.equals(FitexSyncEnum.DATA_INSERTED.name())){ // insert data
                addInsertData(syncObject);
            }
        }
    }

    /*sync. node removed */
    public void syncNodeRemoved(ISyncObject syncObject){
        String syncName = syncObject.getProperty(sync_name);
        if(syncName != null){
            if(syncName.equals(FitexSyncEnum.DATA_DELETED.name())){ // delete data
                deleteData(syncObject);
            }
        }
    }

    /*sync. node changed */
    public void syncNodeChanged(ISyncObject syncObject){
        String syncName = syncObject.getProperty(sync_name);
        if(syncName != null){
            if(syncName.equals(FitexSyncEnum.DATA_CHANGED.name())){ // update data
                updateData(syncObject);
            }else if(syncName.equals(FitexSyncEnum.DATAHEADER_CHANGED.name())){ // update header
                updateDataHeader(syncObject);
            }else if(syncName.equals(FitexSyncEnum.DATA_IGNORED.name())){ // ignore data
                updateIgnoreData(syncObject);
            }else if(syncName.equals(FitexSyncEnum.GRAPH_PARAM_UPDATED.name())){ // update graph param
                updateGraphParam(syncObject);
            }else if(syncName.equals(FitexSyncEnum.FUNCTION_MODEL_UPDATED.name())){ // update function model
                updateFunctionModel(syncObject);
            }else if(syncName.equals(FitexSyncEnum.DATASET_SORT.name())){ // sort data
                updateSortData(syncObject);
            }else if(syncName.equals(FitexSyncEnum.PASTE.name())){ // paste
                updatePaste(syncObject);
            }
        }
    }

    private String xmlToString(Element e){
        return new JDomStringConversion().xmlToString(e);
    }
    private Element stringToXml(String s){
        return new JDomStringConversion().stringToXml(s);
    }
    
    // UPDATE DATA
    // add the event
    public void addSyncUpdateData(String value, int idRow, int idCol){
        ISyncObject syncObject = new SyncObject();
        syncObject.setToolname(TOOLNAME);
        syncObject.setProperty(sync_name, FitexSyncEnum.DATA_CHANGED.name());
        syncObject.setProperty(sync_data_value, value);
        syncObject.setProperty(sync_id_row, Integer.toString(idRow));
        syncObject.setProperty(sync_id_col, Integer.toString(idCol));
        fitex.changeFitexSyncObject(syncObject);
    }

   // calls when received a sync. event
    private void updateData(ISyncObject syncObject){
        int idRow = -1;
        int idCol = -1;
        try{
            idRow = Integer.parseInt(syncObject.getProperty(sync_id_row));
            idCol = Integer.parseInt(syncObject.getProperty(sync_id_col));
        }catch(NumberFormatException e){
            return;
        }
        fitex.updateData(syncObject.getProperty(sync_data_value), idRow, idCol);
    }

    // UPDATE DATAHEADER
    // add the event
    public void addSyncUpdateDataHeader(DataHeader header){
        ISyncObject syncObject = new SyncObject();
        syncObject.setToolname(TOOLNAME);
        syncObject.setProperty(sync_name, FitexSyncEnum.DATAHEADER_CHANGED.name());
        syncObject.setProperty(sync_header_xml, xmlToString(header.toXMLLog()));
        fitex.changeFitexSyncObject(syncObject);
    }

   // calls when received a sync. event
    private void updateDataHeader(ISyncObject syncObject){
        String s = syncObject.getProperty(sync_header_xml);
        try{
            DataHeader header = new DataHeader(stringToXml(s));
            fitex.updateDataHeader(header);
        }catch(JDOMException ex){
            logger.log(Level.SEVERE, "updateDataHeader synch. "+ex);
        }
    }

    // IGNORED DATA
    // add the event
    public void addSyncUpdateIgnoreData(boolean isIgnored, ArrayList<Data> listData){
        ISyncObject syncObject = new SyncObject();
        syncObject.setToolname(TOOLNAME);
        syncObject.setProperty(sync_name, FitexSyncEnum.DATA_IGNORED.name());
        syncObject.setProperty(sync_is_ignored, Boolean.toString(isIgnored));
        Element e = new Element(sync_listData);
        for(Iterator<Data> d= listData.iterator();d.hasNext();){
            e.addContent(d.next().toXMLLog());
        }
        syncObject.setProperty(sync_listData, xmlToString(e));
        fitex.changeFitexSyncObject(syncObject);
    }

   // calls when received a sync. event
    private void updateIgnoreData(ISyncObject syncObject){
        boolean isIgnored= Boolean.parseBoolean(syncObject.getProperty(sync_is_ignored));
        ArrayList<Data> listData = new ArrayList();
        Element xmlData = stringToXml(syncObject.getProperty(sync_listData));
        try{
            for(Iterator<Element> el = xmlData.getChildren(Data.TAG_DATA).iterator();el.hasNext();){
                Data data = new Data(el.next());
                listData.add(data);
            }
            fitex.updateIgnoreData(isIgnored, listData);
        }catch(JDOMException ex){
            logger.log(Level.SEVERE, "updateIgnoreData synch. "+ex);
        }
    }


    // CREATE OPERATION
    // add the event
    public void addSyncCreateoperation(DataOperation operation){
        ISyncObject syncObject = new SyncObject();
        syncObject.setToolname(TOOLNAME);
        syncObject.setProperty(sync_name, FitexSyncEnum.OPERATION_CREATED.name());
        String xmlData = "";
        xmlData = xmlToString(operation.toXMLLog());
        syncObject.setProperty(sync_operation, xmlData);
        fitex.addFitexSyncObject(syncObject);
    }

   // calls when received a sync. event
    private void createOperation(ISyncObject syncObject){
        DataOperation operation = null;
        try{
            operation = new DataOperation(stringToXml(syncObject.getProperty(sync_operation)));
            fitex.createOperation(operation);
        }catch(JDOMException ex){
            logger.log(Level.SEVERE, "createOperation synch. "+ex);
        }
    }

    // GRAPH CREATED
    // add the event
    public void addSyncGraphCreated(Visualization vis){
        ISyncObject syncObject = new SyncObject();
        syncObject.setToolname(TOOLNAME);
        syncObject.setProperty(sync_name, FitexSyncEnum.GRAPH_CREATED.name());
        String xmlData = "";
        xmlData = xmlToString(vis.toXMLLog());
        syncObject.setProperty(sync_visualization, xmlData);
        fitex.addFitexSyncObject(syncObject);
    }

   // calls when received a sync. event
    private void createGraph(ISyncObject syncObject){
        Visualization vis  = null;
        try{
            vis = new Visualization(stringToXml(syncObject.getProperty(sync_visualization)));
            fitex.createVisualization(vis);
        }catch(JDOMException ex){
            logger.log(Level.SEVERE, "createGraph synch. "+ex);
        }
    }

    // GRAPH PARAM UPDATED
    // add the event
    public void addSyncGraphParamUpdated(Graph graph){
        ISyncObject syncObject = new SyncObject();
        syncObject.setToolname(TOOLNAME);
        syncObject.setProperty(sync_name, FitexSyncEnum.GRAPH_PARAM_UPDATED.name());
        String xmlData = "";
        xmlData = xmlToString(graph.toXMLLog());
        syncObject.setProperty(sync_visualization, xmlData);
        fitex.addFitexSyncObject(syncObject);
    }

   // calls when received a sync. event
    private void updateGraphParam(ISyncObject syncObject){
        Graph graph  = null;
        try{
            graph = new Graph(stringToXml(syncObject.getProperty(sync_visualization)));
            fitex.updateGraphParam(graph);
        }catch(JDOMException ex){
            logger.log(Level.SEVERE, "updateGraphParam synch. "+ex);
        }
    }

    // FUNCTION MODEL UPDATED
    // add the event
    public void addSyncFunctionModelUpdated(Graph graph, FunctionModel fm){
        ISyncObject syncObject = new SyncObject();
        syncObject.setToolname(TOOLNAME);
        syncObject.setProperty(sync_name, FitexSyncEnum.FUNCTION_MODEL_UPDATED.name());
        String xmlData = "";
        xmlData = xmlToString(fm.toXML());
        syncObject.setProperty(sync_function, xmlData);
        syncObject.setProperty(sync_visualization, xmlToString(graph.toXMLLog()));
        fitex.addFitexSyncObject(syncObject);
    }

   // calls when received a sync. event
    private void updateFunctionModel(ISyncObject syncObject){
        FunctionModel fm  = null;
        Graph graph = null;
        try{
            graph = new Graph(stringToXml(syncObject.getProperty(sync_visualization)));
            fm = new FunctionModel(stringToXml(syncObject.getProperty(sync_function)));
            fitex.updateFunctionModel(graph, fm);
        }catch(JDOMException ex){
            logger.log(Level.SEVERE, "updateFunctionModel synch. "+ex);
        }
    }

    // DATA DELETED
    // add the event
    public void addSyncDataDeleted(ArrayList<Data> listData, ArrayList<DataHeader> listHeader, ArrayList<DataOperation> listOperation, ArrayList<Integer>[] listRowAndCol){
        ISyncObject syncObject = new SyncObject();
        syncObject.setToolname(TOOLNAME);
        syncObject.setProperty(sync_name, FitexSyncEnum.DATA_DELETED.name());
        //data
        Element e = new Element(sync_listData);
        for(Iterator<Data> d= listData.iterator();d.hasNext();){
            e.addContent(d.next().toXMLLog());
        }
        syncObject.setProperty(sync_listData, xmlToString(e));
        //header
        Element e2 = new Element(sync_listHeader);
        for(Iterator<DataHeader> h= listHeader.iterator();h.hasNext();){
            e.addContent(h.next().toXMLLog());
        }
        syncObject.setProperty(sync_listHeader, xmlToString(e2));
        //operation
        Element e3 = new Element(sync_listOperations);
        for(Iterator<DataOperation> o= listOperation.iterator();o.hasNext();){
            e.addContent(o.next().toXMLLog());
        }
        syncObject.setProperty(sync_listOperations, xmlToString(e3));
        // selected rows/cols
        Element e4 = new Element(sync_listSelectedRows);
        for(Iterator<Integer> i= listRowAndCol[0].iterator();i.hasNext();){
            int id = i.next();
            e4.addContent(new Element(sync_id_row).setText(Integer.toString(id)));
        }
        syncObject.setProperty(sync_listSelectedRows, xmlToString(e4));
        Element e5 = new Element(sync_listSelectedCols);
        for(Iterator<Integer> j= listRowAndCol[1].iterator();j.hasNext();){
            int id = j.next();
            e5.addContent(new Element(sync_id_col).setText(Integer.toString(id)));
        }
        syncObject.setProperty(sync_listSelectedCols, xmlToString(e5));
        //
        fitex.addFitexSyncObject(syncObject);
    }

   // calls when received a sync. event
    private void deleteData(ISyncObject syncObject){
        try{
            ArrayList<Data> listData = new ArrayList();
            ArrayList<DataHeader> listHeader = new ArrayList();
            ArrayList<DataOperation> listOperation = new ArrayList();
            ArrayList<Integer>[] listRowAndCol = new ArrayList[2];
            //data
            Element xmlData = stringToXml(syncObject.getProperty(sync_listData));
            for(Iterator<Element> el = xmlData.getChildren(Data.TAG_DATA).iterator();el.hasNext();){
                Data data = new Data(el.next());
                listData.add(data);
            }
            //header
            Element xmlHeader = stringToXml(syncObject.getProperty(sync_listHeader));
            for(Iterator<Element> el = xmlHeader.getChildren(DataHeader.TAG_HEADER).iterator();el.hasNext();){
                DataHeader header = new DataHeader(el.next());
                listHeader.add(header);
            }
            //operation
            Element xmlOp = stringToXml(syncObject.getProperty(sync_listOperations));
            for(Iterator<Element> el = xmlOp.getChildren(DataOperation.TAG_OPERATION).iterator();el.hasNext();){
                DataOperation op = new DataOperation(el.next());
                listOperation.add(op);
            }
            //selected rows/cols
            ArrayList<Integer> listRows = new ArrayList();
            ArrayList<Integer> listCols = new ArrayList();
            Element xmlRows = stringToXml(syncObject.getProperty(sync_listSelectedRows));
            for(Iterator<Element> el = xmlRows.getChildren(sync_id_row).iterator();el.hasNext();){
                int id = Integer.parseInt(el.next().getText());
                listRows.add(id);
            }
            Element xmlCols = stringToXml(syncObject.getProperty(sync_listSelectedCols));
            for(Iterator<Element> el = xmlCols.getChildren(sync_id_col).iterator();el.hasNext();){
                int id = Integer.parseInt(el.next().getText());
                listCols.add(id);
            }
            listRowAndCol[0] = listRows;
            listRowAndCol[1] = listCols;
            //
            fitex.deleteData(listData, listHeader, listOperation, listRowAndCol);
        }catch(JDOMException ex){
            logger.log(Level.SEVERE, "deleteData synch. "+ex);
        }
    }

    // DATA INSERT
    // add the event
    public void addSyncInsertData(boolean isOnCol, int nb, int idBefore){
        ISyncObject syncObject = new SyncObject();
        syncObject.setToolname(TOOLNAME);
        syncObject.setProperty(sync_name, FitexSyncEnum.DATA_INSERTED.name());
        syncObject.setProperty(sync_is_on_col, Boolean.toString(isOnCol));
        syncObject.setProperty(sync_nb, Integer.toString(nb));
        syncObject.setProperty(sync_id_before, Integer.toString(idBefore));
        fitex.addFitexSyncObject(syncObject);
    }

   // calls when received a sync. event
    private void addInsertData(ISyncObject syncObject){
        int nb = 0;
        int idBefore  =-1;
        try{
            boolean isOnCol = Boolean.getBoolean(syncObject.getProperty(sync_is_on_col));
            nb = Integer.parseInt(syncObject.getProperty(sync_nb));
            idBefore = Integer.parseInt(syncObject.getProperty(sync_id_before));
            fitex.insertData(isOnCol, nb, idBefore);
        }catch(NumberFormatException ex){
            logger.log(Level.SEVERE, "addInsertData synch. "+ex);
        }
    }

    // SORT DATA
    // add the event
    public void addSyncSortData(Vector exchange){
        ISyncObject syncObject = new SyncObject();
        syncObject.setToolname(TOOLNAME);
        syncObject.setProperty(sync_name, FitexSyncEnum.DATASET_SORT.name());
        Element e = new Element(sync_listData);
        for(Iterator v= exchange.iterator();v.hasNext();){
            e.addContent(new Element(sync_id_row).setText(Integer.toString((Integer)v.next())));
        }
        syncObject.setProperty(sync_listData, xmlToString(e));
        fitex.addFitexSyncObject(syncObject);
    }

   // calls when received a sync. event
    private void updateSortData(ISyncObject syncObject){
        try{
            Vector exchange = new Vector();
            Element xmlData = stringToXml(syncObject.getProperty(sync_listData));
            for(Iterator<Element> el = xmlData.getChildren(sync_id_row).iterator();el.hasNext();){
                int id = Integer.parseInt(el.next().getText());
                exchange.add(id);
            }
            fitex.sortData(exchange);
        }catch(NumberFormatException ex){
            logger.log(Level.SEVERE, "updateSortData synch. "+ex);
        }
    }

    // PASTE
    // add the event
    public void addSyncPaste(CopyDataset copyDs, int[] selCell){
        ISyncObject syncObject = new SyncObject();
        syncObject.setToolname(TOOLNAME);
        syncObject.setProperty(sync_name, FitexSyncEnum.PASTE.name());
        syncObject.setProperty(sync_dataset, xmlToString(copyDs.toXMLLog()));
        fitex.addFitexSyncObject(syncObject);
    }

   // calls when received a sync. event
    private void updatePaste(ISyncObject syncObject){
        CopyDataset copyDs = null;
        int[] selCell = null;
         try{
            copyDs = new CopyDataset(stringToXml(syncObject.getProperty(sync_dataset)));
            fitex.paste(copyDs, selCell);
        }catch(JDOMException ex){
            logger.log(Level.SEVERE, "updatePaste synch. "+ex);
        }
    }
}
