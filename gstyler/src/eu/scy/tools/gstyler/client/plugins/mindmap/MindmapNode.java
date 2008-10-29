package eu.scy.tools.gstyler.client.plugins.mindmap;

import eu.scy.tools.gstyler.client.graph.Node;


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
        MindmapNode nodeClone = new MindmapNode();
        MindmapNodeModel modelClone = new MindmapNodeModel();
        modelClone.setHeader(getModel().getHeader());
        modelClone.setNote(getModel().getNote());
        nodeClone.setModel(modelClone);
        return nodeClone;
    }
}
