package eu.scy.collaborationservice;

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

}
