package eu.scy.tools.gstyler.client.plugins.mindmap;

import eu.scy.tools.gstyler.client.graph.node.Node;


public class MindmapNode extends Node<MindmapNodeModel, MindmapNodeView> {

    @Override
    public MindmapNodeModel createModel() {
        return new MindmapNodeModel();
    }

    @Override
    public MindmapNodeView createView() {
        return new MindmapNodeView(this);
    }
    
    @Override
    public Node<MindmapNodeModel, MindmapNodeView> createClone() {
        MindmapNode clone = new MindmapNode();
        clone.setModel(getModel().createClone());
        return clone;
    }
}
