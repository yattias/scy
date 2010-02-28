/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.db;

import eu.scy.tools.dataProcessTool.common.Mission;
import eu.scy.tools.dataProcessTool.utilities.CopexReturn;
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
}
