/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.edp;

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
import eu.scy.client.tools.copex.utilities.CopexImage;
import eu.scy.client.tools.copex.utilities.CopexReturn;
import eu.scy.client.tools.copex.utilities.MyConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.SystemColor;
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
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import eu.scy.client.common.scyi18n.ResourceBundleWrapper;

/**
 * main panel COPEX
 * @author Marjolaine
 */
public class CopexPanel extends JPanel {
    // CONSTANTES
    /*panel size*/
    public final static int PANEL_WIDTH = 550;
    public final static int PANEL_HEIGHT = 350;

    /* version */
    private String version = "2.1";
    protected ControllerInterface controller;
    public final static Color backgroundColor = SystemColor.control;
    /* locale */
    private Locale locale ;
    /* ressource bundle */
    //private ResourceBundle bundle;
    private ResourceBundleWrapper bundle;
    //private ResourceBundleWrapper bundle;
    /* images de l'editeur */
    private ArrayList<CopexImage> listImage;
    private boolean scyMode;
    private boolean dbMode;
    private ActionCopex actionCopex;
    /* identifiant user */
    private String idUser;
    /* identifiant mission */
    private long dbKeyMission;
    private long dbKeyGroup;
    private long dbKeyLabDoc;
    private CopexMission mission;
    // liste des protocoles ouverts
    private ArrayList<LearnerProcedure> listProc = null;
    private ArrayList<EdPPanel> listCopexPanel;
    private EdPPanel activCopex;
    /* liste des images des taches */
    private ArrayList<CopexImage> listTaskImage;
    /* liste des grandeurs physiques */
    private ArrayList<PhysicalQuantity> listPhysicalQuantity ;

    private File lastUsedFileOpen = null;
    private transient SAXBuilder builder = new SAXBuilder(false);
    
    private CopexTabbedPane copexTabbedPane;
    private JFrame ownerFrame;


    public CopexPanel(JFrame ownerFrame,boolean scyMode, Locale locale){
        this.ownerFrame = ownerFrame;
        this.locale = locale;
        this.idUser = "1";
        this.dbKeyMission = 1;
        this.dbKeyGroup = 1;
        this.dbKeyLabDoc = 1;
        this.listTaskImage = new ArrayList();
        this.scyMode = scyMode;
        this.dbMode = false;
        initEdP(null, null);
    }

    public CopexPanel(boolean scyMode, Locale locale){
        this.ownerFrame = null;
        this.locale = locale;
        this.idUser = "1";
        this.dbKeyMission = 1;
        this.dbKeyGroup = 1;
        this.dbKeyLabDoc = 1;
        this.listTaskImage = new ArrayList();
        this.scyMode = scyMode;
        this.dbMode = false;
        initEdP(null, null);
    }


    public CopexPanel(JFrame ownerFrame,Locale locale, URL copexURL, String idUser, long dbKeyMission, long dbKeyGroup, long dbKeyLabDoc) {
        this.ownerFrame = ownerFrame;
        this.locale = locale;
        this.idUser = idUser;
        this.dbKeyMission = dbKeyMission;
        this.dbKeyGroup = dbKeyGroup;
        this.dbKeyLabDoc = dbKeyLabDoc;
        this.listTaskImage = new ArrayList();
        this.scyMode = false;
        this.dbMode = true;
        initEdP(null, copexURL);
    }

    public void initEdP(CopexApplet applet, URL url){
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
       // Initialisation du look and feel
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
      if(applet == null)
        if(url != null){
            this.controller = new CopexControllerDB(this, url);
        }else
            this.controller = new CopexController(this) ;
      else{
          this.controller = new CopexControllerDB(this, applet.getCodeBase());
      }
      setMinimumSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
      setSize(PANEL_WIDTH, PANEL_HEIGHT);
      setLayout(new BorderLayout());
      setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    @Override
    public Locale getLocale(){
        return this.locale;
    }

    public void addActionCopex(ActionCopex actionCopex){
        this.actionCopex = actionCopex;
    }
    /* chargement des donnees */
    public void loadData(){
      setCursor(new Cursor(Cursor.WAIT_CURSOR));
       // appel au noyau : chargement des donnees
      //String logFileName = "logFile"+CopexUtilities.getCurrentDate()+"-"+dbKeyMission+"-"+idUser+".xml";
      //String fileMission = "copexMission_SCI121.xml";
      //String fileMission = "copexMission_simple.xml";
      String fileMission = "copexMission_SCY1_CO2.xml";
      CopexReturn cr = this.controller.initEdP(locale, idUser, dbKeyMission,dbKeyGroup, dbKeyLabDoc, fileMission);
      if (cr.isError()){
          setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
          System.out.println("erreur chargement des donnees ....");
          displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
          this.stop();
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
    /* retourne un message selon cle*/
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

    /* retourne un message selon cle*/
    public String getBundleStringKey(String key){
        String s = "";
        try{
            //s = this.bundle.getString(key);
            s = this.bundle.getString(key);
            return s;
        }catch(Exception e){
            return null;
        }
    }

    /**
     * retourne l'image de la tache correspondant au nom
     */
    public  Image getTaskImage(String img){
        if (img == null || img.equals(""))
            return null;
       int nbImg = listTaskImage.size();
       for(int i=0; i<nbImg; i++){
           if (listTaskImage.get(i).getImgName().equals(img)){
               return listTaskImage.get(i).getImg();
           }
       }
      displayError(new CopexReturn(getBundleString("MSG_ERROR_IMAGE")+img, false), getBundleString("TITLE_DIALOG_ERROR"));
       return null;
    }
    
     public void stop() {
        CopexReturn cr = this.controller.stopEdP();
        if (cr.isError())
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
    }

    
      /* initialisation de l'application avec les donnees */
    public void initEdp(CopexMission mission, ArrayList<LearnerProcedure> listProc,  ArrayList<PhysicalQuantity> listPhysicalQuantity) {
       setCursor(new Cursor(Cursor.WAIT_CURSOR));
       // mise a jour des donnees :
       this.mission = mission;
       this.listProc = listProc;
       this.listPhysicalQuantity = listPhysicalQuantity ;
       listCopexPanel = new ArrayList();
       // augmente temps d'affichage du tooltiptext
       ToolTipManager.sharedInstance().setDismissDelay(10000);
       ToolTipManager.sharedInstance().setInitialDelay(0);
       if(scyMode){
           initProcedure();
       }else{
           initTabbedPane();
       }
    }
    private void initProcedure(){
        if(listProc.size() > 0){
            EdPPanel copex = new EdPPanel(this, listProc.get(0), controller, mission, mission.getListInitialProc(), listPhysicalQuantity);
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
//        revalidate();
//        repaint();
    }

    private CopexTabbedPane getCopexTabbedPane(){
        if(copexTabbedPane == null){
            copexTabbedPane = new CopexTabbedPane(this);
        }
        return copexTabbedPane;
    }

    private void addCopexPanel(LearnerProcedure proc, boolean addProc){
        EdPPanel copex = new EdPPanel(this, proc, controller, proc.getMission(), proc.getMission().getListInitialProc(), listPhysicalQuantity);
        activCopex = copex;
        listCopexPanel.add(copex);
        if(addProc)
            listProc.add(proc);
        if(scyMode){
            //System.out.println("addCopexpanel, scyMode");
            this.add(copex, BorderLayout.CENTER);
        }else{
            copexTabbedPane.addTab(proc.getName(getLocale()), copex);
        }
    }

    /* retourne vrai si dans la mission on peut ajouter un proc */
    public boolean canAddProc(){
        if(mission == null)
            return true;
        return true;
    }
    /* retourne le tooltiptext sur le bouton d'ouverture */
    public String getToolTipTextOpen(){
        return getBundleString("TOOLTIPTEXT_OPEN_PROC");
    }

    public void setQuestionDialog(){
        if(activCopex != null)
            activCopex.setQuestionDialog();
    }

    /* recherche de l'indice
     d'un protocole */
    private int getIdProc(long dbKey){
        int id = -1;
        int nbP = listProc.size();
        for (int i=0; i<nbP; i++){
            if (listProc.get(i).getDbKey() == dbKey)
                return i;
        }
        return id;
    }
    /* fermeture d'un protocole */
    public void closeProc(LearnerProcedure proc) {
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

    /* retourne le protocole actif */
    public LearnerProcedure getProcActiv() {
        return this.getCopexTabbedPane().getProcActiv();
    }
    
    /* renvoit vrai si aucun proc n'est ouvert */
     public boolean noProc(){
         return listProc.size() == 0;
     }


     /* affichage du proc d'aide */
    public void displayHelpProc(LearnerProcedure helpProc){
        if(scyMode){
//            // a terme il faut ouvrir un nouvel elo ?
//            if(actionCopex != null)
//                actionCopex.loadHelpProc(helpProc);
            EdPPanel helpPanel = new EdPPanel(this, helpProc, controller, helpProc.getMission(), helpProc.getMission().getListInitialProc(), listPhysicalQuantity);
            HelpDialog help = new HelpDialog(activCopex, helpPanel);
            help.setVisible(true);
        }else{
            if (getIdProc(helpProc.getDbKey()) == -1)
                addCopexPanel(helpProc, true);
            else
                getCopexTabbedPane().setSelected(helpProc);
        }
    }


   /* affichage d'un message concernant les proc lockes */
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

    /* demande quel proc initial pour creation d'un proc */
    public void askForInitialProc(){
        CreateProcDialog dialog = new CreateProcDialog(this, controller, mission.getListInitialProc()) ;
        dialog.setVisible(true);
    }

    /* retourne le point pour afficher la boite de dialogue */
    public Point getLocationDialog(){
        if(activCopex == null)
            return new Point(10, 10);
        return this.activCopex.getLocationDialog();
    }

    

    public void createProc(LearnerProcedure proc){
        addCopexPanel(proc, true);
    }
    public void updateProc(LearnerProcedure proc){
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
    public void openDialogCloseProc(LearnerProcedure proc) {
        if(dbMode){
//            CloseProcDialog closeD = new CloseProcDialog(this, controller, proc);
//            closeD.setVisible(true);
            // fermeture du proc
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
            // liste des protocoles a copier :
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

    

    // load/open ELO SCY ou non
    public void loadELO(Element elo){
        if(scyMode){
            CopexReturn cr = this.controller.deleteProc(activCopex.getLearnerProc());
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
        // scyMode est vrai
        CopexReturn cr = this.controller.deleteProc(activCopex.getLearnerProc());
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

    // SCY : retourne l'elo courant
    public Element getXProc(){
        logSaveProc(activCopex.getLearnerProc());
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
        actionCopex.logAction(MyConstants.LOG_TYPE_OPEN_PROC, attribute);
    }

    /* log : save proc */
    public void logSaveProc(LearnerProcedure proc){
        List<CopexProperty> attribute = CopexLog.logProc(locale,proc);
        actionCopex.logAction(MyConstants.LOG_TYPE_SAVE_PROC, attribute);
    }
    /* log: delete proc */
    public void logDeleteProc(LearnerProcedure proc){
        List<CopexProperty> attribute = CopexLog.logProc(locale,proc);
        actionCopex.logAction(MyConstants.LOG_TYPE_DELETE_PROC, attribute);
    }

    /* log: rename proc */
    public void logRenameProc(LearnerProcedure proc, String oldName, String newName){
        List<CopexProperty> attribute = CopexLog.logRenameProc(proc, oldName, newName);
        actionCopex.logAction(MyConstants.LOG_TYPE_RENAME_PROC, attribute);
    }

    /* log : print proc */
    public void logPrintProc(LearnerProcedure proc){
        List<CopexProperty> attribute = CopexLog.logProc(locale,proc);
        actionCopex.logAction(MyConstants.LOG_TYPE_PRINT_PROC, attribute);
    }

    /* log: create proc */
    public void logCreateProc(LearnerProcedure proc){
        List<CopexProperty> attribute = CopexLog.logProc(locale,proc);
        actionCopex.logAction(MyConstants.LOG_TYPE_CREATE_PROC, attribute);
    }
    /* log: copy proc */
    public void logCopyProc(LearnerProcedure proc, LearnerProcedure procToCopy){
        List<CopexProperty> attribute = CopexLog.logCopyProc(locale,proc, procToCopy);
        actionCopex.logAction(MyConstants.LOG_TYPE_COPY_PROC, attribute);
    }

    /* log: update task */
    public void logUpdateQuestion(LearnerProcedure proc, Question oldQuestion, Question newQuestion){
        List<CopexProperty> attribute = CopexLog.logUpdateTask(locale,proc, oldQuestion, newQuestion);
        actionCopex.logAction(MyConstants.LOG_TYPE_UPDATE_QUESTION, attribute);
    }

    /* log: update step */
    public void logUpdateStep(LearnerProcedure proc, Step oldStep, Step newStep){
        List<CopexProperty> attribute = CopexLog.logUpdateTask(locale,proc, oldStep, newStep);
        actionCopex.logAction(MyConstants.LOG_TYPE_UPDATE_STEP, attribute);
    }

    /* log: update action */
    public void logUpdateAction(LearnerProcedure proc, CopexAction oldAction, CopexAction newAction){
        List<CopexProperty> attribute = CopexLog.logUpdateTask(locale,proc, oldAction, newAction);
        actionCopex.logAction(MyConstants.LOG_TYPE_UPDATE_ACTION, attribute);
    }

    /* log: add step*/
    public void logAddStep(LearnerProcedure proc, Step step,TaskTreePosition position){
        List<CopexProperty> attribute = CopexLog.logAddTask(locale,proc, step, position);
        actionCopex.logAction(MyConstants.LOG_TYPE_ADD_STEP, attribute);
    }

    /* log: add action*/
    public void logAddAction(LearnerProcedure proc, CopexAction action,TaskTreePosition position){
        List<CopexProperty> attribute = CopexLog.logAddTask(locale,proc, action, position);
        actionCopex.logAction(MyConstants.LOG_TYPE_ADD_STEP, attribute);
    }

    /* log : paste */
    public void logPaste(LearnerProcedure proc, ArrayList<CopexTask> listTask, List<TaskTreePosition> listPositionTask){
        List<CopexProperty> attribute = CopexLog.logListTask(locale,proc, listTask, listPositionTask);
        actionCopex.logAction(MyConstants.LOG_TYPE_PASTE, attribute);
    }

    /* log : copy */
    public void logCopy(LearnerProcedure proc, ArrayList<CopexTask> listTask, List<TaskTreePosition> listPositionTask){
        List<CopexProperty> attribute = CopexLog.logListTask(locale,proc, listTask, listPositionTask);
        actionCopex.logAction(MyConstants.LOG_TYPE_COPY, attribute);
    }

    /* log : suppr */
    public void logDelete(LearnerProcedure proc, ArrayList<CopexTask> listTask, List<TaskTreePosition> listPositionTask){
        List<CopexProperty> attribute = CopexLog.logListTask(locale,proc, listTask, listPositionTask);
        actionCopex.logAction(MyConstants.LOG_TYPE_DELETE, attribute);
    }

    /* log : cut */
    public void logCut(LearnerProcedure proc, ArrayList<CopexTask> listTask, List<TaskTreePosition> listPositionTask){
        List<CopexProperty> attribute = CopexLog.logListTask(locale,proc, listTask, listPositionTask);
        actionCopex.logAction(MyConstants.LOG_TYPE_CUT, attribute);
    }

    /* log : drag&drop */
    public void logDragDrop(LearnerProcedure proc, ArrayList<CopexTask> listTask, List<TaskTreePosition> listPositionTask, TaskTreePosition insertPosition){
        List<CopexProperty> attribute = CopexLog.logDragDrop(locale,proc, listTask, listPositionTask, insertPosition);
        actionCopex.logAction(MyConstants.LOG_TYPE_DRAG_DROP, attribute);
    }

    /* log : hypothesis */
    public void logHypothesis(LearnerProcedure proc, Hypothesis oldHypothesis, Hypothesis newHypothesis){
        List<CopexProperty> attribute = CopexLog.logHypothesis(locale,proc, oldHypothesis, newHypothesis);
        actionCopex.logAction(MyConstants.LOG_TYPE_HYPOTHESIS, attribute);
    }
    /* log : general principle */
    public void logGeneralPrinciple(LearnerProcedure proc, GeneralPrinciple oldPrinciple, GeneralPrinciple newGeneralPrinciple){
        List<CopexProperty> attribute = CopexLog.logGeneralPrinciple(locale,proc, oldPrinciple, newGeneralPrinciple);
        actionCopex.logAction(MyConstants.LOG_TYPE_GENERAL_PRINCIPLE, attribute);
    }
    /* log : evaluatione */
    public void logEvaluation(LearnerProcedure proc, Evaluation oldEvaluation, Evaluation newEvaluation){
        List<CopexProperty> attribute = CopexLog.logEvaluation(locale,proc, oldEvaluation, newEvaluation);
        actionCopex.logAction(MyConstants.LOG_TYPE_EVALUATION, attribute);
    }

    /* log: create material used */
    public void logCreateMaterialUsed(LearnerProcedure proc, ArrayList<MaterialUsed> listMaterialUsed){
        List<CopexProperty> attribute = CopexLog.logMaterialUsed(locale,proc, listMaterialUsed);
        actionCopex.logAction(MyConstants.LOG_TYPE_CREATE_MATERIAL_USED, attribute);
    }

    /* log: delete material used */
    public void logDeleteMaterialUsed(LearnerProcedure proc, ArrayList<MaterialUsed> listMaterialUsed){
        List<CopexProperty> attribute = CopexLog.logMaterialUsed(locale,proc, listMaterialUsed);
        actionCopex.logAction(MyConstants.LOG_TYPE_DELETE_MATERIAL_USED, attribute);
    }
    /* log: update material used */
    public void logUpdateMaterialUsed(LearnerProcedure proc, ArrayList<MaterialUsed> oldListMaterialUsed, ArrayList<MaterialUsed> newListMaterialUsed){
        List<CopexProperty> attribute = CopexLog.logUpdateMaterialUsed(locale,proc, oldListMaterialUsed, newListMaterialUsed);
        actionCopex.logAction(MyConstants.LOG_TYPE_UPDATE_MATERIAL_USED, attribute);
    }

    /* log: redo rename proc */
    public void logRedoRenameProc(LearnerProcedure proc, String oldName, String newName){
        List<CopexProperty> attribute = CopexLog.logRenameProc(proc, oldName, newName);
        actionCopex.logAction(MyConstants.LOG_TYPE_REDO_RENAME_PROC, attribute);
    }

    /* log : redo cut */
    public void logRedoCut(LearnerProcedure proc, ArrayList<CopexTask> listTask, List<TaskTreePosition> listPositionTask){
        List<CopexProperty> attribute = CopexLog.logListTask(locale,proc, listTask, listPositionTask);
        actionCopex.logAction(MyConstants.LOG_TYPE_REDO_CUT, attribute);
    }

    /* log : redo paste */
    public void logRedoPaste(LearnerProcedure proc, ArrayList<CopexTask> listTask, List<TaskTreePosition> listPositionTask){
        List<CopexProperty> attribute = CopexLog.logListTask(locale,proc, listTask, listPositionTask);
        actionCopex.logAction(MyConstants.LOG_TYPE_REDO_PASTE, attribute);
    }

    /* log : redo drag&drop */
    public void logRedoDragDrop(LearnerProcedure proc, ArrayList<CopexTask> listTask, List<TaskTreePosition> listPositionTask, TaskTreePosition insertPosition){
        List<CopexProperty> attribute = CopexLog.logDragDrop(locale,proc, listTask, listPositionTask, insertPosition);
        actionCopex.logAction(MyConstants.LOG_TYPE_REDO_DRAG_DROP, attribute);
    }

    /* log : redo delete task */
    public void logRedoDeleteTask(LearnerProcedure proc, ArrayList<CopexTask> listTask, List<TaskTreePosition> listPositionTask){
        List<CopexProperty> attribute = CopexLog.logListTask(locale,proc, listTask, listPositionTask);
        actionCopex.logAction(MyConstants.LOG_TYPE_REDO_DELETE_TASK, attribute);
    }

    /* log : redo edit step */
    public void logRedoEditStep(LearnerProcedure proc, Step oldStep, Step newStep){
        List<CopexProperty> attribute = CopexLog.logUpdateTask(locale,proc, oldStep, newStep);
        actionCopex.logAction(MyConstants.LOG_TYPE_REDO_EDIT_STEP, attribute);
    }
    /* log : redo edit action */
    public void logRedoEditAction(LearnerProcedure proc, CopexAction oldAction, CopexAction newAction){
        List<CopexProperty> attribute = CopexLog.logUpdateTask(locale,proc, oldAction, newAction);
        actionCopex.logAction(MyConstants.LOG_TYPE_REDO_EDIT_ACTION, attribute);
    }
    /* log : redo edit question */
    public void logRedoEditQuestion(LearnerProcedure proc, Question oldQuestion, Question newQuestion){
        List<CopexProperty> attribute = CopexLog.logUpdateTask(locale,proc, oldQuestion, newQuestion);
        actionCopex.logAction(MyConstants.LOG_TYPE_REDO_EDIT_QUESTION, attribute);
    }

    /* log : redo add step */
    public void logRedoAddStep(LearnerProcedure proc, Step step, TaskTreePosition position){
        List<CopexProperty> attribute = CopexLog.logAddTask(locale,proc, step, position);
        actionCopex.logAction(MyConstants.LOG_TYPE_REDO_ADD_STEP, attribute);
    }
    /* log : redo add action */
    public void logRedoAddAction(LearnerProcedure proc, CopexAction action, TaskTreePosition position){
        List<CopexProperty> attribute = CopexLog.logAddTask(locale,proc, action, position);
        actionCopex.logAction(MyConstants.LOG_TYPE_REDO_ADD_ACTION, attribute);
    }
     /* log : undo paste */
    public void logUndoPaste(LearnerProcedure proc, ArrayList<CopexTask> listTask, List<TaskTreePosition> listPositionTask){
        List<CopexProperty> attribute = CopexLog.logListTask(locale,proc, listTask, listPositionTask);
        actionCopex.logAction(MyConstants.LOG_TYPE_UNDO_PASTE, attribute);
    }

    /* log : undo drag&drop */
    public void logUndoDragDrop(LearnerProcedure proc, ArrayList<CopexTask> listTask, List<TaskTreePosition> listPositionTask, TaskTreePosition insertPosition){
        List<CopexProperty> attribute = CopexLog.logDragDrop(locale,proc, listTask, listPositionTask, insertPosition);
        actionCopex.logAction(MyConstants.LOG_TYPE_UNDO_DRAG_DROP, attribute);
    }

    /* log: undo rename proc */
    public void logUndoRenameProc(LearnerProcedure proc, String oldName, String newName){
        List<CopexProperty> attribute = CopexLog.logRenameProc(proc, oldName, newName);
        actionCopex.logAction(MyConstants.LOG_TYPE_UNDO_RENAME_PROC, attribute);
    }

    /* log : undo add task */
    public void logUndoAddTask(LearnerProcedure proc, ArrayList<CopexTask> listTask, List<TaskTreePosition> listPositionTask){
        List<CopexProperty> attribute = CopexLog.logListTask(locale,proc, listTask, listPositionTask);
        actionCopex.logAction(MyConstants.LOG_TYPE_UNDO_ADD_TASK, attribute);
    }

    /* log : undo edit step */
    public void logUndoEditStep(LearnerProcedure proc, Step oldStep, Step newStep){
        List<CopexProperty> attribute = CopexLog.logUpdateTask(locale,proc, oldStep, newStep);
        actionCopex.logAction(MyConstants.LOG_TYPE_UNDO_EDIT_STEP, attribute);
    }
    /* log : undo edit action */
    public void logUndoEditAction(LearnerProcedure proc, CopexAction oldAction, CopexAction newAction){
        List<CopexProperty> attribute = CopexLog.logUpdateTask(locale,proc, oldAction, newAction);
        actionCopex.logAction(MyConstants.LOG_TYPE_UNDO_EDIT_ACTION, attribute);
    }
    /* log : undo edit question */
    public void logUndoEditQuestion(LearnerProcedure proc, Question oldQuestion, Question newQuestion){
        List<CopexProperty> attribute = CopexLog.logUpdateTask(locale,proc, oldQuestion, newQuestion);
        actionCopex.logAction(MyConstants.LOG_TYPE_UNDO_EDIT_QUESTION, attribute);
    }

    /* log : undo cut */
    public void logUndoCut(LearnerProcedure proc, CopexTask task, TaskTreePosition position){
        List<CopexProperty> attribute = CopexLog.logTask(locale,proc, task, position);
        actionCopex.logAction(MyConstants.LOG_TYPE_UNDO_CUT, attribute);
    }

    /* log : undo delete task */
    public void logUndoDeleteTask(LearnerProcedure proc, CopexTask task, TaskTreePosition position){
        List<CopexProperty> attribute = CopexLog.logTask(locale,proc, task, position);
        actionCopex.logAction(MyConstants.LOG_TYPE_UNDO_DELETE_TASK, attribute);
    }

    /* log : load new elo */
    public void logLoadELO(LearnerProcedure proc){
        List<CopexProperty> attribute = CopexLog.logProc(locale, proc);
        actionCopex.logAction(MyConstants.LOG_TYPE_LOAD_ELO, attribute);
    }

    /* log : new elo */
    public void logNewELO(){
        actionCopex.logAction(MyConstants.LOG_TYPE_NEW_ELO, new LinkedList());
    }

    public void logActionInDB(String type, List<CopexProperty> attribute){
        CopexReturn cr = this.controller.logUserActionInDB(type, attribute);
        if(cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
        }
    }

    /* return true if the proc can be close (if it is not the proc of the labdoc) */
    public boolean canCloseProc(LearnerProcedure p){
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.isLabDocProc(p, v);
        if(cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return false;
        }
        return !(Boolean)v.get(0);
    }

}
