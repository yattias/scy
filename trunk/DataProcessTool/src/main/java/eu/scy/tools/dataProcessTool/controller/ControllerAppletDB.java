/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.controller;

import java.util.ArrayList;
import eu.scy.tools.dataProcessTool.db.*;
import eu.scy.tools.dataProcessTool.common.*;
import eu.scy.tools.dataProcessTool.dataTool.MainDataToolPanel;
import eu.scy.tools.dataProcessTool.dnd.SubData;
import eu.scy.tools.dataProcessTool.pdsELO.BarVisualization;
import eu.scy.tools.dataProcessTool.pdsELO.GraphVisualization;
import eu.scy.tools.dataProcessTool.pdsELO.IgnoredData;
import eu.scy.tools.dataProcessTool.pdsELO.PieVisualization;
import eu.scy.tools.dataProcessTool.pdsELO.ProcessedData;
import eu.scy.tools.dataProcessTool.pdsELO.ProcessedDatasetELO;
import eu.scy.tools.dataProcessTool.utilities.CopexReturn;
import eu.scy.tools.dataProcessTool.utilities.DataConstants;
import eu.scy.tools.dataProcessTool.utilities.MyConstants;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import org.jdom.Element;
import org.jdom.JDOMException;
import roolo.elo.JDomStringConversion;

/**
 * controller de l'applet
 * @author Marjolaine Bodin
 */
public class ControllerAppletDB implements ControllerInterface{

    // PROPERTY 
    /* interface */
    private MainDataToolPanel viewInterface;
    private ScyApplet applet;
    /* bd */
    private DataBaseCommunication dbC;
    /* mission */
    private Mission mission ;
    /* utilisateur */
    private ToolUser toolUser;
    
    /* liste des types d'operations possibles */
    private TypeOperation[] tabTypeOperations;
    /* liste des visualisations possibles */
    private TypeVisualization[] tabTypeVisual;
    /* liste des dataset liés à la mission */
    private ArrayList<Dataset> listDataSet;

    private long dbKeyMission;
    private long dbKeyUser;

    // CONSTRUCTOR 
    public ControllerAppletDB(ScyApplet applet, MainDataToolPanel dataToolPanel, long dbKeyMission, long dbKeyUser ) {
        this.applet = applet;
        this.viewInterface = dataToolPanel ;
        this.dbKeyMission = dbKeyMission ;
        this.dbKeyUser = dbKeyUser ;
    }
    
    
    // METHODE
    @Override
    public CopexReturn load(){
        return load(this.dbKeyMission, this.dbKeyUser);
    }


    /* chargement et initialisation des données pour une msision et un user donné */
    public CopexReturn load(long dbKeyMission, long dbKeyUser){
        System.out.println("debut load "+dbKeyMission+" - "+dbKeyUser);
        dbC = new DataBaseCommunication(applet, MyConstants.DB_COPEX, dbKeyMission, dbKeyUser);
        // initialisation de l'utilisateur et de la mission
        System.out.println("load mission");
        ArrayList v = new ArrayList();
        dbC.updateDb(MyConstants.DB_COPEX);
        CopexReturn cr = CopexMissionFromDB.loadMissionFromDB(dbC, dbKeyMission, v);
        if (cr.isError()){
            return cr;
        }
        mission = (Mission)v.get(0);
        System.out.println("mission : "+dbKeyMission+"  - "+(mission == null));
        System.out.println("debut user");
        v = new ArrayList();
        cr = ToolUserFromDB.loadDataUserFromDB(dbC, dbKeyUser, v);
        if (cr.isError()){
            return cr;
        }
        toolUser = (ToolUser)v.get(0);
        System.out.println("toolUser : "+dbKeyUser+"  - "+(toolUser == null));
        if(toolUser == null || mission == null){
            System.out.println("est null : "+toolUser+" - "+mission) ;
            return  new CopexReturn(applet.getBundleString("MSG_ERROR_LOAD_DATA"), false);
        }
        dbC.updateDb(MyConstants.DB_COPEX_DATA);
        // chargement des données
        // chargement des types d'operation
        System.out.println("load type operation");
        v = new ArrayList();
        cr = OperationFromDB.getAllTypeOperation(dbC,getLocale(), v);
        if(cr.isError())
            return cr;
        tabTypeOperations = (TypeOperation[])v.get(0);
        // liste des visualisations possibles
        System.out.println("load visualization");
        cr  = loadTypeVisual();
        if (cr.isError())
            return cr;
        System.out.println("load dataset");
        v = new ArrayList();
        cr = DatasetFromDB.getAllDatasetFromDB(dbC, toolUser.getDbKey(), mission, tabTypeOperations,tabTypeVisual, v);
        if (cr.isError()){
            return cr;
        }
        listDataSet = (ArrayList<Dataset>)v.get(0);
        
        // clone les objets
        System.out.println("clone");
        TypeVisualization[] tabTypeVisualC = new TypeVisualization[tabTypeVisual.length];
        for (int i=0; i<tabTypeVisual.length; i++){
            tabTypeVisualC[i] = (TypeVisualization)tabTypeVisual[i].clone();
        }
        TypeOperation[] tabTypeOpC = new TypeOperation[tabTypeOperations.length];
        for (int i=0; i<tabTypeOperations.length; i++){
            tabTypeOpC[i] = (TypeOperation)tabTypeOperations[i].clone();
        }
        ArrayList<Dataset> listDataSetC = new ArrayList();
        int nb = this.listDataSet.size();
        for (int i=0; i<nb; i++){
            listDataSetC.add((Dataset)listDataSet.get(i).clone());
        }
        System.out.println("init applet");
        this.viewInterface.initDataTool(tabTypeVisualC, tabTypeOpC, listDataSetC);
        System.out.println("fin load");
        return new CopexReturn();
    }

   

    /* locale de l'applet */
    private Locale getLocale(){
        return this.viewInterface.getLocale() ;
    }
    
    
     /* retourne vrai si de type ds , pds sinon */
    private boolean isDatasetType(String xmlString){
       Element el=  new JDomStringConversion().stringToXml(xmlString);
       if (el.getName().equals(ProcessedDatasetELO.TAG_ELO_CONTENT)){
           return false;
       }
       return true;
    }
    
    // lecture d'un fichier
    private CopexReturn readFile(String fileName, ArrayList v){
        String xmlString = "";
        try{
            URL urlDB;
            urlDB = new URL(applet.getCodeBase(), applet.getDirectoryELO()+fileName);
            HttpURLConnection urlCon = (HttpURLConnection)urlDB.openConnection();
            urlCon.setDoOutput(true);
            urlCon.setDoInput(true);
            urlCon.setRequestMethod("POST");
            urlCon.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlCon.getInputStream(), "utf8"));
            String ligne;
            while ((ligne = reader.readLine()) != null) {
               //System.out.println("ligne lue : "+ligne);
               xmlString += ligne;
           }
            reader.close();
            urlCon.disconnect();
        }catch(IOException e){
            return new CopexReturn(e.getMessage(), false);
        }
        v.add(xmlString);
        return new CopexReturn();
    }

   /* retourne un dataset construit à partir d'un ELO dataset */
    private Dataset getDataset(roolo.elo.content.dataset.DataSet eloDs, String name){
        // pas d'operations, pas de visualization
        ArrayList<DataOperation> listOperation = new ArrayList();
        ArrayList<Visualization> listVisualization = new ArrayList();
        // header
        roolo.elo.content.dataset.DataSetHeader header =  eloDs.getHeader(getLocale()) ;
        if (header == null)
            header = eloDs.getHeaders().get(0);
        int nbCols = header.getColumnCount() ;
        DataHeader[] dataHeader = new DataHeader[nbCols];
        for (int i=0; i<nbCols; i++){
            dataHeader[i] = new DataHeader(-1, header.getColumns().get(i).getSymbol(), i) ;
        }
        // data
        List<roolo.elo.content.dataset.DataSetRow> listRows = eloDs.getValues() ;
        int nbRows = listRows.size() ;
        Data[][] data = new Data[nbRows][nbCols];
        for (int i=0; i<nbRows; i++){
            for (int j=0; j<nbCols; j++){
                String s = listRows.get(i).getValues().get(j);
                double value;
                try{
                    value = Double.parseDouble(s);
                }catch(NumberFormatException e){
                    return null;
                }
                data[i][j] = new Data(-1, value, i, j, false);
            }
        }
        // creation du dataset
        Dataset ds = new Dataset(-1, name, nbCols, nbRows,  dataHeader, data, listOperation,listVisualization  );

        return ds;
    }
    
    /* chargement des types de visualisation possible */
    private CopexReturn loadTypeVisual(){
        ArrayList v = new ArrayList();
        CopexReturn cr = VisualizationFromDB.getAllTypeVisualizationFromDB(dbC, v);
        if (cr.isError())
            return cr;
        tabTypeVisual = (TypeVisualization[])v.get(0);
        return new CopexReturn();
    }
    
    /* retourne l'indice du dataset -1 sinon */
    private int getIdDataset(Dataset ds){
        int nb = this.listDataSet.size();
        for (int i=0; i<nb; i++){
            if (listDataSet.get(i).getDbKey() == ds.getDbKey())
                return i;
        }
        return -1;
    }

    /* retourne l'indice de l'operation du dataset -1 sinon */
    private int getIdOperation(Dataset ds, DataOperation operation){
        int nb = ds.getListOperation().size();
        for (int i=0; i<nb; i++){
            if (ds.getListOperation().get(i).getDbKey() == operation.getDbKey())
                return i;
        }
        return -1;
    }
    /* creation d'une table retourne en v[0] l'objet DataSet créé */
    @Override
    public CopexReturn createTable(String name, ArrayList v) {
        // par defaut on créé une table de 10 lignes par 2 colonnes
        int nbRows = 10;
        int nbCol = 2;
        ArrayList<DataOperation> listOp = new ArrayList();
        ArrayList<Visualization> listVis = new ArrayList();
        Data[][] data = new Data[nbRows][nbCol];
        DataHeader[] tabHeader = new DataHeader[nbCol] ;
        // enregistrement base 
        ArrayList v2 = new ArrayList();
        CopexReturn cr = DatasetFromDB.createDatasetInDB(dbC, name, nbRows,  nbCol, toolUser.getDbKey(), mission.getDbKey(), v2);
        if (cr.isError())
            return cr;
        long dbKey = (Long)v2.get(0);
        // creation ds
        Dataset ds = new Dataset(dbKey, name, nbCol, nbRows, tabHeader, data, listOp, listVis);
        this.listDataSet.add(ds);
        v.add(ds.clone());
        return new CopexReturn();
        
    }


    /* fermeture d'un dataset */
    @Override
    public CopexReturn closeDataset(Dataset ds){
        int id = getIdDataset(ds);
        if (id == -1)
            return new CopexReturn(applet.getBundleString("MSG_ERROR_CLOSE_DATA_TABLE"), false);
        listDataSet.get(id).setOpen(false);
        return new CopexReturn();
    }

    /* suppression d'un dataset */
    @Override
    public CopexReturn deleteDataset(Dataset ds){
        int id = getIdDataset(ds);
        if (id == -1)
            return new CopexReturn(applet.getBundleString("MSG_ERROR_DELETE_DATA_TABLE"), false);
        // suppression en base
        CopexReturn cr = DatasetFromDB.deleteDatasetFromDB(dbC, ds.getDbKey());
        if (cr.isError())
            return cr;
        // suppression en memoire
        this.listDataSet.remove(id);
        return new CopexReturn();
    }

    /* renommer la feuille de données */
    @Override
    public CopexReturn updateDatasetName(Dataset ds, String newName){
        int id = getIdDataset(ds);
        if (id == -1)
            return new CopexReturn(applet.getBundleString("MSG_ERROR_UPDATE_DATASET_NAME"), false);
        // maj base
        CopexReturn cr = DatasetFromDB.updateDatasetNameInDB(dbC, ds.getDbKey(), newName);
        if (cr.isError())
            return cr;
        // memoire
        this.listDataSet.get(id).setName(newName);
        return new CopexReturn();
    }

    /*change le statut valeur ignorée - retourne en v[0] le nouveau dataset */
    @Override
    public CopexReturn setDataIgnored(Dataset ds, boolean isIgnored, ArrayList<Data> listData, ArrayList v){
        int id = getIdDataset(ds);
        if (id == -1)
            return new CopexReturn(applet.getBundleString("MSG_ERROR_DATA_IGNORED"), false);
        // maj base
        CopexReturn cr = DatasetFromDB.setDataIgnoredInDB(dbC, listData, isIgnored);
        if (cr.isError())
            return cr;
        // maj en memoire
        Data[][] datas = this.listDataSet.get(id).getData() ;
        int nb = listData.size();
        for (int k=0; k<nb; k++){
            Data d = listData.get(k);
            datas[d.getNoRow()][d.getNoCol()].setIsIgnoredData(isIgnored);
        }
         // recalcule des operations
        this.listDataSet.get(id).calculateOperation();
        // en v[0] le nouveau dataset clone
        v.add(this.listDataSet.get(id).clone());
        return new CopexReturn();
    }

    /* creation d'une nouvelle operation - retourne en v[0] le nouveau dataset et en v[1] le nouveau DataOperation */
    @Override
    public CopexReturn createOperation(Dataset ds, int typeOperation, boolean isOnCol, ArrayList<Integer> listNo, ArrayList v){
        int id = getIdDataset(ds);
        if (id == -1)
            return new CopexReturn(applet.getBundleString("MSG_ERROR_CREATE_OPERATION"), false);

        long dbKey = -1;
        TypeOperation typeOp = getTypeOperation(typeOperation);
        if (typeOp == null)
            return new CopexReturn(applet.getBundleString("MSG_ERROR_CREATE_OPERATION"), false);
        String name = typeOp.getCodeName() ;
        DataOperation operation = new DataOperation(dbKey, name, typeOp, isOnCol, listNo) ;
        // creation en base
        ArrayList v2 = new ArrayList();
        CopexReturn cr = OperationFromDB.createOperationInDB(dbC, ds.getDbKey(), operation, v2);
        if (cr.isError())
            return cr;
        dbKey = (Long)v2.get(0);
        // creation en memoire
        this.listDataSet.get(id).addOperation(operation);
        // en v[0] le nouveau dataset clone et en v[1] le dataOperation
        v.add(this.listDataSet.get(id).clone());
        v.add(operation.clone());
        return new CopexReturn();
    }

    /* creation d'une nouvelle operation parametree - retourne en v[0] le nouveau dataset et en v[1] le nouveau DataOperation */
    @Override
    public CopexReturn createOperationParam(Dataset ds, int typeOperation, boolean isOnCol, ArrayList<Integer> listNo,String[] tabValue,  ArrayList v){
        int id = getIdDataset(ds);
        if (id == -1)
            return new CopexReturn(applet.getBundleString("MSG_ERROR_CREATE_OPERATION"), false);

        long dbKey = -1;
        TypeOperation typeOp = getTypeOperation(typeOperation);
        if (typeOp == null)
            return new CopexReturn(applet.getBundleString("MSG_ERROR_CREATE_OPERATION"), false);
        String name = typeOp.getCodeName() ;
        ParamOperation[] allParam = getParamOperation((TypeOperationParam)typeOp, tabValue);
        DataOperationParam operation = new DataOperationParam(dbKey, name, typeOp, isOnCol, listNo, allParam) ;
        // creation en base
        ArrayList v2 = new ArrayList();
        CopexReturn cr = OperationFromDB.createOperationInDB(dbC, ds.getDbKey(), operation, v2);
        if (cr.isError())
            return cr;
        dbKey = (Long)v2.get(0);
        ParamOperation[] allParamDB = (ParamOperation[])v2.get(1);
        for (int i=0; i<allParam.length; i++){
            allParam[i].setDbKey(allParamDB[i].getDbKey());
        }
        operation.setAllParam(allParam);
        // creation en memoire
        this.listDataSet.get(id).addOperation(operation);
        // en v[0] le nouveau dataset clone et en v[1] le dataOperation
        v.add(this.listDataSet.get(id).clone());
        v.add(operation.clone());
        return new CopexReturn();
    }

    /* retourne les parametres de l'operation */
    private ParamOperation[] getParamOperation(TypeOperationParam typeOp, String[] tabValue ){
        ParamOperation[]  allParam = new ParamOperation[typeOp.getNbParam()];
        for (int i=0; i<tabValue.length; i++){
            Double value= null;
            if (tabValue[i].length() > 0)
                value = Double.parseDouble(tabValue[i]);
            ParamOperation p = new ParamOperation(-1, value, i);
            allParam[i] = p;
        }
        return allParam ;
    }

    /* recherche du type d'operation - null sinon */
    private TypeOperation getTypeOperation(int t){
        for (int i=0; i<tabTypeOperations.length; i++){
            if (tabTypeOperations[i].getType() == t)
                return tabTypeOperations[i] ;
        }
        return null;
    }

    /* recherche du type d'operation - null sinon */
    private TypeOperation getTypeOperation(String symbol){
        for (int i=0; i<tabTypeOperations.length; i++){
            if (tabTypeOperations[i].getCodeName().equals(symbol))
                return tabTypeOperations[i] ;
        }
        return null;
    }


    /* mise à jour d'une valeur : titre header */
    @Override
    public CopexReturn updateDataHeader(Dataset ds, int colIndex, String title, ArrayList v){
        int id = getIdDataset(ds);
        if (id == -1)
            return new CopexReturn(applet.getBundleString("MSG_ERROR_UPDATE_DATA"), false);
        // header existe t il deja ?
        DataHeader header = this.listDataSet.get(id).getDataHeader(colIndex);
        if(header == null){
            // header n'existe pas : on le cree
            ArrayList v2 = new ArrayList();
            CopexReturn cr = DatasetFromDB.createDataHeaderInDB(dbC, title, colIndex, this.listDataSet.get(id).getDbKey(), v2);
            if (cr.isError())
                return cr;
            long dbKey = (Long)v2.get(0);
            this.listDataSet.get(id).setDataHeader(new DataHeader(dbKey, title, colIndex), colIndex);
        }else{
            // header existe => on le modifie
            CopexReturn cr = DatasetFromDB.updateDataHeaderInDB(dbC, header.getDbKey(), title);
            if (cr.isError())
                return cr;
            header.setValue(title);
            this.listDataSet.get(id).setDataHeader(header, header.getNoCol());
        }

        // en v[0] : le nouveau dataset
        v.add(this.listDataSet.get(id).clone());
        return new CopexReturn();
    }

    /* mise à jour d'une valeur : titre operation */
    @Override
    public CopexReturn updateDataOperation(Dataset ds, DataOperation operation, String title, ArrayList v){
        int id = getIdDataset(ds);
        if (id == -1)
            return new CopexReturn(applet.getBundleString("MSG_ERROR_UPDATE_DATA"), false);
        int idOp = getIdOperation(this.listDataSet.get(id), operation);
        if (idOp == -1)
            return new CopexReturn(applet.getBundleString("MSG_ERROR_UPDATE_DATA"), false);
        // modif en base
        CopexReturn cr  = OperationFromDB.updateOperationTitleInDB(dbC, operation.getDbKey(), title);
        if (cr.isError())
            return cr;
        // maj en memoire
        this.listDataSet.get(id).getListOperation().get(idOp).setName(title);
        // en v[0] : le nouveau dataset
        v.add(this.listDataSet.get(id).clone());
        return new CopexReturn();
    }

    /* mise à jour d'une valeur : donnee dataset */
    @Override
    public CopexReturn updateData(Dataset ds, int rowIndex, int colIndex, double value, ArrayList v){
        int id = getIdDataset(ds);
        if (id == -1)
            return new CopexReturn(applet.getBundleString("MSG_ERROR_UPDATE_DATA"), false);
        // data existe t il deja ?
        Data data = this.listDataSet.get(id).getData(rowIndex, colIndex);
        if(data == null){
            // data n'existe pas : on le cree
            ArrayList v2 = new ArrayList();
            CopexReturn cr = DatasetFromDB.createDataInDB(dbC, value, rowIndex, colIndex, this.listDataSet.get(id).getDbKey(), v2);
            if (cr.isError())
                return cr;
            long dbKey = (Long)v2.get(0);
            this.listDataSet.get(id).setData(new Data(dbKey,value, rowIndex, colIndex, false), rowIndex, colIndex);
        }else{
            // data existe => on le modifie
            CopexReturn cr = DatasetFromDB.updateDataInDB(dbC, data.getDbKey(), value);
            if (cr.isError())
                return cr;
            data.setValue(value);
            this.listDataSet.get(id).setData(data, rowIndex, colIndex);
        }

        // en v[0] : le nouveau dataset
        v.add(this.listDataSet.get(id).clone());
        return new CopexReturn();
    }

    /* fermeture d'une visualization */
    @Override
    public CopexReturn closeVisualization(Dataset ds, Visualization vis){
        int id = getIdDataset(ds);
        if (id == -1)
            return new CopexReturn(applet.getBundleString("MSG_ERROR_CLOSE_GRAPH"), false);
        int idVis = ds.getIdVisualization(vis.getDbKey()) ;
        if (idVis == -1 )
            return new CopexReturn(applet.getBundleString("MSG_ERROR_CLOSE_GRAPH"), false);
        listDataSet.get(id).getListVisualization().get(idVis).setOpen(false);
        return new CopexReturn();
    }

    /* suppression d'une visualization */
    @Override
    public CopexReturn deleteVisualization(Dataset ds, Visualization vis){
        int id = getIdDataset(ds);
        if (id == -1)
            return new CopexReturn(applet.getBundleString("MSG_ERROR_DELETE_GRAPH"), false);
        int idVis = ds.getIdVisualization(vis.getDbKey()) ;
        if (idVis == -1 )
            return new CopexReturn(applet.getBundleString("MSG_ERROR_DELETE_GRAPH"), false);
        // suppression en base
        CopexReturn cr = VisualizationFromDB.deleteVisualizationFromDB(dbC, listDataSet.get(id).getListVisualization().get(idVis).getDbKey());
        if (cr.isError())
            return cr;
        // suppression en memoire
        this.listDataSet.remove(id);
        return new CopexReturn();
    }

    /* creation d'une visualization - renvoit en v[0] le nouveua dataset et en v[1] l'objet visualization */
    @Override
    public CopexReturn createVisualization(Dataset ds, Visualization vis, ArrayList v){
        int id = getIdDataset(ds);
        if (id == -1)
            return new CopexReturn(applet.getBundleString("MSG_ERROR_CREATE_GRAPH"), false);
        // creation en base
        ArrayList v2 = new ArrayList();
        CopexReturn cr = VisualizationFromDB.createVisualizationInDB(dbC, listDataSet.get(id).getDbKey(), vis, v2);
        if (cr.isError())
            return cr;
        long dbKey = (Long)v2.get(0);
        // memoire
        vis.setDbKey(dbKey);
        listDataSet.get(id).getListVisualization().add(vis);
        // en v[0] le nouveau ds clone et en v[1] le nouveau vis clone
        v.add(listDataSet.get(id).clone());
        v.add(vis.clone());
        return new CopexReturn();
    }

    /* update nom graphe */
    @Override
    public CopexReturn updateVisualizationName(Dataset ds, Visualization vis, String newName){
         int id = getIdDataset(ds);
        if (id == -1)
            return new CopexReturn(applet.getBundleString("MSG_ERROR_UPDATE_VISUALIZATION_NAME"), false);
        int idVis = ds.getIdVisualization(vis.getDbKey()) ;
        if (idVis == -1 )
            return new CopexReturn(applet.getBundleString("MSG_ERROR_UPDATE_VISUALIZATION_NAME"), false);
        // changement en base
        CopexReturn cr = VisualizationFromDB.updateVisualizationTitleInDB(dbC,listDataSet.get(id).getListVisualization().get(idVis).getDbKey() , newName);
        // memoire
        listDataSet.get(id).getListVisualization().get(idVis).setName(newName);
        return new CopexReturn();
    }

    

   
    /* suppression de données et/ou operation sur un dataset */
    @Override
    public CopexReturn deleteData(boolean confirm, Dataset ds, ArrayList<Data> listData, ArrayList<DataOperation> listOperation, ArrayList<Integer>[] listRowAndCol){
        int id = getIdDataset(ds);
        if (id == -1)
            return new CopexReturn(applet.getBundleString("MSG_ERROR_DELETE_DATA"), false);
        Dataset myDs = listDataSet.get(id);
        // suppression des operations
        CopexReturn cr = OperationFromDB.deleteOperationFromDB(dbC,  listOperation);
        if (cr.isError())
            return cr;
        int nbOp = listOperation.size();
        for (int i=0; i<nbOp; i++){
            DataOperation op = listOperation.get(i);
            myDs.removeOperation(op);
        }
        int nbRowsSel = listRowAndCol[0].size();
        int nbColsSel = listRowAndCol[1].size();
        // suppression de données :
        // si toutes les colonnes ou toutes les lignes sont sel : on supprime tout le dataset apres confirmation
        boolean allData = nbRowsSel == myDs.getNbRows() || nbColsSel == myDs.getNbCol();
        if (allData){
            if (confirm){
                // suppression du dataset
                cr = deleteDataset(myDs);
                if (cr.isError())
                    return cr;
                viewInterface.removeDataset(ds);
            }else{
                cr = new CopexReturn(applet.getBundleString("MSG_CONFIRM_DELETE_DATASET"), true);
                cr.setConfirm(applet.getBundleString("MSG_CONFIRM_DELETE_DATASET"), true);
                return cr;
            }
        }else{
            //suppression d'une partie des données
            // si il y a des colonnes / lignes entieres => on supprime eventuellement les operations sur ces colonnes ou les visualization sur ces colonnes
            ArrayList<DataHeader> listDataHeader = new ArrayList();
            ArrayList<Integer> listNoHeader = new ArrayList();
            ArrayList listNoToDel = new ArrayList();
            ArrayList<DataOperation> listOperationToDel = new ArrayList();
            ArrayList<Visualization> listVisualizationToDel = new ArrayList();
            if(nbColsSel > 0){
                // suppression header
                for (int i=0; i<nbColsSel; i++){
                    if (myDs.getDataHeader(listRowAndCol[1].get(i)) != null)
                        listDataHeader.add(myDs.getDataHeader(listRowAndCol[1].get(i)));
                    listNoHeader.add(listRowAndCol[1].get(i));
                }
                cr = DatasetFromDB.deleteDataHeaderFromDB(dbC, listDataHeader);
                if (cr.isError())
                    return cr;
            }
            if (nbRowsSel > 0 || nbColsSel > 0){
                // on supprime les operations liees à ces colonnes ou ces lignes
                // puis on supprime les operations qui ne portent plus sur aucune colonne ou ligne
                ArrayList<DataOperation> myListOp = myDs.getListOperation();
                int nbTotOp = myListOp.size();
                ArrayList<DataOperation> copyListOp = new ArrayList();
                for (int i=0; i<nbTotOp; i++){
                    copyListOp.add((DataOperation)myListOp.get(i).clone());
                }
                for (int i=0; i<nbRowsSel; i++){
                    for (int j=0; j<nbTotOp; j++){
                        if (!myListOp.get(j).isOnCol() && myListOp.get(j).getListNo().contains(listRowAndCol[0].get(i))){
                            ArrayList toDel = new ArrayList();
                            toDel.add(myListOp.get(j).getDbKey());
                            toDel.add(listRowAndCol[0].get(i));
                            listNoToDel.add(toDel);
                            copyListOp.get(j).getListNo().remove(listRowAndCol[0].get(i));
                        }
                    }
                }
                for (int i=0; i<nbColsSel; i++){
                    for (int j=0; j<nbTotOp; j++){
                        if (myListOp.get(j).isOnCol() &&  myListOp.get(j).getListNo().contains(listRowAndCol[1].get(i))){
                            ArrayList toDel = new ArrayList();
                            toDel.add(myListOp.get(j).getDbKey());
                            toDel.add(listRowAndCol[1].get(i));
                            listNoToDel.add(toDel);
                            copyListOp.get(j).getListNo().remove(listRowAndCol[1].get(i));
                        }
                    }
                }
                cr = OperationFromDB.deleteNoOperationFromDB(dbC, listNoToDel);
                if (cr.isError())
                    return cr;
                // si des operations ne portent plus sur rien on les supprime
                for (int i=0; i<nbTotOp; i++){
                    if (copyListOp.get(i).getListNo().size() == 0){
                        listOperationToDel.add(copyListOp.get(i));
                    }
                }
                cr = OperationFromDB.deleteOperationFromDB(dbC, listOperationToDel);
                if (cr.isError())
                    return cr;

                // visualization : si porte sur la ligne ou la colonne => suppression
                ArrayList<Visualization> myListVis = myDs.getListVisualization();
                int nbTotVis = myListVis.size();
                for (int i=0; i<nbRowsSel; i++){
                    for (int j=0; j<nbTotVis; j++){
                        if (!myListVis.get(j).isOnCol() && myListVis.get(j).isOnNo(listRowAndCol[0].get(i))){
                            listVisualizationToDel.add(myListVis.get(j));
                        }
                    }
                }
                for (int i=0; i<nbColsSel; i++){
                    for (int j=0; j<nbTotVis; j++){
                        if (myListVis.get(j).isOnCol() && myListVis.get(j).isOnNo(listRowAndCol[1].get(i))){
                            listVisualizationToDel.add(myListVis.get(j));
                        }
                    }
                }
                cr = VisualizationFromDB.deleteVisualizationFromDB(dbC, listVisualizationToDel);
                if (cr.isError())
                    return cr;

            }
            // suppression des donnees
            cr = DatasetFromDB.deleteDataFromDB(dbC, listData);
            if (cr.isError())
                return cr;
            // mise à jour en memoire et appel de l'applet
            //remove listDataHeader
            for (int i=0; i<listNoHeader.size(); i++){
                myDs.removeHeader(listNoHeader.get(i));
            }
            //remove listNoToDel
            for (int i=0; i<listNoToDel.size(); i++){
                long dbKeyOp = (Long)((ArrayList)listNoToDel.get(i)).get(0);
                int no = (Integer)((ArrayList)listNoToDel.get(i)).get(1);
                myDs.removeNoOperation(dbKeyOp, no);
            }
            //remove listOperationToDel
            for(int i=0; i<listOperationToDel.size(); i++){
                myDs.removeOperation(listOperationToDel.get(i));
            }
             //remove listVisualizationToDel
            for(int i=0; i<listVisualizationToDel.size(); i++){
                myDs.removeVisualization(listVisualizationToDel.get(i));
            }
            //remove data
            myDs.removeData(listRowAndCol);
            // mise à jour des no : header, data, operation, visualizaion
            cr = DatasetFromDB.updateNoInDB(dbC, myDs);
            if (cr.isError())
                return cr;
            // appel applet
            viewInterface.updateDataset((Dataset)myDs.clone());

        }
        return new CopexReturn();

    }

     /* chargement d'un ELO */
    @Override
    public CopexReturn loadELO(String xmlString){
        String name = "ELO";
        Dataset ds;
        if (isDatasetType(xmlString)){
            roolo.elo.content.dataset.DataSet eloDs ;
            try{
            eloDs = new roolo.elo.content.dataset.DataSet(xmlString) ;
            }catch(JDOMException e){
                return new CopexReturn(""+e, false);
            }
            ds = getDataset(eloDs, name);
            if (ds == null)
            return new CopexReturn(viewInterface.getBundleString("MSG_ERROR_FLOAT_VALUE"), false);
        }else{
            try{
                ProcessedDatasetELO edoPds = new ProcessedDatasetELO(xmlString);
                ds = getPDS(edoPds, name);
                if (ds == null)
            return new CopexReturn(viewInterface.getBundleString("MSG_ERROR_FLOAT_VALUE"), false);
            }catch(JDOMException e){
                   return new CopexReturn(""+e, false);
            }
        }
        listDataSet.add(ds);
        this.viewInterface.createDataset((Dataset)ds.clone());
        return new CopexReturn();
    }

    /* retourne un dataset construit à partir d'un ELO pds */
    private Dataset getPDS(ProcessedDatasetELO eloPDS, String name){
        // pas d'operations, pas de visualization
        ArrayList<DataOperation> listOperation = new ArrayList();
        ArrayList<Visualization> listVisualization = new ArrayList();
        // header
        roolo.elo.content.dataset.DataSet eloDs = eloPDS.getDataset();
        roolo.elo.content.dataset.DataSetHeader header =  eloDs.getHeader(getLocale()) ;
        if (header == null)
            header = eloDs.getHeaders().get(0);
        int nbCols = header.getColumnCount() ;
        DataHeader[] dataHeader = new DataHeader[nbCols];
        for (int i=0; i<nbCols; i++){
            dataHeader[i] = new DataHeader(-1, header.getColumns().get(i).getSymbol(), i) ;
        }
        // data
        List<roolo.elo.content.dataset.DataSetRow> listRows = eloDs.getValues() ;
        int nbRows = listRows.size() ;
        Data[][] data = new Data[nbRows][nbCols];
        for (int i=0; i<nbRows; i++){
            for (int j=0; j<nbCols; j++){
                String s = listRows.get(i).getValues().get(j);
                double value;
                try{
                    value = Double.parseDouble(s);
                }catch(NumberFormatException e){
                    return null;
                }
                data[i][j] = new Data(-1, value, i, j, false);
            }
        }
        // liste des données ignorées
        ProcessedData pds = eloPDS.getProcessedData();
        IgnoredData ig = pds.getIgnoredData();
        List<eu.scy.tools.dataProcessTool.pdsELO.Data> listData = ig.getListIgnoredData();
        if (listData != null){
        for (int i=0; i<listData.size(); i++){
            String noCols = listData.get(i).getColumnId();
            String noRows = listData.get(i).getRowId() ;
            System.out.println("PDS ELO, ignored data : "+noRows+" /"+noCols);
            int noCol = -1;
            int noRow= -1;
            try{
                noCol = Integer.parseInt(noCols);
                noRow = Integer.parseInt(noRows);
            }catch(NumberFormatException e){
                continue;
            }
            if (noRow > -1 && noRow <data.length & noCol >-1 && data[noRow] != null && noCol <data[noRow].length ){
                System.out.println("mise en mémoire...");
                data[noRow][noCol].setIsIgnoredData(true);
            }
        }
        }
        // liste des operations
        List<eu.scy.tools.dataProcessTool.pdsELO.Operation> listOp = pds.getListOperations();
        for (int i=0; i<listOp.size(); i++){
            eu.scy.tools.dataProcessTool.pdsELO.Operation op = listOp.get(i);
            List<String> listRef = op.getReferences();
            ArrayList<Integer> listNo = new ArrayList();
            for (int k=0; k<listRef.size(); k++){
                try{
                    int n = Integer.parseInt(listRef.get(k));
                    listNo.add(n);
                }catch(NumberFormatException e){
                        continue;
                }
            }
            DataOperation myOp = new DataOperation(-1, op.getName(), getTypeOperation(op.getSymbol()), op.isIsOnCol(), listNo);
            listOperation.add(myOp);
        }
        // liste des visualization
        List<eu.scy.tools.dataProcessTool.pdsELO.Visualization> listVis = pds.getListVisualization();
        for (int i=0; i<listVis.size(); i++){
            eu.scy.tools.dataProcessTool.pdsELO.Visualization vis = listVis.get(i);
            TypeVisualization type = getTypeVisualization(vis.getType());
            Visualization myVis = null;
            int[] tabNo  = new int[1];
            if (vis instanceof BarVisualization){
                tabNo[0] =((BarVisualization)vis).getId();
                 myVis = new Visualization(-1, vis.getName(), type, tabNo, vis.isIsOnCol()) ;
            } else if (vis instanceof PieVisualization){
                tabNo[0] =((PieVisualization)vis).getId();
                myVis = new Visualization(-1, vis.getName(), type, tabNo, vis.isIsOnCol()) ;
            } else if(vis instanceof GraphVisualization){
                GraphVisualization g = ((GraphVisualization)vis);
                tabNo = new int[2];
                tabNo[0] = g.getX_axis();
                tabNo[1] = g.getY_axis();
                ParamGraph paramGraph = new ParamGraph(g.getXName(), g.getYName(), g.getXMin(), g.getXMax(), g.getYMin(), g.getYMax(), g.getDeltaX(), g.getDeltaY());
                ArrayList<FunctionModel> listFunctionModel  = null;
                List<eu.scy.tools.dataProcessTool.pdsELO.FunctionModel> lfm = g.getListFunctionModel();
                if (lfm != null){
                    listFunctionModel = new ArrayList();
                    int n = lfm.size();
                    for(int j=0; j<n; j++){
                        FunctionModel fm = new FunctionModel(-1, lfm.get(j).getDescription(), new Color(lfm.get(j).getColorR(),lfm.get(j).getColorG(),lfm.get(j).getColorB() ));
                        listFunctionModel.add(fm);
                    }
                }
                myVis = new Graph(-1, vis.getName(), type, tabNo, vis.isIsOnCol(), paramGraph, listFunctionModel);
            }
            listVisualization.add(myVis);
        }
        // creation du dataset
        Dataset ds = new Dataset(-1, name, nbCols, nbRows,  dataHeader, data, listOperation,listVisualization  );
        return ds;
    }

     private TypeVisualization getTypeVisualization(String codeType){
        int code = 0;
        if (codeType.equals(DataConstants.TYPE_VIS_BAR))
            code = DataConstants.VIS_BAR;
        else if (codeType.equals(DataConstants.TYPE_VIS_GRAPH))
            code = DataConstants.VIS_GRAPH ;
        else if (codeType.equals(DataConstants.TYPE_VIS_HISTO))
            code = DataConstants.VIS_HISTO ;
        else if (codeType.equals(DataConstants.TYPE_VIS_PIE))
            code = DataConstants.VIS_PIE;
        for (int i=0; i<tabTypeVisual.length; i++){
            if(tabTypeVisual[i].getCode() == code)
                return tabTypeVisual[i];
        }
        return null;
    }

   @Override
    public Element getPDS(Dataset ds){
        int id = getIdDataset(ds);
        if (id == -1 ){
            return null;
        }
        Dataset dataset = listDataSet.get(id);
        // ELO format
        ProcessedDatasetELO pdsELO = dataset.toELO(viewInterface.getLocale());
        return pdsELO.toXML() ;

    }

   /* ajout ou modification d'une fonction modeme */
    @Override
    public CopexReturn setFunctionModel(Dataset ds, Visualization vis, String description, Color fColor, ArrayList v){
        int id = getIdDataset(ds);
        if (id == -1)
            return new CopexReturn(viewInterface.getBundleString("MSG_ERROR_SET_FUNCTION_MODEL"), false);
        Dataset myDs = listDataSet.get(id);
        int idVis = myDs.getIdVisualization(vis.getDbKey()) ;
        if (idVis == -1)
            return new CopexReturn(viewInterface.getBundleString("MSG_ERROR_SET_FUNCTION_MODEL"), false);
        Visualization myVis = myDs.getListVisualization().get(idVis);
        if (! (myVis instanceof Graph))
            return new CopexReturn(viewInterface.getBundleString("MSG_ERROR_SET_FUNCTION_MODEL"), false);
        Graph graph = (Graph)myVis;
        FunctionModel fm = graph.getFunctionModel(fColor);
        if (fm == null){
            // creation de la fonction
            // creation en base
            ArrayList v2 = new ArrayList();
            CopexReturn cr = VisualizationFromDB.createFunctionModelInDB(dbC, graph.getDbKey(), description, fColor,  v2);
            if (cr.isError())
                return cr;
            long dbKey = (Long)v2.get(0);
            // memoire
            FunctionModel myFm = new FunctionModel(dbKey, description, fColor);
            ((Graph)listDataSet.get(id).getListVisualization().get(idVis)).addFunctionModel(myFm);
        }else{
            //mise à jour
            if (description == null || description.length() == 0){
                // suppression
                CopexReturn cr = VisualizationFromDB.deleteFunctionModelFromDB(dbC, fm.getDbKey());
                if (cr.isError())
                    return cr;
                ((Graph)listDataSet.get(id).getListVisualization().get(idVis)).deleteFunctionModel(fColor);
            }else{
                // modification
                CopexReturn cr = VisualizationFromDB.updateFunctionModelInDB(dbC, fm.getDbKey(), description);
                if (cr.isError())
                    return cr;
                fm.setDescription(description);
            }
        }
        v.add(myDs.clone());
        return new CopexReturn();
    }

    /* insertion de lignes ou de colonnes */
    @Override
    public CopexReturn  insertData(Dataset ds,  boolean isOnCol, int nb, int idBefore, ArrayList v){
        int id = getIdDataset(ds);
        if (id == -1)
            return new CopexReturn(viewInterface.getBundleString("MSG_ERROR_INSERT_DATA"), false);
        Dataset myDs = listDataSet.get(id);
        int newNbRow = myDs.getNbRows() ;
        int newNbCol = myDs.getNbCol() ;
        if (isOnCol)
            newNbCol += nb;
        else
            newNbRow += nb ;
        CopexReturn cr = DatasetFromDB.updateDatasetMatriceInDB(dbC, myDs.getDbKey(), newNbRow, newNbCol);
        if (cr.isError())
            return cr;
        if (isOnCol)
            myDs.insertCol(nb, idBefore);
        else
            myDs.insertRow(nb, idBefore);

        cr = DatasetFromDB.updateNoInDB(dbC, myDs);
        if (cr.isError())
            return cr;
        v.add(myDs.clone());
        return new CopexReturn();
    }


    /* impression */
    @Override
    public CopexReturn printDataset(ArrayList<Dataset> listDsToPrint, boolean isAllVis){
        return new CopexReturn("Not yet implemented", false);
    }

    /* drag and drop de colonnes */
    @Override
    public CopexReturn moveSubData(SubData subDataToMove, int noColInsertBefore, ArrayList v){
        return new CopexReturn();
    }

    /* mise à jour dataset apres sort */
    // TODO  enregistre on en base 
    @Override
    public CopexReturn updateDatasetRow(Dataset ds, Vector exchange, ArrayList v){
        int id = getIdDataset(ds);
        if (id == -1)
            return new CopexReturn(viewInterface.getBundleString("MSG_ERROR_SORT"), false);
        Dataset myDs = listDataSet.get(id);
        myDs.exchange(exchange);
        v.add(myDs.clone());
        return new CopexReturn();
    }

}
