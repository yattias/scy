package eu.scy.scymapper.api.diagram.controller;

import eu.scy.scymapper.api.diagram.model.INodeModel;

/**
 * @author bjoerge
 * @created 23.jun.2009
 * Time: 16:07:20
 * To change this template use File | Settings | File Templates.
 */
public interface INodeLinkController extends ILinkController {
    public void setToNode(INodeModel toNode);
    public void setFromNode(INodeModel toNode);

}
