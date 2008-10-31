package eu.scy.tools.gstyler.client.test;

import java.util.ArrayList;
import java.util.Collection;

import eu.scy.tools.gstyler.client.graph.EdgeCreationHandle;
import eu.scy.tools.gstyler.client.graph.NodeView;


public class ViewMockep extends NodeView<NodeMockup> {

    public ViewMockep(NodeMockup node) {
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
