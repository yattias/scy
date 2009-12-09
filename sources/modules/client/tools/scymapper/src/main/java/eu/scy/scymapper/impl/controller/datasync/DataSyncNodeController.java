package eu.scy.scymapper.impl.controller.datasync;

import eu.scy.client.common.datasync.ISyncSession;
import eu.scy.common.datasync.ISyncObject;
import eu.scy.common.datasync.SyncObject;
import eu.scy.scymapper.api.diagram.INodeModel;
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
    public void setSize(Dimension p) {
        super.setSize(p);
    }

    @Override
    public void setLocation(Point p) {
		ISyncObject syncObject = new SyncObject();

		syncObject.setProperty("elementId", model.getId());

		logger.debug(syncObject.getProperty("initial"));
		syncObject.setProperty("changedProperty", String.valueOf(DataSyncDiagramController.PROP_NODE_LOCATION));
		syncObject.setProperty("x", String.valueOf(p.x));
		syncObject.setProperty("y", String.valueOf(p.y));
		syncSession.changeSyncObject(syncObject);
    }

    @Override
    public void setLabel(String text) {
        super.setLabel(text);
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