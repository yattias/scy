/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.db;

import eu.scy.client.tools.dataProcessTool.common.Mission;
import eu.scy.client.tools.dataProcessTool.utilities.CopexReturn;
import eu.scy.client.tools.dataProcessTool.utilities.DataConstants;
import java.util.ArrayList;

/**
 * manage mission in DB labbook
 * @author Marjolaine
 */
public class CopexMissionFromDB {
    /*load mission : en v[0] : mission */
    public static CopexReturn loadMissionFromDB(DataBaseCommunication dbC, long dbKeyMission, ArrayList v){
        dbC.updateDb(DataConstants.DB_LABBOOK);
        Mission mission = null;
        String query = "SELECT ID_MISSION, CODE, NAME, DESCRIPTION " +
                "FROM MISSION   " +
                "WHERE ID_MISSION = "+dbKeyMission +" ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("ID_MISSION");
        listFields.add("CODE");
        listFields.add("NAME");
        listFields.add("DESCRIPTION");
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError()){
            dbC.updateDb(DataConstants.DB_LABBOOK_FITEX);
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
            mission = new Mission(dbKey,  mission_name,code,desc);
        }
        dbC.updateDb(DataConstants.DB_LABBOOK_FITEX);
        v.add(mission);
        return new CopexReturn();
    }

    public static CopexReturn getAllMissionFromDB(DataBaseCommunication dbC, long dbKeyUser, long dbKeyMission, ArrayList v){
        dbC.updateDb(DataConstants.DB_LABBOOK);
        ArrayList<Mission> listMission = new ArrayList();
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
            dbC.updateDb(DataConstants.DB_LABBOOK_FITEX);
            return cr;
        }
        dbC.updateDb(DataConstants.DB_LABBOOK_FITEX);
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
            Mission m = new Mission(dbKey, mission_name,code,  description);

            listMission.add(m);
        }

        v.add(listMission);
        return new CopexReturn();
    }

    /* unset the locker for the specified labdoc */
    public static CopexReturn unsetLabdocLockerInDB(DataBaseCommunication dbC, long dbKeyLabdoc){
        dbC.updateDb(DataConstants.DB_LABBOOK);
        String query = "DELETE FROM LABDOC_STATUS WHERE ID_LABDOC = "+dbKeyLabdoc+" AND LABDOC_STATUS = '"+DataConstants.LABDOC_STATUS_LOCK+"' ;";
        CopexReturn cr = dbC.executeQuery(query);
        dbC.updateDb(DataConstants.DB_LABBOOK_FITEX);
        return cr;
    }

    /** update the labdoc status */
    public static CopexReturn updateLabdocStatusInDB(DataBaseCommunication dbC, long dbKeyLabdoc, long dbKeyUser, long dbKeyGroup){
        dbC.updateDb(DataConstants.DB_LABBOOK);
        String query = "SELECT ID_LEARNER FROM LINK_GROUP_LEARNER "
                + "WHERE ID_LEARNER_GROUP = "+dbKeyGroup+" AND ID_LEARNER != "+dbKeyUser+";";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("ID_LEARNER");

        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError()){
            dbC.updateDb(DataConstants.DB_LABBOOK_FITEX);
            return cr;
        }
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("ID_LEARNER");
            long idLearner = -1;
            try{
                idLearner = Long.parseLong(s);
            }catch(NumberFormatException e){
                
            }
            if (s == null)
                continue;
            String querySt = "SELECT ID_LABDOC FROM LABDOC_STATUS "
                    + "WHERE ID_LABDOC = "+dbKeyLabdoc+" AND ID_LB_USER = "+idLearner+" AND LABDOC_STATUS != '"+DataConstants.LABDOC_STATUS_LOCK+"';";
            ArrayList v3 = new ArrayList();
            ArrayList<String> listFields2 = new ArrayList();
            listFields2.add("ID_LABDOC");
            cr = dbC.sendQuery(querySt, listFields2, v3);
            if (cr.isError()){
                dbC.updateDb(DataConstants.DB_LABBOOK_FITEX);
                return cr;
            }
            int nbR3 = v3.size();
            int nbSt = 0;
            for (int j=0; j<nbR3; j++){
                nbSt++;
            }
            if(nbSt == 0){
                String queryInsert = "INSERT INTO LABDOC_STATUS (ID_LABDOC, ID_LB_USER, LABDOC_STATUS) "
                            + "VALUES ("+dbKeyLabdoc+", "+idLearner+", '"+DataConstants.LABDOC_STATUS_UPDATE+"')";
                cr = dbC.executeQuery(queryInsert);
                if (cr.isError()){
                    dbC.updateDb(DataConstants.DB_LABBOOK_FITEX);
                    return cr;
                }
            }
        }
        dbC.updateDb(DataConstants.DB_LABBOOK_FITEX);
        return new CopexReturn();
    }
}
