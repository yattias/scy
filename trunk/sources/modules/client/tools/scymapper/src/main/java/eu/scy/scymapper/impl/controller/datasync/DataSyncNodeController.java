package eu.scy.scymapper.impl.controller.datasync;

import eu.scy.client.common.datasync.ISyncSession;
import eu.scy.common.datasync.ISyncObject;
import eu.scy.common.datasync.SyncObject;
import eu.scy.scymapper.api.diagram.model.INodeModel;
import eu.scy.scymapper.impl.controller.NodeController;
import org.apache.log4j.Logger;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Bjorge Naess
 * @created 8.dec.2009 21:03:51
 */
public class DataSyncNodeController extends NodeController {
	private final static Logger logger = Logger.getLogger(DataSyncNodeController.class);

	private ISyncSession syncSession;

	private TimerTask locationChangeBroadcast;
	private TimerTask sizeChangeBroadcast;

	public DataSyncNodeController(INodeModel node, ISyncSession session) {
		super(node);
		this.syncSession = session;
    }

	@Override
    public void setSize(final Dimension d) {
		// Size and location (see setLocation) are properties that are changed VERY frequently when a user move a node
		// The code below makes sure changes in these properties are broadcasted more infrequent
		if (sizeChangeBroadcast != null && sizeChangeBroadcast.scheduledExecutionTime() > System.currentTimeMillis())
			sizeChangeBroadcast.cancel();

		sizeChangeBroadcast = new TimerTask() {
			@Override
			public void run() {
				ISyncObject syncObject = new SyncObject();
				syncObject.setProperty("id", model.getId());
				syncObject.setProperty("size", d.height+","+d.width);
				syncSession.changeSyncObject(syncObject);
			}
		};

		Timer t = new Timer();
		t.schedule(sizeChangeBroadcast, 1000);

		// Update the model locally
		model.setSize(d);

    }

    @Override
    public void setLocation(final Point p) {

		// Only broadcast the change to datasync service if it is more than a certain amount of millisecs since last update
		if (locationChangeBroadcast != null && locationChangeBroadcast.scheduledExecutionTime() > System.currentTimeMillis())
			locationChangeBroadcast.cancel();

		locationChangeBroadcast = new TimerTask() {
			@Override
			public void run() {
				ISyncObject syncObject = new SyncObject();
				syncObject.setProperty("id", model.getId());
				syncObject.setProperty("location", p.x+","+p.y);
				syncSession.changeSyncObject(syncObject);
			}
		};

		Timer t = new Timer();
		t.schedule(locationChangeBroadcast, 1000);

		// Update the model locally
		model.setLocation(p);
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