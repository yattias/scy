/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.edp;

import eu.scy.tools.copex.common.CopexMission;
import eu.scy.tools.copex.common.CopexTask;
import eu.scy.tools.copex.common.LearnerProcedure;
import eu.scy.tools.copex.common.PhysicalQuantity;
import eu.scy.tools.copex.controller.ControllerInterface;
import eu.scy.tools.copex.controller.CopexController;
import eu.scy.tools.copex.controller.CopexControllerDB;
import eu.scy.tools.copex.utilities.ActionCopex;
import eu.scy.tools.copex.utilities.CopexImage;
import eu.scy.tools.copex.utilities.CopexReturn;
import eu.scy.tools.copex.utilities.CopexUtilities;
import eu.scy.tools.copex.utilities.MyConstants;
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
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * main panel COPEX
 * @author Marjolaine
 */
public class CopexPanel extends JPanel {
    // CONSTANTES
    /*panel size*/
    public final static int PANEL_WIDTH = 550;
    public final static int PANEL_HEIGHT = 370;

    /* version */
    private String version = "2.0";
    protected ControllerInterface controller;
    public final static Color backgroundColor = SystemColor.control;
    /* locale */
    private Locale locale ;
    /* ressource bundle */
    private ResourceBundle bundle;
    /* images de l'editeur */
    private ArrayList<CopexImage> listImage;
    private boolean scyMode;
    private boolean dbMode;
    private ActionCopex actionCopex;
    /* identifiant user */
    private String idUser;
    /* identifiant mission */
    private long dbKeyMission;
    /* mode de l'applet */
    private int mode;
    /* nom de l'utilisateur */
    private String userName;
    /* prenom de l'utilisateur */
    private String firstName;

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


    public CopexPanel(boolean scyMode){
        this.idUser = "1";
        this.dbKeyMission = 1;
        this.mode = MyConstants.COPEX_MODE;
        this.userName = "";
        this.firstName = "";
        this.listTaskImage = new ArrayList();
        this.scyMode = scyMode;
        this.dbMode = false;
        initEdP(null, null);
    }




    public CopexPanel(URL copexURL, String idUser, long dbKeyMission, int mode, String userName, String firstName) {
        this.idUser = idUser;
        this.dbKeyMission = dbKeyMission;
        this.mode = mode;
        this.userName = userName;
        this.firstName = firstName;
        this.listTaskImage = new ArrayList();
        this.scyMode = false;
        this.dbMode = true;
        initEdP(null, copexURL);
    }

    public void initEdP(CopexApplet applet, URL url){
        // i18n
        locale = Locale.getDefault();
        locale = new Locale("en", "GB");
        try{
            this.bundle = ResourceBundle.getBundle("CopexBundle", locale);
        }catch(MissingResourceException e){
          try{
              // par defaut on prend l'anglais
              locale = new Locale("en", "GB");
              bundle = ResourceBundle.getBundle("CopexBundle");
          }catch (MissingResourceException e2){
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            System.out.println("ERREUR lors du chargement de copex, la langue specifiee "+locale+" n'existe pas : "+e);
            displayError(new CopexReturn("ERREUR lors du chargement de copex : "+e, false), "ERROR LANGUAGE");
            return;
            }
        }
      // Initialisation du look and feel
        try{
            String myLookAndFeel=UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(myLookAndFeel);
        }catch(Exception e){
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            System.out.println("ERREUR dans l'initialisation du lookAndFeel : "+e) ;
            JOptionPane.showMessageDialog(this , "ERREUR ans l'initialisation du lookAndFeel : "+e, "ERROR",JOptionPane.ERROR_MESSAGE);
        }
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


    public void addActionCopex(ActionCopex actionCopex){
        this.actionCopex = actionCopex;
    }
    /* chargement des donnees */
    public void loadData(){
      setCursor(new Cursor(Cursor.WAIT_CURSOR));
       // appel au noyau : chargement des donnees
      String logFileName = "logFile"+CopexUtilities.getCurrentDate()+"-"+dbKeyMission+"-"+idUser+".xml";
      CopexReturn cr = this.controller.initEdP(locale, idUser, dbKeyMission, mode, userName, firstName, logFileName);
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
    
    public  ImageIcon getCopexImage(String img){
        ImageIcon imgIcon = new ImageIcon(getClass().getResource( "/" +img));
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
        if(s == null){
            try{
                String msg = this.bundle.getString("ERROR_KEY");
                msg = CopexUtilities.replace(msg, 0, key);
                displayError(new CopexReturn(msg, false) , this.bundle.getString("TITLE_DIALOG_ERROR"));
            }catch(Exception e2){
                System.out.println("No message found : "+key);
                displayError(new CopexReturn("No message found !", false) ,"ERROR");
             }
        }
        return s;
    }

    /* retourne un message selon cle*/
    public String getBundleStringKey(String key){
        String s = "";
        try{
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
            revalidate();
            repaint();
        }
    }

    private void initTabbedPane(){
        this.add(getCopexTabbedPane(), BorderLayout.CENTER);
        int nb = listProc.size();
        for (int i=0; i<nb; i++){
            addCopexPanel(listProc.get(i));
        }
        revalidate();
        repaint();
    }

    private CopexTabbedPane getCopexTabbedPane(){
        if(copexTabbedPane == null){
            copexTabbedPane = new CopexTabbedPane(this);
        }
        return copexTabbedPane;
    }

    private void addCopexPanel(LearnerProcedure proc){
        EdPPanel copex = new EdPPanel(this, proc, controller, proc.getMission(), proc.getMission().getListInitialProc(), listPhysicalQuantity);
        activCopex = copex;
        listCopexPanel.add(copex);
        listProc.add(proc);
        if(scyMode){
            System.out.println("addCopexpanel, scyMode");
            this.add(copex, BorderLayout.CENTER);
        }else{
            copexTabbedPane.addTab(proc.getName(), copex);
        }
    }

    /* retourne vrai si dans la mission on peut ajouter un proc */
    public boolean canAddProc(){
        if(mission == null)
            return true;
        return this.mission.getOptions().isCanAddProc();
    }
    /* retourne le tooltiptext sur le bouton d'ouverture */
    public String getToolTipTextOpen(){
        return getBundleString("TOOLTIPTEXT_OPEN_PROC");
    }

    /* recherche de l'indice d'un protocole */
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
            // a terme il faut ouvrir un nouvel elo ?
            if(actionCopex != null)
                actionCopex.loadHelpProc(helpProc);
        }else{
            if (getIdProc(helpProc.getDbKey()) == -1)
                addCopexPanel(helpProc);
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
        addCopexPanel(proc);
    }
    public void updateProc(LearnerProcedure proc){
        int id = getIdProc(proc.getDbKey());
        if(id != -1){
            listProc.set(id, proc);
            listCopexPanel.get(id).updateProc(proc);
        }
    }

    /* ouverture auto de la fenetre d'edition de la question */
    public void openQuestionDialog(){
        
    }

    public void setQuestionDialog(){
       
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

    public void updateDataSheet(LearnerProcedure proc){
        int id = getIdProc(proc.getDbKey());
        if(id != -1){
            listProc.set(id, proc);
            listCopexPanel.get(id).updateDataSheet(proc.getDataSheet());
        }
    }
    public void deleteDataSheet(LearnerProcedure proc){
        int id = getIdProc(proc.getDbKey());
        if(id != -1){
            listProc.set(id, proc);
            listCopexPanel.get(id).deleteDataSheet();
        }
    }

    public void createDataSheet(LearnerProcedure proc){
        int id = getIdProc(proc.getDbKey());
        if(id != -1){
            listProc.set(id, proc);
            listCopexPanel.get(id).createDataSheet(proc.getDataSheet());
        }
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
            CloseProcDialog closeD = new CloseProcDialog(this, controller, proc);
            closeD.setVisible(true);
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
        return activCopex.getExperimentalProcedure();
    }

    private boolean hasModification(){
        return activCopex.hasModification();
    }

}
