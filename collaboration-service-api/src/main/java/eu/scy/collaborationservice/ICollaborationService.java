package eu.scy.collaborationservice;

import java.util.ArrayList;

import eu.scy.collaborationservice.event.ICollaborationServiceListener;
import eu.scy.collaborationservice.session.ICollaborationSession;
import eu.scy.communications.message.IScyMessage;


/**
 * Collaboration Service Interface
 * 
 * @author anthonyp
 */
public interface ICollaborationService {

 
  /**
   * Do a create operation for a object to be syncronized
   * 
   * @param scyMessage
   * @return
   * @throws CollaborationServiceException
   */
  public void create(IScyMessage scyMessage) throws CollaborationServiceException;
  
  /**
   * Return a specific message based on id
   * 
   * @param id
   * @return
   * @throws CollaborationServiceException
   */
  public IScyMessage read(String id) throws CollaborationServiceException;
  
  /**
   * Updates an object with the following id
   * 
   * @param scyMessage - update message
   * @param id - id of the object to update
   * @return 
   * @throws CollaborationServiceException
   */
  public void update(IScyMessage scyMessage, String id) throws CollaborationServiceException;
  
  /**
   * Deletes the specified message
   * 
   * @param id of the object to delete
   * @throws CollaborationServiceException
   */
  public void delete(String id) throws CollaborationServiceException;
  
  
  /**
   * Gives the ability for a client to add a listener. When the collaboration service
   * updates it notify all clients listening.
   * 
   * @param collaborationListener
   * @throws CollaborationServiceException
   */
  public void addCollaborationListener(ICollaborationServiceListener collaborationListener);
  
   /**
    * Connects to the collaboration service of the specific implementation
    * 
    * @param username
    * @param password
    * @throws CollaborationServiceException
    */
   public void connect(String username, String password) throws CollaborationServiceException;
   
   /**
    * Connects to the collaboration service of the specific implementation
    * 
    * @param username
    * @param password
    * @param group
    * @throws CollaborationServiceException
    */
   public void connect(String username, String password, String group) throws CollaborationServiceException;
   
   /**
    * test method
    */ 
    public void sendCallBack(IScyMessage scyMessage) throws CollaborationServiceException;

    /**
     * 
     * @param message defining the query
     * @return list of messages containing the query result
     */
    public ArrayList<IScyMessage> doQuery(IScyMessage queryMessage);

    /**
     * Gives the complete set of ScyMessges which represent changes for this specific client
     * 
     * @param toolName - name of tool
     * @param session -  id of session shared by client instances
     * @return
     */
    public ArrayList<IScyMessage> synchronizeClientState(String toolName, String session);
    
    
    /**
     * Creates and returns an instance of ICollaborationSession
     * 
     * @param toolName - name of tool
     * @param userName - user name
     * @return ICollaborationSession
     */
    public ICollaborationSession createSession(String toolName, String userName);
    
}
