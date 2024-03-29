package eu.scy.client.tools.fitex.main;

import eu.scy.client.common.scyi18n.ResourceBundleWrapper;
import eu.scy.common.datasync.ISyncObject;
import eu.scy.elo.contenttype.dataset.DataSetColumn;
import eu.scy.elo.contenttype.dataset.DataSetHeader;
import eu.scy.elo.contenttype.dataset.DataSetRow;
import eu.scy.client.tools.dataProcessTool.collaboration.FitexSyncManager;
import eu.scy.client.tools.dataProcessTool.common.*;
import eu.scy.client.tools.dataProcessTool.controller.ControllerInterface;
import eu.scy.client.tools.dataProcessTool.controller.DataController;
import eu.scy.client.tools.dataProcessTool.controller.DataControllerDB;
import eu.scy.client.tools.dataProcessTool.controller.DataControllerLabBook;
import eu.scy.client.tools.dataProcessTool.dataTool.DataTableModel;
import eu.scy.client.tools.dataProcessTool.dataTool.FitexTabbedPane;
import eu.scy.client.tools.dataProcessTool.dataTool.FitexToolPanel;
import eu.scy.client.tools.dataProcessTool.dataTool.ImportCsvDialog;
import eu.scy.client.tools.dataProcessTool.dataTool.ImportDialog;
import eu.scy.client.tools.dataProcessTool.dataTool.MergeDialog;
import eu.scy.client.tools.dataProcessTool.dataTool.OpenDataAction;
import eu.scy.client.tools.dataProcessTool.dataTool.OpenFitexDialog;
import eu.scy.client.tools.dataProcessTool.logger.FitexLog;
import eu.scy.client.tools.dataProcessTool.logger.FitexProperty;
import eu.scy.client.tools.dataProcessTool.undoRedo.DataUndoRedo;
import eu.scy.client.tools.dataProcessTool.utilities.ActionDataProcessTool;
import eu.scy.client.tools.dataProcessTool.utilities.CopexReturn;
import eu.scy.client.tools.dataProcessTool.utilities.DataConstants;
import eu.scy.client.tools.dataProcessTool.utilities.MyFileFilter;
import eu.scy.client.tools.dataProcessTool.utilities.MyFileFilterCSV_GMBL;
import eu.scy.client.tools.dataProcessTool.utilities.MyUtilities;

import eu.scy.client.tools.fitex.analyseFn.Function;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import javax.swing.*;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import roolo.elo.JDomStringConversion;

/**
 * main panel for the data tool
 * @author Marjolaine
 */

public class DataProcessToolPanel extends javax.swing.JPanel implements OpenDataAction {
    //CST
    /* width */
    public static final int PANEL_WIDTH = 660;
    /* height */
    public static final int PANEL_HEIGHT = 355;
    /* MODE DEBUG */
    public static final boolean DEBUG_MODE = false;

    public static final int MENU_BAR_HEIGHT = 28;
    public static final int ICON_HEIGHT=31;
    public static final int ICON_WIDTH = 160;

    /* locale */
    private Locale locale ;
    /* ressource bundle */
    private ResourceBundleWrapper bundle;
    /* version */
    private String version = "3.8";
    /* number format */
    private NumberFormat numberFormat;

    private boolean scyMode;
    private boolean dbMode;
    /* controller */
    private ControllerInterface controller;
    /* sync. maanger*/
    private FitexSyncManager fitexSync ;

    private long dbKeyUser;
    private long dbKeyMission;
    private long dbKeyGroup;
    private long dbKeyLabDoc;
    private String labDocName;

    // DATA
    /* list of datasets */
    private ArrayList<Dataset> listDataset;
    private ArrayList<FitexToolPanel> listFitexPanel;
    private FitexToolPanel activFitex;
    /*list of types of visualizations*/
    private TypeVisualization[] tabTypeVis;
    /* list of types of operations */
    private TypeOperation[] tabTypeOp;
    /* list of predefined functions*/
    private ArrayList<PreDefinedFunctionCategory> listPreDefinedFunction;


    private ActionDataProcessTool fitexAction;

    private FitexTabbedPane fitexTabbedPane;
    private File lastUsedFileOpen = null;
    private File lastUsedFileImport = null;
    private File lastUsedFileMerge = null;
    private XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
    private transient SAXBuilder builder = new SAXBuilder(false);
    private  String sepField;
    private  String sepText;
    private String charEncoding;

    public DataProcessToolPanel(boolean scyMode, Locale locale) {
        super();
        this.scyMode = scyMode;
        this.locale = locale;
        this.dbMode = false;
        initComponents();
        initData(null);
        this.dbKeyMission = 1;
        this.dbKeyUser = 1;
        this.dbKeyGroup =0;
        this.dbKeyLabDoc = 0;
        this.labDocName = "";
    }

    public DataProcessToolPanel(URL url, Locale locale, long dbKeyMission, long dbKeyUser, long dbKeyGroup, long dbKeyLabDoc, String labDocName) {
        super();
        this.scyMode = false;
        this.locale = locale;
        this.dbMode = true;
        this.dbKeyMission = dbKeyMission;
        this.dbKeyUser = dbKeyUser;
        this.dbKeyGroup = dbKeyGroup;
        this.dbKeyLabDoc = dbKeyLabDoc;
        this.labDocName = labDocName;
        initComponents();
        initData(url);
    }
    

    private void initData(URL url){
        bundle = new ResourceBundleWrapper("fitex");
        initNumberFormat();
        if(url == null){
            this.controller = new DataController(this);
        }else{
            //this.controller = new DataControllerDB(this, url, dbKeyMission, dbKeyUser, dbKeyGroup, dbKeyLabDoc, labDocName);
            this.controller = new DataControllerLabBook(this, url, dbKeyMission, dbKeyUser, dbKeyGroup, dbKeyLabDoc, labDocName);
        }

        initGUI();
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }


    /**
    * Instanciates the object ActionDataProcessTool.
    * @param action ActionDataProcessTool
    */
    public void addFitexAction(ActionDataProcessTool fitexAction){
        this.fitexAction=fitexAction;
    }


    public String getVersion(){
        return this.version;
    }
    

    /* load data */
    public void loadData(){
      setCursor(new Cursor(Cursor.WAIT_CURSOR));
       // controller load
      CopexReturn cr = this.controller.load();
      if (cr.isError()){
          setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
          displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
      }
      logStartTool();
      setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }


    @Override
    public Locale getLocale(){
        return this.locale;
    }

    /* get the message corresponding to the key */
    public String getBundleString(String key){
        return bundle.getString("FITEX."+key);
    }

    private void initNumberFormat(){
        numberFormat = NumberFormat.getNumberInstance(getLocale());
        numberFormat.setMaximumFractionDigits(Integer.MAX_VALUE);
        numberFormat.setGroupingUsed(false);
    }

    public NumberFormat getNumberFormat(){
        return numberFormat;
    }

    /* show errors & warnings */
    public boolean displayError(CopexReturn dr, String title) {
        if (dr.mustConfirm ()){
            int erreur = JOptionPane.showConfirmDialog(this ,dr.getText() , title,JOptionPane.OK_CANCEL_OPTION);
            if (erreur == JOptionPane.OK_OPTION)
		return true;
	}else if (dr.isError()){
            JOptionPane.showMessageDialog(this ,dr.getText() , title,JOptionPane.ERROR_MESSAGE);
	}else if (dr.isWarning()){
            JOptionPane.showMessageDialog(this ,dr.getText() , title,JOptionPane.WARNING_MESSAGE);
	}
        return false;
    }

    /* show errors & warnings */
    public boolean  displayError(String msgError) {
        return displayError(new CopexReturn(msgError, false), getBundleString("TITLE_DIALOG_ERROR"));
    }

    public  ImageIcon getCopexImage(String img){
        ImageIcon imgIcon = new ImageIcon(getClass().getResource( "/Images/" +img));
        if (imgIcon == null){
            displayError(new CopexReturn(getBundleString("MSG_ERROR_IMAGE")+img, false), getBundleString("TITLE_DIALOG_ERROR"));
            return null;
        }else
            return imgIcon;
    }

    public Image getIconDialog(){
        return getCopexImage("labbook.png").getImage();
    }

    
    /* initialization GUI */
    protected void initGUI(){
        setMinimumSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setSize(PANEL_WIDTH, PANEL_HEIGHT);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        if(fitexAction != null)
            fitexAction.resizeDataToolPanel(getWidth(), getHeight());
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    public void initDataProcessingTool(TypeVisualization[] tabTypeVisual, TypeOperation[] tabTypeOp, ArrayList<PreDefinedFunctionCategory> listPreDefinedFunction, ArrayList<Dataset> listDataset){
        this.listDataset = listDataset;
        this.tabTypeOp = tabTypeOp;
        this.tabTypeVis = tabTypeVisual;
        this.listPreDefinedFunction = listPreDefinedFunction;
        this.listFitexPanel = new ArrayList();
        if(scyMode)
            initDataset();
        else
            initTabbedPane();
    }

    /* show msg for locked dataset */
    public void displayDatasetLocked(ArrayList<String> listDatasetNameLocked){
        if(listDatasetNameLocked == null)
            return;
        int nb = listDatasetNameLocked.size();
        if (nb == 0)
            return;
        String msg = getBundleString("MSG_WARNING_DATASET_LOCKED");
        for (int i=0; i<nb; i++){
            msg += " \n"+listDatasetNameLocked.get(i);
        }
        displayError(new CopexReturn(msg, true), getBundleString("TITLE_DIALOG_WARNING"));
    }

    private void initDataset(){
        if(listDataset.size() > 0){
            FitexToolPanel fitex = new FitexToolPanel(this, listDataset.get(0), controller, tabTypeVis, tabTypeOp, listPreDefinedFunction);
            fitex.initDataProcessingTool(listDataset.get(0));
            activFitex = fitex;
            listFitexPanel.add(fitex);
            this.add(fitex, BorderLayout.CENTER);
            revalidate();
            repaint();
        }
    }

    private void initTabbedPane(){
        this.add(getFitexTabbedPane(), BorderLayout.CENTER);
        int nb = listDataset.size();
        for (int i=0; i<nb; i++){
            addFitexPanel(listDataset.get(i));
        }
        revalidate();
        repaint();
    }

    private void addFitexPanel(Dataset ds){
        FitexToolPanel fitex = new FitexToolPanel(this, ds, controller, tabTypeVis, tabTypeOp, listPreDefinedFunction);
        fitex.initDataProcessingTool(ds);
        activFitex = fitex;
        listFitexPanel.add(fitex);
        fitexTabbedPane.addTab(ds.getName(), fitex);
    }
     

    
    
    public void setDataset(Dataset ds, boolean replace){
        if(replace || (scyMode && activFitex != null))
            activFitex.setDataset(ds);
        else if(!scyMode){
            this.addFitexPanel(ds);
        }
    }

    private FitexTabbedPane getFitexTabbedPane(){
        if(fitexTabbedPane == null){
            fitexTabbedPane = new FitexTabbedPane(this);
        }
        return fitexTabbedPane;
    }


    public boolean canSave(){
        return !scyMode && !dbMode;
    }

    public boolean canImport(){
        //return !scyMode;
        return true;
    }

    public boolean canPrint(){
        return !scyMode;
    }
    
    /* returns the tooltip text on the open button  */
    public String getToolTipTextOpen(){
        return getBundleString("TOOLTIPTEXT_OPEN_DATASET");
    }

    public void openDialogAddDataset(){
        OpenFitexDialog openFitexDialog =null;
        if(dbMode){
            ArrayList v = new ArrayList();
            CopexReturn cr = this.controller.getListDatasetToOpenOrMerge(v);
            if (cr.isError()){
                displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            ArrayList<Mission> listMission = (ArrayList)v.get(0);
            ArrayList<ArrayList<Dataset>> listDatasetMission = (ArrayList) v.get(1);
            openFitexDialog = new OpenFitexDialog(this, dbMode, listMission, listDatasetMission);
        }else{
            openFitexDialog = new OpenFitexDialog(this, dbMode, lastUsedFileOpen);
        }
        openFitexDialog.addOpenFitexAction(this);
        openFitexDialog.setVisible(true);
    }

    public void openDialogImport(){
        if(!dbMode){
            openImportChooseFile();
            return;
        }
        ImportDialog importDialog =null;
        if(dbMode){
            ArrayList v = new ArrayList();
            CopexReturn cr = this.controller.getListDatasetToOpenOrMerge(v);
            if (cr.isError()){
                displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            ArrayList<Mission> listMission = (ArrayList)v.get(0);
            ArrayList<ArrayList<Dataset>> listDatasetMission = (ArrayList) v.get(1);
            importDialog = new ImportDialog(this, dbMode, listMission, listDatasetMission);
        }else{
            importDialog = new ImportDialog(this, dbMode, lastUsedFileImport, lastUsedFileMerge);
        }
        importDialog.addOpenFitexAction(this);
        importDialog.setVisible(true);
    }

    public void openDialogCloseDataset(Dataset ds) {
        if(dbMode){
//            CloseDatasetDialog closeFitexDialog = new CloseDatasetDialog(this, ds);
//            closeFitexDialog.setVisible(true);
            closeDataset(ds);
        }else{
            if(activFitex.hasModification()){
                int ok = JOptionPane.showConfirmDialog(this, this.getBundleString("MESSAGE_DATASET_CLOSE"), this.getBundleString("TITLE_DIALOG_EXIT"),JOptionPane.OK_CANCEL_OPTION );
                if(ok == JOptionPane.OK_OPTION){
                    fitexTabbedPane.removeDataset(ds);
                    logDeleteDataset(ds);
                }
            }else{
                fitexTabbedPane.removeDataset(ds);
                logDeleteDataset(ds);
            }
        }
    }


    public boolean  closeDataset(Dataset dataset){
        CopexReturn cr = this.controller.closeDataset(dataset);
        if(cr.isError()){
           displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
           return false;
        }
        logDeleteDataset(dataset);
        fitexTabbedPane.removeDataset(dataset);
        return true;
    }

    public boolean removeDataset(Dataset dataset){
        CopexReturn cr = this.controller.deleteDataset(dataset);
        if(cr.isError()){
           displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
           return false;
        }
        logDeleteDataset(dataset);
        fitexTabbedPane.removeDataset(dataset);
        return true;
    }

    /* detete dataset (from controller) */
    public void deleteDataset(Dataset dataset ){
        if(fitexTabbedPane != null){
            fitexTabbedPane.removeDataset(dataset);
        }
        if(scyMode){
            activFitex.deleteAll();
            ArrayList v = new ArrayList();
            CopexReturn cr = this.controller.createDefaultDataset(getBundleString("DEFAULT_DATASET_NAME"), v);
            if(cr.isError()){
                displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            Dataset ds = (Dataset)v.get(0) ;
            this.setDataset(ds, false);
        }
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setMinimumSize(new java.awt.Dimension(0, 0));
        setPreferredSize(new java.awt.Dimension(480, 330));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 480, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 330, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        
    }//GEN-LAST:event_formComponentResized


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables


    public Element getPDS(){
        //logSaveDataset(activFitex.getDataset());
         return activFitex.getPDS();
     }

    public String getPreview(){
        //logSaveDataset(activFitex.getDataset());
         return activFitex.getPreview();
     }

    // new elo in scy
    public void newElo(){
        // scyMode is true
        CopexReturn cr = this.controller.deleteDataset(activFitex.getDataset());
        if(cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        activFitex.deleteAll();
        ArrayList v = new ArrayList();
        cr = this.controller.createDefaultDataset(getBundleString("DEFAULT_DATASET_NAME"), v);
        if(cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        Dataset ds = (Dataset)v.get(0) ;
        this.setDataset(ds, false);
        //logNewElo();
    }

    // new elo, not in scy, new tab
    @Override
    public void newElo(String name){
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.createDefaultDataset(name, v);
        if(cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        Dataset ds = (Dataset)v.get(0) ;
        this.setDataset(ds, false);
    }


    private void loadELO(String xmlContent, String dsName){
        if(scyMode){
            CopexReturn cr = this.controller.deleteDataset(activFitex.getDataset());
            if(cr.isError()){
                displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            activFitex.deleteAll();
        }
        CopexReturn cr = this.controller.loadELO(xmlContent, dsName);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
        }else if(cr.isWarning()){
            displayError(cr, getBundleString("TITLE_DIALOG_WARNING"));
        }
        if(!scyMode){
            logOpenDataset(activFitex.getDataset());
        }
    }

    // load/open ELO 
    public void loadELO(String xmlContent){
        loadELO(xmlContent, null);
     }

    //load/openELO
    @Override
    public void openELO(File file){
        if(MyUtilities.isCSVFile(file)|| MyUtilities.isGMBLFile(file)){
            importELO(file, true);
        }else{
            InputStreamReader fileReader = null;
            try{
                lastUsedFileOpen = file;
                fileReader = new InputStreamReader(new FileInputStream(file), "utf-8");
                Document doc = builder.build(fileReader, file.getAbsolutePath());
                loadELO(new JDomStringConversion().xmlToString(doc.getRootElement()));
            }catch (Exception e){
                e.printStackTrace();
                displayError(new CopexReturn("Erreur durant le chargement "+e, false), getBundleString("TITLE_DIALOG_ERROR"));
            }
            finally{
                if (fileReader != null)
                try{
                    fileReader.close();
                }catch (IOException e){
                    displayError(new CopexReturn("Erreur durant le chargement, fermeture fichier "+e, false), getBundleString("TITLE_DIALOG_ERROR"));
                }
            }
        }
    }

    //load/openELO
    @Override
    public void openDataset(Mission m, Dataset ds ){
        CopexReturn cr = this.controller.openDataset(m, ds);
        if(cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
        }
    }


    // IMPORT CSV SCY
    /* read csv file */
    public eu.scy.elo.contenttype.dataset.DataSet importCSVFile(File file, boolean createNew){
        ImportCsvDialog aDialog = new ImportCsvDialog(activFitex);
        aDialog.setVisible(true);
        if(sepField == null && sepText == null && charEncoding == null)
            return null;
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.importCSVFile(file, sepField, sepText,charEncoding, v);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return null;
        }
        eu.scy.elo.contenttype.dataset.DataSet elo = (eu.scy.elo.contenttype.dataset.DataSet)v.get(0);
        if(elo != null ){
            String fileName = file.getName();
            if(fileName != null){
                int id = fileName.indexOf(".csv");
                if(id != -1)
                    fileName = fileName.substring(0, id);
            }
            if(createNew){
                // import and load
                if(dbMode){
                    cr = this.controller.deleteDataset(activFitex.getDataset());
                    if(cr.isError()){
                        displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
                        return null;
                    }
                    activFitex.deleteAll();
                }
                loadELO(new JDomStringConversion().xmlToString(elo.toXML()), fileName);
            }else{
                //import & merge
                activFitex.mergeELO(elo.toXML(), false);
            }
        }
        logImportCsvFile(file.getPath(), activFitex.getDataset());
        return elo;
    }

    public void importCsvData(String sepField, String sepText, String charEncoding){
        this.sepField = sepField;
        this.sepText = sepText;
        this.charEncoding = charEncoding;
    }

    @Override
    public void importELO(File file, boolean createNew) {
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        lastUsedFileImport = file;
        if(MyUtilities.isGMBLFile(file)){
            eu.scy.elo.contenttype.dataset.DataSet dsElo = importGMBLFile(file, createNew);
        }else if(MyUtilities.isCSVFile(file)){
            eu.scy.elo.contenttype.dataset.DataSet dsElo = importCSVFile(file, createNew);
        }else{
            mergeELO(file);
        }
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
//        if(dsElo != null && !scyMode){
//            loadELO(new JDomStringConversion().xmlToString(dsElo.toXML()));
//        }
    }

    // merge 
    public void mergeELO(Element elo){
        Dataset ds = (Dataset)(activFitex.getDataset().clone());
        this.activFitex.mergeELO(elo, true);
        logMergeDataset(ds,"", activFitex.getDataset());
    }

    @Override
    public void mergeELO(File file){
        lastUsedFileMerge = file;
        InputStreamReader fileReader = null;
        try{
            Dataset ds = (Dataset)(activFitex.getDataset().clone());
            fileReader = new InputStreamReader(new FileInputStream(file), "utf-8");
            Document doc = builder.build(fileReader, file.getAbsolutePath());
            mergeELO(doc.getRootElement());
            logMergeDataset(ds,file.getPath(), activFitex.getDataset());
        }catch (Exception e){
            e.printStackTrace();
            displayError(new CopexReturn("Erreur durant le chargement "+e, false), "Erreur");
        }
        finally{
            if (fileReader != null)
                try{
                    fileReader.close();
                }catch (IOException e){
                    displayError(new CopexReturn("Erreur durant le chargement, fermeture fichier "+e, false), "Erreur");
		}
        }
    }

    // merge with a dataset in database
    @Override
    public void mergeDataset(Mission mission, Dataset ds) {
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.mergeDataset(activFitex.getDataset(), mission, ds, v);
        if(cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        Dataset dataset = (Dataset)v.get(0);
        setDataset(dataset, true);
    }

    //initialize the model with the header
    public void initializeHeader(DataSetHeader header){
        int nbC = header.getColumnCount();
        if(nbC==0)
            return;
        if(scyMode){
            CopexReturn cr = this.controller.deleteDataset(activFitex.getDataset());
            if(cr.isError()){
                displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
        }
        if(nbC > 0){
            String[] headers = new String[nbC];
            String[] units = new String[nbC];
            String[] types = new String[nbC];
            String[] descriptions = new String[nbC];
            int j=0;
            for (Iterator<DataSetColumn> h = header.getColumns().iterator();h.hasNext();){
                DataSetColumn c = h.next();
                //headers[j] = c.getSymbol();
                headers[j] = DataHeader.computeHeaderValue(c.getSymbol(), c.getDescription());
                units[j] = c.getUnit();
                types[j] = c.getType();
                descriptions[j] = c.getDescription();
                j++;
            }
            ArrayList v = new ArrayList();
            CopexReturn cr = this.controller.createDataset(getBundleString("DEFAULT_DATASET_NAME"), headers, units,types, descriptions, v);
            if (cr.isError()){
                displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            }
            Dataset ds = (Dataset)v.get(0);
            setDataset(ds, false);
            logInitializeHeader(ds);
        }
    }

    // add row
    public void addRow(DataSetRow row){
        if(activFitex != null){
            activFitex.addData(row);
            logAddRow(activFitex.getDataset(), row);
        }
    }

    /* exit fitex */
    public void endTool(){
        CopexReturn cr = this.controller.stopFitex();
        if (cr.isError())
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
        logEndTool();
    }

    /* log: start tool */
    public void logStartTool(){
        if(!scyMode)
            fitexAction.logAction(DataConstants.LOG_TYPE_START_TOOL, new LinkedList());
    }

    /* log: end tool */
    public void logEndTool(){
        if(!scyMode)
            fitexAction.logAction(DataConstants.LOG_TYPE_END_TOOL, new LinkedList());
    }

    /* log: open dataset */
    public void logOpenDataset(Dataset ds){
        List<FitexProperty> attribute = FitexLog.logOpenDataset(ds);
        fitexAction.logAction(DataConstants.LOG_TYPE_OPEN_DATASET, attribute);
    }

    /* log: edit_dataset*/
    public void logEditData(Dataset ds, Data oldData, Data newData){
        List<FitexProperty> attribute = FitexLog.logEditData(ds, locale,oldData, newData);
        fitexAction.logAction(DataConstants.LOG_TYPE_EDIT_DATA, attribute);
    }
    /* log: merge dataset*/
    public void logMergeDataset(Dataset ds,String fileName , Dataset finalDataset){
       List<FitexProperty> attribute = FitexLog.logMergeDataset(ds, locale, fileName, finalDataset);
       fitexAction.logAction(DataConstants.LOG_TYPE_MERGE_DATASET, attribute);
    }
    /* log: import csv file */
    public void logImportCsvFile(String fileName, Dataset ds){
        List<FitexProperty> attribute = FitexLog.logImportCsvFile(fileName, ds, locale);
        fitexAction.logAction(DataConstants.LOG_TYPE_IMPORT_CSV_FILE, attribute);
    }

    /* log: import gmbl file */
    public void logImportGMBLFile(String fileName, Dataset ds){
        List<FitexProperty> attribute = FitexLog.logImportCsvFile(fileName, ds, locale);
        fitexAction.logAction(DataConstants.LOG_TYPE_IMPORT_GMBL_FILE, attribute);
    }

    /* log: delete dataset*/
    public void logDeleteDataset(Dataset ds){
       List<FitexProperty> attribute = FitexLog.logDeleteDataset(ds);
       fitexAction.logAction(DataConstants.LOG_TYPE_DELETE_DATASET, attribute);
    }

    /* log: insert columns */
    public void logInsertColumns(Dataset ds, int nbCol, int idBefore){
       List<FitexProperty> attribute = FitexLog.logInsertColumns(ds, locale, nbCol, idBefore);
       fitexAction.logAction(DataConstants.LOG_TYPE_INSERT_COLUMNS, attribute);
    }

    /* log: insert rows */
    public void logInsertRows(Dataset ds, int nbRows, int idBefore){
       List<FitexProperty> attribute = FitexLog.logInsertRows(ds, locale, nbRows, idBefore);
       fitexAction.logAction(DataConstants.LOG_TYPE_INSERT_ROWS, attribute);
    }

    /* log: addrow */
    public void logAddRow(Dataset ds, DataSetRow row){
        List<FitexProperty> attribute = FitexLog.logAddRow(ds, locale,row);
        fitexAction.logAction(DataConstants.LOG_ADD_ROW, attribute);
    }

    /* log: initialize header */
    public void logInitializeHeader(Dataset ds){
        List<FitexProperty> attribute = FitexLog.logInitializeHeader(ds, locale);
        fitexAction.logAction(DataConstants.LOG_INITIALIZE_HEADER, attribute);
    }

    /* log: delete datas*/
    public void logDeleteDatas(Dataset ds, ArrayList<Data> listData,  ArrayList<Integer> listNoDataRow, ArrayList<Integer> listNoDataCol,ArrayList<DataOperation> listOperation){
        List<FitexProperty> attribute = FitexLog.logDeleteData(ds, locale, listData,listNoDataRow, listNoDataCol,listOperation );
        fitexAction.logAction(DataConstants.LOG_TYPE_DELETE, attribute);
    }


    /* log: add operation */
    public void logAddOperation(Dataset ds, DataOperation operation){
        List<FitexProperty> attribute = FitexLog.logAddOperation(ds, operation);
        fitexAction.logAction(DataConstants.LOG_TYPE_ADD_OPERATION, attribute);
    }
    /* log: edit data header*/
    public void logEditHeader(Dataset ds, DataHeader oldHeader, DataHeader newHeader){
        List<FitexProperty> attribute = FitexLog.logEditHeader(ds, locale, oldHeader, newHeader);
        fitexAction.logAction(DataConstants.LOG_TYPE_EDIT_HEADER, attribute);
    }

    /* log: ignore data */
    public void logIgnoreData(Dataset ds, boolean isIgnored, ArrayList<Data> listData){
        List<FitexProperty> attribute = FitexLog.logIgnoreData(ds, locale, isIgnored, listData);
        fitexAction.logAction(DataConstants.LOG_TYPE_IGNORE_DATA, attribute);
    }

    /* log: create Visualization */
    public void logCreateVisualization(Dataset ds, Visualization vis){
        List<FitexProperty> attribute = FitexLog.logCreateVisualization(ds, locale, vis);
        fitexAction.logAction(DataConstants.LOG_TYPE_CREATE_VISUALIZATION, attribute);
    }

    /* log: delete visualization */
    public void logDeleteVisualization(Dataset ds, Visualization vis){
        List<FitexProperty> attribute = FitexLog.logDeleteVisualization(ds, locale, vis);
        fitexAction.logAction(DataConstants.LOG_TYPE_DELETE_VISUALIZATION, attribute);
    }

    /* log: graph mode */
    public void logGraphMode(Dataset ds, Visualization vis, char graphMode){
        List<FitexProperty> attribute = FitexLog.logGraphMode(ds, vis, graphMode);
        fitexAction.logAction(DataConstants.LOG_TYPE_GRAPH_MODE, attribute);
    }

    /* log: update graph param */
    public void logUpdateGraphParam(Dataset ds, String oldName, Visualization newVis){
        List<FitexProperty> attribute = FitexLog.logUpdateGraphParam(ds, oldName, newVis);
        fitexAction.logAction(DataConstants.LOG_TYPE_UPDATE_VISUALIZATION, attribute);
    }

    /* sort dataset, list title / 0-1: ascending/descending  */
    public void logSortDataset(Dataset ds, List<Object[]> elementToSort){
        List<FitexProperty> attribute = FitexLog.logSortDataset(ds, elementToSort);
        fitexAction.logAction(DataConstants.LOG_TYPE_SORT_DATASET, attribute);
    }

    /* log: copy dataset */
    public void logCopy(Dataset ds, ArrayList<int[]> listSelCell){
        List<FitexProperty> attribute = FitexLog.logCopy(ds, listSelCell);
        fitexAction.logAction(DataConstants.LOG_TYPE_COPY, attribute);
    }

    /* log: paste*/
    public void logPaste(Dataset ds, int[] selCell, CopyDataset copyDs){
        List<FitexProperty> attribute = FitexLog.logPaste(ds, locale, selCell, copyDs);
        fitexAction.logAction(DataConstants.LOG_TYPE_PASTE, attribute);
    }

    /* log: cut dataset */
    public void logCut(Dataset ds){
        fitexAction.logAction(DataConstants.LOG_TYPE_CUT, new LinkedList());
    }
    
    /* log: undo*/
    public void logUndo(Dataset ds, DataUndoRedo undoAction){
        List<FitexProperty> attribute = FitexLog.logUndoRedo(ds, locale,  undoAction);
        fitexAction.logAction(DataConstants.LOG_TYPE_UNDO, attribute);
    }

    /* log: undo*/
    public void logRedo(Dataset ds, DataUndoRedo redoAction){
        List<FitexProperty> attribute = FitexLog.logUndoRedo(ds, locale,  redoAction);
        fitexAction.logAction(DataConstants.LOG_TYPE_REDO, attribute);
    }

    /* log: function model */
    public void logFunctionModel(Dataset ds, Graph graph, String description,  Color fColor, ArrayList<FunctionParam> listParam){
        List<FitexProperty> attribute = FitexLog.logFunctionModel(ds, locale, graph, description, fColor, listParam);
        fitexAction.logAction(DataConstants.LOG_TYPE_GRAPH_FUNCTION, attribute);
    }

    /* log: rename dataset */
    public void logRenameDataset(String oldName, String newName){
        List<FitexProperty> attribute = FitexLog.logRenameDataset(oldName, newName);
        fitexAction.logAction(DataConstants.LOG_TYPE_RENAME_DATASET, attribute);
    }

    /* log: save dataset */
    public void logSaveDataset(Dataset ds){
        List<FitexProperty> attribute = FitexLog.logSaveDataset(ds);
        fitexAction.logAction(DataConstants.LOG_TYPE_SAVE_DATASET, attribute);
    }

    /* log: csv export */
    public void logExportCSV(Dataset ds, String fileName){
        List<FitexProperty> attribute = FitexLog.logExportCsv(ds, fileName);
        fitexAction.logAction(DataConstants.LOG_TYPE_EXPORT_CSV_FILE, attribute);
    }

    /* log: new elo */
    public void logNewElo(){
        fitexAction.logAction(DataConstants.LOG_TYPE_NEW, new LinkedList());
    }

    
    /* log an action in db*/
    public void logAction(String type, List<FitexProperty> attribute){
        CopexReturn cr = this.controller.logUserActionInDB(type, attribute);
        if(cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
        }
    }

    public Function getFunction(String formulaValue){
        if(activFitex != null)
            return activFitex.getFunction(formulaValue);
        return null;
    }

    /* return true if the dataset can be close (if it is not the dataset of the labdoc) */
    public boolean canCloseDataset(Dataset ds){
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.isLabDocDataset(ds, v);
        if(cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return false;
        }
        return !(Boolean)v.get(0);
    }

    public boolean canRenameDataset(){
        return !dbMode;
    }

    public ArrayList<Object> getListGraph(){
        if(activFitex != null)
            return activFitex.getListGraph();
        else
            return new ArrayList();
    }

    /* returns the interface panel for the thumbnail */
    public Container getInterfacePanel(){
        if(activFitex != null)
            return activFitex.getInterfacePanel();
        else
            return new JPanel();
    }


    private void openImportChooseFile(){
        JFileChooser aFileChooser = new JFileChooser();
        if(canImportXML()){
            aFileChooser.setFileFilter(new MyFileFilter());
        }else{
            aFileChooser.setFileFilter(new MyFileFilterCSV_GMBL());
        }
        if (lastUsedFileImport != null){
            aFileChooser.setCurrentDirectory(lastUsedFileImport.getParentFile());
            aFileChooser.setSelectedFile(lastUsedFileImport);
        }
        int userResponse = aFileChooser.showOpenDialog(this);
        if (userResponse == JFileChooser.APPROVE_OPTION){
            setCursor(new Cursor(Cursor.WAIT_CURSOR));
            File file = aFileChooser.getSelectedFile();
            if( (canImportXML() && (!MyUtilities.isCSVFile(file) && !MyUtilities.isGMBLFile(file)&& !MyUtilities.isXMLFile(file)) )
                    || (!canImportXML() && (!MyUtilities.isCSVFile(file) && !MyUtilities.isGMBLFile(file)) )){
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                displayError(new CopexReturn(getBundleString("MSG_ERROR_FILE"), false), getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            lastUsedFileImport = file;
            if(lastUsedFileImport == null){
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                displayError(new CopexReturn(getBundleString("MSG_ERROR_IMPORT_DATASET") ,false), getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            importELO(lastUsedFileImport, false);
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            return;
        }
    }

    public boolean canImportXML(){
        return !dbMode && !scyMode;
    }

    /* return true if can import gmbl file*/
    public boolean canImportGMBLFile(){
        //return this.scyMode || !this.dbMode;
        return true;
    }

    /* import a gmbl file => get the dataset elo */
    public eu.scy.elo.contenttype.dataset.DataSet importGMBLFile(File file, boolean createNew){
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.importGMBLFile(file, v);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return null;
        }
        eu.scy.elo.contenttype.dataset.DataSet elo = (eu.scy.elo.contenttype.dataset.DataSet)v.get(0);
        if(elo != null ){
            String fileName = file.getName();
            if(fileName != null){
                int id = fileName.indexOf(".");
                if(id != -1)
                    fileName = fileName.substring(0, id);
            }
            if(createNew){
                // import and load
                if(dbMode){
                    cr = this.controller.deleteDataset(activFitex.getDataset());
                    if(cr.isError()){
                        displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
                        return null;
                    }
                    activFitex.deleteAll();
                }
                loadELO(new JDomStringConversion().xmlToString(elo.toXML()), fileName);
            }else{
                //import & merge
                activFitex.mergeELO(elo.toXML(), false);
            }
        }
        logImportGMBLFile(file.getPath(), activFitex.getDataset());
        return elo;
    }

    public DataTableModel getDataTableModel(Dataset ds){
       if(fitexTabbedPane != null)
        return fitexTabbedPane.getDataTableModel(ds);
       else if(activFitex != null)
           return activFitex.getDataTableModel();
       else return null;
    }

    public void setActivFitex(FitexToolPanel activFitex){
        this.activFitex = activFitex;
    }

    /* ask the user / merge 1 row */
    public void askForMergeType(Dataset ds1, Dataset ds2, Element elo,  boolean isMatrix){
        MergeDialog mergeDialog = new MergeDialog(this, ds1, ds2, elo, isMatrix);
        mergeDialog.setVisible(true);
    }

    public boolean mergeDatasets(Dataset ds1, Dataset ds2, Element elo, String mergeAction){
        if(mergeAction.equals(DataConstants.actionMerge)){
            CopexReturn cr = this.controller.mergeELO(ds1, elo, false);
            if (cr.isError()){
                displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
                return false;
            }else if(cr.isWarning()){
                displayError(cr, getBundleString("TITLE_DIALOG_WARNING"));
                return false;
            }
        }else if (mergeAction.equals(DataConstants.actionMergeRow)){
            CopexReturn cr = this.controller.mergeRowELO(ds1, ds2);
            if (cr.isError()){
                displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
                return false;
            }else if(cr.isWarning()){
                displayError(cr, getBundleString("TITLE_DIALOG_WARNING"));
                return false;
            }
        }else if (mergeAction.equals(DataConstants.actionMatrixAddOperation)){
            CopexReturn cr = this.controller.mergeMatrixAddOperation(ds1, ds2);
            if (cr.isError()){
                displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
                return false;
            }else if(cr.isWarning()){
                displayError(cr, getBundleString("TITLE_DIALOG_WARNING"));
                return false;
            }
        }else if (mergeAction.equals(DataConstants.actionMatrixMultiplyOperation)){
            CopexReturn cr = this.controller.mergeMatrixMultiplyOperation(ds1, ds2);
            if (cr.isError()){
                displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
                return false;
            }else if(cr.isWarning()){
                displayError(cr, getBundleString("TITLE_DIALOG_WARNING"));
                return false;
            }
        }
        return true;
    }

    public boolean controlLenght(){
        return !scyMode;
    }

    /** set the current proc in read-only mode */
    public void setReadOnly(boolean readOnly){
        if(activFitex != null){
            activFitex.setReadOnly(readOnly);
        }
    }

    /*starts the collaboration */
    public void startCollaboration(){
        fitexSync = new FitexSyncManager(this);
    }

    /* ends the collaboration */
    public void endCollaboration(){
        fitexSync = null;
    }

    /* returns true if the tool is sync with another (collaboration) */
    public boolean isCollaborate(){
        return fitexSync != null;
    }

    // received added sync Object
    public void syncNodeAdded(ISyncObject syncObject){
        fitexSync.syncNodeAdded(syncObject);
    }

    // received removed sync Object
    public void syncNodeRemoved(ISyncObject syncObject){
        fitexSync.syncNodeRemoved(syncObject);
    }

    // received changed sync Object
    public void syncNodeChanged(ISyncObject syncObject){
        fitexSync.syncNodeChanged(syncObject);
    }

    // sent added sync. object
    public void addFitexSyncObject(ISyncObject syncObject) {
        if(fitexAction != null)
            fitexAction.addFitexSyncObject(syncObject);
    }

    // sent changed sync. object
    public void changeFitexSyncObject(ISyncObject syncObject) {
        if(fitexAction != null)
            fitexAction.changeFitexSyncObject(syncObject);
    }

    // sent removed sync. object
    public void removeFitexSyncObject(ISyncObject syncObject) {
        if(fitexAction != null)
            fitexAction.removeFitexSyncObject(syncObject);
    }

    // UPDATE DATA
    /* receives sync. event: update data*/
    public void updateData(String value, int idRow, int idCol){
        if(activFitex != null){
            activFitex.updateData(activFitex.getDataset(), value, idRow, idCol, false);
        }
    }

    /*sent sync. event: update data*/
    public void addSyncUpdateData(String value, int idRow, int idCol){
        if(isCollaborate())
            fitexSync.addSyncUpdateData(value, idRow, idCol);
    }

    // UPDATE HEADER
    /* receives sync. event: update dataheader*/
    public void updateDataHeader(DataHeader header){
        if(activFitex != null){
            activFitex.updateDataHeader(activFitex.getDataset(), header.getValue(), header.getUnit(), header.getNoCol(), header.getDescription(), header.getType(), header.getFormulaValue(), header.isScientificNotation(), header.getNbShownDecimals(), header.getNbSignificantDigits(),  false);
        }
    }

    /*sent sync. event: update dataheader*/
    public void addSyncUpdateDataHeader(DataHeader header){
        if(isCollaborate())
            fitexSync.addSyncUpdateDataHeader(header);
    }

    // IGNORE DATA
    /* receives sync. event: ignore data*/
    public void updateIgnoreData(boolean isIgnored, ArrayList<Data> listData){
        if(activFitex != null){
            activFitex.setDataIgnored(activFitex.getDataset(),isIgnored,listData, false);
        }
    }

    /*sent sync. event: ignore data */
    public void addSyncUpdateIgnoreData(boolean isIgnored, ArrayList<Data> listData){
        if(isCollaborate())
            fitexSync.addSyncUpdateIgnoreData(isIgnored,listData);
    }

    // CREATE OPERATION
    /* receives sync. event: create operation*/
    public void createOperation(DataOperation operation){
        if(activFitex != null){
            activFitex.createOperation(activFitex.getDataset(), operation.getTypeOperation().getType(),operation.isOnCol(), operation.getListNo(), false);
        }
    }

    /*sent sync. event: create operation */
    public void addSyncCreateOperation(DataOperation operation){
        if(isCollaborate())
            fitexSync.addSyncCreateoperation(operation);
    }

    // GRAPH CREATED
    /* receives sync. event: create vis*/
    public void createVisualization(Visualization vis){
        if(activFitex != null){
            DataHeader header1 = null;
            DataHeader headerLabel = null;
            ArrayList<PlotXY> listPlot = null;
            if(vis instanceof SimpleVisualization){
                header1 = ((SimpleVisualization)vis).getHeader();
                headerLabel = ((SimpleVisualization)vis).getHeaderLabel();
            }else if(vis instanceof Graph){
                listPlot = ((Graph)vis).getParamGraph().getPlots();
            }
            activFitex.createVisualization(vis.getName(), vis.getType(),   header1,headerLabel, listPlot, false);
        }
    }

    /*sent sync. event: create vis */
    public void addSyncCreateVisualization(Visualization vis){
        if(isCollaborate())
            fitexSync.addSyncGraphCreated(vis);
    }

    // GRAPH PARAM UPDATED
    /* receives sync. event: update graph param*/
    public void updateGraphParam(Graph graph){
        if(activFitex != null){
            activFitex.updateGraphParam(graph, graph.getName(), graph.getParamGraph(), false);
        }
    }

    /*sent sync. event: update graph param */
    public void addSyncUpdateGraphParam(Graph graph){
        if(isCollaborate())
            fitexSync.addSyncGraphParamUpdated(graph);
    }

    // UPDATE FUNCTION MODEL
    /* receives sync. event: update function model*/
    public void updateFunctionModel(Graph graph, FunctionModel fm){
        if(activFitex != null){
            activFitex.setFunctionModel(graph, fm.getDescription(), fm.getType(), fm.getColor(), fm.getListParam(), fm.getIdPredefFunction(), false);
        }
    }

    /*sent sync. event: update function model */
    public void addSyncUpdateFunction(Graph graph, FunctionModel fm){
        if(isCollaborate())
            fitexSync.addSyncFunctionModelUpdated(graph, fm);
    }

    // DELETE DATA
    /* receives sync. event: delete data*/
    public void deleteData(ArrayList<Data> listData, ArrayList<DataHeader> listHeader, ArrayList<DataOperation> listOperation, ArrayList<Integer>[] listRowAndCol){
        if(activFitex != null){
            activFitex.deleteData(activFitex.getDataset(), listData, listHeader, listOperation, listRowAndCol, false);
        }
    }

    /*sent sync. event: delete data*/
    public void addSyncDeleteData(ArrayList<Data> listData, ArrayList<DataHeader> listHeader, ArrayList<DataOperation> listOperation, ArrayList<Integer>[] listRowAndCol){
        if(isCollaborate())
            fitexSync.addSyncDataDeleted(listData, listHeader, listOperation, listRowAndCol);
    }

    // INSERT DATA
    /* receives sync. event: insert data*/
    public void insertData(boolean isOnCol, int nb, int idBefore){
        if(activFitex != null){
            activFitex.insertData(activFitex.getDataset(), isOnCol, nb, idBefore, false);
        }
    }

    /*sent sync. event: insert data*/
    public void addSyncInsertData(boolean isOnCol, int nb, int idBefore){
        if(isCollaborate())
            fitexSync.addSyncInsertData(isOnCol, nb, idBefore);
    }

    // DATASET SORT
    /* receives sync. event: sort data*/
    public void sortData(Vector exchange){
        if(activFitex != null){
            activFitex.updateDatasetRow(activFitex.getDataset(), exchange,false);
        }
    }

    /*sent sync. event: insert data*/
    public void addSyncInsertData(Vector exchange){
        if(isCollaborate())
            fitexSync.addSyncSortData(exchange);
    }


    // PASTE
    /* receives sync. event: paste*/
    public void paste(CopyDataset copyDs, int[] selCell){
        if(activFitex != null){
            activFitex.paste(copyDs, selCell, false);
        }
    }

    /*sent sync. event: insert data*/
    public void addSyncPaste(CopyDataset copyDs, int[] selCell){
        if(isCollaborate())
            fitexSync.addSyncPaste(copyDs, selCell);
    }
    

}
