package eu.scy.scymapper.impl.ui.diagram.modes;

import eu.scy.scymapper.api.ILinkFactory;
import eu.scy.scymapper.api.ILinkFactory.Type;
import eu.scy.scymapper.api.diagram.model.ILinkModel;
import eu.scy.scymapper.api.diagram.model.INodeLinkModel;
import eu.scy.scymapper.api.diagram.model.INodeModel;
import eu.scy.scymapper.api.diagram.view.NodeViewComponent;
import eu.scy.scymapper.impl.configuration.SCYMapperStandaloneConfig;
import eu.scy.scymapper.impl.model.ComboNodeLinkModel;
import eu.scy.scymapper.impl.model.NodeLinkModel;
import eu.scy.scymapper.impl.ui.diagram.ConceptDiagramView;
import eu.scy.scymapper.impl.ui.diagram.LinkView;
import eu.scy.scymapper.impl.ui.diagram.RichNodeView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA. User: Bjoerge Date: 03.nov.2009 Time: 15:10:11 To change this template use File | Settings | File Templates.
 */
public class ConnectMode implements IDiagramMode {

    public static final int CONNECTION_MADE = 1;

    public static final int CONNECTION_CANCELLED = 2;

    private ConceptDiagramView view;

    LinkView connector = null;

    private INodeModel currentTarget;

    private INodeModel sourceNode;

    Collection<ActionListener> listeners = new ArrayList<ActionListener>();

    private RichNodeView targetComponent;

    private RichNodeView sourceComponent;

    private RichNodeView currentTargetComponent;

    private ILinkFactory factory;

    public JComponent getSourceComponent() {
        return sourceComponent;
    }

    public JComponent getTargetComponent() {
        return targetComponent;
    }

    public void addActionListener(ActionListener l) {
        listeners.add(l);
    }

    public void removeActionListener(ActionListener l) {
        listeners.remove(l);
    }

    public ConnectMode(ConceptDiagramView view, LinkView connector, ILinkFactory linkFactory) {
        this.view = view;
        this.connector = connector;
        this.factory = linkFactory;
        view.add(connector);
        view.setComponentZOrder(connector, 0);
    }

    private final MouseAdapter mouseListener = new MouseAdapter() {

        private RichNodeView getSourceComponent(MouseEvent e){
            if (e.getSource() instanceof RichNodeView){
                return (RichNodeView) e.getSource();

            }else{
                JTextPane jtp =(JTextPane)e.getSource();
                return (RichNodeView)jtp.getParent();

            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (e.getComponent() instanceof NodeViewComponent) {
                e.getComponent().setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
           sourceComponent= getSourceComponent(e);
            sourceNode = sourceComponent.getModel();
            sourceComponent.setBorder(BorderFactory.createLineBorder(Color.green, 1));
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
            sourceComponent= getSourceComponent(e);
            sourceComponent.setBorder(BorderFactory.createEmptyBorder());

            if (currentTarget != null) {
                INodeLinkModel link = null;
                if (factory.getType() == Type.SCY) {
                    link = new NodeLinkModel(sourceNode, currentTarget);
                } else if (factory.getType() == Type.COLLIDE) {
                    link = new ComboNodeLinkModel(sourceNode, currentTarget, SCYMapperStandaloneConfig.getInstance().getRelations());
                }
                ILinkModel connectorLink = connector.getModel();
                link.setLabel(connectorLink.getLabel());
                link.setShape(connectorLink.getShape());
                link.setStyle(connectorLink.getStyle());
                view.getController().add(link);
                view.remove(connector);
                sourceComponent.setBorder(BorderFactory.createEmptyBorder());
                targetComponent = currentTargetComponent;
                getNodeViewForModel(currentTarget).setBorder(BorderFactory.createEmptyBorder());
                ActionEvent aEvt = new ActionEvent(sourceComponent, CONNECTION_MADE, "CONNECTION_MADE");
                for (ActionListener listener : listeners) {
                    listener.actionPerformed(aEvt);
                }
            } else {
                ActionEvent aEvt = new ActionEvent(sourceComponent, CONNECTION_CANCELLED, "CONNECTION_CANCELLED");
                for (ActionListener listener : listeners) {
                    listener.actionPerformed(aEvt);
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {

            // The relative mouse position from the component x,y
            Point relPoint = e.getPoint();

            JComponent component = getSourceComponent(e);

            // Create the new location
            Point newLocation = component.getLocation();
            // Translate the newLocation with the relative point
            // newLocation.translate(relPoint.x, relPoint.y);
            newLocation.translate(relPoint.x, relPoint.y);
            connector.setTo(newLocation);

            INodeModel nodeAt = view.getModel().getNodeAt(newLocation);

            if (nodeAt != null && !nodeAt.equals(sourceNode)) {
                currentTarget = nodeAt;
                // Get the component for target node in order to paint its
                // border
                currentTargetComponent = getNodeViewForModel(currentTarget);
                currentTargetComponent.setBorder(BorderFactory.createLineBorder(Color.green, 1));

                Point snap = currentTarget.getConnectionPoint(sourceNode.getCenterLocation());
                // targetSnap.translate(relCenter.x, relCenter.y);
                connector.setTo(snap);
                connector.setFrom(sourceNode.getConnectionPoint(snap));
            } else if (currentTarget != null) {
                getNodeViewForModel(currentTarget).setBorder(BorderFactory.createEmptyBorder());
                connector.setTo(newLocation);
            } else
                connector.setTo(newLocation);
            if (nodeAt == null)
                currentTarget = null;
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
        return mouseListener;
    }

    @Override
    public FocusListener getFocusListener() {
        return new FocusAdapter() {};
    }
}
