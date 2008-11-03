package eu.scy.tools.gstyler.client;


import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import eu.scy.tools.gstyler.client.graph.GWTGraph;
import eu.scy.tools.gstyler.client.graph.application.GraphApplication;
import eu.scy.tools.gstyler.client.menu.GStylerMenuBar;

/**
 * Main class construction the GStyler Application.
 * Uses the GWTGraph widget in the main area and a PaletteContainer on the right side to add new nodes 
 */
public class GStyler extends SimplePanel implements GraphApplication {

    public static String VERSION = "GStyler 0.2-SNAPSHOT";

    private GWTGraph graph;
    private PluginManager paletteContainer;
    private MenuBar menuBar;
    

    public GStyler() {
        VerticalPanel p = new VerticalPanel();
        p.setSize("100%", "100%");
        add(p);
        
        menuBar = new GStylerMenuBar(this);
        p.add(menuBar);
        
        HorizontalSplitPanel mainPanel = new HorizontalSplitPanel();
        mainPanel.setSplitPosition("75%");
        p.add(mainPanel);
        p.setCellHeight(mainPanel, "100%");
        
        graph = new GWTGraph();
        mainPanel.add(graph);

        paletteContainer = new PluginManager(this);
        mainPanel.add(paletteContainer);
    }

    /* (non-Javadoc)
     * @see eu.scy.tools.gstyler.client.GraphApplication#getGraph()
     */
    public GWTGraph getGraph() {
        return graph;
    }
}
