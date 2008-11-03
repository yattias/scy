package eu.scy.tools.gstyler.client.graph.application;


import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import eu.scy.tools.gstyler.client.graph.GWTGraph;
import eu.scy.tools.gstyler.client.graph.dnd.node.CopyNodeDragController;

/**
 * Abstract base class for graph plugins.
 * Contains a Graph to add new nodes to the main Graph and
 * an actionsPanel which should be filled by subclasses. 
 */
public abstract class AbstractGraphPlugin extends VerticalPanel implements GraphPlugin {

    private GWTGraph graph;

    private HorizontalPanel actionsPanel;

    private GraphApplication graphApplication;
    
    public AbstractGraphPlugin(GraphApplication graphApplication) {
        this.graphApplication = graphApplication;
        setTitle(getName());

        graph = new GWTGraph(new CopyNodeDragController(graphApplication.getGraph()));
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

    public GraphApplication getGraphApplication() {
        return graphApplication;
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
