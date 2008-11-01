package eu.scy.tools.gstyler.client.graph.node;

import eu.scy.tools.gstyler.client.graph.GWTGraph;

public abstract class Node<M extends NodeModel, V extends NodeView<?>> {

    private M model;
    private V view;
    private GWTGraph parentGraph;
    
    public Node() {
        this.model = createModel();
        this.view = createView();
        view.updateFromModel();
    }

    public V getNodeView() {
        return view;
    }
    
    public Node<M, V> clone() {
        return createClone();
    }
    
    public M getModel() {
        return model;
    }

    public void setModel(M model) {
        this.model = model;
        view.updateFromModel();
    }

    public GWTGraph getParentGraph() {
        return parentGraph;
    }

    public void setParentGraph(GWTGraph parentGraph) {
        this.parentGraph = parentGraph;
    }

    public abstract Node<M, V> createClone();
    public abstract V createView();
    public abstract M createModel();
}
