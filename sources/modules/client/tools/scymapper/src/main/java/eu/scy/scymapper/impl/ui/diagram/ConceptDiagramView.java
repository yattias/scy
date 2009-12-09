package eu.scy.scymapper.impl.ui.diagram;

import eu.scy.scymapper.api.diagram.*;
import eu.scy.scymapper.impl.controller.DefaultElementControllerFactory;
import eu.scy.scymapper.impl.controller.IElementControllerFactory;
import eu.scy.scymapper.impl.ui.diagram.modes.DragMode;
import eu.scy.scymapper.impl.ui.diagram.modes.IDiagramMode;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 23.jan.2009
 * Time: 06:40:09
 */
public class ConceptDiagramView extends JLayeredPane implements IDiagramListener {

    private IDiagramMode mode = new DragMode(this);

    private IDiagramModel model;
    private IElementControllerFactory elementControllerFactory = new DefaultElementControllerFactory();
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

	public void setElementControllerFactory(IElementControllerFactory factory) {
		elementControllerFactory = factory;
	}

    private void initializeGUI() {

        // Create views for links in my model
        for (ILinkModel link : model.getLinks()) {
            addLinkView(link);
        }
        // Create views for nodes in my model
        for (INodeModel node : model.getNodes()) {
            addNodeView(node);
        }
    }

    private void addNodeView(INodeModel node) {
        NodeView view = new NodeView(elementControllerFactory.createNodeController(node), node);

        // Subscribe to mouse events in this nodes component to display the add-link button
        view.addMouseListener(new MouseListenerDelegator());
        view.addMouseMotionListener(new MouseMotionListenerDelegator());

        // I want to listen for mouse events in the component of this node to be able to add new links
        view.addFocusListener(new FocusListenerDelegator());

        add(view);

        view.setLabelEditable(true);

        view.repaint();
    }

    private void addLinkView(ILinkModel link) {
        if (link instanceof INodeLinkModel) {

            final ConceptLinkView view = new ConceptLinkView(elementControllerFactory.createLinkController(link), (INodeLinkModel) link);
            view.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (!e.isControlDown()) selectionModel.clearSelection();
                    selectionModel.select(view.getModel());
                }
            });
            add(view);
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(getComponentsWidth(), getComponentsHeight());
    }

    public int getComponentsWidth() {
        int maxW = getParent() != null ? getParent().getWidth() : 0;
        for (Component component : getComponents()) {
            int compW = component.getX() + component.getWidth();
            if (compW > maxW) maxW = compW;
        }
        return maxW;
    }

    public int getComponentsHeight() {
        int maxH = getParent() != null ? getParent().getHeight() : 0;
        for (Component component : getComponents()) {
            int compH = component.getY() + component.getHeight();
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
        addNodeView(node);
    }

    @Override
    public void linkAdded(ILinkModel link) {
        addLinkView(link);
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
        System.out.println("ConceptDiagramView.nodeRemoved: " + n);
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

    public IDiagramController getController() {
        return controller;
    }
    public void setController(IDiagramController controller) {
        this.controller = controller;
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
            mode.getFocusListener().focusGained(e);
        }

        @Override
        public void focusLost(FocusEvent e) {
            mode.getFocusListener().focusLost(e);
        }
    }
}
