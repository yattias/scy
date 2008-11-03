package eu.scy.tools.gstyler.client.plugins.qoc.edges;

import eu.scy.tools.gstyler.client.graph.GWTGraph;
import eu.scy.tools.gstyler.client.graph.NodeListener;
import eu.scy.tools.gstyler.client.graph.edge.Edge;
import eu.scy.tools.gstyler.client.graph.node.Node;
import eu.scy.tools.gstyler.client.plugins.qoc.nodes.OptionNode;
import eu.scy.tools.gstyler.client.plugins.qoc.nodes.QuestionNode;


public class QuestionEdge extends Edge implements NodeListener {

    
    public QuestionEdge() {
        super();
    }

    public QuestionEdge(Node<?, ?> node1, Node<?, ?> node2) {
        super(node1, node2);
    }

    private void updateConnectedQuestionNode() {
        if (getNode1() instanceof QuestionNode) {
            ((QuestionNode) getNode1()).calculatePreference();
        }
        if (getNode2() instanceof QuestionNode) {
            ((QuestionNode) getNode2()).calculatePreference();
        }  
    }
    
    @Override
    public void setParentGraph(GWTGraph parentGraph) {
        super.setParentGraph(parentGraph);
        updateConnectedQuestionNode();
    }

    public void nodeAdded(Node<?, ?> node) {
        // TODO Auto-generated method stub
    }

    public void nodeChanged(Node<?, ?> node) {
        if (node instanceof OptionNode && isConnectedTo(node)) {
            updateConnectedQuestionNode();
        }
    }

    public void nodeRemoved(Node<?, ?> node) {
        // TODO Auto-generated method stub
    }

}
