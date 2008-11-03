package eu.scy.tools.gstyler.client.graph;

import eu.scy.tools.gstyler.client.graph.edge.Edge;

/**
 * Classes implementing the EdgeListener interface may register at a GWTGraph to be notified
 * when edges are added, changed or removed.
 * Nodes implementing this interface will be automatically added as a listener when being added to the graph.
 */
public interface EdgeListener {

    public void edgeAdded(Edge edge);
    
    public void edgeChanged(Edge edge);

    public void edgeRemoved(Edge edge);
}
