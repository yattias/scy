/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import eu.scy.elo.contenttype.dataset.DataSet;
import eu.scy.elo.contenttype.dataset.DataSetColumn;
import eu.scy.elo.contenttype.dataset.DataSetHeader;
import eu.scy.elo.contenttype.dataset.DataSetRow;
import eu.scy.tools.dataProcessTool.utilities.CopexReturn;

import eu.scy.tools.dataProcessTool.common.*;
import eu.scy.tools.dataProcessTool.dataTool.DataProcessToolPanel;
import eu.scy.tools.dataProcessTool.dataTool.DataTableModel;
import eu.scy.tools.dataProcessTool.dnd.SubData;
import eu.scy.tools.dataProcessTool.pdsELO.BarVisualization;
import eu.scy.tools.dataProcessTool.pdsELO.GraphVisualization;
import eu.scy.tools.dataProcessTool.pdsELO.IgnoredData;
import eu.scy.tools.dataProcessTool.pdsELO.PieVisualization;
import eu.scy.tools.dataProcessTool.pdsELO.ProcessedData;
import java.util.List;
import java.util.Locale;
import eu.scy.tools.dataProcessTool.pdsELO.ProcessedDatasetELO;
import eu.scy.tools.dataProcessTool.pdsELO.XYAxis;
import eu.scy.tools.dataProcessTool.print.DataPrint;
import eu.scy.tools.dataProcessTool.utilities.DataConstants;
import eu.scy.tools.dataProcessTool.utilities.MyConstants;
import eu.scy.tools.dataProcessTool.utilities.MyUtilities;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.Vector;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.Element;
import org.jdom.JDOMException;
import roolo.elo.JDomStringConversion;


/**
 * controller de l'applet
 * @author Marjolaine Bodin
 */
public class DataController implements ControllerInterface{

    // PROPERTY 
    /* interface */
    private DataProcessToolPanel dataToolPanel;
    /* liste des types d'operations possibles */
    private TypeOperation[] tabTypeOperations;
    /* liste des visualisations possibles */
    private TypeVisualization[] tabTypeVisual;
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

    /* mission */
    private Mission mission ;
    /* utilisateur */
    private ToolUser toolUser;

   
    // CONSTRUCTOR
    public DataController(DataProcessToolPanel dataToolPanel) {
        this.dataToolPanel = dataToolPanel;
        this.listDataset = new ArrayList();
        this.listNoDefaultCol = new ArrayList();
    }


    // METHODE
    /* chargement et initialisation des donnees */
    @Override
    public CopexReturn load(){
        // chargement des types d'operation
        tabTypeOperations = new TypeOperation[5];
        int id=0;
        tabTypeOperations[id++] = new TypeOperation(1, 0, "SUM", new Color(246,251,125));
        tabTypeOperations[id++] = new TypeOperation(2, 1, "AVG", new Color(212,251,125));
        tabTypeOperations[id++] = new TypeOperation(3, 2, "MIN", new Color(194,254,221));
        tabTypeOperations[id++] = new TypeOperation(4, 3, "MAX", new Color(206,209,230));
        tabTypeOperations[id++] = new TypeOperationParam(5, 4, "SUM IF", new Color(255,153,153),2,getLibelleCountIf());

        // liste des visualisations possibles
        tabTypeVisual = new TypeVisualization[3];
        id=0;
        tabTypeVisual[id++] = new TypeVisualization(1, 0,dataToolPanel.getBundleString("LABEL_GRAPH"), 2 );
        tabTypeVisual[id++] = new TypeVisualization(2, 1,dataToolPanel.getBundleString("LABEL_PIECHART"), 1 );
        tabTypeVisual[id++] = new TypeVisualization(3, 2,dataToolPanel.getBundleString("LABEL_BARCHART"), 1 );

        idDataSet = 1;
        idOperation = 1;
        idDataHeader = 1;
        idData = 1;
        idVisualization = 1;
        idFunctionModel = 1;
        idFunctionParam = 1;
        idParamOp = 1;
        // mission
        mission = new Mission(1, "DATA", "Data Processing Tool", "");
        toolUser = new ToolUser("userName", "userFirstName");
        // creation dataset vierge
        ArrayList v = new ArrayList();
        CopexReturn cr = createTable(dataToolPanel.getBundleString("DEFAULT_DATASET_NAME"),  v);
        if (cr.isError())
            return cr;
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


    /* locale de l'applet */
    private Locale getLocale(){
        return this.dataToolPanel.getLocale() ;
    }

    /* retourne le libelle pour l'operation sumif*/
    private String getLibelleCountIf(){
        return this.dataToolPanel.getBundleString("LIBELLE_SUM_IF");
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
            DataHeader h = new DataHeader(idDataHeader++, this.dataToolPanel.getBundleString("DEFAULT_DATAHEADER_NAME")+" "+(i+1), this.dataToolPanel.getBundleString("DEFAULT_DATAHEADER_UNIT"), i,MyConstants.DEFAULT_TYPE_COLUMN, this.dataToolPanel.getBundleString("DEFAULT_DATAHEADER_DESCRIPTION"));
            tabHeader[i] = h;
        }
        // creation ds
        Dataset dataset = new Dataset(idDataSet++, name, nbCol, nbRows, tabHeader, data, listOp, listVis);
        listNoDefaultCol.add(nbCol);
        listDataset.add(dataset);
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
        int nbCols = header.getColumnCount() ;;
        List<DataSetRow> listRows = eloDs.getValues() ;
        int nbRows = listRows.size() ;
        
        if(nbCols > 0){
            // header
            DataHeader[] dataHeader = new DataHeader[nbCols];
            for (int i=0; i<nbCols; i++){
                String unit = header.getColumns().get(i).getUnit();
                String type = header.getColumns().get(i).getType();
                if (type == null || !type.equals(MyConstants.TYPE_DOUBLE) || !type.equals(MyConstants.TYPE_STRING))
                    type = MyConstants.DEFAULT_TYPE_COLUMN;
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
            ds = new Dataset(idDataSet++, name, nbCols, nbRows,  dataHeader, data, listOperation,listVisualization  );
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
            if (type == null || !type.equals(MyConstants.TYPE_DOUBLE) || !type.equals(MyConstants.TYPE_STRING))
                    type = MyConstants.DEFAULT_TYPE_COLUMN;
            dataHeader[i] = new DataHeader(idDataHeader++, header.getColumns().get(i).getSymbol(), unit, i, type, header.getColumns().get(i).getDescription()) ;
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
            DataOperation myOp = new DataOperation(idOperation++, op.getName(), getTypeOperation(op.getSymbol()), op.isIsOnCol(), listNo);
            listOperation.add(myOp);
        }
        // liste des visualization
        List<eu.scy.tools.dataProcessTool.pdsELO.Visualization> listVis = pds.getListVisualization();
        for (int i=0; i<listVis.size(); i++){
            eu.scy.tools.dataProcessTool.pdsELO.Visualization vis = listVis.get(i);
            TypeVisualization type = getTypeVisualization(vis.getType());
            Visualization myVis = null;
            if (vis instanceof BarVisualization){
                int idLabelHeader = ((BarVisualization)vis).getIdLabelHeader();
                DataHeader headerLabel = null;
                if(idLabelHeader != -1){
                    headerLabel = dataHeader[idLabelHeader];
                }
                 myVis = new SimpleVisualization(idVisualization++, vis.getName(), type,((BarVisualization)vis).getId() , headerLabel) ;
            } else if (vis instanceof PieVisualization){
                int idLabelHeader = ((PieVisualization)vis).getIdLabelHeader();
                DataHeader headerLabel = null;
                if(idLabelHeader != -1){
                    headerLabel = dataHeader[idLabelHeader];
                }
                myVis = new SimpleVisualization(idVisualization++, vis.getName(), type, ((PieVisualization)vis).getId(), headerLabel) ;
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
                            listParam.add(new FunctionParam(idFunctionParam++, lfm.get(j).getListParam().get(k).getParam(), lfm.get(j).getListParam().get(k).getValue()));
                        }
                        FunctionModel fm = new FunctionModel(idFunctionModel++, lfm.get(j).getDescription(), new Color(lfm.get(j).getColorR(),lfm.get(j).getColorG(),lfm.get(j).getColorB() ), listParam);
                        listFunctionModel.add(fm);
                    }
                }
                myVis = new Graph(idVisualization++, vis.getName(), type,  paramGraph, listFunctionModel);
            }
            listVisualization.add(myVis);
        }
        // creation du dataset
        Dataset ds = new Dataset(idDataSet++, pds.getName(), nbCols, nbRows,  dataHeader, data, listOperation,listVisualization  );
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
        DataOperation operation = new DataOperation(idOperation++, name, typeOp, isOnCol, listNo) ;
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
        DataOperationParam operation = new DataOperationParam(idOperation++, name, typeOp, isOnCol, listNo, allParam) ;
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
            ParamOperation p = new ParamOperation(idParamOp++, value, i);
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
            dataset.setDataHeader(new DataHeader(idDataHeader++, title, unit, colIndex, type, description), colIndex);
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
            // header existe => on le modifie
            header.setValue(title);
            header.setUnit(unit);
            header.setDescription(description);
            header.setType(type);
            dataset.setDataHeader(header, header.getNoCol());
            if(type.equals(MyConstants.TYPE_STRING)){
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
        if(type.equals(MyConstants.TYPE_DOUBLE)){
            for (int i=0; i<nbRows; i++){
                if(ds.getData(i, colIndex) != null && !ds.getData(i, colIndex).isDoubleValue()){
                    return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_UPDATE_HEADER_TYPE_DOUBLE"), false);
                }
            }
        }
        if(type.equals(MyConstants.TYPE_STRING)){
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
            dataset.setData(new Data(idData++,value, rowIndex, colIndex, false), rowIndex, colIndex);
        }else{
            // data existe => on le modifie
            if(value == null){
                setDataNull(ds, rowIndex, colIndex);
            }else{
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
        // memoire
        vis.setDbKey(idVisualization++);
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
            // suprresion des données
            dataset.removeData(listData);
            dataset.calculateOperation();
            //System.out.println("dataset apres remove : "+dataset.toString());
            listDataset.set(idDs, dataset);
            v.add((Dataset)dataset.clone());
            v.add(tabDel);
        }
        return new CopexReturn();

    }

    /* suppression de donnees et/ou operation sur un dataset */
//    @Override
//    public CopexReturn deleteData(boolean confirm, Dataset ds, ArrayList<Data> listData, ArrayList<DataOperation> listOperation, ArrayList<Integer>[] listRowAndCol, ArrayList v){
//        ArrayList<Visualization> listVisualizationToDel = new ArrayList();
//        int idDs = getIdDataset(ds.getDbKey());
//        if(idDs == -1){
//            return new CopexReturn(dataToolPanel.getBundleString("MSG_ERROR_DATASET"), false);
//        }
//        Dataset dataset = listDataset.get(idDs);
//        // suppression des operations
//        int nbOp = listOperation.size();
//        for (int i=0; i<nbOp; i++){
//            DataOperation op = listOperation.get(i);
//            dataset.removeOperation(op);
//        }
//        int nbRowsSel = listRowAndCol[0].size();
//        int nbColsSel = listRowAndCol[1].size();
//        // suppression de donnees :
//        // si toutes les colonnes ou toutes les lignes sont sel : on supprime tout le dataset apres confirmation
//        boolean allData = nbRowsSel == dataset.getNbRows() || nbColsSel == dataset.getNbCol();
//        if (allData){
//            if (confirm){
//                // suppression du dataset
//                CopexReturn cr = deleteDataset(dataset);
//                if (cr.isError())
//                    return cr;
//                //dataToolPanel.removeDataset(ds);
//            }else{
//                CopexReturn cr = new CopexReturn(dataToolPanel.getBundleString("MSG_CONFIRM_DELETE_DATASET"), true);
//                cr.setConfirm(dataToolPanel.getBundleString("MSG_CONFIRM_DELETE_DATASET"), true);
//                return cr;
//            }
//        }else{
//            //suppression d'une partie des donnees
//            // si il y a des colonnes / lignes entieres => on supprime eventuellement les operations sur ces colonnes ou les visualization sur ces colonnes
//            //ArrayList<DataHeader> listDataHeader = new ArrayList();
//            ArrayList<Integer> listNoHeader = new ArrayList();
//            ArrayList listNoToDel = new ArrayList();
//            ArrayList<DataOperation> listOperationToDel = new ArrayList();
//            if(nbColsSel > 0){
//                // suppression header
//                for (int i=0; i<nbColsSel; i++){
//                    //listDataHeader.add(myDs.getDataHeader(listRowAndCol[1].get(i)));
//                    int id = listNoHeader.size();
//                    for(int j=0; j<listNoHeader.size(); j++){
//                        if(listNoHeader.get(j)> listRowAndCol[1].get(i)){
//                            id = j;
//                            break;
//                        }
//                    }
//                    listNoHeader.add(id,listRowAndCol[1].get(i)) ;
//                }
//            }
//            if (nbRowsSel > 0 || nbColsSel > 0){
//                // on supprime les operations liees a ces colonnes ou ces lignes
//                // puis on supprime les operations qui ne portent plus sur aucune colonne ou ligne
//                ArrayList<DataOperation> myListOp = dataset.getListOperation();
//                int nbTotOp = myListOp.size();
//                ArrayList<DataOperation> copyListOp = new ArrayList();
//                for (int i=0; i<nbTotOp; i++){
//                    copyListOp.add((DataOperation)myListOp.get(i).clone());
//                }
//                for (int i=0; i<nbRowsSel; i++){
//                    for (int j=0; j<nbTotOp; j++){
//                        if (!myListOp.get(j).isOnCol() && myListOp.get(j).getListNo().contains(listRowAndCol[0].get(i))){
//                            ArrayList toDel = new ArrayList();
//                            toDel.add(myListOp.get(j).getDbKey());
//                            toDel.add(listRowAndCol[0].get(i));
//                            listNoToDel.add(toDel);
//                            copyListOp.get(j).getListNo().remove(listRowAndCol[0].get(i));
//                        }
//                    }
//                }
//                for (int i=0; i<nbColsSel; i++){
//                    for (int j=0; j<nbTotOp; j++){
//                        if (myListOp.get(j).isOnCol() && myListOp.get(j).getListNo().contains(listRowAndCol[1].get(i))){
//                            ArrayList toDel = new ArrayList();
//                            toDel.add(myListOp.get(j).getDbKey());
//                            toDel.add(listRowAndCol[1].get(i));
//                            listNoToDel.add(toDel);
//                            copyListOp.get(j).getListNo().remove(listRowAndCol[1].get(i));
//                        }
//                    }
//                }
//
//                // si des operations ne portent plus sur rien on les supprime
//                for (int i=0; i<nbTotOp; i++){
//                    if (copyListOp.get(i).getListNo().size() == 0){
//                        listOperationToDel.add(copyListOp.get(i));
//                    }
//                }
//                // visualization : si porte sur la ligne ou la colonne => suppression
//                ArrayList<Visualization> myListVis = dataset.getListVisualization();
//                int nbTotVis = myListVis.size();
//                for (int i=0; i<nbColsSel; i++){
//                    for (int j=0; j<nbTotVis; j++){
//                        if (myListVis.get(j).isOnNo(listRowAndCol[1].get(i))){
//                            listVisualizationToDel.add(myListVis.get(j));
//                        }
//                    }
//                }
//
//            }
//            // suppression des donnees
//            // mise a jour en memoire et appel de l'applet
//            //remove listDataHeader
//           /*for (int i=0; i<listDataHeader.size(); i++){
//                myDs.removeHeader(listDataHeader.get(i));
//            }*/
//            int nbH = listNoHeader.size();
//            for (int i=nbH-1; i>=0; i--){
//                dataset.removeHeader(listNoHeader.get(i));
//            }
//            //remove listNoToDel
//            for (int i=0; i<listNoToDel.size(); i++){
//                long dbKeyOp = (Long)((ArrayList)listNoToDel.get(i)).get(0);
//                int no = (Integer)((ArrayList)listNoToDel.get(i)).get(1);
//                dataset.removeNoOperation(dbKeyOp, no);
//            }
//            //remove listOperationToDel
//            for(int i=0; i<listOperationToDel.size(); i++){
//                dataset.removeOperation(listOperationToDel.get(i));
//            }
//             //remove listVisualizationToDel
////            for(int i=0; i<listVisualizationToDel.size(); i++){
////                dataset.removeVisualization(listVisualizationToDel.get(i));
////            }
//            //remove data
//            dataset.removeData(listRowAndCol);
//            dataset.calculateOperation();
//            // appel applet
//            listDataset.set(idDs, dataset);
//            v.add((Dataset)dataset.clone());
//            v.add(listVisualizationToDel);
//        }
//        return new CopexReturn();
//
//    }

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
            // memoire
            FunctionModel myFm = new FunctionModel(idFunctionModel++, description, fColor, listParam);
            ((Graph)dataset.getListVisualization().get(idVis)).addFunctionModel(myFm);
        }else{
            //mise a jour
            if (description == null || description.length() == 0){
                // suppression
                ((Graph)dataset.getListVisualization().get(idVis)).deleteFunctionModel(fColor);
            }else{
                // modification
                fm.setDescription(description);
                fm.setListParam(listParam);
            }
        }
        listDataset.set(idDs, dataset);
        v.add(dataset.clone());
        return new CopexReturn();
    }

    /* insertion de lignes ou de colonnes */
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
                CopexReturn cr= updateDataHeader(ds, true, idBefore+j, this.dataToolPanel.getBundleString("DEFAULT_DATAHEADER_NAME")+" "+(no+1), this.dataToolPanel.getBundleString("DEFAULT_DATAHEADER_UNIT"), this.dataToolPanel.getBundleString("DEFAULT_DATAHEADER_DESCRIPTION"),MyConstants.DEFAULT_TYPE_COLUMN, v2);
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

    /* drag and drop de colonnes */
    @Override
    public CopexReturn moveSubData(SubData subDataToMove, int noColInsertBefore, ArrayList v){
        return new CopexReturn();
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
        for (int i=0; i<nbCol; i++){
            tabHeader[i] = new DataHeader(idDataHeader++, headers[i], units[i], i, types[i], descriptions[i]);
        }
        // creation ds
        Dataset ds = new Dataset(idDataSet++, name, nbCol, nbRows, tabHeader,  data, listOp, listVis);
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
            ((Graph)vis).setParamGraph(paramGraph);
//            if (paramGraph.isAutoscale()){
//                ArrayList v2 = new ArrayList();
//                CopexReturn cr = findAxisParam(dataset, ((Graph)vis), v2);
//                if(cr.isError())
//                    return cr;
//                ParamGraph pg = (ParamGraph)(v2.get(0));
//                ((Graph)vis).setParamGraph(pg);
//                dataset.getListVisualization().set(idVis, ((Graph)vis));
//            }
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
                    dataset.setDataHeader(new DataHeader(idDataHeader++,ds.getDataHeader(j).getValue() , ds.getDataHeader(j).getUnit(), j+nbCols1, ds.getDataHeader(j).getType(), ds.getDataHeader(j).getDescription()), j+nbCols1);
                }
                for (int i=0; i<nbRows2; i++){
                    Data nd = null;
                    if (ds.getData(i, j) != null){
                        nd = new Data(idData++, ds.getData(i, j).getValue(), i,j+nbCols1, ds.getData(i, j).isIgnoredData() );
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
                dataset.addOperation(new DataOperation(idOperation++, op.getName(), op.getTypeOperation(), op.isOnCol(), listNo));
            }
            // merge des vis
            ArrayList<Visualization> listVis = ds.getListVisualization();
            int nVis = listVis.size();
            for (int i=0; i<nVis; i++){
                Visualization vis = listVis.get(i);
                if(vis instanceof SimpleVisualization){
                    int noCol = ((SimpleVisualization)vis).getNoCol()+nbCols1;
                    DataHeader headerLabel = null;
                    if(((SimpleVisualization)vis).getHeaderLabel() != null){
                        headerLabel = dataset.getDataHeader(((SimpleVisualization)vis).getHeaderLabel().getNoCol()+nbCols1);
                    }
                    SimpleVisualization sv = new SimpleVisualization(idVisualization++, vis.getName(), vis.getType(),noCol, headerLabel);
                    dataset.addVisualization(sv);
                }else if(vis instanceof Graph){
                    ArrayList<PlotXY> listPlots = new ArrayList();
                    for (Iterator<PlotXY> p = ((Graph)vis).getParamGraph().getPlots().iterator();p.hasNext();){
                        PlotXY plot = p.next();
                        listPlots.add(new PlotXY(dataset.getDataHeader(plot.getHeaderX().getNoCol()+nbCols1), dataset.getDataHeader(plot.getHeaderY().getNoCol()+nbCols1), plot.getPlotColor()));
                    }
                    ParamGraph pg = new ParamGraph(listPlots, ((Graph)vis).getParamGraph().getX_min(),((Graph)vis).getParamGraph().getX_max(), ((Graph)vis).getParamGraph().getX_min(), ((Graph)vis).getParamGraph().getY_max(), ((Graph)vis).getParamGraph().getDeltaX(), ((Graph)vis).getParamGraph().getDeltaY(),  ((Graph)vis).getParamGraph().isDeltaFixedAutoscale());
                    Graph g = new Graph(idVisualization++, vis.getName(), vis.getType(),pg,((Graph)vis).getListFunctionModel());
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
                   header = new DataHeader(idDataHeader++, subData.getDataHeader(j).getValue(),subData.getDataHeader(j).getUnit(),  idC+j, subData.getDataHeader(j).getType(), subData.getDataHeader(j).getDescription());
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
                    nd = new Data(idData++, d.getValue(), idR+i, idC+j, d.isIgnoredData()) ;
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
                            DataSetColumn c = new DataSetColumn(value, "", MyConstants.DEFAULT_TYPE_COLUMN);
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
                        String ctype = MyConstants.TYPE_DOUBLE;
                        for(Iterator<DataSetRow> r = ds.getValues().iterator(); r.hasNext();){
                            String s = r.next().getValues().get(j);
                            try{
                                double d = Double.parseDouble(s);
                            }catch(NumberFormatException e1){
                                ctype = MyConstants.TYPE_STRING;
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
}
