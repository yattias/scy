/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.db;

import eu.scy.tools.dataProcessTool.common.DataOperation;
import eu.scy.tools.dataProcessTool.common.TypeOperation;
import eu.scy.tools.dataProcessTool.utilities.CopexReturn;
import eu.scy.tools.dataProcessTool.utilities.MyUtilities;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Gestion des types d'operations
 * @author Marjolaine Bodin
 */
public class OperationFromDB {
    
    /* chargement des types d'operations : liste en v[0] */
    public static CopexReturn getAllTypeOperation(DataBaseCommunication dbC, Locale locale, ArrayList v){
        ArrayList<TypeOperation> listOp = new ArrayList();
        TypeOperation[] tabTypeOp = null;
        int nbOp = 0;
        String query = "SELECT ID_TYPE_OPERATION, CODE, OPERATION_TYPE, COLOR_R, COLOR_G, COLOR_B FROM TYPE_OPERATION ;";
        
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("ID_TYPE_OPERATION");
        listFields.add("CODE");
        listFields.add("OPERATION_TYPE");
        listFields.add("COLOR_R");
        listFields.add("COLOR_G");
        listFields.add("COLOR_B");
        
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("ID_TYPE_OPERATION");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            String code = rs.getColumnData("CODE");
            if (code == null)
                continue;
            s = rs.getColumnData("OPERATION_TYPE");
            if (s == null)
                continue;
            int type = 0;
            try{
                type = Integer.parseInt(s);
            }catch(NumberFormatException e){
                
            }
            int colorR = 255;
            int colorG = 255;
            int colorB = 255;
            s = rs.getColumnData("COLOR_R");
            if (s == null)
                continue;
            try{
                colorR = Integer.parseInt(s);
            } catch(NumberFormatException e){
                
            }     
            s = rs.getColumnData("COLOR_G");
            if (s == null)
                continue;
            try{
                colorG = Integer.parseInt(s);
            } catch(NumberFormatException e){
                
            }
            s = rs.getColumnData("COLOR_B");
            if (s == null)
                continue;
            try{
                colorB = Integer.parseInt(s);
            } catch(NumberFormatException e){
                
            }
            TypeOperation op = new TypeOperation(dbKey, type, code, new Color(colorR, colorG, colorB));
            listOp.add(op);
            nbOp++;
        }
        // on met ca dans un tableau
        tabTypeOp = new TypeOperation[nbOp];
        for (int i=0; i<nbOp; i++){
            tabTypeOp[i] = listOp.get(i);
        }
        v.add(tabTypeOp);
        return new CopexReturn();
    }

    
    /*creation d'une nouvelle operation - retourne en v[0] le nouvel id*/
    public static CopexReturn createOperationInDB(DataBaseCommunication dbC, long dbKeyDs, DataOperation operation, ArrayList v){
        String name = operation.getName() ;
        name  = MyUtilities.replace("\'",name,"''") ;
        int isOnCol = 0;
        if (operation.isOnCol())
            isOnCol = 1;
        String query = "INSERT INTO DATA_OPERATION (ID_DATA_OPERATION, OP_NAME, IS_ON_COL) VALUES (NULL, '"+name+"', "+isOnCol+") ;";
        String queryID = "SELECT max(last_insert_id(`ID_DATA_OPERATION`))   FROM DATA_OPERATION ;";
        ArrayList v2 = new ArrayList();
        CopexReturn cr = dbC.getNewIdInsertInDB(query, queryID, v2);
        if (cr.isError())
            return cr;
        long dbKey = (Long)v2.get(0);

        // liens
        String query1 = "INSERT INTO LINK_DATASET_OPERATION (ID_DATASET, ID_DATA_OPERATION) VALUES ("+dbKeyDs+", "+dbKey+") ;";
        String query2 = "INSERT INTO LINK_OPERATION_TYPE (ID_DATA_OPERATION, ID_TYPE_OPERATION) VALUES ("+dbKey+", "+operation.getTypeOperation().getDbKey()+") ;";
        int nb = operation.getListNo().size();
        int nbQ = nb+2 ;
        String[] querys = new String[nbQ];
        int i=0;
        querys[i++] = query1;
        querys[i++] = query2;
        for (int k=0; k<nb; k++){
            String query3 = "INSERT INTO LIST_NO_OPERATION (ID_DATA_OPERATION, OPERATION_NO) VALUES ("+dbKey+", "+operation.getListNo().get(k)+"); ";
            querys[i++] = query3;
        }
        v2 = new ArrayList();
        cr = dbC.executeQuery(querys, v2);
        if(cr.isError())
            return cr;
        v.add(dbKey);
        return new CopexReturn();
    }

   
    /* mise a jour titre operation */
    public static CopexReturn updateOperationTitleInDB(DataBaseCommunication dbC, long dbKeyOp, String title){
        title = MyUtilities.replace("\'",title,"''") ;
        String query = "UPDATE DATA_OPERATION SET OP_NAME = '"+title+"' WHERE ID_DATA_OPERATION = "+dbKeyOp+" ;";
        String[] querys = new String[1];
        querys[0] = query;
        ArrayList v2 = new ArrayList();
        CopexReturn cr = dbC.executeQuery(querys, v2);
        return cr;
    }

    /* suppression d'une liste d'operations */
    public static CopexReturn deleteOperationFromDB(DataBaseCommunication dbC, ArrayList<DataOperation> listOperation){
        int nbOp = listOperation.size();
        if (nbOp == 0)
            return new CopexReturn();
        String listDbKey = ""+listOperation.get(0).getDbKey();
        for (int i=1; i<nbOp; i++){
            long dbKey = listOperation.get(i).getDbKey() ;
            listDbKey += ", "+dbKey;
        }
        String queryNo = "DELETE FROM LIST_NO_OPERATION WHERE ID_DATA_OPERATION IN ( "+listDbKey+" ); ";
        String queryType = "DELETE FROM LINK_OPERATION_TYPE WHERE ID_DATA_OPERATION IN ( "+listDbKey+" ); ";
        String queryLink = "DELETE FROM LINK_DATASET_OPERATION WHERE ID_DATA_OPERATION IN ( "+listDbKey+" ) ;";
        String queryParam = "DELETE FROM PARAM_OPERATION WHERE ID_OP_PARAM IN (SELECT ID_PARAM_OPERATION FROM LINK_OPERATION_PARAM WHERE ID_DATA_OPERATION IN ( "+listDbKey+" ));";
        String queryLinkParam = "DELETE FROM LINK_OPERATION_PARAM WHERE ID_DATA_OPERATION IN ( "+listDbKey+" );";
        String queryOp = "DELETE FROM DATA_OPERATION WHERE ID_DATA_OPERATION IN ( "+listDbKey+" ) ;";
        String[] querys = new String[6];
        querys[0] = queryNo;
        querys[1] = queryType;
        querys[2] = queryLink;
        querys[3] = queryParam ;
        querys[4] = queryLinkParam ;
        querys[5] = queryOp;
        ArrayList v2 = new ArrayList();
        CopexReturn cr = dbC.executeQuery(querys, v2);
        return cr;
    }

    /* suppression de numeros de colonnes pour des operations : liste de liste : long dbKeyOp, int no */
    public static CopexReturn deleteNoOperationFromDB(DataBaseCommunication dbC, ArrayList list){
        int nb = list.size();
        String[] querys = new String[nb];
        for (int i=0; i<nb; i++){
            long dbKeyOp = (Long)(((ArrayList)list.get(i)).get(0));
            int no = (Integer)(((ArrayList)list.get(i)).get(1));
            String query = "DELETE FROM LIST_NO_OPERATION WHERE ID_DATA_OPERATION = "+dbKeyOp+" AND OPERATION_NO = "+no+" ;";
            querys[i] = query;
            
        }
        ArrayList v2 = new ArrayList();
        CopexReturn cr = dbC.executeQuery(querys, v2);
        return cr;
    }

    /* mise a jour des no : on supprime tout et on recree tout */
    public static CopexReturn updateNoInDB(DataBaseCommunication dbC, ArrayList<DataOperation> listOperation){
        ArrayList v2 = new ArrayList();
        int nbOp = listOperation.size();
        String[] querys = new String[nbOp] ;
        for (int i=0; i<nbOp; i++){
            querys[i] = "DELETE FROM LIST_NO_OPERATION WHERE ID_DATA_OPERATION = "+listOperation.get(i).getDbKey()+" ;";
        }
        CopexReturn cr = dbC.executeQuery(querys, v2);
        if(cr.isError())
            return cr;
        ArrayList<String> listQ = new ArrayList();
        for (int i=0; i<nbOp; i++){
            int n = listOperation.get(i).getListNo().size() ;
            for (int j=0; j<n; j++){
                listQ.add("INSERT INTO LIST_NO_OPERATION (ID_DATA_OPERATION, OPERATION_NO) VALUES ("+listOperation.get(i).getDbKey()+", "+listOperation.get(i).getListNo().get(j)+") ;");
            }
        }
        int nb = listQ.size();
        querys = new String[nb];
        for (int i=0; i<nb; i++){
            querys[i] = listQ.get(i);
        }
        cr = dbC.executeQuery(querys, v2);
        return cr;
    }
}
