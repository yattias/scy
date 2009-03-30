package eu.scy.collaborationservice.session;

import java.sql.Timestamp;
import java.util.ArrayList;

import eu.scy.communications.message.IScyMessage;


/**
 * Collaboration Session Interface
 * 
 * @author thomasd
 */
public interface ICollaborationSession {

    
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
     * @return ArrayList containing ScyMessages representing the users on the session
     * 
     */
    public ArrayList<IScyMessage> getUsers();
    
    /**
     * set user
     * 
     * @param userName
     */
    public void setUser(String userName);

    
    /**
     * set tool
     * 
     * @param toolName
     */
    public void setTool(String toolName);
    
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
    
    /**
     * Returns a scyMessge version of the collaboration service
     * 
     */
    public IScyMessage convertToScyMessage();
}
