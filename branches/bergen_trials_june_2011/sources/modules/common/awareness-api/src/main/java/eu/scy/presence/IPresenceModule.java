package eu.scy.presence;

import java.util.List;


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
     * Get all the buddies
     * 
     * @return all buddies for given user
     * 
     */
    public List<String> getBuddies(String userName) throws PresenceModuleException;
    
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
     * adds a buddy list listener
     * 
     * @param rosterListener
     */
    public void addRosterListener(IPresenceRosterListener rosterListener);
    

    /**
     * adds a packet listener
     * 
     * @param packetListener
     */
    public void addPacketListener(IPresencePacketListener packetListener);

    
    
    /**
     * Add buddy
     * 
     * @param buddy
     * @throws PresenceModuleException
     */
    public void addBuddy(String buddy, String group) throws PresenceModuleException;
    
    /**
     * remove buddy
     * 
     * @param buddy
     * @throws PresenceModuleException
     */
    public void removeBuddy(String buddy) throws PresenceModuleException;

    /**
     * joins a group
     * 
     * @param groupName
     * @throws PresenceModuleException
     */
    public void joinGroup(String groupName, String userName) throws PresenceModuleException;
    
    /**
     * leaves a group
     * 
     * @param groupName
     * @throws PresenceModuleException
     */
    public void leaveGroup(String groupName, String userName) throws PresenceModuleException;
    
    /**
     * get all the groups with this user
     * 
     * @param userName
     * @return
     * @throws PresenceModuleException
     */
    public List<String> getGroups(String userName) throws PresenceModuleException;
    
    /**
     * Get all the groups
     * 
     * @return
     * @throws PresenceModuleException
     */
    public List<String> getGroups() throws PresenceModuleException;
}
