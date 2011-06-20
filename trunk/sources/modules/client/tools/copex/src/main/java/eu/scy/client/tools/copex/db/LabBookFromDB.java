/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.db;

import eu.scy.client.tools.copex.synchro.Locker;
import eu.scy.client.tools.copex.utilities.CopexReturn;
import eu.scy.client.tools.copex.utilities.CopexUtilities;
import eu.scy.client.tools.copex.utilities.MyConstants;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.jdom.Element;

/**
 *
 * @author Marjolaine
 */
public class LabBookFromDB {
    /* load the experimental procedure for a specified labDoc */
    public static CopexReturn getAllExperimentalProcedureFromDB(DataBaseCommunication dbC, Locker locker, long dbKeyLabDoc, ArrayList v){
        dbC.updateDb(MyConstants.DB_LABBOOK);
        String query = "SELECT LABDOC_DATA FROM LABDOC WHERE ID_LABDOC = "+dbKeyLabDoc+" ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("DATA");
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError()){
            return cr;
        }
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("DATA");
            if (s != null && s.length() > 0 && !s.equals("null")&& !s.equals("NULL")){
                v.add(CopexUtilities.stringToXml(s));
            }
        }
        return new CopexReturn();
    }

    /*save the experimental procedure for the specified labdoc */
    public static CopexReturn setExperimentalProcedureInDB(DataBaseCommunication dbC, long dbkeyLabDoc, Element xproc, String preview){
        dbC.updateDb(MyConstants.DB_LABBOOK);
        String xp = CopexUtilities.xmlToString(xproc);
        xp =  CopexUtilities.replace("\'",xp,"''") ;
        preview =  CopexUtilities.replace("\'",preview,"''") ;
        ArrayList v = new ArrayList();
        String[] querys = new String[1];
        String queryInsert = "UPDATE LABDOC SET LABDOC_DATA = '"+xp+"', PREVIEW = '"+preview+"' WHERE ID_LABDOC = "+dbkeyLabDoc+" ;" ;
        querys[0] = queryInsert;
        CopexReturn cr = dbC.executeQuery(querys, v)     ;
        return cr;
    }

    /*insert the initial proc in db */
    public static CopexReturn saveInitialProcedureInDB(DataBaseCommunication dbC, long dbKeyMission, Element initProc){
        dbC.updateDb(MyConstants.DB_LABBOOK);
        String xp = CopexUtilities.xmlToString(initProc);
        xp =  CopexUtilities.replace("\'",xp,"''") ;
        ArrayList v = new ArrayList();
        String[] querys = new String[1];
        String queryInsert = "UPDATE MISSION SET COPEX_INIT = '"+xp+"' WHERE ID_MISSION = "+dbKeyMission+" ;";
        querys[0] = queryInsert;
        CopexReturn cr = dbC.executeQuery(querys, v)     ;
        return cr;
    }

    /* load the list of initial procedure for a specified mission */
    public static CopexReturn getInitialProcedureMissionFromDB(DataBaseCommunication dbC, long dbKeyMission, ArrayList v){
        dbC.updateDb(MyConstants.DB_LABBOOK);
        String query = "SELECT COPEX_INIT FROM MISSION M WHERE ID_MISSION = "+dbKeyMission+" ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("COPEX_INIT");
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError()){
            return cr;
        }
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("COPEX_INIT");
            if (s != null && s.length() > 0 && !s.equals("null")&& !s.equals("NULL")){
                v.add(CopexUtilities.stringToXml(s));
            }
        }
        return new CopexReturn();
    }
}
