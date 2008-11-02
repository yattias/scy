package eu.scy.tools.gstyler.client.plugins.qoc.edges;

import eu.scy.tools.gstyler.client.graph.GWTGraph;
import eu.scy.tools.gstyler.client.graph.NodeListener;
import eu.scy.tools.gstyler.client.graph.edge.Edge;
import eu.scy.tools.gstyler.client.graph.node.Node;
import eu.scy.tools.gstyler.client.plugins.qoc.nodes.OptionNode;


public class PositiveEdge extends Edge implements NodeListener {

    public PositiveEdge() {
    }
    
    public PositiveEdge(Node<?, ?> node1, Node<?, ?> node2) {
        init(node1, node2);
    }
    
    public void setParentGraph(GWTGraph parentGraph) {
        super.setParentGraph(parentGraph);
        updateConnectedCriteria();
    }
    
    private void updateConnectedCriteria() {
        // propagate change to connected nodes
        if (getNode1() instanceof OptionNode) {
            ((OptionNode) getNode1()).calculatePreference();
        }
        if (getNode2() instanceof OptionNode) {
            ((OptionNode) getNode1()).calculatePreference();
        }  
    }
    
    public void nodeChanged(Node<?, ?> source) {
        updateConnectedCriteria();
    }

    public void nodeAdded(Node<?, ?> node) {
        // TODO Auto-generated method stub
    }

    public void nodeRemoved(Node<?, ?> node) {
        // TODO Auto-generated method stub
    }
}
