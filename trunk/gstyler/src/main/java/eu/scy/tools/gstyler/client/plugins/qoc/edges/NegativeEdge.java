package eu.scy.tools.gstyler.client.plugins.qoc.edges;

import eu.scy.tools.gstyler.client.graph.node.Node;
import eu.scy.tools.gstyler.client.plugins.qoc.QOCCSSConstants;


public class NegativeEdge extends EvaluationEdge {

    public NegativeEdge() {
        super();
    }

    public NegativeEdge(Node<?, ?> node1, Node<?, ?> node2) {
        super(node1, node2);
    }

    protected String getStyleName() {
        return QOCCSSConstants.CSS_NEGATIVE_EDGE;
    }
}
