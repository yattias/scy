package eu.scy.scymapper.impl.ui.diagram.modes;

import eu.scy.scymapper.api.diagram.model.ILinkModel;
import eu.scy.scymapper.api.diagram.model.INodeModel;
import eu.scy.scymapper.impl.model.NodeLinkModel;
import eu.scy.scymapper.impl.ui.diagram.ConceptDiagramView;
import eu.scy.scymapper.impl.ui.diagram.LinkView;
import eu.scy.scymapper.impl.ui.diagram.RichNodeView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by IntelliJ IDEA.
 * User: Bjoerge
 * Date: 03.nov.2009
 * Time: 15:10:11
 * To change this template use File | Settings | File Templates.
 */
public class ConnectMode implements IDiagramMode {

    private ConceptDiagramView view;
    LinkView connector = null;

    public ConnectMode(ConceptDiagramView view, LinkView connector) {
        this.view = view;
        this.connector = connector;
        this.view.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        view.add(connector);
        view.setComponentZOrder(connector, 0);
    }

    private INodeModel sourceNode;
    private final MouseListener mouseListener = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            RichNodeView comp = (RichNodeView) e.getSource();
            sourceNode = comp.getModel();
            comp.setBorder(BorderFactory.createLineBorder(Color.green, 1));
            Point relPoint = e.getPoint();
            Point loc = new Point(sourceNode.getLocation());
            loc.translate(relPoint.x, relPoint.y);
            connector.setFrom(loc);
            connector.setTo(loc);
            connector.setVisible(true);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            connector.setVisible(false);
            RichNodeView node = (RichNodeView) e.getSource();
            node.setBorder(BorderFactory.createEmptyBorder());

            if (currentTarget != null) {
                NodeLinkModel link = new NodeLinkModel(sourceNode, currentTarget);
                ILinkModel connectorLink = connector.getModel();
                link.setLabel(connectorLink.getLabel());
                link.setShape(connectorLink.getShape());
                link.setStyle(connectorLink.getStyle());
                view.getController().addLink(link);
                view.remove(connector);
                view.setMode(new DragMode(view));
                node.setBorder(BorderFactory.createEmptyBorder());
                getNodeViewForModel(currentTarget).setBorder(BorderFactory.createEmptyBorder());
            }
        }
    };
    private INodeModel currentTarget;
    private final MouseMotionListener mouseMotionListener = new MouseMotionAdapter() {
        @Override
        public void mouseDragged(MouseEvent e) {

            // The relative mouse position from the component x,y
            Point relPoint = e.getPoint();

            RichNodeView node = (RichNodeView) e.getSource();

            // Create the new location
            Point newLocation = node.getLocation();
            // Translate the newLocation with the relative point
            //newLocation.translate(relPoint.x, relPoint.y);
            newLocation.translate(relPoint.x, relPoint.y);
            connector.setTo(newLocation);

            INodeModel nodeAt = view.getModel().getNodeAt(newLocation);

            if (nodeAt != null && !nodeAt.equals(sourceNode)) {
                currentTarget = nodeAt;
                // Get the component for target node in order to paint its border
                getNodeViewForModel(currentTarget).setBorder(BorderFactory.createLineBorder(Color.green, 1));

                Point snap = currentTarget.getConnectionPoint(sourceNode.getCenterLocation());
                //targetSnap.translate(relCenter.x, relCenter.y);
                connector.setTo(snap);
                connector.setFrom(sourceNode.getConnectionPoint(snap));
            }
            else if (currentTarget != null) {
                getNodeViewForModel(currentTarget).setBorder(BorderFactory.createEmptyBorder());
                connector.setTo(newLocation);
            }
            else connector.setTo(newLocation);
            if (nodeAt == null) currentTarget = null;
        }
    };

    private RichNodeView getNodeViewForModel(INodeModel node) {
        for (Component c : view.getComponents()) {
            if (c instanceof RichNodeView && ((RichNodeView) c).getModel().equals(node)) {
                return (RichNodeView) c;
            }
        }
        return null;
    }
    @Override
    public MouseListener getMouseListener() {
        return mouseListener;
    }

    @Override
    public MouseMotionListener getMouseMotionListener() {
        return mouseMotionListener;
    }

    @Override
    public FocusListener getFocusListener() {
        return new FocusAdapter() {
        };
    }
}
