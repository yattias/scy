package eu.scy.scymapper.api.links;

import eu.scy.scymapper.api.nodes.INode;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 23.jun.2009
 * Time: 16:07:20
 * To change this template use File | Settings | File Templates.
 */
public interface INodeLinkController extends ILinkController {
    public void setToNode(INode toNode);
    public void setFromNode(INode toNode);

}
