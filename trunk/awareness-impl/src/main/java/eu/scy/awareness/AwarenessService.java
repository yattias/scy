package eu.scy.awareness;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import eu.scy.awareness.api.IAwarenessService;


public class AwarenessService implements IAwarenessService, org.jivesoftware.smack.MessageListener {

    private final static Logger logger = Logger.getLogger(AwarenessService.class.getName());
    private ConnectionConfiguration config;
    private XMPPConnection connection;
    
    private AwarenessService() {        
    }
    
    
    public static AwarenessService createAwarenessService(String username, String password) {
        AwarenessService as = new AwarenessService();
        as.config = new ConnectionConfiguration("wiki.intermedia.uio.no", 5222, "AwarenessService");
        as.connection = new XMPPConnection(as.config);
        try {
            as.connection.connect();
        } catch (XMPPException e) {
            logger.error("Error during connect");
            e.printStackTrace();
        }
        try {
            as.connection.login(username, password);
        } catch (XMPPException e) {
            logger.error("Error during login");
            e.printStackTrace();
        }
        return as;
    }
    

    public void closeAwarenessService() {
        connection.disconnect();
    }
    
    
    //TODO: return array should probably contain instances of scy users or somesuch
    public ArrayList<String> getBuddies(String username) {
        Roster roster = this.connection.getRoster();
        Collection<RosterEntry> rosterEntries = roster.getEntries();
        ArrayList<String> buddies = new ArrayList<String>();
        for (RosterEntry buddy:rosterEntries) {
            buddies.add(buddy.getUser());
        }
        return buddies;
    }
    
    
    public void sendMessage(String recipient, String message) {
        Chat chat = connection.getChatManager().createChat(recipient, this);
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
        if(message.getType() == Message.Type.chat) {
            logger.debug(chat.getParticipant() + " says: " + message.getBody());          
        }
    }
    
}
