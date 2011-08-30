package eu.scy.scymapper.impl.ui.diagram;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import org.apache.log4j.Logger;

import eu.scy.scymapper.api.diagram.controller.IDiagramController;
import eu.scy.scymapper.api.diagram.model.IDiagramListener;
import eu.scy.scymapper.api.diagram.model.IDiagramModel;
import eu.scy.scymapper.api.diagram.model.IDiagramSelectionModel;
import eu.scy.scymapper.api.diagram.model.ILinkModel;
import eu.scy.scymapper.api.diagram.model.IModeListener;
import eu.scy.scymapper.api.diagram.model.INodeLinkModel;
import eu.scy.scymapper.api.diagram.model.INodeModel;
import eu.scy.scymapper.api.diagram.view.LinkViewComponent;
import eu.scy.scymapper.api.diagram.view.NodeViewComponent;
import eu.scy.scymapper.impl.controller.DefaultElementControllerFactory;
import eu.scy.scymapper.impl.controller.IElementControllerFactory;
import eu.scy.scymapper.impl.model.ComboNodeLinkModel;
import eu.scy.scymapper.impl.model.ConnectorModel;
import eu.scy.scymapper.impl.model.NodeLinkModel;
import eu.scy.scymapper.impl.ui.Localization;
import eu.scy.scymapper.impl.ui.diagram.modes.ConnectMode;
import eu.scy.scymapper.impl.ui.diagram.modes.DragMode;
import eu.scy.scymapper.impl.ui.diagram.modes.IDiagramMode;
import org.dom4j.util.NodeComparator;

/**
 * Created by IntelliJ IDEA. User: Henrik Date: 23.jan.2009 Time: 06:40:09
 */
public class ConceptDiagramView extends JLayeredPane implements IDiagramListener {

    private final static Logger logger = Logger.getLogger(ConceptDiagramView.class);

    private IDiagramMode mode = new DragMode(this);

    private IDiagramModel model;

    private IElementControllerFactory elementControllerFactory = new DefaultElementControllerFactory();

    private IDiagramController controller;

    private IDiagramSelectionModel selectionModel;

    private KeyListener deleteKeyListener = new DeleteKeyListener();

    private int nodeCount = 0;

    private List<IModeListener> modeListeners;

    public ConceptDiagramView(IDiagramController controller, IDiagramModel model, final IDiagramSelectionModel selectionModel) {
        this.controller = controller;
        this.model = model;
        this.selectionModel = selectionModel;
        modeListeners = new ArrayList<IModeListener>();

        setLayout(null);

        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                selectionModel.clearSelection();
                requestFocus();
            }
        });

        initializeGUI();

        setBackground(Color.white);
        setOpaque(true);

        // Register myself as observer for changes in the model
        this.model.addDiagramListener(this);
        setAutoscrolls(true);
    }

    public void setMode(IDiagramMode mode) {
        if (mode instanceof ConnectMode) {
            synchronized (modeListeners) {
                for (IModeListener modeListener : modeListeners) {
                    modeListener.edgeModeEnabled();
                }
            }
            setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        } else {
            synchronized (modeListeners) {
                for (IModeListener modeListener : modeListeners) {
                    modeListener.nodeModeEnabled();
                }
            }
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

        }
        this.mode = mode;
    }

    public void setElementControllerFactory(IElementControllerFactory factory) {
        elementControllerFactory = factory;
        // Adapt controller for previously created nodes and links
        for (int i = 0; i < getComponentCount(); i++) {
            Component c = getComponent(i);
            if (c instanceof NodeViewComponent) {
                NodeViewComponent nvc = (NodeViewComponent) c;
                nvc.setController(factory.createNodeController(nvc.getModel()));
            } else if (c instanceof LinkViewComponent) {
                LinkViewComponent lvc = (LinkViewComponent) c;
                lvc.setController(factory.createLinkController(lvc.getModel()));
            }
        }
    }

    private void initializeGUI() {

        // Create views for links in my model
        for (ILinkModel link : model.getLinks()) {
            addLinkView(link, false);
        }
        // Create views for nodes in my model
        for (INodeModel node : model.getNodes()) {
            addNodeView(node, false);
        }
    }

    private void addNodeView(INodeModel node, boolean editable) {

        logger.debug("NODE ADDED: " + node);

        RichNodeView view;

        if (node instanceof ConnectorModel) {
            view = new ConnectorView(elementControllerFactory.createNodeController(node), node, this);
        } else {
            view = new RichNodeView(elementControllerFactory.createNodeController(node), node, this);
        }

        view.setLabelEditable(editable, true);

        // Subscribe to mouse events in this nodes component to display the add-link button
        view.addMouseListener(new MouseListenerDelegator());
        view.addMouseMotionListener(new MouseMotionListenerDelegator());

        // I want to listen for mouse events in the component of this node to be able to add new links
        view.addFocusListener(new FocusListenerDelegator());

        view.addKeyListener(deleteKeyListener);
        add(view, 0);
        view.repaint();
        this.nodeCount++;
    }

    private void addLinkView(ILinkModel link, boolean editable) {
        if (link instanceof NodeLinkModel) {

            final ConceptLinkView view = new ConceptLinkView(elementControllerFactory.createLinkController(link), (INodeLinkModel) link);
            view.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (!e.isControlDown())
                        selectionModel.clearSelection();
                    selectionModel.select(view.getModel());
                }
            });
            view.setLabelEditable(editable);

            view.addKeyListener(deleteKeyListener);

            view.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (!e.isControlDown())
                        selectionModel.clearSelection();
                    selectionModel.select(view.getModel());
                }
            });

            add(view);

            view.repaint();
        } else if (link instanceof ComboNodeLinkModel) {
            final ComboConceptLinkView view = new ComboConceptLinkView(elementControllerFactory.createLinkController(link), (ComboNodeLinkModel) link);
            final JComboBox comboBox = view.getComboBox();
            comboBox.addPopupMenuListener(new PopupMenuListener() {

                @Override
                public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                    if (!view.getModel().isSelected()) {
                        selectionModel.clearSelection();
                        selectionModel.select(view.getModel());
                        comboBox.setPopupVisible(true);
                    }
                }

                @Override
                public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                    logger.debug(3);
                }

                @Override
                public void popupMenuCanceled(PopupMenuEvent e) {
                    // TODO Auto-generated method stub

                }
            });

            view.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (!e.isControlDown())
                        selectionModel.clearSelection();
                    selectionModel.select(view.getModel());
                }
            });

            add(view);

            view.repaint();

        }
    }

    public int getNodeCount() {
        return nodeCount;
    }

    public Dimension getPreferredSize() {
        return new Dimension(getComponentsWidth(), getComponentsHeight());
    }

    public int getComponentsWidth() {
        int maxW = getParent() != null ? getParent().getWidth() : 0;
        for (Component component : getComponents()) {
            int compW = component.getX() + component.getWidth();
            if (compW > maxW)
                maxW = compW;
        }
        return maxW;
    }

    public int getComponentsHeight() {
        int maxH = getParent() != null ? getParent().getHeight() : 0;
        for (Component component : getComponents()) {
            int compH = component.getY() + component.getHeight();
            if (compH > maxH)
                maxH = compH;
        }
        return maxH;
    }

    @Override
    public void updated(IDiagramModel diagramModel) {
        logger.debug("ConceptDiagramView.updated");
    }

    @Override
    public void nodeSelected(INodeModel n) {
        logger.debug("ConceptDiagramView.nodeSelected");
    }

    @Override
    public void nodeAdded(INodeModel node, boolean focused) {
        addNodeView(node, focused);
    }

    @Override
    public void linkAdded(ILinkModel link, boolean focused) {
        addLinkView(link, focused);
    }

    @Override
    public void linkRemoved(ILinkModel link) {
        for (Component component : getComponents()) {
            if (component instanceof LinkView) {
                LinkView lw = (LinkView) component;
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
        for (Component component : getComponents()) {
            if (component instanceof RichNodeView) {
                RichNodeView nw = (RichNodeView) component;
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
            ((MouseAdapter) mode.getMouseListener()).mouseDragged(e);
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            ((MouseAdapter) mode.getMouseListener()).mouseMoved(e);
        }
    }

    private class MouseListenerDelegator implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            mode.getMouseListener().mouseClicked(e); // To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        public void mousePressed(MouseEvent e) {
            mode.getMouseListener().mousePressed(e); // To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        public void mouseExited(MouseEvent e) {
            mode.getMouseListener().mouseExited(e); // To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            mode.getMouseListener().mouseEntered(e); // To change body of overridden methods use File | Settings | File Templates.
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

    private class DeleteKeyListener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                confirmAndRemoveSelectedObjects();
            }
        }
    }

    public void confirmAndRemoveSelectedObjects() {
        int answer = JOptionPane.showConfirmDialog(ConceptDiagramView.this, Localization.getString("Dialog.Confirm.Delete.Text"), Localization.getString("Dialog.Confirm.Delete.Title"), JOptionPane.YES_NO_OPTION);
        if (answer == JOptionPane.YES_OPTION) {
            if (selectionModel.hasLinkSelection()) {
                for (ILinkModel link : selectionModel.getSelectedLinks())
                    controller.remove(link);
            }
            if (selectionModel.hasNodeSelection()) {
                for (INodeModel node : selectionModel.getSelectedNodes())
                    controller.remove(node);
            }
        }
    }

    public void confirmAndRemoveAll() {
        int answer = JOptionPane.showConfirmDialog(ConceptDiagramView.this, Localization.getString("Dialog.Confirm.Clear.Text"), Localization.getString("Dialog.Confirm.Clear.Title"), JOptionPane.YES_NO_OPTION);
        if (answer == JOptionPane.YES_OPTION) {
            controller.removeAll();
        }

    }

    public void addModeListener(IModeListener modeListener){
        synchronized (modeListeners) {
            modeListeners.add(modeListener);
        }
    }
    public void removeModeListener(IModeListener modeListener){
        synchronized (modeListeners) {
            modeListeners.remove(modeListener);
        }
    }
}
