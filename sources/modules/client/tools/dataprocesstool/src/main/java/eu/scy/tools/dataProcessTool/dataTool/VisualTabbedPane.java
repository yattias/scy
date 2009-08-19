/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.dataTool;

import eu.scy.tools.dataProcessTool.common.DataHeader;
import eu.scy.tools.dataProcessTool.common.Dataset;
import eu.scy.tools.dataProcessTool.common.FunctionModel;
import eu.scy.tools.dataProcessTool.common.TypeVisualization;
import eu.scy.tools.dataProcessTool.common.Visualization;
import eu.scy.tools.dataProcessTool.common.Graph ;
import eu.scy.tools.dataProcessTool.common.ParamGraph;
import eu.scy.tools.dataProcessTool.utilities.CloseTab;
import eu.scy.tools.dataProcessTool.utilities.CopexButtonPanel;
import eu.scy.tools.dataProcessTool.utilities.CopexReturn;
import eu.scy.tools.dataProcessTool.utilities.DataConstants;
import eu.scy.tools.fitex.GUI.FitexPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

/**
 * onglets visualisation des graphes d'un dataset
 * @author Marjolaine Bodin
 */
public class VisualTabbedPane extends JTabbedPane{
    // PROPERTY
    /* owner */
    private DataProcessToolPanel owner;
    /* liste des CloseTAb */
    private ArrayList<CloseTab> listCloseTab;
    /* close TAb du +*/
    private CloseTab closeTabAdd;
    /* liste des graphes */
    private ArrayList<CopexGraph> listGraph;
    /* liste des JScrollPane */
    private ArrayList<JScrollPane> listScrollPane;
    /* graphe actif */
    private CopexGraph activGraph ;
    
    
    
    // CONSTRUCTOR 
    public VisualTabbedPane(DataProcessToolPanel a) {
        super();
        this.owner = a;
        listGraph = new ArrayList();
        listScrollPane = new ArrayList();
        init();
    }

    protected void init(){
       UIManager.put("TabbedPane.contentAreaColor",Color.WHITE);
       UIManager.put("TabbedPane.selectedColor",Color.WHITE);
       UIManager.put("TabbedPane.selected",Color.WHITE);
       UIManager.put("TabbedPane.focus",Color.WHITE);
       UIManager.put("TabbedPane.borderHightlightColor",DataProcessToolPanel.backgroundColor);
       UIManager.put("TabbedPane.tabAreaBackground",DataProcessToolPanel.backgroundColor);
       UIManager.put("TabbedPane.light",DataProcessToolPanel.backgroundColor);
       UIManager.put("TabbedPane.unselectedTabBackground",DataProcessToolPanel.backgroundColor);
       UIManager.put("TabbedPane.unselectedTabHighlight",DataProcessToolPanel.backgroundColor);
       updateUI();
       this.listCloseTab = new ArrayList();
       // initialisation du tabbedPane : onglet vierge afin d'ajouter un proc
       addTab(null, new JLabel(""));
       ImageIcon  iconClose = owner.getCopexImage("Bouton-onglet_ouverture.png");
       ImageIcon  iconRollOver = owner.getCopexImage("Bouton-onglet_ouverture_sur.png");
       ImageIcon  iconClic = owner.getCopexImage("Bouton-onglet_ouverture_cli.png");
       ImageIcon  iconDisabled = owner.getCopexImage("Bouton-onglet_ouverture_grise.png");
        closeTabAdd = new CloseTab(owner, this, null,"", iconClose, iconRollOver, iconClic, iconDisabled);
        setTabComponentAt(0, closeTabAdd);

   }
     /* selectionne l'onglet */
    public void setSelected(CloseTab tab){
        int index = -1;
         for (int i=0;i<listCloseTab.size(); i++){
           CloseTab t = listCloseTab.get(i);
           if (tab.equals(t)){
               index = i;
               break;
           }
       }
        if (index != -1){
            setSelectedIndex( index);
            listCloseTab.get(index).setSelected(true);
        }
    }
    
    @Override
    public void addTab(String title, Component component) {
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setSize(this.getWidth() -10, this.getHeight() -10);
        listScrollPane.add(scrollPane);
        int index = 0;
        if (getTabCount() > 1)
            index = getTabCount()-1 ;
        super.insertTab("", null, scrollPane, "",  index);
        ImageIcon  iconClose = this.owner.getCopexImage("Bouton-onglet_fermeture.png");
        ImageIcon  iconRollOver = this.owner.getCopexImage("Bouton-onglet_fermeture_sur.png");
        ImageIcon  iconClic = this.owner.getCopexImage("Bouton-onglet_fermeture_cli.png");
        
        CopexGraph g = null;
        if (component instanceof CopexGraph)
            g = (CopexGraph)component ;
        CloseTab closeTab = new CloseTab(this.owner, this,g, title, iconClose, iconRollOver, iconClic, iconClose);
        if (component instanceof CopexGraph){
            listGraph.add((CopexGraph)component);
            activGraph = (CopexGraph)component;
            listCloseTab.add(closeTab);
        }
        
        setTabComponentAt(index, closeTab);
        setSelectedIndex(index);
        System.out.println("add tab : "+getTabComponentAt(index).getWidth()+", "+getTabComponentAt(index).getHeight());
    }

    @Override
    public void setSelectedIndex(int index) {
        if (this.listGraph.size() > 0 && index == this.listGraph.size()){
            return;
        }
        if (listGraph.size() == 0){
            if (this.closeTabAdd != null)
                this.closeTabAdd.setSelected(true);
            return;
        }

        super.setSelectedIndex(index);
        this.closeTabAdd.setSelected(false);
        if (index == this.listGraph.size())
            this.activGraph = null;
        else
            this.activGraph = this.listGraph.get(index);
        for (int k=0; k<listCloseTab.size(); k++){
            if (k == index){
                this.listCloseTab.get(k).setSelected(true);
            }else
                this.listCloseTab.get(k).setSelected(false);
        }
       
        this.owner.updateMenuGraph();
    }

    
    


    /* fermeture d'un onglet */
    public void closeTab(int id){
        remove(id);
        listGraph.remove(id);
        listCloseTab.remove(id);
        setSelectedIndex(0);
        revalidate();
        repaint();
    }

    /* retourne le graphe correspondant à un closeTab*/
    public Visualization getVisualization(CloseTab closeTab){
        int nb = listCloseTab.size();
        for (int i=0; i<nb; i++){
            if (listCloseTab.get(i).equals(closeTab)){
                return listGraph.get(i).getVisualization() ;
            }
        }
        return null;
    }

   /* recherche indice table correspondant au vis  // - 1 sinon */
    private int getIdVisualizationTab(Visualization vis){
        int nb =listGraph.size();
        for (int i=0; i<nb; i++){
            if (listGraph.get(i).getVisualization().getDbKey() == vis.getDbKey()){
                return i;
            }
        }
        return -1;
    }

    /* retourne le visualisation selectionne - null sinon */
    public Visualization getSelectedVisualization(){
        Visualization vis = null;
        if (activGraph != null)
            vis = activGraph.getVisualization();
        return vis;
    }

    /* fermeture d'un onglet */
    public void closeTab(Visualization vis){
        int id = getIdVisualizationTab(vis);
        if (id == -1){
            owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_CLOSE_GRAPH"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
            return ;
        }
        closeTab(id);
    }

    /* mise à jour du graph sur l'onglet courant */
    public void setCurrentTab(Component c){
        setComponentAt(getSelectedIndex(), c);
        if (c instanceof CopexGraph){
            listGraph.remove(listGraph.size()-1);
            listGraph.add((CopexGraph)c);
            activGraph = (CopexGraph)c;
        }
        revalidate();
        repaint();
    }

    /* mise à jour du nom du graphe */
    public void updateVisualizationName(Visualization vis, String newName){
        int id = getIdVisualizationTab(vis);
        if (id == -1){
            owner.displayError(new CopexReturn(owner.getBundleString("MSG_ERROR_UPDATE_VISUALIZATION_NAME"), false), owner.getBundleString("TITLE_DIALOG_ERROR"));
            return ;
        }
        listGraph.get(id).getVisualization().setName(newName);
        listCloseTab.get(id).updateTitle(newName);
        revalidate();
        repaint();
    }

    /* mise à jour du dataset => on enleve tout et on affiche les graphes du nouveau dataset*/
    public void updateDataset(Dataset ds, boolean sameDs){
        int selIndex = getSelectedIndex() ;
       int nb = listGraph.size();
        for (int i=nb-1; i>-1; i--){
            remove(i);
        }
        listGraph = new ArrayList();
        listCloseTab = new ArrayList();
        setSelectedIndex(0);
        revalidate();
        repaint();
       ArrayList<Visualization> listVis = ds.getListVisualization() ;
       nb = listVis.size();
       for (int i=0; i<nb; i++){
           display(ds, listVis.get(i), true);
       }
       if (sameDs && selIndex >=0 && selIndex < listVis.size())
           setSelectedIndex(selIndex);
    }

    

    /* mise à jour du dataset */
    public void updateVisualization(Visualization v){
        Visualization vis = getSelectedVisualization();
        if (vis != null){
            int id = getIdVisualizationTab(vis);
            vis = v ;
            if (id != -1){
                this.listGraph.get(id).setGraph(v);
                if(this.listGraph.get(id).getGraphComponent() instanceof FitexPanel && vis instanceof Graph){
                    ParamGraph pg = ((Graph)vis).getParamGraph() ;
                    ((FitexPanel)(this.listGraph.get(id).getGraphComponent())).setParameters(pg);
                }
            }
        }
    }

    /* affichage d'une visualisation */
    public void display(Dataset ds, Visualization vis, boolean create){
        TypeVisualization type = vis.getType() ;
        long dbKeyDs = ds.getDbKey() ;
        DataHeader header1 = ds.getDataHeader(vis.getTabNo()[0]);
        DataHeader header2 = null;
        CopexGraph cGraph = null;
        if (type.getNbColParam() ==2){
            header2 = ds.getDataHeader(vis.getTabNo()[1]);
        }
        if (type.getCode() == DataConstants.VIS_PIE){
            DefaultPieDataset pieDataset = new DefaultPieDataset();
            for (int i=0; i<ds.getNbRows(); i++){
                int no = i+1;
                if (ds.getData(i, header1.getNoCol()) != null && !ds.getData(i, header1.getNoCol()).isIgnoredData())
                    pieDataset.setValue(""+no, ds.getData(i, header1.getNoCol()).getValue());
            }

            JFreeChart pieChart = ChartFactory.createPieChart(header1.getValue(),
            pieDataset, true, true, true);
            ChartPanel cPanel = new ChartPanel(pieChart, false);
            // in a panel for the resize
            JPanel p = new JPanel();
            p.setLayout(new BorderLayout());
            p.setPreferredSize(getSize());
            p.add(cPanel, BorderLayout.CENTER);
            cGraph = new CopexGraph(owner, dbKeyDs, vis, p) ;
           
        }else if (type.getCode() == DataConstants.VIS_BAR){
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (int i=0; i<ds.getNbRows(); i++){
                int no = i+1;
                if (ds.getData(i, header1.getNoCol()) != null && !ds.getData(i, header1.getNoCol()).isIgnoredData())
                    dataset.addValue(ds.getData(i, header1.getNoCol()).getValue(), ""+no, ""+no);
            }

            JFreeChart barChart = ChartFactory.createBarChart(header1.getValue(), "",
                "", dataset, PlotOrientation.VERTICAL, true, true, false);
            ChartPanel cPanel = new ChartPanel(barChart);
            JPanel p = new JPanel();
            p.setLayout(new BorderLayout());
            p.setPreferredSize(getSize());
            p.add(cPanel, BorderLayout.CENTER);
            cGraph = new CopexGraph(owner,dbKeyDs, vis, p) ;
        }else if (type.getCode() == DataConstants.VIS_GRAPH){
            int id1 = header1.getNoCol() ;
            int id2 = header2.getNoCol();
            Object[][] datas = new Object[ds.getNbRows()][3];
            for (int i=0; i<ds.getNbRows(); i++){
                if (ds.getData(i, id1) != null && ds.getData(i, id2) !=null ){
                    datas[i][0] = ds.getData(i, id1).getValue();
                    datas[i][1] = ds.getData(i, id2).getValue();
                    datas[i][2] = ds.getData(i, id1).isIgnoredData() && ds.getData(i, id2).isIgnoredData() ;
                }
            }
            String[] columnNames = new String[3];
            DefaultTableModel datamodel = new DefaultTableModel(datas, columnNames);
            ArrayList<FunctionModel> listFunctionModel = ((Graph)vis).getListFunctionModel() ;
            ParamGraph pg = ((Graph)vis).getParamGraph() ;
            FitexPanel gPanel = new FitexPanel(owner.getLocale(), datamodel, listFunctionModel,pg) ;
            cGraph = new FitexGraph(owner, dbKeyDs, vis, gPanel) ;
        }
          if (cGraph == null)
              return;
         if (create)
                addTab(vis.getName(), cGraph);
            else
                setCurrentTab(cGraph);
    }

    public boolean isAutoScale(){
//        if(this.activGraph != null && activGraph.getGraphComponent() instanceof FitexPanel){
//            return ((FitexPanel)activGraph.getGraphComponent()).isAutomaticScale() ;
//        }
        return false;
    }

    /* retourne vrai si le bouton + est clique */
    public boolean isButtonAdd(CopexButtonPanel b){
        return b == closeTabAdd.getButtonClose();
    }

    public void resizePanel(int width, int height){
        setSize(width, height);
    }
   
    public boolean canMenu2DPlot(){
        Visualization vis = getSelectedVisualization() ;
        return vis != null && vis instanceof Graph;
    }


    public char getGraphMode(){
        if(this.activGraph != null && activGraph instanceof FitexGraph){
            return ((FitexGraph)activGraph).getFitexPanel().getGraphMode();
        }
        return DataConstants.MODE_DEFAULT ;
    }

    public void setGraphMode(char graphMode){
        if(this.activGraph != null && activGraph instanceof FitexGraph){
            ((FitexGraph)activGraph).getFitexPanel().setGraphMode(graphMode);
        }
    }

    public ArrayList<Object> getListGraphPDF(){
        ArrayList<Object> list = new ArrayList();
        int nb = this.listGraph.size();
        for (int i=0; i<nb; i++){
            if(listGraph.get(i) instanceof FitexGraph){
                list.add(((FitexGraph)listGraph.get(i)).getFitexPanel().getDrawPanel());
            }else if (listGraph.get(i) instanceof JPanel){
                JPanel p = ((JPanel)listGraph.get(i));
                if (p.getComponentCount()>0 && p.getComponent(0) instanceof JPanel){
                    JPanel p2 =(JPanel)p.getComponent(0);
                    if (p2.getComponentCount()>0 && p2.getComponent(0) instanceof ChartPanel){
                        ChartPanel cPanel = (ChartPanel)p2.getComponent(0);
                        list.add(cPanel.getChart());
                    }
                }
            }
        }
        return list;
    }

    public void displayFunctionModel(){
        if(this.activGraph != null && activGraph instanceof FitexGraph){
            ((FitexGraph)activGraph).getFitexPanel().displayFunctionModel();
        }
    }
}
