package eu.scy.client.common.datasync;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.muc.MultiUserChat;

import eu.scy.common.datasync.ISyncAction;
import eu.scy.common.datasync.ISyncObject;
import eu.scy.common.datasync.SyncAction;
import eu.scy.common.datasync.SyncActionPacketTransformer;
import eu.scy.common.datasync.ISyncAction.Type;
import eu.scy.common.message.DataSyncMessagePacketTransformer;
import eu.scy.common.message.SyncMessage;
import eu.scy.common.message.SyncMessage.Event;
import eu.scy.common.message.SyncMessage.Response;
import eu.scy.common.smack.SmacketExtension;
import eu.scy.common.smack.SmacketExtensionProvider;

public class SyncSession implements ISyncSession {
	
	private MultiUserChat muc;
	private XMPPConnection xmppConnection;
	private Vector<ISyncListener> listeners;
	private String toolid;
	
	private BlockingQueue<Packet> queryQueue;
	
	public SyncSession(XMPPConnection xmppConnection, MultiUserChat muc, String toolid, ISyncListener listener) throws DataSyncException {
		this(xmppConnection, muc, toolid, listener, false);
	}
	
	public SyncSession(XMPPConnection xmppConnection, MultiUserChat muc, String toolid, ISyncListener listener, boolean fetchState) throws DataSyncException {
		this.listeners = new Vector<ISyncListener>();
		this.xmppConnection = xmppConnection;
		this.muc = muc;
		this.toolid = toolid;
		
		listeners.add(listener);
		
		final SyncActionPacketTransformer syncActionTransformer = new SyncActionPacketTransformer();
		final DataSyncMessagePacketTransformer syncMessageTransformer = new DataSyncMessagePacketTransformer();
		
		queryQueue = new LinkedBlockingQueue<Packet>();
		
		//add extenison provider
		SmacketExtensionProvider.registerExtension(syncActionTransformer);
		SmacketExtensionProvider.registerExtension(syncMessageTransformer);
		
	    ProviderManager providerManager = ProviderManager.getInstance();
	    SmacketExtensionProvider extensionProvider = new SmacketExtensionProvider();
	    providerManager.addExtensionProvider(syncActionTransformer.getElementname(), syncActionTransformer.getNamespace(), extensionProvider);
	    providerManager.addExtensionProvider(syncMessageTransformer.getElementname(), syncMessageTransformer.getNamespace(), extensionProvider);
		
		xmppConnection.addPacketListener(new PacketListener(){
		
			@Override
			public void processPacket(Packet packet) {
				if (packet.getExtension(syncActionTransformer.getElementname(), syncActionTransformer.getNamespace()) != null) {
					SmacketExtension extension = (SmacketExtension) packet.getExtension(syncActionTransformer.getElementname(), syncActionTransformer.getNamespace());
					SmacketExtension se = (SmacketExtension) extension;
					SyncAction syncAction = (SyncAction) se.getTransformer().getObject();
					for (ISyncListener listener : listeners) {
						switch (syncAction.getType()) {
						case add:
							listener.syncObjectAdded(syncAction.getSyncObject());
							break;
						case change:
							listener.syncObjectChanged(syncAction.getSyncObject());
							break;
						case remove:
							listener.syncObjectRemoved(syncAction.getSyncObject());
							break;
						default:
							break;
						}
					}
				} else if (packet.getExtension(syncMessageTransformer.getElementname(), syncMessageTransformer.getNamespace()) != null) {
					try {
						queryQueue.put(packet);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}, new PacketFilter() {
			
			@Override
			public boolean accept(Packet packet) {
				if(packet.getExtension(syncActionTransformer.getElementname(), syncActionTransformer.getNamespace()) != null) {
					return true;
				} else if (packet.getExtension(syncMessageTransformer.getElementname(), syncMessageTransformer.getNamespace()) != null) {
					PacketExtension extension = packet.getExtension(syncMessageTransformer.getElementname(), syncMessageTransformer.getNamespace());
					SmacketExtension se = (SmacketExtension) extension;
					SyncMessage message = (SyncMessage) se.getTransformer().getObject();
					if (message.getType().equals(eu.scy.common.message.SyncMessage.Type.answer) && message.getEvent().equals(Event.queryall)) {
						return true;
					}
				}
				return false;
				
			}
		});
		
		if (fetchState) {
			List<ISyncObject> syncObjects = getAllSyncObjects();
			System.out.println(syncObjects);
			Collections.sort(syncObjects, new Comparator<ISyncObject>() {
				@Override
				public int compare(ISyncObject o1, ISyncObject o2) {
					return (int) (o1.getCreationTime() - o2.getCreationTime());
				}
			});
			for (ISyncObject syncObject : syncObjects) {
				for (ISyncListener l : listeners) {
					l.syncObjectAdded(syncObject);
				}
			}
		}
	}
	
	@Override
	public void addSyncObject(ISyncObject syncObject) {
		syncObject.setCreator(muc.getNickname());
		sendSyncAction(Type.add, syncObject);
	}

	@Override
	public void changeSyncObject(ISyncObject syncObject) {
		syncObject.setLastModificator(muc.getNickname());
		syncObject.setLastModificationTime(System.currentTimeMillis());
		sendSyncAction(Type.change, syncObject);
	}

	@Override
	public void removeSyncObject(ISyncObject syncObject) {
		sendSyncAction(Type.remove, syncObject);	
	}
	
	private void sendSyncAction(Type type, ISyncObject syncObject) {
		Message message = muc.createMessage();
		message.setFrom(xmppConnection.getUser());
		
		ISyncAction syncAction = new SyncAction(this.getId(), muc.getNickname(), type, syncObject);
		
		SyncActionPacketTransformer syncActionTransformer = new SyncActionPacketTransformer(syncAction);
		message.addExtension(new SmacketExtension(syncActionTransformer));
		
		try {
			muc.sendMessage(message);
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public List<ISyncObject> getAllSyncObjects() throws DataSyncException {
		return getAllSyncObjects(10, TimeUnit.SECONDS);
	}
	
	@Override
	public List<ISyncObject> getAllSyncObjects(int time, TimeUnit unit) throws DataSyncException {
		List<ISyncObject> list = new LinkedList<ISyncObject>();
		SyncMessage request = new SyncMessage(eu.scy.common.message.SyncMessage.Type.request);
		request.setUserId(xmppConnection.getUser());
		request.setToolId(toolid);
		request.setEvent(Event.queryall);
		request.setSessionId(muc.getRoom());
		
		DataSyncMessagePacketTransformer syncMessageTransformer = new DataSyncMessagePacketTransformer();
		syncMessageTransformer.setObject(request);
		
		Message sentPacket = muc.createMessage();
		sentPacket.setFrom(xmppConnection.getUser());
		
		SmacketExtension extension = new SmacketExtension(syncMessageTransformer);
		sentPacket.addExtension(extension);
		
		try {
			muc.sendMessage(sentPacket);
			
			Packet packet = queryQueue.poll(20, TimeUnit.SECONDS);
			if (packet == null) {
				throw new DataSyncException("Exception during querying session data");
			}
			extension = (SmacketExtension) packet.getExtension(syncMessageTransformer.getElementname(), syncMessageTransformer.getNamespace());
			SyncMessage reply = (SyncMessage) extension.getTransformer().getObject();
			if (reply.getResponse().equals(Response.success)) {
				return reply.getSyncObjects();
			} else {
				//TODO throw new exception, now return empty list
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		
		return list;
	}

	@Override
	public ISyncObject getSyncObject(String id) {
		ISyncObject syncObject;
		// TODO request a syncobject with a given id in a session from datasyncmodule
		// for this session
		return null;
	}
	
	@Override
	public void addSyncListener(ISyncListener listener) {
		this.listeners.add(listener);
	}
	
	@Override
	public void removeSyncListener(ISyncListener listener) {
		this.listeners.remove(listener);
	}

	@Override
	public String getId() {
		return muc.getRoom();
	}

	@Override
	public void leaveSession() {
		muc.leave();
		listeners.removeAllElements();
	}
}