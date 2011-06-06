/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.dataTool;

import eu.scy.client.tools.fitex.main.DataProcessToolPanel;
import eu.scy.client.tools.dataProcessTool.common.DataHeader;
import eu.scy.client.tools.dataProcessTool.common.Dataset;
import eu.scy.client.tools.dataProcessTool.common.FunctionModel;
import eu.scy.client.tools.dataProcessTool.common.Graph;
import eu.scy.client.tools.dataProcessTool.common.ParamGraph;
import eu.scy.client.tools.dataProcessTool.common.SimpleVisualization;
import eu.scy.client.tools.dataProcessTool.common.TypeVisualization;
import eu.scy.client.tools.dataProcessTool.common.Visualization;
import eu.scy.client.tools.dataProcessTool.utilities.ActionMenu;
import eu.scy.client.tools.dataProcessTool.utilities.DataConstants;
import eu.scy.client.tools.dataProcessTool.utilities.MyMenuItem;
import eu.scy.client.tools.fitex.GUI.FitexPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

/**
 * internal frame which contains a graph
 * @author Marjolaine
 */
public class InternalGraphFrame extends JInternalFrame implements ActionMenu, InternalFrameListener, ComponentListener, FocusListener{
    private FitexToolPanel fitex;
    private Dataset dataset;
    private Visualization visualization;

    private JMenuBar menuBarGraph;
    private JSeparator sepV1;
    private MyMenuItem menuItemParam;
    private MyMenuItem menuItemAutoScale;
    private MyMenuItem menuItemUnZoom;
    private MyMenuItem menuItemMove;
    private MyMenuItem menuItemFunction;

    private JPanel panelGraph;
    private JPanel mainPanel;
    private JScrollPane scrollPane;
    private CopexGraph cGraph;

    public InternalGraphFrame(FitexToolPanel fitex, Dataset dataset, Visualization visualization) {
        super();
        this.fitex = fitex;
        this.dataset = dataset;
        this.visualization = visualization;
        initGUI();
    }

    private void initGUI(){
        this.setTitle(visualization.getName());
        this.setResizable(true);
        this.setMaximizable(true);
        this.setIconifiable(true);
        this.setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addInternalFrameListener(this);
        this.addComponentListener(this);
        setLayout(new BorderLayout());
        this.add(getMainPanel(), BorderLayout.CENTER);
        mainPanel.add(getGraphMenuBar(), BorderLayout.NORTH);
        mainPanel.add(getPanelGraph(), BorderLayout.CENTER);
    }

    /* main panel */
    private JPanel getMainPanel(){
        if(this.mainPanel == null){
            mainPanel = new JPanel();
            mainPanel.setName("mainPanel");
            mainPanel.setLayout(new BorderLayout());
        }
        return mainPanel;
    }

    /* menu Bar graph */
    private JMenuBar getGraphMenuBar(){
        if (this.menuBarGraph == null){
            this.menuBarGraph = new JMenuBar();
            this.menuBarGraph.setLayout( null);
            this.menuBarGraph.setName("menuBarGraph");
            this.menuBarGraph.setSize(this.getWidth(), DataProcessToolPanel.MENU_BAR_HEIGHT);
            this.menuBarGraph.setPreferredSize(this.menuBarGraph.getSize());
            menuBarGraph.add(getMenuItemParam());
            if (visualization.getType().getCode() == DataConstants.VIS_GRAPH){
                menuBarGraph.add(getMenuItemMove());
                menuBarGraph.add(getMenuItemAutoScale());
                menuBarGraph.add(getMenuItemUnZoom());
                menuBarGraph.add(getSepV1());
                menuBarGraph.add(getMenuItemFunction());
            }
        }
        return this.menuBarGraph;
    }

    
    private MyMenuItem getMenuItemParam(){
        if (menuItemParam == null){
            menuItemParam = new MyMenuItem(fitex.getBundleString("TOOLTIPTEXT_MENU_GRAPH_PARAM"),menuBarGraph.getBackground(),fitex.getCopexImage("Bouton-AdT-28_graph_param.png"), fitex.getCopexImage("Bouton-AdT-28_graph_param_sur.png"), fitex.getCopexImage("Bouton-AdT-28_graph_param_cli.png"),fitex.getCopexImage("Bouton-AdT-28_graph_param_gri.png"));
            menuItemParam.setBounds(0, 0, menuItemParam.getWidth(), menuItemParam.getHeight());
            menuItemParam.addActionMenu(this);
        }
        return menuItemParam;
    }

     private JSeparator getSepV1(){
        if(sepV1 == null){
            sepV1 = new JSeparator(JSeparator.VERTICAL);
            sepV1.setBounds(menuItemUnZoom.getX()+menuItemUnZoom.getWidth(), 0, 5, DataProcessToolPanel.MENU_BAR_HEIGHT);
        }
        return sepV1;
    }
    private MyMenuItem getMenuItemAutoScale(){
        if (menuItemAutoScale == null){
            menuItemAutoScale = new MyMenuItem(fitex.getBundleString("TOOLTIPTEXT_MENU_GRAPH_AUTOSCALE"),menuBarGraph.getBackground(),fitex.getCopexImage("Bouton-AdT-28_graph_autoscale.png"), fitex.getCopexImage("Bouton-AdT-28_graph_autoscale_sur.png"), fitex.getCopexImage("Bouton-AdT-28_graph_autoscale_cli.png"), fitex.getCopexImage("Bouton-AdT-28_graph_autoscale_gri.png"));
            menuItemAutoScale.setBounds(menuItemMove.getX()+menuItemMove.getWidth(), 0, menuItemAutoScale.getWidth(), menuItemAutoScale.getHeight());
            menuItemAutoScale.addActionMenu(this);
        }
        return menuItemAutoScale;
    }

    private MyMenuItem getMenuItemMove(){
        if (menuItemMove == null){
            menuItemMove = new MyMenuItem(fitex.getBundleString("TOOLTIPTEXT_MENU_GRAPH_MOVE"),menuBarGraph.getBackground(),fitex.getCopexImage("Bouton-AdT-28_graph_move.png"), fitex.getCopexImage("Bouton-AdT-28_graph_move_sur.png"), fitex.getCopexImage("Bouton-AdT-28_graph_move_cli.png"), fitex.getCopexImage("Bouton-AdT-28_graph_move_gri.png"));
            menuItemMove.setBounds(menuItemParam.getX()+menuItemParam.getWidth(), 0, menuItemMove.getWidth(), menuItemMove.getHeight());
            menuItemMove.addActionMenu(this);
        }
        return menuItemMove;
    }

    private MyMenuItem getMenuItemUnZoom(){
        if (menuItemUnZoom == null){
            menuItemUnZoom = new MyMenuItem(fitex.getBundleString("TOOLTIPTEXT_MENU_GRAPH_PREVIOUS_ZOOM"),menuBarGraph.getBackground(),fitex.getCopexImage("zoomPrev.png"), fitex.getCopexImage("zoomPrev_survol.png"), fitex.getCopexImage("zoomNext.png"), fitex.getCopexImage("zoomPrev_grise.png"));
            menuItemUnZoom.setBounds(menuItemAutoScale.getX()+menuItemAutoScale.getWidth(), 0, menuItemUnZoom.getWidth(), menuItemUnZoom.getHeight());
            menuItemUnZoom.addActionMenu(this);
        }
        return menuItemUnZoom;
    }
    
    private MyMenuItem getMenuItemFunction(){
        if (menuItemFunction == null){
            menuItemFunction = new MyMenuItem(fitex.getBundleString("TOOLTIPTEXT_MENU_GRAPH_CURVE"),menuBarGraph.getBackground(),fitex.getCopexImage("Bouton-AdT-28_graph.png"), fitex.getCopexImage("Bouton-AdT-28_graph_sur.png"), fitex.getCopexImage("Bouton-AdT-28_graph_cli.png"), fitex.getCopexImage("Bouton-AdT-28_graph_gri.png"));
            menuItemFunction.setBounds(sepV1.getX()+sepV1.getWidth(), 0, menuItemFunction.getWidth(), menuItemFunction.getHeight());
            menuItemFunction.addActionMenu(this);
        }
        return menuItemFunction;
    }

    private JPanel getPanelGraph(){
        if(panelGraph == null){
            panelGraph = new JPanel();
            panelGraph.setName("panelGraph");
            panelGraph.setLayout(new BorderLayout());
            constructGraph();
        }
        return panelGraph;
    }

    private void constructGraph(){
        cGraph = null;
        TypeVisualization type = visualization.getType() ;
        if (type.getCode() == DataConstants.VIS_PIE){
            cGraph = constructPieGraph();
            boolean canEdit = dataset.getRight() == DataConstants.EXECUTIVE_RIGHT;
            this.menuItemParam.setEnabled(canEdit);
        }else if (type.getCode() == DataConstants.VIS_BAR){
            cGraph = constructBarGraph();
            boolean canEdit = dataset.getRight() == DataConstants.EXECUTIVE_RIGHT;
            this.menuItemParam.setEnabled(canEdit);
        }else if (type.getCode() == DataConstants.VIS_GRAPH){
            cGraph = constructXYGraph();
            updateMenuGraph();
        }
        scrollPane = new JScrollPane(cGraph);
        scrollPane.setSize(this.getWidth() -10, this.getHeight() -10);
        this.panelGraph.add(scrollPane);
    }

    private CopexGraph constructPieGraph(){
        DataHeader header = dataset.getDataHeader(((SimpleVisualization)visualization).getNoCol());
        DataHeader labelHeader = ((SimpleVisualization)visualization).getHeaderLabel();
        DefaultPieDataset pieDataset = new DefaultPieDataset();
        for (int i=0; i<dataset.getNbRows(); i++){
            int no = i+1;
            if (dataset.getData(i, header.getNoCol()) != null && !dataset.getData(i, header.getNoCol()).isIgnoredData()){
                String label = ""+no;
                if(labelHeader != null){
                    label = dataset.getData(i, labelHeader.getNoCol()) == null ? "" : dataset.getData(i, labelHeader.getNoCol()).getValue();
                }
                pieDataset.setValue(label, dataset.getData(i, header.getNoCol()).getDoubleValue());
            }
        }

        JFreeChart pieChart = ChartFactory.createPieChart(header.getValue(), pieDataset, true, true, true);
        ChartPanel cPanel = new ChartPanel(pieChart, false);
        // in a panel for the resize
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        p.setPreferredSize(getSize());
        p.add(cPanel, BorderLayout.CENTER);
        return  new CopexGraph(fitex, dataset.getDbKey(), visualization, p) ;
    }

    private CopexGraph constructBarGraph(){
        DataHeader header = dataset.getDataHeader(((SimpleVisualization)visualization).getNoCol());
        DataHeader labelHeader = ((SimpleVisualization)visualization).getHeaderLabel();
        DefaultCategoryDataset ds = new DefaultCategoryDataset();
        for (int i=0; i<dataset.getNbRows(); i++){
            int no = i+1;
            if (dataset.getData(i, header.getNoCol()) != null && !dataset.getData(i, header.getNoCol()).isIgnoredData()){
                String label = ""+no;
                if(labelHeader != null){
                    label = dataset.getData(i, labelHeader.getNoCol()) == null ? "" : dataset.getData(i, labelHeader.getNoCol()).getValue();
                }
                ds.addValue(dataset.getData(i, header.getNoCol()).getDoubleValue(), label, label);
            }
        }

        JFreeChart barChart = ChartFactory.createBarChart(header.getValue(), "",
                "", ds, PlotOrientation.VERTICAL, true, true, false);
        ChartPanel cPanel = new ChartPanel(barChart);
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        p.setPreferredSize(getSize());
        p.add(cPanel, BorderLayout.CENTER);
        return new CopexGraph(fitex,dataset.getDbKey(), visualization, p) ;
    }

    private CopexGraph constructXYGraph(){
        int nbPlots = ((Graph)visualization).getNbPlots();
        DefaultTableModel[] datamodel = new DefaultTableModel[nbPlots];
        Color[] plotsColor = new Color[nbPlots];
        int nbRows = dataset.getNbRows();
        for (int d=0; d<nbPlots; d++){
            DataHeader header1 = dataset.getDataHeader(((Graph)visualization).getParamGraph().getPlots().get(d).getHeaderX().getNoCol());
            DataHeader header2 = dataset.getDataHeader(((Graph)visualization).getParamGraph().getPlots().get(d).getHeaderY().getNoCol());
            int id1 = header1.getNoCol() ;
            int id2 = header2.getNoCol();
            Object[][] datas = new Object[dataset.getNbRows()][3];
            for (int i=0; i<nbRows; i++){
                if (dataset.getData(i, id1) != null && dataset.getData(i, id2) !=null ){
                    datas[i][0] = dataset.getData(i, id1).getDoubleValue();
                    datas[i][1] = dataset.getData(i, id2).getDoubleValue();
                    datas[i][2] = dataset.getData(i, id1).isIgnoredData() || dataset.getData(i, id2).isIgnoredData() ;
                }
            }
            plotsColor[d] = ((Graph)visualization).getParamGraph().getPlots().get(d).getPlotColor();
            String[] columnNames = new String[3];
            datamodel[d] = new DefaultTableModel(datas, columnNames);
        }
        ArrayList<FunctionModel> listFunctionModel = ((Graph)visualization).getListFunctionModel() ;
        ParamGraph pg = ((Graph)visualization).getParamGraph() ;
        FitexPanel gPanel = new FitexPanel(fitex, datamodel, plotsColor, listFunctionModel,pg, dataset.getRight()) ;
        return new FitexGraph(fitex, dataset.getDbKey(), visualization, gPanel) ;
    }

    public void updateDataset(Dataset ds, Visualization vis, boolean update){
        this.visualization = vis;
        this.dataset = ds;
        panelGraph.removeAll();
        constructGraph();
        panelGraph.revalidate();
        panelGraph.repaint();

    }

    public Visualization getVisualization() {
        return visualization;
    }

    


    public void updateVisualization(Visualization vis){
        this.visualization = vis;
        if(cGraph.getGraphComponent() instanceof FitexPanel && vis instanceof Graph){
            ParamGraph pg = ((Graph)vis).getParamGraph() ;
            ((FitexPanel)cGraph.getGraphComponent()).setParameters(pg);
        }
    }

    @Override
    public void actionClick(MyMenuItem item) {
        if (item.equals(getMenuItemParam())){
            fitex.openDialogGraphParam(visualization);
            return;
        }else if (item.equals(getMenuItemMove())){
            char graphMode = DataConstants.MODE_DEFAULT;
            if(cGraph instanceof FitexGraph){
                graphMode = ((FitexGraph)cGraph).getFitexPanel().updateGraphMode();
            }
            fitex.setGraphMode(visualization, graphMode);
            updateMenuGraph();
            return;
        }else if (item.equals(getMenuItemAutoScale())){
            ((FitexPanel)cGraph.getGraphComponent()).setPreviousParam();
            fitex.autoscale(visualization);
            updateMenuGraph();
            return;
        }else if (item.equals(getMenuItemFunction())){
            displayFunctionModel();
            updateMenuGraph();
            return;
        }else if (item.equals(getMenuItemUnZoom())){
            if(cGraph.getGraphComponent() instanceof FitexPanel){
               ((FitexPanel)(cGraph.getGraphComponent())).previousParam();
               updateMenuGraph();
               return;
            }
        }
    }

    private void displayFunctionModel(){
        if(this.cGraph != null && cGraph instanceof FitexGraph){
            ((FitexGraph)cGraph).getFitexPanel().displayFunctionModel();
        }
    }

    private char getGraphMode(){
        if(this.cGraph != null && cGraph instanceof FitexGraph){
            return ((FitexGraph)cGraph).getFitexPanel().getGraphMode();
        }
        return DataConstants.MODE_DEFAULT ;
    }


     private boolean isDisplayFunctionModel(){
        if(this.cGraph != null && cGraph instanceof FitexGraph){
            return ((FitexGraph)cGraph).getFitexPanel().isDisplayFunctionModel();
        }
        return false;
    }

    


    /* update menu */
    public void updateMenuGraph(){
        boolean enabled = visualization instanceof Graph;
        boolean canEdit = dataset.getRight() == DataConstants.EXECUTIVE_RIGHT;
        char graphMode = getGraphMode();
        this.menuItemParam.setEnabled(canEdit);
        this.menuItemAutoScale.setEnabled(canEdit && enabled);
        this.menuItemMove.setEnabled(canEdit && enabled );
        this.menuItemUnZoom.setEnabled(canEdit && enabled );
        if(cGraph.getGraphComponent() instanceof FitexPanel){
            boolean isPreviousParam = ((FitexPanel)(cGraph.getGraphComponent())).isPreviousParam();
            boolean isNextParam = ((FitexPanel)(cGraph.getGraphComponent())).isNextParam();
            this.menuItemUnZoom.setEnabled(isPreviousParam || isNextParam);
            if(isPreviousParam){
                this.menuItemUnZoom.setItemIcon(fitex.getCopexImage("zoomPrev.png"));
                this.menuItemUnZoom.setItemRolloverIcon(fitex.getCopexImage("zoomPrev_survol.png"));
            }else if (isNextParam){
                this.menuItemUnZoom.setItemIcon(fitex.getCopexImage("zoomNext.png"));
                this.menuItemUnZoom.setItemRolloverIcon(fitex.getCopexImage("zoomNext_survol.png"));
            }
        }
        this.menuItemFunction.setEnabled(canEdit);
        if(isDisplayFunctionModel()){
            this.menuItemFunction.setItemIcon(fitex.getCopexImage("Bouton-AdT-28_graph_push.png"));
            this.menuItemFunction.setItemRolloverIcon(fitex.getCopexImage("Bouton-AdT-28_graph_surcli.png"));
        }else{
            this.menuItemFunction.setItemIcon(fitex.getCopexImage("Bouton-AdT-28_graph.png"));
            this.menuItemFunction.setItemRolloverIcon(fitex.getCopexImage("Bouton-AdT-28_graph_sur.png"));
        }
        switch (graphMode){
            case DataConstants.MODE_MOVE :
                this.menuItemMove.setItemIcon(fitex.getCopexImage("Bouton-AdT-28_graph_move_push.png"));
                this.menuItemMove.setItemRolloverIcon(fitex.getCopexImage("Bouton-AdT-28_graph_move_surcli.png"));
                this.menuItemMove.setToolTipText(fitex.getBundleString("TOOLTIPTEXT_MENU_GRAPH_ZOOM"));
                break;
            case DataConstants.MODE_ZOOM :
                this.menuItemMove.setItemIcon(fitex.getCopexImage("Bouton-AdT-28_graph_move.png"));
                this.menuItemMove.setItemRolloverIcon(fitex.getCopexImage("Bouton-AdT-28_graph_move_sur.png"));
                this.menuItemMove.setToolTipText(fitex.getBundleString("TOOLTIPTEXT_MENU_GRAPH_MOVE"));
                break;
        }
        repaint();
    }

    @Override
    public void internalFrameOpened(InternalFrameEvent e) {

    }

    @Override
    public void internalFrameClosing(InternalFrameEvent e) {
        boolean isOk = fitex.closeGraph(visualization);
        if(isOk){
            this.dispose();
        }
    }

    @Override
    public void internalFrameClosed(InternalFrameEvent e) {
        if(isIcon)
            fitex.removeIcon();
    }

    @Override
    public void internalFrameIconified(InternalFrameEvent e) {
        fitex.addIcon();
    }

    @Override
    public void internalFrameDeiconified(InternalFrameEvent e) {
        fitex.removeIcon();
    }

    @Override
    public void internalFrameActivated(InternalFrameEvent e) {

    }

    @Override
    public void internalFrameDeactivated(InternalFrameEvent e) {

    }

    public Object getGraphPDF(){
        if(cGraph instanceof FitexGraph){
            return  ((FitexGraph)cGraph).getFitexPanel().getDrawPanel();
        }else if (cGraph instanceof JPanel){
            JPanel p = ((JPanel)cGraph);
            if (p.getComponentCount()>0 && p.getComponent(0) instanceof JPanel){
                JPanel p2 =(JPanel)p.getComponent(0);
                if (p2.getComponentCount()>0 && p2.getComponent(0) instanceof ChartPanel){
                    ChartPanel cPanel = (ChartPanel)p2.getComponent(0);
                        return cPanel.getChart();
                 }
             }
         }
        return null;
    }

    
    public void updateVisualizationName(String newName){
        this.visualization.setName(newName);
        setTitle(newName);
    }

    public void setPreviousZoom(){
        updateMenuGraph();
    }

    @Override
    public void componentResized(ComponentEvent e) {
        if(cGraph instanceof FitexGraph){
            int min = 150;
            ((FitexGraph)cGraph).getFitexPanel().updateSize(Math.max(min,panelGraph.getWidth()-25), Math.max(min,panelGraph.getHeight()-20));
        }
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }

    @Override
    public void focusGained(FocusEvent e) {

    }

    @Override
    public void focusLost(FocusEvent e) {
        fitex.exportHTML();
    }
}
