package eu.scy.presence.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.FromContainsFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

import eu.scy.presence.IPresenceModule;
import eu.scy.presence.IPresencePacketEvent;
import eu.scy.presence.IPresencePacketListener;
import eu.scy.presence.IPresenceRosterEvent;
import eu.scy.presence.IPresenceRosterListener;
import eu.scy.presence.PresenceModuleException;
import eu.scy.presence.PresencePacketEvent;
import eu.scy.presence.PresenceRosterEvent;



public class PresenceModuleXMPPImpl implements IPresenceModule, MessageListener, RosterListener, PacketListener {
    
    private static final Logger logger = Logger.getLogger(PresenceModuleXMPPImpl.class.getName());
    public static final String OPENFIRE_SYSTEM_JID = "thematrix@scy.intermedia.uio.no";
    
    private ConnectionConfiguration config;
    private XMPPConnection xmppConnection;
    
    private ArrayList<IPresenceRosterListener> rosterListeners = new ArrayList<IPresenceRosterListener>();
    private ArrayList<IPresencePacketListener> packetListeners = new ArrayList<IPresencePacketListener>();
    private Roster roster;
    
    public static final PacketFilter MESSAGE_FROM_SYSTEM_FILTER = new AndFilter(new PacketTypeFilter(Message.class), new FromContainsFilter(OPENFIRE_SYSTEM_JID));
    
    
    public PresenceModuleXMPPImpl() {  
    }
    
    public boolean isConnected() {
        if( xmppConnection != null)
            return xmppConnection.isConnected();
        return false;
    }
    
    public void createPresenceModule(String username, String password) {
        
        Properties props = new Properties();
        try {
            props.load(PresenceModuleXMPPImpl.class.getResourceAsStream("server.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        String address = props.getProperty("presence.service.address");
        String port = props.getProperty("presence.service.port");
        String name = props.getProperty("presence.service.name");
        
        if( username == null && password == null ){
            username = props.getProperty("presence.service.username");
            password = props.getProperty("presence.service.password");
        }
        //as.config = new ConnectionConfiguration("scy.intermedia.uio.no", 5222, "AwarenessService");
        config = new ConnectionConfiguration(address, new Integer(port).intValue(), name);
        //config = new ConnectionConfiguration("imediamac09.uio.no", 5275, "eclipse");
        xmppConnection = new XMPPConnection(config);
        try {
            xmppConnection.connect();
        } catch (XMPPException e) {
            logger.severe("Error during connect");
            e.printStackTrace();
        }
        try {
            xmppConnection.login(username, password);
            
        } catch (XMPPException e) {
            logger.severe("Error during login");
            e.printStackTrace();            
        }
        initListeners();
    }
    
    
    private void initListeners() {
        this.xmppConnection.addPacketListener(this, MESSAGE_FROM_SYSTEM_FILTER);
        this.xmppConnection.getRoster().addRosterListener(this);
    }
    
    public void closePresenceModule() {
        xmppConnection.disconnect();
    }
    
    
    //TODO: return array should probably contain instances of scy users or somesuch
    public List<String> getBuddies(String username) {
        roster = this.xmppConnection.getRoster();
        Collection<RosterEntry> rosterEntries = roster.getEntries();
        ArrayList<String> buddies = new ArrayList<String>();
        for (RosterEntry buddy:rosterEntries) {
            logger.fine("buddy: " + buddy.getName());
            buddies.add(buddy.getName());
        }
        return buddies;
    }
    
    
    public void sendMessage(String recipient, String message) {
        Chat chat = xmppConnection.getChatManager().createChat(recipient, (MessageListener) this);
        try {
            chat.sendMessage(message);            
            List<String> users = new ArrayList<String>();
            users.add(chat.getParticipant());
            //update the listeners
            for (IPresenceRosterListener al : rosterListeners) {
                if (al != null){
                    RosterEntry entry = roster.getEntry(chat.getParticipant());
                    al.handlePresenceRosterEvent(new PresenceRosterEvent(this, users, message,IPresenceRosterEvent.MESSAGE_SENT));
                }
            }
        } catch (XMPPException e) {
            logger.severe("Error during sendMessage");
            e.printStackTrace();
        }
    }    
    
    
    public void setStatus(String username, String status) {
    }
    
    
    public void processMessage(Chat chat, Message message) {
        if (message.getType() == Message.Type.chat) {           
            logger.fine(chat.getParticipant() + " says: " + message.getBody());
            List<String> users = new ArrayList<String>();
            users.add(chat.getParticipant());
            //process the events
            for (IPresenceRosterListener al : rosterListeners) {
                if (al != null){
                    al.handlePresenceRosterEvent(new PresenceRosterEvent(this, users, message.getBody(),IPresenceRosterEvent.MESSAGE_RECEIVED));
                }
            }
        }
    }
    
    /**
     * This method will return status: unavailable for all users which the currently logged in user 
     * (the one who opened the xmppconnection) does not subscribe to presence data for. It is not enough 
     * to be member of the same group, or for the logged in user to be admin in group or even admin 
     * one the xmpp server.
     * 
     * @param groupName - name of group to check
     * @return Map of usernames and online status, both as String
     */
    public Map<String, String> getStatusForUsersInGroup(String groupName) {
        roster = this.xmppConnection.getRoster();
        roster.addRosterListener(this);
        ArrayList<RosterGroup> groups = new ArrayList<RosterGroup>(roster.getGroups()); 
        ArrayList<RosterEntry> usersInGroup = new ArrayList<RosterEntry>(); 
        HashMap<String, String> users = null;
        logger.fine("roster for user: " + this.xmppConnection.getUser());
        for (RosterGroup group : groups) {
            if (groupName.equals(group.getName())) {
                users = new HashMap<String, String>();
                usersInGroup = new ArrayList<RosterEntry>(group.getEntries()); 
                if (usersInGroup != null && usersInGroup.size() > 0) {
                    for (RosterEntry rosterEntry : usersInGroup) {
                        logger.fine("1 group " + groupName + " has member: " + rosterEntry.getName() + ", status: " + roster.getPresence(rosterEntry.getName()).toString());
                        //logger.debug("2 group " + groupName + " has member: " + rosterEntry.getName() + ", status: " + rosterEntry.getStatus());
                        users.put(rosterEntry.getName(), roster.getPresence(rosterEntry.getName()).toString());
                    }
                    return users;
                } else {
                    logger.fine("no entries in group " + groupName);
                    return null;
                }
            }
        }
        logger.fine("no group named " + groupName);
        return null;
    }
    
    
    @Override
    public void addBuddy(String userName, String group) throws PresenceModuleException {
        
    }
    
    @Override
    public List<String> getBuddies() throws PresenceModuleException {
        roster = this.xmppConnection.getRoster();
        Collection<RosterEntry> rosterEntries = roster.getEntries();
        ArrayList<String> buddies = new ArrayList<String>();
        for (RosterEntry buddy:rosterEntries) {
            //            IPresenceUser au = new PresenceUser();
            //            au.setName(buddy.getName());
            //            au.setUsername(buddy.getUser());
            //            au.setPresence(roster.getPresence(buddy.getUser()).toString());
            buddies.add(buddy.getName());
        }
        return buddies;
    }
    
    @Override
    public List<String> getGroups() throws PresenceModuleException {
        roster = this.xmppConnection.getRoster();
        Collection<RosterGroup> rosterGroups = roster.getGroups();
        ArrayList<String> groups = new ArrayList<String>();
        for (RosterGroup group:rosterGroups) {
            groups.add(group.getName());
        }
        return groups;
        
    }
    
    @Override
    public List<String> getGroups(String userName) throws PresenceModuleException {
        //      roster = this.xmppConnection.getRoster().g;
        //      roster = this.xmppConnection.getRoster();
        //        Collection<RosterGroup> rosterGroups = roster.getGroups();
        //        ArrayList<String> groups = new ArrayList<String>();
        //        for (RosterGroup group:rosterGroups) {
        //            groups.add(group.getName());
        //        }
        //        return groups;    
        return null;
    }
    
    @Override
    public void getPresence(String user) throws PresenceModuleException {
        //XXX why not return the presence?
        this.xmppConnection.getRoster().getPresence(user);
    }
    
    @Override
    public void getStatus(String arg0) throws PresenceModuleException {
        //XXX why not implement this?
    }
    
    
    @Override
    public void removeBuddy(String arg0) throws PresenceModuleException {
        
        
    }
    
    @Override
    public void setPresence(String userName, String group) throws PresenceModuleException {
        
    }
    
    
    //-- smack
    @Override
    public void entriesAdded(Collection<String> users) {
        for (IPresenceRosterListener rl : rosterListeners) {
            if (rl != null){
                rl.handlePresenceRosterEvent(new PresenceRosterEvent(this, users, null, IPresenceRosterEvent.ADDED));
            }
        }
    }
    
    @Override
    public void entriesDeleted(Collection<String> users) {
        for (IPresenceRosterListener rl : rosterListeners) {
            if (rl != null){
                rl.handlePresenceRosterEvent(new PresenceRosterEvent(this, users, null, IPresenceRosterEvent.DELETED));
            }
        }
    }
    
    @Override
    public void entriesUpdated(Collection<String> users) {
        for (IPresenceRosterListener rl : rosterListeners) {
            if (rl != null){
                rl.handlePresenceRosterEvent(new PresenceRosterEvent(this, users, null, IPresenceRosterEvent.UPDATED));
            }
        }
    }
    
    @Override
    public void presenceChanged(Presence presence) {
        List<String> users = new ArrayList<String>();
        users.add(presence.getFrom());
        
        for (IPresenceRosterListener rl : rosterListeners) {
            if (rl != null){
                rl.handlePresenceRosterEvent(new PresenceRosterEvent(this, users, presence.getStatus(), IPresenceRosterEvent.UPDATED));
            }
        }
    }
    
        
    /**
     * Processes a packet
     */
    @Override
    public void processPacket(Packet packet) {
        for (IPresencePacketListener pl : packetListeners) {
            if (pl != null){
                if (OPENFIRE_SYSTEM_JID.equals(packet.getFrom())) {
                    logger.fine("=== for agent smith only ===");
                    // the body of the packet is a presence update forwarded to agentsmith from thematrix
                    pl.handlePresencePacketEvent(new PresencePacketEvent(this, null, ((Message) packet).getBody(), IPresencePacketEvent.MESSAGE_RECEIVED));                    
                } else {
                    pl.handlePresencePacketEvent(new PresencePacketEvent(this, null, packet.toString(), IPresencePacketEvent.MESSAGE_RECEIVED));                    
                }
            }
        }
    }
    
    
    @Override
    public void addRosterListener(IPresenceRosterListener rosterListener) {
        this.rosterListeners.add(rosterListener);
    }
    

    @Override
    public void addPacketListener(IPresencePacketListener packetListener) {
        this.packetListeners.add(packetListener);
    }

    
    @Override
	public void joinGroup(String groupName, String userName) throws PresenceModuleException {		
	}

    
    @Override
    public void leaveGroup(String groupName, String userName) throws PresenceModuleException {
    }
    
}
