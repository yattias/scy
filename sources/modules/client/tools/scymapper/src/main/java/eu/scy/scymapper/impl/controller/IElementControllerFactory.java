package eu.scy.scymapper.impl.controller;

import eu.scy.scymapper.api.diagram.ILinkController;
import eu.scy.scymapper.api.diagram.ILinkModel;
import eu.scy.scymapper.api.diagram.INodeController;
import eu.scy.scymapper.api.diagram.INodeModel;

/**
 * @author bjoerge
 * @created 09.des.2009 00:02:56
 */
public interface IElementControllerFactory {
	public INodeController createNodeController(INodeModel node);
	public ILinkController createLinkController(ILinkModel link);
}
