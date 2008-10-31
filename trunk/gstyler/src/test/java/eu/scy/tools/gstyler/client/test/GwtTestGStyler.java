package eu.scy.tools.gstyler.client.test;

import com.google.gwt.junit.client.GWTTestCase;

import eu.scy.tools.gstyler.client.graph.Edge;
import eu.scy.tools.gstyler.client.graph.GWTGraph;

/**
 * IMPORTANT: New test classes _must_ start with GwtTest - see http://gwt-maven.googlecode.com/svn/docs/maven-googlewebtoolkit2-plugin/testing.html
 */
public class GwtTestGStyler extends GWTTestCase {

    @Override
    public String getModuleName() {
        return "eu.scy.tools.gstyler.GStyler";
    }

    public void testEdgeNotInitialized() {
        GWTGraph graph = new GWTGraph();
        Edge edge = new Edge();
        assertFalse(graph.addEdge(edge));
    }
}
