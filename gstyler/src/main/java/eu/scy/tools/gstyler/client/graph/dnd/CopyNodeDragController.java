package eu.scy.tools.gstyler.client.graph.dnd;



import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandlerAdapter;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.google.gwt.user.client.ui.RootPanel;

import eu.scy.tools.gstyler.client.graph.GWTGraph;
import eu.scy.tools.gstyler.client.graph.node.Node;
import eu.scy.tools.gstyler.client.graph.node.NodeView;

/**
 * This DragController is responsible for copying the nodes from the palette to the main graph
 */
public class CopyNodeDragController extends PickupDragController {

    public CopyNodeDragController(final GWTGraph targetGraph) {
        super(RootPanel.get(), true);
        // Create a proxy (== copy of the dragSource) as we do not want to move the source node but copy it directly
        setBehaviorDragProxy(true);
        registerDropController(targetGraph.getDropController());

        addDragHandler(new DragHandlerAdapter() {

            public void onPreviewDragEnd(DragEndEvent event) throws VetoDragException {
                
                // Get the dragged nodeView, clone the corresponding node and add it to the graph
                GWTGraph graph = targetGraph;
                NodeView<?> nodeView = (NodeView<?>) event.getContext().draggable;
                Node<?, ?> newNode = nodeView.getNode().createClone();
                graph.addNode(newNode, event.getContext().desiredDraggableX, event.getContext().desiredDraggableY);
                
                // TODO: something is wrong with the modefull stuff. Check it!
                //graph.leaveEdgeMode();
                
                // throw the exception, so that the original widget will not be moved
                throw new VetoDragException();
            }
        });
    }
}