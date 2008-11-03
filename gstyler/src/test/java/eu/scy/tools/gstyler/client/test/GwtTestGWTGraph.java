package eu.scy.tools.gstyler.client.test;

import java.util.Collection;

import com.google.gwt.junit.client.GWTTestCase;

import eu.scy.tools.gstyler.client.graph.GWTGraph;
import eu.scy.tools.gstyler.client.graph.NodeListener;
import eu.scy.tools.gstyler.client.graph.edge.Edge;
import eu.scy.tools.gstyler.client.graph.node.Node;

/**
 * IMPORTANT: New test classes _must_ start with GwtTest - see http://gwt-maven.googlecode.com/svn/docs/maven-googlewebtoolkit2-plugin/testing.html
 * 
 * To run the test use "-Xmx512m -Xms512m" as VMArgs
 */
public class GwtTestGWTGraph extends GWTTestCase implements NodeListener {

    private GWTGraph graph;
    private NodeMockup node3;
    private NodeMockup node1;
    private NodeMockup node2;
    private Edge edge1;
    private Edge edge2;
    private boolean callbackCalled;

    @Override
    public String getModuleName() {
        return "eu.scy.tools.gstyler.GStyler";
    }

    public void testAddNode() {
        GWTGraph graph = new GWTGraph();
        callbackCalled = false;
        graph.addNodeListener(this);
        Node<?, ?> node1 = new NodeMockup();
        assertFalse(graph.getNodes().contains(node1));
        assertEquals(0, graph.getNodes().size());
        graph.addNode(node1, 100, 100);
        assertTrue(callbackCalled);
        assertTrue(graph.getNodes().contains(node1));
        assertEquals(1, graph.getNodes().size());
    }
    
    public void testAddEdgeUninitialized() {
        GWTGraph graph = new GWTGraph();
        Edge edge = new Edge();
        assertFalse(graph.addEdge(edge));
    }
    
    public void testAddEdgeWithoutNodes() {
        GWTGraph graph = new GWTGraph();
        Edge edge = new Edge();
        Node<?, ?> node1 = new NodeMockup();
        Node<?, ?> node2 = new NodeMockup();
        edge.init(node1, node2);
        assertFalse(graph.addEdge(edge));
    }
    
    public void testAddEdge() {
        GWTGraph graph = new GWTGraph();
        Edge edge = new Edge();
        Node<?, ?> node1 = new NodeMockup();
        Node<?, ?> node2 = new NodeMockup();
        graph.addNode(node1, 100, 100);
        graph.addNode(node2, 200, 200);
        edge.init(node1, node2);
        assertEquals(0, graph.getEdges().size());
        assertFalse(graph.getEdges().contains(edge));
        assertTrue(graph.addEdge(edge));
        assertEquals(1, graph.getEdges().size());
        assertTrue(graph.getEdges().contains(edge));
    }
    
    public void testRemoveEdgeNonexistant() {
        GWTGraph graph = new GWTGraph();
        assertFalse(graph.removeEdge(new Edge()));
    }
    
    public void testGetEdge() {
        createSimpleGraph();

        assertEquals(edge1, graph.getEdge(node1, node2));
        assertEquals(edge1, graph.getEdge(node2, node1));
        assertNull(graph.getEdge(node1, new NodeMockup()));
    }

    public void testGetOtherNode() {
        createSimpleGraph();

        assertEquals(node2, edge1.getOtherNode(node1));
        assertEquals(node1, edge1.getOtherNode(node2));
        assertEquals(null, edge1.getOtherNode(node3));
    }
    
    public void testRemoveNode() {
        GWTGraph graph = new GWTGraph();
        callbackCalled = false;
        graph.addNodeListener(this);
        Node<?, ?> node1 = new NodeMockup();
        assertFalse(graph.removeNode(node1));
        assertFalse(callbackCalled); // removing of node will failed, thus callback should not get called
        assertEquals(0, graph.getNodes().size());
        graph.addNode(node1, 100, 100);
        assertEquals(1, graph.getNodes().size());
        assertTrue(graph.removeNode(node1));
        assertTrue(callbackCalled);
        assertEquals(0, graph.getNodes().size());
    }
    
    public void testRemoveNodeWithEdges() {
        createSimpleGraph();
        // Removing a node results in all edges to or from this node being deleted
        assertTrue(graph.removeNode(node1));
        assertEquals(2, graph.getNodes().size());
        assertEquals(0, graph.getEdges().size());
    }
    
    /**
     * Creates a simple Graph
     * 
     *      Node1<--->Node2<---->Node3 
     */
    private void createSimpleGraph() {
        graph = new GWTGraph();
        edge1 = new Edge();
        node1 = new NodeMockup();
        node2 = new NodeMockup();
        graph.addNode(node1, 100, 100);
        graph.addNode(node2, 200, 200);
        edge1.init(node1, node2);
        assertEquals(0, graph.getEdges().size());
        assertFalse(graph.getEdges().contains(edge1));
        assertTrue(graph.addEdge(edge1));
        assertEquals(1, graph.getEdges().size());
        assertTrue(graph.getEdges().contains(edge1));

        node3 = new NodeMockup();
        graph.addNode(node3, 300, 300);
        edge2 = new Edge();
        edge2.init(node1, node3);
        assertTrue(graph.addEdge(edge2));
        assertEquals(3, graph.getNodes().size());
        assertEquals(2, graph.getEdges().size());
    }

    public void testGetEdgesForNode() {
        createSimpleGraph();
        Collection<Edge> c = graph.getEdgesForNode(node1);
        assertEquals(2, c.size());
        assertTrue(c.contains(edge1));
        assertTrue(c.contains(edge2));
    }

    public void testNodeChange() {
        GWTGraph graph = new GWTGraph();
        NodeMockup node1 = new NodeMockup();
        callbackCalled = false;
        graph.addNodeListener(this);
        node1.setModel(new ModelMockup());
        assertFalse(callbackCalled); // callback works only when the node is inside a graph we registered as a listener for
        graph.addNode(node1, 100, 100);
        assertTrue(callbackCalled);
    }
    
    public void testClear() {
        createSimpleGraph();
        graph.clear();
        assertEquals(0, graph.getNodes().size());
        assertEquals(0, graph.getEdges().size());
    }
    
    public void nodeAdded(Node<?, ?> node) {
        callbackCalled = true;
    }

    public void nodeChanged(Node<?, ?> node) {
        callbackCalled = true;
    }

    public void nodeRemoved(Node<?, ?> node) {
        callbackCalled = true;
    }
}
