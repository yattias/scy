/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.db;

import eu.scy.tools.dataProcessTool.common.*;
import java.util.ArrayList;
import eu.scy.tools.dataProcessTool.utilities.CopexReturn;
import eu.scy.tools.dataProcessTool.utilities.DataConstants;
import eu.scy.tools.dataProcessTool.utilities.MyUtilities;
import java.awt.Color;

/**
 * Gestion en bd des dataset
 * @author Marjolaine
 */
public class DatasetFromDB {

    /* chargement de tous les ds lie à un utilisateur et à une mission donnée */
    public static CopexReturn getAllDatasetFromDB(DataBaseCommunication dbC, long dbKeyUser, Mission mission, TypeOperation[] tabTypeOp,TypeVisualization[] tabTypeVis, ArrayList v){
        long dbKeyMission = mission.getDbKey();
        ArrayList<Dataset> listDataset = new ArrayList();
        String query = "SELECT D.ID_DATASET, D.DATASET_NAME, D.NB_COL, D.NB_ROW " +
                " FROM DATASET D, LINK_DATASET_MISSION_USER  L WHERE " +
                "L.ID_DATASET = D.ID_DATASET AND L.ID_MISSION = "+dbKeyMission+" AND L.ID_USER = "+dbKeyUser+" ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("D.ID_DATASET");
        listFields.add("D.DATASET_NAME");
        listFields.add("D.NB_COL");
        listFields.add("D.NB_ROW");

        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("D.ID_DATASET");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            String name = rs.getColumnData("D.DATASET_NAME");
            if (name == null)
                continue;
            s = rs.getColumnData("D.NB_COL");
            if (s == null)
                continue;
            int nbCol = 0;
            try{
                nbCol = Integer.parseInt(s);
            }catch(NumberFormatException e){
                System.out.println(e);
            }
            s = rs.getColumnData("D.NB_ROW");
            if (s == null)
                continue;
            int nbRow = 0;
            try{
                nbRow = Integer.parseInt(s);
            }catch(NumberFormatException e){
                System.out.println(e);
            }
            // chargement header
            ArrayList v3 = new ArrayList();
            cr = getAllDatasetHeaderFromDB(dbC, dbKey,nbCol,  v3);
            if (cr.isError())
                return cr;
            DataHeader[] tabDataHeader = (DataHeader[])v3.get(0);
            // chargement des data
            v3 = new ArrayList();
            cr = getAllDatasetDataFromDB(dbC, dbKey,nbRow, nbCol, v3);
            if (cr.isError())
                return cr;
            Data[][] data = (Data[][])v3.get(0);
            // chargement des operations
            v3 = new ArrayList();
            cr = getAllDatasetOperationFromDB(dbC, dbKey,tabTypeOp,  v3);
            if (cr.isError())
                return cr;
            ArrayList<DataOperation> listOp = (ArrayList<DataOperation>)v3.get(0);
            // chargement des visualizations
            v3 = new ArrayList();
            cr = getAllDatasetVisualizationFromDB(dbC, dbKey,tabTypeVis,  v3);
            if (cr.isError())
                return cr;
            ArrayList<Visualization> listVis = (ArrayList<Visualization>)v3.get(0);
            //creation du dataset
            Dataset ds= new Dataset(dbKey, name, nbCol, nbRow, tabDataHeader, data, listOp, listVis);
            listDataset.add(ds);
        }
        v.add(listDataset);
        return new CopexReturn();
    }

    /* chargement de tous les ds header d'un dataset donné */
    public static CopexReturn getAllDatasetHeaderFromDB(DataBaseCommunication dbC, long dbKeyDs,  int nbCol, ArrayList v){
        DataHeader[] tabHeader = new DataHeader[nbCol] ;
        String query = "SELECT D.ID_HEADER, D.VALUE, D.NO_COL " +
                "FROM DATA_HEADER D, LINK_DATASET_HEADER L " +
                "WHERE D.ID_HEADER = L.ID_HEADER AND L.ID_DATASET = "+dbKeyDs+" ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("D.ID_HEADER");
        listFields.add("D.VALUE");
        listFields.add("D.NO_COL");

        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("D.ID_HEADER");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            String value = rs.getColumnData("D.VALUE");
            if (value == null)
                continue;
            s = rs.getColumnData("D.NO_COL");
            if (s == null)
                continue;
            int noCol = 0;
            try{
                noCol = Integer.parseInt(s);
            }catch(NumberFormatException e){
                System.out.println(e);
            }
            DataHeader dh= new DataHeader(dbKey, value, noCol);
            tabHeader[noCol] = dh;
        }
        v.add(tabHeader);
        return new CopexReturn();
    }

    /* chargement de tous les operation d'un dataset donné */
    public static CopexReturn getAllDatasetOperationFromDB(DataBaseCommunication dbC, long dbKeyDs,  TypeOperation[] tabTypeOp, ArrayList v){
        ArrayList<DataOperation> listDataOp = new ArrayList();
        String query = "SELECT D.ID_DATA_OPERATION, D.OP_NAME, D.IS_ON_COL, T.ID_TYPE_OPERATION " +
                "FROM DATA_OPERATION D, LINK_DATASET_OPERATION L, LINK_OPERATION_TYPE T " +
                "WHERE D.ID_DATA_OPERATION = L.ID_DATA_OPERATION AND L.ID_DATASET = "+dbKeyDs+"  AND D.ID_DATA_OPERATION = T.ID_DATA_OPERATION;  ";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("D.ID_DATA_OPERATION");
        listFields.add("D.OP_NAME");
        listFields.add("D.IS_ON_COL");
        listFields.add("T.ID_TYPE_OPERATION");

        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("D.ID_DATA_OPERATION");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            String name = rs.getColumnData("D.OP_NAME");
            if (name == null)
                continue;
            s = rs.getColumnData("D.IS_ON_COL");
            if (s == null)
                continue;
            int n = 0;
            try{
                n = Integer.parseInt(s);
            }catch(NumberFormatException e){
                System.out.println(e);
            }
            boolean isOnCol = n==1;
            s = rs.getColumnData("T.ID_TYPE_OPERATION");
            if (s == null)
                continue;
            long dbKeyType = Long.parseLong(s);
            // recherche du type Operation
            TypeOperation typeOperation = null;
            for (int j=0; j<tabTypeOp.length; j++){
                if (tabTypeOp[j].getDbKey() == dbKeyType){
                    typeOperation = tabTypeOp[j];
                    break;
                }
            }
            if (typeOperation == null)
                return new CopexReturn("ERROR TYPE OPERATION", false);
            // list des numeros de col/row surlesquells s'applique l'operation
            ArrayList v3 = new ArrayList();
            cr = getAllNoOperation(dbC, dbKey, v3);
            if (cr.isError())
                return cr;
            ArrayList<Integer> listNo = (ArrayList<Integer>)v3.get(0);

            // creation objet dataOperation
            DataOperation op = new DataOperation(dbKey, name, typeOperation, isOnCol, listNo );
            listDataOp.add(op);
        }
        
        v.add(listDataOp);
        return new CopexReturn();
    }

    private static CopexReturn getAllNoOperation(DataBaseCommunication dbC, long dbKey, ArrayList v){
            // list des numeros de col/row surlesquells s'applique l'operation
            ArrayList<Integer> listNo = new ArrayList();
            String query = "SELECT NO FROM LIST_NO_OPERATION WHERE ID_DATA_OPERATION = "+dbKey+" ;";
            ArrayList v2 = new ArrayList();
            ArrayList<String> listFields = new ArrayList();
            listFields.add("NO");
            CopexReturn cr = dbC.sendQuery(query, listFields, v2);
            if (cr.isError())
                return cr;
            int nbR = v2.size();
            for (int i=0; i<nbR; i++){
                ResultSetXML rs = (ResultSetXML)v2.get(i);
                String s = rs.getColumnData("NO");
                if (s == null)
                    continue;
                int no = 0;
                try{
                    no = Integer.parseInt(s);
                }catch(NumberFormatException e){
                    System.out.println(e);
                }
                listNo.add(no);
            }
            v.add(listNo);
            return new CopexReturn();
    }
    /* chargement des données d'un data set */
    public static CopexReturn getAllDatasetDataFromDB(DataBaseCommunication dbC, long dbKeyDs,  int nbRows, int nbCol, ArrayList v){
        Data[][] tabData = new Data[nbRows][nbCol] ;
        String query = "SELECT D.ID_DATA, D.VALUE, D.NO_COL, D.NO_ROW, D.IS_IGNORED " +
                "FROM COPEX_DATA D, LINK_DATASET_DATA L " +
                "WHERE D.ID_DATA = L.ID_DATA AND L.ID_DATASET = "+dbKeyDs+" ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("D.ID_DATA");
        listFields.add("D.VALUE");
        listFields.add("D.NO_COL");
        listFields.add("D.NO_ROW");
        listFields.add("D.IS_IGNORED");

        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("D.ID_DATA");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            s = rs.getColumnData("D.VALUE");
            if (s == null)
                continue;
            float value = 0;
            try{
                value = Float.parseFloat(s);
            }catch(NumberFormatException e){
                System.out.println(e);
            }
            s = rs.getColumnData("D.NO_COL");
            if (s == null)
                continue;
            int noCol = 0;
            try{
                noCol = Integer.parseInt(s);
            }catch(NumberFormatException e){
                System.out.println(e);
            }
            s = rs.getColumnData("D.NO_ROW");
            if (s == null)
                continue;
            int noRow = 0;
            try{
                noRow = Integer.parseInt(s);
            }catch(NumberFormatException e){
                System.out.println(e);
            }
            s = rs.getColumnData("D.IS_IGNORED");
            if (s == null)
                continue;
            int val = 0;
            try{
                val = Integer.parseInt(s);
            }catch(NumberFormatException e){
                System.out.println(e);
            }
            boolean isIgnored = val == 1;
            Data d = new Data(dbKey, value, noRow, noCol, isIgnored);
            tabData[noRow][noCol] = d;
        }
        v.add(tabData);
        return new CopexReturn();
    }

    /* creation d'un dataset vierge - en v[0] le nouveau dbKey */
    public static CopexReturn createDatasetInDB(DataBaseCommunication dbC, String name, int nbRow, int nbCol, long dbKeyUser, long dbKeyMission, ArrayList v){
        name = MyUtilities.replace("\'",name,"''") ;
        String query = "INSERT INTO DATASET (ID_DATASET, DATASET_NAME, NB_COL, NB_ROW) VALUES (NULL, '"+name+"', "+nbCol+", "+nbRow+") ;";
        String queryID = "SELECT max(last_insert_id(`ID_DATASET`))   FROM DATASET ;";
        ArrayList v2 = new ArrayList();
        CopexReturn cr = dbC.getNewIdInsertInDB(query, queryID, v2);
        if (cr.isError())
            return cr;
        long dbKey = (Long)v2.get(0);
        // lien mission /user
        query = "INSERT INTO LINK_DATASET_MISSION_USER (ID_DATASET, ID_MISSION, ID_USER) VALUES ("+dbKey+", "+dbKeyMission+", "+dbKeyUser+") ;";
        String[] querys = new String[1];
        querys[0] = query;
        v2 = new ArrayList();
        cr = dbC.executeQuery(querys, v2);
        v.add(dbKey);
        return cr;
    }

    /* suppression d'un dataset en base : suppression des operations, des données, des headers et des liens */
    public static CopexReturn deleteDatasetFromDB(DataBaseCommunication dbC, long dbKeyDataset){
        ArrayList v = new ArrayList();
        String[] querys = new String[14];
        // suppression des visualisations associées 
        String queryDelVisType = "DELETE FROM LINK_VISUALIZATION_TYPE WHERE ID_DATA_VISUALIZATION IN (SELECT ID_DATA_VISUALIZATION FROM LINK_DATASET_VISUALIZATION WHERE ID_DATASET = "+dbKeyDataset+ ") ;";
        String queryDelVisNo = "DELETE FROM LIST_NO_VISUALIZATION WHERE ID_DATA_VISUALIZATION IN (SELECT ID_DATA_VISUALIZATION FROM  LINK_DATASET_VISUALIZATION WHERE ID_DATASET = "+dbKeyDataset+ " ) ;";
        String queryDelParamGraph = "DELETE FROM PARAM_VIS_GRAPH WHERE ID_DATA_VISUALIZATION IN (SELECT ID_DATA_VISUALIZATION FROM LINK_DATASET_VISUALIZATION WHERE ID_DATASET = "+dbKeyDataset+ ") ;";
        String queryDelVis = "DELETE FROM DATA_VISUALIZATION WHERE ID_DATA_VISUALIZATION IN (SELECT ID_DATA_VISUALIZATION FROM LINK_DATASET_VISUALIZATION WHERE ID_DATASET = "+dbKeyDataset+ ")";
        String queryDelLinkVis = "DELETE FROM LINK_DATASET_VISUALIZATION WHERE ID_DATASET = "+dbKeyDataset+ " ;";
        // suppression des operations
        String queryDelOpType = "DELETE FROM LINK_OPERATION_TYPE WHERE ID_DATA_OPERATION IN (SELECT ID_DATA_OPERATION FROM LINK_DATASET_OPERATION WHERE ID_DATASET = "+dbKeyDataset+ ") ;";
        String queryDelNo = "DELETE FROM LIST_NO_OPERATION WHERE ID_DATA_OPERATION IN (SELECT ID_DATA_OPERATION FROM LINK_DATASET_OPERATION WHERE ID_DATASET = "+dbKeyDataset+ " );";
        String queryDelOp = "DELETE FROM DATA_OPERATION WHERE ID_DATA_OPERATION IN (SELECT ID_DATA_OPERATION FROM LINK_DATASET_OPERATION WHERE ID_DATASET = "+dbKeyDataset+ ") ;";
        String queryDelLinkOp = "DELETE FROM LINK_DATASET_OPERATION WHERE ID_DATASET = "+dbKeyDataset+ " ;";
        // suppression des données
        String queryDelData = "DELETE FROM COPEX_DATA WHERE ID_DATA IN (SELECT ID_DATA FROM LINK_DATASET_DATA WHERE ID_DATASET = "+dbKeyDataset+ " ;";
        String queryDelLinkData = "DELETE FROM LINK_DATASET_DATA WHERE ID_DATASET = "+dbKeyDataset+ " ;";
        // suppression des header
        String queryDelHeader = "DELETE FROM DATA_HEADER WHERE ID_HEADER IN (SELECT ID_HEADER FROM LINK_DATASET_HEADER WHERE ID_DATASET = "+dbKeyDataset+ " ;";
        String queryDelLinkHeader = "DELETE FROM LINK_DATASET_HEADER WHERE ID_DATASET = "+dbKeyDataset+ " ;";
        // suppression du lien entre user/ mission
        String queryDelUserMission = "DELETE FROM LINK_DATASET_MISSION_USER WHERE ID_DATASET = "+dbKeyDataset+" ;";
        // bloc de requete
        int i=0;
        querys[i++] = queryDelVisType;
        querys[i++] = queryDelVisNo ;
        querys[i++] = queryDelParamGraph ;
        querys[i++] = queryDelVis ;
        querys[i++] = queryDelLinkVis ;
        querys[i++] = queryDelOpType ;
        querys[i++] = queryDelNo;
        querys[i++] = queryDelOp ;
        querys[i++] = queryDelLinkOp;
        querys[i++] = queryDelData;
        querys[i++] = queryDelLinkData;
        querys[i++] = queryDelHeader ;
        querys[i++] = queryDelLinkHeader ;
        querys[i++] = queryDelUserMission ;

        CopexReturn cr = dbC.executeQuery(querys, v);
        return cr;
    }

    /* mise à jour du nom du dataset */
    public static CopexReturn updateDatasetNameInDB(DataBaseCommunication dbC, long dbKey, String newName){
        newName = MyUtilities.replace("\'",newName,"''") ;
        ArrayList v = new ArrayList();
        String[] querys = new String[1];
        String query = "UPDATE DATASET SET DATASET_NAME = '"+newName+"' WHERE ID_DATASET = "+dbKey+" ;";
        querys[0] = query ;
        CopexReturn cr = dbC.executeQuery(querys, v);
        return cr;
    }

    /* met à jour le statut ignored des datas */
    public static CopexReturn setDataIgnoredInDB(DataBaseCommunication dbC, ArrayList<Data> listData, boolean isIgnored){
        int ignore = 0;
        if (isIgnored)
            ignore = 1;
        String listIdData = "";
        int nb = listData.size();
        if (nb > 0)
            listIdData += listData.get(0).getDbKey();
        for (int i=1; i<nb; i++)
            listIdData += ","+listData.get(i).getDbKey();
        ArrayList v = new ArrayList();
        String[] querys = new String[1];
        String query = "UPDATE COPEX_DATA SET IS_IGNORED =  "+ignore+" WHERE ID_DATA IN ("+listIdData+") ;" ;
        querys[0] = query ;
        CopexReturn cr = dbC.executeQuery(querys, v);
        return cr;
    }

    /* creation d'un header - retourne en v[0] le nouveau dbKey */
    public static CopexReturn createDataHeaderInDB(DataBaseCommunication dbC, String value, int noCol, long dbKeyDs, ArrayList v){
        value = MyUtilities.replace("\'",value,"''") ;
        String query = "INSERT INTO DATA_HEADER (ID_HEADER, VALUE, NO_COL) VALUES (NULL, '"+value+"', "+noCol+") ;";
        String queryID = "SELECT max(last_insert_id(`ID_HEADER`))   FROM DATA_HEADER ;";
        ArrayList v2 = new ArrayList();
        CopexReturn cr = dbC.getNewIdInsertInDB(query, queryID, v2);
        if (cr.isError())
            return cr;
        long dbKey = (Long)v2.get(0);
        // lien dataset / header
        query = "INSERT INTO LINK_DATASET_HEADER (ID_DATASET, ID_HEADER) VALUES ("+dbKeyDs+", "+dbKey+") ;";
        String[] querys = new String[1];
        querys[0] = query;
        v2 = new ArrayList();
        cr = dbC.executeQuery(querys, v2);
        v.add(dbKey);
        return cr;
    }

    /* mise à jour d'un header */
    public static CopexReturn updateDataHeaderInDB(DataBaseCommunication dbC, long dbKey, String value){
        value = MyUtilities.replace("\'",value,"''") ;
        ArrayList v = new ArrayList();
        String[] querys = new String[1];
        String query = "UPDATE DATA_HEADER SET VALUE = '"+value+"' WHERE ID_HEADER = "+dbKey+" ;";
        querys[0] = query ;
        CopexReturn cr = dbC.executeQuery(querys, v);
        return cr;
    }

    /* creation d'un data - retourne en v[0] le nouveau dbKey */
    public static CopexReturn createDataInDB(DataBaseCommunication dbC, double value, int noRow, int noCol, long dbKeyDs, ArrayList v){
        String query = "INSERT INTO COPEX_DATA (ID_DATA, VALUE, NO_ROW, NO_COL, IS_IGNORED) VALUES (NULL, "+value+", "+noRow+", "+noCol+", 0) ;";
        String queryID = "SELECT max(last_insert_id(`ID_DATA`))   FROM COPEX_DATA ;";
        ArrayList v2 = new ArrayList();
        CopexReturn cr = dbC.getNewIdInsertInDB(query, queryID, v2);
        if (cr.isError())
            return cr;
        long dbKey = (Long)v2.get(0);
        // lien dataset / data
        query = "INSERT INTO LINK_DATASET_DATA (ID_DATASET, ID_DATA) VALUES ("+dbKeyDs+", "+dbKey+") ;";
        String[] querys = new String[1];
        querys[0] = query;
        v2 = new ArrayList();
        cr = dbC.executeQuery(querys, v2);
        v.add(dbKey);
        return cr;
    }

    /* mise à jour d'un data */
    public static CopexReturn updateDataInDB(DataBaseCommunication dbC, long dbKey, double value){
        ArrayList v = new ArrayList();
        String[] querys = new String[1];
        String query = "UPDATE COPEX_DATA SET VALUE = "+value+" WHERE ID_HEADER = "+dbKey+" ;";
        querys[0] = query ;
        CopexReturn cr = dbC.executeQuery(querys, v);
        return cr;
    }

    /* suppression de tous les dataset d'un etudiant et d'une mission données */
    public static CopexReturn removeAllDatasetFromDB(DataBaseCommunication  dbC, long dbKeyMission, long dbKeyUser){
        String query = "SELECT D.ID_DATASET " +
                " FROM DATASET D, LINK_DATASET_MISSION_USER  L WHERE " +
                "L.ID_DATASET = D.ID_DATASET AND L.ID_MISSION = "+dbKeyMission+" AND L.ID_USER = "+dbKeyUser+" ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("D.ID_DATASET");

        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("D.ID_DATASET");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            cr = deleteDatasetFromDB(dbC, dbKey);
            if (cr.isError())
                return cr;
        }
        return new CopexReturn();
    }

    /* creation d'un dataset - retourne en v[0] le dataset avec les dbKey*/
    public static CopexReturn createDatasetInDB(DataBaseCommunication dbC, Dataset ds, long dbKeyMission, long dbKeyuser, ArrayList v){
        // creation dataset
        ArrayList v2 = new ArrayList();
        CopexReturn cr = createDatasetInDB(dbC, ds.getName(), ds.getNbRows(), ds.getNbCol(), dbKeyuser, dbKeyMission, v2);
        if (cr.isError())
            return cr;
        long dbKey = (Long)v2.get(0);
        ds.setDbKey(dbKey);
        // creation des header
        DataHeader[] tabHeader = ds.getListDataHeader();
        for (int i=0; i<tabHeader.length; i++){
            v2 = new ArrayList();
            cr = createDataHeaderInDB(dbC, tabHeader[i].getValue(), tabHeader[i].getNoCol(), dbKey, v2);
            if (cr.isError())
                return cr;
            long dbKeyHeader = (Long)v2.get(0);
            tabHeader[i].setDbKey(dbKeyHeader);
            ds.setDataHeader(tabHeader[i],tabHeader[i].getNoCol()) ;
        }
        // creation des data
        Data[][] tabData = ds.getData() ;
        for (int i=0; i<ds.getNbRows(); i++){
            for (int j=0; j<ds.getNbCol(); j++){
                v2 = new ArrayList();
                cr = createDataInDB(dbC, tabData[i][j].getValue(), i, j, dbKey, v2) ;
                if (cr.isError())
                    return cr;
                long dbKeyData = (Long)v2.get(0);
                tabData[i][j].setDbKey(dbKeyData);
                ds.setData(tabData[i][j], i, j);
            }
        }
        // en v[0] le ds
        v.add(ds);
        return new CopexReturn();
    }

    /* chargement de tous les visualizations d'un dataset donné */
    public static CopexReturn getAllDatasetVisualizationFromDB(DataBaseCommunication dbC, long dbKeyDs,  TypeVisualization[] tabTypeVis, ArrayList v){
        ArrayList<Visualization> listDataVis = new ArrayList();
        String query = "SELECT D.ID_DATA_VISUALIZATION, D.VIS_NAME, D.IS_ON_COL, T.ID_TYPE_VISUALIZATION " +
                "FROM DATA_VISUALIZATION D, LINK_DATASET_VISUALIZATION L, LINK_VISUALIZATION_TYPE T " +
                "WHERE D.ID_DATA_VISUALIZATION = L.ID_DATA_VISUALIZATION AND L.ID_DATASET = "+dbKeyDs+"  AND D.ID_DATA_VISUALIZATION = T.ID_DATA_VISUALIZATION;  ";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("D.ID_DATA_VISUALIZATION");
        listFields.add("D.VIS_NAME");
        listFields.add("D.IS_ON_COL");
        listFields.add("T.ID_TYPE_VISUALIZATION");

        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("D.ID_DATA_VISUALIZATION");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            String name = rs.getColumnData("D.VIS_NAME");
            if (name == null)
                continue;
            s = rs.getColumnData("D.IS_ON_COL");
            if (s == null)
                continue;
            int n = 0;
            try{
                n = Integer.parseInt(s);
            }catch(NumberFormatException e){
                System.out.println(e);
            }
            boolean isOnCol = n==1;
            s = rs.getColumnData("T.ID_TYPE_VISUALIZATION");
            if (s == null)
                continue;
            long dbKeyType = Long.parseLong(s);
            // recherche du type Visualization
            TypeVisualization typeVisualization = null;
            for (int j=0; j<tabTypeVis.length; j++){
                if (tabTypeVis[j].getDbKey() == dbKeyType){
                    typeVisualization = tabTypeVis[j];
                    break;
                }
            }
            if (typeVisualization == null)
                return new CopexReturn("ERROR TYPE VISUALIZATION", false);
            // list des numeros de col/row surlesquells s'applique l'operation
            ArrayList v3 = new ArrayList();
            cr = getAllNoVisualization(dbC, dbKey, v3);
            if (cr.isError())
                return cr;
            int[] listNo = (int[])v3.get(0);
            Visualization vis;
            // eventuellement charge les données du graphe
            if (typeVisualization.getCode() == DataConstants.VIS_GRAPH){
                v3 = new ArrayList();
                cr = getAllParamGraph(dbC, dbKey, v3);
                if (cr.isError())
                    return cr;
                ParamGraph paramGraph = (ParamGraph)v3.get(0);
                v3 = new ArrayList();
                cr = getAllFunctionModelGraphFromDB(dbC, dbKey, v3);
                if (cr.isError())
                    return cr;
                ArrayList<FunctionModel> listFunctionModel = (ArrayList<FunctionModel>)v3.get(0);
                vis = new Graph(dbKey, name, typeVisualization, listNo, isOnCol, paramGraph, listFunctionModel) ;
            }else{
                // creation objet dataVisualization
                vis = new Visualization(dbKey, name, typeVisualization,  listNo, isOnCol);
            }
            listDataVis.add(vis);
        }

        v.add(listDataVis);
        return new CopexReturn();
    }

    /* chargement des no col/lignes de la vis*/
    private static CopexReturn getAllNoVisualization(DataBaseCommunication dbC, long dbKey, ArrayList v){
            // list des numeros de col/row surlesquells s'applique la visualizaztion
            ArrayList<Integer> listNo = new ArrayList();
            int nb = 0;
            String query = "SELECT NO FROM LIST_NO_VISUALIZATION WHERE ID_DATA_VISUALIZATION = "+dbKey+" ;";
            ArrayList v2 = new ArrayList();
            ArrayList<String> listFields = new ArrayList();
            listFields.add("NO");
            CopexReturn cr = dbC.sendQuery(query, listFields, v2);
            if (cr.isError())
                return cr;
            int nbR = v2.size();
            for (int i=0; i<nbR; i++){
                ResultSetXML rs = (ResultSetXML)v2.get(i);
                String s = rs.getColumnData("NO");
                if (s == null)
                    continue;
                int no = 0;
                try{
                    no = Integer.parseInt(s);
                }catch(NumberFormatException e){
                    System.out.println(e);
                }
                nb++;
                listNo.add(no);
            }
            // tableau
            int[] tabNo = new int[nb];
            for (int i=0; i<nb; i++)
                tabNo[i] = listNo.get(i);
            v.add(tabNo);
            return new CopexReturn();
    }

    /* chargement des parametres d'un graphe */
    public static CopexReturn getAllParamGraph(DataBaseCommunication dbC, long dbKey, ArrayList v){
        ParamGraph param = null;
        String query = "SELECT X_NAME, Y_NAME, X_MIN, X_MAX, Y_MIN, Y_MAX, DELTA_X, DELTA_Y FROM PARAM_VIS_GRAPH WHERE ID_DATA_VISUALIZATION = "+dbKey+" ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("X_NAME");
        listFields.add("Y_NAME");
        listFields.add("X_MIN");
        listFields.add("X_MAX");
        listFields.add("Y_MIN");
        listFields.add("Y_MAX");
        listFields.add("DELTA_X");
        listFields.add("DELTA_Y");
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String xName = rs.getColumnData("X_NAME");
            if (xName == null)
                continue;
            String yName = rs.getColumnData("Y_NAME");
            if (yName == null)
                continue;
            String s = rs.getColumnData("X_MIN");
            if (s == null)
                continue;
            double xMin = -10;
            try{
                xMin = Float.parseFloat(s);
            }catch(NumberFormatException e){
            }
            s = rs.getColumnData("X_MAX");
            if (s == null)
                continue;
            double xMax = 10;
            try{
                xMax = Float.parseFloat(s);
            }catch(NumberFormatException e){
            }
            s = rs.getColumnData("Y_MIN");
            if (s == null)
                continue;
            double yMin = -10;
            try{
                yMin = Float.parseFloat(s);
            }catch(NumberFormatException e){
            }
            s = rs.getColumnData("Y_MAX");
            if (s == null)
                continue;
            double yMax = 10;
            try{
                yMax = Float.parseFloat(s);
            }catch(NumberFormatException e){
            }
            s = rs.getColumnData("DELTA_X");
            if (s == null)
                continue;
            double deltaX = 1;
            try{
                deltaX = Float.parseFloat(s);
            }catch(NumberFormatException e){
            }
            s = rs.getColumnData("DELTA_Y");
            if (s == null)
                continue;
            double deltaY = 1;
            try{
                deltaY = Float.parseFloat(s);
            }catch(NumberFormatException e){
            }
            param = new ParamGraph(xName, yName, xMin, xMax, yMin, yMax, deltaX, deltaY, true);

        }
        v.add(param);
        return new CopexReturn();
    }

    /* chargement de la liste des function  model */
    private static CopexReturn getAllFunctionModelGraphFromDB(DataBaseCommunication dbC, long dbKeyGraph, ArrayList v){
        ArrayList<FunctionModel> listFunctionModel = new ArrayList();
        String query = "SELECT F.ID_FUNCTION_MODEL, F.DESCRIPTION, F.COLOR_R, F.COLOR_G, F.COLOR_B " +
                "FROM FUNCTION_MODEL F, LINK_GRAPH_FUNCTION_MODEL  L " +
                "WHERE L.ID_DATA_VISUALIZATION = "+dbKeyGraph+" AND L.ID_FUNCTION_MODEL = L.ID_FUNCTION_MODEL ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("F.ID_FUNCTION_MODEL");
        listFields.add("F.DESCRIPTION");
        listFields.add("F.COLOR_R");
        listFields.add("F.COLOR_G");
        listFields.add("F.COLOR_B");
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("F.ID_FUNCTION_MODEL");
            if (s == null )
                continue;
            long dbKey = Long.parseLong(s);
            String description = rs.getColumnData("F.DESCRIPTION");
            s = rs.getColumnData("F.COLOR_R");
            if (s==null)
                continue;
            int colorR = 0;
            try{
                colorR = Integer.parseInt(s);
            }catch(NumberFormatException e){

            }
            s = rs.getColumnData("F.COLOR_G");
            if (s==null)
                continue;
            int colorG = 0;
            try{
                colorG = Integer.parseInt(s);
            }catch(NumberFormatException e){

            }
            s = rs.getColumnData("F.COLOR_B");
            if (s==null)
                continue;
            int colorB = 0;
            try{
                colorB = Integer.parseInt(s);
            }catch(NumberFormatException e){

            }
            FunctionModel fm = new FunctionModel(dbKey, description, new Color(colorR, colorG, colorB));
            listFunctionModel.add(fm);
        }
        v.add(listFunctionModel);
        return new CopexReturn();
    }

    
    /* suppression de donnees d'un dataset */
    public static CopexReturn deleteDataFromDB(DataBaseCommunication dbC, ArrayList<Data> listData){
        int nbData =listData.size();
        if (nbData == 0)
            return new CopexReturn();
        String listDbKeyData = ""+listData.get(0).getDbKey();
        for (int i=1; i<nbData; i++){
            listDbKeyData += ", "+listData.get(i).getDbKey() ;
        }
        String queryDelLink = "DELETE FROM LINK_DATASET_DATA WHERE ID_DATA IN ( "+listDbKeyData+") ;";
        String queryDelData = "DELETE FROM COPEX_DATA WHERE ID_DATA IN ( "+listDbKeyData+") ;";
        String[] querys = new String[2];
        querys[0] = queryDelLink;
        querys[1] = queryDelData;
        ArrayList v2 = new ArrayList();
        CopexReturn cr = dbC.executeQuery(querys, v2);
        return cr;
    }

    /* suppression de header d'un dataset */
    public static CopexReturn deleteDataHeaderFromDB(DataBaseCommunication dbC, ArrayList<DataHeader> listDataHeader){
        int nbDataH =listDataHeader.size();
        if (nbDataH == 0)
            return new CopexReturn();
        String listDbKeyData = ""+listDataHeader.get(0).getDbKey();
        for (int i=1; i<nbDataH; i++){
            listDbKeyData += ", "+listDataHeader.get(i).getDbKey() ;
        }
        String queryDelLink = "DELETE FROM LINK_DATASET_HEADER WHERE ID_HEADER IN ( "+listDbKeyData+") ;";
        String queryDelData = "DELETE FROM DATA_HEADER WHERE ID_HEADER IN ( "+listDbKeyData+") ;";
        String[] querys = new String[2];
        querys[0] = queryDelLink;
        querys[1] = queryDelData;
        ArrayList v2 = new ArrayList();
        CopexReturn cr = dbC.executeQuery(querys, v2);
        return cr;
    }

    /* mise à jour du numero de colonne pour les data, header, operation, visualization */
    public static CopexReturn updateNoInDB(DataBaseCommunication dbC, Dataset ds){
        ArrayList<String> listQuerys = new ArrayList();
        // dataset : nbRow and nbCol
        listQuerys.add("UPDATE DATASET SET NB_COL = "+ds.getNbCol()+", NB_ROW = "+ds.getNbRows()+" WHERE ID_DATASET= "+ds.getDbKey()+" ;");
        // data
        Data[][] data = ds.getData();
        int nbRows = ds.getNbRows() ;
        int nbCols = ds.getNbCol() ;
        System.out.println("******update data : "+nbRows+" / "+nbCols);
        for (int i=0; i<nbRows; i++){
            for (int j=0; j<nbCols; j++){
                if (data[i][j] != null)
                    listQuerys.add("UPDATE COPEX_DATA SET NO_COL ="+j+" , NO_ROW ="+i+"  WHERE ID_DATA = "+data[i][j].getDbKey()+" ;");
            }
        }
        //data header
        DataHeader[] listDataHeader = ds.getListDataHeader() ;
        int nb =  listDataHeader.length ;
        for (int i=0; i<nb; i++){
            if (listDataHeader[i] != null)
                listQuerys.add("UPDATE DATA_HEADER SET NO_COL = "+listDataHeader[i].getNoCol() +" WHERE ID_HEADER = "+listDataHeader[i].getDbKey()+" ;");
        }
        nb = listQuerys.size();
        String[] querys = new String[nb];
        for (int i=0; i<nb; i++)
            querys[i] = listQuerys.get(i);
        ArrayList v2 = new ArrayList();
        CopexReturn cr = dbC.executeQuery(querys, v2);
        if (cr.isError())
            return cr;
        // liste des operations / visualizations : on supprime tout et on recréé tout
        cr = OperationFromDB.updateNoInDB(dbC, ds.getListOperation());
        if (cr.isError())
            return cr;
        cr = VisualizationFromDB.updateNoInDB(dbC, ds.getListVisualization());
        if (cr.isError())
            return cr;
        return new CopexReturn();
    }

    /* mise à jour du nombre de lignes et col pour la matrice */
    public static CopexReturn updateDatasetMatriceInDB(DataBaseCommunication dbC, long dbKey, int newNbRow, int newNbCol){
        String query = "UPDATE DATASET SET NB_COL = "+newNbCol+", NB_ROW = "+newNbRow+" WHERE ID_DATASET = "+dbKey+" ;";
        ArrayList v2 = new ArrayList();
        String[] querys = new String[1];
        querys[0]  =query ;
        CopexReturn cr = dbC.executeQuery(querys, v2);
        return cr;
    }

}
