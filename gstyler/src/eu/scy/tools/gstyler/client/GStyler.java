package eu.scy.tools.gstyler.client;


import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import eu.scy.tools.gstyler.client.graph.Edge;
import eu.scy.tools.gstyler.client.graph.GWTGraph;
import eu.scy.tools.gstyler.client.graph.Node;
import eu.scy.tools.gstyler.client.plugins.mindmap.MindmapNode;

/**
 * Main class construction the GStyler Application.
 * Uses the GWTGraph widget in the main area and a PaletteContainer on the right side to add new nodes 
 */
public class GStyler extends SimplePanel {

    public static String VERSION = "GStyler 0.0.6";

    private GWTGraph graph;
    private PaletteContainer paletteContainer;
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

        paletteContainer = new PaletteContainer(this);
        mainPanel.add(paletteContainer);
        
        addTestNodes();
    }

    private void addTestNodes() {
        Node<?, ?> n1 = new MindmapNode();
        Node<?, ?> n2 = new MindmapNode();
        graph.addNode(n1, 100, 100);
        graph.addNode(n2, 200, 300);
        
        graph.addNode(new MindmapNode(), 400, 100);
        
        Edge e = new Edge(n1, n2);
        graph.addEdge(e);
    }

    public GWTGraph getGraph() {
        return graph;
    }
}
