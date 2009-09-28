package eu.scy.scymapper.impl.component;

import eu.scy.scymapper.api.IConceptLinkModel;
import eu.scy.scymapper.api.diagram.*;
import eu.scy.scymapper.impl.controller.LinkConnectorController;
import eu.scy.scymapper.impl.controller.LinkController;
import eu.scy.scymapper.impl.controller.NodeController;
import eu.scy.scymapper.impl.model.NodeLinkModel;
import eu.scy.scymapper.impl.model.SimpleLink;
import eu.scy.scymapper.impl.shapes.links.Arrow;
import eu.scy.scymapper.impl.shapes.links.Arrowhead;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 23.jan.2009
 * Time: 06:40:09
 * To change this template use File | Settings | File Templates.
 */
public class ConceptDiagramView extends JPanel implements IDiagramModelListener, INodeModelListener {

    private IDiagramModel model;
    private IDiagramController controller;

	private IDiagramSelectionModel selectionModel;

    private static final String CONNECTOR_FILENAME = "add_connector.png";
    private NodeMouseMotionListener nodeMouseMotionListener;
	private SelectionMouseListener selectionMouseListener;

	public ConceptDiagramView(IDiagramController controller, IDiagramModel model, IDiagramSelectionModel selectionModel) {
        this.controller = controller;
        this.model = model;
		this.selectionModel = selectionModel;

		// Register myself as observer for changes in the model
        this.model.addObserver(this);

        // Create the listener for node mouseover
        nodeMouseMotionListener = new NodeMouseMotionListener();
        selectionMouseListener = new SelectionMouseListener();

		addMouseListener(selectionMouseListener);

        setLayout(null);

        initializeGUI();
		setAutoscrolls(true);
    }

	private void initializeGUI() {

        // Create views for links in my model
        for (ILinkModel link : model.getLinks()) {
            addLink((IConceptLinkModel)link);
        }
        // Create views for nodes in my model
        for (INodeModel node : model.getNodes()) {
            addNode(node);
        }
    }
	private void addNode(INodeModel node) {
        NodeView view = new NodeView(new NodeController(node), node);

        // Subscribe to mouse events in this nodes component to display the add-link button
        view.addMouseMotionListener(nodeMouseMotionListener);

        view.addMouseListener(selectionMouseListener);

        // I want to listen for mouseover in the component of this node to be able to add new links
        view.addFocusListener(new NodeFocusListener());

        // Subscribe to changes in this node
        node.addObserver(this);
        add(view);
        repaint(view.getBounds());
    }

    private void addLink(IConceptLinkModel link) {
        ConceptLinkView view = new ConceptLinkView(new LinkController(link), link);
        add(view);
    }

	public Dimension getPreferredSize() {
		return new Dimension(getComponentsWidth(), getComponentsHeight());
	}
	public int getComponentsWidth() {
		int maxW = getParent() != null ? getParent().getWidth() : 0;
		for (Component component : getComponents()) {
			int compW = component.getX()+component.getWidth();
			if (compW > maxW) maxW = compW;
		}
		return maxW;
	}
	public int getComponentsHeight() {
		int maxH = getParent() != null ? getParent().getHeight() : 0;
		for (Component component : getComponents()) {
			int compH = component.getY()+component.getHeight();
			if (compH > maxH) maxH = compH;
		}
		return maxH;
	}
    @Override
    public void updated(IDiagramModel diagramModel) {
        System.out.println("ConceptDiagramView.updated");
    }

    @Override
    public void nodeAdded(INodeModel n) {
        addNode(n);
    }

    @Override
    public void linkAdded(ILinkModel link) {
        addLink((IConceptLinkModel)link);
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
		revalidate();
    }

    @Override
    public void resized(INodeModel node) {
		revalidate();
    }
	@Override
    public void labelChanged(INodeModel node) {
        System.out.println("NodeModel label changed: "+node);
    }

    @Override
    public void styleChanged(INodeModel node) {
        System.out.println("ConceptDiagramView.styleChanged");
    }

    @Override
    public void shapeChanged(INodeModel node) {
        System.out.println("ConceptDiagramView.shapeChanged");
    }

    @Override
    public void nodeSelected(INodeModel conceptNode) {
		
    }

	private class SelectionMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			Component comp = e.getComponent();
			if (!e.isControlDown()) selectionModel.clearSelection();
			if (comp instanceof NodeView) {
				INodeModel node = ((NodeView)comp).getModel();
				if (e.isControlDown() && node.isSelected())
					selectionModel.unselect(node);
				else
					selectionModel.select(node);
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
	private class NodeMouseMotionListener implements MouseMotionListener {
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

                targetNode.setConnectionCandidate(true);

                currentTarget = targetNode;

            } else if (currentTarget != null) {
                currentTarget.setConnectionCandidate(false);
                currentTarget = null;
            }
            else {
                link.setTo(location);
            }
        }

        private LinkView getTemplink() {
            if (tempLink == null) {
				Arrow arrow = new Arrow();
				arrow.setArrowhead(new Arrowhead());
				ILinkModel linkModel = new SimpleLink(arrow);
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

                IConceptLinkModel newLink = new NodeLinkModel(source.getModel(), currentTarget.getModel());
				Arrow arrow = new Arrow();
				arrow.setArrowhead(new Arrowhead());
				newLink.setShape(arrow);
                controller.addLink(newLink);

                currentTarget.setConnectionCandidate(false);
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

			Component comp = e.getComponent();
            setComponentZOrder(comp, 0);
            repaint();
        }

        @Override
        public void focusLost(FocusEvent e) {

        }
    }
}
