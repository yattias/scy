/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.controller;

import eu.scy.elo.contenttype.dataset.DataSet;
import eu.scy.elo.contenttype.dataset.DataSetColumn;
import eu.scy.elo.contenttype.dataset.DataSetHeader;
import eu.scy.elo.contenttype.dataset.DataSetRow;
import roolo.elo.JDomStringConversion;


import eu.scy.client.tools.dataProcessTool.common.*;
import eu.scy.client.tools.fitex.main.DataProcessToolPanel;
import eu.scy.client.tools.dataProcessTool.dataTool.DataTableModel;
import eu.scy.client.tools.dataProcessTool.gmbl.GmblColumn;
import eu.scy.client.tools.dataProcessTool.gmbl.GmblDataset;
import eu.scy.client.tools.dataProcessTool.gmbl.GmblDocument;
import eu.scy.client.tools.dataProcessTool.logger.FitexProperty;
import eu.scy.client.tools.dataProcessTool.pdsELO.BarVisualization;
import eu.scy.client.tools.dataProcessTool.pdsELO.GraphVisualization;
import eu.scy.client.tools.dataProcessTool.pdsELO.IgnoredData;
import eu.scy.client.tools.dataProcessTool.pdsELO.PieVisualization;
import eu.scy.client.tools.dataProcessTool.pdsELO.ProcessedData;
import eu.scy.client.tools.dataProcessTool.pdsELO.ProcessedDatasetELO;
import eu.scy.client.tools.dataProcessTool.pdsELO.ProcessedHeader;
import eu.scy.client.tools.dataProcessTool.pdsELO.XYAxis;
import eu.scy.client.tools.dataProcessTool.utilities.CopexReturn;
import eu.scy.client.tools.dataProcessTool.print.DataPrint;
import eu.scy.client.tools.dataProcessTool.utilities.DataConstants;
import eu.scy.client.tools.dataProcessTool.utilities.MyUtilities;
import eu.scy.client.tools.fitex.analyseFn.Function;

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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import org.jdom.output.Format;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.output.XMLOutputter;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;


/**
 * controller for standalone app. or scy
 * @author Marjolaine Bodin
 */
public class DataController implements ControllerInterface{
    private String fitexFunctionFileName = "fitexFunctions.xml";
    /* ui */
    private DataProcessToolPanel dataToolPanel;
    /* list of operations types */
    private TypeOperation[] tabTypeOperations;
    /* list of visualizations types */
    private TypeVisualization[] tabTypeVisual;
    /* list of predefined functions*/
    private ArrayList<PreDefinedFunctionCategory> listPreDefinedFunction;
    /* dataset */
    private ArrayList<Dataset> listDataset;
    private ArrayList<Integer> listNoDefaultCol;
    long idDataSet;
    long idOperation;
    long idDataHeader;
    long idData;
    long idVisualization;
    long idFunctionModel;
    long idFunctionParam;
    long idParamOp;
    long idPlot;

    /* mission */
    private Mission mission ;
    /* user */
    private Group group;

    private XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());


    public DataController(DataProcessToolPanel dataToolPanel) {
        this.dataToolPanel = dataToolPanel;
        this.listDataset = new ArrayList();
        this.listNoDefaultCol = new ArrayList();
    }


    /* load data and initialization */
    @Override
    public CopexReturn load(){
        // load: operations types
        tabTypeOperations = new TypeOperation[4];
        int id=0;
        tabTypeOperations[id++] = new TypeOperation(1, 0, "SUM", new Color(246,251,125));
        tabTypeOperations[id++] = new TypeOperation(2, 1, "AVG", new Color(212,251,125));
        tabTypeOperations[id++] = new TypeOperation(3, 2, "MIN", new Color(194,254,221));
        tabTypeOperations[id++] = new TypeOperation(4, 3, "MAX", new Color(206,209,230));

        // visualizations types
        tabTypeVisual = new TypeVisualization[3];
        id=0;
        tabTypeVisual[id++] = new TypeVisualization(1, 0,dataToolPanel.getBundleString("LABEL_GRAPH"), 2 );
        tabTypeVisual[id++] = new TypeVisualization(2, 1,dataToolPanel.getBundleString("LABEL_PIECHART"), 1 );
        tabTypeVisual[id++] = new TypeVisualization(3, 2,dataToolPanel.getBundleString("LABEL_BARCHART"), 1 );
        // predefinde functions
        CopexReturn cr = loadPreDefinedFunction();
        if(cr.isError())
            return cr;
        idDataSet = 1;
        idOperation = 1;
        idDataHeader = 1;
        idData = 1;
        idVisualization = 1;
        idFunctionModel = 1;
        idFunctionParam = 1;
        idParamOp = 1;
        idPlot = 1;
        // mission
        mission = new Mission(1, "DATA", "Data Processing Tool", "");
        group = new Group(1, new LinkedList());
        // blank dataset
        ArrayList v = new ArrayList();
        cr = createTable(dataToolPanel.getBundleString("DEFAULT_DATASET_NAME"),  v);
        if (cr.isError())
            return cr;
        // objects clone
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
        return new CopexReturn();
    }

    /* load predefined functions */
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

    /* locale app. */
    private Locale getLocale(){
        return this.dataToolPanel.getLocale() ;
    }

    
    /* create a default dataset (in v[0]) */
    public CopexReturn createTable(String name, ArrayList v) {
        // default, 10 rows , 2 cols
        int nbRows = 10;
        int nbCol = 2;
        ArrayList<DataOperation> listOp = new ArrayList();
        ArrayList<Visualization> listVis = new ArrayList();
        Data[][] data = new Data[nbRows][nbCol];
        DataHeader[] tabHeader = new DataHeader[nbCol] ;
        for (int i=0; i<nbCol; i++){
            DataHeader h = new DataHeader(idDataHeader++, this.dataToolPanel.getBundleString("DEFAULT_DATAHEADER_NAME")+(i+1), this.dataToolPanel.getBundleString("DEFAULT_DATAHEADER_UNIT"), i,DataConstants.DEFAULT_TYPE_COLUMN, this.dataToolPanel.getBundleString("DEFAULT_DATAHEADER_DESCRIPTION"), null, false, DataConstants.NB_DECIMAL_UNDEFINED, DataConstants.NB_SIGNIFICANT_DIGITS_UNDEFINED, DataConstants.DEFAULT_DATASET_ALIGNMENT);
            tabHeader[i] = h;
        }
        // dataset creation
        Dataset dataset = new Dataset(idDataSet++,mission, -1, name, nbCol, nbRows, tabHeader, data, listOp, listVis, DataConstants.EXECUTIVE_RIGHT);
        listNoDefaultCol.add(nbCol);
        listDataset.add(dataset);
        v.add(dataset.clone());
        return new CopexReturn();

    }


    /* load the specified ELO */
    @Override
    public CopexReturn loadELO(String xmlString, String dsName){
        Dataset ds = null;
        ArrayList v = new ArrayList();
        CopexReturn cr = getElo(xmlString, v);
        if(v.size() > 0)
            ds = (Dataset)v.get(0);
        if(ds != null){
            if(dsName != null)
                ds.setName(dsName);
            this.listDataset.add(ds);
            this.listNoDefaultCol.add(1);
            this.dataToolPanel.setDataset((Dataset)ds.clone(), false);
        }
        return cr;
    }

    /* return true if dataset, else processeddataset*/
    private boolean isDatasetType(String xmlString){
       Element el=  new JDomStringConversion().stringToXml(xmlString);
       if (el.getName().equals(ProcessedDatasetELO.TAG_ELO_CONTENT)){
           return false;
       }
       return true;
    }
    /* returns the dataset from the DataSet ELO */
    private CopexReturn getDataset(DataSet eloDs, String name, ArrayList v){
        Dataset ds = null;
        CopexReturn cr = new CopexReturn();
        // no operatiosn, no visualizations
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
                if (type == null || (!type.equals(DataConstants.TYPE_DOUBLE) && !type.equalsIgnoreCase(DataConstants.TYPE_STRING)))
                    type = DataConstants.DEFAULT_TYPE_COLUMN;
                String value = DataHeader.computeHeaderValue(header.getColumns().get(i).getSymbol(), header.getColumns().get(i).getDescription());
                dataHeader[i] = new DataHeader(-1, value,unit, i, type, header.getColumns().get(i).getDescription(), null, false, DataConstants.NB_DECIMAL_UNDEFINED, DataConstants.NB_SIGNIFICANT_DIGITS_UNDEFINED, DataConstants.DEFAULT_DATASET_ALIGNMENT) ;
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
            // dataset creation
            ds = new Dataset(idDataSet++, mission,-1,  name, nbCols, nbRows,  dataHeader, data, listOperation,listVisualization, DataConstants.EXECUTIVE_RIGHT  );
            v.add(ds);
        }
        
        return cr;
    }

    /* returns the dataset from the PDS ELO */
    private Dataset getPDS(ProcessedDatasetELO eloPDS){
        // no operation, no visualization
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
            if (type == null || (!type.equals(DataConstants.TYPE_DOUBLE) && !type.equalsIgnoreCase(DataConstants.TYPE_STRING)))
                    type = DataConstants.DEFAULT_TYPE_COLUMN;
            dataHeader[i] = new DataHeader(idDataHeader++, header.getColumns().get(i).getSymbol(), unit, i, type, header.getColumns().get(i).getDescription(), null, false, DataConstants.NB_DECIMAL_UNDEFINED, DataConstants.NB_SIGNIFICANT_DIGITS_UNDEFINED, DataConstants.DEFAULT_DATASET_ALIGNMENT) ;
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
                    data[i][j] = new Data(idData++, s, i, j, false);
                }
            }
        }
        // list data ignored
        ProcessedData pds = eloPDS.getProcessedData();
        IgnoredData ig = pds.getIgnoredData();
        List<eu.scy.client.tools.dataProcessTool.pdsELO.Data> listData = ig.getListIgnoredData();
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
        // operations list
        List<eu.scy.client.tools.dataProcessTool.pdsELO.Operation> listOp = pds.getListOperations();
        for (int i=0; i<listOp.size(); i++){
            eu.scy.client.tools.dataProcessTool.pdsELO.Operation op = listOp.get(i);
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
            DataOperation myOp = new DataOperation(idOperation++, op.getName(), getTypeOperation(op.getSymbol()), op.isIsOnCol(), listNo);
            listOperation.add(myOp);
        }
        //visualization list
        List<eu.scy.client.tools.dataProcessTool.pdsELO.Visualization> listVis = pds.getListVisualization();
        for (int i=0; i<listVis.size(); i++){
            eu.scy.client.tools.dataProcessTool.pdsELO.Visualization vis = listVis.get(i);
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
                 myVis = new SimpleVisualization(idVisualization++, vis.getName(), type,h , headerLabel) ;
            } else if (vis instanceof PieVisualization){
                int idHeader = ((PieVisualization)vis).getId();
                DataHeader h = dataHeader[idHeader];
                int idLabelHeader = ((PieVisualization)vis).getIdLabelHeader();
                DataHeader headerLabel = null;
                if(idLabelHeader != -1){
                    headerLabel = dataHeader[idLabelHeader];
                }
                myVis = new SimpleVisualization(idVisualization++, vis.getName(), type, h, headerLabel) ;
            } else if(vis instanceof GraphVisualization){
                GraphVisualization g = ((GraphVisualization)vis);
                List<XYAxis> listAxis = g.getAxis();
                ArrayList<PlotXY> plots = new ArrayList();
                for (Iterator<XYAxis> a = listAxis.iterator();a.hasNext();){
                    XYAxis axis = a.next();
                    plots.add(new PlotXY(idPlot++, dataHeader[axis.getX_axis()], dataHeader[axis.getY_axis()], axis.getPlotColor()));
                }
                ParamGraph paramGraph = new ParamGraph(plots, g.getXMin(), g.getXMax(), g.getYMin(), g.getYMax(), g.getDeltaX(), g.getDeltaY(),  g.isDeltaFixedAutoscale());
                ArrayList<FunctionModel> listFunctionModel  = null;
                List<eu.scy.client.tools.dataProcessTool.pdsELO.FunctionModel> lfm = g.getListFunctionModel();
                if (lfm != null){
                    listFunctionModel = new ArrayList();
                    int n = lfm.size();
                    for(int j=0; j<n; j++){
                        ArrayList<FunctionParam> listParam = new ArrayList();
                        int np = lfm.get(j).getListParam().size();
                        for (int k=0; k<np; k++){
                            listParam.add(new FunctionParam(idFunctionParam++, lfm.get(j).getListParam().get(k).getParam(), lfm.get(j).getListParam().get(k).getValue()));
                        }
                        FunctionModel fm = new FunctionModel(idFunctionModel++, lfm.get(j).getDescription(),lfm.get(j).getType(),  new Color(lfm.get(j).getColorR(),lfm.get(j).getColorG(),lfm.get(j).getColorB() ), listParam, lfm.get(j).getIdPredefFunction());
                        listFunctionModel.add(fm);
                    }
                }
                myVis = new Graph(idVisualization++, vis.getName(), type,  paramGraph, listFunctionModel);
            }
            listVisualization.add(myVis);
        }
        //dataset creation
        Dataset ds = new Dataset(idDataSet++, mission, -1, pds.getName(), nbCols, nbRows,  dataHeader, data, listOperation,listVisualization , DataConstants.EXECUTIVE_RIGHT );
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

  

    /* returns the index of the operation, otherwise -1  */
    private int getIdOperation(Dataset ds, DataOperation operation){
        int nb = ds.getListOperation().size();
        for (int i=0; i<nb; i++){
            if (ds.getListOperation().get(i).getDbKey() == operation.getDbKey())
                return i;
        }
        return -1;
    }
   
    /* returns the index of the dataset, otherwise -1  */
    private int getIdDataset(long dbKey){
        for (int i=0; i<listDataset.size(); i++){
            if(listDataset.get(i).getDbKey() == dbKey)
                return i;
        }
        return -1;
    }

    /*update the ignored statut of the data  */
    @Override
    public CopexReturn setDataIgnored(Dataset ds, boolean isIgnored, ArrayList<Data> listData, ArrayList v){
        int idDs = getIdDataset(ds.getDbKey());
        if(idDs == -1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        Dataset dataset = listDataset.get(idDs);
        // update
        Data[][] datas = dataset.getData() ;
        int nb = listData.size();
        for (int k=0; k<nb; k++){
            Data d = listData.get(k);
            datas[d.getNoRow()][d.getNoCol()].setIsIgnoredData(isIgnored);
        }
        CopexReturn cr = computeAllData(dataset);
        if(cr.isError())
            return cr;
        // operations calculating
        dataset.calculateOperation();
        
        // v[0] clone dataset
        listDataset.set(idDs, dataset);
        v.add(dataset.clone());
        return new CopexReturn();
    }

    /* creation if a new operation - returns in v[0] the new dataset, v[1] DataOperation */
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
        DataOperation operation = new DataOperation(idOperation++, name, typeOp, isOnCol, listNo) ;
        // creation
        dataset.addOperation(operation);
        // v[0]dataset- v[1] dataOperation
        listDataset.set(idDs, dataset);
        v.add(dataset.clone());
        v.add(operation.clone());
        return new CopexReturn();
    }

    
    
    /* gets the operationType, null otherwise*/
    private TypeOperation getTypeOperation(int t){
        for (int i=0; i<tabTypeOperations.length; i++){
            if (tabTypeOperations[i].getType() == t)
                return tabTypeOperations[i] ;
        }
        return null;
    }

    /* gets the operationType, null otherwise */
    private TypeOperation getTypeOperation(String symbol){
        for (int i=0; i<tabTypeOperations.length; i++){
            if (tabTypeOperations[i].getCodeName().equals(symbol))
                return tabTypeOperations[i] ;
        }
        return null;
    }

    /* update the header of a dataset */
    @Override
    public CopexReturn updateDataHeader(Dataset ds, boolean confirm, int colIndex, String title, String unit, String description, String type, String formulaValue, Function function, boolean scientificNotation, int nbShownDecimals, int nbSignificantDigits, ArrayList v){
        int idDs = getIdDataset(ds.getDbKey());
        if(idDs == -1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        Dataset dataset = listDataset.get(idDs);


        // header already exists?
        DataHeader header = dataset.getDataHeader(colIndex);
        boolean wasFormula = false;
        String oldTitle = "";
        if(header == null){
            // no=> creation
            dataset.setDataHeader(new DataHeader(idDataHeader++, title, unit, colIndex, type, description, formulaValue, scientificNotation, nbShownDecimals, nbSignificantDigits, DataConstants.DEFAULT_DATASET_ALIGNMENT), colIndex);
        }else{
            oldTitle = header.getValue();
            // check units in existing graphs
            CopexReturn cr = controlGraphUnit(dataset, colIndex, unit);
            if(cr.isError())
                return cr;
            // check the type
            // if nnew Type is String  => ask confirmation for  deleting operations/visualizations
            if(!confirm){
                cr = controlColumnType(dataset, colIndex, type);
                if(cr.isError() || cr.isWarning())
                    return cr;
            }
            // header exists => modification
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
                // remove operations if type double => string
                // remove or update visualization if type  double => string
                dataset.removeOperationAndVisualizationOn(colIndex);
            }
        }
        // if name changed, control formula of the others columns to replace eventually the parameter names
        int nbCols = dataset.getNbCol();
        for(int j=0; j<nbCols; j++){
            if(dataset.getDataHeader(j) != null && dataset.getDataHeader(j).getFormulaValue() != null){
                String f = dataset.getDataHeader(j).getFormulaValue();
                if(f.contains(oldTitle)){
                    String newF = f.replace(oldTitle,  title);
                    dataset.getDataHeader(j).setFormulaValue(newF);
                }
            }
        }
        // calculating data, if formula
        if((wasFormula && formulaValue == null) ){
            int nbRows = dataset.getNbRows();
            for(int i=0; i<nbRows; i++){
                setDataNull(dataset, i, colIndex);
            }
        }
        // calculating all data
        CopexReturn cr = computeAllData(dataset);
        if(cr.isError())
            return cr;
        // update the visualizations
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
        // v[0] : new dataset
        listDataset.set(idDs, dataset);
        v.add(dataset.clone());
        return new CopexReturn();
    }

    /* check the consistency axes/units when updating units */
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

    /* check if the type of data in a column coulb be consistency with a specified type (and if string, => not remove graphs) */
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

    /*update the title of an operation */
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
        
        // update
        dataset.getListOperation().get(idOp).setName(title);
        // v[0] : dataset
        listDataset.set(idDs, dataset);
        v.add(dataset.clone());
        return new CopexReturn();
    }

    /* update the data */
    @Override
    public CopexReturn updateData(Dataset ds, int rowIndex, int colIndex, String value,  ArrayList v){
        return updateData(ds, rowIndex, colIndex, value, true, v);
    }

    private CopexReturn updateData(Dataset ds, int rowIndex, int colIndex, String value, boolean compute, ArrayList v){
        int idDs = getIdDataset(ds.getDbKey());
        if(idDs == -1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        Dataset dataset = listDataset.get(idDs);
        // Does the data alredy exist?
        Data data = dataset.getData(rowIndex, colIndex);
        if(data == null && value != null){
            // data doesn't exist => creation
            dataset.setData(new Data(idData++,value, rowIndex, colIndex, false), rowIndex, colIndex);
        }else{
            // data exists => update
            if(value == null){
                setDataNull(ds, rowIndex, colIndex);
            }else{
                data.setValue(value);
                dataset.setData(data, rowIndex, colIndex);
            }
        }
        // calculating data defined with a formula
        if(compute){
            CopexReturn cr  = computeDataRow(dataset, rowIndex);
            if(cr.isError())
                return cr;
        }
        // calculating operations
       dataset.calculateOperation();
        // v[0] : new dataset
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
        // Does the data alredy exist?
        Data data = dataset.getData(rowIndex, colIndex);
        if(data != null){
            dataset.setData(null, rowIndex, colIndex);
        }
        listDataset.set(idDs, dataset);
        return new CopexReturn();
    }

    /* close a visualization */
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

    /* remove a visualization */
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
        
        // remove
        dataset.getListVisualization().remove(idVis);
        listDataset.set(idDs, dataset);
        return new CopexReturn();
    }

    /* visualization creation - returns  v[0]new dataset - v[1] Visualization object */
    @Override
    public CopexReturn createVisualization(Dataset ds, Visualization vis, boolean findAxisParam, ArrayList v){
        int idDs = getIdDataset(ds.getDbKey());
        if(idDs == -1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        Dataset dataset = listDataset.get(idDs);
        if(vis instanceof Graph){
            CopexReturn cr = controlAxis(((Graph)vis).getParamGraph());
            if(cr.isError())
                return cr;
        }
        if (vis instanceof Graph && findAxisParam){
           ArrayList v2 = new ArrayList();
           CopexReturn cr = findAxisParam(ds, ((Graph)vis), v2);
           if(cr.isError())
               return cr;
           ParamGraph pg = (ParamGraph)(v2.get(0));
           ((Graph)vis).setParamGraph(pg);
       }
        // update
        vis.setDbKey(idVisualization++);
        dataset.addVisualization(vis);
       
        // v[0] new dataset - v[1] visualization
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
        // searching for 2 identical plots
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

    /* update visualization  name */
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
        // update
        dataset.getListVisualization().get(idVis).setName(newName);
        listDataset.set(idDs, dataset);
        return new CopexReturn();
    }

  
   
    /* remove a dataset */
    @Override
    public CopexReturn deleteDataset(Dataset ds){
        int idDs = getIdDataset(ds.getDbKey());
        if(idDs == -1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        listDataset.remove(idDs);
        listNoDefaultCol.remove(idDs);
        return new CopexReturn();
    }

    /* remove data and/or operations on a dataset */
    @Override
    public CopexReturn deleteData(boolean confirm, Dataset ds, ArrayList<Data> listData,  ArrayList<Integer> listNoDataRow, ArrayList<Integer> listNoDataCol,ArrayList<DataOperation> listOperation,  ArrayList v){
        int idDs = getIdDataset(ds.getDbKey());
        if(idDs == -1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        Dataset dataset = listDataset.get(idDs);
        int nbRowsSel = listNoDataRow.size();
        int nbColsSel = listNoDataCol.size();
        // remove data
        // if all column or all rows are selected => remove all after confirmation
        boolean allData = nbRowsSel == dataset.getNbRows() || nbColsSel == dataset.getNbCol();
        if (allData){
            if (confirm){
                // dataset remove
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
            //delete one part of the data
            // remove operations
            int nbOp = listOperation.size();
            for (int i=0; i<nbOp; i++){
                DataOperation op = listOperation.get(i);
                dataset.removeOperation(op);
            }
            //remove rows
            ArrayList[] tabDel = dataset.removeRows(listNoDataRow);
            // remove columns
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
            // remove data
            dataset.removeData(listData);
            CopexReturn cr = computeAllData(dataset);
            if(cr.isError())
                return cr;
            dataset.calculateOperation();
            listDataset.set(idDs, dataset);
            v.add((Dataset)dataset.clone());
            v.add(tabDel);
        }
        return new CopexReturn();

    }


    @Override
    /* gets the processed dataset ELO*/
    public Element getPDS(Dataset ds){
        // ELO format
        ProcessedDatasetELO pdsELO = ds.toELO(dataToolPanel.getLocale());
        return pdsELO.toXML() ;
    }

    /*  add or update a function model */
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
            // function creation
            FunctionModel myFm = new FunctionModel(idFunctionModel++, description, type, fColor, listParam, idPredefFunction);
            ((Graph)dataset.getListVisualization().get(idVis)).addFunctionModel(myFm);
        }else{
            //update
            if (description == null || description.length() == 0){
                // delete
                ((Graph)dataset.getListVisualization().get(idVis)).deleteFunctionModel(fColor);
            }else{
                // modification
                fm.setType(type);
                fm.setDescription(description);
                fm.setListParam(listParam);
                fm.setIdPredefFunction(idPredefFunction);
            }
        }
        listDataset.set(idDs, dataset);
        v.add(dataset.clone());
        return new CopexReturn();
    }

    /* insert columns or rows */
    @Override
    public CopexReturn  insertData(Dataset ds,  boolean isOnCol, int nb, int idBefore, ArrayList v){
        int idDs = getIdDataset(ds.getDbKey());
        if(idDs == -1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        Dataset dataset = listDataset.get(idDs);
        if (isOnCol){
            dataset.insertCol(nb, idBefore);
            for(int j=0; j<nb; j++){
                ArrayList v2 = new ArrayList();
                int no = listNoDefaultCol.get(idDs);
                CopexReturn cr= updateDataHeader(ds, true, idBefore+j, this.dataToolPanel.getBundleString("DEFAULT_DATAHEADER_NAME")+(no+1), this.dataToolPanel.getBundleString("DEFAULT_DATAHEADER_UNIT"), this.dataToolPanel.getBundleString("DEFAULT_DATAHEADER_DESCRIPTION"),DataConstants.DEFAULT_TYPE_COLUMN, null,null, false, DataConstants.NB_DECIMAL_UNDEFINED, DataConstants.NB_SIGNIFICANT_DIGITS_UNDEFINED, v2);
                if(cr.isError())
                    return cr;
                listNoDefaultCol.set(idDs, no+1);
            }
        }
        else
            dataset.insertRow(nb, idBefore);
        v.add(dataset.clone());
        listDataset.set(idDs, dataset);
        return new CopexReturn();
    }

    /* print as pdf */
    @Override
    public CopexReturn printDataset(Dataset dataset, boolean printDataset, DataTableModel model, ArrayList<Visualization> listVis, ArrayList<Object> listGraph){
        String fileName = "copex-"+mission.getCode();
        DataPrint pdfPrint = new DataPrint(dataToolPanel, mission, group,dataset, model, printDataset, listVis, listGraph,fileName );
        CopexReturn cr = pdfPrint.printDocument();
        return cr;
    }

   
    /* update dataset after a sort */
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

    /* creation if a dataset with headers and one row of data */
    @Override
    public CopexReturn createDataset(String name, String[] headers, String[] units, String[] types, String[] descriptions, ArrayList v){
        int nbCol = headers.length;
        int nbRows = 1;
        ArrayList<DataOperation> listOp = new ArrayList();
        ArrayList<Visualization> listVis = new ArrayList();
        Data[][] data = new Data[nbRows][nbCol];
        DataHeader[] tabHeader = new DataHeader[nbCol] ;
        for (int i=0; i<nbCol; i++){
            tabHeader[i] = new DataHeader(idDataHeader++, headers[i], units[i], i, types[i], descriptions[i], null, false, DataConstants.NB_DECIMAL_UNDEFINED, DataConstants.NB_SIGNIFICANT_DIGITS_UNDEFINED, DataConstants.DEFAULT_DATASET_ALIGNMENT);
        }
        // dataset creation
        Dataset ds = new Dataset(idDataSet++,mission, -1,  name, nbCol, nbRows, tabHeader,  data, listOp, listVis, DataConstants.EXECUTIVE_RIGHT);
        listDataset.add(ds);
        listNoDefaultCol.add(1);
        v.add(ds.clone());
        return new CopexReturn();
    }

    
    /* add a data row -simulator */
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
        // insertion at the last posistion
        int idBefore = dataset.getNbRows();
        CopexReturn cr = insertData(dataset, false, 1, idBefore, v2) ;
        if(cr.isError())
            return cr;
        // value creation
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
        // if needed, we reajust the graph axes
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

    /* update the parameters of a graph */
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
            ((Graph)vis).setParamGraph(paramGraph);
        }
        listDataset.set(idDs, dataset);
        v.add(vis.clone());
        return new CopexReturn();
    }

    /* search for axis - autoscale mode*/
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
        v.add(pg);
        return new CopexReturn();
    }

    /* autoscale graph update*/
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

    @Override
    public CopexReturn mergeDataset(Dataset currentDs, Mission m, Dataset dsToMerge, ArrayList v){
        return new CopexReturn();
    }
    
   /* merge an elo with the current dataset*/
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
            // if ds contains only 1  row &  &  types fits to dataset & at least 1 numerical column & nb of columns fits to dataset => ask the user
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

            //if(confirm && oneRow && nbCols1 == nbCols2 && dataset.isAllColumnDouble() && ds.isAllColumnDouble()){
            if(confirm && oneRow && nbCols1 == nbCols2 && dataset.isFitTypeColumn(ds) && dataset.hasAtLeastADoubleColumn()){
                dataToolPanel.askForMergeType(dataset, ds,elo,  true);
                return new CopexReturn();
            }
            if(confirm && nbCols1 == nbCols2 && dataset.isFitTypeColumn(ds)){
                dataToolPanel.askForMergeType(dataset, ds,elo,  false);
                return new CopexReturn();
            }
            if(confirm){
                CopexReturn ret = new CopexReturn(dataToolPanel.getBundleString("MSG_CONFIRM_MERGE_DATASET"), true);
                ret.setConfirm(dataToolPanel.getBundleString("MSG_CONFIRM_MERGE_DATASET"));
                return ret;
            }
            if(nbRows2 > nbRows1){
                //add rows
                dataset.insertRow(nbRows2-nbRows1, nbRows1);
            }
            dataset.insertCol(nbCols2, nbCols1);
            for (int j=0; j<nbCols2; j++){
                if (ds.getDataHeader(j) != null){
                    dataset.setDataHeader(new DataHeader(idDataHeader++,ds.getDataHeader(j).getValue() , ds.getDataHeader(j).getUnit(), j+nbCols1, ds.getDataHeader(j).getType(), ds.getDataHeader(j).getDescription(),ds.getDataHeader(j).getFormulaValue(), ds.getDataHeader(j).isScientificNotation(), ds.getDataHeader(j).getNbShownDecimals(), ds.getDataHeader(j).getNbSignificantDigits(), ds.getDataHeader(j).getDataAlignment()), j+nbCols1);
                }
                for (int i=0; i<nbRows2; i++){
                    Data nd = null;
                    if (ds.getData(i, j) != null){
                        nd = new Data(idData++, ds.getData(i, j).getValue(), i,j+nbCols1, ds.getData(i, j).isIgnoredData() );
                    }
                    dataset.setData(nd, i, j+nbCols1);
                }
            }
            // merge operations
            ArrayList<DataOperation> listOp = ds.getListOperation();
            int nbOp = listOp.size();
            for (int i=0; i<nbOp; i++){
                DataOperation op = listOp.get(i);
                ArrayList<Integer> listNo = new ArrayList();
                int n= op.getListNo().size();
                for (int j=0; j<n; j++){
                    listNo.add(op.getListNo().get(j)+nbCols1);
                }
                dataset.addOperation(new DataOperation(idOperation++, op.getName(), op.getTypeOperation(), op.isOnCol(), listNo));
            }
            // merge visualizations
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
                    SimpleVisualization sv = new SimpleVisualization(idVisualization++, vis.getName(), vis.getType(),h, headerLabel);
                    dataset.addVisualization(sv);
                }else if(vis instanceof Graph){
                    ArrayList<PlotXY> listPlots = new ArrayList();
                    for (Iterator<PlotXY> p = ((Graph)vis).getParamGraph().getPlots().iterator();p.hasNext();){
                        PlotXY plot = p.next();
                        listPlots.add(new PlotXY(idPlot++,dataset.getDataHeader(plot.getHeaderX().getNoCol()+nbCols1), dataset.getDataHeader(plot.getHeaderY().getNoCol()+nbCols1), plot.getPlotColor()));
                    }
                    ParamGraph pg = new ParamGraph(listPlots, ((Graph)vis).getParamGraph().getX_min(),((Graph)vis).getParamGraph().getX_max(), ((Graph)vis).getParamGraph().getX_min(), ((Graph)vis).getParamGraph().getY_max(), ((Graph)vis).getParamGraph().getDeltaX(), ((Graph)vis).getParamGraph().getDeltaY(),  ((Graph)vis).getParamGraph().isDeltaFixedAutoscale());
                    Graph g = new Graph(idVisualization++, vis.getName(), vis.getType(),pg,((Graph)vis).getListFunctionModel());
                    dataset.addVisualization(g);
                }
            }
            //update ui
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
        // add as new rows
        for(int i=0; i<nbRows2; i++){
           for(int j=0; j<nbCols2; j++){
               dataset1.setData(ds2.getData(i, j), i+nbRows1, j);
           }
        }
        CopexReturn cr = computeAllData(dataset1);
        if(cr.isError())
            return cr;
        // calculating operations
        dataset1.calculateOperation();
        //update ui
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
//            if(dataset1.getDataHeader(j) != null && !dataset1.getDataHeader(j).isDouble()){
//                return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
//            }
        }
        int idR = ds2.getFirstRowData();
        if(idR == -1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        for(int i=0; i<nbRows1; i++){
            for (int j=0; j<nbCols1; j++){
                if(dataset1.getDataHeader(j) != null  && !dataset1.getDataHeader(j).isDouble()){
                }else{
                    if(dataset1.getData(i, j) == null){
                        dataset1.setData(new Data(idData++, ds2.getData(idR, j) == null ? "" :ds2.getData(idR, j).getValue() , i, j, false), i, j);
                    }else{
                        dataset1.getData(i, j).addToValue(ds2.getData(idR, j) == null ? 0 :ds2.getData(idR, j).getDoubleValue());
                    }
                }
            }
        }
        CopexReturn cr = computeAllData(dataset1);
        if(cr.isError())
            return cr;
        // calculating operations
        dataset1.calculateOperation();
        //update ui
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
        // control types
        for (int j=0; j<nbCols1; j++){
            if(dataset1.getDataHeader(j) != null && ds2.getDataHeader(j) != null && !dataset1.getDataHeader(j).getType().equals(ds2.getDataHeader(j).getType())){
                return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
            }
//            if(dataset1.getDataHeader(j) != null && !dataset1.getDataHeader(j).isDouble()){
//                return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
//            }
        }
         int idR = ds2.getFirstRowData();
        if(idR == -1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        for(int i=0; i<nbRows1; i++){
            for (int j=0; j<nbCols1; j++){
                if(dataset1.getDataHeader(j) != null  && !dataset1.getDataHeader(j).isDouble()){
                }else{
                    if(dataset1.getData(i, j) == null){
                        dataset1.setData(new Data(idData++, ds2.getData(idR, j) == null ? "" :"0" , i, j, false), i, j);
                    }else{
                        dataset1.getData(i, j).multiplyToValue(ds2.getData(idR, j) == null ? 0 :ds2.getData(idR, j).getDoubleValue());
                    }
                }
            }
        }
        CopexReturn cr = computeAllData(dataset1);
        if(cr.isError())
            return cr;
        // calculating operations
        dataset1.calculateOperation();
        //update ui
        this.dataToolPanel.setDataset((Dataset)dataset1.clone(), true);
        return new CopexReturn();
    }

    /* gets the dataset corresponding to the elo(xml format) */
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

    /* copy/paste */
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
            // insert new rows
            dataset.insertRow((idR+nbRowsToPaste) - nbR , nbR);
            for (int i=0; i<(idR+nbRowsToPaste) - nbR; i++){
                listNoRow.add(nbR+i);
            }
        }
        if(idC+nbColsToPaste > nbC){
            // insert new columns
            dataset.insertCol((idC+nbColsToPaste) - nbC, nbC);
            for (int i=0; i<(idC+nbColsToPaste) - nbC; i++){
                listNoCol.add(nbC+i);
            }
        }
        if(pasteHeader){
           for (int j=0; j<nbColsToPaste; j++){
               DataHeader header = null;
               if (copyDs.getListHeader().get(j) != null){
                   header = new DataHeader(idDataHeader++, copyDs.getListHeader().get(j).getValue(),copyDs.getListHeader().get(j).getUnit(),  idC+j, copyDs.getListHeader().get(j).getType(), copyDs.getListHeader().get(j).getDescription(), copyDs.getListHeader().get(j).getFormulaValue(), copyDs.getListHeader().get(j).isScientificNotation(), copyDs.getListHeader().get(j).getNbShownDecimals(), copyDs.getListHeader().get(j).getNbSignificantDigits(), copyDs.getListHeader().get(j).getDataAlignment());
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
                        CopexReturn cr = new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_PASTE_COHERENCE"), false);
                        dataset = cloneDs;
                        return cr;
                    }
                    nd = new Data(idData++, d.getValue(), idR, idC, d.isIgnoredData()) ;
                    Data[] datas = new Data[2];
                   datas[0] = null;
                   if(idC< oldDs.getNbCol() && idR <oldDs.getNbRows()){
                       datas[0] = oldDs.getData(idR, idC);
                   }
                   datas[1] = nd;
                   listData.add(datas);
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
                        nd = new Data(idData++, d.getValue(), idR+i-i0, idC+j-j0, d.isIgnoredData()) ;
                        Data[] datas = new Data[2];
                        datas[0] = null;
                        if(idC+j-j0< oldDs.getNbCol() && idR+i-i0 <oldDs.getNbRows()){
                            datas[0] = oldDs.getData(idR+i-i0, idC+j-j0);
                        }
                        datas[1] = nd;
                        listData.add(datas);
                    }
                    if(idR+i-i0 >= dataset.getNbRows()){
                        // insert new rows
                        dataset.insertRow(1 , dataset.getNbRows());
                        listNoRow.add(dataset.getNbRows());
                    }
                    if(idC+j-j0 >= dataset.getNbCol()){
                        dataset.insertCol(1, dataset.getNbCol());
                        listNoCol.add(dataset.getNbCol());
                        DataHeader header = new DataHeader(idDataHeader++, this.dataToolPanel.getBundleString("DEFAULT_DATAHEADER_NAME")+(idC+j-j0+1), this.dataToolPanel.getBundleString("DEFAULT_DATAHEADER_UNIT"), idC+j-j0,DataConstants.DEFAULT_TYPE_COLUMN, this.dataToolPanel.getBundleString("DEFAULT_DATAHEADER_DESCRIPTION"), null, false, DataConstants.NB_DECIMAL_UNDEFINED, DataConstants.NB_SIGNIFICANT_DIGITS_UNDEFINED, DataConstants.DEFAULT_DATASET_ALIGNMENT);
                        DataHeader[] headers = new DataHeader[2];
                        headers[0] = null;
                        headers[1] = header;
                        listDataHeader.add(headers);
                        dataset.setDataHeader(header, idC+j);
                    }
                    dataset.setData(nd, idR+i-i0, idC+j-j0);
                }
            }
        }
        CopexReturn cr = computeAllData(dataset);
        if(cr.isError())
            return cr;
        // calculating operations
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

    /* read a cvs file => elo dataset  */
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
            int nbC = 0;
            List<DataSetColumn> listC = new LinkedList<DataSetColumn>();
            eu.scy.elo.contenttype.dataset.DataSet ds = new eu.scy.elo.contenttype.dataset.DataSet();
            String line = null;
            String value = null;
            try{
                int id = 0;
                while ((line = reader.readLine()) != null) {
                    String[] tabValues = line.split(sepField);
                    //lineParser = new StringTokenizer(line, sepField);
                    int j=0;
                    DataSetRow datasetRow;
                    List<String> values = new LinkedList<String>();
                    String val = null;
                    value = "";
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

    
    /*creation of a default datast */
    @Override
    public CopexReturn createDefaultDataset(String name, ArrayList v){
        return createTable(name, v);
    }

    /* update dataset name */
    @Override
    public CopexReturn renameDataset(Dataset ds, String name){
        int idDs = getIdDataset(ds.getDbKey());
        if(idDs == -1){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
        }
        Dataset dataset = listDataset.get(idDs);
        dataset.setName(name);
        return new CopexReturn();
    }

    /* close dataset */
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

    /*  update an operation */
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

    /* returns the list of mission & list of dataset perm mission  */
    @Override
    public CopexReturn getListDatasetToOpenOrMerge(ArrayList v){
        ArrayList<Mission> listMission = new ArrayList();
        ArrayList<ArrayList<Dataset>> listDatasetMission = new ArrayList();
        v.add(listMission);
        v.add(listDatasetMission);
        return new CopexReturn();
    }

    /* open dataset*/
    @Override
    public CopexReturn openDataset(Mission mission, Dataset ds){
        return new CopexReturn();
    }

    /* quit fitex */
    @Override
    public CopexReturn stopFitex(){
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
        v.add(false);
        return new CopexReturn();
    }

    /** import a GMBL file, return v[0] the dataset elo */
     @Override
    public CopexReturn importGMBLFile(File file,ArrayList v){
        if (file == null) {
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_FILE_EXIST"), false);
        }
        if(!MyUtilities.isGMBLFile(file)){
            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_FILE_GMBL"), false);
        }
        InputStreamReader fileReader = null;
        SAXBuilder builder = new SAXBuilder(false);
        try{
            fileReader = new InputStreamReader(new FileInputStream(file), "utf-8");
            Document doc = builder.build(fileReader, file.getAbsolutePath());
            Element element = doc.getRootElement();
            GmblDocument gmblDocument = new GmblDocument(element);
            List<DataSetHeader> headers = new LinkedList<DataSetHeader>();
            DataSetHeader header;
            List<DataSetColumn> listC = new LinkedList<DataSetColumn>();
            int nbBMaxRow = 0;
            for(Iterator<GmblDataset> gmds = gmblDocument.getColumns().iterator(); gmds.hasNext();){
                GmblDataset gmblDataset = gmds.next();
                for(Iterator<GmblColumn> col = gmblDataset.getColumns().iterator(); col.hasNext();){
                    GmblColumn gmblColumn = col.next();
                    DataSetColumn c = new DataSetColumn(gmblColumn.getColumnTitle(), "", gmblColumn.getType(), gmblColumn.getUnit());
                    listC.add(c);
                    nbBMaxRow = Math.max(nbBMaxRow, gmblColumn.getCellValues().size());
                }
                header = new DataSetHeader(listC, dataToolPanel.getLocale());
                headers.add(header);
            }
            eu.scy.elo.contenttype.dataset.DataSet ds = new eu.scy.elo.contenttype.dataset.DataSet(headers);
            for(int i=0; i<nbBMaxRow; i++){
                List<String> values = new LinkedList<String>();
                for(Iterator<GmblDataset> gmds = gmblDocument.getColumns().iterator(); gmds.hasNext();){
                    GmblDataset gmblDataset = gmds.next();
                    for(Iterator<GmblColumn> col = gmblDataset.getColumns().iterator(); col.hasNext();){
                        GmblColumn gmblColumn = col.next();
                        List<String> cellsValues = gmblColumn.getCellValues();
                        if(i<cellsValues.size() && cellsValues.get(i) != null){
                            values.add(cellsValues.get(i));
                        }else{
                            values.add("");
                        }
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
        return new CopexReturn();
    }

    /** export a html version */
    @Override
    public CopexReturn exportHTML(){
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
        // update
        for(Iterator<DataHeader> h = listHeader.iterator();h.hasNext();){
            int noCol = h.next().getNoCol();
            if(dataset.getDataHeader(noCol) != null)
                dataset.getDataHeader(noCol).setDataAlignment(align);
        }
        // v[0] new dataset
        listDataset.set(idDs, dataset);
        v.add(dataset.clone());
        return new CopexReturn();
    }
}