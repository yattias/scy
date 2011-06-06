package eu.scy.scymapper.api.diagram.model;

/**
 * @author bjoerge
 * @created 22.jun.2009
 * Time: 18:59:55
 * To change this template use File | Settings | File Templates.
 */
public interface INodeLinkModel extends ILinkModel {

    public boolean isConnected();

    public INodeModel getFromNode();
    public void setFromNode(INodeModel node);

    public INodeModel getToNode();
    public void setToNode(INodeModel node);
}
