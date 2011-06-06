package eu.scy.scymapper.impl.controller.datasync;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import eu.scy.client.common.datasync.ISyncListener;
import eu.scy.client.common.datasync.ISyncSession;
import eu.scy.common.datasync.ISyncObject;
import eu.scy.common.datasync.SyncObject;
import eu.scy.scymapper.api.diagram.model.*;
import eu.scy.scymapper.api.styling.ILinkStyle;
import eu.scy.scymapper.api.styling.INodeStyle;
import eu.scy.scymapper.impl.controller.DiagramController;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.util.HashSet;
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

		// This is a hack - for some reason the first sync object is not sendt to all clients
		this.syncSession.addSyncObject(new SyncObject());
	}
	
	public DataSyncDiagramController(IDiagramModel model) {
		super(model);
	}

	/**
	 * Responds to remotely added objects
	 *
	 * @param syncObject the SyncObject that has been added
	 */
	@Override
	public void syncObjectAdded(ISyncObject syncObject) {
            if(!syncObject.getToolname().equals("scymapper")) {
                return;
            }
		logger.debug("SYNC OBJECT ADDED TO SESSION!!!");

		String xml = syncObject.getProperty("initial");
		XStream xstream = new XStream(new DomDriver());
		IDiagramElement element = (IDiagramElement) xstream.fromXML(xml);
		if (model.getElementById(element.getId()) == null) {
			if (element instanceof INodeModel) {
				final INodeModel node = (INodeModel) element;
				sync(syncObject, node);
				
				if (syncSession != null && syncObject.getCreator().equals(syncSession.getUsername())) {
					model.addNode(node);
				} else {
					model.addNodeRemotely(node);
				}
				
			} else if (element instanceof INodeLinkModel) {
				// Get the actual local objects for the from / to node of this link (it is deserialized)
				final INodeLinkModel link = (INodeLinkModel) element;
				link.setFromNode((INodeModel) model.getElementById(link.getFromNode().getId()));
				link.setToNode((INodeModel) model.getElementById(link.getToNode().getId()));
				
				sync(syncObject, link);
				if (syncSession != null && syncObject.getCreator().equals(syncSession.getUsername())) {
					model.addLink(link);
				} else {
					model.addLinkRemotely(link);
				}
			} else {
				logger.debug("Could not recognize sync object. Skipping.");
			}
		}
	}

	void sync(ISyncObject syncObj, final INodeModel node) {
		for (Map.Entry<String, String> prop : syncObj.getProperties().entrySet()) {
			String key = prop.getKey();
			String value = prop.getValue();
			if (key.equals("location")) {
				String[] pos = value.split(",");
				final int x = new Integer(pos[0]);
				final int y = new Integer(pos[1]);

				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						node.setLocation(x, y);
					}
				});
			} else if (key.equals("size")) {
				String[] pos = value.split(",");
				final int h = new Integer(pos[0]);
				final int w = new Integer(pos[1]);

				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						node.setSize(h, w);
					}
				});
			} else if (key.equals("label")) {
				node.setLabel(value);
			} else if (key.equals("style")) {
				XStream xstream = new XStream(new DomDriver());
				INodeStyle style = (INodeStyle) xstream.fromXML(value);
				node.setStyle(style);
			}
		}
	}

	void sync(ISyncObject syncObj, ILinkModel link) {
		for (Map.Entry<String, String> prop : syncObj.getProperties().entrySet()) {
			String key = prop.getKey();
			String value = prop.getValue();

			if (key.equals("label")) {
				link.setLabel(value);
			} else if (key.equals("style")) {
				XStream xstream = new XStream(new DomDriver());
				ILinkStyle style = (ILinkStyle) xstream.fromXML(value);
				link.setStyle(style);
			}
		}
	}

	/**
	 * @param syncObject
	 */
	@Override
	public void syncObjectChanged(final ISyncObject syncObject) {
            if(!syncObject.getToolname().equals("scymapper")) {
                    return;
            }
		String id = syncObject.getID();

		if (id == null) {
			logger.info("No id of sync object. SKIPPING.");
			return;
		}

		final IDiagramElement element = model.getElementById(id);

		if (element == null) {
			logger.warn("OH NOO! Requesting to change a remote diagram element that is not added locally! ID = " + id);
		} else {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					if (element instanceof INodeModel) {
						sync(syncObject, (INodeModel) element);
					} else if (element instanceof ILinkModel) {
						sync(syncObject, (ILinkModel) element);
					}
				}
			});
		}
	}

	/**
	 * Responds to remotely removed objects
	 */
	@Override
	public void syncObjectRemoved(ISyncObject syncObject) {
                if(!syncObject.getToolname().equals("scymapper")) {
                    return;
                }
		String id = syncObject.getID();

		logger.debug("Sync object for element id " + id + " deleted remotely!!");

		final IDiagramElement element = model.getElementById(id);
		if (element != null) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					if (element instanceof INodeModel) {
						INodeModel n = (INodeModel) element;
						HashSet<INodeLinkModel> linksToRemove = new HashSet<INodeLinkModel>();
						for (ILinkModel link : model.getLinks()) {
							if (link instanceof INodeLinkModel) {
								INodeLinkModel nodeLink = (INodeLinkModel) link;
								if (n.equals(nodeLink.getFromNode()) || n.equals(nodeLink.getToNode())) {
									linksToRemove.add(nodeLink);
								}
							}
						}
						model.removeNode(n);
						for (ILinkModel link : linksToRemove) model.removeLink(link);
					} else if (element instanceof ILinkModel)
						model.removeLink((ILinkModel) element);
				}
			});
		} else {
			logger.warn("Diagram element with id " + id + " for syncobject with id " + syncObject.getID() + " NOT FOUND!!!");
		}
	}

	/**
	 * Responds to a request to add a node locally
	 *
	 * @param n node to add
	 */
	@Override
	public void add(INodeModel n) {
		// This is called whenever the user adds a new node to the diagra
		logger.debug("User locally added node with ID " + n.getId());

		ISyncObject syncObject = new SyncObject();
		
		syncObject.setID(n.getId());

		syncObject.setToolname("scymapper");

		XStream xstream = new XStream(new DomDriver());
		String xml = xstream.toXML(n);

		//System.out.println("xml = " + xml);
		syncObject.setProperty("initial", xml);

		syncSession.addSyncObject(syncObject);

		logger.debug("Added sync object to session: " + syncObject.getID());
	}

	@Override
	public void add(ILinkModel l) {

		// This is called whenever the user adds a new node to the diagra
		logger.debug("User locally added link with ID " + l.getId());

		ISyncObject syncObject = new SyncObject();
		syncObject.setID(l.getId());
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
	public void remove(INodeModel node) {
		if (!node.getConstraints().getCanDelete()) {
			logger.warn("Tried to delete a locked node");
			return;
		}

		logger.debug("User is locally removing node with ID " + node.getId());
		ISyncObject syncObject = new SyncObject();
		syncObject.setID(node.getId());
		syncObject.setToolname("scymapper");
		syncSession.removeSyncObject(syncObject);
	}

	@Override
	public void remove(ILinkModel l) {
		logger.debug("User is locally removing link with ID " + l.getId());
		ISyncObject syncObject = new SyncObject();
		syncObject.setID(l.getId());
		syncObject.setToolname("scymapper");
		syncSession.removeSyncObject(syncObject);
	}

	public void setSession(ISyncSession currentSession, boolean writeCurrentStateToServer) {
		this.syncSession = currentSession;
		if (writeCurrentStateToServer) {
			for (INodeModel node : model.getNodes()) {
				add(node);
			}
			for (ILinkModel link : model.getLinks()) {
				add(link);
			}
		}
	}
}
