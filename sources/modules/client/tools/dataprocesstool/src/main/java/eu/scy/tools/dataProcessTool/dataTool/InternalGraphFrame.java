/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.dataTool;

import eu.scy.tools.dataProcessTool.common.DataHeader;
import eu.scy.tools.dataProcessTool.common.Dataset;
import eu.scy.tools.dataProcessTool.common.FunctionModel;
import eu.scy.tools.dataProcessTool.common.Graph;
import eu.scy.tools.dataProcessTool.common.ParamGraph;
import eu.scy.tools.dataProcessTool.common.SimpleVisualization;
import eu.scy.tools.dataProcessTool.common.TypeVisualization;
import eu.scy.tools.dataProcessTool.common.Visualization;
import eu.scy.tools.dataProcessTool.utilities.ActionMenu;
import eu.scy.tools.dataProcessTool.utilities.DataConstants;
import eu.scy.tools.dataProcessTool.utilities.MyMenuItem;
import eu.scy.tools.fitex.GUI.FitexPanel;
import java.awt.BorderLayout;
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
public class InternalGraphFrame extends JInternalFrame implements ActionMenu, InternalFrameListener{
    private FitexToolPanel fitex;
    private Dataset dataset;
    private Visualization visualization;

    private JMenuBar menuBarGraph;
    private JSeparator sepV1;
    private JSeparator sepV2;
    private MyMenuItem menuItemParam;
    private MyMenuItem menuItemAutoScale;
    private MyMenuItem menuItemZoom;
    private MyMenuItem menuItemMove;
    private MyMenuItem menuItemCurve;

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
                menuBarGraph.add(getSepV1());
                menuBarGraph.add(getMenuItemAutoScale());
                menuBarGraph.add(getMenuItemMove());
                menuBarGraph.add(getMenuItemZoom());
                menuBarGraph.add(getSepV2());
                menuBarGraph.add(getMenuItemCurve());
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
            sepV1.setBounds(menuItemParam.getX()+menuItemParam.getWidth(), 0, 5, DataProcessToolPanel.MENU_BAR_HEIGHT);
        }
        return sepV1;
    }
    private MyMenuItem getMenuItemAutoScale(){
        if (menuItemAutoScale == null){
            menuItemAutoScale = new MyMenuItem(fitex.getBundleString("TOOLTIPTEXT_MENU_GRAPH_AUTOSCALE"),menuBarGraph.getBackground(),fitex.getCopexImage("Bouton-AdT-28_graph_autoscale.png"), fitex.getCopexImage("Bouton-AdT-28_graph_autoscale_sur.png"), fitex.getCopexImage("Bouton-AdT-28_graph_autoscale_cli.png"), fitex.getCopexImage("Bouton-AdT-28_graph_autoscale_gri.png"));
            menuItemAutoScale.setBounds(sepV1.getX()+sepV1.getWidth(), 0, menuItemAutoScale.getWidth(), menuItemAutoScale.getHeight());
            menuItemAutoScale.addActionMenu(this);
        }
        return menuItemAutoScale;
    }

    private MyMenuItem getMenuItemMove(){
        if (menuItemMove == null){
            menuItemMove = new MyMenuItem(fitex.getBundleString("TOOLTIPTEXT_MENU_GRAPH_MOVE"),menuBarGraph.getBackground(),fitex.getCopexImage("Bouton-AdT-28_graph_move.png"), fitex.getCopexImage("Bouton-AdT-28_graph_move_sur.png"), fitex.getCopexImage("Bouton-AdT-28_graph_move_cli.png"), fitex.getCopexImage("Bouton-AdT-28_graph_move_gri.png"));
            menuItemMove.setBounds(menuItemAutoScale.getX()+menuItemAutoScale.getWidth(), 0, menuItemMove.getWidth(), menuItemMove.getHeight());
            menuItemMove.addActionMenu(this);
        }
        return menuItemMove;
    }

    private MyMenuItem getMenuItemZoom(){
        if (menuItemZoom == null){
            menuItemZoom = new MyMenuItem(fitex.getBundleString("TOOLTIPTEXT_MENU_GRAPH_ZOOM"),menuBarGraph.getBackground(),fitex.getCopexImage("Bouton-AdT-28_graph_zoom.png"), fitex.getCopexImage("Bouton-AdT-28_graph_zoom_sur.png"), fitex.getCopexImage("Bouton-AdT-28_graph_zoom_cli.png"), fitex.getCopexImage("Bouton-AdT-28_graph_zoom_gri.png"));
            menuItemZoom.setBounds(menuItemMove.getX()+menuItemMove.getWidth(), 0, menuItemZoom.getWidth(), menuItemZoom.getHeight());
            menuItemZoom.addActionMenu(this);
        }
        return menuItemZoom;
    }
    private JSeparator getSepV2() {
        if(sepV2 == null){
            sepV2 = new JSeparator(JSeparator.VERTICAL);
            sepV2.setBounds(menuItemZoom.getX()+menuItemZoom.getWidth(), 0, 5, DataProcessToolPanel.MENU_BAR_HEIGHT);
        }
        return sepV2;
    }

    private MyMenuItem getMenuItemCurve(){
        if (menuItemCurve == null){
            menuItemCurve = new MyMenuItem(fitex.getBundleString("TOOLTIPTEXT_MENU_GRAPH_CURVE"),menuBarGraph.getBackground(),fitex.getCopexImage("Bouton-AdT-28_graph.png"), fitex.getCopexImage("Bouton-AdT-28_graph_sur.png"), fitex.getCopexImage("Bouton-AdT-28_graph_cli.png"), fitex.getCopexImage("Bouton-AdT-28_graph_gri.png"));
            menuItemCurve.setBounds(sepV2.getX()+sepV2.getWidth(), 0, menuItemCurve.getWidth(), menuItemCurve.getHeight());
            menuItemCurve.addActionMenu(this);
        }
        return menuItemCurve;
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
        }else if (type.getCode() == DataConstants.VIS_BAR){
            cGraph = constructBarGraph();
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
        DefaultPieDataset pieDataset = new DefaultPieDataset();
        for (int i=0; i<dataset.getNbRows(); i++){
            int no = i+1;
            if (dataset.getData(i, header.getNoCol()) != null && !dataset.getData(i, header.getNoCol()).isIgnoredData())
                pieDataset.setValue(""+no, dataset.getData(i, header.getNoCol()).getValue());
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
        DefaultCategoryDataset ds = new DefaultCategoryDataset();
        for (int i=0; i<dataset.getNbRows(); i++){
            int no = i+1;
            if (dataset.getData(i, header.getNoCol()) != null && !dataset.getData(i, header.getNoCol()).isIgnoredData())
                ds.addValue(dataset.getData(i, header.getNoCol()).getValue(), ""+no, ""+no);
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
        int nbRows = dataset.getNbRows();
        for (int d=0; d<nbPlots; d++){
            DataHeader header1 = dataset.getDataHeader(((Graph)visualization).getParamGraph().getPlots().get(d).getHeaderX().getNoCol());
            DataHeader header2 = dataset.getDataHeader(((Graph)visualization).getParamGraph().getPlots().get(d).getHeaderY().getNoCol());
            int id1 = header1.getNoCol() ;
            int id2 = header2.getNoCol();
            Object[][] datas = new Object[dataset.getNbRows()][3];
            for (int i=0; i<nbRows; i++){
                if (dataset.getData(i, id1) != null && dataset.getData(i, id2) !=null ){
                    datas[i][0] = dataset.getData(i, id1).getValue();
                    datas[i][1] = dataset.getData(i, id2).getValue();
                    datas[i][2] = dataset.getData(i, id1).isIgnoredData() || dataset.getData(i, id2).isIgnoredData() ;
                }
            }
            String[] columnNames = new String[3];
            datamodel[d] = new DefaultTableModel(datas, columnNames);
        }
        ArrayList<FunctionModel> listFunctionModel = ((Graph)visualization).getListFunctionModel() ;
        ParamGraph pg = ((Graph)visualization).getParamGraph() ;
        FitexPanel gPanel = new FitexPanel(fitex.getLocale(), datamodel, listFunctionModel,pg) ;
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
        }else if (item.equals(getMenuItemZoom())){
            if(cGraph instanceof FitexGraph){
                ((FitexGraph)cGraph).getFitexPanel().setGraphMode(DataConstants.MODE_ZOOM);
            }
            fitex.setGraphMode(visualization, DataConstants.MODE_ZOOM);
            updateMenuGraph();
            return;
        }else if (item.equals(getMenuItemMove())){
            if(cGraph instanceof FitexGraph){
                ((FitexGraph)cGraph).getFitexPanel().setGraphMode(DataConstants.MODE_MOVE);
            }
            fitex.setGraphMode(visualization, DataConstants.MODE_MOVE);
            updateMenuGraph();
            return;
        }else if (item.equals(getMenuItemAutoScale())){
            if(cGraph instanceof FitexGraph){
                ((FitexGraph)cGraph).getFitexPanel().setGraphMode(DataConstants.MODE_AUTOSCALE);
            }
            fitex.setGraphMode(visualization, DataConstants.MODE_AUTOSCALE);
            if (visualization instanceof Graph){
                fitex.setAutoScale(this.dataset.getDbKey(),visualization.getDbKey() , true);
            }
            updateMenuGraph();
            return;
        }else if (item.equals(getMenuItemCurve())){
            displayFunctionModel();
            updateMenuGraph();
            return;
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

    


    /* mise a jour du menu */
    public void updateMenuGraph(){
        boolean enabled = visualization instanceof Graph;
        char graphMode = getGraphMode();
        //this.menuItemParam.setEnabled(enabled);
        this.menuItemAutoScale.setEnabled(enabled);
        this.menuItemMove.setEnabled(enabled );
        this.menuItemZoom.setEnabled(enabled );
        if(isDisplayFunctionModel()){
            this.menuItemCurve.setItemIcon(fitex.getCopexImage("Bouton-AdT-28_graph_push.png"));
            this.menuItemCurve.setItemRolloverIcon(fitex.getCopexImage("Bouton-AdT-28_graph_surcli.png"));
        }else{
            this.menuItemCurve.setItemIcon(fitex.getCopexImage("Bouton-AdT-28_graph.png"));
            this.menuItemCurve.setItemRolloverIcon(fitex.getCopexImage("Bouton-AdT-28_graph_sur.png"));
        }
        switch (graphMode){
            case DataConstants.MODE_AUTOSCALE :
                this.menuItemAutoScale.setItemIcon(fitex.getCopexImage("Bouton-AdT-28_graph_autoscale_push.png"));
                this.menuItemAutoScale.setItemRolloverIcon(fitex.getCopexImage("Bouton-AdT-28_graph_autoscale_surcli.png"));
                this.menuItemMove.setItemIcon(fitex.getCopexImage("Bouton-AdT-28_graph_move.png"));
                this.menuItemMove.setItemRolloverIcon(fitex.getCopexImage("Bouton-AdT-28_graph_move_sur.png"));
                this.menuItemZoom.setItemIcon(fitex.getCopexImage("Bouton-AdT-28_graph_zoom.png"));
                this.menuItemZoom.setItemRolloverIcon(fitex.getCopexImage("Bouton-AdT-28_graph_zoom_sur.png"));
                break;
            case DataConstants.MODE_MOVE :
                this.menuItemAutoScale.setItemIcon(fitex.getCopexImage("Bouton-AdT-28_graph_autoscale.png"));
                this.menuItemAutoScale.setItemRolloverIcon(fitex.getCopexImage("Bouton-AdT-28_graph_autoscale_sur.png"));
                this.menuItemMove.setItemIcon(fitex.getCopexImage("Bouton-AdT-28_graph_move_push.png"));
                this.menuItemMove.setItemRolloverIcon(fitex.getCopexImage("Bouton-AdT-28_graph_move_surcli.png"));
                this.menuItemZoom.setItemIcon(fitex.getCopexImage("Bouton-AdT-28_graph_zoom.png"));
                this.menuItemZoom.setItemRolloverIcon(fitex.getCopexImage("Bouton-AdT-28_graph_zoom_sur.png"));
                break;
            case DataConstants.MODE_ZOOM :
                this.menuItemAutoScale.setItemIcon(fitex.getCopexImage("Bouton-AdT-28_graph_autoscale.png"));
                this.menuItemAutoScale.setItemRolloverIcon(fitex.getCopexImage("Bouton-AdT-28_graph_autoscale_sur.png"));
                this.menuItemMove.setItemIcon(fitex.getCopexImage("Bouton-AdT-28_graph_move.png"));
                this.menuItemMove.setItemRolloverIcon(fitex.getCopexImage("Bouton-AdT-28_graph_move_sur.png"));
                this.menuItemZoom.setItemIcon(fitex.getCopexImage("Bouton-AdT-28_graph_zoom_push.png"));
                this.menuItemZoom.setItemRolloverIcon(fitex.getCopexImage("Bouton-AdT-28_graph_zoom_surcli.png"));
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
}
