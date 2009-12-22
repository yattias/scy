package eu.scy.scymapper.impl.controller.datasync;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import eu.scy.client.common.datasync.ISyncListener;
import eu.scy.client.common.datasync.ISyncSession;
import eu.scy.common.datasync.ISyncObject;
import eu.scy.common.datasync.SyncObject;
import eu.scy.scymapper.api.diagram.model.*;
import eu.scy.scymapper.impl.controller.DiagramController;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * @author bjoerge
 * @created 08.des.2009 17:04:53
 */
public class DataSyncDiagramController extends DiagramController implements ISyncListener {

	private final static Logger logger = Logger.getLogger(DataSyncDiagramController.class);

	private ISyncSession syncSession;

	public DataSyncDiagramController(IDiagramModel model, ISyncSession syncSession) {
		super(model);
		this.syncSession = syncSession;
		this.syncSession.addSyncListener(this);
		ISyncObject initObj = new SyncObject();
		this.syncSession.addSyncObject(initObj);
	}

	/**
	 * Responds to remotely added objects
	 *
	 * @param syncObject the SyncObject that has been added
	 */
	@Override
	public void syncObjectAdded(ISyncObject syncObject) {

		logger.debug("SYNC OBJECT ADDED TO SESSION!!!");

		String xml = syncObject.getProperty("initial");
		XStream xstream = new XStream(new DomDriver());
		IDiagramElement element = (IDiagramElement) xstream.fromXML(xml);

		System.out.println("element = " + element);

		//TODO: This is not the most elegant way of doin' it
		if (element instanceof INodeModel) {
			INodeModel node = (INodeModel) element;
			super.addNode(node);
		} else if (element instanceof INodeLinkModel) {
			// Get the actual local objects for the from / to node of this link (it is deserialized)
			INodeLinkModel link = (INodeLinkModel) element;
			link.setFromNode((INodeModel) model.getElementById(link.getFromNode().getId()));
			link.setToNode((INodeModel) model.getElementById(link.getToNode().getId()));
			super.addLink(link);
		} else {
			logger.debug("Could not recognize sync object. Skipping.");
		}
	}

	void sync(ISyncObject syncObj, INodeModel node) {
		for (Map.Entry<String, String> prop : syncObj.getProperties().entrySet()) {
			String key = prop.getKey();
			String value = prop.getValue();
			if (key.equals("location")) {
				String[] pos = value.split(",");
				int x = new Integer(pos[0]);
				int y = new Integer(pos[1]);
				node.setLocation(x, y);
			}
			if (key.equals("size")) {
				String[] pos = value.split(",");
				int h = new Integer(pos[0]);
				int w = new Integer(pos[1]);
				node.setSize(h, w);
			}
			if (key.equals("label")) {
				node.setLabel(value);
			}
		}
	}

	void sync(ISyncObject syncObj, ILinkModel link) {
		for (Map.Entry<String, String> prop : syncObj.getProperties().entrySet()) {
			String key = prop.getKey();
			String value = prop.getValue();

			if (key.equals("label")) {
				link.setLabel(value);
			}
		}
	}

	@Override
	public void syncObjectChanged(ISyncObject syncObject) {

		IDiagramElement element = model.getElementById(syncObject.getProperty("id"));

		if (element == null) {
			logger.warn("NOOOO!!!! Requesting to change a remote diagram element that is not added locally!!!!!");
		} else {
			if (element instanceof INodeModel) {
				sync(syncObject, (INodeModel) element);
			} else if (element instanceof ILinkModel) {
				sync(syncObject, (ILinkModel) element);
			}
		}
	}

	/**
	 * Responds to remotely removed objects
	 */
	@Override
	public void syncObjectRemoved(ISyncObject syncObject) {

		String id = syncObject.getProperty("id");

		logger.debug("Sync object for element id " + id + " deleted remotely!!");

		IDiagramElement element = model.getElementById(id);
		if (element != null) {
			if (element instanceof INodeModel)
				super.removeNode((INodeModel) element);
			else if (element instanceof ILinkModel)
				super.removeLink((ILinkModel) element);
		} else {
			logger.warn("Diagram element for syncobject with id " + syncObject.getID() + " NOT FOUND!!!");
		}
	}

	/**
	 * Responds to a request to add a node locally
	 *
	 * @param n node to add
	 */
	@Override
	public void addNode(INodeModel n) {
		// This is called whenever the user adds a new node to the diagra
		logger.debug("User locally added node with ID " + n.getId());

		ISyncObject syncObject = new SyncObject();

		syncObject.setProperty("id", n.getId());

		syncObject.setToolname("scymapper");

		XStream xstream = new XStream(new DomDriver());
		String xml = xstream.toXML(n);

		//System.out.println("xml = " + xml);
		syncObject.setProperty("initial", xml);

		syncSession.addSyncObject(syncObject);

		logger.debug("Added sync object to session: " + syncObject.getID());
	}

	@Override
	public void addLink(ILinkModel l) {

		// This is called whenever the user adds a new node to the diagra
		logger.debug("User locally added link with ID " + l.getId());

		ISyncObject syncObject = new SyncObject();
		syncObject.setToolname("scymapper");

		XStream xstream = new XStream(new DomDriver());
		String xml = xstream.toXML(l);

		//System.out.println("xml = " + xml);
		syncObject.setProperty("initial", xml);
		syncObject.setProperty("id", l.getId());

		syncSession.addSyncObject(syncObject);

		logger.debug("Added sync object: " + syncObject.getID());
	}

	/**
	 * Responds to a request to delete a node locally
	 *
	 * @param node node to remove
	 */
	@Override
	public void removeNode(INodeModel node) {
		logger.debug("User is locally removing node with ID " + node.getId());
		ISyncObject syncObject = new SyncObject();
		syncObject.setProperty("id", node.getId());
		syncSession.removeSyncObject(syncObject);
	}

	@Override
	public void removeLink(ILinkModel l) {
		logger.debug("User is locally removing link with ID " + l.getId());
		ISyncObject syncObject = new SyncObject();
		syncObject.setProperty("id", l.getId());
		syncSession.removeSyncObject(syncObject);
	}
}
