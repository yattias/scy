package eu.scy.scyplanner.impl.diagram;

import eu.scy.scymapper.api.diagram.*;
import eu.scy.scymapper.impl.controller.LinkConnectorController;
import eu.scy.scymapper.impl.controller.LinkController;
import eu.scy.scymapper.impl.controller.NodeController;
import eu.scy.scymapper.impl.model.NodeLinkModel;
import eu.scy.scymapper.impl.model.NodeModel;
import eu.scy.scymapper.impl.model.SimpleLink;
import eu.scy.scymapper.impl.shapes.links.Arrow;
import eu.scy.scymapper.impl.ui.diagram.ConceptLinkView;
import eu.scy.scymapper.impl.ui.diagram.ConnectionPoint;
import eu.scy.scymapper.impl.ui.diagram.LinkView;
import eu.scy.scymapper.impl.ui.diagram.NodeView;

import eu.scy.scyplanner.application.SCYPlannerApplicationManager;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

/**
 * User: Bjoerge Naess
 * Date: 28.aug.2009
 * Time: 11:40:29
 */
public class SCYPlannerDiagramView extends JPanel implements INodeModelListener, IDiagramListener {
	private IDiagramModel model;
    private IDiagramController controller;

    private static final String CONNECTOR_FILENAME = "add_connector.png";
    private NodeMouseListener nodeMouseListener;

    private ObservableView observable = new ObservableView();

    public SCYPlannerDiagramView(IDiagramController controller, IDiagramModel model) {        
        this.controller = controller;
        this.model = model;

        setBackground(SCYPlannerApplicationManager.getAlternativeBackgroundColor());

        // Register myself as observer for changes in the model
        this.model.addDiagramListener(this);

        // Create the listener for node mouseover
        nodeMouseListener = new NodeMouseListener();

        setLayout(null);

        initializeGUI();
    }

    public void addObserver(Observer observer) {
        observable.addObserver(observer);
    }

    private void initializeGUI() {

        // Create views for links in my model
        for (ILinkModel link : model.getLinks()) {
            addLink((INodeLinkModel)link);
        }
        // Create views for nodes in my model
        for (INodeModel node : model.getNodes()) {
            addNode(node);
        }

    }

    private void addNode(INodeModel node) {
        NodeView view = new NodeView(new NodeController(node), node);

        // Subscribe to mouse events in this nodes component to display the add-link button
        view.addMouseMotionListener(nodeMouseListener);
        view.addMouseListener(nodeMouseListener);

        // I want to listen for mouseover in the component of this node to be able to add new links
        view.addFocusListener(new NodeFocusListener());

        // Subscribe to changes in this node
        node.addListener(this);

        add(view);
        repaint(view.getBounds());
    }

    private void addLink(INodeLinkModel link) {
        ConceptLinkView view = new ConceptLinkView(new LinkController(link), link);
        add(view);
    }

    @Override
    public void updated(IDiagramModel diagramModel) {
        System.out.println("ConceptDiagramView.updated");
    }

    @Override
    public void nodeSelected(INodeModel n) {
        System.out.println("SCYPlannerDiagramView.nodeSelected");
    }

    @Override
    public void selectionChanged(INodeModel n) {
        System.out.println("ConceptDiagramView.selectionChanged");
    }

    @Override
    public void deleted(NodeModel nodeModel) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void nodeAdded(INodeModel n) {
        addNode(n);
    }

    @Override
    public void linkAdded(ILinkModel link) {
        addLink((INodeLinkModel)link);
    }

    @Override
    public void linkRemoved(ILinkModel link) {
        System.out.println("ConceptDiagramView.linkRemoved");
    }

    @Override
    public void nodeRemoved(INodeModel n) {
        System.out.println("ConceptDiagramView.nodeRemoved");
    }

    @Override
    public void moved(INodeModel node) {

    }

    @Override
    public void resized(INodeModel node) {

    }

    @Override
    public void labelChanged(INodeModel node) {
        System.out.println("NodeModel label changed: "+node);
    }

    @Override
    public void shapeChanged(INodeModel node) {
        System.out.println("ConceptDiagramView.shapeChanged");
    }

    private class ComponentFocusListener implements FocusListener {
        private ComponentFocusListener() {
        }

        @Override
        public void focusGained(FocusEvent e) {
            setComponentZOrder(e.getComponent(), 0);
        }

        @Override
        public void focusLost(FocusEvent e) {

        }
    }

    private class NodeMouseListener implements MouseMotionListener, MouseListener {
        Component connectSymbol = null;
        private ConnectorButtonListener connectorButtonListener;

        @Override
        public void mouseDragged(MouseEvent e) {
            Component c = e.getComponent();
            if (c instanceof NodeView) {
                NodeView node = (NodeView) e.getComponent();
                enableConnectorButton(node);
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            Component c = e.getComponent();
            if (c instanceof NodeView) {
                NodeView node = (NodeView) c;
                //Point location = e.getPoint();
                //location.translate(node.getX(), node.getY());
                //TODO: Fix this: enableConnectorButton(node, node.getConnectionPoint(location));
                enableConnectorButton(node);
            }
        }

        /**
         * Display the new connector button for a concept node
         *
         * @param node
         */
        private void enableConnectorButton(NodeView node) {

            Component connectSymbol = getConnectSymbol();

            // Display the connection point at the topmost corner of the shaped node
            Point connectionPoint = node.getConnectionPoint(node.getLocation());

            connectionPoint.translate(-connectSymbol.getWidth(), -connectSymbol.getHeight());

            enableConnectorButton(node, connectionPoint);

        }
        /**
         * Display the new connector button for a concept node
         *
         * @param node
         */
        private void enableConnectorButton(NodeView node, Point p) {

            Component connectSymbol = getConnectSymbol();
            connectSymbol.setLocation(p);

            ConnectorButtonListener listener = getConnectorButtonListener();
            listener.setSource(node);

            connectSymbol.setVisible(true);
            setComponentZOrder(connectSymbol, 0);

        }

        private NodeView getNearestNode(Point p) {
            double closestDistance = Double.MAX_VALUE;
            NodeView foundNode = null;
            for (Component c : getComponents()) {
                if (!(c instanceof NodeView)) continue;
                NodeView node = (NodeView) c;
                Point connectionPoint = node.getConnectionPoint(p);
                double this_dist = connectionPoint.distance(p.x, p.y);
                if (this_dist < closestDistance) {
                    closestDistance = this_dist;
                    foundNode = node;
                }
            }
            return foundNode;
        }

        private ConnectorButtonListener getConnectorButtonListener() {
            if (connectorButtonListener == null) {
                connectorButtonListener = new ConnectorButtonListener();
            }
            return connectorButtonListener;
        }

        private Component getConnectSymbol() {
            if (connectSymbol == null) {
                try {
                    URL uri = getClass().getResource(CONNECTOR_FILENAME);
                    if (uri == null) throw new FileNotFoundException("File " + CONNECTOR_FILENAME + " not found");
                    BufferedImage i = ImageIO.read(uri);
                    connectSymbol = new ConnectionPoint(i);
                    connectSymbol.setSize(i.getWidth(), i.getHeight());
                } catch (IOException e) {
                    JButton button = new JButton("+");
                    Font f = new Font("Serif", Font.PLAIN, 10);
                    button.setFont(f);
                    button.setSize(20, 20);
                    button.setMargin(new Insets(1, 1, 1, 1));
                    connectSymbol = button;
                }
                ConnectorButtonListener l = getConnectorButtonListener();
                connectSymbol.addMouseListener(l);
                connectSymbol.addMouseMotionListener(l);
                connectSymbol.setVisible(false);
                add(connectSymbol);
            }
            return connectSymbol;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            Component c = e.getComponent();
            if (c instanceof NodeView) {
                if (e.getClickCount() >= 2) {
                    NodeView node = (NodeView) c;
                    observable.observableChanged();
                    observable.notifyObservers(node.getModel());
                    observable.clearObservableChanged();
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    private class ConnectorButtonListener implements MouseMotionListener, MouseListener {
        private NodeView source;
        private NodeView currentTarget;
        private LinkView tempLink;

        @Override
        public void mouseDragged(MouseEvent e) {

            Component button = e.getComponent();
            Point location = new Point(button.getLocation());
            location.translate(e.getX(), e.getY());

            LinkView link = getTemplink();

            Point from = source.getConnectionPoint(location);

            link.setFrom(from);

            button.setVisible(false);
            link.setVisible(true);

            Component c = getComponentAt(location);
            if (c != null && c instanceof NodeView && !c.equals(source)) {

                NodeView targetNode = (NodeView) c;

                link.setTo(targetNode.getConnectionPoint(from));

                currentTarget = targetNode;

            } else if (currentTarget != null) {
                currentTarget = null;
            }
            else {
                link.setTo(location);
            }
        }

        private LinkView getTemplink() {
            if (tempLink == null) {
                ILinkModel linkModel = new SimpleLink(new Arrow());
                tempLink = new LinkView(new LinkConnectorController(linkModel), linkModel);
                add(tempLink);
            }
            return tempLink;
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (currentTarget != null) {

                INodeLinkModel newLink = new NodeLinkModel(source.getModel(), currentTarget.getModel());
                newLink.setShape(new Arrow());
                controller.addLink(newLink);

                currentTarget = null;
            }
            getTemplink().setVisible(false);
            repaint();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void mouseExited(MouseEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void setSource(NodeView source) {
            this.source = source;
        }
    }

    private class NodeFocusListener implements FocusListener {
        @Override
        public void focusGained(FocusEvent e) {
            setComponentZOrder(e.getComponent(), 0);
            repaint();
        }

        @Override
        public void focusLost(FocusEvent e) {

        }
    }

    private class ObservableView extends Observable {
        private void observableChanged() {
            setChanged();
        }

        private void clearObservableChanged() {
            clearChanged();
        }
    }
}
