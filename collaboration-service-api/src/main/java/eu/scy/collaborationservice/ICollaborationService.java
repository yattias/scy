package eu.scy.collaborationservice;

import eu.scy.collaborationservice.event.ICollaborationServiceListener;
import eu.scy.communications.message.ScyMessage;
import eu.scy.core.model.impl.ScyBaseObject;


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
  public void create(ScyMessage scyMessage) throws CollaborationServiceException;
  
  /**
   * Return a specific message based on id
   * 
   * @param id
   * @return
   * @throws CollaborationServiceException
   */
  public ScyMessage read(String id) throws CollaborationServiceException;
  
  /**
   * Updates an object with the following id
   * 
   * @param scyMessage - update message
   * @param id - id of the object to update
   * @return 
   * @throws CollaborationServiceException
   */
  public void update(ScyMessage scyMessage, String id) throws CollaborationServiceException;
  
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
    public void sendCallBack(ScyMessage scyMessage) throws CollaborationServiceException;
   
}
