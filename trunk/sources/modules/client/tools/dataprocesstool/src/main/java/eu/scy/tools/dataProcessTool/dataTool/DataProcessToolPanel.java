/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MainDataToolPanel.java
 *
 * Created on 14 janv. 2009, 10:59:21
 */

package eu.scy.tools.dataProcessTool.dataTool;

import eu.scy.client.common.scyi18n.ResourceBundleWrapper;
import eu.scy.elo.contenttype.dataset.DataSetColumn;
import eu.scy.elo.contenttype.dataset.DataSetHeader;
import eu.scy.elo.contenttype.dataset.DataSetRow;
import eu.scy.tools.dataProcessTool.common.*;
import eu.scy.tools.dataProcessTool.controller.ControllerInterface;
import eu.scy.tools.dataProcessTool.controller.DataController;
import eu.scy.tools.dataProcessTool.controller.DataControllerDB;
import eu.scy.tools.dataProcessTool.logger.FitexLog;
import eu.scy.tools.dataProcessTool.logger.FitexProperty;
import eu.scy.tools.dataProcessTool.undoRedo.DataUndoRedo;
import eu.scy.tools.dataProcessTool.utilities.ActionDataProcessTool;
import eu.scy.tools.dataProcessTool.utilities.CopexReturn;
import eu.scy.tools.dataProcessTool.utilities.DataConstants;
import eu.scy.tools.dataProcessTool.utilities.MyFileFilter;
import eu.scy.tools.dataProcessTool.utilities.MyFileFilterCSV_GMBL;
import eu.scy.tools.dataProcessTool.utilities.MyUtilities;

import eu.scy.tools.fitex.analyseFn.Function;
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
//import java.util.MissingResourceException;
//import java.util.ResourceBundle;
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

    //CONSTANTES
    
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
    //private ResourceBundle bundle;
    /* version */
    private String version = "3.8";
    /* number format */
    private NumberFormat numberFormat;

    private boolean scyMode;
    private boolean dbMode;
    /* interface noyau */
    private ControllerInterface controller;
    private long dbKeyUser;
    private long dbKeyMission;
    private long dbKeyGroup;
    private long dbKeyLabDoc;
    private String labDocName;

    // Donnees
    /* liste des donnees */
    private ArrayList<Dataset> listDataset;
    private ArrayList<FitexToolPanel> listFitexPanel;
    private FitexToolPanel activFitex;
    /*tableau des differents types de visualisation */
    private TypeVisualization[] tabTypeVis;
    /* tableau des differentes operations possibles */
    private TypeOperation[] tabTypeOp;
    /* liste des fonctions predef*/
    private ArrayList<PreDefinedFunctionCategory> listPreDefinedFunction;


    private ActionDataProcessTool action;

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
//        // i18n
//        locale = Locale.getDefault();
//        locale = new Locale("en", "GB");
//        //locale = new Locale("fr", "FR");
//        try{
//            this.bundle = ResourceBundle.getBundle("languages/fitex" , locale);
//        }catch(MissingResourceException e){
//          try{
//              // english language by def.
//              locale = new Locale("en");
//              bundle = ResourceBundle.getBundle("languages/fitex", locale);
//          }catch (MissingResourceException e2){
//            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
//            System.out.println("ERREUR lors du chargement de l'applet, la langue specifiee "+locale+" n'existe pas : "+e2);
//            displayError(new CopexReturn("ERREUR lors du chargement de l'application : "+e, false), "ERROR LANGUAGE");
//            return;
//          }
//        }
        bundle = new ResourceBundleWrapper("fitex");
        //
        initNumberFormat();
        // noyau
        if(url == null){
            this.controller = new DataController(this);
        }else{
            this.controller = new DataControllerDB(this, url, dbKeyMission, dbKeyUser, dbKeyGroup, dbKeyLabDoc, labDocName);
        }

        initGUI();
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

    }


    /**
    * Instancie l'objet ActionDataProcessTool.
    * @param action ActionDataProcessTool
    */
    public void addFitexAction(ActionDataProcessTool action){
        this.action=action;
    }


    public String getVersion(){
        return this.version;
    }
    

    /* chargement des donnees */
    public void loadData(){
      setCursor(new Cursor(Cursor.WAIT_CURSOR));
       // appel au noyau : chargement des donnees
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
    /* retourne un message selon cle*/
    public String getBundleString(String key){
        return bundle.getString("FITEX."+key);
//       String s = "";
//        try{
//            s = this.bundle.getString(key);
//        }catch(Exception e){
//            System.out.println("getBundleString "+e);
//            try{
//                String msg = this.bundle.getString("ERROR_KEY");
//                msg = MyUtilities.replace(msg, 0, key);
//                displayError(new CopexReturn(msg, false) , this.bundle.getString("TITLE_DIALOG_ERROR"));
//            }catch(Exception e2){
//                displayError(new CopexReturn("No message found !"+key, false) ,"ERROR");
//             }
//        }
//        return s;

    }

    private void initNumberFormat(){
        numberFormat = NumberFormat.getNumberInstance(getLocale());
        numberFormat.setMaximumFractionDigits(Integer.MAX_VALUE);
        numberFormat.setGroupingUsed(false);
    }
    public NumberFormat getNumberFormat(){
        return numberFormat;
    }
    /* affichage des erreurs*/
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

    /* affichage des erreurs*/
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

    
    /* initialisation de l'applet */
    protected void initGUI(){
        // Initialisation du look and feel
//        try{
//            String myLookAndFeel=UIManager.getSystemLookAndFeelClassName();
//            UIManager.setLookAndFeel(myLookAndFeel);
//        }catch(Exception e){
//            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
//            System.out.println("ERREUR dans l'initialisation du lookAndFeel : "+e) ;
//            JOptionPane.showMessageDialog(this , "ERREUR ans l'initialisation du lookAndFeel : "+e, "ERROR",JOptionPane.ERROR_MESSAGE);
//        }
        setMinimumSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setSize(PANEL_WIDTH, PANEL_HEIGHT);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        if(action != null)
            action.resizeDataToolPanel(getWidth(), getHeight());
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
    /* affichage d'un message concernant les dataset lockes */
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
    
    /* retourne le tooltiptext sur le bouton d'ouverture */
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

    /* appel par le noyau suppression du dataset */
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

    // nouveau interface scy
    public void newElo(){
        // scyMode est vrai
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
    // nouvel elo non scy, multi onglets
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

    // load/open ELO SCY ou non
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
    /* lecture de fichier cvs => elo ds */
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

    // merge SCY ou autre
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

    // merge avec un ds de la base
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

    /* sortie de l'outil */
    public void endTool(){
        CopexReturn cr = this.controller.stopFitex();
        if (cr.isError())
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
        logEndTool();
    }
    /* log: start tool */
    public void logStartTool(){
        if(!scyMode)
            action.logAction(DataConstants.LOG_TYPE_START_TOOL, new LinkedList());
    }
    /* log: end tool */
    public void logEndTool(){
        if(!scyMode)
            action.logAction(DataConstants.LOG_TYPE_END_TOOL, new LinkedList());
    }

    /* log: open dataset */
    public void logOpenDataset(Dataset ds){
        List<FitexProperty> attribute = FitexLog.logOpenDataset(ds);
        action.logAction(DataConstants.LOG_TYPE_OPEN_DATASET, attribute);
    }

    /* log: edit_dataset*/
    public void logEditData(Dataset ds, Data oldData, Data newData){
        List<FitexProperty> attribute = FitexLog.logEditData(ds, locale,oldData, newData);
        action.logAction(DataConstants.LOG_TYPE_EDIT_DATA, attribute);
    }
    /* log: merge dataset*/
    public void logMergeDataset(Dataset ds,String fileName , Dataset finalDataset){
       List<FitexProperty> attribute = FitexLog.logMergeDataset(ds, locale, fileName, finalDataset);
       action.logAction(DataConstants.LOG_TYPE_MERGE_DATASET, attribute);
    }
    /* log: import csv file */
    public void logImportCsvFile(String fileName, Dataset ds){
        List<FitexProperty> attribute = FitexLog.logImportCsvFile(fileName, ds, locale);
        action.logAction(DataConstants.LOG_TYPE_IMPORT_CSV_FILE, attribute);
    }

    /* log: import gmbl file */
    public void logImportGMBLFile(String fileName, Dataset ds){
        List<FitexProperty> attribute = FitexLog.logImportCsvFile(fileName, ds, locale);
        action.logAction(DataConstants.LOG_TYPE_IMPORT_GMBL_FILE, attribute);
    }

    /* log: delete dataset*/
    public void logDeleteDataset(Dataset ds){
       List<FitexProperty> attribute = FitexLog.logDeleteDataset(ds);
       action.logAction(DataConstants.LOG_TYPE_DELETE_DATASET, attribute);
    }

    /* log: insert columns */
    public void logInsertColumns(Dataset ds, int nbCol, int idBefore){
       List<FitexProperty> attribute = FitexLog.logInsertColumns(ds, locale, nbCol, idBefore);
       action.logAction(DataConstants.LOG_TYPE_INSERT_COLUMNS, attribute);
    }

    /* log: insert rows */
    public void logInsertRows(Dataset ds, int nbRows, int idBefore){
       List<FitexProperty> attribute = FitexLog.logInsertRows(ds, locale, nbRows, idBefore);
       action.logAction(DataConstants.LOG_TYPE_INSERT_ROWS, attribute);
    }

    /* log: addrow */
    public void logAddRow(Dataset ds, DataSetRow row){
        List<FitexProperty> attribute = FitexLog.logAddRow(ds, locale,row);
        action.logAction(DataConstants.LOG_ADD_ROW, attribute);
    }

    /* log: initialize header */
    public void logInitializeHeader(Dataset ds){
        List<FitexProperty> attribute = FitexLog.logInitializeHeader(ds, locale);
        action.logAction(DataConstants.LOG_INITIALIZE_HEADER, attribute);
    }

    /* log: delete datas*/
    public void logDeleteDatas(Dataset ds, ArrayList<Data> listData,  ArrayList<Integer> listNoDataRow, ArrayList<Integer> listNoDataCol,ArrayList<DataOperation> listOperation){
        List<FitexProperty> attribute = FitexLog.logDeleteData(ds, locale, listData,listNoDataRow, listNoDataCol,listOperation );
        action.logAction(DataConstants.LOG_TYPE_DELETE, attribute);
    }


    /* log: add operation */
    public void logAddOperation(Dataset ds, DataOperation operation){
        List<FitexProperty> attribute = FitexLog.logAddOperation(ds, operation);
        action.logAction(DataConstants.LOG_TYPE_ADD_OPERATION, attribute);
    }
    /* log: edit data header*/
    public void logEditHeader(Dataset ds, DataHeader oldHeader, DataHeader newHeader){
        List<FitexProperty> attribute = FitexLog.logEditHeader(ds, locale, oldHeader, newHeader);
        action.logAction(DataConstants.LOG_TYPE_EDIT_HEADER, attribute);
    }

    /* log: ignore data */
    public void logIgnoreData(Dataset ds, boolean isIgnored, ArrayList<Data> listData){
        List<FitexProperty> attribute = FitexLog.logIgnoreData(ds, locale, isIgnored, listData);
        action.logAction(DataConstants.LOG_TYPE_IGNORE_DATA, attribute);
    }

    /* log: create Visualization */
    public void logCreateVisualization(Dataset ds, Visualization vis){
        List<FitexProperty> attribute = FitexLog.logCreateVisualization(ds, locale, vis);
        action.logAction(DataConstants.LOG_TYPE_CREATE_VISUALIZATION, attribute);
    }

    /* log: delete visualization */
    public void logDeleteVisualization(Dataset ds, Visualization vis){
        List<FitexProperty> attribute = FitexLog.logDeleteVisualization(ds, locale, vis);
        action.logAction(DataConstants.LOG_TYPE_DELETE_VISUALIZATION, attribute);
    }

    /* log: graph mode */
    public void logGraphMode(Dataset ds, Visualization vis, char graphMode){
        List<FitexProperty> attribute = FitexLog.logGraphMode(ds, vis, graphMode);
        action.logAction(DataConstants.LOG_TYPE_GRAPH_MODE, attribute);
    }

    /* log: update graph param */
    public void logUpdateGraphParam(Dataset ds, String oldName, Visualization newVis){
        List<FitexProperty> attribute = FitexLog.logUpdateGraphParam(ds, oldName, newVis);
        action.logAction(DataConstants.LOG_TYPE_UPDATE_VISUALIZATION, attribute);
    }

    /* sort dataset, list title / 0-1: croissant/decroissant  */
    public void logSortDataset(Dataset ds, List<Object[]> elementToSort){
        List<FitexProperty> attribute = FitexLog.logSortDataset(ds, elementToSort);
        action.logAction(DataConstants.LOG_TYPE_SORT_DATASET, attribute);
    }

    /* log: copy dataset */
    public void logCopy(Dataset ds, ArrayList<int[]> listSelCell){
        List<FitexProperty> attribute = FitexLog.logCopy(ds, listSelCell);
        action.logAction(DataConstants.LOG_TYPE_COPY, attribute);
    }

    /* log: paste*/
    public void logPaste(Dataset ds, int[] selCell, CopyDataset copyDs){
        List<FitexProperty> attribute = FitexLog.logPaste(ds, locale, selCell, copyDs);
        action.logAction(DataConstants.LOG_TYPE_PASTE, attribute);
    }

    /* log: cut dataset */
    public void logCut(Dataset ds){
        action.logAction(DataConstants.LOG_TYPE_CUT, new LinkedList());
    }
    
    /* log: undo*/
    public void logUndo(Dataset ds, DataUndoRedo undoAction){
        List<FitexProperty> attribute = FitexLog.logUndoRedo(ds, locale,  undoAction);
        action.logAction(DataConstants.LOG_TYPE_UNDO, attribute);
    }

    /* log: undo*/
    public void logRedo(Dataset ds, DataUndoRedo redoAction){
        List<FitexProperty> attribute = FitexLog.logUndoRedo(ds, locale,  redoAction);
        action.logAction(DataConstants.LOG_TYPE_REDO, attribute);
    }

    /* log: function model */
    public void logFunctionModel(Dataset ds, Graph graph, String description,  Color fColor, ArrayList<FunctionParam> listParam){
        List<FitexProperty> attribute = FitexLog.logFunctionModel(ds, locale, graph, description, fColor, listParam);
        action.logAction(DataConstants.LOG_TYPE_GRAPH_FUNCTION, attribute);
    }

    /* log: rename dataset */
    public void logRenameDataset(String oldName, String newName){
        List<FitexProperty> attribute = FitexLog.logRenameDataset(oldName, newName);
        action.logAction(DataConstants.LOG_TYPE_RENAME_DATASET, attribute);
    }

    /* log: save dataset */
    public void logSaveDataset(Dataset ds){
        List<FitexProperty> attribute = FitexLog.logSaveDataset(ds);
        action.logAction(DataConstants.LOG_TYPE_SAVE_DATASET, attribute);
    }

    /* log: csv export */
    public void logExportCSV(Dataset ds, String fileName){
        List<FitexProperty> attribute = FitexLog.logExportCsv(ds, fileName);
        action.logAction(DataConstants.LOG_TYPE_EXPORT_CSV_FILE, attribute);
    }

    /* log: new elo */
    public void logNewElo(){
        action.logAction(DataConstants.LOG_TYPE_NEW, new LinkedList());
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

    /* import a gmbli file => get the dataset elo */
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
}
