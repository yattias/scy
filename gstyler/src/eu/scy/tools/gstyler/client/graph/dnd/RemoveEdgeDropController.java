package eu.scy.tools.gstyler.client.graph.dnd;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.google.gwt.user.client.ui.Widget;

import eu.scy.tools.gstyler.client.graph.GWTGraph;
import eu.scy.tools.gstyler.client.graph.Node;


/**
 * This DropController removes an edge between two nodes
 */
public class RemoveEdgeDropController extends DrawEdgeDropController {

    public RemoveEdgeDropController(Widget nodeView, GWTGraph graph) {
        super(nodeView, graph);
    }

    @Override
    public void handleDrop(DragContext context, Node<?, ?> n1, Node<?, ?> n2) {
        System.out.println("Need to remove edge between " + n1 + " and " + n2);
        graph.removeEdge( graph.getEdge(n1, n2) );
    }

}
