package eu.scy.tools.gstyler.client.plugins.qoc.nodes;

import eu.scy.tools.gstyler.client.graph.EdgeListener;
import eu.scy.tools.gstyler.client.graph.edge.Edge;
import eu.scy.tools.gstyler.client.graph.node.Node;
import eu.scy.tools.gstyler.client.plugins.qoc.edges.EvaluationEdge;
import eu.scy.tools.gstyler.client.plugins.qoc.edges.NegativeEdge;
import eu.scy.tools.gstyler.client.plugins.qoc.edges.PositiveEdge;


public class OptionNode extends Node<OptionNodeModel, OptionNodeView> implements EdgeListener {

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

    public void calculateScore() {
        int score = 0; 
        for (Edge edge : getParentGraph().getEdgesForNode(this) ) {
            if (edge instanceof EvaluationEdge) {
                CriterionNode cNode = (CriterionNode) edge.getOtherNode(this);
                if (edge instanceof PositiveEdge) {
                    score += cNode.getModel().getRelevance();
                } else if (edge instanceof NegativeEdge) {
                    score -= cNode.getModel().getRelevance();
                } else {
                    System.out.println("Not a QOC Edge: " + edge);
                }
            }
        }
        System.out.println("new score: " + score);
        getModel().setScore(score);
        getNodeView().updateFromModel();
        fireNodeChangedEvent();
    }

    public void setPreferred(boolean b) {
        if (b) {
            getModel().setTitle("BEST OPTION");
        } else {
            getModel().setTitle("Option");
        }
        getNodeView().updateFromModel();
    }

    public void edgeAdded(Edge edge) {
    }

    public void edgeChanged(Edge edge) {
    }

    public void edgeRemoved(Edge edge) {
        if (edge.isConnectedTo(this)) {
            calculateScore();
        }
    }

}
