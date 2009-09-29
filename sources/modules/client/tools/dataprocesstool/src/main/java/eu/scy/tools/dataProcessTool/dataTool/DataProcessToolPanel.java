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

import eu.scy.tools.dataProcessTool.common.*;
import eu.scy.tools.dataProcessTool.controller.ControllerInterface;
import eu.scy.tools.dataProcessTool.controller.DataController;
import eu.scy.tools.dataProcessTool.controller.DataControllerDB;
import eu.scy.tools.dataProcessTool.utilities.ActionDataProcessTool;
import eu.scy.tools.dataProcessTool.utilities.CopexReturn;
import eu.scy.tools.dataProcessTool.utilities.MyUtilities;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
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
    public final static Color backgroundColor = SystemColor.control;
    /* width */
    public static final int PANEL_WIDTH = 530;
    /* height */
    public static final int PANEL_HEIGHT = 330;

    
    
    /* MODE DEBUG */
    public static final boolean DEBUG_MODE = false;

    public static final int MENU_BAR_HEIGHT = 28;
    public static final int ICON_HEIGHT=31;
    public static final int ICON_WIDTH = 160;
    //PROPERTY
    /* locale */
    private Locale locale ;
    /* ressource bundle */
    private ResourceBundle bundle;
    /* version */
    private String version = "3.0";

    // NOYAU
    private boolean scyMode;
    private boolean dbMode;
    /* interface noyau */
    private ControllerInterface controller;
    private long dbKeyUser;
    private long dbKeyMission;
    private String userName;
    private String firstName;

    // Donnees
    /* liste des donnees */
    private ArrayList<Dataset> listDataset;
    private ArrayList<FitexToolPanel> listFitexPanel;
    private FitexToolPanel activFitex;
    /*tableau des differents types de visualisation */
    private TypeVisualization[] tabTypeVis;
    /* tableau des differentes operations possibles */
    private TypeOperation[] tabTypeOp;


    private ActionDataProcessTool action;

    private FitexTabbedPane fitexTabbedPane;
    private File lastUsedFileOpen = null;
    private File lastUsedFileImport = null;
    private File lastUsedFileMerge = null;
    private XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
    private transient SAXBuilder builder = new SAXBuilder(false);

    public DataProcessToolPanel(boolean scyMode) {
        super();
        this.scyMode = scyMode;
        this.dbMode = false;
        initComponents();
        initData(null);
        this.dbKeyMission = 1;
        this.dbKeyUser = 1;
        this.firstName = "";
        this.userName = "";
    }

    public DataProcessToolPanel(URL url, long dbKeyMission, long dbKeyUser) {
        super();
        this.scyMode = false;
        this.dbMode = true;
        this.dbKeyMission = dbKeyMission;
        this.dbKeyUser = dbKeyUser;
        initComponents();
        initData(url);
    }
    

    private void initData(URL url){
        // i18n
        locale = Locale.getDefault();
        locale = new Locale("en", "GB");
        try{
            this.bundle = ResourceBundle.getBundle("DataToolBundle" , locale);
        }catch(MissingResourceException e){
          try{
              // par defaut on prend l'anglais
              locale = new Locale("en", "GB");
              bundle = ResourceBundle.getBundle("DataToolBundle", locale);
          }catch (MissingResourceException e2){
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            System.out.println("ERREUR lors du chargement de l'applet, la langue specifiee "+locale+" n'existe pas : "+e2);
            displayError(new CopexReturn("ERREUR lors du chargement de l'application : "+e, false), "ERROR LANGUAGE");
            return;
            }
        }
        
        // noyau
        if(url == null){
            this.controller = new DataController(this);
        }else{
            this.controller = new DataControllerDB(this, url, dbKeyMission, dbKeyUser);
        }

        initGUI();
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

    }


    /**
    * Instancie l'objet ActionDataProcessTool.
    * @param action ActionDataProcessTool
    */
    public void addActionCopexButton(ActionDataProcessTool action){
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
          System.out.println("erreur chargement des donnees ....");
          displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
      }
      setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }


    @Override
    public Locale getLocale(){
        return this.locale;
    }
    /* retourne un message selon cle*/
    public String getBundleString(String key){
       String s = "";
        try{
            s = this.bundle.getString(key);
        }catch(Exception e){
            System.out.println("getBundleString "+e);
            try{
                String msg = this.bundle.getString("ERROR_KEY");
                msg = MyUtilities.replace(msg, 0, key);
                displayError(new CopexReturn(msg, false) , this.bundle.getString("TITLE_DIALOG_ERROR"));
            }catch(Exception e2){
                displayError(new CopexReturn("No message found !"+key, false) ,"ERROR");
             }
        }
        return s;

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
        ImageIcon imgIcon = new ImageIcon(getClass().getResource( "/" +img));
        if (imgIcon == null){
            displayError(new CopexReturn(getBundleString("MSG_ERROR_IMAGE")+img, false), getBundleString("TITLE_DIALOG_ERROR"));
            return null;
        }else
            return imgIcon;
    }


    /* initialisation de l'applet */
    protected void initGUI(){
        // Initialisation du look and feel
        try{
            String myLookAndFeel=UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(myLookAndFeel);
        }catch(Exception e){
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            System.out.println("ERREUR dans l'initialisation du lookAndFeel : "+e) ;
            JOptionPane.showMessageDialog(this , "ERREUR ans l'initialisation du lookAndFeel : "+e, "ERROR",JOptionPane.ERROR_MESSAGE);
        }
        setMinimumSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setSize(PANEL_WIDTH, PANEL_HEIGHT);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        if(action != null)
            action.resizeDataToolPanel(getWidth(), getHeight());
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    public void initDataProcessingTool(TypeVisualization[] tabTypeVisual, TypeOperation[] tabTypeOp, ArrayList<Dataset> listDataset){
        this.listDataset = listDataset;
        this.tabTypeOp = tabTypeOp;
        this.tabTypeVis = tabTypeVisual;
        this.listFitexPanel = new ArrayList();
        if(scyMode)
            initDataset();
        else
            initTabbedPane();
    }

    private void initDataset(){
        if(listDataset.size() > 0){
            FitexToolPanel fitex = new FitexToolPanel(this, listDataset.get(0), controller, tabTypeVis, tabTypeOp);
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
        FitexToolPanel fitex = new FitexToolPanel(this, ds, controller, tabTypeVis, tabTypeOp);
        fitex.initDataProcessingTool(ds);
        activFitex = fitex;
        listFitexPanel.add(fitex);
        fitexTabbedPane.addTab(ds.getName(), fitex);
    }
     

    
    
    public void setDataset(Dataset ds){
        if(scyMode && activFitex != null)
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

    public boolean canPrint(){
        return !scyMode;
    }
    
    /* retourne le tooltiptext sur le bouton d'ouverture */
    public String getToolTipTextOpen(){
        return getBundleString("TOOLTIPTEXT_OPEN_DATASET");
    }

    public void openDialogAddDataset(){
        OpenFitexDialog openFitexDialog = new OpenFitexDialog(this, dbMode, lastUsedFileOpen, lastUsedFileImport, lastUsedFileMerge);
        openFitexDialog.addOpenFitexAction(this);
        openFitexDialog.setVisible(true);
    }

    public void openDialogCloseDataset(Dataset ds) {
        if(dbMode){
            CloseDatasetDialog closeFitexDialog = new CloseDatasetDialog(this, ds);
            closeFitexDialog.setVisible(true);
        }else{
            int ok = JOptionPane.showConfirmDialog(this, this.getBundleString("MESSAGE_DATASET_CLOSE"), this.getBundleString("TITLE_DIALOG_EXIT"),JOptionPane.OK_CANCEL_OPTION );
            if(ok == JOptionPane.OK_OPTION){
                fitexTabbedPane.removeDataset(ds);
            }
        }
    }


    public boolean  closeDataset(Dataset dataset){
        CopexReturn cr = this.controller.closeDataset(dataset);
        if(cr.isError()){
           displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
           return false;
        }
        fitexTabbedPane.removeDataset(dataset);
        return true;
    }

    public boolean removeDataset(Dataset dataset){
        CopexReturn cr = this.controller.deleteDataset(dataset);
        if(cr.isError()){
           displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
           return false;
        }
        fitexTabbedPane.removeDataset(dataset);
        return true;
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
        cr = this.controller.createDefaultDataset("Dataset", v);
        if(cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        Dataset ds = (Dataset)v.get(0) ;
        this.setDataset(ds);
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
        this.setDataset(ds);
    }


    // load/open ELO SCY ou non
    public void loadELO(String xmlContent){
        if(scyMode){
            CopexReturn cr = this.controller.deleteDataset(activFitex.getDataset());
            if(cr.isError()){
                displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
        }
        CopexReturn cr = this.controller.loadELO(xmlContent);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
        }
     }

    //load/openELO
    @Override
    public void openELO(File file){
        InputStreamReader fileReader = null;
        try{
            lastUsedFileOpen = file;
            fileReader = new InputStreamReader(new FileInputStream(file), "utf-8");
            Document doc = builder.build(fileReader, file.getAbsolutePath());
            loadELO(new JDomStringConversion().xmlToString(doc.getRootElement()));
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

    //load/openELO
    // TODO
    @Override
    public void openDataset(Mission m, Dataset ds ){

    }


    // IMPORT CSV SCY
    /* lecture de fichier cvs => elo ds */
    public eu.scy.elo.contenttype.dataset.DataSet importCSVFile(File file){
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.importCSVFile(file, v);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return null;
        }
        eu.scy.elo.contenttype.dataset.DataSet elo = (eu.scy.elo.contenttype.dataset.DataSet)v.get(0);
        return elo;
    }

    @Override
    public void importELO(File file) {
        lastUsedFileImport = file;
        eu.scy.elo.contenttype.dataset.DataSet dsElo = importCSVFile(file);
        if(dsElo != null && !scyMode){
            loadELO(new JDomStringConversion().xmlToString(dsElo.toXML()));
        }
    }

    // merge SCY ou autre
    public void mergeELO(Element elo){
        this.activFitex.mergeELO(elo);
    }

    @Override
    public void mergeELO(File file){
        lastUsedFileMerge = file;
        InputStreamReader fileReader = null;
		try{
            fileReader = new InputStreamReader(new FileInputStream(file), "utf-8");
            Document doc = builder.build(fileReader, file.getAbsolutePath());
            mergeELO(doc.getRootElement());
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
    //TODO
    @Override
    public void mergeDataset(Mission mission, Dataset ds) {
        
    }


    
}
