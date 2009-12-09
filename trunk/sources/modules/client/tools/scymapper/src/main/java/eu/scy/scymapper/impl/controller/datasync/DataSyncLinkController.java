package eu.scy.scymapper.impl.controller.datasync;

import eu.scy.client.common.datasync.ISyncSession;
import eu.scy.scymapper.api.diagram.ILinkModel;
import eu.scy.scymapper.impl.controller.LinkController;


/**
 * @author bjoerge
 * @created 09.des.2009 00:19:20
 */
public class DataSyncLinkController extends LinkController {

	private ISyncSession syncSession;

	public DataSyncLinkController(ILinkModel link, ISyncSession session) {
		super(link);
		this.syncSession = session;
	}

	@Override
	public void setLabel(String text) {
		// TODO
	}
}
