package eu.scy.tools.gstyler.client.plugins.qoc.nodes;

import eu.scy.tools.gstyler.client.graph.node.Node;


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

}
