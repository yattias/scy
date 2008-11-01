package eu.scy.tools.gstyler.client.plugins.qoc.nodes;

import eu.scy.tools.gstyler.client.graph.node.Node;


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

}
