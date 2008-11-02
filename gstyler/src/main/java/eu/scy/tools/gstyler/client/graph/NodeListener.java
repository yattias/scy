package eu.scy.tools.gstyler.client.graph;

import eu.scy.tools.gstyler.client.graph.node.Node;


public interface NodeListener {

    public void nodeAdded(Node<?, ?> node);
    
    public void nodeChanged(Node<?, ?> node);

    public void nodeRemoved(Node<?, ?> node);
}
