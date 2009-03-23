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

import eu.scy.elo.contenttype.dataset.DataSetColumn;
import eu.scy.elo.contenttype.dataset.DataSetHeader;
import eu.scy.elo.contenttype.dataset.DataSetRow;
import eu.scy.tools.dataProcessTool.db.ScyApplet;
import eu.scy.tools.dataProcessTool.common.*;
import eu.scy.tools.dataProcessTool.controller.ControllerApplet;
import eu.scy.tools.dataProcessTool.controller.ControllerAppletDB;
import eu.scy.tools.dataProcessTool.controller.ControllerInterface;
import eu.scy.tools.dataProcessTool.dnd.SubData;
import eu.scy.tools.dataProcessTool.utilities.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.swing.*;
import org.jdom.Element;

/**
 * main panel for the data tool
 * @author Marjolaine
 */
public class MainDataToolPanel extends javax.swing.JPanel {

    //CONSTANTES
    public final static Color backgroundColor = SystemColor.control;
    /* width */
    public static final int PANEL_WIDTH = 800;
    /* height */
    public static final int PANEL_HEIGHT = 600;
    /* decale le tabbed pane de la visualisation */
    private static final int DECAL_VISUAL = 50;
    /* MODE DEBUG */
    public static final boolean DEBUG_MODE = true;

    //PROPERTY
    /* locale */
    protected Locale locale ;
    /* ressource bundle */
    protected ResourceBundle bundle;

    
    /* version */
    private String version = "version 0.2";

    // NOYAU
    /* interface noyau */
    private ControllerInterface controller;

    // Données
    /* liste des données */
    private ArrayList<Dataset> listDataSet;
    /*tableau des différents types de visualisation */
    private TypeVisualization[] tabTypeVis;
    /* tableau des différentes operations possibles */
    private TypeOperation[] tabTypeOp;


    // IHM
    /* pannel principal */
    private BorderPanel borderPanel;
    /* panel data process */
    private JPanel panelDataProcess;
    /* panel data visualizer */
    private JPanel panelDataVisualizer;
    private JPanel panelSpace;
    private JLabel labelVersion;
    /* separateur */
    private JSplitPane splitPane;
    /* menu Bar */
    private JMenuBar menuBar;
    private MyMenu menuChevron ;
    private JSeparator sep1;
    private JSeparator sep2;
    private JSeparator sep3;
    /* item menu */
    private MyMenuItem menuItemSort;
    private MyMenuItem menuItemUndo;
    private MyMenuItem menuItemRedo;
    private MyMenuItem menuItemSuppr ;
    private MyMenuItem menuItemCopy;
    private MyMenuItem menuItemPaste;
    private MyMenuItem menuItemCut;
    private MyMenuItem menuItemPrint;
    private MyMenuItem menuItemHelp;

    /* data organizer*/
    private JScrollPane scrollPaneDataOrganizer;
    private DataSetTabbedPane dataSetTabbedPane;

    /* data visualizer */
    private JScrollPane scrollPaneDataVisualizer;
    private VisualTabbedPane dataVisTabbedPane;

    

    
    /** Creates new form MainDataToolPanel */
    public MainDataToolPanel() {
        super();
        locale = new Locale("en", "GB");
        initComponents();
        initDat();
    }

    /** Creates new form MainDataToolPanel */
    public MainDataToolPanel(ScyApplet applet, long dbKeyMission, long dbKeyUser) {
        super();
        locale = new Locale("en", "GB");
        initComponents();
        initDat(applet, dbKeyMission, dbKeyUser);
    }


    private void initDat(){
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
            System.out.println("ERREUR lors du chargement de l'applet, la langue spécifiée "+locale+" n'existe pas : "+e);
            displayError(new CopexReturn("ERREUR lors du chargement de l'applet : "+e, false), "ERROR LANGUAGE");
            return;
            }
        }
        
        initScyApplet();
        setSize(PANEL_WIDTH, PANEL_HEIGHT);
        // noyau
       this.controller = new ControllerApplet(this) ;
        CopexReturn cr = this.controller.load();
        if (cr.isError()){
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

    }

     private void initDat(ScyApplet applet, long dbKeyMission, long dbKeyUser){
         System.out.println("initData, locale");
        // i18n
        locale = Locale.getDefault();
        locale = new Locale("en", "GB");
        try{
            this.bundle = ResourceBundle.getBundle("DataToolBundle", locale);
        }catch(MissingResourceException e){
          try{
              // par defaut on prend l'anglais
              locale = new Locale("en", "GB");
              bundle = ResourceBundle.getBundle("DataToolBundle", locale);
          }catch (MissingResourceException e2){
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            System.out.println("ERREUR lors du chargement de l'applet, la langue spécifiée "+locale+" n'existe pas : "+e);
            displayError(new CopexReturn("ERREUR lors du chargement de l'applet : "+e, false), "ERROR LANGUAGE");
            return;
            }
        }
        initScyApplet();
        setSize(PANEL_WIDTH, PANEL_HEIGHT);
        // noyau
        System.out.println("appel au noyau");
       this.controller = new ControllerAppletDB(applet, this, dbKeyMission, dbKeyUser) ;
        CopexReturn cr = this.controller.load();
        if (cr.isError()){
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return;
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
                msg = ScyUtilities.replace(msg, 0, key);
                displayError(new CopexReturn(msg, false) , this.bundle.getString("TITLE_DIALOG_ERROR"));
            }catch(Exception e2){
                displayError(new CopexReturn("Aucun message trouvé !", false) ,"ERROR");
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
      /* int nbImg = listImage.size();
       for(int i=0; i<nbImg; i++){
           if (listImage.get(i).getImgName().equals(img)){
               return listImage.get(i).getImg();
           }
       }
      displayError(new CopexReturn(getBundleString("MSG_ERROR_IMAGE")+img, false), getBundleString("TITLE_DIALOG_ERROR"));
       return null;*/
        ImageIcon imgIcon = new ImageIcon(getClass().getResource( "/" +img));
        if (imgIcon == null){
            //System.out.println("NULLLLLLL");
            //return new ImageIcon("C:\\Users\\Marjolaine\\Documents\\COPEX\\IHM\\Images\\"+img);
            displayError(new CopexReturn(getBundleString("MSG_ERROR_IMAGE")+img, false), getBundleString("TITLE_DIALOG_ERROR"));
            return null;
        }else
            return imgIcon;
    }


    /* initialisation de l'applet */
    protected void initScyApplet(){

        // Initialisation du look and feel
        try{
            String myLookAndFeel=UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(myLookAndFeel);
        }catch(Exception e){
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            System.out.println("ERREUR dans l'initialisation du lookAndFeel : "+e) ;
            JOptionPane.showMessageDialog(this , "ERREUR ans l'initialisation du lookAndFeel : "+e, "ERROR",JOptionPane.ERROR_MESSAGE);
        }
        // TODO recuperation de la provenance de l'appet : SCY OU COPEX
       
        // chargement des images
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

    }
    /* initialisation graphique */
    public void initDataTool(TypeVisualization[] typesVis, TypeOperation[] typesOp, ArrayList<Dataset> listDs){
        this.listDataSet = listDs;
        this.tabTypeVis = typesVis ;
        this.tabTypeOp = typesOp;
        // creation du panel de fond
        getBorderPanel();
        // menu bar
        getMenuBar();
        // creation des 2 panels
        getPanelDataProcess();
        getPanelDataVisualizer();
        // split pane
        getSplitPane();
        // ajoute les DS
        int nbDs = this.listDataSet.size();
        for (int k=0; k<nbDs; k++){
            DataTable dt = new DataTable(this, this.listDataSet.get(k));
            this.dataSetTabbedPane.addTab(this.listDataSet.get(k).getName(),dt );
        }
        this.dataSetTabbedPane.setSelectedIndex(0);
        repaint();
    }

    

    

     
    /* double clic sur l'onglet */
    public void doubleClickTab(ScyTabbedPane tabbedPane, CloseTab closeTab){
        if (tabbedPane instanceof DataSetTabbedPane){
            // ouverture de dialog permettant de renommer un dataset
            Dataset dataset = getDataSetTabbedPane().getDataset(closeTab);
            if (dataset == null)
                return;
            RenameTableDialog dialog = new RenameTableDialog(this, dataset);
            dialog.setVisible(true);
        }else if (tabbedPane instanceof VisualTabbedPane){
            // renommer un graphe
            // ouverture de dialog permettant de renommer un graphe
            Visualization vis = getDataVisTabbedPane().getVisualization(closeTab);
            if (vis == null)
                return;
            EditVisualizationDialog dialog = new EditVisualizationDialog(this, vis);
            dialog.setVisible(true);
        }
    }

    /* clic sur bouton*/
    public void clickButtonEvent(CopexButtonPanel button){
         ArrayList v = new ArrayList();
         if (this.dataSetTabbedPane.isButtonClose(button, v)){
            Dataset dsToClose = (Dataset)v.get(0);
            openDialogCloseDataset(dsToClose);
         }else if (this.dataSetTabbedPane.isButtonAdd(button)){
            openDialogCreateDataset();
         }else if (this.dataVisTabbedPane.isButtonAdd(button)){
             openDialogCreateVisual();
         }else if (this.dataVisTabbedPane.isButtonClose(button, v)){
             Visualization gToClose = (Visualization)v.get(0);
             openDialogCloseGraph(gToClose);
         }
     }

    /*
     * menu
     */
    public void clickMenuEvent(MyMenuItem item){
        if (item.equals(getMenuItemPrint())){
            actionPrint();
        }else if (item.equals(getMenuItemSort())){
            actionSort();
        }
    }

    /* impression */
    private void actionPrint(){
        PrintDialog dialog = new PrintDialog(this);
        dialog.setVisible(true);
    }

    /* tri */
    private void actionSort(){
        Vector listColumns = new Vector();
        Dataset ds= getDataSetTabbedPane().getSelectedDataset();
        if (ds == null)
            return;
        DataHeader[] headers = ds.getListDataHeader();
        for (int i=0; i<headers.length; i++){
            if (headers[i] != null)
                listColumns.add(headers[i].getValue());
        }
        if(listColumns.size() > 0){
            SortDialog dialog = new SortDialog(this, listColumns);
            dialog.setVisible(true);
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

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        resizePanel();
    }//GEN-LAST:event_formComponentResized


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables


    /* construction splitPane*/
    private JSplitPane getSplitPane(){
        if (this.splitPane == null){
            this.splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, getPanelDataProcess(), getPanelDataVisualizer());
            splitPane.setBounds(0, this.menuBar.getHeight(), this.getWidth(), this.getHeight()-this.menuBar.getHeight());
            splitPane.setOneTouchExpandable(true);
            splitPane.setDividerLocation(this.getWidth()*2/3);
            splitPane.setDividerSize(10);
            splitPane.setResizeWeight(0.5D);
            borderPanel.add(splitPane,  BorderLayout.CENTER);
        }
        return this.splitPane;
    }

    /* panel de fond */
    private BorderPanel getBorderPanel(){
        if (this.borderPanel == null){
            borderPanel = new BorderPanel();
            borderPanel.setBounds(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
            this.add(borderPanel);
        }
        return this.borderPanel;
    }

    /* panel data process */
    private JPanel getPanelDataProcess(){
        if (this.panelDataProcess == null){
            this.panelDataProcess = new JPanel();
            this.panelDataProcess.setName("panelDataProcess");
            this.panelDataProcess.setLayout(new BorderLayout());
            this.panelDataProcess.setBounds(0, this.menuBar.getHeight(), this.getWidth()*2/3,this.getHeight()-this.menuBar.getHeight() );
            this.panelDataProcess.add(getScrollPaneDataOrganizer());
            getBorderPanel().add(this.panelDataProcess, BorderLayout.CENTER);
        }
        return this.panelDataProcess;
    }
    /* panel data visualizer */
    private JPanel getPanelDataVisualizer(){
        if (this.panelDataVisualizer == null){
            this.panelDataVisualizer = new JPanel();
            this.panelDataVisualizer.setName("panelDataVisualizer");
            this.panelDataVisualizer.setLayout(new BorderLayout());
            this.panelDataVisualizer.setBounds(this.panelDataProcess.getWidth(), this.menuBar.getHeight(), this.getWidth()/3, this.getHeight()-this.menuBar.getHeight());
            this.panelDataVisualizer.add(getPanelSpace(), BorderLayout.NORTH );
            this.panelDataVisualizer.add(getScrollPaneDataVisualizer(), BorderLayout.CENTER);
            getBorderPanel().add(this.panelDataVisualizer, BorderLayout.PAGE_START);
        }
        return this.panelDataVisualizer;
    }
    /* panel espce */
    private JPanel getPanelSpace(){
        if (this.panelSpace == null){
            this.panelSpace = new JPanel();
            this.panelSpace.setName("panelSpace");
            this.panelSpace.setLayout(new BorderLayout());
            this.panelSpace.setSize(getWidth() /3, DECAL_VISUAL);
            this.panelSpace.add(getLabelVersion());
        }
        return this.panelSpace ;
    }
    /* label vserion */
    private JLabel getLabelVersion(){
        if (this.labelVersion == null){
            this.labelVersion = new JLabel(version);
        }
        return this.labelVersion ;
    }
    /* menu Bar */
    private JMenuBar getMenuBar(){
        if (this.menuBar == null){
            this.menuBar = new JMenuBar();
            this.menuBar.setLayout( null);
            this.menuBar.setName("menuBar");
            this.menuBar.setSize(PANEL_WIDTH, 28);
            this.menuBar.setPreferredSize(this.menuBar.getSize());
            menuBar.add(getMenuItemSort());
            menuBar.add(getSep2());
            menuBar.add(getMenuItemUndo());
            menuBar.add(getMenuItemRedo());
            menuBar.add(getSep3());
            menuBar.add(getMenuChevron());
            menuBar.add(getMenuItemHelp());
            getBorderPanel().add(this.menuBar, BorderLayout.NORTH);
        }
        return this.menuBar;
    }

    
    private JSeparator getSep2(){
        if (sep2 == null){
            sep2 = new JSeparator(JSeparator.VERTICAL);
            sep2.setBackground(Color.DARK_GRAY);
            sep2.setBounds(menuItemSort.getX()+menuItemSort.getWidth(), 0, 5, menuBar.getHeight());
        }
        return sep2;
    }
    private JSeparator getSep3(){
        if (sep3 == null){
            sep3 = new JSeparator(JSeparator.VERTICAL);
            sep3.setBackground(Color.DARK_GRAY);
            sep3.setBounds(menuItemRedo.getX()+menuItemRedo.getWidth(), 0, 5, menuBar.getHeight());
        }
        return sep3;
    }
    
    private MyMenuItem getMenuItemSort(){
        if (menuItemSort == null){
            menuItemSort = new MyMenuItem(this, getBundleString("TOOLTIPTEXT_MENU_SORT"),menuBar.getBackground(),getCopexImage("Bouton-mat_triabc.png"), getCopexImage("Bouton-mat_triabc_survol.png"), getCopexImage("Bouton-mat_triabc_clic.png"), getCopexImage("Bouton-mat_triabc.png"));
            menuItemSort.setBounds(0, 0, menuItemSort.getWidth(), menuItemSort.getHeight());
        }
        return menuItemSort;
    }

    private MyMenuItem getMenuItemUndo(){
        if (menuItemUndo == null){
            menuItemUndo = new MyMenuItem(this, getBundleString("TOOLTIPTEXT_MENU_UNDO"),menuBar.getBackground(),getCopexImage("Bouton-AdT-28_undo.png"), getCopexImage("Bouton-AdT-28_undo_survol.png"), getCopexImage("Bouton-AdT-28_undo_clic.png"), getCopexImage("Bouton-AdT-28_undo_grise.png"));
            menuItemUndo.setBounds(sep2.getX()+sep2.getWidth(), 0, menuItemUndo.getWidth(), menuItemUndo.getHeight());
        }
        return menuItemUndo;
    }

     private MyMenuItem getMenuItemRedo(){
        if (menuItemRedo == null){
            menuItemRedo = new MyMenuItem(this, getBundleString("TOOLTIPTEXT_MENU_REDO"),menuBar.getBackground(),getCopexImage("Bouton-AdT-28_redo.png"), getCopexImage("Bouton-AdT-28_redo_survol.png"), getCopexImage("Bouton-AdT-28_redo_clic.png"), getCopexImage("Bouton-AdT-28_redo_grise.png"));
            menuItemRedo.setBounds(menuItemUndo.getX()+menuItemUndo.getWidth(), 0, menuItemRedo.getWidth(), menuItemRedo.getHeight());
        }
        return menuItemRedo;
    }

     private MyMenu getMenuChevron(){
        if (menuChevron == null){
            menuChevron = new MyMenu(this,"",menuBar.getBackground(),getCopexImage("Bouton-AdT-28_chevrons.png"), getCopexImage("Bouton-AdT-28_chevrons_surv.png"), getCopexImage("Bouton-AdT-28_chevrons.png"), getCopexImage("Bouton-AdT-28_chevrons.png"));
            menuChevron.setToolTipText(null);
            //menuChevron.setBackground(this.menuBar.getBackground());
            menuChevron.setBounds(sep3.getX()+sep3.getWidth(), 0, menuChevron.getWidth(), menuChevron.getHeight());
            menuChevron.add(getMenuItemCut());
            menuChevron.add(getMenuItemCopy());
            menuChevron.add(getMenuItemPaste());
            menuChevron.add(getMenuItemSuppr());
            menuChevron.add(getMenuItemPrint());
        }
        return menuChevron;
    }
     private MyMenuItem getMenuItemCut(){
        if (menuItemCut == null){
            menuItemCut = new MyMenuItem(this, getBundleString("TOOLTIPTEXT_MENU_CUT"),menuBar.getBackground(),getCopexImage("Bouton-AdT-28_couper.png"), getCopexImage("Bouton-AdT-28_couper_survol.png"), getCopexImage("Bouton-AdT-28_couper_clic.png"), getCopexImage("Bouton-AdT-28_couper_grise.png"));
            menuItemCut.setSize(menuItemCut.getWidth(), menuItemCut.getHeight());
            menuItemCut.setPreferredSize(new Dimension(menuItemCut.getWidth(), menuItemCut.getHeight()));
            menuItemCut.setBounds(0, 0, menuItemCut.getWidth(), menuItemCut.getHeight());

        }
        return menuItemCut;
    }
      private MyMenuItem getMenuItemCopy(){
        if (menuItemCopy == null){
            menuItemCopy = new MyMenuItem(this, getBundleString("TOOLTIPTEXT_MENU_COPY"),menuBar.getBackground(),getCopexImage("Bouton-AdT-28_copier.png"), getCopexImage("Bouton-AdT-28_copier_survol.png"), getCopexImage("Bouton-AdT-28_copier_clic.png"), getCopexImage("Bouton-AdT-28_copier_grise.png"));
            menuItemCopy.setSize(menuItemCopy.getWidth(), menuItemCopy.getHeight());
            menuItemCopy.setPreferredSize(new Dimension(menuItemCopy.getWidth(), menuItemCopy.getHeight()));
            menuItemCopy.setBounds(0, menuItemCut.getHeight(), menuItemCopy.getWidth(), menuItemCopy.getHeight());

        }
        return menuItemCopy;
    }
     private MyMenuItem getMenuItemPaste(){
        if (menuItemPaste == null){
            menuItemPaste = new MyMenuItem(this, getBundleString("TOOLTIPTEXT_MENU_PASTE"),menuBar.getBackground(),getCopexImage("Bouton-AdT-28_coller.png"), getCopexImage("Bouton-AdT-28_coller_survol.png"), getCopexImage("Bouton-AdT-28_coller_clic.png"), getCopexImage("Bouton-AdT-28_coller_grise.png"));
            menuItemPaste.setSize(menuItemPaste.getWidth(), menuItemPaste.getHeight());
            menuItemPaste.setPreferredSize(new Dimension(menuItemPaste.getWidth(), menuItemPaste.getHeight()));
            menuItemPaste.setBounds(0, menuItemCopy.getHeight(), menuItemPaste.getWidth(), menuItemPaste.getHeight());

        }
        return menuItemPaste;
    }

     private MyMenuItem getMenuItemSuppr(){
        if (menuItemSuppr == null){
            menuItemSuppr = new MyMenuItem(this, getBundleString("TOOLTIPTEXT_MENU_SUPPR"),menuBar.getBackground(),getCopexImage("Bouton-AdT-28_supprimer.png"), getCopexImage("Bouton-AdT-28_supprimer_sur.png"), getCopexImage("Bouton-AdT-28_supprimer_cli.png"), getCopexImage("Bouton-AdT-28_supprimer_gri.png"));
            menuItemSuppr.setSize(menuItemSuppr.getWidth(), menuItemSuppr.getHeight());
            menuItemSuppr.setPreferredSize(new Dimension(menuItemSuppr.getWidth(), menuItemSuppr.getHeight()));
            menuItemSuppr.setBounds(0, menuItemPaste.getHeight(), menuItemSuppr.getWidth(), menuItemSuppr.getHeight());
        }
        return menuItemSuppr;
    }
     private MyMenuItem getMenuItemPrint(){
        if (menuItemPrint == null){
            menuItemPrint = new MyMenuItem(this, getBundleString("TOOLTIPTEXT_MENU_PRINT"),menuBar.getBackground(),getCopexImage("Bouton-AdT-28_imprimer.png"), getCopexImage("Bouton-AdT-28_imprimer_surv.png"), getCopexImage("Bouton-AdT-28_imprimer_clic.png"), getCopexImage("Bouton-AdT-28_imprimer.png"));
            menuItemPrint.setSize(menuItemPrint.getWidth(), menuItemPrint.getHeight());
            menuItemPrint.setPreferredSize(new Dimension(menuItemPrint.getWidth(), menuItemPrint.getHeight()));
            menuItemPrint.setBounds(0, menuItemSuppr.getHeight(), menuItemPrint.getWidth(), menuItemPrint.getHeight());
        }
        return menuItemPrint;
    }
    private MyMenuItem getMenuItemHelp(){
        if (menuItemHelp == null){
            menuItemHelp = new MyMenuItem(this, getBundleString("TOOLTIPTEXT_MENU_HELP"),menuBar.getBackground(),getCopexImage("Bouton-AdT-28_help.png"), getCopexImage("Bouton-AdT-28_help_survol.png"), getCopexImage("Bouton-AdT-28_help_clic.png"), getCopexImage("Bouton-AdT-28_help.png"));
            menuItemHelp.setBounds(getMenuChevron().getX()+getMenuChevron().getWidth(), 0, menuItemHelp.getWidth(), menuItemHelp.getHeight());
        }
        return menuItemHelp;
    }

    /* scroll pane data organizer */
    private JScrollPane getScrollPaneDataOrganizer(){
        if (this.scrollPaneDataOrganizer == null){
            scrollPaneDataOrganizer = new JScrollPane(getDataSetTabbedPane());
            //scrollPaneDataOrganizer = new JScrollPane();
            //getDataSetTabbedPane();
            scrollPaneDataOrganizer.setBounds(0, 0, panelDataProcess.getWidth(), panelDataProcess.getHeight());
        }
        return this.scrollPaneDataOrganizer ;
    }

    /* tabbed pane data organizer */
    private DataSetTabbedPane getDataSetTabbedPane(){
        if (this.dataSetTabbedPane == null){
            dataSetTabbedPane = new DataSetTabbedPane(this);
        }
        return this.dataSetTabbedPane ;
    }

    /* scroll pane data visualizer */
    private JScrollPane getScrollPaneDataVisualizer(){
        if (this.scrollPaneDataVisualizer == null){
           scrollPaneDataVisualizer = new JScrollPane(getDataVisTabbedPane() );
           //scrollPaneDataVisualizer = new JScrollPane();
           //getDataVisTabbedPane() ;
        }
        return this.scrollPaneDataVisualizer ;
    }

    /* tabbed pane data visualizer */
    private VisualTabbedPane getDataVisTabbedPane(){
        if (this.dataVisTabbedPane == null){
            dataVisTabbedPane = new VisualTabbedPane(this);
        }
        return this.dataVisTabbedPane ;
    }

    // METHODES ACTION
    /* recherche de l'indice du ds dans la liste, -1 sinon */
    private int getIdDataset(long dbKey){
        int nb = this.listDataSet.size();
        for (int i=0; i<nb; i++){
            if (listDataSet.get(i).getDbKey() == dbKey)
                return i;
        }
        return -1;
    }

    /* mise à jour du menu */
    public void updateMenu(){
        getMenuItemSort().setEnabled(getDataSetTabbedPane().canSort());
        getMenuItemUndo().setEnabled(getDataSetTabbedPane().canUndo());
        getMenuItemRedo().setEnabled(getDataSetTabbedPane().canRedo());
        getMenuItemSuppr().setEnabled(getDataSetTabbedPane().canSuppr());
        getMenuItemCopy().setEnabled(getDataSetTabbedPane().canCopy());
        getMenuItemPaste().setEnabled(getDataSetTabbedPane().canPaste());
        getMenuItemCut().setEnabled(getDataSetTabbedPane().canCut());
        getMenuItemPrint().setEnabled(true);
        getMenuItemHelp().setEnabled(true);
        repaint();
    }
    /* creation d'une nouvelle feuille de données */
    public boolean createTable(String name){
        ArrayList v = new ArrayList();
        CopexReturn sr = this.controller.createTable(name, v);
        if (sr.isError()){
            displayError(sr, getBundleString("TITLE_DIALOG_ERROR"));
            return false;
        }
        Dataset ds = (Dataset)v.get(0);
        this.listDataSet.add(ds);
        DataTable dt = new DataTable(this, ds);
        this.dataSetTabbedPane.addTab(ds.getName(),dt );

        return true ;
    }
     /* creation d'une nouvelle feuille de données */
    public void createDataset(Dataset ds){
        this.listDataSet.add(ds);
        DataTable dt = new DataTable(this, ds);
        this.dataSetTabbedPane.addTab(ds.getName(),dt );
    }


    /* creation d un nouveau graphe */
    public boolean createTypeVisual(TypeVisualization type, String name){
        Dataset ds = getDataSetTabbedPane().getSelectedDataset();
        if (ds == null )
            return false;

        OptionGraphPanel optionGraphPanel = new OptionGraphPanel(this, ds.getListDataHeader(), type, name);
        CopexGraph cgraph = new CopexGraph(new Graph(-1, name, type, null, true, null, null), optionGraphPanel);
        getDataVisTabbedPane().addTab(name, cgraph);
        splitPane.setDividerLocation(this.getWidth()*1/3);
        return true;
    }


    /* ouverture dialog creation dataset */
    private void openDialogCreateDataset(){
        CreateDataTableDialog dialog = new CreateDataTableDialog(this);
        dialog.setVisible(true);
    }

    /* ouverture dialog creation viuals */
    private void openDialogCreateVisual(){
        CreateDataVisualDialog dialog = new CreateDataVisualDialog(this, tabTypeVis);
        dialog.setVisible(true);
    }

    /* ouverture dialog fermeture dataset */
    private void openDialogCloseDataset(Dataset ds){
        CloseTableDialog dialog = new CloseTableDialog(this, ds);
        dialog.setVisible(true);
    }

    /* ouverture dialog fermeture graphe */
    private void openDialogCloseGraph(Visualization vis){
        CloseTableDialog dialog = new CloseTableDialog(this, vis);
        dialog.setVisible(true);
    }

    /* fermeture d'un dataset */
    public boolean closeDataset(Dataset ds){
        CopexReturn cr = this.controller.closeDataset(ds);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return false;
        }
        int id = getIdDataset(ds.getDbKey());
        if (id != -1)
            listDataSet.remove(id);
        getDataSetTabbedPane().closeTab(ds);
        updateMenu();
        return true;
    }

    /* suppression d'un dataset */
    public boolean deleteDataset(Dataset ds){
        CopexReturn cr = this.controller.deleteDataset(ds);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return false;
        }
        removeDataset(ds);
        return true;
    }

    /*suppression dataset (appel notamment par le controller) */
    public void removeDataset(Dataset ds){
        int id = getIdDataset(ds.getDbKey());
        if (id != -1)
            listDataSet.remove(id);
        getDataSetTabbedPane().closeTab(ds);
        updateMenu();
    }

    /* renomme un dataset */
    public boolean updateDatasetName(Dataset ds, String newName){
        CopexReturn cr = this.controller.updateDatasetName(ds, newName);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return false;
        }
        int id = getIdDataset(ds.getDbKey());
        if (id != -1){
            listDataSet.get(id).setName(newName);
        }
        getDataSetTabbedPane().updateDatasetName(ds, newName);
        return true;
    }

    /* met la liste des données d'un dataset ignoreées ou non */
    public void setDataIgnored(Dataset ds, boolean isIgnored, ArrayList<Data> listData){
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.setDataIgnored(ds, isIgnored, listData,v);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        Dataset nds = (Dataset)v.get(0);
        int id = getIdDataset(ds.getDbKey());
        if (id != -1){
            listDataSet.set(id, nds) ;
        }
        getDataSetTabbedPane().updateDataset(nds, true);
        getDataVisTabbedPane().updateDataset(nds, true);
    }

    /* nouvelle operation sur le dataset */
    public void createOperation(Dataset ds, int typeOp, boolean isOnCol, ArrayList<Integer> listNo){
        TypeOperation type = getOperation(typeOp);
        if (type == null)
            return;
        if (type instanceof TypeOperationParam){
            openParamOperationDialog(ds, (TypeOperationParam)type, isOnCol, listNo);
            return;
        }
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.createOperation(ds, typeOp, isOnCol, listNo, v);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        Dataset nds = (Dataset)v.get(0);
        int id = getIdDataset(ds.getDbKey());
        if (id != -1){
            listDataSet.set(id, nds) ;
        }
        DataOperation operation = (DataOperation)v.get(1);
        getDataSetTabbedPane().createOperation(nds,  operation);
    }

    /* mise à jour d'une données dans la table */
    public void updateData(Dataset ds, double value, int rowIndex, int columnIndex){
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.updateData(ds, rowIndex, columnIndex, value, v);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        Dataset nds = (Dataset)v.get(0);
        int id = getIdDataset(ds.getDbKey());
        if (id != -1){
            listDataSet.set(id, nds) ;
        }
        getDataSetTabbedPane().updateDataset(nds, true);
    }
    /* mise à jour d'une données header */
    public void updateDataHeader(Dataset ds, String value, int colIndex){
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.updateDataHeader(ds, colIndex, value, v);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        Dataset nds = (Dataset)v.get(0);
        int id = getIdDataset(ds.getDbKey());
        if (id != -1){
            listDataSet.set(id, nds) ;
        }
        getDataSetTabbedPane().updateDataset(nds, false);
    }

    /* mise à jour d'une données title operation */
    public void updateDataOperation(Dataset ds, String value, DataOperation operation){
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.updateDataOperation(ds, operation, value, v);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        Dataset nds = (Dataset)v.get(0);
        int id = getIdDataset(ds.getDbKey());
        if (id != -1){
            listDataSet.set(id, nds) ;
        }
        getDataSetTabbedPane().updateDataset(nds, false);
    }

    /* dessine le graphe choisi avec la ou les colonnes choisies */
    public void drawGraph(DataHeader header1, DataHeader header2, TypeVisualization type, String  name){
        Dataset ds = getDataSetTabbedPane().getSelectedDataset() ;
        if (ds == null)
            return;
        if (header1 == null)
            return;
        int[] tabNo;
        if (type.getNbColParam() > 1){
            tabNo = new int[2];
            tabNo[0] = header1.getNoCol() ;
            tabNo[1] = header2.getNoCol() ;
        }else{
            tabNo = new int[1];
            tabNo[0] = header1.getNoCol() ;
        }
        Visualization vis = new Visualization(-1, name, type, tabNo, true) ;
        if (type.getCode() == DataConstants.VIS_GRAPH){
            ParamGraph paramGraph = new ParamGraph(header1.getValue(), header2.getValue(), -10, 10, 1, -10,10,1);
            vis = new Graph(-1, name, type, tabNo, true, paramGraph, null);
        }
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.createVisualization(ds, vis, v) ;
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return ;
        }
        Dataset nds = (Dataset)v.get(0);
        int id = getIdDataset(ds.getDbKey());
        if (id != -1){
            listDataSet.set(id, nds) ;
        }
         vis = (Visualization)v.get(1);
        getDataVisTabbedPane().display(ds, vis, false);
    }

    /*provisoire - retourne le répertoire des ELO*/
    public String getDirectoryELO(){
        return "ELO/";
    }


    /* fermeture d'un visualization */
    public boolean closeVisualization(Visualization vis){
        Dataset ds = getDataSetTabbedPane().getSelectedDataset() ;
        if (ds == null)
            return false;
        CopexReturn cr = this.controller.closeVisualization(ds, vis);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return false;
        }
        int id = getIdDataset(ds.getDbKey());
        if (id != -1)
            listDataSet.get(id).removeVisualization(vis);
        getDataVisTabbedPane().closeTab(vis);
        updateMenu();
        return true;
    }

    /* suppression d'une visualization */
    public boolean deleteVisualization(Visualization vis){
        Dataset ds = getDataSetTabbedPane().getSelectedDataset() ;
        if (ds == null)
            return false;
        CopexReturn cr = this.controller.deleteVisualization(ds, vis);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return false;
        }
        int id = getIdDataset(ds.getDbKey());
        if (id != -1)
            listDataSet.get(id).removeVisualization(vis);
        getDataVisTabbedPane().closeTab(vis);
        updateMenu();
        return true;
    }

    /* met a jour un dataset actif */
    public void setDatasetActiv(Dataset ds){
        // met a jour les graphes
        int idDs = getIdDataset(ds.getDbKey());
        if (idDs != -1){
            Dataset myDs = listDataSet.get(idDs);
            getDataVisTabbedPane().updateDataset(myDs, false);
            if (myDs.getListVisualization().size() > 0)
                splitPane.setDividerLocation(this.getWidth()*1/3);
            else
                splitPane.setDividerLocation(this.getWidth()*2/3);
        }
        updateMenu();
    }

    /* renomme un graphe */
    public boolean updateVisualizationName(Visualization vis, String newName){
        Dataset ds = getDataSetTabbedPane().getSelectedDataset() ;
        if (ds == null)
            return false;
        CopexReturn cr = this.controller.updateVisualizationName(ds, vis, newName);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return false;
        }
        int id = getIdDataset(ds.getDbKey());
        if (id != -1){
            int idVis = listDataSet.get(id).getIdVisualization(vis.getDbKey());
            if (idVis != -1){
                listDataSet.get(id).getListVisualization().get(idVis).setName(newName);
            }

        }
        getDataVisTabbedPane().updateVisualizationName(vis, newName);
        return true;
    }

    /* retourne la couleur selon le type d'operations */
    public Color getOperationColor(int typeOp){
        for (int i=0; i<tabTypeOp.length; i++){
            if (tabTypeOp[i].getType() == typeOp){
                return tabTypeOp[i].getColor();
            }
        }
        return null;
    }

    /* retourne l'operation correspondant à un type */
    private TypeOperation getOperation(int typeOp){
        for (int i=0; i<tabTypeOp.length; i++){
            if (tabTypeOp[i].getType() == typeOp)
                return tabTypeOp[i];
        }
        return null;
    }

    /* suppression de données et d'operations */
    public void deleteData(Dataset ds, ArrayList<Data> listData, ArrayList<DataOperation> listOperation, ArrayList<Integer>[] listRowAndCol){
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.deleteData(false, ds, listData, listOperation, listRowAndCol);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }else if (cr.isWarning()){
            boolean isOk = displayError(cr, getBundleString("TITLE_DIALOG_CONFIRM"));
            if (isOk){
                cr = this.controller.deleteData(true, ds, listData, listOperation, listRowAndCol);
                if (cr.isError()){
                    displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
                    return;
                }
            }
        }
    }

    /* mise à jour d'un dataset */
    public void updateDataset(Dataset ds){
        int id = getIdDataset(ds.getDbKey());
        if (id != -1){
            listDataSet.set(id, ds) ;
        }
        getDataSetTabbedPane().updateDataset(ds, true);
        getDataVisTabbedPane().updateDataset(ds, true);
    }

    /* chargement d'un ELO */
    public void loadELO(String xmlContent){
        CopexReturn cr = this.controller.loadELO(xmlContent);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
        }
    }

    public void resizePanel(){
        if(this.borderPanel != null){
            this.borderPanel.setSize(this.getWidth(), this.getHeight());
            this.menuBar.setSize(this.getWidth(), this.menuBar.getHeight());
            this.panelDataProcess.setBounds(0, this.menuBar.getHeight(), this.getWidth()*2/3,this.getHeight()-this.menuBar.getHeight() );
            this.scrollPaneDataOrganizer.setBounds(0, 0, panelDataProcess.getWidth(), panelDataProcess.getHeight());
            this.panelDataVisualizer.setBounds(this.panelDataProcess.getWidth(), this.menuBar.getHeight(), this.getWidth()/3, this.getHeight()-this.menuBar.getHeight());
            int oldDiv = splitPane.getDividerLocation();
            this.splitPane.setBounds(0, this.menuBar.getHeight(), this.getWidth(), this.getHeight()-this.menuBar.getHeight());
            splitPane.setDividerLocation(oldDiv);
            repaint();
        }
    }
    public Element getPDS(){
        return this.controller.getPDS(getDataSetTabbedPane().getSelectedDataset());
    }

    /* creation ou mise à jour d'une fonction modele */
    public void setFunctionModel(String description, Color fColor){
        Dataset ds = getDataSetTabbedPane().getSelectedDataset();
        Visualization vis = getDataVisTabbedPane().getSelectedVisualization() ;
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.setFunctionModel(ds, vis, description, fColor, v);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        Dataset nds = (Dataset)v.get(0);
        int id = getIdDataset(ds.getDbKey());
        if (id != -1){
            listDataSet.set(id, nds) ;
        }
    }

    /* insertion de lignes ou colonnes */
    public void insertData(Dataset ds,  boolean isOnCol, int nb, int idBefore){
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.insertData(ds, isOnCol, nb, idBefore, v);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        Dataset nds = (Dataset)v.get(0);
        int id = getIdDataset(ds.getDbKey());
        if (id != -1){
            listDataSet.set(id, nds) ;
        }
        getDataSetTabbedPane().updateDataset(nds, true);
    }

    /* impression */
    public boolean printDataset(boolean isAllDs, boolean isAllVis){
        ArrayList<Dataset> listDsToPrint = new ArrayList();
        if (isAllDs){
            listDsToPrint = listDataSet ;
        }else{
            Dataset ds = getDataSetTabbedPane().getSelectedDataset();
            if (ds == null){
                displayError(new CopexReturn(getBundleString("MSG_ERROR_PRINT"), false), getBundleString("TITLE_DIALOG_ERROR"));
                return false;
            }else
                listDsToPrint.add(ds);
        }
        CopexReturn cr = this.controller.printDataset(listDsToPrint, isAllVis);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return false;
        }
        return true;
    }

    /* move SubData à l'intérieur de la même table */
    public boolean moveSubData(SubData subDataToMove, int noColInsertBefore){
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.moveSubData(subDataToMove, noColInsertBefore, v);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return false;
        }
        Dataset nds = (Dataset)v.get(0);
        int id = getIdDataset(nds.getDbKey());
        if (id != -1){
            listDataSet.set(id, nds) ;
        }
        return true;
    }

    /* ouvre fenetre dialog pour parametre d'une operation */
    private void openParamOperationDialog(Dataset ds, TypeOperationParam typeOp, boolean isOnCol, ArrayList<Integer> listNo){
        ParamOperationDialog dialog = new ParamOperationDialog(this, typeOp, ds, isOnCol, listNo);
        dialog.setVisible(true);
    }

    /* parametres d'une operation */
    public boolean setParamOperation(Dataset ds, TypeOperationParam typeOp, boolean isOnCol, ArrayList<Integer> listNo, String[] tabValue){
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.createOperationParam(ds, typeOp.getType(), isOnCol, listNo, tabValue, v);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return false;
        }
        Dataset nds = (Dataset)v.get(0);
        int id = getIdDataset(ds.getDbKey());
        if (id != -1){
            listDataSet.set(id, nds) ;
        }
        DataOperation operation = (DataOperation)v.get(1);
        getDataSetTabbedPane().createOperation(nds,  operation);
        return true;
    }

    /* execution du tri */
    public void executeSort(ElementToSort keySort1, ElementToSort keySort2, ElementToSort keySort3){
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        getDataSetTabbedPane().executeSort(keySort1, keySort2, keySort3);
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    /* mise à jour du dataset apres un tri */
    public void updateDatasetRow(Dataset ds, Vector exchange){
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.updateDatasetRow(ds, exchange, v);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        Dataset nds = (Dataset)v.get(0);
        getDataSetTabbedPane().updateDataset(nds, true);
    }

    /*creation d'une nouvelle table avec un header donné  */
    private long createDataset(String dsName, String[] headers){
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.createDataset(dsName,headers, v);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return -1;
        }
        Dataset ds = (Dataset)v.get(0);
        this.listDataSet.add(ds);
        DataTable dt = new DataTable(this, ds);
        this.dataSetTabbedPane.addTab(ds.getName(),dt );
        repaint();
        return ds.getDbKey() ;
    }
    /*creation d'une nouvelle table avec un header donné  */
    public long createDataset(String dsName, DataSetHeader header){
        String headers[] = new String[header.getColumnCount()];
        List<DataSetColumn> listColumns = header.getColumns();
        int nbCol = listColumns.size() ;
        for (int i=0; i<nbCol; i++){
            headers[i] = listColumns.get(i).getDescription() ;
        }
        return createDataset(dsName, headers);
    }

     /* ajout d'une ligne de données   */
    public void addData(long dbKeyDs,DataSetRow row ){
        List<String> listValues = row.getValues() ;
        int nbV = listValues.size() ;
        Double[] values = new Double[nbV];
        for (int i=0; i<nbV; i++){
            double d = 0;
            try{
                d = Double.parseDouble(listValues.get(i));
            }catch(NumberFormatException e){
                
            }
            values[i] = d ;
        }
        addData(dbKeyDs, values);
    }


    /* ajout d'une ligne de données   */
    private void addData(long dbKeyDs, Double[] values){
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.addData(dbKeyDs, values,  v);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return ;
        }
        Dataset nds = (Dataset)v.get(0);
        int id = getIdDataset(dbKeyDs);
        if (id != -1){
            listDataSet.set(id, nds) ;
        }
        getDataSetTabbedPane().updateDataset(nds, true);
        repaint();
    }

}
