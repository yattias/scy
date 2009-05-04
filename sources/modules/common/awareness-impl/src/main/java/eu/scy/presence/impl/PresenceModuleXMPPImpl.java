package eu.scy.presence.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import eu.scy.presence.IPresenceListListener;
import eu.scy.presence.IPresenceListener;
import eu.scy.presence.IPresenceModule;
import eu.scy.presence.IPresenceUser;
import eu.scy.presence.PresenceModuleException;
import eu.scy.presence.PresenceUser;
import eu.scy.presence.event.PresenceEvent;


public class PresenceModuleXMPPImpl implements IPresenceModule, MessageListener {

    private final static Logger logger = Logger.getLogger(PresenceModuleXMPPImpl.class.getName());
    private ConnectionConfiguration config;
    private XMPPConnection xmppConnection;
    private ArrayList<IPresenceListener> presenceListeners = new ArrayList<IPresenceListener>();
    private Roster roster;

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
        
        //as.config = new ConnectionConfiguration("wiki.intermedia.uio.no", 5222, "AwarenessService");
        config = new ConnectionConfiguration(address, new Integer(port).intValue(), name);
        xmppConnection = new XMPPConnection(config);
        try {
            xmppConnection.connect();
        } catch (XMPPException e) {
            logger.error("Error during connect");
            e.printStackTrace();
        }
        try {
            xmppConnection.login(username, password);
        } catch (XMPPException e) {
            logger.error("Error during login");
            e.printStackTrace();
            
        }
    }
    

    public void closePresenceModule() {
        xmppConnection.disconnect();
    }
    
    
    //TODO: return array should probably contain instances of scy users or somesuch
    public ArrayList<IPresenceUser> getBuddies(String username) {
        roster = this.xmppConnection.getRoster();
        Collection<RosterEntry> rosterEntries = roster.getEntries();
        ArrayList<IPresenceUser> buddies = new ArrayList<IPresenceUser>();
        for (RosterEntry buddy:rosterEntries) {
            IPresenceUser au = new PresenceUser();
            au.setName(buddy.getName());
            au.setUsername(buddy.getUser());
            au.setPresence(roster.getPresence(buddy.getUser()).toString());
            buddies.add(au);
        }
        return buddies;
    }
    
    
    public void sendMessage(String recipient, String message) {
        Chat chat = xmppConnection.getChatManager().createChat(recipient, (MessageListener) this);
        try {
            chat.sendMessage(message);
        } catch (XMPPException e) {
            logger.error("Error during sendMessage");
            e.printStackTrace();
        }
    }    
    
    
    public void setStatus(String username, String status) {
    }

    
    public void processMessage(Chat chat, Message message) {
        if (message.getType() == Message.Type.chat) {           
            logger.debug(chat.getParticipant() + " says: " + message.getBody());   
            //process the events
            for (IPresenceListener al : presenceListeners) {
                if (al != null){
                    RosterEntry entry = roster.getEntry(chat.getParticipant());
                    PresenceEvent presenceEvent = new PresenceEvent(this, entry.getName(), message.getBody());
                    //al.handlePresenceEvent(presenceEvent);
                }
            }
        }
    }
    
    public void addPresenceListener(IPresenceListener presenceListener){
        presenceListeners.add(presenceListener);
   }

    @Override
    public void addBuddy(String arg0) throws PresenceModuleException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addListListener(IPresenceListListener arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addStatusListener(IPresenceListener arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<String> getBuddies() throws PresenceModuleException {
        // TODO Auto-generated method stub
        return null;
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
        return null;        
    }

    @Override
    public void getPresence(String arg0) throws PresenceModuleException {
        // TODO Auto-generated method stub
    }

    @Override
    public void getStatus(String arg0) throws PresenceModuleException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void joinGroup(String arg0) throws PresenceModuleException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void leaveGroup(String arg0) throws PresenceModuleException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void removeBuddy(String arg0) throws PresenceModuleException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setPresence(String arg0, String arg1) throws PresenceModuleException {
        // TODO Auto-generated method stub
        
    }

    
}
