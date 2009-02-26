/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.elobrowser.tool.dataProcessTool;

import javax.swing.JPanel;
import eu.scy.tools.dataProcessTool.dataTool.*;

import java.awt.BorderLayout;
import org.jdom.Element;

/**
 * Entry point of the data process visualization tool,
 * interface between the tool and ToolBrokerAPI
 * @author Marjolaine
 */
public class DataToolPanel extends JPanel  {
    /* data process visualization tool */
    private MainDataToolPanel dataProcessVisualizationTool;
    

    /* Constructor data Tool panel - blank */
    public DataToolPanel() {
        super();
        this.setLayout(new BorderLayout());
        this.dataProcessVisualizationTool = new MainDataToolPanel();
        this.add(this.dataProcessVisualizationTool, BorderLayout.CENTER);
        setSize(MainDataToolPanel.PANEL_WIDTH, MainDataToolPanel.PANEL_HEIGHT);
        setPreferredSize(getSize());
    }

    /* resize */
    private void resizePanel(){
        System.out.println("resize data tool panel : "+this.getWidth()+", "+this.getHeight());
    }

    
    /* load ELO into data process tool */
    public void loadELO(String xmlContent){
        this.dataProcessVisualizationTool.loadELO(xmlContent);
    }

 

    public void newElo(){
        this.dataProcessVisualizationTool.createTable("NEW");
    }

    public Element getPDS(){
        return this.dataProcessVisualizationTool.getPDS();
    }

   
}
