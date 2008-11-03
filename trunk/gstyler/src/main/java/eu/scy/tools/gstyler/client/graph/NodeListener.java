package eu.scy.tools.gstyler.client.graph;

import eu.scy.tools.gstyler.client.graph.node.Node;

/**
 * Classes implementing the NodeListener interface may register at a GWTGraph to be notified
 * when nodes are added, changed or removed.
 * Edges implementing this interface will be automatically added as a listener when being added to the graph.
 */
public interface NodeListener {

    public void nodeAdded(Node<?, ?> node);
    
    public void nodeChanged(Node<?, ?> node);

    public void nodeRemoved(Node<?, ?> node);
}
