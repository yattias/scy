/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.scy.scyDataTool.dataTool;

import com.scy.scyDataTool.common.Visualization;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JPanel;

/**
 * graphe => JPanel
 * @author Marjolaine
 */
public class CopexGraph extends JPanel {
    // PROPERTY
    /* visualisation */
    private Visualization vis ;
    /* composant graphique*/
    private  Component graphComponent ;

    // CONSTRUCTOR
    public CopexGraph(Visualization vis, Component graphComponent) {
        super();
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
