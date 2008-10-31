package eu.scy.tools.gstyler.client.test;

import eu.scy.tools.gstyler.client.graph.NodeModel;


public class ModelMockup extends NodeModel {

    @Override
    public NodeModel createClone() {
        return new ModelMockup();
    }

}
