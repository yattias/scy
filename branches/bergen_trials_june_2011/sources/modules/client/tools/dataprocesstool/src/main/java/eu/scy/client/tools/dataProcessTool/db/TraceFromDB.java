/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.db;

import eu.scy.client.tools.dataProcessTool.logger.FitexLog;
import eu.scy.client.tools.dataProcessTool.logger.FitexProperty;
import eu.scy.client.tools.dataProcessTool.utilities.CopexReturn;
import eu.scy.client.tools.dataProcessTool.utilities.DataConstants;
import eu.scy.client.tools.dataProcessTool.utilities.MyUtilities;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * save the action logging in the database
 * @author Marjolaine
 */
public class TraceFromDB {
    /* creation log */
    public static CopexReturn logUserActionInDB(DataBaseCommunication dbC, long dbKeyMission, long dbKeyUser, long dbKeyTrace ,String type, List<FitexProperty> attribute){
        int oldDb = dbC.getDb();
        dbC.updateDb(DataConstants.DB_LABBOOK);
        // save action
       String queryAction = "INSERT INTO USER_ACTION (ID_ACTION, ID_TRACE, DATE_ACTION, TIME_ACTION, NAME_ACTION, CONTEXT_ACTION) " +
               "VALUES (NULL, "+dbKeyTrace+", CURDATE(), CURTIME(), '"+type+"', 'FITEX') ;";
       String queryID = "SELECT max(last_insert_id(`ID_ACTION`))   FROM USER_ACTION ;";
       ArrayList v2 = new ArrayList();
       CopexReturn cr = dbC.getNewIdInsertInDB(queryAction, queryID, v2);
       if (cr.isError()){
           dbC.updateDb(oldDb);
           return cr;
       }
       long dbKeyAction = (Long)v2.get(0);

        // save action's param
       String[] querys = new String[attribute.size()];
       int i=0;
        for(Iterator<FitexProperty> p = attribute.iterator();p.hasNext();){
            FitexProperty prop = p.next();
            String queryParam  = "";
            String b =  MyUtilities.replace("\'",prop.getValue(),"''") ;
            if(prop.getName().equals(FitexLog.TAG_DATASET_MODEL)){
                queryParam ="INSERT INTO ACTION_ATTRIBUTE (ID_ATTRIBUTE, ID_ACTION, ATTRIBUTE_NAME, ATTRIBUTE_VALUE_BLOB) VALUES (NULL, "+dbKeyAction+", '"+prop.getName()+"', '"+b+"') ;" ;
            }else{
                queryParam = "INSERT INTO ACTION_ATTRIBUTE (ID_ATTRIBUTE, ID_ACTION, ATTRIBUTE_NAME, ATTRIBUTE_VALUE) VALUES (NULL, "+dbKeyAction+", '"+prop.getName()+"', '"+prop.getValue()+"') ;" ;
            }
//            queryID = "SELECT max(last_insert_id(`ID_ATTRIBUTE`))   FROM ACTION_ATTRIBUTE ;";
//            v2 = new ArrayList();
//            cr = dbC.getNewIdInsertInDB(queryParam, queryID, v2);
//            if (cr.isError()){
//                dbC.updateDb(oldDb);
//                return cr;
//            }
            querys[i++] = queryParam;
        }
       ArrayList v3 = new ArrayList();
        cr = dbC.executeQuery(querys, v3);
        if (cr.isError()){
            dbC.updateDb(oldDb);
            return cr;
        }
        dbC.updateDb(oldDb);
        return cr;
    }



    public static CopexReturn  getIdTrace(DataBaseCommunication dbC,long dbKeyMission, long dbKeyUser, ArrayList v){
        long dbKeyTrace = -1;
        String queryTrace = "SELECT ID_TRACE FROM LB_TRACE WHERE ID_MISSION = "+dbKeyMission+" AND ID_USER = "+dbKeyUser+" ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("ID_TRACE");
        dbC.updateDb(DataConstants.DB_LABBOOK);
        CopexReturn cr = dbC.sendQuery(queryTrace, listFields, v2);
        if (cr.isError()){
            dbC.updateDb(DataConstants.DB_LABBOOK_FITEX);
            return cr;
        }
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("ID_TRACE");
            if (s == null)
                continue;
            dbKeyTrace = Long.parseLong(s);
        }
	    if (dbKeyTrace == -1){
                queryTrace = "INSERT INTO LB_TRACE (ID_TRACE, ID_MISSION, ID_USER) VALUES (NULL, "+dbKeyMission+", "+dbKeyUser+") ;";
                String queryID = "SELECT max(last_insert_id(`ID_TRACE`))   FROM LB_TRACE ;";
                v2 = new ArrayList();
                cr = dbC.getNewIdInsertInDB(queryTrace, queryID, v2) ;
                if (cr.isError()){
                    dbC.updateDb(DataConstants.DB_LABBOOK_FITEX);
                    return cr;
                }
                dbKeyTrace = (Long) v2.get(0);
        }
        dbC.updateDb(DataConstants.DB_LABBOOK_FITEX);
        v.add(dbKeyTrace);
        return new CopexReturn();
    }
}
