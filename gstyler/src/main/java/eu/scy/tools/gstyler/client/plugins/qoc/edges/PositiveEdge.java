package eu.scy.tools.gstyler.client.plugins.qoc.edges;

import eu.scy.tools.gstyler.client.graph.NodeListener;
import eu.scy.tools.gstyler.client.graph.node.Node;


public class PositiveEdge extends EvaluationEdge implements NodeListener {

    public PositiveEdge() {
        super();
    }

    public PositiveEdge(Node<?, ?> node1, Node<?, ?> node2) {
        super(node1, node2);
    }

}
