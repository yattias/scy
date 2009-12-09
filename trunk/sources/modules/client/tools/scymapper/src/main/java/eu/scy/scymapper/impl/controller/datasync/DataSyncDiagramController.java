package eu.scy.scymapper.impl.controller.datasync;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import eu.scy.client.common.datasync.ISyncListener;
import eu.scy.client.common.datasync.ISyncSession;
import eu.scy.common.datasync.ISyncObject;
import eu.scy.common.datasync.SyncObject;
import eu.scy.scymapper.api.diagram.*;
import eu.scy.scymapper.impl.controller.DiagramController;
import org.apache.log4j.Logger;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author bjoerge
 * @created 08.des.2009 17:04:53
 */
public class DataSyncDiagramController extends DiagramController implements ISyncListener {
	public final static int PROP_NODE_LOCATION = 0;
	public final static int PROP_NODE_SIZE = 1;
	public final static int PROP_NODE_STYLE = 2;
	public final static int PROP_NODE_SHAPE = 3;

	private final static Logger logger = Logger.getLogger(DataSyncDiagramController.class);

	private ISyncSession syncSession;

	// Keep track of all sync ids
	Map<String, IDiagramElement> elementIds = new HashMap<String, IDiagramElement>();

	public DataSyncDiagramController(IDiagramModel model, ISyncSession syncSession) {
		super(model);
		this.syncSession = syncSession;
		this.syncSession.addSyncListener(this);
	}

	/**
	 * Responds to remotely added objects
	 *
	 * @param syncObject the SyncObject that has been added
	 */
	@Override
	public void syncObjectAdded(ISyncObject syncObject) {

		String xml = syncObject.getProperty("initial");
		XStream xstream = new XStream(new DomDriver());
		IDiagramElement element = (IDiagramElement) xstream.fromXML(xml);

		//TODO: This is not the most elegant way of doin' it
		if (element instanceof INodeModel) {
			INodeModel node = (INodeModel) element;
			model.addNode(node);
		}
		if (element instanceof INodeLinkModel) {
			// Get the actual local objects for the from / to node of this link (it is deserialized)
			INodeLinkModel link = (INodeLinkModel) element;
			link.setFromNode((INodeModel)model.getElementById(link.getFromNode().getId()));
			link.setToNode((INodeModel)model.getElementById(link.getToNode().getId()));
			model.addLink(link);
		}

		// Remove initial data after the sync object is added
		syncObject.setProperty("initial", null);

		logger.debug("Added sync object with id " + syncObject.getID() + " for element with id " + element.getId());
	}

	@Override
	public void syncObjectChanged(ISyncObject syncObject) {

		String elementId = syncObject.getProperty("elementId");

		IDiagramElement element = model.getElementById(elementId);


		if (element == null) {
			logger.warn("NOOOO!!!! Requesting to change a remote diagram element that is not added locally!!!!!");
		} else {
			int changedProperty = new Integer(syncObject.getProperty("changedProperty"));

			switch (changedProperty) {
				case PROP_NODE_LOCATION:
					synchronized (this) {
						INodeModel node = ((INodeModel) element);
						logger.debug("######### syncObjectChanged::: " + changedProperty + " :: NODE: " + node);
						int x = new Integer(syncObject.getProperty("x"));
						int y = new Integer(syncObject.getProperty("y"));
						System.out.println("y = " + y);
						System.out.println("x = " + x);
						node.setLocation(new Point(x, y));
					}
					break;
			}
		}

	}

	/**
	 * Responds to remotely removed objects
	 */
	@Override
	public void syncObjectRemoved(ISyncObject syncObject) {

		String elementId = syncObject.getProperty("elementId");

		logger.debug("Sync object for element id " + elementId + " deleted remotely!!");

		IDiagramElement element = model.getElementById(elementId);
		if (element != null) {

			if (element instanceof INodeModel)
				model.removeNode((INodeModel) element);
			if (element instanceof ILinkModel)
				model.removeLink((ILinkModel) element);

		} else {
			logger.warn("Node for syncobject with id " + syncObject.getID() + " NOT FOUND!!!");
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

		syncObject.setProperty("elementId", n.getId());

		syncObject.setToolname("scymapper");

		XStream xstream = new XStream(new DomDriver());
		String xml = xstream.toXML(n);

		//System.out.println("xml = " + xml);
		syncObject.setProperty("initial", xml);

		syncSession.addSyncObject(syncObject);

		logger.debug("Added sync object: " + syncObject.getID());
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
		syncObject.setProperty("elementId", node.getId());

		logger.warn("Sync object for node with id " + node.getId() + " NOT FOUND!!!");

		syncSession.removeSyncObject(syncObject);

		logger.debug("Removed sync object: " + syncObject.getID());
	}
}
