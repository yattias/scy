package eu.scy.scymapper.api.diagram;

import eu.scy.scymapper.api.diagram.INodeModel;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 23.jun.2009
 * Time: 16:07:20
 * To change this template use File | Settings | File Templates.
 */
public interface INodeLinkController extends ILinkController {
    public void setToNode(INodeModel toNode);
    public void setFromNode(INodeModel toNode);

}
