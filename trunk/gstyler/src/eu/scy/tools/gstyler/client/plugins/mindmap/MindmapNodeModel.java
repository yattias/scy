package eu.scy.tools.gstyler.client.plugins.mindmap;

import eu.scy.tools.gstyler.client.graph.NodeModel;


public class MindmapNodeModel extends NodeModel {
    
    private String note;

    public MindmapNodeModel() {
        this.header = "Note";
        this.note = "";
    }
    
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
