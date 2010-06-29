package eu.scy.communications.datasync.session;

import java.sql.Timestamp;
import java.util.List;

import eu.scy.communications.message.ISyncMessage;


/**
 * Collaboration Session Interface
 * 
 * @author thomasd
 */
public interface IDataSyncSession {

    
    /**
     * Expires the session
     * 
     */
    public void expire();
    
    /**
     * Renews the session, putting off expiration time
     * 
     */
    public void renew();    
    
    /**
     * Get time of session expiration
     * 
     * @return TimeStamp when session expires
     */
    public Timestamp getExpirationDate();
    

    /**
     * Get all users on the session
     * 
     * @return ArrayList containing SyncMessages representing the users on the session
     * 
     */
    public List<String> getUsers();
    
    /**
     * set user
     * 
     * @param userName
     */
    public void setUsers(List<String> users);

    /**
     * adds a user
     * 
     * @param userName
     */
    public void addUser(String userName);
    
    /**
     * removes a user from the session
     * 
     * @param userName
     */
    public void removeUser(String userName);
    
    /**
     * set tool
     * 
     * @param toolName
     */
    public void setToolId(String toolId);
    
    /**
     * gets the toolId
     * 
     * @return
     */
    public String getToolId();
    
    /**
     * get id
     * 
     * @return session id
     */
    public String getId();
    
    /**
     * set id
     * 
     * @param id
     */
    public void setId(String id);

    /**
     * Returns the id of the sessions as found in the persistence layer
     * 
     * @return string
     */
    public String getPersistenceId();
    
    /**
     * Stores the id of the sessions as found in the persistence layer
     * 
     * @param persistenceId
     */
    public void setPersistenceId(String persistenceId);
    
  
   
}
