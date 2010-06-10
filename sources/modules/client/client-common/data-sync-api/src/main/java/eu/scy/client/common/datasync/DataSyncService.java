package eu.scy.client.common.datasync;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.muc.MultiUserChat;

import eu.scy.common.configuration.Configuration;
import eu.scy.common.message.DataSyncMessagePacketTransformer;
import eu.scy.common.message.SyncMessage;
import eu.scy.common.message.SyncMessage.Event;
import eu.scy.common.message.SyncMessage.Response;
import eu.scy.common.message.SyncMessage.Type;
import eu.scy.common.packetextension.SCYPacketTransformer;
import eu.scy.common.smack.SmacketExtension;
import eu.scy.common.smack.SmacketExtensionProvider;

public class DataSyncService implements IDataSyncService {
	
	private static final Logger logger = Logger.getLogger(DataSyncService.class);

	private BlockingQueue<Packet> packetLock;
	
	private XMPPConnection xmppConnection;

	public DataSyncService() {
		packetLock = new SynchronousQueue<Packet>();
	}
	
	public void init(XMPPConnection xmppConnection) {
		
		this.xmppConnection = xmppConnection;
		final SCYPacketTransformer transformer = new DataSyncMessagePacketTransformer();
		
		// add extenison provider
		SmacketExtensionProvider.registerExtension(transformer);

		ProviderManager providerManager = ProviderManager.getInstance();
		providerManager.addExtensionProvider(transformer.getElementname(), transformer
				.getNamespace(), new SmacketExtensionProvider());
		
		xmppConnection.addPacketListener(new PacketListener(){
		
			@Override
			public void processPacket(Packet packet) {
				try {
					packetLock.put(packet);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, new PacketFilter() {
		
			@Override
			public boolean accept(Packet packet) {
				PacketExtension extension = packet.getExtension(transformer.getElementname(), transformer.getNamespace());
				if(extension != null) {
					SmacketExtension se = (SmacketExtension) extension;
					SyncMessage message = (SyncMessage) se.getTransformer().getObject();
					if (message.getType().equals(Type.answer) && message.getEvent().equals(Event.create)) {
						return true;
					}
				}
				return false;
			}
		});
	}
	
	/**
	 * @deprecated use {@link IDataSyncService#createSession(ISyncListener, String)} instead to provide a toolid
	 */
	@Deprecated
	@Override
	public ISyncSession createSession(ISyncListener listener) throws DataSyncException {
		return createSession(listener, "default-tool");
	}
	
	@Override
	public ISyncSession createSession(ISyncListener listener, String toolid) throws DataSyncException {
		checkConnectionState();
		
		SyncMessage command = new SyncMessage(Type.command);
		SCYPacketTransformer transformer = new DataSyncMessagePacketTransformer();
		
		command.setEvent(Event.create);
		command.setUserId(xmppConnection.getUser());
		command.setToolId(toolid);
		transformer.setObject(command);
		
		Packet sentPacket = new Message();
		sentPacket.setFrom(xmppConnection.getUser());
		sentPacket.setTo(Configuration.getInstance().getSCYHubName() + "." + Configuration.getInstance().getOpenFireHost());
		
		SmacketExtension extension = new SmacketExtension(transformer);
		sentPacket.addExtension(extension);
		
		logger.debug("Sending message to create session for tool " + toolid + " to receiver: " + Configuration.getInstance().getSCYHubName() + "." + Configuration.getInstance().getOpenFireHost());
		
		xmppConnection.sendPacket(sentPacket);
		
		Message receivedPacket = null;
		ISyncSession newSession = null;
		
		try {
			// TODO: check IDs for request and response
			receivedPacket = (Message) packetLock.poll(10, TimeUnit.SECONDS);
			
			if (receivedPacket == null) {
				logger.error("Creating session with toolid " + toolid + " timed out!");
				throw new DataSyncException("Creating session with toolid " + toolid + " timed out!");
			}
			
			logger.debug("Received response for session creating from server.");
			
			extension = (SmacketExtension) receivedPacket.getExtension(transformer.getElementname(), transformer.getNamespace());
			SyncMessage message = (SyncMessage) extension.getTransformer().getObject();
			
			if(message.getResponse().equals(Response.success)) {
				String mucID = message.getSessionId(); // defined by xmpp response
				if (mucID == null) {
					throw new DataSyncException("Session could not be created!");
				}
				MultiUserChat muc = new MultiUserChat(xmppConnection, mucID);
				muc.join(xmppConnection.getUser());
				newSession = new SyncSession(xmppConnection, muc, toolid, listener);
				
				logger.debug("Session successfully created with id: " + mucID);
			} else if (message.getResponse().equals(Response.failure)) {
				logger.error("Failure during session creation");
				throw new DataSyncException("Creating session with toolid " + toolid + " timed out!");
			}
		} catch (InterruptedException e) {
			logger.error("Exception while creating session", e);
			throw new DataSyncException("Something really bad happened ...", e);
		} catch (XMPPException e) {
			logger.error("Exception while creating session", e);
			throw new DataSyncException("Exception in XMPP communication during session creation process", e);
		}
		return newSession;
	}

	private void checkConnectionState() throws DataSyncException {
		if (!xmppConnection.isAuthenticated()) {
			throw new DataSyncException("Connection is not authenticated. User already logged out? Wrong username or password?");
		}
		if (!xmppConnection.isConnected()) {
			throw new DataSyncException("Client not connected to server!");
		}
	}

	@Override
	public ISyncSession joinSession(String mucID, ISyncListener listener) throws DataSyncException {
		return joinSession(mucID, listener, "default-tool");
	}
	@Override
	public ISyncSession joinSession(String mucID, ISyncListener listener, String toolid) throws DataSyncException {
		return joinSession(mucID, listener, toolid, true);
	}

	@Override
	public ISyncSession joinSession(String mucID, ISyncListener listener, String toolid, boolean fetchState) throws DataSyncException {
		checkConnectionState();
		
		MultiUserChat muc = new MultiUserChat(xmppConnection, mucID);
		try {
			muc.join(xmppConnection.getUser());
			ISyncSession joinedSession = new SyncSession(xmppConnection, muc, toolid, listener, true);
			return joinedSession;
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		return null;
	}
}