/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.dataTool;

import eu.scy.client.tools.dataProcessTool.common.Visualization;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JPanel;

/**
 * graphe => JPanel
 * @author Marjolaine
 */
public class CopexGraph extends JPanel{
    /*data tool panel */
    protected FitexToolPanel owner;
    /* id dataset */
    protected long dbKeyDs;
    /* visualisation */
    protected Visualization vis ;
    /*  graph component*/
    protected  Component graphComponent ;

    public CopexGraph(FitexToolPanel owner, long dbKeyDs, Visualization vis, Component graphComponent) {
        super();
        this.owner = owner;
        this.dbKeyDs = dbKeyDs;
        this.vis = vis;
        this.graphComponent = graphComponent ;
        init();
    }

    public Visualization getVisualization() {
        return this.vis;
    }

    public void setGraph(Visualization vis) {
        this.vis = vis;
    }

    public Component getGraphComponent() {
        return graphComponent;
    }

    public void setGraphComponent(Component graphComponent) {
        this.graphComponent = graphComponent;
    }

    public long getDbKeyDs() {
        return dbKeyDs;
    }

    public void setDbKeyDs(long dbKeyDs) {
        this.dbKeyDs = dbKeyDs;
    }

    private void init(){
        setLayout(new BorderLayout());
        this.add(graphComponent, BorderLayout.CENTER);
    }

    /* gets the visualization name */
    public String getVisualizationName(){
        return this.vis == null?"":this.vis.getName();
    }

}
