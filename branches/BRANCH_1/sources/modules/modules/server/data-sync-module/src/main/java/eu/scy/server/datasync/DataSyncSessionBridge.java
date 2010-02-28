package eu.scy.server.datasync;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.MultiUserChat;

import eu.scy.common.configuration.Configuration;
import eu.scy.common.datasync.ISyncObject;
import eu.scy.common.datasync.SyncAction;
import eu.scy.common.datasync.SyncObject;

/**
 * This class stores and retrieves all objects in a synchronized session into
 * and from the tuplespace.
 * 
 * @author giemza
 */
public class DataSyncSessionBridge {
	
	private final Logger logger = Logger.getLogger(DataSyncSessionBridge.class);

	private String id;
	private String toolId;
	private List<String> users;

	private TupleSpace sessionSpace;

	private MultiUserChat muc;

	public DataSyncSessionBridge(String id) {
		this.id = id;
		users = new ArrayList<String>();
		
		logger.debug("DataSyncSessionBridge initialised for session " + id);
	}

	public void connect(XMPPConnection connection) throws Exception {
		muc = new MultiUserChat(connection, id);
		muc.create(id);
		Form form = new Form(Form.TYPE_SUBMIT);
		muc.sendConfigurationForm(form);
		logger.debug("Successfully connected to MUC session");

		sessionSpace = new TupleSpace(new User("SyncSessionBridge@" + id),
				Configuration.getInstance().getSQLSpacesServerHost(),
				Configuration.getInstance().getSQLSpacesServerPort(), id);
		logger.debug("Successfully connected to TupleSpace");
	}

	public void process(SyncAction action) {
		switch (action.getType()) {
		case add:
			processObjectAdded(action);
			break;
		case change:
			processObjectChanged(action);
			break;
		case remove:
			processObjectRemoved(action);
			break;
		default:
			break;
		}
	}

	private void processObjectRemoved(SyncAction action) {
		try {
			ISyncObject syncObject = action.getSyncObject();
			Tuple sat = new Tuple();
			sat.add(SyncObject.PATH);
			sat.add(syncObject.getID());
			sat.add(Field.createWildCardField());
			sessionSpace.take(sat);
			logger.debug("Logged object removed event for object " + syncObject.getID() + " into " + id);
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}

	private void processObjectChanged(SyncAction action) {
		try {
			ISyncObject syncObject = action.getSyncObject();
			Tuple sat = new Tuple();
			sat.add(SyncObject.PATH);
			sat.add(syncObject.getID());
			sat.add(Field.createWildCardField());
			sat = sessionSpace.read(sat);
			
			Map<String, String> oldprops = new HashMap<String, String>();
			for (int i = 7; i < sat.getNumberOfFields(); i++) {
				String keyValue = sat.getField(i).getValue().toString();
				if(keyValue.contains("=")) {
					String[] split = keyValue.split("=");
					oldprops.put(split[0], split[1]);
				}
			}
			
			Map<String, String> newprops = action.getSyncObject().getProperties();
			
			for (String key : newprops.keySet()) {
				String value = newprops.get(key);
				if(value.equals(SyncObject.DELETE)) {
					oldprops.remove(key);
				} else {
					oldprops.put(key, value);
				}
			}
			
			Tuple nsat = new Tuple();
			nsat.add(SyncObject.PATH);
			nsat.add(syncObject.getID());
			nsat.add(sat.getField(2).getValue());
			nsat.add(sat.getField(3).getValue());
			nsat.add(action.getUserId());
			nsat.add(action.getTimestamp());
			nsat.add(syncObject.getToolname());
			for (String key : oldprops.keySet()) {
				String value = oldprops.get(key);
				nsat.add(key + "=" + value);
			}
			sessionSpace.update(sat.getTupleID(), nsat);
			logger.debug("Logged object update event for object " + syncObject.getID() + " into " + id);
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}

	private void processObjectAdded(SyncAction action) {
		ISyncObject syncObject = action.getSyncObject();
		Tuple sat = new Tuple();
		sat.add(SyncObject.PATH);
		sat.add(syncObject.getID());
		sat.add(syncObject.getCreator());
		sat.add(syncObject.getCreationTime());
		if(syncObject.getLastModificator() != null) {
			sat.add(syncObject.getLastModificator());
		} else {
			sat.add(new Field(String.class));
		}
		sat.add(syncObject.getLastModificationTime());
		sat.add(syncObject.getToolname());
		for (String key : syncObject.getProperties().keySet()) {
			String value = syncObject.getProperty(key);
			sat.add(key + "=" + value);
		}
		try {
			sessionSpace.write(sat);
			logger.debug("Logged object added event for object " + syncObject.getID() + " into " + id);
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}

	public void setToolId(String toolName) {
		this.toolId = toolName;
	}

	public String getToolId() {
		return this.toolId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setUsers(List<String> users) {
		this.users = users;
	}

	public void addUser(String userName) {
		if (userName != null && !this.users.contains(userName)) {
			users.add(userName);
		}
	}

	public List<String> getUsers() {
		return this.users;
	}

	public void removeUser(String userName) {
		if (userName != null && this.users.contains(userName)) {
			this.users.remove(userName);
		}
	}

	public void shutdown() {
		if(muc.isJoined()) {
			muc.leave();
		}
		try {
			if(sessionSpace.isConnected()) {
				sessionSpace.disconnect();
			}
		} catch (TupleSpaceException e) {
			logger.error(e);
		}
		logger.debug("Shutdown DataSyncSessionBridge for session " + id);
	}
}
