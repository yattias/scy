/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.edp;

import eu.scy.tools.copex.common.*;
import eu.scy.tools.copex.controller.CopexControllerAppletDB;
import eu.scy.tools.copex.controller.ControllerInterface;
import eu.scy.tools.copex.controller.CopexController;
import eu.scy.tools.copex.controller.CopexControllerDB;
import eu.scy.tools.copex.dnd.SubTree;
import eu.scy.tools.copex.print.PrintDialog;
import eu.scy.tools.copex.utilities.*;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.ArrayList;
import javax.swing.*;
import org.jdom.Element;

/**
 * main edp panel
 * @author Marjolaine
 */
public class EdPPanel extends JPanel {
    // CONSTANTES
    /* activation de l'ajout de protocole */
    public  static boolean CAN_ADD_PROC = true;
    /* activation des cellules editables dans le dataSheet */
    public  static boolean CAN_EDIT_DATASHEET = true;
    /*panel size*/
    public final static int PANEL_WIDTH = 550;
    public final static int PANEL_HEIGHT = 370;

    /* version */
    private String version = "Version 1.2.1";
    protected ControllerInterface controller;
    public final static Color backgroundColor = SystemColor.control;
    /* locale */
    protected Locale locale ;
    /* ressource bundle */
    protected ResourceBundle bundle;
    /* images de l'éditeur */
    private ArrayList<CopexImage> listImage;
    /* identifiant user */
    protected String idUser;
    /* identifiant mission */
    protected long dbKeyMission;
    /* mode de l'applet */
    protected int mode;
    /* nom de l'utilisateur */
    protected String userName;
    /* prenom de l'utilisateur */
    protected String firstName;
    
    
    // mission
    private CopexMission mission = null;
    // liste des protocoles ouverts
    private ArrayList<LearnerProcedure> listProc = null;
    /* liste protocole initial */
    private ArrayList<InitialProcedure> listInitProc = null;
    // protocole actif
    private LearnerProcedure procActiv = null;
    /* mode d'affichage des commentaires */
    private char modeComments = MyConstants.COMMENTS;
    /* liste des grandeurs physiques */
    private ArrayList<PhysicalQuantity> listPhysicalQuantity ;


    /* level affiché */
    private int levelMenu = 1;

    /* elements copies */
    private SubTree subTreeCopy;
    /* panel */
    private JPanel panelRight;
    private JPanel panelLeft ;
    /* bordure */
    private BorderPanel backgroundPanel;

    // boutons du menu
    private JMenuBar menuBar ;
    private JSeparator sep1;
    private JSeparator sep2;
    private JSeparator sep3;
    private JSeparator sep4;
    private MyMenu menuArbo = null;
    private MyMenuItem menuItem1 = null;
    private MyMenuItem menuItem2 = null;
    private MyMenuItem menuItem3 = null;
    private MyMenuItem menuItem4 = null;
    private MyMenuItem menuItem5 = null;
    private MyMenuItem menuItem6 = null;
    private MyMenuItem menuItem7 = null;
    private MyMenuItem menuItem8 = null;
    private MyMenuItem menuItem9 = null;
    private MyMenuItem menuItemAddQ = null;
    private MyMenuItem menuItemAddE = null;
    private MyMenuItem menuItemAddA = null;
    private MyMenuItem menuItemComm = null;
    private MyMenuItem menuItemUndo = null;
    private MyMenuItem menuItemRedo = null;
    private MyMenuItem menuItemCut = null;
    private MyMenuItem menuItemCopy = null;
    private MyMenuItem menuItemPaste = null;
    private MyMenuItem menuItemSuppr = null;
    private MyMenuItem menuItemPrint = null;
    private MyMenuItem menuItemHelp = null;

    //
    private JSplitPane splitPaneFrame = null;
    // panel de droite
    private JLayeredPane layeredPane = null;
    private MaterialPanel panelMaterial = null;
    private DataSheetPanel panelDataSheet = null;
    private MySeparator sepMaterial = null;
    private MySeparator sepDataSheet = null;
    // panel de gauche
    private CopexTabbedPane tabbedPaneProc = null;
    private JScrollPane scrollPaneTabbedPane = null;

    /* liste des images des taches */
    private ArrayList<CopexImage> listTaskImage;

    /* chemin fichier export datasheet */
    private String currentExportPath = null;

    private boolean openQuestionDialog;

    /* action du panel*/
    private EdPAction action;

    public EdPPanel(){
        this.idUser = "1";
        this.dbKeyMission = 1;
        this.mode = MyConstants.COPEX_MODE;
        this.userName = "";
        this.firstName = "";
        this.listTaskImage = new ArrayList();
        initEdP(null, null);
    }


    public EdPPanel(CopexApplet applet, String idUser, long dbKeyMission, int mode, String userName, String firstName) {
        this.idUser = idUser;
        this.dbKeyMission = dbKeyMission;
        this.mode = mode;
        this.userName = userName;
        this.firstName = firstName;
        this.listTaskImage = new ArrayList();
        initEdP(applet, null);
    }

    public EdPPanel(URL copexURL, String idUser, long dbKeyMission, int mode, String userName, String firstName) {
        this.idUser = idUser;
        this.dbKeyMission = dbKeyMission;
        this.mode = mode;
        this.userName = userName;
        this.firstName = firstName;
        this.listTaskImage = new ArrayList();
        initEdP(null, copexURL);
    }


     /**
    * Instancie l'objet EdPAction.
    * @param action EdPAction
    */
    public void addEdPAction(EdPAction action){
        this.action=action;
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

    /**
     * initialisation de l'applet
     */
    public void initEdP(CopexApplet applet, URL url){
        setLayout(new BorderLayout());
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
            System.out.println("ERREUR lors du chargement de copex, la langue spécifiée "+locale+" n'existe pas : "+e);
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
      initPanel() ;
      setCursor(new Cursor(Cursor.WAIT_CURSOR));
      if(applet == null)
        if(url != null){
            this.controller = new CopexControllerDB(this, url);
        }else
            this.controller = new CopexController(this) ;
      else{
          this.controller = new CopexControllerAppletDB(this, applet);
      }
      //long dbKeyUser = 1;
      //long dbKeyMission = 1;
      listProc = new ArrayList();
      procActiv = null;
      mission = null;
      setMenuBar();
      setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
      displayPanelRightComponent("initEdp");
    }

    /* affichage composants */
    private void displayPanelRightComponent(String txt){
//        System.out.println("****"+txt);
//        if (panelRight != null){
//            int nb = panelRight.getComponentCount();
//            System.out.println("nb Composants : "+nb);
//            for (int i=0; i<nb; i++){
//                System.out.println("comp "+i+" :"+panelRight.getComponent(i) == null ?"" : panelRight.getComponent(i).getName());
//                System.out.println(panelRight.getComponent(i).getClass());
//            }
//        }
//        System.out.println("**************");
    }

    /* chargement des données */
    public void loadData(){
      setCursor(new Cursor(Cursor.WAIT_CURSOR));
       // appel au noyau : chargement des données
      String logFileName = "logFile"+CopexUtilities.getCurrentDate()+"-"+dbKeyMission+"-"+idUser+".xml";
      CopexReturn cr = this.controller.initEdP(locale, idUser, dbKeyMission, mode, userName, firstName, logFileName);
      if (cr.isError()){
          setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
          System.out.println("erreur chargement des données ....");
          displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
          this.stop();
      }
      setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    /* initialisation de l'application avec les données */
    public void initEdp(CopexMission mission, ArrayList<LearnerProcedure> listProc, ArrayList<InitialProcedure> listInitProc, ArrayList<PhysicalQuantity> listPhysicalQuantity) {
       setCursor(new Cursor(Cursor.WAIT_CURSOR));
       // mise à jour des données :
       this.mission = mission;
       this.listProc = listProc;
       this.listInitProc = listInitProc ;
       this.listPhysicalQuantity = listPhysicalQuantity ;
       // determine le proc actif:
       for (Iterator iter=listProc.iterator();iter.hasNext();){
           LearnerProcedure p = (LearnerProcedure)iter.next();
           if (p.isActiv(mission)){
               this.procActiv = p;
               break;
           }
       }
       // mise à jour graphique
       setPanels();
       if(isMaterialAvailable()){
            panelMaterial.setPanelDetailsShown();
            panelMaterial.setButtonEnabled(true);
       }else{
           //panelMaterial.setPanelDetailsHide();
           // panelMaterial.setButtonEnabled(false);
       }
       if (this.controller.useDataSheet())
           panelDataSheet.setPanelDetailsShown();
       if(! canAddProc())
            getTabbedPaneProc().setAddProcDisabled();
       getTabbedPaneProc().beginRegister();
       JScrollBar vsb = this.scrollPaneTabbedPane.getVerticalScrollBar() ;
       this.scrollPaneTabbedPane.getVerticalScrollBar().setValues(0, getTabbedPaneProc().getHeight(), 0, vsb.getMaximum());
       updateMenu();
       // augmente temps d'affichage du tooltiptext
       ToolTipManager.sharedInstance().setDismissDelay(10000);
       ToolTipManager.sharedInstance().setInitialDelay(0);
       setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
       //if(openQuestionDialog )
       //     openQuestionDialog();
       Enumeration<Object> list =  UIManager.getDefaults().keys();
       for (Enumeration e = list ; e.hasMoreElements() ;) {
            Object o = e.nextElement();
            //if(o.toString().startsWith("Tree"))
             //   System.out.println(o.toString());
     }
    displayPanelRightComponent("fin loadData");
    }


    /* maj des données */
    public void updateMission(CopexMission mission, ArrayList<LearnerProcedure> listProc, ArrayList<InitialProcedure> listInitProc){
        // mise à jour des données :
       this.mission = mission;
       this.listProc = listProc;
       this.listInitProc = listInitProc ;
    }


    /*retourne la version */
    public String getVersion(){
        return this.version ;
    }

    /* double clic sur l'onglet */
    public void doubleClickTab(CopexTabbedPane tabbedPane, CloseTab closeTab){
        if (tabbedPane instanceof CopexTabbedPane){
            // ouverture de dialog permettant de renommer un proc
            openDialogEditProc();
        }
    }

    /*
     * retourne le controleur
     */
    public ControllerInterface getController(){
        return this.controller;
    }
    
    /* mise en place d'une bordure */
    private void setBorder(){
        backgroundPanel = new BorderPanel();
        backgroundPanel.setBounds(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
        add(backgroundPanel, BorderLayout.CENTER);
    }


    /**
     * initialisation de la barre de menu
     */
    public void setMenuBar(){
        this.menuBar = new JMenuBar();
        this.menuBar.setLayout( null);
        this.menuBar.setSize(this.getWidth(), 28);
        //this.menuBar.setBackground(backgroundColor);
        this.menuBar.setPreferredSize(this.menuBar.getSize());
        menuBar.add(getMenuItemAddQ());
        menuBar.add(getMenuItemAddE());
        menuBar.add(getMenuItemAddA());
        menuBar.add(getMenuItemSuppr());
        menuBar.add(getSep1());
        menuBar.add(getMenuItemCut());
        menuBar.add(getMenuItemCopy());
        menuBar.add(getMenuItemPaste());
        menuBar.add(getSep2());
        menuBar.add(getMenuArbo());
        menuBar.add(getMenuItemComm());
        menuBar.add(getSep3());
        menuBar.add(getMenuItemUndo());
        menuBar.add(getMenuItemRedo());
        menuBar.add(getSep4());
        menuBar.add(getMenuItemPrint());
        menuBar.add(getMenuItemHelp());
        menuBar.setBounds(0, 0, this.getWidth(), menuBar.getHeight());
        backgroundPanel.add(menuBar, BorderLayout.NORTH);
    }

    private JSeparator getSep1(){
        if (sep1 == null){
            sep1 = new JSeparator(JSeparator.VERTICAL);
            sep1.setBackground(Color.DARK_GRAY);
            sep1.setBounds(menuItemSuppr.getX()+menuItemSuppr.getWidth(), 0, 5, menuBar.getHeight());
        }
        return sep1;
    }
    private JSeparator getSep2(){
        if (sep2 == null){
            sep2 = new JSeparator(JSeparator.VERTICAL);
            sep2.setBackground(Color.DARK_GRAY);
            sep2.setBounds(menuItemPaste.getX()+menuItemPaste.getWidth(), 0, 5, menuBar.getHeight());
        }
        return sep2;
    }
    private JSeparator getSep3(){
        if (sep3 == null){
            sep3 = new JSeparator(JSeparator.VERTICAL);
            sep3.setBackground(Color.DARK_GRAY);
            sep3.setBounds(menuItemComm.getX()+menuItemComm.getWidth(), 0, 5, menuBar.getHeight());
        }
        return sep3;
    }
    private JSeparator getSep4(){
        if (sep4 == null){
            sep4 = new JSeparator(JSeparator.VERTICAL);
            sep4.setBackground(Color.DARK_GRAY);
            sep4.setBounds(menuItemRedo.getX()+menuItemRedo.getWidth(), 0, 5, menuBar.getHeight());
        }
        return sep4;
    }
    private MyMenuItem getMenuItemAddQ(){
        if (menuItemAddQ == null){
            menuItemAddQ = new MyMenuItem(this, getBundleString("TOOLTIPTEXT_MENU_ADDQ"), menuBar.getBackground(),getCopexImage("Bouton-AdT-28_question.png"), getCopexImage("Bouton-AdT-28_question_surv.png"),  getCopexImage("Bouton-AdT-28_question_clic.png"), getCopexImage("Bouton-AdT-28_question_gris.png"));
            menuItemAddQ.setBounds(0, 0, menuItemAddQ.getWidth(), menuItemAddQ.getHeight());
        }
        return menuItemAddQ;
    }
    private MyMenuItem getMenuItemAddE(){
        if (menuItemAddE == null){
            menuItemAddE = new MyMenuItem(this, getBundleString("TOOLTIPTEXT_MENU_ADDE"),menuBar.getBackground(),getCopexImage("Bouton-AdT-30_etape.png"), getCopexImage("Bouton-AdT-30_etape_survol.png"), getCopexImage("Bouton-AdT-30_etape_clic.png"), getCopexImage("Bouton-AdT-30_etape_grise.png"));
            menuItemAddE.setBounds(menuItemAddQ.getX()+menuItemAddQ.getWidth(), 0, menuItemAddE.getWidth(), menuItemAddE.getHeight());
        }
        return menuItemAddE;
    }
    private MyMenuItem getMenuItemAddA(){
        if (menuItemAddA == null){
            menuItemAddA = new MyMenuItem(this, getBundleString("TOOLTIPTEXT_MENU_ADDA"),menuBar.getBackground(),getCopexImage("Bouton-AdT-30_action.png"), getCopexImage("Bouton-AdT-30_action_survol.png"), getCopexImage("Bouton-AdT-30_action_clic.png"), getCopexImage("Bouton-AdT-30_action_grise.png"));
            menuItemAddA.setBounds(menuItemAddE.getX()+menuItemAddE.getWidth(), 0, menuItemAddA.getWidth(), menuItemAddA.getHeight());
        }
        return menuItemAddA;
    }
    private MyMenu getMenuArbo(){
        if (menuArbo == null){
            levelMenu = 1;
            String toolTipText = getBundleString("TOOLTIPTEXT_MENU_ARBO_GENERAL");
            menuArbo = new MyMenu(this,toolTipText,menuBar.getBackground(),getCopexImage("Bouton-AdT-28_1.png"), getCopexImage("Bouton-AdT-28_1_survol.png"), getCopexImage("Bouton-AdT-28_1_clic.png"), getCopexImage("Bouton-AdT-28_1na.png"));
           // menuArbo.setBackground(this.menuBar.getBackground());
            menuArbo.setBounds(sep2.getX()+sep2.getWidth(), 0, menuArbo.getWidth(), menuArbo.getHeight());

        }
        return menuArbo;
    }
     private MyMenuItem getMenuItemComm(){
        if (menuItemComm == null){
            menuItemComm = new MyMenuItem(this, getBundleString("TOOLTIPTEXT_MENU_COMM"),menuBar.getBackground(),getCopexImage("Bouton-AdT-28_comment-yes.png"), getCopexImage("Bouton-AdT-28_comment-yes_s.png"), getCopexImage("Bouton-AdT-28_comment-yes_c.png"), getCopexImage("Bouton-AdT-28_comment-no.png"));
            menuItemComm.setBounds(menuArbo.getX()+menuArbo.getWidth(), 0, menuItemComm.getWidth(), menuItemComm.getHeight());
        }
        return menuItemComm;
    }
     private MyMenuItem getMenuItemUndo(){
        if (menuItemUndo == null){
            menuItemUndo = new MyMenuItem(this, getBundleString("TOOLTIPTEXT_MENU_UNDO"),menuBar.getBackground(),getCopexImage("Bouton-AdT-28_undo.png"), getCopexImage("Bouton-AdT-28_undo_survol.png"), getCopexImage("Bouton-AdT-28_undo_clic.png"), getCopexImage("Bouton-AdT-28_undo_grise.png"));
            menuItemUndo.setBounds(sep3.getX()+sep3.getWidth(), 0, menuItemUndo.getWidth(), menuItemUndo.getHeight());
        }
        return menuItemUndo;
    }

     private MyMenuItem getMenuItemRedo(){
        if (menuItemRedo == null){
            menuItemRedo = new MyMenuItem(this, getBundleString("TOOLTIPTEXT_MENU_REDO"),menuBar.getBackground(),getCopexImage("Bouton-AdT-28_redo.png"), getCopexImage("Bouton-AdT-28_redo_survol.png"), getCopexImage("Bouton-AdT-28_redo_clic.png"),getCopexImage("Bouton-AdT-28_redo_grise.png"));
            menuItemRedo.setBounds(menuItemUndo.getX()+menuItemUndo.getWidth(), 0, menuItemRedo.getWidth(), menuItemRedo.getHeight());
        }
        return menuItemRedo;
    }
     private MyMenuItem getMenuItemHelp(){
        if (menuItemHelp == null){
            menuItemHelp = new MyMenuItem(this, getBundleString("TOOLTIPTEXT_MENU_HELP"),menuBar.getBackground(),getCopexImage("Bouton-AdT-28_help.png"), getCopexImage("Bouton-AdT-28_help_survol.png"), getCopexImage("Bouton-AdT-28_help_clic.png"), getCopexImage("Bouton-AdT-28_help.png"));
            menuItemHelp.setBounds(menuItemPrint.getX()+menuItemPrint.getWidth(), 0, menuItemHelp.getWidth(), menuItemHelp.getHeight());
        }
        return menuItemHelp;
    }

      private MyMenuItem getMenuItemCut(){
        if (menuItemCut == null){
            menuItemCut = new MyMenuItem(this, getBundleString("TOOLTIPTEXT_MENU_CUT"),menuBar.getBackground(),getCopexImage("Bouton-AdT-28_couper.png"), getCopexImage("Bouton-AdT-28_couper_survol.png"), getCopexImage("Bouton-AdT-28_couper_clic.png"), getCopexImage("Bouton-AdT-28_couper_grise.png"));
            menuItemCut.setSize(menuItemCut.getWidth(), menuItemCut.getHeight());
            menuItemCut.setPreferredSize(new Dimension(menuItemCut.getWidth(), menuItemCut.getHeight()));
            menuItemCut.setBounds(sep1.getX()+sep1.getWidth(), 0, menuItemCut.getWidth(), menuItemCut.getHeight());

        }
        return menuItemCut;
    }
      private MyMenuItem getMenuItemCopy(){
        if (menuItemCopy == null){
            menuItemCopy = new MyMenuItem(this, getBundleString("TOOLTIPTEXT_MENU_COPY"),menuBar.getBackground(),getCopexImage("Bouton-AdT-28_copier.png"), getCopexImage("Bouton-AdT-28_copier_survol.png"), getCopexImage("Bouton-AdT-28_copier_clic.png"), getCopexImage("Bouton-AdT-28_copier_grise.png"));
            menuItemCopy.setSize(menuItemCopy.getWidth(), menuItemCopy.getHeight());
            menuItemCopy.setPreferredSize(new Dimension(menuItemCopy.getWidth(), menuItemCopy.getHeight()));
            menuItemCopy.setBounds(menuItemCut.getX()+menuItemCut.getWidth(), 0,menuItemCopy.getWidth(), menuItemCopy.getHeight());

        }
        return menuItemCopy;
    }
     private MyMenuItem getMenuItemPaste(){
        if (menuItemPaste == null){
            menuItemPaste = new MyMenuItem(this, getBundleString("TOOLTIPTEXT_MENU_PASTE"),menuBar.getBackground(),getCopexImage("Bouton-AdT-28_coller.png"), getCopexImage("Bouton-AdT-28_coller_survol.png"), getCopexImage("Bouton-AdT-28_coller_clic.png"), getCopexImage("Bouton-AdT-28_coller_grise.png"));
            menuItemPaste.setSize(menuItemPaste.getWidth(), menuItemPaste.getHeight());
            menuItemPaste.setPreferredSize(new Dimension(menuItemPaste.getWidth(), menuItemPaste.getHeight()));
            menuItemPaste.setBounds(menuItemCopy.getX()+menuItemCopy.getWidth(), 0, menuItemPaste.getWidth(), menuItemPaste.getHeight());

        }
        return menuItemPaste;
    }

     private MyMenuItem getMenuItemSuppr(){
        if (menuItemSuppr == null){
            menuItemSuppr = new MyMenuItem(this, getBundleString("TOOLTIPTEXT_MENU_SUPPR"),menuBar.getBackground(),getCopexImage("Bouton-AdT-28_supprimer.png"), getCopexImage("Bouton-AdT-28_supprimer_sur.png"), getCopexImage("Bouton-AdT-28_supprimer_cli.png"), getCopexImage("Bouton-AdT-28_supprimer_gri.png"));
            menuItemSuppr.setSize(menuItemSuppr.getWidth(), menuItemSuppr.getHeight());
            menuItemSuppr.setPreferredSize(new Dimension(menuItemSuppr.getWidth(), menuItemSuppr.getHeight()));
            menuItemSuppr.setBounds(menuItemAddA.getX()+menuItemAddA.getWidth(), 0, menuItemSuppr.getWidth(), menuItemSuppr.getHeight());
        }
        return menuItemSuppr;
    }
     private MyMenuItem getMenuItemPrint(){
        if (menuItemPrint == null){
            menuItemPrint = new MyMenuItem(this, getBundleString("TOOLTIPTEXT_MENU_PRINT"),menuBar.getBackground(),getCopexImage("Bouton-AdT-28_pdf.png"), getCopexImage("Bouton-AdT-28_pdf_survol.png"), getCopexImage("Bouton-AdT-28_pdf_clic.png"), getCopexImage("Bouton-AdT-28_pdf_grise.png"));
            menuItemPrint.setSize(menuItemPrint.getWidth(), menuItemPrint.getHeight());
            menuItemPrint.setPreferredSize(new Dimension(menuItemPrint.getWidth(), menuItemPrint.getHeight()));
            menuItemPrint.setBounds(sep4.getX()+sep4.getWidth(), 0, menuItemPrint.getWidth(), menuItemPrint.getHeight());
        }
        return menuItemPrint;
    }

     private MyMenuItem getMenuItem1(){
        if (menuItem1 == null){
            String toolTipText = getBundleString("TOOLTIPTEXT_MENU_ARBO");
            toolTipText = CopexUtilities.replace(toolTipText, 0, "1");
            menuItem1 = new MyMenuItem(this, toolTipText,menuBar.getBackground(),getCopexImage("Bouton-AdT-28_1.png"), getCopexImage("Bouton-AdT-28_1_survol.png"),getCopexImage("Bouton-AdT-28_1_clic.png"), getCopexImage("Bouton-AdT-28_1na.png"));
            menuItem1.setSize(menuItem1.getWidth(), menuItem1.getHeight());
            menuItem1.setPreferredSize(new Dimension(menuItem1.getWidth(), menuItem1.getHeight()));
            menuItem1.setBounds(0, 0, menuItem1.getWidth(), menuItem1.getHeight());
        }
        return menuItem1;
    }
     private MyMenuItem getMenuItem2(){
        if (menuItem2 == null){
            String toolTipText = getBundleString("TOOLTIPTEXT_MENU_ARBO");
            toolTipText = CopexUtilities.replace(toolTipText, 0, "2");
            menuItem2 = new MyMenuItem(this, toolTipText,menuBar.getBackground(),getCopexImage("Bouton-AdT-28_2.png"), getCopexImage("Bouton-AdT-28_2_survol.png"), getCopexImage("Bouton-AdT-28_2_clic.png"), getCopexImage("Bouton-AdT-28_2na.png"));
            menuItem2.setSize(menuItem2.getWidth(), menuItem2.getHeight());
            menuItem2.setPreferredSize(new Dimension(menuItem2.getWidth(), menuItem2.getHeight()));
            menuItem2.setBounds(0, menuItem1.getHeight(), menuItem2.getWidth(), menuItem2.getHeight());
        }
        return menuItem2;
    }
     private MyMenuItem getMenuItem3(){
        if (menuItem3 == null){
            String toolTipText = getBundleString("TOOLTIPTEXT_MENU_ARBO");
            toolTipText = CopexUtilities.replace(toolTipText, 0, "3");
            menuItem3 = new MyMenuItem(this, toolTipText,menuBar.getBackground(),getCopexImage("Bouton-AdT-28_3.png"), getCopexImage("Bouton-AdT-28_3_survol.png"), getCopexImage("Bouton-AdT-28_3_clic.png"), getCopexImage("Bouton-AdT-28_3na.png"));
            menuItem3.setSize(menuItem3.getWidth(), menuItem3.getHeight());
            menuItem3.setPreferredSize(new Dimension(menuItem3.getWidth(), menuItem3.getHeight()));
            menuItem3.setBounds(0, menuItem2.getHeight(), menuItem3.getWidth(), menuItem3.getHeight());
        }
        return menuItem3;
    }
     private MyMenuItem getMenuItem4(){
        if (menuItem4 == null){
            String toolTipText = getBundleString("TOOLTIPTEXT_MENU_ARBO");
            toolTipText = CopexUtilities.replace(toolTipText, 0, "4");
            menuItem4 = new MyMenuItem(this, toolTipText,menuBar.getBackground(),getCopexImage("Bouton-AdT-28_4.png"), getCopexImage("Bouton-AdT-28_4_survol.png"), getCopexImage("Bouton-AdT-28_4_clic.png"), getCopexImage("Bouton-AdT-28_4na.png"));
            menuItem4.setSize(menuItem4.getWidth(), menuItem4.getHeight());
            menuItem4.setPreferredSize(new Dimension(menuItem4.getWidth(), menuItem4.getHeight()));
            menuItem4.setBounds(0, menuItem3.getHeight(), menuItem4.getWidth(), menuItem4.getHeight());
        }
        return menuItem4;
    }
     private MyMenuItem getMenuItem5(){
        if (menuItem5 == null){
            String toolTipText = getBundleString("TOOLTIPTEXT_MENU_ARBO");
            toolTipText = CopexUtilities.replace(toolTipText, 0, "5");
            menuItem5 = new MyMenuItem(this, toolTipText,menuBar.getBackground(),getCopexImage("Bouton-AdT-28_5.png"), getCopexImage("Bouton-AdT-28_5_survol.png"), getCopexImage("Bouton-AdT-28_5_clic.png"), getCopexImage("Bouton-AdT-28_5na.png"));
            menuItem5.setSize(menuItem5.getWidth(), menuItem5.getHeight());
            menuItem5.setPreferredSize(new Dimension(menuItem5.getWidth(), menuItem5.getHeight()));
            menuItem5.setBounds(0, menuItem4.getHeight(), menuItem5.getWidth(), menuItem5.getHeight());
        }
        return menuItem5;
    }
     private MyMenuItem getMenuItem6(){
        if (menuItem6 == null){
            String toolTipText = getBundleString("TOOLTIPTEXT_MENU_ARBO");
            toolTipText = CopexUtilities.replace(toolTipText, 0, "6");
            menuItem6 = new MyMenuItem(this, toolTipText,menuBar.getBackground(),getCopexImage("Bouton-AdT-28_6.png"), getCopexImage("Bouton-AdT-28_6_survol.png"), getCopexImage("Bouton-AdT-28_6_clic.png"), getCopexImage("Bouton-AdT-28_6na.png"));
            menuItem6.setSize(menuItem6.getWidth(), menuItem6.getHeight());
            menuItem6.setPreferredSize(new Dimension(menuItem6.getWidth(), menuItem6.getHeight()));
            menuItem6.setBounds(0, menuItem5.getHeight(), menuItem6.getWidth(), menuItem6.getHeight());
        }
        return menuItem6;
    }
     private MyMenuItem getMenuItem7(){
        if (menuItem7 == null){
            String toolTipText = getBundleString("TOOLTIPTEXT_MENU_ARBO");
            toolTipText = CopexUtilities.replace(toolTipText, 0, "7");
            menuItem7 = new MyMenuItem(this, toolTipText,menuBar.getBackground(),getCopexImage("Bouton-AdT-28_7.png"), getCopexImage("Bouton-AdT-28_7_survol.png"), getCopexImage("Bouton-AdT-28_7_clic.png"), getCopexImage("Bouton-AdT-28_7na.png"));
            menuItem7.setSize(menuItem7.getWidth(), menuItem7.getHeight());
            menuItem7.setPreferredSize(new Dimension(menuItem7.getWidth(), menuItem7.getHeight()));
            menuItem7.setBounds(0, menuItem6.getHeight(), menuItem7.getWidth(), menuItem7.getHeight());
        }
        return menuItem7;
    }

     private MyMenuItem getMenuItem8(){
        if (menuItem8 == null){
            String toolTipText = getBundleString("TOOLTIPTEXT_MENU_ARBO");
            toolTipText = CopexUtilities.replace(toolTipText, 0, "8");
            menuItem8 = new MyMenuItem(this, toolTipText,menuBar.getBackground(),getCopexImage("Bouton-AdT-28_8.png"), getCopexImage("Bouton-AdT-28_8_survol.png"), getCopexImage("Bouton-AdT-28_8_clic.png"), getCopexImage("Bouton-AdT-28_8na.png"));
            menuItem8.setSize(menuItem8.getWidth(), menuItem8.getHeight());
            menuItem8.setPreferredSize(new Dimension(menuItem8.getWidth(), menuItem8.getHeight()));
            menuItem8.setBounds(0, menuItem7.getHeight(), menuItem8.getWidth(), menuItem8.getHeight());
        }
        return menuItem8;
    }
     private MyMenuItem getMenuItem9(){
        if (menuItem9 == null){
            String toolTipText = getBundleString("TOOLTIPTEXT_MENU_ARBO");
            toolTipText = CopexUtilities.replace(toolTipText, 0, "9");
            menuItem9 = new MyMenuItem(this, toolTipText,menuBar.getBackground(),getCopexImage("Bouton-AdT-28_9.png"), getCopexImage("Bouton-AdT-28_9_survol.png"), getCopexImage("Bouton-AdT-28_9_clic.png"), getCopexImage("Bouton-AdT-28_9na.png"));
            menuItem9.setSize(menuItem9.getWidth(), menuItem9.getHeight());
            menuItem9.setPreferredSize(new Dimension(menuItem9.getWidth(), menuItem9.getHeight()));
            menuItem9.setBounds(0, menuItem8.getHeight(), menuItem9.getWidth(), menuItem9.getHeight());
        }
        return menuItem9;
    }
    /*
     * initialisation des 2 panels
     */
    private void setPanels(){
        setPanelLeft();
        setPanelRight();
        splitPaneFrame = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, panelLeft, panelRight);
        splitPaneFrame.setBounds(0, this.menuBar.getHeight(), this.getWidth(), this.getHeight()-this.menuBar.getHeight());
        splitPaneFrame.setOneTouchExpandable(true);
        splitPaneFrame.setDividerLocation(this.getWidth()*2/3);
        splitPaneFrame.setDividerSize(10);
        //splitPaneFrame.resetToPreferredSizes();
        splitPaneFrame.setResizeWeight(0.5D);
        //getContentPane().add(splitPaneFrame,  BorderLayout.CENTER);
        backgroundPanel.add(splitPaneFrame,  BorderLayout.CENTER);
    }

     /**
     * initialisation panel droite : materiel + datasheet
     */
    private void setPanelRight(){
        /*layeredPane = new JLayeredPane();
        this.panelRight.setBounds(this.panelLeft.getWidth(), this.menuBar.getHeight(), this.getWidth()/3, this.getHeight()-this.menuBar.getHeight());
        layeredPane.add(getPanelMaterial(),0);
        layeredPane.setPreferredSize(getPanelMaterial().getPreferredSize());
        layeredPane.setSize(getPanelMaterial().getPreferredSize());
        layeredPane.setBounds(0, 0, layeredPane.getWidth(), layeredPane.getHeight());
        this.panelRight.add(layeredPane);
        sepMaterial = new MySeparator(this.panelRight.getWidth());
        sepMaterial.setBounds(0, layeredPane.getHeight(), sepMaterial.getWidth(), sepMaterial.getHeight());
        this.panelRight.add(sepMaterial);
         */
        this.panelRight.setBounds(this.panelLeft.getWidth(), this.menuBar.getHeight(), this.getWidth()/3, this.getHeight()-this.menuBar.getHeight());
        if(isMaterialAvailable()){
            getPanelMaterial();
            panelMaterial.setBounds(0, 0, panelMaterial.getWidth(), panelMaterial.getHeight());
            this.panelRight.add(panelMaterial);
            if(sepMaterial == null){
                sepMaterial = new MySeparator(this.panelRight.getWidth());
                sepMaterial.setName("sepMaterial");
                sepMaterial.setBounds(0, panelMaterial.getHeight(), sepMaterial.getWidth(), sepMaterial.getHeight());
                this.panelRight.add(sepMaterial);
            }
        }
        if (this.controller.useDataSheet()){
            if(panelMaterial == null)
                this.panelRight.add(getPanelDataSheet(0));
            else
                this.panelRight.add(getPanelDataSheet(panelMaterial.getHeight()+sepMaterial.getHeight()));
            sepDataSheet = new MySeparator(this.panelRight.getWidth());
            sepDataSheet.setName("sepDataSheet");
            sepDataSheet.setBounds(0, this.panelDataSheet.getY()+this.panelDataSheet.getHeight(), sepDataSheet.getWidth(), sepDataSheet.getHeight());
            this.panelRight.add(sepDataSheet);
        }
        this.panelRight.revalidate();
        this.panelRight.repaint();
    }

    /**
     * initialisation panel gauche : arbre
     */
    private void setPanelLeft(){
        this.panelLeft.setBounds(0, this.menuBar.getHeight(), this.getWidth()*2/3,this.getHeight()-this.menuBar.getHeight() );
        //this.panelLeft.setMaximumSize(new Dimension(0, 0));
        getScrollPaneTabbedPane();
        this.panelLeft.add(this.scrollPaneTabbedPane);
        // on ajoute les protocoles
        int size = listProc.size();
        LearnerProcedure procActif = null;
        for (int i=0;i< size; i++){
            LearnerProcedure p = (LearnerProcedure)listProc.get(i);
           if (p != null){
                addProc(p, false);
                if (p.isActiv(this.mission))
                    procActif = p;
           }
        }
        // proc actif
        if (procActif != null){
            getTabbedPaneProc().setSelected(procActif);
        }

    }
    private JScrollPane getScrollPaneTabbedPane(){
        if (scrollPaneTabbedPane == null){
            scrollPaneTabbedPane = new JScrollPane(getTabbedPaneProc());
            scrollPaneTabbedPane.setBounds(0, 0, panelLeft.getWidth(), panelLeft.getHeight());
        }
        return scrollPaneTabbedPane;
    }
    private CopexTabbedPane getTabbedPaneProc(){
        if (tabbedPaneProc == null){
            tabbedPaneProc = new CopexTabbedPane(this);
            //tabbedPaneProc.setBounds(0, 0, panelLeft.getWidth(), panelLeft.getHeight());
        }
        return tabbedPaneProc;
    }
    /*
     * construction du panel materiel
     */
    private CopexPanelHideShow getPanelMaterial(){
        if (panelMaterial == null){
           panelMaterial = new MaterialPanel(this, this.panelRight, this.layeredPane, procActiv == null ? new ArrayList() : procActiv.getInitialProc().getListMaterial(), procActiv == null ? new ArrayList() :procActiv.getListMaterialUse(), procActiv == null ? MyConstants.NONE_RIGHT : procActiv.getRight() );
           panelMaterial.setName("panelMaterial");
        }
        return panelMaterial;
    }


    /*
     * construction du panel data sheet
     */
    private CopexPanelHideShow getPanelDataSheet(int y){
        if (panelDataSheet == null){
            panelDataSheet = new DataSheetPanel(this, this.panelRight);
            panelDataSheet.setName("panelDataSheet");
            if (this.procActiv != null){
                panelDataSheet.setDataSheet(procActiv.getDataSheet());

            }
            panelDataSheet.setBounds(0, y, panelDataSheet.getWidth(), panelDataSheet.getHeight());
        }
        return panelDataSheet;
    }



    /*
     * mise à jour du menu
     */
   public void updateMenu(){
       if (procActiv == null){
        // pas de protocole actif => on grise les menus
           getMenuItemAddQ().setEnabled(false);
           getMenuItemAddE().setEnabled(false);
           getMenuItemAddA().setEnabled(false);
           getMenuArbo().setEnabled(false);
           getMenuItemComm().setEnabled(false);
           getMenuItemUndo().setEnabled(false);
           getMenuItemRedo().setEnabled(false);
           getMenuItemSuppr().setEnabled(false);
           getMenuItemCut().setEnabled(false);
           getMenuItemCopy().setEnabled(false);
           getMenuItemPaste().setEnabled(false);
           getMenuItemPrint().setEnabled(false);
       }else{ // un protocole est actif :
           // ajout d'une sous-question : si un élément est sélectionné et possible d'ajouter une sous question
           getMenuItemAddQ().setEnabled(getTabbedPaneProc().canAddQ());
           // ajout d'une etape : si un element de l'arbre est sel
           getMenuItemAddE().setEnabled(getTabbedPaneProc().canAddE());
           // ajout d'une action : si un element de l'arbre est sel
           getMenuItemAddA().setEnabled(getTabbedPaneProc().canAddA());
           // arbor : mise à jour du menu / arbo du protocole
           getMenuArbo().setEnabled(true);
           updateMenuArbo();
           // commentaires
           getMenuItemComm().setEnabled(getTabbedPaneProc().isComments());
           // undo
            getMenuItemUndo().setEnabled(getTabbedPaneProc().canUndo());
           // redo
           getMenuItemRedo().setEnabled(getTabbedPaneProc().canRedo());
           // cut :
           getMenuItemCut().setEnabled(getTabbedPaneProc().canCut());
           // copy
           getMenuItemCopy().setEnabled(getTabbedPaneProc().canCopy());
           // paste
           boolean canPaste = getTabbedPaneProc().canPaste();
           getMenuItemPaste().setEnabled(canPaste);
           // suppr
           getMenuItemSuppr().setEnabled(getTabbedPaneProc().canSuppr());
           // print
           getMenuItemPrint().setEnabled(true);
       }
       repaint();
   }

    /* peut on coller */
    public boolean canPaste(){
       return subTreeCopy != null;
    }

   // mise à jour du menu arbo :
   private void updateMenuArbo(){
       updateLevel(getTabbedPaneProc().getLevel());
   }
    public void updateLevel(int level){
        // dans le menu arboresence on met jusqu'au niveau demande
        getMenuArbo().removeAll();
        menuItem1 = null;
        menuItem2 = null;
        menuItem3 = null;
        menuItem4 = null;
        menuItem5 = null;
        menuItem6 = null;
        menuItem7 = null;
        menuItem8 = null;
        menuItem9 = null;

        int maxL = Math.min(level, 9); // au dessus de 9 on ne gere plus les niveaux !!
        for (int i=1; i<=maxL; i++){
            if (i == 1)
                getMenuArbo().add(getMenuItem1());
            if (i == 2)
                getMenuArbo().add(getMenuItem2());
            if (i == 3)
                getMenuArbo().add(getMenuItem3());
            if (i == 4)
                getMenuArbo().add(getMenuItem4());
            if (i == 5)
                getMenuArbo().add(getMenuItem5());
            if (i == 6)
                getMenuArbo().add(getMenuItem6());
            if (i == 7)
                getMenuArbo().add(getMenuItem7());
            if (i == 8)
                getMenuArbo().add(getMenuItem8());
            if (i == 9)
                getMenuArbo().add(getMenuItem9());
        }
    }
    /*
     * evenement souris
     */
    public void clickMenuEvent(MyMenuItem item){
        if (item == getMenuItemAddQ()){
            this.controller.addQuestion();
        }else if (item == getMenuItemAddE()){
            this.controller.addEtape();
        }else if (item == getMenuItemAddA()){
            this.controller.addAction();
        }else if (item == getMenuItemPrint()){
            this.controller.print();
        }else if (item == getMenuItemComm()){
            setDisplayComments();
        }else if (item == getMenuItem1()){
            displayLevel(1);
        }else if (item == getMenuItem2()){
            displayLevel(2);
        }else if (item == getMenuItem3()){
            displayLevel(3);
        }else if (item == getMenuItem4()){
            displayLevel(4);
        }else if (item == getMenuItem5()){
            displayLevel(5);
        }else if (item == getMenuItem6()){
            displayLevel(6);
        }else if (item == getMenuItem7()){
            displayLevel(7);
        }else if (item == getMenuItem8()){
            displayLevel(8);
        }else if (item == getMenuItem9()){
            displayLevel(9);
        }else if (item == getMenuItemSuppr()){
            this.suppr();
        }else if (item == getMenuItemCopy()){
            CopexReturn cr = this.controller.copy();
            if (cr.isError()){
                displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            }
        }else if (item == getMenuItemPaste()){
            setCursor(new Cursor(Cursor.WAIT_CURSOR));
            CopexReturn cr = this.controller.paste();
            if (cr.isError()){
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            }
             setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }else if (item == getMenuItemCut()){
            setCursor(new Cursor(Cursor.WAIT_CURSOR));
            CopexReturn cr = this.controller.cut();
            if (cr.isError()){
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            }
             setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }else if (item.equals(getMenuItemUndo())){
            setCursor(new Cursor(Cursor.WAIT_CURSOR));
            getTabbedPaneProc().undo();
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }else if (item.equals(getMenuItemRedo())){
            setCursor(new Cursor(Cursor.WAIT_CURSOR));
            getTabbedPaneProc().redo();
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }else if (item == getMenuItemHelp()){
            this.openDialogHelp();
        }
    }
    

    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        setBackground(new java.awt.Color(236, 233, 216));
    }// </editor-fold>


    // Variables declaration - do not modify
    // End of variables declaration


    /* ouverture de la fenetre de dialogue permettant la creation d'une sous question */
    public void openDialogAddQ() {
        QuestionDialog addQ = new QuestionDialog(this);
        addQ.setVisible(true);
    }

    /* ouverture de la fenetre de dialogue permettant la creation d'une etape */
    public void openDialogAddE() {
        LearnerProcedure proc = getProcActiv();
        if (proc == null)
            return;
        InitialProcedure initProc = proc.getInitialProc() ;
        StepDialog addE = new StepDialog(this, initProc.isTaskRepeat());
        addE.setVisible(true);
    }
    // ouverture de la fenetre de dialoguer permettant d'ajouter une action
    public void openDialogAddA() {
        LearnerProcedure proc = getProcActiv();
        if (proc == null)
            return;
        InitialProcedure initProc = proc.getInitialProc() ;
        ActionDialog2 addA = new ActionDialog2(this, initProc.isFreeAction(), initProc.getListNamedAction(), this.listPhysicalQuantity, initProc.isTaskRepeat());
        addA.setVisible(true);
    }

    /* ouverture de la fenetre d'impression */
    public void openDialogPrint() {
        PrintDialog printD = new PrintDialog(this);
        printD.setVisible(true);
    }

     /* ouverture de la fenetre d'aide */
    public void openDialogHelp() {
        CopexReturn cr = this.controller.openHelpDialog();
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
        }
        HelpDialog helpD = new HelpDialog(this);
        helpD.setVisible(true);
    }
    /* ouverture de la fenetre d'ajout d'un tableau pour resultat */
    public void openDialogDataSheet() {
        DataSheetDialog dataSheetD;
        procActiv = getProcActiv();
        if (procActiv != null){
            DataSheet dataSheet = procActiv.getDataSheet();
            if (dataSheet == null){
                dataSheetD = new DataSheetDialog(this, procActiv);
            }else{
                dataSheetD = new DataSheetDialog(this, procActiv, dataSheet.getNbRows(), dataSheet.getNbColumns());
            }
            dataSheetD.setVisible(true);
        }
    }

    /* ouverture de la fenetre de dialogue pour fermer un protocole */
     public void openDialogCloseProc(LearnerProcedure proc) {
        CloseProcDialog closeD = new CloseProcDialog(this, controller, proc);
        closeD.setVisible(true);
    }

     /* ouverture de la fenetre de dialogue qui permet de creer un nouveau protocole */
     public void openDialogAddProc() {
         if ( !CAN_ADD_PROC)
             return ;
         // liste des protocoles à copier :
         ArrayList v = new ArrayList();
         CopexReturn cr = this.controller.getListProcToCopyOrOpen(v);
         if (cr.isError()){
             displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
             return;
         }
         ArrayList<LearnerProcedure> listProcOn = (ArrayList)v.get(0);
         ArrayList<CopexMission> listMission = (ArrayList) v.get(1);
         ArrayList<ArrayList<LearnerProcedure>> listProcMission = (ArrayList<ArrayList<LearnerProcedure>>) v.get(2);
         AddProcDialog addProcD = new AddProcDialog(this, this.controller, listProcOn, listMission, listProcMission, listInitProc);
         addProcD.setVisible(true);
    }

     /* ajout d'un protocole */
     public void addProc(LearnerProcedure proc){
         addProc(proc, true);
     }
    /*ajout d'un protocole */
    private void addProc(LearnerProcedure proc, boolean addList){
        System.out.println("addProc");
        if (addList)
            listProc.add(proc);
       procActiv = proc;
       CopexTree treeProc = new CopexTree(this, proc, proc.getInitialProc(), this.controller, this.panelLeft.getWidth());
       getTabbedPaneProc().addTab(proc.getName(), treeProc);
       updateMenu();
    }

    // impression
    public void printCopex(boolean printProc, boolean printComments, boolean printDataSheet){
        LearnerProcedure proc = null;
        if (printProc)
            proc = getProcActiv() ;
        CopexReturn cr = controller.printCopex(proc, printComments, printDataSheet);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
        }
    }


    /* Affichage ou non des commentaires */
    private void setDisplayComments(){
        switch (modeComments){
            case MyConstants.COMMENTS :
                // on enleve commentaires et on met à jour le bouton du menu
                modeComments = MyConstants.NO_COMMENTS;
                getTabbedPaneProc().setComments(modeComments);
                getMenuItemComm().setItemIcon(getCopexImage("Bouton-AdT-28_comment-no.png"));
                getMenuItemComm().setItemClicIcon(getCopexImage("Bouton-AdT-28_comment-no_cl.png"));
                getMenuItemComm().setItemRolloverIcon(getCopexImage("Bouton-AdT-28_comment-no_su.png"));
                getMenuItemComm().setToolTipText(getBundleString("TOOLTIPTEXT_MENU_COMM"));
                break;
            case MyConstants.NO_COMMENTS :
                modeComments = MyConstants.COMMENTS;
                getTabbedPaneProc().setComments(modeComments);
                getMenuItemComm().setItemIcon(getCopexImage("Bouton-AdT-28_comment-yes.png"));
                getMenuItemComm().setItemClicIcon(getCopexImage("Bouton-AdT-28_comment-yes_c.png"));
                getMenuItemComm().setItemRolloverIcon(getCopexImage("Bouton-AdT-28_comment-yes_s.png"));
                getMenuItemComm().setToolTipText(getBundleString("TOOLTIPTEXT_MENU_NO_COMM"));
                break;
        }
        repaint();
    }

    /* ajout d'une nouvelle action */
    public CopexReturn addAction(CopexAction newAction){
        // determine le protocole actif et la position de l'ajout de l'action
        TaskSelected ts = getTabbedPaneProc().getTaskSelected();
        if (ts == null || ts.getProc() == null )
            return new CopexReturn(getBundleString("MSG_ERROR_ADD_ACTION"), false);

        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.addAction(newAction, ts.getProc(), ts.getTaskBrother(),ts.getTaskParent(), v);
        if (cr.isError())
            return new CopexReturn(getBundleString("MSG_ERROR_ADD_ACTION"), false);
        LearnerProcedure newProc = (LearnerProcedure)v.get(0);
        updateProc(newProc);
        getTabbedPaneProc().addTask(newAction,ts);
        getTabbedPaneProc().addEdit_addTask(getTabbedPaneProc().getTaskSelected(newAction), ts);
        updateMenu();
        return new CopexReturn();
    }

    /* modification action */
    public CopexReturn updateAction(CopexAction newAction){
        // determine le protocole actif et la position de l'ajout de l'action
        TaskSelected ts = getTabbedPaneProc().getTaskSelected();
        if (ts == null || ts.getProc() == null || ts.getSelectedTask() == null)
            return new CopexReturn(getBundleString("MSG_ERROR_UPDATE_ACTION"), false);
        CopexTask  t = ts.getSelectedTask();
        if (!t.isAction())
            return new CopexReturn(getBundleString("MSG_ERROR_UPDATE_ACTION"), false);
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.updateAction(newAction, ts.getProc(), (CopexAction)t, v);
        if (cr.isError())
            return new CopexReturn(getBundleString("MSG_ERROR_UPDATE_ACTION"), false);
        LearnerProcedure newProc = (LearnerProcedure)v.get(0);
        getTabbedPaneProc().updateProc(newProc);
        getTabbedPaneProc().updateTask(newAction, ts);
        updateMenu();
        return new CopexReturn();
    }

    /* ajout d'une nouvelle étape */
    public CopexReturn addStep(Step newStep){
        // determine le protocole actif et la position de l'ajout de l'étape
        TaskSelected ts = getTabbedPaneProc().getTaskSelected();
        if (ts == null || ts.getProc() == null )
            return new CopexReturn(getBundleString("MSG_ERROR_ADD_STEP"), false);

        ArrayList v = new ArrayList();
       CopexReturn cr = this.controller.addStep(newStep, ts.getProc(), ts.getTaskBrother(),ts.getTaskParent(), v);
        if (cr.isError())
            return new CopexReturn(getBundleString("MSG_ERROR_ADD_STEP"), false);
        LearnerProcedure newProc = (LearnerProcedure)v.get(0);
        getTabbedPaneProc().updateProc(newProc);
        getTabbedPaneProc().addTask(newStep, ts);
        getTabbedPaneProc().addEdit_addTask(getTabbedPaneProc().getTaskSelected(newStep), ts);
        updateMenu();
        return new CopexReturn();
    }

    /* modification étape */
    public CopexReturn updateStep(Step newStep){
        // determine le protocole actif
        TaskSelected ts = getTabbedPaneProc().getTaskSelected();
        if (ts == null || ts.getProc() == null || ts.getSelectedTask() == null)
            return new CopexReturn(getBundleString("MSG_ERROR_UPDATE_STEP"), false);
        CopexTask  t = ts.getSelectedTask();
        if (!t.isStep())
            return new CopexReturn(getBundleString("MSG_ERROR_UPDATE_STEP"), false);
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.updateStep(newStep, ts.getProc(), (Step)t, v);
        if (cr.isError())
            return new CopexReturn(getBundleString("MSG_ERROR_UPDATE_STEP"), false);
        LearnerProcedure newProc = (LearnerProcedure)v.get(0);
        getTabbedPaneProc().updateProc(newProc);
        getTabbedPaneProc().updateTask(newStep,ts);
        updateMenu();
        return new CopexReturn();
    }

    /* modification sous question */
    public CopexReturn updateQuestion(Question newQuestion){
        // determine le protocole actif
        TaskSelected ts = getTabbedPaneProc().getTaskSelected();
        if (ts == null || ts.getProc() == null || ts.getSelectedTask() == null)
            return new CopexReturn(getBundleString("MSG_ERROR_UPDATE_QUESTION"), false);
        CopexTask  t = ts.getSelectedTask();
        if (!t.isQuestion())
            return new CopexReturn(getBundleString("MSG_ERROR_UPDATE_QUESTION"), false);
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.updateQuestion(newQuestion, ts.getProc(), (Question)t, v);
        if (cr.isError())
            return new CopexReturn(getBundleString("MSG_ERROR_UPDATE_QUESTION"), false);
        LearnerProcedure newProc = (LearnerProcedure)v.get(0);
        getTabbedPaneProc().updateProc(newProc);
        getTabbedPaneProc().updateTask(newQuestion,ts);
        updateMenu();
        return new CopexReturn();
    }

     /* ajout d'une nouvelle sous question */
    public CopexReturn addQuestion(Question newQuestion){
        // determine le protocole actif et la position de l'ajout de la sous question
        TaskSelected ts = getTabbedPaneProc().getTaskSelected();
        if (ts == null || ts.getProc() == null )
            return new CopexReturn(getBundleString("MSG_ERROR_ADD_QUESTION"), false);

        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.addQuestion(newQuestion, ts.getProc(), ts.getTaskBrother(),ts.getTaskParent(), v);
        if (cr.isError())
            return new CopexReturn(getBundleString("MSG_ERROR_ADD_QUESTION"), false);
        LearnerProcedure newProc = (LearnerProcedure)v.get(0);
        updateProc(newProc);
        getTabbedPaneProc().addTask(newQuestion,ts);
        getTabbedPaneProc().addEdit_addTask(getTabbedPaneProc().getTaskSelected(newQuestion), ts);
        updateMenu();
        return new CopexReturn();
    }

    /* affichage d'un niveau d'arboresence */
    private void displayLevel(int level){
        getTabbedPaneProc().displayLevel(level);
        // change icone du menu selon le niveau
        levelMenu = level;
        updateIconMenu();
    }

    /* mise à jour de l'icone du menu */
    private void updateIconMenu(){
        ImageIcon img = getCopexImage("Bouton-AdT-28_"+levelMenu+".png");
        ImageIcon imgSurvol = getCopexImage("Bouton-AdT-28_"+levelMenu+"_survol.png");
        ImageIcon imgClic = getCopexImage("Bouton-AdT-28_"+levelMenu+"_clic.png");
        ImageIcon imgDisabled = getCopexImage("Bouton-AdT-28_"+levelMenu+"na.png");
        getMenuArbo().setImage(img);
        getMenuArbo().setImageSurvol(imgSurvol);
        getMenuArbo().setImageClic(imgClic);
        getMenuArbo().setImageDisabled(imgDisabled);
        getMenuArbo().repaint();
    }


    /* suppression */
    public void suppr(){
        suppr(true);
    }
    /* suppression avec ou non demande de confirmation */
    public void suppr(boolean confirm){
        CopexReturn cr = suppr(getTabbedPaneProc().getTasksSelected(), confirm);
        if (cr.isError()){
            displayError(cr , this.bundle.getString("TITLE_DIALOG_ERROR"));
        }
    }
    /* suppression de la selection de l'arbre */
    public CopexReturn suppr(ArrayList<TaskSelected> listTs, boolean confirm){

        // recupere la selection à supprimer
        if (listTs == null )
            return new CopexReturn(getBundleString("MSG_ERROR_DELETE_TASK"), false);
        // demande de confirmation :
        boolean confirmRemove = true;
        if (confirm){
            CopexReturn cr = new CopexReturn();
            if (listTs.size() > 1)
                cr.setConfirm(getBundleString("MSG_CONFIRM_SUPPR_ALL"));
            else
                cr.setConfirm(getBundleString("MSG_CONFIRM_SUPPR"));
            confirmRemove = displayError(cr, getBundleString("TITLE_DIALOG_WARNING"));
        }
        if (confirmRemove){
            ArrayList v = new ArrayList();
            // duplique la liste en ajoutant les enfants des taches
            ArrayList<TaskSelected> listTsSuppr = getListTs(listTs);

            ArrayList<TaskSelected> list = new ArrayList();
            int nbTs = listTsSuppr.size();
            for (int i=0;i<nbTs; i++){
                CopexTask task = listTsSuppr.get(i).getTaskToAttach();
                 TaskSelected ts = getTabbedPaneProc().getTaskSelected(task);
                 list.add(ts);
            }
            CopexReturn cr = this.controller.suppr(listTs, v, confirm, MyConstants.NOT_UNDOREDO);
            if (cr.isError())
                return new CopexReturn(getBundleString("MSG_ERROR_DELETE_TASK"), false);
            LearnerProcedure newProc = (LearnerProcedure)v.get(0);
            updateProc(newProc);
            getTabbedPaneProc().suppr(listTs);

            if (confirm){ // suppression
                subTreeCopy = null;
                getTabbedPaneProc().addEdit_deleteTask(listTsSuppr, list);
            }else{ // CUT
                addEdit_cut(listTsSuppr, list, subTreeCopy);
            }
        }
        updateMenu();

        return new CopexReturn();
    }

    //retourne la liste des taches à supprimer en ajoutant les enfants
    private ArrayList<TaskSelected> getListTs(ArrayList<TaskSelected> listTs){
        ArrayList<TaskSelected> listT = new ArrayList();
        int nbTs = listTs.size();
        for (int t=0; t<nbTs; t++){
            TaskSelected ts = listTs.get(t);
            listT.add(ts);
            if (ts.getSelectedTask() instanceof Step || ts.getSelectedTask() instanceof Question){
                // recupere les enfants
               ArrayList<CopexTask> lc = listTs.get(t).getListAllChildren();
               // on les ajoute
               int n = lc.size();
               for (int k=0; k<n; k++){
                   // ajoute
                   listT.add(getTabbedPaneProc().getTaskSelected(lc.get(k)));
               }
            }


        }
        // supprimes les occurences multiples
        int nbT = listT.size();
        ArrayList<TaskSelected> lt = new ArrayList();
        ArrayList<TaskSelected> listTClean = new ArrayList();
        for (int t=0; t<nbT; t++){
            int id = getId(lt, listT.get(t).getSelectedTask().getDbKey());
            if (id == -1){
                listTClean.add(listT.get(t));
            }
            lt.add(listT.get(t));
        }
        return listTClean;
    }
     /* cherche un indice dans la liste, -1 si non trouvé */
    private int getId(ArrayList<TaskSelected> listT, long dbKey){
        int nbT = listT.size();
        for (int i=0; i<nbT; i++){
            CopexTask t = listT.get(i).getSelectedTask();
            if (t.getDbKey() == dbKey)
                return i;
        }
        return -1;
    }


    /* creation d'un nouveau protocole */
    public void createProc(LearnerProcedure proc) {
        addProc(proc, true);

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
        }
        listProc.remove(idP);
        getTabbedPaneProc().removeProc(proc);
        if (listProc.size() == 0 && this.controller.useDataSheet())
            this.panelDataSheet.setDataSheet(null);
        updateMenu();
    }

    /* suppression d'un protocole */
    public void deleteProc(LearnerProcedure proc) {
        closeProc(proc);
    }

    /* copie des elements selectionnés */
    public void copy() {
        subTreeCopy = getTabbedPaneProc().copy();
        updateMenu();
    }



    /* retourne le protocole actif */
    public LearnerProcedure getProcActiv() {
        return this.getTabbedPaneProc().getProcActiv();
    }

    public TaskSelected getSelectedTask() {
        return this.getTabbedPaneProc().getTaskSelected();
    }

    /* renommer un protocole*/
    public void updateProcName(LearnerProcedure proc, String name) {
        int idP = getIdProc(proc.getDbKey());
        if (idP == -1)
            displayError(new CopexReturn(getBundleString("MSG_ERROR_RENAME_PROC"), false), getBundleString("TITLE_DIALOG_ERROR"));
        listProc.get(idP).setName(name);
        getTabbedPaneProc().updateProcName(proc, name);
    }

    // ouverture de la fenetre de dialoguer permettant de renommer un protocole
    public void openDialogEditProc() {
        LearnerProcedure proc = getProcActiv();
        if (proc != null){
            EditProcDialog editProcD = new EditProcDialog(this, controller, proc);
            editProcD.setVisible(true);
        }
    }

    /* mise à jour du statut actif d'un protocole */
    public void setActiv(LearnerProcedure p, boolean register){
         this.procActiv = p;
        if (this.controller.useDataSheet() && this.panelDataSheet != null){
            this.panelDataSheet.setDataSheet(procActiv.getDataSheet());
            this.panelDataSheet.setRight(procActiv.getRight() != MyConstants.NONE_RIGHT);
        }
         if(isMaterialAvailable()){
             if(panelMaterial == null){
                getPanelMaterial();
                panelMaterial.setBounds(0, 0, panelMaterial.getWidth(), panelMaterial.getHeight());
                this.panelRight.add(panelMaterial);
                sepMaterial = new MySeparator(this.panelRight.getWidth());
                sepMaterial.setBounds(0, panelMaterial.getHeight(), sepMaterial.getWidth(), sepMaterial.getHeight());
                sepMaterial.setName("sepMaterial2");
                this.panelRight.add(sepMaterial);
             }
         }else{
             if (panelMaterial != null){
                 this.panelRight.remove(panelMaterial);
                 this.panelRight.remove(sepMaterial);
                 sepMaterial = null;
                 panelMaterial = null;
             }
         }
         if (this.panelMaterial != null){
             this.panelMaterial.setListMaterial(procActiv.getRight(), procActiv.getInitialProc().getListMaterial(), procActiv.getListMaterialUse());
             if(isMaterialAvailable()){
                 this.panelMaterial.setPanelDetailsShown();
                 this.panelMaterial.setButtonEnabled(true);
             }else{
                 this.panelMaterial.setPanelDetailsHide();
                 this.panelMaterial.setButtonEnabled(false);
             }
         }
        //resizeHideShowPanel();
        panelRightComponentResized(null);
        if (register){
            p.setActiv(true);
            CopexReturn cr = controller.setProcActiv(p);
            if (cr.isError())
                displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
        }
    }

    /* retourne l'arbre à copier */
    public SubTree getSubTreeCopy() {
        return this.subTreeCopy;
    }

    public void paste(LearnerProcedure proc, ArrayList<CopexTask> listTask, TaskSelected t, char undoRedo) {
        int idP = getIdProc(proc.getDbKey());
        if (idP != -1)
            this.listProc.set(idP, proc);
        getTabbedPaneProc().updateProc(proc);
        getTabbedPaneProc().addTasks(listTask, getSubTreeCopy(), t);
        updateMenu();
        //if (undoRedo == CopexUtilities.NOT_UNDOREDO){
            ArrayList<TaskSelected> listTs = new ArrayList();
            int nbT = listTask.size();
            for (int i=0; i<nbT; i++){
                TaskSelected ts = getTabbedPaneProc().getTaskSelected(listTask.get(i));
                listTs.add(ts);
            }
            addEdit_paste(subTreeCopy, t, listTs);
        //}
    }

   /* replace les differents panneaux dans le panel droite */
    public void resizeHideShowPanel(){
        /* if(layeredPane != null){
            layeredPane.setSize(getPanelMaterial().getSize());
            layeredPane.setBounds(0, 0, layeredPane.getWidth(), layeredPane.getHeight());
            sepMaterial.setBounds(0, layeredPane.getHeight(), panelRight.getWidth(), sepMaterial.getHeight());
            if (this.controller.useDataSheet())
                this.panelDataSheet.setBounds(0, layeredPane.getHeight()+sepMaterial.getHeight(), panelRight.getWidth(), panelDataSheet.getHeight());
            //layeredPane.repaint();

        }
         */
         if( panelMaterial !=null){
            panelMaterial.setBounds(0, 0, panelMaterial.getWidth(), panelMaterial.getHeight());
            sepMaterial.setBounds(0, panelMaterial.getHeight(), panelRight.getWidth(), sepMaterial.getHeight());
            if (this.controller.useDataSheet()&& panelDataSheet != null) {
                this.panelDataSheet.setBounds(0, panelMaterial.getHeight()+sepMaterial.getHeight()+10, panelRight.getWidth(), panelDataSheet.getHeight());
            }

         }else if (!isMaterialAvailable()){
             if (this.controller.useDataSheet() && panelDataSheet != null){
                this.panelDataSheet.setBounds(0, 0, panelRight.getWidth(), panelRight.getHeight()-MySeparator.HEIGHT_SEP);
             }
         }
         if (this.controller.useDataSheet() && sepDataSheet != null){
                sepDataSheet.setBounds(0, panelDataSheet.getHeight()+ panelDataSheet.getY(), panelRight.getWidth(), sepDataSheet.getHeight());
            }
         this.panelRight.revalidate();
         this.panelRight.repaint();

    }

    /* creation d'une feuille de données */
    public void createDataSheet(LearnerProcedure proc) {
        int idP = getIdProc(proc.getDbKey());
        if (idP == -1){
            displayError(new CopexReturn(getBundleString("MSG_ERROR_CREATE_DATASHEET"), false), getBundleString("TITLE_DIALOG_ERROR"));
        }
        listProc.get(idP).setDataSheet(proc.getDataSheet());
        this.panelDataSheet.setDataSheet(proc.getDataSheet());
    }

    /* mise à jour d'une donnée du tableau DataSheet */
    public void updateDataSheet(String value, int noRow, int noCol){
        DataSheet datasheet = procActiv.getDataSheet();
        String oldValue = "";
        if (datasheet.getDataAt(noRow, noCol) != null)
            oldValue = datasheet.getDataAt(noRow, noCol).getData() ;
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.updateDataSheet(procActiv, value, noRow, noCol, v, MyConstants.NOT_UNDOREDO);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        CopexData newData = (CopexData)v.get(0);
        editDataSheet(datasheet, newData, noRow, noCol);
       addEdit_editDataSheet(datasheet, oldValue, value, noRow, noCol);
    }

    public void editDataSheet(DataSheet dataSheet, CopexData data, int noRow, int noCol){
        dataSheet.setValueAt(data, noRow, noCol);
        this.panelDataSheet.updateDataSheet(dataSheet);
    }
    /* modification d'une feuille de données*/
    public void updateDataSheet(LearnerProcedure proc) {
        int idP = getIdProc(proc.getDbKey());
        if (idP == -1){
            displayError(new CopexReturn(getBundleString("MSG_ERROR_UPDATE_DATASHEET"), false), getBundleString("TITLE_DIALOG_ERROR"));
        }
        listProc.get(idP).setDataSheet(proc.getDataSheet());
        this.panelDataSheet.setDataSheet(proc.getDataSheet());
    }

    
    /* mise à jour proc*/
     public void updateProc(LearnerProcedure proc) {
        int idP = getIdProc(proc.getDbKey());
        if (idP == -1)
            displayError(new CopexReturn(getBundleString("MSG_ERROR_DRAG_AND_DROP"), false), getBundleString("TITLE_DIALOG_ERROR"));
        listProc.set(idP, proc);
        getTabbedPaneProc().updateProc(proc);
    }

     /* renvoit vrai si aucun proc n'est ouvert */
     public boolean noProc(){
         return listProc.size() == 0;
     }

     

     

    



     public String getDirectoryPhp(){
        return "../editeurProtocole/InterfaceServer/";
      }

    
    private void panelRightComponentResized(java.awt.event.ComponentEvent evt){
        int  height = this.panelRight.getHeight() / 2 - (2*MySeparator.HEIGHT_SEP);
        if(!this.controller.useDataSheet())
            height = this.panelRight.getHeight() - MySeparator.HEIGHT_SEP  ;
        if(panelMaterial == null)
            height =this.panelRight.getHeight() - MySeparator.HEIGHT_SEP ;
        if (panelMaterial != null)
            this.panelMaterial.resizePanelDetails(this.panelRight.getWidth(), height);
        if (this.controller.useDataSheet() && panelDataSheet != null)
            this.panelDataSheet.resizePanelDetails(this.panelRight.getWidth(), height);
        resizeHideShowPanel();
    }

    private void initPanel(){
        setBorder();
        panelRight = new javax.swing.JPanel();

        panelLeft = new javax.swing.JPanel();

        panelRight.setName("panelRight"); // NOI18N
        panelRight.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                panelRightComponentResized(evt);
            }
        });
        panelLeft.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                panelLeftComponentResized(evt);
            }
        });
        panelRight.setLayout(null);
        panelRight.setBackground(backgroundColor);
        backgroundPanel.add(panelRight, java.awt.BorderLayout.CENTER);

        panelLeft.setName("panelLeft"); // NOI18N
        panelLeft.setLayout(new java.awt.BorderLayout());
        backgroundPanel.add(panelLeft, java.awt.BorderLayout.PAGE_START);
    }

    /* ajout d'un evenement de undo redo pour renommer un protocole */
    public void  addEdit_renameProc(LearnerProcedure proc, String name){
        getTabbedPaneProc().addEdit_renameProc(proc, name);
        updateMenu();
    }

    /* ajout d'un evenement : creation d'un datasheet */
     public void addEdit_createDataSheet(int nbRows, int nbCols, DataSheet dataSheet){
         getTabbedPaneProc().addEdit_createDataSheet(nbRows, nbCols, dataSheet);
         updateMenu();
     }

      /* ajout d'un evenement : modification d'un datasheet */
     public void addEdit_updateDataSheet(DataSheet dataSheet, int oldNbRows, int oldNbCols, int newNbRows, int newNbCols){
         getTabbedPaneProc().addEdit_updateDataSheet(dataSheet, oldNbRows, oldNbCols, newNbRows, newNbCols);
         updateMenu();
     }

     /* ajout d'un evenement : edition d'une donnee d'un datasheet */
     public void addEdit_editDataSheet(DataSheet dataSheet, String oldData, String newData, int noRow, int noCol){
          getTabbedPaneProc().addEdit_editDataSheet(dataSheet, oldData, newData, noRow, noCol);
         updateMenu();
     }


    /* suppression d'une feuille de données */
    public void deleteDataSheet(LearnerProcedure proc) {
       int idP = getIdProc(proc.getDbKey());
        if (idP == -1){
            displayError(new CopexReturn(getBundleString("MSG_ERROR_CREATE_DATASHEET"), false), getBundleString("TITLE_DIALOG_ERROR"));
        }
        listProc.get(idP).setDataSheet(null);
        this.panelDataSheet.setDataSheet(null);
    }

    /* mise à jour de l'arbre */
    public void setSubTree(SubTree subTree){
        this.subTreeCopy = subTree;
    }

     /* ajout d'un evenement : cut */
     public void addEdit_cut(ArrayList<TaskSelected> listTask, ArrayList<TaskSelected> listTs, SubTree subTree ){
         getTabbedPaneProc().addEdit_cut(listTask, listTs, subTree);
         updateMenu();
     }

     /* coller */
      public void addEdit_paste(SubTree subTree, TaskSelected ts, ArrayList<TaskSelected> listTask){
         getTabbedPaneProc().addEdit_paste(subTree, ts, listTask);
         updateMenu();
      }

    public void stop() {
        CopexReturn cr = this.controller.stopEdP();
        if (cr.isError())
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
    }

    /* retourne le chemin et nom du fichier de parametres */
    public String getFileParameters(){
        return "../editeurProtocole/parameters.txt";
    }

   

    /* affichage du proc d'aide */
    public void displayHelpProc(){
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.getHelpProc(v);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        cr = this.controller.openHelpProc();
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
        }
        CopexMission helpMission = (CopexMission)v.get(0);
        LearnerProcedure helpProc = (LearnerProcedure)v.get(1);
        ArrayList<Material> listMat = (ArrayList<Material>)v.get(2);
        if (getIdProc(helpProc.getDbKey()) == -1)
            addProc(helpProc);
        else
            getTabbedPaneProc().setSelected(helpProc);
    }

    /* fermeture fenetre aide*/
    public void closeHelpDialog(){
        CopexReturn cr = this.controller.closeHelpDialog();
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
        }
    }

    public boolean addMaterialUseForProc(Material m, String j, char undoRedo){
        LearnerProcedure p = getProcActiv() ;
        CopexReturn cr = this.controller.addMaterialUseForProc(p, m, j, undoRedo);
        if (cr.isError()){
            displayError(cr, getBundleString("MSG_ERROR_UPDATE_MATERIAL_USE"));
            return false;
        }
        int idP = getIdProc(p.getDbKey());
        MaterialUseForProc matUse = new MaterialUseForProc(m, j);
        if (idP != -1){
            listProc.get(idP).addMaterialUse(matUse);
        }
        this.panelMaterial.addMaterialUse((MaterialUseForProc)matUse.clone());
        if (undoRedo == MyConstants.NOT_UNDOREDO){
            getTabbedPaneProc().addEdit_addMaterialUseForProc(p, m, j);
        }
        updateMenu();
        return true;
    }

    public boolean updateMaterialUseForProc(Material m, String j, char undoRedo){
        LearnerProcedure p = getProcActiv() ;
        CopexReturn cr = this.controller.updateMaterialUseForProc(p, m, j, undoRedo);
        if (cr.isError()){
            displayError(cr, getBundleString("MSG_ERROR_UPDATE_MATERIAL_USE"));
            return false;
        }
        int idP = getIdProc(p.getDbKey());
        MaterialUseForProc matUse = new MaterialUseForProc(m, j);
        String oldJ = "";
        if (idP != -1){
            MaterialUseForProc  oldMatUse = listProc.get(idP).getMaterialUse(m);
            if (oldMatUse != null && oldMatUse.getJustification() != null)
                oldJ = new String(oldMatUse.getJustification());
            listProc.get(idP).updateMaterialUse(matUse);
        }
        this.panelMaterial.updateMaterialUse((MaterialUseForProc)matUse.clone());
        if (undoRedo == MyConstants.NOT_UNDOREDO){
            getTabbedPaneProc().addEdit_updateMaterialUseForProc(p, m, oldJ, j);
        }
         updateMenu();
        return true;
    }

    /* supprime l'utilisation de ce materiel*/
    public void removeMaterialUse(Material m, char undoRedo){
        LearnerProcedure p = getProcActiv() ;
        CopexReturn cr = this.controller.removeMaterialUseForProc(p, m, undoRedo);
        if (cr.isError()){
            displayError(cr, getBundleString("MSG_ERROR_UPDATE_MATERIAL_USE"));
            return;
        }
        int idP = getIdProc(p.getDbKey());
        String justification = "";
        if (idP != -1){
            ArrayList<MaterialUseForProc> listMat = listProc.get(idP).getListMaterialUse();
            if (listMat != null){
                int id = -1;
                int n = listMat.size();
                for (int i=0; i<n; i++){
                    if (listMat.get(i).getMaterial().getDbKey() == m.getDbKey()){
                        id = i;
                        justification = listMat.get(i).getJustification() == null ? "" : new String(listMat.get(i).getJustification());
                        break;
                    }
                }
                if (id !=-1){
                    listMat.remove(id);
                }
            }
        }
        this.panelMaterial.removeMaterialUse(m);
        if (undoRedo == MyConstants.NOT_UNDOREDO){
            getTabbedPaneProc().addEdit_removeMaterialUseForProc(p, m, justification);
        }
         updateMenu();
    }
    /* retourne vrai s'il existe un panel datasheet */
    public boolean useDataSheet(){
        return this.controller.useDataSheet() ;
    }

    /* resize arbre*/
    private void panelLeftComponentResized(java.awt.event.ComponentEvent evt){
        if (this.tabbedPaneProc != null){
            this.tabbedPaneProc.resizeWidth(this.panelLeft.getWidth());
        }
     }

    /* retourne la liste des materiels de la mission de ce type de materiel */
    public ArrayList<Material> getListMaterial(TypeMaterial type,TypeMaterial type2, boolean andTypes, boolean modeAdd){
        ArrayList<Material> listMaterial = new ArrayList();
        LearnerProcedure proc = getProcActiv();
        if (proc == null)
            return listMaterial ;
        if(type == null){
            //tout
            return proc.getInitialProc().getListMaterial();
        }
        boolean controlType2 = type2 != null;
        long dbKeyType2 = -1;
        if (controlType2)
            dbKeyType2 = type2.getDbKey();
        ArrayList<Material> matMision = proc.getInitialProc().getListMaterial() ;
        int nbM = matMision.size();
        for (int i=0; i<nbM; i++){
            if (matMision.get(i).isType(type.getDbKey())){
                if ((controlType2 && matMision.get(i).isType(dbKeyType2)&& andTypes) || !controlType2 || (controlType2 && !andTypes) )
                    listMaterial.add(matMision.get(i));
            }else  if(controlType2 && !andTypes && matMision.get(i).isType(type2.getDbKey())){
                listMaterial.add(matMision.get(i));
            }
        }
        // on ajoute les materiels de ce type qui ont été créé jusqu'à maintenant
        ArrayList<CopexTask> listTaskBefore = getTabbedPaneProc().getListTaskBeforeSel(modeAdd);
        int nb = listTaskBefore.size();
        for (int i=0; i<nb; i++){
            if (listTaskBefore.get(i) instanceof CopexActionManipulation){
                ArrayList<Material> matProd = ((CopexActionManipulation)listTaskBefore.get(i)).getListMaterialProd() ;
                for (int j=0 ;j<matProd.size(); j++){
                    
                    if (matProd.get(j).isType(type.getDbKey())){
                        if ((controlType2 && matProd.get(j).isType(dbKeyType2)) || !controlType2 || (controlType2 && !andTypes)){
                            boolean isInList = false;
                            for (int l=0; l<listMaterial.size(); l++){
                                if (listMaterial.get(l).getName().equals(matProd.get(j).getName())){
                                    isInList = true;
                                    break;
                                }
                            }
                            if (!isInList)
                                listMaterial.add(matProd.get(j));
                        }
                    }else if (controlType2 && !andTypes && matProd.get(j).isType(type2.getDbKey())){
                        boolean isInList = false;
                            for (int l=0; l<listMaterial.size(); l++){
                                if (listMaterial.get(l).getName().equals(matProd.get(j).getName())){
                                    isInList = true;
                                    break;
                                }
                            }
                            if (!isInList)
                                listMaterial.add(matProd.get(j));
                    }
                 }
            }
        }
        return listMaterial ;
    }

    /* retourne la liste du materiel produit jusqu'ici */
    public ArrayList<Material> getMaterialProd(boolean modeAdd){
        ArrayList<Material> listMaterialProd = new ArrayList();
        ArrayList<CopexTask> listTaskBefore = getTabbedPaneProc().getListTaskBeforeSel(modeAdd);
        int nb = listTaskBefore.size();
        for (int i=0; i<nb; i++){
            if (listTaskBefore.get(i) instanceof CopexActionManipulation){
                ArrayList<Material> matProd = ((CopexActionManipulation)listTaskBefore.get(i)).getListMaterialProd() ;
                for (int j=0 ;j<matProd.size(); j++){
                    boolean isInList = false;
                    for (int k=0; k<listMaterialProd.size(); k++){
                        if (listMaterialProd.get(k).getName().equals(matProd.get(j).getName()))
                            isInList = true;
                    }
                    //if (listMaterialProd.indexOf(matProd.get(j)) == -1)
                    if (!isInList)
                        listMaterialProd.add(matProd.get(j));
                }
            }
        }
        return listMaterialProd ;
    }

    /* retourne la liste du data produit jusqu'ici */
    public ArrayList<QData> getDataProd(boolean modeAdd){
        ArrayList<QData> listDataProd = new ArrayList();
        ArrayList<CopexTask> listTaskBefore = getTabbedPaneProc().getListTaskBeforeSel(modeAdd);
        int nb = listTaskBefore.size();
        for (int i=0; i<nb; i++){
            if (listTaskBefore.get(i) instanceof CopexActionAcquisition || listTaskBefore.get(i) instanceof CopexActionTreatment){
                ArrayList<QData> dataProd = new ArrayList();
                if (listTaskBefore.get(i) instanceof CopexActionAcquisition)
                    dataProd = ((CopexActionAcquisition)listTaskBefore.get(i)).getListDataProd() ;
                else if (listTaskBefore.get(i) instanceof CopexActionTreatment)
                    dataProd = ((CopexActionTreatment)listTaskBefore.get(i)).getListDataProd() ;
                for (int j=0 ;j<dataProd.size(); j++){
                    boolean isInList = false;
                    for (int k=0; k<listDataProd.size(); k++){
                        if (listDataProd.get(k).getName().equals(dataProd.get(j).getName()))
                            isInList = true;
                    }
                    //if (listMaterialProd.indexOf(matProd.get(j)) == -1)
                    if (!isInList)
                        listDataProd.add(dataProd.get(j));
                }
            }
        }
        return listDataProd ;
    }


    /* retourne la liste des grandeurs physiques */
    public ArrayList<PhysicalQuantity> getListPhysicalQuantity(){
        return this.listPhysicalQuantity ;
    }

    /* retourne vrai s'il s'agit d'un material de la mission du proc */
    public boolean isMaterialFromMission(long dbKeyMaterial){
        LearnerProcedure proc = getProcActiv();
        if (proc == null)
            return false;
        ArrayList<Material> listMaterial = proc.getInitialProc().getListMaterial();
        int nbMat = listMaterial.size();
        for (int i=0; i<nbMat; i++){
            if (listMaterial.get(i).getDbKey() == dbKeyMaterial)
                return true;
        }
        return false;
    }

    /* retourne le tooltiptext sur le bouton d'ouverture */
    public String getToolTipTextOpen(){
        return getBundleString("TOOLTIPTEXT_OPEN_PROC");
    }

    /* exportation de la feuille de données */
    public void exportDataSheet(){
        String extension = "xls";
        ExperimentalProcedure p = getProcActiv();
        if (p == null || p.getDataSheet() == null){
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                displayError(new CopexReturn(getBundleString("MSG_WARNING_NO_DATASHEET"), true), getBundleString("TITLE_DIALOG_WARNING"));
                return;
        }
       /* // ouverture fenetre de selection fichier
        JFileChooser fc = new JFileChooser(currentExportPath);
        fc.setFileFilter(new ObjFilter());
        int r = fc.showSaveDialog(this);
        fc.setLocation(getLocationDialog());
        * */
        //bug sur le focus de la fenetre => on ouvre une dialogue invisble pour ouvrir le file chooser : bof !!
        MyFileChooser dialog = new MyFileChooser(this, currentExportPath);
        int r = dialog.showDialog();
        if (r == JFileChooser.APPROVE_OPTION){
           // File file = fc.getSelectedFile();
            File file = dialog.getSelectedFile();
            if(!CopexUtilities.getExtensionFile(file).equals(extension)){
                file = CopexUtilities.setExtensionFile(file, extension);
            }
            currentExportPath = file.getPath() ;
            setCursor(new Cursor(Cursor.WAIT_CURSOR));
            CopexReturn cr = this.controller.exportDataSheet(getProcActiv(), file) ;
            if (cr.isError()){
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    /* retourne le point pour afficher la boite de dialogue */
    public Point getLocationDialog(){
        return new Point( (int)this.getLocationOnScreen().getX() +(this.getWidth() /3), (int)this.getLocationOnScreen().getY()+this.menuBar.getHeight());
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

    /* retourne vrai si applet est visiblee */
    public boolean isAppletVisible(){
        return this.isVisible();
    }

    /* demande quel proc initial pour creation d'un proc */
    public void askForInitialProc(){
        CreateProcDialog dialog = new CreateProcDialog(this, controller, listInitProc) ;
        dialog.setVisible(true);
    }

    /* TP Muriel  :retourne vrai si affichage des hypotheses */
    public boolean isDisplayHypothesis(){
        if(this.mission == null)
            return true;
        return this.mission.getDbKey() !=4;
    }

    /* retourne vrai si il y a une liste de materiel à afficher */
    public boolean isMaterialAvailable(){
        LearnerProcedure p = getProcActiv();
        if(p == null)
            return false;
        return p.getInitialProc().getListMaterial().size() > 0 ;
    }

    /* ouverture auto de la fenetre d'edition de la question */
    public void openQuestionDialog(){
        //repaint();
        //getTabbedPaneProc().openQuestionDialog();
    }

    public void setQuestionDialog(){
       this.openQuestionDialog = true;
    }

    /* chargement ELO */
    public void loadELO(Element xmlContent){
        CopexReturn cr = this.controller.loadELO(xmlContent);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
        }
    }

    /* creation nouvel ELO */
    public void newELO(){
        CopexReturn cr = this.controller.newELO();
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
        }
    }

    /* retourne ELO proc */
    public Element getExperimentalProcedure(){
        if(this.getProcActiv() != null)
            return this.controller.getExperimentalProcedure(this.getProcActiv());
        return null;
    }

    /* retourne vrai si dans la mission on peut ajouter un proc */
    public boolean canAddProc(){
        if(mission == null)
            return true;
        return this.mission.getOptions().isCanAddProc();
    }

    /* retourne la liste des parametres des actions de l'etape */
    public ArrayList<InitialActionParam> getStepInitialParam(Step step){
        ArrayList v = new ArrayList();
        LearnerProcedure proc = getProcActiv();
        if (proc != null){
            CopexReturn cr =this.controller.getTaskInitialParam(proc, step, v);
            if(cr.isError()){
                displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            }else{
                return (ArrayList<InitialActionParam>)v.get(0);
            }
        }
        return new ArrayList();
    }


    /* retourne la liste des output des actions de l'etape */
    public ArrayList<InitialOutput> getStepInitialOutput(Step step){
        ArrayList v = new ArrayList();
        LearnerProcedure proc = getProcActiv();
        if (proc != null){
            CopexReturn cr =this.controller.getTaskInitialOutput(proc, step, v);
            if(cr.isError()){
                displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            }else{
                return (ArrayList<InitialOutput>)v.get(0);
            }
        }
        return new ArrayList();
    }

   

}
