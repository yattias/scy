/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.copexDataTool.db;

import com.scy.scyDataTool.common.TypeVisualization;
import com.scy.scyDataTool.common.Visualization;
import com.scy.scyDataTool.utilities.CopexReturn;
import com.scy.scyDataTool.utilities.ScyUtilities;
import java.awt.Color;
import java.util.ArrayList;

/**
 * gestion de la visualisation des données dans la base
 * @author Marjolaine
 */
public class VisualizationFromDB {
    /* chargement des types de visualization : liste en v[0] */
    public static CopexReturn getAllTypeVisualizationFromDB(DataBaseCommunication dbC, ArrayList v){
        ArrayList<TypeVisualization> listVis = new ArrayList();
        TypeVisualization[] tabTypeVis = null;
        int nbVis = 0;
        String query = "SELECT ID_TYPE_VISUALIZATION, CODE, TYPE, NB_COL_PARAM FROM TYPE_VISUALIZATION ;";

        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("ID_TYPE_VISUALIZATION");
        listFields.add("CODE");
        listFields.add("TYPE");
        listFields.add("NB_COL_PARAM");

        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("ID_TYPE_VISUALIZATION");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            String code = rs.getColumnData("CODE");
            if (code == null)
                continue;
            s = rs.getColumnData("TYPE");
            if (s == null)
                continue;
            int type = 0;
            try{
                type = Integer.parseInt(s);
            }catch(NumberFormatException e){

            }
            int nbColParam = 1;
            s = rs.getColumnData("NB_COL_PARAM");
            if (s == null)
                continue;
            try{
                nbColParam = Integer.parseInt(s);
            } catch(NumberFormatException e){

            }
            
            TypeVisualization vis = new TypeVisualization(dbKey, type, code, nbColParam);
            listVis.add(vis);
            nbVis++;
        }
        // on met ça dans un tableau
        tabTypeVis = new TypeVisualization[nbVis];
        for (int i=0; i<nbVis; i++){
            tabTypeVis[i] = listVis.get(i);
        }
        v.add(tabTypeVis);
        return new CopexReturn();
    }

     /* mise à jour titre visualization */
    public static CopexReturn updateVisualizationTitleInDB(DataBaseCommunication dbC, long dbKeyVis, String title){
        title = ScyUtilities.replace("\'",title,"''") ;
        String query = "UPDATE DATA_VISUALIZATION SET VIS_NAME = '"+title+"' WHERE ID_DATA_VISUALIZATION = "+dbKeyVis+" ;";
        String[] querys = new String[1];
        querys[0] = query;
        ArrayList v2 = new ArrayList();
        CopexReturn cr = dbC.executeQuery(querys, v2);
        return cr;
    }
    
    /*creation d'une nouvelle visualisation - retourne en v[0] le nouvel id*/
    public static CopexReturn createVisualizationInDB(DataBaseCommunication dbC, long dbKeyDs, Visualization visualization, ArrayList v){
        String name = visualization.getName() ;
        name  = ScyUtilities.replace("\'",name,"''") ;
       String query = "INSERT INTO DATA_VISUALIZATION (ID_DATA_VISUALIZATION, VIS_NAME) VALUES (NULL, '"+name+"') ;";
        String queryID = "SELECT max(last_insert_id(`ID_DATA_VISUALIZATION`))   FROM DATA_VISUALIZATION ;";
        ArrayList v2 = new ArrayList();
        CopexReturn cr = dbC.getNewIdInsertInDB(query, queryID, v2);
        if (cr.isError())
            return cr;
        long dbKey = (Long)v2.get(0);

        // liens
        String query1 = "INSERT INTO LINK_DATASET_VISUALIZATION (ID_DATASET, ID_DATA_VISUALIZATION) VALUES ("+dbKeyDs+", "+dbKey+") ;";
        String query2 = "INSERT INTO LINK_VISUALIZATION_TYPE (ID_DATA_VISUALIZATION, ID_TYPE_VISUALIZATION) VALUES ("+dbKey+", "+visualization.getType().getDbKey()+") ;";
        int nb = visualization.getTabNo().length ;
        int nbQ = nb+2 ;
        String[] querys = new String[nbQ];
        int i=0;
        querys[i++] = query1;
        querys[i++] = query2;
        for (int k=0; k<nb; k++){
            String query3 = "INSERT INTO LIST_NO_VISUALIZATION (ID_DATA_VISUALIZATION, NO) VALUES ("+dbKey+", "+visualization.getTabNo()[k]+"); ";
            querys[i++] = query3;
        }
        // graphe ?
        v2 = new ArrayList();
        cr = dbC.executeQuery(querys, v2);
        v.add(dbKey);
        return cr;
    }

    /* suppression d'une visualization en base  */
    public static CopexReturn deleteVisualizationFromDB(DataBaseCommunication dbC, long dbKeyVis){
        ArrayList v = new ArrayList();
        String[] querys = new String[7];
        // suppression des visualisations associées
        String queryDelVisType = "DELETE FROM LINK_VISUALIZATION_TYPE WHERE ID_DATA_VISUALIZATION = "+dbKeyVis+" ;";
        String queryDelVisNo = "DELETE FROM LIST_NO_VISUALIZATION WHERE ID_DATA_VISUALIZATION = "+dbKeyVis+" ;";
        String queryDelParamGraph = "DELETE FROM PARAM_VIS_GRAPH WHERE ID_DATA_VISUALIZATION = "+dbKeyVis+" ;";
        String queryDelFunction = "DELETE FROM FUNCTION_MODEL WHERE ID_FUNCTION_MODEL IN (SELECT ID_FUNCTION_MODEL FROM LINK_GRAPH_FUNCTION_MODEL WHERE ID_DATA_VISUALIZATION = "+dbKeyVis+");";
        String queryDelFunctionLink  = "DELETE FROM LINK_GRAPH_FUNCTION_MODEL WHERE ID_DATA_VISUALIZATION = "+dbKeyVis+";";
        String queryDelVis = "DELETE FROM DATA_VISUALIZATION WHERE ID_DATA_VISUALIZATION  = "+dbKeyVis+" ;";
        String queryDelLinkVis = "DELETE FROM LINK_DATASET_VISUALIZATION WHERE ID_DATA_VISUALIZATION = "+dbKeyVis+" ;";

        // bloc de requete
        int i=0;
        querys[i++] = queryDelVisType;
        querys[i++] = queryDelVisNo ;
        querys[i++] = queryDelParamGraph ;
        querys[i++] = queryDelFunction;
        querys[i++] = queryDelFunctionLink;
        querys[i++] = queryDelVis ;
        querys[i++] = queryDelLinkVis ;

        CopexReturn cr = dbC.executeQuery(querys, v);
        return cr;
    }

    /* ajout d'une fonction modele, en v[0] le nouvel id  */
    public static CopexReturn createFunctionModelInDB(DataBaseCommunication dbC, long dbKeyGraph, String description, Color fColor, ArrayList v){
        String desc  = ScyUtilities.replace("\'",description,"''") ;
        String query = "INSERT INTO FUNCTION_MODEL (ID_FUNCTION_MODEL, DESCRIPTION, COLOR_R, COLOR_G, COLOR_B) VALUES (NULL, '"+desc+"',"+fColor.getRed()+" , "+fColor.getGreen()+","+fColor.getBlue()+" ) ;";
        String queryID = "SELECT max(last_insert_id(`ID_FUNCTION_MODEL`))   FROM FUNCTION_MODEL ;";
        ArrayList v2 = new ArrayList();
        CopexReturn cr = dbC.getNewIdInsertInDB(query, queryID, v2);
        if (cr.isError())
            return cr;
        long dbKey = (Long)v2.get(0);

        // lien
        String queryLink = "INSERT INTO LINK_GRAPH_FUNCTION_MODEL (ID_DATA_VISUALIZATION, ID_FUNCTION_MODEL) VALUES ("+dbKeyGraph+", "+dbKey+") ;";
        String[] querys = new String[1];
        querys[0] = queryLink;
        v2 = new ArrayList();
        cr = dbC.executeQuery(querys, v2);
        v.add(dbKey);
        return cr;
    }

    /* suppression d'une fonction modele */
    public static CopexReturn deleteFunctionModelFromDB(DataBaseCommunication dbC, long dbKey){
        String queryLink = "DELETE FROM LINK_GRAPH_FUNCTION_MODEL WHERE ID_FUNCTION_MODEL = "+dbKey;
        String queryDel = "DELETE FROM FUNCTION_MODEL WHERE ID_FUNCTION_MODEL = "+dbKey;
        String[] querys = new String[2];
        querys[0] = queryLink ;
        querys[1] = queryDel  ;
        ArrayList v = new ArrayList();
        CopexReturn cr = dbC.executeQuery(querys, v);
        return cr;
    }

     /* mise à jour d'une fonction modele */
    public static CopexReturn updateFunctionModelInDB(DataBaseCommunication dbC, long dbKey, String description){
        String desc  = ScyUtilities.replace("\'",description,"''") ;
        String query = "UPDATE FUNCTION_MODEL SET DESCRIPTION = '"+desc+"' WHERE ID_FUNCTION_MODEL = "+dbKey+" ;";
        String[] querys = new String[1];
        querys[0] = query ;
        ArrayList v = new ArrayList();
        CopexReturn cr = dbC.executeQuery(querys, v);
        return cr;
    }

    /* suppression de plusieurs visualizations */
    public static CopexReturn deleteVisualizationFromDB(DataBaseCommunication dbC, ArrayList<Visualization> listVis){
        int nbVis = listVis.size();
        for (int i=0; i<nbVis; i++){
            CopexReturn cr = deleteVisualizationFromDB(dbC, listVis.get(i).getDbKey());
            if (cr.isError())
                return cr;
        }
        return new CopexReturn();
    }

    /* mise à jour des no : on supprime tout et on recréé tout */
    public static CopexReturn updateNoInDB(DataBaseCommunication dbC, ArrayList<Visualization> listVisualization){
        ArrayList v2 = new ArrayList();
        int nbVis= listVisualization.size();
        String[] querys = new String[nbVis] ;
        for (int i=0; i<nbVis; i++){
            querys[i] = "DELETE FROM LIST_NO_VISUALIZATION WHERE ID_DATA_VISUALIZATION = "+listVisualization.get(i).getDbKey()+" ;";
        }
        CopexReturn cr = dbC.executeQuery(querys, v2);
        if(cr.isError())
            return cr;
        ArrayList<String> listQ = new ArrayList();
        for (int i=0; i<nbVis; i++){
            int n = listVisualization.get(i).getTabNo().length ;
            for (int j=0; j<n; j++){
                listQ.add("INSERT INTO LIST_NO_VISUALIZATION (ID_DATA_VISUALIZATION, NO) VALUES ("+listVisualization.get(i).getDbKey()+", "+listVisualization.get(i).getTabNo()[j]+") ;");
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
