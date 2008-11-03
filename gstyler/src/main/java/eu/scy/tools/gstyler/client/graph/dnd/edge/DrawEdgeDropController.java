package eu.scy.tools.gstyler.client.graph.dnd.edge;


import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.AbstractDropController;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import eu.scy.tools.gstyler.client.graph.GWTGraphCSSConstants;
import eu.scy.tools.gstyler.client.graph.GWTGraph;
import eu.scy.tools.gstyler.client.graph.edge.Edge;
import eu.scy.tools.gstyler.client.graph.node.Node;
import eu.scy.tools.gstyler.client.graph.node.NodeView;

/**
 * When drawing an edge and successfully completing the drop on a Node, subclasses of this controller
 * handle the drop to create or remove an edge. 
 */
public class DrawEdgeDropController extends AbstractDropController {

    protected GWTGraph graph;
    protected Edge edge;
    

    /**
     * @param nodeView Has to be a NodeView!!
     * @param graph Parent graph of given NodeView
     */
    public DrawEdgeDropController(Widget nodeView, GWTGraph graph, Edge edge) {
        super(nodeView);
        this.graph = graph;
        this.edge = edge;
    }

    public void onPreviewDrop(DragContext context) throws VetoDragException {
        if (getDropTarget() == ((DrawEdgeDragController) context.dragController).getSourceNode().getNodeView()) {
            // No drop on self allowed
            throw new VetoDragException();
        }
    }
    
    public void onDrop(DragContext context) {
        RootPanel.get().removeStyleDependentName(GWTGraphCSSConstants.CSS_CLICKABLE_WIDGET);
        if ( !(getDropTarget() instanceof NodeView)) {
            // Only drops on NodeViews allowed
            return;
        }
        // drop sucessfull, get the nodes
        DrawEdgeDragController dc = (DrawEdgeDragController) context.dragController;
        Node<?, ?> n1 = dc.getSourceNode().getNodeView().getNode();
        Node<?, ?> n2 = ((NodeView<?>) getDropTarget()).getNode();
        
        // Create new edge or remove existing one of there is already an edge
        Edge existingEdge = graph.getEdge(n1, n2);
        if (existingEdge != null) {
            graph.removeEdge(existingEdge);
        } else if (edge != null){
            edge.init(n1, n2);
            graph.addEdge(edge);
        }
    }

    /**
     * Highlights possible nodes to drop on (!= sourceNode)
     */
    public void onEnter(DragContext context) {
        super.onEnter(context);
        if (getDropTarget() != ((DrawEdgeDragController) context.dragController).getSourceNode().getNodeView()) {
            getDropTarget().addStyleName(GWTGraphCSSConstants.CSS_NODE_DROPTARGET_ENGAGE);
        }
    }
    
    /**
     * Removes highlighting applied in onEnter()
     */
    public void onLeave(DragContext context) {
       super.onLeave(context);
       getDropTarget().removeStyleName(GWTGraphCSSConstants.CSS_NODE_DROPTARGET_ENGAGE);
    }
}
