package eu.scy.tools.gstyler.client.graph.dnd;


import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.AbstractDropController;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import eu.scy.tools.gstyler.client.graph.CSSConstants;
import eu.scy.tools.gstyler.client.graph.GWTGraph;
import eu.scy.tools.gstyler.client.graph.edge.Edge;
import eu.scy.tools.gstyler.client.graph.node.Node;
import eu.scy.tools.gstyler.client.graph.node.NodeView;

/**
 * When drawing an edge and successfully completing the drop on a Node, subclasses of this controller
 * handle the drop to create or remove an edge. 
 */
public abstract class DrawEdgeDropController extends AbstractDropController {

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
            System.out.println("No drop on self allowed!");
            throw new VetoDragException();
        }
    }
    
    public void onDrop(DragContext context) {
        RootPanel.get().removeStyleDependentName(CSSConstants.CSS_DRAW_EDGES);
        if ( !(getDropTarget() instanceof NodeView)) {
            System.out.println("drop target not a nodeView: " + getDropTarget() );
            return;
        }
        System.out.println("drop sucessfull.");
        DrawEdgeDragController dc = (DrawEdgeDragController) context.dragController;
        Node<?, ?> n1 = dc.getSourceNode().getNodeView().getNode();
        Node<?, ?> n2 = ((NodeView<?>) getDropTarget()).getNode();
        
        handleDrop(context, n1, n2);
    }

    /**
     * Highlights possible nodes to drop on (!= sourceNode)
     */
    public void onEnter(DragContext context) {
        super.onEnter(context);
        if (getDropTarget() != ((DrawEdgeDragController) context.dragController).getSourceNode().getNodeView()) {
            getDropTarget().setStyleName(CSSConstants.CSS_DROPTARGET_NODE_ENGAGE);
        }
    }
    
    /**
     * Removes highlighting applied in onEnter()
     */
    public void onLeave(DragContext context) {
       super.onLeave(context);
       getDropTarget().removeStyleName(CSSConstants.CSS_DROPTARGET_NODE_ENGAGE);
    }
    
    /**
     * @param context DragContext of the completed DragOperation
     * @param n1 The node the drag started
     * @param n2 The node the drop was completed on
     */
    public abstract void handleDrop(DragContext context, Node<?, ?> n1, Node<?, ?> n2);
}
