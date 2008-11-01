package eu.scy.tools.gstyler.client.graph.node;

public abstract class NodeModel {
    
    protected String title;
    
    public NodeModel() {
        title = "Unknown Node";
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * You have to overwrite this method and make sure your Model creates a clone of itself!
     */
    public abstract NodeModel createClone();
}
