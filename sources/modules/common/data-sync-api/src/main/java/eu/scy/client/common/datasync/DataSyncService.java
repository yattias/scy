package eu.scy.client.common.datasync;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.util.StringUtils;
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
	
	private Connection connection;

        private final HashMap<String, ISyncSession> sessionMap;

        private PacketFilter sessionCreateAnswerFilter;

        private PacketListener sessionCreateAnswerListener;

	public DataSyncService() {
		packetLock = new SynchronousQueue<Packet>();
                sessionMap = new HashMap<String, ISyncSession>();
	}

	public void init(Connection xmppConnection) {
		
		this.connection = xmppConnection;
		final SCYPacketTransformer transformer = new DataSyncMessagePacketTransformer();
		
		// add extenison provider
		SmacketExtensionProvider.registerExtension(transformer);

		ProviderManager providerManager = ProviderManager.getInstance();
		providerManager.addExtensionProvider(transformer.getElementname(), transformer
				.getNamespace(), new SmacketExtensionProvider());
		
		sessionCreateAnswerListener = new PacketListener() {
		
			@Override
			public void processPacket(Packet packet) {
				try {
					packetLock.put(packet);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		sessionCreateAnswerFilter = new PacketFilter() {
		
			@Override
			public boolean accept(Packet packet) {
				PacketExtension extension = packet.getExtension(transformer.getElementname(), transformer.getNamespace());
				if(extension != null) {
					SmacketExtension se = (SmacketExtension) extension;
					SyncMessage message = (SyncMessage) se.getTransformer().getObject();
					if (message != null && message.getType().equals(Type.answer) && message.getEvent().equals(Event.create)) {
						return true;
					}
				}
				return false;
			}
		};
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
		command.setUserId(connection.getUser());
		command.setToolId(toolid);
		transformer.setObject(command);
		
		Packet sentPacket = new Message();
		sentPacket.setFrom(connection.getUser());
		sentPacket.setTo(Configuration.getInstance().getSCYHubName() + "." + Configuration.getInstance().getOpenFireHost());
		
		SmacketExtension extension = new SmacketExtension(transformer);
		sentPacket.addExtension(extension);
		
		Message receivedPacket = null;
		ISyncSession newSession = null;
		
		try {
		        connection.addPacketListener(sessionCreateAnswerListener, sessionCreateAnswerFilter);
		        
		        int tries = 5;
		        
		        while (tries > 0) {
		            logger.debug("Sending message to create session for tool " + toolid + " to receiver: " + Configuration.getInstance().getSCYHubName() + "." + Configuration.getInstance().getOpenFireHost());
		            connection.sendPacket(sentPacket);
		            
		            receivedPacket = (Message) packetLock.poll(5, TimeUnit.SECONDS);
		            
		            if (receivedPacket == null) {
		                tries--;
		            } else {
		                break;
		            }
		        }
		        
			if (receivedPacket == null) {
				logger.error("Creating session with toolid " + toolid + " timed out!");
				throw new DataSyncException("Creating session with toolid " + toolid + " timed out!");
			}
			connection.removePacketListener(sessionCreateAnswerListener);
			logger.debug("Received response for session creating from server.");
			
			extension = (SmacketExtension) receivedPacket.getExtension(transformer.getElementname(), transformer.getNamespace());
			SyncMessage message = (SyncMessage) extension.getTransformer().getObject();
			
			if(message.getResponse().equals(Response.success)) {
				String mucID = message.getSessionId(); // defined by xmpp response
				if (mucID == null) {
					throw new DataSyncException("Session could not be created!");
				}
				newSession = joinSession(mucID, listener, toolid, false);
				logger.debug("Session successfully created with id: " + mucID);
			} else if (message.getResponse().equals(Response.failure)) {
				logger.error("Failure during session creation");
				throw new DataSyncException("Creating session with toolid " + toolid + " failed!");
			}
		} catch (InterruptedException e) {
			logger.error("Exception while creating session", e);
			throw new DataSyncException("Something really bad happened ...", e);
		}
		return newSession;
	}

	private void checkConnectionState() throws DataSyncException {
		if (!connection.isAuthenticated()) {
			throw new DataSyncException("Connection is not authenticated. User already logged out? Wrong username or password?");
		}
		if (!connection.isConnected()) {
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
                synchronized (sessionMap) {
                    if (sessionMap.containsKey(mucID) && sessionMap.get(mucID).isConnected()) {
                        ISyncSession session = sessionMap.get(mucID);
                        session.addSyncListener(listener);
                        if (fetchState) {
                            session.fetchState(toolid);
                        }
                        return session;
                    } else {
                        MultiUserChat muc = new MultiUserChat(connection, mucID);
                        try {
                                muc.join(StringUtils.parseBareAddress(connection.getUser()), null, null, 10 * 1000);
                                ISyncSession joinedSession = new SyncSession(connection, muc, toolid, listener);
                                sessionMap.put(mucID, joinedSession);
                                if (fetchState) {
                                    joinedSession.fetchState(toolid);
                                }
                                return joinedSession;
                        } catch (XMPPException e) {
                                throw new DataSyncException(e);
                        }
                    }
                }
	}

    @Override
       public String toString() {
          String host="?";
          int port=-1;
          String user="?";
          if (connection!=null){
             host = connection.getHost();
             port = connection.getPort();
             user = connection.getUser();
          }
          return this.getClass().getName() + "{host=" + host + ", port=" + port + ", user=" + user  + '}';
       }
}