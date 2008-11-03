package eu.scy.tools.gstyler.client.plugins.qoc.edges;

import eu.scy.tools.gstyler.client.graph.node.Node;
import eu.scy.tools.gstyler.client.plugins.qoc.QOCCSSConstants;


public class PositiveEdge extends EvaluationEdge {

    public PositiveEdge() {
        super();
    }

    public PositiveEdge(Node<?, ?> node1, Node<?, ?> node2) {
        super(node1, node2);
    }

    protected String getStyleName() {
        return QOCCSSConstants.CSS_POSITIVE_EDGE;
    }
}
