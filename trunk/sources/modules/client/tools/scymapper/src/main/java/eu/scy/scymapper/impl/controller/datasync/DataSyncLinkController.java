package eu.scy.scymapper.impl.controller.datasync;

import eu.scy.client.common.datasync.ISyncSession;
import eu.scy.common.datasync.ISyncObject;
import eu.scy.common.datasync.SyncObject;
import eu.scy.scymapper.api.diagram.model.ILinkModel;
import eu.scy.scymapper.impl.controller.LinkController;
import org.apache.log4j.Logger;


/**
 * @author bjoerge
 * @created 09.des.2009 00:19:20
 */
public class DataSyncLinkController extends LinkController {
	private final static Logger logger = Logger.getLogger(DataSyncLinkController.class);

	private ISyncSession syncSession;

	public DataSyncLinkController(ILinkModel link, ISyncSession session) {
		super(link);
		this.syncSession = session;
	}

	@Override
	public void setLabel(String text) {
		ISyncObject syncObject = new SyncObject();
		syncObject.setProperty("id", model.getId());
		syncObject.setProperty("label", text);
		syncSession.changeSyncObject(syncObject);
	}
}
