package eu.scy.tools.gstyler.client.test;

import java.util.ArrayList;
import java.util.Collection;

import eu.scy.tools.gstyler.client.graph.edge.EdgeCreationHandle;
import eu.scy.tools.gstyler.client.graph.node.NodeView;


public class ViewMockup extends NodeView<NodeMockup> {

    public ViewMockup(NodeMockup node) {
        super(node);
    }

    @Override
    public Collection<EdgeCreationHandle> getEdgeCreationHandles() {
        return new ArrayList<EdgeCreationHandle>();
    }

    @Override
    public void updateFromModel() {
        
    }

}
