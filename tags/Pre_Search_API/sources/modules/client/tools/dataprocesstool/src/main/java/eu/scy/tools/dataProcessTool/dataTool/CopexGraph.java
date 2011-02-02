/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.dataTool;

import eu.scy.tools.dataProcessTool.common.Visualization;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JPanel;

/**
 * graphe => JPanel
 * @author Marjolaine
 */
public class CopexGraph extends JPanel{
    // PROPERTY
    /*data tool panel */
    protected FitexToolPanel owner;
    /* id dataset */
    protected long dbKeyDs;
    /* visualisation */
    protected Visualization vis ;
    /* composant graphique*/
    protected  Component graphComponent ;

    // CONSTRUCTOR
    public CopexGraph(FitexToolPanel owner, long dbKeyDs, Visualization vis, Component graphComponent) {
        super();
        this.owner = owner;
        this.dbKeyDs = dbKeyDs;
        this.vis = vis;
        this.graphComponent = graphComponent ;
        init();
    }

    // GETTER AND SETTER
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

    // METHOD
    private void init(){
        setLayout(new BorderLayout());
        this.add(graphComponent, BorderLayout.CENTER);
    }

    /* nom de la visualisation */
    public String getVisualizationName(){
        return this.vis == null?"":this.vis.getName();
    }

}
