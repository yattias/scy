package eu.scy.tools.gstyler.client.graph.node;

public abstract class Node<M extends NodeModel, V extends NodeView<?>> {

    private M model;
    private V view;
    
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

    public abstract Node<M, V> createClone();
    public abstract V createView();
    public abstract M createModel();
}
