package eu.scy.awareness.presence;

import java.util.List;

import eu.scy.awareness.event.IAwarenessMessageListener;


/**
 * Presence Service Interface
 * 
 * @author anthonyp
 */
public interface IPresenceModule {
    //to be replace with scy user
    
    /**
     * Get all the buddies
     * 
     * @return all buddies
     * 
     */
    public List<String> getBuddies() throws PresenceModuleException;
   
    
    /**
     * sets the presence of the user. TODO: use an enum
     * 
     * @param username
     * @param presence
     * @throws PresenceModuleException
     */
    public void setPresence(String username, String presence) throws PresenceModuleException;
    
    /**
     * gets the presence of the user. TODO: use an enum
     * 
     * @param username
     * @throws PresenceModuleException
     */
    public void getPresence(String username) throws PresenceModuleException;
    
    /**
     * sets the status of user
     * 
     * @param username
     * @param status
     * @throws PresenceModuleException
     */
    public void setStatus(String username, String status) throws PresenceModuleException;
    
    /**
     * gets the status of user
     * 
     * @param status
     * @throws PresenceModuleException
     */
    public void getStatus(String username) throws PresenceModuleException;
    
    
    /**
     * adds a presence status listener
     * 
     * @param awarenessPresenceListener
     */
    public void addStatusListener(IPresenceListener awarenessPresenceListener);
    
    /**
     * adds a buddy list listener
     * 
     * @param awarenessListListener
     */
    public void addListListener(IPresenceListListener awarenessListListener);
    
   
    /**
     * Add buddy
     * 
     * @param buddy
     * @throws PresenceModuleException
     */
    public void addBuddy(String buddy) throws PresenceModuleException;
    
    /**
     * remove buddy
     * 
     * @param buddy
     * @throws PresenceModuleException
     */
    public void removeBuddy(String buddy) throws PresenceModuleException;

    public void joinGroup(String groupName) throws PresenceModuleException;
    
    public void leaveGroup(String groupName) throws PresenceModuleException;
    
    public List<String> getGroups(String userName) throws PresenceModuleException;
    
    public List<String> getGroups() throws PresenceModuleException;
}
