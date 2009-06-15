package eu.scy.datasync.impl.factory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.provider.ProviderManager;

import eu.scy.communications.packet.extension.object.ScyObjectPacketExtension;
import eu.scy.core.model.ScyBase;
import eu.scy.datasync.api.DataSyncException;
import eu.scy.datasync.api.IDataSyncModule;
import eu.scy.datasync.api.ISyncMessage;
import eu.scy.datasync.api.event.IDataSyncListener;
import eu.scy.datasync.api.session.IDataSyncSession;
import eu.scy.datasync.impl.SyncMessage;
import eu.scy.datasync.impl.event.DataSyncEvent;

public class DataSyncXMPPImpl implements IDataSyncModule {
    
    private final static Logger logger = Logger.getLogger(DataSyncXMPPImpl.class.getName());
    private ConnectionConfiguration config;
    private XMPPConnection xmppConnection;
    private Roster roster;
    private String groupName;
    private ArrayList<IDataSyncListener> dataSyncListeners = new ArrayList<IDataSyncListener>();
    private String hostAddress;
    private String hostPort;
    private String hostName;
    private Chat chat;
    public String COMPONENT_NAME = "collaboration-service-plugin";
    
    public DataSyncXMPPImpl() {}
    
    public XMPPConnection getConnection(){
        return xmppConnection;
    }
    
    @Override
    public void connect(String username, String password) throws DataSyncException {
        this.connect(username, password, "test");
    }
    
    public void connect(String username, String password, String groupName) {
        this.groupName = groupName;
        Properties props = new Properties();
        try {
            props.load(DataSyncXMPPImpl.class.getResourceAsStream("datasync.server.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SmackConfiguration.setPacketReplyTimeout(100000);
        SmackConfiguration.setKeepAliveInterval(1000000);
        
        hostAddress = props.getProperty("datasync.address");
        hostPort = props.getProperty("datasync.port");
        hostName = props.getProperty("datasync.name");
        
        config = new ConnectionConfiguration(hostAddress, new Integer(hostPort).intValue(), hostName);
        config.setCompressionEnabled(true);
//        config.setSASLAuthenticationEnabled(true);
        config.setReconnectionAllowed(true);
        
        this.xmppConnection = new XMPPConnection(config);
        
        this.xmppConnection.DEBUG_ENABLED = true;
        try {
            
            this.xmppConnection.connect();
            this.xmppConnection.addConnectionListener(new ConnectionListener() {
                
                @Override
                public void connectionClosed() {
                    System.out.println("datasync server closed;");
                    try {
                        xmppConnection.connect();
                    } catch (XMPPException e) {
                        e.printStackTrace();
                    }
                    System.out.println("datasync server trying to reconnect;");
                }
                
                @Override
                public void connectionClosedOnError(Exception arg0) {
                    System.out.println("datasync server error closed;");
                }
                
                @Override
                public void reconnectingIn(int arg0) {
                    System.out.println("datasync server reconnecting;");
                }
                @Override
                public void reconnectionFailed(Exception arg0) {
                    System.out.println("datasync server reconnecting failed");
                }
                @Override
                public void reconnectionSuccessful() {
                    System.out.println("datasync server reconnectings success");
                }
            });

            ProviderManager providerManager = ProviderManager.getInstance();
//            providerManager.addExtensionProvider(ScyObjectPacketExtension.ELEMENT_NAME, ScyObjectPacketExtension.NAMESPACE, new ScyObjectExtensionProvider());
//            xmppConnection.addPacketListener(new PacketListener(){
//
//                @Override
//                public void processPacket(Packet packet) {
//                    System.out.println("packet; " + packet);
//                    org.jivesoftware.smack.packet.PacketExtension extension = packet.getExtension(ScyObjectPacketExtension.ELEMENT_NAME,ScyObjectPacketExtension.NAMESPACE);
//                    if( extension instanceof ScyObjectPacketExtension ) {
//                        System.out.println("got it");
//                    }
//                    
//                }}, null);
            
            xmppConnection.addPacketListener(new PacketListener(){

                @Override
                public void processPacket(Packet scyPacket) {
                    
                    
                    
                   PacketExtension eventPacketExtension = (PacketExtension) scyPacket.getExtension(ScyObjectPacketExtension.ELEMENT_NAME, ScyObjectPacketExtension.NAMESPACE);
                    
                    if (eventPacketExtension != null &&  eventPacketExtension instanceof ScyObjectPacketExtension) {
                        for (IDataSyncListener cl : dataSyncListeners) {
                            if (cl != null) {
                                ScyObjectPacketExtension scyExt =
                                    (ScyObjectPacketExtension) eventPacketExtension;

                                //FIXME
                                DataSyncEvent dataSyncEvent = new DataSyncEvent(this, null);
                                cl.handleDataSyncEvent(dataSyncEvent);
                            }// if
                        }// for
                }// processPacket

                    
                }},  null);
//            new PacketExtensionFilter(ScyObjectPacketExtension.ELEMENT_NAME,
//            ScyObjectPacketExtension.NAMESPACE)
            if (username == null || password == null) {
                this.xmppConnection.loginAnonymously();
            } else {
                this.xmppConnection.login(username, password);
            }
        } catch (XMPPException e) {
            logger.error("Error during connect");
            e.printStackTrace();
        }
        
        try {
            this.setUpRoster(groupName);
        } catch (XMPPException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    protected void setUpRoster(String groupName) throws XMPPException {
        roster = xmppConnection.getRoster();
//        roster.createEntry("collaboration-service-plugin.imediamac09.uio.no", "collaboration-service-plugin",new String[]{ groupName});
//        roster.createEntry(COMPONENT_NAME+ "."+hostAddress, "COMPONENT_NAME",new String[]{ groupName});
        roster.createEntry(COMPONENT_NAME+ "."+hostAddress, "COMPONENT_NAME",new String[]{ groupName});
        if (roster != null) {
            roster.setSubscriptionMode(Roster.SubscriptionMode.accept_all);
            RosterGroup group = roster.getGroup(groupName);
            logger.debug("Registered Groups: " + roster.getGroupCount() + " Entries: " + roster.getEntryCount());
            roster.addRosterListener(new RosterListener() {
                
                @Override
                public void entriesAdded(Collection<String> arg0) {
                    System.out.println("entriesAdded " + arg0);
                }
                
                @Override
                public void entriesDeleted(Collection<String> arg0) {
                    System.out.println("entriesDeleted " + arg0);
                }
                
                @Override
                public void entriesUpdated(Collection<String> arg0) {
                    System.out.println("entriesUpdated " + arg0);
                }
                
                @Override
                public void presenceChanged(Presence arg0) {
                    System.out.println("presenceChanged " + arg0);
                }
            });
        }
    }
    
    public void closeCollaborationService() {
        xmppConnection.disconnect();
    }
    
    public void sendMessage(String recipient, String message, Object objToSend) {
        if( chat == null ){
        chat = xmppConnection.getChatManager().createChat(recipient, new MessageListener() {
                
                @Override
                public void processMessage(Chat chat, org.jivesoftware.smack.packet.Message message) {
                    System.out.println("chat; " + message + " message; " + message);
                }
            });
        } else {
            try {
                
               org.jivesoftware.smack.packet.Message m = createTestMessage(objToSend);
                chat.sendMessage(m);
            } catch (XMPPException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    
    
    public Packet createTestPacket(Object objToSend) {
        logger.error("sending packet from the collaboration service");
        Packet m = new org.jivesoftware.smack.packet.Message();
//        m.setTo("collaboration-service-plugin"+"."+xmppConnection.getHost());
//        m.setTo("collaboration-service-plugin.imediamac09.uio.no");
        m.setTo(COMPONENT_NAME +".imediamac09.uio.no");
//       m.setTo("collaboration-service-plugin."+ hostAddress);
//        m.setTo(xmppConnection.getUser());
        m.setFrom(xmppConnection.getUser());
        PacketExtension ext = new ScyObjectPacketExtension();
        ((ScyObjectPacketExtension) ext).setScyBase((ScyBase) objToSend);
        m.addExtension((org.jivesoftware.smack.packet.PacketExtension) ext);
        return m;
    }
    
    public org.jivesoftware.smack.packet.Message createTestMessage(Object objToSend) {
        logger.error("sending MESSAGING from the collaboration service");
        org.jivesoftware.smack.packet.Message m = new org.jivesoftware.smack.packet.Message();
        m.setTo("component"+"."+xmppConnection.getHost());
//        m.setTo(xmppConnection.getUser()+"@"+xmppConnection.getHost());
//       m.setTo("collaboration-service-plugin."+ hostAddress);
        m.setFrom(xmppConnection.getUser());
        ScyObjectPacketExtension ext = new ScyObjectPacketExtension();
        ext.setScyBase((ScyBase) objToSend);
        m.addExtension((org.jivesoftware.smack.packet.PacketExtension) ext);
        return m;
    }
    
    public void sendPacket(Object objToSend, String message) {
        
        Packet m = createTestPacket(objToSend);
        if( xmppConnection.isConnected()){
            xmppConnection.sendPacket(m);
        }else{
            try {
                xmppConnection.connect();
                xmppConnection.sendPacket(m);
                logger.error("sending SCY Packet");
            } catch (XMPPException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
    }
  
    
    @Override
    public void create(ISyncMessage syncMessage) throws DataSyncException {
        this.sendPacket(syncMessage, "create");
    }

    @Override
    public void delete(String id) throws DataSyncException {
        this.sendPacket(id, "delete");
    }

    @Override
    public void update(ISyncMessage syncMessage) throws DataSyncException {
        if (syncMessage.getPersistenceId() != null) {
            this.sendPacket(syncMessage, "update");
        }
        throw new DataSyncException();
    }

    @Override
    public void sendCallBack(ISyncMessage syncMessage) throws DataSyncException {
    }
    
    @Override
    public void addDataSyncListener(IDataSyncListener collaborationListener) {
        dataSyncListeners.add(collaborationListener);
    }

    @Override
    public ISyncMessage read(String id) throws DataSyncException {
        this.sendPacket(id, "read");
        return new SyncMessage();
    }

    @Override
    public ArrayList<ISyncMessage> doQuery(ISyncMessage queryMessage) {
        return null;
    }

    @Override
    public ArrayList<ISyncMessage> synchronizeClientState(String userName, String toolName, String session, boolean includeChangesByUser) {
        //ISyncMessage syncMessage = SyncMessage.createScyMessage(null, toolName, null, null, SyncMessage.MESSAGE_TYPE_QUERY, SyncMessage.QUERY_TYPE_ALL, null, null, null, 0, session);
        ISyncMessage queryMessage = SyncMessage.createSyncMessage(session, toolName, null, SyncMessage.MESSAGE_TYPE_QUERY, userName, null, 0);
        return this.doQuery(queryMessage);
    }

    @Override
    public IDataSyncSession createSession(String arg0, String arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IDataSyncSession joinSession(String session, String userName, String toolName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ArrayList<IDataSyncSession> getSessions(String session, String userName, String toolName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean sessionExists(String session, String userName) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void cleanSession(String sessionId) {
        // TODO Auto-generated method stub
        
    }

}
