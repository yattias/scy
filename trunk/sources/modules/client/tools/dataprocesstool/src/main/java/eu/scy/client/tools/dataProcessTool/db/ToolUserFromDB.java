/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.db;

import eu.scy.client.tools.dataProcessTool.common.Group;
import eu.scy.client.tools.dataProcessTool.common.ToolUser;
import eu.scy.client.tools.dataProcessTool.utilities.CopexReturn;
import eu.scy.client.tools.dataProcessTool.utilities.DataConstants;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
/**
 * user in labbook
 * @author Marjolaine
 */
public class ToolUserFromDB {
    /** load the users of the group */
    public static CopexReturn loadGroupFromDB(DataBaseCommunication dbC, long dbKeyGroup, ArrayList v){
        dbC.updateDb(DataConstants.DB_LABBOOK);
        Group group = null;
        List<ToolUser> list = new LinkedList();
        String query = "SELECT U.ID_LB_USER, U.USER_NAME, U.FIRST_NAME FROM LB_USER U, LINK_GROUP_LEARNER G " +
                "WHERE G.ID_LEARNER_GROUP =  "+dbKeyGroup+" AND G.ID_LEARNER = U.ID_LB_USER ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("U.ID_LB_USER");
        listFields.add("U.USER_NAME");
        listFields.add("U.FIRST_NAME");

        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError()){
            dbC.updateDb(DataConstants.DB_LABBOOK_FITEX);
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

             ToolUser l = new ToolUser(dbKeyUser, name, firstName);
             list.add(l);
        }

        group = new Group(dbKeyGroup, list);
        dbC.updateDb(DataConstants.DB_LABBOOK_FITEX);
        v.add(group);
        return new CopexReturn();
    }

   
    
}
