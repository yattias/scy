package eu.scy.datasync.client;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketExtensionFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.provider.ProviderManager;

import eu.scy.communications.datasync.event.DataSyncEvent;
import eu.scy.communications.datasync.event.IDataSyncEvent;
import eu.scy.communications.datasync.event.IDataSyncListener;
import eu.scy.communications.message.ISyncMessage;
import eu.scy.communications.message.impl.SyncMessageHelper;
import eu.scy.communications.packet.extension.datasync.DataSyncPacketExtension;
import eu.scy.communications.packet.extension.datasync.DataSynceExtensionProvider;
import eu.scy.datasync.CommunicationProperties;
import eu.scy.datasync.adapter.ScyCommunicationAdapter;


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
    public CommunicationProperties communicationProps; 
    private String LOGIN;
    
    
    /**
     * Constructor sets up the service
     */
    public DataSyncService() {
        init();
    }
    
    /**
     * Initialize the connection
     */
    protected void init() {
        //add extenison provider
        ProviderManager providerManager = ProviderManager.getInstance();
        providerManager.addExtensionProvider(DataSyncPacketExtension.ELEMENT_NAME, DataSyncPacketExtension.NAMESPACE, new DataSynceExtensionProvider());
        
        SmackConfiguration.setPacketReplyTimeout(100000);
        SmackConfiguration.setKeepAliveInterval(60*1000);

        communicationProps = new CommunicationProperties();        
        config = new ConnectionConfiguration(communicationProps.datasyncServerHost, new Integer(communicationProps.datasyncServerPort).intValue());
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
        
        LOGIN = "datasync@" + communicationProps.datasyncServerHost;
        
        try {
            this.xmppConnection.login(LOGIN, "datasync");
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

        PacketListener packetListner = new PacketListener() {            
            @Override
            public void processPacket(Packet packet) {
                DataSyncPacketExtension extension = (DataSyncPacketExtension) packet.getExtension(DataSyncPacketExtension.ELEMENT_NAME, DataSyncPacketExtension.NAMESPACE);
                System.out.println("XML " + extension.toXML());
                logger.debug("in ur connection, sniffing ur packets");
                
                //call all the ones listening
                for (IDataSyncListener dataSyncListener : dataSyncListeners) {
                    IDataSyncEvent dse = new DataSyncEvent(this, extension.toPojo());
                    dataSyncListener.handleDataSyncEvent(dse);
                    
                }
                
                
            }
        };
        
        //packet extension filter we only want dataextension ones
        PacketExtensionFilter dataSynceExtensionFilter = new PacketExtensionFilter(DataSyncPacketExtension.ELEMENT_NAME, DataSyncPacketExtension.NAMESPACE);
        
        this.xmppConnection.addPacketListener(packetListner, dataSynceExtensionFilter);
        
    }
    
    /**
     * Sends a message
     * 
     * @parm ISyncMessage - message to send
     */
    @Override
    public void sendMessage(ISyncMessage syncMessage) {
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
        
        Message xmpp = SyncMessageHelper.convertToSmackXmppMessage(syncMessage);
        xmppConnection.sendPacket(xmpp);
        
    }
    
    /**
     * Creates a new session
     * 
     * @param toolId - tool id
     * @param userName - user id
     */
    @Override
    public void createSession(String toolId, String userName) {
        ISyncMessage syncMessage = SyncMessageHelper.createSyncMessageWithDefaultExp(null, toolId, userName, null,
                communicationProps.clientEventCreateSession, null);
        this.sendMessage(syncMessage);
    }

    /**
     * adds a new listener to this service
     */
    @Override
    public void addDataSyncListener(IDataSyncListener iDataSyncListener) {
        this.dataSyncListeners.add(iDataSyncListener);
    }


    
}



