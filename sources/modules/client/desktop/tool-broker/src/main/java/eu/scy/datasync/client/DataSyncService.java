package eu.scy.datasync.client;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.ProviderManager;

import eu.scy.datasync.CommunicationProperties;
import eu.scy.datasync.adapter.ScyCommunicationAdapter;
import eu.scy.datasync.api.DataSyncException;
import eu.scy.datasync.api.event.IDataSyncListener;
import eu.scy.datasync.api.session.IDataSyncSession;
import eu.scy.datasync.impl.event.DataSyncEvent;
import eu.scy.datasync.impl.factory.DataSyncLocalImpl;
import eu.scy.datasync.impl.session.DataSyncSessionFactory;
import eu.scy.communications.message.ISyncMessage;
import eu.scy.communications.message.impl.SyncMessage;
import eu.scy.communications.packet.extension.object.ScyObjectPacketExtension;


/**
 * Tool client for doing data sync
 * 
 * @author thomasd
 *
 */
public class DataSyncService implements IDataSyncService {
    
    private static final Logger logger = Logger.getLogger(DataSyncService.class.getName());
    
    private ScyCommunicationAdapter scyCommunicationAdapter;
    private ArrayList<IDataSyncListener> dataSyncListeners = new ArrayList<IDataSyncListener>();
    private ConnectionConfiguration config;
    private XMPPConnection xmppConnection;
    private CommunicationProperties props; 
    
    
    public DataSyncService() {
        SmackConfiguration.setPacketReplyTimeout(100000);
        SmackConfiguration.setKeepAliveInterval(60*1000);

        props = new CommunicationProperties();        
        config = new ConnectionConfiguration(props.datasyncServerHost, new Integer(props.datasyncServerPort).intValue());
        config.setCompressionEnabled(true);
        config.setReconnectionAllowed(true);
        this.xmppConnection = new XMPPConnection(config);
        this.xmppConnection.DEBUG_ENABLED = true;

        try {            
            this.xmppConnection.connect();
            logger.debug("successful connection to xmpp server " + config.getHost() + ":" + config.getPort());
        } catch (XMPPException e) {
            logger.error("Error during xmpp connect");
            e.printStackTrace();
        }
        
        try {
            this.xmppConnection.login("datasyncservice@" + props.datasyncServerHost, "datasyncservice");
            logger.debug("xmpp login ok");
        } catch (XMPPException e1) {
            logger.error("xmpp login failed. bummer. " + e1);
            e1.printStackTrace();
        }
        
        this.xmppConnection.addConnectionListener(new ConnectionListener() {
            
            @Override
            public void connectionClosed() {
                logger.debug("datasync closed connection");
                try {
                    xmppConnection.connect();
                    logger.debug("datasync reconnected");
                } catch (XMPPException e) {
                    e.printStackTrace();
                    logger.debug("datasync failed to reconnect");
                }
            }
            
            @Override
            public void connectionClosedOnError(Exception arg0) {
                logger.debug("datasync server error closed;");
            }
            
            @Override
            public void reconnectingIn(int arg0) {
                logger.debug("datasync server reconnecting;");
            }
            @Override
            public void reconnectionFailed(Exception arg0) {
                logger.debug("datasync server reconnecting failed");
            }
            @Override
            public void reconnectionSuccessful() {
                logger.debug("datasync server reconnecting success");
            }
        });

        //ProviderManager providerManager = ProviderManager.getInstance();

    }
    
    @Override
    public void sendMessage(SyncMessage syncMessage) {
        if (!xmppConnection.isConnected()) {
            try {
                xmppConnection.connect();
                logger.debug("reconnected xmppConnection");
            } catch (XMPPException e) {
                logger.error("failed to reconnect xmppConnection: " + e);
                e.printStackTrace();
                return;
            }
        }
        logger.debug("datasync service sending xml......." + syncMessage.convertToXMPPMessage().toXML());
        xmppConnection.sendPacket(syncMessage.convertToXMPPMessage());
    }
    
    //implement synchronize client state
    
    
//    public IDataSyncSession createSession(String toolName, String userName) {
//        IDataSyncSession dataSyncSession = DataSyncSessionFactory.getDataSyncSession(null, toolName, userName);
//        ISyncMessage message = dataSyncSession.convertToSyncMessage();
//        try {
//            this.create(message);
//        } catch (DataSyncException e) {
//            logger.debug.error("Failed to create ScyMessage: " + message.toString());
//            e.printStackTrace();
//        }
//        return dataSyncSession;
//    }
//    
//    
//    public ArrayList<IDataSyncSession> getSessions(String session, String userName, String toolName) {
//        //ISyncMessage syncMessage = SyncMessage.createScyMessage(userName, toolName, null, null, ScyMessage.MESSAGE_TYPE_QUERY, ScyMessage.QUERY_TYPE_ALL, null, null, null, 0, session);        
//        ISyncMessage queryMessage = SyncMessage.createSyncMessage(session, toolName, null, SyncMessage.MESSAGE_TYPE_QUERY, userName, null, 0);
//        ArrayList<ISyncMessage> messages = this.doQuery(queryMessage);
//        ArrayList<IDataSyncSession> sessions = new ArrayList<IDataSyncSession>();
//        for (ISyncMessage message : messages) {
//            sessions.add(DataSyncSessionFactory.getDataSyncSession(message));
//        }
//        return sessions;
//    }
//    
//    
//    public IDataSyncSession joinSession(String session, String userName, String toolName) {
//        IDataSyncSession iCollaborationSession = null;
//        if (sessionExists(session, userName)) {
//            logger.debug.warn(userName + " is already member of session " + session);
//        } 
//        else if (sessionExists(session, null)) {
//            iCollaborationSession = createSession(toolName, userName);
//            logger.debug.debug(userName + " is now a member of session " + session);
//        } else {
//            logger.debug.error("could not find session: " + session);
//        }
//        return iCollaborationSession;
//    }
//    
//    
//    public boolean sessionExists(String session, String userName) {
//        return getSessions(session, userName, null).size() > 0;
//    }
//    
//    
//    public ArrayList<ISyncMessage> synchronizeClientState(String userName, String client, String session, boolean includeChangesByUser) {
//        //would have been nice to do a precise query, instead of filtering away userName afterwards
//        //ISyncMessage syncMessage = ((SyncMessage) SyncMessage).createScyMessage(null, client, null, null, SyncMessage.MESSAGE_TYPE_QUERY, SyncMessage.QUERY_TYPE_ALL, null, null, null, 0, session);
//        ISyncMessage queryMessage = SyncMessage.createSyncMessage(session, client, userName, null, SyncMessage.MESSAGE_TYPE_QUERY, null, 0);
//        ArrayList<ISyncMessage> messages = this.scyCommunicationAdapter.doQuery(queryMessage);
//        if (includeChangesByUser) {
//            return messages;
//        }
//        ArrayList<ISyncMessage> messagesFiltered = new ArrayList<ISyncMessage>();
//        for (ISyncMessage syncMessage : messages) {
//            if (!userName.equals(syncMessage.getFrom())) {
//                messagesFiltered.add(syncMessage);
//            }
//        }
//        return messagesFiltered;
//    }
//    
//    
//    public void cleanSession(String sessionId) {
//        ArrayList<IDataSyncSession> sessions = getSessions(sessionId, null, null);
//        for (IDataSyncSession collaborationSession : sessions) {
//            try {
//                this.delete(collaborationSession.getPersistenceId());
//            } catch (DataSyncException e) {
//                logger.debug.error("Trouble while deleting session: " + e);
//                e.printStackTrace();
//            }
//        }
//    }
//    
    
}



