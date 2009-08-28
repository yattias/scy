package eu.scy.scymapper.impl.model;

import eu.scy.scymapper.api.IConceptLinkModel;
import eu.scy.scymapper.api.diagram.INodeModel;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 22.jun.2009
 * Time: 18:40:14
 */
public class NodeLinkModel extends SimpleLink implements IConceptLinkModel {
    private INodeModel fromNode;
    private INodeModel toNode;

    public NodeLinkModel(INodeModel fromNode, INodeModel toNode) {
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
    public INodeModel getFromNode() {
        return fromNode;
    }

    @Override
    public void setFromNode(INodeModel node) {
        fromNode = node;
    }

    @Override
    public INodeModel getToNode() {
        return toNode;
    }

    @Override
    public void setToNode(INodeModel node) {
        toNode = node;
    }
}
