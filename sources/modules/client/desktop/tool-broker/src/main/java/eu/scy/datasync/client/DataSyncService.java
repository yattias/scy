package eu.scy.datasync.client;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketExtensionFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.provider.ProviderManager;

import eu.scy.communications.datasync.event.DataSyncEvent;
import eu.scy.communications.datasync.event.IDataSyncEvent;
import eu.scy.communications.datasync.event.IDataSyncListener;
import eu.scy.communications.datasync.properties.CommunicationProperties;
import eu.scy.communications.message.ISyncMessage;
import eu.scy.communications.message.impl.SyncMessageHelper;
import eu.scy.communications.packet.extension.datasync.DataSyncExtensionProvider;
import eu.scy.communications.packet.extension.datasync.DataSyncPacketExtension;
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
    public CommunicationProperties communicationProps = new CommunicationProperties();
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
        ISyncMessage syncMessage = SyncMessageHelper.createSyncMessage(null, toolId, userName, null, null,
                communicationProps.clientEventCreateSession, null,1000*60*60*24);
        this.sendMessage(syncMessage);
    }

    /**
     * adds a new listener to this service
     */
    @Override
    public void addDataSyncListener(IDataSyncListener iDataSyncListener) {
        this.dataSyncListeners.add(iDataSyncListener);
    }

    @Override
    public void getSessions(ISyncMessage syncMessage) {
        ISyncMessage newSyncMessage = SyncMessageHelper.createSyncMessageWithDefaultExp(null, syncMessage.getToolId(), syncMessage.getFrom(), syncMessage.getTo(), null,
                communicationProps.clientEventCreateSession, null);
        this.sendMessage(newSyncMessage);
    }

    @Override
    public void init(XMPPConnection xmppConnection) {
       this.xmppConnection = xmppConnection;
        
       //add extenison provider
       ProviderManager providerManager = ProviderManager.getInstance();
       providerManager.addExtensionProvider(DataSyncPacketExtension.ELEMENT_NAME, DataSyncPacketExtension.NAMESPACE, new DataSyncExtensionProvider());

       PacketListener packetListner = new PacketListener() {            
           @Override
           public void processPacket(Packet packet) {
               DataSyncPacketExtension extension = (DataSyncPacketExtension) packet.getExtension(DataSyncPacketExtension.ELEMENT_NAME, DataSyncPacketExtension.NAMESPACE);
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
    
    
    @Override
    public void disconnect() {
        this.xmppConnection.disconnect();
    }

    @Override
    public void joinSession(String sessionId, String toolId, String userName) {
        ISyncMessage syncMessage = SyncMessageHelper.createSyncMessageWithDefaultExp(sessionId, null, userName, null, null,
                communicationProps.clientEventJoinSession, null);
        this.sendMessage(syncMessage);
        
    }


    
}



