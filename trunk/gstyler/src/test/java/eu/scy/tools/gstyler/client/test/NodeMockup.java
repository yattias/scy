package eu.scy.tools.gstyler.client.test;

import eu.scy.tools.gstyler.client.graph.Node;


public class NodeMockup extends Node<ModelMockup, ViewMockep> {

    @Override
    public Node<ModelMockup, ViewMockep> createClone() {
        return new NodeMockup();
    }

    @Override
    public ModelMockup createModel() {
        return new ModelMockup();
    }

    @Override
    public ViewMockep createView() {
        return new ViewMockep(this);
    }

}
