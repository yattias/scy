/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.db;

import eu.scy.client.tools.copex.common.*;
import eu.scy.client.tools.copex.utilities.CopexReturn;
import eu.scy.client.tools.copex.utilities.CopexUtilities;
import eu.scy.client.tools.copex.utilities.MyConstants;
import java.util.ArrayList;

/**
 * gestion de la table COPEX_MISSION dans la BD
 * @author MBO
 */
public class MissionFromDB {
    
    
     public static CopexReturn getMissionFromDB_xml(DataBaseCommunication dbC, long dbKeyMission, long dbKeyLearner, ArrayList v) {
        dbC.updateDb(MyConstants.DB_COPEX);
        CopexMission m = null;
        String query = "SELECT M.ID_MISSION, M.CODE, M.MISSION_NAME, M.SUM_UP, L.STATUT " +
                "FROM COPEX_MISSION M , LINK_MISSION_LEARNER L " +
                "WHERE L.ID_MISSION = "+dbKeyMission +" AND L.ID_LEARNER = "+dbKeyLearner +" AND " +
                "L.ID_MISSION = M.ID_MISSION;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("M.ID_MISSION");
        listFields.add("M.CODE");
        listFields.add("M.MISSION_NAME");
        listFields.add("M.SUM_UP");
        listFields.add("L.STATUT");
        dbC.updateDb(MyConstants.DB_COPEX);
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError()){
            dbC.updateDb(MyConstants.DB_COPEX_EDP);
            return cr;
        }
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("M.ID_MISSION");
            long dbKey = Long.parseLong(s);
            String code = rs.getColumnData("M.CODE");
            String mission_name = rs.getColumnData("M.MISSION_NAME");
            String sumUp = rs.getColumnData("M.SUM_UP");
            s = rs.getColumnData("L.STATUT");
            char statut = s.charAt(0);
            ArrayList v3 = new ArrayList();
            cr  = getMissionOptionFromDB(dbC, dbKey, v3);
            if (cr.isError())
                return cr;
            OptionMission options = (OptionMission)v3.get(0);
            m = new CopexMission(dbKey,  mission_name,code,sumUp, statut, options);
        }
       v.add(m);
       dbC.updateDb(MyConstants.DB_COPEX_EDP);
       return new CopexReturn();     
        
    }
     
   
    
   
    /* chargement de toutes les missions  de l'utilisateur sauf idMission*/
    public static CopexReturn getAllMissionsFromDB_xml(DataBaseCommunication dbC, long dbKeyUser, long dbKeyMission,  ArrayList v) {
        dbC.updateDb(MyConstants.DB_COPEX);
        ArrayList<CopexMission> listMission = new ArrayList();
        String query = "SELECT M.ID_MISSION, M.CODE, M.MISSION_NAME, M.SUM_UP, L.STATUT " +
                "FROM COPEX_MISSION M, LINK_MISSION_LEARNER L " +
                "WHERE M.ID_MISSION != "+dbKeyMission +" AND  M.ID_MISSION = L.ID_MISSION " +
                "AND L.ID_LEARNER = "+dbKeyUser+" ;";
       
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("M.ID_MISSION");
        listFields.add("M.CODE");
        listFields.add("M.MISSION_NAME");
        listFields.add("M.SUM_UP");
        listFields.add("L.STATUT");
        
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError()){
            dbC.updateDb(MyConstants.DB_COPEX_EDP);
            return cr;
        }
        dbC.updateDb(MyConstants.DB_COPEX_EDP);
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("M.ID_MISSION");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            String code = rs.getColumnData("M.CODE");
            if (code == null)
                continue;
            String mission_name = rs.getColumnData("M.MISSION_NAME");
            if (mission_name == null)
                continue;
            String description = rs.getColumnData("M.SUM_UP");
            if (description == null)
                continue;
             s = rs.getColumnData("L.STATUT");
            if (s == null)
                continue;
            char statut = s.charAt(0);
            ArrayList v3 = new ArrayList();
            cr = getMissionOptionFromDB(dbC, dbKey, v3);
            if(cr.isError())
                return cr;
            OptionMission options= (OptionMission)v3.get(0);
            CopexMission m = new CopexMission(dbKey, mission_name,code,  description, statut, options);
            
            
            listMission.add(m);
        }
        
        v.add(listMission);
        return new CopexReturn();
    }
    
    
   
    /* mise a jour de la date de modif d'une mission */
    static public CopexReturn updateDateMissionInDB_xml(DataBaseCommunication dbC, long dbKeyMission, java.sql.Date date){
        dbC.updateDb(MyConstants.DB_COPEX);
        String dM = CopexUtilities.dateToSQL(date);
        String query = "UPDATE COPEX_MISSION SET DATE_LAST_MODIFICATION = '"+dM+"' "+
                    "WHERE ID_MISSION = "+dbKeyMission+" ;";
        ArrayList v = new ArrayList();
        String[] querys = new String[1];
        querys[0] = query ;
        CopexReturn cr = dbC.executeQuery(querys, v);
        dbC.updateDb(MyConstants.DB_COPEX_EDP);
        return cr;
        
    }

    public static CopexReturn createUserMissionInDB(DataBaseCommunication dbC, long dbKeyUser, long dbKeyMission){
        String query = "INSERT INTO LINK_MISSION_LEARNER (ID_MISSION, ID_LEARNER, STATUT) " +
                "VALUES ("+dbKeyMission+", "+dbKeyUser+", 'T');";
        dbC.updateDb(MyConstants.DB_COPEX);
        ArrayList v = new ArrayList();
        String[] querys = new String[1];
        querys[0] = query ;
        CopexReturn cr = dbC.executeQuery(querys, v);
        dbC.updateDb(MyConstants.DB_COPEX_EDP);
        if(cr.isError())
            return cr;
        // proc initial
        String queryInitProc = "SELECT ID_PROC FROM LINK_MISSION_PROC " +
                "WHERE ID_MISSION  = "+dbKeyMission+" AND ID_PROC IN (SELECT ID_PROC FROM INITIAL_PROC) ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("ID_PROC");

        cr = dbC.sendQuery(queryInitProc, listFields, v2);
        if (cr.isError()){
            return cr;
        }
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("ID_PROC");
            if (s == null)
                continue;
            long dbKeyProc = Long.parseLong(s);
            String queryProc = "INSERT INTO LINK_MISSION_PROC (ID_MISSION, ID_PROC, ID_USER) VALUES ("+dbKeyMission+", "+dbKeyProc+", "+dbKeyUser+") ;";
            v = new ArrayList();
            querys = new String[1];
            querys[0] = queryProc ;
            cr = dbC.executeQuery(querys, v);
            if(cr.isError())
                return cr;
        }
        return new CopexReturn();
    }

    /* chargement des options de la mission */
    public static CopexReturn getMissionOptionFromDB(DataBaseCommunication dbC, long dbKey, ArrayList v){
        String query = "SELECT ADD_PROC,  TRACE FROM OPTION_MISSION WHERE ID_MISSION = "+dbKey+" ;";
        dbC.updateDb(MyConstants.DB_COPEX);
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("ADD_PROC");
        listFields.add("TRACE");
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        dbC.updateDb(MyConstants.DB_COPEX_EDP);
        if(cr.isError())
            return cr;
        int nbR = v2.size();
        OptionMission options = null;
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("ADD_PROC");
            boolean addProc = s.equals("1");
            s = rs.getColumnData("TRACE");
            boolean trace = s.equals("1");
            options = new OptionMission(addProc,  trace);
        }
        v.add(options);
        return new CopexReturn();
    }
   
   
    
   
  
    
   
    
}
