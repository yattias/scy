package eu.scy.scymapper.impl.controller;

import eu.scy.scymapper.api.diagram.ILinkController;
import eu.scy.scymapper.api.diagram.ILinkModel;
import eu.scy.scymapper.api.diagram.INodeController;
import eu.scy.scymapper.api.diagram.INodeModel;

/**
 * @author bjoerge
 * @created 09.des.2009 00:11:26
 */
public class DefaultElementControllerFactory implements IElementControllerFactory {
	@Override
	public INodeController createNodeController(INodeModel node) {
		return new NodeController(node);
	}

	@Override
	public ILinkController createLinkController(ILinkModel link) {
		return new LinkController(link);
	}
}
