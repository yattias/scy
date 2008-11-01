package eu.scy.tools.gstyler.client.graph.dnd;

import java.util.Collection;

import pl.balon.gwt.diagrams.client.connection.Connection;
import pl.balon.gwt.diagrams.client.connector.UIObjectConnector;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandlerAdapter;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.google.gwt.user.client.ui.RootPanel;

import eu.scy.tools.gstyler.client.graph.GWTGraph;
import eu.scy.tools.gstyler.client.graph.node.Node;

/**
 * When drawing an edge this DragController sets up all allowed drag targets (i.e. Nodes) 
 * When the drag operation is finished the temporarily added edge is deleted. 
 */
public class DrawEdgeDragController extends MoveNodeDragController {

    private Node<?, ?> sourceNode;

    public DrawEdgeDragController(final GWTGraph graph, Node<?, ?> sourceNode, final Connection tmpConnection, Collection<DropController> dropControllers) {
        super(RootPanel.get(), false);
        this.sourceNode = sourceNode;
        
        // Set up all allowed targets (i.e. NodeViews
        for (DropController c : dropControllers) {
            registerDropController(c);
        }
        
        addDragHandler(new DragHandlerAdapter() {

            public void onDragEnd(DragEndEvent event) {
                System.out.println("Drag ended. Removing temp connection.");
                UIObjectConnector.unwrap(event.getContext().draggable);
                graph.remove(event.getContext().draggable);
                tmpConnection.remove();
            }
        });
    }

    public Node<?, ?> getSourceNode() {
        return sourceNode;
    }
}
