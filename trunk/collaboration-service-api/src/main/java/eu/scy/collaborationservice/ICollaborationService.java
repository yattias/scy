package eu.scy.collaborationservice;

import java.util.ArrayList;

import eu.scy.collaborationservice.event.ICollaborationServiceListener;
import eu.scy.communications.message.IScyMessage;
import eu.scy.communications.message.impl.ScyMessage;


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
    public ArrayList<IScyMessage> doQuery(ScyMessage queryMessage);
   
}
