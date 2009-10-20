package eu.scy.client.common.datasync;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smackx.muc.MultiUserChat;

import eu.scy.common.datasync.ISyncObject;

public class SyncSession implements ISyncSession {
	
	private MultiUserChat muc;
	private XMPPConnection xmppConnection;
	private Vector<ISyncListener> listeners;

	public SyncSession(XMPPConnection xmppConnection, MultiUserChat muc) {
		this.listeners = new Vector<ISyncListener>();
		this.xmppConnection = xmppConnection;
		this.muc = muc;
	}
	
	@Override
	public void addSyncObject(ISyncObject syncObject) {
		// TODO send syncObject to muc		
	}

	@Override
	public void changeSyncObject(ISyncObject syncObject) {
		// TODO send syncObject to muc
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
	public void removeSyncObject(ISyncObject syncObject) {
		// TODO send remove-syncobject to muc		
	}
	
	@Override
	public void addSyncListener(ISyncListener listener) {
		this.listeners.add(listener);
	}
	
	@Override
	public void removeSyncListener(ISyncListener listener) {
		this.listeners.remove(listener);
	}
	
	public void xmppIncomingMessagesGoHere() {
		// TODO
		// unpack message
		// parse: syncObjectAdded, changed, removed?
		// call listeners
	}
	
}
