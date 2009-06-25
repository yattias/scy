package eu.scy.colemo.client.ui.impl.controller;

import eu.scy.colemo.client.ui.api.links.IConceptLink;
import eu.scy.colemo.client.ui.api.links.INodeLinkController;
import eu.scy.colemo.client.ui.api.nodes.IConceptNode;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Bjørge Næss
 * Date: 22.jun.2009
 * Time: 20:00:05
 * To change this template use File | Settings | File Templates.
 */
public class NodeLinkController implements INodeLinkController {
    private IConceptLink model;

    public NodeLinkController(IConceptLink link) {
        this.model = link;
    }

    @Override
    public void setLabel(String text) {
        model.setLabel(text);
    }

    @Override
    public void setTo(Point p) {

    }

    @Override
    public void setFrom(Point p) {
        
    }

    @Override
    public void setToNode(IConceptNode toNode) {
        model.setToNode(toNode);
    }

    @Override
    public void setFromNode(IConceptNode fromNode) {
        model.setFromNode(fromNode);
    }
}