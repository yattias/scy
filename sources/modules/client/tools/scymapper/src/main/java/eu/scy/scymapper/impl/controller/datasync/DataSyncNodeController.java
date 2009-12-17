package eu.scy.scymapper.impl.controller.datasync;

import eu.scy.client.common.datasync.ISyncSession;
import eu.scy.common.datasync.ISyncObject;
import eu.scy.common.datasync.SyncObject;
import eu.scy.scymapper.api.diagram.model.INodeModel;
import eu.scy.scymapper.impl.controller.NodeController;
import org.apache.log4j.Logger;

import java.awt.*;

/**
 * @author Bjorge Naess
 * @created 8.dec.2009 21:03:51
 */
public class DataSyncNodeController extends NodeController {
	private final static Logger logger = Logger.getLogger(DataSyncNodeController.class);

	private ISyncSession syncSession;

	public DataSyncNodeController(INodeModel node, ISyncSession session) {
		super(node);
		this.syncSession = session;
    }

	@Override
    public void setSize(Dimension d) {
		ISyncObject syncObject = new SyncObject();
		syncObject.setProperty("id", model.getId());
		syncObject.setProperty("size", d.height+","+d.width);
		syncSession.changeSyncObject(syncObject);
    }

    @Override
    public void setLocation(Point p) {
		ISyncObject syncObject = new SyncObject();
		syncObject.setProperty("id", model.getId());
		syncObject.setProperty("location", p.x+","+p.y);
		syncSession.changeSyncObject(syncObject);
    }

    @Override
    public void setLabel(String text) {
		ISyncObject syncObject = new SyncObject();
		syncObject.setProperty("id", model.getId());
		syncObject.setProperty("label", text);
		syncSession.changeSyncObject(syncObject);
    }

	@Override
	public void setSelected(boolean b) {
		 super.setSelected(b);
	}

    @Override
    public void setDeleted(boolean b) {
        super.setDeleted(b);
    }
}