package eu.scy.collaborationservice;

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
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketExtensionFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.packet.Presence;

import eu.scy.communications.packet.extension.ScyObjectPacketExtension;
import eu.scy.core.model.ScyBase;
import eu.scy.core.model.impl.ScyBaseObject;

public class CollaborationService implements ICollaborationService {
    
    private final static Logger logger = Logger.getLogger(CollaborationService.class.getName());
    private ConnectionConfiguration config;
    private XMPPConnection xmppConnection;
    private Roster roster;
    private String groupName;
    private ArrayList<ICollaborationListener> collaborationListeners = new ArrayList<ICollaborationListener>();
    
    public CollaborationService() {}
    
    public XMPPConnection getConnection(){
        return xmppConnection;
    }
    
    public XMPPConnection connect(String username, String password, String groupName) {
        this.groupName = groupName;
        Properties props = new Properties();
        try {
            props.load(CollaborationService.class.getResourceAsStream("collaboration.server.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        String address = props.getProperty("collaborationservice.address");
        String port = props.getProperty("collaborationservice.port");
        String name = props.getProperty("collaborationservice.name");
        
        config = new ConnectionConfiguration(address, new Integer(port).intValue(), name);
        
        this.xmppConnection = new XMPPConnection(config);
        
        this.xmppConnection.DEBUG_ENABLED = true;
        try {
            
            this.xmppConnection.connect();
            this.xmppConnection.addConnectionListener(new ConnectionListener() {
                
                @Override
                public void connectionClosed() {
                    System.out.println("collaboration server closed;");
                    try {
                        xmppConnection.connect();
                    } catch (XMPPException e) {
                        e.printStackTrace();
                    }
                    System.out.println("collaboration server trying to reconnect;");
                }
                
                @Override
                public void connectionClosedOnError(Exception arg0) {
                    System.out.println("collaboration server error closed;");
                    
                }
                
                @Override
                public void reconnectingIn(int arg0) {
                    System.out.println("collaboration server reconnecting;");
                    
                }
                
                @Override
                public void reconnectionFailed(Exception arg0) {
                    System.out.println("collaboration server reconnecting failed");
                }
                
                @Override
                public void reconnectionSuccessful() {
                    System.out.println("collaboration server reconnectings success");
                    
                }
            });
            // PacketFilter pf = new PacketTypeFilter(Message.class);
            // PacketCollector pc = xmppConnection.createPacketCollector(pf);
            
//            PacketFilter scyFilter = new PacketFilter() {
//                
//                public boolean accept(Packet packet) {
//                    
//                    Message mess = (Message) packet;
//                    return "scy".equals(mess.getProperty("groupId"));
//                }
//            };
            
            // PacketFilter andFilter = new AndFilter(new
            // PacketTypeFilter(Message.class),scyFilter);
            //
            //
            //         
            // this.xmppConnection.addPacketListener(this, null);
            
            // PacketCollector pc =
            // xmppConnection.createPacketCollector(scyPackets);
            
            xmppConnection.addPacketListener(new PacketListener(){

                @Override
                public void processPacket(Packet scyPacket) {
                    
                    
                    
                   PacketExtension eventPacketExtension = scyPacket.getExtension(ScyObjectPacketExtension.ELEMENT_NAME, ScyObjectPacketExtension.NAMESPACE);
                    
                    if (eventPacketExtension != null &&  eventPacketExtension instanceof ScyObjectPacketExtension) {
                        for (ICollaborationListener cl : collaborationListeners) {
                            if (cl != null) {
                                ScyObjectPacketExtension scyExt =
                                    (ScyObjectPacketExtension) eventPacketExtension;


                                CollaborationEvent collaborationEvent = new CollaborationEvent(this, scyExt.getName(), scyExt.getDescription());
                                cl.handleCollaborationEvent(collaborationEvent);
                            }// if
                        }// for
                }// processPacket

                    
                }},  new PacketExtensionFilter(ScyObjectPacketExtension.ELEMENT_NAME,
                    ScyObjectPacketExtension.NAMESPACE));
            
            if (username == null || password == null) {
                this.xmppConnection.loginAnonymously();
            } else {
                this.xmppConnection.login(username, password);
            }
        } catch (XMPPException e) {
            logger.error("Error during connect");
            e.printStackTrace();
        }
        
        this.setUpRoster(groupName);
        
        return xmppConnection;
    }
    
    protected void setUpRoster(String groupName) {
        roster = xmppConnection.getRoster();
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
        Chat chat = xmppConnection.getChatManager().createChat(recipient, new MessageListener() {
            
            @Override
            public void processMessage(Chat arg0, Message message) {
                System.out.println("chat; " + arg0 + " message; " + message);
                
            }
        });
        
        try {
            Message newMessage = new Message();
            newMessage.setBody(message);
            newMessage.setProperty("group", groupName);
            newMessage.setProperty("customData", objToSend);
            chat.sendMessage(newMessage);
        } catch (XMPPException e) {
            logger.error("Error during sendMessage");
            e.printStackTrace();
        }
    }
    
    public void sendPacket(Object objToSend, String message) {
        logger.error("sending packet from the collaboration service");
        Message newMessage = new Message();
        newMessage.setFrom("your mom");
        newMessage.setBody(message);
        newMessage.setProperty("groupId", "scy");
        newMessage.setProperty("group", groupName);
        newMessage.setProperty("customData", objToSend);
        ScyObjectPacketExtension ext = new ScyObjectPacketExtension();
        ext.setScyBase((ScyBase) objToSend);
        newMessage.addExtension(ext);
        xmppConnection.sendPacket(newMessage);
        
    }
    
    @Override
    public void create(ScyBaseObject scyBaseObject) {
        this.sendPacket(scyBaseObject, "create");
    }
    
    @Override
    public void delete(ScyBaseObject scyBaseObject) {
        this.sendPacket(scyBaseObject, "delete");
    }
    
    @Override
    public void read(String id) {

    }
    
    @Override
    public void update(ScyBaseObject scyBaseObject) {
        this.sendPacket(scyBaseObject, "update");
    }
    
    public void addCollaborationListener(ICollaborationListener collaborationListener) {
        collaborationListeners.add(collaborationListener);
    }
}
