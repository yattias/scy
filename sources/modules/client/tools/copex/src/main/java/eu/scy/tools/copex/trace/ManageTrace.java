/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

import eu.scy.tools.copex.db.DataBaseCommunication;
import eu.scy.tools.copex.db.ResultSetXML;
import eu.scy.tools.copex.db.TraceFromDB;
import eu.scy.tools.copex.utilities.CopexReturn;
import eu.scy.tools.copex.utilities.CopexUtilities;
import eu.scy.tools.copex.utilities.MyConstants;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

/**
 * gestion de la trace
 * @author MBO
 */
public class ManageTrace {
    // ATTRIBUTS
    /* edP */
    private EdPTrace edP;
    /* nom du fichier */
    private String fileName;
    /* compteur */
    private int cpt;
    /* dbC*/
    private DataBaseCommunication dbC;
    /* dbKeyMission */
    private long dbKeyMission;
    /* dbKeyUser*/
    private long dbKeyUser;
    private long dbKeyTrace;
    // CONSTRUCTEURS 
    public ManageTrace(EdPTrace edP, String fileName, int cpt) {
       this.edP = edP;
        this.fileName = fileName;
        this.cpt = cpt;
    }
    
    public ManageTrace(DataBaseCommunication dbC, long dbKeyUser, long dbKeyMission) {
       this.edP = null;
        this.fileName = "";
        this.cpt = 0;
        this.dbKeyMission = dbKeyMission;
        this.dbKeyUser = dbKeyUser;
        this.dbC = dbC;
        getIdTrace();
    }
    
    // METHODES
    /* ajout d'une action */
    public CopexReturn addAction(EdPTrace action){
        if(action instanceof TraceAction)
            ((TraceAction)action).initParameter();
        String id = cpt+"";
        this.cpt++;
        Date date = CopexUtilities.getCurrentDate();
        Time time = CopexUtilities.getCurrentTime();
        CopexTrace trace = new CopexTrace(id, date, time, action);
        // MBO le 23/10/08 : on enregistre plutot en base
        CopexReturn cr = TraceFromDB.addTraceInDB_xml(dbC, trace, dbKeyMission, dbKeyUser, dbKeyTrace);
        return cr;
        /*try {
            TraceFile file = new TraceFile(edP, fileName);
            file.addAction(trace.toStringXML());
            file.closeFile();
        } catch (IOException ex) {
            return new CopexReturn(ex.getMessage(), false);
        }
        return new CopexReturn();*/
    }
    
    public CopexReturn  getIdTrace(){
       dbKeyTrace = -1;
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
        return new CopexReturn();
    }
    /* mise a jour du compteur */
    public CopexReturn updateIdentifier(int cpt){
        this.cpt = cpt +1;
        return new CopexReturn();
    }
}
