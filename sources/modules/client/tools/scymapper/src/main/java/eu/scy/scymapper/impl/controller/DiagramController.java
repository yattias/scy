package eu.scy.scymapper.impl.controller;

import eu.scy.scymapper.api.diagram.IDiagramController;
import eu.scy.scymapper.api.diagram.IDiagram;
import eu.scy.scymapper.api.links.IConceptLink;
import eu.scy.scymapper.api.nodes.INode;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 24.jun.2009
 * Time: 12:03:12
 */
public class DiagramController implements IDiagramController {
    private IDiagram model;

    public DiagramController(IDiagram diagram) {
        this.model = diagram;
    }

    @Override
    public void setName(String name) {
        model.setName(name);
    }

    @Override
    public void addNode(INode n, boolean preventOverlap) {
        model.addNode(n);
    }

    @Override
    public void addNode(INode n) {
        model.addNode(n);
    }

    @Override
    public void addLink(IConceptLink l) {
        model.addLink(l);
    }
}
