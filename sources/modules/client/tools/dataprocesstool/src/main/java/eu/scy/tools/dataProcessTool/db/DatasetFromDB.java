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
import java.util.Iterator;

/**
 * Gestion en bd des dataset
 * @author Marjolaine
 */
public class DatasetFromDB {

    /* chargement de tous les ds lie a un utilisateur et a une mission donnee */
    public static CopexReturn getAllDatasetFromDB(DataBaseCommunication dbC, long dbKeyUser, Mission mission, TypeOperation[] tabTypeOp,TypeVisualization[] tabTypeVis,Color[] plotsColor, Color[] functionsColor, ArrayList v){
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
            cr = getAllDatasetVisualizationFromDB(dbC, dbKey,tabTypeVis, plotsColor,functionsColor, tabDataHeader,  v3);
            if (cr.isError())
                return cr;
            ArrayList<Visualization> listVis = (ArrayList<Visualization>)v3.get(0);
            System.out.println("chargement dataset, nbVis : "+listVis.size());
            //creation du dataset
            Dataset ds= new Dataset(dbKey, name, nbCol, nbRow, tabDataHeader, data, listOp, listVis);
            listDataset.add(ds);
        }
        v.add(listDataset);
        return new CopexReturn();
    }

    /* chargemement de tous les datasets(nom) de la liste des missions donnees*/
    public static CopexReturn getAllDatasetFromDB(DataBaseCommunication dbC, long dbKeyUser, ArrayList<Mission> listMission, ArrayList v){
        ArrayList<ArrayList<Dataset>> listDatasetMission = new ArrayList();
        for(Iterator<Mission> m = listMission.iterator(); m.hasNext();){
            ArrayList v2 = new ArrayList();
            CopexReturn cr = getMissionDatasetFromDB(dbC, dbKeyUser,m.next().getDbKey(), v2 );
            if(cr.isError())
                return cr;
            listDatasetMission.add((ArrayList<Dataset>)v2.get(0));
        }
        v.add(listDatasetMission);
        return new CopexReturn();
    }

    private static CopexReturn getMissionDatasetFromDB(DataBaseCommunication dbC, long dbKeyUser, long dbKeyMission , ArrayList v){
        ArrayList<Dataset> listDataset = new ArrayList();
        String query = "SELECT D.ID_DATASET, D.DATASET_NAME  " +
                " FROM DATASET D, LINK_DATASET_MISSION_USER  L WHERE " +
                "L.ID_DATASET = D.ID_DATASET AND L.ID_MISSION = "+dbKeyMission+" AND L.ID_USER = "+dbKeyUser+" ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("D.ID_DATASET");
        listFields.add("D.DATASET_NAME");

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

            //creation du dataset
            Dataset ds= new Dataset(dbKey, name);
            listDataset.add(ds);
        }
        v.add(listDataset);
        return new CopexReturn();
    }
    
    /* chargement de tous les ds header d'un dataset donne */
    public static CopexReturn getAllDatasetHeaderFromDB(DataBaseCommunication dbC, long dbKeyDs,  int nbCol, ArrayList v){
        DataHeader[] tabHeader = new DataHeader[nbCol] ;
        String query = "SELECT D.ID_HEADER, D.VALUE,D.UNIT, D.NO_COL, D.TYPE, D.DESCRIPTION " +
                "FROM DATA_HEADER D, LINK_DATASET_HEADER L " +
                "WHERE D.ID_HEADER = L.ID_HEADER AND L.ID_DATASET = "+dbKeyDs+" ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("D.ID_HEADER");
        listFields.add("D.VALUE");
        listFields.add("D.UNIT");
        listFields.add("D.NO_COL");
        listFields.add("D.TYPE");
        listFields.add("D.DESCRIPTION");
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
            String unit = rs.getColumnData("D.UNIT");
            if (unit == null)
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
            String type = rs.getColumnData("D.TYPE");
            if (type == null)
                continue;
            String description = rs.getColumnData("D.DESCRIPTION");
            if (description == null)
                continue;
            DataHeader dh= new DataHeader(dbKey, value,unit, noCol, type, description);
            tabHeader[noCol] = dh;
        }
        v.add(tabHeader);
        return new CopexReturn();
    }

    /* chargement de tous les operation d'un dataset donne */
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
    /* chargement des donnees d'un data set */
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

    /* suppression d'un dataset en base : suppression des operations, des donnees, des headers et des liens */
    public static CopexReturn deleteDatasetFromDB(DataBaseCommunication dbC, long dbKeyDataset){
        ArrayList v = new ArrayList();
        String[] querys = new String[15];
        // suppression des visualisations associees 
        String queryDelVisType = "DELETE FROM LINK_VISUALIZATION_TYPE WHERE ID_DATA_VISUALIZATION IN (SELECT ID_DATA_VISUALIZATION FROM LINK_DATASET_VISUALIZATION WHERE ID_DATASET = "+dbKeyDataset+ ") ;";
        String queryDelVisNo = "DELETE FROM PIE_BAR_VISUALIZATION WHERE ID_DATA_VISUALIZATION IN (SELECT ID_DATA_VISUALIZATION FROM  LINK_DATASET_VISUALIZATION WHERE ID_DATASET = "+dbKeyDataset+ " ) ;";
        String queryDelParamGraph = "DELETE FROM PARAM_VIS_GRAPH WHERE ID_DATA_VISUALIZATION IN (SELECT ID_DATA_VISUALIZATION FROM LINK_DATASET_VISUALIZATION WHERE ID_DATASET = "+dbKeyDataset+ ") ;";
        String queryDelVis = "DELETE FROM DATA_VISUALIZATION WHERE ID_DATA_VISUALIZATION IN (SELECT ID_DATA_VISUALIZATION FROM LINK_DATASET_VISUALIZATION WHERE ID_DATASET = "+dbKeyDataset+ ") ;";
        String queryDelLinkVis = "DELETE FROM LINK_DATASET_VISUALIZATION WHERE ID_DATASET = "+dbKeyDataset+ " ;";
        // suppression des operations
        String queryDelOpType = "DELETE FROM LINK_OPERATION_TYPE WHERE ID_DATA_OPERATION IN (SELECT ID_DATA_OPERATION FROM LINK_DATASET_OPERATION WHERE ID_DATASET = "+dbKeyDataset+ ") ;";
        String queryDelNo = "DELETE FROM LIST_NO_OPERATION WHERE ID_DATA_OPERATION IN (SELECT ID_DATA_OPERATION FROM LINK_DATASET_OPERATION WHERE ID_DATASET = "+dbKeyDataset+ " );";
        String queryDelOp = "DELETE FROM DATA_OPERATION WHERE ID_DATA_OPERATION IN (SELECT ID_DATA_OPERATION FROM LINK_DATASET_OPERATION WHERE ID_DATASET = "+dbKeyDataset+ ") ;";
        String queryDelLinkOp = "DELETE FROM LINK_DATASET_OPERATION WHERE ID_DATASET = "+dbKeyDataset+ " ;";
        // suppression des donnees
        String queryDelData = "DELETE FROM COPEX_DATA WHERE ID_DATA IN (SELECT ID_DATA FROM LINK_DATASET_DATA WHERE ID_DATASET = "+dbKeyDataset+ " );";
        String queryDelLinkData = "DELETE FROM LINK_DATASET_DATA WHERE ID_DATASET = "+dbKeyDataset+ " ;";
        // suppression des header
        String queryDelHeader = "DELETE FROM DATA_HEADER WHERE ID_HEADER IN (SELECT ID_HEADER FROM LINK_DATASET_HEADER WHERE ID_DATASET = "+dbKeyDataset+ " );";
        String queryDelLinkHeader = "DELETE FROM LINK_DATASET_HEADER WHERE ID_DATASET = "+dbKeyDataset+ " ;";
        // suppression du dataset 
        String queryDs = "DELETE FROM DATASET WHERE ID_DATASET = "+dbKeyDataset+" ;";
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
        querys[i++] = queryDs;
        querys[i++] = queryDelUserMission ;

        CopexReturn cr = dbC.executeQuery(querys, v);
        return cr;
    }

    /* mise a jour du nom du dataset */
    public static CopexReturn updateDatasetNameInDB(DataBaseCommunication dbC, long dbKey, String newName){
        newName = MyUtilities.replace("\'",newName,"''") ;
        ArrayList v = new ArrayList();
        String[] querys = new String[1];
        String query = "UPDATE DATASET SET DATASET_NAME = '"+newName+"' WHERE ID_DATASET = "+dbKey+" ;";
        querys[0] = query ;
        CopexReturn cr = dbC.executeQuery(querys, v);
        return cr;
    }

    /* met a jour le statut ignored des datas */
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
    public static CopexReturn createDataHeaderInDB(DataBaseCommunication dbC, String value, String unit, int noCol, String type, String description, long dbKeyDs, ArrayList v){
        value = MyUtilities.replace("\'",value,"''") ;
        unit = MyUtilities.replace("\'",unit,"''") ;
        type = MyUtilities.replace("\'",type,"''") ;
        description = MyUtilities.replace("\'",description,"''") ;
        String query = "INSERT INTO DATA_HEADER (ID_HEADER, VALUE, UNIT,NO_COL, TYPE, DESCRIPTION) VALUES (NULL, '"+value+"', '"+unit+"', "+noCol+", '"+type+"', '"+description+"') ;";
        System.out.println("createDataHeaderInDB : "+query);
        String queryID = "SELECT max(last_insert_id(`ID_HEADER`))   FROM DATA_HEADER ;";
        System.out.println("createDataHeaderInDB : "+queryID);
        ArrayList v2 = new ArrayList();
        CopexReturn cr = dbC.getNewIdInsertInDB(query, queryID, v2);
        if (cr.isError())
            return cr;
        long dbKey = (Long)v2.get(0);
        System.out.println("==> dbKey : "+dbKey);
        // lien dataset / header
        query = "INSERT INTO LINK_DATASET_HEADER (ID_DATASET, ID_HEADER) VALUES ("+dbKeyDs+", "+dbKey+") ;";
        System.out.println("cree lien : "+query);
        String[] querys = new String[1];
        querys[0] = query;
        v2 = new ArrayList();
        cr = dbC.executeQuery(querys, v2);
        v.add(dbKey);
        return cr;
    }

    /* mise a jour d'un header */
    public static CopexReturn updateDataHeaderInDB(DataBaseCommunication dbC, long dbKey, String value, String unit, String description, String type){
        value = MyUtilities.replace("\'",value,"''") ;
        unit = MyUtilities.replace("\'",unit,"''") ;
        description = MyUtilities.replace("\'",description,"''") ;
        type = MyUtilities.replace("\'",type,"''") ;
        ArrayList v = new ArrayList();
        String[] querys = new String[1];
        String query = "UPDATE DATA_HEADER SET VALUE = '"+value+"', UNIT= '"+unit+"' , DESCRIPTION = '"+description+"', TYPE = '"+type+"' WHERE ID_HEADER = "+dbKey+" ;";
        querys[0] = query ;
        CopexReturn cr = dbC.executeQuery(querys, v);
        return cr;
    }

    /* creation d'un data - retourne en v[0] le nouveau dbKey */
    public static CopexReturn createDataInDB(DataBaseCommunication dbC, String value, int noRow, int noCol, long dbKeyDs, ArrayList v){
        value = MyUtilities.replace("\'",value,"''") ;
        String query = "INSERT INTO COPEX_DATA (ID_DATA, VALUE, NO_ROW, NO_COL, IS_IGNORED) VALUES (NULL, '"+value+"', "+noRow+", "+noCol+", 0) ;";
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

    /* mise a jour d'un data */
    public static CopexReturn updateDataInDB(DataBaseCommunication dbC, long dbKey, String value){
        value = MyUtilities.replace("\'",value,"''") ;
        ArrayList v = new ArrayList();
        String[] querys = new String[1];
        String query = "UPDATE COPEX_DATA SET VALUE = '"+value+"' WHERE ID_DATA = "+dbKey+" ;";
        querys[0] = query ;
        CopexReturn cr = dbC.executeQuery(querys, v);
        return cr;
    }

    /* suppression de tous les dataset d'un etudiant et d'une mission donnees */
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
            cr = createDataHeaderInDB(dbC, tabHeader[i].getValue(),tabHeader[i].getUnit(), tabHeader[i].getNoCol(), tabHeader[i].getType(), tabHeader[i].getDescription(), dbKey, v2);
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

    /* chargement de tous les visualizations d'un dataset donne */
    public static CopexReturn getAllDatasetVisualizationFromDB(DataBaseCommunication dbC, long dbKeyDs,  TypeVisualization[] tabTypeVis, Color[] plotsColor, Color[] functionsColor,DataHeader[] listCols, ArrayList v){
        ArrayList<Visualization> listDataVis = new ArrayList();
        String query = "SELECT D.ID_DATA_VISUALIZATION, D.VIS_NAME, T.ID_TYPE_VISUALIZATION " +
                "FROM DATA_VISUALIZATION D, LINK_DATASET_VISUALIZATION L, LINK_VISUALIZATION_TYPE T " +
                "WHERE D.ID_DATA_VISUALIZATION = L.ID_DATA_VISUALIZATION AND L.ID_DATASET = "+dbKeyDs+"  AND D.ID_DATA_VISUALIZATION = T.ID_DATA_VISUALIZATION;  ";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("D.ID_DATA_VISUALIZATION");
        listFields.add("D.VIS_NAME");
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
            // si pie ou bar
            Visualization vis = null;
            if (typeVisualization.getCode() == DataConstants.VIS_BAR || typeVisualization.getCode() == DataConstants.VIS_PIE){
                ArrayList v3 = new ArrayList();
                cr = getSimpleVisualizationFromDB(dbC, dbKey, name, typeVisualization, listCols, v3);
                if (cr.isError())
                    return cr;
                vis = (SimpleVisualization)v3.get(0);
            }else if (typeVisualization.getCode() == DataConstants.VIS_GRAPH){
            // eventuellement charge les donnees du graphe
                ArrayList v3 = new ArrayList();
                cr = getAllParamGraph(dbC, dbKey, listCols, plotsColor, v3);
                if (cr.isError())
                    return cr;
                ParamGraph paramGraph = (ParamGraph)v3.get(0);
                v3 = new ArrayList();
                cr = getAllFunctionModelGraphFromDB(dbC, dbKey, functionsColor, v3);
                if (cr.isError())
                    return cr;
                ArrayList<FunctionModel> listFunctionModel = (ArrayList<FunctionModel>)v3.get(0);
                vis = new Graph(dbKey, name, typeVisualization, paramGraph, listFunctionModel) ;
            }
            listDataVis.add(vis);
        }

        v.add(listDataVis);
        return new CopexReturn();
    }

    private static CopexReturn getSimpleVisualizationFromDB(DataBaseCommunication dbC, long dbKey,String name, TypeVisualization type, DataHeader[] listCols, ArrayList v){
        SimpleVisualization vis = null;
        String query = "SELECT ID_HEADER, ID_HEADER_LABEL FROM PIE_BAR_VISUALIZATION WHERE ID_DATA_VISUALIZATION = "+dbKey+" ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("ID_HEADER");
        listFields.add("ID_HEADER_LABEL");

        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("ID_HEADER");
            if (s == null)
                continue;
            long dbKeyHeader = Long.parseLong(s);
            DataHeader header = getHeader(dbKeyHeader, listCols);
            s = rs.getColumnData("ID_HEADER_LABEL");
            long dbKeyHeaderLabel = -1;
            if(s != null){
                try{
                    dbKeyHeaderLabel = Long.parseLong(s);
                }catch(NumberFormatException e){

                }
            }
            DataHeader headerLabel = null;
            if(dbKeyHeaderLabel != -1)
                headerLabel = getHeader(dbKeyHeaderLabel, listCols);
            vis = new SimpleVisualization(dbKey, name, type, header, headerLabel);
        }
        v.add(vis);
        return new CopexReturn();
    }

    private static DataHeader getHeader(long dbKey, DataHeader[] list){
        for(int i=0; i<list.length; i++){
            if(list[i] != null && list[i].getDbKey() == dbKey)
                return list[i];
        }
        return null;
    }

    /* chargement des parametres d'un graphe */
    public static CopexReturn getAllParamGraph(DataBaseCommunication dbC, long dbKey, DataHeader[] listCol, Color[] plotsColor, ArrayList v){
        ParamGraph param = null;
        String query = "SELECT X_MIN, X_MAX, Y_MIN, Y_MAX, DELTA_X, DELTA_Y, DELTA_FIXED_AUTOSCALE FROM PARAM_VIS_GRAPH WHERE ID_DATA_VISUALIZATION = "+dbKey+" ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("X_MIN");
        listFields.add("X_MAX");
        listFields.add("Y_MIN");
        listFields.add("Y_MAX");
        listFields.add("DELTA_X");
        listFields.add("DELTA_Y");
        listFields.add("DELTA_FIXED_AUTOSCALE");
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
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
            s = rs.getColumnData("DELTA_FIXED_AUTOSCALE");
            if(s== null)
                continue;
            boolean deltaFixedAutoscale = false;
            try{
                int d = Integer.parseInt(s);
                if(d==1)
                    deltaFixedAutoscale = true;
                System.out.println("d ... "+d);
            }catch(NumberFormatException e){
                
            }
            ArrayList v3 = new ArrayList();
            cr= getAllPlotsXYFromDB(dbC, dbKey, listCol, plotsColor, v3);
            if(cr.isError())
                return cr;
            ArrayList<PlotXY> plots = (ArrayList<PlotXY>)v3.get(0);
            param = new ParamGraph(plots, xMin, xMax, yMin, yMax, deltaX, deltaY, deltaFixedAutoscale);
        }
        v.add(param);
        return new CopexReturn();
    }

    /* chargement des plots xy*/
    private static CopexReturn getAllPlotsXYFromDB(DataBaseCommunication dbC, long dbKey, DataHeader[] listCol,Color[] plotsColor, ArrayList v){
        ArrayList<PlotXY> plots = new ArrayList();
        String query = "SELECT P.ID_PLOT, P.ID_HEADER_X, P.ID_HEADER_Y, P.ID_PLOT_COLOR " +
                "FROM PLOT_XY_GRAPH P, LINK_GRAPH_PLOT L " +
                "WHERE L.ID_DATA_VISUALIZATION = "+dbKey+" AND " +
                "L.ID_PLOT = P.ID_PLOT ;" ;
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("P.ID_PLOT");
        listFields.add("P.ID_HEADER_X");
        listFields.add("P.ID_HEADER_Y");
        listFields.add("P.ID_PLOT_COLOR");
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("P.ID_HEADER_X");
            if(s == null)
                continue;
            long dbKeyX = Long.parseLong(s);
            DataHeader headerX = null;
            for (int k=0; k<listCol.length; k++){
                if(listCol[k] != null && listCol[k].getDbKey() == dbKeyX){
                    headerX = listCol[k];
                    break;
                }
            }
            s = rs.getColumnData("P.ID_HEADER_Y");
            if(s == null)
                continue;
            long dbKeyY = Long.parseLong(s);
            DataHeader headerY = null;
            for (int k=0; k<listCol.length; k++){
                if(listCol[k] != null && listCol[k].getDbKey() == dbKeyY){
                    headerY = listCol[k];
                    break;
                }
            }
            s = rs.getColumnData("P.ID_PLOT_COLOR");
            int idPlotColor = -1;
            try{
                idPlotColor = Integer.parseInt(s);
            }catch(NumberFormatException e){
            }
            Color plotColor = plotsColor[idPlotColor];
            PlotXY plot = new PlotXY(headerX, headerY, plotColor);
            plots.add(plot);

        }
        v.add(plots);
        return new CopexReturn();
    }

    /* chargement des param d'une fonction model */
    private static CopexReturn getAllFunctionParamFromDB(DataBaseCommunication dbC, long dbKeyFunction, ArrayList v){
        ArrayList<FunctionParam> listFunctionParam = new ArrayList();
        String query = "SELECT P.ID_FUNCTION_PARAM, P.PARAM_NAME, P.PARAM_VALUE " +
                "FROM FUNCTION_PARAM P, LINK_FUNCTION_PARAM L " +
                "WHERE L.ID_FUNCTION_MODEL = "+dbKeyFunction+" AND L.ID_FUNCTION_PARAM = L.ID_FUNCTION_PARAM ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("P.ID_FUNCTION_PARAM");
        listFields.add("P.PARAM_NAME");
        listFields.add("P.PARAM_VALUE");
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("P.ID_FUNCTION_PARAM");
            if (s == null )
                continue;
            long dbKey = Long.parseLong(s);
            String paramName = rs.getColumnData("P.PARAM_NAME");
            s = rs.getColumnData("P.PARAM_VALUE");
            if (s==null)
                continue;
            double value = Double.NaN;
            try{
                value = Double.parseDouble(s);
            }catch(NumberFormatException e){

            }
            FunctionParam fp = new FunctionParam(dbKey, paramName, value);
            listFunctionParam.add(fp);
        }
        v.add(listFunctionParam);
        return new CopexReturn();
    }


    /* chargement de la liste des function  model */
    private static CopexReturn getAllFunctionModelGraphFromDB(DataBaseCommunication dbC, long dbKeyGraph, Color[] functionsColor, ArrayList v){
        ArrayList<FunctionModel> listFunctionModel = new ArrayList();
        String query = "SELECT F.ID_FUNCTION_MODEL, F.DESCRIPTION, F.ID_FUNCTION_COLOR " +
                "FROM FUNCTION_MODEL F, LINK_GRAPH_FUNCTION_MODEL  L " +
                "WHERE L.ID_DATA_VISUALIZATION = "+dbKeyGraph+" AND L.ID_FUNCTION_MODEL = L.ID_FUNCTION_MODEL ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("F.ID_FUNCTION_MODEL");
        listFields.add("F.DESCRIPTION");
        listFields.add("F.ID_FUNCTION_COLOR");
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
            s = rs.getColumnData("F.ID_FUNCTION_COLOR");
            if (s==null)
                continue;
            int idFunctionColor = -1;
            try{
                idFunctionColor = Integer.parseInt(s);
            }catch(NumberFormatException e){

            }
            Color functionColor = functionsColor[idFunctionColor];
            ArrayList v3 = new ArrayList();
            cr = getAllFunctionParamFromDB(dbC, dbKey, v3);
            if(cr.isError())
                return cr;
            ArrayList<FunctionParam> listParam = (ArrayList<FunctionParam>)v3.get(0);
            FunctionModel fm = new FunctionModel(dbKey, description, functionColor, listParam);
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

    /* mise a jour du numero de colonne pour les data, header, operation, visualization */
    public static CopexReturn updateNoInDB(DataBaseCommunication dbC, Dataset ds){
        ArrayList<String> listQuerys = new ArrayList();
        // dataset : nbRow and nbCol
        listQuerys.add("UPDATE DATASET SET NB_COL = "+ds.getNbCol()+", NB_ROW = "+ds.getNbRows()+" WHERE ID_DATASET= "+ds.getDbKey()+" ;");
        // data
        Data[][] data = ds.getData();
        int nbRows = ds.getNbRows() ;
        int nbCols = ds.getNbCol() ;
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
        // liste des operations / visualizations : on supprime tout et on recree tout
        cr = OperationFromDB.updateNoInDB(dbC, ds.getListOperation());
        if (cr.isError())
            return cr;
        
        return new CopexReturn();
    }

    /* mise a jour du nombre de lignes et col pour la matrice */
    public static CopexReturn updateDatasetMatriceInDB(DataBaseCommunication dbC, long dbKey, int newNbRow, int newNbCol){
        String query = "UPDATE DATASET SET NB_COL = "+newNbCol+", NB_ROW = "+newNbRow+" WHERE ID_DATASET = "+dbKey+" ;";
        ArrayList v2 = new ArrayList();
        String[] querys = new String[1];
        querys[0]  =query ;
        CopexReturn cr = dbC.executeQuery(querys, v2);
        return cr;
    }

}
