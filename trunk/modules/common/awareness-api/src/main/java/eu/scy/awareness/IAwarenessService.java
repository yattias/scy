package eu.scy.awareness;

import java.util.ArrayList;
import java.util.List;

import eu.scy.awareness.event.IAwarenessListListener;
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
    public List<String> getBuddies() throws AwarenessServiceException;
    
    /**
     * 
     * 
     * @param recipient
     * @param message
     * @throws AwarenessServiceException
     */
    public void sendMessage(String recipient, String message) throws AwarenessServiceException;
    
    /**
     * sets the presence of the user. TODO: use an enum
     * 
     * @param username
     * @param presence
     * @throws AwarenessServiceException
     */
    public void setPresence(String username, String presence) throws AwarenessServiceException;
    
    /**
     * sets the status of user
     * 
     * @param username
     * @param status
     * @throws AwarenessServiceException
     */
    public void setStatus(String username, String status) throws AwarenessServiceException;
    
    /**
     * adds a presence listener
     * 
     * @param awarenessPresenceListener
     */
    public void addAwarenessPresenceListener(IAwarenessPresenceListener awarenessPresenceListener);
    
    /**
     * adds a buddy list listener
     * 
     * @param awarenessListListener
     */
    public void addAwarenessListListener(IAwarenessListListener awarenessListListener);
    
    
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
     * join a collaboration session
     * 
     * @param session
     * @throws AwarenessServiceException
     */
    public void joinSession(String session) throws AwarenessServiceException;
    
    /**
     * leaves a collaboration sesson
     * 
     * @param session
     * @throws AwarenessServiceException
     */
    public void leaveSession(String session) throws AwarenessServiceException;
}
