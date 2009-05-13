/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.db;

import eu.scy.tools.dataProcessTool.common.ToolUser;
import eu.scy.tools.dataProcessTool.utilities.CopexReturn;
import java.util.ArrayList;
/**
 * gestion des utilisateurs
 * TODO : gestion de bases separees
 * @author Marjolaine
 */
public class ToolUserFromDB {
    /*chargement de l'utilisateur : en v[0] : le user */
    public static CopexReturn loadDataUserFromDB(DataBaseCommunication dbC, long dbKeyUser, ArrayList v){
        ToolUser user = null;
        String query = "SELECT USER_NAME, FIRSTNAME " +
                "FROM COPEX_USER   " +
                "WHERE ID_USER =  "+dbKeyUser +"   ;";
        

        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("USER_NAME");
        listFields.add("FIRSTNAME");

        System.out.println("query user : "+query);
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String name = rs.getColumnData("USER_NAME");
            if (name == null)
                continue;
            String firstName = rs.getColumnData("FIRSTNAME");
            if (firstName == null)
                continue;
            user = new ToolUser(dbKeyUser, name, firstName);
        }

        v.add(user);
        return new CopexReturn();
    }

   
    
}
