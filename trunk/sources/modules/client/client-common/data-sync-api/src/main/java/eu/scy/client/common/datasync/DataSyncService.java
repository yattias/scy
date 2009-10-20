package eu.scy.client.common.datasync;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smackx.muc.MultiUserChat;

public class DataSyncService implements IDataSyncService {

	private XMPPConnection xmppConnection;

	public DataSyncService(XMPPConnection xmppConnection) {
		this.xmppConnection = xmppConnection;
	}
	
	@Override
	public ISyncSession createSession(ISyncListener listener) {
		
		// TODO send message to SCYHub / DataSyncModule via OpenFire
		// DataSyncModule creates a new muc and a SCYServerSession to store SyncObjects in SQLSpaces
		
		// wait for matching response from xmppConnection
		// (handle the asynchronous xmpp communication)
		// response defines mucID (= room);
		
		String mucID = ""; // defined by xmpp response
		MultiUserChat muc = new MultiUserChat(xmppConnection, mucID);
		ISyncSession newSession = new SyncSession(xmppConnection, muc);
		newSession.addSyncListener(listener);
		return newSession;
	}

	@Override
	public ISyncSession joinSession(String mucID, ISyncListener listener) {
		MultiUserChat muc = new MultiUserChat(xmppConnection, mucID);
		ISyncSession joinedSession = new SyncSession(xmppConnection, muc);
		joinedSession.addSyncListener(listener);
		return joinedSession;
	}

}
