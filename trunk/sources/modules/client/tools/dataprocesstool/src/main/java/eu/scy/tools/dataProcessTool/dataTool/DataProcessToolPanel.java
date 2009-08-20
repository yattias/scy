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

import eu.scy.elo.contenttype.dataset.DataSetRow;
import eu.scy.tools.dataProcessTool.common.*;
import eu.scy.tools.dataProcessTool.controller.ControllerInterface;
import eu.scy.tools.dataProcessTool.controller.DataController;
import eu.scy.tools.dataProcessTool.controller.DataControllerDB;
import eu.scy.tools.dataProcessTool.undoRedo.DeleteUndoRedo;
import eu.scy.tools.dataProcessTool.undoRedo.EditDataUndoRedo;
import eu.scy.tools.dataProcessTool.undoRedo.EditHeaderUndoRedo;
import eu.scy.tools.dataProcessTool.undoRedo.IgnoreDataUndoRedo;
import eu.scy.tools.dataProcessTool.undoRedo.InsertUndoRedo;
import eu.scy.tools.dataProcessTool.undoRedo.OperationUndoRedo;
import eu.scy.tools.dataProcessTool.undoRedo.PasteUndoRedo;
import eu.scy.tools.dataProcessTool.utilities.*;
import eu.scy.tools.dataProcessTool.utilities.CopexReturn;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
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
public class DataProcessToolPanel extends javax.swing.JPanel {

    //CONSTANTES
    public final static Color backgroundColor = SystemColor.control;
    /* width */
    public static final int PANEL_WIDTH = 285;
    /* height */
    public static final int PANEL_HEIGHT = 325;

    /* width graph */
    public static final int PANEL_WIDTH_GRAPH = 600;
    /* height graph */
    public static final int PANEL_HEIGHT_GRAPH = 350;
    
    /* MODE DEBUG */
    public static final boolean DEBUG_MODE = false;

    private static final int MENU_BAR_HEIGHT = 28;

    //PROPERTY
    /* locale */
    private Locale locale ;
    /* ressource bundle */
    private ResourceBundle bundle;
    /* version */
    private String version = "version 2.0";

    // NOYAU
    /* interface noyau */
    private ControllerInterface controller;
    private long dbKeyUser;
    private long dbKeyMission;
    private String userName;
    private String firstName;

    // Donnees
    /* liste des donnees */
    private Dataset dataset;
    /*tableau des differents types de visualisation */
    private TypeVisualization[] tabTypeVis;
    /* tableau des differentes operations possibles */
    private TypeOperation[] tabTypeOp;


    // IHM
    /* pannel principal */
    private BorderPanel borderPanel;
    /* panel dataset */
    private JPanel panelDataset;
    /* panel data visualizer */
    private JPanel panelGraph;
    private JLabel labelVersion;
    /* separateur */
    private JSplitPane splitPane;
    /* menu Bar */
    private JPanel panelMenuData;
    private JMenuBar menuBarData1;
    private JMenuBar menuBarData2;
    /* item menu */
    private JSeparator sep1;
    private JSeparator sep2;
    private JSeparator sep3;
    private JSeparator sep4;
    private JSeparator sep5;
    private JSeparator sep6;
    private MyMenuItem menuItemInsert;
    private MyMenuItem menuItemSuppr ;
    private MyMenuItem menuItemCopy;
    private MyMenuItem menuItemPaste;
    private MyMenuItem menuItemCut;
    private MyMenuItem menuItemSort;
    private MyMenuItem menuItemIgnore;
    private MyMenuItem menuItemUndo;
    private MyMenuItem menuItemRedo;
    private MyMenuItem menuItemSum;
    private MyMenuItem menuItemAvg;
    private MyMenuItem menuItemMin;
    private MyMenuItem menuItemMax;
    private MyMenuItem menuItemAddGraph;
    private MyMenuItem menuItemPrint;


    private JMenuBar menuBarGraph;
    private JSeparator sepV1;
    private JSeparator sepV2;
    private MyMenuItem menuItemParam;
    private MyMenuItem menuItemAutoScale;
    private MyMenuItem menuItemZoom;
    private MyMenuItem menuItemMove;
    private MyMenuItem menuItemCurve;

    /* data organizer*/
    private JScrollPane scrollPaneDataOrganizer;
    private DataTable datasetTable;

    /* data visualizer */
    private JScrollPane scrollPaneDataVisualizer;
    private VisualTabbedPane dataVisTabbedPane;

    public DataProcessToolPanel() {
        super();
        initComponents();
        initData(null);
        this.dbKeyMission = 1;
        this.dbKeyUser = 1;
        this.firstName = "";
        this.userName = "";
    }

    public DataProcessToolPanel(URL url, long dbKeyMission, long dbKeyUser) {
        super();
        initComponents();
        initData(url);
        this.dbKeyMission = dbKeyMission;
        this.dbKeyUser = dbKeyUser;
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
        // creation du panel de fond
        this.add(getBorderPanel());
        this.borderPanel.add(getPanelDataset(), BorderLayout.CENTER);
        this.panelDataset.add(getDataMenuBar(), BorderLayout.NORTH);
        setSize(PANEL_WIDTH, PANEL_HEIGHT);
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

    }

    public void initDataProcessingTool(TypeVisualization[] tabTypeVisual, TypeOperation[] tabTypeOp, Dataset dataset){
        this.dataset = dataset;
        this.tabTypeOp = tabTypeOp;
        this.tabTypeVis = tabTypeVisual;
        setDataset();
        updateMenuData();
    }
   
     
    /* double clic sur l'onglet */
    public void doubleClickTab(VisualTabbedPane tabbedPane, CloseTab closeTab){
        if (tabbedPane instanceof VisualTabbedPane){
            // renommer un graphe
            // ouverture de dialog permettant de renommer un graphe
            Visualization vis = getDataVisTabbedPane().getVisualization(closeTab);
            if (vis == null)
                return;
            EditVisualizationDialog dialog = new EditVisualizationDialog(this, vis);
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

        setMinimumSize(new java.awt.Dimension(285, 300));
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
            this.splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, getPanelDataset(), getPanelGraph());
            splitPane.setBounds(0, 0, this.getWidth(), this.getHeight());
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
        }
        return this.borderPanel;
    }

    /* panel dataset */
    private JPanel getPanelDataset(){
        if (this.panelDataset == null){
            this.panelDataset = new JPanel();
            this.panelDataset.setName("panelDataset");
            this.panelDataset.setLayout(new BorderLayout());
            this.panelDataset.setBounds(0, 0, this.getWidth()*2/3,this.getHeight() );
            panelDataset.addComponentListener(new java.awt.event.ComponentAdapter() {
                @Override
                public void componentResized(java.awt.event.ComponentEvent evt) {
                    panelDatasetComponentResized(evt);
                }
            });
        }
        return this.panelDataset;
    }
    /* panel data graphes */
    private JPanel getPanelGraph(){
        if (this.panelGraph == null){
            this.panelGraph = new JPanel();
            this.panelGraph.setName("panelGraph");
            this.panelGraph.setLayout(new BorderLayout());
            this.panelGraph.setBounds(this.panelDataset.getWidth(), 0, this.getWidth()/3, this.getHeight());
            this.panelGraph.add(getGraphMenuBar(), BorderLayout.NORTH);
            this.panelGraph.add(getScrollPaneDataVisualizer(), BorderLayout.CENTER);
            getBorderPanel().add(this.panelGraph, BorderLayout.PAGE_START);
            panelGraph.addComponentListener(new java.awt.event.ComponentAdapter() {
                @Override
                public void componentResized(java.awt.event.ComponentEvent evt) {
                    panelVizComponentResized(evt);
                }
            });
        }
        return this.panelGraph;
    }
    
    /* label vserion */
    private JLabel getLabelVersion(){
        if (this.labelVersion == null){
            this.labelVersion = new JLabel(version);
        }
        return this.labelVersion ;
    }

    /* menu Bar data */
    private JPanel getDataMenuBar(){
        if (this.panelMenuData == null){
            panelMenuData = new JPanel();
            panelMenuData.setName("panelMenuData");
            panelMenuData.setLayout(new BoxLayout(panelMenuData, BoxLayout.Y_AXIS));
            this.menuBarData1 = new JMenuBar();
            this.menuBarData1.setLayout( null);
            this.menuBarData1.setName("menuBarData1");
            this.menuBarData1.setSize(this.getWidth(), MENU_BAR_HEIGHT);
            this.menuBarData1.setPreferredSize(this.menuBarData1.getSize());
            this.menuBarData2 = new JMenuBar();
            this.menuBarData2.setLayout( null);
            this.menuBarData2.setName("menuBarData2");
            this.menuBarData2.setSize(this.getWidth(), MENU_BAR_HEIGHT);
            this.menuBarData2.setPreferredSize(this.menuBarData2.getSize());

            menuBarData1.add(getMenuItemInsert());
            menuBarData1.add(getMenuItemSuppr());
            menuBarData1.add(getSep1());
            menuBarData1.add(getMenuItemCopy());
            menuBarData1.add(getMenuItemPaste());
            menuBarData1.add(getMenuItemCut());
            menuBarData1.add(getSep2());
            menuBarData1.add(getMenuItemSort());
            menuBarData1.add(getMenuItemIgnore());
            menuBarData1.add(getSep3());
            menuBarData1.add(getMenuItemUndo());
            menuBarData1.add(getMenuItemRedo());
            menuBarData1.add(getSep4());

            menuBarData2.add(getMenuItemSum());
            menuBarData2.add(getMenuItemAvg());
            menuBarData2.add(getMenuItemMin());
            menuBarData2.add(getMenuItemMax());
            menuBarData2.add(getSep5());
            menuBarData2.add(getMenuItemAddGraph());
            menuBarData2.add(getSep6());
            menuBarData2.add(getMenuItemPrint());

            panelMenuData.add(menuBarData1);
            panelMenuData.add(menuBarData2);
        }
        return panelMenuData;
    }

    private MyMenuItem getMenuItemInsert(){
        if (menuItemInsert == null){
            menuItemInsert = new MyMenuItem(this, getBundleString("TOOLTIPTEXT_MENU_INSERT"),menuBarData1.getBackground(),getCopexImage("Bouton-AdT-28_insert.png"), getCopexImage("Bouton-AdT-28_insert_sur.png"), getCopexImage("Bouton-AdT-28_insert_cli.png"), getCopexImage("Bouton-AdT-28_insert_gri.png"));
            menuItemInsert.setBounds(0, 0, menuItemInsert.getWidth(), menuItemInsert.getHeight());
        }
        return menuItemInsert;
    }

    private MyMenuItem getMenuItemSuppr(){
        if (menuItemSuppr == null){
            menuItemSuppr = new MyMenuItem(this, getBundleString("TOOLTIPTEXT_MENU_SUPPR"),menuBarData1.getBackground(),getCopexImage("Bouton-AdT-28_supprimer.png"), getCopexImage("Bouton-AdT-28_supprimer_sur.png"), getCopexImage("Bouton-AdT-28_supprimer_cli.png"), getCopexImage("Bouton-AdT-28_supprimer_gri.png"));
            menuItemSuppr.setBounds(menuItemInsert.getX()+menuItemInsert.getWidth(), 0, menuItemSuppr.getWidth(), menuItemSuppr.getHeight());
        }
        return menuItemSuppr;
    }

    private JSeparator getSep1(){
        if(sep1 == null){
            sep1 = new JSeparator(JSeparator.VERTICAL);
            sep1.setBackground(Color.DARK_GRAY);
            sep1.setBounds(menuItemSuppr.getX()+menuItemSuppr.getWidth(), 0, 5, MENU_BAR_HEIGHT);
        }
        return sep1;
    }

    private MyMenuItem getMenuItemCopy(){
        if (menuItemCopy == null){
            menuItemCopy = new MyMenuItem(this, getBundleString("TOOLTIPTEXT_MENU_COPY"),menuBarData1.getBackground(),getCopexImage("Bouton-AdT-28_copier.png"), getCopexImage("Bouton-AdT-28_copier_survol.png"), getCopexImage("Bouton-AdT-28_copier_clic.png"), getCopexImage("Bouton-AdT-28_copier_grise.png"));
            menuItemCopy.setBounds(sep1.getX()+sep1.getWidth(), 0, menuItemCopy.getWidth(), menuItemCopy.getHeight());
        }
        return menuItemCopy;
    }

     private MyMenuItem getMenuItemPaste(){
        if (menuItemPaste == null){
            menuItemPaste = new MyMenuItem(this, getBundleString("TOOLTIPTEXT_MENU_PASTE"),menuBarData1.getBackground(),getCopexImage("Bouton-AdT-28_coller.png"), getCopexImage("Bouton-AdT-28_coller_survol.png"), getCopexImage("Bouton-AdT-28_coller_clic.png"), getCopexImage("Bouton-AdT-28_coller_grise.png"));
            menuItemPaste.setBounds(menuItemCopy.getX()+menuItemCopy.getWidth(), 0, menuItemPaste.getWidth(), menuItemPaste.getHeight());
        }
        return menuItemPaste;
    }
     
     private MyMenuItem getMenuItemCut(){
        if (menuItemCut == null){
            menuItemCut = new MyMenuItem(this, getBundleString("TOOLTIPTEXT_MENU_CUT"),menuBarData1.getBackground(),getCopexImage("Bouton-AdT-28_couper.png"), getCopexImage("Bouton-AdT-28_couper_survol.png"), getCopexImage("Bouton-AdT-28_couper_clic.png"), getCopexImage("Bouton-AdT-28_couper_grise.png"));
            menuItemCut.setBounds(menuItemPaste.getX()+menuItemPaste.getWidth(), 0, menuItemCut.getWidth(), menuItemCut.getHeight());

        }
        return menuItemCut;
    }

     private JSeparator getSep2(){
        if(sep2 == null){
            sep2 = new JSeparator(JSeparator.VERTICAL);
            sep2.setBackground(Color.DARK_GRAY);
            sep2.setBounds(menuItemCut.getX()+menuItemCut.getWidth(), 0, 5, MENU_BAR_HEIGHT);
        }
        return sep2;
    }
    
    private MyMenuItem getMenuItemSort(){
        if (menuItemSort == null){
            menuItemSort = new MyMenuItem(this, getBundleString("TOOLTIPTEXT_MENU_SORT"),menuBarData1.getBackground(),getCopexImage("Bouton-mat_triabc.png"), getCopexImage("Bouton-mat_triabc_survol.png"), getCopexImage("Bouton-mat_triabc_clic.png"), getCopexImage("Bouton-mat_triabc.png"));
            menuItemSort.setBounds(sep2.getX()+sep2.getWidth(), 0, menuItemSort.getWidth(), menuItemSort.getHeight());
        }
        return menuItemSort;
    }

    private MyMenuItem getMenuItemIgnore(){
        if (menuItemIgnore == null){
            menuItemIgnore = new MyMenuItem(this, getBundleString("TOOLTIPTEXT_MENU_IGNORE"),menuBarData1.getBackground(),getCopexImage("Bouton-AdT-28_ignore.png"), getCopexImage("Bouton-AdT-28_ignore_sur.png"), getCopexImage("Bouton-AdT-28_ignore_cli.png"), getCopexImage("Bouton-AdT-28_ignore_gri.png"));
            menuItemIgnore.setBounds(menuItemSort.getX()+menuItemSort.getWidth(), 0, menuItemIgnore.getWidth(), menuItemIgnore.getHeight());
        }
        return menuItemIgnore;
    }

    private JSeparator getSep3(){
        if(sep3 == null){
            sep3 = new JSeparator(JSeparator.VERTICAL);
            sep3.setBackground(Color.DARK_GRAY);
            sep3.setBounds(menuItemIgnore.getX()+menuItemIgnore.getWidth(), 0, 5, MENU_BAR_HEIGHT);
        }
        return sep3;
    }

    private MyMenuItem getMenuItemUndo(){
        if (menuItemUndo == null){
            menuItemUndo = new MyMenuItem(this, getBundleString("TOOLTIPTEXT_MENU_UNDO"),menuBarData1.getBackground(),getCopexImage("Bouton-AdT-28_undo.png"), getCopexImage("Bouton-AdT-28_undo_survol.png"), getCopexImage("Bouton-AdT-28_undo_clic.png"), getCopexImage("Bouton-AdT-28_undo_grise.png"));
            menuItemUndo.setBounds(sep3.getX()+sep3.getWidth(), 0, menuItemUndo.getWidth(), menuItemUndo.getHeight());
        }
        return menuItemUndo;
    }

     private MyMenuItem getMenuItemRedo(){
        if (menuItemRedo == null){
            menuItemRedo = new MyMenuItem(this, getBundleString("TOOLTIPTEXT_MENU_REDO"),menuBarData1.getBackground(),getCopexImage("Bouton-AdT-28_redo.png"), getCopexImage("Bouton-AdT-28_redo_survol.png"), getCopexImage("Bouton-AdT-28_redo_clic.png"), getCopexImage("Bouton-AdT-28_redo_grise.png"));
            menuItemRedo.setBounds(menuItemUndo.getX()+menuItemUndo.getWidth(), 0, menuItemRedo.getWidth(), menuItemRedo.getHeight());
        }
        return menuItemRedo;
    }

     private JSeparator getSep4(){
        if(sep4 == null){
            sep4 = new JSeparator(JSeparator.VERTICAL);
            sep4.setBackground(Color.DARK_GRAY);
            sep4.setBounds(menuItemRedo.getX()+menuItemRedo.getWidth(), 0, 5, MENU_BAR_HEIGHT);
        }
        return sep4;
    }

    private MyMenuItem getMenuItemSum(){
        if (menuItemSum == null){
            menuItemSum = new MyMenuItem(this, getBundleString("TOOLTIPTEXT_MENU_SUM"),menuBarData2.getBackground(),getCopexImage("Bouton-AdT-28_sum.png"), getCopexImage("Bouton-AdT-28_sum_sur.png"), getCopexImage("Bouton-AdT-28_sum_cli.png"), getCopexImage("Bouton-AdT-28_sum_gri.png"));
            menuItemSum.setBounds(0, 0, menuItemSum.getWidth(), menuItemSum.getHeight());
        }
        return menuItemSum;
    }
    private MyMenuItem getMenuItemAvg(){
        if (menuItemAvg == null){
            menuItemAvg = new MyMenuItem(this, getBundleString("TOOLTIPTEXT_MENU_AVG"),menuBarData2.getBackground(),getCopexImage("Bouton-AdT-28_avg.png"), getCopexImage("Bouton-AdT-28_avg_sur.png"), getCopexImage("Bouton-AdT-28_avg_cli.png"), getCopexImage("Bouton-AdT-28_avg_gri.png"));
            menuItemAvg.setBounds(menuItemSum.getX()+menuItemSum.getWidth(), 0, menuItemAvg.getWidth(), menuItemAvg.getHeight());
        }
        return menuItemAvg;
    }
    private MyMenuItem getMenuItemMin(){
        if (menuItemMin == null){
            menuItemMin = new MyMenuItem(this, getBundleString("TOOLTIPTEXT_MENU_MIN"),menuBarData2.getBackground(),getCopexImage("Bouton-AdT-28_min.png"), getCopexImage("Bouton-AdT-28_min_sur.png"), getCopexImage("Bouton-AdT-28_min_cli.png"), getCopexImage("Bouton-AdT-28_min_gri.png"));
            menuItemMin.setBounds(menuItemAvg.getX()+menuItemAvg.getWidth(), 0, menuItemMin.getWidth(), menuItemMin.getHeight());
        }
        return menuItemMin;
    }
    private MyMenuItem getMenuItemMax(){
        if (menuItemMax == null){
            menuItemMax = new MyMenuItem(this, getBundleString("TOOLTIPTEXT_MENU_MAX"),menuBarData2.getBackground(),getCopexImage("Bouton-AdT-28_max.png"), getCopexImage("Bouton-AdT-28_max_sur.png"), getCopexImage("Bouton-AdT-28_max_cli.png"), getCopexImage("Bouton-AdT-28_max_gri.png"));
            menuItemMax.setBounds(menuItemMin.getX()+menuItemMin.getWidth(), 0, menuItemMax.getWidth(), menuItemMax.getHeight());
        }
        return menuItemMax;
    }
      private JSeparator getSep5(){
        if(sep5 == null){
            sep5 = new JSeparator(JSeparator.VERTICAL);
            sep5.setBackground(Color.DARK_GRAY);
            sep5.setBounds(menuItemMax.getX()+menuItemMax.getWidth(), 0, 5, MENU_BAR_HEIGHT);
        }
        return sep5;
    }
    private MyMenuItem getMenuItemAddGraph(){
        if (menuItemAddGraph == null){
            menuItemAddGraph = new MyMenuItem(this, getBundleString("TOOLTIPTEXT_MENU_ADD_GRAPH"),menuBarData2.getBackground(),getCopexImage("Bouton-AdT-28_chart.png"), getCopexImage("Bouton-AdT-28_chart_sur.png"), getCopexImage("Bouton-AdT-28_chart_cli.png"), getCopexImage("Bouton-AdT-28_chart_gri.png"));
            menuItemAddGraph.setBounds(sep5.getX()+sep5.getWidth(), 0, menuItemAddGraph.getWidth(), menuItemAddGraph.getHeight());
        }
        return menuItemAddGraph;
    }

   
    private JSeparator getSep6(){
        if(sep6 == null){
            sep6 = new JSeparator(JSeparator.VERTICAL);
            sep6.setBackground(Color.DARK_GRAY);
            sep6.setBounds(menuItemAddGraph.getX()+menuItemAddGraph.getWidth(), 0, 5, MENU_BAR_HEIGHT);
        }
        return sep6;
    }
    private MyMenuItem getMenuItemPrint(){
        if (menuItemPrint == null){
            menuItemPrint = new MyMenuItem(this, getBundleString("TOOLTIPTEXT_MENU_PRINT"),menuBarData2.getBackground(),getCopexImage("Bouton-AdT-28_pdf.png"), getCopexImage("Bouton-AdT-28_pdf_survol.png"), getCopexImage("Bouton-AdT-28_pdf_clic.png"), getCopexImage("Bouton-AdT-28_pdf_grise.png"));
            menuItemPrint.setBounds(sep6.getX()+sep6.getWidth(), 0, menuItemPrint.getWidth(), menuItemPrint.getHeight());
        }
        return menuItemPrint;
    }

    /* menu Bar graph */
    private JMenuBar getGraphMenuBar(){
        if (this.menuBarGraph == null){
            this.menuBarGraph = new JMenuBar();
            this.menuBarGraph.setLayout( null);
            this.menuBarGraph.setName("menuBarGraph");
            this.menuBarGraph.setSize(this.getWidth(), MENU_BAR_HEIGHT);
            this.menuBarGraph.setPreferredSize(this.menuBarGraph.getSize());
            menuBarGraph.add(getMenuItemParam());
            menuBarGraph.add(getSepV1());
            menuBarGraph.add(getMenuItemAutoScale());
            menuBarGraph.add(getMenuItemMove());
            menuBarGraph.add(getMenuItemZoom());
            menuBarGraph.add(getSepV2());
            menuBarGraph.add(getMenuItemCurve());
        }
        return this.menuBarGraph;
    }

    private MyMenuItem getMenuItemParam(){
        if (menuItemParam == null){
            menuItemParam = new MyMenuItem(this, getBundleString("TOOLTIPTEXT_MENU_GRAPH_PARAM"),menuBarGraph.getBackground(),getCopexImage("Bouton-AdT-28_graph_param.png"), getCopexImage("Bouton-AdT-28_graph_param_sur.png"), getCopexImage("Bouton-AdT-28_graph_param_cli.png"), getCopexImage("Bouton-AdT-28_graph_param_gri.png"));
            menuItemParam.setBounds(0, 0, menuItemParam.getWidth(), menuItemParam.getHeight());
        }
        return menuItemParam;
    }

     private JSeparator getSepV1(){
        if(sepV1 == null){
            sepV1 = new JSeparator(JSeparator.VERTICAL);
            sepV1.setBackground(Color.DARK_GRAY);
            sepV1.setBounds(menuItemParam.getX()+menuItemParam.getWidth(), 0, 5, MENU_BAR_HEIGHT);
        }
        return sepV1;
    }
    private MyMenuItem getMenuItemAutoScale(){
        if (menuItemAutoScale == null){
            menuItemAutoScale = new MyMenuItem(this, getBundleString("TOOLTIPTEXT_MENU_GRAPH_AUTOSCALE"),menuBarGraph.getBackground(),getCopexImage("Bouton-AdT-28_graph_autoscale.png"), getCopexImage("Bouton-AdT-28_graph_autoscale_sur.png"), getCopexImage("Bouton-AdT-28_graph_autoscale_cli.png"), getCopexImage("Bouton-AdT-28_graph_autoscale_gri.png"));
            menuItemAutoScale.setBounds(sepV1.getX()+sepV1.getWidth(), 0, menuItemAutoScale.getWidth(), menuItemAutoScale.getHeight());
        }
        return menuItemAutoScale;
    }

    private MyMenuItem getMenuItemMove(){
        if (menuItemMove == null){
            menuItemMove = new MyMenuItem(this, getBundleString("TOOLTIPTEXT_MENU_GRAPH_MOVE"),menuBarGraph.getBackground(),getCopexImage("Bouton-AdT-28_graph_move.png"), getCopexImage("Bouton-AdT-28_graph_move_sur.png"), getCopexImage("Bouton-AdT-28_graph_move_cli.png"), getCopexImage("Bouton-AdT-28_graph_move_gri.png"));
            menuItemMove.setBounds(menuItemAutoScale.getX()+menuItemAutoScale.getWidth(), 0, menuItemMove.getWidth(), menuItemMove.getHeight());
        }
        return menuItemMove;
    }

    private MyMenuItem getMenuItemZoom(){
        if (menuItemZoom == null){
            menuItemZoom = new MyMenuItem(this, getBundleString("TOOLTIPTEXT_MENU_GRAPH_ZOOM"),menuBarGraph.getBackground(),getCopexImage("Bouton-AdT-28_graph_zoom.png"), getCopexImage("Bouton-AdT-28_graph_zoom_sur.png"), getCopexImage("Bouton-AdT-28_graph_zoom_cli.png"), getCopexImage("Bouton-AdT-28_graph_zoom_gri.png"));
            menuItemZoom.setBounds(menuItemMove.getX()+menuItemMove.getWidth(), 0, menuItemZoom.getWidth(), menuItemZoom.getHeight());
        }
        return menuItemZoom;
    }
    private JSeparator getSepV2(){
        if(sepV2 == null){
            sepV2 = new JSeparator(JSeparator.VERTICAL);
            sepV2.setBackground(Color.DARK_GRAY);
            sepV2.setBounds(menuItemZoom.getX()+menuItemZoom.getWidth(), 0, 5, MENU_BAR_HEIGHT);
        }
        return sepV2;
    }

    private MyMenuItem getMenuItemCurve(){
        if (menuItemCurve == null){
            menuItemCurve = new MyMenuItem(this, getBundleString("TOOLTIPTEXT_MENU_GRAPH_CURVE"),menuBarGraph.getBackground(),getCopexImage("Bouton-AdT-28_graph.png"), getCopexImage("Bouton-AdT-28_graph_sur.png"), getCopexImage("Bouton-AdT-28_graph_cli.png"), getCopexImage("Bouton-AdT-28_graph_gri.png"));
            menuItemCurve.setBounds(sepV2.getX()+sepV2.getWidth(), 0, menuItemCurve.getWidth(), menuItemCurve.getHeight());
        }
        return menuItemCurve;
    }
    /* scroll pane data organizer */
    private JScrollPane getScrollPaneDataOrganizer(){
        if (this.scrollPaneDataOrganizer == null){
            scrollPaneDataOrganizer = new JScrollPane(getDataTable());
            scrollPaneDataOrganizer.setBounds(0, panelMenuData.getHeight(), panelDataset.getWidth(), panelDataset.getHeight());
        }
        return this.scrollPaneDataOrganizer ;
    }

    /*table dataset */
    private DataTable getDataTable(){
        if (this.datasetTable == null){
            datasetTable = new DataTable(this, dataset);
        }
        return this.datasetTable;
    }

    /* scroll pane data visualizer */
    private JScrollPane getScrollPaneDataVisualizer(){
        if (this.scrollPaneDataVisualizer == null){
           scrollPaneDataVisualizer = new JScrollPane(getDataVisTabbedPane() );
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

    

    /* mise a jour du menu */
    public void updateMenuData(){
        this.menuItemInsert.setEnabled(datasetTable.canInsert());
        getMenuItemSuppr().setEnabled(datasetTable.canSuppr());
        getMenuItemCopy().setEnabled(datasetTable.canCopy());
        getMenuItemPaste().setEnabled(datasetTable.canPaste());
        getMenuItemCut().setEnabled(datasetTable.canCut());
        getMenuItemSort().setEnabled(datasetTable.canSort());
        this.menuItemIgnore.setEnabled(datasetTable.canIgnore());
        getMenuItemUndo().setEnabled(datasetTable.canUndo());
        getMenuItemRedo().setEnabled(datasetTable.canRedo());
        boolean canOp = datasetTable.canOperations();
        this.menuItemSum.setEnabled(canOp);
        this.menuItemAvg.setEnabled(canOp);
        this.menuItemMin.setEnabled(canOp);
        this.menuItemMax.setEnabled(canOp);
        boolean dsNotNull = dataset != null;
        this.menuItemAddGraph.setEnabled(dsNotNull);
        this.menuItemPrint.setEnabled(dsNotNull);
        repaint();
    }

    
    private void setDataset(){
        if (dataset != null){
            this.panelDataset.add(getScrollPaneDataOrganizer());
        }
        resizePanel();
    }



    /* mise a jour du menu */
    public void updateMenuGraph(){
        boolean enabled = this.dataVisTabbedPane.canMenu2DPlot();
        char graphMode = this.dataVisTabbedPane.getGraphMode();
        this.menuItemParam.setEnabled(enabled);
        this.menuItemAutoScale.setEnabled(enabled);
        this.menuItemMove.setEnabled(enabled );
        this.menuItemZoom.setEnabled(enabled );
        switch (graphMode){
            case DataConstants.MODE_AUTOSCALE :
                this.menuItemAutoScale.setItemIcon(getCopexImage("Bouton-AdT-28_graph_autoscale_push.png"));
                this.menuItemMove.setItemIcon(getCopexImage("Bouton-AdT-28_graph_move.png"));
                this.menuItemZoom.setItemIcon(getCopexImage("Bouton-AdT-28_graph_zoom.png"));
                break;
            case DataConstants.MODE_MOVE :
                this.menuItemAutoScale.setItemIcon(getCopexImage("Bouton-AdT-28_graph_autoscale.png"));
                this.menuItemMove.setItemIcon(getCopexImage("Bouton-AdT-28_graph_move_push.png"));
                this.menuItemZoom.setItemIcon(getCopexImage("Bouton-AdT-28_graph_zoom.png"));
                break;
            case DataConstants.MODE_ZOOM :
                this.menuItemAutoScale.setItemIcon(getCopexImage("Bouton-AdT-28_graph_autoscale.png"));
                this.menuItemMove.setItemIcon(getCopexImage("Bouton-AdT-28_graph_move.png"));
                this.menuItemZoom.setItemIcon(getCopexImage("Bouton-AdT-28_graph_zoom_push.png"));
                break;
        }
        repaint();
    }
    
    

    public void clickMenuEvent(MyMenuItem item){
        if (item.equals(getMenuItemInsert())){
            datasetTable.insert();
        }else if (item.equals(getMenuItemSuppr())){
            datasetTable.delete();
        }else if (item.equals(getMenuItemCopy())){
            datasetTable.copy();
        }else if (item.equals(getMenuItemPaste())){
            datasetTable.paste();
        }else if (item.equals(getMenuItemCut())){
            datasetTable.cut();
        }else if (item.equals(getMenuItemSort())){
            openDialogSort();
        }else if(item.equals(getMenuItemIgnore())){
            ArrayList<Data> dataSel = datasetTable.getSelectedData() ;
            if(dataSel != null && dataSel.size() > 0){
                boolean ignore =!dataSel.get(0).isIgnoredData();
                setDataIgnored(dataset, ignore,datasetTable.getSelectedData());
            }
        }else if (item.equals(getMenuItemUndo())){
            datasetTable.undo();
        }else if (item.equals(getMenuItemRedo())){
            datasetTable.redo();
        }else if (item.equals(getMenuItemSum())){
            datasetTable.sum();
        }else if (item.equals(getMenuItemAvg())){
            datasetTable.avg();
        }else if (item.equals(getMenuItemMin())){
            datasetTable.min();
        }else if (item.equals(getMenuItemMax())){
            datasetTable.max();
        }else if (item.equals(getMenuItemAddGraph())){
            openDialogCreateVisual();
        }else if (item.equals(getMenuItemPrint())){
            print();
        }else if (item.equals(getMenuItemParam())){
            openDialogGraphParam();
        }else if (item.equals(getMenuItemZoom())){
            setGraphMode(DataConstants.MODE_ZOOM);
        }else if (item.equals(getMenuItemMove())){
            setGraphMode(DataConstants.MODE_MOVE);
        }else if (item.equals(getMenuItemAutoScale())){
            setGraphMode(DataConstants.MODE_AUTOSCALE);
        }else if (item.equals(getMenuItemCurve())){
            displayFunctionModel();
        }else
            displayError(new CopexReturn("Not yet implemented !!", false), "En travaux");
    }

    /* ouverture de la fenetre des parametres du graphe */
    private void openDialogGraphParam(){
        ParamGraph paramGraph = null;
        Visualization vis = getDataVisTabbedPane().getSelectedVisualization();
        if (vis != null && vis instanceof Graph){
            paramGraph = ((Graph)vis).getParamGraph();
        }
        GraphParamDialog dialog = new GraphParamDialog(this, paramGraph);
        dialog.setVisible(true);
    }

    


     /* ouverture dialog creation viuals */
    public void openDialogCreateVisual(){
        CreateDataVisualDialog dialog = new CreateDataVisualDialog(this, tabTypeVis, dataset.getListDataHeader());
        dialog.setVisible(true);
    }


    /*ouverture de la fenetre de tri*/
    private void openDialogSort(){
        Vector listColumns = new Vector();
        String selColumn = null;
        Object o = datasetTable.getSelectedFirstColumn();
        if(o != null){
            if(o instanceof DataHeader)
                selColumn = ((DataHeader)o).getValue();
            else if (o instanceof DataOperation)
                selColumn = ((DataOperation)o).getName();
        }
        for (int i=0; i<dataset.getListDataHeader().length; i++){
            listColumns.add(dataset.getListDataHeader()[i].getValue());
        }
        ArrayList<DataOperation> listOp = dataset.getListOperationOnRows();
        int nb = listOp.size();
        for (int i=0; i<nb; i++){
            listColumns.add(listOp.get(i).getName());
        }
        SortDialog sortDialog = new SortDialog(this, listColumns, selColumn);
        sortDialog.setVisible(true);
    }

    /* creation d'un graphique */
    public boolean createVisualization(String name, TypeVisualization typeVis, DataHeader header1, DataHeader header2){
         if (header1 == null)
            return false;
        int[] tabNo;
        if (typeVis.getNbColParam() > 1){
            tabNo = new int[2];
            tabNo[0] = header1.getNoCol() ;
            tabNo[1] = header2.getNoCol() ;
        }else{
            tabNo = new int[1];
            tabNo[0] = header1.getNoCol() ;
        }
        Visualization vis = new Visualization(-1, name, typeVis, tabNo, true) ;
        if (typeVis.getCode() == DataConstants.VIS_GRAPH){
            ParamGraph paramGraph = new ParamGraph(header1.getValue(), header2.getValue(), -10, 10,  -10,10,1,1, true);
            vis = new Graph(-1, name, typeVis, tabNo, true, paramGraph, null);
        }
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.createVisualization(dataset, vis,true, v) ;
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return false;
        }
        Dataset nds = (Dataset)v.get(0);
        dataset = nds;
        vis = (Visualization)v.get(1);
        if(panelGraph == null){
            getPanelGraph();
            getSplitPane();
        }
        getDataVisTabbedPane().display(dataset, vis, true);
        setSize(Math.max(this.getWidth(), PANEL_WIDTH_GRAPH), Math.max(this.getHeight(), PANEL_HEIGHT_GRAPH));
        resizePanel();
        updateMenuGraph();
        return true;
    }


    private void panelDatasetComponentResized(ComponentEvent evt){
        if(this.scrollPaneDataOrganizer != null){
            this.scrollPaneDataOrganizer.setSize(panelDataset.getWidth(), panelDataset.getHeight()-panelMenuData.getHeight());
            // resize de la table
            //if(datasetTable.getWidth() < this.getWidth()){
                //datasetTable.setOwnerWidth(panelDataset.getWidth());
            //}else{
            //    datasetTable.setOwnerWidth(PANEL_WIDTH);
            //}
        }
        resizeMenuBar();
        panelDataset.revalidate();
        panelDataset.repaint();
    }

    private void panelVizComponentResized(ComponentEvent evt){
        //getDataVisTabbedPane().resizePanel(panelGraph.getWidth(), panelGraph.getHeight()-menuBarGraph.getHeight());
    }


    public void resizePanel(){
        if(this.borderPanel != null){
            this.borderPanel.setSize(this.getWidth(), this.getHeight());
            if(this.panelGraph == null){
                panelMenuData.setSize(this.getWidth(), panelMenuData.getHeight());
                panelDataset.setSize(this.getWidth(), this.getHeight()-panelMenuData.getHeight());
                
            }else{
                int w = PANEL_WIDTH ;
                if(this.getWidth() <= w){
                    w = this.getWidth() / 4;
                }
                panelMenuData.setSize(w, panelMenuData.getHeight());
                panelDataset.setSize(w, this.getHeight());
                this.splitPane.setBounds(0, 0, splitPane.getWidth(), this.getHeight());
                this.panelGraph.setSize(this.getWidth()- w-splitPane.getWidth(), this.getHeight());
                splitPane.setDividerLocation(w);
            }
        }
        resizeMenuBar();
    }

    /*resize menu bar */
    private void resizeMenuBar(){
        int nbIcone = 15;
        int nbSep = 6 ;
        int wIcone = 30;
        int wSep = 5;
        int widthDispo = this.panelDataset.getWidth() ;
        int w = nbIcone*wIcone+nbSep*wSep;
        if(w <= widthDispo && menuBarData2 != null){
            // menu sur une ligne
            if(menuItemSum != null){
                menuBarData1.remove(getMenuItemSum());
                menuBarData1.remove(getMenuItemAvg());
                menuBarData1.remove(getMenuItemMin());
                menuBarData1.remove(getMenuItemMax());
                menuBarData1.remove(getSep5());
                menuBarData1.remove(getMenuItemAddGraph());
                menuBarData1.remove(getSep6());
                menuBarData1.remove(getMenuItemPrint());
            }
            menuItemSum = null;
            menuItemAvg = null;
            menuItemMin = null;
            menuItemMax = null;
            sep5 = null;
            menuItemAddGraph = null;
            sep6 = null;
            menuItemPrint = null;
            menuBarData1.add(getMenuItemSum());
            this.menuItemSum.setBounds(sep4.getX()+sep4.getWidth(), 0, menuItemSum.getWidth(), menuItemSum.getHeight());
            menuBarData1.add(getMenuItemAvg());
            menuBarData1.add(getMenuItemMin());
            menuBarData1.add(getMenuItemMax());
            menuBarData1.add(getSep5());
            menuBarData1.add(getMenuItemAddGraph());
            menuBarData1.add(getSep6());
            menuBarData1.add(getMenuItemPrint());

            this.panelMenuData.remove(menuBarData2);
            menuBarData2 = null;
        }else if (w > widthDispo && menuBarData2 == null){
            // menu sur 2 lignes
            if(menuItemSum != null){
                menuBarData1.remove(getMenuItemSum());
                menuBarData1.remove(getMenuItemAvg());
                menuBarData1.remove(getMenuItemMin());
                menuBarData1.remove(getMenuItemMax());
                menuBarData1.remove(getSep5());
                menuBarData1.remove(getMenuItemAddGraph());
                menuBarData1.remove(getSep6());
                menuBarData1.remove(getMenuItemPrint());
            }
            menuItemSum = null;
            menuItemAvg = null;
            menuItemMin = null;
            menuItemMax = null;
            sep5 = null;
            menuItemAddGraph = null;
            sep6 = null;
            menuItemPrint = null;
            
            this.menuBarData2 = new JMenuBar();
            this.menuBarData2.setLayout( null);
            this.menuBarData2.setName("menuBarData2");
            this.menuBarData2.setSize(this.getWidth(), MENU_BAR_HEIGHT);
            this.menuBarData2.setPreferredSize(this.menuBarData2.getSize());
            menuBarData2.add(getMenuItemSum());
            menuBarData2.add(getMenuItemAvg());
            menuBarData2.add(getMenuItemMin());
            menuBarData2.add(getMenuItemMax());
            menuBarData2.add(getSep5());
            menuBarData2.add(getMenuItemAddGraph());
            menuBarData2.add(getSep6());
            menuBarData2.add(getMenuItemPrint());
            this.panelMenuData.add(menuBarData2);
        }
        panelMenuData.revalidate();
        panelMenuData.repaint();
    }

    /* met la liste des donnees d'un dataset ignoreees ou non */
    public void setDataIgnored(Dataset ds, boolean isIgnored, ArrayList<Data> listData){
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.setDataIgnored(ds, isIgnored, listData,v);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        Dataset nds = (Dataset)v.get(0);
        dataset = nds;
        datasetTable.updateDataset(nds, true);
        getDataVisTabbedPane().updateDataset(nds, true);
        datasetTable.addUndo(new IgnoreDataUndoRedo(datasetTable, this, controller, isIgnored, listData));
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
        dataset = nds;
        DataOperation operation = (DataOperation)v.get(1);
        datasetTable.createOperation(nds,  operation);
        datasetTable.addUndo(new OperationUndoRedo(datasetTable, this, controller, operation));
    }

    /* mise a jour d'une donnees dans la table */
    public void updateData(Dataset ds, Double value, int rowIndex, int columnIndex){
        Double oldValue = ds.getData(rowIndex, columnIndex) == null ? null : ds.getData(rowIndex, columnIndex).getValue();
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.updateData(ds, rowIndex, columnIndex, value, v);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        Dataset nds = (Dataset)v.get(0);
        dataset = nds;
        datasetTable.updateDataset(nds, true);
        if(dataVisTabbedPane != null){
            dataVisTabbedPane.updateDataset( nds, true);
        }
        datasetTable.addUndo(new EditDataUndoRedo(datasetTable, this, controller, oldValue, value, rowIndex, columnIndex));
    }
    /* mise a jour d'une donnees header */
    public boolean  updateDataHeader(Dataset ds, String value, String unit, int colIndex){
        String oldValue = ds.getDataHeader(colIndex) == null ? "" : ds.getDataHeader(colIndex).getValue();
        String oldUnit = ds.getDataHeader(colIndex) == null ? "" : ds.getDataHeader(colIndex).getUnit();
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.updateDataHeader(ds, colIndex, value, unit,v);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return false;
        }
        Dataset nds = (Dataset)v.get(0);
        dataset = nds;
        datasetTable.updateDataset(nds, true);
        datasetTable.addUndo(new EditHeaderUndoRedo(datasetTable, this, controller, oldValue, value, oldUnit, unit, colIndex));
        return true;
    }

    /* mise a jour d'une donnees title operation */
    public void updateDataOperation(Dataset ds, String value, DataOperation operation){
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.updateDataOperation(ds, operation, value, v);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        Dataset nds = (Dataset)v.get(0);
        dataset = nds;
        datasetTable.updateDataset(nds, false);
    }

    
    

    /* fermeture d'un visualization */
    public boolean closeVisualization(Visualization vis){
       if (dataset == null)
            return false;
        CopexReturn cr = this.controller.closeVisualization(dataset, vis);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return false;
        }
        dataset.removeVisualization(vis);
        getDataVisTabbedPane().closeTab(vis);
        updateMenuGraph();
        return true;
    }

    /* suppression d'une visualization */
    public boolean deleteVisualization(Visualization vis){
        if (dataset == null)
            return false;
        CopexReturn cr = this.controller.deleteVisualization(dataset, vis);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return false;
        }
        dataset.removeVisualization(vis);
        getDataVisTabbedPane().closeTab(vis);
        updateMenuGraph();
        return true;
    }

   

    /* renomme un graphe */
    public boolean updateVisualizationName(Visualization vis, String newName){
        if (dataset == null)
            return false;
        CopexReturn cr = this.controller.updateVisualizationName(dataset, vis, newName);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return false;
        }
        int idVis = dataset.getIdVisualization(vis.getDbKey());
        if (idVis != -1){
                dataset.getListVisualization().get(idVis).setName(newName);
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

    /* retourne l'operation correspondant a un type */
    private TypeOperation getOperation(int typeOp){
        for (int i=0; i<tabTypeOp.length; i++){
            if (tabTypeOp[i].getType() == typeOp)
                return tabTypeOp[i];
        }
        return null;
    }

    /* suppression de donnees et d'operations */
    public void deleteData(Dataset ds, ArrayList<Data> listData, ArrayList<DataHeader> listHeader, ArrayList<DataOperation> listOperation, ArrayList<Integer>[] listRowAndCol){
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
        datasetTable.addUndo(new DeleteUndoRedo(datasetTable, this, controller, listData, listHeader, listRowAndCol, listOperation));
    }

    /* mise a jour d'un dataset */
    public void updateDataset(Dataset ds){
        dataset = ds;
        datasetTable.updateDataset(ds, true);
        if(dataVisTabbedPane != null){
            getDataVisTabbedPane().updateDataset(ds, true);
        }
    }

    public void setDataset(Dataset ds){
        dataset = ds;
        datasetTable.updateDataset(ds, true);
        if(dataVisTabbedPane == null){
            int n = ds.getListVisualization().size() ;
            if(n > 0){
                getPanelGraph();
                getSplitPane();
                for (int i=0; i<n; i++){
                    getDataVisTabbedPane().display(ds, ds.getListVisualization().get(i), true);
                }
                setSize(Math.max(this.getWidth(), PANEL_WIDTH_GRAPH), Math.max(this.getHeight(), PANEL_HEIGHT_GRAPH));
            }
        }else{
            getDataVisTabbedPane().updateDataset(ds, true);
        }
    }

    /* chargement d'un ELO */
    public void loadELO(String xmlContent){
        CopexReturn cr = this.controller.loadELO(xmlContent);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
        }
    }

    /*merge d'un ELO avec le courant */
    public void mergeELO(Element elo){
        CopexReturn cr = this.controller.mergeELO(elo);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
        }
    }
    
    public Element getPDS(){
        return this.controller.getPDS(dataset);
    }

    /* creation ou mise a jour d'une fonction modele */
    public void setFunctionModel(String description, Color fColor){
        Visualization vis = getDataVisTabbedPane().getSelectedVisualization() ;
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.setFunctionModel(dataset, vis, description, fColor, v);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        Dataset nds = (Dataset)v.get(0);
        dataset = nds;
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
        dataset = nds;
        int newId = idBefore + nb-1;
        datasetTable.updateDataset(nds, true);
        datasetTable.addUndo(new InsertUndoRedo(datasetTable, this, controller, isOnCol, nb, idBefore));
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
        dataset = nds;
        DataOperation operation = (DataOperation)v.get(1);
        datasetTable.createOperation(nds,  operation);
        return true;
    }

    /* execution du tri */
    public void executeSort(ElementToSort keySort1, ElementToSort keySort2, ElementToSort keySort3){
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        datasetTable.executeSort(keySort1, keySort2, keySort3);
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    /* mise a jour du dataset apres un tri */
    public void updateDatasetRow(Dataset ds, Vector exchange){
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.updateDatasetRow(ds, exchange, v);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        Dataset nds = (Dataset)v.get(0);
        datasetTable.updateDataset(nds, true);
    }

    

     /* ajout d'une ligne de donnees   */
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


    /* ajout d'une ligne de donnees   */
    private void addData(long dbKeyDs, Double[] values){
        ArrayList v = new ArrayList();
        boolean autoScale = getDataVisTabbedPane().isAutoScale();
        CopexReturn cr = this.controller.addData(dbKeyDs, values,  autoScale, v);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return ;
        }
        Dataset nds = (Dataset)v.get(0);
        dataset = nds;
        datasetTable.updateDataset(nds, true);
        getDataVisTabbedPane().updateDataset(nds, true);
    }

    /* mis a jour des parametres */
    public boolean updateGraphParam(ParamGraph pg){
        ArrayList v = new ArrayList();
        if(dataset == null)
            return false;
        Visualization vis = getDataVisTabbedPane().getSelectedVisualization() ;
        if (vis == null || !(vis instanceof Graph))
            return false;
        CopexReturn cr = this.controller.setParamGraph(dataset.getDbKey(), vis.getDbKey(),pg, v);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return false ;
        }
        Visualization newvis = (Visualization)v.get(0);
        
        int idVis = dataset.getIdVisualization(vis.getDbKey());
        if (idVis == -1)
            return true;
        dataset.getListVisualization().set(idVis, newvis);
        datasetTable.updateDataset(dataset, false);
        getDataVisTabbedPane().updateVisualization(newvis);
        return true;
    }

   

    /* maj autoscale */
    public void setAutoScale(long dbKeyDs, long dbKeyVis, boolean autoScale){
        System.out.println("setAutoScale : "+autoScale);
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.setAutoScale(dbKeyDs, dbKeyVis,autoScale,  v);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return ;
        }
        Visualization vis = (Visualization)v.get(0);
        int idVis = dataset.getIdVisualization(dbKeyVis);
        if (idVis == -1)
            return;
        dataset.getListVisualization().set(idVis, vis);
    }

    /* copie de donnees */
    public boolean paste(Dataset subData, int[] selCell){
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.paste(dataset.getDbKey(), subData, selCell, v);
        if(cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return false;
        }
        Dataset nds = (Dataset)v.get(0);
        ArrayList<Data[]> listData = (ArrayList<Data[]>)v.get(1);
        ArrayList<DataHeader[]> listDataHeader = (ArrayList<DataHeader[]>)v.get(2);
        ArrayList<Integer>[] listRowAndCol = (ArrayList<Integer>[])v.get(3);
        dataset = nds;
        datasetTable.updateDataset(nds, true);
        getDataVisTabbedPane().updateDataset(nds, true);
        datasetTable.addUndo(new PasteUndoRedo(datasetTable, this, controller, subData, selCell, listData, listDataHeader, listRowAndCol));
        return true;
    }

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

    /* impression */
    public void print(){
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        ArrayList<Object> list = new ArrayList();
        if (dataVisTabbedPane != null){
            list = this.dataVisTabbedPane.getListGraphPDF();
        }
        CopexReturn cr = this.controller.printDataset(this.datasetTable.getTableModel(), list);
        if(cr.isError()){
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    private void setGraphMode(char graphMode){
        this.dataVisTabbedPane.setGraphMode(graphMode);
        Visualization vis =this.dataVisTabbedPane.getSelectedVisualization() ;
        if (vis != null && vis instanceof Graph){
            setAutoScale(dataset.getDbKey(), vis.getDbKey(), graphMode == DataConstants.MODE_AUTOSCALE);
        }
        updateMenuGraph();
    }

    /* fermeture d'un graphe */
    public void closeGraph(Visualization vis){
        deleteVisualization(vis);
        int nb = dataset.getListVisualization().size();
        if (nb == 0){
            borderPanel.remove(panelGraph);
            panelGraph.removeAll();
            menuBarGraph = null;
            scrollPaneDataVisualizer = null;
            dataVisTabbedPane = null;
            panelGraph = null;
            borderPanel.remove(splitPane);
            splitPane = null;
            this.borderPanel.add(getPanelDataset(), BorderLayout.CENTER);
            this.panelDataset.add(getDataMenuBar(), BorderLayout.NORTH);
            setSize(PANEL_WIDTH, PANEL_HEIGHT);
        }
    }

    /* ouverture fenetre edition header */
    public void editDataHeader(DataHeader header, int noCol){
        EditDataHeaderDialog editDialog = new EditDataHeaderDialog(this, dataset,header,noCol,  DataConstants.EXECUTIVE_RIGHT);
        editDialog.setVisible(true);
    }

    /* resize de la table par l'utilisateur */
    public void resizeTable(int newWidth, int newHeight, int maxHeight){
//        int h = panelDataset.getHeight()-panelMenuData.getHeight();
//        if (newHeight < h){
//            h = newHeight;
//        }else
//            h = panelDataset.getHeight()-panelMenuData.getHeight();
//        this.scrollPaneDataOrganizer.setSize(this.scrollPaneDataOrganizer.getWidth(), h);
//        this.setPreferredSize(this.scrollPaneDataOrganizer.getSize());
//        this.scrollPaneDataOrganizer.revalidate();
//        repaint();
    }

    /* affichage ou non de la partie fonctions */
    private void displayFunctionModel(){
        this.dataVisTabbedPane.displayFunctionModel();
    }

    public void newElo(){
        CopexReturn cr = this.controller.deleteDataset(this.dataset);
        if(cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        ArrayList v = new ArrayList();
        cr = this.controller.createDefaultDataset(v);
        if(cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        this.setDataset((Dataset)v.get(0));
    }
}
