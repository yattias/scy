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

import eu.scy.tools.gstyler.client.graph.dnd.edge.DrawEdgeDropController;
import eu.scy.tools.gstyler.client.graph.dnd.edge.DrawEdgeMouseListener;
import eu.scy.tools.gstyler.client.graph.dnd.node.MoveNodeDragController;
import eu.scy.tools.gstyler.client.graph.edge.Edge;
import eu.scy.tools.gstyler.client.graph.edge.EdgeCreationHandle;
import eu.scy.tools.gstyler.client.graph.node.Node;
import eu.scy.tools.gstyler.client.graph.node.NodeView;

/**
 * GWTGraph allows Nodes being added and moved around via Drag&Drop, as well as edges connecting the nodes.
 */
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

    private Collection<EdgeListener> edgeListeners = new ArrayList<EdgeListener>();

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
        if (node instanceof EdgeListener) {
            edgeListeners.add((EdgeListener) node);
        }
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

        // Add any EdgeHandlers to draw Edges in MOVE_NODES mode from specific handles
        if (nodeView.getEdgeCreationHandles() != null) {
            for (EdgeCreationHandle h : nodeView.getEdgeCreationHandles()) {
                h.getHandle().addMouseListener(new DrawEdgeMouseListener(this, nodeView, InteractionMode.MOVE_NODES, h.getEdge()));
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
        addToNodeToEdgeMap(edge.getNode1(), edge);
        addToNodeToEdgeMap(edge.getNode2(), edge);
        edge.getConnection().appendTo(this);
        
        edge.setParentGraph(this);
        if (edge instanceof NodeListener) {
            addNodeListener((NodeListener) edge);
        }
        fireEdgeAddedEvent(edge);
        return true;
    }

    private void addToNodeToEdgeMap(Node<?, ?> node, Edge edge) {
        Collection<Edge> c = nodeToEdgeMap.get(node);
        if (c == null) {
            c = new ArrayList<Edge>();
        }
        c.add(edge);
        nodeToEdgeMap.put(node, c);
    }
    
    private void removeFromNodeToEdgeMap(Node<?, ?> node, Edge edge) {
        Collection<Edge> c = nodeToEdgeMap.get(node);
        if (c != null) {
            c.remove(edge);
        }
        nodeToEdgeMap.put(node, c);
    }
    
    public void addNodeListener(NodeListener listener) {
        nodeListeners.add(listener);
    }

    public void addEdgeListener(EdgeListener listener) {
        edgeListeners.add(listener);
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
            // Create a copied array to avoid a ConcurrentModificationException
            Edge[] edges = nodeToEdgeMap.get(node).toArray(new Edge[nodeToEdgeMap.get(node).size()]);
            for (Edge e : edges) {
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
        removeFromNodeToEdgeMap(edge.getNode1(), edge);
        removeFromNodeToEdgeMap(edge.getNode2(), edge);
        
        edges.remove(edge);
        if (edge instanceof NodeListener) {
            nodeListeners.remove(edge);
        }
        edge.getConnection().remove();
        fireEdgeRemoved(edge);
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
    public Collection<DropController> getDrawEdgeDropControllers(Edge edge) {
        ArrayList<DropController> list = new ArrayList<DropController>();
        for (Widget w : getChildren()) {
            if (w instanceof NodeView) {
                list.add(new DrawEdgeDropController(w, this, edge));
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

    public void enterEdgeMode(Edge edge) {
        interactionMode = InteractionMode.EDIT_EDGES;
        
        // Nodes are not draggable anymore...
        for (Widget w : getChildren()) {
            if (w instanceof NodeView) {
                dragController.makeNotDraggable(w);
                
                NodeView<?> nodeView = (NodeView<?>) w;
                // .. instead: The DragHandle may be used to draw edges
                SourcesMouseEvents dragHandle = (SourcesMouseEvents) nodeView.getDragHandle();
                dragHandle.addMouseListener(new DrawEdgeMouseListener(this, nodeView, InteractionMode.EDIT_EDGES, edge));
            }
        }
    }

    public void enterNodeMode() {
        interactionMode = InteractionMode.MOVE_NODES;
        
        // Make nodes draggable again
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
    
    private void fireNodeAddedEvent(Node<?, ?> node) {
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
    
    private void fireEdgeAddedEvent(Edge edge) {
        for (EdgeListener l : edgeListeners ) {
            l.edgeAdded(edge);
        }
    }
    
    // FIXME: currently unused, maybe use in Edge
    public void fireEdgeChangedEvent(Edge edge) {
        for (EdgeListener l : edgeListeners ) {
            l.edgeChanged(edge);
        }
    }
    
    private void fireEdgeRemoved(Edge edge) {
        for (EdgeListener l : edgeListeners ) {
            l.edgeRemoved(edge);
        } 
    }
    

    /**
     * @return All Nodes connected to the given Edge
     */
    public Collection<Edge> getEdgesForNode(Node<?,?> node) {
        return nodeToEdgeMap.get(node);
    }
    
    /**
     * Removes all Nodes (and thus all edges) from this Graph
     */
    public void clear() {
        // Create a copied array to avoid a ConcurrentModificationException
        Node<?,?>[] nodes = getNodes().toArray(new Node[getNodes().size()]);
        for (Node<?, ?> n: nodes) {
            removeNode(n);
        }
    }
}
