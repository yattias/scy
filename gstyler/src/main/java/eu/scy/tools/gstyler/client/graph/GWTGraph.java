package eu.scy.tools.gstyler.client.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.allen_sauer.gwt.dnd.client.DragController;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.Widget;

import eu.scy.tools.gstyler.client.graph.dnd.CreateEdgeDropController;
import eu.scy.tools.gstyler.client.graph.dnd.DrawEdgeMouseListener;
import eu.scy.tools.gstyler.client.graph.dnd.MoveNodeDragController;
import eu.scy.tools.gstyler.client.graph.edge.Edge;
import eu.scy.tools.gstyler.client.graph.edge.EdgeCreationHandle;
import eu.scy.tools.gstyler.client.graph.node.Node;
import eu.scy.tools.gstyler.client.graph.node.NodeView;

public class GWTGraph extends AbsolutePanel {

    /**
     * The graph can differentiate two interaction modes: MOVE_NODES enables the user to move nodes and only draw/delete edges when clicking on the EdgeHandles EDIT_EDGES locks all nodes in position and enables the user to draw and delte edges from any DragHandle of the node
     * 
     */
    public enum InteractionMode {
        MOVE_NODES,
        EDIT_EDGES;
    }

    /**
     * DragController to be used to drag nodes around or onto other graphs
     */
    private DragController dragController;

    /**
     * DropController to be used to drop nodes from the pallete's graph onto the main graph
     */
    private DropController dropController;

    private ArrayList<Node<?, ?>> nodes = new ArrayList<Node<?, ?>>();

    private ArrayList<Edge> edges = new ArrayList<Edge>();

    private HashMap<Node<?,?>, Collection<Edge>> nodeToEdgeMap = new HashMap<Node<?,?>, Collection<Edge>>();
    
    private InteractionMode interactionMode = InteractionMode.MOVE_NODES;

    private Collection<NodeListener> nodeListeners =  new ArrayList<NodeListener>();

    /**
     * Creates a new GWTGraph with a normal MoveNodeDragController which allows for nodes to be moved around freely on the graphs area
     */
    public GWTGraph() {
        setSize("100%", "100%");
        dragController = new MoveNodeDragController(this, true);
        dropController = new AbsolutePositionDropController(this);
    }

    /**
     * Creates a new GWTGraph with the given DragController. This can be used to eg. specify a CopyNodeDragController to copy nodes to other Graphs
     */
    public GWTGraph(DragController dragController) {
        setSize("100%", "100%");
        this.dragController = dragController;
        dropController = new AbsolutePositionDropController(this);
    }

    /**
     * Returns this Graphs DropController. This is useful for creating DragControllers like the CopyNodeDragController used in Palettes which should allow drops on this graph
     */
    public DropController getDropController() {
        return dropController;
    }

    /**
     * Adds the given node to this graph at the specified position
     * 
     * @param node
     *            the node to be added
     * @param left
     *            the nodes's left position
     * @param top
     *            the nodes's top position
     */
    public void addNode(Node<?, ?> node, int left, int top) {
        super.add(node.getNodeView(), left, top);
        initNode(node);
        fireNodeAddedEvent(node);
    }

    private void initNode(Node<?, ?> node) {
        nodes.add(node);
        node.setParentGraph(this);
        initNodeView(node.getNodeView());
    }

    private void initNodeView(NodeView<?> nodeView) {
        if (interactionMode.equals(InteractionMode.MOVE_NODES)) {
            // Make the node draggeable and thus moveable
            dragController.makeDraggable(nodeView);
        }

        // When in DRAW_EDGES mode the DragHandle may be used to draw edges
        SourcesMouseEvents dragHandle = (SourcesMouseEvents) nodeView.getDragHandle();
        dragHandle.addMouseListener(new DrawEdgeMouseListener(this, nodeView, false));

        // Add any EdgeHandlers to draw Edges any time from specific handles
        if (nodeView.getEdgeCreationHandles() != null) {
            for (EdgeCreationHandle w : nodeView.getEdgeCreationHandles()) {
                w.getHandle().addMouseListener(new DrawEdgeMouseListener(this, nodeView, true));
            }
        }
    }

    /**
     * Adds an Edge to this graph
     */
    public boolean addEdge(Edge edge) {
        if (edge.isValid() == false) {
            return false;
        }
        if (!nodes.contains(edge.getNode1()) || !nodes.contains(edge.getNode2())) {
            return false;
        }
        edges.add(edge);
        addToNodeToEdgeMap(edge, edge.getNode1());
        addToNodeToEdgeMap(edge, edge.getNode2());
        edge.getConnection().appendTo(this);
        
        edge.setParentGraph(this);
        if (edge instanceof NodeListener) {
            addNodeListener((NodeListener) edge);
        }
        return true;
    }

    private void addToNodeToEdgeMap(Edge edge, Node<?, ?> node) {
        Collection<Edge> c = nodeToEdgeMap.get(node);
        if (c == null) {
            c = new ArrayList<Edge>();
        }
        c.add(edge);
        nodeToEdgeMap.put(node, c);
    }
    
    public void addNodeListener(NodeListener l) {
        nodeListeners.add(l);
    }

    /**
     * Removes the given Node from this graph. This also removes all Edges connected to that node
     *
     * @return true if the node was removed, false if not (ie. the node was not in this graph)
     */
    public boolean removeNode(Node<?, ?> node) {
        if (!nodes.contains(node)) {
            return false;
        }
        if (nodeToEdgeMap.get(node) != null) {
            // Remove all Edges connected to this node
            for (Edge e : nodeToEdgeMap.get(node)) {
                removeEdge(e);
            }
            nodeToEdgeMap.remove(node);
        }
        nodes.remove(node);
        remove(node.getNodeView());
        fireNodeRemoved(node);
        return true;
    }

    /**
     * Removes the given edge from this graph
     * 
     * @return true if the edge was removed, false if not (ie. the edge was not in this graph)
     */
    public boolean removeEdge(Edge edge) {
        if (!edges.contains(edge)) {
            return false;
        }
        edges.remove(edge);
        if (edge instanceof NodeListener) {
            nodeListeners.remove(edge);
        }
        edge.getConnection().remove();
        return true;
    }

    /**
     * @return Edge from n1 to n2, if none found Edge from n2 to n1 or null
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
        ArrayList<DropController> list = new ArrayList<DropController>();
        for (Widget w : getChildren()) {
            if (w instanceof NodeView) {
                NodeView<?> nodeView = (NodeView<?>) w;
                if (nodeView.getEdgeCreationHandles() != null) {
                    for (EdgeCreationHandle h : nodeView.getEdgeCreationHandles()) {
                        list.add(new CreateEdgeDropController(w, this, h.getEdge()));
                    }
                }
            }
        }
        return list;
    }

    /**
     * @return Current InteractionMode of this GWTGraph
     */
    public InteractionMode getInteractionMode() {
        return interactionMode;
    }

    /**
     * Sets the new InteractionMode for this GWTGraph
     */
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
        for (Widget w : getChildren()) {
            if (w instanceof NodeView) {
                dragController.makeNotDraggable(w);
            }
        }
    }

    private void leaveEdgeMode() {
        for (Widget w : getChildren()) {
            if (w instanceof NodeView) {
                dragController.makeDraggable(w);
            }
        }
    }
    
    public Collection<Node<?, ?>> getNodes() {
        return nodes;
    }

    public Collection<Edge> getEdges() {
        return edges;
    }
    
    public void fireNodeAddedEvent(Node<?, ?> node) {
        for (NodeListener l : nodeListeners ) {
            l.nodeAdded(node);
        }
    }
    
    public void fireNodeChangedEvent(Node<?, ?> node) {
        for (NodeListener l : nodeListeners ) {
            l.nodeChanged(node);
        }
    }
    
    private void fireNodeRemoved(Node<?, ?> node) {
        for (NodeListener l : nodeListeners ) {
            l.nodeRemoved(node);
        } 
    }

    public Collection<Edge> getEdgesForNode(Node<?,?> node) {
        return nodeToEdgeMap.get(node);
    }
    
    public void clear() {
        Collection<Node<?,?>> c = new ArrayList<Node<?,?>>();
        c.addAll(getNodes());
        for (Node<?, ?> n: c) {
            removeNode(n);
        }
    }
}
