package eu.scy.scymapper.impl.ui.diagr;

import eu.scy.scymapper.api.diagram.*;
import eu.scy.scymapper.impl.controller.LinkController;
import eu.scy.scymapper.impl.controller.NodeController;
import eu.scy.scymapper.impl.ui.diagr.modes.DragMode;
import eu.scy.scymapper.impl.ui.diagr.modes.IDiagramMode;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 23.jan.2009
 * Time: 06:40:09
 * To change this template use File | Settings | File Templates.
 */
public class ConceptDiagramView extends JPanel implements IDiagramListener {


    private IDiagramMode mode = new DragMode(this);

    private IDiagramModel model;
    private IDiagramController controller;

	private IDiagramSelectionModel selectionModel;

    private final static Logger logger = Logger.getLogger(ConceptDiagramView.class);

	public ConceptDiagramView(IDiagramController controller, IDiagramModel model, final IDiagramSelectionModel selectionModel) {
        this.controller = controller;
        this.model = model;
		this.selectionModel = selectionModel;

		// Register myself as observer for changes in the model
        this.model.addDiagramListener(this);

        setLayout(null);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectionModel.clearSelection();
                requestFocus();
            }
        });

        initializeGUI();
		setAutoscrolls(true);
    }

    public void setMode(IDiagramMode mode) {
        this.mode = mode;
    }
	private void initializeGUI() {

        // Create views for links in my model
        for (ILinkModel link : model.getLinks()) {
            addLink(link);
        }
        // Create views for nodes in my model
        for (INodeModel node : model.getNodes()) {
            addNode(node);
        }
    }
	private void addNode(INodeModel node) {
        NodeView view = new NodeView(new NodeController(node), node);

        // Subscribe to mouse events in this nodes component to display the add-link button
        view.addMouseListener(new MouseListenerDelegator());
        view.addMouseMotionListener(new MouseMotionListenerDelegator());

        // I want to listen for mouse events in the component of this node to be able to add new links
        view.addFocusListener(new FocusListenerDelegator());

        add(view);
        repaint(view.getBounds());
    }

    private void addLink(ILinkModel link) {
        if (link instanceof INodeLinkModel) {
            ConceptLinkView view = new ConceptLinkView(new LinkController(link), (INodeLinkModel)link);
            add(view);
            view.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // TODO: Improve this
                    requestFocus();
                    Point p = new Point(e.getPoint());
                    p.translate(e.getComponent().getX(), e.getComponent().getY());

                    p.translate(ConceptDiagramView.this.getX(), ConceptDiagramView.this.getY());

                    System.out.println("p = " + p);
                    LinkView linkView = getNearestLink(e.getPoint());
                    System.out.println("linkView = " + linkView);

                    double dist = getDistanceToLine(linkView.getModel(), p);
                    System.out.println("dist = " + dist);
                    //linkView.setBorder(BorderFactory.createLineBorder(Color.red, 1));
//                    System.out.println("point = " + e.getPoint());
//                    System.out.println("linkView = " + linkView);
//                    System.out.println("getDistanceToLine(linkView, containerRelative) = " + getDistanceToLine(linkView.getModel(), e.getPoint()));
//                    if (getDistanceToLine(linkView.getModel(), e.getPoint()) < 10) {
//                        if (!e.isControlDown()) getSelectionModel().clearSelection();
//                        getSelectionModel().select(linkView.getModel());
//                    }
                }
                double getDistanceToLine(ILinkModel model, Point p) {
                    Point2D from = new Point2D.Double();
                    from.setLocation(model.getFrom());

                    Point2D to = new Point2D.Double();
                    to.setLocation(model.getTo());

                    return new Line2D.Double(from, to).ptLineDist(p.x, p.y);
                }
                LinkView getNearestLink(Point p) {
                    double shortestDist = -1;
                    LinkView nearest = null;
                    for (Component comp : ConceptDiagramView.this.getComponents()) {
                        if (!(comp instanceof LinkView)) continue;
                        LinkView link = (LinkView) comp;
                        double dist = getDistanceToLine(link.getModel(), p);
                        if (shortestDist == -1 || dist < shortestDist) {
                            shortestDist = dist;
                            nearest = link;
                        }
                    }
                    return nearest;
                }
            });
            repaint(view.getBounds());
        }
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
    public void nodeSelected(INodeModel n) {
        System.out.println("ConceptDiagramView.nodeSelected");
    }

    @Override
    public void nodeAdded(INodeModel node) {
        addNode(node);
    }

    @Override
    public void linkAdded(ILinkModel link) {
        addLink(link);
    }

    @Override
    public void linkRemoved(ILinkModel link) {
        for (Component component : getComponents()) {
			if (component instanceof ConceptLinkView) {
				ConceptLinkView lw = (ConceptLinkView) component;
				if (lw.getModel().equals(link)) {
					remove(lw);
					repaint();
					return;
				}
			}
		}
    }

    @Override
    public void nodeRemoved(INodeModel n) {
		System.out.println("ConceptDiagramView.nodeRemoved: "+n);
        for (Component component : getComponents()) {
			if (component instanceof NodeView) {
				NodeView nw = (NodeView) component;
				if (nw.getModel().equals(n)) {
					remove(nw);
					repaint();
					return;
				}
			}
		}
    }

    public IDiagramModel getModel() {
        return model;
    }

    public IDiagramSelectionModel getSelectionModel() {
        return selectionModel;
    }

    private class MouseMotionListenerDelegator implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e) {
            mode.getMouseMotionListener().mouseDragged(e);
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            mode.getMouseMotionListener().mouseMoved(e);
        }
    }
    private class MouseListenerDelegator implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            mode.getMouseListener().mouseClicked(e);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        public void mousePressed(MouseEvent e) {
            mode.getMouseListener().mousePressed(e);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        public void mouseExited(MouseEvent e) {
            mode.getMouseListener().mouseExited(e);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            mode.getMouseListener().mouseEntered(e);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            mode.getMouseListener().mouseReleased(e);
        }
    }
    private class FocusListenerDelegator implements FocusListener {
        @Override
        public void focusGained(FocusEvent e) {
            System.out.println("ConceptDiagramView$FocusListenerDelegator.focusGained");
            mode.getFocusListener().focusGained(e);
        }
        @Override
        public void focusLost(FocusEvent e) {
            System.out.println("ConceptDiagramView$FocusListenerDelegator.focusLost");
            mode.getFocusListener().focusLost(e);
        }
    }
}
