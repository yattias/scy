/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.main;

import eu.scy.client.tools.copex.common.CopexAction;
import eu.scy.client.tools.copex.common.CopexMission;
import eu.scy.client.tools.copex.common.CopexTask;
import eu.scy.client.tools.copex.common.Evaluation;
import eu.scy.client.tools.copex.common.GeneralPrinciple;
import eu.scy.client.tools.copex.common.Hypothesis;
import eu.scy.client.tools.copex.common.LearnerProcedure;
import eu.scy.client.tools.copex.common.MaterialUsed;
import eu.scy.client.tools.copex.common.PhysicalQuantity;
import eu.scy.client.tools.copex.common.Question;
import eu.scy.client.tools.copex.common.Step;
import eu.scy.client.tools.copex.controller.ControllerInterface;
import eu.scy.client.tools.copex.controller.CopexController;
import eu.scy.client.tools.copex.controller.CopexControllerDB;
import eu.scy.client.tools.copex.logger.CopexLog;
import eu.scy.client.tools.copex.logger.CopexProperty;
import eu.scy.client.tools.copex.logger.TaskTreePosition;
import eu.scy.client.tools.copex.utilities.ActionCopex;
import eu.scy.client.tools.copex.utilities.CopexReturn;
import eu.scy.client.tools.copex.utilities.MyConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.SystemColor;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
//import java.util.MissingResourceException;
//import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import eu.scy.client.common.scyi18n.ResourceBundleWrapper;
import eu.scy.client.tools.copex.collaboration.CopexSyncManager;
import eu.scy.client.tools.copex.common.CopexTeacher;
import eu.scy.client.tools.copex.common.ExperimentalProcedure;
import eu.scy.client.tools.copex.common.InitialProcedure;
import eu.scy.client.tools.copex.common.Material;
import eu.scy.client.tools.copex.common.MaterialStrategy;
import eu.scy.client.tools.copex.common.TypeMaterial;
import eu.scy.client.tools.copex.controller.CopexControllerAuth;
import eu.scy.client.tools.copex.controller.CopexControllerLabBook;
import eu.scy.client.tools.copex.db.DataBaseCommunication;
import eu.scy.client.tools.copex.edp.CopexSwingWorker;
import eu.scy.client.tools.copex.edp.CopexTabbedPane;
import eu.scy.client.tools.copex.edp.CreateProcDialog;
import eu.scy.client.tools.copex.edp.EdPPanel;
import eu.scy.client.tools.copex.edp.HelpDialog;
import eu.scy.client.tools.copex.edp.OpenCopexDialog;
import eu.scy.client.tools.copex.edp.TaskSelected;
import eu.scy.client.tools.copex.utilities.CopexUtilities;
import eu.scy.common.datasync.ISyncObject;
import java.awt.Container;
import java.beans.PropertyChangeListener;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 * main panel COPEX
 * @author Marjolaine
 */
public class CopexPanel extends JPanel implements PropertyChangeListener{
    /*panel size*/
    public final static int PANEL_WIDTH = 550;
    public final static int PANEL_HEIGHT = 350;

    /* version */
    private String version = "2.5";
    protected ControllerInterface controller;
    public final static Color backgroundColor = SystemColor.control;
    /* locale */
    private Locale locale ;
    /* ressource bundle */
    //private ResourceBundle bundle;
    private ResourceBundleWrapper bundle;
    //private ResourceBundleWrapper bundle;
    private boolean scyMode;
    private boolean dbMode;
    private ActionCopex actionCopex;
    /* user identifier */
    private String idUser;
    /* mission identifier */
    private long dbKeyMission;
    private long dbKeyGroup;
    private long dbKeyLabDoc;
    private String labDocName;
    private CopexMission mission;
    // list of proc open
    private ArrayList<ExperimentalProcedure> listProc = null;
    private ArrayList<EdPPanel> listCopexPanel;
    private EdPPanel activCopex;
    /* list of physical quantitites  */
    private ArrayList<PhysicalQuantity> listPhysicalQuantity ;

    private File lastUsedFileOpen = null;
    private transient SAXBuilder builder = new SAXBuilder(false);
    
    private CopexTabbedPane copexTabbedPane;
    private JFrame ownerFrame;

    private JProgressBar progressBar;
    private JPanel paneProgess;

    /* sync. maanger*/
    private CopexSyncManager copexSync ;

    public CopexPanel(JFrame ownerFrame,boolean scyMode, Locale locale){
        this.ownerFrame = ownerFrame;
        this.locale = locale;
        this.idUser = "1";
        this.dbKeyMission = 1;
        this.dbKeyGroup = 1;
        this.dbKeyLabDoc = 1;
        this.labDocName = "";
        this.scyMode = scyMode;
        this.dbMode = false;
        initEdP(null);
    }

    public CopexPanel(boolean scyMode, Locale locale){
        this.ownerFrame = null;
        this.locale = locale;
        this.idUser = "1";
        this.dbKeyMission = 1;
        this.dbKeyGroup = 1;
        this.dbKeyLabDoc = 1;
        this.labDocName = "";
        this.scyMode = scyMode;
        this.dbMode = false;
        initEdP(null);
    }


    public CopexPanel(JFrame ownerFrame,Locale locale, URL copexURL, String idUser, long dbKeyMission, long dbKeyGroup, long dbKeyLabDoc, String labDocName) {
        this.ownerFrame = ownerFrame;
        this.locale = locale;
        this.idUser = idUser;
        this.dbKeyMission = dbKeyMission;
        this.dbKeyGroup = dbKeyGroup;
        this.dbKeyLabDoc = dbKeyLabDoc;
        this.scyMode = false;
        this.dbMode = true;
        this.labDocName = labDocName;
        initEdP(copexURL);
    }

    public CopexPanel(Locale locale, CopexTeacher teacher, CopexMission mission, InitialProcedure initProc,  ArrayList<PhysicalQuantity> listPhysicalQuantity, ArrayList<MaterialStrategy> listMaterialStrategy, TypeMaterial defaultTypeMaterial){
        this.ownerFrame = null;
        this.locale = locale;
        this.idUser = Long.toString(teacher.getDbKey());
        this.dbKeyMission = mission.getDbKey();
        this.dbKeyGroup = 0;
        this.dbKeyLabDoc = 0;
        this.labDocName = "";
        this.scyMode = true;
        this.dbMode = false;
        initEdP(teacher, mission, initProc, listPhysicalQuantity, listMaterialStrategy, defaultTypeMaterial, null);
    }

    // initEdp for copexAuthoring
    private void initEdP(CopexTeacher teacher, CopexMission mission, InitialProcedure initProc,  ArrayList<PhysicalQuantity> listPhysicalQuantity, ArrayList<MaterialStrategy> listMaterialStrategy, TypeMaterial defaultTypeMaterial, DataBaseCommunication dbC){
        bundle = new ResourceBundleWrapper(this);
        setSize(PANEL_WIDTH, PANEL_HEIGHT);
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        if(dbC == null){
            this.controller = new CopexControllerAuth(this, listPhysicalQuantity, listMaterialStrategy, defaultTypeMaterial, initProc, teacher, mission);
        }else{
            this.controller = new CopexControllerAuth(this, listPhysicalQuantity, listMaterialStrategy, defaultTypeMaterial, initProc, teacher, mission, dbC);
        }
        setMinimumSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setSize(PANEL_WIDTH, PANEL_HEIGHT);
        setLayout(new BorderLayout());
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }


    public void initEdP(URL url){
//        // i18n
//        locale = Locale.getDefault();
//        locale = new Locale("en", "GB");
//        //locale = new Locale("fr", "FR");
//        try{
//            this.bundle = ResourceBundle.getBundle("languages/copex", locale);
//        }catch(MissingResourceException e){
//          try{
//              // by default, english language
//              locale = new Locale("en");
//              bundle = ResourceBundle.getBundle("languages/copex", locale);
//          }catch (MissingResourceException e2){
//            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
//            System.out.println("Error while loading COPEX, the specified language "+locale+" does not exist: "+e2);
//            displayError(new CopexReturn("Error while loading COPEX: "+e2, false), "ERROR LANGUAGE");
//            return;
//            }
//        }
        bundle = new ResourceBundleWrapper(this);
       // Initialization look and feel
//        try{
//            String myLookAndFeel=UIManager.getSystemLookAndFeelClassName();
//            UIManager.setLookAndFeel(myLookAndFeel);
//        }catch(Exception e){
//            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
//            System.out.println("ERREUR dans l'initialisation du lookAndFeel : "+e) ;
//            JOptionPane.showMessageDialog(this , "ERREUR ans l'initialisation du lookAndFeel : "+e, "ERROR",JOptionPane.ERROR_MESSAGE);
//        }
      setSize(PANEL_WIDTH, PANEL_HEIGHT);
      setCursor(new Cursor(Cursor.WAIT_CURSOR));
      if(url != null){
          //this.controller = new CopexControllerDB(this, url);
          this.controller = new CopexControllerLabBook(this, url);
      }else
          this.controller = new CopexController(this) ;
      
      setMinimumSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
      setSize(PANEL_WIDTH, PANEL_HEIGHT);
      setLayout(new BorderLayout());
      setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
      if(!scyMode){
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        paneProgess = new JPanel();
        paneProgess.setLayout(new BorderLayout());
        paneProgess.setOpaque(true);
        paneProgess.add(progressBar, BorderLayout.CENTER);
        JLabel labelprogess = new JLabel("Veuillez patienter pendant le chargement....");
        paneProgess.add(labelprogess, BorderLayout.PAGE_END);
        this.add(paneProgess, BorderLayout.PAGE_START);
        CopexSwingWorker task = new CopexSwingWorker(this);
        task.addPropertyChangeListener(this);
        task.execute();
        }else{
          loadData();
        }
    }

    public void setProgress(int value){
        this.progressBar.setValue(value);
    }

    @Override
    public Locale getLocale(){
        return this.locale;
    }

    public void addActionCopex(ActionCopex actionCopex){
        this.actionCopex = actionCopex;
    }


    /* load data */
    public void loadData(){
      setCursor(new Cursor(Cursor.WAIT_CURSOR));
      
      //String logFileName = "logFile"+CopexUtilities.getCurrentDate()+"-"+dbKeyMission+"-"+idUser+".xml";
      //String fileMission = "copexMission_SCI121.xml";
      //String fileMission = "copexMission_simple.xml";
      //String fileMission = "copexMission_TEA_FR.xml";
      //String fileMission = "copexMission_TEA_JP.xml";
      String fileMission = "copexMission_SCY1_CO2.xml";
      //String fileMission = "copexMission_PKA.xml";
      CopexReturn cr = this.controller.initEdP(locale, idUser, dbKeyMission,dbKeyGroup, dbKeyLabDoc, labDocName, fileMission);
      if (cr.isError()){
          setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
          displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
          this.stop();
      }
      if(!scyMode){
        progressBar.setValue(100);
        remove(paneProgess);
        revalidate();
      }
      setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    
    public String getVersion(){
        return this.version;
    }

    public Image getIconDialog(){
        return getCopexImage("labbook.png").getImage();
    }

    public JFrame getOwnerFrame(){
        return this.ownerFrame;
    }
    
    public  ImageIcon getCopexImage(String img){
        ImageIcon imgIcon = new ImageIcon(getClass().getResource("/Images/" +img));
        if (imgIcon == null){
            displayError(new CopexReturn(getBundleString("MSG_ERROR_IMAGE")+img, false), getBundleString("TITLE_DIALOG_ERROR"));
            return null;
        }else
            return imgIcon;
    }

    /* display errors */
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

    /* returns the string for the specified key */
    public String getBundleString(String key){
        String s = getBundleStringKey(key);
//        if(s == null){
//            try{
//                String msg = this.bundle.getString("ERROR_KEY");
//                msg = CopexUtilities.replace(msg, 0, key);
//                displayError(new CopexReturn(msg, false) , this.bundle.getString("TITLE_DIALOG_ERROR"));
//            }catch(Exception e2){
//                System.out.println("No message found : "+key);
//                displayError(new CopexReturn("No message found !", false) ,"ERROR");
//             }
//        }
        return s;
    }

    /* returns the string for the specified key*/
    public String getBundleStringKey(String key){
        String s = "";
        try{
            //s = this.bundle.getString(key);
            s = this.bundle.getString("COPEX."+key);
            return s;
        }catch(Exception e){
            return null;
        }
    }

    
     public void stop() {
        CopexReturn cr = this.controller.stopEdP();
        if (cr.isError())
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
    }

    
      /* initialization of the application, with data */
    public void initEdp(CopexMission mission, ArrayList<ExperimentalProcedure> listProc,  ArrayList<PhysicalQuantity> listPhysicalQuantity) {
       setCursor(new Cursor(Cursor.WAIT_CURSOR));
       // update
       this.mission = mission;
       this.listProc = listProc;
       this.listPhysicalQuantity = listPhysicalQuantity ;
       listCopexPanel = new ArrayList();
       //ToolTipManager.sharedInstance().setDismissDelay(10000);
       //ToolTipManager.sharedInstance().setInitialDelay(0);
       if(scyMode){
           initProcedure();
       }else{
           initTabbedPane();
       }
    }
    private void initProcedure(){
        if(listProc.size() > 0){
            EdPPanel copex = new EdPPanel(this, listProc.get(0), controller, listPhysicalQuantity);
            activCopex = copex;
            listCopexPanel.add(copex);
            this.add(copex, BorderLayout.CENTER);
        }
    }

    private void initTabbedPane(){
        this.add(getCopexTabbedPane(), BorderLayout.CENTER);
        int nb = listProc.size();
        for (int i=0; i<nb; i++){
            addCopexPanel(listProc.get(i), false);
        }
    }

    private CopexTabbedPane getCopexTabbedPane(){
        if(copexTabbedPane == null){
            copexTabbedPane = new CopexTabbedPane(this);
        }
        return copexTabbedPane;
    }

    private void addCopexPanel(ExperimentalProcedure proc, boolean addProc){
        EdPPanel copex = new EdPPanel(this, proc, controller,  listPhysicalQuantity);
        activCopex = copex;
        listCopexPanel.add(copex);
        if(addProc)
            listProc.add(proc);
        if(scyMode){
            this.add(copex, BorderLayout.CENTER);
        }else{
            copexTabbedPane.addTab(proc.getName(getLocale()), copex);
        }
    }

    /* returns true in the msision, we can add a proc */
    public boolean canAddProc(){
        if(mission == null)
            return true;
        return true;
    }

    /* returns the  tooltiptext on th eopen button  */
    public String getToolTipTextOpen(){
        return getBundleString("TOOLTIPTEXT_OPEN_PROC");
    }

    public void setQuestionDialog(){
        if(activCopex != null)
            activCopex.setQuestionDialog();
    }

    /* returns the index of  a procedure */
    private int getIdProc(long dbKey){
        int id = -1;
        int nbP = listProc.size();
        for (int i=0; i<nbP; i++){
            if (listProc.get(i).getDbKey() == dbKey)
                return i;
        }
        return id;
    }

    /*close proc */
    public void closeProc(ExperimentalProcedure proc) {
        int idP = getIdProc(proc.getDbKey());
        if (idP == -1){
            displayError(new CopexReturn(getBundleString("MSG_ERROR_CLOSE_PROC"), false), getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        listProc.remove(idP);
        listCopexPanel.remove(idP);
        if(scyMode){
            remove(activCopex);
        }else{
            getCopexTabbedPane().removeProc(proc);
        }
    }

    /* returns the activ proc*/
    public ExperimentalProcedure getProcActiv() {
        return this.getCopexTabbedPane().getProcActiv();
    }
    
    /* returns true if any proc is open*/
     public boolean noProc(){
         return listProc.isEmpty();
     }


     /* show the help proc */
    public void displayHelpProc(LearnerProcedure helpProc){
        if(scyMode){
//            // it could be a new elo?
            EdPPanel helpPanel = new EdPPanel(this, helpProc, controller,  listPhysicalQuantity);
            HelpDialog help = new HelpDialog(activCopex, helpPanel);
            help.setVisible(true);
        }else{
            if (getIdProc(helpProc.getDbKey()) == -1)
                addCopexPanel(helpProc, true);
            else
                getCopexTabbedPane().setSelected(helpProc);
        }
    }


   /* display a msg about the locked proc */
    public void displayProcLocked(ArrayList<String> listProcNameLocked){
        if(listProcNameLocked == null)
            return;
        int nb = listProcNameLocked.size();
        if (nb == 0)
            return;
        String msg = getBundleString("MSG_WARNING_PROC_LOCKED");
        for (int i=0; i<nb; i++){
            msg += " \n"+listProcNameLocked.get(i);
        }
        displayError(new CopexReturn(msg, true), getBundleString("TITLE_DIALOG_WARNING"));
    }

    /* ask which initial proc for creation of a new proc  */
    public void askForInitialProc(){
        CreateProcDialog dialog = new CreateProcDialog(this, controller, mission.getListInitialProc()) ;
        dialog.setVisible(true);
    }

    /* returns the point to display the dialog */
    public Point getLocationDialog(){
        if(activCopex == null)
            return new Point(10, 10);
        return this.activCopex.getLocationDialog();
    }

    

    public void createProc(ExperimentalProcedure proc){
        addCopexPanel(proc, true);
    }
    
    public void reloadProc(ExperimentalProcedure proc){
        if(activCopex != null)
            this.remove(activCopex);
        listCopexPanel = new ArrayList();
        activCopex = null;
        listProc = new ArrayList();
        if(proc != null){
            createProc(proc);
        }
    }

    public void updateProc(ExperimentalProcedure proc){
        int id = getIdProc(proc.getDbKey());
        if(id != -1){
            listProc.set(id, proc);
            listCopexPanel.get(id).updateProc(proc);
        }
    }

    public void updateProc(LearnerProcedure proc, boolean update){
        int id = getIdProc(proc.getDbKey());
        if(id != -1){
            listProc.set(id, proc);
            listCopexPanel.get(id).updateProc(proc, update);
        }
    }
    
    

    public void deleteProc(LearnerProcedure proc){
        closeProc(proc);
    }
    public void updateProcName(LearnerProcedure proc, String name){
        int id = getIdProc(proc.getDbKey());
        if(id != -1){
            listProc.set(id, proc);
            listCopexPanel.get(id).updateProcName(name);
        }
        copexTabbedPane.updateProcName(proc, name);
    }

    

    public void paste(LearnerProcedure proc, ArrayList<CopexTask> listTask, TaskSelected t, char undoRedo) {
        int id = getIdProc(proc.getDbKey());
        if(id != -1){
            listProc.set(id, proc);
            listCopexPanel.get(id).paste(listTask, t, undoRedo);
        }
    }

    public boolean isMission(){
        return dbMode;
    }

    public boolean canSave(){
        return !scyMode && !dbMode;
    }

    public boolean canPrint(){
        return !scyMode;
    }
    public void openDialogCloseProc(ExperimentalProcedure proc) {
        if(dbMode){
//            CloseProcDialog closeD = new CloseProcDialog(this, controller, proc);
//            closeD.setVisible(true);
            // close proc
            CopexReturn cr = controller.closeProc(proc);
            if(cr.isError()){
                displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            }
        }else{
            if(hasModification()){
                int ok = JOptionPane.showConfirmDialog(this, this.getBundleString("MSG_CLOSE_PROC"), this.getBundleString("TITLE_DIALOG_EXIT"),JOptionPane.OK_CANCEL_OPTION );
                if(ok == JOptionPane.OK_OPTION){
                    CopexReturn cr = controller.closeProc(proc);
                    if (cr.isError()){
                        displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
                    }
                }
            }else{
                CopexReturn cr = controller.closeProc(proc);
                if (cr.isError()){
                    displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
                }
            }
        }
    }

    public void openDialogaddProc(){
        if(dbMode){
            // list of proc to copy
            ArrayList v = new ArrayList();
            CopexReturn cr = this.controller.getListProcToCopyOrOpen(v);
            if (cr.isError()){
                displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            ArrayList<LearnerProcedure> listProcOn = (ArrayList)v.get(0);
            ArrayList<CopexMission> listMission = (ArrayList) v.get(1);
            ArrayList<ArrayList<LearnerProcedure>> listProcMission = (ArrayList<ArrayList<LearnerProcedure>>) v.get(2);
            OpenCopexDialog openDialog = new OpenCopexDialog(this, controller, listMission, listProcMission, mission.getListInitialProc());
            openDialog.setVisible(true);
        }else{
            OpenCopexDialog openDialog = new OpenCopexDialog(this,  controller, lastUsedFileOpen, mission.getListInitialProc());
            openDialog.setVisible(true);
        }
    }

    //load/openELO
    public void openProc(File file){
        InputStreamReader fileReader = null;
        try{
            lastUsedFileOpen = file;
            fileReader = new InputStreamReader(new FileInputStream(file), "utf-8");
            Document doc = builder.build(fileReader, file.getAbsolutePath());
            loadELO(doc.getRootElement());
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

    

    // load/open ELO SCY or not
    public void loadELO(Element elo){
        if(scyMode){
            CopexReturn cr = this.controller.deleteProc(activCopex.getExperimentalProc());
            if(cr.isError()){
                displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
        }
        CopexReturn cr = this.controller.loadELO(elo);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
        }
     }

    
    // new ELO SCY
    public void newELO(){
        // scyMode is true
        CopexReturn cr = this.controller.deleteProc(activCopex.getExperimentalProc());
        if(cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        ArrayList v = new ArrayList();
        cr = this.controller.newELO();
        if(cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
    }

    // SCY : returns the current elo
    public Element getXProc(){
        logSaveProc(activCopex.getExperimentalProc());
        return activCopex.getExperimentalProcedure();
    }

    
    private boolean hasModification(){
        return activCopex.hasModification();
    }

    /* log : start tool */
    public void logStartTool(){
        if(!scyMode)
            actionCopex.logAction(MyConstants.LOG_TYPE_START_TOOL, new LinkedList());
    }
     /* log : end tool */
    public void logEndTool(){
        if(!scyMode)
            actionCopex.logAction(MyConstants.LOG_TYPE_END_TOOL, new LinkedList());
    }
     /* log : open help */
    public void logOpenHelp(){
        actionCopex.logAction(MyConstants.LOG_TYPE_OPEN_HELP, new LinkedList());
    }
     /* log : close help */
    public void logCloseHelp(){
        actionCopex.logAction(MyConstants.LOG_TYPE_CLOSE_HELP, new LinkedList());
    }
    /* log : open help proc */
    public void logOpenHelpProc(){
        actionCopex.logAction(MyConstants.LOG_TYPE_OPEN_HELP_PROC, new LinkedList());
    }
    /* log : close help proc */
    public void logCloseHelpProc(){
        actionCopex.logAction(MyConstants.LOG_TYPE_CLOSE_HELP_PROC, new LinkedList());
    }
     /* log: open proc */
    public void logOpenProc(LearnerProcedure proc, long dbKeyMission, String missionCode){
        List<CopexProperty> attribute = CopexLog.logOpenProc(locale,proc, dbKeyMission, missionCode);
        if(!scyMode)
            actionCopex.logAction(MyConstants.LOG_TYPE_OPEN_PROC, attribute);
    }

    /* log : save proc */
    public void logSaveProc(ExperimentalProcedure proc){
        List<CopexProperty> attribute = CopexLog.logProc(locale,proc);
        if(!scyMode)
            actionCopex.logAction(MyConstants.LOG_TYPE_SAVE_PROC, attribute);
    }
    /* log: delete proc */
    public void logDeleteProc(ExperimentalProcedure proc){
        List<CopexProperty> attribute = CopexLog.logProc(locale,proc);
        if(!scyMode)
            actionCopex.logAction(MyConstants.LOG_TYPE_DELETE_PROC, attribute);
    }

    /* log: rename proc */
    public void logRenameProc(ExperimentalProcedure proc, String oldName, String newName){
        List<CopexProperty> attribute = CopexLog.logRenameProc(proc, oldName, newName);
        actionCopex.logAction(MyConstants.LOG_TYPE_RENAME_PROC, attribute);
    }

    /* log : print proc */
    public void logPrintProc(ExperimentalProcedure proc){
        List<CopexProperty> attribute = CopexLog.logProc(locale,proc);
        actionCopex.logAction(MyConstants.LOG_TYPE_PRINT_PROC, attribute);
    }

    /* log: create proc */
    public void logCreateProc(ExperimentalProcedure proc){
        List<CopexProperty> attribute = CopexLog.logProc(locale,proc);
        if(!scyMode)
            actionCopex.logAction(MyConstants.LOG_TYPE_CREATE_PROC, attribute);
    }
    /* log: copy proc */
    public void logCopyProc(LearnerProcedure proc, LearnerProcedure procToCopy){
        List<CopexProperty> attribute = CopexLog.logCopyProc(locale,proc, procToCopy);
        actionCopex.logAction(MyConstants.LOG_TYPE_COPY_PROC, attribute);
    }

    /* log: update task */
    public void logUpdateQuestion(ExperimentalProcedure proc, Question oldQuestion, Question newQuestion){
        List<CopexProperty> attribute = CopexLog.logUpdateTask(locale,proc, oldQuestion, newQuestion);
        actionCopex.logAction(MyConstants.LOG_TYPE_UPDATE_QUESTION, attribute);
    }

    /* log: update step */
    public void logUpdateStep(ExperimentalProcedure proc, Step oldStep, Step newStep){
        List<CopexProperty> attribute = CopexLog.logUpdateTask(locale,proc, oldStep, newStep);
        actionCopex.logAction(MyConstants.LOG_TYPE_UPDATE_STEP, attribute);
    }

    /* log: update action */
    public void logUpdateAction(ExperimentalProcedure proc, CopexAction oldAction, CopexAction newAction){
        List<CopexProperty> attribute = CopexLog.logUpdateTask(locale,proc, oldAction, newAction);
        actionCopex.logAction(MyConstants.LOG_TYPE_UPDATE_ACTION, attribute);
    }

    /* log: add step*/
    public void logAddStep(ExperimentalProcedure proc, Step step,TaskTreePosition position){
        List<CopexProperty> attribute = CopexLog.logAddTask(locale,proc, step, position);
        actionCopex.logAction(MyConstants.LOG_TYPE_ADD_STEP, attribute);
    }

    /* log: add action*/
    public void logAddAction(ExperimentalProcedure proc, CopexAction action,TaskTreePosition position){
        List<CopexProperty> attribute = CopexLog.logAddTask(locale,proc, action, position);
        actionCopex.logAction(MyConstants.LOG_TYPE_ADD_STEP, attribute);
    }

    /* log : paste */
    public void logPaste(ExperimentalProcedure proc, ArrayList<CopexTask> listTask, List<TaskTreePosition> listPositionTask){
        List<CopexProperty> attribute = CopexLog.logListTask(locale,proc, listTask, listPositionTask);
        actionCopex.logAction(MyConstants.LOG_TYPE_PASTE, attribute);
    }

    /* log : copy */
    public void logCopy(ExperimentalProcedure proc, ArrayList<CopexTask> listTask, List<TaskTreePosition> listPositionTask){
        List<CopexProperty> attribute = CopexLog.logListTask(locale,proc, listTask, listPositionTask);
        actionCopex.logAction(MyConstants.LOG_TYPE_COPY, attribute);
    }

    /* log : suppr */
    public void logDelete(ExperimentalProcedure proc, ArrayList<CopexTask> listTask, List<TaskTreePosition> listPositionTask){
        List<CopexProperty> attribute = CopexLog.logListTask(locale,proc, listTask, listPositionTask);
        actionCopex.logAction(MyConstants.LOG_TYPE_DELETE, attribute);
    }

    /* log : cut */
    public void logCut(ExperimentalProcedure proc, ArrayList<CopexTask> listTask, List<TaskTreePosition> listPositionTask){
        List<CopexProperty> attribute = CopexLog.logListTask(locale,proc, listTask, listPositionTask);
        actionCopex.logAction(MyConstants.LOG_TYPE_CUT, attribute);
    }

    /* log : drag&drop */
    public void logDragDrop(ExperimentalProcedure proc, ArrayList<CopexTask> listTask, List<TaskTreePosition> listPositionTask, TaskTreePosition insertPosition){
        List<CopexProperty> attribute = CopexLog.logDragDrop(locale,proc, listTask, listPositionTask, insertPosition);
        actionCopex.logAction(MyConstants.LOG_TYPE_DRAG_DROP, attribute);
    }

    /* log : hypothesis */
    public void logHypothesis(ExperimentalProcedure proc, Hypothesis oldHypothesis, Hypothesis newHypothesis){
        List<CopexProperty> attribute = CopexLog.logHypothesis(locale,proc, oldHypothesis, newHypothesis);
        actionCopex.logAction(MyConstants.LOG_TYPE_HYPOTHESIS, attribute);
    }
    /* log : general principle */
    public void logGeneralPrinciple(ExperimentalProcedure proc, GeneralPrinciple oldPrinciple, GeneralPrinciple newGeneralPrinciple){
        List<CopexProperty> attribute = CopexLog.logGeneralPrinciple(locale,proc, oldPrinciple, newGeneralPrinciple);
        actionCopex.logAction(MyConstants.LOG_TYPE_GENERAL_PRINCIPLE, attribute);
    }
    /* log : evaluation */
    public void logEvaluation(ExperimentalProcedure proc, Evaluation oldEvaluation, Evaluation newEvaluation){
        List<CopexProperty> attribute = CopexLog.logEvaluation(locale,proc, oldEvaluation, newEvaluation);
        actionCopex.logAction(MyConstants.LOG_TYPE_EVALUATION, attribute);
    }

    /* log: create material used */
    public void logCreateMaterialUsed(ExperimentalProcedure proc, ArrayList<MaterialUsed> listMaterialUsed){
        List<CopexProperty> attribute = CopexLog.logMaterialUsed(locale,proc, listMaterialUsed);
        actionCopex.logAction(MyConstants.LOG_TYPE_CREATE_MATERIAL_USED, attribute);
    }

    /* log: delete material used */
    public void logDeleteMaterialUsed(ExperimentalProcedure proc, ArrayList<MaterialUsed> listMaterialUsed){
        List<CopexProperty> attribute = CopexLog.logMaterialUsed(locale,proc, listMaterialUsed);
        actionCopex.logAction(MyConstants.LOG_TYPE_DELETE_MATERIAL_USED, attribute);
    }
    /* log: update material used */
    public void logUpdateMaterialUsed(ExperimentalProcedure proc, ArrayList<MaterialUsed> oldListMaterialUsed, ArrayList<MaterialUsed> newListMaterialUsed){
        List<CopexProperty> attribute = CopexLog.logUpdateMaterialUsed(locale,proc, oldListMaterialUsed, newListMaterialUsed);
        actionCopex.logAction(MyConstants.LOG_TYPE_UPDATE_MATERIAL_USED, attribute);
    }

    /* log: redo rename proc */
    public void logRedoRenameProc(ExperimentalProcedure proc, String oldName, String newName){
        List<CopexProperty> attribute = CopexLog.logRenameProc(proc, oldName, newName);
        //actionCopex.logAction(MyConstants.LOG_TYPE_REDO_RENAME_PROC, attribute);
        actionCopex.logAction(MyConstants.LOG_TYPE_REDO, attribute);
    }

    /* log : redo cut */
    public void logRedoCut(ExperimentalProcedure proc, ArrayList<CopexTask> listTask, List<TaskTreePosition> listPositionTask){
        List<CopexProperty> attribute = CopexLog.logListTask(locale,proc, listTask, listPositionTask);
        //actionCopex.logAction(MyConstants.LOG_TYPE_REDO_CUT, attribute);
        actionCopex.logAction(MyConstants.LOG_TYPE_REDO, attribute);
    }

    /* log : redo paste */
    public void logRedoPaste(ExperimentalProcedure proc, ArrayList<CopexTask> listTask, List<TaskTreePosition> listPositionTask){
        List<CopexProperty> attribute = CopexLog.logListTask(locale,proc, listTask, listPositionTask);
        //actionCopex.logAction(MyConstants.LOG_TYPE_REDO_PASTE, attribute);
        actionCopex.logAction(MyConstants.LOG_TYPE_REDO, attribute);
    }

    /* log : redo drag&drop */
    public void logRedoDragDrop(ExperimentalProcedure proc, ArrayList<CopexTask> listTask, List<TaskTreePosition> listPositionTask, TaskTreePosition insertPosition){
        List<CopexProperty> attribute = CopexLog.logDragDrop(locale,proc, listTask, listPositionTask, insertPosition);
        //actionCopex.logAction(MyConstants.LOG_TYPE_REDO_DRAG_DROP, attribute);
        actionCopex.logAction(MyConstants.LOG_TYPE_REDO, attribute);
    }

    /* log : redo delete task */
    public void logRedoDeleteTask(ExperimentalProcedure proc, ArrayList<CopexTask> listTask, List<TaskTreePosition> listPositionTask){
        List<CopexProperty> attribute = CopexLog.logListTask(locale,proc, listTask, listPositionTask);
        //actionCopex.logAction(MyConstants.LOG_TYPE_REDO_DELETE_TASK, attribute);
        actionCopex.logAction(MyConstants.LOG_TYPE_REDO, attribute);
    }

    /* log : redo edit step */
    public void logRedoEditStep(ExperimentalProcedure proc, Step oldStep, Step newStep){
        List<CopexProperty> attribute = CopexLog.logUpdateTask(locale,proc, oldStep, newStep);
        //actionCopex.logAction(MyConstants.LOG_TYPE_REDO_EDIT_STEP, attribute);
        actionCopex.logAction(MyConstants.LOG_TYPE_REDO, attribute);
    }
    /* log : redo edit action */
    public void logRedoEditAction(ExperimentalProcedure proc, CopexAction oldAction, CopexAction newAction){
        List<CopexProperty> attribute = CopexLog.logUpdateTask(locale,proc, oldAction, newAction);
        //actionCopex.logAction(MyConstants.LOG_TYPE_REDO_EDIT_ACTION, attribute);
        actionCopex.logAction(MyConstants.LOG_TYPE_REDO, attribute);
    }
    /* log : redo edit question */
    public void logRedoEditQuestion(ExperimentalProcedure proc, Question oldQuestion, Question newQuestion){
        List<CopexProperty> attribute = CopexLog.logUpdateTask(locale,proc, oldQuestion, newQuestion);
        //actionCopex.logAction(MyConstants.LOG_TYPE_REDO_EDIT_QUESTION, attribute);
        actionCopex.logAction(MyConstants.LOG_TYPE_REDO, attribute);
    }

    /* log : redo add step */
    public void logRedoAddStep(ExperimentalProcedure proc, Step step, TaskTreePosition position){
        List<CopexProperty> attribute = CopexLog.logAddTask(locale,proc, step, position);
        //actionCopex.logAction(MyConstants.LOG_TYPE_REDO_ADD_STEP, attribute);
        actionCopex.logAction(MyConstants.LOG_TYPE_REDO, attribute);
    }
    /* log : redo add action */
    public void logRedoAddAction(ExperimentalProcedure proc, CopexAction action, TaskTreePosition position){
        List<CopexProperty> attribute = CopexLog.logAddTask(locale,proc, action, position);
        //actionCopex.logAction(MyConstants.LOG_TYPE_REDO_ADD_ACTION, attribute);
        actionCopex.logAction(MyConstants.LOG_TYPE_REDO, attribute);
    }
     /* log : undo paste */
    public void logUndoPaste(LearnerProcedure proc, ArrayList<CopexTask> listTask, List<TaskTreePosition> listPositionTask){
        List<CopexProperty> attribute = CopexLog.logListTask(locale,proc, listTask, listPositionTask);
        //actionCopex.logAction(MyConstants.LOG_TYPE_UNDO_PASTE, attribute);
        actionCopex.logAction(MyConstants.LOG_TYPE_UNDO, attribute);
    }

    /* log : undo drag&drop */
    public void logUndoDragDrop(ExperimentalProcedure proc, ArrayList<CopexTask> listTask, List<TaskTreePosition> listPositionTask, TaskTreePosition insertPosition){
        List<CopexProperty> attribute = CopexLog.logDragDrop(locale,proc, listTask, listPositionTask, insertPosition);
        //actionCopex.logAction(MyConstants.LOG_TYPE_UNDO_DRAG_DROP, attribute);
        actionCopex.logAction(MyConstants.LOG_TYPE_UNDO, attribute);
    }

    /* log: undo rename proc */
    public void logUndoRenameProc(ExperimentalProcedure proc, String oldName, String newName){
        List<CopexProperty> attribute = CopexLog.logRenameProc(proc, oldName, newName);
        //actionCopex.logAction(MyConstants.LOG_TYPE_UNDO_RENAME_PROC, attribute);
        actionCopex.logAction(MyConstants.LOG_TYPE_UNDO, attribute);
    }

    /* log : undo add task */
    public void logUndoAddTask(ExperimentalProcedure proc, ArrayList<CopexTask> listTask, List<TaskTreePosition> listPositionTask){
        List<CopexProperty> attribute = CopexLog.logListTask(locale,proc, listTask, listPositionTask);
        //actionCopex.logAction(MyConstants.LOG_TYPE_UNDO_ADD_TASK, attribute);
        actionCopex.logAction(MyConstants.LOG_TYPE_UNDO, attribute);
    }

    /* log : undo edit step */
    public void logUndoEditStep(ExperimentalProcedure proc, Step oldStep, Step newStep){
        List<CopexProperty> attribute = CopexLog.logUpdateTask(locale,proc, oldStep, newStep);
        //actionCopex.logAction(MyConstants.LOG_TYPE_UNDO_EDIT_STEP, attribute);
        actionCopex.logAction(MyConstants.LOG_TYPE_UNDO, attribute);
    }
    /* log : undo edit action */
    public void logUndoEditAction(ExperimentalProcedure proc, CopexAction oldAction, CopexAction newAction){
        List<CopexProperty> attribute = CopexLog.logUpdateTask(locale,proc, oldAction, newAction);
        //actionCopex.logAction(MyConstants.LOG_TYPE_UNDO_EDIT_ACTION, attribute);
        actionCopex.logAction(MyConstants.LOG_TYPE_UNDO, attribute);
    }
    /* log : undo edit question */
    public void logUndoEditQuestion(ExperimentalProcedure proc, Question oldQuestion, Question newQuestion){
        List<CopexProperty> attribute = CopexLog.logUpdateTask(locale,proc, oldQuestion, newQuestion);
        //actionCopex.logAction(MyConstants.LOG_TYPE_UNDO_EDIT_QUESTION, attribute);
        actionCopex.logAction(MyConstants.LOG_TYPE_UNDO, attribute);
    }

    /* log : undo cut */
    public void logUndoCut(ExperimentalProcedure proc, CopexTask task, TaskTreePosition position){
        List<CopexProperty> attribute = CopexLog.logTask(locale,proc, task, position);
        //actionCopex.logAction(MyConstants.LOG_TYPE_UNDO_CUT, attribute);
        actionCopex.logAction(MyConstants.LOG_TYPE_UNDO, attribute);
    }

    /* log : undo delete task */
    public void logUndoDeleteTask(ExperimentalProcedure proc, CopexTask task, TaskTreePosition position){
        List<CopexProperty> attribute = CopexLog.logTask(locale,proc, task, position);
        //actionCopex.logAction(MyConstants.LOG_TYPE_UNDO_DELETE_TASK, attribute);
        actionCopex.logAction(MyConstants.LOG_TYPE_UNDO, attribute);
    }

    /* log : load new elo */
    public void logLoadELO(ExperimentalProcedure proc){
        List<CopexProperty> attribute = CopexLog.logProc(locale, proc);
        if(!scyMode)
            actionCopex.logAction(MyConstants.LOG_TYPE_LOAD_ELO, attribute);
    }

    /* log : new elo */
    public void logNewELO(){
        if(!scyMode)
            actionCopex.logAction(MyConstants.LOG_TYPE_NEW_ELO, new LinkedList());
    }

    public void logActionInDB(String type, List<CopexProperty> attribute){
        CopexReturn cr = this.controller.logUserActionInDB(type, attribute);
        if(cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
        }
    }

    /* return true if the proc can be close (if it is not the proc of the labdoc) */
    public boolean canCloseProc(ExperimentalProcedure p){
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.isLabDocProc(p, v);
        if(cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return false;
        }
        return !(Boolean)v.get(0);
    }

    /* return true if can modify the title of the proc (not dbmode) */
    public boolean canUpdateTitle(){
        return !dbMode;
    }

    /* return the panel for the thumbnail*/
    public Container getInterfacePanel(){
        if(activCopex!= null){
            return activCopex.getInterfacePanel();
        }
        return new JPanel();
    }

    public boolean controlLenght(){
        return !scyMode;
    }

    
    /** update the hypothesis (scy text)*/
    public void setProcedureHypothsesis(String hypothesisText){
        if(activCopex != null){
            activCopex.setProcedureHypothesis(hypothesisText);
        }
    }

    /** update the question (scy text)*/
    public void setProcedureQuestion(String questionText){
        if(activCopex != null){
            activCopex.setProcedureQuestion(questionText);
        }
    }

    /** returns true if the user can edit the research question */
    public boolean canEditResearchQuestion(){
        if(activCopex != null){
            return activCopex.getExperimentalProc().getQuestion().getEditRight() == MyConstants.EXECUTE_RIGHT;
        }
        return false;
    }

    /** returns true if the user can edit the hypothesis */
    public boolean canEditHypothesis(){
        if(activCopex != null){
            return activCopex.getExperimentalProc().getHypothesisMode() != MyConstants.MODE_MENU_NO;
        }
        return false;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress".equals(evt.getPropertyName())) {
            int progress = (Integer) evt.getNewValue();
            progressBar.setValue(progress);
        } 
    }

    public String getPreview(){
        if(activCopex != null){
            return activCopex.getPreview();
        }
        return "";
     }

    /** set the current proc in read-only mode */
    public void setReadOnly(boolean readOnly){
        if(activCopex != null){
            activCopex.setExperimentalProcedureReadOnly(readOnly);
        }
    }

    public List<Material> getListMaterial(){
        return this.controller.getListMaterial(activCopex.getExperimentalProc());
    }

    public long getIdMaterial(){
        return this.controller.getIdMaterial();
    }

    public long getIdQuantity(){
        return this.controller.getIdQuantity();
    }

    public List<PhysicalQuantity> getListPhysicalQuantity(){
        return this.controller.getListPhysicalQuantity();
    }

    public List<TypeMaterial> getListTypeMaterial(){
        return this.controller.getListTypeMaterial();
    }

    /*starts the collaboration */
    public void startCollaboration(){
        copexSync = new CopexSyncManager(this);
    }

    /* ends the collaboration */
    public void endCollaboration(){
        copexSync = null;
    }

    /* returns true if the tool is sync with another (collaboration) */
    public boolean isCollaborate(){
        return copexSync != null;
    }

    // received added sync Object
    public void syncNodeAdded(ISyncObject syncObject){
        copexSync.syncNodeAdded(syncObject);
    }

    // received removed sync Object
    public void syncNodeRemoved(ISyncObject syncObject){
        copexSync.syncNodeRemoved(syncObject);
    }

    // received changed sync Object
    public void syncNodeChanged(ISyncObject syncObject){
        copexSync.syncNodeChanged(syncObject);
    }

    // sent added sync. object
    public void addCopexSyncObject(ISyncObject syncObject) {
        if(actionCopex != null)
            actionCopex.addCopexSyncObject(syncObject);
    }

    // sent changed sync. object
    public void changeCopexSyncObject(ISyncObject syncObject) {
        if(actionCopex != null)
            actionCopex.changeCopexSyncObject(syncObject);
    }

    // sent removed sync. object
    public void removeCopexSyncObject(ISyncObject syncObject) {
        if(actionCopex != null)
            actionCopex.removeCopexSyncObject(syncObject);
    }

    // UPDATE QUESTION
    public void updateQuestion(String value){
        Element e = CopexUtilities.stringToXml(value);
        if(activCopex != null){
            try {
                Question q = new Question(e, activCopex.getExperimentalProc().getQuestion().getDbKey());
                activCopex.updateQuestion(q,q.getDescription(locale), q.getComments(locale), false);
            } catch (JDOMException ex) {
                Logger.getLogger(CopexPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /*sent sync. event: update question */
    public void addSyncUpdateQuestion(Question question){
        if(isCollaborate())
            copexSync.addSyncUpdateQuestion(CopexUtilities.xmlToString(question.toXML()));
    }

    // UPDATE HYPOTHESIS
    public void updateHypothesis(String value){
        Element e = CopexUtilities.stringToXml(value);
        if(activCopex != null){
            try {
                Hypothesis h = new Hypothesis(e, -1);
                activCopex.updateHypothesis(h,h.getHypothesis(locale), h.getComment(locale), false);
            } catch (JDOMException ex) {
                Logger.getLogger(CopexPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /*sent sync. event: update hypothesis */
    public void addSyncUpdateHypothesis(Hypothesis hypothesis){
        if(isCollaborate())
            copexSync.addSyncUpdateHypothesis(CopexUtilities.xmlToString(hypothesis.toXML()));
    }

    // UPDATE GENERAL PRINCIPLE
    public void updateGeneralPrinciple(String value){
        Element e = CopexUtilities.stringToXml(value);
        if(activCopex != null){
            try {
                GeneralPrinciple p = new GeneralPrinciple(e, -1);
                activCopex.updateGeneralPrinciple(p,p.getPrinciple(locale), p.getComment(locale), false);
            } catch (JDOMException ex) {
                Logger.getLogger(CopexPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /*sent sync. event: update general principle */
    public void addSyncUpdateGeneralPrinciple(GeneralPrinciple principle){
        if(isCollaborate())
            copexSync.addSyncUpdateGeneralPrinciple(CopexUtilities.xmlToString(principle.toXML()));
    }

    // UPDATE EVALUATION
    public void updateEvaluation(String value){
        Element e = CopexUtilities.stringToXml(value);
        if(activCopex != null){
            try {
                Evaluation ev = new Evaluation(e, -1);
                activCopex.updateEvaluation(ev,ev.getEvaluation(locale), ev.getComment(locale), false);
            } catch (JDOMException ex) {
                Logger.getLogger(CopexPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /*sent sync. event: update evaluation */
    public void addSyncUpdateEvaluation(Evaluation evaluation){
        if(isCollaborate())
            copexSync.addSyncUpdateEvaluation(CopexUtilities.xmlToString(evaluation.toXML()));
    }

    // MATERIAL ADDED
    public void setMaterial(ArrayList<MaterialUsed> listMaterialToCreate, ArrayList<MaterialUsed> listMaterialToDelete, ArrayList<MaterialUsed> listMaterialToUpdate){
        if(activCopex != null){
            activCopex.setMaterialUsed(listMaterialToCreate, listMaterialToDelete, listMaterialToUpdate, false);
        }
    }

    /*sent sync. event: set material used */
    public void addSyncUpdateMaterial(ArrayList<MaterialUsed> listMaterialToCreate, ArrayList<MaterialUsed> listMaterialToDelete, ArrayList<MaterialUsed> listMaterialToUpdate){
        if(isCollaborate())
            copexSync.addSyncMaterialAdded(listMaterialToCreate, listMaterialToDelete, listMaterialToUpdate);
    }

    // ADD TASK
    public void addTask(CopexTask task, CopexTask taskSelected, char insertIn){
        if(activCopex != null){
            TaskSelected ts = activCopex.getTaskSelected(taskSelected, insertIn);
            if(task instanceof CopexAction){
                activCopex.addAction((CopexAction)task, ts,insertIn, false);
            }else if(task instanceof Step){
                activCopex.addStep((Step)task, ts,insertIn, false);
            }
        }
    }

    /*sent sync. event:  add task */
    public void addSyncAddTask(CopexTask task, CopexTask taskSelected, char insertIn){
        if(isCollaborate())
            copexSync.addSyncAddTask(task, taskSelected, insertIn);
    }

    public void exportToPdf(){
       activCopex.printCopex(true);
    }
}
