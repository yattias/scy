package eu.scy.scymapper.impl.controller.datasync;

import eu.scy.client.common.datasync.ISyncSession;
import eu.scy.scymapper.api.diagram.controller.ILinkController;
import eu.scy.scymapper.api.diagram.controller.INodeController;
import eu.scy.scymapper.api.diagram.model.ILinkModel;
import eu.scy.scymapper.api.diagram.model.INodeModel;
import eu.scy.scymapper.impl.controller.IElementControllerFactory;

/**
 * @author bjoerge
 * @created 09.des.2009 00:16:59
 */
public class DataSyncElementControllerFactory implements IElementControllerFactory {

	ISyncSession session;

	public DataSyncElementControllerFactory(ISyncSession session) {
		this.session = session;
	}

	@Override
	public INodeController createNodeController(INodeModel node) {
		return new DataSyncNodeController(node, session);
	}

	@Override
	public ILinkController createLinkController(ILinkModel link) {
		return new DataSyncLinkController(link, session);		
	}
}
