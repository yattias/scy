package eu.scy.tools.gstyler.client.plugins.qoc.nodes;

import eu.scy.tools.gstyler.client.graph.node.Node;


@SuppressWarnings("unchecked")
public abstract class QOCNode<M extends QOCNodeModel, V extends QOCNodeView> extends Node {
    
    public M getModel() {
        return (M) model;
    }
    
    public void setModel(M model) {
        this.model = model;
    }
}
