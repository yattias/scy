package eu.scy.tools.gstyler.client.plugins.qoc.nodes;

import eu.scy.tools.gstyler.client.graph.edge.Edge;
import eu.scy.tools.gstyler.client.graph.node.Node;
import eu.scy.tools.gstyler.client.plugins.qoc.edges.PositiveEdge;


public class OptionNode extends Node<OptionNodeModel, OptionNodeView> {

    @Override
    public Node<OptionNodeModel, OptionNodeView> createClone() {
        OptionNode clone = new OptionNode();
        clone.setModel(getModel().createClone());
        return clone;
    }

    @Override
    public OptionNodeModel createModel() {
        return new OptionNodeModel();
    }

    @Override
    public OptionNodeView createView() {
        return new OptionNodeView(this);
    }

    public void calculatePreference() {
       int score = 0; 
       for (Edge e : getParentGraph().getEdgesForNode(this) ) {
           if (e instanceof PositiveEdge) {
               CriterionNode cNode = (CriterionNode) e.getOtherNode(this);
               score += cNode.getModel().getRelevance();
           }
       }
       getModel().setScore(score);
       getNodeView().updateFromModel();
    }

}
