package eu.scy.tools.gstyler.client.test;

import eu.scy.tools.gstyler.client.graph.node.Node;


public class NodeMockup extends Node<ModelMockup, ViewMockup> {

    @Override
    public Node<ModelMockup, ViewMockup> createClone() {
        return new NodeMockup();
    }

    @Override
    public ModelMockup createModel() {
        return new ModelMockup();
    }

    @Override
    public ViewMockup createView() {
        return new ViewMockup(this);
    }

}
