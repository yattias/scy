/**
 * 
 */
package eu.scy.server.datasync;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.ProviderManager;
import org.xmpp.component.Component;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;

import eu.scy.common.datasync.SyncAction;
import eu.scy.common.datasync.SyncActionPacketTransformer;
import eu.scy.common.message.DataSyncMessagePacketTransformer;
import eu.scy.common.message.SyncMessage;
import eu.scy.common.message.SyncMessage.Event;
import eu.scy.common.message.SyncMessage.Response;
import eu.scy.common.message.SyncMessage.Type;
import eu.scy.common.packetextension.SCYPacketTransformer;
import eu.scy.commons.smack.SmacketExtensionProvider;
import eu.scy.commons.whack.WhacketExtension;
import eu.scy.scyhub.SCYHubModule;


/**
 * @author giemza
 * 
 */
public class DataSyncModule extends SCYHubModule {

	private static final Logger logger = Logger.getLogger(DataSyncModule.class.getName());

	// sessionid -> sessionlogger
	private Map<String, DataSyncSessionLogger> loggers;

	private XMPPConnection connection;

	/**
	 * @param scyhub
	 */
	public DataSyncModule(Component scyhub) {
		super(scyhub, new DataSyncMessagePacketTransformer());

		loggers = new HashMap<String, DataSyncSessionLogger>();

		try {
			// TODO use Configuration class again, when extracted out of scy-commons blob
//			ConnectionConfiguration cc = new ConnectionConfiguration(
//					Configuration.getInstance().getDatasyncServerHost(),
//					Configuration.getInstance().getDatasyncServerPort());
			ConnectionConfiguration cc = new ConnectionConfiguration(
					"scy.collide.info", 5222);

			connection = new XMPPConnection(cc);
			connection.connect();
			connection.login("datasynclistener", "datasync");

			final SyncActionPacketTransformer sapt = new SyncActionPacketTransformer();

			// add extenison provider
			SmacketExtensionProvider.registerExtension(sapt);

			ProviderManager providerManager = ProviderManager.getInstance();
			providerManager.addExtensionProvider(sapt.getElementname(), sapt
					.getNamespace(), new SmacketExtensionProvider());

			connection.addPacketListener(new DataSyncPacketFilterListener(loggers, transformer), new DataSyncPacketFilterListener(loggers, transformer));
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

			// Create a random session id
			// right now we create one sync session with a fixed id so we can
			// better test
			// String sessionId = UUID.randomUUID().toString() +
			// "@syncsessions.scy.collide.info";
			String sessionId = command.getToolId() + "-datasyncsession" + "@syncsessions.scy.collide.info";

			// create a session logger with the random id
			DataSyncSessionLogger dssl = new DataSyncSessionLogger(sessionId);

			// response to client
			SyncMessage response = new SyncMessage(Type.answer);
			try {
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
			responseMessage.setFrom("scyhub.scy.collide.info");
			responseMessage.setID(packet.getID());

			send(responseMessage);
		}
	}
	
	private class DataSyncPacketFilterListener implements PacketFilter, PacketListener {

		private Map<String, DataSyncSessionLogger> loggers;
		
		private SCYPacketTransformer transformer;

		public DataSyncPacketFilterListener(Map<String, DataSyncSessionLogger> loggers, SCYPacketTransformer transformer) {
			this.loggers = loggers;
			this.transformer = transformer;
		}
		
		@Override
		public boolean accept(org.jivesoftware.smack.packet.Packet packet) {
			return packet.getExtension(transformer.getElementname(), transformer.getNamespace()) != null;
		}

		@Override
		public void processPacket(org.jivesoftware.smack.packet.Packet packet) {
			PacketExtension packetExtension = packet.getExtension(transformer.getElementname(), transformer.getNamespace());
			if (packetExtension != null && packetExtension instanceof WhacketExtension) {
				WhacketExtension extension = (WhacketExtension) packetExtension;
				
				SyncAction action = (SyncAction) extension.getPojo();
				DataSyncSessionLogger logger = loggers.get(action.getSessionId());
				
				logger.log(action);
			}
		}
		
	}
	
}
