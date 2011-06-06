package eu.scy.collaborationservice.impl;

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
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.provider.ProviderManager;
import org.xmpp.packet.PacketExtension;

import eu.scy.collaborationservice.CollaborationServiceException;
import eu.scy.collaborationservice.ICollaborationService;
import eu.scy.collaborationservice.event.CollaborationServiceEvent;
import eu.scy.collaborationservice.event.ICollaborationServiceListener;
import eu.scy.collaborationservice.session.ICollaborationSession;
import eu.scy.communications.message.IScyMessage;
import eu.scy.communications.message.impl.ScyMessage;
import eu.scy.communications.packet.extension.object.ScyObjectPacketExtension;
import eu.scy.core.model.ScyBase;

public class CollaborationServiceXMPPImpl implements ICollaborationService {
    
    private final static Logger logger = Logger.getLogger(CollaborationServiceXMPPImpl.class.getName());
    private ConnectionConfiguration config;
    private XMPPConnection xmppConnection;
    private Roster roster;
    private String groupName;
    private ArrayList<ICollaborationServiceListener> collaborationListeners = new ArrayList<ICollaborationServiceListener>();
    private String hostAddress;
    private String hostPort;
    private String hostName;
    private Chat chat;
    public String COMPONENT_NAME = "collaboration-service-plugin";
    
    public CollaborationServiceXMPPImpl() {}
    
    public XMPPConnection getConnection(){
        return xmppConnection;
    }
    
    @Override
    public void connect(String username, String password) throws CollaborationServiceException {
        this.connect(username, password, "test");
    }
    
    public void connect(String username, String password, String groupName) {
        this.groupName = groupName;
        Properties props = new Properties();
        try {
            props.load(CollaborationServiceXMPPImpl.class.getResourceAsStream("collaboration.server.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SmackConfiguration.setPacketReplyTimeout(100000);
        SmackConfiguration.setKeepAliveInterval(1000000);
        
        hostAddress = props.getProperty("collaborationservice.address");
        hostPort = props.getProperty("collaborationservice.port");
        hostName = props.getProperty("collaborationservice.name");
        
        config = new ConnectionConfiguration("scy.intermedia.uio.no", 5222, "AwarenessService");
        //config = new ConnectionConfiguration(hostAddress, new Integer(hostPort).intValue(), hostName);
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
                    // System.out.println("collaboration server closed;");
                    try {
                        xmppConnection.connect();
                    } catch (XMPPException e) {
                        e.printStackTrace();
                    }
                    // System.out.println("collaboration server trying to reconnect;");
                }
                
                @Override
                public void connectionClosedOnError(Exception arg0) {
                    // System.out.println("collaboration server error closed;");
                }
                
                @Override
                public void reconnectingIn(int arg0) {
                    // System.out.println("collaboration server reconnecting;");
                }
                @Override
                public void reconnectionFailed(Exception arg0) {
                    // System.out.println("collaboration server reconnecting failed");
                }
                @Override
                public void reconnectionSuccessful() {
                    // System.out.println("collaboration server reconnectings success");
                }
            });

            ProviderManager providerManager = ProviderManager.getInstance();
//            providerManager.addExtensionProvider(ScyObjectPacketExtension.ELEMENT_NAME, ScyObjectPacketExtension.NAMESPACE, new ScyObjectExtensionProvider());
//            xmppConnection.addPacketListener(new PacketListener(){
//
//                @Override
//                public void processPacket(Packet packet) {
//                    // System.out.println("packet; " + packet);
//                    org.jivesoftware.smack.packet.PacketExtension extension = packet.getExtension(ScyObjectPacketExtension.ELEMENT_NAME,ScyObjectPacketExtension.NAMESPACE);
//                    if( extension instanceof ScyObjectPacketExtension ) {
//                        // System.out.println("got it");
//                    }
//                    
//                }}, null);
            
            xmppConnection.addPacketListener(new PacketListener(){

                @Override
                public void processPacket(Packet scyPacket) {
                    
                    
                    
                   PacketExtension eventPacketExtension = (PacketExtension) scyPacket.getExtension(ScyObjectPacketExtension.ELEMENT_NAME, ScyObjectPacketExtension.NAMESPACE);
                    
                    if (eventPacketExtension != null &&  eventPacketExtension instanceof ScyObjectPacketExtension) {
                        for (ICollaborationServiceListener cl : collaborationListeners) {
                            if (cl != null) {
                                ScyObjectPacketExtension scyExt =
                                    (ScyObjectPacketExtension) eventPacketExtension;

                                //FIXME
                                CollaborationServiceEvent collaborationEvent = new CollaborationServiceEvent(this, null);
                                cl.handleCollaborationServiceEvent(collaborationEvent);
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
                    // System.out.println("entriesAdded " + arg0);
                }
                
                @Override
                public void entriesDeleted(Collection<String> arg0) {
                    // System.out.println("entriesDeleted " + arg0);
                }
                
                @Override
                public void entriesUpdated(Collection<String> arg0) {
                    // System.out.println("entriesUpdated " + arg0);
                }
                
                @Override
                public void presenceChanged(Presence arg0) {
                    // System.out.println("presenceChanged " + arg0);
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
                    // System.out.println("chat; " + message + " message; " + message);
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
    public void create(IScyMessage scyMessage) throws CollaborationServiceException {
        this.sendPacket(scyMessage, "create");
    }

    @Override
    public void delete(String id) throws CollaborationServiceException {
        this.sendPacket(id, "delete");
    }

    @Override
    public void update(IScyMessage scyMessage, String id) throws CollaborationServiceException {
        this.sendPacket(scyMessage, "update");
    }

    @Override
    public void sendCallBack(IScyMessage scyMessage) throws CollaborationServiceException {
    }
    
    @Override
    public void addCollaborationListener(ICollaborationServiceListener collaborationListener) {
        collaborationListeners.add(collaborationListener);
    }

    @Override
    public IScyMessage read(String id) throws CollaborationServiceException {
        this.sendPacket(id, "read");
        return new ScyMessage();
    }

    @Override
    public ArrayList<IScyMessage> doQuery(IScyMessage queryMessage) {
        return null;
    }

    @Override
    public ArrayList<IScyMessage> synchronizeClientState(String userName, String toolName, String session, boolean includeChangesByUser) {
        IScyMessage scyMessage = ScyMessage.createScyMessage(null, toolName, null, null, ScyMessage.MESSAGE_TYPE_QUERY, ScyMessage.QUERY_TYPE_ALL, null, null, null, 0, session);
        return this.doQuery(scyMessage);
    }

    @Override
    public ICollaborationSession createSession(String arg0, String arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ICollaborationSession joinSession(String session, String userName, String toolName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ArrayList<ICollaborationSession> getSessions(String session, String userName, String toolName) {
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
