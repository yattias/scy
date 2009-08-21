/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.db;

import eu.scy.tools.copex.trace.CopexTrace;
import eu.scy.tools.copex.trace.EdPTrace;
import eu.scy.tools.copex.trace.ParameterUserAction;
import eu.scy.tools.copex.trace.TraceAction;
import eu.scy.tools.copex.utilities.CopexReturn;
import eu.scy.tools.copex.utilities.CopexUtilities;
import eu.scy.tools.copex.utilities.MyConstants;
import java.util.ArrayList;

/**
 * enregistrement de la trace en base
 * @author MBO
 */
public class TraceFromDB {
    
    /* creation de la trace */
    static public CopexReturn addTraceInDB_xml(DataBaseCommunication dbC, CopexTrace trace, long dbKeyMission, long dbKeyUser, long dbKeyTrace){
        int oldDb = dbC.getDb();
        //Profiler.start();
       // Profiler.start("addTrace");
        dbC.updateDb(MyConstants.DB_COPEX);
        EdPTrace action = trace.getAction();
        int nbP = 0;
        if(action instanceof TraceAction){
            // enregistrement des parametres
            ArrayList<ParameterUserAction> listParameter = ((TraceAction)action).getListParameter();
            nbP = ((TraceAction)action).getNbParameters();
            // Profiler.start("param");
            for (int i=0; i<nbP; i++){
                ParameterUserAction p = listParameter.get(i);
                String queryParam = "INSERT INTO STRUCTURAL_ACTION (ID_PARAM, NAME_PARAM, VALUE_PARAM) VALUES (NULL, '"+p.getName()+"', '"+p.getValue()+"') ;" ;
                String queryID = "SELECT max(last_insert_id(`ID_PARAM`))   FROM STRUCTURAL_ACTION ;";
                ArrayList v2 = new ArrayList();
                CopexReturn cr = dbC.getNewIdInsertInDB(queryParam, queryID, v2);
                if (cr.isError()){
                    dbC.updateDb(oldDb);
                    return cr;
                }
                long dbKeyParam = (Long)v2.get(0);
                //on met a jour l'id
                p.setDbKey(dbKeyParam);
            }
        }
        // Profiler.end("param");
       // enregistrement de l'action
       String curDate = CopexUtilities.dateToSQL(trace.getDate());
       String curTime = CopexUtilities.timeToSQL(trace.getTime());
       // Profiler.start("action");
       String queryAction = "INSERT INTO USER_ACTION (ID_ACTION, DATE_ACTION, TIME_ACTION, NAME_ACTION, ASSIGNEMENT_ACTION) VALUES (NULL, CURDATE(), CURTIME(), '"+action.getType()+"', '"+trace.getAssignement()+"') ;";
       String queryID = "SELECT max(last_insert_id(`ID_ACTION`))   FROM USER_ACTION ;";  
       ArrayList v2 = new ArrayList();
       CopexReturn cr = dbC.getNewIdInsertInDB(queryAction, queryID, v2);
       if (cr.isError()){
           dbC.updateDb(oldDb);
           return cr;
       }
       long dbKeyAction = (Long)v2.get(0);
       //Profiler.end("action");
      
        // enregistrement des liens 
      // Profiler.start("liens");
        ArrayList v = new ArrayList();
        String[]querys = new String[nbP+1];
        String query1 = "INSERT INTO LINK_TRACE_ACTION (ID_TRACE, ID_ACTION) VALUES ("+dbKeyTrace+", "+dbKeyAction+") ;";
        querys[0] = query1 ;
        if(action instanceof TraceAction){
            ArrayList<ParameterUserAction> listParameter = ((TraceAction)action).getListParameter();
            // enregistrement des parametres
            for (int i=0; i<nbP; i++){
                ParameterUserAction p = listParameter.get(i);
                String queryP = "INSERT INTO LINK_ACTION_PARAM (ID_ACTION, ID_PARAM) VALUES ("+dbKeyAction+", "+p.getDbKey()+");";
                querys[i+1] = queryP ;
            }
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
}
