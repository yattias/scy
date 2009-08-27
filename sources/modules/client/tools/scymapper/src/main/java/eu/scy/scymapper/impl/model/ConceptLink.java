package eu.scy.scymapper.impl.model;

import eu.scy.scymapper.api.links.IConceptLink;
import eu.scy.scymapper.api.nodes.INode;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 22.jun.2009
 * Time: 18:40:14
 */
public class ConceptLink extends SimpleLink implements IConceptLink {
    private INode fromNode;
    private INode toNode;

    public ConceptLink(INode fromNode, INode toNode) {
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
    public INode getFromNode() {
        return fromNode;
    }

    @Override
    public void setFromNode(INode node) {
        fromNode = node;
    }

    @Override
    public INode getToNode() {
        return toNode;
    }

    @Override
    public void setToNode(INode node) {
        toNode = node;
    }
}
