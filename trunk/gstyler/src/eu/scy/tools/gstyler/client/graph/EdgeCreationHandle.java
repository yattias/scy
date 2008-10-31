package eu.scy.tools.gstyler.client.graph;

import com.google.gwt.user.client.ui.SourcesMouseEvents;

/**
 * Handle a NodeView may have which being clicked on results in a specific edge being drawn
 */
public class EdgeCreationHandle {
    
    private SourcesMouseEvents handle;
    private Edge edge;
    
    public EdgeCreationHandle(SourcesMouseEvents handle, Edge edge) {
        super();
        this.handle = handle;
        this.edge = edge;
    }
    
    /**
     * @return The Widget implementing SourcesMouseEvents where the user clicks on to draw the edge
     */
    public SourcesMouseEvents getHandle() {
        return handle;
    }

    /**
     * @return The edge to be created
     */
    public Edge getEdge() {
        return edge;
    }
}
