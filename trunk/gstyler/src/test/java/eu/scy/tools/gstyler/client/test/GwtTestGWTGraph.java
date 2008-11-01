package eu.scy.tools.gstyler.client.test;

import com.google.gwt.junit.client.GWTTestCase;

import eu.scy.tools.gstyler.client.graph.GWTGraph;
import eu.scy.tools.gstyler.client.graph.edge.Edge;
import eu.scy.tools.gstyler.client.graph.node.Node;

/**
 * IMPORTANT: New test classes _must_ start with GwtTest - see http://gwt-maven.googlecode.com/svn/docs/maven-googlewebtoolkit2-plugin/testing.html
 */
public class GwtTestGWTGraph extends GWTTestCase {

    @Override
    public String getModuleName() {
        return "eu.scy.tools.gstyler.GStyler";
    }

    public void testAddNode() {
        GWTGraph graph = new GWTGraph();
        Node<?, ?> node1 = new NodeMockup();
        assertFalse(graph.getNodes().contains(node1));
        graph.addNode(node1, 100, 100);
        assertTrue(graph.getNodes().contains(node1));
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
        assertFalse(graph.getEdges().contains(edge));
        assertTrue(graph.addEdge(edge));
        assertTrue(graph.getEdges().contains(edge));
    }
    
    public void testRemoveEdgeNonexistant() {
        GWTGraph graph = new GWTGraph();
        Edge edge = new Edge();
        assertFalse(graph.removeEdge(edge));
    }
    
    public void testGetEdge() {
        // Same as testAddEdge()
        GWTGraph graph = new GWTGraph();
        Edge edge = new Edge();
        Node<?, ?> node1 = new NodeMockup();
        Node<?, ?> node2 = new NodeMockup();
        graph.addNode(node1, 100, 100);
        graph.addNode(node2, 200, 200);
        edge.init(node1, node2);
        assertFalse(graph.getEdges().contains(edge));
        assertTrue(graph.addEdge(edge));
        assertTrue(graph.getEdges().contains(edge));
        
        Edge edge2 = graph.getEdge(node1, node2);
        assertEquals(edge, edge2);
        
        Edge edge3 = graph.getEdge(node2, node1);
        assertEquals(edge, edge3);
        
        Node<?, ?> node3 = new NodeMockup();
        Edge edge4 = graph.getEdge(node1, node3);
        assertNull(edge4);
    }
}
