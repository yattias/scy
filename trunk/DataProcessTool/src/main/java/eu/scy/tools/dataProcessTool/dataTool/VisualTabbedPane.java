/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.dataTool;

import eu.scy.tools.dataProcessTool.common.Data;
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
import eu.scy.tools.dataProcessTool.utilities.ScyTabbedPane;
import java.awt.Component;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
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
public class VisualTabbedPane extends ScyTabbedPane{
    // PROPERTY
    /* liste des graphes */
    private ArrayList<CopexGraph> listGraph;
    /* liste des JScrollPane */
    private ArrayList<JScrollPane> listScrollPane;
    /* graphe actif */
    private CopexGraph activGraph ;
    
    
    
    // CONSTRUCTOR 
    public VisualTabbedPane(MainDataToolPanel a) {
        super();
        this.owner = a;
        listGraph = new ArrayList();
        listScrollPane = new ArrayList();
        init();
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void setSelected(CloseTab tab) {
        super.setSelected(tab);
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
        
        CloseTab closeTab = new CloseTab(this.owner, this, title, iconClose, iconRollOver, iconClic, iconClose);
        if (component instanceof CopexGraph){
            listGraph.add((CopexGraph)component);
            activGraph = (CopexGraph)component;
            listCloseTab.add(closeTab);
        }
        
        setTabComponentAt(index, closeTab);
        setSelectedIndex(index);
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
    }

    /* retourne vrai si le bouton est un bouton de fermeture et dans ce cas, 
     * met en v[0] le dataset à fermer
     */
    public boolean isButtonClose(CopexButtonPanel b, ArrayList v){
       for (int i=0;i<listCloseTab.size(); i++){
           CloseTab t = listCloseTab.get(i);
           if (b.equals(t.getButtonClose())){
               v.add(listGraph.get(i).getVisualization());
               return true;
           }
       }
       return false;
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
                if(this.listGraph.get(id).getGraphComponent() instanceof GraphPanel && vis instanceof Graph){
                    ParamGraph pg = ((Graph)vis).getParamGraph() ;
                    ((GraphPanel)(this.listGraph.get(id).getGraphComponent())).setParameters(pg.getX_min(), pg.getX_max(), pg.getDeltaX(), pg.getY_min(), pg.getY_max(), pg.getDeltaY());;
                }
            }
        }
    }

    /* affichage d'une visualisation */
    public void display(Dataset ds, Visualization vis, boolean create){
        TypeVisualization type = vis.getType() ;
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
            cGraph = new CopexGraph(vis, cPanel) ;
           
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
            cGraph = new CopexGraph(vis, cPanel) ;
        }else if (type.getCode() == DataConstants.VIS_GRAPH){
            int id1 = header1.getNoCol() ;
            int id2 = header2.getNoCol();
            Data[][] datas = new Data[ds.getNbRows()][2];
            for (int i=0; i<ds.getNbRows(); i++){
                datas[i][0] = ds.getData(i, id1);
                datas[i][1] = ds.getData(i, id2);
            }
            ArrayList<FunctionModel> listFunctionModel = ((Graph)vis).getListFunctionModel() ;
            ParamGraph pg = ((Graph)vis).getParamGraph() ;
            GraphPanel gPanel = new GraphPanel(owner, ds.getDbKey(), vis.getDbKey(), datas, listFunctionModel,
                    pg.getX_min(), pg.getX_max(), pg.getDeltaX(),
                    pg.getY_min(), pg.getY_max(), pg.getDeltaY(), pg.isAutoscale()) ;
            cGraph = new CopexGraph(vis, gPanel) ;
        }
          if (cGraph == null)
              return;
         if (create)
                addTab(vis.getName(), cGraph);
            else
                setCurrentTab(cGraph);
    }

    public boolean isAutoScale(){
        if(this.activGraph != null && activGraph.getGraphComponent() instanceof GraphPanel){
            return ((GraphPanel)activGraph.getGraphComponent()).isAutomaticScale() ;
        }
        return false;
    }

   
    
}
