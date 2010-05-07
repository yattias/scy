/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.controller;



import eu.scy.elo.contenttype.dataset.DataSet;
import eu.scy.elo.contenttype.dataset.DataSetColumn;
import eu.scy.elo.contenttype.dataset.DataSetHeader;
import eu.scy.elo.contenttype.dataset.DataSetRow;
import roolo.elo.JDomStringConversion;

import eu.scy.tools.dataProcessTool.utilities.CopexReturn;
import eu.scy.tools.dataProcessTool.utilities.DataConstants;
import eu.scy.tools.dataProcessTool.utilities.MyUtilities;
import eu.scy.tools.dataProcessTool.common.*;
import eu.scy.tools.dataProcessTool.db.*;
import eu.scy.tools.dataProcessTool.dataTool.DataProcessToolPanel;
import eu.scy.tools.dataProcessTool.dataTool.DataTableModel;
import eu.scy.tools.dataProcessTool.pdsELO.BarVisualization;
import eu.scy.tools.dataProcessTool.pdsELO.GraphVisualization;
import eu.scy.tools.dataProcessTool.pdsELO.IgnoredData;
import eu.scy.tools.dataProcessTool.pdsELO.PieVisualization;
import eu.scy.tools.dataProcessTool.pdsELO.ProcessedData;
import eu.scy.tools.dataProcessTool.pdsELO.ProcessedDatasetELO;
import eu.scy.tools.dataProcessTool.pdsELO.XYAxis;
import eu.scy.tools.dataProcessTool.print.DataPrint;

import java.util.List;
import java.util.Locale;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import org.jdom.output.Format;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.output.XMLOutputter;

/**
 * controller de l'applet
 * @author Marjolaine Bodin
 */
public class DataControllerDB implements ControllerInterface{
    /* bd */
    private DataBaseCommunication dbC;
    /* url */
    private URL dataURL;

    /* interface */
    private DataProcessToolPanel dataToolPanel;
    /* liste des types d'operations possibles */
    private TypeOperation[] tabTypeOperations;
    /* liste des visualisations possibles */
    private TypeVisualization[] tabTypeVisual;
    private Color[] plotsColor;
    private Color[] functionsColor;
    /* dataset */
    private ArrayList<Dataset> listDataset;
    private ArrayList<Integer> listNoDefaultCol;
    
    /* mission */
    private Mission mission ;
    /* utilisateur */
    private ToolUser toolUser;
    private long dbKeyMission;
    private long dbKeyUser;

    private ArrayList<Mission> listUserMission;
    private ArrayList<ArrayList<Dataset>> listUserDatasetMission;

    private XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());

   
    // CONSTRUCTOR
    public DataControllerDB(DataProcessToolPanel dataToolPanel, URL url, long dbKeyMission, long dbKeyUser) {
        this.dataToolPanel = dataToolPanel ;
        this.dbKeyMission = dbKeyMission ;
        this.dbKeyUser = dbKeyUser ;
        this.dataURL = url;
        dbC = new DataBaseCommunication(dataURL, DataConstants.DB_COPEX, dbKeyMission, ""+dbKeyUser);
        this.listDataset = new ArrayList();
        this.listNoDefaultCol = new ArrayList();
    }


    /* chargement et initialisation des donnees */
    @Override
    public CopexReturn load(){
        // mission
        ArrayList v = new ArrayList();
        dbC.updateDb(DataConstants.DB_COPEX);
        CopexReturn cr = CopexMissionFromDB.loadMissionFromDB(dbC, dbKeyMission, v);
        if (cr.isError()){
            return cr;
        }
        mission = (Mission)v.get(0);
        //user 
        v = new ArrayList();
        cr = ToolUserFromDB.loadDataUserFromDB(dbC, dbKeyUser, v);
        if (cr.isError()){
            return cr;
        }
        toolUser = (ToolUser)v.get(0);
        if(toolUser == null || mission == null){
            return  new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_LOAD_DATA"), false);
        }
        dbC.updateDb(DataConstants.DB_COPEX_DATA);
        // chargement des donnees
        // chargement des types d'operation
        v = new ArrayList();
        cr = OperationFromDB.getAllTypeOperation(dbC,getLocale(), v);
        if(cr.isError())
            return cr;
        tabTypeOperations = (TypeOperation[])v.get(0);

        // liste des visualisations possibles
        cr  = loadTypeVisual();
        if (cr.isError())
            return cr;
        // plots color
        plotsColor = new Color[DataConstants.MAX_PLOT];
        plotsColor[0] = DataConstants.SCATTER_PLOT_COLOR_1;
        plotsColor[1] = DataConstants.SCATTER_PLOT_COLOR_2;
        plotsColor[2] = DataConstants.SCATTER_PLOT_COLOR_3;
        plotsColor[3] = DataConstants.SCATTER_PLOT_COLOR_4;
        // functions Color
        functionsColor = new Color[DataConstants.MAX_FUNCTION];
        functionsColor[0] = DataConstants.FUNCTION_COLOR_1;
        functionsColor[1]= DataConstants.FUNCTION_COLOR_2;
        functionsColor[2] = DataConstants.FUNCTION_COLOR_3;
        // load dataset
        v = new ArrayList();
        cr = DatasetFromDB.getAllDatasetFromDB(dbC, toolUser.getDbKey(), mission, tabTypeOperations,tabTypeVisual, plotsColor, functionsColor,v);
        if (cr.isError()){
            return cr;
        }
        listDataset = (ArrayList<Dataset>)v.get(0);
        for(Iterator<Dataset> l = listDataset.iterator();l.hasNext();){
            listNoDefaultCol.add(l.next().getNbCol());
        }
        // charge les missions et noms des dataset de l'utilisateur
        v = new ArrayList();
        cr = CopexMissionFromDB.getAllMissionFromDB(this.dbC, toolUser.getDbKey(), mission.getDbKey(), v);
        if (cr.isError()){
            return cr;
        }
        listUserMission = (ArrayList<Mission>)v.get(0);
        v = new ArrayList();
        cr = DatasetFromDB.getAllDatasetFromDB(this.dbC, toolUser.getDbKey(), listUserMission, v);
        if (cr.isError()){
            return cr;
        }
        listUserDatasetMission = (ArrayList<ArrayList<Dataset>>)v.get(0);
        // si pas de dataset, on en cree un par dedaut
        if(listDataset.size()==0){
            v = new ArrayList();
            cr = createTable(dataToolPanel.getBundleString("DEFAULT_DATASET_NAME"), v);
            if(cr.isError())
                return cr;
        }
        // clone les objets
       TypeVisualization[] tabTypeVisualC = new TypeVisualization[tabTypeVisual.length];
        for (int i=0; i<tabTypeVisual.length; i++){
            tabTypeVisualC[i] = (TypeVisualization)tabTypeVisual[i].clone();
        }
        TypeOperation[] tabTypeOpC = new TypeOperation[tabTypeOperations.length];
        for (int i=0; i<tabTypeOperations.length; i++){
            tabTypeOpC[i] = (TypeOperation)tabTypeOperations[i].clone();
        }
        ArrayList<Dataset> listDsC = new ArrayList();
        for (int i=0; i<listDataset.size(); i++){
            listDsC.add((Dataset)listDataset.get(i).clone());
        }
        this.dataToolPanel.initDataProcessingTool(tabTypeVisualC, tabTypeOpC, listDsC);
        return new CopexReturn();
    }

    /* chargement des types de visualisation possible */
    private CopexReturn loadTypeVisual(){
        ArrayList v = new ArrayList();
        CopexReturn cr = VisualizationFromDB.getAllTypeVisualizationFromDB(dbC, getLocale(), v);
        if (cr.isError())
            return cr;
        tabTypeVisual = (TypeVisualization[])v.get(0);
        return new CopexReturn();
    }

    /* locale de l'applet */
    private Locale getLocale(){
        return this.dataToolPanel.getLocale() ;
    }

   
     /* creation d'une table retourne en v[0] l'objet DataSet cree */
    public CopexReturn createTable(String name, ArrayList v) {
        // par defaut on cree une table de 10 lignes par 2 colonnes
        int nbRows = 10;
        int nbCol = 2;
        ArrayList<DataOperation> listOp = new ArrayList();
        ArrayList<Visualization> listVis = new ArrayList();
        Data[][] data = new Data[nbRows][nbCol];
        DataHeader[] tabHeader = new DataHeader[nbCol] ;
        for (int i=0; i<nbCol; i++){
            DataHeader h = new DataHeader(-1, this.dataToolPanel.getBundleString("DEFAULT_DATAHEADER_NAME")+" "+(i+1), this.dataToolPanel.getBundleString("DEFAULT_DATAHEADER_UNIT"), i,DataConstants.DEFAULT_TYPE_COLUMN, this.dataToolPanel.getBundleString("DEFAULT_DATAHEADER_DESCRIPTION"));
            tabHeader[i] = h;
        }
        // enregistrement base
        ArrayList v2 = new ArrayList();
        CopexReturn cr = DatasetFromDB.createDatasetInDB(dbC, dataToolPanel.getBundleString("DEFAULT_DATASET_NAME"), nbRows,  nbCol, toolUser.getDbKey(), mission.getDbKey(), v2);
        if (cr.isError())
            return cr;
        long dbKey = (Long)v2.get(0);
        for (int i=0; i<nbCol; i++){
            v2 = new ArrayList();
            cr = DatasetFromDB.createDataHeaderInDB(dbC,tabHeader[i].getValue(), tabHeader[i].getUnit(), i, tabHeader[i].getType(), tabHeader[i].getDescription(), dbKey, v2 );
            if (cr.isError())
                return cr;
            long dbKeyH = (Long)v2.get(0);
            tabHeader[i].setDbKey(dbKeyH);
        }
        // creation ds
        Dataset dataset = new Dataset(dbKey, name, nbCol, nbRows, tabHeader, data, listOp, listVis);
        listDataset.add(dataset);
        listNoDefaultCol.add(nbCol);
        v.add(dataset.clone());
        return new CopexReturn();

    }


    /* chargement d'un ELO */
    @Override
    public CopexReturn loadELO(String xmlString){
        Dataset ds = null;
        ArrayList v = new ArrayList();
        CopexReturn cr = getElo(xmlString, v);
        if(v.size() > 0)
            ds = (Dataset)v.get(0);
        if(ds != null){
            this.listDataset.add(ds);
            this.listNoDefaultCol.add(1);
            this.dataToolPanel.setDataset((Dataset)ds.clone());
        }
        return cr;
    }

    /* retourne vrai si de type ds , pds sinon */
    private boolean isDatasetType(String xmlString){
       Element el=  new JDomStringConversion().stringToXml(xmlString);
       if (el.getName().equals(ProcessedDatasetELO.TAG_ELO_CONTENT)){
           return false;
       }
       return true;
    }

    /* retourne un dataset construit a partir d'un ELO dataset */
    private CopexReturn getDataset(DataSet eloDs, String name, ArrayList v){
        Dataset ds = null;
        CopexReturn cr = new CopexReturn();
        // pas d'operations, pas de visualization
        ArrayList<DataOperation> listOperation = new ArrayList();
        ArrayList<Visualization> listVisualization = new ArrayList();

        // header
        DataSetHeader header =  eloDs.getHeader(getLocale()) ;
        if (header == null)
            header = eloDs.getHeaders().get(0);
        int nbCols = header.getColumnCount() ;
        List<DataSetRow> listRows = eloDs.getValues() ;
        int nbRows = listRows.size() ;

        if(nbCols > 0){
            // header
            DataHeader[] dataHeader = new DataHeader[nbCols];
            for (int i=0; i<nbCols; i++){
                String unit = header.getColumns().get(i).getUnit();
                String type = header.getColumns().get(i).getType();
                if (type == null || !type.equals(DataConstants.TYPE_DOUBLE) || !type.equals(DataConstants.TYPE_STRING))
                    type = DataConstants.DEFAULT_TYPE_COLUMN;
                dataHeader[i] = new DataHeader(-1, header.getColumns().get(i).getSymbol(),unit, i, type, header.getColumns().get(i).getDescription()) ;
            }
            // data
            Data[][] data = new Data[nbRows][nbCols];
            for (int i=0; i<nbRows; i++){
                for (int j=0; j<nbCols; j++){
                    String s = listRows.get(i).getValues().get(j);
                    if(s == null || s.equals("")){
                        data[i][j] = null;
                    }else{
                        data[i][j] = new Data(-1, s, i, j, false);
                    }
                }
            }
            if(nbRows == 0){
                nbRows = 1;
                data = new Data[1][nbCols];
            }
            // creation du dataset
            ds = new Dataset(-1, name, nbCols, nbRows,  dataHeader, data, listOperation,listVisualization  );
            v.add(ds);
        }

        return cr;
    }

    /* retourne un dataset construit a partir d'un ELO pds */
    private Dataset getPDS(ProcessedDatasetELO eloPDS){
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
            String unit = header.getColumns().get(i).getUnit();
            String type = header.getColumns().get(i).getType();
            if (type == null || !type.equals(DataConstants.TYPE_DOUBLE) || !type.equals(DataConstants.TYPE_STRING))
                    type = DataConstants.DEFAULT_TYPE_COLUMN;
            dataHeader[i] = new DataHeader(-1, header.getColumns().get(i).getSymbol(), unit, i, type, header.getColumns().get(i).getDescription()) ;
        }
        // data
        List<DataSetRow> listRows = eloDs.getValues() ;
        int nbRows = listRows.size() ;
        Data[][] data = new Data[nbRows][nbCols];
        for (int i=0; i<nbRows; i++){
            for (int j=0; j<nbCols; j++){
                String s = listRows.get(i).getValues().get(j);
                if(s == null || s.equals("") || s.equals(Double.toString(Double.NaN))){
                    data[i][j] = null;
                }else{
                    data[i][j] = new Data(-1, s, i, j, false);
                }
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
            int noCol = -1;
            int noRow= -1;
            try{
                noCol = Integer.parseInt(noCols);
                noRow = Integer.parseInt(noRows);
            }catch(NumberFormatException e){
                continue;
            }
            if (noRow > -1 && noRow <data.length & noCol >-1 && data[noRow] != null && noCol <data[noRow].length ){
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
            if (vis instanceof BarVisualization){
                int idHeader = ((BarVisualization)vis).getId();
                DataHeader h = dataHeader[idHeader];
                int idLabelHeader = ((BarVisualization)vis).getIdLabelHeader();
                DataHeader headerLabel = null;
                if(idLabelHeader != -1){
                    headerLabel = dataHeader[idLabelHeader];
                }
                 myVis = new SimpleVisualization(-1, vis.getName(), type,h , headerLabel) ;
            } else if (vis instanceof PieVisualization){
                int idHeader = ((PieVisualization)vis).getId();
                DataHeader h = dataHeader[idHeader];
                int idLabelHeader = ((PieVisualization)vis).getIdLabelHeader();
                DataHeader headerLabel = null;
                if(idLabelHeader != -1){
                    headerLabel = dataHeader[idLabelHeader];
                }
                myVis = new SimpleVisualization(-1, vis.getName(), type, h, headerLabel) ;
            } else if(vis instanceof GraphVisualization){
                GraphVisualization g = ((GraphVisualization)vis);
                List<XYAxis> listAxis = g.getAxis();
                ArrayList<PlotXY> plots = new ArrayList();
                for (Iterator<XYAxis> a = listAxis.iterator();a.hasNext();){
                    XYAxis axis = a.next();
                    plots.add(new PlotXY(dataHeader[axis.getX_axis()], dataHeader[axis.getY_axis()], axis.getPlotColor()));
                }
                ParamGraph paramGraph = new ParamGraph(plots, g.getXMin(), g.getXMax(), g.getYMin(), g.getYMax(), g.getDeltaX(), g.getDeltaY(),  g.isDeltaFixedAutoscale());
                ArrayList<FunctionModel> listFunctionModel  = null;
                List<eu.scy.tools.dataProcessTool.pdsELO.FunctionModel> lfm = g.getListFunctionModel();
                if (lfm != null){
                    listFunctionModel = new ArrayList();
                    int n = lfm.size();
                    for(int j=0; j<n; j++){
                        ArrayList<FunctionParam> listParam = new ArrayList();
                        int np = lfm.get(j).getListParam().size();
                        for (int k=0; k<np; k++){
                            listParam.add(new FunctionParam(-1, lfm.get(j).getListParam().get(k).getParam(), lfm.get(j).getListParam().get(k).getValue()));
                        }
                        FunctionModel fm = new FunctionModel(-1, lfm.get(j).getDescription(), new Color(lfm.get(j).getColorR(),lfm.get(j).getColorG(),lfm.get(j).getColorB() ), listParam);
                        listFunctionModel.add(fm);
                    }
                }
                myVis = new Graph(-1, vis.getName(), type,  paramGraph, listFunctionModel);
            }
            listVisualization.add(myVis);
        }
        // creation du dataset
        Dataset ds = new Dataset(-1, pds.getName(), nbCols, nbRows,  dataHeader, data, listOperation,listVisualization  );
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

  

    /* retourne l'indice de l'operation du dataset -1 sinon */
    private int getIdOperation(Dataset ds, DataOperation operation){
        int nb = ds.getListOperation().size();
        for (int i=0; i<nb; i++){
            if (ds.getListOperation().get(i).getDbKey() == operation.getDbKey())
                return i;
        }
        return -1;
    }
   

    private int getIdDataset(long dbKey){
        for (int i=0; i<listDataset.size(); i++){
            if(listDataset.get(i).getDbKey() == dbKey)
                return i;
        }
        return -1;
    }

    /*change le statut valeur ignoree - retourne en v[0] le nouveau dataset */
    @Override
    public CopexReturn setDataIgnored(Dataset ds, boolean isIgnored, ArrayList<Data> listData, ArrayList v){
        int idDs = getIdDataset(ds.getDbKey());
        if(idDs == -1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        Dataset dataset = listDataset.get(idDs);
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
        // recalcule des operations
        dataset.calculateOperation();
        // en v[0] le nouveau dataset clone
        listDataset.set(idDs, dataset);
        v.add(dataset.clone());
        return new CopexReturn();
    }



    /* creation d'une nouvelle operation - retourne en v[0] le nouveau dataset et en v[1] le nouveau DataOperation */
    @Override
    public CopexReturn createOperation(Dataset ds, int typeOperation, boolean isOnCol, ArrayList<Integer> listNo, ArrayList v){
        int idDs = getIdDataset(ds.getDbKey());
        if(idDs == -1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        Dataset dataset = listDataset.get(idDs);
        TypeOperation typeOp = getTypeOperation(typeOperation);
        if (typeOp == null)
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_CREATE_OPERATION"), false);
        String name = typeOp.getCodeName() ;
        long dbKey = -1;
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
        listDataset.set(idDs, dataset);
        v.add(dataset.clone());
        v.add(operation.clone());
        return new CopexReturn();
    }

    /* creation d'une nouvelle operation parametree - retourne en v[0] le nouveau dataset et en v[1] le nouveau DataOperation */
    @Override
    public CopexReturn createOperationParam(Dataset ds, int typeOperation, boolean isOnCol, ArrayList<Integer> listNo,String[] tabValue,  ArrayList v){
        int idDs = getIdDataset(ds.getDbKey());
        if(idDs == -1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        Dataset dataset = listDataset.get(idDs);
        TypeOperation typeOp = getTypeOperation(typeOperation);
        if (typeOp == null)
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_CREATE_OPERATION"), false);
        String name = typeOp.getCodeName() ;
        ParamOperation[] allParam = getParamOperation((TypeOperationParam)typeOp, tabValue);
        long dbKey = -1;
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
        listDataset.set(idDs, dataset);
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
    
    /* mise a jour d'une valeur : titre header */
    @Override
    public CopexReturn updateDataHeader(Dataset ds, boolean confirm, int colIndex, String title, String unit, String description, String type, ArrayList v){
        int idDs = getIdDataset(ds.getDbKey());
        if(idDs == -1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        Dataset dataset = listDataset.get(idDs);


        // header existe t il deja ?
        DataHeader header = dataset.getDataHeader(colIndex);
        if(header == null){
            // header n'existe pas : on le cree
            ArrayList v2 = new ArrayList();
            CopexReturn cr = DatasetFromDB.createDataHeaderInDB(dbC, title,unit,  colIndex, type, description, dataset.getDbKey(),v2);
            if (cr.isError())
                return cr;
            long dbKey = (Long)v2.get(0);
            dataset.setDataHeader(new DataHeader(dbKey, title, unit, colIndex, type, description), colIndex);
        }else{
            // controle des unites par rapport / graphes existants
            CopexReturn cr = controlGraphUnit(dataset, colIndex, unit);
            if(cr.isError())
                return cr;
            // controle du type
            // si type devient string => demande conf pour suppression des operations / visulisations s'il y a lieu.
            if(!confirm){
                cr = controlColumnType(dataset, colIndex, type);
                if(cr.isError() || cr.isWarning())
                    return cr;
            }
            cr = DatasetFromDB.updateDataHeaderInDB(dbC, header.getDbKey(), title, unit, description, type);
            if (cr.isError())
                return cr;
            // header existe => on le modifie
            header.setValue(title);
            header.setUnit(unit);
            header.setDescription(description);
            header.setType(type);
            dataset.setDataHeader(header, header.getNoCol());
            if(type.equals(DataConstants.TYPE_STRING)){
                // supprime les operations si type : double=> string
                // supprimer ou modfifie les visualizations si type : double=> string
                dataset.removeOperationAndVisualizationOn(colIndex);
            }
        }
        // mies a jour vis
        for(Iterator<Visualization> vis = dataset.getListVisualization().iterator();vis.hasNext();){
            Visualization visu = vis.next();
            if(visu instanceof Graph){
                ArrayList<PlotXY> plots = ((Graph)visu).getParamGraph().getPlots();
                for(Iterator<PlotXY> p = plots.iterator(); p.hasNext();){
                    PlotXY plot = p.next();
                    if(plot.getHeaderX().getNoCol() == colIndex){
                        plot.getHeaderX().setValue(title);
                        plot.getHeaderX().setUnit(unit);
                    }else if (plot.getHeaderY().getNoCol() == colIndex){
                        plot.getHeaderY().setValue(title);
                        plot.getHeaderY().setUnit(unit);
                    }
                }
            }
        }
        // en v[0] : le nouveau dataset
        listDataset.set(idDs, dataset);
        v.add(dataset.clone());
        return new CopexReturn();
    }

    /* control de la coherence des axes / unite lors d'un chamgenet d'unite */
    private CopexReturn controlGraphUnit(Dataset dataset, int colIndex, String unit){
        for(Iterator<Visualization> vis = dataset.getListVisualization().iterator();vis.hasNext();){
            Visualization visu = vis.next();
            if(visu instanceof Graph){
                if(((Graph)visu).isOnNo(colIndex)){
                    String unitX = ((Graph)visu).getParamGraph().getPlot1().getHeaderX().getUnit();
                    String unitY = ((Graph)visu).getParamGraph().getPlot1().getHeaderY().getUnit();
                    if(((Graph)visu).getParamGraph().getPlot1().getHeaderX().getNoCol() == colIndex){
                        unitX = unit;
                    }else if(((Graph)visu).getParamGraph().getPlot1().getHeaderY().getNoCol() == colIndex){
                        unitY = unit;
                    }
                    ArrayList<PlotXY> plots = ((Graph)visu).getParamGraph().getPlots();
                    int nbP = plots.size();
                    for (int i=1; i<nbP; i++){
                        PlotXY plot = plots.get(i);
                        String ux = plot.getHeaderX().getUnit();
                        String uy = plot.getHeaderY().getUnit();
                        if(plot.getHeaderX().getNoCol() == colIndex){
                            ux = unit;
                        }else if(plot.getHeaderY().getNoCol() == colIndex){
                            uy = unit;
                        }
                        if((ux != null && unitX != null && !ux.equals(unitX)) || (uy != null && unitY != null &&  !uy.equals(unitY))){
                            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_AXIS_COHERENCE"), false);
                        }
                    }
                }
            }
        }
        return new CopexReturn();
    }

    /* controle que les donnees de la colonne sont de type double et que si chaine de carac. cela ne supprime pas des graphes */
    private CopexReturn controlColumnType(Dataset ds, int colIndex, String type){
        int nbRows = ds.getNbRows();
        if(type.equals(DataConstants.TYPE_DOUBLE)){
            for (int i=0; i<nbRows; i++){
                if(ds.getData(i, colIndex) != null && !ds.getData(i, colIndex).isDoubleValue()){
                    return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_UPDATE_HEADER_TYPE_DOUBLE"), false);
                }
            }
        }
        if(type.equals(DataConstants.TYPE_STRING)){
            String msg = "";
            ArrayList<DataOperation> listOperations = ds.getOperationOnCol(colIndex);
            int nbO = listOperations.size();
            if(nbO > 0){
                msg += dataToolPanel.getBundleString("MSG_WARNING_DELETE_OPERATION");
                for(Iterator<DataOperation> o = listOperations.iterator();o.hasNext();){
                    msg += "\n - "+o.next().getName();
                }
            }
            ArrayList<Visualization> listVisualisations = ds.getVisualizationOnCol(colIndex);
            int nbV = listVisualisations.size();
            if(nbV > 0){
                if(msg.length() > 0)
                    msg +="\n";
                msg += dataToolPanel.getBundleString("MSG_WARNING_DELETE_VISUALIZATION");
                for(Iterator<Visualization> v = listVisualisations.iterator(); v.hasNext();){
                    msg += "\n - "+v.next().getName();
                }
            }
            if(msg.length() > 0){
                msg += "\n"+dataToolPanel.getBundleString("MSG_WARNING_UPDATE_TYPE");
            }
            if(msg.length() > 0){
                CopexReturn cr = new CopexReturn(msg, true);
                cr.setConfirm(msg);
                return cr;
            }
        }
        return new CopexReturn();
    }
    /* mise a jour d'une valeur : titre operation */
    @Override
    public CopexReturn updateDataOperation(Dataset ds, DataOperation operation, String title, ArrayList v){
        int idDs = getIdDataset(ds.getDbKey());
        if(idDs == -1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        Dataset dataset = listDataset.get(idDs);
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
        listDataset.set(idDs, dataset);
        v.add(dataset.clone());
        return new CopexReturn();
    }

    /* mise a jour d'une valeur : donnee dataset */
    @Override
    public CopexReturn updateData(Dataset ds, int rowIndex, int colIndex, String value, ArrayList v){
        int idDs = getIdDataset(ds.getDbKey());
        if(idDs == -1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        Dataset dataset = listDataset.get(idDs);
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
        // recalcule des operations
        dataset.calculateOperation();
        
        // en v[0] : le nouveau dataset
        //System.out.println("dataset apres updateData : "+dataset.toString());
        listDataset.set(idDs, dataset);
        v.add(dataset.clone());
        return new CopexReturn();
    }

    private CopexReturn setDataNull(Dataset ds, int rowIndex, int colIndex){
       int idDs = getIdDataset(ds.getDbKey());
        if(idDs == -1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        Dataset dataset = listDataset.get(idDs);
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
        listDataset.set(idDs, dataset);
        return new CopexReturn();
    }

    /* fermeture d'une visualization */
    @Override
    public CopexReturn closeVisualization(Dataset ds, Visualization vis){
        int idDs = getIdDataset(ds.getDbKey());
        if(idDs == -1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        Dataset dataset = listDataset.get(idDs);
        int idVis = dataset.getIdVisualization(vis.getDbKey()) ;
        if (idVis == -1 ){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_CLOSE_GRAPH"), false);
        }
        dataset.getListVisualization().get(idVis).setOpen(false);
        listDataset.set(idDs, dataset);
        return new CopexReturn();
    }

    /* suppression d'une visualization */
    @Override
    public CopexReturn deleteVisualization(Dataset ds, Visualization vis){
        int idDs = getIdDataset(ds.getDbKey());
        if(idDs == -1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        Dataset dataset = listDataset.get(idDs);
        int idVis = dataset.getIdVisualization(vis.getDbKey()) ;
        if (idVis == -1 )
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DELETE_GRAPH"), false);
        // suppression en base
        CopexReturn cr = VisualizationFromDB.deleteVisualizationFromDB(dbC, dataset.getListVisualization().get(idVis).getDbKey());
        if (cr.isError())
            return cr;
        // suppression en memoire
        dataset.getListVisualization().remove(idVis);
        listDataset.set(idDs, dataset);
        return new CopexReturn();
    }

    /* creation d'une visualization - renvoit en v[0] le nouveua dataset et en v[1] l'objet visualization */
    @Override
    public CopexReturn createVisualization(Dataset ds, Visualization vis, boolean findAxisParam, ArrayList v){
        int idDs = getIdDataset(ds.getDbKey());
        if(idDs == -1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        Dataset dataset = listDataset.get(idDs);
        // creation en base
        ArrayList v2 = new ArrayList();
        CopexReturn cr = VisualizationFromDB.createVisualizationInDB(dbC, dataset.getDbKey(), vis,plotsColor, v2);
        if (cr.isError())
            return cr;
        long dbKey = (Long)v2.get(0);
        if(vis instanceof Graph){
            cr = controlAxis(((Graph)vis).getParamGraph());
            if(cr.isError())
                return cr;
        }
        if (vis instanceof Graph && findAxisParam){
           v2 = new ArrayList();
           cr = findAxisParam(ds, ((Graph)vis), v2);
           if(cr.isError())
               return cr;
           ParamGraph pg = (ParamGraph)(v2.get(0));
           ((Graph)vis).setParamGraph(pg);
           cr = VisualizationFromDB.updateGraphParamInDB(dbC, dbKey, pg);
           if(cr.isError())
               return cr;
       }
        // memoire
        vis.setDbKey(dbKey);
        dataset.addVisualization(vis);
       
        // en v[0] le nouveau ds clone et en v[1] le nouveau vis clone
        listDataset.set(idDs, dataset);
        v.add(dataset.clone());
        v.add(vis.clone());
        return new CopexReturn();
    }


    private CopexReturn controlAxis(ParamGraph pg){
        String unitX = pg.getPlot1().getHeaderX().getUnit();
        String unitY = pg.getPlot1().getHeaderY().getUnit();
        int nbP = pg.getPlots().size();
        for (int i=1; i<nbP; i++){
            PlotXY plot = pg.getPlots().get(i);
            if(unitX != null && plot.getHeaderX().getUnit() != null && !plot.getHeaderX().getUnit().equals(unitX)){
                return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_UNIT_X"), false);
            }
            if(unitY != null && plot.getHeaderY().getUnit() != null && !plot.getHeaderY().getUnit().equals(unitY)){
                return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_UNIT_Y"), false);
            }
        }
        // recherche de 2 plots identiques
        boolean samePlot = false;
        for(int i=0; i<nbP;i++ ){
            PlotXY plot = pg.getPlots().get(i);
            for(int j=i+1; j<nbP; j++){
                if(plot.getHeaderX().getNoCol() == pg.getPlots().get(j).getHeaderX().getNoCol() &&
                            plot.getHeaderY().getNoCol() == pg.getPlots().get(j).getHeaderY().getNoCol()){
                    samePlot = true;
                    break;
                }
            }
        }
        if(samePlot)
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_SAME_PLOT"), false);
        return new CopexReturn();
    }
    /* update nom graphe */
    @Override
    public CopexReturn updateVisualizationName(Dataset ds, Visualization vis, String newName){
        int idDs = getIdDataset(ds.getDbKey());
        if(idDs == -1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        Dataset dataset = listDataset.get(idDs);
        int idVis = ds.getIdVisualization(vis.getDbKey()) ;
        if (idVis == -1 )
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_UPDATE_VISUALIZATION_NAME"), false);
        // changement en base
        CopexReturn cr = VisualizationFromDB.updateVisualizationTitleInDB(dbC,dataset.getListVisualization().get(idVis).getDbKey() , newName);
        if(cr.isError())
            return cr;
        // memoire
        dataset.getListVisualization().get(idVis).setName(newName);
        listDataset.set(idDs, dataset);
        return new CopexReturn();
    }

  
   
    /* suppression d'un dataset */
    @Override
    public CopexReturn deleteDataset(Dataset ds){
        int idDs = getIdDataset(ds.getDbKey());
        if(idDs == -1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        Dataset dataset = listDataset.get(idDs);
        // suppression en base
        CopexReturn cr = DatasetFromDB.deleteDatasetFromDB(dbC, dataset.getDbKey());
        if (cr.isError())
            return cr;
        listDataset.remove(idDs);
        listNoDefaultCol.remove(idDs);
        return new CopexReturn();
    }

    /* suppression de donnees et/ou operation sur un dataset */
    @Override
    public CopexReturn deleteData(boolean confirm, Dataset ds, ArrayList<Data> listData,  ArrayList<Integer> listNoDataRow, ArrayList<Integer> listNoDataCol,ArrayList<DataOperation> listOperation,  ArrayList v){
        int idDs = getIdDataset(ds.getDbKey());
        if(idDs == -1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        Dataset dataset = listDataset.get(idDs);
        int nbRowsSel = listNoDataRow.size();
        int nbColsSel = listNoDataCol.size();
        // suppression de donnees :
        // si toutes les colonnes ou toutes les lignes sont sel : on supprime tout le dataset apres confirmation
        boolean allData = nbRowsSel == dataset.getNbRows() || nbColsSel == dataset.getNbCol();
        if (allData){
            if (confirm){
                // suppression du dataset
                CopexReturn cr = deleteDataset(dataset);
                if (cr.isError())
                    return cr;
                dataToolPanel.deleteDataset(ds);
            }else{
                CopexReturn cr = new CopexReturn(dataToolPanel.getBundleString("MSG_CONFIRM_DELETE_DATASET"), true);
                cr.setConfirm(dataToolPanel.getBundleString("MSG_CONFIRM_DELETE_DATASET"), true);
                return cr;
            }
        }else{
            //suppression d'une partie des donnees
            // suppression des operations
            int nbOp = listOperation.size();
            CopexReturn cr = OperationFromDB.deleteOperationFromDB(dbC, listOperation);
            if(cr.isError())
                return cr;
            for (int i=0; i<nbOp; i++){
                DataOperation op = listOperation.get(i);
                dataset.removeOperation(op);
            }
            //suppression des lignes :
            ArrayList[] tabDel = dataset.removeRows(listNoDataRow);
            // supression des colonnes
            ArrayList[] tabDel2 = dataset.removeCols(listNoDataCol);
            ArrayList<DataOperation> listOpToDel = (ArrayList<DataOperation>)tabDel[1];
            ArrayList<Visualization> listVisToDel = (ArrayList<Visualization>)tabDel[3];
            ArrayList<DataOperation> listOpToUpdate = (ArrayList<DataOperation>)tabDel[0];
            ArrayList<Visualization> listVisToUpdate = (ArrayList<Visualization>)tabDel[2];
            for(Iterator<DataOperation> o = ((ArrayList<DataOperation>)tabDel2[0]).iterator();o.hasNext();){
                listOpToUpdate.add(o.next());
            }
            for(Iterator<DataOperation> o = ((ArrayList<DataOperation>)tabDel2[1]).iterator();o.hasNext();){
                listOpToDel.add(o.next());
            }
            for(Iterator<Visualization> o = ((ArrayList<Visualization>)tabDel2[2]).iterator();o.hasNext();){
                listVisToUpdate.add(o.next());
            }
            for(Iterator<Visualization> o = ((ArrayList<Visualization>)tabDel2[3]).iterator();o.hasNext();){
                listVisToDel.add(o.next());
            }
            tabDel[0] = listOpToUpdate;
            tabDel[1] = listOpToDel;
            tabDel[2] = listVisToUpdate;
            tabDel[3] = listVisToDel;
            // suppresion des données
            cr = DatasetFromDB.deleteDataFromDB(dbC, listData);
            if(cr.isError())
                return cr;
            dataset.removeData(listData);
            dataset.calculateOperation();
            //System.out.println("dataset apres remove : "+dataset.toString());
            listDataset.set(idDs, dataset);
            v.add((Dataset)dataset.clone());
            v.add(tabDel);
        }
        return new CopexReturn();

    }


    @Override
    public Element getPDS(Dataset ds){
        // ELO format
        ProcessedDatasetELO pdsELO = ds.toELO(dataToolPanel.getLocale());
        return pdsELO.toXML() ;
        
    }

    /* ajout ou modification d'une fonction modeme */
    @Override
    public CopexReturn setFunctionModel(Dataset ds, Visualization vis, String description, Color fColor, ArrayList<FunctionParam> listParam,ArrayList v){
        int idDs = getIdDataset(ds.getDbKey());
        if(idDs == -1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        Dataset dataset = listDataset.get(idDs);
        int idVis = dataset.getIdVisualization(vis.getDbKey()) ;
        if (idVis == -1)
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_SET_FUNCTION_MODEL"), false);
        Visualization myVis = dataset.getListVisualization().get(idVis);
        if (! (myVis instanceof Graph))
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_SET_FUNCTION_MODEL"), false);
        Graph graph = (Graph)myVis;
        FunctionModel fm = graph.getFunctionModel(fColor);
        if (fm == null){
            // creation de la fonction
            // creation en base
            ArrayList v2 = new ArrayList();
            int idFunctionColor = getIdFunctionColor(fColor);
            CopexReturn cr = VisualizationFromDB.createFunctionModelInDB(dbC, graph.getDbKey(), description, idFunctionColor,  listParam, v2);
            if (cr.isError())
                return cr;
            long dbKey = (Long)v2.get(0);
            listParam = (ArrayList<FunctionParam>)v2.get(1);
            // memoire
            FunctionModel myFm = new FunctionModel(dbKey, description, fColor, listParam);
            ((Graph)dataset.getListVisualization().get(idVis)).addFunctionModel(myFm);
        }else{
            //mise a jour
            if (description == null || description.length() == 0){
                // suppression
                CopexReturn cr = VisualizationFromDB.deleteFunctionModelFromDB(dbC, fm.getDbKey());
                if (cr.isError())
                    return cr;
                ((Graph)dataset.getListVisualization().get(idVis)).deleteFunctionModel(fColor);
            }else{
                // modification
                ArrayList v2 = new ArrayList();
                CopexReturn cr = VisualizationFromDB.updateFunctionModelInDB(dbC, fm.getDbKey(), description, listParam, v2);
                if (cr.isError())
                    return cr;
                fm.setDescription(description);
                fm.setListParam(listParam);
            }
        }
        listDataset.set(idDs, dataset);
        v.add(dataset.clone());
        return new CopexReturn();
    }

    private int getIdFunctionColor(Color fColor){
        for(int i=0; i<functionsColor.length; i++){
            if(functionsColor[i].equals(fColor))
                return i;
        }
        return -1;
    }
    /* insertion de lignes ou de colonnes */
    @Override
    public CopexReturn  insertData(Dataset ds,  boolean isOnCol, int nb, int idBefore, ArrayList v){
        int idDs = getIdDataset(ds.getDbKey());
        if(idDs == -1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        Dataset dataset = listDataset.get(idDs);
        int newNbRow = dataset.getNbRows() ;
        int newNbCol = dataset.getNbCol() ;
        if (isOnCol)
            newNbCol += nb;
        else
            newNbRow += nb ;
        CopexReturn cr = DatasetFromDB.updateDatasetMatriceInDB(dbC, dataset.getDbKey(), newNbRow, newNbCol);
        if (cr.isError())
            return cr;
        if (isOnCol){
            dataset.insertCol(nb, idBefore);
            for(int j=0; j<nb; j++){
                ArrayList v2 = new ArrayList();
                int no = listNoDefaultCol.get(idDs);
                cr= updateDataHeader(ds, true, idBefore+j, this.dataToolPanel.getBundleString("DEFAULT_DATAHEADER_NAME")+" "+(no+1), this.dataToolPanel.getBundleString("DEFAULT_DATAHEADER_UNIT"), this.dataToolPanel.getBundleString("DEFAULT_DATAHEADER_DESCRIPTION"),DataConstants.DEFAULT_TYPE_COLUMN, v2);
                if(cr.isError())
                    return cr;
                listNoDefaultCol.set(idDs, no+1);
            }
        }
        else
            dataset.insertRow(nb, idBefore);

        v.add(dataset.clone());
        listDataset.set(idDs, dataset);
        //System.out.println("dataset apres insertData : "+dataset.toString());
        return new CopexReturn();
    }

    /* impression */
    @Override
    public CopexReturn printDataset(Dataset dataset, boolean printDataset, DataTableModel model, ArrayList<Visualization> listVis, ArrayList<Object> listGraph){
        String fileName = "copex-"+mission.getCode();
        DataPrint pdfPrint = new DataPrint(dataToolPanel, mission, toolUser,dataset, model, printDataset, listVis, listGraph,fileName );
        CopexReturn cr = pdfPrint.printDocument();
        return cr;
    }

    
    /* mise a jour dataset apres sort */
    @Override
    public CopexReturn updateDatasetRow(Dataset ds, Vector exchange, ArrayList v){
        int idDs = getIdDataset(ds.getDbKey());
        if(idDs == -1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        Dataset dataset = listDataset.get(idDs);
        dataset.exchange(exchange);
        v.add(dataset.clone());
        listDataset.set(idDs, dataset);
        return new CopexReturn();
    }

    @Override
    public CopexReturn updateDataset(Dataset ds, ArrayList v){
        int idDs = getIdDataset(ds.getDbKey());
        if(idDs == -1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        Dataset dataset = listDataset.get(idDs);
        dataset = (Dataset)ds.clone();
        v.add(dataset.clone());
        listDataset.set(idDs, dataset);
        return new CopexReturn();
    }

    /* creation d'un dataset avec l'en tete - 1 ligne de donnees */
    @Override
    public CopexReturn createDataset(String name, String[] headers, String[] units, String[] types, String[] descriptions, ArrayList v){
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
            cr = DatasetFromDB.createDataHeaderInDB(dbC, headers[i],units[i], i,types[i], descriptions[i], dbKey, v2);
            if(cr.isError())
                return cr;
            long dbKeyH = (Long)v2.get(0);
            tabHeader[i] = new DataHeader(dbKeyH, headers[i], units[i], i, types[i], descriptions[i]);
        }
        // creation ds
        Dataset ds = new Dataset(dbKey, name, nbCol, nbRows, tabHeader,  data, listOp, listVis);
        listDataset.add(ds);
        listNoDefaultCol.add(1);
        v.add(ds.clone());
        return new CopexReturn();
    }

    
    /* ajout d'une ligne de donnees */
    @Override
    public CopexReturn addData(long dbKeyDs, String[] values, ArrayList v){
        int idDs = getIdDataset(dbKeyDs);
        if(idDs == -1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        Dataset dataset = listDataset.get(idDs);
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
        // si besoin on reajuste les axes des graphes
       for(Iterator<Visualization> vis = dataset.getListVisualization().iterator(); vis.hasNext();){
           Visualization aVis = vis.next();
           if(aVis instanceof Graph){
               if (! ((Graph)aVis).contains(dataset.getRow(idRow))){
                   v2 = new ArrayList();
                   cr = findAxisParam(dataset, (Graph)aVis, v2);
                   if(cr.isError())
                       return cr;
                   ParamGraph pg = (ParamGraph)(v2.get(0));
                  ((Graph)aVis).setParamGraph(pg);
               }
           }
       }
        listDataset.set(idDs, dataset);
        v.add(dataset.clone());
        return new CopexReturn() ;
    }

    /*mise a jour des param */
    @Override
    public CopexReturn setParamGraph(long dbKeyDs, long dbKeyVis, ParamGraph paramGraph, ArrayList v){
        int idDs = getIdDataset(dbKeyDs);
        if(idDs == -1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        Dataset dataset = listDataset.get(idDs);
        int idVis = dataset.getIdVisualization(dbKeyVis);
        if (idVis == -1)
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_UPDATE_PARAM_GRAPH"), false);
        Visualization vis = dataset.getListVisualization().get(idVis);
        if (vis instanceof Graph){
            CopexReturn cr = controlAxis(paramGraph);
            if(cr.isError())
                return cr;
            cr = VisualizationFromDB.updateGraphParamInDB(dbC, vis.getDbKey(), paramGraph);
            if(cr.isError())
                return cr;
            ((Graph)vis).setParamGraph(paramGraph);
        }
        listDataset.set(idDs, dataset);
        v.add(vis.clone());
        return new CopexReturn();
    }

    /*recherche des axes */
    private CopexReturn findAxisParam(Dataset ds, Graph vis, ArrayList v){
        ArrayList<Double> listX = new ArrayList();
        ArrayList<Double> listY = new ArrayList();
        ArrayList<PlotXY> plots = vis.getParamGraph().getPlots();
        for(Iterator<PlotXY> p = plots.iterator();p.hasNext();){
            PlotXY plot = p.next();
            for (int i=0; i<ds.getNbRows(); i++){
                if(ds.getData(i, plot.getHeaderX().getNoCol()) != null && ds.getData(i, plot.getHeaderY().getNoCol()) != null ){
                    listX.add(ds.getData(i, plot.getHeaderX().getNoCol()).getDoubleValue());
                    listY.add(ds.getData(i, plot.getHeaderY().getNoCol()).getDoubleValue());
                }
            }
        }
        ParamGraph pg = null;
        int nb = listX.size();
        if(nb >0 ){
            double minX = listX.get(0);
            double minY = listY.get(0);
            double maxX = listX.get(0);
            double maxY = listY.get(0) ;
            for (int i=1; i<nb; i++){
                if (listX.get(i)< minX)
                    minX = listX.get(i);
                if (listX.get(i) > maxX)
                    maxX = listX.get(i);
                if (listY.get(i) < minY)
                    minY = listY.get(i);
                if (listY.get(i) > maxY)
                    maxY = listY.get(i);
            }
            // +/- 10%
            double  x_min  = minX - Math.abs(minX/10) ;
            double  y_min  = minY -Math.abs(minY/10) ;
            double  x_max  = maxX +Math.abs(maxX/10) ;
            double  y_max  = maxY +Math.abs(maxY/10);
            double deltaX = vis.getParamGraph().getDeltaX();
            double deltaY = vis.getParamGraph().getDeltaY();
            if(!vis.getParamGraph().isDeltaFixedAutoscale()){
                deltaX = Math.abs((x_max - x_min) / 10) ;
                deltaY = Math.abs((y_max - y_min) / 10) ;
//                deltaX = Math.floor(deltaX*10)*0.1;
//                deltaY = Math.floor(deltaY*10)*0.1;
                deltaX = MyUtilities.floor(deltaX, 3);
                deltaY = MyUtilities.floor(deltaY, 3);
                if(deltaX == 0)
                    deltaX = 0.001;
                if(deltaY == 0)
                    deltaY = 0.001;
            }
            pg = new ParamGraph(vis.getParamGraph().getPlots(), x_min, x_max, y_min, y_max, deltaX, deltaY, vis.getParamGraph().isDeltaFixedAutoscale());
        }else{
            pg = new ParamGraph(vis.getParamGraph().getPlots(), -10, 10, -10 ,10, 1,1, vis.getParamGraph().isDeltaFixedAutoscale());
        }
//        System.out.println("findAxisParam : \n");
//        System.out.println("...x : "+pg.getX_min()+" / "+pg.getX_max());
//        System.out.println("...y : "+pg.getY_min()+" / "+pg.getY_max());
//        System.out.println("...delta : "+pg.getDeltaX()+" / "+pg.getDeltaY()+"\n");
        v.add(pg);
        return new CopexReturn();
    }

    /* maj autoscale*/
    @Override
    public CopexReturn setAutoScale(long dbKeyDs, long dbKeyVis, boolean autoScale, ArrayList v){
        int idDs = getIdDataset(dbKeyDs);
        if(idDs == -1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        Dataset dataset = listDataset.get(idDs);
        int idVis = dataset.getIdVisualization(dbKeyVis);
        if (idVis == -1)
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_UPDATE_PARAM_GRAPH"), false);
        Visualization vis = dataset.getListVisualization().get(idVis);
        if (vis instanceof Graph){
            if(autoScale){
                ArrayList v2 = new ArrayList();
                CopexReturn cr =findAxisParam(dataset, (Graph)vis, v2);
                if(cr.isError())
                    return cr;
                ParamGraph pg = (ParamGraph)(v2.get(0));
                ((Graph)vis).setParamGraph(pg);
                dataset.getListVisualization().set(idVis, ((Graph)vis));
            }
        }
        listDataset.set(idDs, dataset);
        v.add(vis);
        return new CopexReturn();
    }

   /*merge un ELO avec l'ELO courant*/
    @Override
    public CopexReturn mergeELO(Dataset d, Element elo){
        int idDs = getIdDataset(d.getDbKey());
        if(idDs ==-1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        Dataset dataset = listDataset.get(idDs);
        String xmlContent = new JDomStringConversion().xmlToString(elo);
        ArrayList v = new ArrayList();
        CopexReturn cr = getElo(xmlContent, v);
        Dataset ds = null;
        if(v.size() > 0)
            ds = (Dataset)v.get(0);
        if(ds != null){
            //merge dataset et ds
            int nbRows1 = dataset.getNbRows() ;
            int nbCols1 = dataset.getNbCol() ;
            int nbRows2 = ds.getNbRows();
            int nbCols2 = ds.getNbCol();
            if(nbRows2 > nbRows1){
                //on ajoute des lignes
                dataset.insertRow(nbRows2-nbRows1, nbRows1);
            }
            dataset.insertCol(nbCols2, nbCols1);
            for (int j=0; j<nbCols2; j++){
                if (ds.getDataHeader(j) != null){
                    dataset.setDataHeader(new DataHeader(-1,ds.getDataHeader(j).getValue() , ds.getDataHeader(j).getUnit(), j+nbCols1, ds.getDataHeader(j).getType(), ds.getDataHeader(j).getDescription()), j+nbCols1);
                }
                for (int i=0; i<nbRows2; i++){
                    Data nd = null;
                    if (ds.getData(i, j) != null){
                        nd = new Data(-1, ds.getData(i, j).getValue(), i,j+nbCols1, ds.getData(i, j).isIgnoredData() );
                    }
                    dataset.setData(nd, i, j+nbCols1);
                }
            }
            // merge des operations
            ArrayList<DataOperation> listOp = ds.getListOperation();
            int nbOp = listOp.size();
            for (int i=0; i<nbOp; i++){
                DataOperation op = listOp.get(i);
                ArrayList<Integer> listNo = new ArrayList();
                int n= op.getListNo().size();
                for (int j=0; j<n; j++){
                    listNo.add(op.getListNo().get(j)+nbCols1);
                }
                dataset.addOperation(new DataOperation(-1, op.getName(), op.getTypeOperation(), op.isOnCol(), listNo));
            }
            // merge des vis
            ArrayList<Visualization> listVis = ds.getListVisualization();
            int nVis = listVis.size();
            for (int i=0; i<nVis; i++){
                Visualization vis = listVis.get(i);
                if(vis instanceof SimpleVisualization){
                    int noCol = ((SimpleVisualization)vis).getNoCol()+nbCols1;
                    DataHeader h = dataset.getDataHeader(noCol);
                    DataHeader headerLabel = null;
                    if(((SimpleVisualization)vis).getHeaderLabel() != null){
                        headerLabel = dataset.getDataHeader(((SimpleVisualization)vis).getHeaderLabel().getNoCol()+nbCols1);
                    }
                    SimpleVisualization sv = new SimpleVisualization(-1, vis.getName(), vis.getType(),h, headerLabel);
                    dataset.addVisualization(sv);
                }else if(vis instanceof Graph){
                    ArrayList<PlotXY> listPlots = new ArrayList();
                    for (Iterator<PlotXY> p = ((Graph)vis).getParamGraph().getPlots().iterator();p.hasNext();){
                        PlotXY plot = p.next();
                        listPlots.add(new PlotXY(dataset.getDataHeader(plot.getHeaderX().getNoCol()+nbCols1), dataset.getDataHeader(plot.getHeaderY().getNoCol()+nbCols1), plot.getPlotColor()));
                    }
                    ParamGraph pg = new ParamGraph(listPlots, ((Graph)vis).getParamGraph().getX_min(),((Graph)vis).getParamGraph().getX_max(), ((Graph)vis).getParamGraph().getX_min(), ((Graph)vis).getParamGraph().getY_max(), ((Graph)vis).getParamGraph().getDeltaX(), ((Graph)vis).getParamGraph().getDeltaY(),  ((Graph)vis).getParamGraph().isDeltaFixedAutoscale());
                    Graph g = new Graph(-1, vis.getName(), vis.getType(),pg,((Graph)vis).getListFunctionModel());
                    dataset.addVisualization(g);
                }
            }
            //mise a jour graphique
            this.dataToolPanel.setDataset((Dataset)dataset.clone());
        }
        return cr;
    }

    /* convertir un elo au format xml en dataset*/
    private CopexReturn getElo(String xmlContent, ArrayList v){
        Dataset ds = null;
        if(isDatasetType(xmlContent)){
            DataSet eloDs ;
            try{
                eloDs = new DataSet(xmlContent) ;
            }catch(JDOMException e){
                return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_LOAD_DATA"), false) ;
            }
            //ds = getDataset(eloDs, "ELO");
            ArrayList v2 = new ArrayList();
            CopexReturn cr = getDataset(eloDs, "ELO", v2);
            if(v2.size() > 0){
                ds = (Dataset)v2.get(0);
                v.add(ds);
            }
            return cr;
        }else{
            try{
                ProcessedDatasetELO edoPds = new ProcessedDatasetELO(xmlContent);
                ds = getPDS(edoPds);
                if (ds == null)
                    return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_LOAD_DATA"), false) ;
                else
                    v.add(ds);
            }catch(JDOMException e){
                   return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_LOAD_DATA"), false) ;
            }
        }
        return new CopexReturn();
    }
    
    /* copie-colle */
    @Override
    public CopexReturn paste(long dbKeyDs, Dataset subData, int[] selCell, ArrayList v){
        int idDs = getIdDataset(dbKeyDs);
        if(idDs == -1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        Dataset dataset = listDataset.get(idDs);
        Dataset oldDs = (Dataset)dataset.clone() ;
        if (selCell == null){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_PASTE"), false);
        }
        ArrayList<Data[]> listData = new ArrayList();
        ArrayList<DataHeader[]> listDataHeader = new ArrayList();
        ArrayList<Integer>[] listRowAndCol = new ArrayList[2];
        ArrayList<Integer> listNoRow = new ArrayList();
        ArrayList<Integer> listNoCol = new ArrayList();
        int idR = selCell[0] ;
        int idC = selCell[1] ;
        int nbRowsToPaste = subData.getNbRows();
        int nbColsToPaste = subData.getNbCol();
        int nbR = dataset.getNbRows() ;
        int nbC = dataset.getNbCol() ;
        
        if(idC == nbC){
            dataset.insertCol(nbColsToPaste, idC);
            for (int i=0; i<nbColsToPaste; i++){
                listNoCol.add(idC+i);
            }
        }
        if(idC == -1){
            dataset.insertCol(nbColsToPaste, 0);
            idC = 0 ;
            for (int i=0; i<nbColsToPaste; i++){
                listNoCol.add(i);
            }
        }
        if(idR == nbR ){
            dataset.insertRow(nbRowsToPaste, idR);
            for (int i=0; i<nbRowsToPaste; i++){
                listNoRow.add(idR+i);
            }
        }
        boolean pasteHeader = false;
        if(idR == -1 && idC > -1 && idC <=nbR){
            pasteHeader = true;
            idR = 0;
        }else if (idR == -1){
            dataset.insertRow(nbRowsToPaste, 0);
            idR = 0;
            for (int i=0; i<nbRowsToPaste; i++){
                listNoRow.add(i);
            }
        }

        nbR = dataset.getNbRows() ;
        nbC = dataset.getNbCol() ;

        if(idR+nbRowsToPaste > nbR ){
            // insertion de nouvelles lignes
            dataset.insertRow((idR+nbRowsToPaste) - nbR , nbR);
            for (int i=0; i<(idR+nbRowsToPaste) - nbR; i++){
                listNoRow.add(nbR+i);
            }
        }
        if(idC+nbColsToPaste > nbC){
            // insertion de nouvelles colonnes
            dataset.insertCol((idC+nbColsToPaste) - nbC, nbC);
            for (int i=0; i<(idC+nbColsToPaste) - nbC; i++){
                listNoCol.add(nbC+i);
            }
        }
        if(pasteHeader){
           for (int j=0; j<nbColsToPaste; j++){
               DataHeader header = null;
               if (subData.getDataHeader(j) != null){
                   header = new DataHeader(-1, subData.getDataHeader(j).getValue(),subData.getDataHeader(j).getUnit(),  idC+j, subData.getDataHeader(j).getType(), subData.getDataHeader(j).getDescription());
                   DataHeader[] headers = new DataHeader[2];
                   headers[0] = null;
                   if(idC+j< oldDs.getNbCol()){
                       headers[0] = oldDs.getDataHeader(idC+j);
                   }
                   headers[1] = header;
                   listDataHeader.add(headers);
               }
               dataset.setDataHeader(header, idC+j);
           }
        }
        for (int i=0; i<nbRowsToPaste; i++){
            for (int j=0; j<nbColsToPaste; j++){
                Data d = subData.getData(i, j);
                Data nd = null;
                if (d != null){
                    nd = new Data(-1, d.getValue(), idR+i, idC+j, d.isIgnoredData()) ;
                    Data[] datas = new Data[2];
                   datas[0] = null;
                   if(idC+j< oldDs.getNbCol() && idR+i <oldDs.getNbRows()){
                       datas[0] = oldDs.getData(idR+i, idC+j);
                   }
                   datas[1] = nd;
                   listData.add(datas);
                }
                dataset.setData(nd, idR+i, idC+j);
            }
        }
        // recalcule des operations
        dataset.calculateOperation();
        listDataset.set(idDs, dataset);
        v.add(dataset.clone());
        v.add(listData);
        v.add(listDataHeader);
        listRowAndCol[0] = listNoRow ;
        listRowAndCol[1] = listNoCol;
        v.add(listRowAndCol);
        return new CopexReturn();
    }

    /* lecture de fichier cvs  => elo ds */
    @Override
    public CopexReturn importCSVFile(File file, ArrayList v){
        if (file == null) {
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_FILE_EXIST"), false);
        }
        if(!file.getName().substring(file.getName().length()-3).equals("csv")){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_FILE_CSV"), false);
        }
        StringTokenizer lineParser;
        try{
            CopexReturn cr=  new CopexReturn();
            InputStreamReader fileReader = null;
            try {
                fileReader = new InputStreamReader(new FileInputStream(file), "utf-8");
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(DataController.class.getName()).log(Level.SEVERE, null, ex);
            }
            BufferedReader reader = new BufferedReader(fileReader);
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
                        //System.out.println(value);
                        if (id == 0){
                            DataSetColumn c = new DataSetColumn(value, "", DataConstants.DEFAULT_TYPE_COLUMN);
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
                // fix the type of the columns
                if(headers.size() > 0){
                    int nbCol = headers.get(0).getColumnCount();
                    for(int j=0; j<nbCol; j++){
                        String ctype = DataConstants.TYPE_DOUBLE;
                        for(Iterator<DataSetRow> r = ds.getValues().iterator(); r.hasNext();){
                            String s = r.next().getValues().get(j);
                            try{
                                double d = Double.parseDouble(s);
                            }catch(NumberFormatException e1){
                                ctype = DataConstants.TYPE_STRING;
                                break;
                            }
                        }
                        headers.get(0).getColumns().set(j, new DataSetColumn(headers.get(0).getColumns().get(j).getSymbol(), headers.get(0).getColumns().get(j).getDescription(), ctype, headers.get(0).getColumns().get(j).getUnit()));
                    }
                }
            }catch(IOException e){
                cr = new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_IMPORT_FILE"), false);
            }catch(Exception ex){
                cr = new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_IMPORT_FILE"), false);
            }
            finally{
                if (reader != null)
		try{
                    reader.close();
                    if(cr.isError())
                        return cr;
                    v.add(ds);
                    return new CopexReturn();
		}catch (IOException e){
                    cr = new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_CLOSE_FILE")+" "+e, false);
		}
            }
        }catch(FileNotFoundException e){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_FILE_EXIST"), false);
        }
        return new CopexReturn();
    }

    
    /*creation dataset par default */
    @Override
    public CopexReturn createDefaultDataset(String name, ArrayList v){
        return createTable(name, v);
    }

    /* mise a jour du nom du dataset */
    @Override
    public CopexReturn renameDataset(Dataset ds, String name){
        int idDs = getIdDataset(ds.getDbKey());
        if(idDs == -1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        Dataset dataset = listDataset.get(idDs);
        CopexReturn cr = DatasetFromDB.updateDatasetNameInDB(dbC, dataset.getDbKey(), name);
        if(cr.isError())
            return cr;
        dataset.setName(name);
        return new CopexReturn();
    }

    /* fermeture d'un dataset */
    @Override
    public CopexReturn closeDataset(Dataset ds){
        int id = getIdDataset(ds.getDbKey());
        if(id == -1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        this.listDataset.remove(id);
        this.listNoDefaultCol.remove(id);
        return new CopexReturn();
    }

    /* maj d'une operation */
    @Override
    public CopexReturn updateOperation(Dataset ds, DataOperation operation , ArrayList v){
        int id = getIdDataset(ds.getDbKey());
        if(id == -1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        Dataset dataset = this.listDataset.get(id);
        int idOp = dataset.getIdOperation(operation.getDbKey());
        if(idOp == -1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        DataOperation myOp = dataset.getListOperation().get(idOp);
        myOp.setListNo(operation.getListNo());
        dataset.calculateOperation();
        v.add(dataset.clone());
        return new CopexReturn();
    }

    /* retourne la liste des missions et la liste des dataset / mission */
    @Override
    public CopexReturn getListDatasetToOpenOrMerge(ArrayList v){
        ArrayList<Mission> listMission = new ArrayList();
        ArrayList<ArrayList<Dataset>> listDatasetMission = new ArrayList();
        for(Iterator<Mission> m = listUserMission.iterator(); m.hasNext();){
            listMission.add((Mission)m.next().clone());
        }
        for(Iterator<ArrayList<Dataset>> l = listUserDatasetMission.iterator(); l.hasNext();){
            ArrayList<Dataset> list = l.next();
            ArrayList<Dataset> myList = new ArrayList();
            for(Iterator<Dataset> a = list.iterator(); a.hasNext();){
                myList.add((Dataset)a.next().clone());
            }
            listDatasetMission.add(myList);
        }
        v.add(listMission);
        v.add(listDatasetMission);
        return new CopexReturn();
    }
}
