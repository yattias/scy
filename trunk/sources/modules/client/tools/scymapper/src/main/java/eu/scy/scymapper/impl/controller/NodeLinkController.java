package eu.scy.scymapper.impl.controller;

import eu.scy.scymapper.api.IConceptLinkModel;
import eu.scy.scymapper.api.diagram.INodeLinkController;
import eu.scy.scymapper.api.diagram.INodeModel;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 22.jun.2009
 * Time: 20:00:05
 * To change this template use File | Settings | File Templates.
 */
public class NodeLinkController implements INodeLinkController {
    private IConceptLinkModel model;

    public NodeLinkController(IConceptLinkModel link) {
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
    public void setToNode(INodeModel toNode) {
        model.setToNode(toNode);
    }

    @Override
    public void setFromNode(INodeModel fromNode) {
        model.setFromNode(fromNode);
    }
}