package eu.scy.tools.gstyler.client.graph;

import com.allen_sauer.gwt.dnd.client.HasDragHandle;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;



public abstract class NodeView<N extends Node<?,?>> extends VerticalPanel implements HasDragHandle {

    private N node;

    protected Label header;
    
    public NodeView(N node) {
        super();
        this.node = node;
        this.setHorizontalAlignment(ALIGN_CENTER);
        header = new Label();
        header.setWidth("100%");
        header.addStyleName(CSSConstants.CSS_DRAG_HANDLE);
        add(header);
    }
    
    public N getNode() {
        return node;
    }

    public abstract SourcesMouseEvents getEdgeHandle();
    
    /**
     * A DragHandle needs to implement SourcesMouseEvents, such as FocusPanel, Image or Label already do
     */
    public Widget getDragHandle() {
        return header;
    }
    
    public abstract void updateFromModel();
}
