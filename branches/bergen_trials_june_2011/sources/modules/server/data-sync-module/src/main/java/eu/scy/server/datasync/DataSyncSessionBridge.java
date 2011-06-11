package eu.scy.server.datasync;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.MultiUserChat;

import eu.scy.common.configuration.Configuration;
import eu.scy.common.datasync.ISyncObject;
import eu.scy.common.datasync.SyncAction;
import eu.scy.common.datasync.SyncActionPacketTransformer;
import eu.scy.common.datasync.SyncObject;
import eu.scy.common.message.DataSyncMessagePacketTransformer;
import eu.scy.common.message.SyncMessage;
import eu.scy.common.message.SyncMessage.Event;
import eu.scy.common.message.SyncMessage.Response;
import eu.scy.common.message.SyncMessage.Type;
import eu.scy.common.packetextension.SCYPacketTransformer;
import eu.scy.common.smack.SmacketExtension;

/**
 * This class stores and retrieves all objects in a synchronized session into
 * and from the tuplespace.
 * 
 * @author giemza
 */
public class DataSyncSessionBridge {
	
	private static final int DISCONNECTION_DELAY = 60 * 1000;

	private final Logger logger = Logger.getLogger(DataSyncSessionBridge.class);
	
	private final Lock lock = new ReentrantLock();

	private String id;
	private String toolId;
	private List<String> users;

	private TupleSpace sessionSpace;
	
	private XMPPConnection connection;
	private MultiUserChat muc;

	private Timer disconnectionTimer;

	public DataSyncSessionBridge(String id, Timer disconnectionTimer) {
		this.id = id;
		this.disconnectionTimer = disconnectionTimer;
		users = new ArrayList<String>();
		
		logger.debug("DataSyncSessionBridge initialised for session " + id);
	}

	public void connect(XMPPConnection connection) throws Exception {
		this.connection = connection;
		muc = new MultiUserChat(connection, id);
		muc.create(connection.getUser());
		Form form = muc.getConfigurationForm();
		form = form.createAnswerForm();
		form.setAnswer("muc#roomconfig_persistentroom", true);
		form.setAnswer("muc#roomconfig_enablelogging", true);
		muc.sendConfigurationForm(form);
		logger.debug("Successfully connected to MUC session");
		
		registerBridge(connection);
	}
	
	public void join(XMPPConnection connection) throws Exception {
		this.connection = connection;
		muc = new MultiUserChat(connection, id);
		muc.join(connection.getUser());
		
		registerBridge(connection);
	}

	private void registerBridge(XMPPConnection connection)
			throws TupleSpaceException {
		DataSyncPacketFilterListener packetFilterListener = new DataSyncPacketFilterListener(id);
                connection.addPacketListener(packetFilterListener, packetFilterListener);
	}
	
    private TupleSpace getSessionSpace() {
        if (sessionSpace == null) {
            try {
                connectToSQLSpaces();
                disconnectionTimer.schedule(new DisconnectionTask(), DISCONNECTION_DELAY);
            } catch (TupleSpaceException e) {
                logger.debug("Could not connect to session space in DataSyncSessionBridge for session " + id, e);
            }
        }
        return sessionSpace;
    }

    /**
     * @throws TupleSpaceException
     */
    public void connectToSQLSpaces() throws TupleSpaceException {
        sessionSpace = new TupleSpace(new User("SyncSessionBridge@" + id.substring(id.lastIndexOf("-"))), Configuration.getInstance().getSQLSpacesServerHost(), Configuration.getInstance().getSQLSpacesServerPort(), id);
        logger.debug("Successfully connected to TupleSpace @ " + Configuration.getInstance().getSQLSpacesServerHost() + ":" + Configuration.getInstance().getSQLSpacesServerPort());
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
			lock.lock();
			ISyncObject syncObject = action.getSyncObject();
			Tuple sat = new Tuple();
			sat.add(SyncObject.PATH);
			sat.add(syncObject.getID());
			sat.add(Field.createWildCardField());
			getSessionSpace().take(sat);
			logger.debug("Logged object removed event for object " + syncObject.getID() + " into " + id);
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	private void processObjectChanged(SyncAction action) {
		try {
			lock.lock();
			ISyncObject syncObject = action.getSyncObject();
			Tuple qt = new Tuple();
			qt.add(SyncObject.PATH);
			qt.add(syncObject.getID());
			qt.add(Field.createWildCardField());
			Tuple sat = getSessionSpace().read(qt);
			if (sat != null) {
				Map<String, String> oldprops = new HashMap<String, String>();
				for (int i = 7; i < sat.getNumberOfFields(); i++) {
					String keyValue = sat.getField(i).getValue().toString();
					if(keyValue.contains("=")) {
						int index = keyValue.indexOf("=");
						String key = keyValue.substring(0, index);
						String value = keyValue.substring(index + 1);
						oldprops.put(key, value);
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
				getSessionSpace().update(sat.getTupleID(), nsat);
				logger.debug("Logged object update event for object " + syncObject.getID() + " into " + id);
			} else {
				logger.debug("WARNING: Logged object update event for object " + syncObject.getID() + " but no syncobject tuple found!");
//				logger.debug("WARNING: Logged object update event for object " + syncObject.getID() + " but no object to update in TS, creating new one instead");
//				processObjectAdded(action);
			}
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	private void processObjectAdded(SyncAction action) {
		ISyncObject syncObject = action.getSyncObject();
		Tuple sat = new Tuple();
		sat.add(SyncObject.PATH); // 0
		sat.add(syncObject.getID()); // 1
		sat.add(syncObject.getCreator()); // 2
		sat.add(syncObject.getCreationTime()); // 3
		if(syncObject.getLastModificator() != null) {
			sat.add(syncObject.getLastModificator()); // 4
		} else {
			sat.add(new Field(String.class)); // 4
		}
		sat.add(syncObject.getLastModificationTime()); // 5
		sat.add(syncObject.getToolname()); // 6
		for (String key : syncObject.getProperties().keySet()) {
			String value = syncObject.getProperty(key);
			sat.add(key + "=" + value);
		}
		try {
			lock.lock();
			getSessionSpace().write(sat);
			logger.debug("Logged object added event for object " + syncObject.getID() + " into " + id);
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	private void queryAll(SyncMessage command) {
		SyncMessage syncMessage = new SyncMessage();
		syncMessage.setEvent(Event.queryall);
		syncMessage.setType(Type.answer);
		syncMessage.setSessionId(id);
		try {
			lock.lock();
			Tuple[] tuples = getSessionSpace().readAll(new Tuple());
			
			List<ISyncObject> syncObjects = new LinkedList<ISyncObject>();
			for (Tuple tuple : tuples) {
				SyncObject object = new SyncObject();
				object.setID(tuple.getField(1).getValue().toString());
				object.setCreator(tuple.getField(2).getValue().toString());
				object.setCreationTime((Long) tuple.getField(3).getValue());
				Object modificator = tuple.getField(4).getValue();
				if (modificator != null) {
					object.setLastModificator(modificator.toString());
				}
				object.setLastModificationTime((Long) tuple.getField(5).getValue());
				object.setToolname(tuple.getField(6).getValue().toString());
				// Properties
				for (int i = 7; i < tuple.getNumberOfFields(); i++) {
					String propertyString = tuple.getField(i).getValue().toString();
					if(propertyString.contains("=")) {
						int index = propertyString.indexOf("=");
						String key = propertyString.substring(0, index);
						String value = propertyString.substring(index + 1);
						object.setProperty(key, value);
					}
				}
				syncObjects.add(object);
			}
			syncMessage.setSyncobjects(syncObjects);
			syncMessage.setResponse(Response.success);
		} catch (TupleSpaceException e) {
			syncMessage.setResponse(Response.failure);
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		
		syncMessage.setSessionId(id);
		DataSyncMessagePacketTransformer dsmpt = new DataSyncMessagePacketTransformer(syncMessage);
		
		Packet responseMessage = new Message();
		responseMessage.setFrom(connection.getUser());
		responseMessage.setTo(command.getUserId());
		responseMessage.addExtension(new SmacketExtension(dsmpt));
		
		connection.sendPacket(responseMessage);
	}

	private void queryOne(SyncMessage command) {
		
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
			if(getSessionSpace().isConnected()) {
			    getSessionSpace().disconnect();
			}
		} catch (TupleSpaceException e) {
			logger.error(e);
		}
		logger.debug("Shutdown DataSyncSessionBridge for session " + id);
	}
	
	private class DataSyncPacketFilterListener implements PacketFilter, PacketListener {

		private String sessionId;
		
        private List<SCYPacketTransformer> transformers;

        public DataSyncPacketFilterListener(String sessionId) {
            this.sessionId = sessionId.substring(0, sessionId.indexOf('@')).toLowerCase();
            transformers = new LinkedList<SCYPacketTransformer>();
            transformers.add(new SyncActionPacketTransformer());
            transformers.add(new DataSyncMessagePacketTransformer());
        }

        @Override
        public boolean accept(org.jivesoftware.smack.packet.Packet packet) {
        	for (SCYPacketTransformer transformer : transformers) {
        		if(packet.getExtension(transformer.getElementname(), transformer.getNamespace()) != null) {
        			return true;
        		}
			}
        	return false;
        }

        @Override
        public void processPacket(org.jivesoftware.smack.packet.Packet packet) {
        	for (SCYPacketTransformer transformer : transformers) {
	            PacketExtension packetExtension = packet.getExtension(transformer.getElementname(), transformer.getNamespace());
	            if (packetExtension != null && packetExtension instanceof SmacketExtension) {
	                SmacketExtension extension = (SmacketExtension) packetExtension;
	                
	                if (transformer instanceof SyncActionPacketTransformer) {
	                	SyncAction action = (SyncAction) extension.getTransformer().getObject();
	                	if (action.getSessionId().toLowerCase().startsWith(sessionId)) {
	                		process(action);
	                	}
	                } else if (transformer instanceof DataSyncMessagePacketTransformer) {
	                	SyncMessage command = (SyncMessage) extension.getTransformer().getObject();
	                	if (command.getSessionId().toLowerCase().startsWith(sessionId)) {
	                		if (command.getEvent() == Event.queryall) {
	                			logger.debug("Command: " + command.getEvent() + " " + command.getUserId() + " " + command.getToolId());
	                			queryAll(command);
	                		} else if (command.getEvent() == Event.queryone) {
	                			logger.debug("Command: " + command.getEvent() + " " + command.getUserId() + " " + command.getToolId());
	                			queryOne(command);
	                		}
	                	}
	                }
	            }
        	}
        }

    }
	
	private class DisconnectionTask extends TimerTask {
            
            @Override
            public void run() {
                if (sessionSpace == null && sessionSpace.isConnected()) {
                    try {
                        lock.lock();
                        sessionSpace.disconnect();
                        sessionSpace = null;
                    } catch (TupleSpaceException e) {
                        logger.debug("Could not execute scheduled disconnection from tuplespace", e);
                    } finally {
                        lock.unlock();
                    }
                }
            }
        };
}
