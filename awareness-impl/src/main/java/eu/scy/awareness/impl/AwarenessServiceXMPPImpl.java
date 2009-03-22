package eu.scy.awareness.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import eu.scy.awareness.AwarenessServiceException;
import eu.scy.awareness.AwarenessUser;
import eu.scy.awareness.IAwarenessService;
import eu.scy.awareness.IAwarenessUser;
import eu.scy.awareness.event.AwarenessEvent;
import eu.scy.awareness.event.IAwarenessListListener;
import eu.scy.awareness.event.IAwarenessPresenceListener;


public class AwarenessServiceXMPPImpl implements IAwarenessService, MessageListener {

    private final static Logger logger = Logger.getLogger(AwarenessServiceXMPPImpl.class.getName());
    private ConnectionConfiguration config;
    private XMPPConnection xmppConnection;
    private ArrayList<IAwarenessPresenceListener> awarenessListeners = new ArrayList<IAwarenessPresenceListener>();
    private Roster roster;

    public AwarenessServiceXMPPImpl() {        
    }
 
    public boolean isConnected() {
       if( xmppConnection != null)
           return xmppConnection.isConnected();
       
       return false;
    }
    
    public void createAwarenessService(String username, String password) {
        
        Properties props = new Properties();
        try {
            props.load(AwarenessServiceXMPPImpl.class.getResourceAsStream("server.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String address = props.getProperty("awareness.service.address");
        String port = props.getProperty("awareness.service.port");
        String name = props.getProperty("awareness.service.name");
        
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
    

    public void closeAwarenessService() {
        xmppConnection.disconnect();
    }
    
    
    //TODO: return array should probably contain instances of scy users or somesuch
    public ArrayList<IAwarenessUser> getBuddies(String username) {
        roster = this.xmppConnection.getRoster();
        Collection<RosterEntry> rosterEntries = roster.getEntries();
        ArrayList<IAwarenessUser> buddies = new ArrayList<IAwarenessUser>();
        for (RosterEntry buddy:rosterEntries) {
            IAwarenessUser au = new AwarenessUser();
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
            for (IAwarenessPresenceListener al : awarenessListeners) {
                if (al != null){
                    RosterEntry entry = roster.getEntry(chat.getParticipant());
                    AwarenessEvent awarenessEvent = new AwarenessEvent(this, entry.getName(), message.getBody());
//                    al.handleAwarenessEvent(awarenessEvent);
                }
            }
        }
    }
    
    public void addAwarenessListener(IAwarenessPresenceListener awarenessListener){
        awarenessListeners.add(awarenessListener);
   }

    @Override
    public void addAwarenessListListener(IAwarenessListListener awarenessListListener) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addAwarenessPresenceListener(IAwarenessPresenceListener awarenessPresenceListener) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addBuddy(String buddy) throws AwarenessServiceException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void joinSession(String session) throws AwarenessServiceException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void leaveSession(String session) throws AwarenessServiceException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void removeBuddy(String buddy) throws AwarenessServiceException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public ArrayList<String> getBuddies() throws AwarenessServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setPresence(String username, String presence) throws AwarenessServiceException {
        // TODO Auto-generated method stub
        
    }
    
}
