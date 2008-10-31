package eu.scy.tools.gstyler.client.graph;

import java.util.Collection;

import com.allen_sauer.gwt.dnd.client.HasDragHandle;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class NodeView<N extends Node<?, ?>> extends VerticalPanel implements HasDragHandle {

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

    /**
     * A DragHandle is used to move the nodes via drag and drop during the MOVE_NODES InteractionMode.
     * A DragHandle needs to be a Widget implementing SourcesMouseEvents like FocusPanel, Image or Label
     * 
     * By Default the header label is the DragHandle, overwrite this method to change this behaviour
     */
    public Widget getDragHandle() {
        return header;
    }

    /**
     * EdgeCreationHandles are used to draw edges via drag and drop from one node to another node during the MOVE_NODES InteractionMode
     */
    public abstract Collection<EdgeCreationHandle> getEdgeCreationHandles();

    /**
     * This method needs to update the view according to the current data in its model
     */
    public abstract void updateFromModel();
}
