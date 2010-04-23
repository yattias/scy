package eu.scy.client.common.datasync;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

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
import eu.scy.common.smack.SmacketExtension;
import eu.scy.common.smack.SmacketExtensionProvider;

public class SyncSession implements ISyncSession {
	
	private MultiUserChat muc;
	private XMPPConnection xmppConnection;
	private Vector<ISyncListener> listeners;
	
	private SyncActionPacketTransformer transformer;
	
	public SyncSession(XMPPConnection xmppConnection, MultiUserChat muc) {
		this.listeners = new Vector<ISyncListener>();
		this.xmppConnection = xmppConnection;
		this.muc = muc;
		
		transformer = new SyncActionPacketTransformer();
		
		//add extenison provider
		SmacketExtensionProvider.registerExtension(transformer);
		
	    ProviderManager providerManager = ProviderManager.getInstance();
	    providerManager.addExtensionProvider(SyncSession.this.transformer.getElementname(), SyncSession.this.transformer.getNamespace(), new SmacketExtensionProvider());
		
		xmppConnection.addPacketListener(new PacketListener(){
		
			@Override
			public void processPacket(Packet packet) {
				for (ISyncListener listener : listeners) {
					PacketExtension extension = packet.getExtension(SyncSession.this.transformer.getElementname(), SyncSession.this.transformer.getNamespace());
					if(extension != null && extension instanceof SmacketExtension) {
						SmacketExtension se = (SmacketExtension) extension;
						SyncAction syncAction = (SyncAction) se.getTransformer().getObject();
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
				}
			}
		}, new PacketFilter() {
			
			@Override
			public boolean accept(Packet packet) {
				return packet.getExtension(SyncSession.this.transformer.getElementname(), SyncSession.this.transformer.getNamespace()) != null;
			}
		});
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
		
		transformer = new SyncActionPacketTransformer(syncAction);
		message.addExtension(new SmacketExtension(transformer));
		
		try {
			muc.sendMessage(message);
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public List<ISyncObject> getAllSyncObjects() {
		List<ISyncObject> list = new LinkedList<ISyncObject>();
		// TODO request a list of all syncobject the datasyncmodule
		// for this session
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
	
}
