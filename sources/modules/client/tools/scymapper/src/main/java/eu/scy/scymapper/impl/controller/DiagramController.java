package eu.scy.scymapper.impl.controller;

import eu.scy.scymapper.api.diagram.IDiagramController;
import eu.scy.scymapper.api.diagram.IDiagramModel;
import eu.scy.scymapper.api.diagram.INodeModel;
import eu.scy.scymapper.api.diagram.ILinkModel;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 24.jun.2009
 * Time: 12:03:12
 */
public class DiagramController implements IDiagramController {
    private IDiagramModel model;

    public DiagramController(IDiagramModel diagramModel) {
        this.model = diagramModel;
    }

    @Override
    public void setName(String name) {
        model.setName(name);
    }

    @Override
    public void addNode(INodeModel n, boolean preventOverlap) {
        model.addNode(n);
    }

    @Override
    public void addNode(INodeModel n) {
        model.addNode(n);
    }

    @Override
    public void addLink(ILinkModel l) {
        model.addLink(l);
    }
}
