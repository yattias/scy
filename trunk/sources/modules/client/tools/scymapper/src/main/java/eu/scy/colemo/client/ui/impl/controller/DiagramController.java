package eu.scy.colemo.client.ui.impl.controller;

import eu.scy.colemo.client.ui.api.diagram.IDiagramController;
import eu.scy.colemo.client.ui.api.diagram.IDiagram;
import eu.scy.colemo.client.ui.api.nodes.IConceptNode;
import eu.scy.colemo.client.ui.api.links.IConceptLink;

/**
 * Created by IntelliJ IDEA.
 * User: Bjørge Næss
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
    public void addNode(IConceptNode n, boolean preventOverlap) {
        model.addNode(n);
    }

    @Override
    public void addNode(IConceptNode n) {
        model.addNode(n);
    }

    @Override
    public void addLink(IConceptLink l) {
        model.addLink(l);
    }
}
