package eu.scy.tools.gstyler.client.graph;


import java.util.ArrayList;
import java.util.Collection;

import pl.balon.gwt.diagrams.client.connection.Connection;
import pl.balon.gwt.diagrams.client.connection.StraightTwoEndedConnection;
import pl.balon.gwt.diagrams.client.connector.Connector;
import pl.balon.gwt.diagrams.client.connector.UIObjectConnector;

import com.allen_sauer.gwt.dnd.client.DragController;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.Widget;

import eu.scy.tools.gstyler.client.graph.dnd.CreateEdgeDropController;
import eu.scy.tools.gstyler.client.graph.dnd.DrawEdgeMouseListener;
import eu.scy.tools.gstyler.client.graph.dnd.MoveNodeDragController;
import eu.scy.tools.gstyler.client.graph.dnd.RemoveEdgeDropController;

public class GWTGraph extends AbsolutePanel {

    /**
     * DragController to be used to drag nodes around or onto other graphs
     */
    public DragController dragController;

    /**
     * DropController to be used to drop nodes from the pallete's graph onto the main graph
     */
    private DropController dropController;

    private boolean edgeMode = false;
    
    /**
     * Creates a new GWTGraph with a normal MoveNodeDragController which allows for nodes
     * to be moved around freely on the graphs area
     */
    public GWTGraph() {
        setSize("100%", "100%");
        dragController = new MoveNodeDragController(this, true);
        dropController = new AbsolutePositionDropController(this);
    }
    
    /**
     * Creates a new GWTGraph with the given DragController.
     * This can be used to eg. specify a CopyNodeDragController to copy nodes to other Graphs
     */
    public GWTGraph(DragController dragController) {
        setSize("100%", "100%");
        this.dragController = dragController;
        dropController = new AbsolutePositionDropController(this);
    }
    
    /**
     * Returns this Graphs DropController.
     * This is useful for creating DragControllers like the CopyNodeDragController used in Palettes which should allow drops on this graph
     */
    public DropController getDropController() {
        return dropController;
    }
    
    /**
     * Adds the given node to this graph at the specified position
     *
     * @param node the node to be added
     * @param left the nodes's left position
     * @param top the nodes's top position
     */
    public void addNode(Node<?, ?> node, int left, int top) {
        super.add(node.getNodeView(), left, top);
        initNode(node);
    }
    
    private void initNode(Node<?, ?> node) {
        initNodeView(node.getNodeView());
        // FIXME: add to internal array?
    }
    
    private void initNodeView(NodeView<?> nodeView) {
        dragController.makeDraggable(nodeView);
        SourcesMouseEvents dragHandle = (SourcesMouseEvents) nodeView.getDragHandle();
        dragHandle.addMouseListener(new DrawEdgeMouseListener(this, nodeView, false));
        nodeView.getEdgeHandle().addMouseListener(new DrawEdgeMouseListener(this, nodeView, true));
    }

    public void addEdge(Edge edge) {
      // FIXME: add to internal array?
      Connector c1 = UIObjectConnector.wrap(edge.getNode1().getNodeView());
      Connector c2 = UIObjectConnector.wrap(edge.getNode2().getNodeView());
      Connection connection = new StraightTwoEndedConnection(c1, c2);
      connection.appendTo(this);
    }

    /**
     * Returns a Collection of DropControllers, eg. to be used as targets when drawing new edges
     */
    public Collection<DropController> getCreateEdgeDropControllers() {
        ArrayList<DropController> list = new ArrayList<DropController> ();
        for (Widget w : getChildren()) {
            if (w instanceof NodeView) {
                list.add(new CreateEdgeDropController(w, this));
            }
        }
        return list;
    }
    
    /**
     * Returns a Collection of DropControllers, eg. to be used as targets when removing edges
     */
    public Collection<DropController> getRemoveEdgeDropControllers() {
        ArrayList<DropController> list = new ArrayList<DropController> ();
        for (Widget w : getChildren()) {
            if (w instanceof NodeView) {
                list.add(new RemoveEdgeDropController(w, this));
            }
        }
        return list;
    }

    public boolean isEdgeMode() {
        return edgeMode;
    }

    public void enterEdgeMode() {
        edgeMode = true;
        for (Widget w: getChildren() ) {
            if (w instanceof NodeView) {
                dragController.makeNotDraggable(w);
            }
        }
    }

    public void leaveEdgeMode() {
        edgeMode = false;
        for (Widget w: getChildren() ) {
            if (w instanceof NodeView) {
                dragController.makeDraggable(w);
            }
        }
    }
}
