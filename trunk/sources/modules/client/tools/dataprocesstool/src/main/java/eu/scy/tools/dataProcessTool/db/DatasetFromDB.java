/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.db;

import eu.scy.tools.dataProcessTool.common.*;
import eu.scy.tools.dataProcessTool.synchro.Locker;
import java.util.ArrayList;
import eu.scy.tools.dataProcessTool.utilities.CopexReturn;
import eu.scy.tools.dataProcessTool.utilities.DataConstants;
import eu.scy.tools.dataProcessTool.utilities.MyUtilities;
import java.awt.Color;
import java.util.Iterator;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * Gestion en bd des dataset
 * @author Marjolaine
 */
public class DatasetFromDB {

    /* load the dataset for a specified labDoc */
    public static CopexReturn getAllDatasetFromDB(DataBaseCommunication dbC, Locker locker, Mission mission, long dbKeyLabDoc, TypeOperation[] tabTypeOp,TypeVisualization[] tabTypeVis,Color[] plotsColor, Color[] functionsColor, ArrayList v){
        ArrayList<Dataset> listDataset = new ArrayList();
        ArrayList<String> listDatasetLocked = new ArrayList();
        boolean allDatasetLocked = true;
        String query = "SELECT D.ID_DATASET, D.DATASET_NAME, D.NB_COL, D.NB_ROW " +
                " FROM DATASET D, LINK_LABDOC_DATASET  L WHERE " +
                "L.ID_DATASET = D.ID_DATASET AND L.ID_LABDOC = "+dbKeyLabDoc+"  ;";
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
        if(nbR == 0)
            allDatasetLocked = false;
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
                // System.out.println(e);
            }
            s = rs.getColumnData("D.NB_ROW");
            if (s == null)
                continue;
            int nbRow = 0;
            try{
                nbRow = Integer.parseInt(s);
            }catch(NumberFormatException e){
                // System.out.println(e);
            }
            // MBO dataset lockes
            if (locker.isLocked(dbKey)){
                listDatasetLocked.add(name);
            }else{
                allDatasetLocked = false;
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
                // System.out.println("chargement dataset, nbVis : "+listVis.size());
                //creation du dataset
                Dataset ds= new Dataset(dbKey, mission,dbKeyLabDoc, name, nbCol, nbRow, tabDataHeader, data, listOp, listVis, DataConstants.EXECUTIVE_RIGHT);
                listDataset.add(ds);
            }
        }
        v.add(listDataset);
        v.add(listDatasetLocked);
        v.add(allDatasetLocked);
        return new CopexReturn();
    }

    /* chargement d'un dataset */
    public static CopexReturn getDatasetFromDB(DataBaseCommunication dbC, long dbKey,Mission mission, TypeOperation[] tabTypeOp,TypeVisualization[] tabTypeVis,Color[] plotsColor, Color[] functionsColor, ArrayList v){
        Dataset ds = null;
        String query = "SELECT D.DATASET_NAME, D.NB_COL, D.NB_ROW " +
                " FROM DATASET D WHERE " +
                "D.ID_DATASET = "+dbKey+" ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("D.DATASET_NAME");
        listFields.add("D.NB_COL");
        listFields.add("D.NB_ROW");

        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String name = rs.getColumnData("D.DATASET_NAME");
            if (name == null)
                continue;
            String s = rs.getColumnData("D.NB_COL");
            if (s == null)
                continue;
            int nbCol = 0;
            try{
                nbCol = Integer.parseInt(s);
            }catch(NumberFormatException e){
                // System.out.println(e);
            }
            s = rs.getColumnData("D.NB_ROW");
            if (s == null)
                continue;
            int nbRow = 0;
            try{
                nbRow = Integer.parseInt(s);
            }catch(NumberFormatException e){
                // System.out.println(e);
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
            // System.out.println("chargement dataset, nbVis : "+listVis.size());
            //creation du dataset
            ds= new Dataset(dbKey, mission,-1, name, nbCol, nbRow, tabDataHeader, data, listOp, listVis, DataConstants.NONE_RIGHT);
        }
        v.add(ds);
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
        // recup dans labbook des labdocs
        dbC.updateDb(DataConstants.DB_LABBOOK);
        ArrayList<Long> listLabDoc = new ArrayList();
        String query = "SELECT D.ID_LABDOC FROM LABDOC D, MISSION_CONF C " +
                "WHERE C.ID_MISSION_CONF = D.ID_MISSION_CONF " +
                "AND C.ID_LEARNER_GROUP IN (SELECT ID_LEARNER_GROUP FROM LINK_GROUP_LEARNER WHERE ID_LEARNER = "+dbKeyUser+" ) ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("D.ID_LABDOC");
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError()){
            dbC.updateDb(DataConstants.DB_LABBOOK_FITEX);
            return cr;
        }
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("D.ID_LABDOC");
            try{
                Long ld = Long.parseLong(s);
                listLabDoc.add(ld);
            }catch(NumberFormatException e){

            }
        }
        dbC.updateDb(DataConstants.DB_LABBOOK_FITEX);
        //recup des dataset
        ArrayList<Dataset> listDataset = new ArrayList();
        int nbLD = listLabDoc.size();
        if(nbLD == 0){
            v.add(listDataset);
            return new CopexReturn();
        }
        String listLD = "";
        for(int i=0; i<nbLD; i++){
            listLD += listLabDoc.get(i);
            if(i < nbLD-1)
                listLD += ", ";
        }
        query = "SELECT D.ID_DATASET, D.DATASET_NAME  " +
                " FROM DATASET D, LINK_LABDOC_PROC L   " +
                "WHERE D.ID_LABDOC IN ("+listLD+") AND "+
                "L.ID_DATASET = D.ID_DATASET  ;";
        v2 = new ArrayList();
        listFields = new ArrayList();
        listFields.add("D.ID_DATASET");
        listFields.add("D.DATASET_NAME");

         cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        nbR = v2.size();
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
            Dataset ds= new Dataset(dbKey, name, DataConstants.NONE_RIGHT);
            listDataset.add(ds);
        }
        v.add(listDataset);
        return new CopexReturn();
    }
    
    /* chargement de tous les ds header d'un dataset donne */
    public static CopexReturn getAllDatasetHeaderFromDB(DataBaseCommunication dbC, long dbKeyDs,  int nbCol, ArrayList v){
        DataHeader[] tabHeader = new DataHeader[nbCol] ;
        String query = "SELECT D.ID_HEADER, D.VALUE,D.UNIT, D.NO_COL, D.TYPE, D.DESCRIPTION, D.FORMULA_VALUE, D.SCIENTIFIC_NOTATION, D.NB_SHOWN_DECIMALS, D.NB_SIGNIFICANT_DIGITS, D.DATA_ALIGNMENT " +
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
        listFields.add("D.FORMULA_VALUE");
        listFields.add("D.SCIENTIFIC_NOTATION");
        listFields.add("D.NB_SHOWN_DECIMALS");
        listFields.add("D.NB_SIGNIFICANT_DIGITS");
        listFields.add("D.DATA_ALIGNMENT");
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
                // System.out.println(e);
            }
            String type = rs.getColumnData("D.TYPE");
            if (type == null)
                continue;
            String description = rs.getColumnData("D.DESCRIPTION");
            if (description == null)
                continue;
            String formulaValue = rs.getColumnData("D.FORMULA_VALUE");
            if(formulaValue != null && formulaValue.equals(""))
                formulaValue = null;
            boolean scientificNotation = false;
            s = rs.getColumnData("D.SCIENTIFIC_NOTATION");
            try{
                int b = Integer.parseInt(s);
                scientificNotation = (b ==1);
            }catch(NumberFormatException e){
            }
            int nbShownDecimal = DataConstants.NB_DECIMAL_UNDEFINED;
            s = rs.getColumnData("D.NB_SHOWN_DECIMALS");
            try{
                nbShownDecimal = Integer.parseInt(s);
            }catch(NumberFormatException e){
            }
            int nbSignificantDigits = DataConstants.NB_SIGNIFICANT_DIGITS_UNDEFINED;
            s = rs.getColumnData("D.NB_SIGNIFICANT_DIGITS");
            try{
                nbSignificantDigits = Integer.parseInt(s);
            }catch(NumberFormatException e){
            }
            s = rs.getColumnData("D.DATA_ALIGNMENT");
            int dataAlignment = DataConstants.DEFAULT_DATASET_ALIGNMENT;
            try{
                int f = Integer.parseInt(s);
                if(f == SwingConstants.LEFT || f == SwingConstants.CENTER || f == SwingConstants.RIGHT)
                    dataAlignment = f;
            }catch(NumberFormatException e){

            }
            DataHeader dh= new DataHeader(dbKey, value,unit, noCol, type, description, formulaValue, scientificNotation, nbShownDecimal, nbSignificantDigits, dataAlignment);
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
                // System.out.println(e);
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
                    // System.out.println(e);
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
                // System.out.println(e);
            }
            s = rs.getColumnData("D.NO_ROW");
            if (s == null)
                continue;
            int noRow = 0;
            try{
                noRow = Integer.parseInt(s);
            }catch(NumberFormatException e){
                // System.out.println(e);
            }
            s = rs.getColumnData("D.IS_IGNORED");
            if (s == null)
                continue;
            int val = 0;
            try{
                val = Integer.parseInt(s);
            }catch(NumberFormatException e){
                // System.out.println(e);
            }
            boolean isIgnored = val == 1;
            Data d = new Data(dbKey, value, noRow, noCol, isIgnored);
            tabData[noRow][noCol] = d;
        }
        v.add(tabData);
        return new CopexReturn();
    }

    /* creation d'un dataset vierge - en v[0] le nouveau dbKey */
    public static CopexReturn createDatasetInDB(DataBaseCommunication dbC, String name, int nbRow, int nbCol, long dbKeyLabDoc, ArrayList v){
        name = MyUtilities.replace("\'",name,"''") ;
        String query = "INSERT INTO DATASET (ID_DATASET, DATASET_NAME, NB_COL, NB_ROW) VALUES (NULL, '"+name+"', "+nbCol+", "+nbRow+") ;";
        String queryID = "SELECT max(last_insert_id(`ID_DATASET`))   FROM DATASET ;";
        ArrayList v2 = new ArrayList();
        CopexReturn cr = dbC.getNewIdInsertInDB(query, queryID, v2);
        if (cr.isError())
            return cr;
        long dbKey = (Long)v2.get(0);
        // lien mission /user
        query = "INSERT INTO LINK_LABDOC_DATASET (ID_DATASET, ID_LABDOC) VALUES ("+dbKey+", "+dbKeyLabDoc+") ;";
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
        String[] querys = new String[21];
        // suppression des visualisations associees 
        String queryDelVisType = "DELETE FROM LINK_VISUALIZATION_TYPE WHERE ID_DATA_VISUALIZATION IN (SELECT ID_DATA_VISUALIZATION FROM LINK_DATASET_VISUALIZATION WHERE ID_DATASET = "+dbKeyDataset+ ") ;";
        String queryDelVisNo = "DELETE FROM PIE_BAR_VISUALIZATION WHERE ID_DATA_VISUALIZATION IN (SELECT ID_DATA_VISUALIZATION FROM  LINK_DATASET_VISUALIZATION WHERE ID_DATASET = "+dbKeyDataset+ " ) ;";
        String queryDelPlot = "DELETE FROM PLOT_XY_GRAPH WHERE ID_PLOT IN (SELECT ID_PLOT FROM LINK_GRAPH_PLOT WHERE ID_DATA_VISUALIZATION  IN (SELECT ID_DATA_VISUALIZATION FROM LINK_DATASET_VISUALIZATION WHERE ID_DATASET = "+dbKeyDataset+ "));";
        String queryDelLinkPlot = "DELETE FROM LINK_GRAPH_PLOT WHERE ID_DATA_VISUALIZATION  IN (SELECT ID_DATA_VISUALIZATION FROM LINK_DATASET_VISUALIZATION WHERE ID_DATASET = "+dbKeyDataset+ ") ;";
        String queryDelParamGraph = "DELETE FROM PARAM_VIS_GRAPH WHERE ID_DATA_VISUALIZATION IN (SELECT ID_DATA_VISUALIZATION FROM LINK_DATASET_VISUALIZATION WHERE ID_DATASET = "+dbKeyDataset+ ") ;";
        String queryDelFunctionParam = "DELETE FROM FUNCTION_PARAM WHERE ID_FUNCTION_PARAM IN (SELECT ID_FUNCTION_PARAM FROM LINK_FUNCTION_PARAM WHERE ID_FUNCTION_MODEL IN (SELECT ID_FUNCTION_MODEL FROM LINK_GRAPH_FUNCTION_MODEL WHERE ID_DATA_VISUALIZATION  IN (SELECT ID_DATA_VISUALIZATION FROM LINK_DATASET_VISUALIZATION WHERE ID_DATASET = "+dbKeyDataset+ "))) ;";
        String queryDelLinkFunctionParam = "DELETE FROM LINK_FUNCTION_PARAM WHERE ID_FUNCTION_MODEL IN (SELECT ID_FUNCTION_MODEL FROM LINK_GRAPH_FUNCTION_MODEL WHERE ID_DATA_VISUALIZATION  IN (SELECT ID_DATA_VISUALIZATION FROM LINK_DATASET_VISUALIZATION WHERE ID_DATASET = "+dbKeyDataset+ "));";
        String queryDelFunctionModel = "DELETE FROM FUNCTION_MODEL WHERE ID_FUNCTION_MODEL IN (SELECT ID_FUNCTION_MODEL FROM LINK_GRAPH_FUNCTION_MODEL WHERE ID_DATA_VISUALIZATION  IN (SELECT ID_DATA_VISUALIZATION FROM LINK_DATASET_VISUALIZATION WHERE ID_DATASET = "+dbKeyDataset+ "));";
        String queryDelLinkFunctionModel = "DELETE FROM LINK_GRAPH_FUNCTION_MODEL WHERE ID_DATA_VISUALIZATION  IN (SELECT ID_DATA_VISUALIZATION FROM LINK_DATASET_VISUALIZATION WHERE ID_DATASET = "+dbKeyDataset+ ") ;";
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
        // suppression du lien entre user/ labdoc
        String queryDelUserMission = "DELETE FROM LINK_LABDOC_DATASET WHERE ID_DATASET = "+dbKeyDataset+" ;";
        // bloc de requete
        int i=0;
        querys[i++] = queryDelVisType;
        querys[i++] = queryDelVisNo ;
        querys[i++] = queryDelPlot;
        querys[i++] = queryDelLinkPlot;
        querys[i++] = queryDelParamGraph ;
        querys[i++] = queryDelFunctionParam;
        querys[i++] = queryDelLinkFunctionParam;
        querys[i++] = queryDelFunctionModel;
        querys[i++] = queryDelLinkFunctionModel;
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
    public static CopexReturn createDataHeaderInDB(DataBaseCommunication dbC, String value, String unit, int noCol, String type, String description, String formulaValue, boolean scientificNotation, int nbShownDecimals, int nbSignificantDigits, long dbKeyDs, ArrayList v){
        value = MyUtilities.replace("\'",value,"''") ;
        unit = MyUtilities.replace("\'",unit,"''") ;
        type = MyUtilities.replace("\'",type,"''") ;
        description = MyUtilities.replace("\'",description,"''") ;
        if(formulaValue != null)
            formulaValue = MyUtilities.replace("\'",formulaValue,"''") ;
        String s = "NULL";
        if(formulaValue != null)
            s = "'"+formulaValue+"'";
        int sn = scientificNotation ? 1:0;
        String nbShDec = "NULL";
        if(nbShownDecimals != DataConstants.NB_DECIMAL_UNDEFINED)
            nbShDec = Integer.toString(nbShownDecimals);
        String nbSigDig = "NULL";
        if(nbSignificantDigits != DataConstants.NB_SIGNIFICANT_DIGITS_UNDEFINED)
            nbSigDig = Integer.toString(nbSignificantDigits);
        String query = "INSERT INTO DATA_HEADER (ID_HEADER, VALUE, UNIT,NO_COL, TYPE, DESCRIPTION, FORMULA_VALUE, SCIENTIFIC_NOTATION, NB_SHOWN_DECIMALS, NB_SIGNIFICANT_DIGITS) VALUES (NULL, '"+value+"', '"+unit+"', "+noCol+", '"+type+"', '"+description+"', "+s+", "+sn+", "+nbShDec+", "+nbSigDig+") ;";
        // System.out.println("createDataHeaderInDB : "+query);
        String queryID = "SELECT max(last_insert_id(`ID_HEADER`))   FROM DATA_HEADER ;";
        // System.out.println("createDataHeaderInDB : "+queryID);
        ArrayList v2 = new ArrayList();
        CopexReturn cr = dbC.getNewIdInsertInDB(query, queryID, v2);
        if (cr.isError())
            return cr;
        long dbKey = (Long)v2.get(0);
        // System.out.println("==> dbKey : "+dbKey);
        // lien dataset / header
        query = "INSERT INTO LINK_DATASET_HEADER (ID_DATASET, ID_HEADER) VALUES ("+dbKeyDs+", "+dbKey+") ;";
        // System.out.println("cree lien : "+query);
        String[] querys = new String[1];
        querys[0] = query;
        v2 = new ArrayList();
        cr = dbC.executeQuery(querys, v2);
        v.add(dbKey);
        return cr;
    }

    /* mise a jour d'un header */
    public static CopexReturn updateDataHeaderInDB(DataBaseCommunication dbC, long dbKey, String value, String unit, String description,String type, String formulaValue, boolean scientificNotation, int nbShownDecimals, int nbSignificantDigits){
        value = MyUtilities.replace("\'",value,"''") ;
        unit = MyUtilities.replace("\'",unit,"''") ;
        description = MyUtilities.replace("\'",description,"''") ;
        type = MyUtilities.replace("\'",type,"''") ;
        if(formulaValue != null)
            formulaValue = MyUtilities.replace("\'",formulaValue,"''") ;
        ArrayList v = new ArrayList();
        String[] querys = new String[1];
        String s = "NULL";
        if(formulaValue != null)
            s = "'"+formulaValue+"'";
        int sn = scientificNotation ? 1:0;
        String nbShDec = "NULL";
        if(nbShownDecimals != DataConstants.NB_DECIMAL_UNDEFINED)
            nbShDec = Integer.toString(nbShownDecimals);
        String nbSigDig = "NULL";
        if(nbSignificantDigits != DataConstants.NB_SIGNIFICANT_DIGITS_UNDEFINED)
            nbSigDig = Integer.toString(nbSignificantDigits);
        String query = "UPDATE DATA_HEADER SET VALUE = '"+value+"', UNIT= '"+unit+"' , DESCRIPTION = '"+description+"', TYPE = '"+type+"', FORMULA_VALUE = "+s+", SCIENTIFIC_NOTATION = "+sn+", NB_SHOWN_DECIMALS = "+nbShDec+", NB_SIGNIFICANT_DIGITS = "+nbSigDig+"  WHERE ID_HEADER = "+dbKey+" ;";
        querys[0] = query ;
        CopexReturn cr = dbC.executeQuery(querys, v);
        return cr;
    }

    public static CopexReturn updateHeaderFormulaInDB(DataBaseCommunication dbC, long dbKey, String formulaValue){
        if(formulaValue != null)
            formulaValue = MyUtilities.replace("\'",formulaValue,"''") ;
        ArrayList v = new ArrayList();
        String[] querys = new String[1];
        String s = "NULL";
        if(formulaValue != null)
            s = "'"+formulaValue+"'";
        String query = "UPDATE DATA_HEADER SET FORMULA_VALUE = "+s+" WHERE ID_HEADER = "+dbKey+" ;";
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

    

    /* creation d'un dataset - retourne en v[0] le dataset avec les dbKey*/
    public static CopexReturn createDatasetInDB(DataBaseCommunication dbC, Dataset ds, long dbKeylabDoc, ArrayList v){
        // creation dataset
        ArrayList v2 = new ArrayList();
        CopexReturn cr = createDatasetInDB(dbC, ds.getName(), ds.getNbRows(), ds.getNbCol(), dbKeylabDoc, v2);
        if (cr.isError())
            return cr;
        long dbKey = (Long)v2.get(0);
        ds.setDbKey(dbKey);
        // creation des header
        DataHeader[] tabHeader = ds.getListDataHeader();
        for (int i=0; i<tabHeader.length; i++){
            v2 = new ArrayList();
            cr = createDataHeaderInDB(dbC, tabHeader[i].getValue(),tabHeader[i].getUnit(), tabHeader[i].getNoCol(), tabHeader[i].getType(), tabHeader[i].getDescription(), tabHeader[i].getFormulaValue(),tabHeader[i].isScientificNotation(), tabHeader[i].getNbShownDecimals(), tabHeader[i].getNbSignificantDigits(),  dbKey, v2);
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
                // System.out.println("d ... "+d);
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
            String s = rs.getColumnData("P.ID_PLOT");
            if(s == null)
                continue;
            long dbKeyPlot = Long.parseLong(s);
            s = rs.getColumnData("P.ID_HEADER_X");
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
            PlotXY plot = new PlotXY(dbKeyPlot, headerX, headerY, plotColor);
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
                "WHERE L.ID_FUNCTION_MODEL = "+dbKeyFunction+" AND L.ID_FUNCTION_PARAM = P.ID_FUNCTION_PARAM ;";
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
        String query = "SELECT F.ID_FUNCTION_MODEL, F.DESCRIPTION, F.F_TYPE, F.ID_FUNCTION_COLOR, F.ID_PREDEF_FUNCTION " +
                "FROM FUNCTION_MODEL F, LINK_GRAPH_FUNCTION_MODEL  L " +
                "WHERE L.ID_DATA_VISUALIZATION = "+dbKeyGraph+" AND L.ID_FUNCTION_MODEL = F.ID_FUNCTION_MODEL ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("F.ID_FUNCTION_MODEL");
        listFields.add("F.DESCRIPTION");
        listFields.add("F.F_TYPE");
        listFields.add("F.ID_FUNCTION_COLOR");
        listFields.add("F.ID_PREDEF_FUNCTION");
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
            s = rs.getColumnData("F.F_TYPE");
            char type = s.charAt(0);
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
            String idPredefFunction = rs.getColumnData("F.ID_PREDEF_FUNCTION");
            FunctionModel fm = new FunctionModel(dbKey, description, type, functionColor, listParam, idPredefFunction);
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

    public static CopexReturn setPreviewLabdocInDB(DataBaseCommunication dbC, long dbKeyLabdoc, String preview){
        preview =  MyUtilities.replace("\'",preview,"''") ;
        String query = "UPDATE LABDOC SET PREVIEW =  '"+preview+"' WHERE ID_LABDOC = "+dbKeyLabdoc+" ; ";
        dbC.updateDb(DataConstants.DB_LABBOOK);
        CopexReturn cr = dbC.executeQuery(query);
        if(cr.isError()){
            dbC.updateDb(DataConstants.DB_LABBOOK_FITEX);
            return cr;
        }
        dbC.updateDb(DataConstants.DB_LABBOOK_FITEX);
        return new CopexReturn();
    }

    /* update the text alignment for the selected headers */
    public static CopexReturn updateJustifyTextInDB(DataBaseCommunication dbC, int align, ArrayList<DataHeader> listHeader){
        if(listHeader.isEmpty())
            return new CopexReturn();
        String listH = ""+listHeader.get(0).getDbKey();
        int nbH = listHeader.size();
        for (int i=1; i<nbH; i++){
            listH += ", "+listHeader.get(i).getDbKey() ;
        }
        String query = "UPDATE DATA_HEADER SET DATA_ALIGNMENT = "+align+" WHERE ID_HEADER  IN ("+listH+");";
        ArrayList v2 = new ArrayList();
        String[] querys = new String[1];
        querys[0]  =query ;
        CopexReturn cr = dbC.executeQuery(querys, v2);
        return cr;
    }

    

}
