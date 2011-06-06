/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.db;

import eu.scy.client.tools.copex.common.*;
import eu.scy.client.tools.copex.utilities.CopexReturn;
import eu.scy.client.tools.copex.utilities.MyConstants;
import java.util.ArrayList;

/**
 * mission and labdoc status in the db labbook
 * @author Marjolaine
 */
public class MissionFromDB {
    
    
     public static CopexReturn getMissionFromDB(DataBaseCommunication dbC, long dbKeyMission, ArrayList v) {
        CopexMission m = null;
        String query = "SELECT ID_MISSION, CODE, NAME, DESCRIPTION " +
                "FROM MISSION   " +
                "WHERE ID_MISSION = "+dbKeyMission +" ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("ID_MISSION");
        listFields.add("CODE");
        listFields.add("NAME");
        listFields.add("DESCRIPTION");
        dbC.updateDb(MyConstants.DB_LABBOOK);
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError()){
            dbC.updateDb(MyConstants.DB_LABBOOK_COPEX);
            return cr;
        }
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("ID_MISSION");
            long dbKey = Long.parseLong(s);
            String code = rs.getColumnData("CODE");
            String mission_name = rs.getColumnData("NAME");
            String desc = rs.getColumnData("DESCRIPTION");
            m = new CopexMission(dbKey,  mission_name,code,desc);
        }
       v.add(m);
       dbC.updateDb(MyConstants.DB_LABBOOK_COPEX);
       return new CopexReturn();     
        
    }
     
   
    
   
    /* chargement de toutes les missions  de l'utilisateur sauf idMission*/
    public static CopexReturn getAllMissionsFromDB(DataBaseCommunication dbC, long dbKeyUser, long dbKeyMission,  ArrayList v) {
        dbC.updateDb(MyConstants.DB_LABBOOK);
        ArrayList<CopexMission> listMission = new ArrayList();
        String query = "SELECT M.ID_MISSION, M.CODE, M.NAME, M.DESCRIPTION " +
                "FROM MISSION M, MISSION_CONF C " +
                "WHERE M.ID_MISSION != "+dbKeyMission +" AND  M.ID_MISSION = C.ID_MISSION " +
                "AND C.ID_LEARNER_GROUP IN (SELECT ID_LEARNER_GROUP FROM LINK_GROUP_LEARNER WHERE ID_LEARNER =  "+dbKeyUser+" );";
       
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("M.ID_MISSION");
        listFields.add("M.CODE");
        listFields.add("M.NAME");
        listFields.add("M.DESCRIPTION");
        
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError()){
            dbC.updateDb(MyConstants.DB_LABBOOK_COPEX);
            return cr;
        }
        dbC.updateDb(MyConstants.DB_LABBOOK_COPEX);
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
            String mission_name = rs.getColumnData("M.NAME");
            if (mission_name == null)
                continue;
            String description = rs.getColumnData("M.DESCRIPTION");
            if (description == null)
                continue;
            CopexMission m = new CopexMission(dbKey, mission_name,code,  description);
            
            listMission.add(m);
        }
        
        v.add(listMission);
        return new CopexReturn();
    }

    /* unset the locker for the specified labdoc */
    public static CopexReturn unsetLabdocLockerInDB(DataBaseCommunication dbC, long dbKeyLabdoc){
        dbC.updateDb(MyConstants.DB_LABBOOK);
        String query = "DELETE FROM LABDOC_STATUS WHERE ID_LABDOC = "+dbKeyLabdoc+" AND LABDOC_STATUS = '"+MyConstants.LABDOC_STATUS_LOCK+"' ;";
        CopexReturn cr = dbC.executeQuery(query);
        dbC.updateDb(MyConstants.DB_LABBOOK_COPEX);
        return cr;
    }

    /** update the labdoc status */
    public static CopexReturn updateLabdocStatusInDB(DataBaseCommunication dbC, long dbKeyLabdoc, long dbKeyUser, long dbKeyGroup){
        dbC.updateDb(MyConstants.DB_LABBOOK);
        String query = "SELECT ID_LEARNER FROM LINK_GROUP_LEARNER "
                + "WHERE ID_LEARNER_GROUP = "+dbKeyGroup+" AND ID_LEARNER != "+dbKeyUser+";";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("ID_LEARNER");

        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError()){
            dbC.updateDb(MyConstants.DB_LABBOOK_COPEX);
            return cr;
        }
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("ID_LEARNER");
            long idLearner = Long.parseLong(s);
            if (s == null)
                continue;
            String querySt = "SELECT ID_LABDOC FROM LABDOC_STATUS "
                    + "WHERE ID_LABDOC = "+dbKeyLabdoc+" AND ID_LB_USER = "+idLearner+" AND LABDOC_STATUS != '"+MyConstants.LABDOC_STATUS_LOCK+"';";
            ArrayList v3 = new ArrayList();
            ArrayList<String> listFields2 = new ArrayList();
            listFields2.add("ID_LABDOC");
            cr = dbC.sendQuery(querySt, listFields2, v3);
            if (cr.isError()){
                dbC.updateDb(MyConstants.DB_LABBOOK_COPEX);
                return cr;
            }
            int nbR3 = v3.size();
            int nbSt = 0;
            for (int j=0; j<nbR3; j++){
                nbSt++;
            }
            if(nbSt == 0){
                String queryInsert = "INSERT INTO LABDOC_STATUS (ID_LABDOC, ID_LB_USER, LABDOC_STATUS) "
                            + "VALUES ("+dbKeyLabdoc+", "+idLearner+", '"+MyConstants.LABDOC_STATUS_UPDATE+"')";
                cr = dbC.executeQuery(queryInsert);
                if (cr.isError()){
                    dbC.updateDb(MyConstants.DB_LABBOOK_COPEX);
                    return cr;
                }
            }
        }
        dbC.updateDb(MyConstants.DB_LABBOOK_COPEX);
        return new CopexReturn();
    }
}
