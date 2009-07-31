package eu.scy.colemo.client.ui.impl.model;

import eu.scy.colemo.client.ui.api.links.IConceptLink;
import eu.scy.colemo.client.ui.api.nodes.IConceptNode;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 22.jun.2009
 * Time: 18:40:14
 */
public class ConceptLink extends SimpleLink implements IConceptLink {
    private IConceptNode fromNode;
    private IConceptNode toNode;

    public ConceptLink(IConceptNode fromNode, IConceptNode toNode) {
        this.fromNode = fromNode;
        this.toNode = toNode;
    }

    @Override
    public Point getFrom() {
        Point to = toNode.getConnectionPoint(fromNode.getCenterLocation());
        return fromNode.getConnectionPoint(to);

    }

    @Override
    public Point getTo() {
        Point from = fromNode.getConnectionPoint(toNode.getCenterLocation());
        return toNode.getConnectionPoint(from);
    }

    @Override
    public IConceptNode getFromNode() {
        return fromNode;
    }

    @Override
    public void setFromNode(IConceptNode node) {
        fromNode = node;
    }

    @Override
    public IConceptNode getToNode() {
        return toNode;
    }

    @Override
    public void setToNode(IConceptNode node) {
        toNode = node;
    }
}
