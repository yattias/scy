/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.dataTool;


import eu.scy.elo.contenttype.dataset.DataSetRow;
import eu.scy.tools.dataProcessTool.common.CopyDataset;
import eu.scy.tools.dataProcessTool.common.Data;
import eu.scy.tools.dataProcessTool.common.DataHeader;
import eu.scy.tools.dataProcessTool.common.DataOperation;
import eu.scy.tools.dataProcessTool.common.Dataset;
import eu.scy.tools.dataProcessTool.common.FunctionParam;
import eu.scy.tools.dataProcessTool.common.Graph;
import eu.scy.tools.dataProcessTool.common.ParamGraph;
import eu.scy.tools.dataProcessTool.common.PlotXY;
import eu.scy.tools.dataProcessTool.common.PreDefinedFunctionCategory;
import eu.scy.tools.dataProcessTool.common.SimpleVisualization;
import eu.scy.tools.dataProcessTool.common.TypeOperation;
import eu.scy.tools.dataProcessTool.common.TypeVisualization;
import eu.scy.tools.dataProcessTool.common.Visualization;
import eu.scy.tools.dataProcessTool.controller.ControllerInterface;
import eu.scy.tools.dataProcessTool.print.PrintDialog;
import eu.scy.tools.dataProcessTool.undoRedo.DataUndoRedo;
import eu.scy.tools.dataProcessTool.undoRedo.DeleteUndoRedo;
import eu.scy.tools.dataProcessTool.undoRedo.EditDataUndoRedo;
import eu.scy.tools.dataProcessTool.undoRedo.EditHeaderUndoRedo;
import eu.scy.tools.dataProcessTool.undoRedo.IgnoreDataUndoRedo;
import eu.scy.tools.dataProcessTool.undoRedo.InsertUndoRedo;
import eu.scy.tools.dataProcessTool.undoRedo.OperationUndoRedo;
import eu.scy.tools.dataProcessTool.undoRedo.PasteUndoRedo;
import eu.scy.tools.dataProcessTool.undoRedo.SortUndoRedo;
import eu.scy.tools.dataProcessTool.utilities.ActionMenu;
import eu.scy.tools.dataProcessTool.utilities.CopexReturn;
import eu.scy.tools.dataProcessTool.utilities.DataConstants;
import eu.scy.tools.dataProcessTool.utilities.ElementToSort;
import eu.scy.tools.dataProcessTool.utilities.MyFileFilterCSV;
import eu.scy.tools.dataProcessTool.utilities.MyFileFilterXML;
import eu.scy.tools.dataProcessTool.utilities.MyMenuItem;
import eu.scy.tools.dataProcessTool.utilities.MyUtilities;
import eu.scy.tools.fitex.analyseFn.Function;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.IllegalComponentStateException;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;



/**
 * fitex tool panel
 * @author Marjolaine
 */
public class FitexToolPanel extends JPanel implements ActionMenu  {
    private DataProcessToolPanel dataProcessToolPanel;
    /* dataset */
    private Dataset dataset;
    
    /* interface noyau */
    private ControllerInterface controller;
    /*tableau des differents types de visualisation */
    private TypeVisualization[] tabTypeVis;
    /* tableau des differentes operations possibles */
    private TypeOperation[] tabTypeOp;
    /* liste des fonctions predef*/
    private ArrayList<PreDefinedFunctionCategory> listPreDefinedFunction;

    private File lastUsedFile = null;
    private File lastUsedFileCSV = null;
    private XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
    
    // IHM
    /* panel dataset */
    private JPanel panelDataset;
    private JDesktopPane desktopPane;
    private ArrayList<InternalGraphFrame> listGraphFrame;
    private int nbIcon;
    private boolean datasetModif;
    /* menu Bar */
    private JPanel panelMenuData;
    private JMenuBar menuBarData;
    /* item menu */
    private JSeparator sep1;
    private JSeparator sep2;
    private JSeparator sep3;
    private JSeparator sep4;
    private JSeparator sep5;
    private JSeparator sep6;
    private JSeparator sep7;
    private MyMenuItem menuItemSave;
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
    private MyMenuItem menuItemCsv;
    private MyMenuItem menuItemHelp;
    private MyMenuItem menuItemImport;
    /* data organizer*/
    private JScrollPane scrollPaneDataOrganizer;
    private DataTable datasetTable;

    public FitexToolPanel(DataProcessToolPanel dataProcessToolPanel, Dataset dataset, ControllerInterface controller, TypeVisualization[] tabTypeVis, TypeOperation[] tabTypeOp, ArrayList<PreDefinedFunctionCategory> listPreDefinedFunction) {
        super();
        this.dataProcessToolPanel = dataProcessToolPanel;
        this.dataset = dataset;
        this.controller = controller;
        this.tabTypeVis = tabTypeVis;
        this.tabTypeOp = tabTypeOp;
        this.listPreDefinedFunction = listPreDefinedFunction;
        this.datasetModif = false;
        initGUI();
    }

    private void initGUI(){
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        this.listGraphFrame = new ArrayList();
        setLayout(null);
        setSize(DataProcessToolPanel.PANEL_WIDTH, DataProcessToolPanel.PANEL_HEIGHT);
        // creation du panel de fond
        this.add(getDesktopPanel(), BorderLayout.CENTER);
        this.desktopPane.add(getPanelDataset());

        this.panelDataset.add(getDataMenuBar(), BorderLayout.NORTH);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                resizePanel();
            }
        });
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

     public void initDataProcessingTool( Dataset dataset){
        this.dataset = dataset;
        setDataset();
        updateMenuData();
    }

     /* retourne un message selon cle*/
    public String getBundleString(String key){
        return dataProcessToolPanel.getBundleString(key);
    }
    /* affichage des erreurs*/
    public boolean displayError(CopexReturn cr, String title) {
        return dataProcessToolPanel.displayError(cr, title);
    }
    public  ImageIcon getCopexImage(String img){
        return dataProcessToolPanel.getCopexImage(img);
    }
    public Image getIconDialog(){
        return dataProcessToolPanel.getIconDialog();
    }
    @Override
    public Locale getLocale(){
        return dataProcessToolPanel.getLocale();
    }

    public NumberFormat getNumberFormat(){
        return dataProcessToolPanel.getNumberFormat();
    }

    public URL getHelpManualPage(){
        String helpFile = "languages/fitexHelpManual-"+getLocale().getLanguage()+".xhtml";
        URL urlhelp = this.getClass().getClassLoader().getResource(helpFile);
        if(urlhelp != null)
            return urlhelp;
        return  this.getClass().getClassLoader().getResource("languages/fitexHelpManual-en.xhtml");
        
    }
    
    /* construction desktop*/
    private JDesktopPane getDesktopPanel(){
        if (desktopPane == null){
            desktopPane = new JDesktopPane();
            desktopPane.setName("desktopPane");
            desktopPane.setBounds(0, 0, this.getWidth(), this.getHeight());
        }
        return desktopPane;
    }



    
    /* panel dataset */
    private JPanel getPanelDataset(){
        if (this.panelDataset == null){
            this.panelDataset = new JPanel();
            this.panelDataset.setName("panelDataset");
            this.panelDataset.setLayout(new BorderLayout());
            this.panelDataset.setBounds(0, 0, desktopPane.getWidth(), desktopPane.getHeight());
            this.desktopPane.setBackground(panelDataset.getBackground());

            panelDataset.addComponentListener(new java.awt.event.ComponentAdapter() {
                @Override
                public void componentResized(java.awt.event.ComponentEvent evt) {
                    panelDatasetComponentResized(evt);
                }
            });
        }
        return this.panelDataset;
    }


    /* menu Bar data */
    private JPanel getDataMenuBar(){
        if (this.panelMenuData == null){
            panelMenuData = new JPanel();
            panelMenuData.setName("panelMenuData");
            panelMenuData.setLayout(new BoxLayout(panelMenuData, BoxLayout.Y_AXIS));
            this.menuBarData = new JMenuBar();
            this.menuBarData.setLayout( null);
            this.menuBarData.setName("menuBarData");
            this.menuBarData.setSize(this.getWidth(), DataProcessToolPanel.MENU_BAR_HEIGHT);
            this.menuBarData.setPreferredSize(this.menuBarData.getSize());

            if(dataProcessToolPanel.canSave()){
                menuBarData.add(getMenuItemSave());
            }
            if(dataProcessToolPanel.canImport()){
                menuBarData.add(getMenuItemImport());
                menuBarData.add(getSep7());
            }
            menuBarData.add(getMenuItemInsert());
            menuBarData.add(getMenuItemSuppr());
            menuBarData.add(getSep1());
            menuBarData.add(getMenuItemSort());
            menuBarData.add(getMenuItemIgnore());
            menuBarData.add(getSep2());
            menuBarData.add(getMenuItemCut());
            menuBarData.add(getMenuItemCopy());
            menuBarData.add(getMenuItemPaste());
            menuBarData.add(getSep3());
            menuBarData.add(getMenuItemSum());
            menuBarData.add(getMenuItemAvg());
            menuBarData.add(getMenuItemMin());
            menuBarData.add(getMenuItemMax());
            menuBarData.add(getSep4());
            menuBarData.add(getMenuItemUndo());
            menuBarData.add(getMenuItemRedo());
            menuBarData.add(getSep5());
            menuBarData.add(getMenuItemAddGraph());
            menuBarData.add(getSep6());
            if(dataProcessToolPanel.canPrint()){
                menuBarData.add(getMenuItemPrint());
            }
            menuBarData.add(getMenuItemCsv());
            menuBarData.add(getMenuItemHelp());
            panelMenuData.add(menuBarData);
        }
        return panelMenuData;
    }

    private MyMenuItem getMenuItemSave(){
        if (menuItemSave == null){
            menuItemSave = new MyMenuItem(getBundleString("TOOLTIPTEXT_MENU_SAVE"),menuBarData.getBackground(),getCopexImage("Bouton-AdT-28_save.png"), getCopexImage("Bouton-AdT-28_save_survol.png"), getCopexImage("Bouton-AdT-28_save_clic.png"), getCopexImage("Bouton-AdT-28_save_grise.png"));
            menuItemSave.setBounds(0, 0, menuItemSave.getWidth(), menuItemSave.getHeight());
            menuItemSave.addActionMenu(this);
        }
        return menuItemSave;
    }
    private MyMenuItem getMenuItemImport(){
        if (menuItemImport == null){
            menuItemImport = new MyMenuItem(getBundleString("TOOLTIPTEXT_MENU_IMPORT"),menuBarData.getBackground(),getCopexImage("import.png"), getCopexImage("import_survol.png"), getCopexImage("import_clic.png"), getCopexImage("import_grise.png"));
            int x = 0;
            if(menuItemSave != null){
                x = menuItemSave.getX()+menuItemSave.getWidth();
            }
            menuItemImport.setBounds(x, 0, menuItemImport.getWidth(), menuItemImport.getHeight());
            menuItemImport.addActionMenu(this);
        }
        return menuItemImport;
    }

    private JSeparator getSep7(){
        if(sep7 == null){
            sep7 = new JSeparator(JSeparator.VERTICAL);
            sep7.setBounds(menuItemImport.getX()+menuItemImport.getWidth(), 0, 5, DataProcessToolPanel.MENU_BAR_HEIGHT);
        }
        return sep7;
    }


    private MyMenuItem getMenuItemInsert(){
        if (menuItemInsert == null){
            menuItemInsert = new MyMenuItem(getBundleString("TOOLTIPTEXT_MENU_INSERT"),menuBarData.getBackground(),getCopexImage("Bouton-AdT-28_insert.png"), getCopexImage("Bouton-AdT-28_insert_sur.png"), getCopexImage("Bouton-AdT-28_insert_cli.png"), getCopexImage("Bouton-AdT-28_insert_gri.png"));
            int x = 0;
            if(dataProcessToolPanel.canImport())
                x = sep7.getX()+sep7.getWidth();
            menuItemInsert.setBounds(x, 0, menuItemInsert.getWidth(), menuItemInsert.getHeight());
            menuItemInsert.addActionMenu(this);
        }
        return menuItemInsert;
    }

    private MyMenuItem getMenuItemSuppr(){
        if (menuItemSuppr == null){
            menuItemSuppr = new MyMenuItem(getBundleString("TOOLTIPTEXT_MENU_SUPPR"),menuBarData.getBackground(),getCopexImage("Bouton-AdT-28_supprimer.png"), getCopexImage("Bouton-AdT-28_supprimer_sur.png"), getCopexImage("Bouton-AdT-28_supprimer_cli.png"), getCopexImage("Bouton-AdT-28_supprimer_gri.png"));
            menuItemSuppr.setBounds(menuItemInsert.getX()+menuItemInsert.getWidth(), 0, menuItemSuppr.getWidth(), menuItemSuppr.getHeight());
            menuItemSuppr.addActionMenu(this);
        }
        return menuItemSuppr;
    }

    private JSeparator getSep1(){
        if(sep1 == null){
            sep1 = new JSeparator(JSeparator.VERTICAL);
            sep1.setBounds(menuItemSuppr.getX()+menuItemSuppr.getWidth(), 0, 5, DataProcessToolPanel.MENU_BAR_HEIGHT);
        }
        return sep1;
    }

    private MyMenuItem getMenuItemSort(){
        if (menuItemSort == null){
            menuItemSort = new MyMenuItem(getBundleString("TOOLTIPTEXT_MENU_SORT"),menuBarData.getBackground(),getCopexImage("Bouton-mat_triabc.png"), getCopexImage("Bouton-mat_triabc_survol.png"), getCopexImage("Bouton-mat_triabc_clic.png"), getCopexImage("Bouton-mat_triabc.png"));
            menuItemSort.setBounds(sep1.getX()+sep1.getWidth(), 0, menuItemSort.getWidth(), menuItemSort.getHeight());
            menuItemSort.addActionMenu(this);
        }
        return menuItemSort;
    }

    private MyMenuItem getMenuItemIgnore(){
        if (menuItemIgnore == null){
            menuItemIgnore = new MyMenuItem(getBundleString("TOOLTIPTEXT_MENU_IGNORE"),menuBarData.getBackground(),getCopexImage("Bouton-AdT-28_ignore.png"), getCopexImage("Bouton-AdT-28_ignore_sur.png"), getCopexImage("Bouton-AdT-28_ignore_cli.png"), getCopexImage("Bouton-AdT-28_ignore_gri.png"));
            menuItemIgnore.setBounds(menuItemSort.getX()+menuItemSort.getWidth(), 0, menuItemIgnore.getWidth(), menuItemIgnore.getHeight());
            menuItemIgnore.addActionMenu(this);
        }
        return menuItemIgnore;
    }

    private JSeparator getSep2(){
        if(sep2 == null){
            sep2 = new JSeparator(JSeparator.VERTICAL);
            sep2.setBounds(menuItemIgnore.getX()+menuItemIgnore.getWidth(), 0, 5, DataProcessToolPanel.MENU_BAR_HEIGHT);
        }
        return sep2;
    }

    private MyMenuItem getMenuItemCut(){
        if (menuItemCut == null){
            menuItemCut = new MyMenuItem(getBundleString("TOOLTIPTEXT_MENU_CUT"),menuBarData.getBackground(),getCopexImage("Bouton-AdT-28_couper.png"), getCopexImage("Bouton-AdT-28_couper_survol.png"), getCopexImage("Bouton-AdT-28_couper_clic.png"), getCopexImage("Bouton-AdT-28_couper_grise.png"));
            menuItemCut.setBounds(sep2.getX()+sep2.getWidth(), 0, menuItemCut.getWidth(), menuItemCut.getHeight());
            menuItemCut.addActionMenu(this);

        }
        return menuItemCut;
    }

    private MyMenuItem getMenuItemCopy(){
        if (menuItemCopy == null){
            menuItemCopy = new MyMenuItem(getBundleString("TOOLTIPTEXT_MENU_COPY"),menuBarData.getBackground(),getCopexImage("Bouton-AdT-28_copier.png"), getCopexImage("Bouton-AdT-28_copier_survol.png"), getCopexImage("Bouton-AdT-28_copier_clic.png"), getCopexImage("Bouton-AdT-28_copier_grise.png"));
            menuItemCopy.setBounds(menuItemCut.getX()+menuItemCut.getWidth(), 0, menuItemCopy.getWidth(), menuItemCopy.getHeight());
            menuItemCopy.addActionMenu(this);
        }
        return menuItemCopy;
    }

     private MyMenuItem getMenuItemPaste(){
        if (menuItemPaste == null){
            menuItemPaste = new MyMenuItem(getBundleString("TOOLTIPTEXT_MENU_PASTE"),menuBarData.getBackground(),getCopexImage("Bouton-AdT-28_coller.png"), getCopexImage("Bouton-AdT-28_coller_survol.png"), getCopexImage("Bouton-AdT-28_coller_clic.png"), getCopexImage("Bouton-AdT-28_coller_grise.png"));
            menuItemPaste.setBounds(menuItemCopy.getX()+menuItemCopy.getWidth(), 0, menuItemPaste.getWidth(), menuItemPaste.getHeight());
            menuItemPaste.addActionMenu(this);
        }
        return menuItemPaste;
    }



    private JSeparator getSep3(){
        if(sep3 == null){
            sep3 = new JSeparator(JSeparator.VERTICAL);
            sep3.setBounds(menuItemPaste.getX()+menuItemPaste.getWidth(), 0, 5, DataProcessToolPanel.MENU_BAR_HEIGHT);
        }
        return sep3;
    }

    private MyMenuItem getMenuItemSum(){
        if (menuItemSum == null){
            menuItemSum = new MyMenuItem(getBundleString("TOOLTIPTEXT_MENU_SUM"),menuBarData.getBackground(),getCopexImage("Bouton-AdT-28_sum.png"), getCopexImage("Bouton-AdT-28_sum_sur.png"), getCopexImage("Bouton-AdT-28_sum_cli.png"), getCopexImage("Bouton-AdT-28_sum_gri.png"));
            menuItemSum.setBounds(sep3.getX()+sep3.getWidth(), 0, menuItemSum.getWidth(), menuItemSum.getHeight());
            menuItemSum.addActionMenu(this);
        }
        return menuItemSum;
    }
    private MyMenuItem getMenuItemAvg(){
        if (menuItemAvg == null){
            menuItemAvg = new MyMenuItem(getBundleString("TOOLTIPTEXT_MENU_AVG"),menuBarData.getBackground(),getCopexImage("Bouton-AdT-28_avg.png"), getCopexImage("Bouton-AdT-28_avg_sur.png"), getCopexImage("Bouton-AdT-28_avg_cli.png"), getCopexImage("Bouton-AdT-28_avg_gri.png"));
            menuItemAvg.setBounds(menuItemSum.getX()+menuItemSum.getWidth(), 0, menuItemAvg.getWidth(), menuItemAvg.getHeight());
            menuItemAvg.addActionMenu(this);
        }
        return menuItemAvg;
    }
    private MyMenuItem getMenuItemMin(){
        if (menuItemMin == null){
            menuItemMin = new MyMenuItem(getBundleString("TOOLTIPTEXT_MENU_MIN"),menuBarData.getBackground(),getCopexImage("Bouton-AdT-28_min.png"), getCopexImage("Bouton-AdT-28_min_sur.png"), getCopexImage("Bouton-AdT-28_min_cli.png"), getCopexImage("Bouton-AdT-28_min_gri.png"));
            menuItemMin.setBounds(menuItemAvg.getX()+menuItemAvg.getWidth(), 0, menuItemMin.getWidth(), menuItemMin.getHeight());
            menuItemMin.addActionMenu(this);
        }
        return menuItemMin;
    }
    private MyMenuItem getMenuItemMax(){
        if (menuItemMax == null){
            menuItemMax = new MyMenuItem(getBundleString("TOOLTIPTEXT_MENU_MAX"),menuBarData.getBackground(),getCopexImage("Bouton-AdT-28_max.png"), getCopexImage("Bouton-AdT-28_max_sur.png"), getCopexImage("Bouton-AdT-28_max_cli.png"), getCopexImage("Bouton-AdT-28_max_gri.png"));
            menuItemMax.setBounds(menuItemMin.getX()+menuItemMin.getWidth(), 0, menuItemMax.getWidth(), menuItemMax.getHeight());
            menuItemMax.addActionMenu(this);
        }
        return menuItemMax;
    }

    private JSeparator getSep4(){
        if(sep4 == null){
            sep4 = new JSeparator(JSeparator.VERTICAL);
            sep4.setBounds(menuItemMax.getX()+menuItemMax.getWidth(), 0, 5, DataProcessToolPanel.MENU_BAR_HEIGHT);
        }
        return sep4;
    }

    private MyMenuItem getMenuItemUndo(){
        if (menuItemUndo == null){
            menuItemUndo = new MyMenuItem(getBundleString("TOOLTIPTEXT_MENU_UNDO"),menuBarData.getBackground(),getCopexImage("Bouton-AdT-28_undo.png"), getCopexImage("Bouton-AdT-28_undo_survol.png"), getCopexImage("Bouton-AdT-28_undo_clic.png"), getCopexImage("Bouton-AdT-28_undo_grise.png"));
            menuItemUndo.setBounds(sep4.getX()+sep4.getWidth(), 0, menuItemUndo.getWidth(), menuItemUndo.getHeight());
            menuItemUndo.addActionMenu(this);
        }
        return menuItemUndo;
    }

     private MyMenuItem getMenuItemRedo(){
        if (menuItemRedo == null){
            menuItemRedo = new MyMenuItem(getBundleString("TOOLTIPTEXT_MENU_REDO"),menuBarData.getBackground(),getCopexImage("Bouton-AdT-28_redo.png"), getCopexImage("Bouton-AdT-28_redo_survol.png"), getCopexImage("Bouton-AdT-28_redo_clic.png"), getCopexImage("Bouton-AdT-28_redo_grise.png"));
            menuItemRedo.setBounds(menuItemUndo.getX()+menuItemUndo.getWidth(), 0, menuItemRedo.getWidth(), menuItemRedo.getHeight());
            menuItemRedo.addActionMenu(this);
        }
        return menuItemRedo;
    }




      private JSeparator getSep5(){
        if(sep5 == null){
            sep5 = new JSeparator(JSeparator.VERTICAL);
            sep5.setBounds(menuItemRedo.getX()+menuItemRedo.getWidth(), 0, 5, DataProcessToolPanel.MENU_BAR_HEIGHT);
        }
        return sep5;
    }
    private MyMenuItem getMenuItemAddGraph(){
        if (menuItemAddGraph == null){
            menuItemAddGraph = new MyMenuItem(getBundleString("TOOLTIPTEXT_MENU_ADD_GRAPH"),menuBarData.getBackground(),getCopexImage("Bouton-AdT-28_chart.png"), getCopexImage("Bouton-AdT-28_chart_sur.png"), getCopexImage("Bouton-AdT-28_chart_cli.png"), getCopexImage("Bouton-AdT-28_chart_gri.png"));
            menuItemAddGraph.setBounds(sep5.getX()+sep5.getWidth(), 0, menuItemAddGraph.getWidth(), menuItemAddGraph.getHeight());
            menuItemAddGraph.addActionMenu(this);
        }
        return menuItemAddGraph;
    }


    private JSeparator getSep6(){
        if(sep6 == null){
            sep6 = new JSeparator(JSeparator.VERTICAL);
            sep6.setBounds(menuItemAddGraph.getX()+menuItemAddGraph.getWidth(), 0, 5, DataProcessToolPanel.MENU_BAR_HEIGHT);
        }
        return sep6;
    }
    private MyMenuItem getMenuItemPrint(){
        if (menuItemPrint == null){
            menuItemPrint = new MyMenuItem(getBundleString("TOOLTIPTEXT_MENU_PRINT"),menuBarData.getBackground(),getCopexImage("Bouton-AdT-28_pdf.png"), getCopexImage("Bouton-AdT-28_pdf_survol.png"), getCopexImage("Bouton-AdT-28_pdf_clic.png"), getCopexImage("Bouton-AdT-28_pdf_grise.png"));
            menuItemPrint.setBounds(sep6.getX()+sep6.getWidth(), 0, menuItemPrint.getWidth(), menuItemPrint.getHeight());
            menuItemPrint.addActionMenu(this);
        }
        return menuItemPrint;
    }
    private MyMenuItem getMenuItemCsv(){
        if (menuItemCsv == null){
            menuItemCsv = new MyMenuItem(getBundleString("TOOLTIPTEXT_MENU_CSV"),menuBarData.getBackground(),getCopexImage("csv.png"), getCopexImage("csv_survol.png"), getCopexImage("csv_clic.png"), getCopexImage("csv_grise.png"));
            int x = sep6.getX()+sep6.getWidth();
            if(menuItemPrint != null){
                x = menuItemPrint.getX()+menuItemPrint.getWidth();
            }
            menuItemCsv.setBounds(x, 0, menuItemCsv.getWidth(), menuItemCsv.getHeight());
            menuItemCsv.addActionMenu(this);
        }
        return menuItemCsv;
    }
     private MyMenuItem getMenuItemHelp(){
        if (menuItemHelp == null){
            menuItemHelp = new MyMenuItem(getBundleString("TOOLTIPTEXT_MENU_HELP"),menuBarData.getBackground(),getCopexImage("Bouton-AdT-28_help.png"), getCopexImage("Bouton-AdT-28_help_survol.png"), getCopexImage("Bouton-AdT-28_help_clic.png"), getCopexImage("Bouton-AdT-28_help.png"));
            menuItemHelp.setBounds(menuItemCsv.getX()+menuItemCsv.getWidth(), 0, menuItemHelp.getWidth(), menuItemHelp.getHeight());
            menuItemHelp.addActionMenu(this);
        }
        return menuItemHelp;
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




    /* mise a jour du menu */
    public void updateMenuData(){
        if(datasetTable == null)
            return;
        boolean canEdit = dataset.getRight() == DataConstants.EXECUTIVE_RIGHT;
        if(menuItemSave != null)
            menuItemSave.setEnabled(canEdit);
        this.menuItemInsert.setEnabled(canEdit && datasetTable.canInsert());
        getMenuItemSuppr().setEnabled(canEdit &&datasetTable.canSuppr());
        getMenuItemCopy().setEnabled(canEdit &&datasetTable.canCopy());
        getMenuItemPaste().setEnabled(canEdit &&datasetTable.canPaste());
        getMenuItemCut().setEnabled(canEdit &&datasetTable.canCut());
        getMenuItemSort().setEnabled(datasetTable.canSort());
        this.menuItemIgnore.setEnabled(canEdit &&datasetTable.canIgnore());
        if(isAllSelectionIgnore()){
            this.menuItemIgnore.setItemIcon(getCopexImage("Bouton-AdT-28_ignore_push.png"));
            this.menuItemIgnore.setItemRolloverIcon(getCopexImage("Bouton-AdT-28_ignore_surcli.png"));
        }else{
            this.menuItemIgnore.setItemIcon(getCopexImage("Bouton-AdT-28_ignore.png"));
            this.menuItemIgnore.setItemRolloverIcon(getCopexImage("Bouton-AdT-28_ignore_sur.png"));
        }
        getMenuItemUndo().setEnabled(canEdit &&datasetTable.canUndo());
        getMenuItemRedo().setEnabled(canEdit &&datasetTable.canRedo());
        boolean canOp = datasetTable.canOperations();
        this.menuItemSum.setEnabled(canEdit &&canOp);
        this.menuItemAvg.setEnabled(canEdit &&canOp);
        this.menuItemMin.setEnabled(canEdit &&canOp);
        this.menuItemMax.setEnabled(canEdit &&canOp);
        //boolean isData = isData() && dataset.getListDataHeaderDouble(true).length > 0;
        boolean isData = dataset.getListDataHeaderDouble(true).length > 0;
        this.menuItemAddGraph.setEnabled(canEdit &&isData);
        if(menuItemPrint != null)
            this.menuItemPrint.setEnabled(dataset != null);
        repaint();
    }

    public void setVerticalScroll(){
        scrollPaneDataOrganizer.getVerticalScrollBar().setValue(0);
    }
    // retourne vrai s'il y a au moins une ligne de donnees
    private boolean isData(){
        if(dataset == null)
            return false;
        int nbR = dataset.getNbRows();
        int nbC = dataset.getNbCol();
        for (int i=0; i<nbR; i++){
            for (int j=0; j<nbC; j++){
                if(dataset.getData(i, j) != null)
                    return true;
            }
        }
        return false;
    }

    private void setDataset(){
        if (dataset != null){
            this.panelDataset.add(getScrollPaneDataOrganizer());
            int nb = this.dataset.getListVisualization().size();
            for (int i=0; i<nb; i++){
                createInternalGraphFrame(dataset.getListVisualization().get(i));
            }
        }
        resizePanel();
    }




    public void clickMenuEvent(MyMenuItem item){
        if (item.equals(getMenuItemInsert())){
            datasetTable.insert();
            return;
        }else if (item.equals(getMenuItemSuppr())){
            datasetTable.delete();
            return;
        }else if (item.equals(getMenuItemCopy())){
            copy();
            return;
        }else if (item.equals(getMenuItemPaste())){
            datasetTable.paste();
            return;
        }else if (item.equals(getMenuItemCut())){
            cut();
            return;
        }else if (item.equals(getMenuItemSort())){
            openDialogSort();
            return;
        }else if(item.equals(getMenuItemIgnore())){
            ArrayList<Data> dataSel = datasetTable.getSelectedData() ;
            if(dataSel != null && dataSel.size() > 0){
                boolean ignore =!dataSel.get(0).isIgnoredData();
                setDataIgnored(dataset, ignore,datasetTable.getSelectedData());
            }
            return;
        }else if (item.equals(getMenuItemUndo())){
            datasetTable.undo();
            return;
        }else if (item.equals(getMenuItemRedo())){
            datasetTable.redo();
            return;
        }else if (item.equals(getMenuItemSum())){
            datasetTable.sum();
            return;
        }else if (item.equals(getMenuItemAvg())){
            datasetTable.avg();
            return;
        }else if (item.equals(getMenuItemMin())){
            datasetTable.min();
            return;
        }else if (item.equals(getMenuItemMax())){
            datasetTable.max();
            return;
        }else if (item.equals(getMenuItemAddGraph())){
            openDialogCreateVisual();
            return;
        }else if (item.equals(menuItemPrint)){
            openDialogPrint();
            return;
        }else if(item.equals(menuItemSave)){
            saveFitex();
            return;
        }else if(item.equals(menuItemHelp)){
            openHelpDialog();
            return;
        }else if(item.equals(menuItemCsv)){
            openCsvDialog();
            return;
        }else if(item.equals(menuItemImport)){
            openImportDialog();
            return;
        }
        displayError(new CopexReturn("Not yet implemented !!", false), "En travaux");
    }


    private void copy(){
        ArrayList<int[]> listSelCell = datasetTable.copy();
    }

    /* log: cut dataset */
    public void logCut(Dataset ds){
        dataProcessToolPanel.logCut(dataset);
    }
    /* log: copy dataset */
    public void logCopy(Dataset ds, ArrayList<int[]> listSelCell){
        dataProcessToolPanel.logCopy(dataset, listSelCell);
    }

    private void cut(){
        datasetTable.cut();
    }

    public void openDialogGraphParam(Visualization vis){
        if(vis instanceof Graph){
            GraphParamDialog dialog = new GraphParamDialog(this, vis, dataset.getListDataHeaderDouble(true));
            dialog.setVisible(true);
        }else{
            GraphParamDialog dialog = new GraphParamDialog(this, vis);
            dialog.setVisible(true);
        }
    }

    /* ouverture fenetre d'aide */
    private void openHelpDialog(){
        HelpDialog helpD = new HelpDialog(this);
        helpD.setVisible(true);
    }


     /* ouverture dialog creation viuals */
    public void openDialogCreateVisual(){
        DataHeader[] tabHeader = dataset.getListDataHeaderDouble(true);
        TypeVisualization[] tabVis = getListTypeVisualization(tabHeader.length);
        DataHeader[] tabHeaderLabel = dataset.getListDataHeaderDouble(false);
        CreateDataVisualDialog dialog = new CreateDataVisualDialog(this, tabVis, tabHeader, tabHeaderLabel);
        dialog.setVisible(true);
    }

    private TypeVisualization[] getListTypeVisualization(int nbHeader){
        int nb = tabTypeVis.length;
        if(nbHeader <2){
            for(int i=0; i<tabTypeVis.length; i++){
                if(tabTypeVis[i].getNbColParam() > 1)
                    nb--;
            }
            TypeVisualization[] tab = new TypeVisualization[nb];
            int id = 0;
            for(int i=0; i<tabTypeVis.length; i++){
                if(tabTypeVis[i].getNbColParam() <= 1){
                    tab[id] = tabTypeVis[i];
                    id++;
                }
            }
            return tab;
        }
        return tabTypeVis;
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
    public boolean createVisualization(String name, TypeVisualization typeVis, DataHeader header1, DataHeader headerLabel, ArrayList<PlotXY> listPlot){
        Visualization vis = null;
        if(header1 != null){
            vis = new SimpleVisualization(-1, name, typeVis, header1, headerLabel) ;
        }else
        if (typeVis.getCode() == DataConstants.VIS_GRAPH){
            ParamGraph paramGraph = new ParamGraph(listPlot, -10, 10,  -10,10,1,1, false);
            vis = new Graph(-1, name, typeVis, paramGraph, null);
        }
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.createVisualization(dataset, vis,true, v) ;
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return false;
        }
        Dataset nds = (Dataset)v.get(0);
        dataset = nds;
        datasetModif = true;
        vis = (Visualization)v.get(1);
        createInternalGraphFrame(vis);
        dataProcessToolPanel.logCreateVisualization(dataset, vis);
        return true;
    }


    private void createInternalGraphFrame(Visualization vis){
        int nb = listGraphFrame.size();
        InternalGraphFrame gFrame = new InternalGraphFrame(this, dataset, vis);
        int y = this.scrollPaneDataOrganizer.getY()+nb*20;
        int h = panelDataset.getHeight() - panelMenuData.getHeight()-y;
        int w = h;
        int x = this.getWidth()-w;
        gFrame.setBounds(x,y,w,h);
        listGraphFrame.add(gFrame);
        desktopPane.add(gFrame);
        gFrame.setVisible(true);
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
        panelDataset.revalidate();
        panelDataset.repaint();
    }

    
    public void resizePanel(){
        desktopPane.setSize(getSize());
        setPanelDatasetHeight();
        replaceIcons();
    }


    /* met la liste des donnees d'un dataset ignoreees ou non */
    public void setDataIgnored(Dataset ds, boolean isIgnored, ArrayList<Data> listData){
        ArrayList v = new ArrayList();
        datasetTable.markSelectedCell();
        CopexReturn cr = this.controller.setDataIgnored(ds, isIgnored, listData,v);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        Dataset nds = (Dataset)v.get(0);
        dataset = nds;
        datasetTable.updateDataset(nds, true);
        datasetTable.selectOldCell();
        updateGraphs(nds, true);
        datasetModif = true;
        datasetTable.addUndo(new IgnoreDataUndoRedo(datasetTable, this, controller, isIgnored, listData));
        dataProcessToolPanel.logIgnoreData(dataset, isIgnored, listData);
    }

    private void  updateGraphs(Dataset ds, boolean update){
        int nb = listGraphFrame.size();
        for(int i=0; i<nb; i++){
            long dbKeyVis = listGraphFrame.get(i).getVisualization().getDbKey();
            Visualization vis = ds.getVisualization(dbKeyVis);
            if(vis != null)
                listGraphFrame.get(i).updateDataset(ds, vis, update);
        }
        // ferme les vis qui n'existent plus
        for(int i=nb-1; i>=0; i--){
            long dbKeyVis = listGraphFrame.get(i).getVisualization().getDbKey();
            Visualization vis = ds.getVisualization(dbKeyVis);
            if(vis == null){
                InternalGraphFrame gFrame = getInternalFrame(dbKeyVis);
                if(gFrame != null){
                    listGraphFrame.remove(gFrame);
                    gFrame.dispose();
                }
            }
        }
        // ouvre les vis nouvelles
        for(Iterator<Visualization> v = ds.getListVisualization().iterator();v.hasNext();){
            Visualization vis = v.next();
            InternalGraphFrame gFrame = getInternalFrame(vis.getDbKey());
            if(gFrame == null){
                createInternalGraphFrame(vis);
            }
        }

    }

    /* nouvelle operation sur le dataset */
    public void createOperation(Dataset ds, int typeOp, boolean isOnCol, ArrayList<Integer> listNo){
        TypeOperation type = getOperation(typeOp);
        if (type == null)
            return;
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
        datasetModif = true;
        datasetTable.addUndo(new OperationUndoRedo(datasetTable, this, controller, operation));
        dataProcessToolPanel.logAddOperation(ds, operation);
    }

    /* mise a jour d'une donnees dans la table */
    public void updateData(Dataset ds, String value, int rowIndex, int columnIndex){
        Data oldData = ds.getData(rowIndex, columnIndex);
        String oldValue = ds.getData(rowIndex, columnIndex) == null ? null : ds.getData(rowIndex, columnIndex).getValue();
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.updateData(ds, rowIndex, columnIndex, value, v);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        Dataset nds = (Dataset)v.get(0);
        dataset = nds;
        datasetTable.updateDataset(nds, true);
        Data newData = nds.getData(rowIndex, columnIndex);
        updateGraphs(nds, true);
        datasetModif = true;
        datasetTable.addUndo(new EditDataUndoRedo(datasetTable, this, controller, oldValue, value, rowIndex, columnIndex));
        dataProcessToolPanel.logEditData(ds, oldData, newData);
    }
    /* mise a jour d'une donnees header */
    public boolean  updateDataHeader(Dataset ds, String value, String unit, int colIndex, String description, String type, String formulaValue, boolean scientificNotation, int nbShownDecimals, int nbSignificantDigits){
        DataHeader oldHeader = ds.getDataHeader(colIndex);
        String oldValue = ds.getDataHeader(colIndex) == null ? "" : ds.getDataHeader(colIndex).getValue();
        String oldUnit = ds.getDataHeader(colIndex) == null ? "" : (ds.getDataHeader(colIndex).getUnit() == null ? "" : ds.getDataHeader(colIndex).getUnit());
        String oldDescription = ds.getDataHeader(colIndex) == null ? "" : ds.getDataHeader(colIndex).getDescription();
        String oldType = ds.getDataHeader(colIndex) == null ? DataConstants.DEFAULT_TYPE_COLUMN : ds.getDataHeader(colIndex).getType();
        String oldFormula = ds.getDataHeader(colIndex) == null ? null : ds.getDataHeader(colIndex).getFormulaValue();
        boolean oldScientificNotation = ds.getDataHeader(colIndex) == null ? false : ds.getDataHeader(colIndex).isScientificNotation();
        int oldNbShownDecimals = ds.getDataHeader(colIndex) == null ? DataConstants.NB_DECIMAL_UNDEFINED : ds.getDataHeader(colIndex).getNbShownDecimals();
        int oldNbSignificantDigits = ds.getDataHeader(colIndex) == null ? DataConstants.NB_SIGNIFICANT_DIGITS_UNDEFINED : ds.getDataHeader(colIndex).getNbSignificantDigits();
        ArrayList v = new ArrayList();
        Function function = getFunction(formulaValue);
        CopexReturn cr = this.controller.updateDataHeader(ds,false, colIndex, value, unit,description, type, formulaValue, function, scientificNotation, nbShownDecimals, nbSignificantDigits, v);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return false;
        }else if(cr.isWarning()){
            boolean isOk = displayError(cr, getBundleString("TITLE_DIALOG_WARNING"));
            if(isOk){
                cr = this.controller.updateDataHeader(ds,true, colIndex, value, unit,description, type,formulaValue,function,scientificNotation, nbShownDecimals, nbSignificantDigits, v);
                if(cr.isError()){
                    displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
                    return false;
                }
            }else{
                return true;
            }
        }
        Dataset nds = (Dataset)v.get(0);
        DataHeader newHeader = nds.getDataHeader(colIndex);
        dataset = nds;
        //datasetTable.updateDataset(nds, true);
        updateDataset(nds);
        datasetModif = true;
        datasetTable.addUndo(new EditHeaderUndoRedo(datasetTable, this, controller, oldValue, value, oldUnit, unit, colIndex, oldDescription, description, oldType, type, oldFormula, formulaValue, oldScientificNotation, scientificNotation, oldNbShownDecimals, nbShownDecimals, oldNbSignificantDigits, nbSignificantDigits));
        dataProcessToolPanel.logEditHeader(dataset, oldHeader, newHeader);
        return true;
    }

    public Function getFunction(String formulaValue){
        if(formulaValue != null){
            return new Function(this, formulaValue, DataConstants.FUNCTION_TYPE_Y_FCT_X, null, null);
        }
        return null;
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
        datasetModif = true;
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
        InternalGraphFrame gFrame = getInternalFrame(vis.getDbKey());
        if(gFrame != null){
           listGraphFrame.remove(gFrame);
        }
        dataProcessToolPanel.logDeleteVisualization(dataset, vis);
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
        InternalGraphFrame gFrame = getInternalFrame(vis.getDbKey());
        if(gFrame != null){
            gFrame.updateVisualizationName(newName);
        }
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
    public TypeOperation getOperation(int typeOp){
        for (int i=0; i<tabTypeOp.length; i++){
            if (tabTypeOp[i].getType() == typeOp)
                return tabTypeOp[i];
        }
        return null;
    }

    /* suppression de donnees et d'operations */
    public void deleteData(Dataset ds,  ArrayList<Data> listData, ArrayList<DataHeader> listHeader, ArrayList<DataOperation> listOperation, ArrayList<Integer>[] listRowAndCol){
        ArrayList v = new ArrayList();
        ArrayList<DataOperation> listOperationToUpdate = new ArrayList();
        ArrayList<Visualization> listVisualizationToUpdate = new ArrayList();
        ArrayList<DataOperation> listOperationToDel = new ArrayList();
        ArrayList<Visualization> listVisualizationToDel = new ArrayList();
        // marque les anciennes donnees
        ArrayList<Data> oldListData = new ArrayList();
        for(Iterator<Data> d = listData.iterator();d.hasNext();){
            oldListData.add((Data)d.next().clone());
        }
        for(Iterator<Integer> i=listRowAndCol[0].iterator();i.hasNext();){
            int id = i.next();
            for(int j=0; j<dataset.getNbCol(); j++){
                Data data = dataset.getData(id, j);
                if(data != null)
                    oldListData.add((Data)data.clone());
            }
        }
        for(Iterator<Integer> j=listRowAndCol[1].iterator();j.hasNext();){
            int id = j.next();
            for(int i=0; i<dataset.getNbRows(); i++){
                Data data = dataset.getData(i, id);
                if(data != null && oldListData.indexOf(data) == -1)
                    oldListData.add((Data)data.clone());
            }
        }
        // appel au noyau
        CopexReturn cr = this.controller.deleteData(false, ds, listData, listRowAndCol[0], listRowAndCol[1],listOperation,  v);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }else if (cr.isWarning()){
            v = new ArrayList();
            boolean isOk = displayError(cr, getBundleString("TITLE_DIALOG_CONFIRM"));
            if (isOk){
                cr = this.controller.deleteData(true, ds, listData, listRowAndCol[0], listRowAndCol[1],listOperation,  v);
                if (cr.isError()){
                    displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
                    return;
                }
            }
            if(v.isEmpty()){
                //suppression
            }else{
                Dataset newDs = (Dataset)v.get(0);
                ArrayList[] tabDel = (ArrayList[])v.get(1);
                listOperationToUpdate =  (ArrayList<DataOperation>)tabDel[0];
                listOperationToDel = (ArrayList<DataOperation>)tabDel[1];
                listVisualizationToUpdate = (ArrayList<Visualization>)tabDel[2];
                listVisualizationToDel = (ArrayList<Visualization>)tabDel[3];
                updateDataset(newDs);
            }
        }else{
            if(v.isEmpty()){
                //suppression
            }else{
                Dataset newDs = (Dataset)v.get(0);
                ArrayList[] tabDel = (ArrayList[])v.get(1);
                listOperationToUpdate =  (ArrayList<DataOperation>)tabDel[0];
                listOperationToDel = (ArrayList<DataOperation>)tabDel[1];
                listVisualizationToUpdate = (ArrayList<Visualization>)tabDel[2];
                listVisualizationToDel = (ArrayList<Visualization>)tabDel[3];
                updateDataset(newDs);
            }
        }
        datasetModif = true;
        updateMenuData();
        datasetTable.addUndo(new DeleteUndoRedo(datasetTable, this, controller, oldListData,listData, listHeader, listRowAndCol, listOperation, listOperationToUpdate, listOperationToDel,listVisualizationToUpdate, listVisualizationToDel));
        //log
        ArrayList<Integer> listIdRows = listRowAndCol[0];
        ArrayList<Integer> listIdColumns = listRowAndCol[1];
        dataProcessToolPanel.logDeleteDatas(dataset, listData, listRowAndCol[0], listRowAndCol[1],listOperation);
    }

    /* mise a jour d'un dataset */
    public void updateDataset(Dataset ds){
        dataset = ds;
        datasetTable.updateDataset(ds, true);
        updateGraphs(ds, true);
    }

    public void setDataset(Dataset ds){
        dataset = ds;
        datasetTable.updateDataset(ds, true);
//        int n = ds.getListVisualization().size() ;
//       for (int i=0; i<n; i++){
//           createInternalGraphFrame(ds.getListVisualization().get(i));
//       }
        updateGraphs(ds, true);
        updateMenuData();
    }

   

    /*merge d'un ELO avec le courant */
    public void mergeELO(Element elo, boolean confirm){
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        CopexReturn cr = this.controller.mergeELO(dataset, elo, confirm);
        if (cr.isError()){
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
        }else if(cr.isWarning()){
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            boolean isok = displayError(cr, getBundleString("TITLE_DIALOG_WARNING"));
            if(isok){
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                cr = this.controller.mergeELO(dataset, elo, false);
                if(cr.isError()){
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
                }
            }
        }
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    public Element getPDS(){
        return this.controller.getPDS(dataset);
    }

    /* creation ou mise a jour d'une fonction modele */
    public void setFunctionModel(Graph graph, String description,char type,  Color fColor, ArrayList<FunctionParam> listParam, String idPredefFunction ){
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.setFunctionModel(dataset, graph, description, type, fColor, listParam, idPredefFunction, v);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        Dataset nds = (Dataset)v.get(0);
        dataset = nds;
        datasetModif = true;
        dataProcessToolPanel.logFunctionModel(dataset, graph, description, fColor, listParam);
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
        datasetTable.updateDataset(nds, true);
        if(isOnCol)
            datasetTable.selectCols(idBefore, nb);
        else
            datasetTable.selectRows(idBefore, nb);
        datasetModif = true;
        updateMenuData();
        datasetTable.addUndo(new InsertUndoRedo(datasetTable, this, controller, isOnCol, nb, idBefore));
        if(isOnCol)
            dataProcessToolPanel.logInsertColumns(ds, nb, idBefore);
        else
            dataProcessToolPanel.logInsertRows(ds, nb, idBefore);
    }




    

    /* execution du tri */
    public void executeSort(ElementToSort keySort1, ElementToSort keySort2, ElementToSort keySort3){
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        datasetTable.executeSort(keySort1, keySort2, keySort3);
        List<Object[]> elementToSort = new LinkedList();
        if(keySort1 != null){
            elementToSort.add(getSortElement(keySort1));
        }
        if(keySort2 != null){
            elementToSort.add(getSortElement(keySort2));
        }
        if(keySort3 != null){
            elementToSort.add(getSortElement(keySort3));
        }
        dataProcessToolPanel.logSortDataset(dataset, elementToSort);
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    private Object[] getSortElement(ElementToSort keySort){
        Object[] tab = new Object[2];
        tab[0] = keySort.getColumnName();
        tab[1] = keySort.getOrder();
        return tab;
    }

    /* mise a jour du dataset apres un tri */
    public void updateDatasetRow(Dataset ds, Vector exchange){
        Dataset oldDs = (Dataset)dataset.clone();
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.updateDatasetRow(ds, exchange, v);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        Dataset nds = (Dataset)v.get(0);
        Dataset newDs = (Dataset)nds.clone();
        datasetTable.updateDataset(nds, true);
        this.datasetTable.addUndo(new SortUndoRedo(datasetTable, this, controller, oldDs, newDs));
    }



     /* ajout d'une ligne de donnees   */
    public void addData(DataSetRow row ){
        List<String> listValues = row.getValues() ;
        int nbV = listValues.size() ;
        String[] values = new String[nbV];
        for (int i=0; i<nbV; i++){
            values[i] = listValues.get(i) ;
        }
        addData(values);
    }


    /* ajout d'une ligne de donnees   */
    private void addData(String[] values){
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.addData(dataset.getDbKey(), values,  v);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return ;
        }
        Dataset nds = (Dataset)v.get(0);
        //Data newData = (Data)v.get(1);
        dataset = nds;
        datasetTable.updateDataset(nds, true);
        updateGraphs(nds, true);
        updateMenuData();
        //dataProcessToolPanel.logAddRow(dataset, newData);
    }

    
    /* mis a jour des parametres */
    public boolean updateGraphParam(Graph graph, String graphName, ParamGraph pg){
        String oldName = new String(graph.getName());
        if(!graph.getName().equals(graphName)){
            updateVisualizationName(graph, graphName);
        }
        // same axis ?
        boolean sameAxis = true;
        int nbOld =graph.getParamGraph().getPlots().size();
        int nbNew = pg.getPlots().size();
        if(nbOld != nbNew)
            sameAxis = false;
        else{
            for(int i=0; i<nbOld; i++){
                DataHeader hxold = graph.getParamGraph().getPlots().get(i).getHeaderX();
                DataHeader hyold = graph.getParamGraph().getPlots().get(i).getHeaderY();
                DataHeader hx = pg.getPlots().get(i).getHeaderX();
                DataHeader hy = pg.getPlots().get(i).getHeaderY();
                if(hxold.getNoCol()!= hx.getNoCol() || hyold.getNoCol() != hy.getNoCol()){
                    sameAxis = false;
                    break;
                }
            }
        }
        ArrayList v = new ArrayList();
        if(dataset == null)
            return false;
        CopexReturn cr = this.controller.setParamGraph(dataset.getDbKey(), graph.getDbKey(),pg, v);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return false ;
        }
        Visualization newvis = (Visualization)v.get(0);
        int idVis = dataset.getIdVisualization(graph.getDbKey());
        if (idVis == -1)
            return true;
        dataset.getListVisualization().set(idVis, newvis);
        datasetTable.updateDataset(dataset, false);
        InternalGraphFrame gFrame = getInternalFrame(graph.getDbKey());
        if(gFrame != null){
            if(sameAxis)
                gFrame.updateVisualization(newvis);
            else
                gFrame.updateDataset(dataset, newvis, true);
        }
        dataProcessToolPanel.logUpdateGraphParam(dataset, oldName, newvis);
        return true;
    }


    public void autoscale(Visualization vis){
        setAutoScale(dataset.getDbKey(), vis.getDbKey(),true);
    }

    /* maj autoscale */
    private void setAutoScale(long dbKeyDs, long dbKeyVis, boolean autoScale){
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
        InternalGraphFrame gFrame = getInternalFrame(dbKeyVis);
        if(gFrame != null){
            gFrame.updateVisualization(vis);
        }
    }

    private InternalGraphFrame getInternalFrame(long dbKeyVis){
        int nb = listGraphFrame.size();
        for (int i=0; i<nb; i++){
            if (listGraphFrame.get(i).getVisualization().getDbKey() == dbKeyVis)
                return listGraphFrame.get(i);
        }
        return null;
    }


    /* copie de donnees */
    public boolean paste(CopyDataset copyDs, int[] selCell){
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.paste(dataset.getDbKey(), copyDs, selCell, v);
        if(cr.isError()){
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return false;
        }
        Dataset nds = (Dataset)v.get(0);
        ArrayList<Data[]> listData = (ArrayList<Data[]>)v.get(1);
        ArrayList<DataHeader[]> listDataHeader = (ArrayList<DataHeader[]>)v.get(2);
        ArrayList<Integer>[] listRowAndCol = (ArrayList<Integer>[])v.get(3);
        dataset = nds;
        datasetTable.updateDataset(nds, true);
        updateGraphs(nds, true);

        datasetTable.addUndo(new PasteUndoRedo(datasetTable, this, controller, copyDs, selCell, listData, listDataHeader, listRowAndCol));
        dataProcessToolPanel.logPaste(dataset, selCell, copyDs);
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        return true;
    }

    
    /* impression */
    public boolean  printFitex(boolean printDataSheet, ArrayList<Visualization> listVis ){
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        ArrayList<Object> list = new ArrayList();
        int nbVis = listVis.size();
        for (int i=0; i<nbVis; i++){
            InternalGraphFrame gFrame = getInternalFrame(listVis.get(i).getDbKey());
            if(gFrame != null){
               Object o = gFrame.getGraphPDF();
                if(o!=null)
                   list.add(o);
            }
        }


        CopexReturn cr = this.controller.printDataset(dataset, printDataSheet, this.datasetTable.getTableModel(), listVis, list);
        if(cr.isError()){
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return false;
        }
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        return true;
    }


    public void setGraphMode(Visualization vis, char graphMode){
        if (vis != null && vis instanceof Graph){
            //setAutoScale(dataset.getDbKey(), vis.getDbKey(), graphMode == DataConstants.MODE_AUTOSCALE);
            dataProcessToolPanel.logGraphMode(dataset, vis, graphMode);
        }
    }


    /* fermeture d'un graphe */
    public boolean closeGraph(Visualization vis){
        int ok = JOptionPane.showConfirmDialog(this, this.getBundleString("MESSAGE_EXIT"), this.getBundleString("TITLE_DIALOG_EXIT"),JOptionPane.OK_CANCEL_OPTION );
        if(ok == JOptionPane.OK_OPTION){
            deleteVisualization(vis);
            int nb = dataset.getListVisualization().size();
            datasetModif = true;
            return true;
        }else
            return false;
    }

    /* ouverture fenetre edition header */
    public void editDataHeader(DataHeader header, int noCol){
        EditDataHeaderDialog editDialog = new EditDataHeaderDialog(this, dataset,header,noCol,  dataset.getRight(), dataset.getWords(noCol));
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


    

    public Dataset getDataset(){
        return this.dataset;
    }
    /* retourne le nom du dataset */
    public String getDatasetName(){
        return this.dataset.getName();
    }

    /* renomme le nom du dataset, retourne le nom */
    public String renameDataset(){
        if(dataset.getRight() == DataConstants.EXECUTIVE_RIGHT){
            RenameDatasetDialog dialog = new RenameDatasetDialog(this, dataset.getName());
            dialog.setVisible(true);
        }
        return this.dataset.getName();
    }

    public boolean canRenameDataset(){
        return dataProcessToolPanel.canRenameDataset();
    }

    public boolean renameDataset(String name){
        String oldName = new String(dataset.getName());
        CopexReturn cr = this.controller.renameDataset(dataset, name);
        if(cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return false;
        }
        this.dataset.setName(name);
        dataProcessToolPanel.logRenameDataset(oldName,  name);
        return true;
    }

    public void deleteAll(){
        int nb = this.dataset.getListVisualization().size();
        for (int i=0; i<nb; i++){
            InternalGraphFrame gFrame = getInternalFrame(this.dataset.getListVisualization().get(i).getDbKey());
            if(gFrame != null){
                listGraphFrame.remove(gFrame);
                gFrame.dispose();
            }
        }
    }


    @Override
    public void actionClick(MyMenuItem item) {
        clickMenuEvent(item);
    }

    public void addIcon(){
        nbIcon++;
        //System.out.println("addIcon "+nbIcon);
        setPanelDatasetHeight();
    }
    public void removeIcon(){
        nbIcon--;
        //System.out.println("removeIcon "+nbIcon);
        setPanelDatasetHeight();
    }
    public void setPanelDatasetHeight(){
        int h = 0;
        if (nbIcon > 0){
            // calucl du nombre de lignes
            int nbL = nbIcon*DataProcessToolPanel.ICON_WIDTH/this.getWidth()+1;
            h = nbL*DataProcessToolPanel.ICON_HEIGHT;
        }
        this.panelDataset.setSize(getWidth(), getHeight()-h);
    }

    /*Bug ID: 4765256 Icons in JDesktopPane not repositioned when pane is resized*/
    public void replaceIcons(){
        int nb = this.listGraphFrame.size();
        int x = 0;
        int y = this.getHeight()-DataProcessToolPanel.ICON_HEIGHT;
        for (int i=0; i<nb; i++){
            InternalGraphFrame gFrame = listGraphFrame.get(i);
            if(gFrame.isIcon() && gFrame.getDesktopIcon() != null){
                JInternalFrame.JDesktopIcon icon = gFrame.getDesktopIcon();
                icon.setBounds(x, y, icon.getWidth(), icon.getHeight());
                if(x+(2*DataProcessToolPanel.ICON_WIDTH) < getWidth()){
                    x += DataProcessToolPanel.ICON_WIDTH;
                }else{
                    x = 0;
                    y = y-DataProcessToolPanel.ICON_HEIGHT;
                }
            }
        }
    }

    private void openDialogPrint(){
        PrintDialog dialog = new PrintDialog(this, dataset);
        dialog.setVisible(true);
    }

    /* retourne le point pour afficher la boite de dialogue */
    public Point getLocationDialog(){
        try{
            return new Point( (int)this.getLocationOnScreen().getX() +(this.getWidth() /3), (int)this.getLocationOnScreen().getY()+this.menuBarData.getHeight());
        }catch(IllegalComponentStateException e){
            return new Point(100,100);
        }
    }

    /* sauvegarde*/
    private void saveFitex(){
        Element pds = getPDS() ;
        JFileChooser aFileChooser = new JFileChooser();
        aFileChooser.setFileFilter(new MyFileFilterXML());
        if (lastUsedFile != null){
			aFileChooser.setCurrentDirectory(lastUsedFile.getParentFile());
            aFileChooser.setSelectedFile(lastUsedFile);
        }else{
            File file = new File(aFileChooser.getCurrentDirectory(), dataset.getName()+".xml");
            aFileChooser.setSelectedFile(file);
        }
        int r = aFileChooser.showSaveDialog(this);
        if (r == JFileChooser.APPROVE_OPTION){
            File file = aFileChooser.getSelectedFile();
            if(!MyUtilities.isXMLFile(file)){
                file = MyUtilities.getXMLFile(file);
            }
			lastUsedFile = file;
			OutputStreamWriter fileWriter = null;
			try
			{
				fileWriter = new OutputStreamWriter(new FileOutputStream(file), "utf-8");
				xmlOutputter.output(pds, fileWriter);
                datasetModif = false;
                dataProcessToolPanel.logSaveDataset(dataset);
			}
			catch (IOException e)
			{
				displayError(new CopexReturn(getBundleString("MSG_ERROR_SAVE"), false), getBundleString("TITLE_DIALOG_ERROR"));
			}
			finally
			{
				if (fileWriter != null)
					try
					{
						fileWriter.close();
					}
					catch (IOException e)
					{
						displayError(new CopexReturn(getBundleString("MSG_ERROR_SAVE"), false), getBundleString("TITLE_DIALOG_ERROR"));
					}
			}
        }
    }

    private boolean isAllSelectionIgnore(){
        return this.datasetTable.isAllSelectionIgnore();
    }

    public boolean hasModification(){
        return datasetModif;
    }
    public void setModification(){
        this.datasetModif = true;
    }

    /* log: undo*/
    public void logUndo(DataUndoRedo undoAction){
        dataProcessToolPanel.logUndo(dataset, undoAction);
    }

    /* log: undo*/
    public void logRedo(DataUndoRedo redoAction){
        dataProcessToolPanel.logRedo(dataset, redoAction);
    }

    public void setPreviousZoom(Graph graph){
        InternalGraphFrame gFrame = getInternalFrame(graph.getDbKey());
        if(gFrame != null){
            gFrame.setPreviousZoom();
        }
    }

    public ArrayList<PreDefinedFunctionCategory> getListPreDefinedFunction(){
        return this.listPreDefinedFunction;
    }

    /* ouverture fenetre choix fichier pour csv */
    private void openCsvDialog(){
        JFileChooser aFileChooser = new JFileChooser();
        aFileChooser.setFileFilter(new MyFileFilterCSV());
        if (lastUsedFileCSV != null){
            aFileChooser.setCurrentDirectory(lastUsedFileCSV.getParentFile());
            aFileChooser.setSelectedFile(lastUsedFileCSV);
        }else{
            File file = new File(aFileChooser.getCurrentDirectory(), dataset.getName()+".csv");
            aFileChooser.setSelectedFile(file);
        }
        int r = aFileChooser.showSaveDialog(this);
        if (r == JFileChooser.APPROVE_OPTION){
            File file = aFileChooser.getSelectedFile();
            if(!MyUtilities.isCSVFile(file)){
                file = MyUtilities.getCSVFile(file);
            }
            lastUsedFileCSV = file;
            String sep = getCSVSeparator();
            PrintWriter writer = null;
            try{
                writer = new PrintWriter(new BufferedWriter (new OutputStreamWriter(new FileOutputStream(file), "utf-8")));
                // header
                String s = "";
                for(int j=0; j<dataset.getListDataHeader().length; j++){
                    s += dataset.getListDataHeader()[j] == null? "" : dataset.getListDataHeader()[j].getValue();
                    if(j <dataset.getListDataHeader().length -1)
                        s+= sep;
                }
                writer.println(s);
                // data
                Data[][] data = dataset.getData();
                int nbR = dataset.getNbRows();
                int nbC = dataset.getNbCol();
                for(int i=0; i<nbR; i++){
                    s = "";
                    for(int j=0; j<nbC; j++){
                        if(data[i][j] != null){
                            if(data[i][j].isDoubleValue()){
                                if(!Double.isNaN(data[i][j].getDoubleValue()))
                                    s += NumberFormat.getNumberInstance(getLocale()).format(data[i][j].getDoubleValue());
                            }else{
                                s += data[i][j].getValue();
                            }
                        }
                        if(j <nbC -1)
                            s+= sep;
                    }
                    writer.println(s);
                }
                //log
		dataProcessToolPanel.logExportCSV(dataset, file.getPath());
            }catch (IOException e){
                displayError(new CopexReturn(getBundleString("MSG_ERROR_CSV"), false), getBundleString("TITLE_DIALOG_ERROR"));
            }
            finally{
                if (writer != null)
                    try{
                        writer.close();
                    }catch (Exception e){
                        displayError(new CopexReturn(getBundleString("MSG_ERROR_CSV"), false), getBundleString("TITLE_DIALOG_ERROR"));
                    }
            }
        }
    }

    // selon locale
    private String getCSVSeparator(){
        DecimalFormatSymbols s = new DecimalFormatSymbols(getLocale());
        if(s.getDecimalSeparator() == ',')
            return ";";
        else
            return ",";
    }

    public void importCsvData(String sepField, String sepText, String charEncoding){
        dataProcessToolPanel.importCsvData(sepField, sepText, charEncoding);
    }

    public ArrayList<Object> getListGraph(){
        ArrayList<Object> list = new ArrayList();
        int nbVis = dataset.getListVisualization().size();
        for (int i=0; i<nbVis; i++){
            InternalGraphFrame gFrame = getInternalFrame(dataset.getListVisualization().get(i).getDbKey());
            if(gFrame != null){
               Object o = gFrame.getGraphPDF();
                if(o!=null)
                   list.add(o);
            }
        }
        return list;
    }

    /* returns the interface panel for the thumbnail */
    public Container getInterfacePanel(){
        datasetTable.setSize(128,128);
        return datasetTable;
    }

    public void openImportDialog(){
        dataProcessToolPanel.openDialogImport();
    }

    public DataTableModel getDataTableModel(){
        return this.datasetTable.getTableModel();
    }


    public void exportHTML(){
        controller.exportHTML();
    }
}
