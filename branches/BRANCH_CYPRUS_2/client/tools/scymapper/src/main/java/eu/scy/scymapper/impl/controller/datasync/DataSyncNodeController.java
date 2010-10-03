package eu.scy.scymapper.impl.controller.datasync;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import eu.scy.client.common.datasync.ISyncSession;
import eu.scy.common.datasync.ISyncObject;
import eu.scy.common.datasync.SyncObject;
import eu.scy.scymapper.api.diagram.model.INodeModel;
import eu.scy.scymapper.api.styling.INodeStyle;
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

//	private TimerTask locationChangeBroadcast;
//	private TimerTask sizeChangeBroadcast;

	public DataSyncNodeController(INodeModel node, ISyncSession session) {
		super(node);
		this.syncSession = session;
	}

	@Override
	public void setSize(final Dimension dimension) {
		if (!model.getConstraints().getCanResize()) return;

		int minHeight = model.getStyle().getMinHeight();
		int minWidth = model.getStyle().getMinWidth();

		if (dimension.height < minHeight) dimension.height = minHeight;
		if (dimension.width < minWidth) dimension.width = minWidth;
		// Size and location (see setLocation) are properties that are changed VERY frequently when a user move a node
		// The code below makes sure changes in these properties are broadcasted more infrequent
//		if (sizeChangeBroadcast != null && sizeChangeBroadcast.scheduledExecutionTime() > System.currentTimeMillis())
//			sizeChangeBroadcast.cancel();
//
//		sizeChangeBroadcast = new TimerTask() {
//			@Override
//			public void run() {
		ISyncObject syncObject = new SyncObject();
		syncObject.setID(model.getId());
		syncObject.setToolname("scymapper");
		syncObject.setProperty("size", dimension.height + "," + dimension.width);
		syncSession.changeSyncObject(syncObject);
//			}
//		};
//
//		Timer t = new Timer();
//		t.schedule(sizeChangeBroadcast, 100);
//
//		// Update the model locally
//		model.setSize(d);

	}

	@Override
	public void setLocation(final Point p) {
		if (p.getX() < 0) p.x = 0;
		if (p.y < 0) p.y = 0;
		ISyncObject syncObject = new SyncObject();
		syncObject.setID(model.getId());
		syncObject.setToolname("scymapper");
		syncObject.setProperty("location", p.x + "," + p.y);
		syncSession.changeSyncObject(syncObject);
	}

	@Override
	public void setLabel(String text) {
		ISyncObject syncObject = new SyncObject();
		syncObject.setID(model.getId());
		syncObject.setToolname("scymapper");
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

	@Override
	public void setStyle(INodeStyle style) {
		ISyncObject syncObject = new SyncObject();
		syncObject.setID(model.getId());
		syncObject.setToolname("scymapper");
		XStream xstream = new XStream(new DomDriver());
		String xml = xstream.toXML(style);

		syncObject.setProperty("style", xml);
		syncSession.changeSyncObject(syncObject);
	}
}