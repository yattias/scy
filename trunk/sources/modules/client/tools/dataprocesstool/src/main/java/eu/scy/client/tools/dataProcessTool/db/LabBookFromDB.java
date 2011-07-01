/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.db;

import eu.scy.client.tools.dataProcessTool.common.Mission;
import eu.scy.client.tools.dataProcessTool.synchro.Locker;
import eu.scy.client.tools.dataProcessTool.utilities.CopexReturn;
import eu.scy.client.tools.dataProcessTool.utilities.DataConstants;
import eu.scy.client.tools.dataProcessTool.utilities.MyUtilities;
import java.util.ArrayList;
import org.jdom.Element;

/**
 *
 * @author Marjolaine
 */
public class LabBookFromDB {

    /* load the dataset for a specified labDoc */
    public static CopexReturn getAllDatasetFromDB(DataBaseCommunication dbC, Locker locker, Mission mission, long dbKeyLabDoc, ArrayList v){
        dbC.updateDb(DataConstants.DB_LABBOOK);
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
                v.add(MyUtilities.stringToXml(s));
            }
        }
        return new CopexReturn();
    }

    /*save the dataset for the specified labdoc */
    public static CopexReturn setDatasetInDB(DataBaseCommunication dbC, long dbkeyLabDoc, Element dataset, String preview){
        String ds = MyUtilities.xmlToString(dataset);
        ds =  MyUtilities.replace("\'",ds,"''") ;
        preview =  MyUtilities.replace("\'",preview,"''") ;
        ArrayList v = new ArrayList();
        String[] querys = new String[1];
        String queryInsert = "UPDATE LABDOC SET LABDOC_DATA = '"+ds+"', PREVIEW = '"+preview+"' WHERE ID_LABDOC = "+dbkeyLabDoc+" ;" ;
        querys[0] = queryInsert;
        CopexReturn cr = dbC.executeQuery(querys, v)     ;
        return cr;
    }

}
