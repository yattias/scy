/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.controller;



//import eu.scy.elo.contenttype.dataset.DataSet;
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
import eu.scy.tools.dataProcessTool.gmbl.GmblColumn;
import eu.scy.tools.dataProcessTool.gmbl.GmblDataset;
import eu.scy.tools.dataProcessTool.logger.FitexProperty;
import eu.scy.tools.dataProcessTool.pdsELO.BarVisualization;
import eu.scy.tools.dataProcessTool.pdsELO.GraphVisualization;
import eu.scy.tools.dataProcessTool.pdsELO.IgnoredData;
import eu.scy.tools.dataProcessTool.pdsELO.PieVisualization;
import eu.scy.tools.dataProcessTool.pdsELO.ProcessedData;
import eu.scy.tools.dataProcessTool.pdsELO.ProcessedDatasetELO;
import eu.scy.tools.dataProcessTool.pdsELO.ProcessedHeader;
import eu.scy.tools.dataProcessTool.pdsELO.XYAxis;
import eu.scy.tools.dataProcessTool.print.DataPrint;

import eu.scy.tools.dataProcessTool.print.FitexHTML;
import eu.scy.tools.dataProcessTool.synchro.Locker;
import eu.scy.tools.fitex.analyseFn.Function;
import java.util.List;
import java.util.Locale;
import java.util.Iterator;
import java.util.LinkedList;
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
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;

/**
 * controller db
 * @author Marjolaine Bodin
 */
public class DataControllerDB implements ControllerInterface{
    private String fitexFunctionFileName = "fitexFunctions.xml";
    /* db*/
    private DataBaseCommunication dbC;
    /* url */
    private URL dataURL;

    /* interface */
    private DataProcessToolPanel dataToolPanel;
    /* liste des types d'operations possibles */
    private TypeOperation[] tabTypeOperations;
    /* liste des visualisations possibles */
    private TypeVisualization[] tabTypeVisual;
    /* liste des fonctions predef*/
    private ArrayList<PreDefinedFunctionCategory> listPreDefinedFunction;
    private Color[] plotsColor;
    private Color[] functionsColor;
    /* dataset */
    private ArrayList<Dataset> listDataset;
    private ArrayList<Integer> listNoDefaultCol;
    
    /* mission */
    private Mission mission ;
    /* utilisateur */
    private long dbKeyMission;
    private long dbKeyUser;
    private long dbKeyGroup;
    private Group group;
    private long dbKeyLabDoc;
    private String labDocName;

    private ArrayList<Mission> listUserMission;
    private ArrayList<ArrayList<Dataset>> listUserDatasetMission;

    private FitexHTML fitexHtml;
    private long dbKeyTrace = -1;

    /* locker */
    private Locker locker;

   
    public DataControllerDB(DataProcessToolPanel dataToolPanel, URL url, long dbKeyMission, long dbKeyUser, long dbKeyGroup, long dbKeyLabDoc, String labDocName) {
        this.dataToolPanel = dataToolPanel ;
        this.dbKeyMission = dbKeyMission ;
        this.dbKeyUser = dbKeyUser ;
        this.dbKeyLabDoc = dbKeyLabDoc;
        this.labDocName = labDocName;
        this.dbKeyGroup = dbKeyGroup;
        this.dataURL = url;
        dbC = new DataBaseCommunication(dataURL, DataConstants.DB_LABBOOK_FITEX, dbKeyMission, ""+dbKeyUser);
        this.listDataset = new ArrayList();
        this.listNoDefaultCol = new ArrayList();
        this.fitexHtml = new FitexHTML(dataToolPanel);
    }


    /* chargement et initialisation des donnees */
    @Override
    public CopexReturn load(){
        // mission
        ArrayList v = new ArrayList();
        CopexReturn cr = CopexMissionFromDB.loadMissionFromDB(dbC, dbKeyMission, v);
        if (cr.isError()){
            return cr;
        }
        mission = (Mission)v.get(0);
        //user 
        v = new ArrayList();
        cr = ToolUserFromDB.loadGroupFromDB(dbC, dbKeyGroup, v);
        if (cr.isError()){
            return cr;
        }
        group = (Group)v.get(0);
        if(group == null || mission == null){
            return  new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_LOAD_DATA"), false);
        }
        // chargement trace
        v = new ArrayList();
        cr = TraceFromDB.getIdTrace(dbC, dbKeyMission, dbKeyUser, v);
        if(cr.isError())
            return cr;
        dbKeyTrace = (Long)v.get(0);
        // LOCKER
        DataBaseCommunication dbLabBook = new DataBaseCommunication(dataURL, DataConstants.DB_LABBOOK, dbKeyMission, ""+dbKeyUser);
        this.locker = new Locker(dataToolPanel, dbLabBook, dbKeyUser);
        if(locker.isLocked(dbKeyLabDoc)){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_LOCKED"), false);
        }
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
        //predefined fct
        cr = loadPreDefinedFunction();
        if(cr.isError())
            return cr;
        // load dataset
        boolean allDatasetLocked =false;
        ArrayList<String> listDatasetLocked = new ArrayList();
        v = new ArrayList();
        cr = DatasetFromDB.getAllDatasetFromDB(dbC, locker,mission, dbKeyLabDoc, tabTypeOperations,tabTypeVisual, plotsColor, functionsColor,v);
        if (cr.isError()){
            return cr;
        }
        listDataset = (ArrayList<Dataset>)v.get(0);
        listDatasetLocked = (ArrayList<String>)v.get(1);
        allDatasetLocked = (Boolean)v.get(2);
        for(Iterator<Dataset> l = listDataset.iterator();l.hasNext();){
            listNoDefaultCol.add(l.next().getNbCol());
        }
         setLockers(listDataset);
        // charge les missions et noms des dataset de l'utilisateur
        v = new ArrayList();
        cr = CopexMissionFromDB.getAllMissionFromDB(this.dbC, dbKeyUser, mission.getDbKey(), v);
        if (cr.isError()){
            return cr;
        }
        listUserMission = (ArrayList<Mission>)v.get(0);
        v = new ArrayList();
        cr = DatasetFromDB.getAllDatasetFromDB(this.dbC, dbKeyUser, listUserMission, v);
        if (cr.isError()){
            return cr;
        }
        listUserDatasetMission = (ArrayList<ArrayList<Dataset>>)v.get(0);
        // si pas de dataset, on en cree un par dedaut
        if(listDataset.isEmpty() && !allDatasetLocked){
            v = new ArrayList();
            cr = createTable(dataToolPanel.getBundleString("DEFAULT_DATASET_NAME"), v);
            if(cr.isError())
                return cr;
        }
        for(Iterator<Dataset> ds = listDataset.iterator();ds.hasNext();){
            Dataset myds = ds.next();
            cr = DatasetFromDB.updateDatasetNameInDB(dbC, myds.getDbKey(), labDocName);
            if(cr.isError())
                return cr;
            myds.setName(labDocName);
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
        this.dataToolPanel.initDataProcessingTool(tabTypeVisualC, tabTypeOpC, listPreDefinedFunction, listDsC);
        this.dataToolPanel.displayDatasetLocked(listDatasetLocked);
        return new CopexReturn();
    }

    /* chargement des fonctions predefinies */
    private CopexReturn loadPreDefinedFunction(){
        listPreDefinedFunction = new ArrayList();
        InputStreamReader fileReader = null;
        SAXBuilder builder = new SAXBuilder(false);
        try{
            InputStream s = this.getClass().getClassLoader().getResourceAsStream("languages/"+fitexFunctionFileName);
            fileReader = new InputStreamReader(s, "utf-8");
            Document doc = builder.build(fileReader);
            Element fitexFunction = doc.getRootElement();
            for (Iterator<Element> variableElem = fitexFunction.getChildren(PreDefinedFunctionCategory.TAG_PRE_DEFINED_CATEGORY).iterator(); variableElem.hasNext();) {
                listPreDefinedFunction.add(new PreDefinedFunctionCategory(variableElem.next()));
            }

        }catch(IOException e1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_LOAD_FITEX_FUNCTION")+" "+e1, false);
        }catch(JDOMException e2){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_LOAD_FITEX_FUNCTION")+" "+e2, false);
        }
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
            DataHeader h = new DataHeader(-1, this.dataToolPanel.getBundleString("DEFAULT_DATAHEADER_NAME")+(i+1), this.dataToolPanel.getBundleString("DEFAULT_DATAHEADER_UNIT"), i,DataConstants.DEFAULT_TYPE_COLUMN, this.dataToolPanel.getBundleString("DEFAULT_DATAHEADER_DESCRIPTION"), null, false, DataConstants.NB_DECIMAL_UNDEFINED, DataConstants.NB_SIGNIFICANT_DIGITS_UNDEFINED, DataConstants.DEFAULT_DATASET_ALIGNMENT);
            tabHeader[i] = h;
        }
        // enregistrement base
        ArrayList v2 = new ArrayList();
        CopexReturn cr = DatasetFromDB.createDatasetInDB(dbC, dataToolPanel.getBundleString("DEFAULT_DATASET_NAME"), nbRows,  nbCol, dbKeyLabDoc,  v2);
        if (cr.isError())
            return cr;
        long dbKey = (Long)v2.get(0);
        for (int i=0; i<nbCol; i++){
            v2 = new ArrayList();
            cr = DatasetFromDB.createDataHeaderInDB(dbC,tabHeader[i].getValue(), tabHeader[i].getUnit(), i, tabHeader[i].getType(), tabHeader[i].getDescription(),tabHeader[i].getFormulaValue(), tabHeader[i].isScientificNotation(), tabHeader[i].getNbShownDecimals(), tabHeader[i].getNbSignificantDigits(), dbKey, v2 );
            if (cr.isError())
                return cr;
            long dbKeyH = (Long)v2.get(0);
            tabHeader[i].setDbKey(dbKeyH);
        }
        // creation ds
        Dataset dataset = new Dataset(dbKey, mission, dbKeyLabDoc, name, nbCol, nbRows, tabHeader, data, listOp, listVis, DataConstants.EXECUTIVE_RIGHT);
        setLocker(dbKeyLabDoc);
        cr = exportHTML(dataset);
        if(cr.isError())
            return cr;
        cr = updateLabdocStatus();
        if(cr.isError())
            return cr;
        listDataset.add(dataset);
        listNoDefaultCol.add(nbCol);
        v.add(dataset.clone());
        return new CopexReturn();

    }


    /* chargement d'un ELO */
    @Override
    public CopexReturn loadELO(String xmlString, String dsName){
        Dataset ds = null;
        ArrayList v = new ArrayList();
        CopexReturn cr = getElo(xmlString, v);
        if(v.size() > 0)
            ds = (Dataset)v.get(0);
        if(ds != null){
            setLocker(ds.getDbKeyLabDoc());
            if(dsName != null)
                ds.setName(dsName);
            this.listDataset.add(ds);
            this.listNoDefaultCol.add(1);
            this.dataToolPanel.setDataset((Dataset)ds.clone(), false);
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
    private CopexReturn getDataset(eu.scy.elo.contenttype.dataset.DataSet eloDs, String name, ArrayList v){
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
                dataHeader[i] = new DataHeader(-1, header.getColumns().get(i).getSymbol(),unit, i, type, header.getColumns().get(i).getDescription(), null, false, DataConstants.NB_DECIMAL_UNDEFINED, DataConstants.NB_SIGNIFICANT_DIGITS_UNDEFINED, DataConstants.DEFAULT_DATASET_ALIGNMENT) ;
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
            long dbKey = 1;
            // enregistrement base
           ArrayList v2 = new ArrayList();
           cr = DatasetFromDB.createDatasetInDB(dbC, name, nbRows,  nbCols, dbKeyLabDoc, v2);
           if (cr.isError())
               return cr;
           dbKey = (Long)v2.get(0);
           for (int i=0; i<nbCols; i++){
               v2 = new ArrayList();
               cr = DatasetFromDB.createDataHeaderInDB(dbC,dataHeader[i].getValue(), dataHeader[i].getUnit(), i, dataHeader[i].getType(), dataHeader[i].getDescription(),dataHeader[i].getFormulaValue(), dataHeader[i].isScientificNotation(), dataHeader[i].getNbShownDecimals(), dataHeader[i].getNbSignificantDigits(),  dbKey, v2 );
               if (cr.isError())
                  return cr;
               long dbKeyH = (Long)v2.get(0);
               dataHeader[i].setDbKey(dbKeyH);
           }
           for(int i=0; i<nbRows; i++){
               for(int j=0; j<nbCols; j++){
                   if(data[i][j] != null){
                       v2 = new ArrayList();
                       cr = DatasetFromDB.createDataInDB(dbC, data[i][j].getValue(), i, j, dbKey, v2);
                       if(cr.isError())
                           return cr;
                       long dbKeyData = (Long)v2.get(0);
                       data[i][j].setDbKey(dbKeyData);
                   }
               }
            }
            ds = new Dataset(dbKey, mission, dbKeyLabDoc, name, nbCols, nbRows,  dataHeader, data, listOperation,listVisualization, DataConstants.EXECUTIVE_RIGHT  );
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
        eu.scy.elo.contenttype.dataset.DataSet eloDs = eloPDS.getDataset();
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
            dataHeader[i] = new DataHeader(-1, header.getColumns().get(i).getSymbol(), unit, i, type, header.getColumns().get(i).getDescription(), null, false, DataConstants.NB_DECIMAL_UNDEFINED, DataConstants.NB_SIGNIFICANT_DIGITS_UNDEFINED, DataConstants.DEFAULT_DATASET_ALIGNMENT) ;
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
        // headers pds
        List<ProcessedHeader> listPdsHeader = pds.getListProcessedHeaders();
        if(listPdsHeader != null){
            for(Iterator<ProcessedHeader> h = listPdsHeader.iterator(); h.hasNext();){
                ProcessedHeader ph = h.next();
                int noCol = -1;
                try{
                    noCol = Integer.parseInt(ph.getColumnId());
                }catch(NumberFormatException e){
                    continue;
                }
                if(noCol >=0 && noCol < dataHeader.length && dataHeader[noCol] != null && ph.getFormula() != null && !ph.getFormula().equals("")){
                    dataHeader[noCol].setFormulaValue(ph.getFormula());
                }
                if(noCol >=0 && noCol < dataHeader.length && dataHeader[noCol] != null){
                    dataHeader[noCol].setScientificNotation(ph.isScientificNotation());
                    dataHeader[noCol].setNbShownDecimals(ph.getNbShownDecimals());
                    dataHeader[noCol].setNbSignificantDigits(ph.getNbSignificantDigits());
                }
                int dataAlign = ph.getDataAlignment();
                if(noCol >=0 && noCol < dataHeader.length && dataHeader[noCol] != null){
                    dataHeader[noCol].setDataAlignment(dataAlign);
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
                    plots.add(new PlotXY(-1, dataHeader[axis.getX_axis()], dataHeader[axis.getY_axis()], axis.getPlotColor()));
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
                        FunctionModel fm = new FunctionModel(-1, lfm.get(j).getDescription(),lfm.get(j).getType(),  new Color(lfm.get(j).getColorR(),lfm.get(j).getColorG(),lfm.get(j).getColorB() ), listParam, lfm.get(j).getIdPredefFunction());
                        listFunctionModel.add(fm);
                    }
                }
                myVis = new Graph(-1, vis.getName(), type,  paramGraph, listFunctionModel);
            }
            listVisualization.add(myVis);
        }
        // creation du dataset
        Dataset ds = new Dataset(-1,mission, dbKeyLabDoc, pds.getName(), nbCols, nbRows,  dataHeader, data, listOperation,listVisualization , DataConstants.EXECUTIVE_RIGHT );
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
        cr = computeAllData(dataset);
        if(cr.isError())
            return cr;
        // recalcule des operations
        dataset.calculateOperation();
        cr = exportHTML(dataset);
        if(cr.isError())
            return cr;
        cr = updateLabdocStatus();
        if(cr.isError())
            return cr;
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
        operation.setDbKey(dbKey);
        // creation en memoire
        dataset.addOperation(operation);
        // en v[0] le nouveau dataset clone et en v[1] le dataOperation
        cr = exportHTML(dataset);
        if(cr.isError())
            return cr;
        cr = updateLabdocStatus();
        if(cr.isError())
            return cr;
        listDataset.set(idDs, dataset);
        v.add(dataset.clone());
        v.add(operation.clone());
        return new CopexReturn();
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
    public CopexReturn updateDataHeader(Dataset ds, boolean confirm, int colIndex, String title, String unit, String description, String type, String formulaValue, Function function, boolean scientificNotation, int nbShownDecimals, int nbSignificantDigits, ArrayList v){
        int idDs = getIdDataset(ds.getDbKey());
        if(idDs == -1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        Dataset dataset = listDataset.get(idDs);


        // header existe t il deja ?
        DataHeader header = dataset.getDataHeader(colIndex);
        boolean wasFormula = false;
        String oldTitle = "";
        if(header == null){
            // header n'existe pas : on le cree
            ArrayList v2 = new ArrayList();
            CopexReturn cr = DatasetFromDB.createDataHeaderInDB(dbC, title,unit,  colIndex, type, description, formulaValue, scientificNotation, nbShownDecimals, nbSignificantDigits, dataset.getDbKey(),v2);
            if (cr.isError())
                return cr;
            long dbKey = (Long)v2.get(0);
            dataset.setDataHeader(new DataHeader(dbKey, title, unit, colIndex, type, description, formulaValue, scientificNotation, nbShownDecimals, nbSignificantDigits, DataConstants.DEFAULT_DATASET_ALIGNMENT), colIndex);
        }else{
            oldTitle = header.getValue();
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
            cr = DatasetFromDB.updateDataHeaderInDB(dbC, header.getDbKey(), title, unit, description, type, formulaValue, scientificNotation, nbShownDecimals, nbSignificantDigits);
            if (cr.isError())
                return cr;
            // header existe => on le modifie
            wasFormula = header.isFormula();
            header.setValue(title);
            header.setUnit(unit);
            header.setDescription(description);
            header.setType(type);
            header.setFormulaValue(formulaValue);
            header.setScientificNotation(scientificNotation);
            header.setNbShownDecimals(nbShownDecimals);
            header.setNbSignificantDigits(nbSignificantDigits);
            dataset.setDataHeader(header, header.getNoCol());
            if(type.equals(DataConstants.TYPE_STRING)){
                // supprime les operations si type : double=> string
                // supprimer ou modfifie les visualizations si type : double=> string
                ArrayList[] tab = dataset.removeOperationAndVisualizationOn(colIndex);
                ArrayList<DataOperation> listOperationToUpdate = tab[0];
                ArrayList<DataOperation> listOperationToDel = tab[1];
                ArrayList<Visualization> listVisualizationToUpdate = tab[2];
                ArrayList<Visualization> listVisualizationToDel = tab[3];
                ArrayList<Long> listPlotsToRemove = tab[4];
                cr = OperationFromDB.updateNoInDB(dbC, listOperationToUpdate);
                if(cr.isError())
                    return cr;
                cr = OperationFromDB.deleteOperationFromDB(dbC, listOperationToDel);
                if(cr.isError())
                    return cr;
                cr = VisualizationFromDB.deletePlotsFromDB(dbC, listPlotsToRemove);
                if(cr.isError())
                    return cr;
                cr = VisualizationFromDB.deleteVisualizationFromDB(dbC, listVisualizationToDel);
                if(cr.isError())
                    return cr;
            }
        }
        // if name changed, control formula of the others columns to replace eventually the parameter names
        int nbCols = dataset.getNbCol();
        for(int j=0; j<nbCols; j++){
            if(dataset.getDataHeader(j) != null && dataset.getDataHeader(j).getFormulaValue() != null){
                String f = dataset.getDataHeader(j).getFormulaValue();
                if(!oldTitle.equals("") && f.contains(oldTitle)){
                    String newF = f.replace(oldTitle,  title);
                    CopexReturn cr = DatasetFromDB.updateHeaderFormulaInDB(dbC,dataset.getDataHeader(j).getDbKey(), newF);
                    if(cr.isError())
                        return cr;
                    dataset.getDataHeader(j).setFormulaValue(newF);
                }
            }
        }
        // calcul des données si formule
        if((wasFormula && formulaValue == null) ){
            int nbRows = dataset.getNbRows();
            for(int i=0; i<nbRows; i++){
                setDataNull(dataset, i, colIndex);
            }
        }
        // en fait on recalcule tout, ca peut entreainer des changements dans les autres colonnes
//        CopexReturn cr = computeDataCol(dataset, colIndex, formulaValue, function);
//        if(cr.isError())
//            return cr;
        CopexReturn cr = computeAllData(dataset);
        if(cr.isError())
            return cr;
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
            }else if(visu instanceof SimpleVisualization){
                if (((SimpleVisualization)visu).getHeader().getNoCol() == colIndex){
                    ((SimpleVisualization)visu).getHeader().setValue(title);
                    ((SimpleVisualization)visu).getHeader().setUnit(unit);
                }
                if(((SimpleVisualization)visu).getHeaderLabel() != null && ((SimpleVisualization)visu).getHeaderLabel().getNoCol() == colIndex){
                    ((SimpleVisualization)visu).getHeaderLabel().setValue(title);
                    ((SimpleVisualization)visu).getHeaderLabel().setUnit(unit);
                }
            }
        }
//        cr = exportHTML(dataset);
//        if(cr.isError())
//            return cr;
        cr = updateLabdocStatus();
        if(cr.isError())
            return cr;
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
        cr = exportHTML(dataset);
        if(cr.isError())
            return cr;
        cr = updateLabdocStatus();
        if(cr.isError())
            return cr;
        // en v[0] : le nouveau dataset
        listDataset.set(idDs, dataset);
        v.add(dataset.clone());
        return new CopexReturn();
    }

    /* mise a jour d'une valeur : donnee dataset */
    @Override
    public CopexReturn updateData(Dataset ds, int rowIndex, int colIndex, String value,  ArrayList v){
        return updateData(ds, rowIndex, colIndex, value, true, v);
    }

    private CopexReturn updateData(Dataset ds, int rowIndex, int colIndex, String value,  boolean compute, ArrayList v){
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
        // recalcule les donnees def par des formules
        if(compute){
            CopexReturn cr  = computeDataRow(dataset, rowIndex);
            if(cr.isError())
                return cr;
        }
        // recalcule des operations
        dataset.calculateOperation();
        
        // en v[0] : le nouveau dataset
        //System.out.println("dataset apres updateData : "+dataset.toString());
        CopexReturn cr = exportHTML(dataset);
        if(cr.isError())
            return cr;
        cr = updateLabdocStatus();
        if(cr.isError())
            return cr;
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
        cr = exportHTML(dataset);
        if(cr.isError())
            return cr;
        cr = updateLabdocStatus();
        if(cr.isError())
            return cr;
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
        vis = (Visualization)v2.get(1);
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
       cr = exportHTML(dataset);
        if(cr.isError())
            return cr;
        cr = updateLabdocStatus();
        if(cr.isError())
            return cr;
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
        cr = exportHTML(dataset);
        if(cr.isError())
            return cr;
        cr = updateLabdocStatus();
        if(cr.isError())
            return cr;
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
        unsetLocker(dataset);
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
            ArrayList<DataHeader> listDataHeader = new ArrayList();
            ArrayList<Data> listDataFromHeader = new ArrayList();
            int nbRows = dataset.getNbRows();
            for(Iterator<Integer> i = listNoDataCol.iterator();i.hasNext();){
                int id = i.next();
                if(dataset.getDataHeader(id) != null)
                    listDataHeader.add(dataset.getDataHeader(id));
                for(int r=0; r<nbRows; r++){
                    if(dataset.getData(r, id) != null){
                        listDataFromHeader.add(dataset.getData(r, id));
                    }
                }
            }
            ArrayList[] tabDel2 = dataset.removeCols(listNoDataCol);
            cr = DatasetFromDB.deleteDataHeaderFromDB(dbC, listDataHeader);
            if(cr.isError())
                return cr;
            cr = DatasetFromDB.deleteDataFromDB(dbC, listDataFromHeader);
            ArrayList<DataOperation> listOpToDel = (ArrayList<DataOperation>)tabDel[1];
            ArrayList<Visualization> listVisToDel = (ArrayList<Visualization>)tabDel[3];
            ArrayList<DataOperation> listOpToUpdate = (ArrayList<DataOperation>)tabDel[0];
            ArrayList<Visualization> listVisToUpdate = (ArrayList<Visualization>)tabDel[2];
            ArrayList<Long> listPlotsToRemove = new ArrayList();
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
            for(Iterator<Long> o = ((ArrayList<Long>)tabDel2[4]).iterator();o.hasNext();){
                listPlotsToRemove.add(o.next());
            }
            tabDel[0] = listOpToUpdate;
            tabDel[1] = listOpToDel;
            tabDel[2] = listVisToUpdate;
            tabDel[3] = listVisToDel;
            cr = OperationFromDB.deleteOperationFromDB(dbC, listOpToDel);
            if(cr.isError())
                return cr;
            cr = OperationFromDB.updateNoInDB(dbC, listOpToUpdate);
            if(cr.isError())
                return cr;
            cr = VisualizationFromDB.deleteVisualizationFromDB(dbC, listVisToDel);
            if(cr.isError())
                return cr;
            cr = VisualizationFromDB.deletePlotsFromDB(dbC, listPlotsToRemove);
            if(cr.isError())
                return cr;
            // suppresion des données
            cr = DatasetFromDB.deleteDataFromDB(dbC, listData);
            if(cr.isError())
                return cr;
            cr = DatasetFromDB.updateDatasetMatriceInDB(dbC, dataset.getDbKey(),dataset.getNbRows(), dataset.getNbCol());
            if(cr.isError())
                return cr;
            cr = DatasetFromDB.updateNoInDB(dbC, dataset);
            if (cr.isError())
                return cr;
            dataset.removeData(listData);
            cr = computeAllData(dataset);
            if(cr.isError())
                return cr;
            dataset.calculateOperation();
            //System.out.println("dataset apres remove : "+dataset.toString());
            listDataset.set(idDs, dataset);
            v.add((Dataset)dataset.clone());
            v.add(tabDel);
        }
//        CopexReturn cr = exportHTML(dataset);
//        if(cr.isError())
//            return cr;
        CopexReturn cr = updateLabdocStatus();
        if(cr.isError())
            return cr;
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
    public CopexReturn setFunctionModel(Dataset ds, Visualization vis, String description, char type, Color fColor, ArrayList<FunctionParam> listParam, String idPredefFunction,ArrayList v){
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
            CopexReturn cr = VisualizationFromDB.createFunctionModelInDB(dbC, graph.getDbKey(), description, type, idFunctionColor,  listParam, idPredefFunction, v2);
            if (cr.isError())
                return cr;
            long dbKey = (Long)v2.get(0);
            listParam = (ArrayList<FunctionParam>)v2.get(1);
            // memoire
            FunctionModel myFm = new FunctionModel(dbKey, description,type, fColor, listParam, idPredefFunction);
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
                if(fm.getDescription() != null && fm.getDescription().equals(description) && fm.getType() == type  && sameParam(listParam, fm.getListParam())){
                    int nbP = listParam.size();
                    for(int i=0; i<nbP; i++){
                        listParam.get(i).setDbKey(fm.getListParam().get(i).getDbKey());
                    }
                    CopexReturn cr = VisualizationFromDB.updateFunctionModelParamInDB(dbC, listParam);
                    if (cr.isError())
                        return cr;
                }else{
                    // modification
                    ArrayList v2 = new ArrayList();
                    CopexReturn cr = VisualizationFromDB.updateFunctionModelInDB(dbC, fm.getDbKey(), description,type,  listParam, idPredefFunction, v2);
                    if (cr.isError())
                        return cr;
                    listParam = (ArrayList<FunctionParam>)v2.get(0);
                }
                fm.setType(type);
                fm.setDescription(description);
                fm.setListParam(listParam);
                fm.setIdPredefFunction(idPredefFunction);
            }
        }
//        CopexReturn cr = exportHTML(dataset);
//        if(cr.isError())
//            return cr;
        CopexReturn cr = updateLabdocStatus();
        if(cr.isError())
            return cr;
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

    private static boolean sameParam(ArrayList<FunctionParam> p1, ArrayList<FunctionParam> p2){
        if(p1 != null  && p2 != null && p1.size()==p2.size()){
            int n = p1.size();
            for(int i=0; i<n; i++){
                if(!p1.get(i).getParam().equals(p2.get(i).getParam()))
                    return false;
            }
            return true;
        }
        return false;
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
                cr= updateDataHeader(dataset, true, idBefore+j, this.dataToolPanel.getBundleString("DEFAULT_DATAHEADER_NAME")+(no+1), this.dataToolPanel.getBundleString("DEFAULT_DATAHEADER_UNIT"), this.dataToolPanel.getBundleString("DEFAULT_DATAHEADER_DESCRIPTION"),DataConstants.DEFAULT_TYPE_COLUMN, null, null, false, DataConstants.NB_DECIMAL_UNDEFINED, DataConstants.NB_SIGNIFICANT_DIGITS_UNDEFINED, v2);
                if(cr.isError())
                    return cr;
                listNoDefaultCol.set(idDs, no+1);
            }
        }
        else
            dataset.insertRow(nb, idBefore);
        cr = DatasetFromDB.updateNoInDB(dbC, dataset);
        if (cr.isError())
            return cr;
//        cr = exportHTML(dataset);
//        if(cr.isError())
//            return cr;
        cr = updateLabdocStatus();
        if(cr.isError())
            return cr;
        v.add(dataset.clone());
        listDataset.set(idDs, dataset);
        //System.out.println("dataset apres insertData : "+dataset.toString());
        return new CopexReturn();
    }

    /* impression */
    @Override
    public CopexReturn printDataset(Dataset dataset, boolean printDataset, DataTableModel model, ArrayList<Visualization> listVis, ArrayList<Object> listGraph){
        String fileName = "copex-"+mission.getCode();
        DataPrint pdfPrint = new DataPrint(dataToolPanel, mission, group,dataset, model, printDataset, listVis, listGraph,fileName );
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
        CopexReturn cr = exportHTML(dataset);
        if(cr.isError())
            return cr;
        cr = updateLabdocStatus();
        if(cr.isError())
            return cr;
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
        CopexReturn cr = exportHTML(dataset);
        if(cr.isError())
            return cr;
        cr = updateLabdocStatus();
        if(cr.isError())
            return cr;
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
        CopexReturn cr = DatasetFromDB.createDatasetInDB(dbC, name, nbRows,  nbCol, dbKeyLabDoc,  v2);
        if (cr.isError())
            return cr;
        long dbKey = (Long)v2.get(0);
        
        for (int i=0; i<nbCol; i++){
            v2 = new ArrayList();
            cr = DatasetFromDB.createDataHeaderInDB(dbC, headers[i],units[i], i,types[i], descriptions[i], null, false, DataConstants.NB_DECIMAL_UNDEFINED, DataConstants.NB_SIGNIFICANT_DIGITS_UNDEFINED, dbKey, v2);
            if(cr.isError())
                return cr;
            long dbKeyH = (Long)v2.get(0);
            tabHeader[i] = new DataHeader(dbKeyH, headers[i], units[i], i, types[i], descriptions[i], null, false, DataConstants.NB_DECIMAL_UNDEFINED, DataConstants.NB_SIGNIFICANT_DIGITS_UNDEFINED, DataConstants.DEFAULT_DATASET_ALIGNMENT);
        }
        // creation ds
        Dataset ds = new Dataset(dbKey, mission, dbKeyLabDoc, name, nbCol, nbRows, tabHeader,  data, listOp, listVis, DataConstants.EXECUTIVE_RIGHT);
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
        cr = computeDataRow(dataset, idRow);
        if(cr.isError())
            return cr;
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
                   cr = VisualizationFromDB.updateGraphParamInDB(dbC, aVis.getDbKey(), pg);
                    if(cr.isError())
                        return cr;
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
            // maj plots
            for(Iterator<PlotXY> plots = paramGraph.getPlots().iterator(); plots.hasNext();){
                PlotXY plot = plots.next();
                if(plot.getDbKey() == -1){
                    // creation
                    ArrayList v2 = new ArrayList();
                    cr = VisualizationFromDB.createPlotInDB(dbC, vis.getDbKey(), plot, plotsColor, v2);
                    if(cr.isError())
                        return cr;
                    long dbKeyPlot = (Long)v2.get(0);
                    plot.setDbKey(dbKeyPlot);
                }else{
                    int id = isPlotToUpdate(((Graph)vis).getParamGraph().getPlots(), plot);
                    if(id != -1){
                        cr = VisualizationFromDB.updatePlotInDB(dbC, ((Graph)vis).getParamGraph().getPlots().get(id).getDbKey(), plot, plotsColor);
                        if(cr.isError())
                            return cr;
                    }
                }
            }
            // cherche les plos a supprimer
            for(Iterator<PlotXY> p =((Graph)vis).getParamGraph().getPlots().iterator();p.hasNext(); ){
                PlotXY plot = p.next();
                boolean remove = true;
                for(Iterator<PlotXY> plots = paramGraph.getPlots().iterator(); plots.hasNext();){
                    if(plots.next().getDbKey() == plot.getDbKey())
                        remove = false;
                }
                if(remove){
                    cr = VisualizationFromDB.removePlotFromDB(dbC,  plot.getDbKey());
                    if(cr.isError())
                        return cr;
                }
            }
            //maj memoire
            ((Graph)vis).setParamGraph(paramGraph);
        }
//        CopexReturn cr = exportHTML(dataset);
//        if(cr.isError())
//            return cr;
        CopexReturn cr = updateLabdocStatus();
        if(cr.isError())
            return cr;
        listDataset.set(idDs, dataset);
        v.add(vis.clone());
        return new CopexReturn();
    }

    /* recherche si le plot est a modifier */
    private int isPlotToUpdate(ArrayList<PlotXY> listPlots, PlotXY plot){
        int nb = listPlots.size();
        for(int i=0; i<nb; i++){
            if(listPlots.get(i).getDbKey() == plot.getDbKey()){
                if(!plot.getPlotColor().equals(listPlots.get(i).getPlotColor()) ||
                    plot.getHeaderX().getDbKey() !=   listPlots.get(i).getHeaderX().getDbKey() ||
                    plot.getHeaderY().getDbKey() !=   listPlots.get(i).getHeaderY().getDbKey())
                    return i;
            }
        }
        return -1;
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
            double  x_min  = minX - Math.abs(minX/5) ;
            double  y_min  = minY -Math.abs(minY/5) ;
            double  x_max  = maxX +Math.abs(maxX/5) ;
            double  y_max  = maxY +Math.abs(maxY/5);
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
                cr = VisualizationFromDB.updateGraphParamInDB(dbC, vis.getDbKey(), pg);
                if(cr.isError())
                    return cr;
                ((Graph)vis).setParamGraph(pg);
                dataset.getListVisualization().set(idVis, ((Graph)vis));
            }
        }
        CopexReturn cr = exportHTML(dataset);
        if(cr.isError())
            return cr;
        cr = updateLabdocStatus();
        if(cr.isError())
            return cr;
        listDataset.set(idDs, dataset);
        v.add(vis);
        return new CopexReturn();
    }

    @Override
    public CopexReturn mergeDataset(Dataset currentDs, Mission m, Dataset dsToMerge, ArrayList v){
        Dataset datasetToMerge = null;
        int idDs = getIdDataset(currentDs.getDbKey());
        if(idDs == -1)
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        Dataset dataset = listDataset.get(idDs);
        int idToMerge = getIdDataset(dsToMerge.getDbKey());
        if(idToMerge == -1){
            // il faut le charger
        }else{
            datasetToMerge = listDataset.get(idToMerge);
        }
        if(datasetToMerge == null)
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        //merge dataset et datasetToMerge
        int nbRows1 = dataset.getNbRows() ;
        int nbCols1 = dataset.getNbCol() ;
        int nbRows2 = datasetToMerge.getNbRows();
        int nbCols2 = datasetToMerge.getNbCol();
        /* mise a jour du nombre de lignes et de col*/
        int newNbRows = nbRows1;
        if(nbRows2 > nbRows1){
            //on ajoute des lignes
            dataset.insertRow(nbRows2-nbRows1, nbRows1);
            newNbRows +=nbRows2-nbRows1;
        }
        dataset.insertCol(nbCols2, nbCols1);
        CopexReturn cr = DatasetFromDB.updateDatasetMatriceInDB(dbC, dataset.getDbKey(), newNbRows, nbCols2);
        if(cr.isError())
            return cr;
        ArrayList v2 = new ArrayList();
        for (int j=0; j<nbCols2; j++){
            if (datasetToMerge.getDataHeader(j) != null){
                v2 = new ArrayList();
                cr = DatasetFromDB.createDataHeaderInDB(dbC, datasetToMerge.getDataHeader(j).getValue() , datasetToMerge.getDataHeader(j).getUnit(), j+nbCols1, datasetToMerge.getDataHeader(j).getType(),  datasetToMerge.getDataHeader(j).getDescription(), datasetToMerge.getDataHeader(j).getFormulaValue(),datasetToMerge.getDataHeader(j).isScientificNotation(), datasetToMerge.getDataHeader(j).getNbShownDecimals(), datasetToMerge.getDataHeader(j).getNbSignificantDigits(), dataset.getDbKey(), v2);
                if(cr.isError())
                    return cr;
                long dbKeyHeader = (Long)v2.get(0);
                dataset.setDataHeader(new DataHeader(dbKeyHeader,datasetToMerge.getDataHeader(j).getValue() , datasetToMerge.getDataHeader(j).getUnit(), j+nbCols1, datasetToMerge.getDataHeader(j).getType(), datasetToMerge.getDataHeader(j).getDescription(), datasetToMerge.getDataHeader(j).getFormulaValue(), datasetToMerge.getDataHeader(j).isScientificNotation(), datasetToMerge.getDataHeader(j).getNbShownDecimals(), datasetToMerge.getDataHeader(j).getNbSignificantDigits(), datasetToMerge.getDataHeader(j).getDataAlignment()), j+nbCols1);

            }
            for (int i=0; i<nbRows2; i++){
                Data nd = null;
                if (datasetToMerge.getData(i, j) != null){
                    v2 = new ArrayList();
                    cr = DatasetFromDB.createDataInDB(dbC, datasetToMerge.getData(i, j).getValue(), i,j+nbCols1, dataset.getDbKey(),  v2);
                    if(cr.isError())
                        return cr;
                    long dbKeyData = (Long)v2.get(0);
                    nd = new Data(dbKeyData, datasetToMerge.getData(i, j).getValue(), i,j+nbCols1, datasetToMerge.getData(i, j).isIgnoredData() );
                    if(datasetToMerge.getData(i, j).isIgnoredData()){
                        ArrayList<Data> listDataIgnored = new ArrayList();
                        listDataIgnored.add(nd);
                        cr = DatasetFromDB.setDataIgnoredInDB(dbC, listDataIgnored, true);
                        if(cr.isError())
                            return cr;
                    }
                }
                dataset.setData(nd, i, j+nbCols1);
            }
        }
        // merge des operations
        ArrayList<DataOperation> listOp = datasetToMerge.getListOperation();
        int nbOp = listOp.size();
        for (int i=0; i<nbOp; i++){
            DataOperation op = listOp.get(i);
            ArrayList<Integer> listNo = new ArrayList();
            int n= op.getListNo().size();
            for (int j=0; j<n; j++){
                listNo.add(op.getListNo().get(j)+nbCols1);
            }
            DataOperation operation = new DataOperation(-1, op.getName(), op.getTypeOperation(), op.isOnCol(), listNo);
            v2 = new ArrayList();
            cr = OperationFromDB.createOperationInDB(dbC, dataset.getDbKey(), operation, v2);
            if(cr.isError())
                return cr;
            long dbKeyOperation = (Long)v2.get(0);
            operation.setDbKey(dbKeyOperation);
            dataset.addOperation(operation);
        }
        // merge des vis
        ArrayList<Visualization> listVis = datasetToMerge.getListVisualization();
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
                v2 = new ArrayList();
                cr = VisualizationFromDB.createVisualizationInDB(dbC, dataset.getDbKey(), sv, plotsColor, v2);
                if(cr.isError())
                    return cr;
                long dbKeyVis = (Long)v2.get(0);
                sv.setDbKey(dbKeyVis);
                dataset.addVisualization(sv);
            }else if(vis instanceof Graph){
                ArrayList<PlotXY> listPlots = new ArrayList();
                for (Iterator<PlotXY> p = ((Graph)vis).getParamGraph().getPlots().iterator();p.hasNext();){
                    PlotXY plot = p.next();
                    listPlots.add(new PlotXY(-1, dataset.getDataHeader(plot.getHeaderX().getNoCol()+nbCols1), dataset.getDataHeader(plot.getHeaderY().getNoCol()+nbCols1), plot.getPlotColor()));
                }
                ParamGraph pg = new ParamGraph(listPlots, ((Graph)vis).getParamGraph().getX_min(),((Graph)vis).getParamGraph().getX_max(), ((Graph)vis).getParamGraph().getX_min(), ((Graph)vis).getParamGraph().getY_max(), ((Graph)vis).getParamGraph().getDeltaX(), ((Graph)vis).getParamGraph().getDeltaY(),  ((Graph)vis).getParamGraph().isDeltaFixedAutoscale());
                Graph g = new Graph(-1, vis.getName(), vis.getType(),pg,((Graph)vis).getListFunctionModel());
                v2 = new ArrayList();
                cr = VisualizationFromDB.createVisualizationInDB(dbC, dataset.getDbKey(), g, plotsColor, v2);
                if(cr.isError())
                    return cr;
                long dbKeyVis = (Long)v2.get(0);
                g = (Graph)v2.get(1);
                g.setDbKey(dbKeyVis);
                dataset.addVisualization(g);
            }
        }
        cr = exportHTML(dataset);
        if(cr.isError())
            return cr;
        cr = updateLabdocStatus();
        if(cr.isError())
            return cr;
        v.add(dataset.clone());
        return new CopexReturn();
    }

   /*merge un ELO avec l'ELO courant*/
    @Override
    public CopexReturn mergeELO(Dataset d, Element elo, boolean confirm){
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
            // if ds contains only 1 numerical row &  nb of columns fits to dataset => ask the user
            // - add as a new row ?
            // - merge ?
            // - add the values of ds to every row of dataset (kind of a matrix-add-operation) ?
            // - multiply the values of ds with every row of datast (kind of a matrix-multipy-operation)

            // if nb of columns &  types fits to dataset   => ask the user
            // - merge?
            //- add new rows?
            
            //merge dataset et ds
            int nbRows1 = dataset.getNbRows() ;
            int nbCols1 = dataset.getNbCol() ;
            int nbRows2 = ds.getNbRows();
            int nbCols2 = ds.getNbCol();
            boolean oneRow = ds.getNbRowsData() == 1;
            if(confirm && oneRow && nbCols1 == nbCols2 && dataset.isAllColumnDouble() && ds.isAllColumnDouble()){
                dataToolPanel.askForMergeType(dataset, ds,elo,  true);
                return new CopexReturn();
            }
            if(confirm && nbCols1 == nbCols2 && dataset.isFitTypeColumn(ds)){
                dataToolPanel.askForMergeType(dataset, ds,elo,  false);
                return new CopexReturn();
            }
            if(nbRows2 > nbRows1){
                //on ajoute des lignes
                dataset.insertRow(nbRows2-nbRows1, nbRows1);
            }
            dataset.insertCol(nbCols2, nbCols1);
            cr = DatasetFromDB.updateDatasetMatriceInDB(dbC, dataset.getDbKey(), dataset.getNbRows(), dataset.getNbCol());
            if(cr.isError())
                return cr;
            for (int j=0; j<nbCols2; j++){
                if (ds.getDataHeader(j) != null){
                    ArrayList v2 = new ArrayList();
                    cr = DatasetFromDB.createDataHeaderInDB(dbC, ds.getDataHeader(j).getValue(),ds.getDataHeader(j).getUnit(),  j+nbCols1, ds.getDataHeader(j).getType(), ds.getDataHeader(j).getDescription(), ds.getDataHeader(j).getFormulaValue(), ds.getDataHeader(j).isScientificNotation(), ds.getDataHeader(j).getNbShownDecimals(), ds.getDataHeader(j).getNbSignificantDigits(), dataset.getDbKey(),v2);
                    if (cr.isError())
                        return cr;
                    long dbKeyHeader = (Long)v2.get(0);
                    dataset.setDataHeader(new DataHeader(dbKeyHeader,ds.getDataHeader(j).getValue() , ds.getDataHeader(j).getUnit(), j+nbCols1, ds.getDataHeader(j).getType(), ds.getDataHeader(j).getDescription(), ds.getDataHeader(j).getFormulaValue(), ds.getDataHeader(j).isScientificNotation(), ds.getDataHeader(j).getNbShownDecimals(), ds.getDataHeader(j).getNbSignificantDigits(), ds.getDataHeader(j).getDataAlignment()), j+nbCols1);
                }
                for (int i=0; i<nbRows2; i++){
                    Data nd = null;
                    if (ds.getData(i, j) != null){
                        nd = new Data(-1, ds.getData(i, j).getValue(), i,j+nbCols1, ds.getData(i, j).isIgnoredData() );
                        ArrayList v2 = new ArrayList();
                        cr = DatasetFromDB.createDataInDB(dbC,ds.getData(i, j).getValue(), i, j+nbCols1, dataset.getDbKey(), v2);
                        if(cr.isError())
                            return cr;
                        long dbKeyData = (Long)v2.get(0);
                        nd.setDbKey(dbKeyData);
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
                ArrayList v2 = new ArrayList();
                DataOperation operation = new DataOperation(-1, op.getName(), op.getTypeOperation(), op.isOnCol(), listNo);
                cr = OperationFromDB.createOperationInDB(dbC, ds.getDbKey(), operation, v2);
                if (cr.isError())
                    return cr;
                long dbKeyOp = (Long)v2.get(0);
                operation.setDbKey(dbKeyOp);
                dataset.addOperation(operation);
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
                        listPlots.add(new PlotXY(-1, dataset.getDataHeader(plot.getHeaderX().getNoCol()+nbCols1), dataset.getDataHeader(plot.getHeaderY().getNoCol()+nbCols1), plot.getPlotColor()));
                    }
                    ParamGraph pg = new ParamGraph(listPlots, ((Graph)vis).getParamGraph().getX_min(),((Graph)vis).getParamGraph().getX_max(), ((Graph)vis).getParamGraph().getX_min(), ((Graph)vis).getParamGraph().getY_max(), ((Graph)vis).getParamGraph().getDeltaX(), ((Graph)vis).getParamGraph().getDeltaY(),  ((Graph)vis).getParamGraph().isDeltaFixedAutoscale());
                    Graph g = new Graph(-1, vis.getName(), vis.getType(),pg,((Graph)vis).getListFunctionModel());
                    dataset.addVisualization(g);
                }
                ArrayList v2 = new ArrayList();
                cr = createVisualization(dataset, vis,false, v2);
                if(cr.isError())
                    return cr;
            }
            //mise a jour graphique
            this.dataToolPanel.setDataset((Dataset)dataset.clone(), true);
        }
        return cr;
    }

    /** merge the rows of the datasets */
    @Override
    public CopexReturn mergeRowELO(Dataset ds1, Dataset ds2){
        int idDs = getIdDataset(ds1.getDbKey());
        if(idDs ==-1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        Dataset dataset1 = listDataset.get(idDs);
        int nbRows1 = dataset1.getNbRows() ;
        int nbCols1 = dataset1.getNbCol() ;
        int nbRows2 = ds2.getNbRows();
        int nbCols2 = ds2.getNbCol();
        if(nbCols1 != nbCols2){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        // control types
        for (int j=0; j<nbCols1; j++){
            if(dataset1.getDataHeader(j) != null && ds2.getDataHeader(j) != null && !dataset1.getDataHeader(j).getType().equals(ds2.getDataHeader(j).getType())){
                return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
            }
        }
        // insert the news rows
        dataset1.insertRow(nbRows2, nbRows1);
        CopexReturn cr = DatasetFromDB.updateDatasetMatriceInDB(dbC, dataset1.getDbKey(), dataset1.getNbRows(), dataset1.getNbCol());
        if(cr.isError())
            return cr;
        // add as new rows
        for(int i=0; i<nbRows2; i++){
           for(int j=0; j<nbCols2; j++){
               dataset1.setData(ds2.getData(i, j), i+nbRows1, j);
               ArrayList v2 = new ArrayList();
               cr = updateData(dataset1,i+nbRows1, j, ds2.getData(i, j) == null ?"":ds2.getData(i, j).getValue() , v2);
               if(cr.isError())
                   return cr;
           }
        }
        cr = computeAllData(dataset1);
        if(cr.isError())
            return cr;
        // recalcule des operations
        dataset1.calculateOperation();
        //mise a jour graphique
        this.dataToolPanel.setDataset((Dataset)dataset1.clone(), true);
        return new CopexReturn();
    }
    /** add the values of ds to every row of dataset (kind of a matrix-add-operation)  */
    @Override
    public CopexReturn mergeMatrixAddOperation(Dataset ds1, Dataset ds2){
        int idDs = getIdDataset(ds1.getDbKey());
        if(idDs ==-1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        Dataset dataset1 = listDataset.get(idDs);
        int nbRows1 = dataset1.getNbRows() ;
        int nbCols1 = dataset1.getNbCol() ;
        int nbRows2 = ds2.getNbRows();
        int nbCols2 = ds2.getNbCol();
        if(nbCols1 != nbCols2){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        if(ds2.getNbRowsData() != 1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        // control types
        for (int j=0; j<nbCols1; j++){
            if(dataset1.getDataHeader(j) != null && ds2.getDataHeader(j) != null && !dataset1.getDataHeader(j).getType().equals(ds2.getDataHeader(j).getType())){
                return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
            }
            if(dataset1.getDataHeader(j) != null && !dataset1.getDataHeader(j).isDouble()){
                return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
            }
        }
        int idR = ds2.getFirstRowData();
        if(idR == -1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        for(int i=0; i<nbRows1; i++){
            for (int j=0; j<nbCols1; j++){
                if(dataset1.getData(i, j) == null){
                    dataset1.setData(new Data(-1, ds2.getData(idR, j) == null ? "" :ds2.getData(idR, j).getValue() , i, j, false), i, j);
                }else{
                    dataset1.getData(i, j).addToValue(ds2.getData(idR, j) == null ? 0 :ds2.getData(idR, j).getDoubleValue());
                }
                ArrayList v2 = new ArrayList();
                CopexReturn cr = updateData(dataset1,i, j, dataset1.getData(i, j).getValue() , v2);
                if(cr.isError())
                   return cr;
            }
        }
        CopexReturn cr = computeAllData(dataset1);
        if(cr.isError())
            return cr;
        // recalcule des operations
        dataset1.calculateOperation();
        //mise a jour graphique
        this.dataToolPanel.setDataset((Dataset)dataset1.clone(), true);
        return new CopexReturn();
    }
    /** multiply the values of ds to every row of dataset (kind of a matrix-multiply-operation) */
    @Override
    public CopexReturn mergeMatrixMultiplyOperation(Dataset ds1, Dataset ds2){
        int idDs = getIdDataset(ds1.getDbKey());
        if(idDs ==-1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        Dataset dataset1 = listDataset.get(idDs);
        int nbRows1 = dataset1.getNbRows() ;
        int nbCols1 = dataset1.getNbCol() ;
        int nbRows2 = ds2.getNbRows();
        int nbCols2 = ds2.getNbCol();
        if(nbCols1 != nbCols2){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        if(ds2.getNbRowsData() != 1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        int idR = ds2.getFirstRowData();
        if(idR == -1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        // control types
        for (int j=0; j<nbCols1; j++){
            if(dataset1.getDataHeader(j) != null && ds2.getDataHeader(j) != null && !dataset1.getDataHeader(j).getType().equals(ds2.getDataHeader(j).getType())){
                return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
            }
            if(dataset1.getDataHeader(j) != null && !dataset1.getDataHeader(j).isDouble()){
                return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
            }
        }
        for(int i=0; i<nbRows1; i++){
            for (int j=0; j<nbCols1; j++){
                if(dataset1.getData(i, j) == null){
                    dataset1.setData(new Data(-1, ds2.getData(idR, j) == null ? "" :"0" , i, j, false), i, j);
                }else{
                    dataset1.getData(i, j).multiplyToValue(ds2.getData(idR, j) == null ? 0 :ds2.getData(idR, j).getDoubleValue());
                }
                ArrayList v2 = new ArrayList();
                CopexReturn cr = updateData(dataset1,i, j, dataset1.getData(i, j).getValue() , v2);
                if(cr.isError())
                   return cr;
            }
        }
        CopexReturn cr = computeAllData(dataset1);
        if(cr.isError())
            return cr;
        // recalcule des operations
        dataset1.calculateOperation();
        //mise a jour graphique
        this.dataToolPanel.setDataset((Dataset)dataset1.clone(), true);
        return new CopexReturn();
    }

    /* convertir un elo au format xml en dataset*/
    private CopexReturn getElo(String xmlContent,ArrayList v){
        Dataset ds = null;
        if(isDatasetType(xmlContent)){
            eu.scy.elo.contenttype.dataset.DataSet eloDs ;
            try{
                eloDs = new eu.scy.elo.contenttype.dataset.DataSet(xmlContent) ;
            }catch(JDOMException e){
                return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_LOAD_DATA"), false) ;
            }
            //ds = getDataset(eloDs, "ELO");
            ArrayList v2 = new ArrayList();
            CopexReturn cr = getDataset(eloDs, dataToolPanel.getBundleString("DEFAULT_DATASET_NAME"),v2);
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
    public CopexReturn paste(long dbKeyDs, CopyDataset copyDs, int[] selCell, ArrayList v){
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
        int nbRowsToPaste = 0;
        if(copyDs.getListRow() != null)
            nbRowsToPaste = copyDs.getListRow().size();
        int nbColsToPaste = 0;
        if(copyDs.getListHeader() != null)
            nbColsToPaste = copyDs.getListHeader().size();
        int nbR = dataset.getNbRows() ;
        int nbC = dataset.getNbCol() ;
        Dataset cloneDs = (Dataset)dataset.clone();
        
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
        CopexReturn cr = DatasetFromDB.updateDatasetMatriceInDB(dbC, dataset.getDbKey(), dataset.getNbRows(), dataset.getNbCol());
        if(cr.isError())
            return cr;
        if(pasteHeader){
           for (int j=0; j<nbColsToPaste; j++){
               DataHeader header = null;
               if (copyDs.getListHeader().get(j) != null){
                   header = new DataHeader(-1, copyDs.getListHeader().get(j).getValue(),copyDs.getListHeader().get(j).getUnit(),  idC+j, copyDs.getListHeader().get(j).getType(), copyDs.getListHeader().get(j).getDescription(), copyDs.getListHeader().get(j).getFormulaValue(), copyDs.getListHeader().get(j).isScientificNotation(), copyDs.getListHeader().get(j).getNbShownDecimals(), copyDs.getListHeader().get(j).getNbSignificantDigits(), copyDs.getListHeader().get(j).getDataAlignment());
                   DataHeader[] headers = new DataHeader[2];
                   headers[0] = null;
                   if(idC+j< oldDs.getNbCol()){
                       headers[0] = oldDs.getDataHeader(idC+j);
                   }
                   headers[1] = header;
                   listDataHeader.add(headers);
               }
               if(header != null && (dataset.getDataHeader(idC+j) == null || dataset.getDataHeader(idC+j).getDbKey() == -1 ) ){
                   //creation
                   ArrayList v2 = new ArrayList();
                   cr = DatasetFromDB.createDataHeaderInDB(dbC, header.getValue(), header.getUnit(), header.getNoCol(),
                           header.getType(), header.getDescription(), header.getFormulaValue(), header.isScientificNotation(),
                           header.getNbShownDecimals(), header.getNbSignificantDigits(), dataset.getDbKey(), v2);
                   if(cr.isError())
                        return cr;
                  long dbKeyH = (Long)v2.get(0);
                  header.setDbKey(dbKeyH);
               }else if(header != null){
                   cr = DatasetFromDB.updateDataHeaderInDB(dbC, dataset.getDataHeader(idC+j).getDbKey(), header.getValue(), header.getUnit(), header.getDescription(),
                           header.getType(), header.getFormulaValue(), header.isScientificNotation(),
                           header.getNbShownDecimals(), header.getNbSignificantDigits());
                    if(cr.isError())
                        return cr;
               }else if(header == null && dataset.getDataHeader(idC+j) != null && dataset.getDataHeader(idC+j).getDbKey() != -1){
                   cr = DatasetFromDB.updateDataHeaderInDB(dbC, dataset.getDataHeader(idC+j).getDbKey(), "", "", "",
                           DataConstants.TYPE_DOUBLE, "", false,
                           DataConstants.NB_DECIMAL_UNDEFINED,DataConstants.NB_SIGNIFICANT_DIGITS_UNDEFINED);
                    if(cr.isError())
                        return cr;
               }
               dataset.setDataHeader(header, idC+j);
           }
        }
        if(copyDs.getListData() != null){
            int i0 = 0;
            int j0 = 0;
            int nbData = copyDs.getListData().size();
            if(nbData > 0){
                Data d = copyDs.getListData().get(0);
                i0 = d.getNoRow();
                j0 = d.getNoCol();
                if(d.getValue() == null || d.getValue().equals("")){
                    d = null;
                }
                Data nd = null;
                if (d != null){
                    if(!dataset.getDataHeader(d.getNoCol()).isDouble() && dataset.getDataHeader(idC) != null && dataset.getDataHeader(idC).isDouble()){
                        dataset = cloneDs;
                        cr = new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_PASTE_COHERENCE"), false);
                            return cr;
                    }
                    nd = new Data(-1, d.getValue(), idR, idC, d.isIgnoredData()) ;
                    Data[] datas = new Data[2];
                   datas[0] = null;
                   if(idC< oldDs.getNbCol() && idR <oldDs.getNbRows()){
                       datas[0] = oldDs.getData(idR, idC);
                   }
                   datas[1] = nd;
                   listData.add(datas);
                }
                if(nd != null){
                    if(dataset.getData(idR, idC) == null || dataset.getData(idR, idC).getDbKey() == -1){
                        ArrayList v2 = new ArrayList();
                        cr = DatasetFromDB.createDataInDB(dbC, nd.getValue(), idR, idC, dataset.getDbKey(), v2);
                        if(cr.isError())
                            return cr;
                        long dbKeyData = (Long)v2.get(0);
                        nd.setDbKey(dbKeyData);
                    }else{
                        cr = DatasetFromDB.updateDataInDB(dbC, dataset.getData(idR, idC).getDbKey(), nd.getValue());
                        if(cr.isError())
                            return cr;
                    }
                }else{
                    if(dataset.getData(idR, idC) != null && dataset.getData(idR, idC).getDbKey() > -1){
                        cr = DatasetFromDB.updateDataInDB(dbC, dataset.getData(idR, idC).getDbKey(), "");
                        if(cr.isError())
                            return cr;
                    }
                }
                dataset.setData(nd, idR, idC);

                for(int k=1; k<nbData; k++){
                    d = copyDs.getListData().get(k);
                    int i= d.getNoRow();
                    int j= d.getNoCol();
                    if(d.getValue() == null || d.getValue().equals("")){
                        d = null;
                    }
                    nd = null;
                    if (d != null){
                        nd = new Data(-1, d.getValue(), idR+i-i0, idC+j-j0, d.isIgnoredData()) ;
                        Data[] datas = new Data[2];
                        datas[0] = null;
                        if(idC+j-j0< oldDs.getNbCol() && idR+i-i0 <oldDs.getNbRows()){
                            datas[0] = oldDs.getData(idR+i-i0, idC+j-j0);
                        }
                        datas[1] = nd;
                        listData.add(datas);
                    }
                    if(idR+i-i0 >= dataset.getNbRows()){
                        // insertion de nouvelles lignes
                        dataset.insertRow(1 , dataset.getNbRows());
                        listNoRow.add(dataset.getNbRows());
                    }
                    if(idC+j-j0 >= dataset.getNbCol()){
                        dataset.insertCol(1, dataset.getNbCol());
                        listNoCol.add(dataset.getNbCol());
                        DataHeader header = new DataHeader(-1, this.dataToolPanel.getBundleString("DEFAULT_DATAHEADER_NAME")+(idC+j-j0+1), this.dataToolPanel.getBundleString("DEFAULT_DATAHEADER_UNIT"), idC+j-j0,DataConstants.DEFAULT_TYPE_COLUMN, this.dataToolPanel.getBundleString("DEFAULT_DATAHEADER_DESCRIPTION"), null, false, DataConstants.NB_DECIMAL_UNDEFINED, DataConstants.NB_SIGNIFICANT_DIGITS_UNDEFINED, DataConstants.DEFAULT_DATASET_ALIGNMENT);
                        DataHeader[] headers = new DataHeader[2];
                        
                        dataset.setDataHeader(header, idC+j);
                        ArrayList v2 = new ArrayList();
                        cr = DatasetFromDB.createDataHeaderInDB(dbC, header.getValue(), header.getUnit(), header.getNoCol(),
                           header.getType(), header.getDescription(), header.getFormulaValue(), header.isScientificNotation(),
                           header.getNbShownDecimals(), header.getNbSignificantDigits(), dataset.getDbKey(), v2);
                        if(cr.isError())
                            return cr;
                        long dbKeyH = (Long)v2.get(0);
                        header.setDbKey(dbKeyH);
                        headers[0] = null;
                        headers[1] = header;
                        listDataHeader.add(headers);
                    }
                    if(nd != null){
                        if(dataset.getData(idR+i-i0, idC+j-j0) == null || dataset.getData(idR+i-i0, idC+j-j0).getDbKey() == -1){
                            ArrayList v2 = new ArrayList();
                            cr = DatasetFromDB.createDataInDB(dbC, nd.getValue(), idR+i-i0, idC+j-j0, dataset.getDbKey(), v2);
                            if(cr.isError())
                                return cr;
                            long dbKeyData = (Long)v2.get(0);
                            nd.setDbKey(dbKeyData);
                        }else{
                            cr = DatasetFromDB.updateDataInDB(dbC, dataset.getData(idR+i-i0, idC+j-j0).getDbKey(), nd.getValue());
                            if(cr.isError())
                                return cr;
                        }
                    }else{
                        if(dataset.getData(idR+i-i0, idC+j-j0) != null && dataset.getData(idR+i-i0, idC+j-j0).getDbKey() > -1){
                            cr = DatasetFromDB.updateDataInDB(dbC, dataset.getData(idR+i-i0, idC+j-j0).getDbKey(), "");
                            if(cr.isError())
                                return cr;
                        }
                    }
                    dataset.setData(nd, idR+i-i0, idC+j-j0);
                }
                cr = DatasetFromDB.updateDatasetMatriceInDB(dbC, dataset.getDbKey(), dataset.getNbRows(), dataset.getNbCol());
                if(cr.isError())
                    return cr;
            }
        }
        cr = computeAllData(dataset);
        if(cr.isError())
            return cr;
        // recalcule des operations
        dataset.calculateOperation();
        listDataset.set(idDs, dataset);
        v.add(dataset.clone());
        v.add(listData);
        v.add(listDataHeader);
        listRowAndCol[0] = listNoRow ;
        listRowAndCol[1] = listNoCol;
        v.add(listRowAndCol);
        cr = exportHTML(dataset);
        if(cr.isError())
            return cr;
        cr = updateLabdocStatus();
        if(cr.isError())
            return cr;
        return new CopexReturn();
    }

    /* lecture de fichier cvs  => elo ds */
    @Override
    public CopexReturn importCSVFile(File file, String sepField, String sepText, String charEncoding, ArrayList v){
        String g="\"";
        if (file == null) {
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_FILE_EXIST"), false);
        }
        if(!file.getName().substring(file.getName().length()-3).equals("csv")){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_FILE_CSV"), false);
        }
        //StringTokenizer lineParser;
        try{
            boolean sepQuotMark = sepField.equals(DataConstants.CSV_SEPARATOR_COMMA) && sepText != null && sepText.equals(DataConstants.CSV_SEPARATOR_TEXT_QUOTATION_MARK);
            CopexReturn cr=  new CopexReturn();
            InputStreamReader fileReader = null;
            try {
                //fileReader = new InputStreamReader(new FileInputStream(file), "utf-8");
                fileReader = new InputStreamReader(new FileInputStream(file), charEncoding);
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
            int nbC = 0;
            try{
                int id = 0;
                while ((line = reader.readLine()) != null) {
                    //lineParser = new StringTokenizer(line, sepField);
                    String[] tabValues = line.split(sepField);
                    int j=0;
                    DataSetRow datasetRow;
                    List<String> values = new LinkedList<String>();
                    String val = null;
                    //while (lineParser.hasMoreElements()) {
                    for(int k=0; k<tabValues.length; k++){
                        //value = (String) lineParser.nextElement();
                        value = tabValues[k];
                        if(sepQuotMark){
                            if (value.startsWith(g) && value.endsWith(g)){
                                value = value.substring(1, value.length()-1);
                            }else if(value.startsWith(g)){
                                val = value.substring(1);
                            }else if(value.endsWith(g) && val != null){
                                value = val+"."+value.substring(0, value.length()-1);
                                val = null;
                            }
                        }
                        //System.out.println(value);
                        if(val != null)
                            continue;
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
                        nbC = header.getColumnCount();
                        ds = new eu.scy.elo.contenttype.dataset.DataSet(headers);
                    } else {
                        if(values.size() < nbC){
                            for(int k=0; k<nbC; k++){
                                values.add("");
                            }
                        }
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
                cr = new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_IMPORT_FILE")+e, false);
            }catch(Exception ex){
                cr = new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_IMPORT_FILE")+ex, false);
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
        cr = exportHTML(dataset);
        if(cr.isError())
            return cr;
        cr = updateLabdocStatus();
        if(cr.isError())
            return cr;
        return new CopexReturn();
    }

    /* fermeture d'un dataset */
    @Override
    public CopexReturn closeDataset(Dataset ds){
        int id = getIdDataset(ds.getDbKey());
        if(id == -1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
//        this.listDataset.remove(id);
//        this.listNoDefaultCol.remove(id);
        this.listDataset.get(id).setOpen(false);
        unsetLocker(listDataset.get(id));
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
        CopexReturn cr = exportHTML(dataset);
        if(cr.isError())
            return cr;
        cr = updateLabdocStatus();
        if(cr.isError())
            return cr;
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
        // on ajoute les protocoles de la mission en cours qui sont fermes
        int nbDClose = 0;
        ArrayList<Dataset> dsClose = new ArrayList();
        int n = listDataset.size();
        for (int k=0; k<n; k++){
            if (!listDataset.get(k).isOpen() && listDataset.get(k).getMission() != null && listDataset.get(k).getMission().getDbKey() == mission.getDbKey()){
                dsClose.add((Dataset)listDataset.get(k).clone());
                nbDClose++;
            }
        }
        // on ajoute la mission en cours
        if (nbDClose > 0){
            listMission.add((Mission)this.mission.clone());
            listDatasetMission.add(dsClose);
        }
        v.add(listMission);
        v.add(listDatasetMission);
        return new CopexReturn();
    }

    /* open dataset*/
    @Override
    public CopexReturn openDataset(Mission mission, Dataset ds){
        int id = getIdDataset(ds.getDbKey());
        if(id != -1){
            // dataset deja charge, de la mission
            if(locker.isLocked(ds.getDbKeyLabDoc())){
                return new CopexReturn(dataToolPanel.getBundleString("MSG_WARNING_DATASET_LOCKED")+"\n"+listDataset.get(id).getName(), false);
            }
            listDataset.get(id).setOpen(true);
            this.dataToolPanel.setDataset((Dataset)listDataset.get(id).clone(), false);
        }else{
            int idM = getIdMission(mission.getDbKey());
            if(idM != -1){
                int  idDs = getIdDataset(ds.getDbKey(), listUserDatasetMission.get(idM));
                if(idDs != -1){
                    ArrayList v2 = new ArrayList();
                    CopexReturn cr = loadDataset(listUserDatasetMission.get(idM).get(idDs),listUserMission.get(idM), v2);
                    if(cr.isError())
                        return cr;
                    Dataset dataset = (Dataset)v2.get(0);
                    this.dataToolPanel.setDataset((Dataset)dataset.clone(), false);
                }
            }
        }
        return new CopexReturn();
    }

    private int getIdMission(long dbKey){
        int nbM = this.listUserMission.size();
        for (int i=0; i<nbM; i++){
            if (dbKey == this.listUserMission.get(i).getDbKey()){
                return i;
            }
        }
        return -1;
    }

    private int getIdDataset(long dbKey, ArrayList<Dataset> listDs){
        int nb = listDs.size();
        for(int i=0; i<nb; i++){
            if(dbKey == listDs.get(i).getDbKey())
                return i;
        }
        return -1;
    }

    private CopexReturn loadDataset(Dataset ds, Mission mission, ArrayList v){
        if(locker.isLocked(ds.getDbKey())){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_WARNING_DATASET_LOCKED")+"\n"+ds.getName(), false);
        }
        ArrayList v2 = new ArrayList();
        CopexReturn cr = DatasetFromDB.getDatasetFromDB(dbC, ds.getDbKey(),mission, tabTypeOperations, tabTypeVisual, plotsColor, functionsColor, v2);
        if(cr.isError())
            return cr;
        Dataset dataset = (Dataset)v2.get(0);
        setLocker(dataset.getDbKeyLabDoc());
        listDataset.add(dataset);
        listNoDefaultCol.add(dataset.getNbCol());
        v.add(dataset);
        return new CopexReturn();
    }

     /* pose les verrous sur une feuille de donnees*/
    private void setLocker(long dbKeyLabdoc){
        this.locker.setLabdocLocker(dbKeyLabdoc);
    }


    /* pose les verrous sur une liste feuilles de donnees */
    private void setLockers(ArrayList<Dataset> listDs){
        int nb = listDs.size();
        ArrayList listId = new ArrayList();
        for (int i=0; i<nb; i++){
            listId.add(listDs.get(i).getDbKeyLabDoc());
        }
        this.locker.setLabdocLockers(listId);
    }

    /* deverouille une feuille de donnees */
    private void unsetLocker(Dataset ds){
        this.locker.unsetLabdocLocker(ds.getDbKeyLabDoc());
    }
    /* deverouille une liste de feuille de donnees */
    private void unsetLockers(ArrayList<Dataset> listDs){
        int nb = listDs.size();
        ArrayList listId = new ArrayList();
        for (int i=0; i<nb; i++){
            listId.add(listDs.get(i).getDbKeyLabDoc());
        }
        this.locker.unsetLabdocLockers(listId);
    }

    /* arret de fitex */
    @Override
    public CopexReturn stopFitex(){
        for(Iterator<Dataset> d = listDataset.iterator();d.hasNext();){
            CopexReturn cr = exportHTML(d.next());
            if(cr.isError())
                return cr;
        }
        // deverouille les feuilles de donnnees
        unsetLockers(this.listDataset);
        this.locker.stop();
        // unset the locker in labbook !!
        //CopexMissionFromDB.unsetLabdocLockerInDB(dbC, dbKeyLabDoc);
        return new CopexReturn();
    }

    private CopexReturn computeAllData(Dataset dataset){
        int nbCol = dataset.getNbCol();
        for(int j=0; j<nbCol; j++){
            if(dataset.getDataHeader(j) != null && dataset.getDataHeader(j).isFormula()){
                String formula = dataset.getDataHeader(j).getFormulaValue();
                Function function = dataToolPanel.getFunction(formula);
                CopexReturn cr = computeDataCol(dataset, j, formula, function);
                if(cr.isError())
                    return cr;
            }
        }
        return new CopexReturn();
    }

    private CopexReturn computeDataRow(Dataset dataset, int noRow){
        int nbCol = dataset.getNbCol();
        for(int j=0; j<nbCol; j++){
            if(dataset.getDataHeader(j) != null && dataset.getDataHeader(j).isFormula()){
                Function function = dataToolPanel.getFunction(dataset.getDataHeader(j).getFormulaValue());
                CopexReturn cr = computeData(dataset,noRow,  j, function);
                if(cr.isError())
                    return cr;
            }
        }
        return new CopexReturn();
    }

    private CopexReturn computeData(Dataset dataset, int rowIndex, int colIndex, Function function){
        double val = Double.NaN;
        boolean isNonDefined=false;
        for (String param:function.getMapParametre().keySet()) {
             DataHeader headerParam = dataset.getDataHeader(param);
             val = Double.NaN;
             if(headerParam == null){
                 isNonDefined = true;
                 break;
             }
             if(dataset.getData(rowIndex, headerParam.getNoCol()) != null)
                 val = dataset.getData(rowIndex, headerParam.getNoCol()).getDoubleValue();
             function.getMapParametre().get(param).setValeur(val);
             function.setValeurParametre(headerParam.getValue(), val);
         }
         ArrayList v2 = new ArrayList();
         String value = null;
         if(!isNonDefined){
             value = ""+function.getExpression().valeur(0);
         }
         CopexReturn cr = updateData(dataset, rowIndex, colIndex, value, false, v2);
         return cr;
    }

    private CopexReturn computeDataCol(Dataset dataset, int colIndex, String formulaValue, Function function){
        if(formulaValue != null && function != null){
            int nbRows = dataset.getNbRows();
            for(int i=0; i<nbRows; i++){
                CopexReturn cr = computeData(dataset, i, colIndex, function);
                if(cr.isError())
                    return cr;
            }
        }
        return new CopexReturn();
    }

    /** return true in v[0] if the specified dataset is the dataset from the labdoc */
    @Override
    public CopexReturn isLabDocDataset(Dataset ds, ArrayList v){
        v.add(ds == null? false : ds.getDbKeyLabDoc() == dbKeyLabDoc);
        return new CopexReturn();
    }

    /** export in html format */
    private CopexReturn exportHTML(Dataset ds ){
        ArrayList v = new ArrayList();
        ArrayList<Object> listGraph = dataToolPanel.getListGraph();
        CopexReturn cr = fitexHtml.exportDatasetHTML(dataURL, ds, dataToolPanel.getDataTableModel(ds), listGraph, dbKeyLabDoc, v);
        if(cr.isError())
            return cr;
        String s = (String)v.get(0);
        cr = DatasetFromDB.setPreviewLabdocInDB(dbC, dbKeyLabDoc, s);
        return cr;
    }

    /** update the labdoc status */
    private CopexReturn updateLabdocStatus(){
        CopexReturn cr = CopexMissionFromDB.updateLabdocStatusInDB(dbC, dbKeyLabDoc, dbKeyUser, dbKeyGroup);
        return cr;
    }

    /** import a GMBL file, return v[0] the dataset elo */
     @Override
    public CopexReturn importGMBLFile(File file,ArrayList v){
        if (file == null) {
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_FILE_EXIST"), false);
        }
        if(!file.getName().substring(file.getName().length()-4).equals("gmbl")){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_FILE_GMBL"), false);
        }
        InputStreamReader fileReader = null;
        SAXBuilder builder = new SAXBuilder(false);
        try{
            fileReader = new InputStreamReader(new FileInputStream(file), "utf-8");
            Document doc = builder.build(fileReader, file.getAbsolutePath());
            Element element = doc.getRootElement();
            GmblDataset gmblDataset = new GmblDataset(element);
            List<DataSetHeader> headers = new LinkedList<DataSetHeader>();
            DataSetHeader header;
            List<DataSetColumn> listC = new LinkedList<DataSetColumn>();
            int nbBMaxRow = 0;
            for(Iterator<GmblColumn> col = gmblDataset.getColumns().iterator(); col.hasNext();){
                GmblColumn gmblColumn = col.next();
                DataSetColumn c = new DataSetColumn(gmblColumn.getColumnTitle(), "", gmblColumn.getType(), gmblColumn.getUnit());
                listC.add(c);
                nbBMaxRow = Math.max(nbBMaxRow, gmblColumn.getCellValues().size());
            }
            header = new DataSetHeader(listC, dataToolPanel.getLocale());
            headers.add(header);
            eu.scy.elo.contenttype.dataset.DataSet ds = new eu.scy.elo.contenttype.dataset.DataSet(headers);
            for(int i=0; i<nbBMaxRow; i++){
                List<String> values = new LinkedList<String>();
                for(Iterator<GmblColumn> col = gmblDataset.getColumns().iterator(); col.hasNext();){
                    GmblColumn gmblColumn = col.next();
                    List<String> cellsValues = gmblColumn.getCellValues();
                    if(i<cellsValues.size() && cellsValues.get(i) != null){
                        values.add(cellsValues.get(i));
                    }else{
                        values.add("");
                    }
                }
                DataSetRow datasetRow = new DataSetRow(values);
                ds.addRow(datasetRow);
            }
            v.add(ds);
        }catch(IOException e1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_IMPORT_FILE")+" "+e1, false);
        }catch(JDOMException e2){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_IMPORT_FILE")+" "+e2, false);
        }
        return new CopexReturn();
    }

     /** log a user action in the db*/
    @Override
    public CopexReturn logUserActionInDB(String type, List<FitexProperty> attribute) {
        return TraceFromDB.logUserActionInDB(dbC, mission.getDbKey(), dbKeyUser, dbKeyTrace, type, attribute);
    }

     /** export a html version */
    @Override
    public CopexReturn exportHTML(){
        if(listDataset.size() > 0)
            return exportHTML(listDataset.get(0));
        return new CopexReturn();
    }

    /** justify the text for the selected headers */
    @Override
    public CopexReturn justifyText(Dataset ds, int align, ArrayList<DataHeader> listHeader, ArrayList v){
        int idDs = getIdDataset(ds.getDbKey());
        if(idDs == -1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        Dataset dataset = listDataset.get(idDs);
        CopexReturn cr = DatasetFromDB.updateJustifyTextInDB(dbC, align, listHeader);
        if(cr.isError()){
            return cr;
        }
        // maj en memoire
        for(Iterator<DataHeader> h = listHeader.iterator();h.hasNext();){
            int noCol = h.next().getNoCol();
            if(dataset.getDataHeader(noCol) != null)
                dataset.getDataHeader(noCol).setDataAlignment(align);
        }
        // en v[0] le nouveau dataset clone
        listDataset.set(idDs, dataset);
        v.add(dataset.clone());
        return new CopexReturn();
    }
}
