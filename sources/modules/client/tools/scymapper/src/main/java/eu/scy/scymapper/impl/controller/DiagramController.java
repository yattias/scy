package eu.scy.scymapper.impl.controller;

import eu.scy.scymapper.api.diagram.controller.IDiagramController;
import eu.scy.scymapper.api.diagram.model.IDiagramModel;
import eu.scy.scymapper.api.diagram.model.ILinkModel;
import eu.scy.scymapper.api.diagram.model.INodeLinkModel;
import eu.scy.scymapper.api.diagram.model.INodeModel;
import org.apache.log4j.Logger;

import java.util.HashSet;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 24.jun.2009
 * Time: 12:03:12
 */
public class DiagramController implements IDiagramController {
    private final static Logger logger = Logger.getLogger(DiagramController.class);

    protected IDiagramModel model;

    public DiagramController(IDiagramModel diagramModel) {
        this.model = diagramModel;
    }

    @Override
    public void setName(String name) {
        model.setName(name);
    }

    @Override
    public void addNode(INodeModel n, boolean preventOverlap) {
        addNode(n);
    }

    @Override
    public void addNode(INodeModel n) {
        model.addNode(n);
    }

    @Override
    public void addLink(ILinkModel l) {
        model.addLink(l);
    }

    @Override
    public void removeAll() {
        for (INodeModel n : model.getNodes())
            removeNode(n);
    }

    @Override
    public void removeNode(INodeModel n) {
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
        for (ILinkModel link : linksToRemove) removeLink(link);
    }

    public void removeLink(ILinkModel l) {
        model.removeLink(l);
    }
}
