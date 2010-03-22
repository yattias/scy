/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.db;


import eu.scy.client.tools.copex.logger.CopexProperty;
import eu.scy.client.tools.copex.utilities.CopexReturn;
import eu.scy.client.tools.copex.utilities.CopexUtilities;
import eu.scy.client.tools.copex.utilities.MyConstants;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * enregistrement de la trace en base
 * @author MBO
 */
public class TraceFromDB {

    /* creation de la trace */
    public static CopexReturn logUserActionInDB(DataBaseCommunication dbC, long dbKeyMission, long dbKeyUser, long dbKeyTrace ,String type, List<CopexProperty> attribute){
        int oldDb = dbC.getDb();
        dbC.updateDb(MyConstants.DB_COPEX);
        // enregistrement des parametres
        for(Iterator<CopexProperty> p = attribute.iterator();p.hasNext();){
            CopexProperty prop = p.next();
            String queryParam = "INSERT INTO STRUCTURAL_ACTION (ID_PARAM, NAME_PARAM, VALUE_PARAM) VALUES (NULL, '"+prop.getName()+"', '"+prop.getValue()+"') ;" ;
            String queryID = "SELECT max(last_insert_id(`ID_PARAM`))   FROM STRUCTURAL_ACTION ;";
            ArrayList v2 = new ArrayList();
            CopexReturn cr = dbC.getNewIdInsertInDB(queryParam, queryID, v2);
            if (cr.isError()){
                dbC.updateDb(oldDb);
                return cr;
            }
            long dbKeyParam = (Long)v2.get(0);
            prop.setDbKey(dbKeyParam);
        }
        // enregistrement de l'action
       // Profiler.start("action");
       String queryAction = "INSERT INTO USER_ACTION (ID_ACTION, DATE_ACTION, TIME_ACTION, NAME_ACTION, ASSIGNEMENT_ACTION) " +
               "VALUES (NULL, CURDATE(), CURTIME(), '"+type+"', 'PROCEDURE') ;";
       String queryID = "SELECT max(last_insert_id(`ID_ACTION`))   FROM USER_ACTION ;";
       ArrayList v2 = new ArrayList();
       CopexReturn cr = dbC.getNewIdInsertInDB(queryAction, queryID, v2);
       if (cr.isError()){
           dbC.updateDb(oldDb);
           return cr;
       }
       long dbKeyAction = (Long)v2.get(0);
       // enregistrement des liens
      // Profiler.start("liens");
        ArrayList v = new ArrayList();
        String[]querys = new String[attribute.size()+1];
        String query1 = "INSERT INTO LINK_TRACE_ACTION (ID_TRACE, ID_ACTION) VALUES ("+dbKeyTrace+", "+dbKeyAction+") ;";
        querys[0] = query1 ;
        int i=0;
        for(Iterator<CopexProperty> p = attribute.iterator();p.hasNext();){
            CopexProperty prop = p.next();
            String queryP = "INSERT INTO LINK_ACTION_PARAM (ID_ACTION, ID_PARAM) VALUES ("+dbKeyAction+", "+prop.getDbKey()+");";
            querys[i+1] = queryP ;
            i++;
        }
        cr = dbC.executeQuery(querys, v);
        /*Profiler.end("liens");
        Profiler.end("addTrace");
        System.out.println("Resultat :\n"+Profiler.display());
        System.out.println("\nStats  :\n"+Profiler.getStats());
        Profiler.reset();*/
        dbC.updateDb(oldDb);
        return cr;
    }
    
    /* retourne en v[0] et en v[1] la date et l'heure */
    public static CopexReturn getDateAndTime(DataBaseCommunication dbC, ArrayList v){
        String query = "SELECT CURDATE(), CURTIME() ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("CURDATE()");
        listFields.add("CURTIME()");
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String date = rs.getColumnData("CURDATE()");
            String time = rs.getColumnData("CURTIME()");
            v.add(date);
            v.add(time);
            break;
        }
        return new CopexReturn();
    }

    public static CopexReturn  getIdTrace(DataBaseCommunication dbC,long dbKeyMission, long dbKeyUser, ArrayList v){
        long dbKeyTrace = -1;
        String queryTrace = "SELECT ID_TRACE FROM COPEX_TRACE WHERE ID_MISSION = "+dbKeyMission+" AND ID_USER = "+dbKeyUser+" ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("ID_TRACE");
        dbC.updateDb(MyConstants.DB_COPEX);
        CopexReturn cr = dbC.sendQuery(queryTrace, listFields, v2);
        if (cr.isError()){
            dbC.updateDb(MyConstants.DB_COPEX_EDP);
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
                queryTrace = "INSERT INTO COPEX_TRACE (ID_TRACE, ID_MISSION, ID_USER) VALUES (NULL, "+dbKeyMission+", "+dbKeyUser+") ;";
                String queryID = "SELECT max(last_insert_id(`ID_TRACE`))   FROM COPEX_TRACE ;";
                v2 = new ArrayList();
                cr = dbC.getNewIdInsertInDB(queryTrace, queryID, v2) ;
                if (cr.isError()){
                    dbC.updateDb(MyConstants.DB_COPEX_EDP);
                    return cr;
                }
                dbKeyTrace = (Long) v2.get(0);
        }
        dbC.updateDb(MyConstants.DB_COPEX_EDP);
        v.add(dbKeyTrace);
        return new CopexReturn();
    }
}
