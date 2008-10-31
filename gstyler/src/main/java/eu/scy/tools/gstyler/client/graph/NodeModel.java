package eu.scy.tools.gstyler.client.graph;



public abstract class NodeModel {
    
    protected String header;
    
    public NodeModel() {
        header = "Unknown Node";
    }
    
    public String getHeader() {
        return header;
    }
    
    public void setHeader(String header) {
        this.header = header;
    }

    public abstract NodeModel createClone();
}
