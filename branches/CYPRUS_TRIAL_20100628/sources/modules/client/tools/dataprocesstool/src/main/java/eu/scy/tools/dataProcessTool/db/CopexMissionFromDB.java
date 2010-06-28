/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.db;

import eu.scy.tools.dataProcessTool.common.Mission;
import eu.scy.tools.dataProcessTool.utilities.CopexReturn;
import eu.scy.tools.dataProcessTool.utilities.DataConstants;
import java.util.ArrayList;

/**
 * manage mission in DB
 * @author Marjolaine
 */
public class CopexMissionFromDB {
    /*chargement de la mission : en v[0] : la mission */
    public static CopexReturn loadMissionFromDB(DataBaseCommunication dbC, long dbKeyMission, ArrayList v){
        dbC.updateDb(DataConstants.DB_LABBOOK);
        Mission mission = null;
        String query = "SELECT ID_MISSION, CODE, NAME, SUM_UP " +
                "FROM MISSION   " +
                "WHERE ID_MISSION = "+dbKeyMission +" ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("ID_MISSION");
        listFields.add("CODE");
        listFields.add("NAME");
        listFields.add("SUM_UP");
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
            String sumUp = rs.getColumnData("SUM_UP");
            mission = new Mission(dbKey,  mission_name,code,sumUp);
        }
        dbC.updateDb(DataConstants.DB_LABBOOK_FITEX);
        v.add(mission);
        return new CopexReturn();
    }

    public static CopexReturn getAllMissionFromDB(DataBaseCommunication dbC, long dbKeyUser, long dbKeyMission, ArrayList v){
        dbC.updateDb(DataConstants.DB_LABBOOK);
        ArrayList<Mission> listMission = new ArrayList();
        String query = "SELECT M.ID_MISSION, M.CODE, M.NAME, M.SUM_UP " +
                "FROM MISSION M, MISSION_CONF C " +
                "WHERE M.ID_MISSION != "+dbKeyMission +" AND  M.ID_MISSION = C.ID_MISSION " +
                "AND C.ID_LEARNER_GROUP IN (SELECT ID_LEARNER_GROUP FROM LINK_GROUP_LEARNER WHERE ID_LEARNER =  "+dbKeyUser+" );";

        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("M.ID_MISSION");
        listFields.add("M.CODE");
        listFields.add("M.NAME");
        listFields.add("M.SUM_UP");

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
            String description = rs.getColumnData("M.SUM_UP");
            if (description == null)
                continue;
            Mission m = new Mission(dbKey, mission_name,code,  description);

            listMission.add(m);
        }

        v.add(listMission);
        return new CopexReturn();
    }
}
