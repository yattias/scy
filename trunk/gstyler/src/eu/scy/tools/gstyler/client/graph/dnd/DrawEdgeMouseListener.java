package eu.scy.tools.gstyler.client.graph.dnd;

import pl.balon.gwt.diagrams.client.connection.Connection;
import pl.balon.gwt.diagrams.client.connection.StraightTwoEndedConnection;
import pl.balon.gwt.diagrams.client.connector.Connector;
import pl.balon.gwt.diagrams.client.connector.UIObjectConnector;

import com.allen_sauer.gwt.dnd.client.DragController;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListenerAdapter;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import eu.scy.tools.gstyler.client.graph.CSSConstants;
import eu.scy.tools.gstyler.client.graph.GWTGraph;
import eu.scy.tools.gstyler.client.graph.NodeView;

/**
 * This MouseListener is used to capture MouseClicks on a widget of a NodeView
 * and then add a (temporarily) edge to be moved vie Drag&Drop.
 * 
 * Using the DrawEdgeDragController this edge is deleted afterwards, and the DrawEdgeDropController
 * adds a real edge to the graph if possible.
 */
public class DrawEdgeMouseListener extends MouseListenerAdapter {

    private NodeView<?> nodeView;
    private GWTGraph graph;
    private boolean modeless;

    /**
     * @param graph The graph edges will be drawn for
     * @param nodeView The Source nodeView which is the start for the new edge
     */
    public DrawEdgeMouseListener(GWTGraph graph, NodeView<?> nodeView, boolean modeless) {
        this.graph = graph;
        this.nodeView = nodeView;
        this.modeless = modeless;
    }

    public void onMouseDown(Widget sender, int x, int y) {
        
        // Any clicks will be ignored if not in edge mode
        if (modeless == false && graph.isEdgeMode() == false) {
            System.out.println("not in egde mode");
            return;
        }
        
        // Changes the mouse pointer to a hand
        RootPanel.get().setStylePrimaryName(CSSConstants.CSS_DRAW_EDGES);
        
        // Add an invisible Label as a dummy object to the graph...
        int left = nodeView.getAbsoluteLeft() + x;
        int top = nodeView.getAbsoluteTop() + y;
        Label l = new Label("");
        graph.add(l, left, top);

        // ... and connect it to the sourceNode, so that an edge will be drawn
        Connector c1 = UIObjectConnector.wrap(sender);
        Connector c2 = UIObjectConnector.wrap(l);
        Connection connection = new StraightTwoEndedConnection(c1, c2);
        connection.appendTo(graph);

        // Now construct the necessary DragController and directly start the DragOperation on the label
        DragController dragC = new DrawEdgeDragController(graph, nodeView.getNode(), connection, graph.getCreateEdgeDropControllers());
        dragC.makeDraggable(l);
        fireMouseDownEvent(l, 0, 0);
    }

    /**
     * Fires a MouseDown Event on the given Widget
     */
    public static native void fireMouseDownEvent(Widget sender, int x, int y) /*-{
        var cl = sender.@com.google.gwt.user.client.ui.Label::mouseListeners;
        cl.@com.google.gwt.user.client.ui.MouseListenerCollection::fireMouseDown(Lcom/google/gwt/user/client/ui/Widget;II)(sender, x, y);
    }-*/;
    
}
