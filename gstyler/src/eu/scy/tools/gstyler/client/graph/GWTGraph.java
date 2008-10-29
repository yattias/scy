package eu.scy.tools.gstyler.client.graph;


import java.util.ArrayList;
import java.util.Collection;

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
     * The graph can differentiate two interaction modes:
     * MOVE_NODES enables the user to move nodes and only draw/delete edges when clicking on the EdgeHandles
     * EDIT_EDGES locks all nodes in position and enables the user to draw and delte edges from any DragHandle of the node
     *
     */
    public enum InteractionMode {
        MOVE_NODES,
        EDIT_EDGES;
    }
    
    /**
     * DropController to be used to drop nodes from the pallete's graph onto the main graph
     */
    private DropController dropController;
    
    private ArrayList<Node<?, ?>> nodes = new ArrayList<Node<?, ?>>();
    
    private ArrayList<Edge> edges = new ArrayList<Edge>();

    public InteractionMode interactionMode;
    
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
        nodes.add(node);
    }
    
    private void initNodeView(NodeView<?> nodeView) {
        dragController.makeDraggable(nodeView);
        SourcesMouseEvents dragHandle = (SourcesMouseEvents) nodeView.getDragHandle();
        dragHandle.addMouseListener(new DrawEdgeMouseListener(this, nodeView, false));
        nodeView.getEdgeHandle().addMouseListener(new DrawEdgeMouseListener(this, nodeView, true));
    }

    /**
     * Adds an Edge to this graph
     */
    public void addEdge(Edge edge) {
      edge.getConnection().appendTo(this);
      edges.add(edge);
    }

    /**
     * Removes the given edge from this graph
     */
    public void removeEdge(Edge edge) {
        edges.remove(edge);
        edge.getConnection().remove();
    }
    
    /**
     * @return Edge from n1 to n2, if none found Edge from n2 to n2 or null
     */
    public Edge getEdge(Node<?, ?> n1, Node<?, ?> n2) {
        for (Edge e : edges) {
            if (e.getNode1() == n1) {
                if (e.getNode2() == n2) {
                    return e;
                }
            }
        }
        for (Edge e : edges) {
            if (e.getNode2() == n1) {
                if (e.getNode1() == n2) {
                    return e;
                }
            }
        }
        return null;
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
    // FIXME: either use or remove
    public Collection<DropController> getRemoveEdgeDropControllers() {
        ArrayList<DropController> list = new ArrayList<DropController> ();
        for (Widget w : getChildren()) {
            if (w instanceof NodeView) {
                list.add(new RemoveEdgeDropController(w, this));
            }
        }
        return list;
    }

    public InteractionMode getOmteractionMode() {
        return interactionMode;
    }

    public void setInteractionMode(InteractionMode interactionMode) {
        switch (interactionMode) {
            case MOVE_NODES:
                leaveEdgeMode();
                break;
                
            case EDIT_EDGES:
                enterEdgeMode();
                break;
        }
        this.interactionMode = interactionMode;
    }
    
    private void enterEdgeMode() {
        interactionMode = InteractionMode.EDIT_EDGES;
        for (Widget w: getChildren() ) {
            if (w instanceof NodeView) {
                dragController.makeNotDraggable(w);
            }
        }
    }

    private void leaveEdgeMode() {
        for (Widget w: getChildren() ) {
            if (w instanceof NodeView) {
                dragController.makeDraggable(w);
            }
        }
    }
}
