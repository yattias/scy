package eu.scy.tools.gstyler.client.plugins.qoc.nodes;

import eu.scy.tools.gstyler.client.graph.edge.Edge;
import eu.scy.tools.gstyler.client.graph.node.Node;
import eu.scy.tools.gstyler.client.plugins.qoc.edges.QuestionEdge;


public class QuestionNode extends Node<QuestionNodeModel, QuestionNodeView> {

    @Override
    public Node<QuestionNodeModel, QuestionNodeView> createClone() {
        QuestionNode clone = new QuestionNode();
        clone.setModel(getModel().createClone());
        return clone;
    }

    @Override
    public QuestionNodeModel createModel() {
        return new QuestionNodeModel();
    }

    @Override
    public QuestionNodeView createView() {
        return new QuestionNodeView(this);
    }

    public void calculatePreference() {
        OptionNode maxNode = null;
        for (Edge edge : getParentGraph().getEdgesForNode(this) ) {
            if (edge instanceof QuestionEdge) {
                OptionNode oNode = (OptionNode) edge.getOtherNode(this);
                oNode.setPreferred(false);
                if (maxNode == null) {
                    maxNode = oNode;
                } else if (oNode.getModel().getScore() > maxNode.getModel().getScore()) {
                    maxNode = oNode;
                }
            }
        }
        maxNode.setPreferred(true);
    }
}
