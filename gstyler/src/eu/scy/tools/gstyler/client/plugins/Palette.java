package eu.scy.tools.gstyler.client.plugins;


import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import eu.scy.tools.gstyler.client.GStyler;
import eu.scy.tools.gstyler.client.graph.GWTGraph;
import eu.scy.tools.gstyler.client.graph.dnd.CopyNodeDragController;

/**
 * Abstract base class for the Visible part of plugins.
 * Contains a Graph to add new nodes to the main Graph and
 * an actionsPanel which should be filled by subclasses. 
 */
public abstract class Palette extends VerticalPanel {

    private GWTGraph graph;

    private HorizontalPanel actionsPanel;

    public Palette(GStyler gstyler) {
        setTitle(getName());

        graph = new GWTGraph(new CopyNodeDragController(gstyler.getGraph()));
        graph.setHeight("400px");
        add(graph);

        actionsPanel = new HorizontalPanel();
        add(actionsPanel);
    }

    protected GWTGraph getGraph() {
        return graph;
    }

    protected HorizontalPanel getActionsPanel() {
        return actionsPanel;
    }

    public abstract String getName();
}
