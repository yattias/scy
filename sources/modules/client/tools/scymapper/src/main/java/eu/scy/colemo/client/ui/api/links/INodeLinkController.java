package eu.scy.colemo.client.ui.api.links;

import eu.scy.colemo.client.ui.api.nodes.IConceptNode;

/**
 * Created by IntelliJ IDEA.
 * User: Bjørge Næss
 * Date: 23.jun.2009
 * Time: 16:07:20
 * To change this template use File | Settings | File Templates.
 */
public interface INodeLinkController extends ILinkController {
    public void setToNode(IConceptNode toNode);
    public void setFromNode(IConceptNode toNode);

}
