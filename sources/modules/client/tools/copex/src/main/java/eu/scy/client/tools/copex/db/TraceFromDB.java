/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.db;


import eu.scy.client.tools.copex.logger.CopexLog;
import eu.scy.client.tools.copex.logger.CopexProperty;
import eu.scy.client.tools.copex.utilities.CopexReturn;
import eu.scy.client.tools.copex.utilities.MyConstants;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jdom.Element;
import roolo.elo.JDomStringConversion;

/**
 * logging in database
 * @author Marjolaine
 */
public class TraceFromDB {

    /* create action log */
    public static CopexReturn logUserActionInDB(DataBaseCommunication dbC, long dbKeyMission, long dbKeyUser, long dbKeyTrace ,String type, List<CopexProperty> attribute){
        int oldDb = dbC.getDb();
        dbC.updateDb(MyConstants.DB_LABBOOK);
        // save action
       String queryAction = "INSERT INTO USER_ACTION (ID_ACTION, ID_TRACE, DATE_ACTION, TIME_ACTION, NAME_ACTION, CONTEXT_ACTION) " +
               "VALUES (NULL, "+dbKeyTrace+", CURDATE(), CURTIME(), '"+type+"', 'COPEX') ;";
       String queryID = "SELECT max(last_insert_id(`ID_ACTION`))   FROM USER_ACTION ;";
       ArrayList v2 = new ArrayList();
       CopexReturn cr = dbC.getNewIdInsertInDB(queryAction, queryID, v2);
       if (cr.isError()){
           dbC.updateDb(oldDb);
           return cr;
       }
       long dbKeyAction = (Long)v2.get(0);

        // save parameters
       String[] querys = new String[attribute.size()];
       int i=0;
        for(Iterator<CopexProperty> p = attribute.iterator();p.hasNext();){
            CopexProperty prop = p.next();
            String queryParam  = "";
            String b =  AccesDB.replace("\'",prop.getValue(),"''") ;
            if(prop.getName().equals(CopexLog.TAG_PROC_MODEL)){
                queryParam ="INSERT INTO ACTION_ATTRIBUTE (ID_ATTRIBUTE, ID_ACTION, ATTRIBUTE_NAME, ATTRIBUTE_VALUE_BLOB) VALUES (NULL, "+dbKeyAction+", '"+prop.getName()+"', '"+b+"') ;" ;
            }else{
                if(prop.getSubElement() != null && prop.getSubElement() instanceof Element){
                    queryParam ="INSERT INTO ACTION_ATTRIBUTE (ID_ATTRIBUTE, ID_ACTION, ATTRIBUTE_NAME, ATTRIBUTE_VALUE_BLOB) VALUES (NULL, "+dbKeyAction+", '"+prop.getName()+"', '"+AccesDB.replace("\'",new JDomStringConversion().xmlToString((Element)prop.getSubElement()),"''")+"') ;" ;
                }else{
                    queryParam = "INSERT INTO ACTION_ATTRIBUTE (ID_ATTRIBUTE, ID_ACTION, ATTRIBUTE_NAME, ATTRIBUTE_VALUE) VALUES (NULL, "+dbKeyAction+", '"+prop.getName()+"', '"+prop.getValue()+"') ;" ;
                }
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
        dbC.updateDb(MyConstants.DB_LABBOOK);
        CopexReturn cr = dbC.sendQuery(queryTrace, listFields, v2);
        if (cr.isError()){
            dbC.updateDb(MyConstants.DB_LABBOOK_COPEX);
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
                    dbC.updateDb(MyConstants.DB_LABBOOK_COPEX);
                    return cr;
                }
                dbKeyTrace = (Long) v2.get(0);
        }
        dbC.updateDb(MyConstants.DB_LABBOOK_COPEX);
        v.add(dbKeyTrace);
        return new CopexReturn();
    }
}
