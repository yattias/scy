package eu.scy.tools.gstyler.client.plugins.qoc.edges;

import eu.scy.tools.gstyler.client.graph.GWTGraph;
import eu.scy.tools.gstyler.client.graph.NodeListener;
import eu.scy.tools.gstyler.client.graph.edge.Edge;
import eu.scy.tools.gstyler.client.graph.node.Node;
import eu.scy.tools.gstyler.client.plugins.qoc.nodes.CriterionNode;
import eu.scy.tools.gstyler.client.plugins.qoc.nodes.OptionNode;

public class EvaluationEdge extends Edge implements NodeListener {

    public EvaluationEdge() {
        super();
    }

    public EvaluationEdge(Node<?, ?> node1, Node<?, ?> node2) {
        super(node1, node2);
    }

    @Override
    public void setParentGraph(GWTGraph parentGraph) {
        super.setParentGraph(parentGraph);
        updateConnectedCriterionNode();
    }

    private void updateConnectedCriterionNode() {
        if (getNode1() instanceof OptionNode) {
            ((OptionNode) getNode1()).calculateScore();
        }
        if (getNode2() instanceof OptionNode) {
            ((OptionNode) getNode2()).calculateScore();
        }  
    }

    public void nodeChanged(Node<?, ?> source) {
        System.out.println("node change!");
        if (source instanceof CriterionNode && isConnectedTo(source)) {
            updateConnectedCriterionNode();
        }
    }

    public void nodeAdded(Node<?, ?> node) {
    }

    public void nodeRemoved(Node<?, ?> node) {
    }

}