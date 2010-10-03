/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.db;



import eu.scy.client.tools.copex.common.CopexGroup;
import eu.scy.client.tools.copex.common.CopexLearner;
import eu.scy.client.tools.copex.utilities.CopexReturn;
import eu.scy.client.tools.copex.utilities.MyConstants;
import java.util.*;

/**
 * gestion des utilisateurs
 * @author MBO
 */
public class UserFromDB {
    
    public static CopexReturn getGroupFromDB(DataBaseCommunication dbC, long dbKeyGroup, ArrayList v) {
        dbC.updateDb(MyConstants.DB_LABBOOK);
        CopexGroup group = null;
        List<CopexLearner> list = new LinkedList();
        String query = "SELECT U.ID_LB_USER, U.USER_NAME, U.FIRST_NAME FROM LB_USER U, LINK_GROUP_LEARNER G " +
                "WHERE G.ID_LEARNER_GROUP =  "+dbKeyGroup+" AND G.ID_LEARNER = U.ID_LB_USER ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("U.ID_LB_USER");
        listFields.add("U.USER_NAME");
        listFields.add("U.FIRST_NAME");
        
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError()){
            dbC.updateDb(MyConstants.DB_LABBOOK_COPEX);
            return cr;
        }
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("U.ID_LB_USER");
            long dbKeyUser = Long.parseLong(s);
            String name = rs.getColumnData("U.USER_NAME");
            if (name == null)
                continue;
            String firstName = rs.getColumnData("U.FIRST_NAME");
            if (firstName == null)
                continue;
            
             CopexLearner l = new CopexLearner(dbKeyUser, name, firstName);
             list.add(l);
        }
        group = new CopexGroup(dbKeyGroup, list);
        dbC.updateDb(MyConstants.DB_LABBOOK_COPEX);
        v.add(group);
        return new CopexReturn();     
        
    }
}
