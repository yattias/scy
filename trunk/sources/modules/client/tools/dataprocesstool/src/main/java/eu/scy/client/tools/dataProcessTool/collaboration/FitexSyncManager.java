/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.collaboration;

import eu.scy.common.datasync.ISyncObject;
import eu.scy.common.datasync.SyncObject;
import eu.scy.client.tools.dataProcessTool.common.Data;
import eu.scy.client.tools.dataProcessTool.common.DataHeader;
import eu.scy.client.tools.dataProcessTool.common.DataOperation;
import eu.scy.client.tools.fitex.main.DataProcessToolPanel;
import java.util.ArrayList;
import java.util.Iterator;
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
            }
        }
    }

    /*sync. node removed */
    public void syncNodeRemoved(ISyncObject syncObject){

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
        String xmlData = "";
        for(Iterator<Data> d= listData.iterator();d.hasNext();){
            xmlData += xmlToString(d.next().toXMLLog());
        }
        syncObject.setProperty(sync_listData, xmlData);
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
}
