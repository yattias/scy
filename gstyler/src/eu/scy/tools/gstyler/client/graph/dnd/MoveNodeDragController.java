package eu.scy.tools.gstyler.client.graph.dnd;

import pl.balon.gwt.diagrams.client.connector.UIObjectConnector;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * DragController to be used to move nodes freely around on a graph.
 * Takes care for updating the corresponding edges.
 */
public class MoveNodeDragController extends PickupDragController {

    public MoveNodeDragController(AbsolutePanel boundaryPanel, boolean allowDroppingOnBoundaryPanel) {
        super(boundaryPanel, allowDroppingOnBoundaryPanel);
    }

    /**
     * Updates the display of the edges during movement
     */
    public void dragMove() {
        super.dragMove();
        UIObjectConnector c = UIObjectConnector.getWrapper(context.draggable);
        if (c != null) {
            c.update();
        }
    }

    /**
     * FIXME: No idea why this is needed, but without it connected nodes won't be draggable
     */
    public void makeDraggable(Widget widget) {
        super.makeDraggable(widget);
        DOM.setStyleAttribute(widget.getElement(), "position", "absolute");
        DOM.setStyleAttribute(widget.getElement(), "zIndex", "100");
    }
}
