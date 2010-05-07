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
 * Gestion des  des missions
 * @author Marjolaine
 */
public class CopexMissionFromDB {
    /*chargement de la mission : en v[0] : la mission */
    public static CopexReturn loadMissionFromDB(DataBaseCommunication dbC, long dbKeyMission, ArrayList v){
        Mission mission = null;
        String query = "SELECT CODE, MISSION_NAME, SUM_UP " +
                "FROM `COPEX_MISSION`  " +
                "WHERE ID_MISSION = "+dbKeyMission +"   ;";

        //System.out.println("query"+query);
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("CODE");
        listFields.add("MISSION_NAME");
        listFields.add("SUM_UP");

        //.out.println("envoi requete ");
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        
        //System.out.println("nb : "+nbR);
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String code = rs.getColumnData("CODE");
            if (code == null)
                continue;
            String mission_name = rs.getColumnData("MISSION_NAME");
            if (mission_name == null)
                continue;
            String sum_up = rs.getColumnData("SUM_UP");
            if (sum_up == null)
                continue;

            mission = new Mission(dbKeyMission, code, mission_name, sum_up);
        }

        v.add(mission);
        return new CopexReturn();
    }

    public static CopexReturn getAllMissionFromDB(DataBaseCommunication dbC, long dbKeyUser, long dbKeyMission, ArrayList v){
        dbC.updateDb(DataConstants.DB_COPEX);
        ArrayList<Mission> listMission = new ArrayList();
        String query = "SELECT M.ID_MISSION, M.CODE, M.MISSION_NAME, M.SUM_UP " +
                "FROM COPEX_MISSION M, LINK_MISSION_LEARNER L " +
                "WHERE M.ID_MISSION != "+dbKeyMission +" AND  M.ID_MISSION = L.ID_MISSION " +
                "AND L.ID_LEARNER = "+dbKeyUser+" ;";

        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("M.ID_MISSION");
        listFields.add("M.CODE");
        listFields.add("M.MISSION_NAME");
        listFields.add("M.SUM_UP");

        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError()){
            dbC.updateDb(DataConstants.DB_COPEX_DATA);
            return cr;
        }
        dbC.updateDb(DataConstants.DB_COPEX_DATA);
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
            Mission m = new Mission(dbKey, code, mission_name, description);
            listMission.add(m);
        }

        v.add(listMission);
        return new CopexReturn();
    }
}
