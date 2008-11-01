package eu.scy.tools.gstyler.client.common;


import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import eu.scy.tools.gstyler.client.graph.GWTGraph;
import eu.scy.tools.gstyler.client.graph.dnd.CopyNodeDragController;

/**
 * Abstract base class for the visible part of plugins.
 * Contains a Graph to add new nodes to the main Graph and
 * an actionsPanel which should be filled by subclasses. 
 */
public abstract class AbstractPalette extends VerticalPanel implements Palette {

    private GWTGraph graph;

    private HorizontalPanel actionsPanel;

    public AbstractPalette(GraphApplication gstyler) {
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

    /* (non-Javadoc)
     * @see eu.scy.tools.gstyler.client.plugins.Palette#getName()
     */
    public abstract String getName();

    /* (non-Javadoc)
     * @see eu.scy.tools.gstyler.client.plugins.Palette#getUI()
     */
    public Widget getUI() {
        return this;
    }
}
