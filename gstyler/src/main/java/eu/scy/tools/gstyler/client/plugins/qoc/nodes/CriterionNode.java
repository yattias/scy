package eu.scy.tools.gstyler.client.plugins.qoc.nodes;

import eu.scy.tools.gstyler.client.graph.node.Node;


public class CriterionNode extends QOCNode<CriterionNodeModel, CriterionNodeView> {

    @SuppressWarnings("unchecked")
    @Override
    public Node<CriterionNodeModel, CriterionNodeView> createClone() {
        CriterionNode clone = new CriterionNode();
        clone.setModel(getModel().createClone());
        return clone;
    }

    @Override
    public CriterionNodeModel createModel() {
        return new CriterionNodeModel();
    }

    @Override
    public CriterionNodeView createView() {
        return new CriterionNodeView(this);
    }

    public void setRelevance(int relevance) {
        getModel().setRelevance(relevance);
        fireNodeChangedEvent();
    }

    public int getRelevance() {
        return getModel().getRelevance();
    }
}
