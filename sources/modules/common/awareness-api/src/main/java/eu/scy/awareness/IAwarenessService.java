package eu.scy.awareness;

import java.util.List;

import org.jivesoftware.smack.XMPPConnection;

import eu.scy.awareness.event.IAwarenessRosterListener;
import eu.scy.awareness.event.IAwarenessMessageListener;
import eu.scy.awareness.event.IAwarenessPresenceListener;

/**
 * Awareness Service Interface
 * 
 * @author anthonyp
 */
public interface IAwarenessService {
    //to be replace with scy user
    
    /**
     * Get all the buddies
     * 
     * @return all buddies
     * 
     */
    public List<IAwarenessUser> getBuddies() throws AwarenessServiceException;
    
    /**
     * 
     * 
     * @param recipient
     * @param message
     * @throws AwarenessServiceException
     */
    public void sendMessage(String recipient, String message) throws AwarenessServiceException;
    
    /**
     * sets the presence of user either online or offline
     * 
     * @param username
     * @param presence
     * @throws AwarenessServiceException
     */
    public void setPresence(String presence) throws AwarenessServiceException;
    
    /**
     * sets the status of user
     * 
     * @param status
     * @throws AwarenessServiceException
     */
    public void setStatus(String status) throws AwarenessServiceException;
    
    /**
     * adds a presence listener
     * 
     * @param awarenessPresenceListener
     */
    public void addAwarenessPresenceListener(IAwarenessPresenceListener awarenessPresenceListener);
    
    public void removeAwarenessPresenceListener(IAwarenessPresenceListener awarenessPresenceListener);
    
    /**
     * adds a buddy list listener
     * 
     * @param awarenessListListener
     */
    public void addAwarenessRosterListener(IAwarenessRosterListener awarenessListListener);
    
    
    /**
     * adds a message listener
     * 
     * @param awarenessListListener
     */
    public void addAwarenessMessageListener(IAwarenessMessageListener awarenessMessageListener);
    
    /**
     * Add buddy
     * 
     * @param buddy
     * @throws AwarenessServiceException
     */
    public void addBuddy(String buddy) throws AwarenessServiceException;
    
    /**
     * remove buddy
     * 
     * @param buddy
     * @throws AwarenessServiceException
     */
    public void removeBuddy(String buddy) throws AwarenessServiceException;

    /**
     * initialize the service with connection.
     * 
     * @param connection
     */
    public void init(XMPPConnection connection);
    
    /**
     * is the service connected
     * 
     * @return
     */
    public boolean isConnected();
    
    /**
     * disconnect the xmpp session
     * 
     */
    public void disconnect();
   
}
