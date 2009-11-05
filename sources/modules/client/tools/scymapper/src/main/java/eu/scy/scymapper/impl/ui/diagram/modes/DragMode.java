package eu.scy.scymapper.impl.ui.diagram.modes;

import eu.scy.scymapper.impl.ui.diagram.ConceptDiagramView;
import eu.scy.scymapper.impl.ui.diagram.NodeView;
import eu.scy.scymapper.impl.ui.diagram.modes.IDiagramMode;

import java.awt.*;
import java.awt.event.*;

/**
 * Created by IntelliJ IDEA.
 * User: Bjoerge
 * Date: 03.nov.2009
 * Time: 15:10:18
 */
public class DragMode implements IDiagramMode {
    private ConceptDiagramView view;

    private Point relativePos = null;

    private final MouseListener mouseListener = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            relativePos = e.getPoint();
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            Component com = e.getComponent();
            com.getParent().setComponentZOrder(com, 0);
            if (!e.isControlDown()) view.getSelectionModel().clearSelection();
            view.getSelectionModel().select(((NodeView) com).getModel());
        }
    };
    private final MouseMotionListener mouseMotionListener = new MouseMotionAdapter() {
        @Override
        public void mouseDragged(MouseEvent e) {

            // The relative mouse position from the component x,y
            Point relPoint = e.getPoint();

            NodeView view = (NodeView) e.getSource();

            // Create the new location
            Point newLocation = view.getLocation();
            // Translate the newLocation with the relative point
            newLocation.translate(relPoint.x, relPoint.y);
            newLocation.translate(-relativePos.x, -relativePos.y);

            view.getModel().setLocation(newLocation);
        }
    };

    private FocusListener focusHandler = new FocusAdapter() {};

    public DragMode(ConceptDiagramView view) {
        this.view = view;
        this.view.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    @Override
    public MouseListener getMouseListener() {
        return mouseListener;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public MouseMotionListener getMouseMotionListener() {
        return mouseMotionListener;
    }

    @Override
    public FocusListener getFocusListener() {
        return focusHandler;
    }

}
