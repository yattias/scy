/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.controller;

import java.util.ArrayList;

import eu.scy.elo.contenttype.dataset.DataSet;
import eu.scy.elo.contenttype.dataset.DataSetColumn;
import eu.scy.elo.contenttype.dataset.DataSetHeader;
import eu.scy.elo.contenttype.dataset.DataSetRow;
import eu.scy.tools.dataProcessTool.db.*;
import eu.scy.tools.dataProcessTool.common.*;
import eu.scy.tools.dataProcessTool.dataTool.DataProcessToolPanel;
import eu.scy.tools.dataProcessTool.dataTool.DataTableModel;
import eu.scy.tools.dataProcessTool.dnd.SubData;
import eu.scy.tools.dataProcessTool.pdsELO.BarVisualization;
import eu.scy.tools.dataProcessTool.pdsELO.GraphVisualization;
import eu.scy.tools.dataProcessTool.pdsELO.IgnoredData;
import eu.scy.tools.dataProcessTool.pdsELO.PieVisualization;
import eu.scy.tools.dataProcessTool.pdsELO.ProcessedData;
import eu.scy.tools.dataProcessTool.pdsELO.ProcessedDatasetELO;
import eu.scy.tools.dataProcessTool.print.DataPrint;
import eu.scy.tools.dataProcessTool.utilities.CopexReturn;
import eu.scy.tools.dataProcessTool.utilities.DataConstants;
import eu.scy.tools.dataProcessTool.utilities.MyConstants;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;
import org.jdom.Element;
import org.jdom.JDOMException;
import roolo.elo.JDomStringConversion;

/**
 * controller de l'applet
 * @author Marjolaine Bodin
 */
public class DataControllerDB implements ControllerInterface{

    // PROPERTY 
    /* interface */
    private DataProcessToolPanel dataToolPanel;
    /* bd */
    private DataBaseCommunication dbC;
    /* url */
    private URL dataURL;
    /* mission */
    private Mission mission ;
    /* utilisateur */
    private ToolUser toolUser;
    
    /* liste des types d'operations possibles */
    private TypeOperation[] tabTypeOperations;
    /* liste des visualisations possibles */
    private TypeVisualization[] tabTypeVisual;
    /* dataset */
    private Dataset dataset;

    private long dbKeyMission;
    private long dbKeyUser;

    // CONSTRUCTOR 
    public DataControllerDB(DataProcessToolPanel dataToolPanel, URL url, long dbKeyMission, long dbKeyUser ) {
        this.dataToolPanel = dataToolPanel ;
        this.dbKeyMission = dbKeyMission ;
        this.dbKeyUser = dbKeyUser ;
        this.dataURL = url;
        dbC = new DataBaseCommunication(dataURL, MyConstants.DB_COPEX, dbKeyMission, ""+dbKeyUser);
    }
    
    
    // METHODE
    @Override
    public CopexReturn load(){
        return load(this.dbKeyMission, this.dbKeyUser);
    }


    /* chargement et initialisation des donnees pour une msision et un user donne */
    public CopexReturn load(long dbKeyMission, long dbKeyUser){
        System.out.println("debut load "+dbKeyMission+" - "+dbKeyUser);
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
            return  new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_LOAD_DATA"), false);
        }
        dbC.updateDb(MyConstants.DB_COPEX_DATA);
        // chargement des donnees
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
        dataset = (Dataset)v.get(0);
        
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
        Dataset dsC = (Dataset)dataset.clone();
        System.out.println("init applet");
        this.dataToolPanel.initDataProcessingTool(tabTypeVisualC, tabTypeOpC, dsC);
        System.out.println("fin load");
        return new CopexReturn();
    }

   

    /* locale de l'applet */
    private Locale getLocale(){
        return this.dataToolPanel.getLocale() ;
    }
    
    
     /* retourne vrai si de type ds , pds sinon */
    private boolean isDatasetType(String xmlString){
       Element el=  new JDomStringConversion().stringToXml(xmlString);
       if (el.getName().equals(ProcessedDatasetELO.TAG_ELO_CONTENT)){
           return false;
       }
       return true;
    }
    
    

   /* retourne un dataset construit a  partir d'un ELO dataset */
    private Dataset getDataset(DataSet eloDs, String name){
        // pas d'operations, pas de visualization
        ArrayList<DataOperation> listOperation = new ArrayList();
        ArrayList<Visualization> listVisualization = new ArrayList();
        // header
        DataSetHeader header =  eloDs.getHeader(getLocale()) ;
        if (header == null)
            header = eloDs.getHeaders().get(0);
        int nbCols = header.getColumnCount() ;
        DataHeader[] dataHeader = new DataHeader[nbCols];
        for (int i=0; i<nbCols; i++){
            dataHeader[i] = new DataHeader(-1, header.getColumns().get(i).getSymbol(),header.getColumns().get(i).getType(),  i) ;
        }
        // data
        List<DataSetRow> listRows = eloDs.getValues() ;
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
    
   

    /* retourne l'indice de l'operation du dataset -1 sinon */
    private int getIdOperation(Dataset ds, DataOperation operation){
        int nb = ds.getListOperation().size();
        for (int i=0; i<nb; i++){
            if (ds.getListOperation().get(i).getDbKey() == operation.getDbKey())
                return i;
        }
        return -1;
    }
    /* creation d'une table retourne en v[0] l'objet DataSet cree */
    public CopexReturn createTable(ArrayList v) {
        // par defaut on cree une table de 10 lignes par 2 colonnes
        int nbRows = 10;
        int nbCol = 2;
        ArrayList<DataOperation> listOp = new ArrayList();
        ArrayList<Visualization> listVis = new ArrayList();
        Data[][] data = new Data[nbRows][nbCol];
        DataHeader[] tabHeader = new DataHeader[nbCol] ;
        // enregistrement base 
        ArrayList v2 = new ArrayList();
        CopexReturn cr = DatasetFromDB.createDatasetInDB(dbC, "", nbRows,  nbCol, toolUser.getDbKey(), mission.getDbKey(), v2);
        if (cr.isError())
            return cr;
        long dbKey = (Long)v2.get(0);
        // creation ds
        dataset = new Dataset(dbKey, "", nbCol, nbRows, tabHeader, data, listOp, listVis);
        v.add(dataset.clone());
        return new CopexReturn();
        
    }


    

    /* suppression d'un dataset */
    @Override
    public CopexReturn deleteDataset(Dataset ds){
        // suppression en base
        CopexReturn cr = DatasetFromDB.deleteDatasetFromDB(dbC, dataset.getDbKey());
        if (cr.isError())
            return cr;
        // suppression en memoire
        dataset = null;
        return new CopexReturn();
    }

    /*creation dataset par default */
    @Override
    public CopexReturn createDefaultDataset(ArrayList v){
        return createTable(v);
    }

    /*change le statut valeur ignoree - retourne en v[0] le nouveau dataset */
    @Override
    public CopexReturn setDataIgnored(Dataset ds, boolean isIgnored, ArrayList<Data> listData, ArrayList v){
        CopexReturn cr = setDataIgnored(ds, isIgnored, listData);
        if(cr.isError())
            return cr;
         // recalcule des operations
        dataset.calculateOperation();
        // en v[0] le nouveau dataset clone
        v.add(dataset.clone());
        return new CopexReturn();
    }

    private CopexReturn setDataIgnored(Dataset ds, boolean isIgnored, ArrayList<Data> listData){
        // maj base
        CopexReturn cr = DatasetFromDB.setDataIgnoredInDB(dbC, listData, isIgnored);
        if (cr.isError())
            return cr;
        // maj en memoire
        Data[][] datas = dataset.getData() ;
        int nb = listData.size();
        for (int k=0; k<nb; k++){
            Data d = listData.get(k);
            datas[d.getNoRow()][d.getNoCol()].setIsIgnoredData(isIgnored);
        }
        return new CopexReturn();
    }

    /* creation d'une nouvelle operation - retourne en v[0] le nouveau dataset et en v[1] le nouveau DataOperation */
    @Override
    public CopexReturn createOperation(Dataset ds, int typeOperation, boolean isOnCol, ArrayList<Integer> listNo, ArrayList v){
        
        long dbKey = -1;
        TypeOperation typeOp = getTypeOperation(typeOperation);
        if (typeOp == null)
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_CREATE_OPERATION"), false);
        String name = typeOp.getCodeName() ;
        DataOperation operation = new DataOperation(dbKey, name, typeOp, isOnCol, listNo) ;
        // creation en base
        ArrayList v2 = new ArrayList();
        CopexReturn cr = OperationFromDB.createOperationInDB(dbC, ds.getDbKey(), operation, v2);
        if (cr.isError())
            return cr;
        dbKey = (Long)v2.get(0);
        // creation en memoire
        dataset.addOperation(operation);
        // en v[0] le nouveau dataset clone et en v[1] le dataOperation
        v.add(dataset.clone());
        v.add(operation.clone());
        return new CopexReturn();
    }

    /* creation d'une nouvelle operation parametree - retourne en v[0] le nouveau dataset et en v[1] le nouveau DataOperation */
    @Override
    public CopexReturn createOperationParam(Dataset ds, int typeOperation, boolean isOnCol, ArrayList<Integer> listNo,String[] tabValue,  ArrayList v){
        long dbKey = -1;
        TypeOperation typeOp = getTypeOperation(typeOperation);
        if (typeOp == null)
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_CREATE_OPERATION"), false);
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
        dataset.addOperation(operation);
        // en v[0] le nouveau dataset clone et en v[1] le dataOperation
        v.add(dataset.clone());
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


    /* mise a  jour d'une valeur : titre header */
    @Override
    public CopexReturn updateDataHeader(Dataset ds, int colIndex, String title, String unit, ArrayList v){
        CopexReturn cr = setDataHeader(ds, colIndex, title, unit);
        if(cr.isError())
            return cr;
        // en v[0] : le nouveau dataset
        v.add(dataset.clone());
        return new CopexReturn();
    }

    private CopexReturn setDataHeader(Dataset ds, int colIndex, String title, String unit){
        // header existe t il deja ?
        DataHeader header = dataset.getDataHeader(colIndex);
        if(header == null){
            // header n'existe pas : on le cree
            ArrayList v2 = new ArrayList();
            CopexReturn cr = DatasetFromDB.createDataHeaderInDB(dbC, title,unit,  colIndex, dataset.getDbKey(), v2);
            if (cr.isError())
                return cr;
            long dbKey = (Long)v2.get(0);
            dataset.setDataHeader(new DataHeader(dbKey, title, unit, colIndex), colIndex);
        }else{
            // header existe => on le modifie
            CopexReturn cr = DatasetFromDB.updateDataHeaderInDB(dbC, header.getDbKey(), title, unit);
            if (cr.isError())
                return cr;
            header.setValue(title);
            header.setUnit(unit);
            dataset.setDataHeader(header, header.getNoCol());
        }
        return new CopexReturn();
    }

    /* mise a  jour d'une valeur : titre operation */
    @Override
    public CopexReturn updateDataOperation(Dataset ds, DataOperation operation, String title, ArrayList v){
        int idOp = getIdOperation(dataset, operation);
        if (idOp == -1)
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_UPDATE_DATA"), false);
        // modif en base
        CopexReturn cr  = OperationFromDB.updateOperationTitleInDB(dbC, operation.getDbKey(), title);
        if (cr.isError())
            return cr;
        // maj en memoire
        dataset.getListOperation().get(idOp).setName(title);
        // en v[0] : le nouveau dataset
        v.add(dataset.clone());
        return new CopexReturn();
    }

    /* mise a  jour d'une valeur : donnee dataset */
    @Override
    public CopexReturn updateData(Dataset ds, int rowIndex, int colIndex, Double value, ArrayList v){
        CopexReturn cr = setData(ds, rowIndex, colIndex, value);
        if(cr.isError())
            return cr;
        // recalcule des operations
        dataset.calculateOperation();
        // en v[0] : le nouveau dataset
        v.add(dataset.clone());
        return new CopexReturn();
    }

    private CopexReturn setData(Dataset ds, int rowIndex, int colIndex, Double value){
        // data existe t il deja ?
        Data data = dataset.getData(rowIndex, colIndex);
        if(data == null && value != null){
            // data n'existe pas : on le cree
            ArrayList v2 = new ArrayList();
            CopexReturn cr = DatasetFromDB.createDataInDB(dbC, value, rowIndex, colIndex, dataset.getDbKey(), v2);
            if (cr.isError())
                return cr;
            long dbKey = (Long)v2.get(0);
            dataset.setData(new Data(dbKey,value, rowIndex, colIndex, false), rowIndex, colIndex);
        }else{
            // data existe => on le modifie
            if(value == null){
                setDataNull(ds, rowIndex, colIndex);
            }else{
                CopexReturn cr = DatasetFromDB.updateDataInDB(dbC, data.getDbKey(), value);
                if (cr.isError())
                    return cr;
                data.setValue(value);
                dataset.setData(data, rowIndex, colIndex);
            }
            
        }
        return new CopexReturn();
    }
    /* fermeture d'une visualization */
    @Override
    public CopexReturn closeVisualization(Dataset ds, Visualization vis){
        int idVis = ds.getIdVisualization(vis.getDbKey()) ;
        if (idVis == -1 )
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_CLOSE_GRAPH"), false);
        dataset.getListVisualization().get(idVis).setOpen(false);
        return new CopexReturn();
    }

    /* suppression d'une visualization */
    @Override
    public CopexReturn deleteVisualization(Dataset ds, Visualization vis){
        int idVis = ds.getIdVisualization(vis.getDbKey()) ;
        if (idVis == -1 )
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DELETE_GRAPH"), false);
        // suppression en base
        CopexReturn cr = VisualizationFromDB.deleteVisualizationFromDB(dbC, dataset.getListVisualization().get(idVis).getDbKey());
        if (cr.isError())
            return cr;
        // suppression en memoire
        dataset.getListVisualization().remove(idVis);
        return new CopexReturn();
    }

    /* creation d'une visualization - renvoit en v[0] le nouveua dataset et en v[1] l'objet visualization */
    // TODO enregistrement BD
    @Override
    public CopexReturn createVisualization(Dataset ds, Visualization vis, boolean findAxisParam, ArrayList v){
        // creation en base
        ArrayList v2 = new ArrayList();
        CopexReturn cr = VisualizationFromDB.createVisualizationInDB(dbC, dataset.getDbKey(), vis, v2);
        if (cr.isError())
            return cr;
        long dbKey = (Long)v2.get(0);
        if (vis instanceof Graph && findAxisParam){
           v2 = new ArrayList();
           cr = findAxisParam(ds, ((Graph)vis), v2);
           if(cr.isError())
               return cr;
           ParamGraph pg = (ParamGraph)(v2.get(0));
           ((Graph)vis).setParamGraph(pg);
       }
        // memoire
        vis.setDbKey(dbKey);
        dataset.getListVisualization().add(vis);
        // en v[0] le nouveau ds clone et en v[1] le nouveau vis clone
        v.add(dataset.clone());
        v.add(vis.clone());
        return new CopexReturn();
    }

    /* update nom graphe */
    @Override
    public CopexReturn updateVisualizationName(Dataset ds, Visualization vis, String newName){
        int idVis = ds.getIdVisualization(vis.getDbKey()) ;
        if (idVis == -1 )
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_UPDATE_VISUALIZATION_NAME"), false);
        // changement en base
        CopexReturn cr = VisualizationFromDB.updateVisualizationTitleInDB(dbC,dataset.getListVisualization().get(idVis).getDbKey() , newName);
        // memoire
        dataset.getListVisualization().get(idVis).setName(newName);
        return new CopexReturn();
    }

    

   
    /* suppression de donnees et/ou operation sur un dataset */
    @Override
    public CopexReturn deleteData(boolean confirm, Dataset ds, ArrayList<Data> listData, ArrayList<DataOperation> listOperation, ArrayList<Integer>[] listRowAndCol){
        Dataset myDs = dataset;
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
        // suppression de donnees :
        // si toutes les colonnes ou toutes les lignes sont sel : on supprime tout le dataset apres confirmation
        boolean allData = nbRowsSel == myDs.getNbRows() || nbColsSel == myDs.getNbCol();
        if (allData){
            if (confirm){
                // suppression du dataset
                cr = deleteDataset(myDs);
                if (cr.isError())
                    return cr;
                //dataToolPanel.removeDataset(dataset);
            }else{
                cr = new CopexReturn(dataToolPanel.getBundleString("MSG_CONFIRM_DELETE_DATASET"), true);
                cr.setConfirm(dataToolPanel.getBundleString("MSG_CONFIRM_DELETE_DATASET"), true);
                return cr;
            }
        }else{
            //suppression d'une partie des donnees
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
                // on supprime les operations liees a  ces colonnes ou ces lignes
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
            // mise a  jour en memoire et appel de l'applet
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
            // mise a  jour des no : header, data, operation, visualizaion
            cr = DatasetFromDB.updateNoInDB(dbC, myDs);
            if (cr.isError())
                return cr;
            // appel applet
            dataToolPanel.updateDataset((Dataset)myDs.clone());

        }
        return new CopexReturn();

    }

     /* chargement d'un ELO */
    @Override
    public CopexReturn loadELO(String xmlString){
        String name = "ELO";
        Dataset ds;
        if (isDatasetType(xmlString)){
            DataSet eloDs ;
            try{
            eloDs = new DataSet(xmlString) ;
            }catch(JDOMException e){
                return new CopexReturn(""+e, false);
            }
            ds = getDataset(eloDs, name);
            if (ds == null)
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_FLOAT_VALUE"), false);
        }else{
            try{
                ProcessedDatasetELO edoPds = new ProcessedDatasetELO(xmlString);
                ds = getPDS(edoPds, name);
                if (ds == null)
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_FLOAT_VALUE"), false);
            }catch(JDOMException e){
                   return new CopexReturn(""+e, false);
            }
        }
        dataset = ds;
        ///this.dataToolPanel.createDataset((Dataset)ds.clone());
        return new CopexReturn();
    }

    /* retourne un dataset construit a  partir d'un ELO pds */
    private Dataset getPDS(ProcessedDatasetELO eloPDS, String name){
        // pas d'operations, pas de visualization
        ArrayList<DataOperation> listOperation = new ArrayList();
        ArrayList<Visualization> listVisualization = new ArrayList();
        // header
        DataSet eloDs = eloPDS.getDataset();
        DataSetHeader header =  eloDs.getHeader(getLocale()) ;
        if (header == null)
            header = eloDs.getHeaders().get(0);
        int nbCols = header.getColumnCount() ;
        DataHeader[] dataHeader = new DataHeader[nbCols];
        for (int i=0; i<nbCols; i++){
            dataHeader[i] = new DataHeader(-1, header.getColumns().get(i).getSymbol(), header.getColumns().get(i).getType(),i) ;
        }
        // data
        List<DataSetRow> listRows = eloDs.getValues() ;
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
        // liste des donnees ignorees
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
                System.out.println("mise en memoire...");
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
                ParamGraph paramGraph = new ParamGraph(g.getXName(), g.getYName(), g.getXMin(), g.getXMax(), g.getYMin(), g.getYMax(), g.getDeltaX(), g.getDeltaY(), false);
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
        // ELO format
        ProcessedDatasetELO pdsELO = dataset.toELO(dataToolPanel.getLocale());
        return pdsELO.toXML() ;

    }

   /* ajout ou modification d'une fonction modeme */
    @Override
    public CopexReturn setFunctionModel(Dataset ds, Visualization vis, String description, Color fColor, ArrayList v){
        Dataset myDs = dataset;
        int idVis = myDs.getIdVisualization(vis.getDbKey()) ;
        if (idVis == -1)
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_SET_FUNCTION_MODEL"), false);
        Visualization myVis = myDs.getListVisualization().get(idVis);
        if (! (myVis instanceof Graph))
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_SET_FUNCTION_MODEL"), false);
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
            ((Graph)dataset.getListVisualization().get(idVis)).addFunctionModel(myFm);
        }else{
            //mise a  jour
            if (description == null || description.length() == 0){
                // suppression
                CopexReturn cr = VisualizationFromDB.deleteFunctionModelFromDB(dbC, fm.getDbKey());
                if (cr.isError())
                    return cr;
                ((Graph)dataset.getListVisualization().get(idVis)).deleteFunctionModel(fColor);
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
        int newNbRow = dataset.getNbRows() ;
        int newNbCol = dataset.getNbCol() ;
        if (isOnCol)
            newNbCol += nb;
        else
            newNbRow += nb ;
        CopexReturn cr = DatasetFromDB.updateDatasetMatriceInDB(dbC, dataset.getDbKey(), newNbRow, newNbCol);
        if (cr.isError())
            return cr;
        if (isOnCol)
            dataset.insertCol(nb, idBefore);
        else
            dataset.insertRow(nb, idBefore);

        cr = DatasetFromDB.updateNoInDB(dbC, dataset);
        if (cr.isError())
            return cr;
        v.add(dataset.clone());
        return new CopexReturn();
    }


    /* impression */
    @Override
    public CopexReturn printDataset(DataTableModel model, ArrayList<Object> listGraph){
        String fileName = "copex-"+mission.getCode();
        DataPrint pdfPrint = new DataPrint(dataToolPanel, mission, toolUser,dataset, model, listGraph, fileName );
        CopexReturn cr = pdfPrint.printDocument();
        return cr;
    }

    /* drag and drop de colonnes */
    @Override
    public CopexReturn moveSubData(SubData subDataToMove, int noColInsertBefore, ArrayList v){
        return new CopexReturn();
    }

    /* mise a  jour dataset apres sort */
    // TODO  enregistre on en base 
    @Override
    public CopexReturn updateDatasetRow(Dataset ds, Vector exchange, ArrayList v){
        dataset.exchange(exchange);
        v.add(dataset.clone());
        return new CopexReturn();
    }

    /* creation d'un dataset avec l'en tete - 1 ligne de donnees */
    @Override
    public CopexReturn createDataset(String name, String[] headers, String[] units, ArrayList v){
        int nbCol = headers.length;
        int nbRows = 1;
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

        for (int i=0; i<nbCol; i++){
            v2 = new ArrayList();
            cr = DatasetFromDB.createDataHeaderInDB(dbC, headers[i],units[i], i, dbKey, v2);
            if(cr.isError())
                return cr;
            long dbKeyH = (Long)v2.get(0);
            tabHeader[i] = new DataHeader(dbKeyH, headers[i], units[i], i);
        }
        // creation ds
        dataset = new Dataset(dbKey, name, nbCol, nbRows, tabHeader, data, listOp, listVis);
        v.add(dataset.clone());
        return new CopexReturn();
    }

   
    
    /* ajout d'une ligne de donnees */
    @Override
    public CopexReturn addData(long dbKeyDs, Double[] values, boolean autoScale, ArrayList v){
        if(values.length != dataset.getNbCol())
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_ADD_DATA"), false);
        ArrayList v2 = new ArrayList();
        // insertion en avnt derniere position
        int idBefore = dataset.getNbRows();
        CopexReturn cr = insertData(dataset, false, 1, idBefore, v2) ;
        if(cr.isError())
            return cr;
        // cree les valeurs
        int idRow = dataset.getNbRows() - 2;
        if (idRow <0)
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_ADD_DATA"), false);
        for (int i=0; i<values.length; i++){
            v2 = new ArrayList();
            cr = updateData(dataset, idRow, i, values[i], v2);
            if(cr.isError())
                return cr;
        }
        if (dataset.getListVisualization() != null && autoScale){
            for (int i=0; i<dataset.getListVisualization().size(); i++){
                if (dataset.getListVisualization().get(i) instanceof Graph){
                    v2 = new ArrayList();
                    cr = findAxisParam(dataset, (Graph)dataset.getListVisualization().get(i), v2);
                    if(cr.isError())
                        return cr;
                    ParamGraph pg = (ParamGraph)(v2.get(0));
                    ((Graph)(dataset.getListVisualization().get(i))).setParamGraph(pg);
                }
            }
        }
        v.add(dataset.clone());
        return new CopexReturn() ;
    }

    /*mise a  jour des param */
    // TODO enregistrement base
    @Override
    public CopexReturn setParamGraph(long dbKeyDs, long dbKeyVis, ParamGraph paramGraph, ArrayList v){
        int idVis = dataset.getIdVisualization(dbKeyVis);
        if (idVis == -1)
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_UPDATE_PARAM_GRAPH"), false);
        Visualization vis = dataset.getListVisualization().get(idVis);
        if (vis instanceof Graph){
            ((Graph)vis).setParamGraph(paramGraph);
            if (paramGraph.isAutoscale()){
                ArrayList v2 = new ArrayList();
                CopexReturn cr = findAxisParam(dataset, ((Graph)vis), v2);
                if(cr.isError())
                    return cr;
                ParamGraph pg = (ParamGraph)(v2.get(0));
                ((Graph)vis).setParamGraph(pg);
            }
        }
        v.add(vis.clone());
        return new CopexReturn();
    }

     /*recherche des axes */
    private CopexReturn findAxisParam(Dataset ds, Graph vis, ArrayList v){
        int[] tabNo = vis.getTabNo();
        double[] listX;
        double[] listY;
        if (vis.isOnCol()){
            listX = new double[ds.getNbRows()];
            listY = new double[ds.getNbRows()];
            for (int i=0; i<ds.getNbRows(); i++){
                listX[i] = ds.getData(i, tabNo[0]) == null ? 0 : ds.getData(i, tabNo[0]).getValue();
                listY[i] = ds.getData(i, tabNo[1]) == null ? 0 : ds.getData(i, tabNo[1]).getValue();
            }
        }else{
            // TODO
            listX = new double[0];
            listY = new double[0];
        }
        double minX = 0;
        double minY = 0;
        double maxX = 0;
        double maxY = 0 ;
        for (int i=0; i<listX.length; i++){
            if (listX[i] < minX)
                minX = listX[i];
            if (listX[i] > maxX)
                maxX = listX[i];
            if (listY[i] < minY)
                minY = listY[i];
            if (listY[i] > maxY)
                maxY = listY[i];
        }
        // +/- 10%
        double  x_min  = minX + minX/10 ;
        double  y_min  = minY +minY/10 ;
        double  x_max  = maxX +maxX/10 ;
        double  y_max  = maxY +maxY/10;
        double deltaX = (x_max - x_min) / 10 ;
        double deltaY = (y_max - y_min) / 10 ;
        ParamGraph pg = new ParamGraph(vis.getParamGraph().getX_name(), vis.getParamGraph().getY_name(), x_min, x_max, y_min, y_max, deltaX, deltaY, vis.getParamGraph().isAutoscale());

        v.add(pg);
        return new CopexReturn();
    }

     /* maj autoscale*/
    // TODO enregistrement BD
    @Override
    public CopexReturn setAutoScale(long dbKeyDs, long dbKeyVis, boolean autoScale, ArrayList v){
        int idVis = dataset.getIdVisualization(dbKeyVis);
        if (idVis == -1)
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_UPDATE_PARAM_GRAPH"), false);
        Visualization vis = dataset.getListVisualization().get(idVis);
        if (vis instanceof Graph){
            ((Graph)vis).getParamGraph().setAutoscale(autoScale);
        }
        v.add(vis);
        return new CopexReturn();
    }

    /*merge un ELO avec l'ELO courant*/
    @Override
    public CopexReturn mergeELO(Element elo){
        return new CopexReturn();
    }

    /* copie-colle */
    //TODO Bd
    @Override
    public CopexReturn paste(long dbKeyDs, Dataset subData, int[] selCell, ArrayList v){
        if (selCell == null){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_PASTE"), false);
        }
        int idR = selCell[0] ;
        int idC = selCell[1] ;
        int nbRowsToPaste = subData.getNbRows();
        int nbColsToPaste = subData.getNbCol();
        int nbR = dataset.getNbRows() ;
        int nbC = dataset.getNbCol() ;

        if(idC == nbC){
            dataset.insertCol(nbColsToPaste, idC);
        }
        if(idC == -1){
            dataset.insertCol(nbColsToPaste, 0);
            idC = 0 ;
        }
        if(idR == nbR ){
            dataset.insertRow(nbRowsToPaste, idR);
        }
        boolean pasteHeader = false;
        if(idR == -1 && idC > -1 && idC <=nbR){
            pasteHeader = true;
            idR = 0;
        }else if (idR == -1){
            dataset.insertRow(nbRowsToPaste, 0);
            idR = 0;
        }

        nbR = dataset.getNbRows() ;
        nbC = dataset.getNbCol() ;

        if(idR+nbRowsToPaste > nbR ){
            // insertion de nouvelles lignes
            dataset.insertRow((idR+nbRowsToPaste) - nbR , nbR);
        }
        if(idC+nbColsToPaste > nbC){
            // insertion de nouvelles colonnes
            dataset.insertCol((idC+nbColsToPaste) - nbC, nbC);
        }
        if(pasteHeader){
           for (int j=0; j<nbColsToPaste; j++){
               DataHeader header = null;
               if (subData.getDataHeader(j) != null){
                   CopexReturn  cr = setDataHeader(dataset, idC+j, subData.getDataHeader(j).getValue(), subData.getDataHeader(j).getUnit());
                   if(cr.isError())
                       return cr;
               }else{
                   CopexReturn cr = setDataHeader(dataset, idC+j, "", "");
                   if(cr.isError())
                       return cr;
               }
           }
        }
        for (int i=0; i<nbRowsToPaste; i++){
            for (int j=0; j<nbColsToPaste; j++){
                Data d = subData.getData(i, j);
                Data nd = null;
                if(d != null){
                    CopexReturn cr = setData(dataset, idR+i, idC+j, d.getValue());
                    if(cr.isError())
                        return cr;
                    ArrayList<Data> listD = new ArrayList();
                    listD.add(dataset.getData(idR+i, idC+j)) ;
                    cr = setDataIgnored(dataset, d.isIgnoredData(),listD);
                    if(cr.isError())
                        return cr;
                }else{
                    CopexReturn cr = setDataNull(dataset, idR+i, idC+j);
                    if (cr.isError())
                        return cr;
                }
            }
        }
        // recalcule des operations
        dataset.calculateOperation();
        v.add(dataset.clone());
        return new CopexReturn();
    }

    private CopexReturn setDataNull(Dataset ds, int rowIndex, int colIndex){
        // data existe t il deja ?
        Data data = dataset.getData(rowIndex, colIndex);
        if(data != null){
            ArrayList<Data> listData = new ArrayList();
            listData.add(data);
            CopexReturn cr = DatasetFromDB.deleteDataFromDB(dbC, listData);
            if(cr.isError())
                return cr;
            dataset.setData(null, rowIndex, colIndex);
        }
        return new CopexReturn();
    }

    /* lecture de fichier cvs, => elo ds */
    @Override
    public CopexReturn importCSVFile(File file, ArrayList v){
        if (file == null) {
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_FILE_EXIST"), false);
        }
        StringTokenizer lineParser;
        try{
            CopexReturn cr=  new CopexReturn();
            BufferedReader reader = new BufferedReader(new FileReader(file));
            List<DataSetHeader> headers = new LinkedList<DataSetHeader>();
            DataSetHeader header;
            List<DataSetColumn> listC = new LinkedList<DataSetColumn>();
            eu.scy.elo.contenttype.dataset.DataSet ds = new eu.scy.elo.contenttype.dataset.DataSet();
            String line = null;
            String value = null;
            try{
                int id = 0;
                while ((line = reader.readLine()) != null) {
                    lineParser = new StringTokenizer(line, ";");
                    int j=0;
                    DataSetRow datasetRow;
                    List<String> values = new LinkedList<String>();
                    while (lineParser.hasMoreElements()) {
                        value = (String) lineParser.nextElement();
                        System.out.println(value);
                        if (id == 0){
                            DataSetColumn c = new DataSetColumn(value, value, "double");
                            listC.add(c);
                        }else{
                            values.add(value);
                        }
                        j++;
                    }
                    if(id == 0){
                        header = new DataSetHeader(listC, dataToolPanel.getLocale());
                        headers.add(header);
                        ds = new eu.scy.elo.contenttype.dataset.DataSet(headers);
                    } else {
                        datasetRow = new DataSetRow(values);
                        ds.addRow(datasetRow);
                    }
                    id++;
                }
            }catch(IOException e){
                cr = new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_IMPORT_FILE"), false);
            }
            finally
			{
				if (reader != null)
					try
					{
						reader.close();
                        if(cr.isError())
                            return cr;

                        v.add(ds);
                        return new CopexReturn();
					}
					catch (IOException e)
					{
						cr = new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_CLOSE_FILE")+" "+e, false);
					}
			}
        }catch(FileNotFoundException e){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_FILE_EXIST"), false);
        }
        return new CopexReturn();
    }
}
