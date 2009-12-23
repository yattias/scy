/**
 * 
 */
package eu.scy.server.datasync;

import eu.scy.common.configuration.Configuration;
import eu.scy.common.datasync.SyncAction;
import eu.scy.common.datasync.SyncActionPacketTransformer;
import eu.scy.common.message.DataSyncMessagePacketTransformer;
import eu.scy.common.message.SyncMessage;
import eu.scy.common.message.SyncMessage.Event;
import eu.scy.common.message.SyncMessage.Response;
import eu.scy.common.message.SyncMessage.Type;
import eu.scy.common.packetextension.SCYPacketTransformer;
import eu.scy.commons.smack.SmacketExtension;
import eu.scy.commons.smack.SmacketExtensionProvider;
import eu.scy.commons.whack.WhacketExtension;
import eu.scy.scyhub.SCYHubModule;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.ProviderManager;
import org.xmpp.component.Component;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * @author giemza
 * 
 */
public class DataSyncModule extends SCYHubModule {

	private static final Logger logger = Logger.getLogger(DataSyncModule.class.getName());

	// sessionid -> sessionbridge
	private Map<String, DataSyncSessionBridge> bridges;

	private XMPPConnection connection;

	/**
	 * @param scyhub
	 */
	public DataSyncModule(Component scyhub) {
		super(scyhub, new DataSyncMessagePacketTransformer());

		bridges = new HashMap<String, DataSyncSessionBridge>();

		try {
			ConnectionConfiguration cc = new ConnectionConfiguration(
					Configuration.getInstance().getOpenFireHost(),
					Configuration.getInstance().getOpenFirePort());
			connection = new XMPPConnection(cc);
			connection.connect();
			connection.login("jt11", "jt11");
			connection.addConnectionListener(new ConnectionListener() {
				@Override public void connectionClosed() {}
				@Override public void connectionClosedOnError(Exception e) {}
				@Override public void reconnectingIn(int seconds) {}
				@Override public void reconnectionFailed(Exception e) {}
				@Override public void reconnectionSuccessful() {}
			});

			final SyncActionPacketTransformer sapt = new SyncActionPacketTransformer();

			// add extenison provider
			SmacketExtensionProvider.registerExtension(sapt);

			ProviderManager providerManager = ProviderManager.getInstance();
			providerManager.addExtensionProvider(sapt.getElementname(), sapt
					.getNamespace(), new SmacketExtensionProvider());

			DataSyncPacketFilterListener packetFilterListener = new DataSyncPacketFilterListener(bridges, new SyncActionPacketTransformer());
			connection.addPacketListener(packetFilterListener, packetFilterListener);
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.scy.scyhub.SCYHubModule#process(org.xmpp.packet.Packet)
	 */
	@Override
	protected void process(Packet packet, WhacketExtension extension) {
		logger.debug("Received packet with DataSyncCommandExtension");
		Object pojo = extension.getPojo();

		if (pojo instanceof SyncMessage) {
			SyncMessage command = (SyncMessage) pojo;
			logger.debug("Command: " + command.getEvent() + " "
					+ command.getUserId() + " " + command.getToolId());

			String sessionId = UUID.randomUUID().toString() + "@syncsessions." + Configuration.getInstance().getOpenFireHost();

			// create a session logger with the random id
			DataSyncSessionBridge dssl = new DataSyncSessionBridge(sessionId);
			bridges.put(sessionId, dssl);

			// response to client
			SyncMessage response = new SyncMessage(Type.answer);
			try {
				//first check if connection is still alive
				if(!connection.isConnected()) {
					connection.connect();
				}
				// try to connect the logger
				dssl.connect(connection);

				// if everything is okay we return success
				response.setEvent(Event.create);
				response.setResponse(Response.success);
				response.setMessage(sessionId);
			} catch (Exception e) {
				logger.error(
						"Error while creating DataSyncSessionLogger for session "
								+ sessionId, e);
				// if not we send a failure
				response.setEvent(Event.create);
				response.setResponse(Response.failure);
			}

			// we create the response message, add the extension and send it
			// with the same id to the client
			Message responseMessage = new Message();

			responseMessage.addExtension(new WhacketExtension(transformer
					.getElementname(), transformer.getNamespace(), response));
			responseMessage.setTo(packet.getFrom());
			responseMessage.setFrom(Configuration.getInstance().getSCYHubName() + "." + Configuration.getInstance().getOpenFireHost());
			responseMessage.setID(packet.getID());

			send(responseMessage);
		}
	}
	
	private class DataSyncPacketFilterListener implements PacketFilter, PacketListener {

		private Map<String, DataSyncSessionBridge> bridges;
		
		private SCYPacketTransformer transformer;

		public DataSyncPacketFilterListener(Map<String, DataSyncSessionBridge> bridges, SCYPacketTransformer transformer) {
			this.bridges = bridges;
			this.transformer = transformer;
		}
		
		@Override
		public boolean accept(org.jivesoftware.smack.packet.Packet packet) {
			return packet.getExtension(transformer.getElementname(), transformer.getNamespace()) != null;
		}

		@Override
		public void processPacket(org.jivesoftware.smack.packet.Packet packet) {
			PacketExtension packetExtension = packet.getExtension(transformer.getElementname(), transformer.getNamespace());
			if (packetExtension != null && packetExtension instanceof SmacketExtension) {
				SmacketExtension extension = (SmacketExtension) packetExtension;
				
				SyncAction action = (SyncAction) extension.getTransformer().getObject();
				DataSyncSessionBridge bridge = bridges.get(action.getSessionId());
				
				if(bridge != null) {
					bridge.process(action);
				}
			}
		}
		
	}

	@Override
	public void shutdown() {
		for (DataSyncSessionBridge bridge : bridges.values()) {
			bridge.shutdown();
		}
		connection.disconnect();
	}
}
