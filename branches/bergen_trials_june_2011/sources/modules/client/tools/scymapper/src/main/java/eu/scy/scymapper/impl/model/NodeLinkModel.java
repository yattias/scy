package eu.scy.scymapper.impl.model;

import eu.scy.scymapper.api.diagram.model.INodeLinkModel;
import eu.scy.scymapper.api.diagram.model.INodeModel;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 22.jun.2009
 * Time: 18:40:14
 */
public class NodeLinkModel extends SimpleLink implements INodeLinkModel {
    private INodeModel fromNode;
    private INodeModel toNode;

    private String myLabel;
    
    public NodeLinkModel(Point from, Point to) {
        this.from = from;
        this.to = to;
    }

    public NodeLinkModel(INodeModel fromNode, INodeModel toNode) {
        this.fromNode = fromNode;
        this.toNode = toNode;
    }

	@Override
    public Point getFrom() {

        if (!isConnected()) return from;

        Point to = toNode.getConnectionPoint(fromNode.getCenterLocation());
        return fromNode.getConnectionPoint(to);

    }

    @Override
    public Point getTo() {

        if (!isConnected()) return to;

        Point from = fromNode.getConnectionPoint(toNode.getCenterLocation());
        return toNode.getConnectionPoint(from);
    }

    @Override
    public boolean isConnected() {
        return this.fromNode != null && this.toNode != null; 
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

    @Override
    public String getLabel() {
            return myLabel;
    }

    @Override
    public void setLabel(String label) {
            this.label = label;
            this.myLabel = label;
            notifyLabelChanged();
    }
    
}
