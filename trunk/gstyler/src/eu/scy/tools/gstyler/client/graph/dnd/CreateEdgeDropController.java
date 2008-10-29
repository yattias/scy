package eu.scy.tools.gstyler.client.graph.dnd;


import com.allen_sauer.gwt.dnd.client.DragContext;
import com.google.gwt.user.client.ui.Widget;

import eu.scy.tools.gstyler.client.graph.Edge;
import eu.scy.tools.gstyler.client.graph.GWTGraph;
import eu.scy.tools.gstyler.client.graph.Node;

/**
 * This DropController creates an edge between two nodes
 */
public class CreateEdgeDropController extends DrawEdgeDropController {

    public CreateEdgeDropController(Widget nodeView, GWTGraph graph) {
        super(nodeView, graph);
    }

    @Override
    public void handleDrop(DragContext context, Node<?, ?> n1, Node<?, ?> n2) {
        Edge e = new Edge(n1, n2);
        graph.addEdge(e);
    }

}
