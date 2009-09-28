/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.dataTool;


import eu.scy.elo.contenttype.dataset.DataSetRow;
import eu.scy.tools.dataProcessTool.common.Data;
import eu.scy.tools.dataProcessTool.common.DataHeader;
import eu.scy.tools.dataProcessTool.common.DataOperation;
import eu.scy.tools.dataProcessTool.common.Dataset;
import eu.scy.tools.dataProcessTool.common.FunctionParam;
import eu.scy.tools.dataProcessTool.common.Graph;
import eu.scy.tools.dataProcessTool.common.ParamGraph;
import eu.scy.tools.dataProcessTool.common.TypeOperation;
import eu.scy.tools.dataProcessTool.common.TypeOperationParam;
import eu.scy.tools.dataProcessTool.common.TypeVisualization;
import eu.scy.tools.dataProcessTool.common.Visualization;
import eu.scy.tools.dataProcessTool.controller.ControllerInterface;
import eu.scy.tools.dataProcessTool.print.PrintDialog;
import eu.scy.tools.dataProcessTool.undoRedo.DeleteUndoRedo;
import eu.scy.tools.dataProcessTool.undoRedo.EditDataUndoRedo;
import eu.scy.tools.dataProcessTool.undoRedo.EditHeaderUndoRedo;
import eu.scy.tools.dataProcessTool.undoRedo.IgnoreDataUndoRedo;
import eu.scy.tools.dataProcessTool.undoRedo.InsertUndoRedo;
import eu.scy.tools.dataProcessTool.undoRedo.OperationUndoRedo;
import eu.scy.tools.dataProcessTool.undoRedo.PasteUndoRedo;
import eu.scy.tools.dataProcessTool.utilities.ActionMenu;
import eu.scy.tools.dataProcessTool.utilities.CopexReturn;
import eu.scy.tools.dataProcessTool.utilities.DataConstants;
import eu.scy.tools.dataProcessTool.utilities.ElementToSort;
import eu.scy.tools.dataProcessTool.utilities.MyFileFilterXML;
import eu.scy.tools.dataProcessTool.utilities.MyMenuItem;
import eu.scy.tools.dataProcessTool.utilities.MyUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    private File lastUsedFile = null;
    private XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
    
    // IHM
    /* panel dataset */
    private JPanel panelDataset;
    private JDesktopPane desktopPane;
    private ArrayList<InternalGraphFrame> listGraphFrame;
    private int nbIcon;
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
    /* data organizer*/
    private JScrollPane scrollPaneDataOrganizer;
    private DataTable datasetTable;

    public FitexToolPanel(DataProcessToolPanel dataProcessToolPanel, Dataset dataset, ControllerInterface controller, TypeVisualization[] tabTypeVis, TypeOperation[] tabTypeOp) {
        super();
        this.dataProcessToolPanel = dataProcessToolPanel;
        this.dataset = dataset;
        this.controller = controller;
        this.tabTypeVis = tabTypeVis;
        this.tabTypeOp = tabTypeOp;
        initGUI();
    }

    /* initialisation de l'applet */
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
            if(dataProcessToolPanel.canPrint()){
                menuBarData.add(getSep6());
                menuBarData.add(getMenuItemPrint());
            }

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

    private JSeparator getSep7(){
        if(sep7 == null){
            sep7 = new JSeparator(JSeparator.VERTICAL);
            sep7.setBounds(menuItemSave.getX()+menuItemSave.getWidth(), 0, 5, DataProcessToolPanel.MENU_BAR_HEIGHT);
        }
        return sep7;
    }


    private MyMenuItem getMenuItemInsert(){
        if (menuItemInsert == null){
            menuItemInsert = new MyMenuItem(getBundleString("TOOLTIPTEXT_MENU_INSERT"),menuBarData.getBackground(),getCopexImage("Bouton-AdT-28_insert.png"), getCopexImage("Bouton-AdT-28_insert_sur.png"), getCopexImage("Bouton-AdT-28_insert_cli.png"), getCopexImage("Bouton-AdT-28_insert_gri.png"));
            int x = 0;
            if(dataProcessToolPanel.canSave())
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
        boolean isData = isData();
        this.menuItemAddGraph.setEnabled(isData);
        if(menuItemPrint != null)
            this.menuItemPrint.setEnabled(dataset != null);
        repaint();
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
            datasetTable.copy();
            return;
        }else if (item.equals(getMenuItemPaste())){
            datasetTable.paste();
            return;
        }else if (item.equals(getMenuItemCut())){
            datasetTable.cut();
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
        }

        displayError(new CopexReturn("Not yet implemented !!", false), "En travaux");
    }


    public void openDialogGraphParam(Visualization vis){
        ParamGraph paramGraph = null;
        if (vis != null && vis instanceof Graph){
            paramGraph = ((Graph)vis).getParamGraph();
        }
        GraphParamDialog dialog = new GraphParamDialog(this, ((Graph)vis), dataset.getListDataHeader());
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
            ParamGraph paramGraph = new ParamGraph(header1, header2, -10, 10,  -10,10,1,1, true);
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
        createInternalGraphFrame(vis);
        return true;
    }


    private void createInternalGraphFrame(Visualization vis){
        int nb = listGraphFrame.size();
        InternalGraphFrame gFrame = new InternalGraphFrame(this, dataset, vis);
        int y = this.scrollPaneDataOrganizer.getY()+nb*20;
        int h = panelDataset.getHeight() - panelMenuData.getHeight()-y-20;
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
        CopexReturn cr = this.controller.setDataIgnored(ds, isIgnored, listData,v);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }
        Dataset nds = (Dataset)v.get(0);
        dataset = nds;
        datasetTable.updateDataset(nds, true);
        updateGraphs(nds, true);
        datasetTable.addUndo(new IgnoreDataUndoRedo(datasetTable, this, controller, isIgnored, listData));
    }

    private void  updateGraphs(Dataset ds, boolean update){
        int nb = listGraphFrame.size();
        for(int i=0; i<nb; i++){
            long dbKeyVis = listGraphFrame.get(i).getVisualization().getDbKey();
            Visualization vis = ds.getVisualization(dbKeyVis);
            if(vis != null)
                listGraphFrame.get(i).updateDataset(ds, vis, update);
        }
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
        CopexReturn cr = this.controller.deleteData(false, ds, listData, listOperation, listRowAndCol, v);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return;
        }else if (cr.isWarning()){
            v = new ArrayList();
            boolean isOk = displayError(cr, getBundleString("TITLE_DIALOG_CONFIRM"));
            if (isOk){
                cr = this.controller.deleteData(true, ds, listData, listOperation, listRowAndCol, v);
                if (cr.isError()){
                    displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
                    return;
                }
            }
            if(v.size() == 0){
                //suppression
            }else{
                Dataset newDs = (Dataset)v.get(0);
                updateDataset(newDs);
            }
        }
        datasetTable.addUndo(new DeleteUndoRedo(datasetTable, this, controller, listData, listHeader, listRowAndCol, listOperation));
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
        int n = ds.getListVisualization().size() ;
       for (int i=0; i<n; i++){
           createInternalGraphFrame(ds.getListVisualization().get(i));
       }
    }

   

    /*merge d'un ELO avec le courant */
    public void mergeELO(Element elo){
        CopexReturn cr = this.controller.mergeELO(dataset, elo);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
        }
    }

    public Element getPDS(){
        return this.controller.getPDS(dataset);
    }

    /* creation ou mise a jour d'une fonction modele */
    public void setFunctionModel(Graph graph, String description, Color fColor, ArrayList<FunctionParam> listParam ){
        ArrayList v = new ArrayList();
        CopexReturn cr = this.controller.setFunctionModel(dataset, graph, description, fColor, listParam, v);
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
        if(isOnCol)
            datasetTable.selectCols(idBefore, nb);
        else
            datasetTable.selectRows(idBefore, nb);
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
        //boolean autoScale = getDataVisTabbedPane().isAutoScale();
        boolean autoScale = true;
        CopexReturn cr = this.controller.addData(dbKeyDs, values,  autoScale, v);
        if (cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return ;
        }
        Dataset nds = (Dataset)v.get(0);
        dataset = nds;
        datasetTable.updateDataset(nds, true);
    }

    /* mis a jour des parametres */
    public boolean updateGraphParam(Graph graph, ParamGraph pg){
        DataHeader hxold = graph.getParamGraph().getHeaderX();
        DataHeader hyold = graph.getParamGraph().getHeaderY();
        DataHeader hx = pg.getHeaderX();
        DataHeader hy = pg.getHeaderY();
        boolean sameAxis = hxold.getDbKey()== hx.getDbKey() && hyold.getDbKey() == hy.getDbKey();
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
                gFrame.modifyVisualization(newvis);
        }
        return true;
    }



    /* maj autoscale */
    public void setAutoScale(long dbKeyDs, long dbKeyVis, boolean autoScale){
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
        updateGraphs(nds, true);

        datasetTable.addUndo(new PasteUndoRedo(datasetTable, this, controller, subData, selCell, listData, listDataHeader, listRowAndCol));
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
            setAutoScale(dataset.getDbKey(), vis.getDbKey(), graphMode == DataConstants.MODE_AUTOSCALE);
        }
    }

    /* fermeture d'un graphe */
    public boolean closeGraph(Visualization vis){
        int ok = JOptionPane.showConfirmDialog(this, this.getBundleString("MESSAGE_EXIT"), this.getBundleString("TITLE_DIALOG_EXIT"),JOptionPane.OK_CANCEL_OPTION );
        if(ok == JOptionPane.OK_OPTION){
            deleteVisualization(vis);
            int nb = dataset.getListVisualization().size();
            return true;
        }else
            return false;
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


    

    public Dataset getDataset(){
        return this.dataset;
    }
    /* retourne le nom du dataset */
    public String getDatasetName(){
        return this.dataset.getName();
    }

    /* renomme le nom du dataset, retourne le nom */
    public String renameDataset(){
        RenameDatasetDialog dialog = new RenameDatasetDialog(this, dataset.getName());
        dialog.setVisible(true);
        return this.dataset.getName();
    }

    public boolean renameDataset(String name){
        CopexReturn cr = this.controller.renameDataset(dataset, name);
        if(cr.isError()){
            displayError(cr, getBundleString("TITLE_DIALOG_ERROR"));
            return false;
        }
        this.dataset.setName(name);
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
        System.out.println("addIcon "+nbIcon);
        setPanelDatasetHeight();
    }
    public void removeIcon(){
        nbIcon--;
        System.out.println("removeIcon "+nbIcon);
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
        return new Point( (int)this.getLocationOnScreen().getX() +(this.getWidth() /3), (int)this.getLocationOnScreen().getY()+this.menuBarData.getHeight());
    }

    /* sauvegarde*/
    private void saveFitex(){
        Element pds = getPDS() ;
        JFileChooser aFileChooser = new JFileChooser();
        aFileChooser.setFileFilter(new MyFileFilterXML());
        if (lastUsedFile != null)
			aFileChooser.setCurrentDirectory(lastUsedFile.getParentFile());
        int r = aFileChooser.showSaveDialog(this);
        if (r == JFileChooser.APPROVE_OPTION){
            File file = aFileChooser.getSelectedFile();
            if(!MyUtilities.isXMLFile(file)){
                displayError(new CopexReturn(getBundleString("MSG_ERROR_FILE_XML"), false), getBundleString("TITLE_DIALOG_ERROR"));
                return;
            }
			lastUsedFile = file;
			FileWriter fileWriter = null;
			try
			{
				fileWriter = new FileWriter(file);
				xmlOutputter.output(pds, fileWriter);
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
}
