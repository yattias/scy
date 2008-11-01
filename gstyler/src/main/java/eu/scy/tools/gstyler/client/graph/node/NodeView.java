package eu.scy.tools.gstyler.client.graph.node;

import java.util.Collection;

import com.allen_sauer.gwt.dnd.client.HasDragHandle;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import eu.scy.tools.gstyler.client.graph.CSSConstants;
import eu.scy.tools.gstyler.client.graph.edge.EdgeCreationHandle;

/**
 * According to the MVC pattern this class is the graphical representation of a Node.
 * UI logic has to go here, especially updating the view according to the nodes model.
 * Also this view may return a list of EdgeCreationHandles which allow the user to draw edges by clicking on a UI element of this node
 */
public abstract class NodeView<N extends Node<?, ?>> extends VerticalPanel implements HasDragHandle {

    private N node;

    protected Label header;

    /**
     * Creates a new NodeView for the given Node
     */
    public NodeView(N node) {
        super();
        this.node = node;
        this.setHorizontalAlignment(ALIGN_CENTER);
        header = new Label();
        header.setWidth("100%");
        header.addStyleName(CSSConstants.CSS_DRAG_HANDLE);
        add(header);
    }

    /**
     * @return The node this view is associated with
     */
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
     * @return Collection of EdgeCreationHandles which may be empty, but not null 
     */
    public abstract Collection<EdgeCreationHandle> getEdgeCreationHandles();

    /**
     * This method needs to update the view according to the current data in its model
     */
    public abstract void updateFromModel();
}
