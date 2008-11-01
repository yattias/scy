package eu.scy.tools.gstyler.client.graph.node;

import java.util.Collection;

import com.allen_sauer.gwt.dnd.client.HasDragHandle;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import eu.scy.tools.gstyler.client.graph.CSSConstants;
import eu.scy.tools.gstyler.client.graph.edge.EdgeCreationHandle;

/**
 * According to the MVC pattern this class is the graphical representation of a Node. UI logic has to go here, especially updating the view according to the nodes model. Also this view may return a list of EdgeCreationHandles which allow the user to draw edges by clicking on a UI element of this
 * node
 */
public abstract class NodeView<N extends Node<?, ?>> extends VerticalPanel implements HasDragHandle {

    private N node;

    /**
     * Panel in the top containing the titleLabel and the popupLabel
     */
    protected HorizontalPanel topPanel;

    /**
     * Title of the node, also used as DragHandle
     */
    protected Label titleLabel;

    /**
     * Label which being clicked on activates the NodePopupMenu
     */
    protected Label popupLabel;

    /**
     * Creates a new NodeView for the given Node
     */
    public NodeView(N node) {
        super();
        this.node = node;
        topPanel = new HorizontalPanel();
        topPanel.setHorizontalAlignment(ALIGN_CENTER);
        topPanel.setWidth("100%");
    
        popupLabel = new Label("X");
        final NodeView<N> nodeView = this;
        popupLabel.addClickListener(new PopupClickListener(nodeView));
        topPanel.add(popupLabel);
        
        titleLabel = new Label();
        titleLabel.setWidth("100%");
        titleLabel.addStyleName(CSSConstants.CSS_DRAG_HANDLE);
        topPanel.add(titleLabel);
        
        add(topPanel);
    }

    /**
     * @return The node this view is associated with
     */
    public N getNode() {
        return node;
    }

    /**
     * A DragHandle is used to move the nodes via drag and drop during the MOVE_NODES InteractionMode. A DragHandle needs to be a Widget implementing SourcesMouseEvents like FocusPanel, Image or Label
     * 
     * By Default the header label is the DragHandle, overwrite this method to change this behaviour
     */
    public Widget getDragHandle() {
        return titleLabel;
    }

    /**
     * EdgeCreationHandles are used to draw edges via drag and drop from one node to another node during the MOVE_NODES InteractionMode
     * 
     * @return Collection of EdgeCreationHandles which may be empty, but not null
     */
    public abstract Collection<EdgeCreationHandle> getEdgeCreationHandles();

    /**
     * This method needs to update the view according to the current data in its model
     */
    public abstract void updateFromModel();
}
