package eu.scy.tools.gstyler.client.plugins.qoc.edges;

import eu.scy.tools.gstyler.client.graph.node.Node;


public class NegativeEdge extends EvaluationEdge {

    public NegativeEdge() {
        super();
    }

    public NegativeEdge(Node<?, ?> node1, Node<?, ?> node2) {
        super(node1, node2);
    }

}
