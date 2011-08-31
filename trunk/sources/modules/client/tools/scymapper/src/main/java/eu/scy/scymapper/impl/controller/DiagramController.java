package eu.scy.scymapper.impl.controller;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import eu.scy.scymapper.api.diagram.controller.IDiagramController;
import eu.scy.scymapper.api.diagram.model.IDiagramModel;
import eu.scy.scymapper.api.diagram.model.IDiagramSelectionModel;
import eu.scy.scymapper.api.diagram.model.ILinkModel;
import eu.scy.scymapper.api.diagram.model.INodeLinkModel;
import eu.scy.scymapper.api.diagram.model.INodeModel;

/**
 * Created by IntelliJ IDEA. User: Bjorge Naess Date: 24.jun.2009 Time: 12:03:12
 */
public class DiagramController implements IDiagramController, KeyEventDispatcher {

    private final static Logger logger = Logger.getLogger(DiagramController.class);

    private boolean isEverythingSelected = false;

    protected IDiagramModel model;

    private IDiagramSelectionModel selectionModel;

    public DiagramController(IDiagramModel diagramModel, IDiagramSelectionModel selectionModel) {
        this.model = diagramModel;
        this.selectionModel = selectionModel;
        KeyboardFocusManager kbfm = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        kbfm.addKeyEventDispatcher(this);
    }

    @Override
    public void setName(String name) {
        model.setName(name);
    }

    @Override
    public void add(INodeModel n, boolean preventOverlap) {
        add(n);
    }

    @Override
    public void add(INodeModel n) {
        model.addNode(n);
    }

    @Override
    public void add(ILinkModel l) {
        model.addLink(l);
    }

    @Override
    public void removeAll() {
        Collection<INodeModel> toBeRemoved = new HashSet<INodeModel>();
        for (INodeModel n : model.getNodes())
            toBeRemoved.add(n);
        for (INodeModel n : toBeRemoved)
            remove(n);
    }

    @Override
    public void remove(INodeModel n) {
        if (!n.getConstraints().getCanDelete()) {
            logger.warn("Tried to delete a locked node");
            return;
        }

        HashSet<INodeLinkModel> linksToRemove = new HashSet<INodeLinkModel>();
        for (ILinkModel link : model.getLinks()) {
            if (link instanceof INodeLinkModel) {
                INodeLinkModel nodeLink = (INodeLinkModel) link;
                if (n.equals(nodeLink.getFromNode()) || n.equals(nodeLink.getToNode())) {
                    linksToRemove.add(nodeLink);
                }
            }
        }
        model.removeNode(n);
        for (ILinkModel link : linksToRemove)
            remove(link);
    }

    public synchronized void remove(ILinkModel l) {
        model.removeLink(l);
    }

    public void toggleSelection() {

        Set<INodeModel> nodes = model.getNodes();
        for (INodeModel iNodeModel : nodes) {
            if (isEverythingSelected) {
                iNodeModel.setSelected(false);
            } else {
                iNodeModel.setSelected(true);
            }
        }
        isEverythingSelected = !isEverythingSelected;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_A) {
            selectEverything();
            return true;
        }
        return false;
    }

    private void selectEverything() {
        Set<INodeModel> nodes = model.getNodes();
        for (INodeModel iNodeModel : nodes) {
            selectionModel.select(iNodeModel);
        }
    }
}
